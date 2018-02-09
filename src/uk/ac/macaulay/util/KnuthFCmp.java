/*
 * uk.ac.macaulay.util: KnuthFCmp.java Copyright (C) 2009 Macaulay Institute
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
 * KnuthFCmp
 * 
 * <p>
 * An implementation of the floating-point comparison operators in equations
 * (21-24) on p. 233 of Knuth, D. E. (1998) <i>The Art of Computer Programming,
 * Volume 2: Seminumerical Algorithms. Third Edition.</i> Boston, MA:
 * Addison-Wesley. Given two normalised floating-point numbers <i>a</i> =
 * <i>s</i> &times; 2<sup><i>e</i></sup> and <i>b</i> = <i>t</i> &times;
 * 2<sup><i>f</i></sup>, these equations define the following operations:
 * </p>
 * 
 * <ol start="21">
 * 
 * <li><code>lt(</code><i>a</i><code>, </code><i>b</i> <code>)</code> iff
 * <i>b</i><code> - </code><i>a</i> <code> &gt; epsilon * </code>
 * max(2<sup><i>e</i></sup>, 2<sup><i>f</i></sup>)</li>
 * 
 * <li><code>eq(</code><i>a</i><code>, </code><i>b</i><code>)</code> iff
 * <code>Math.abs(</code><i>b</i><code> - </code><i>a</i>
 * <code>) &lt;= epsilon * </code> max(2<sup><i>e</i></sup>,
 * 2<sup><i>f</i></sup>)</li>
 * 
 * <li><code>gt(</code> <i>a</i><code>, </code><i>b</i><code>)</code> iff
 * <i>a</i><code> - </code> <i>b</i><code> &gt; epsilon * </code>
 * max(2<sup><i>e</i></sup>, 2<sup><i>f</i></sup>)</li>
 * 
 * <li><code>EQ(</code><i>a</i><code>, </code> <i>b</i><code>)</code> iff
 * <code>Math.abs(</code><i>b</i><code> - </code><i>a</i>
 * <code>) &lt;= epsilon * </code> min(2<sup><i>e</i></sup>,
 * 2<sup><i>f</i></sup>)</li>
 * 
 * </ol>
 * 
 * <p>
 * In this implementation, these equations are rearranged to avoid catastrophic
 * cancellation. The methods NE(), GE() and LE() are provided to complement
 * EQ().
 * </p>
 * 
 * <p>
 * On p. 234 (ibid.), Knuth shows that certain results hold when
 * <code>epsilon</code> is in the range [2<i>&epsilon;<i><sub>0</sub>/(1 -
 * &frac12;<i>&epsilon;</i><sub>0</sub>)<sup>2</sup>, 1[; where
 * <i>&epsilon</i><sub>0</sub> = 2<sup>1-<i>p</i></sup>, and <i>p</i> is the
 * number of bits in the significand: 24 for single and 53 for double precision
 * floating point numbers. The implementation enforces this range when setting
 * <code>epsilon</code>, with the exception that 0.0 is also a valid value to
 * allow equivalence with the language default case. The minimum values for
 * epsilon work out at 2.384186e-7 for single precision, and
 * 4.440892098500627e-16 for double.
 * <p>
 * 
 * @author Gary Polhill
 */
public class KnuthFCmp extends AbstractFloatingPointComparison {
  /**
   * Epsilon to use for double precision comparisons
   */
  private final double depsilon;

  /**
   * Epsilon to use for single precision comparisons
   */
  private final float fepsilon;

  /**
   * <i>&epsilon;</i><sub>0</sub> for double precision
   */
  public static final double DOUBLE_EPSILON_0 = Math.scalb(1.0, -52);

  /**
   * <i>&epsilon;</i><sub>0</sub> for single precision
   */
  public static final float FLOAT_EPSILON_0 = Math.scalb(1.0F, -23);

  /**
   * Minimum (inclusive) value for double precision epsilon
   */
  public static final double DOUBLE_EPSILON_MIN =
    (2.0 * DOUBLE_EPSILON_0) / ((1.0 - (0.5 * DOUBLE_EPSILON_0)) * (1.0 - (0.5 * DOUBLE_EPSILON_0)));

  /**
   * Minimum (inclusive) value for single precision epsilon
   */
  public static final float FLOAT_EPSILON_MIN =
    (2.0F * FLOAT_EPSILON_0) / ((1.0F - (0.5F * FLOAT_EPSILON_0)) * (1.0F - (0.5F * FLOAT_EPSILON_0)));

  /**
   * Maximum (non-inclusive) value for double precision epsilon
   */
  public static final double DOUBLE_EPSILON_MAX = 1.0;

  /**
   * Maximum (non-inclusive) value for single precision epsilon
   */
  public static final float FLOAT_EPSILON_MAX = 1.0F;

  /**
   * Default value to use for double precision epsilon
   */
  public static final double DEFAULT_DOUBLE_EPSILON = DOUBLE_EPSILON_MIN;

  /**
   * Default value to use for single precision epsilon
   */
  public static final float DEFAULT_FLOAT_EPSILON = FLOAT_EPSILON_MIN;

  /**
   * <!-- validEpsilon -->
   * 
   * Check a proposed double precision epsilon for validity. An epsilon is valid
   * if it is in the range given above, is zero, and is not infinite or NaN. If
   * a KnuthFCmp is initialised with an invalid epsilon, a
   * FloatingPointComparison (runtime) exception is thrown.
   * 
   * @param epsilon
   * @return <code>true</code> if the double precision epsilon is valid
   */
  public static final boolean validEpsilon(double epsilon) {
    if(Double.isInfinite(epsilon) || Double.isNaN(epsilon)) return false;
    if(epsilon == 0.0) return true;
    return epsilon >= DOUBLE_EPSILON_MIN && epsilon < DOUBLE_EPSILON_MAX;
  }

