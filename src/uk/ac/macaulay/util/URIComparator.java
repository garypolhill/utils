/* uk.ac.macaulay.util: URIComparator.java
 *
 * Copyright (C) 2009  Macaulay Institute
 *
 * This file is part of utils.
 *
 * utils is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with utils. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Contact information:
 *   Gary Polhill
 *   Macaulay Institute, Craigiebuckler, Aberdeen. AB15 8QH. UK.
 *   g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

import java.net.URI;

/**
 * URIComparator
 *
 * 
 *
 * @author Gary Polhill
 */
public class URIComparator implements PartialOrderComparator<URI>, PartialOrderComparable<URI>,
    PartialOrderRelativeComparable<URI>, PartialOrderRelativeComparator<URI> {
  private final URI uri;
  private final boolean ignoreFragment;
  
  public URIComparator() {
    uri = null;
    ignoreFragment = false;
  }
  
  public URIComparator(boolean ignoreFragment) {
    uri = null;
    this.ignoreFragment = ignoreFragment;
  }
  
  public URIComparator(URI uri) {
    this.uri = uri;
    ignoreFragment = false;
  }
  
  public URIComparator(URI uri, boolean ignoreFragment) {
    this.uri = uri;
    this.ignoreFragment = ignoreFragment;
  }
  
  private String[] convert(URI uri) {
    URI nuri = uri.normalize();
    if(ignoreFragment) return new String[] {
      nuri.getScheme(),
      nuri.getUserInfo(),
      nuri.getHost(),
      Integer.toString(nuri.getPort()),
      nuri.getPath(),
      nuri.getQuery()
    };
    else return new String[] {
      nuri.getScheme(),
      nuri.getUserInfo(),
      nuri.getHost(),
      Integer.toString(nuri.getPort()),
      nuri.getPath(),
      nuri.getQuery(),
      nuri.getFragment()
    };
  }
  
  public PartialOrderComparison partialOrderCompare(URI obj1, URI obj2) {
    String[] uri1 = convert(obj1);
    String[] uri2 = convert(obj2);
    
    return ArrayComparator.partialOrderComparison(uri1, uri2);
  }

  public PartialOrderComparison partialOrderCompareTo(URI obj) {
    if(uri == null) throw new NullPointerException();
    return partialOrderCompare(uri, obj);
  }

  public PartialOrderComparison relativeCompareTo(URI objB, URI objC) {
    if(uri == null) throw new NullPointerException();
    return relativeCompare(uri, objB, objC);
  }

  public PartialOrderComparison relativeCompare(URI objA, URI objB, URI objC) {
    String[] uriA = convert(objA);
    String[] uriB = convert(objB);
    String[] uriC = convert(objC);
    return ArrayComparator.relativeComparison(uriA, uriB, uriC);
  }
  
  public int relativeDiffer(URI objA, URI objB, URI objC) {
    String[] uriA = convert(objA);
    String[] uriB = convert(objB);
    String[] uriC = convert(objC);
   
    try {
      Integer diffsAC = ArrayComparator.countDifferences(uriA, uriC);
      Integer diffsBC = ArrayComparator.countDifferences(uriB, uriC);
      return diffsAC.compareTo(diffsBC);
    }
    catch(IncomparableException e) {
      throw new Panic();
    }
    
  }

  public static PartialOrderComparison partialOrderComparison(URI obj1, URI obj2) {
    URIComparator comparator = new URIComparator();
    return comparator.partialOrderCompare(obj1, obj2);
  }
  
  public static PartialOrderComparison partialOrderComparison(URI obj1, URI obj2, boolean ignoreFragment) {
    URIComparator comparator = new URIComparator(ignoreFragment);
    return comparator.partialOrderCompare(obj1, obj2);
  }
  
  public static PartialOrderComparison relativeComparison(URI objA, URI objB, URI objC) {
    URIComparator comparator = new URIComparator();
    return comparator.relativeCompare(objA, objB, objC);
  }
  
  public static PartialOrderComparison relativeComparison(URI objA, URI objB, URI objC, boolean ignoreFragment) {
    URIComparator comparator = new URIComparator(ignoreFragment);
    return comparator.relativeCompare(objA, objB, objC);
  }
  
  public static int relativeDifference(URI objA, URI objB, URI objC) {
    URIComparator comparator = new URIComparator();
    return comparator.relativeDiffer(objA, objB, objC);
  }
  
  public static int relativeDifference(URI objA, URI objB, URI objC, boolean ignoreFragment) {
    URIComparator comparator = new URIComparator(ignoreFragment);
    return comparator.relativeDiffer(objA, objB, objC);
  }
}
