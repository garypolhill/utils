/*
 * uk.ac.macaulay.util: GISRaster.java
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

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <!-- GISRaster -->
 * 
 * A GISRaster is a {@link Table} that also includes georeferencing information
 * and a 'no data' value or cells specifically stated as containing no data. The
 * georeferencing information pertains to the 'origin' of the table, which is
 * the co-ordinate of the bottom-left corner of the cell in the bottom row,
 * leftmost column of the table. The atXY... methods are preferred for accessing
 * table contents, and these methods are overloaded to provide access using
 * co-ordinates on the georeferenced scale.
 * 
 * @author Gary Polhill
 */
public class GISRaster<T> extends Table<T> {
  /**
   * Eastings of the 'origin' of the table (bottom row, leftmost column)
   */
  private final double originX;

  /**
   * Northings of the 'origin' of the table (bottom row, leftmost column)
   */
  private final double originY;

  /**
   * Length of one side of the (square) cells
   */
  private final double cellSize;

  /**
   * Co-ordinates containing no data (map of X to set of Y co-ordinates
   */
  private Map<Integer, Set<Integer>> nodata;

  /**
   * No data value, if used (null if not)
   */
  private final T nodata_value;

  /**
   * Constructor with null nodata_value
   * 
   * @param nrows Number of rows in the table
   * @param ncols Number of columns
   * @param originX Georeference for eastings origin
   * @param originY Georeference for northings origin
   * @param cellSize Length of one side of the (square) cells
   */
  public GISRaster(int nrows, int ncols, double originX, double originY, double cellSize) {
    this(nrows, ncols, originX, originY, cellSize, null);
  }

  /**
   * Constructor with specified nodata_value
   * 
   * @param nrows Number of rows in the raster
   * @param ncols Number of columns in the raster
   * @param originX Georeference for eastings origin
   * @param originY Georeference for northings origin
   * @param cellSize Length of one side of the (square) cells
   * @param nodata_value Value indicating no data for a cell
   */
  public GISRaster(int nrows, int ncols, double originX, double originY, double cellSize, T nodata_value) {
    super(nrows, ncols);
    this.originX = originX;
    this.originY = originY;
    this.cellSize = cellSize;
    if(nodata == null) nodata = new HashMap<Integer, Set<Integer>>();
    this.nodata_value = nodata_value;
  }

  /**
   * Constructor converting an existing table into a GISRaster, with null
   * nodata_value
   * 
   * @param table The table to convert
   * @param originX Georeference for eastings origin
   * @param originY Georeference for northings origin
   * @param cellSize Length of one side of the (square) cells
   */
  public GISRaster(Table<T> table, double originX, double originY, double cellSize) {
    this(table, originX, originY, cellSize, null);
  }

  /**
   * Constructor converting an existing table into a GISRaster, with specified
   * nodata_value
   * 
   * @param table The table to convert
   * @param originX Georeference for eastings origin
   * @param originY Georeference for northings origin
   * @param cellSize Length of one side of the (square) cells
   * @param nodata_value Value to use to indicate no data for that cell
   */
  public GISRaster(Table<T> table, double originX, double originY, double cellSize, T nodata_value) {
    super(table);
    this.originX = originX;
    this.originY = originY;
    this.cellSize = cellSize;
    if(nodata == null) nodata = new HashMap<Integer, Set<Integer>>();
    this.nodata_value = nodata_value;
  }

  /**
   * Cloning constructor
   * 
   * @param raster The GISRaster to clone
   */
  public GISRaster(GISRaster<T> raster) {
    this(raster, raster.originX, raster.originY, raster.cellSize, raster.nodata_value);
    for(Integer x: raster.nodata.keySet()) {
      nodata.put(x, new HashSet<Integer>(raster.nodata.get(x)));
    }
  }

  /**
   * <!-- getOriginX -->
   * 
   * @return The eastings co-ordinate of the origin
   */
  public double getOriginX() {
    return originX;
  }

