/*
 * uk.ac.macaulay.util: AbstractFloatingPointComparison.java
 * 
 * Copyright (C) 2009 Macaulay Institute
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
 * AbstractFloatingPointComparison
 * 
 * 
 * 
 * @author Gary Polhill
 */
public abstract class AbstractFloatingPointComparison implements FloatingPointComparison {

  /**
   * <!-- EQ -->
   * 
   * @see uk.ac.macaulay.util.FloatingPointComparison#EQ(double, double)
   */
  public boolean EQ(double a, double b) {
    return eq(a, b);
  }
  
  public boolean NE(double a, double b) {
    return !EQ(a, b);
  }
  
  public boolean GE(double a, double b) {
    return gt(a, b) || EQ(a, b);
  }
  
  public boolean LE(double a, double b) {
    return lt(a, b) || EQ(a, b);
  }

  public abstract boolean eq(double a, double b);

  public abstract boolean lt(double a, double b);

  public abstract boolean gt(double a, double b);

  public boolean ge(double a, double b) {
    return gt(a, b) || eq(a, b);
  }

  public boolean le(double a, double b) {
    return lt(a, b) || eq(a, b);
  }

  public boolean ne(double a, double b) {
    return !eq(a, b);
  }

  /**
   * <!-- EQ -->
   * 
   * @see uk.ac.macaulay.util.FloatingPointComparison#EQ(float, float)
   */
  public boolean EQ(float a, float b) {
    return eq(a, b);
  }
  
  public boolean NE(float a, float b) {
    return !EQ(a, b);
  }
  
  public boolean GE(float a, float b) {
    return gt(a, b) || EQ(a, b);
  }
  
  public boolean LE(float a, float b) {
    return lt(a, b) || EQ(a, b);
  }

  public abstract boolean eq(float a, float b);

  public abstract boolean lt(float a, float b);

  public abstract boolean gt(float a, float b);

  public boolean ge(float a, float b) {
    return gt(a, b) || eq(a, b);
  }

  public boolean le(float a, float b) {
    return lt(a, b) || eq(a, b);
  }

  public boolean ne(float a, float b) {
    return !eq(a, b);
  }

  public int compare(Number arg0, Number arg1) {
    if(arg0 instanceof Float && arg1 instanceof Float) return compare(arg0.floatValue(), arg1.floatValue());
    else if(arg0 instanceof Double && arg1 instanceof Double) return compare(arg0.doubleValue(), arg1.doubleValue());
    else if((arg0 instanceof Float && !(arg1 instanceof Double))
      || (arg1 instanceof Float && !(arg0 instanceof Double))) {
      if(gt(arg0.floatValue(), arg1.floatValue())) return 1;
      else if(lt(arg0.floatValue(), arg1.floatValue())) return -1;
      else
        return 0;
    }
    else {
      if(gt(arg0.doubleValue(), arg1.doubleValue())) return 1;
      else if(lt(arg0.doubleValue(), arg1.doubleValue())) return -1;
      else
        return 0;
    }
  }

  public int compare(double a, double b) {
    return gt(a, b) ? 1 : (lt(a, b) ? -1 : 0);
  }

  public int compare(float a, float b) {
    return gt(a, b) ? 1 : (lt(a, b) ? -1 : 0);
  }
}
