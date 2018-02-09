/*
 * uk.ac.macaulay.util: Tree.java Copyright (C) 2008 Macaulay Institute
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Tree
 * 
 * @author Gary Polhill
 * 
 * A class implementing a tree datastructure, effectively an acyclic digraph
 * with no repeated nodes.
 */
public class Tree<T> implements Collection<T> {
  /**
   * The nodes in the tree, as a map from object of type T to subtree
   */
  Map<T, Tree<T>> nodes;

  /**
   * The name of this node
   */
  T root;

  /**
   * A set of subnodes for this node
   */
  Set<T> subnodes = null;

  /**
   * The supernode of this node
   */
  Tree<T> supernode = null;

  /**
   * Default constructor for the tree class, creating an empty tree
   */
  public Tree() {
    root = null;
    nodes = new HashMap<T, Tree<T>>();
    subnodes = new LinkedHashSet<T>();
  }

  /**
   * Constructor taking a root node as argument
   * 
   * @param root The root node for the tree
   */
  public Tree(T root) {
    this();
    this.root = root;
    this.supernode = null;
    addNode(root, this);
  }

  /**
   * Constructor taking a root node for this tree as argument and a tree to
   * attach it to
   * 
   * @param root The root node for this tree
   * @param supernode The tree to attach this tree to
   */
  public Tree(T root, Tree<T> supernode) {
    this(root);
    supernode.add(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(T o) {
    // Handle empty case
    if(root == null) {
      root = o;
      addNode(o, this);
      return true;
    }
    // Check if node already added somewhere in this tree
    if(nodes.containsKey(o)) return false;
    // Check if node already in the tree anywhere (so can't add it)
    if(superContains(o)) throw new IllegalArgumentException(o.toString());
    // Create the new node and add it
    Tree<T> newTree = new Tree<T>(o);
    newTree.supernode = this;
    subnodes.add(o);
    addNode(o, newTree);
    return true;
  }

  /**
   * add
   * 
   * Add the object to the existing node. Throws an exception if the node to
   * which the object is to be added is not present in the tree.
   * 
   * @param o The object to add
   * @param node The existing node
   * @return true if the tree was modified
   * @throws NoSuchElementException
   */
  public boolean add(T o, T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).add(o);
  }

  /**
   * add
   * 
   * Add a subtree to this node. None of the elements in the subtree can exist
   * in this tree or its supertree, unless they appear in the same position
   * already.
   * 
   * @param subtree The subtree to add
   * @return true if this caused a change to the tree, false otherwise
   * @throws IllegalArgumentException
   */
  public boolean add(Tree<T> subtree) {
    // Deal with the case that the root of the subtree is already a
    // subnode of this tree
    if(subnodes.contains(subtree.root)) {
      boolean retval = false;
      for(T subsubtree: subtree.subnodes) {
        // Add the subtrees of the argument recursively to the already
        // existing subnode
        retval |= nodes.get(subtree.root).add(subtree.nodes.get(subsubtree));
      }
      return retval;
    }
    // Check that the root node of the subtree isn't already in
    // the tree that this tree is a part of
    if(superContains(subtree.root)) {
      throw new IllegalArgumentException(subtree.root.toString());
    }
    // Add the subtree root
    subnodes.add(subtree.root);
    subtree.supernode = this;
    addNode(subtree);
    return true;
  }

  /**
   * add
   * 
   * Add the subtree to the existing node. Throws an exception if the node to
   * which the subtree is to be added is not present in the tree.
   * 
   * @param subtree The subtree to add
   * @param node The node to add it to
   * @return true if the tree was modified
   * @throws NoSuchElementException
   */
  public boolean add(Tree<T> subtree, T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).add(subtree);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  public boolean addAll(Collection<? extends T> c) {
    boolean retval = false;
    for(T o: c) {
      retval |= add(o);
    }
    return retval;
  }

  /**
   * addAll
   * 
   * Add all the elements in the collection to the specified node, which must
   * exist
   * 
   * @param c The collection to add
   * @param node The node to add them to
   * @return true if the node was modified to add the collection
   * @throws NoSuchElementException
   */
  public boolean addAll(Collection<? extends T> c, T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).addAll(c);
  }

