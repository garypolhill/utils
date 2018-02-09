/*
 * uk.ac.macaulay.util: Table.java
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Table
 * 
 * A table is a datastructure containing typed data in a 2D structure of cells,
 * accessed by co-ordinates.
 * 
 * @author Gary Polhill
 */
public class Table<T> implements Iterable<T> {
  /**
   * The cells of the table
   */
  private ArrayList<ArrayList<T>> cells;

  /**
   * The number of rows in the table
   */
  int nrows;

  /**
   * The number of columns in the table
   */
  int ncols;

  /**
   * Construct a Table by specifying the number of rows and columns it contains.
   * The table will be initialised to null in each cell.
   * 
   * @param nrows The number of rows in the table
   * @param ncols The number of columns in the table
   */
  public Table(int nrows, int ncols) {
    this.nrows = nrows;
    this.ncols = ncols;

    synchronized(this) {
      cells = new ArrayList<ArrayList<T>>(nrows);
      for(int i = 0; i < nrows; i++) {
        ArrayList<T> row = new ArrayList<T>(ncols);
        cells.add(i, row);
        for(int j = 0; j < ncols; j++) {
          row.add(j, null);
        }
      }
    }
  }

  /**
   * Construct a table as a copy of another table
   * 
   * @param table The table to copy
   */
  public Table(Table<T> table) {
    this(table.nrows, table.ncols);

    synchronized(this) {
      for(int i = 0; i < nrows; i++) {
        for(int j = 0; j < ncols; j++) {
          atRC(i, j, table.atRC(i, j));
        }
      }
    }
  }

  /**
   * <!-- nrows -->
   * 
   * @return The number of rows in the table
   */
  public int nrows() {
    return nrows;
  }

  /**
   * <!-- ncols -->
   * 
   * @return The number of columns in the table
   */
  public int ncols() {
    return ncols;
  }

  /**
   * <!-- atXY -->
   * 
   * Access data in the table as though the cells were indexed by (x, y)
   * co-ordinates--x for columns, y for rows, but with (0, 0) at the bottom left
   * 
   * @param x The column
   * @param y The row
   * @return The possibly null entry at column x, row nrows - y - 1
   */
  public T atXY(int x, int y) {
    if(x < 0 || x >= ncols || y < 0 || y >= nrows) throw new ArrayIndexOutOfBoundsException();
    return cells.get((nrows - y) - 1).get(x);
  }

  /**
   * <!-- atXFlipY -->
   * 
   * Access data in the table as though the cells were indexed by (x, y)
   * co-ordinates--x for columns, y for rows, with (0, 0) at the top left (like
   * row, column access)
   * 
   * @param x The column
   * @param y The row
   * @return The possibly null entry at column x, row y
   */
  public T atXFlipY(int x, int y) {
    if(x < 0 || x >= ncols || y < 0 || y >= nrows) throw new ArrayIndexOutOfBoundsException();
    return cells.get(y).get(x);
  }

  /**
   * <!-- atRC -->
   * 
   * Access data in the table by row and column, with row 0 at the top, and
   * column 0 on the left.
   * 
   * @param row The row
   * @param col The column
   * @return
   */
  public T atRC(int row, int col) {
    if(col < 0 || col >= ncols || row < 0 || row >= nrows) throw new ArrayIndexOutOfBoundsException();
    return cells.get(row).get(col);
  }

  /**
   * <!-- findRow -->
   * 
   * Find the first row number where the entry in the specified column has the
   * stated value.
   * 
   * @param col The column to search
   * @param value The value to find in the column
   * @return The first row having the value in the column, or -1 if none.
   */
  public synchronized int findRow(int col, T value) {
    return findRow(0, col, value);
  }

  /**
   * <!-- findRow -->
   * 
   * Find the first row number starting at startRow where the entry in the
   * specified column has the stated value.
   * 
   * @param startRow The first row to start searching in
   * @param col The column to search
   * @param value The value to find in the column
   * @return The first row &gt;= <code>startRow</code> having <code>value</code>
   *         in column <code>col</code>
   */
  public synchronized int findRow(int startRow, int col, T value) {
    for(int row = startRow; row < nrows; row++) {
      if(atRC(row, col).equals(value)) return row;
    }
    return -1;
  }

  /**
   * <!-- atRC2C -->
   * 
   * Return a subsection of a row from the specified column start and end
   * points, inclusively.
   * 
   * @param row The row to select columns from
   * @param colstart The start column
   * @param colend The end column
   * @return The requested subsection of the row
   */
  public ArrayList<T> atRC2C(int row, int colstart, int colend) {
    ArrayList<T> arr = new ArrayList<T>((colend - colstart) + 1);

    for(int i = colstart; i <= colend; i++) {
      arr.add(i - colstart, atRC(row, i));
    }

    return arr;
  }

  /**
   * <!-- atXY -->
   * 
   * Set the value of a cell, accessed by (x, y) co-ordinates, with (0, 0) at
   * the bottom left
   * 
   * @param x The x co-ordinate
   * @param y The y co-ordinate
   * @param value The value to set
   */
  public synchronized void atXY(int x, int y, T value) {
    if(x < 0 || x >= ncols || y < 0 || y >= nrows) throw new ArrayIndexOutOfBoundsException();
    cells.get((nrows - y) - 1).add(x, value);
  }

  /**
   * <!-- atXFlipY -->
   * 
   * Set the value of a cell, accessed by (x, y) co-ordinates, with (0, 0) at
   * the top left
   * 
   * @param x The x co-ordinate
   * @param y The y co-ordinate
   * @param value The value to set
   */
  public synchronized void atXFlipY(int x, int y, T value) {
    if(x < 0 || x >= ncols || y < 0 || y >= nrows) throw new ArrayIndexOutOfBoundsException();
    cells.get(y).add(x, value);
  }

