/*
 * uk.ac.macaulay.util: GISMappedKey.java
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
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- GISMappedKey -->
 * 
 * A key consisting of a one-to-one map from colour to entry. The key can be
 * added to at any time using {@link #addKey(Color, Object)}.
 * 
 * @author Gary Polhill
 */
public class GISMappedKey<T> extends AbstractGISRasterImageConverter<T> implements GISRasterImageConverter<T> {
  /**
   * Map from entry to colour
   */
  private Map<T, Color> key;

  /**
   * Map from colour to entry
   */
  private Map<Color, T> yek;

  /**
   * Constructor with empty map
   */
  public GISMappedKey() {
    key = new HashMap<T, Color>();
    yek = new HashMap<Color, T>();
  }

  /**
   * Constructor with given map
   * 
   * @param key Map from datatype to colour to use for each value
   */
  public GISMappedKey(final Map<T, Color> key) {
    this();
    for(T entry: key.keySet()) {
      addKey(key.get(entry), entry);
    }
  }

  /**
   * <!-- addKey -->
   * 
   * Add an entry to the key. An exception will be thrown if this violates
   * one-to-one mapping.
   * 
   * @param c Colour
   * @param item Entry to associate it with.
   * @throws IllegalArgumentException
   */
  public void addKey(Color c, T item) {
    if((yek.containsKey(c) && !yek.get(c).equals(item)) || (key.containsKey(item) && !key.get(item).equals(c))) {
      throw new IllegalArgumentException("Not a one-to-one mapping from colour to key (" + c + " <-> " + item
        + "). Already have (" + key.get(item) + " <-> " + item + ") and (" + c + " <-> " + yek.get(c) + ")");
    }
    yek.put(c, item);
    key.put(item, c);
  }

  /**
   * <!-- getColor -->
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getColor(java.lang.Object)
   * @param entry
   * @return The colour associated with the entry
   */
  @Override
  public Color getColor(T entry) {
    setFailureMessage("Key " + this + " does not contain entry " + entry);
    return key.get(entry);
  }

  /**
   * <!-- getEntry -->
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getEntry(java.awt.Color)
   * @param colour
   * @return The entry associated with the colour
   */
  @Override
  public T getEntry(Color colour) {
    setFailureMessage("Key " + this + " does not contain colour #" + Integer.toHexString(colour.getRGB()));
    return yek.get(colour);
  }

  /**
   * <!-- toString -->
   * 
   * Return a string representation of this key, for example:
   * <code>GISMappedKey{black &lt;-&gt; #000000, white &lt;-&gt; #ffffff}</code>
   * 
   * @see java.lang.Object#toString()
   * @return A string representation of this key
   */
  @Override
  public String toString() {
    StringBuffer buff = new StringBuffer(getClass().getSimpleName());
    buff.append("{");
    boolean first = true;
    for(T entry: key.keySet()) {
      if(!first) buff.append(", ");
      else
        first = false;
      buff.append(entry);
      buff.append(" <-> #");
      buff.append(Integer.toHexString(key.get(entry).getRGB()));
    }
    buff.append("}");
    return buff.toString();
  }

}
