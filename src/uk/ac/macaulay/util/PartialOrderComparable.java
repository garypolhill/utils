/*
 * uk.ac.macaulay.util: PartialOrderComparable.java
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
 * PartialOrderComparable
 * 
 * Interface to follow for classes having partially ordered comparable
 * relations.
 * 
 * @author Gary Polhill
 */
public interface PartialOrderComparable<T> {
  /**
   * <!-- partialOrderCompareTo -->
   * 
   * Compare this object to the argument, returning
   * PartialOrderComparison.LESS_THAN if this object is less than the argument,
   * PartialOrderComparison.EQUAL_TO if they are equal,
   * PartialOrderComparison.MORE_THAN if this object is more than the argument,
   * and PartialOrderComparison.INCOMPARABLE if they are not ordered.
   * 
   * @param obj The other object to compare to this one
   * @return a PartialOrderComparison result
   */
  public PartialOrderComparison partialOrderCompareTo(T obj);
}
