/*
 * uk.ac.macaulay.util: GISRGBScaledKey.java
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
 * <!-- GISRGBScaledKey -->
 * 
 * A key operating on a single scale in RGB colour space. Two colours are
 * specified at the minimum and the maximum of the scale. The colour to use for
 * intermediate points is on a linear scale on the straight line through RGB
 * space between the minimum and maximum colour points.
 * 
 * @author Gary Polhill
 */
public class GISRGBScaledKey<T extends Number> extends GISScaledKey<T> {

  /**
   * Constructor using default tolerance
   * 
   * @param min Minimum entry
   * @param max Maximum entry
   * @param minC Colour to use at the minimum of the scale
   * @param maxC Colour to use at the maximum of the scale
   */
  public GISRGBScaledKey(T min, T max, Color minC, Color maxC) {
    super(min, max, minC, maxC);
  }

  /**
   * Convenience constructor not requiring the creation of a colour
   * 
   * @param min Minimum entry
   * @param max Maximum entry
   * @param minR Red at minimum entry
   * @param minG Green at minimum entry
   * @param minB Blue at minimum entry
   * @param maxR Red at maximum entry
   * @param maxG Green at maximum entry
   * @param maxB Blue at maximum entry
   */
  public GISRGBScaledKey(T min, T max, int minR, int minG, int minB, int maxR, int maxG, int maxB) {
    this(min, max, new Color(minR, minG, minB), new Color(maxR, maxG, maxB));
  }

  /**
   * Convenience constructor not requiring the creation of a colour using
   * floating point colour values
   * 
   * @param min Minimum entry
   * @param max Maximum entry
   * @param minR Red at minimum entry
   * @param minG Green at minimum entry
   * @param minB Blue at minimum entry
   * @param maxR Red at maximum entry
   * @param maxG Green at maximum entry
   * @param maxB Blue at maximum entry
   */
  public GISRGBScaledKey(T min, T max, float minR, float minG, float minB, float maxR, float maxG, float maxB) {
    this(min, max, new Color(minR, minG, minB), new Color(maxR, maxG, maxB));
  }

  /**
   * Constructor using a specified tolerance
   * 
   * @param min Minimum entry
   * @param max Maximum entry
   * @param minC Colour at minimum entry
   * @param maxC Colour at maximum entry
   * @param tolerance Tolerance
   */
  public GISRGBScaledKey(T min, T max, Color minC, Color maxC, double tolerance) {
    super(min, max, minC, maxC, tolerance);
    // TODO Auto-generated constructor stub
  }

  /**
   * Convenience constructor not requiring the creation of a colour
   * 
   * @param min Minimum entry
   * @param max Maximum entry
   * @param minR Red at minimum entry
   * @param minG Green at minimum entry
   * @param minB Blue at minimum entry
   * @param maxR Red at maximum entry
   * @param maxG Green at maximum entry
   * @param maxB Blue at maximum entry
   * @param tolerance Tolerance
   */
  public GISRGBScaledKey(T min, T max, int minR, int minG, int minB, int maxR, int maxG, int maxB, double tolerance) {
    super(min, max, new Color(minR, minG, minB), new Color(maxR, maxG, maxB), tolerance);
  }

  /**
   * Convenience constructor not requiring the creation of a colour, using
   * floating point colour values
   * 
   * @param min Minimum entry
   * @param max Maximum entry
   * @param minR Red at minimum entry
   * @param minG Green at minimum entry
   * @param minB Blue at minimum entry
   * @param maxR Red at maximum entry
   * @param maxG Green at maximum entry
   * @param maxB Blue at maximum entry
   * @param tolerance Tolerance
   */
  public GISRGBScaledKey(T min, T max, float minR, float minG, float minB, float maxR, float maxG, float maxB,
      double tolerance) {
    super(min, max, new Color(minR, minG, minB), new Color(maxR, maxG, maxB), tolerance);
  }

  /**
   * <!-- getColor -->
   * 
   * Return the colour to use for a given set of RGB values
   * 
   * @see uk.ac.macaulay.util.GISScaledKey#buildColor(float[])
   * @param arg The RGB values
   * @return The colour
   */
  @Override
  protected Color buildColor(float[] arg) {
    return new Color(arg[0], arg[1], arg[2]);
  }

  /**
   * <!-- getComponents -->
   * 
   * Return the RGB values of the colour
   * 
   * @see uk.ac.macaulay.util.GISScaledKey#getComponents(java.awt.Color)
   * @param arg The colour
   * @return The RGB values
   */
  @Override
  protected float[] getComponents(Color arg) {
    return arg.getRGBColorComponents(null);
  }

  /**
   * <!-- fromLinear -->
   * 
   * Use a linear scale--the argument is not converted
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
   * Use a linear scale--the argument is not converted
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
