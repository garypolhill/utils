/*
 * uk.ac.macaulay.util: PartialOrderRelativeComparator.java
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
 * PartialOrderRelativeComparator
 * 
 * Provide a comparator for a relative partially ordered comparison.
 * 
 * @see PartialOrderRelativeComparable
 * @author Gary Polhill
 */
public interface PartialOrderRelativeComparator<T> {
  /**
   * <!-- relativeCompare -->
   * 
   * Return the relative comparability of <code>objA</code> and
   * <code>objB</code> to <code>objC</code>.
   * 
   * @param objA
   * @param objB
   * @param objC
   * @return <code>LESS_THAN</code> if <code>objA</code> is less similar to
   *         <code>objC</code> than <code>objB</code>, <code>MORE_THAN</code> if
   *         it is more similar to <code>objC</code> than <code>objB</code>,
   *         <code>EQUAL_TO</code> if <code>objA.equals(objB)</code>, and
   *         <code>INCOMPARABLE</code> if <code>objA</code> and
   *         <code>objB</code> have incomparable differences from
   *         <code>objC</code>.
   */
  public PartialOrderComparison relativeCompare(T objA, T objB, T objC);
}
