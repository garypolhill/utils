/*
 * uk.ac.macaulay.util: GISMultiScaleKey.java
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
import java.util.ArrayList;
import java.util.Collection;

/**
 * <!-- GISMultiScaleKey -->
 * 
 * This allows multiple scales to be mixed.
 * 
 * @author Gary Polhill
 */
public class GISMultiScaleKey<T extends Number> extends AbstractGISRasterImageConverter<T> implements
    GISRasterImageConverter<T> {

  /**
   * A sorted list of scales
   */
  private ArrayList<GISScaledKey<T>> scales;

  /**
   * Default constructor
   */
  public GISMultiScaleKey() {
    scales = new ArrayList<GISScaledKey<T>>();
  }

  /**
   * Constructor with a prepared collection of scales
   * 
   * @param scales The scales to use
   */
  public GISMultiScaleKey(Collection<GISScaledKey<T>> scales) {
    this();
    for(GISScaledKey<T> scale: scales) {
      addScale(scale);
    }
  }

  /**
   * <!-- addScale -->
   * 
   * Add a scale to this collection of scales. An exception will be thrown if
   * this causes a clash.
   * 
   * @param scale
   * @throws IllegalArgumentException if the insertion of the scale causes a
   *           clash
   */
  public void addScale(GISScaledKey<T> scale) {
    int i;
    for(i = 0; i < scales.size(); i++) {
      if(scale.compareTo(scales.get(i)) < 0) {
        PartialOrderComparison result[] =
          new PartialOrderComparison[] { scale.partialOrderCompareTo(scales.get(i)),
            i == 0 ? PartialOrderComparison.MORE_THAN : scale.partialOrderCompareTo(scales.get(i - 1)) };
        if(result[0] != PartialOrderComparison.LESS_THAN || result[1] != PartialOrderComparison.MORE_THAN) {
          throw new IllegalArgumentException("Scale " + scale + " cannot be inserted "
            + (i == 0 ? "before " + scales.get(i) : "between " + scales.get(i - 1) + " and " + scales.get(i))
            + ", because there are overlapping minima and maxima");
        }
        scales.add(i, scale);
        break;
      }
    }
    if(i == scales.size()) scales.add(scale);
  }

  /**
   * <!-- getColor -->
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getColor(java.lang.Object)
   * @param entry
   * @return The colour for the entry or <code>null</code>
   */
  public Color getColor(T entry) {
    for(int i = 0; i < scales.size(); i++) {
      GISScaledKey<T> scale = scales.get(i);
      if(scale.contains(entry)) return scale.getColor(entry);
    }
    return null;
  }

  /**
   * <!-- getEntry -->
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getEntry(java.awt.Color)
   * @param colour
   * @return The entry for the colour
   */
  @Override
  public T getEntry(Color colour) {
    T entry = null;
    Integer i_entry = null;

    for(int i = 0; i < scales.size(); i++) {
      T entry_i = scales.get(i).getEntry(colour);
      if(entry_i == null) continue;
      if(entry != null && !entry.equals(entry_i)) {
        setFailureMessage("Ambiguous value returned for colour #" + Integer.toHexString(colour.getRGB())
          + "from scale " + scales.get(i) + " (" + entry_i + ") and " + scales.get(i_entry) + " (" + entry + ")");
        return null;
      }
      i_entry = i;
      entry = entry_i;
    }

    if(entry == null) {
      setFailureMessage("Colour #" + Integer.toHexString(colour.getRGB()) + " is not contained in this scale");
    }
    return entry;
  }

  /**
   * <!-- toString -->
   *
   * @see java.lang.Object#toString()
   * @return A string describing this scale
   */
  @Override
  public String toString() {
    StringBuffer buff = new StringBuffer(getClass().getSimpleName());
    buff.append("[");
    for(int i = 0; i < scales.size(); i++) {
      if(i > 0) buff.append(", ");
      buff.append(scales.get(i).toString());
    }
    buff.append("]");
    return buff.toString();
  }
}
