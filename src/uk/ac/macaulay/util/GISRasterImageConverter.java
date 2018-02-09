/*
 * uk.ac.macaulay.util: GISRasterImageConverter.java
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
 * <!-- GISRasterImageConverter -->
 * 
 * Interface for converters from an entry in a cell in a GISRaster to a colour
 * to represent that entry, and vice versa.
 * 
 * @author Gary Polhill
 */
public interface GISRasterImageConverter<T> {
  /**
   * <!-- getColor -->
   * 
   * Return the colour associated with the entry in the cell
   * 
   * @param entry The entry in the cell
   * @return The colour to represent it
   */
  public Color getColor(T entry);

  /**
   * <!-- getEntry -->
   * 
   * Return the entry associated with the colour (in some cases, this may not
   * return an exact value)
   * 
   * @param colour The colour used to represent an entry
   * @return The entry associated with the colour
   */
  public T getEntry(Color colour);

  /**
   * <!-- getFailureMessage -->
   * 
   * The {@link #getColor(Object)} and {@link #getEntry(Color)} methods should
   * return <code>null</code> if they cannot find a value. If they do, this
   * method can be used to find out why. It is only guaranteed to have a
   * sensible value if the last call to one of the above methods returned
   * <code>null</code>.
   * 
   * @return The error message associated with the last <code>null</code> value
   *         returned from <code>getColor()</code> or <code>getEntry()</code>
   */
  public String getFailureMessage();
}
