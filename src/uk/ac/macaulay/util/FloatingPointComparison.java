/*
 * uk.ac.macaulay.util: FloatingPointComparison.java
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

import java.util.Comparator;

/**
 * FloatingPointComparison
 * 
 * Standard methods to be implemented by any class performing a floating-point
 * comparison. Those implementations for which there is any asymmetry in the
 * arguments should always treat the first argument (<code>a</code>) as though
 * it is the expected value for a comparison. That is, one would expect
 * <code>a</code> to always be the constant in any comparison of a variable with
 * a constant.
 * 
 * @author Gary Polhill
 */
public interface FloatingPointComparison extends Comparator<Number> {
  /**
   * <!-- eq -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a approximately equals b
   */
  public boolean eq(double a, double b);

  /**
   * <!-- ne -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is not approximately equal to b
   */
  public boolean ne(double a, double b);

  /**
   * <!-- gt -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately greater than b
   */
  public boolean gt(double a, double b);

  /**
   * <!-- le -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately less than or equal to b
   */
  public boolean le(double a, double b);

  /**
   * <!-- lt -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately less than b
   */
  public boolean lt(double a, double b);

  /**
   * <!-- ge -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately greater than or equal to b
   */
  public boolean ge(double a, double b);

  /**
   * <!-- EQ -->
   * 
   * This method allows a more strict approximately equal option to be
   * implemented (e.g. Knuth). For most implementations, this will simply call
   * eq().
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is more strictly approximately equal to b
   */
  public boolean EQ(double a, double b);
  
  /**
   * <!-- NE -->
   * 
   * Not strictly equal. Most implementations will just call ne().
   *
   * @param a
   * @param b
   * @return <code>true</code> if a is not more strictly approximately equal to b
   */
  public boolean NE(double a, double b);
  
  /**
   * <!-- GE -->
   * 
   * Greater than or strictly equal. Most implementations will call ge().
   *
   * @param a
   * @param b
   * @return <code>true</code> if a is greater than or more strictly approximately equal to b
   */
  public boolean GE(double a, double b);
  
  /**
   * <!-- LE -->
   * 
   * Less than or strictly equal. Most implementations will call le().
   *
   * @param a
   * @param b
   * @return <code>true</code> if a is less than or more strictly approximately equal to b
   */
  public boolean LE(double a, double b);

  /**
   * <!-- eq -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a approximately equals b
   */
  public boolean eq(float a, float b);

  /**
   * <!-- ne -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is not approximately equal to b
   */
  public boolean ne(float a, float b);

  /**
   * <!-- gt -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately greater than b
   */
  public boolean gt(float a, float b);

  /**
   * <!-- le -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately less than or equal to b
   */
  public boolean le(float a, float b);

  /**
   * <!-- lt -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately less than b
   */
  public boolean lt(float a, float b);

  /**
   * <!-- ge -->
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is approximately greater than or equal to b
   */
  public boolean ge(float a, float b);

  /**
   * <!-- EQ -->
   * 
   * This method allows a more strict approximately equal option to be
   * implemented (e.g. Knuth). For most implementations, this will simply call
   * eq().
   * 
   * @param a
   * @param b
   * @return <code>true</code> if a is more strictly approximately equal to b
   */
  public boolean EQ(float a, float b);
  
  /**
   * <!-- NE -->
   * 
   * Not strictly equal. Most implementations will just call ne().
   *
   * @param a
   * @param b
   * @return <code>true</code> if a is not more strictly approximately equal to b
   */
  public boolean NE(float a, float b);
  
  /**
   * <!-- GE -->
   * 
   * Greater than or strictly equal. Most implementations will call ge().
   *
   * @param a
   * @param b
   * @return <code>true</code> if a is greater than or more strictly approximately equal to b
   */
  public boolean GE(float a, float b);
  
  /**
   * <!-- LE -->
   * 
   * Less than or strictly equal. Most implementations will call le().
   *
   * @param a
   * @param b
   * @return <code>true</code> if a is less than or more strictly approximately equal to b
   */
  public boolean LE(float a, float b);

}
