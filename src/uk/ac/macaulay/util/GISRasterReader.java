/*
 * uk.ac.macaulay.util: ImageReader.java
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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * <!-- GISRasterReader -->
 * 
 * This class creates a GISRaster<?> object from a GISRaster file. It can read
 * ARC ASCII grid files and XPM files. To use XPM files, the GIS data need to be
 * embedded within the XPM as extensions. These should be stored in the XPMEXT
 * &lt;name&gt; &lt;value&gt; format, where &lt;name&gt; is replaced with one of
 * the XPM_XLLCORNER, XPM_YLLCORNER and XPM_CELLSIZE strings, and &lt;value&gt;
 * with its corresponding value. If you give it an ARC ASCII grid file, it will
 * create a GISRaster<?> class, with a datatype String, Double or Integer,
 * depending on whether the values in the grid file can be parsed as such. If
 * you give it an XPM file, and the XPM file includes colours (rather than just
 * names), then it will create a GISKeyedRaster<?>, again, with datatypes
 * String, Double or Integer. A GISRaster<?> can be converted to a
 * GISKeyedRaster<?> if it is given a legend, mapping symbolic values onto
 * colours.
 * 
 * @author Gary Polhill
 */
public class GISRasterReader {
  /**
   * The name of the file being read
   */
  private String filename;

  /**
   * The raster created from reading the file
   */
  private GISRaster<?> raster;

  /**
   * The raster type (detected automatically)
   */
  private Class<?> rasterType;
  public static final String XPM_XLLCORNER = "xllcorner";
  public static final String XPM_YLLCORNER = "yllcorner";
  public static final String XPM_CELLSIZE = "cellsize";

  public GISRasterReader(String filename) throws IOException {
    this.filename = filename;
    BufferedReader buff = FileOpener.read(filename);
    if(filename.endsWith(".xpm")) {
      readXPM(buff, new HashMap<Color, String>(), null, null, null, XPM_XLLCORNER, XPM_YLLCORNER, XPM_CELLSIZE);
    }
    else {
      readGridASCII(buff);
    }
  }