  /**
   * <!-- validEpsilon -->
   * 
   * Check a proposed single precision epsilon for validity.
   * 
   * @param epsilon
   * @return <code>true</code> if the single precision epsilon is valid
   */
  public static final boolean validEpsilon(float epsilon) {
    if(Float.isInfinite(epsilon) || Float.isNaN(epsilon)) return false;
    if(epsilon == 0.0F) return true;
    return epsilon >= FLOAT_EPSILON_MIN && epsilon < FLOAT_EPSILON_MAX;
  }

  /**
   * Use default values for epsilons.
   */
  public KnuthFCmp() {
    depsilon = DEFAULT_DOUBLE_EPSILON;
    fepsilon = DEFAULT_FLOAT_EPSILON;
  }

  /**
   * Set both single and double precision epsilons to the same (double
   * precision) value. The if the double precision epsilon is non-zero, then the
   * casted single precision value must also be non-zero.
   * 
   * @param epsilon
   */
  public KnuthFCmp(double epsilon) {
    this(epsilon, (float)epsilon);
    if(epsilon > 0.0 && (float)epsilon == 0.0F) {
      throw new FloatingPointComparisonException("Epsilon " + epsilon
        + " is too small to be used as a single-precision epsilon");
    }
  }

  /**
   * Set both single and double precision epsilons to the same (single
   * precision) value
   * 
   * @param epsilon
   */
  public KnuthFCmp(float epsilon) {
    this((double)epsilon, epsilon);
  }

  /**
   * Allow non-default values for double and float epsilons to be set.
   * 
   * @param depsilon
   * @param fepsilon
   */
  public KnuthFCmp(double depsilon, float fepsilon) {
    if(!validEpsilon(depsilon)) {
      throw new FloatingPointComparisonException("Double-precision epsilon " + depsilon
        + " is infinite, NaN, not in the range [" + DOUBLE_EPSILON_MIN + ", " + DOUBLE_EPSILON_MAX + "[, or not 0.0");
    }
    if(!validEpsilon(fepsilon)) {
      throw new FloatingPointComparisonException("Single-precision epsilon " + fepsilon
        + " is infinite, NaN, not in the range [" + FLOAT_EPSILON_MIN + ", " + FLOAT_EPSILON_MAX + "[, or not 0.0");
    }
    this.depsilon = depsilon;
    this.fepsilon = fepsilon;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(double, double)
   */
  @Override
  public boolean eq(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    return b <= a + tolerance && a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(float, float)
   */
  @Override
  public boolean eq(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    return b <= a + tolerance && a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#EQ(double, double)
   */
  @Override
  public boolean EQ(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(b)) : Math.scalb(depsilon, Math.getExponent(a));
    return b <= a + tolerance && a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#EQ(float, float)
   */
  @Override
  public boolean EQ(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(b)) : Math.scalb(fepsilon, Math.getExponent(a));
    return b <= a + tolerance && a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ge(double, double)
   */
  @Override
  public boolean ge(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    return b <= a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ge(float, float)
   */
  @Override
  public boolean ge(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    return b <= a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#GE(double, double)
   */
  @Override
  public boolean GE(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(b)) : Math.scalb(depsilon, Math.getExponent(a));
    return b <= a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#GE(float, float)
   */
  @Override
  public boolean GE(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(b)) : Math.scalb(fepsilon, Math.getExponent(a));
    return b <= a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(double, double)
   */
  @Override
  public boolean gt(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    return a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(float, float)
   */
  @Override
  public boolean gt(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    return a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#le(double, double)
   */
  @Override
  public boolean le(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    return a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#le(float, float)
   */
  @Override
  public boolean le(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    return a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#LE(double, double)
   */
  @Override
  public boolean LE(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(b)) : Math.scalb(depsilon, Math.getExponent(a));
    return a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#LE(float, float)
   */
  @Override
  public boolean LE(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(b)) : Math.scalb(fepsilon, Math.getExponent(a));
    return a <= b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(double, double)
   */
  @Override
  public boolean lt(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    return b > a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(float, float)
   */
  @Override
  public boolean lt(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    return b > a + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ne(double, double)
   */
  @Override
  public boolean ne(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    return b > a + tolerance || a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ne(float, float)
   */
  @Override
  public boolean ne(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    return b > a + tolerance || a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#NE(double, double)
   */
  @Override
  public boolean NE(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(b)) : Math.scalb(depsilon, Math.getExponent(a));
    return b > a + tolerance || a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#NE(float, float)
   */
  @Override
  public boolean NE(float a, float b) {
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(b)) : Math.scalb(fepsilon, Math.getExponent(a));
    return b > a + tolerance || a > b + tolerance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#compare(double,
   * double)
   */
  @Override
  public int compare(double a, double b) {
    double tolerance = a > b ? Math.scalb(depsilon, Math.getExponent(a)) : Math.scalb(depsilon, Math.getExponent(b));
    if(a > b + tolerance) return 1;
    else if(b > a + tolerance) return -1;
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
    float tolerance = a > b ? Math.scalb(fepsilon, Math.getExponent(a)) : Math.scalb(fepsilon, Math.getExponent(b));
    if(a > b + tolerance) return 1;
    else if(b > a + tolerance) return -1;
    else
      return 0;
  }
}
