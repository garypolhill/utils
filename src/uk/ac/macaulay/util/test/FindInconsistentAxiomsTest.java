/*
 * uk.ac.macaulay.util.test: FindInconsistentAxiomsTest.java
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
package uk.ac.macaulay.util.test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.UnknownOWLOntologyException;

import uk.ac.macaulay.util.ontology.FindInconsistentAxioms;

import junit.framework.TestCase;

/**
 * <!-- FindInconsistentAxiomsTest -->
 * 
 * @author Gary Polhill
 */
public class FindInconsistentAxiomsTest extends TestCase {

  /**
   * @param name
   */
  public FindInconsistentAxiomsTest(String name) {
    super(name);
  }

  /**
   * <!-- setUp -->
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws java.lang.Exception
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * <!-- tearDown -->
   * 
   * @see junit.framework.TestCase#tearDown()
   * @throws java.lang.Exception
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.ontology.FindInconsistentAxioms#getSingleAxiomsCausingInconsistency()}
   * .
   * 
   * @throws OWLOntologyCreationException
   * @throws OWLReasonerException
   * @throws OWLOntologyChangeException
   * @throws UnknownOWLOntologyException
   */
  public final void testGetSingleAxiomsCausingInconsistency() throws OWLOntologyCreationException,
      UnknownOWLOntologyException, OWLOntologyChangeException, OWLReasonerException {
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLDataFactory factory = manager.getOWLDataFactory();
    OWLOntology ontology = manager.createOntology(URI.create("http://www.macaulay.ac.uk/utils/test2.owl"));
    Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
    axioms.add(factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class2"))));
    axioms.add(factory.getOWLDisjointClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class2"))));
    axioms.add(factory.getOWLClassAssertionAxiom(factory.getOWLIndividual(URI.create(ontology.getURI() + "#ind1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class1"))));
    Set<OWLAxiom> inconsistentAxioms = new HashSet<OWLAxiom>(axioms);
    axioms.add(factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class3")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class4"))));
    manager.addAxioms(ontology, axioms);
    FindInconsistentAxioms incon = new FindInconsistentAxioms(ontology);
    Set<OWLAxiom> classInconsistentAxioms = incon.getSingleAxiomsCausingInconsistency();
    assertEquals(inconsistentAxioms, classInconsistentAxioms);
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.ontology.FindInconsistentAxioms#getAxiomPairsCausingInconsistency()}
   * .
   * 
   * @throws OWLOntologyCreationException
   * @throws OWLOntologyChangeException
   * @throws OWLReasonerException
   * @throws UnknownOWLOntologyException
   */
  public final void testGetAxiomPairsCausingInconsistency() throws OWLOntologyCreationException,
      OWLOntologyChangeException, UnknownOWLOntologyException, OWLReasonerException {
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLDataFactory factory = manager.getOWLDataFactory();
    OWLOntology ontology = manager.createOntology(URI.create("http://www.macaulay.ac.uk/utils/test3.owl"));
    Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
    OWLAxiom axiom1 =
      factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class1")), factory
          .getOWLClass(URI.create(ontology.getURI() + "#class2")));
    OWLAxiom axiom2 =
      factory.getOWLDisjointClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class1")), factory
          .getOWLClass(URI.create(ontology.getURI() + "#class2")));
    OWLAxiom axiom3 =
      factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class3")), factory
          .getOWLClass(URI.create(ontology.getURI() + "#class4")));
    OWLAxiom axiom4 =
      factory.getOWLDisjointClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class3")), factory
          .getOWLClass(URI.create(ontology.getURI() + "#class4")));
    OWLAxiom axiom5 = factory.getOWLClassAssertionAxiom(factory.getOWLIndividual(URI.create(ontology.getURI() + "#ind1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class1")));
    OWLAxiom axiom6 = factory.getOWLClassAssertionAxiom(factory.getOWLIndividual(URI.create(ontology.getURI() + "#ind3")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class3")));
    axioms.add(axiom1);
    axioms.add(axiom2);
    axioms.add(axiom3);
    axioms.add(axiom4);
    axioms.add(axiom5);
    axioms.add(axiom6);
    axioms.add(factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class5")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class6"))));
    manager.addAxioms(ontology, axioms);
    FindInconsistentAxioms incon = new FindInconsistentAxioms(ontology);
    Set<Set<OWLAxiom>> inconPairs = incon.getAxiomPairsCausingInconsistency();
    assertEquals(9, inconPairs.size());
    boolean axioms1and3 = false;
    boolean axioms1and4 = false;
    boolean axioms1and6 = false;
    boolean axioms2and3 = false;
    boolean axioms2and4 = false;
    boolean axioms2and6 = false;
    boolean axioms3and5 = false;
    boolean axioms4and5 = false;
    boolean axioms5and6 = false;
    for(Set<OWLAxiom> pair: inconPairs) {
      assertEquals(2, pair.size());
      if(pair.contains(axiom1) && pair.contains(axiom3)) {
        axioms1and3 = true;
      }
      else if(pair.contains(axiom1) && pair.contains(axiom4)) {
        axioms1and4 = true;
      }
      else if(pair.contains(axiom1) && pair.contains(axiom6)) {
        axioms1and6 = true;
      }
      else if(pair.contains(axiom2) && pair.contains(axiom3)) {
        axioms2and3 = true;
      }
      else if(pair.contains(axiom2) && pair.contains(axiom4)) {
        axioms2and4 = true;
      }
      else if(pair.contains(axiom2) && pair.contains(axiom6)) {
        axioms2and6 = true;
      }
      else if(pair.contains(axiom3) && pair.contains(axiom5)) {
        axioms3and5 = true;
      }
      else if(pair.contains(axiom4) && pair.contains(axiom5)) {
        axioms4and5 = true;
      }
      else if(pair.contains(axiom5) && pair.contains(axiom6)) {
        axioms5and6 = true;
      }
      else {
        fail("getAxiomPairsCausingInconsistency() returned " + pair);
      }
    }
    assertTrue(axioms1and3);
    assertTrue(axioms1and4);
    assertTrue(axioms1and6);
    assertTrue(axioms2and3);
    assertTrue(axioms2and4);
    assertTrue(axioms2and6);
    assertTrue(axioms3and5);
    assertTrue(axioms4and5);
    assertTrue(axioms5and6);
  }

  /**
   * Test method for
   * {@link uk.ac.macaulay.util.ontology.FindInconsistentAxioms#ontologyConsistent()}
   * .
   * 
   * @throws OWLOntologyCreationException
   * @throws OWLReasonerException
   * @throws OWLOntologyChangeException
   * @throws UnknownOWLOntologyException
   */
  public final void testOntologyConsistent() throws OWLOntologyCreationException, UnknownOWLOntologyException,
      OWLOntologyChangeException, OWLReasonerException {
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLDataFactory factory = manager.getOWLDataFactory();
    OWLOntology ontology = manager.createOntology(URI.create("http://www.macaulay.ac.uk/utils/test1.owl"));
    Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
    axioms.add(factory.getOWLEquivalentClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class2"))));
    axioms.add(factory.getOWLClassAssertionAxiom(factory.getOWLIndividual(URI.create(ontology.getURI() + "#ind1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class1"))));
    manager.addAxioms(ontology, axioms);
    FindInconsistentAxioms incon = new FindInconsistentAxioms(ontology);
    assertTrue(incon.ontologyConsistent());

    axioms.add(factory.getOWLDisjointClassesAxiom(factory.getOWLClass(URI.create(ontology.getURI() + "#class1")),
        factory.getOWLClass(URI.create(ontology.getURI() + "#class2"))));
    manager.addAxioms(ontology, axioms);
    FindInconsistentAxioms incon2 = new FindInconsistentAxioms(ontology);
    assertFalse(incon2.ontologyConsistent());
  }
}
