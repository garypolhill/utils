/*
 * uk.ac.macaulay.util: FloatingPointComparisonFactory.java Copyright (C) 2009
 * Macaulay Institute
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * FloatingPointComparisonFactory
 * 
 * @author Gary Polhill
 */
public class FloatingPointComparisonFactory {
  @SuppressWarnings("unchecked")
  private static Class<FloatingPointComparison> getFCmpClass(String name) throws ClassNotFoundException {
    Class<?> cls = Class.forName(name);
    if(!Reflection.classImplements(cls, FloatingPointComparison.class)) {
      throw new IllegalArgumentException("Not the name of a class implementing FloatingPointComparison: " + name);
    }
    return (Class<FloatingPointComparison>)cls;
  }

  public static FloatingPointComparison build(String name) throws ClassNotFoundException, SecurityException,
      NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
      InvocationTargetException {
    Class<FloatingPointComparison> fcmp_cls = getFCmpClass(name);
    Constructor<FloatingPointComparison> cons = fcmp_cls.getConstructor();
    return cons.newInstance();
  }

  public static FloatingPointComparison build(String name, Object... args) throws ClassNotFoundException,
      SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
      IllegalAccessException, InvocationTargetException {
    Class<FloatingPointComparison> fcmp_cls = getFCmpClass(name);
    Class<?> args_cls[] = new Class<?>[args.length];
    for(int i = 0; i < args.length; i++) {
      args_cls[i] = args[i].getClass();
    }
    Constructor<FloatingPointComparison> cons = fcmp_cls.getConstructor(args_cls);
    return cons.newInstance(args);
  }
}
