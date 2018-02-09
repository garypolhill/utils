/*
 * uk.ac.macaulay.util: FileFormatException.java
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

import java.io.IOException;

/**
 * <!-- FileFormatException -->
 * 
 * An exception caused when a file does not conform to an expected format.
 * 
 * @author Gary Polhill
 */
public class FileFormatException extends IOException {
  /**
   * Serialisation number
   */
  private static final long serialVersionUID = 7084806136332245023L;

  /**
   * Basic constructor
   * 
   * @param filename The name of the file that does not conform to format
   * @param format The format it is expected to conform to
   * @param expecting What was expected to be found (no quotes put round this in
   *          the message to allow for generic descriptions)
   * @param found What was actually found (this will be put in quotes in the
   *          message, unless null, in which case end of file is assumed)
   */
  public FileFormatException(String filename, String format, String expecting, String found) {
    super("File " + filename + " does not conform to format " + format + ": expecting " + expecting + ", found "
      + (found == null ? "end of file" : "\"" + found + "\""));
  }

  /**
   * Constructor when line number is known
   * 
   * @param filename The name of the file that does not conform to format
   * @param format The format it is expected to conform to
   * @param expecting What was expected to be found (no quotes put round this in
   *          the message to allow for generic descriptions)
   * @param found What was actually found (this will be put in quotes in the
   *          message, unless null, in which case end of file is assumed)
   * @param line The line number where the problem occurred.
   */
  public FileFormatException(String filename, String format, String expecting, String found, int line) {
    super("File " + filename + " does not conform to format " + format + " at line " + line + ": expecting "
      + expecting + ", found " + (found == null ? "end of file" : "\"" + found + "\""));
  }

  /**
   * Constructor when accurate file position is known
   * 
   * @param filename The name of the file that does not conform to format
   * @param format The format it is expected to conform to
   * @param expecting What was expected to be found (no quotes put round this in
   *          the message to allow for generic descriptions)
   * @param found What was actually found (this will be put in quotes in the
   *          message, unless null, in which case end of file is assumed)
   * @param line The line number where the problem occurred.
   * @param ch The character number where the problem occurred.
   */
  public FileFormatException(String filename, String format, String expecting, String found, int line, int ch) {
    super("File " + filename + " does not conform to format " + format + " at line " + line + ", character " + ch
      + ": expecting " + expecting + ", found " + (found == null ? "end of file" : "\"" + found + "\""));
  }
}
