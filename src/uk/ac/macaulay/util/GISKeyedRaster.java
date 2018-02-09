/*
 * uk.ac.macaulay.util: GISKeyedRaster.java
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
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.Map;

/**
 * <!-- GISKeyedRaster -->
 * 
 * A {@link GISRaster} with a converter that maps entries in the table to
 * colours.
 * 
 * @author Gary Polhill
 */
public class GISKeyedRaster<T> extends GISRaster<T> {
  /**
   * The converter providing a one-to-one mapping from entries to colours.
   */
  private GISRasterImageConverter<T> converter;

  /**
   * Constructor converting a table to a GISKeyedRaster with a specified 'no
   * data' value.
   * 
   * @param table A table containing data
   * @param originX The georeference for the left edge of the table
   * @param originY The georeference for the bottom edge of the table
   * @param cellSize The size of the (square) cells
   * @param nodata_value Value indicating no data
   * @param converter The converter to use to supply colours for each cell
   */
  public GISKeyedRaster(Table<T> table, double originX, double originY, double cellSize, T nodata_value,
      GISRasterImageConverter<T> converter) {
    super(table, originX, originY, cellSize, nodata_value);
    this.converter = converter;
  }

  /**
   * Constructor converting a table with no 'no data' value
   * 
   * @param table The table to convert
   * @param originX Georeference for left edge of the table
   * @param originY Georeference for bottom edge of the table
   * @param cellSize The size of one edge of the (square) cells
   * @param converter Converter to supply colours for each cell
   */
  public GISKeyedRaster(Table<T> table, double originX, double originY, double cellSize,
      GISRasterImageConverter<T> converter) {
    super(table, originX, originY, cellSize);
    this.converter = converter;
  }

  /**
   * Constructor for an empty GISKeyedRaster with a specified 'no data' value
   * 
   * @param nrows Number of rows in the raster
   * @param ncols Number of columns in the raster
   * @param originX Georeference for left edge of left column
   * @param originY Georeference for bottom edge of bottom row
   * @param cellSize The size of one edge of the (square) cells
   * @param nodata_value Value to use to indicate 'no data'
   * @param converter Converter to supply colours for each cell
   */
  public GISKeyedRaster(int nrows, int ncols, double originX, double originY, double cellSize, T nodata_value,
      GISRasterImageConverter<T> converter) {
    super(nrows, ncols, originX, originY, cellSize, nodata_value);
    this.converter = converter;
  }

  /**
   * Constructor for an empty GISKeyedRaster with no 'no data' value
   * 
   * @param nrows Number of rows in the raster
   * @param ncols Number of columns in the raster
   * @param originX Georeference for left edge of left column
   * @param originY Georeference for bottom edge of bottom row
   * @param cellSize The size of one edge of the (square) cells
   * @param converter Converter to supply colours for each cell
   */
  public GISKeyedRaster(int nrows, int ncols, double originX, double originY, double cellSize,
      GISRasterImageConverter<T> converter) {
    super(nrows, ncols, originX, originY, cellSize);
    this.converter = converter;
  }

  /**
   * Constructor converting a {@link GISRaster} to a GISKeyedRaster using the
   * converter
   * 
   * @param raster The GISRaster to convert
   * @param converter The converter to do it with
   */
  public GISKeyedRaster(GISRaster<T> raster, GISRasterImageConverter<T> converter) {
    super(raster);
    this.converter = converter;
  }

  /**
   * Cloning constructor
   * 
   * @param raster GISKeyedRaster to clone
   */
  public GISKeyedRaster(GISKeyedRaster<T> raster) {
    super(raster);
    this.converter = raster.converter;
  }

  /**
   * Constructor using a buffered image and a key to build a GISKeyedRaster
   * 
   * @param image The image from which to get colours
   * @param key The key mapping data to colours
   * @param originX Georeference for left edge of the image
   * @param originY Georeference for bottom edge of the image
   * @param cellSize Length of one side of a pixel (assumed square)
   */
  public GISKeyedRaster(BufferedImage image, Map<T, Color> key, double originX, double originY, double cellSize) {
    this(image, new GISMappedKey<T>(key), originX, originY, cellSize);
  }

  /**
   * Constructor using a buffered image and a converter to build a
   * GISKeyedRaster
   * 
   * @param image The image from which to get colours
   * @param converter Converter mapping colours to data
   * @param originX Georeference for the left edge of the image
   * @param originY Georeference for the bottom edge of the image
   * @param cellSize Length of one side of a pixel (assumed square)
   */
  public GISKeyedRaster(BufferedImage image, GISRasterImageConverter<T> converter, double originX, double originY,
      double cellSize) {
    super(image.getWidth(), image.getHeight(), originX, originY, cellSize);
    this.converter = converter;
    for(int x = 0; x < ncols; x++) {
      for(int y = 0; y < nrows; y++) {
        atXY(x, y, new Color(image.getRGB(x, y)));
      }
    }
  }

  /**
   * <!-- getColor -->
   * 
   * @param item
   * @return The colour associated with an entry
   */
  public Color getColor(T item) {
    return converter.getColor(item);
  }

