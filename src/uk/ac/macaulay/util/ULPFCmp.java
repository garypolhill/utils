/*
 * uk.ac.macaulay.util: ULPFCmp.java Copyright (C) 2009 Macaulay
 * Institute
 * 
 * This file is part of obiama-0.2.
 * 
 * obiama-0.2 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * obiama-0.2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with obiama-0.2. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: Gary Polhill Macaulay Institute, Craigiebuckler,
 * Aberdeen. AB15 8QH. UK. g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

/**
 * ULPFCmp
 * 
 * A floating-point comparison class based on Math.ulp(). This is an
 * implementation of the principles outlined in <a
 * href="http://www.ibm.com/developerworks/java/library/j-math2.html">a
 * developerWorks article by Elliotte Rusty Harold</a> in which the recommended
 * JUnit test for floating-point equality is
 * <code>assertEquals(expectedValue, actualValue, 5 * Math.ulp(expectedValue))</code>
 * This introduces asymmetry in the tests, since one of the arguments will be
 * used to derive the tolerance, and the other won't. In the methods of this
 * class, it is always the first argument (<code>a</code>) that is assumed to be
 * the <code>expectedValue</code>.
 * 
 * @author Gary Polhill
 */
public class ULPFCmp extends AbstractFloatingPointComparison {
  private final double dmultiplier;
  private final float fmultiplier;
  public static final int DEFAULT_MULTIPLIER = 1;

  public ULPFCmp() {
    dmultiplier = (double)DEFAULT_MULTIPLIER;
    fmultiplier = (float)DEFAULT_MULTIPLIER;
  }

  public ULPFCmp(int multiplier) {
    if(multiplier < 0) throw new FloatingPointComparisonException("Invalid multiplier for ulp: " + multiplier);
    dmultiplier = (double)multiplier;
    fmultiplier = (float)multiplier;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(double, double)
   */
  @Override
  public boolean eq(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    return a <= b + tolerance && b <= a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(float, float)
   */
  @Override
  public boolean eq(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    return a <= b + tolerance && b <= a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ge(double, double)
   */
  @Override
  public boolean ge(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    return a + tolerance >= b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ge(float, float)
   */
  @Override
  public boolean ge(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    return a + tolerance >= b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(double, double)
   */
  @Override
  public boolean gt(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    return a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(float, float)
   */
  @Override
  public boolean gt(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    return a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#le(double, double)
   */
  @Override
  public boolean le(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    return a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#le(float, float)
   */
  @Override
  public boolean le(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    return a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(double, double)
   */
  @Override
  public boolean lt(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    return a + tolerance < b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(float, float)
   */
  @Override
  public boolean lt(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    return a + tolerance < b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ne(double, double)
   */
  @Override
  public boolean ne(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    return a + tolerance < b || a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ne(float, float)
   */
  @Override
  public boolean ne(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    return a + tolerance < b || a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#compare(double,
   * double)
   */
  @Override
  public int compare(double a, double b) {
    double tolerance = dmultiplier * Math.ulp(Math.abs(a));
    if(a + tolerance < b) return -1;
    else if(a > b + tolerance) return 1;
    else
      return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#compare(float,
   * float)
   */
  @Override
  public int compare(float a, float b) {
    float tolerance = fmultiplier * Math.ulp(Math.abs(a));
    if(a + tolerance < b) return -1;
    else if(a > b + tolerance) return 1;
    else
      return 0;
  }
}
