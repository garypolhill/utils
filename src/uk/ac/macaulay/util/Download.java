/*
 * uk.ac.macaulay.util: Download.java Copyright (C) 2009 Macaulay Institute
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Download
 * 
 * Class to download a file and save it to a directory (tmpdir by default).
 * 
 * @author Gary Polhill
 */
public class Download {
  /**
   * Location to save the downloaded file to
   */
  String loc;
  
  /**
   * Directory the downloaded file was saved to
   */
  String path;

  Download(URI file) throws IOException {
    this(file.toURL());
  }

  Download(URI file, String dir) throws IOException {
    this(file.toURL(), dir);
  }

  Download(String file) throws IOException {
    this(new URL(file));
  }

  Download(String file, String dir) throws IOException {
    this(new URL(file), dir);
  }

  Download(URL file) throws IOException {
    this(file, System.getProperty("java.io.tmpdir"));
  }

  /**
   * The other constructors all end up calling this one. Those not specifying a
   * directory use java.io.tmpdir. A MalformedURLException will be thrown if the
   * URL's path does not end with a file name. The location the file is saved to
   * is given by the directory + file.separator + file name at the end of the
   * URL path.
   * 
   * @param file URL of the file to download
   * @param dir Directory to save the downloaded file to
   * @throws IOException
   */
  Download(URL file, String dir) throws IOException {
    InputStreamReader read = new InputStreamReader(file.openStream());
    
    String[] path = file.getPath().split("/");
    if(path.length < 1 || path[path.length - 1] == null) {
      throw new MalformedURLException(file.toString());
    }
    
    this.path = dir;
    loc = dir + System.getProperty("file.separator") + path[path.length - 1];
    
    FileWriter write = new FileWriter(loc);
    BufferedReader readbuff = new BufferedReader(read);
    BufferedWriter writebuff = new BufferedWriter(write);
    int c;
    while((c = readbuff.read()) != -1) {
      writebuff.write(c);
    }
    readbuff.close();
    writebuff.close();
  }

  /**
   * <!-- getLocation -->
   * 
   * The location the downloaded file was saved to includes the name of the file
   * itself
   * 
   * @return The location the downloaded file was saved to
   */
  String getLocation() {
    return loc;
  }
  
  /**
   * <!-- getPath -->
   *
   * @return The directory the downloaded file was saved to
   */
  String getPath() {
    return path;
  }
}
