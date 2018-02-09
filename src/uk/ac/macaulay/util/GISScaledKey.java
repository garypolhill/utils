/*
 * uk.ac.macaulay.util: GISScaledKey.java
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
 * <!-- GISScaledKey -->
 * 
 * This is an abstract class for keys that map a single range of values onto a
 * straight line through colour space. This is for {@link GISRaster}s that use a
 * {@link Number} as the entry in the table. Although this class accepts any
 * Number, everything is in fact converted to a <code>double</code>, so if
 * you're using large <code>long</code>s, <code>BigInteger</code>s or
 * <code>BigDecimal<code>s,
 * it probably won't work very well. When converting back from the line of colours
 * to the scaled value, each colour parameter will return its own value for the
 * scaled value. If these numbers are too different, then the colour is assumed
 * not to be on the straight line through colour space, and an exception is thrown.
 * The colour parameters at the minimum and maximum of the scale are not assumed
 * to be greater or less than each other.
 * 
 * @author Gary Polhill
 */
public abstract class GISScaledKey<T extends Number> extends AbstractGISRasterImageConverter<T> implements
    GISRasterImageConverter<T>, Comparable<GISScaledKey<T>>, PartialOrderComparable<GISScaledKey<T>> {
  /**
   * The default tolerance with which to detect whether a colour is on the line.
   */
  static final double DEFAULT_TOLERANCE = 16.0 * Math.ulp(1.0);

  /**
   * The colour parameters to use at the minimum of the scale
   */
  private float minC[];

  /**
   * The colour parameters to use at the maximum of the scale
   */
  private float maxC[];

  /**
   * The minimum of the scale, converted to linear
   */
  private double min;

  /**
   * The maximum of the scale, converted to linear
   */
  private double max;

  /**
   * Tolerance when different bands of colours generate different numbers for
   * the scale
   */
  private double tolerance = DEFAULT_TOLERANCE;

  /**
   * Type for entries in the scale (a subclass of Number)
   */
  private Class<?> type;

  /**
   * Constructor using default tolerance
   * 
   * @param min Scale minimum
   * @param max Scale maximum
   * @param minC Colour band parameters at the scale minimum
   * @param maxC Colour band parameters at the scale maximum
   */
  public GISScaledKey(T min, T max, Color minC, Color maxC) {
    type = min.getClass();
    this.min = toLinear(min.doubleValue());
    this.max = toLinear(max.doubleValue());
    if(Double.isInfinite(this.min) || Double.isNaN(this.min)) {
      throw new IllegalArgumentException("Can't convert " + min.getClass() + " " + min
        + " to double precision floating point number");
    }
    if(Double.isInfinite(this.max) || Double.isNaN(this.max)) {
      throw new IllegalArgumentException("Can't convert " + max.getClass() + " " + max
        + " to double precision floating point number");
    }
    if(this.max <= this.min) {
      throw new IllegalArgumentException(max.getClass() + " " + max
        + " converts to double precision floating point number " + this.max + " which is less than or equal to "
        + this.min + " converted from " + min.getClass() + " " + min);
    }
    this.minC = getComponents(minC);
    this.maxC = getComponents(maxC);
    if(minC.equals(maxC)) {
      throw new IllegalArgumentException("Colours " + minC + " and " + maxC + " are equal, and so do not form a scale");
    }
  }

  /**
   * Constructor using specified tolerance
   * 
   * @param min Scale minimum
   * @param max Scale maximum
   * @param minC Colour band parameters to use at scale minimum
   * @param maxC Colour band parameters to use at scale maximum
   * @param tolerance Tolerance
   */
  public GISScaledKey(T min, T max, Color minC, Color maxC, double tolerance) {
    this(min, max, minC, maxC);
    this.tolerance = tolerance;
  }

  /**
   * <!-- buildColor -->
   * 
   * Convert a set of colour band parameters into a {@link Color}.
   * 
   * @param arg Colour band parameter array
   * @return The corresponding Color
   */
  protected abstract Color buildColor(float[] arg);

  /**
   * <!-- getComponents -->
   * 
   * Convert {@link Color} into colour band parameters.
   * 
   * @param arg Color
   * @return The corresponding colour band parameters
   */
  protected abstract float[] getComponents(Color arg);

  /**
   * <!-- toLinear -->
   * 
   * Scale the entry onto the colour scale range using this function
   * 
   * @param arg Value in the entry
   * @return Value to use between min and max
   */
  protected abstract double toLinear(double arg);

  /**
   * <!-- fromLinear -->
   * 
   * Inverse function of {@link #toLinear(double)}.
   * 
   * @param arg Value in the scale between min and max
   * @return Entry value
   */
  protected abstract double fromLinear(double arg);

  /**
   * <!-- contains -->
   * 
   * Does this key contain an entry?
   * 
   * @param entry
   * @return <code>true</code> if the entry is within the range of this key
   */
  public boolean contains(T entry) {
    double dentry = toLinear(entry.doubleValue());
    return dentry >= min && dentry <= max;
  }

  /**
   * <!-- getColor -->
   * 
   * Return the colour to use for the entry
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getColor(java.lang.Object)
   * @param entry
   * @return The colour to use for the entry or <code>null</code> if the entry
   *         is out of range
   */
  public Color getColor(T entry) {
    if(!contains(entry)) {
      setFailureMessage("Scale " + this + " does not contain entry " + entry);
      return null;
    }
    double scale = (max - toLinear(entry.doubleValue())) / (max - min);
    float[] arg = new float[minC.length];
    for(int i = 0; i < arg.length; i++) {
      arg[i] = (float)(scale * (minC[i] - maxC[i]) + minC[i]);
    }
    return buildColor(arg);
  }

  /**
   * <!-- getEntry -->
   * 
   * Return the entry for a given colour. A small tolerance in the difference
   * between entries is allowed to detect when the bands give different values
   * for the entry and hence the colour in the argument is not part of the
   * original scale. If there is any difference in value, the entry returned
   * will be that from the band with maximum difference between minimum and
   * maximum, as this should offer the greatest accuracy.
   * 
   * @see uk.ac.macaulay.util.GISRasterImageConverter#getEntry(java.awt.Color)
   * @param colour The colour to convert to an entry
   * @return The entry for the specified colour
   */
  public T getEntry(Color colour) {
    float arg[] = getComponents(colour);
    if(!inRange(colour)) {
      setFailureMessage("Colour (" + arg[0] + ", " + arg[1] + ", " + arg[2] + ") is not within range (" + minC[0]
        + ", " + minC[1] + ", " + minC[2] + ") to (" + maxC[0] + ", " + maxC[1] + ", " + maxC[2] + ")");
      return null;
    }
    Double[] scale = new Double[] { null, null, null };
    double tscale = 0.0;
    double maxDiff = 0.0;
    for(int i = 0; i < minC.length; i++) {
      if(minC[i] < maxC[i]) {
        scale[i] = new Double((arg[i] - minC[i]) / (maxC[i] - minC[i]));
        for(int j = 0; j < i; j++) {
          if(scale[j] == null) continue;
          if(Math.abs(scale[i] - scale[j]) > tolerance) {
            setFailureMessage("Colour (" + arg[0] + ", " + arg[1] + ", " + arg[2] + ") is not on a line between ("
              + minC[0] + ", " + minC[1] + ", " + minC[2] + ") to (" + maxC[0] + ", " + maxC[1] + ", " + maxC[2] + ")");
            return null;
          }
        }
        double diff = Math.abs(maxC[i] - minC[i]);
        if(diff > maxDiff) {
          maxDiff = diff;
          tscale = scale[i];
        }
      }
    }

    return getNumber(fromLinear(tscale));
  }

  /**
   * <!-- getNumber -->
   * 
   * Construct a number of the corresponding type from a double precision
   * argument (this will lose accuracy for some datatypes in certain cases).
   * 
   * @param n The double precision floating point to convert
   * @return A {@link Number} of the appropriate type
   */
  @SuppressWarnings("unchecked")
  protected T getNumber(Double n) {
    try {
      if(type == Byte.class) {
        return (T)(new Byte((new Double(Math.rint(n))).byteValue()));
      }
      else if(type == Short.class) {
        return (T)(new Short((new Double(Math.rint(n))).shortValue()));
      }
      else if(type == Integer.class) {
        return (T)(new Integer((new Double(Math.rint(n))).intValue()));
      }
      else if(type == Long.class) {
        return (T)(new Long((new Double(Math.rint(n))).longValue()));
      }
      else if(type == java.math.BigInteger.class) {
        return (T)((new java.math.BigDecimal(Math.rint(n))).toBigInteger());
      }
      else if(type == java.util.concurrent.atomic.AtomicInteger.class) {
        return (T)(new java.util.concurrent.atomic.AtomicInteger((new Double(Math.rint(n))).intValue()));
      }
      else if(type == java.util.concurrent.atomic.AtomicLong.class) {
        return (T)(new java.util.concurrent.atomic.AtomicLong((new Double(Math.rint(n))).longValue()));
      }
      else if(type == Float.class) {
        return (T)(new Float(n.floatValue()));
      }
      else if(type == Double.class) {
        return (T)(new Double(n.doubleValue()));
      }
      else if(type == java.math.BigDecimal.class) {
        return (T)(new java.math.BigDecimal(n));
      }
      else {
        setFailureMessage("Type " + type + " is not a known subclass of Number");
        return null;
      }
    }
    catch(ClassCastException e) {
      throw new Bug();
    }
  }

  /**
   * <!-- inRange -->
   * 
   * @param colour
   * @return <code>true</code> if the colour is in the range
   */
  public boolean inRange(Color colour) {
    float arg[] = getComponents(colour);
    for(int i = 0; i < arg.length; i++) {
      if(arg[i] < minC[i] || arg[i] > maxC[i]) return false;
    }
    return true;
  }

  /**
   * <!-- compareTo -->
   * 
   * Compare this with another GISScaledKey, by the minima of their scaled range
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param arg The other GISScaledKey
   * @return -1, 0 or 1, accordingly.
   */
  public int compareTo(GISScaledKey<T> arg) {
    return Double.compare(fromLinear(min), arg.fromLinear(min));
  }

  /**
   * <!-- partialOrderCompareTo -->
   * 
   * Compare this with another GISScaledKey, using partial order comparison
   * 
   * @see uk.ac.macaulay.util.PartialOrderComparable#partialOrderCompareTo(java.lang.Object)
   * @param arg The other GISScaledKey
   * @return <code>LESS_THAN</code> if this scale range is less than the
   *         arguments, and they do not overlap; <code>MORE_THAN</code> if this
   *         scale range is more than the arguments, and they do not overlap;
   *         <code>EQUAL_TO</code> if the ranges are the same;
   *         <code>INCOMPARABLE</code> otherwise.
   */
  public PartialOrderComparison partialOrderCompareTo(GISScaledKey<T> arg) {
    if(fromLinear(max) < arg.fromLinear(arg.min)) return PartialOrderComparison.LESS_THAN;
    if(fromLinear(min) > arg.fromLinear(arg.max)) return PartialOrderComparison.MORE_THAN;
    if(fromLinear(min) == arg.fromLinear(arg.min) && fromLinear(max) == arg.fromLinear(arg.max)) {
      return PartialOrderComparison.EQUAL_TO;
    }
    return PartialOrderComparison.INCOMPARABLE;
  }

  /**
   * <!-- toString -->
   * 
   * Build a string describing this object, including information on the class
   * name, the type, the minimum and maximum values, and the colours at these
   * points. An example would be
   * <code>GISRGBScaledKey&lt;Double&gt;[-10.0: #000000, 10.0: #FFFFFF]</code>
   * 
   * @see java.lang.Object#toString()
   * @return String representation of the object
   */
  @Override
  public String toString() {
    StringBuffer buff = new StringBuffer(this.getClass().getSimpleName());
    buff.append("<");
    buff.append(type.getSimpleName());
    buff.append(">[");
    buff.append(fromLinear(min));
    buff.append(": #");
    buff.append(Integer.toHexString(buildColor(minC).getRGB()));
    buff.append(", ");
    buff.append(fromLinear(max));
    buff.append(": #");
    buff.append(Integer.toHexString(buildColor(maxC).getRGB()));
    buff.append("]");
    return buff.toString();
  }
}
