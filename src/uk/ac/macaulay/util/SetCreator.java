/* uk.ac.macaulay.util: SetCreator.java
 *
 * Copyright (C) 2010  Macaulay Institute
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

import java.util.HashSet;
import java.util.Set;

/**
 * <!-- SetCreator -->
 *
 * @author Gary Polhill
 */
public class SetCreator {
  
  public static <T> Set<T> createSet(T... objs) {
    Set<T> set = new HashSet<T>();
    for(T obj: objs) {
      set.add(obj);
    }
    return set;
  }

}
