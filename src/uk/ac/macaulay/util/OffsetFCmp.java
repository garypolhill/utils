/*
 * uk.ac.macaulay.util: OffsetFCmp.java Copyright (C) 2009 Macaulay Institute
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
 * OffsetFCmp
 * 
 * A (not recommended) method of floating point comparison in which both
 * operands have a large number added to them before the comparison is done.
 * 
 * @author Gary Polhill
 */
public class OffsetFCmp extends AbstractConvertedFCmp {
  /**
   * Addend for double precision floating point comparisons
   */
  private final double daddend;

  /**
   * Addend for single precision floating point comparisons
   */
  private final float faddend;

  /**
   * Default power of two to use for double precision floating point comparisons
   */
  public static final int DEFAULT_DOUBLE_POWER_OF_2 = 13;

  /**
   * Default power of two to use for single precision floating point comparisons
   */
  public static final int DEFAULT_FLOAT_POWER_OF_2 = 6;

  /**
   * Initialise the comparison using default values for the large numbers to add
   */
  public OffsetFCmp() {
    daddend = Math.scalb(1.0, DEFAULT_DOUBLE_POWER_OF_2);
    faddend = Math.scalb(1.0F, DEFAULT_FLOAT_POWER_OF_2);
  }

  /**
   * Use the same power of two addend for the operands for single and double
   * precision
   * 
   * @param power_of_2
   */
  public OffsetFCmp(int power_of_2) {
    this(power_of_2, power_of_2);
  }

  /**
   * Allow the addends to be configured for single and double precision
   * separately
   * 
   * @param double_power_of_2
   * @param float_power_of_2
   */
  public OffsetFCmp(int double_power_of_2, int float_power_of_2) {
    if(double_power_of_2 < 0) {
      throw new FloatingPointComparisonException("Invalid power of two for double precision addend: "
        + double_power_of_2);
    }
    if(float_power_of_2 < 0) {
      throw new FloatingPointComparisonException("Invalid power of two for single precision addend: "
        + float_power_of_2);
    }
    daddend = Math.scalb(1.0, double_power_of_2);
    faddend = Math.scalb(1.0F, float_power_of_2);

    if(Double.isInfinite(daddend)) {
      throw new FloatingPointComparisonException("Power of two is too large for double precision addend: "
        + double_power_of_2);
    }
    if(Float.isInfinite(faddend)) {
      throw new FloatingPointComparisonException("Power of two is too large for single precision addend: "
        + float_power_of_2);
    }
    if(Double.isNaN(daddend)) {
      throw new FloatingPointComparisonException("Double precision addend is not a number");
    }
    if(Float.isNaN(faddend)) {
      throw new FloatingPointComparisonException("Single precision addend is not a number");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractConvertedFCmp#convert(double)
   */
  @Override
  protected double convert(double value) {
    return value + daddend;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractConvertedFCmp#convert(float)
   */
  @Override
  protected float convert(float value) {
    return value + faddend;
  }

}
