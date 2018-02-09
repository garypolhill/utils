/*
 * uk.ac.macaulay.util: XColorName.java
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- XColorName -->
 * 
 * Provide a map from X windows colour names to Java Colors.
 * 
 * @author Gary Polhill
 */
public final class XColorName {
  /**
   * Whether we found RGB
   */
  private static boolean foundRGB;

  /**
   * Whether we found RGB in the JAR file
   */
  private static boolean foundRGBinJAR;

  /**
   * Any exception that occurred trying to read the RGB file
   */
  private static IOException rgbReadException = null;

  /**
   * Where the RGB was found
   */
  private static String whereRGBfound = null;

  /**
   * Named of the file in which X windows colour map is stored (rgb.txt)
   */
  public static final String XCOLOR_NAME_FILE = "rgb.txt";

  /**
   * Array containing possible locations for rgb.txt
   */
  public static final String[] XCOLOR_NAME_FILE_LOCATIONS =
    { "/usr/X11/share/X11", "/usr/lib/X11", "/usr/X11R6/lib/X11", "/usr/X11R6/share/X11" };

  /**
   * Array containing possible environment variables defining the home of the X
   * distribution on this computer (not usually defined, it seems)
   */
  public static final String[] XHOME_ENV_VARS = { "X_HOME", "XHOME", "X" };

  /**
   * Array containing possible subdirectories of the X home where rgb.txt may be
   * found
   */
  public static final String[] XHOME_ENV_VAR_SUBDIRS = { "lib", "share", "lib/X11", "share/X11" };

  /**
   * Internal map to be initialised on class loading
   */
  private static final Map<String, Color> colorMap = readColors();

  /**
   * <!-- main -->
   * 
   * Create a Java Enum containing all the XColors
   * 
   * @param args
   */
  public static void main(String[] args) {
    if(!foundRGB()) {
      System.err.println("Could not load " + XCOLOR_NAME_FILE + " from anywhere: " + rgbReadException);
      System.exit(1);
    }
    System.out.println("/* From " + XCOLOR_NAME_FILE + " found in " + (rgbInJar() ? " jar file" : whereRGBFound())
      + " */");
    System.out.println();
    System.out.println("package uk.ac.macaulay.util;");
    System.out.println();
    System.out.println("import java.awt.Color;");
    System.out.println("import java.util.HashMap;");
    System.out.println("import java.util.Map;");
    System.out.println();
    System.out.println("public enum XColor {");
    HashMap<String, String> keys = new HashMap<String, String>();
    HashMap<String, String> names = new HashMap<String, String>();
    boolean first = true;
    for(String name: colorMap.keySet()) {
      String keysp = name.toUpperCase();
      String[] keywords = keysp.split("\\s+");
      StringBuffer buff = new StringBuffer();
      for(int i = 0; i < keywords.length; i++) {
        if(i > 0) buff.append("_");
        buff.append(keywords[i]);
      }
      String key = buff.toString();
      if(!keys.containsKey(key)) {
        keys.put(key, name);
        if(first) {
          System.out.print("  ");
          first = false;
        }
        else {
          System.out.println(",");
          System.out.print("  ");
        }
        System.out.print(key);
      }
      names.put(name, key);
      System.out.println(";");
    }
    System.out.println();
    System.out.println("  private static Map<String, XColor> map = null;");
    System.out.println();
    System.out.println("  public Color color() {");
    System.out.println("    switch(this) {");
    for(String key: keys.keySet()) {
      Color color = colorMap.get(keys.get(key));
      System.out.println("    case " + key + ":");
      System.out.println("      return new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue()
        + ");");
    }
    System.out.println("    default:");
    System.out.println("      throw new Panic();");
    System.out.println("    }");
    System.out.println("  }");
    System.out.println();
    System.out.println("  public String name() {");
    System.out.println("    switch(this) {");
    for(String key: keys.keySet()) {
      System.out.println("    case " + key + ":");
      System.out.println("      return \"" + keys.get(key) + "\";");
    }
    System.out.println("    default:");
    System.out.println("      throw new Panic();");
    System.out.println("    }");
    System.out.println("  }");
    System.out.println();
    System.out.println("  public static XColor for(String name) {");
    System.out.println("    if(map == null) {");
    for(String name: names.keySet()) {
      System.out.println("      map.put(\"" + name + "\", " + names.get(name) + ");");
    }
    System.out.println("    }");
    System.out.println("    return map.get(name);");
    System.out.println("  }");
    System.out.println("}");
  }

  /**
   * <!-- get -->
   * 
   * Return the Color for the specified name. This will be null if the name
   * isn't found.
   * 
   * @param name the name of the colour
   * @return the Color
   */
  public static Color get(String name) {
    return colorMap.get(name);
  }

  /**
   * <!-- rgbInJar -->
   * 
   * The rgb.txt was loaded via the class loader, and so was found that way.
   * 
   * @return <code>true</code> if rgb.txt was found locally, <code>false</code>
   *         if we had to search for it
   */
  public static boolean rgbInJar() {
    return foundRGBinJAR;
  }

  /**
   * <!-- foundRGB -->
   * 
   * The rgb.txt was successfully loaded
   * 
   * @return <code>true</code> if rgb.txt was successfully loaded.
   */
  public static boolean foundRGB() {
    return foundRGB;
  }

