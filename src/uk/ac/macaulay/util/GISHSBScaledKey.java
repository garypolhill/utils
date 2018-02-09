/*
 * uk.ac.macaulay.util: GISHSBScaledKey.java
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
 * <!-- GISHSBScaledKey -->
 * 
 * A scaled key using a straight line through HSB (Hue, Saturation, Brightness)
 * space.
 * 
 * @author Gary Polhill
 */
public class GISHSBScaledKey<T extends Number> extends GISScaledKey<T> {

  /**
   * Constructor with default tolerance
   * 
   * @param min Minimum on the scale
   * @param max Maximum on the scale
   * @param minC Colour to use at scale minimum
   * @param maxC Colour to use at scale maximum
   */
  public GISHSBScaledKey(T min, T max, Color minC, Color maxC) {
    super(min, max, minC, maxC);
  }

  /**
   * Convenience constructor allowing hue, saturation and brightness for minimum
   * and maximum points to be specified separately
   * 
   * @param min Scale minimum
   * @param max Scale maximum
   * @param minH Hue to use at scale minimum
   * @param minS Saturation to use at scale minimum
   * @param minB Brightness to use at scale minimum
   * @param maxH Hue to use at scale maximum
   * @param maxS Saturation to use at scale maximum
   * @param maxB Brightness to use at scale maximum
   */
  public GISHSBScaledKey(T min, T max, float minH, float minS, float minB, float maxH, float maxS, float maxB) {
    super(min, max, Color.getHSBColor(minH, minS, minB), Color.getHSBColor(maxH, maxS, maxB));
  }

  /**
   * @param min Minimum on the scale
   * @param max Maximum on the scale
   * @param minC Colour to use at scale minimum
   * @param maxC Colour to use at scale maximum
   * @param tolerance Tolerance
   */
  public GISHSBScaledKey(T min, T max, Color minC, Color maxC, double tolerance) {
    super(min, max, minC, maxC, tolerance);
  }

  /**
   * Convenience constructor allowing hue, saturation and brightness for minimum
   * and maximum points to be specified separately
   * 
   * @param min Scale minimum
   * @param max Scale maximum
   * @param minH Hue to use at scale minimum
   * @param minS Saturation to use at scale minimum
   * @param minB Brightness to use at scale minimum
   * @param maxH Hue to use at scale maximum
   * @param maxS Saturation to use at scale maximum
   * @param maxB Brightness to use at scale maximum
   * @param tolerance Tolerance
   */
  public GISHSBScaledKey(T min, T max, float minH, float minS, float minB, float maxH, float maxS, float maxB,
      double tolerance) {
    super(min, max, Color.getHSBColor(minH, minS, minB), Color.getHSBColor(maxH, maxS, maxB), tolerance);
  }

  /**
   * <!-- getColor -->
   * 
   * Return the colour to use for the hue, saturation and brightness passed in
   * the array
   * 
   * @see uk.ac.macaulay.util.GISScaledKey#buildColor(float[])
   * @param arg Array containing hue, saturation and brightness
   * @return Corresponding colour
   */
  @Override
  protected Color buildColor(float[] arg) {
    return Color.getHSBColor(arg[0], arg[1], arg[2]);
  }

  /**
   * <!-- getComponents -->
   * 
   * Split a colour into hue, saturation and brightness
   * 
   * @see uk.ac.macaulay.util.GISScaledKey#getComponents(java.awt.Color)
   * @param arg The colour
   * @return Array containing hue, saturation and brightness
   */
  @Override
  protected float[] getComponents(Color arg) {
    return Color.RGBtoHSB(arg.getRed(), arg.getGreen(), arg.getBlue(), null);
  }

  /**
   * <!-- fromLinear -->
   * 
   * Don't convert the scale
   * 
   * @see uk.ac.macaulay.util.GISScaledKey#fromLinear(double)
   * @param arg
   * @return arg
   */
  @Override
  protected double fromLinear(double arg) {
    return arg;
  }

  /**
   * <!-- toLinear -->
   * 
   * Don't convert the scale
   * 
   * @see uk.ac.macaulay.util.GISScaledKey#toLinear(double)
   * @param arg
   * @return arg
   */
  @Override
  protected double toLinear(double arg) {
    return arg;
  }

}
