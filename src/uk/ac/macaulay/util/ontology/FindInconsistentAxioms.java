/*
 * uk.ac.macaulay.util.ontology: FindInconsistentAxioms.java
 * 
 * Copyright (C) 2010 Macaulay Institute
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mindswap.pellet.exceptions.InconsistentOntologyException;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.UnknownOWLOntologyException;
import org.semanticweb.owl.util.SimpleURIMapper;

/**
 * <!-- FindInconsistentAxioms -->
 * 
 * @author Gary Polhill
 */
public class FindInconsistentAxioms {
  OWLOntologyManager manager;
  Set<OWLAxiom> axioms;
  OWLReasoner reasoner;
  boolean wholeOntologyConsistent;
  URI ontologyURI;

  public FindInconsistentAxioms(URI ontologyURI) throws OWLOntologyCreationException {
    this(ontologyURI, new HashMap<URI, URI>());
  }

  public FindInconsistentAxioms(URI ontologyURI, Map<URI, URI> ontologyLocator) throws OWLOntologyCreationException {
    manager = OWLManager.createOWLOntologyManager();
    for(URI logicalURI: ontologyLocator.keySet()) {
      System.out.println("Mapping " + logicalURI + " to " + ontologyLocator.get(logicalURI));
      manager.addURIMapper(new SimpleURIMapper(logicalURI, ontologyLocator.get(logicalURI)));
    }
    System.out.println("Loading ontology " + ontologyURI);
    OWLOntology ontology = manager.loadOntology(ontologyURI);
    axioms = new HashSet<OWLAxiom>(ontology.getAxioms());
    reasoner = new Reasoner(manager);
    wholeOntologyConsistent = wholeOntologyConsistent(reasoner, manager, ontology);
    manager.removeOntology(ontologyURI);
    this.ontologyURI = ontologyURI;
  }

  /**
   * @param ontology
   * @throws OWLOntologyCreationException
   * @throws OWLOntologyChangeException
   * @throws UnknownOWLOntologyException
   * @throws OWLReasonerException
   */
  public FindInconsistentAxioms(OWLOntology ontology) throws OWLOntologyCreationException, OWLOntologyChangeException,
      UnknownOWLOntologyException {
    ontologyURI = ontology.getURI();
    manager = OWLManager.createOWLOntologyManager();
    OWLOntology tmpOntology = manager.createOntology(ontologyURI);
    axioms = new HashSet<OWLAxiom>(ontology.getAxioms());
    manager.addAxioms(tmpOntology, axioms);
    reasoner = new Reasoner(manager);
    wholeOntologyConsistent = wholeOntologyConsistent(reasoner, manager, tmpOntology);
    manager.removeOntology(ontologyURI);
  }

  private static boolean wholeOntologyConsistent(OWLReasoner reasoner, OWLOntologyManager manager, OWLOntology ontology) {
    try {
      return reasoner.isConsistent(ontology);
    }
    catch(OWLReasonerException e) {
      return false;
    }
  }

  public Set<OWLAxiom> getSingleAxiomsCausingInconsistency() throws OWLOntologyCreationException,
      OWLOntologyChangeException, UnknownOWLOntologyException, OWLReasonerException {
    Set<OWLAxiom> inconsistent = new HashSet<OWLAxiom>();
    if(wholeOntologyConsistent) return inconsistent;
    for(OWLAxiom axiom: axioms) {
      if(axiom instanceof OWLImportsDeclaration) continue;
      Set<OWLAxiom> newAxioms = new HashSet<OWLAxiom>(axioms);
      newAxioms.remove(axiom);
      OWLOntology ontology = manager.createOntology(ontologyURI);
      manager.addAxioms(ontology, newAxioms);
      reasoner.loadOntologies(manager.getImportsClosure(ontology));
      try {
        reasoner.classify();
        if(reasoner.isConsistent(ontology)) inconsistent.add(axiom);
      }
      catch(InconsistentOntologyException e) {
        // do nothing
      }
      reasoner.clearOntologies();
      manager.removeOntology(ontologyURI);
    }
    return inconsistent;
  }