  /**
   * <!-- rgbReadException -->
   * 
   * Any exception that occurred when reading rgb.txt
   * 
   * @return The exception that occurred when reading rgb.txt or
   *         <code>null</code>
   */
  public static IOException rgbReadException() {
    return rgbReadException;
  }

  /**
   * <!-- whereRGBFound -->
   * 
   * Where the RGB was found
   * 
   * @return Where the rgb.txt was found, or <code>null</code> if it wasn't
   */
  public static String whereRGBFound() {
    return whereRGBfound;
  }

  /**
   * <!-- readColors -->
   * 
   * Read in the colours from rgb.txt, a copy of which should be included in the
   * Jar file this class appears in. If not, however, we try looking for the
   * file in some popular predefined locations.
   * 
   * @return A map from String to Color of the colors as they are read in from
   *         rgb.txt, or an empty map if rgb.txt could not be found
   */
  private static Map<String, Color> readColors() {
    ClassLoader cl = XColorName.class.getClassLoader();
    InputStream stream = cl.getResourceAsStream(XCOLOR_NAME_FILE);
    String filename = XCOLOR_NAME_FILE;
    if(stream == null) {
      foundRGBinJAR = false;
      filename = findRGBTXT();
      try {
        if(filename == null) throw new FileNotFoundException();
        stream = new FileInputStream(filename);
      }
      catch(FileNotFoundException e) {
        foundRGB = false;
        rgbReadException = e;
        return new HashMap<String, Color>();
      }
    }
    else {
      foundRGBinJAR = true;
    }
    try {
      whereRGBfound = filename;
      foundRGB = true;
      return readColors(stream, filename);
    }
    catch(IOException e) {
      foundRGB = false;
      rgbReadException = e;
      return new HashMap<String, Color>();
    }
  }

  /**
   * <!-- readColors -->
   * 
   * Read a map of name to colour from any file with a format similar to rgb.txt
   * 
   * @param filename The file to load
   * @return A colour name to Color map
   * @throws IOException
   */
  public static Map<String, Color> readColors(String filename) throws IOException {
    return readColors(new FileInputStream(filename), filename);
  }

  /**
   * <!-- readColors -->
   * 
   * Read a map of name to colour from any stream
   * 
   * @param stream The stream to read from
   * @param filename The name associated with the stream
   * @return The colour map
   * @throws IOException
   */
  private static Map<String, Color> readColors(InputStream stream, String filename) throws IOException {
    return readColors(new BufferedReader(new InputStreamReader(stream)), filename);
  }

  /**
   * <!-- readColors -->
   * 
   * Main read method. The file is assumed to be formatted with # comments, and
   * then three integers for red, green and blue, followed by the name of the
   * colour on the rest of the line
   * 
   * @param bufferedReader The reader to read from
   * @param filename The name associated with the reader
   * @return The map
   * @throws IOException
   */
  private static Map<String, Color> readColors(BufferedReader bufferedReader, String filename) throws IOException {
    TextReader reader = new TextReader(bufferedReader, filename, "rgb.txt");
    Map<String, Color> map = new HashMap<String, Color>();
    while(!reader.eof()) {
      try {
        int r = reader.readInt(EnumSet.of(TextReader.Comment.HASH));
        int g = reader.readInt();
        int b = reader.readInt();
        String name = reader.readLineIgnoreLeadingSpace();
        if(name != null && name.length() > 0) map.put(name, new Color(r, g, b));
      }
      catch(FileFormatException e) {
        if(reader.eof()) break;
        throw e;
      }
    }
    return map;
  }

  /**
   * <!-- findRGBTXT -->
   * 
   * Search in various known locations for rgb.txt
   * 
   * @return A full path to the file, or <code>null</code>
   */
  private static String findRGBTXT() {
    for(int i = 0; i < XCOLOR_NAME_FILE_LOCATIONS.length; i++) {
      File file = new File(rebuildPath(XCOLOR_NAME_FILE_LOCATIONS[i] + "/" + XCOLOR_NAME_FILE));
      if(file.exists() && file.canRead()) return file.getAbsolutePath();
    }
    for(int i = 0; i < XHOME_ENV_VARS.length; i++) {
      String loc = System.getenv(XHOME_ENV_VARS[i]);
      if(loc == null) continue;
      for(int j = 0; j < XHOME_ENV_VAR_SUBDIRS.length; j++) {
        File file = new File(rebuildPath(loc + "/" + XHOME_ENV_VAR_SUBDIRS[j]));
        if(file.exists() && file.canRead()) return file.getAbsolutePath();
      }
    }
    return null;
  }

  /**
   * <!-- rebuildPath -->
   * 
   * Reconstruct a path from a filename. The input is a path separated in UNIX
   * style (/), the output is a path with files separated using whatever the
   * local separator character is.
   * 
   * @param path UNIX-formatted path
   * @return local-formatted path
   */
  private static String rebuildPath(String path) {
    StringBuffer buff = new StringBuffer();
    String[] dirs = path.split("/");
    for(int i = 0; i < dirs.length; i++) {
      if(i > 0) buff.append(System.getProperty("file.separator"));
      if(dirs[i] != null && dirs[i].length() > 0) buff.append(dirs[i]);
    }
    return buff.toString();
  }

}
