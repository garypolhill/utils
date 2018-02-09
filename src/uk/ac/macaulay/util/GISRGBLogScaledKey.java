/* uk.ac.macaulay.util: GISRGBLogScaledKey.java
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

import java.awt.Color;

/**
 * <!-- GISRGBLogScaledKey -->
 * 
 * A log-scale key through the colour space
 *
 * @author Gary Polhill
 */
public class GISRGBLogScaledKey<T extends Number> extends GISRGBScaledKey<T> {

  /**
   * Constructor using default tolerance
   * 
   * @param min The minimum of the scale (not in log form--this will be converted)
   * @param max The maximum of the scale (not in log form)
   * @param minC The colour to use at the minimum of the scale
   * @param maxC The colour to use at the maximum of the scale
   */
  public GISRGBLogScaledKey(T min, T max, Color minC, Color maxC) {
    super(min, max, minC, maxC);
  }

  /**
   * Constructor with integer RGB for colours
   * 
   * @param min The minimum of the scale (not in log form--this will be converted)
   * @param max The maximum of the scale (not in log form)
   * @param minR Red at scale minimum
   * @param minG Green at scale minimum
   * @param minB Blue at scale minimum
   * @param maxR Red at scale maxium
   * @param maxG Green at scale maximum
   * @param maxB Blue at scale maximum
   */
  public GISRGBLogScaledKey(T min, T max, int minR, int minG, int minB, int maxR, int maxG, int maxB) {
    super(min, max, minR, minG, minB, maxR, maxG, maxB);
  }

  /**
   * Constructor with float RGB for the colours
   * 
   * @param min The minimum of the scale (not in log form--this will be converted)
   * @param max The maximum of the scale (not in log form)
   * @param minR Red at scale minimum
   * @param minG Green at scale minimum
   * @param minB Blue at scale minimum
   * @param maxR Red at scale maxium
   * @param maxG Green at scale maximum
   * @param maxB Blue at scale maximum
   */
  public GISRGBLogScaledKey(T min, T max, float minR, float minG, float minB, float maxR, float maxG,
      float maxB) {
    super(min, max, minR, minG, minB, maxR, maxG, maxB);
  }

  /**
   * Constructor with specified tolerance
   * 
   * @param min The minimum of the scale (not in log form--this will be converted)
   * @param max The maximum of the scale (not in log form)
   * @param minC The colour to use at the minimum of the scale
   * @param maxC The colour to use at the maximum of the scale
   * @param tolerance Tolerance
   */
  public GISRGBLogScaledKey(T min, T max, Color minC, Color maxC, double tolerance) {
    super(min, max, minC, maxC, tolerance);
  }

  /**
   * Constructor with specified tolerance and integer RGB for the colours
   * 
   * @param min The minimum of the scale (not in log form--this will be converted)
   * @param max The maximum of the scale (not in log form)
   * @param minR Red at scale minimum
   * @param minG Green at scale minimum
   * @param minB Blue at scale minimum
   * @param maxR Red at scale maxium
   * @param maxG Green at scale maximum
   * @param maxB Blue at scale maximum
   * @param tolerance Tolerance
   */
  public GISRGBLogScaledKey(T min, T max, int minR, int minG, int minB, int maxR, int maxG, int maxB,
      double tolerance) {
    super(min, max, minR, minG, minB, maxR, maxG, maxB, tolerance);
  }

  /**
   * Constructor with specified tolerance and float RGB for the colours
   * 
   * @param min The minimum of the scale (not in log form--this will be converted)
   * @param max The maximum of the scale (not in log form)
   * @param minR Red at scale minimum
   * @param minG Green at scale minimum
   * @param minB Blue at scale minimum
   * @param maxR Red at scale maxium
   * @param maxG Green at scale maximum
   * @param maxB Blue at scale maximum
   * @param tolerance Tolerance
   */
  public GISRGBLogScaledKey(T min, T max, float minR, float minG, float minB, float maxR, float maxG,
      float maxB, double tolerance) {
    super(min, max, minR, minG, minB, maxR, maxG, maxB, tolerance);
  }

  /**
   * <!-- toLinear -->
   *
   * @see uk.ac.macaulay.util.GISRGBScaledKey#toLinear(double)
   * @param arg
   * @return Log (base e) of arg
   */
  @Override
  protected double toLinear(double arg) {
    return Math.log(arg);
  }
  
  /**
   * <!-- fromLinear -->
   *
   * @see uk.ac.macaulay.util.GISRGBScaledKey#fromLinear(double)
   * @param arg
   * @return Exp(arg)
   */
  @Override
  protected double fromLinear(double arg) {
    return Math.exp(arg);
  }
}
