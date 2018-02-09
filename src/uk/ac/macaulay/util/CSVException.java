/*
 * uk.ac.macaulay.util: CSVException.java
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
 * CSVException
 * 
 * Exception for the CSVReader class
 * 
 * @author Gary Polhill
 */
public class CSVException extends Exception {
  /**
   * Serialisation thing
   */
  private static final long serialVersionUID = -3283281942437471968L;

  /**
   * Text describing what the CSVReader expected to find
   */
  String expected;

  /**
   * Text indicating what the CSVReader did find
   */
  String found;

  /**
   * Row number in file, if available (assumed to be present if the filename is
   * given)
   */
  int row;

  /**
   * Column number (measured in cells, not characters) where error occurred
   */
  int col;

  /**
   * Name of file being processed
   */
  String filename = null;

  /**
   * Constructor for the exception
   * 
   * @param expected What was expected--must be non-null for a meaningful
   *          message
   * @param found What was found--must be non-null for a meaningful message
   * @param filename The name of the file--null if not appropriate
   * @param row The row numbe--must be provided for a meaningful message if the
   *          filename is not null
   * @param col The cell number in the line where the error occurred--must be
   *          provided for a meaningful message
   */
  public CSVException(String expected, String found, String filename, int row, int col) {
    this.expected = expected;
    this.found = found;
    this.filename = filename;
    this.row = row;
    this.col = col;
  }

	/**
   * <!-- getMessage -->
   *
   * Return a message--two options are available depending on whether the filename is null.
   *
   * @see java.lang.Throwable#getMessage()
   */
  public String getMessage() {
    if(filename == null) {
      return "CSV error at column " + col + ": expected \"" + expected + "\", found \"" + found + "\"";
    }
    return "Error in CSV file " + filename + " at row " + row + ", column " + col + ": expected \"" + expected
      + "\", found \"" + found + "\"";
  }
}