  /**
   * <!-- getOriginY -->
   * 
   * @return The northings co-ordinate of the origin
   */
  public double getOriginY() {
    return originY;
  }

  /**
   * <!-- getCellSize -->
   * 
   * @return The length of one-side of the square cells
   */
  public double getCellSize() {
    return cellSize;
  }

  /**
   * <!-- getCellArea -->
   * 
   * @return The area of each cell
   */
  public double getCellArea() {
    return cellSize * cellSize;
  }

  /**
   * <!-- getNoDataValue -->
   * 
   * The 'no data' value is an entry in the table that indicates no data are
   * available for that cell. The value is <code>null</code> by default, but
   * that does not mean that all cells have data. The
   * {@link #atXYNoData(double, double)} method (or the <code>int</code>
   * equivalent) may be used to assert that there are no data for a cell without
   * assigning it a value indicating as much. Cell entries are also
   * <code>null</code> by default, which also means no data have been given.
   * 
   * @return The 'no data' value
   */
  public T getNoDataValue() {
    return nodata_value;
  }

  /**
   * <!-- convertX -->
   * 
   * Convert eastings into a column number
   * 
   * @param x Eastings
   * @return Column number in the table (not guaranteed to be an entry in the
   *         table)
   */
  public int convertX(double x) {
    return (int)Math.floor((x - originX) / cellSize);
  }

  /**
   * <!-- convertY -->
   * 
   * Convert northings into an inverse row number
   * 
   * @param y Northings
   * @return Inverse row number in the table (0 at the bottom of the table,
   *         rather than the top, and not guaranteed to be an entry in the
   *         table)
   */
  public int convertY(double y) {
    return (int)Math.floor((y - originY) / cellSize);
  }

  /**
   * <!-- convertX -->
   * 
   * Convert a column number into eastings (at the centre of the cell)
   * 
   * @param x Column number
   * @return Eastings
   */
  public double convertX(int x) {
    return (double)x * cellSize + originX + cellSize / 2.0;
  }

  /**
   * <!-- convertY -->
   * 
   * Convert an inverse row number into northings (at the centre of the cell)
   * 
   * @param y Inverse row number (0 at the bottom of the table, rather than the
   *          top)
   * @return Northings
   */
  public double convertY(int y) {
    return (double)y * cellSize + originY + cellSize / 2.0;
  }

  /**
   * <!-- convertRow -->
   * 
   * Convert a row number into northings (at the centre of the cell)
   * 
   * @param row The row number
   * @return Northings
   */
  public double convertRow(int row) {
    return convertY((nrows - row) - 1);
  }

  /**
   * <!-- isInTable -->
   * 
   * Check if a georeference lies inside the table (all points outside the table
   * are 'no data')
   * 
   * @param x Eastings
   * @param y Northings
   * @return <code>true</code> if this co-ordinate is inside the table
   */
  public boolean isInTable(double x, double y) {
    return isInTableX(x) && isInTableY(y);
  }

  /**
   * <!-- isInTableX -->
   * 
   * Check if eastings lies inside the table
   * 
   * @param x Eastings
   * @return <code>true</code> if eastings is inside the table
   */
  public boolean isInTableX(double x) {
    int col = convertX(x);
    return (col < 0 || col >= ncols) ? false : true;
  }

  /**
   * <!-- isInTableY -->
   * 
   * Check if northings lies inside the table
   * 
   * @param y Northings
   * @return <code>true</code> if northings is inside the table
   */
  public boolean isInTableY(double y) {
    int irow = convertY(y);
    return (irow < 0 || irow >= nrows) ? false : true;
  }

  /**
   * <!-- convertColumn -->
   * 
   * Return a column number or <code>null</code> if eastings is outside the
   * table
   * 
   * @param x Eastings
   * @return Column number guaranteed to be in the table or <code>null</code>
   */
  public Integer convertColumn(double x) {
    int col = convertX(x);
    return isInTableX(x) ? col : null;
  }

