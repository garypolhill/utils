/* uk.ac.macaulay.util: FloatingPointComparisonException.java
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

/**
 * FloatingPointComparisonException
 *
 * Exceptions caused by floating point comparisons
 *
 * @author Gary Polhill
 */
public class FloatingPointComparisonException extends RuntimeException {
  /**
   * Version number for serialisation
   */
  private static final long serialVersionUID = 6693322959938699862L;

  public FloatingPointComparisonException(String msg, double a, double b) {
    super(msg + "(comparing " + a + " and " + b + ")");
  }

  public FloatingPointComparisonException(String msg, float a, float b) {
    super(msg + "(comparing " + a + " and " + b + ")");
  }
  
  public FloatingPointComparisonException(String msg) {
    super(msg);
  }

}
