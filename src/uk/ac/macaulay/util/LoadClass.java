/* uk.ac.macaulay.util: LoadClass.java
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * LoadClass
 * 
 * This is a class to help with loading in a class dynamically.
 * 
 * The code for this class draws on <a href="http://snippets.dzone.com/posts/show/3574">a code snippet from DZone</a>
 * 
 * @author Gary Polhill
 */
public class LoadClass extends java.net.URLClassLoader {

  /**
   * @param urls
   */
  public LoadClass(URL[] urls) {
    super(urls);
  }

  public LoadClass(String className, String jarFile, String path) throws MalformedURLException, ClassNotFoundException {
    this(new URL[0]);
    addFile(path + "/" + jarFile);
    loadClass(className);
  }
  
  public void addFile(String path) throws MalformedURLException {
    String urlPath = new String("jar:file://" + path + "!/");
    addURL(new URL(urlPath));
  }
  
  public static boolean classSearch(String className, String jarFile, Set<URI> search, Map<URI, Set<Exception>> errors) {
    Set<URI> webSearch = new HashSet<URI>();
    for(URI uri: search) {
      if(uri.getScheme().equals("file")) {
        LoadClass lc = searchFor(className, jarFile, uri, uri.getPath(), errors);
        if(lc != null) return true;
      }
      else {
        webSearch.add(uri);
      }
    }
    for(URI uri: webSearch) {
      try {
        Download dl = new Download(uri);
        LoadClass lc = searchFor(className, jarFile, uri, dl.getPath(), errors);
        if(lc != null) return true;
      }
      catch(MalformedURLException e) {
        if(!errors.containsKey(uri)) errors.put(uri, new HashSet<Exception>());
        errors.get(uri).add(e);
      }
      catch(IOException e) {
        if(!errors.containsKey(uri)) errors.put(uri, new HashSet<Exception>());
        errors.get(uri).add(e);
      }
      
    }
    return false;
  }
  
  private static LoadClass searchFor(String className, String jarFile, URI uri, String path, Map<URI, Set<Exception>> errors) {
    try {
      return new LoadClass(className, jarFile, path);
    }
    catch(MalformedURLException e) {
      if(!errors.containsKey(uri)) errors.put(uri, new HashSet<Exception>());
      errors.get(uri).add(e);
    }
    catch(ClassNotFoundException e) {
      if(!errors.containsKey(uri)) errors.put(uri, new HashSet<Exception>());
      errors.get(uri).add(e);
    }
    return null;
  }
}
