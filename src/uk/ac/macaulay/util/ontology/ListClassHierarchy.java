/*
 * uk.ac.macaulay.util.ontology: ListClassHierarchy.java
 * 
 * Copyright (C) 2009 Macaulay Institute
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
package uk.ac.macaulay.util.ontology;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;


/**
 * ListClassHierarchy
 * 
 * 
 * 
 * @author Gary Polhill
 */
public class ListClassHierarchy {
  OWLOntologyManager manager;
  OWLDataFactory factory;
  OWLOntology ontology;
  Map<OWLClass, Set<OWLClass>> supers;
  Map<OWLClass, Set<OWLClass>> subs;
  Map<OWLClass, Set<OWLClass>> equivs;

  /**
   * <!-- main -->
   * 
   * @param args
   */
  public static void main(String[] args) {
    String uri = null;
    try {
      ListClassHierarchy obj;
      uri = args[0];
      if(!uri.startsWith("http://") && !uri.startsWith("https://") && !uri.startsWith("file:/")) {
        String file = uri;
        if(System.getProperty("file.separator").equals("\\")) {
          String[] parts = uri.split("\\\\");
          StringBuffer buf = new StringBuffer(parts[0]);
          for(int i = 1; i < parts.length; i++) {
            buf.append("/");
            buf.append(parts[i]);
          }
          file = buf.toString();
          uri = "file:/" + file;
        }
        else {
          File fp = new File(file);
          try {
            uri = "file:" + fp.getCanonicalPath();
          }
          catch(IOException e) {
            System.out.println(file + ": " + e.getMessage());
          }
        }
      }
      obj = new ListClassHierarchy(uri);
      obj.list();
    }
    catch(OWLOntologyCreationException e) {
      System.err.println("Cannot load ontology: " + e.getMessage());
    }
    catch(URISyntaxException e) {
      System.err.println("Malformed URI: " + uri);
    }
    catch(ArrayIndexOutOfBoundsException e) {
      System.err.println("You must supply the filename as argument");
    }
  }

  ListClassHierarchy(String ontologyURI) throws OWLOntologyCreationException, URISyntaxException {
    manager = OWLManager.createOWLOntologyManager();
    factory = manager.getOWLDataFactory();
    ontology = manager.loadOntologyFromPhysicalURI(new URI(ontologyURI));
    supers = new HashMap<OWLClass, Set<OWLClass>>();
    subs = new HashMap<OWLClass, Set<OWLClass>>();
    equivs = new HashMap<OWLClass, Set<OWLClass>>();
  }

  public void list() {
    Set<OWLClass> classes = ontology.getReferencedClasses();
    Set<OWLClass> roots = new HashSet<OWLClass>();

    for(OWLClass cls: classes) {

      for(OWLDescription mysuper: cls.getSuperClasses(ontology)) {
        if(classes.contains(mysuper)) {
          enter(supers, cls, mysuper.asOWLClass());
        }
      }

      if(!supers.containsKey(cls)) roots.add(cls);

      for(OWLDescription mysub: cls.getSubClasses(ontology)) {
        if(classes.contains(mysub)) {
          enter(subs, cls, mysub.asOWLClass());
        }
      }

      for(OWLDescription myequiv: cls.getEquivalentClasses(ontology)) {
        if(classes.contains(myequiv)) {
          enter(equivs, cls, myequiv.asOWLClass());
        }
      }
    }

    Set<OWLClass> listed = new HashSet<OWLClass>();

    for(OWLClass root: sort(roots)) {
      list(root, listed, 0);
    }

  }

  private LinkedList<OWLClass> sort(Set<OWLClass> set) {
    LinkedList<OWLClass> list = new LinkedList<OWLClass>();
    list.addAll(set);
    Collections.sort(list);
    return list;
  }

  private void list(OWLClass cls, Set<OWLClass> listed, int indent) {
    StringBuffer indentbuf = new StringBuffer();
    for(int i = 0; i < indent; i++) {
      indentbuf.append("  ");
    }
    if(listed.contains(cls)) {
      System.out.println(indentbuf + cls.getURI().getFragment() + " *");
    }
    else {
      System.out.print(indentbuf + cls.getURI().getFragment());
      if(equivs.containsKey(cls)) {
        System.out.print(" = ");
        boolean comma = false;
        for(OWLClass equiv: sort(equivs.get(cls))) {
          if(comma) System.out.print(", ");
          System.out.print(equiv);
        }
      }
      System.out.println();
      listed.add(cls);
      if(!subs.containsKey(cls)) return;
      for(OWLClass sub: sort(subs.get(cls))) {
        list(sub, listed, indent + 1);
      }
    }
  }

  public static void enter(Map<OWLClass, Set<OWLClass>> entry, OWLClass key, OWLClass value) {
    if(!entry.containsKey(key)) {
      entry.put(key, new HashSet<OWLClass>());
    }
    entry.get(key).add(value);
  }

}
