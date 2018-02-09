/*
 * uk.ac.macaulay.util: PartialOrderRelativeComparable.java
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
 * PartialOrderRelativeComparable
 * 
 * Provide an interface for a ternary operator measuring the relative
 * comparability of A and B to C. This is strictly interpreted as follows:
 * 
 * <ul>
 * <li>A is more similar to C than B iff B has the same differences from C that
 * A does and some other differences too.</li>
 * <li>A is less similar to C than B iff A has the same differences from C that
 * B does and some other differences too.</li>
 * <li>A is equally similar to C as B iff A is equal to B.</li>
 * <li>A is incomparable with B in similarity to C in all other cases.</li>
 * </ul>
 * 
 * @author Gary Polhill
 */
public interface PartialOrderRelativeComparable<T> {
  /**
   * <!-- relativeCompareTo -->
   * 
   * Return the relative comparability of <code>this</code> and
   * <code>objB</code> to <code>objC</code>.
   * 
   * @param objB
   * @param objC
   * @return <code>LESS_THAN</code> if <code>this</code> is less similar to
   *         <code>objC</code> than <code>objB</code>, <code>MORE_THAN</code> if
   *         <code>this</code> is more similar to <code>objC</code> than
   *         <code>objB</code>, <code>EQUAL_TO</code> if <code>this</code>
   *         equals <code>objB</code>, and <code>INCOMPARABLE</code> if
   *         <code>this</code> and <code>objB</code> have incomparable
   *         differences from <code>objC</code>.
   */
  public PartialOrderComparison relativeCompareTo(T objB, T objC);
}
