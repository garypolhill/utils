/*
 * uk.ac.macaulay.util: ToleranceWindowsFcmp.java
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
 * ToleranceWindowsFcmp
 * 
 * Compare floating point numbers using a small factor added to one or other of
 * the operators indicating a floating point error tolerance level, epsilon. The
 * implementation is designed to avoid using subtraction during comparisons to
 * avoid catastrophic cancellation--a risk, e.g. were equality to be tested
 * using <code>Math.abs(a - b) < epsilon</code>. This may be paranoia! For
 * example, most of the reading I have done on catastrophic cancellation are
 * when greater accuracy is required in |a-b| than the floating point precision
 * can supply. The above expression only requires enough accuracy that we can
 * tell whether the difference is less than epsilon. The program
 * TestCatastrophicCancellation tests a million random numbers between 0 and 1,
 * finding that in no case did using Math.abs() give a different answer than
 * that used here. However, rewriting the program to test a wider range of numbers
 * found differences in 12.5% of cases.
 * 
 * @author Gary Polhill
 */
public class ToleranceWindowsFCmp extends AbstractFloatingPointComparison {
  /**
   * Epsilon to use for double precision floating point comparisons
   */
  private final double depsilon;

  /**
   * Epsilon to use for single precision floating point comparisons
   */
  private final float fepsilon;

  /**
   * Default epsilon to use for double precision floating point comparisons.
   * This is taken from DBL_EPSILON in float.h.
   */
  public static final double DEFAULT_DOUBLE_EPSILON = 2.2204460492503131e-16;

  /**
   * Default epsilon to use for single precision floating point comparisons.
   * This is taken from FLT_EPSILON in float.h.
   */
  public static final float DEFAULT_FLOAT_EPSILON = 1.19209290e-07F;

  /**
   * <code>true</code> if single precision floating point comparisons are
   * allowed (they can be disallowed depending on initialisation)
   */
  private boolean allowFloat;

  /**
   * Constructor using default single and double epsilons. Single precision
   * floating point comparisons are allowed.
   */
  public ToleranceWindowsFCmp() {
    depsilon = DEFAULT_DOUBLE_EPSILON;
    fepsilon = DEFAULT_FLOAT_EPSILON;
    allowFloat = true;
  }

  /**
   * Constructor passing in a double precision epsilon that will also be used
   * for single precision comparisons. If the double precision epsilon is more
   * than 0, but cannot be cast to a single precision epsilon greater than 0,
   * then single-precision comparisons will not be allowed.
   * 
   * @param epsilon
   */
  public ToleranceWindowsFCmp(double epsilon) {
    this(epsilon, (float)epsilon);
    if(fepsilon == 0.0F && epsilon > 0.0) allowFloat = false;
  }

  /**
   * Constructor passing in a single precision epsilon that will also be used
   * for double precision comparisons.
   * 
   * @param epsilon
   */
  public ToleranceWindowsFCmp(float epsilon) {
    this((double)epsilon, epsilon);
  }

  /**
   * Constructor allowing non-default single and double precision epsilons to be
   * used. Both must be non-negative, non-infinite numbers.
   * 
   * @param depsilon double precision epsilon
   * @param fepsilon single precision epsilon
   */
  public ToleranceWindowsFCmp(double depsilon, float fepsilon) {
    if(depsilon < 0.0) throw new FloatingPointComparisonException("Negative epsilon: " + depsilon);
    if(Double.isInfinite(depsilon) || Double.isNaN(depsilon)) {
      throw new FloatingPointComparisonException("Invalid epsilon: " + depsilon);
    }

    if(fepsilon < 0.0F) throw new FloatingPointComparisonException("Negative epsilon: " + fepsilon);
    if(Float.isInfinite(fepsilon) || Float.isNaN(fepsilon)) {
      throw new FloatingPointComparisonException("Invalid epsilon: " + fepsilon);
    }

    this.depsilon = depsilon;
    this.fepsilon = fepsilon;
    allowFloat = true;
  }

  /**
   * <!-- eq -->
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(double, double)
   */
  @Override
  public boolean eq(double a, double b) {
    return b <= a + depsilon && a <= b + depsilon;
  }

  /**
   * <!-- eq -->
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(float, float)
   */
  @Override
  public boolean eq(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    return b <= a + fepsilon && a <= b + fepsilon;
  }

  @Override
  public boolean ge(double a, double b) {
    return a + depsilon >= b;
  }

  @Override
  public boolean ge(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    return a + fepsilon >= b;
  }

  /**
   * <!-- gt -->
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(double, double)
   */
  @Override
  public boolean gt(double a, double b) {
    return a > b + depsilon;
  }

  /**
   * <!-- gt -->
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(float, float)
   */
  @Override
  public boolean gt(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    return a > b + fepsilon;
  }

  @Override
  public boolean le(double a, double b) {
    return a <= b + depsilon;
  }

  @Override
  public boolean le(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    return a <= b + fepsilon;
  }

  /**
   * <!-- lt -->
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(double, double)
   */
  @Override
  public boolean lt(double a, double b) {
    return a + depsilon < b;
  }

  /**
   * <!-- lt -->
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(float, float)
   */
  @Override
  public boolean lt(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    return a + fepsilon < b;
  }
  
  @Override
  public boolean ne(double a, double b) {
    return a + depsilon < b || a > b + depsilon;
  }
  
  @Override
  public boolean ne(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    return a + fepsilon < b || a > b + fepsilon;
  }

  @Override
  public int compare(double a, double b) {
    if(a > b + depsilon) return 1;
    else if(a + depsilon < b) return -1;
    else
      return 0;
  }

  @Override
  public int compare(float a, float b) {
    if(!allowFloat) {
      throw new FloatingPointComparisonException("Epsilon " + depsilon + " too small for float comparisons", a, b);
    }
    if(a > b + fepsilon) return 1;
    else if(a + fepsilon < b) return -1;
    else
      return 0;
  }
}
