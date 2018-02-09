/*
 * uk.ac.macaulay.util: Reflection.java Copyright (C) 2008 Macaulay Institute
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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Reflection
 * 
 * @author Gary Polhill
 * 
 *         A utility class providing some easier to use reflection tools.
 */
public class Reflection {

  /**
   * Private constructor: ensure singleton
   */
  private Reflection() {
  }

  /**
   * <!-- classImplements -->
   * 
   * Check that a class implements an interface. An IllegalArgumentException is
   * thrown if the iface argument is not an interface.
   * 
   * @param clazz The class to check
   * @param iface The interface it implements
   * @return true if the class implements the methods in the interface, false if
   *         not
   * @throws IllegalArgumentException
   */
  public static boolean classImplements(Class<?> clazz, Class<?> iface) throws IllegalArgumentException {
    if(!iface.isInterface()) throw new IllegalArgumentException();
    return classImplements(clazz, iface, new HashSet<Class<?>>());
  }

  /**
   * <!-- classImplements -->
   * 
   * Private method to check whether a class implements all the methods in an
   * interface. Recursively checks all the methods in all interfaces the
   * interface imports.
   * 
   * @param clazz The class to check
   * @param iface The interface with methods the class is being checked for
   * @param visited A record of all the interfaces checked
   * @return true if the class implements all methods in the interface and any
   *         interfaces it imports, false otherwise
   */
  private static boolean classImplements(Class<?> clazz, Class<?> iface, Set<Class<?>> visited) {
    for(Method method: iface.getMethods()) {
      if(!classHasMethod(clazz, method)) return false;
    }
    visited.add(iface);
    for(Class<?> ifaceImports: iface.getInterfaces()) {
      if(visited.contains(ifaceImports)) continue;
      if(!classImplements(clazz, ifaceImports, visited)) return false;
    }
    return true;
  }

  /**
   * <!-- classHasMethod -->
   * 
   * Check that a class implements a method. The class must have the same
   * parameter types, return type and name.
   * 
   * @param clazz The class to check
   * @param method The method
   * @return true if the class implements the method, false otherwise
   */
  public static boolean classHasMethod(Class<?> clazz, Method method) {
    try {
      Method cMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
      if(!subType(cMethod.getReturnType(), method.getReturnType())) {
        return false;
      }
    }
    catch(NoSuchMethodException e) {
      return false;
    }
    return true;
  }

  /**
   * <!-- classPresent -->
   * 
   * Check if a class is present in the current application.
   * 
   * @param name The name of the class to check
   * @return true if the class is present in the application, false otherwise
   */
  public static boolean classPresent(String name) {
    try {
      Class.forName(name);
      return true;
    }
    catch(ClassNotFoundException e) {
      // This is bad form, an anti-pattern, but I don't know how to test for
      // the existence of a class before asking for it, rather than relying on
      // causing an exception to find it is not there. I note, however,
      // that in Hardcore Java (Simmons Jr., 2004. Sebastopol, CA: O'Reilly),
      // example 5-3, pp. 117-118, ignoring ClassNotFoundException is the
      // recommended way to find if a class exists in a package.
    }
    return false;
  }

  /**
   * <!-- subType -->
   * 
   * Check if a class is a subtype or equal to another class. A subtype-or-equal
   * B is true if A extends B or A implements B or A == B
   * 
   * @param clazz1 The possible subtype
   * @param clazz2 The class or interface to check against
   * @return true if clazz1 is a subtype or equal to clazz1
   */
  public static boolean subType(Class<?> clazz1, Class<?> clazz2) {
    if(clazz1.equals(clazz2)) return true;
    Class<?> clazz3 = clazz1;
    while(clazz3 != null) {
      clazz3 = clazz3.getSuperclass();
      if(clazz3 != null && clazz3.equals(clazz2)) return true;
    }
    if(clazz2.isInterface()) return classImplements(clazz1, clazz2);
    return false;
  }

  /**
   * <!-- getFields -->
   * 
   * Return a set of the fields of a class (including inherited fields) that
   * that class can modify. Overridden fields in superclasses are ignored.
   * 
   * @param clazz The class
   * @return A set of fields
   */
  public static Set<Field> getAccessibleFields(Class<?> clazz) {
    return getAccessibleFields(clazz, null);
  }

  /**
   * <!-- getFields -->
   * 
   * Return a set of the fields of a class that the class can modify and have a
   * specified type. Overridden fields in superclasses are ignored.
   * 
   * @param clazz The class
   * @param type The type
   * @return A set of fields
   */
  public static Set<Field> getAccessibleFields(Class<?> clazz, Class<?> type) {
    return getAccessibleFields(clazz, type, false, new HashSet<String>());
  }

