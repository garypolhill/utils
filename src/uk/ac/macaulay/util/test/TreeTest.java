/*
 * uk.ac.macaulay.util.test: TreeTest.java Copyright (C) 2008 Macaulay Institute
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
package uk.ac.macaulay.util.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import uk.ac.macaulay.util.Tree;
import junit.framework.TestCase;

/**
 * TreeTest
 * 
 * @author Gary Polhill
 * 
 */
public class TreeTest extends TestCase {

  /**
   * @param name
   */
  public TreeTest(String name) {
    super(name);
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#Tree()}.
   */
  public void testTree() {
    Tree<String> tree = new Tree<String>();
    assertEquals(0, tree.size());
    for(String s: tree) {
      fail("Should not be any members: " + s);
    }
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#Tree(java.lang.Object)}.
   */
  public void testTreeT() {
    Tree<String> tree = new Tree<String>("Baum");
    assertEquals(1, tree.size());
    boolean once = false;
    for(String s: tree) {
      assertFalse(once);
      once = true;
      assertEquals("Baum", s);
    }
    assertTrue(once);
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#add(java.lang.Object)}.
   */
  public void testAddT() {
    Tree<Integer> tree = new Tree<Integer>(0);
    assertTrue(tree.add(1));
    assertTrue(tree.add(2));
    assertTrue(tree.add(3));
    assertFalse(tree.add(1));
    assertEquals(4, tree.size());
    int[] j = { -1, 1, 1, 1 };
    for(Iterator<Integer> i = tree.subIterator(); i.hasNext();) {
      int k = i.next();
      assertTrue(k >= 1 && k <= 3);
      assertTrue(j[k] == 1);
      j[k]--;
    }
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#add(java.lang.Object, java.lang.Object)}.
   */
  public void testAddTT() {
    Tree<Double> tree = new Tree<Double>(0.0);
    assertTrue(tree.add(1.0));
    assertTrue(tree.add(2.0, 1.0));
    assertTrue(tree.add(3.0, 1.0));
    assertFalse(tree.add(2.0, 0.0));
    try {
      tree.add(1.0, 3.0);
      fail("No exception thrown when adding node to subtree with supertree " + "already having the node");
    }
    catch(IllegalArgumentException e) {
      assertTrue(true);
    }
    assertEquals(0.0, tree.getSuperNode(1.0));
    assertEquals(1.0, tree.getSuperNode(2.0));
    assertEquals(1.0, tree.getSuperNode(3.0));
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#add(uk.ac.macaulay.util.Tree, java.lang.Object)}
   * .
   */
  public void testAddTreeOfTT() {
    Tree<Double> tree = new Tree<Double>(0.0);
    assertTrue(tree.add(1.0));
    assertTrue(tree.add(2.0));
    assertTrue(tree.add(3.0));
    assertEquals(4, tree.size());
    Tree<Double> tree2 = new Tree<Double>(4.0);
    assertTrue(tree2.add(5.0));
    assertTrue(tree2.add(6.0));
    assertTrue(tree2.add(7.0, 6.0));
    assertEquals(4, tree2.size());
    Tree<Double> tree3 = new Tree<Double>(6.0);
    assertTrue(tree3.add(7.0));
    assertEquals(2, tree3.size());
    assertFalse(tree2.add(tree3));
    assertEquals(4, tree2.size());
    assertTrue(tree.add(tree2, 3.0));
    assertEquals(8, tree.size());
    double[] d = { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 };
    ArrayList<Double> arr = tree.toBreadthFirstArray();
    assertEquals(d.length, arr.size());
    for(int i = 0; i < d.length; i++) {
      assertEquals(d[i], arr.get(i));
    }
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#clear()}.
   */
  public void testClear() {
    Tree<String> tree = new Tree<String>("root");
    assertTrue(tree.add("branch1"));
    assertTrue(tree.add("branch2"));
    assertTrue(tree.add("branch3"));
    tree.clear();
    assertEquals(0, tree.size());
    assertNull(tree.getName());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#contains(java.lang.Object)}
   * .
   */
  public void testContains() {
    Tree<String> tree = new Tree<String>("root");
    assertTrue(tree.add("branch1"));
    assertTrue(tree.add("branch2"));
    assertTrue(tree.add("branch3"));
    assertTrue(tree.contains("root"));
    assertTrue(tree.contains("branch1"));
    assertTrue(tree.contains("branch2"));
    assertTrue(tree.contains("branch3"));
    StringBuffer b = new StringBuffer();
    b.append("b");
    b.append("r");
    b.append("a");
    b.append("n");
    b.append("c");
    b.append("h");
    for(int i = 1; i <= 3; i++) {
      StringBuffer c = new StringBuffer(b);
      c.append(i);
      assertTrue(tree.contains(c.toString()));
    }
    assertFalse(tree.contains(2));
    assertFalse(tree.contains(new HashSet<Object>()));
    assertFalse(tree.contains("bibble"));
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#isEmpty()}.
   */
  public void testIsEmpty() {
    Tree<String> tree = new Tree<String>();
    assertTrue(tree.isEmpty());
    assertTrue(tree.add("root"));
    assertTrue(tree.add("branch1"));
    assertTrue(tree.add("branch2"));
    assertTrue(tree.add("branch3"));
    assertFalse(tree.isEmpty());
    tree.clear();
    assertTrue(tree.isEmpty());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#iterator()}.
   */
  public void testIterator() {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for(int i = 0; i < 100; i++) {
      nodes.add(i);
    }
    Tree<Integer> tree = new Tree<Integer>(0);
    for(int i = 1; i < 100; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      assertTrue(tree.add(i, n));
    }
    for(int i: tree) {
      assertTrue(nodes.contains(i));
      nodes.remove(i);
    }
    assertEquals(0, nodes.size());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#iterator(java.lang.Object)}
   * .
   */
  public void testIteratorT() {
    Tree<String> tree = new Tree<String>("root");
    assertTrue(tree.add("branch1"));
    assertTrue(tree.add("branch2"));
    assertTrue(tree.add("branch3"));
    assertTrue(tree.add("leaf1.1", "branch1"));
    assertTrue(tree.add("leaf1.2", "branch1"));
    assertTrue(tree.add("leaf1.3", "branch1"));
    assertTrue(tree.add("leaf2.1", "branch2"));
    assertTrue(tree.add("leaf2.2", "branch2"));
    assertTrue(tree.add("leaf2.3", "branch2"));
    assertTrue(tree.add("leaf3.1", "branch3"));
    assertTrue(tree.add("leaf3.2", "branch3"));
    assertTrue(tree.add("leaf3.3", "branch3"));
    HashSet<String> nodes = new HashSet<String>();
    nodes.add("branch1");
    nodes.add("leaf1.1");
    nodes.add("leaf1.2");
    nodes.add("leaf1.3");
    for(Iterator<String> i = tree.iterator("branch1"); i.hasNext();) {
      String s = i.next();
      assertTrue(nodes.contains(s));
      nodes.remove(s);
    }
    assertEquals(0, nodes.size());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#remove(java.lang.Object)}.
   */
  public void testRemove() {
    Tree<String> tree = new Tree<String>("root");
    assertTrue(tree.add("branch1"));
    assertTrue(tree.add("branch2"));
    assertTrue(tree.add("branch3"));
    assertTrue(tree.add("leaf1.1", "branch1"));
    assertTrue(tree.add("leaf1.2", "branch1"));
    assertTrue(tree.add("leaf1.3", "branch1"));
    assertTrue(tree.add("leaf2.1", "branch2"));
    assertTrue(tree.add("leaf2.2", "branch2"));
    assertTrue(tree.add("leaf2.3", "branch2"));
    assertTrue(tree.add("leaf3.1", "branch3"));
    assertTrue(tree.add("leaf3.2", "branch3"));
    assertTrue(tree.add("leaf3.3", "branch3"));
    HashSet<String> nodes = new HashSet<String>();
    nodes.add("root");
    nodes.add("branch1");
    nodes.add("leaf1.1");
    nodes.add("leaf1.2");
    nodes.add("leaf1.3");
    nodes.add("branch2");
    nodes.add("leaf2.1");
    nodes.add("leaf2.2");
    nodes.add("leaf2.3");
    nodes.add("branch3");
    nodes.add("leaf3.1");
    nodes.add("leaf3.2");
    nodes.add("leaf3.3");
    assertTrue(tree.containsAll(nodes));
    nodes.remove("leaf3.3");
    assertTrue(tree.remove("leaf3.3"));
    assertTrue(tree.containsAll(nodes));
    assertFalse(tree.contains("leaf3.3"));
    assertFalse(tree.remove("leaf3.4"));
    assertFalse(tree.remove(1));
    nodes.remove("branch1");
    nodes.remove("leaf1.1");
    nodes.remove("leaf1.2");
    nodes.remove("leaf1.3");
    assertTrue(tree.remove("branch1"));
    assertTrue(tree.containsAll(nodes));
    assertFalse(tree.contains("branch1"));
    assertFalse(tree.contains("leaf1.1"));
    assertFalse(tree.contains("leaf1.2"));
    assertFalse(tree.contains("leaf1.3"));
    assertTrue(tree.remove("root"));
    assertEquals(0, tree.size());
    assertTrue(tree.isEmpty());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#size()}.
   */
  public void testSize() {
    Tree<Integer> tree = new Tree<Integer>(0);
    assertEquals(1, tree.size());
    assertTrue(tree.add(1));
    for(int i = 2; i < 100; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 1 ? 1 : n;
      assertEquals(i, tree.size());
      assertTrue(tree.add(i, n));
      assertEquals(i + 1, tree.size());
    }
    tree.remove(1);
    assertEquals(1, tree.size());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#toArray()}.
   */
  public void testToArray() {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for(int i = 0; i < 100; i++) {
      nodes.add(i);
    }
    Tree<Integer> tree = new Tree<Integer>(0);
    for(int i = 1; i < 100; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      assertTrue(tree.add(i, n));
    }
    Object[] arr = tree.toArray();
    for(Object i: arr) {
      assertTrue(i instanceof Integer);
      assertTrue(nodes.contains(i));
      nodes.remove(i);
    }
    assertEquals(0, nodes.size());
  }

  /**
   * Test method for {@link uk.ac.macaulay.util.Tree#toArray(V[])}.
   */
  public void testToArrayVArray() {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for(int i = 0; i < 100; i++) {
      nodes.add(i);
    }
    Tree<Integer> tree = new Tree<Integer>(0);
    for(int i = 1; i < 100; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      assertTrue(tree.add(i, n));
    }
    Integer[] arr = new Integer[100];
    arr = tree.toArray(arr);
    for(Integer i: arr) {
      assertTrue(nodes.contains(i));
      nodes.remove(i);
    }
    assertEquals(0, nodes.size());
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#addAll(java.util.Collection)}.
   */
  public void testAddAllCollectionOfQextendsT() {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for(int i = 1; i < 100; i++) {
      nodes.add(i);
    }
    Tree<Integer> tree = new Tree<Integer>(0);
    assertTrue(tree.addAll(nodes));
    assertFalse(tree.addAll(nodes));
    assertTrue(tree.containsAll(nodes));
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#addAll(java.util.Collection, java.lang.Object)}
   * .
   */
  public void testAddAllCollectionOfQextendsTT() {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for(int i = 2; i < 100; i++) {
      nodes.add(i);
    }
    Tree<Integer> tree = new Tree<Integer>(0);
    assertTrue(tree.add(1));
    assertTrue(tree.addAll(nodes, 1));
    assertFalse(tree.addAll(nodes));
    assertTrue(tree.containsAll(nodes));
    Tree<Integer> tree1 = tree.getSubTree(1);
    assertTrue(tree1.containsAll(nodes));
    assertFalse(tree1.contains(0));
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#containsAll(java.util.Collection)}.
   */
  public void testContainsAll() {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for(int i = 0; i < 100; i++) {
      nodes.add(i);
    }
    Tree<Integer> tree = new Tree<Integer>(0);
    for(int i = 1; i < 100; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      assertTrue(tree.add(i, n));
    }
    assertTrue(tree.containsAll(nodes));
    nodes.remove(90);
    assertTrue(tree.containsAll(nodes));
    nodes.add(101);
    assertFalse(tree.containsAll(nodes));
    assertTrue(tree.containsAll(Arrays.asList(10, 10, 10, 10, 11, 12, 13, 14, 15, 16, 16)));
    assertFalse(tree.containsAll(Arrays.asList(10, 10, 10, 101, 11, 12, 13, 14, 15, 16, 16)));
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#removeAll(java.util.Collection)}.
   */
  public void testRemoveAll() {
    Tree<Integer> tree = new Tree<Integer>(0);
    HashSet<Integer> eighties_victims = new HashSet<Integer>();
    HashSet<Integer> kept = new HashSet<Integer>();
    kept.add(0);
    for(int i = 1; i < 150; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      if(i >= 80 && i <= 89) {
        if(n >= 80 && n <= 89) n = 79;
      }
      else {
        kept.add(i);
      }
      if(n >= 80 && n <= 89 || eighties_victims.contains(n)) {
        eighties_victims.add(i);
        kept.remove(i);
      }
      assertTrue(tree.add(i, n));
    }
    int[] arr = new int[] { 80, 81, 82, 83, 84, 85, 86, 87, 88, 89 };
    assertTrue(tree.removeAll(Arrays.asList(80, 81, 82, 83, 84, 85, 86, 87, 88, 89)));
    assertFalse(tree.removeAll(Arrays.asList(80, 81, 82, 83, 84, 85, 86, 87, 88, 89)));
    for(int i: arr) {
      assertFalse(tree.contains(i));
    }
    for(int i: eighties_victims) {
      assertFalse(tree.contains(i));
    }
    assertTrue(tree.containsAll(kept));
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#retainAll(java.util.Collection)}.
   */
  public void testRetainAll() {
    Tree<Integer> tree = new Tree<Integer>(0);
    HashSet<Integer> eighties_victims = new HashSet<Integer>();
    HashSet<Integer> kept = new HashSet<Integer>();
    kept.add(0);
    for(int i = 1; i < 150; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      n = (i >= 90 && i <= 99) ? (int)(Math.random() * 70.0) : n;
      n = (i >= 140 && i <= 149) ? 90 + (int)(Math.random() * 10.0) : n;
      if((i < 140 || i > 149) && (n >= 90 && n <= 99)) {
        n = (int)(Math.random() * 60.0);
      }
      if(i >= 80 && i <= 89) {
        if(n >= 80 && n <= 89) n = 79;
        eighties_victims.add(i);
      }
      else {
        kept.add(i);
      }
      if(n >= 80 && n <= 89 || eighties_victims.contains(n)) {
        eighties_victims.add(i);
        kept.remove(i);
      }
      assertTrue(tree.add(i, n));
    }
    int[] arr = new int[] { 80, 81, 82, 83, 84, 85, 86, 87, 88, 89 };
    assertTrue(tree.retainAll(kept));
    assertFalse(tree.retainAll(kept));
    for(int i: arr) {
      assertFalse(tree.contains(i));
    }
    for(int i: eighties_victims) {
      assertFalse(tree.contains(i));
    }
    assertTrue(tree.containsAll(kept));
    int[] nineties = new int[] { 90, 91, 92, 93, 94, 95, 96, 97, 98, 99 };
    for(int i: nineties) {
      kept.remove(i);
    }
    try {
      tree.retainAll(kept);
      fail("Should not be possible to retain all in kept because 14x nodes " + "inherited from 9x nodes");
    }
    catch(IllegalArgumentException e) {
      int i = Integer.parseInt(e.getMessage());
      assertEquals(14, i / 10);
    }
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#getSubTree(java.lang.Object)}.
   */
  public void testGetSubTree() {
    Tree<Integer> tree = new Tree<Integer>(0);
    HashSet<Integer> tenTreeNodes = new HashSet<Integer>();
    tenTreeNodes.add(10);
    for(int i = 1; i < 1000; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      if(i > 10 && Math.random() < 0.1) {
        LinkedList<Integer> tenTreeList = new LinkedList<Integer>(tenTreeNodes);
        Collections.shuffle(tenTreeList);
        n = tenTreeList.removeFirst();
      }
      assertTrue(tree.add(i, n));
      if(tenTreeNodes.contains(n)) tenTreeNodes.add(i);
    }
    Tree<Integer> tenTree = tree.getSubTree(10);
    assertTrue(tenTree.containsAll(tenTreeNodes));
    for(int i = 0; i < 1000; i++) {
      if(!tenTreeNodes.contains(i)) assertFalse(tenTree.contains(i));
    }
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#getSuperNodes(java.lang.Object)}.
   */
  public void testGetSuperNodes() {
    Tree<Integer> tree = new Tree<Integer>(0);
    assertTrue(tree.contains(0));
    HashMap<Integer, Integer> supers = new HashMap<Integer, Integer>();
    for(int i = 1; i < 1000; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      assertTrue(tree.add(i, n));
      supers.put(i, n);
    }
    for(int i = 1; i < 1000; i++) {
      HashSet<Integer> mysupers = new HashSet<Integer>();
      int j = i;
      while(supers.containsKey(j)) {
        j = supers.get(j);
        mysupers.add(j);
      }
      Set<Integer> treesupers = tree.getSuperNodes(i);
      assertTrue(treesupers.containsAll(mysupers));
      assertTrue(mysupers.containsAll(treesupers));
    }
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.Tree#getSuperNode(java.lang.Object)}.
   */
  public void testGetSuperNode() {
    Tree<Integer> tree = new Tree<Integer>(0);
    assertTrue(tree.contains(0));
    HashMap<Integer, Integer> supers = new HashMap<Integer, Integer>();
    for(int i = 1; i < 1000; i++) {
      int n = (int)(Math.random() * (double)(i - 1));
      n = n < 0 ? 0 : n;
      assertTrue(tree.add(i, n));
      supers.put(i, n);
    }
    for(int i = 1; i < 1000; i++) {
      assertEquals(supers.get(i), tree.getSuperNode(i));
    }
    assertNull(tree.getSuperNode());
  }

}
