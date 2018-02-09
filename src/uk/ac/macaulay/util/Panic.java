/* uk.ac.macaulay.util: Panic.java
 *
 * Copyright (C) 2009  Macaulay Institute
 *
 * This file is part of utils.
 *
 * utils is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * utils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with utils. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Contact information:
 *   Gary Polhill
 *   Macaulay Institute, Craigiebuckler, Aberdeen. AB15 8QH. UK.
 *   g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

/**
 * Panic
 *
 * An exception that "should not occur"
 *
 * @author Gary Polhill
 */
public final class Panic extends Error {
  /**
   * Serialisation ID
   */
  private static final long serialVersionUID = 6665158453191033400L;

  /**
   * Constructor for a Panic. Creates an exception which can then be used to get
   * the file and line from a stack trace
   */
  public Panic() {
    this(new Exception());
  }
  
  /**
   * Private constructor. Takes the exception created by the default constructor,
   * and uses the stack trace to get the line number and file where the panic
   * occurred.
   * 
   * @param e Exception created by the stack trace
   */
  private Panic(Exception e) {
    super("Panic! File: " + e.getStackTrace()[1].getFileName()
        + ", Line: " + e.getStackTrace()[1].getLineNumber());
  }

}