  /**
   * addNode
   * 
   * Private method to add a node to a tree, recursively calling super-nodes to
   * add the node to those trees too.
   * 
   * @param node The node to add
   * @param tree The tree to add the node to
   */
  private void addNode(T node, Tree<T> tree) {
    nodes.put(node, tree);
    if(supernode != null) supernode.addNode(node, tree);
  }

  /**
   * addNode
   * 
   * Private method to add all the nodes in a tree to this tree, recursing
   * top-down through the tree, and calling addNode(T, Tree<T>) to add the
   * nodes one by one.
   * 
   * @param tree
   */
  private void addNode(Tree<T> tree) {
    addNode(tree.root, tree);
    for(T node: tree.subnodes) {
      addNode(tree.nodes.get(node));
    }
  }

  /**
   * breadthFirstIterator
   * 
   * Return an iterator in breadth-first search order of the tree
   * 
   * @return
   */
  public Iterator<T> breadthFirstIterator() {
    return new BreadthFirstIterator<T>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#clear()
   */
  public void clear() {
    root = null;
    supernode = null;
    subnodes.clear();
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
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
  public boolean containsAll(Collection<?> c) {
    if(c == null || c.size() == 0) return false;
    boolean retval = true;
    for(Object o: c) {
      retval &= nodes.containsKey(o);
    }
    return retval;
  }

  /**
   * depthFirstIterator
   * 
   * Return an iterator in depth-first search order of the tree
   * 
   * @return The iterator
   */
  public Iterator<T> depthFirstIterator() {
    return new DepthFirstIterator<T>();
  }

  /**
   * getLeafNodes
   * 
   * Return a linked list of leaf nodes from this tree
   * 
   * @return list
   */
  public LinkedList<T> getLeafNodes() {
    LinkedList<T> arr = new LinkedList<T>();
    for(Iterator<T> ix = depthFirstIterator(); ix.hasNext();) {
      Tree<T> node = nodes.get(ix.next());
      if(node.isLeaf()) arr.addLast(node.root);
    }
    return arr;
  }

  /**
   * getRoot
   * 
   * @return The root node of the tree of which this tree is a part, or null if
   *         this tree is the root
   */
  public Tree<T> getRoot() {
    if(supernode == null) return this;
    return supernode.getRoot();
  }

  /**
   * getName
   * 
   * @return Return the name of this node (the object of type T associated with
   *         it)
   */
  public T getName() {
    return root;
  }

  /**
   * getSubTree
   * 
   * @param node The node to get the subtree of (an exception is thrown if this
   *          node does not exist)
   * @return The subtree associated with the node
   * @throws NoSuchElementException
   */
  public Tree<T> getSubTree(T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node);
  }

  /**
   * getSuperNode
   * 
   * Return the supernode of this node, or null if there isn't one
   * 
   * @return The supernode of this node
   */
  public T getSuperNode() {
    return supernode == null ? null : supernode.root;
  }

  /**
   * getSuperNode
   * 
   * Return the supernode of the node argument, or null if the argument has no
   * supernode. An exception is thrown if the argument doesn't exist
   * 
   * @param node The node to get the supernode of
   * @return The supernode of the node or null if it has none
   * @throws NoSuchElementException
   */
  public T getSuperNode(T node) {
    Tree<T> nodeSuper = getSuperTree(node);
    return nodeSuper == null ? null : nodeSuper.root;
  }

  /**
   * getSuperNodes
   * 
   * Get all the supernodes of this node, up to the root node
   * 
   * @return A LinkedHashSet of supernodes of this node, of which the root node
   *         will be the last member
   */
  public Set<T> getSuperNodes() {
    LinkedHashSet<T> supers = new LinkedHashSet<T>();
    Tree<T> aSuperNode = supernode;
    while(aSuperNode != null) {
      supers.add(aSuperNode.root);
      aSuperNode = aSuperNode.supernode;
    }
    return supers;
  }

  /**
   * getSuperNodes
   * 
   * Get all the supernodes of the node argument, which must exist in this tree.
   * 
   * @param node The node to get the supernodes of
   * @return A LinkedHashSet of the supernodes of this node, of which the root
   *         node will be the last member
   * @throws NoSuchElementException
   */
  public Set<T> getSuperNodes(T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).getSuperNodes();
  }

