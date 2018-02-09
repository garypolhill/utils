/* uk.ac.macaulay.util.test: TableTest.java
 *
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
package uk.ac.macaulay.util.test;

import java.util.ArrayList;

import uk.ac.macaulay.util.Table;
import junit.framework.TestCase;

/**
 * TableTest
 *
 * Class to test the Table class.
 *
 * @author Gary Polhill
 */
public class TableTest extends TestCase {

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#Table(int, int)}.
   */
  public void testTable() {
    Table<String> t = new Table<String>(8, 10);
    assertTrue(t.ncols() == 10);
    assertTrue(t.nrows() == 8);
    int i = 0;
    for(String s: t) {
      assertTrue(s == null);
      i++;
    }
    assertTrue(i == 80);
  }

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#atXY(int, int)}.
   */
  public void testAtxyIntInt() {
    Table<Integer> t = new Table<Integer>(10, 10);
    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < 10; j++) {
        t.atRC(i, j, (i + 1) * (j + 1));
      }
    }
    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < 10; j++) {
        assertTrue(t.atRC(i, j) == (i + 1) * (j + 1));
        assertTrue(t.atXY(j, 9 - i) == (i + 1) * (j + 1));
        assertTrue(t.atXFlipY(j, i) == (i + 1) * (j + 1));
      }
    }
  }

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#atRC2C(int, int, int)}.
   */
  public void testAtrc2c() {
    Table<String> t = new Table<String>(3, 3);
    t.atRC(0, 0, "The");
    t.atRC(0, 1, "quick");
    t.atRC(0, 2, "brown");
    t.atRC(1, 0, "fox");
    t.atRC(1, 1, "jumps");
    t.atRC(1, 2, "over");
    t.atRC(2, 0, "the");
    t.atRC(2, 1, "lazy");
    t.atRC(2, 2, "dog");
    
    ArrayList<String> tqb = t.atRC2C(0, 0, 2);
    assertTrue(tqb.size() == 3);
    assertTrue(tqb.get(0).equals("The"));
    assertTrue(tqb.get(1).equals("quick"));
    assertTrue(tqb.get(2).equals("brown"));
    
    ArrayList<String> ld = t.atRC2C(2, 1, 2);
    assertTrue(ld.size() == 2);
    assertTrue(ld.get(0).equals("lazy"));
    assertTrue(ld.get(1).equals("dog"));
  }

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#atXFlipY(int, int, java.lang.Object)}.
   */
  public void testAtXFlipIntIntT() {
    Table<Double> t = new Table<Double>(1000, 200);
    
    for(int x = 0; x < 200; x++) {
      for(int y = 0; y < 1000; y++) {
        t.atXFlipY(x, y, Math.log((double)((x + 1) * (y + 1))));
      }
    }
    
    for(int x = 0; x < 200; x++) {
      for(int y = 0; y < 1000; y++) {
        assertTrue(t.atXFlipY(x, y) == Math.log((double)((x + 1) * (y + 1))));
      }
    }
  }

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#atXY(int, int, java.lang.Object)}.
   */
  public void testAtXYIntIntT() {
    Table<Double> t = new Table<Double>(1000, 200);
    
    for(int x = 0; x < 200; x++) {
      for(int y = 0; y < 1000; y++) {
        t.atXY(x, y, Math.log((double)((x + 1) * (y + 1))));
      }
    }
    
    for(int x = 0; x < 200; x++) {
      for(int y = 0; y < 1000; y++) {
        assertTrue(t.atXY(x, y) == Math.log((double)((x + 1) * (y + 1))));
      }
    }
  }

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#flatten()}.
   */
  public void testFlatten() {
    Table<Integer> t = new Table<Integer>(20, 3);
    
    int n = 0;
    for(int r = 0; r < 20; r++) {
      for(int c = 0; c < 3; c++) {
        t.atRC(r, c, ++n);
      }
    }
    
    ArrayList<Integer> arr = t.flatten();
    n = 0;
    for(Integer i: arr) {
      assertTrue(i == ++n);
    }
    assertTrue(n == 60);
  }

  /**
   * Test method for {@link uk.ac.macaulay.fearlusOWL.Table#iterator()}.
   */
  public void testIterator() {
    Table<Integer> t = new Table<Integer>(20, 4);
    
    int n = 0;
    for(int r = 0; r < 20; r++) {
      for(int c = 0; c < 4; c++) {
        t.atRC(r, c, ++n);
      }
    }
    
    n = 0;
    for(Integer i: t) {
      assertTrue(i == ++n);
    }
    assertTrue(n == 80);
  }


}
