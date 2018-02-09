/*
 * uk.ac.macaulay.util: CSVWriter.java
 * 
 * Copyright (C) 2011 Macaulay Institute
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
import java.io.PrintWriter;

/**
 * <!-- CSVWriter -->
 * 
 * Utility class for writing CSV files
 * 
 * @author Gary Polhill
 */
public class CSVWriter {
  /**
   * Stream to write the CSV file to
   */
  private PrintWriter fp;

  /**
   * Boolean confirming that the object is in a state where the CSV file is
   * writable
   */
  private boolean writable;

  /**
   * The number of the next row to be written in (starts at 0)
   */
  private int nextRow;

  /**
   * The number of the next column to be written in (starts at 0)
   */
  private int nextColumn;

  /**
   * The minimum number of columns in any row (default 0)
   */
  private int padColumns;

  /**
   * The minimum number of rows in the file
   */
  private int padRows;

  /**
   * Create a new CSVWriter, writing to the file
   * 
   * @param filename Name of file to write to
   * @throws IOException
   */
  public CSVWriter(String filename) throws IOException {
    this(filename, 0);
  }

  /**
   * Create a new CSVWriter, writing to a file, with a specified minimum number
   * of columns per line
   * 
   * @param filename Name of file to write to
   * @param padColumns Minimum number of columns
   * @throws IOException
   */
  public CSVWriter(String filename, int padColumns) throws IOException {
    this(filename, padColumns, 0);
  }

  /**
   * Create a new CSVWriter, writing to a file, with a specified minimum number
   * of columns per line and rows per file
   * 
   * @param filename Name of file to write to
   * @param padColumns Minimum number of columns
   * @param padRows Minimum number of rows
   * @throws IOException
   */
  public CSVWriter(String filename, int padColumns, int padRows) throws IOException {
    fp = FileOpener.write(filename);
    writable = true;
    nextRow = 0;
    nextColumn = 0;
    this.padColumns = padColumns;
    this.padRows = padRows;
  }

  /**
   * <!-- write -->
   * 
   * Write a 2D array of {@link java.lang.Object}s to the file
   * 
   * @param array Array of <code>Object</code>s
   */
  public void write(Object[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>boolean</code> array writing
   * 
   * @param array
   */
  public void write(boolean[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>char</code> array writing
   * 
   * @param array
   */
  public void write(char[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>short</code> array writing
   * 
   * @param array
   */
  public void write(short[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>int</code> array writing
   * 
   * @param array
   */
  public void write(int[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>long</code> array writing
   * 
   * @param array
   */
  public void write(long[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>float</code> array writing
   * 
   * @param array
   */
  public void write(float[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Convenience method for 2D <code>double</code> array writing
   * 
   * @param array
   */
  public void write(double[][] array) {
    for(int r = 0; r < array.length; r++) {
      for(int c = 0; c < array[r].length; c++) {
        writeCell(array[r][c]);
      }
      writeEndRow();
    }
  }

  /**
   * <!-- write -->
   * 
   * Write a {@link Table} to the CSV file
   * 
   * @param table
   */
  public void write(Table<?> table) {
    for(int row = 0; row < table.nrows(); row++) {
      for(int col = 0; col < table.ncols(); col++) {
        writeCell(table.atRC(row, col));
      }
      writeEndRow();
    }
  }

  /**
   * <!-- writeRC -->
   * 
   * Write a {@link Table} to the CSV file in the format
   * <code>row,column,entry</code> with one entry per line.
   * 
   * @param table
   */
  public void writeRC(Table<?> table) {
    for(int row = 0; row < table.nrows(); row++) {
      for(int col = 0; col < table.ncols(); col++) {
        writeCell(row);
        writeCell(col);
        writeCell(table.atRC(row, col));
        writeEndRow();
      }
    }
  }

  /**
   * <!-- writeXY -->
   * 
   * Write a {@link Table} to the CSV file in the format
   * <code>column,row,entry</code> with one entry per line.
   * 
   * @param table
   */
  public void writeXY(Table<?> table) {
    for(int col = 0; col < table.ncols(); col++) {
      for(int row = 0; row < table.nrows(); row++) {
        writeCell(col);
        writeCell(row);
        writeCell(table.atRC(row, col));
        writeEndRow();
      }
    }
  }

  /**
   * <!-- writeXY -->
   * 
   * Write a {@link GISRaster} to the CSV file in the format
   * <code>X,Y,entry</code> with one entry per line.
   * 
   * @param raster
   */
  public void writeXY(GISRaster<?> raster) {
    for(int col = 0; col < raster.ncols(); col++) {
      for(int row = 0; row < raster.nrows(); row++) {
        writeCell(raster.convertX(col));
        writeCell(raster.convertY(row));
        writeCell(raster.atRC(row, col));
        writeEndRow();
      }
    }

  }

  /**
   * <!-- writeCell -->
   * 
   * Write a cell to the CSV file.
   * 
   * @param data
   */
  public <T> void writeCell(T data) {
    if(!writable) throw new Bug();
    if(nextColumn > 0) fp.print(",");
    if(data != null) fp.print(getCSVCellString(data.toString()));
    nextColumn++;
  }

  /**
   * <!-- writeEndRow -->
   * 
   * Write the end of a row to the CSV file.
   */
  public void writeEndRow() {
    if(!writable) throw new Bug();
    if(padColumns > 0) {
      while(nextColumn < padColumns) {
        writeCell(null);
      }
    }
    fp.println();
    nextRow++;
    nextColumn = 0;
  }

  /**
   * <!-- close -->
   * 
   * Close the CSV file. After this method has been called, the CSV file can no
   * longer be written to. (A {@link Bug} is thrown if any attempt is made.)
   */
  public void close() {
    if(padRows > 0) {
      while(nextRow < padRows) {
        writeEndRow();
      }
    }
    fp.close();
    writable = false;
  }

  /**
   * <!-- finalize -->
   * 
   * Closes the CSV file if the object no longer has any references
   * 
   * @see java.lang.Object#finalize()
   */
  protected void finalize() {
    close();
  }

  /**
   * <!-- getCSVCellString -->
   * 
   * <p>
   * Format a {@link java.lang.String} in CSV format. The rules are:
   * </p>
   * 
   * <ul>
   * <li>If the <code>String</code> contains neither a comma nor a double quote,
   * return the <code>String</code> as is.</li>
   * <li>Else if the <code>String</code> does not contain a double quote, return
   * the <code>String</code> surrounded by double quotes.</li>
   * <li>Else convert the String such that all double quote characters are
   * replaced by two double quote characters, and return the result surrounded
   * by double quotes.</li>
   * </ul>
   * 
   * <p>
   * Thus <code>A</code> becomes <code>A</code>, <code>B,C</code> becomes
   * <code>"B,C"</code>, and <code>"D"</code> becomes <code>"""D"""</code>.
   * </p>
   * 
   * @param data The <code>String</code> to convert
   * @return CSV formatted <code>String</code>
   */
  public static String getCSVCellString(String data) {
    if(!data.contains(",") && !data.contains("\"")) {
      return data;
    }
    else if(data.contains("\"")) {
      StringBuffer buff = new StringBuffer();
      buff.append("\"");

      char[] chrs = data.toCharArray();

      for(int i = 0; i < chrs.length; i++) {
        buff.append(chrs[i]);
        if(chrs[i] == '\"') {
          buff.append(chrs[i]);
        }
      }

      buff.append("\"");
      return buff.toString();
    }
    else
      return new String("\"" + data + "\"");
  }
}
