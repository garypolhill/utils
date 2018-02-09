/*
 * uk.ac.macaulay.obiama: Tree.java Copyright (C) 2008 Macaulay Institute
 * 
 * This file is part of obiama-0.1.
 * 
 * obiama-0.1 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * obiama-0.1 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with obiama-0.1. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: Gary Polhill Macaulay Institute, Craigiebuckler,
 * Aberdeen. AB15 8QH. UK. g.polhill@macaulay.ac.uk
 */
package uk.ac.macaulay.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Tree
 * 
 * @author Gary Polhill
 * 
 */
public class CopyOfTree<T> implements Collection<T> {
  Map<T, Node<T>> nodes;
  Node<T> rootNode;

  private class Node<U> {
    U entry = null;
    Set<U> subnodes = null;
    Node<U> supernode = null;

    public Node<U> add(U o) {
      if(subnodes == null) subnodes = new HashSet<U>();
      if(!subnodes.contains(o)) {
        subnodes.add(o);
        Node<U> node = new Node<U>();
        node.entry = o;
        node.supernode = this;
        return node;
      }
      return null;
    }
  }
  
  public class BreadthFirstIterator<S> implements Iterator<T> {
    private Node<T> node;
    private LinkedList<Node<T>> queue;
    
    public BreadthFirstIterator() {
      node = rootNode;
      queue = new LinkedList<Node<T>>();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return node != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public T next() {
      T thisNode = node.entry;
      for(T subNode: node.subnodes) {
        queue.addLast(nodes.get(subNode));
      }
      node = queue.size() > 0 ? queue.removeFirst() : null;
      return thisNode;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      CopyOfTree<T> subTree = getSubTree(node.entry);
      for(T o: subTree) {
        queue.remove(o);
      }
      CopyOfTree.this.remove(node.entry);
    }
    
  }
  
  private class DepthFirstIterator<R> implements Iterator<T> {
    Node<T> node;
    LinkedList<Node<T>> stack;
    
    public DepthFirstIterator() {
      node = rootNode;
      stack = new LinkedList<Node<T>>();
    }
    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return node != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public T next() {
      T thisNode = node.entry;
      for(T subNode: node.subnodes) {
        stack.addFirst(nodes.get(subNode));
      }
      node = stack.size() == 0 ? null : stack.removeFirst();
      return thisNode;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      // TODO Auto-generated method stub
      
    }
    
  }

  /**
   * 
   */
  public CopyOfTree() {
    rootNode = null;
    nodes = new HashMap<T, Node<T>>();
  }

  public CopyOfTree(T root) {
    this();
    add(root);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(T o) {
    if(rootNode == null) {
      rootNode = new Node<T>();
      rootNode.entry = o;
      nodes.put(o, rootNode);
      return true;
    }
    return add(o, rootNode.entry);
  }

  public boolean add(T o, T node) {
    if(nodes.containsKey(o)) return false;
    if(!nodes.containsKey(node)) throw new IllegalArgumentException();
    Node<T> topNode = nodes.get(node);
    Node<T> newNode = topNode.add(o);
    if(newNode == null) return false;
    nodes.put(o, newNode);
    return true;
  }

  public boolean add(CopyOfTree<T> subtree, T node) {
    if(!nodes.containsKey(node)) throw new IllegalArgumentException();
    if(nodes.containsKey(subtree.rootNode.entry))
      throw new IllegalArgumentException();
    add(subtree.rootNode.entry, node);
    for(Iterator<T> i = subtree.iterator(subtree.rootNode.entry); i.hasNext();) {
      add(subtree.getSubTree(i.next()), subtree.rootNode.entry);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#clear()
   */
  public void clear() {
    rootNode = null;
    nodes.clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#contains(java.lang.Object)
   */
  public boolean contains(Object o) {
    return nodes.containsKey(o);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#isEmpty()
   */
  public boolean isEmpty() {
    return rootNode == null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#iterator()
   */
  public Iterator<T> iterator() {
    return nodes.keySet().iterator();
  }

  public Iterator<T> iterator(T node) {
    if(!nodes.containsKey(node)) return null;
    return nodes.get(node).subnodes.iterator();
  }
  
  public Iterator<T> depthFirstIterator() {
    return new DepthFirstIterator<T>();
  }
  
  public Iterator<T> breadthFirstIterator() {
    return new BreadthFirstIterator<T>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#remove(java.lang.Object)
   */
  public boolean remove(Object o) {
    return removeRetain(o, null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#size()
   */
  public int size() {
    return nodes.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#toArray()
   */
  public Object[] toArray() {
    return nodes.keySet().toArray();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#toArray(T[])
   */
  public <V> V[] toArray(V[] a) {
    return nodes.keySet().toArray(a);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  public boolean addAll(Collection<? extends T> c) {
    return addAll(c, rootNode.entry);
  }

  public boolean addAll(Collection<? extends T> c, T node) {
    boolean retval = false;
    for(T o: c) {
      retval |= add(o, node);
    }
    return retval;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
  public boolean containsAll(Collection<?> c) {
    if(c == null || c.size() == 0) return false;
    boolean retval = true;
    for(Object o: c) {
      retval &= contains(o);
    }
    return retval;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#removeAll(java.util.Collection)
   */
  public boolean removeAll(Collection<?> c) {
    if(c == null || c.size() == 0) return false;
    boolean retval = false;
    for(Object o: c) {
      retval |= remove(o);
    }
    return retval;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#retainAll(java.util.Collection)
   */
  public boolean retainAll(Collection<?> c) {
    if(c == null || c.size() == 0) {
      clear();
      return true;
    }
    boolean retval = false;
    for(T o: nodes.keySet()) {
      if(!c.contains(o)) retval |= removeRetain(o, c);
    }
    return retval;
  }

  private boolean removeRetain(Object o, Collection<?> c) {
    if(!nodes.containsKey(o)) return false;
    Set<T> subnodes = nodes.get(o).subnodes;
    for(T subnode: subnodes) {
      if(c != null && c.contains(subnode)) {
        throw new IllegalArgumentException();
      }
      remove(subnode);
    }
    nodes.remove(o);
    if(rootNode.entry.equals(o)) rootNode = null;
    return true;
  }

  public CopyOfTree<T> getSubTree(T node) {
    if(!nodes.containsKey(node)) return null;
    CopyOfTree<T> sub = new CopyOfTree<T>(node);
    for(Iterator<T> i = iterator(node); i.hasNext();) {
      sub.add(getSubTree(i.next()), node);
    }
    return sub;
  }

  public Set<T> getSuperNodes(T node) {
    if(!nodes.containsKey(node)) return null;
    if(node.equals(rootNode.entry)) return new HashSet<T>();
    T supernode = nodes.get(node).supernode.entry;
    HashSet<T> supers = new HashSet<T>(Collections.singleton(supernode));
    supers.addAll(getSuperNodes(supernode));
    return supers;
  }
  
  public T getSuperNode(T node) {
    if(!nodes.containsKey(node)) throw new IllegalArgumentException();
    if(node.equals(rootNode.entry)) return null;
    return nodes.get(node).supernode.entry;
  }
  
  public T getRootNode() {
    return rootNode == null ? null : rootNode.entry;
  }
  
  public boolean isLeafNode(T node) {
    if(!nodes.containsKey(node)) throw new IllegalArgumentException();
    return nodes.get(node).subnodes == null;
  }
}
