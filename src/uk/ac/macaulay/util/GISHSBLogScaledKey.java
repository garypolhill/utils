/*
 * uk.ac.macaulay.util: GISHSBLogScaledKey.java
 * 
 * Copyright (C) 2010 Macaulay Institute
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

import java.awt.Color;

/**
 * <!-- GISHSBLogScaledKey -->
 * 
 * Log-scale key in HSB space
 * 
 * @author Gary Polhill
 */
public class GISHSBLogScaledKey<T extends Number> extends GISHSBScaledKey<T> {

  /**
   * Constructor using default tolerance
   * 
   * @param min Scale minimum (not log)
   * @param max Scale maximum (not log)
   * @param minC Colour at scale minimum
   * @param maxC Colour at scale maximum
   */
  public GISHSBLogScaledKey(T min, T max, Color minC, Color maxC) {
    super(min, max, minC, maxC);
  }

  /**
   * Constructor using specified hue, saturation and brightness
   * 
   * @param min Scale minimum (not log)
   * @param max Scale maximum (not log)
   * @param minH Hue at scale minimum
   * @param minS Saturation at scale minimum
   * @param minB Brightness at scale minimum
   * @param maxH Hue at scale maximum
   * @param maxS Saturation at scale maximum
   * @param maxB Brightness at scale maximum
   */
  public GISHSBLogScaledKey(T min, T max, float minH, float minS, float minB, float maxH, float maxS, float maxB) {
    super(min, max, minH, minS, minB, maxH, maxS, maxB);
  }

  /**
   * Constructor with specified tolerance
   * 
   * @param min Scale minimum (not log)
   * @param max Scale maximum (not log)
   * @param minC Colour at scale minimum
   * @param maxC Colour at scale maximum
   * @param tolerance Tolerance
   */
  public GISHSBLogScaledKey(T min, T max, Color minC, Color maxC, double tolerance) {
    super(min, max, minC, maxC, tolerance);
  }

  /**
   * Constructor with specified tolerance and given hue, saturation and
   * brightness values for the colours.
   * 
   * @param min Scale minimum (not log)
   * @param max Scale maximum (not log)
   * @param minH Hue at scale minimum
   * @param minS Saturation at scale minimum
   * @param minB Brightness at scale minimum
   * @param maxH Hue at scale maximum
   * @param maxS Saturation at scale maximum
   * @param maxB Brightness at scale maximum
   * @param tolerance Tolerance
   */
  public GISHSBLogScaledKey(T min, T max, float minH, float minS, float minB, float maxH, float maxS, float maxB,
      double tolerance) {
    super(min, max, minH, minS, minB, maxH, maxS, maxB, tolerance);
  }

  /**
   * <!-- toLinear -->
   *
   * @see uk.ac.macaulay.util.GISHSBScaledKey#toLinear(double)
   * @param arg
   * @return log(arg)
   */
  @Override
  protected double toLinear(double arg) {
    return Math.log(arg);
  }

  /**
   * <!-- fromLinear -->
   *
   * @see uk.ac.macaulay.util.GISHSBScaledKey#fromLinear(double)
   * @param arg
   * @return exp(arg)
   */
  @Override
  protected double fromLinear(double arg) {
    return Math.exp(arg);
  }

}