  public <T> GISRasterReader(String filename, double xllcorner, double yllcorner, double cellSize,
      GISRasterImageConverter<T> converter) throws IOException {
    this.filename = filename;
    if(filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".gif")) {
      BufferedImage image = FileOpener.readImage(filename);
      raster = new GISKeyedRaster<T>(image, converter, xllcorner, yllcorner, cellSize);
    }
    else if(filename.endsWith(".xpm")) {
      
    }
    else if(filename.endsWith(".csv")) {
      
    }
  }

  public String getFileName() {
    return filename;
  }

  public Class<?> getRasterDataType() {
    return rasterType;
  }

  public boolean isRasterTypeInteger() {
    return rasterType == Integer.class;
  }

  public boolean isRasterTypeDouble() {
    return rasterType == Double.class;
  }

  @SuppressWarnings("unchecked")
  public GISRaster<String> getRasterString() {
    return (GISRaster<String>)raster;
  }

  @SuppressWarnings("unchecked")
  public GISRaster<Double> getRasterDouble() {
    return (GISRaster<Double>)raster;
  }

  @SuppressWarnings("unchecked")
  public GISRaster<Integer> getRasterInteger() {
    return (GISRaster<Integer>)raster;
  }
  
  public GISRaster<?> getRaster() {
    return raster;
  }
  
  public static GISRaster<?> read(String fileName) throws IOException {
    GISRasterReader reader = new GISRasterReader(fileName);
    return reader.getRaster();
  }

  @SuppressWarnings("unchecked")
  private void readXPM(BufferedReader buff, Map<Color, String> invLegend, Double xllcorner, Double yllcorner,
      Double cellSize, String xllcornerStr, String yllcornerStr, String cellSizeStr) throws IOException {
    TextReader reader = new TextReader(buff, filename, "XPM");

    reader.readExactString("/* XPM */");
    reader.readExactWord("static", EnumSet.of(TextReader.Comment.C));
    reader.readExactStringIgnoreLeadingSpace("char");
    reader.readExactStringIgnoreLeadingSpace("*");
    reader.readWord();
    reader.readExactStringIgnoreLeadingSpace("[");
    reader.readExactStringIgnoreLeadingSpace("]");
    reader.readExactStringIgnoreLeadingSpace("=");
    reader.readExactWord("{", EnumSet.of(TextReader.Comment.C));
    String header = reader.readQuotedString(EnumSet.of(TextReader.Comment.C), '"');
    String[] headerWords = header.split("\\s+");
    if(headerWords.length < 4) {
      throw new FileFormatException(filename, "XPM",
          "<width> <height> <ncolours> <nchrspcolour> [<x hotspot> <y hotspot>] [XPMEXT]", header);
    }
    int width;
    int height;
    int ncolours;
    int nchrspcol;
    boolean extensions = headerWords.length > 4 && headerWords[headerWords.length - 1].equals("XPMEXT");
    try {
      width = Integer.parseInt(headerWords[0]);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "XPM", "integer for width", headerWords[0]);
    }
    try {
      height = Integer.parseInt(headerWords[1]);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "XPM", "integer for height", headerWords[1]);
    }
    try {
      ncolours = Integer.parseInt(headerWords[2]);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "XPM", "integer for number of colours", headerWords[2]);
    }
    try {
      nchrspcol = Integer.parseInt(headerWords[3]);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "XPM", "integer for number of characters per colour", headerWords[3]);
    }
    reader.readExactString(",");

    Map<String, Color> mono = new HashMap<String, Color>();
    Map<String, Color> grey4 = new HashMap<String, Color>();
    Map<String, Color> grey = new HashMap<String, Color>();
    Map<String, Color> colour = new HashMap<String, Color>();
    Map<String, String> symbol = new HashMap<String, String>();
    for(int i = 0; i < ncolours; i++) {
      String colstr = reader.readQuotedString(EnumSet.of(TextReader.Comment.C), '"');
      if(colstr == null || colstr.length() == 0) {
        throw new FileFormatException(filename, "XPM", "colour map", "");
      }

      String colkey = colstr.substring(0, nchrspcol);
      String[] colwords = colstr.substring(nchrspcol + 1).split("\\s+");
      for(int j = 0; j < colwords.length; j++) {
        if(colwords[j] == null || colwords[j].length() == 0) continue;

        String key = colwords[j];
        if(!key.equals("m") || !key.equals("g4") || !key.equals("g") || !key.equals("c") || !key.equals("s")) {
          throw new FileFormatException(filename, "XPM", "\"m\", \"g4\", \"g\", \"c\" or \"s\"", key);
        }

        do {
          j++;
        } while(colwords[j] == null || colwords[j].length() == 0 || j < colwords.length);
        if(j == colwords.length) {
          throw new FileFormatException(filename, "XPM", "colour name for key \"" + key + "\"", "\"");
        }

        if(key.equals("m")) {
          mono.put(colkey, getXPMColor(colwords[j]));
        }
        else if(key.equals("g4")) {
          grey4.put(colkey, getXPMColor(colwords[j]));
        }
        else if(key.equals("g")) {
          grey.put(colkey, getXPMColor(colwords[j]));
        }
        else if(key.equals("c")) {
          colour.put(colkey, getXPMColor(colwords[j]));
        }
        else if(key.equals("s")) {
          symbol.put(colkey, colwords[j]);
        }
      }

      reader.readExactString(",");
    }

    Table<String> table = new Table<String>(height, width);
    Map<String, Color> legend = new HashMap<String, Color>();
    boolean allEntriesHaveColours = true;
    for(int y = 0; y < height; y++) {
      String pixels = reader.readQuotedString(EnumSet.of(TextReader.Comment.C), '"');
      if(pixels == null || pixels.length() != width * nchrspcol) {
        throw new FileFormatException(filename, "XPM", width + " pixels * " + nchrspcol + " character each = " + width
          * nchrspcol + " character quoted string ", pixels);
      }

      for(int x = 0; x < width; x++) {
        String key = pixels.substring(x * nchrspcol, (x + 1) * nchrspcol);
        Color col =
          colour.containsKey(key) ? colour.get(key)
                                 : (grey.containsKey(key) ? grey.get(key)
                                                         : (grey4.containsKey(key) ? grey4.get(key) : (mono
                                                             .containsKey(key) ? mono.get(key) : null)));
        if(col == null && !symbol.containsKey(key)) {
          throw new FileFormatException(filename, "XPM", "valid colour for (" + x + ", " + y + ")", key);
        }
        String entry =
          symbol.containsKey(key) ? symbol.get(key) : (invLegend.containsKey(col) ? invLegend.get(col) : null);
        if(entry == null && col.getAlpha() > 0) {
          throw new FileFormatException(filename, "XPM", "symbol for colour " + col, "");
        }
        if(col.getAlpha() > 0) {
          if(col == null) allEntriesHaveColours = false;
          else
            legend.put(entry, col);
          table.atXFlipY(x, y, entry);
        }
      }

      if(y < height - 1) reader.readExactString(",");
    }

    if(extensions) {
      reader.readExactString(",");
      String ext = reader.readQuotedString(EnumSet.of(TextReader.Comment.C), '"');
      if(ext != null && !ext.equals("XPMENDEXT")) {
        String words[] = ext.split("\\s+");
        if(words[0].equals("XPMEXT") && words.length == 3) {
          try {
            if(words[1].equals(xllcornerStr)) {
              xllcorner = Double.parseDouble(words[2]);
            }
            else if(words[1].equals(yllcornerStr)) {
              yllcorner = Double.parseDouble(words[2]);
            }
            else if(words[1].equals(cellSizeStr)) {
              cellSize = Double.parseDouble(words[2]);
            }
          }
          catch(NumberFormatException e) {
            throw new FileFormatException(filename, "XPM", "a valid double precision floating point number for "
              + words[1], words[2]);
          }
        }
      }
    }

    if(table.areAllParseableInteger()) {
      raster = new GISRaster<Integer>(table.asIntegerTable(), xllcorner, yllcorner, cellSize);
      rasterType = Integer.class;
    }
    else if(table.areAllParseableDouble()) {
      raster = new GISRaster<Double>(table.asDoubleTable(), xllcorner, yllcorner, cellSize);
      rasterType = Double.class;
    }
    else {
      raster = new GISRaster<String>(table, xllcorner, yllcorner, cellSize);
      rasterType = String.class;
    }

    if(allEntriesHaveColours && rasterType == String.class) {
      raster = ((GISRaster<String>)raster).asGISKeyedRaster(legend);
    }
  }

  private Color getXPMColor(String s) throws IOException {
    if(s.equals("None")) return new Color(0, 0, 0, 0);
    else if(s.startsWith("#")) {
      try {
        if((s.length() - 1) % 3 != 0) throw new NumberFormatException();
        int dig = (s.length() - 1) / 3;
        if(dig < 1) throw new NumberFormatException();
        float denom = Math.scalb(1.0F, dig * 4);
        denom -= 1.0F;
        float R = (float)Integer.decode("0x" + s.substring(1, dig + 1)) / denom;
        float G = (float)Integer.decode("0x" + s.substring(dig + 1, (2 * dig) + 1)) / denom;
        float B = (float)Integer.decode("0x" + s.substring((2 * dig) + 1)) / denom;
        return new Color(R, G, B);
      }
      catch(NumberFormatException e) {
        throw new FileFormatException(filename, "XPM", "a hexadecimal colour in format #RnGnBn", s);
      }
    }
    else if(s.startsWith("%")) {
      try {
        if((s.length() - 1) % 3 != 0) throw new NumberFormatException();
        int dig = (s.length() - 1) / 3;
        if(dig < 1) throw new NumberFormatException();
        float denom = Math.scalb(1.0F, dig * 4);
        denom -= 1.0F;
        float H = (float)Integer.decode("0x" + s.substring(1, dig + 1)) / denom;
        float S = (float)Integer.decode("0x" + s.substring(dig + 1, (2 * dig) + 1)) / denom;
        float B = (float)Integer.decode("0x" + s.substring((2 * dig) + 1)) / denom;
        return Color.getHSBColor(H, S, B);
      }
      catch(NumberFormatException e) {
        throw new FileFormatException(filename, "XPM", "a hexadecimal colour in format %HnSnBn", s);
      }
    }
    else {
      if(!XColorName.foundRGB()) {
        throw XColorName.rgbReadException();
      }
      Color c = XColorName.get(s);
      if(c == null) {
        throw new FileFormatException(filename, "XPM", "the word \"None\", an RGB colour as #RnGnBn, an "
          + "HSB (aka HSV) colour as %HnSnBn, or the name of an X standard colour", s);
      }
      return c;
    }
  }

  /**
   * <!-- readGridASCII -->
   * 
   * Read the raster in Grid ASCII format
   * 
   * @param buff
   * @throws IOException
   */
  private void readGridASCII(BufferedReader buff) throws IOException {
    TextReader reader = new TextReader(buff, filename, "ARC ASCII grid");

    Map<String, String> header =
      reader.readOrderedKeyValuePairs(new String[] { "nrows", "ncols", "xllcorner|xllcenter", "yllcorner|yllcenter",
        "cellsize", "?nodata_value" });

    int nrows, ncols;

    try {
      nrows = Integer.parseInt(header.get("nrows"));
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "ARC ASCII grid", "an integer for nrows", header.get("nrows"));
    }

    try {
      ncols = Integer.parseInt(header.get("ncols"));
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "ARC ASCII grid", "an integer for ncols", header.get("ncols"));
    }

    Table<String> table = reader.readTable(nrows, ncols);

    double cellsize, xllcorner, yllcorner;

    try {
      cellsize = Double.parseDouble(header.get("cellsize"));
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "ARC ASCII grid", "a double for cellsize", header.get("cellsize"));
    }

    try {
      xllcorner =
        header.containsKey("xllcorner") ? Double.parseDouble(header.get("xllcorner")) : (Double.parseDouble(header
            .get("xllcenter")) - cellsize / 2.0);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "ARC ASCII grid", "a double for xllcorner or xllcenter", header
          .containsKey("xllcorner") ? header.get("xllcorner") : header.get("xllcenter"));
    }

    try {
      yllcorner =
        header.containsKey("yllcorner") ? Double.parseDouble(header.get("yllcorner")) : (Double.parseDouble(header
            .get("yllcenter")) - cellsize / 2.0);
    }
    catch(NumberFormatException e) {
      throw new FileFormatException(filename, "ARC ASCII grid", "a double for yllcorner or yllcenter", header
          .containsKey("yllcorner") ? header.get("yllcorner") : header.get("yllcenter"));
    }

    String nodata_value = header.containsKey("nodata_value") ? header.get("nodata_value") : null;

    if(nodata_value != null) {
      try {
        int nodata_value_i = Integer.parseInt(nodata_value);
        if(!table.areAllParseableInteger()) throw new NumberFormatException();
        raster = new GISRaster<Integer>(table.asIntegerTable(), xllcorner, yllcorner, cellsize, nodata_value_i);
        rasterType = Integer.class;
      }
      catch(NumberFormatException e1) {
        try {
          double nodata_value_d = Double.parseDouble(nodata_value);
          if(!table.areAllParseableDouble()) throw new NumberFormatException();
          raster = new GISRaster<Double>(table.asDoubleTable(), xllcorner, yllcorner, cellsize, nodata_value_d);
          rasterType = Double.class;
        }
        catch(NumberFormatException e2) {
          raster = new GISRaster<String>(table, xllcorner, yllcorner, cellsize, nodata_value);
          rasterType = String.class;
        }
      }
    }
    else {
      if(table.areAllParseableInteger()) {
        raster = new GISRaster<Integer>(table.asIntegerTable(), xllcorner, yllcorner, cellsize);
        rasterType = Integer.class;
      }
      else if(table.areAllParseableDouble()) {
        raster = new GISRaster<Double>(table.asDoubleTable(), xllcorner, yllcorner, cellsize);
        rasterType = Double.class;
      }
      else {
        raster = new GISRaster<String>(table, xllcorner, yllcorner, cellsize);
        rasterType = String.class;
      }
    }
  }
}
