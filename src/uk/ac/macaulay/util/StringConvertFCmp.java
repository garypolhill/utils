/*
 * uk.ac.macaulay.util: StringConvertFcmp.java Copyright (C) 2009 Macaulay
 * Institute
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
 * StringConvertFcmp
 * 
 * Compare floating point numbers by writing them to a string to a specified
 * number of digits of accuracy first.
 * 
 * @author Gary Polhill
 */
public class StringConvertFCmp extends AbstractConvertedFCmp implements FloatingPointComparison {
  /**
   * Default number of digits of accuracy to use for single precision floating
   * point comparison. This is taken from FLT_DIG in float.h.
   */
  public static final int DEFAULT_N_FLOAT_DIGITS = 6;

  /**
   * Default number of digits of accuracy to use for double precision floating
   * point comparison. This is taken from DBL_DIG in float.h.
   */
  public static final int DEFAULT_N_DOUBLE_DIGITS = 15;

  /**
   * Number of digits of accuracy to use for single precision floating point
   * comparison minus 1
   */
  private final float fdigitsm1;

  /**
   * Number of digits of accuracy to use for double precision floating point
   * comparison minus 1
   */
  private final double ddigitsm1;

  /**
   * Constructor using default numbers of digits for single and double
   * comparison
   */
  public StringConvertFCmp() {
    fdigitsm1 = DEFAULT_N_FLOAT_DIGITS - 1;
    ddigitsm1 = DEFAULT_N_DOUBLE_DIGITS - 1;
  }

  /**
   * Constructor using the same number of digits for single and double
   * comparison
   * 
   * @param digits
   */
  public StringConvertFCmp(int digits) {
    this(digits, digits);
  }

  /**
   * Constructor using specified number of digits for single and double
   * comparison
   * 
   * @param double_digits
   * @param float_digits
   */
  public StringConvertFCmp(int double_digits, int float_digits) {
    if(double_digits <= 0) {
      throw new FloatingPointComparisonException(
          "Invalid number of digits of accuracy for double precision floating-point comparison: " + double_digits);
    }
    if(float_digits <= 0) {
      throw new FloatingPointComparisonException(
          "Invalid number of digits of accuracy for single precision floating-point comparison: " + float_digits);
    }
    fdigitsm1 = float_digits - 1;
    ddigitsm1 = double_digits - 1;
  }

  /**
   * <!-- convert -->
   * 
   * Convert a double precision number to the closest representable number to
   * the value written to the specified number of digits of precision.
   * 
   * @param value
   * @return the converted value
   */
  @Override
  protected double convert(double value) {
    String strval = String.format("%." + ddigitsm1 + "e", value);
    return Double.parseDouble(strval);
  }

  /**
   * <!-- convert -->
   * 
   * Convert a single precision number to the closest representable number to
   * the value written to the specified number of digits of precision.
   * 
   * @param value
   * @return the converted value
   */
  @Override
  protected float convert(float value) {
    String strval = String.format("%." + fdigitsm1 + "e", value);
    return Float.parseFloat(strval);
  }

}