  /**
   * getSuperTree
   * 
   * Get the tree immediately above this one, or null if this is the root
   * 
   * @return The tree immediately above this node, or null if this is the root
   */
  public Tree<T> getSuperTree() {
    return supernode;
  }

  /**
   * getSuperTree
   * 
   * Get the tree immediately above the specified node, or null if it is the
   * root node. The node must exist in the tree.
   * 
   * @param node The node to get the super-tree of
   * @return The super-tree of the node, or null if the node is a root node
   * @throws NoSuchElementException
   */
  public Tree<T> getSuperTree(T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).getSuperTree();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#isEmpty()
   */
  public boolean isEmpty() {
    return root == null;
  }

  /**
   * isLeaf
   * 
   * @return Whether or not this node is a leaf node
   */
  public boolean isLeaf() {
    return subnodes.size() == 0;
  }

  /**
   * isLeaf
   * 
   * @param node The node to check, which must exist in this tree
   * @return Whether or not the node argument is a leaf node
   * @throws NoSuchElementException
   */
  public boolean isLeaf(T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).isLeaf();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#iterator()
   */
  public Iterator<T> iterator() {
    return nodes.keySet().iterator();
  }

  /**
   * iterator
   * 
   * Iterator through the subtree of the node given as argument, which must
   * exist
   * 
   * @param node The node to iterate through
   * @return The iterator
   * @throws NoSuchElementException
   */
  public Iterator<T> iterator(T node) {
    if(!nodes.containsKey(node))
      throw new NoSuchElementException(node.toString());
    return nodes.get(node).iterator();
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

  /**
   * removeNode
   * 
   * Private method to remove a node from the tree, recursively calling
   * super-nodes to remove the node from those trees too.
   * 
   * @param node
   */
  private void removeNode(T node) {
    nodes.remove(node);
    if(subnodes.contains(node)) subnodes.remove(node);
    if(supernode != null) supernode.removeNode(node);
  }

  /**
   * removeRetain
   * 
   * Remove a node retaining all elements in the collection. Removing a node
   * means also removing its subtrees. If any of the subtrees contain a node to
   * be kept, then an exception is thrown.
   * 
   * @param o The node to remove
   * @param c The collection of nodes to retain
   * @return true if the node is present and removed, false if not
   * @throws IllegalArgumentException
   */
  private boolean removeRetain(Object o, Collection<?> c) {
    // Check if the node to remove is present. If not, then there's no
    // change
    if(!nodes.containsKey(o)) return false;
    // Find the node to remove and remove it
    return nodes.get(o).removeRetain(c);
  }

  /**
   * removeRetain
   * 
   * Remove this node, with elements to be retained in c. If this node or any of
   * its subnodes contains a member of c, then an exception is thrown
   * 
   * @param c The elements to be retained
   * @return true
   * @throws IllegalArgumentException
   */
  private boolean removeRetain(Collection<?> c) {
    LinkedList<T> subs = new LinkedList<T>(subnodes);
    // Remove the subnodes recursively
    for(T subnode: subs) {
      nodes.get(subnode).removeRetain(c);
    }
    // Confirm that this node isn't one to be kept
    if(c != null && c.contains(root)) {
      throw new IllegalArgumentException(root.toString());
    }
    // Remove references to the nodes in super-trees
    removeNode(root);
    root = null;
    return true;
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
    HashSet<T> cpnodes = new HashSet<T>();
    cpnodes.addAll(nodes.keySet());
    // Prevent ConcurrentModificationException by looping through a collection
    // independent of the node set
    for(T o: cpnodes) {
      if(!c.contains(o)) retval |= removeRetain(o, c);
    }
    return retval;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#size()
   */
  public int size() {
    return nodes.size();
  }

  /**
   * subIterator
   * 
   * Iterator through the subnodes of this node only
   * 
   * @return The iterator
   */
  public Iterator<T> subIterator() {
    return subnodes.iterator();
  }

  /**
   * superContains
   * 
   * Check if a node is present in the tree of which this tree is a part
   * 
   * @param o The node to check
   * @return true if the node is present in the tree of which this tree is a
   *         part
   */
  public boolean superContains(T o) {
    return getRoot().contains(o);
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

  /**
   * toBreadthFirstArray
   * 
   * Return an array of nodes in the tree in breadth-first search order
   * 
   * @return array
   */
  public ArrayList<T> toBreadthFirstArray() {
    return toXFirstArray(breadthFirstIterator());
  }

  /**
   * toDepthFirstArray
   * 
   * Return an array of nodes in the tree in depth-first search order
   * 
   * @return array
   */
  public ArrayList<T> toDepthFirstArray() {
    return toXFirstArray(depthFirstIterator());
  }

  /**
   * toXFirstArray
   * 
   * Return an array of nodes in an order of the iterator given
   * 
   * @param ix The iterator
   * @return array
   */
  private ArrayList<T> toXFirstArray(Iterator<T> ix) {
    ArrayList<T> arr = new ArrayList<T>();
    arr.ensureCapacity(nodes.size());
    for(int i = 0; ix.hasNext(); i++) {
      arr.add(i, ix.next());
    }
    return arr;
  }

  public String toString() {
    if(root == null) return "null";
    if(isLeaf()) return root.toString();
    StringBuffer buf = new StringBuffer(root.toString() + " (");
    for(T o: subnodes) {
      buf.append(" " + nodes.get(o).toString());
    }
    buf.append(" )");
    return buf.toString();
  }

  /**
   * BreadthFirstIterator
   * 
   * @author Gary Polhill
   * 
   * An iterator doing a breadth first search of the tree
   * 
   * @param <S>
   */
  public class BreadthFirstIterator<S> implements Iterator<T> {
    private Tree<T> node;
    private LinkedList<Tree<T>> queue;

    public BreadthFirstIterator() {
      node = Tree.this;
      queue = new LinkedList<Tree<T>>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return node != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public T next() {
      T thisNode = node.root;
      for(T subNode: node.subnodes) {
        queue.addLast(nodes.get(subNode));
      }
      node = queue.size() > 0 ? queue.removeFirst() : null;
      return thisNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      Tree<T> subTree = getSubTree(node.root);
      for(T o: subTree) {
        queue.remove(nodes.get(o));
      }
      Tree.this.remove(node.root);
    }

  }

  /**
   * DepthFirstIterator
   * 
   * @author Gary Polhill
   * 
   * An iterator doing a depth first search of the tree
   * 
   * @param <R>
   */
  public class DepthFirstIterator<R> implements Iterator<T> {
    Tree<T> node;
    LinkedList<Tree<T>> stack;

    public DepthFirstIterator() {
      node = Tree.this;
      stack = new LinkedList<Tree<T>>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return node != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public T next() {
      T thisNode = node.root;
      for(T subNode: node.subnodes) {
        stack.addFirst(nodes.get(subNode));
      }
      node = stack.size() == 0 ? null : stack.removeFirst();
      return thisNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      Tree<T> subTree = getSubTree(node.root);
      for(T o: subTree) {
        stack.remove(nodes.get(o));
      }
      Tree.this.remove(node.root);
    }

  }
}