  /**
   * <!-- atRC -->
   * 
   * Set the value of a cell, accessed by row and column, with row 0, column 0
   * at the top left
   * 
   * @param row The row
   * @param col The column
   * @param value The value to set
   */
  public synchronized void atRC(int row, int col, T value) {
    if(col < 0 || col >= ncols || row < 0 || row >= nrows) throw new ArrayIndexOutOfBoundsException();
    cells.get(row).add(col, value);
  }

  /**
   * <!-- flatten -->
   * 
   * @return The table as a 1D data structure, row by row
   */
  public ArrayList<T> flatten() {
    ArrayList<T> flattened = new ArrayList<T>(nrows * ncols);

    int i = 0;
    synchronized(this) {
      for(int r = 0; r < nrows; r++) {
        for(int c = 0; c < ncols; c++) {
          flattened.add(i, cells.get(r).get(c));
          i++;
        }
      }
    }
    return flattened;
  }

  /**
   * <!-- asIntegerTable -->
   * 
   * @return This table converted to a table of integers
   * @throws NumberFormatException
   */
  public Table<Integer> asIntegerTable() {
    Table<Integer> table = new Table<Integer>(nrows, ncols);
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj != null) table.atRC(r, c, Integer.parseInt(obj.toString()));
      }
    }
    return table;
  }

  /**
   * <!-- asLongTable -->
   *
   * @return This table converted to a table of longs
   * @throws NumberFormatException
   */
  public Table<Long> asLongTable() {
    Table<Long> table = new Table<Long>(nrows, ncols);
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj != null) table.atRC(r, c, Long.parseLong(obj.toString()));
      }
    }
    return table;
  }

  /**
   * <!-- asFloatTable -->
   *
   * @return This table converted to a table of floats
   * @throws NumberFormatException
   */
  public Table<Float> asFloatTable() {
    Table<Float> table = new Table<Float>(nrows, ncols);
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj != null) table.atRC(r, c, Float.parseFloat(obj.toString()));
      }
    }
    return table;
  }

  /**
   * <!-- asDoubleTable -->
   *
   * @return This table converted to a table of doubles
   * @throws NumberFormatException
   */
  public Table<Double> asDoubleTable() {
    Table<Double> table = new Table<Double>(nrows, ncols);
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj != null) table.atRC(r, c, Double.parseDouble(obj.toString()));
      }
    }
    return table;
  }

  /**
   * <!-- asStringTable -->
   *
   * @return This table converted to a table of strings
   */
  public Table<String> asStringTable() {
    Table<String> table = new Table<String>(nrows, ncols);
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        table.atRC(r, c, atRC(r, c).toString());
      }
    }
    return table;
  }

  /**
   * <!-- areAllParseableInteger -->
   * 
   * @return <code>true</code> if all members of the table are a parseable
   *         integer or null
   */
  public boolean areAllParseableInteger() {
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj == null) continue;
        try {
          Integer.parseInt(obj.toString());
        }
        catch(NumberFormatException e) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * <!-- areAllParseableLong -->
   * 
   * @return <code>true</code> if all members of the table are a parseable long
   *         or null
   */
  public boolean areAllParseableLong() {
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj == null) continue;
        try {
          Long.parseLong(obj.toString());
        }
        catch(NumberFormatException e) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * <!-- areAllParseableFloat -->
   * 
   * @return <code>true</code> if all members of the table are a parseable float
   *         or null
   */
  public boolean areAllParseableFloat() {
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj == null) continue;
        try {
          Float.parseFloat(obj.toString());
        }
        catch(NumberFormatException e) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * <!-- areAllParseableDouble -->
   * 
   * @return <code>true</code> if all members of the table are a parseable
   *         double or null
   */
  public boolean areAllParseableDouble() {
    for(int r = 0; r < nrows; r++) {
      for(int c = 0; c < ncols; c++) {
        T obj = atRC(r, c);
        if(obj == null) continue;
        try {
          Double.parseDouble(obj.toString());
        }
        catch(NumberFormatException e) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * <!-- iterator -->
   * 
   * Provide an iterator for the table, row by row
   * 
   * @see java.lang.Iterable#iterator()
   */
  public Iterator<T> iterator() {
    return new TableIterator<T>(this);
  }

  /**
   * TableIterator
   * 
   * Private class used to iterate over a table, row by row
   * 
   * @param <U>
   * @author Gary Polhill
   */
  private class TableIterator<U> implements Iterator<U> {
    /**
     * The current row number
     */
    int r;

    /**
     * The next column number
     */
    int c;

    /**
     * The table being iterated over
     */
    Table<U> table;

    /**
     * Constructor, initialising the start location at row 0, column 0
     * 
     * @param table The table to iterate over
     */
    TableIterator(Table<U> table) {
      this.table = table;
      r = 0;
      c = 0;
    }

    /**
     * <!-- hasNext -->
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      if(r + 1 < table.nrows()) return true;
      else if(c < table.ncols()) return true;
      else
        return false;
    }

    /**
     * <!-- next -->
     * 
     * @see java.util.Iterator#next()
     */
    public U next() {
      if(c < table.ncols()) return table.atRC(r, c++);
      else if(++r < table.nrows()) {
        c = 0;
        return table.atRC(r, c++);
      }
      return null;
    }

    /**
     * <!-- remove -->
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

}