  /**
   * <!-- getEntry -->
   * 
   * @param c
   * @return Return the entry associated with a colour
   */
  public T getEntry(Color c) {
    return converter.getEntry(c);
  }

  /**
   * <!-- atXYColor -->
   * 
   * Get the colour at a location (a transparent colour is returned if there is
   * no data)
   * 
   * @see GISRaster#atXY(double, double)
   * @param x Eastings
   * @param y Northings
   * @return The colour at the cell corresponding to that location
   */
  public Color atXYColor(double x, double y) {
    if(isAtXYNoData(x, y)) return new Color(0, 0, 0, 0);
    return converter.getColor(super.atXY(x, y));
  }

  /**
   * <!-- atXYColor -->
   * 
   * Get the colour at a cell (a transparent colour is returned if there is no
   * data in that cell)
   * 
   * @see GISRaster#atXY(int, int)
   * @param x X cell
   * @param y Y cell
   * @return The colour at that cell
   */
  public Color atXYColor(int x, int y) {
    if(isAtXYNoData(x, y)) return new Color(0, 0, 0, 0);
    return converter.getColor(super.atXY(x, y));
  }

  /**
   * <!-- atXY -->
   * 
   * Set the colour at a location (this will set the corresponding entry)
   * 
   * @see GISRaster#atXY(double, double, Object)
   * @param x Eastings
   * @param y Northings
   * @param color Colour to set at the corresponding cell
   */
  public void atXY(double x, double y, Color color) {
    atXY(x, y, converter.getEntry(color));
  }

  /**
   * <!-- atXY -->
   * 
   * Set the colour at a cell
   * 
   * @see GISRaster#atXY(int, int, Object)
   * @param x X cell
   * @param y Y cell
   * @param color Colour to set
   */
  public void atXY(int x, int y, Color color) {
    atXY(x, y, converter.getEntry(color));
  }

  /**
   * <!-- getImage -->
   * 
   * @return A {@link BufferedImage} depicting the GISRaster data of default
   *         type ({@link BufferedImage#TYPE_INT_ARGB}) and zoom factor (1).
   */
  public BufferedImage getImage() {
    return getImage(BufferedImage.TYPE_INT_ARGB);
  }

  /**
   * <!-- getImage -->
   * 
   * @param imageType
   * @return A {@link BufferedImage} depicting the GISRaster with the specified
   *         image type with default zoom factor (1)
   */
  public BufferedImage getImage(int imageType) {
    return getImage(imageType, 1);
  }

  /**
   * <!-- getImage -->
   * 
   * @param imageType
   * @param zoomFactor
   * @return A {@link BufferedImage} depicting the GISRaster with the specified
   *         image type and zoom factor
   */
  public BufferedImage getImage(int imageType, int zoomFactor) {
    BufferedImage image = new BufferedImage(ncols * zoomFactor, nrows * zoomFactor, imageType);
    fillImage(image, zoomFactor);
    return image;
  }

  /**
   * <!-- getImage -->
   * 
   * @param imageType
   * @param cm An {@link IndexColorModel} for the image
   * @return A {@link BufferedImage} with the specified type and colour model,
   *         and default zoom factor (1)
   */
  public BufferedImage getImage(int imageType, IndexColorModel cm) {
    return getImage(imageType, cm, 1);
  }

  /**
   * <!-- getImage -->
   * 
   * @param imageType
   * @param cm An {@link IndexColorModel} for the image
   * @param zoomFactor
   * @return A {@link BufferedImage} with the specified type, colour model and
   *         zoom factor
   */
  public BufferedImage getImage(int imageType, IndexColorModel cm, int zoomFactor) {
    BufferedImage image = new BufferedImage(ncols * zoomFactor, nrows * zoomFactor, imageType, cm);
    fillImage(image, zoomFactor);
    return image;
  }

  /**
   * <!-- fillImage -->
   * 
   * Fill the image (assumed to be of the correct size) with the raster data,
   * using the default zoom factor (1)
   * 
   * @param image The image to fill
   */
  public void fillImage(BufferedImage image) {
    fillImage(image, 1);
  }

  /**
   * <!-- fillImage -->
   * 
   * Fill the supplied image with colours corresponding to the raster data,
   * using the zoom factor. The image is assumed to be of the correct size.
   * If it is too big, then the extra pixels will not be set. If too small,
   * then the data in those cells will not be written.
   * 
   * @param image Image to fill
   * @param zoomFactor Zoom factor (number of pixels per cell in the data)
   */
  public void fillImage(BufferedImage image, int zoomFactor) {
    // Save some overhead in the loop calling methods
    int width = image.getWidth();
    int height = image.getHeight();
    
    for(int x = 0; x < ncols; x++) {
      for(int y = 0; y < nrows; y++) {
        Color color = atXYColor(x, y);
        int rgb = color.getRGB();
        for(int xx = 0; xx < zoomFactor; xx++) {
          for(int yy = 0; yy < zoomFactor; yy++) {
            int xxx = x * zoomFactor + xx;
            int yyy = y * zoomFactor + yy;
            if(xxx < width && yyy < height) {
              image.setRGB(xxx, yyy, rgb);
            }
            else {
              break;
            }
          }
        }
      }
    }
  }
}
