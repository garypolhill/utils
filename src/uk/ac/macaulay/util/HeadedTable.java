/*
 * uk.ac.macaulay.util: HeadedTable.java Copyright (C) 2009 Macaulay Institute
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * HeadedTable
 * 
 * A table with headings for each column.
 * 
 * @author Gary Polhill
 */
public class HeadedTable<T> extends Table<T> {
  /**
   * The column headings
   */
  String[] colHeadings;

  /**
   * Map of column headings to column numbers
   */
  private Map<String, Integer> col2ix;

  /**
   * Build a HeadedTable. The number of column headings is assumed to be the
   * same as the number of columns.
   * 
   * @param rows The number of rows
   * @param colHeadings The column headings
   */
  public HeadedTable(int rows, String[] colHeadings) {
    super(rows, colHeadings.length);
    this.colHeadings = colHeadings;
    initCol2Ix();
  }

  /**
   * Build a HeadedTable from an existing table using default column names.
   * These are as commonly used in spreadsheet applications, but without
   * limitations on column numbers: the letters A-Z for columns 1-26, then AA-AZ
   * for 27-52, then BA-BZ, ... ZZ-AZ, AAA-AAZ, ... AAAA-AAAZ, ... etc.
   * 
   * @param table The existing table.
   */
  public HeadedTable(Table<T> table) {
    super(table);
    colHeadings = new String[ncols];
    String[] alphabet =
      new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
        "T", "U", "V", "W", "X", "Y", "Z" };
    for(int col = 0; col < ncols; col++) {
      StringBuffer buf = new StringBuffer(alphabet[col % alphabet.length]);
      for(int colix = (col - (col % alphabet.length)) / alphabet.length; colix > 0; colix =
        (colix - (colix % alphabet.length)) / alphabet.length) {
        buf.insert(0, alphabet[colix % alphabet.length]);
      }
      colHeadings[col] = buf.toString();
    }
    initCol2Ix();
  }

  /**
   * <!-- initCol2Ix -->
   * 
   * Initialise the col2ix map
   */
  private void initCol2Ix() {
    col2ix = new HashMap<String, Integer>();
    for(int col = 0; col < ncols; col++) {
      col2ix.put(colHeadings[col], new Integer(col));
    }
  }

  /**
   * <!-- getColumnHeadingsSet -->
   * 
   * Get the column headings as a Set. Will throw an IllegalStateException if
   * the column headings contain a repeated element (i.e. a column heading is
   * used more than once).
   * 
   * @return The column headings
   */
  public Set<String> getColumnHeadingsSet() {
    Set<String> colSet = new HashSet<String>();
    for(int col = 0; col < ncols; col++) {
      if(colSet.contains(colHeadings[col])) throw new IllegalStateException(colHeadings[col]);
      colSet.add(colHeadings[col]);
    }
    return colSet;
  }

  /**
   * <!-- getColumnHeadings -->
   * 
   * @return The column headings
   */
  public String[] getColumnHeadings() {
    return colHeadings;
  }

  /**
   * <!-- atRC -->
   * 
   * Get the entry at the specified row with the given column heading. This
   * method will throw an IllegalArgumentException if the column heading is not
   * recognised.
   * 
   * @param row The row number
   * @param colHeading The column heading
   * @return The entry
   */
  public T atRC(int row, String colHeading) {
    if(!col2ix.containsKey(colHeading)) throw new IllegalArgumentException(colHeading);
    return super.atRC(row, col2ix.get(colHeading));
  }

  /**
   * <!-- findRow -->
   * 
   * Find the first row where the column headed <code>colHeading</code> has
   * value <code>value</code>
   * 
   * @param colHeading The heading of the column in which to search
   * @param value The value to look for
   * @return The first row having the value in that column, or -1 if not found.
   */
  public int findRow(String colHeading, T value) {
    if(!col2ix.containsKey(colHeading)) throw new IllegalArgumentException(colHeading);
    return super.findRow(col2ix.get(colHeading), value);
  }

  /**
   * <!-- findRow -->
   * 
   * Find the first row starting at <code>startRow</code> where the column
   * headed <code>colHeading</code> has value <code>value</code>
   * 
   * @param startRow The first row at which to start searching
   * @param colHeading The heading of the column in which to search
   * @param value The value to look for
   * @return The row, or -1 if the value isn't found
   */
  public int findRow(int startRow, String colHeading, T value) {
    if(!col2ix.containsKey(colHeading)) throw new IllegalArgumentException(colHeading);
    return super.findRow(startRow, col2ix.get(colHeading), value);
  }

  /**
   * <!-- atRC -->
   * 
   * Set the entry at the specified row with the given column heading. This
   * method will throw an IllegalArgumentException if the column heading is not
   * recognised.
   * 
   * @param row The row number
   * @param colHeading The column heading
   * @param entry The entry
   */
  public void atRC(int row, String colHeading, T entry) {
    if(!col2ix.containsKey(colHeading)) throw new IllegalArgumentException(colHeading);
    super.atRC(row, col2ix.get(colHeading), entry);
  }

  /**
   * <!-- getRow -->
   * 
   * Get a row as a map of column heading to entry
   * 
   * @param row The row number to get
   * @return The map
   */
  public Map<String, T> getRow(int row) {
    Map<String, T> data = new HashMap<String, T>();
    for(int col = 0; col < ncols; col++) {
      data.put(colHeadings[col], atRC(row, col));
    }
    return data;
  }

  /**
   * <!-- getData -->
   * 
   * Get the whole table as an ArrayList of maps of column heading to entry.
   * 
   * @return All the data in the table
   */
  public ArrayList<Map<String, T>> getData() {
    ArrayList<Map<String, T>> data = new ArrayList<Map<String, T>>(nrows);
    for(int row = 0; row < nrows; row++) {
      data.add(row, getRow(row));
    }
    return data;
  }
}
