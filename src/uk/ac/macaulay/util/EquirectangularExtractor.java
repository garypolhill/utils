/*
 * uk.ac.macaulay.util: EquirectangularExtractor.java
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- EquirectangularExtractor -->
 * 
 * This class takes an equirectangular projection raster image as input, and
 * stores it in an array of specified resolution, which can then be saved as a
 * CSV file.
 * 
 * @author Gary Polhill
 */
public class EquirectangularExtractor {
  private int[][] pixels;
  private int pixelsPerDegree;
  public static final int DEFAULT_PIXELS_PER_DEGREE = 1;
  public static final int LONGITUDE_DEGREES = 360;
  public static final int LATITUDE_DEGREES = 180;
  
  public EquirectangularExtractor(String file) throws IOException {
    this(file, DEFAULT_PIXELS_PER_DEGREE);
  }
  
  public EquirectangularExtractor(String file, int pixelsPerDegree) throws IOException {
    this(FileOpener.readImage(file), pixelsPerDegree);
  }
  
  public EquirectangularExtractor(BufferedImage image, int pixelsPerDegree) {
    this.pixelsPerDegree = pixelsPerDegree;
    pixels = new int[this.pixelsPerDegree * LONGITUDE_DEGREES][this.pixelsPerDegree * LATITUDE_DEGREES];
    
    double ratioLong = (double)image.getWidth() / (double)(this.pixelsPerDegree * LONGITUDE_DEGREES);
    double ratioLat = (double)image.getHeight() / (double)(this.pixelsPerDegree * LATITUDE_DEGREES);
    
    int w = (int)Math.round(ratioLong);
    int h = (int)Math.round(ratioLat);
    if(w == 0) w++;
    if(h == 0) h++;
    
    for(int longitude = 0; longitude < pixels.length; longitude++) {
      for(int latitude = 0; latitude < pixels[longitude].length; latitude++) {
        double dx = (double)longitude * ratioLong;
        double dy = (double)latitude * ratioLat;
        
        int x = (int)Math.round(dx);
        int y = (int)Math.round(dy);
        
        BufferedImage tile = image.getSubimage(x, y, w, h);
        
        Map<Integer, Integer> colourCount = new HashMap<Integer, Integer>();
        
        for(int tx = 0; tx < tile.getWidth(); tx++) {
          for(int ty = 0; ty < tile.getHeight(); ty++) {
            if(!colourCount.containsKey(tile.getRGB(tx, ty))) {
              colourCount.put(tile.getRGB(tx, ty), 0);
            }
            colourCount.put(tile.getRGB(tx, ty), colourCount.get(tile.getRGB(tx, ty)) + 1);
          }
        }
        
        int mode = 0;
        int modeCount = -1;
        
        for(Integer colour: colourCount.keySet()) {
          Integer count = colourCount.get(colour);
          if(count > modeCount) {
            modeCount = count;
            mode = colour;
          }
        }
        
        pixels[longitude][latitude] = mode;
      }
    }
  }
  
  public int getPixelsPerDegree() {
    return pixelsPerDegree;
  }
  
  public Table<Integer> getTable() {
    Table<Integer> table = new Table<Integer>(pixelsPerDegree * LATITUDE_DEGREES, pixelsPerDegree * LONGITUDE_DEGREES);
    for(int longitude = 0; longitude < pixelsPerDegree * LONGITUDE_DEGREES; longitude++) {
      for(int latitude = 0; latitude < pixelsPerDegree * LATITUDE_DEGREES; latitude++) {
        table.atRC(latitude, longitude, pixels[longitude][latitude]);
      }
    }
    return table;
  }
  
  public GISRaster<Integer> getGISRaster() {
    return new GISRaster<Integer>(getTable(), -180.0, -90.0, 1.0);
  }
  
  public GISKeyedRaster<Integer> getGISKeyedRaster() {
    return getGISRaster().asGISKeyedRaster(new GISIntegerKey());
  }
  
  public BufferedImage getImage() {
    return getGISKeyedRaster().getImage();
  }

  /**
   * <!-- main -->
   * 
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    if(args.length < 2) {
      throw new Exception("Usage: EquirectangularExtractor <map raster file> <CSV file> [<PNG file>]");
    }
    try {
      EquirectangularExtractor extractor = new EquirectangularExtractor(args[0]);
      CSVWriter writer = new CSVWriter(args[1]);
      writer.writeXY(extractor.getTable());
      if(args.length > 2) {
        FileOpener.writeImage(extractor.getImage(), args[2]);
      }
    }
    catch(IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
