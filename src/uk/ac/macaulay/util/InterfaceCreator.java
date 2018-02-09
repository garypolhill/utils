/*
 * uk.ac.macaulay.util: InterfaceCreator.java
 * 
 * Copyright (C) 2009 Macaulay Institute
 * 
 * This file is part of Utils.
 * 
 * Utils is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tests. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: Gary Polhill Macaulay Institute, Craigiebuckler,
 * Aberdeen. AB15 8QH. UK. g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Constants;

/**
 * InterfaceCreator
 * 
 * Class to create an interface with get/set methods for a list of parameters.
 * 
 * @author Gary Polhill
 */
public class InterfaceCreator extends ClassLoader {
  /**
   * Array of parameter names
   */
  Map<String, Class<?>> parameters;

  /**
   * Name of the interface
   */
  String iname;

  /**
   * Map from datatype to compiler description of it
   */
  private static HashMap<Class<?>, String> type2desc = new HashMap<Class<?>, String>();
  {
    type2desc.put(Boolean.TYPE, "Z");
    type2desc.put(Character.TYPE, "C");
    type2desc.put(Byte.TYPE, "B");
    type2desc.put(Short.TYPE, "S");
    type2desc.put(Integer.TYPE, "I");
    type2desc.put(Float.TYPE, "F");
    type2desc.put(Long.TYPE, "J");
    type2desc.put(Double.TYPE, "D");
  }

  /**
   * @param iname Interface name
   * @param parameters Map of parameters to type
   */
  public InterfaceCreator(String iname, Map<String, Class<?>> parameters) {
    this.parameters = parameters;
    this.iname = iname;
  }

  /**
   * <!-- dot2slash -->
   * 
   * Create a compiler-compatible name for the class by replacing dots in the
   * class name with slashes.
   * 
   * @param str The name of the class
   * @return Compiler compatible class name
   */
  private static String dot2slash(String str) {
    StringBuffer buf = new StringBuffer();
    String[] parts = str.split("\\.");
    if(parts.length == 0) return str;
    buf.append(parts[0]);
    for(int i = 1; i < parts.length; i++) {
      buf.append("/");
      buf.append(parts[i]);
    }
    return buf.toString();
  }

  /**
   * <!-- toFirstUpper -->
   * 
   * Capitalise the first letter of a parameter so that get and/or set can be
   * put in front of it
   * 
   * @param str Parameter name
   * @return Parameter name with first letter upper case
   */
  private static String toFirstUpper(String str) {
    StringBuffer buf = new StringBuffer();
    char[] chrs = str.toCharArray();
    buf.append(Character.toUpperCase(chrs[0]));
    for(int i = 1; i < chrs.length; i++) {
      buf.append(chrs[i]);
    }
    return buf.toString();
  }

  /**
   * <!-- getTypeDescriptor -->
   * 
   * Get a compiler-formatted type descriptor for a parameter's type. URIs are rendered as Strings.
   * 
   * @param type The type
   * @return The compiler type descriptor
   */
  private static String getTypeDescriptor(Class<?> type) {
    if(type.isPrimitive()) {
      return type2desc.get(type);
    }
    else if(type.equals(URI.class)) {
      return new String("L" + dot2slash(String.class.getName()) + ";");
    }
    else if(!type.isArray()) {
      return new String("L" + dot2slash(type.getName()) + ";");
    }
    return dot2slash(type.getName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.ClassLoader#findClass(java.lang.String)
   * 
   * Create the interface
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class findClass(String name) throws ClassNotFoundException {
    if(name.equals(iname)) {
      // Code for ASM that comes with RePast (for ASM 3.2 class, see Tests project)
      ClassWriter cw = new ClassWriter(false);
      cw.visit(Constants.ACC_PUBLIC + Constants.ACC_ABSTRACT + Constants.ACC_INTERFACE, dot2slash(iname),
          "java/lang/Object", null, null);
      for(String parameter: parameters.keySet()) {
        String capitalFieldName = toFirstUpper(parameter);
        String getMethodName = new String("get" + capitalFieldName);
        String setMethodName = new String("set" + capitalFieldName);
        cw.visitMethod(Constants.ACC_PUBLIC + Constants.ACC_ABSTRACT, getMethodName, "()"
          + getTypeDescriptor(parameters.get(parameter)), null);
        cw.visitMethod(Constants.ACC_PUBLIC + Constants.ACC_ABSTRACT, setMethodName, "("
          + getTypeDescriptor(parameters.get(parameter)) + ")V", null);
      }
      cw.visitEnd();
      byte[] b = cw.toByteArray();
      return defineClass(name, b, 0, b.length);
    }
    else
      return super.findClass(name);
  }
}