  /**
   * <!-- convertRow -->
   * 
   * Return a row number or <code>null</code> if northings is outside the table
   * 
   * @param y Northings
   * @return Row number guaranteed to be in the table or <code>null</code>
   */
  public Integer convertRow(double y) {
    int irow = convertY(y);
    return isInTableY(y) ? (nrows - irow) - 1 : null;
  }

  /**
   * <!-- atXY -->
   * 
   * Return a value for a georeferenced co-ordinate. A value is return
   * <i>regardless</i> of whether the co-ordinate corresponds to an entry in the
   * table: all values outside the table are 'no data' values (<code>null</code>
   * by default). If you want an exception to be thrown if the co-ordinate is
   * out of range, use <code>atXY(convertX(x), convertY(y))</code>.
   * 
   * @param x Eastings
   * @param y Northings
   * @return Entry in the table or 'no data' value
   */
  public T atXY(double x, double y) {
    return isInTable(x, y) ? atXY(convertX(x), convertY(y)) : nodata_value;
  }

  /**
   * <!-- atXY -->
   * 
   * Return a value for an (x, y) co-ordinate in the table. If x and y are
   * outside the table's range, then an exception will be thrown.
   * 
   * @see uk.ac.macaulay.util.Table#atXY(int, int)
   * @param x
   * @param y
   * @return Entry in the table for the specified co-ordinate
   */
  @Override
  public T atXY(int x, int y) {
    if(isAtXYNoData(x, y)) return nodata_value;
    return super.atXY(x, y);
  }

  /**
   * <!-- atXFlipY -->
   * 
   * Return a value for the co-ordinate in the table. The y-axis is 'flipped'
   * and is equal to the row number.
   * 
   * @see uk.ac.macaulay.util.Table#atXFlipY(int, int)
   * @param x
   * @param y
   * @return Entry in the table for the specified co-ordinate
   */
  @Override
  public T atXFlipY(int x, int y) {
    if(isAtXYNoData(x, (nrows - y) - 1)) return nodata_value;
    return super.atXFlipY(x, y);
  }

  /**
   * <!-- atRC -->
   * 
   * Return a value for the row and column number.
   * 
   * @see uk.ac.macaulay.util.Table#atRC(int, int)
   * @param row The row
   * @param col The column
   * @return Entry in the table at the specified cell
   */
  @Override
  public T atRC(int row, int col) {
    if(isAtXYNoData(col, (nrows - row) - 1)) return nodata_value;
    return super.atRC(row, col);
  }

  /**
   * <!-- atXY -->
   * 
   * Set the value at a georeferenced co-ordinate. This will throw an exception
   * if an attempt is made to set a cell outside the range of the table to
   * anything other than <code>null</code> or the 'no data' value.
   * 
   * @param x
   * @param y
   * @param value Value to set the cell to
   */
  public void atXY(double x, double y, T value) {
    if(!isInTable(x, y) && (value == null || value.equals(nodata_value))) return;
    atXY(convertX(x), convertY(y), value);
  }

  /**
   * <!-- atXY -->
   * 
   * Set the value at a cell in the table.
   * 
   * @see uk.ac.macaulay.util.Table#atXY(int, int, java.lang.Object)
   * @param x
   * @param y
   * @param value
   */
  @Override
  public void atXY(int x, int y, T value) {
    if(value == null || value.equals(nodata_value)) atXYNoData(x, y);
    else {
      if(isAtXYNoData(x, y) && nodata != null && nodata.containsKey(x) && nodata.get(x).contains(y)) nodata.get(x).remove(y);
      super.atXY(x, y, value);
    }
  }

  /**
   * <!-- atXFlipY -->
   * 
   * Set the value at a cell in the table (y co-ordinated 'flipped' so it equals
   * row)
   * 
   * @see uk.ac.macaulay.util.Table#atXFlipY(int, int, java.lang.Object)
   * @param x
   * @param y
   * @param value
   */
  @Override
  public void atXFlipY(int x, int y, T value) {
    atXY(x, (nrows - y) - 1, value);
  }