  public Set<Set<OWLAxiom>> getAxiomPairsCausingInconsistency() throws OWLOntologyCreationException,
      OWLOntologyChangeException, UnknownOWLOntologyException, OWLReasonerException {
    Set<Set<OWLAxiom>> inconsistent = new HashSet<Set<OWLAxiom>>();
    if(wholeOntologyConsistent) return inconsistent;
    OWLAxiom axiomArr[] = axioms.toArray(new OWLAxiom[0]);
    for(int i = 0; i < axiomArr.length - 1; i++) {
      if(axiomArr[i] instanceof OWLImportsDeclaration) continue;
      for(int j = i + 1; j < axiomArr.length; j++) {
        if(axiomArr[j] instanceof OWLImportsDeclaration) continue;
        Set<OWLAxiom> newAxioms = new HashSet<OWLAxiom>(axioms);
        newAxioms.remove(axiomArr[i]);
        newAxioms.remove(axiomArr[j]);
        OWLOntology ontology = manager.createOntology(ontologyURI);
        manager.addAxioms(ontology, newAxioms);
        reasoner.loadOntologies(manager.getImportsClosure(ontology));
        try {
          reasoner.classify();
          if(reasoner.isConsistent(ontology)) {
            Set<OWLAxiom> pair = new HashSet<OWLAxiom>();
            pair.add(axiomArr[i]);
            pair.add(axiomArr[j]);
            inconsistent.add(pair);
          }
        }
        catch(InconsistentOntologyException e) {
          // do nothing
        }
        reasoner.clearOntologies();
        manager.removeOntology(ontologyURI);
      }
    }
    return inconsistent;
  }

  public boolean ontologyConsistent() {
    return wholeOntologyConsistent;
  }

  public static void usage() {
    usage(null);
  }

  public static void usage(String message) {
    if(message != null) System.err.println(message);
    System.err.println("Usage: java FindInconsistentAxioms {-map logicalURI physicalURI} ontologyURI");
    System.exit(1);
  }

  private static URI buildURIOrDie(String string) {
    try {
      return new URI(string);
    }
    catch(URISyntaxException e) {
      System.err.println("Malformed URI: " + string + " (" + e + ")");
      System.exit(1);
      throw new RuntimeException("Panic");
    }
  }

  /**
   * <!-- main -->
   * 
   * @param args
   */
  public static void main(String[] args) {
    if(args.length == 0) usage();
    Map<URI, URI> logPhysMap = new HashMap<URI, URI>();
    URI ontologyURI = buildURIOrDie(args[args.length - 1]);
    for(int i = 0; i < args.length - 1; i++) {
      if(args[i].equals("-map")) {
        if(i + 2 >= args.length - 1) usage("Expecting two arguments to -map option that aren't the main ontology URI");
        logPhysMap.put(buildURIOrDie(args[i + 1]), buildURIOrDie(args[i + 2]));
        i += 2;
      }
      else {
        usage("Unrecognised option: " + args[i]);
      }
    }
    try {
      FindInconsistentAxioms finder = new FindInconsistentAxioms(ontologyURI, logPhysMap);
      System.out.println("Searching for inconsistencies...");
      if(finder.ontologyConsistent()) {
        System.out.println("Ontology " + ontologyURI + " is consistent");
      }
      else {
        Set<OWLAxiom> inconsistentAxioms = finder.getSingleAxiomsCausingInconsistency();
        if(inconsistentAxioms.size() > 0) {
          System.out.println("Ontology " + ontologyURI
            + " is consistent if any one of the following axioms is removed:");
          for(OWLAxiom axiom: inconsistentAxioms) {
            System.out.println(axiom);
          }
        }
        else {
          Set<Set<OWLAxiom>> inconsistentPairs = finder.getAxiomPairsCausingInconsistency();
          if(inconsistentAxioms.size() > 0) {
            System.out.println("Ontology " + ontologyURI
              + " is consistent if any of the following pairs of axioms is removed:");
            for(Set<OWLAxiom> pair: inconsistentPairs) {
              System.out.println("{");
              for(OWLAxiom axiom: pair) {
                System.out.println("  " + axiom);
              }
              System.out.println("}");
            }
          }
          else {
            System.out.println("There are no consistent subontologies of " + ontologyURI
              + " formed by removing up to two axioms from it.");
          }
        }
      }
    }
    catch(OWLOntologyCreationException e) {
      System.err.println("Problem creating an ontology: " + e);
    }
    catch(OWLReasonerException e) {
      System.err.println("Problem with the reasoner: " + e);
    }
    catch(UnknownOWLOntologyException e) {
      System.err.println("Problem with an unknown OWL ontology: " + e);
    }
    catch(OWLOntologyChangeException e) {
      System.err.println("Problem changing an ontology: " + e);
    }
  }
}
