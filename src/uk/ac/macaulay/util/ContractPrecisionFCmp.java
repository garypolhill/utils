/*
 * uk.ac.macaulay.util: ContractPrecisionFcmp.java
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
 * ContractPrecisionFcmp
 * 
 * Compare floating point numbers by converting double to float before making
 * the comparison. This method will throw runtime exceptions if attempts are
 * made to directly compare floats.
 * 
 * @author Gary Polhill
 */
public class ContractPrecisionFCmp extends AbstractConvertedFCmp {
  protected double convert(double value) {
    float contractedPrecision = (float)value;
    return (double)contractedPrecision;
  }
  
  protected float convert(float value) {
    throw new FloatingPointComparisonException("Can't contract float datatype: " + value);
  }
}
