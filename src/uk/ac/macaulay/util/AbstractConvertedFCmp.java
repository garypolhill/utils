/*
 * uk.ac.macaulay.util: AbstractConvertedFCmp.java Copyright (C) 2009 Macaulay
 * Institute
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
 * AbstractConvertedFCmp
 * 
 * This is a family of floating point comparison algorithms that use standard
 * operators for the comparison, but perform some sort of conversion on the
 * operands before doing so.
 * 
 * @author Gary Polhill
 */
public abstract class AbstractConvertedFCmp extends AbstractFloatingPointComparison {
  protected abstract double convert(double value);

  protected abstract float convert(float value);

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(double, double)
   */
  @Override
  public boolean eq(double a, double b) {
    return convert(a) == convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#eq(float, float)
   */
  @Override
  public boolean eq(float a, float b) {
    return convert(a) == convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ge(double, double)
   */
  @Override
  public boolean ge(double a, double b) {
    return convert(a) >= convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ge(float, float)
   */
  @Override
  public boolean ge(float a, float b) {
    return convert(a) >= convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(double, double)
   */
  @Override
  public boolean gt(double a, double b) {
    return convert(a) > convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#gt(float, float)
   */
  @Override
  public boolean gt(float a, float b) {
    return convert(a) > convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#le(double, double)
   */
  @Override
  public boolean le(double a, double b) {
    return convert(a) <= convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#le(float, float)
   */
  @Override
  public boolean le(float a, float b) {
    return convert(a) <= convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(double, double)
   */
  @Override
  public boolean lt(double a, double b) {
    return convert(a) < convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#lt(float, float)
   */
  @Override
  public boolean lt(float a, float b) {
    return convert(a) < convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ne(double, double)
   */
  @Override
  public boolean ne(double a, double b) {
    return convert(a) != convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#ne(float, float)
   */
  @Override
  public boolean ne(float a, float b) {
    return convert(a) != convert(b);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#compare(double,
   * double)
   */
  @Override
  public int compare(double a, double b) {
    return Double.compare(convert(a), convert(b));
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.macaulay.util.AbstractFloatingPointComparison#compare(float,
   * float)
   */
  @Override
  public int compare(float a, float b) {
    return Float.compare(convert(a), convert(b));
  }

}
