/*
 * uk.ac.macaulay.util: GISIntegerKey.java
 * 
 * Copyright (C) 2011 Macaulay Institute
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
 * <!-- GISIntegerKey -->
 * 
 * A key that just converts an integer to an RGB colour and back again.
 * 
 * @author Gary Polhill
 */
public class GISIntegerKey extends AbstractGISRasterImageConverter<Integer> implements GISRasterImageConverter<Integer> {

  /**
   * <!-- getColor -->
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getColor(java.lang.Object)
   * @param entry
   * @return The colour corresponding to the entry
   */
  @Override
  public Color getColor(Integer entry) {
    return new Color(entry);
  }

  /**
   * <!-- getEntry -->
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getEntry(java.awt.Color)
   * @param colour
   * @return The integer corresponding to the colour
   */
  @Override
  public Integer getEntry(Color colour) {
    return colour.getRGB();
  }

}