  /**
   * <!-- getAccessibleFields -->
   * 
   * Return a set of the fields of a class that the class can modify and have a
   * specified type (which may be null for all types), recursively calling up
   * the class hierarchy to add inherited (but not overridden) fields.
   * 
   * @param clazz The class
   * @param type The type
   * @param superclass True if we are recursing in a superclass of the
   *          originally called class
   * @param names A list of field names already discovered
   * @return A set of fields
   */
  private static Set<Field> getAccessibleFields(Class<?> clazz, Class<?> type, boolean superclass, Set<String> names) {
    // Top of the class hierarchy reached
    if(clazz == null) return new HashSet<Field>();

    Field[] fields = clazz.getDeclaredFields();
    Set<Field> fieldSet = new HashSet<Field>();

    // Loop through all the fields
    for(int i = 0; i < fields.length; i++) {
      // Various conditions not to add a field: it is an overridden field, a
      // private field in a superclass, or it is not of the required type.
      if(names.contains(fields[i].getName())) continue;
      if(superclass && Modifier.isPrivate(fields[i].getModifiers())) {
        // Remember that a class can override a public field to make it
        // private...
        names.add(fields[i].getName());
        continue;
      }
      if(type != null && !subType(fields[i].getType(), type)) continue;

      // Add the field to the set
      fieldSet.add(fields[i]);
      names.add(fields[i].getName());
    }

    // Recurse
    fieldSet.addAll(getAccessibleFields(clazz.getSuperclass(), type, true, names));
    return fieldSet;
  }

  /**
   * <!-- jarPresent -->
   * 
   * @param jar A jar to check for
   * @return <code>true</code> if the jar has been loaded
   */
  public static boolean jarPresent(final String jar) {
    return jarPresent(jar, false);
  }

  /**
   * <!-- jarPresent -->
   * 
   * @param jar A pattern matching one or more jars to check for
   * @return <code>true</code> if a jar matching the pattern has been loaded
   *         (pattern flags are ignored)
   */
  public static boolean jarPresent(final Pattern jar) {
    return jarPresent(jar.pattern(), true);
    // Pattern flags will be ignored.
  }

  /**
   * <!-- jarPatternPresent -->
   * 
   * @param jarPattern A string pattern matching one or more jars to check for
   * @return <code>true</code> if a jar matching the pattern has been loaded
   */
  public static boolean jarPatternPresent(final String jarPattern) {
    return jarPresent(jarPattern, true);
  }

  /**
   * <!-- jarPresent -->
   * 
   * @param jar A pattern or exact name of a jar to look for
   * @param regexp <code>true</code> if <code>jar</code> is a pattern
   * @return <code>true</code> if a matching or equal jar is found
   */
  public static boolean jarPresent(final String jar, final boolean regexp) {
    for(String clazz: System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
      String foundJar = clazz.substring(clazz.lastIndexOf(System.getProperty("file.separator")) + 1);
      if(regexp) {
        if(Pattern.matches("^" + jar + "$", foundJar)) return true;
      }
      else {
        if(foundJar.equals(jar)) return true;
      }
    }
    return false;
  }

  /**
   * <!-- libraryPresent -->
   * 
   * @param lib The name of a library to look for
   * @return <code>true</code> if the library is available (a file with that
   *         name exists in the Java library path)
   */
  public static boolean libraryPresent(final String lib) {
    return libraryPresent(lib, false);
  }

  /**
   * <!-- libraryPresent -->
   * 
   * @param lib A pattern matching the name(s) of libraries to look for (flags
   *          are ignored)
   * @return <code>true</code> if a file matching the pattern exists in the Java
   *         library path
   */
  public static boolean libraryPresent(final Pattern lib) {
    return libraryPresent(lib.pattern(), true);
  }

  /**
   * <!-- libraryPatternPresent -->
   * 
   * @param libPattern A regular expression matching the name(s) of libraries to
   *          look for
   * @return <code>true</code> if a file matching the pattern exists in the Java
   *         library path
   */
  public static boolean libraryPatternPresent(final String libPattern) {
    return libraryPresent(libPattern, true);
  }

  /**
   * <!-- libraryPresent -->
   * 
   * @param lib A name or regular expression of a library to look for
   * @param regexp <code>true</code> if <code>lib</code> is a regular expression
   * @return <code>true</code> if a file with that name or matching the pattern
   *         exists in the Java library path
   */
  public static boolean libraryPresent(final String lib, final boolean regexp) {
    for(String path: System.getProperty("java.library.path").split(System.getProperty("path.separator"))) {
      File f = new File(path);
      if(!f.isDirectory() || !f.canRead()) continue;
      for(File content: f.listFiles()) {
        if(regexp) {
          if(Pattern.matches("^" + lib + "$", content.getName())) return true;
        }
        else {
          if(content.getName().equals(lib) && content.canRead()) return true;
        }
      }
    }
    return false;
  }

}
