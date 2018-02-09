/*
 * uk.ac.macaulay.util: ArrayComparator.java
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
 * ArrayComparator
 * 
 * Provide partial order comparisons for arrays of comparable objects.
 * 
 * @author Gary Polhill
 */
public class ArrayComparator<T extends Comparable<? super T>> implements PartialOrderComparable<T[]>,
    PartialOrderRelativeComparable<T[]>, PartialOrderComparator<T[]>, PartialOrderRelativeComparator<T[]> {
  /**
   * Array to embed in the comparator, for using the *Comparable interface methods.
   */
  private final T[] arr;

  /**
   * Default constructor. The *Comparable interface methods cannot be used without error.
   */
  public ArrayComparator() {
    arr = null;
  }

  /**
   * Constructor allowing the *Comparable interface methods to be used.
   * 
   * @param arr An array to use with this comparator.
   */
  public ArrayComparator(T[] arr) {
    this.arr = arr;
  }

  /**
   * <!-- partialOrderCompareTo -->
   *
   * @see uk.ac.macaulay.util.PartialOrderComparable#partialOrderCompareTo(java.lang.Object)
   */
  public PartialOrderComparison partialOrderCompareTo(T[] obj) {
    if(arr == null) throw new NullPointerException();
    return partialOrderCompare(arr, obj);
  }
  
  /**
   * <!-- partialOrderCompare -->
   * 
   * @see uk.ac.macaulay.util.PartialOrderComparator#partialOrderCompare(java.lang.Object,
   *      java.lang.Object)
   */
  public PartialOrderComparison partialOrderCompare(T[] obj1, T[] obj2) {
    if(obj1.length != obj2.length) return PartialOrderComparison.INCOMPARABLE;
    PartialOrderComparison result = null;
    for(int i = 0; i < obj1.length; i++) {
      int elementResult;
      if(obj1[i] == null || obj2[i] == null) {
        elementResult = obj1[i] != null ? 1 : (obj2[i] != null ? -1 : 0);
      }
      else {
        elementResult = obj1[i].compareTo(obj2[i]);
      }
      if(result == null) {
        result = PartialOrderComparison.get(elementResult);
      }
      else {
        result.generalise(PartialOrderComparison.get(elementResult));
      }
      if(result.incomparable()) return result;
    }
    return result;
  }
  
  /**
   * <!-- relativeCompareTo -->
   *
   * @see uk.ac.macaulay.util.PartialOrderRelativeComparable#relativeCompareTo(java.lang.Object, java.lang.Object)
   */
  public PartialOrderComparison relativeCompareTo(T[] objB, T[] objC) {
    if(arr == null) throw new NullPointerException();
    return relativeCompare(arr, objB, objC);
  }
  
  /**
   * <!-- relativeCompare -->
   * 
   * @see uk.ac.macaulay.util.PartialOrderRelativeComparator#relativeCompare(java.lang.Object,
   *      java.lang.Object, java.lang.Object)
   */
  public PartialOrderComparison relativeCompare(T[] objA, T[] objB, T[] objC) {
    if(objA.length != objB.length || objA.length != objC.length) return PartialOrderComparison.INCOMPARABLE;
    PartialOrderComparison result = null;
    for(int i = 0; i < objA.length; i++) {
      PartialOrderComparison elementResult;
      if(objA[i] == null || objB[i] == null || objC[i] == null) {
        if(objA[i] == null && objB[i] == null) elementResult = PartialOrderComparison.EQUAL_TO;
        else if(objA[i] == null && objC[i] == null) elementResult = PartialOrderComparison.MORE_THAN;
        else if(objB[i] == null && objC[i] == null) elementResult = PartialOrderComparison.LESS_THAN;
      }
      if(objA[i] != null && objA[i].equals(objB[i])) elementResult = PartialOrderComparison.EQUAL_TO;
      else if(objA[i] != null && objA[i].equals(objC[i])) elementResult = PartialOrderComparison.MORE_THAN;
      else if(objB[i] != null && objB[i].equals(objC[i])) elementResult = PartialOrderComparison.LESS_THAN;
      else
        elementResult = PartialOrderComparison.INCOMPARABLE;
      result = result == null ? elementResult : result.generalise(elementResult);
    }
    return null;
  }

  /**
   * <!-- partialOrderComparison -->
   * 
   * Provide a convenience method for static comparison
   * 
   * @param obj1
   * @param obj2
   * @return partialOrderCompare of obj1 and obj2
   */
  public static <U extends Comparable<? super U>> PartialOrderComparison partialOrderComparison(U[] obj1, U[] obj2) {
    ArrayComparator<U> comparator = new ArrayComparator<U>();
    return comparator.partialOrderCompare(obj1, obj2);
  }

  /**
   * <!-- relativeComparison -->
   * 
   * Provide a convenience method for static comparison
   * 
   * @param objA
   * @param objB
   * @param objC
   * @return relativeCompare of objA and objB with objC
   */
  public static <U extends Comparable<? super U>> PartialOrderComparison relativeComparison(U[] objA, U[] objB, U[] objC) {
    ArrayComparator<U> comparator = new ArrayComparator<U>();
    return comparator.relativeCompare(objA, objB, objC);
  }
  
  /**
   * <!-- countDifferences -->
   *
   * Count the differences between two arrays of the same length.
   *
   * @param arr1
   * @param arr2
   * @return The number of elements in arr1 for which the corresponding element in arr2 had a different value.
   * @throws IncomparableException
   */
  public static <U> int countDifferences(U[] arr1, U[] arr2) throws IncomparableException {
    if(arr1.length != arr2.length) throw new IncomparableException();
    int diffs = 0;
    for(int i = 0; i < arr1.length; i++) {
      if(arr1[i] == null && arr2[i] != null) diffs++;
      else if(arr1[i] != null && !arr1[i].equals(arr2[i])) diffs++;
    }
    return diffs;
  }
}
