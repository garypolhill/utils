/*
 * uk.ac.macaulay.util: AbstractGISRasterImageConverter.java
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

/**
 * <!-- AbstractGISRasterImageConverter -->
 * 
 * Message handler for {@link GISRasterImageConverter} implementations.
 * 
 * <!-- In fact, it is rather more general than that... -->
 * 
 * @author Gary Polhill
 */
public abstract class AbstractGISRasterImageConverter<T> implements GISRasterImageConverter<T> {
  /**
   * The message
   */
  private String message = null;

  /**
   * <!-- getFailureMessage -->
   * 
   * Implement part of the GISRasterImageConverter interface.
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getFailureMessage()
   * @return The reason the last call to <code>getColor()</code> or
   *         <code>getEntry()</code> returned <code>null</code>.
   */
  public String getFailureMessage() {
    return message;
  }

  /**
   * <!-- setFailureMessage -->
   * 
   * Set the message
   *
   * @param message The message to set.
   */
  protected void setFailureMessage(String message) {
    this.message = message;
  }
}
