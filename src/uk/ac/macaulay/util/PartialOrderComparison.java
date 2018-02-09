/*
 * uk.ac.macaulay.util: PartialOrderComparison.java
 * 
 * Copyright (C) 2009 Macaulay Institute
 * 
 * This file is part of utils.
 * 
 * utils is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with utils. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: Gary Polhill Macaulay Institute, Craigiebuckler,
 * Aberdeen. AB15 8QH. UK. g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

/**
 * PartialOrderComparison
 * 
 * A result from a comparison of partially ordered objects. Methods are provided
 * enabling an idiom such as the following:
 * 
 * <p>
 * <code>PartialOrderComparable&lt;Object&gt; obj1;<br>
 * PartialOrderComparable&lt;Object&gt; obj2;<br>
 * <br>
 * if(obj1.partialOrderCompareTo(obj2).less()) {<br>
 * &nbsp;&nbsp;// Do something if obj1 < obj2<br>
 * }</code>
 * </p>
 * 
 * <p>
 * Though of course, one could just as well do
 * <code>obj1.partialOrderCompareTo(obj2) == PartialOrderComparison.LESS_THAN</code>
 * in the boolean expression. However, this is more verbose.
 * </p>
 * 
 * @author Gary Polhill
 */
public enum PartialOrderComparison {
  LESS_THAN, EQUAL_TO, MORE_THAN, INCOMPARABLE;

  /**
   * <!-- getComparator -->
   * 
   * Provide a value suitable for returning from compare() or compareTo()
   * methods (Comparator<T> and Comparable<T> interfaces), throwing an exception
   * if the result was INCOMPARABLE.
   * 
   * @return An argument suitable for use in compare() or compareTo() methods
   * @throws IncomparableException
   */
  public int getComparator() throws IncomparableException {
    switch(this) {
    case LESS_THAN:
      return -1;
    case EQUAL_TO:
      return 0;
    case MORE_THAN:
      return 1;
    case INCOMPARABLE:
      throw new IncomparableException();
    default:
      throw new Panic();
    }
  }

  /**
   * <!-- get -->
   * 
   * Convert a return value from compareTo() or compare() into a
   * <code>PartialOrderComparison</code>.
   * 
   * @param comparator The result from compareTo() or compare().
   * @return <code>LESS_THAN</code> if <code>comparator</code> &lt; 0,
   *         <code>MORE_THAN</code> if <code>comparator</code> &gt; 0, and
   *         <code>EQUAL_TO</code> otherwise.
   */
  public static PartialOrderComparison get(int comparator) {
    if(comparator < 0) {
      return LESS_THAN;
    }
    else if(comparator > 0) {
      return MORE_THAN;
    }
    else {
      return EQUAL_TO;
    }
  }

  /**
   * <!-- generalise -->
   * 
   * Generalise a partial order comparison result. The generalisation of two
   * results is the same as the strict generalisation, except that the
   * generalisation of <code>EQUAL_TO</code> and <code>MORE_THAN</code> is
   * <code>MORE_THAN</code>, and the generalisation of <code>EQUAL_TO</code> and
   * <code>LESS_THAN</code> is <code>LESS_THAN</code>.
   * 
   * @param that The result to generalise this one with.
   * @return The generalisation.
   */
  public PartialOrderComparison generalise(PartialOrderComparison that) {
    if(this == that) return this;
    if(this == INCOMPARABLE || that == INCOMPARABLE) return INCOMPARABLE;
    try {
      // If we get here, then this != that (so we shouldn't be returning
      // EQUAL_TO), and neither are incomparable
      if(this.getComparator() <= 0 && that.getComparator() <= 0) return LESS_THAN;
      if(this.getComparator() >= 0 && that.getComparator() >= 0) return MORE_THAN;
    }
    catch(IncomparableException e) {
      throw new Panic();
    }
    return INCOMPARABLE;
  }

  /**
   * <!-- strictGeneralise -->
   * 
   * Generalise a partial order comparison result strictly. The strict
   * generalisation of two results is <code>INCOMPARABLE</code> unless they are
   * both the same.
   * 
   * @param that The partial order comparison result to generalise this one
   *          with.
   * @return The strict generalisation.
   */
  public PartialOrderComparison strictGeneralise(PartialOrderComparison that) {
    return this == that ? this : INCOMPARABLE;
  }

  public boolean less() {
    return this == LESS_THAN;
  }

  public boolean lessOrEqual() {
    return this == LESS_THAN || this == EQUAL_TO;
  }

  public boolean equal() {
    return this == EQUAL_TO;
  }

  public boolean moreOrEqual() {
    return this == MORE_THAN || this == EQUAL_TO;
  }

  public boolean more() {
    return this == MORE_THAN;
  }

  public boolean notEqual() {
    return this != EQUAL_TO;
  }

  public boolean lessOrMore() {
    return this == LESS_THAN || this == MORE_THAN;
  }

  public boolean comparable() {
    return this != INCOMPARABLE;
  }

  public boolean incomparable() {
    return this == INCOMPARABLE;
  }
}