  /**
   * <!-- atRC -->
   * 
   * Set the value at a cell in the table specified using row and column numbers
   * 
   * @see uk.ac.macaulay.util.Table#atRC(int, int, java.lang.Object)
   * @param row
   * @param col
   * @param value
   */
  @Override
  public void atRC(int row, int col, T value) {
    atXY(col, (nrows - row) - 1, value);
  }

  /**
   * <!-- atXYNoData -->
   * 
   * Stipulate that there is no data in the cell corresponding to the
   * georeferenced co-ordinate. (No exception is thrown if the co-ordinate is
   * out of range; if you want one, do
   * <code>atXYNoData(convert(x), convert(y))</code>.)
   * 
   * @param x
   * @param y
   */
  public void atXYNoData(double x, double y) {
    if(!isInTable(x, y)) return;
    atXYNoData(convertX(x), convertY(y));
  }

  /**
   * <!-- atXYNoData -->
   * 
   * Stipulate that there is no data in the specified cell. This will throw an
   * exception if the cell co-ordinates are out of bounds, and set the current
   * entry in that cell to <code>null</code>, deleting any entry therein.
   * 
   * @param x
   * @param y
   */
  public void atXYNoData(int x, int y) {
    atXY(x, y, null);
    if(nodata == null) nodata = new HashMap<Integer, Set<Integer>>();
    if(!nodata.containsKey(x)) {
      nodata.put(x, new HashSet<Integer>());
    }
    nodata.get(x).add(y);
  }

  /**
   * <!-- isAtXYNoData -->
   * 
   * Check whether there is no data for a specified georeference. This will not
   * throw an exception if the georeference is out of the table's bounds, but
   * will instead return <code>true</code>. If you want an exception to be
   * thrown in this case, do <code>isAtXYNoData(convert(x), convert(y))</code>.
   * 
   * @param x
   * @param y
   * @return <code>true</code> if the cell in the table corresponding to the
   *         georeference has a 'no data' entry, or the georeference is out of
   *         bounds.
   */
  public boolean isAtXYNoData(double x, double y) {
    return !isInTable(x, y) || isAtXYNoData(convertX(x), convertY(y));
  }

  /**
   * <!-- isAtXYNoData -->
   * 
   * <p>
   * Check whether a cell has a 'no data' entry. An exception is thrown if the
   * cell is out of bounds. A cell has a 'no data' entry if any of the following
   * apply:
   * </p>
   * 
   * <ul>
   * <li>it is <code>null</code></li>
   * <li>it is equal to the 'no data' value</li>
   * <li>it has been stipulated to have no data via a call to
   * {@link #atXYNoData(double, double)} (or the <code>int</code> equivalent)
   * and no subsequent call to {@link #atXY(double, double)} (or the
   * <code>int</code> equivalent) has given the cell a value.
   * </ul>
   * 
   * @param x
   * @param y
   * @return <code>true</code> if there is no data for the given cell.
   */
  public boolean isAtXYNoData(int x, int y) {
    T entry = super.atXY(x, y);
    return entry == null || (nodata != null && nodata.containsKey(x) && nodata.get(x).contains(y))
      || entry.equals(nodata_value);
  }

  /**
   * <!-- asGISKeyedRaster -->
   * 
   * Convert the GISRaster into a {@link GISKeyedRaster} using a key mapping
   * entries to colours.
   * 
   * @param key The key (aka legend) mapping entries in the table to colours
   * @return A GISKeyedRaster
   */
  public GISKeyedRaster<T> asGISKeyedRaster(Map<T, Color> key) {
    return asGISKeyedRaster(new GISMappedKey<T>(key));
  }

  /**
   * <!-- asGISKeyedRaster -->
   * 
   * Convert the GISRaster into a {@link GISKeyedRaster} using a generic
   * converter providing a one-to-one mapping from entries to colours.
   * 
   * @param converter The converter
   * @return A GISKeyedRaster
   */
  public GISKeyedRaster<T> asGISKeyedRaster(GISRasterImageConverter<T> converter) {
    return new GISKeyedRaster<T>(this, converter);
  }
}
