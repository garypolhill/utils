/* uk.ac.macaulay.util: StringTools.java
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

/**
 * <!-- StringTools -->
 * 
 * Some tools for working with strings.
 *
 * @author Gary Polhill
 */
public final class StringTools {
  private StringTools() {
    // disable construction
  }
  
  /**
   * <!-- join -->
   * 
   * Create a string by joining an array of objects with a separator between
   * each
   * 
   * @param separator the separator between each object
   * @param args the objects to join
   * @return the joined string
   */
  public static String join(String separator, Object... args) {
    StringBuffer buf = new StringBuffer();
    if(args.length == 0) return buf.toString();
    buf.append(args[0]);
    for(int i = 1; i < args.length; i++) {
      buf.append(separator);
      buf.append(args[i].toString());
    }
    return buf.toString();
  }

}
