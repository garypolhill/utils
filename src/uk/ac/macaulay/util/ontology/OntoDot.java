/*
 * uk.ac.macaulay.util.ontology: OntoDot.java
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDeclarationAxiom;
import org.semanticweb.owl.model.OWLDeprecatedClassAxiom;
import org.semanticweb.owl.model.OWLDeprecatedDataPropertyAxiom;
import org.semanticweb.owl.model.OWLDeprecatedObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLIndividualAxiom;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owl.model.SWRLRule;



/**
 * <!-- OntoDot -->
 * 
 * @author Gary Polhill
 */
public class OntoDot {
  public enum ArrowShape {
    BOX, LBOX, RBOX, OBOX, OLBOX, ORBOX, CROW, LCROW, RCROW, DIAMOND, LDIAMOND, RDIAMOND, ODIAMOND, OLDIAMOND,
    ORDIAMOND, DOT, ODOT, INV, LINV, RINV, OINV, OLINV, ORINV, NONE, NORMAL, LNORMAL, RNORMAL, ONORMAL, OLNORMAL,
    ORNORMAL, TEE, LTEE, RTEE, VEE, LVEE, RVEE;

    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public enum EdgeStyle {
    SOLID, DASHED, DOTTED, BOLD, INVIS;

    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public enum NodeStyle {
    SOLID, DASHED, DOTTED, BOLD, INVIS, FILLED, DIAGONALS, ROUNDED;

    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public enum NodeShape {
    BOX, POLYGON, ELLIPSE, CIRCLE, POINT, EGG, TRIANGLE, PLAINTEXT, DIAMOND, TRAPEZIUM, PARALLELOGRAM, HOUSE, PENTAGON,
    HEXAGON, SEPTAGON, OCTAGON, DOUBLECIRCLE, DOUBLEOCTAGON, TRIPLEOCTAGON, INVTRIANGLE, INVTRAPEZIUM, INVHOUSE,
    MDIAMOND, MSQUARE, MCIRCLE, RECT, RECTANGLE, SQUARE, NONE, NOTE, TAB, FOLDER, BOX3D, COMPONENT, RECORD, MRECORD;

    public String toString() {
      if(this == MDIAMOND || this == MSQUARE || this == MCIRCLE || this == MRECORD) {
        return "M" + this.name().substring(1).toLowerCase();
      }
      else {
        return this.name().toLowerCase();
      }
    }
  }

  OWLOntologyManager manager;
  OWLDataFactory factory;
  OWLOntology ontology;
  Double sizeXin = null;
  Double sizeYin = null;

  ArrowShape instanceOfClassShape = ArrowShape.VEE;
  EdgeStyle instanceOfClassStyle = EdgeStyle.DASHED;
  NodeShape instanceNodeShape = NodeShape.MRECORD;
  NodeStyle instanceNodeStyle = NodeStyle.SOLID;

  NodeShape classNodeShape = NodeShape.BOX;
  NodeStyle classNodeStyle = NodeStyle.SOLID;

  String instanceOfClassLabel = "member";
  Set<URI> hideDataProperties;
  Set<URI> hideObjectProperties;
  Set<URI> hideClasses;
  Set<URI> hideIndividuals;
  Set<URI> nodesWritten;
  Map<URI, String> labels;
  
  private int subGraphCounter;

  OntoDot(String ontologyURI) throws OWLOntologyCreationException, URISyntaxException {
    manager = OWLManager.createOWLOntologyManager();
    factory = manager.getOWLDataFactory();
    ontology = manager.loadOntologyFromPhysicalURI(new URI(ontologyURI));
    hideDataProperties = new HashSet<URI>();
    hideObjectProperties = new HashSet<URI>();
    hideClasses = new HashSet<URI>();
    hideIndividuals = new HashSet<URI>();
    nodesWritten = new HashSet<URI>();
    labels = new HashMap<URI, String>();
    for(OWLEntity entity: ontology.getReferencedEntities()) {
      labels.put(entity.getURI(), entity.getURI().getFragment());
    }
    subGraphCounter = 0;
  }

  void setSizeXYin(double sizeXin, double sizeYin) {
    this.sizeXin = sizeXin;
    this.sizeYin = sizeYin;
  }

  private String getClassNodeStyle() {
    return "[shape=" + classNodeShape + ",style=" + classNodeStyle + "]";
  }

  private String getInstanceOfClassStyle() {
    StringBuffer buff = new StringBuffer("[");
    buff.append("style=" + instanceOfClassStyle);
    if(instanceOfClassLabel != null) {
      buff.append(",label=\"" + instanceOfClassLabel + "\"");
    }
    buff.append("[");
    return buff.toString();
  }

  private String getInstanceNodeStyle(OWLIndividual individual) {
    return "[shape="
      + instanceNodeShape
      + ",style="
      + instanceNodeStyle
      + ",label=\""
      + (instanceNodeShape == NodeShape.RECORD || instanceNodeShape == NodeShape.MRECORD ? getRecordForIndividual(individual)
                                                                                        : labels.get(individual
                                                                                            .getURI())) + "\"" + "]";
  }

  private String getRecordForIndividual(OWLIndividual individual) {
    StringBuffer buff = new StringBuffer(individual.getURI().getFragment());
    Map<String, Set<String>> values = new HashMap<String, Set<String>>();
    for(OWLIndividualAxiom axiom: ontology.getAxioms(individual)) {
      if(axiom instanceof OWLDataPropertyAssertionAxiom) {
        OWLDataPropertyAssertionAxiom dpaxiom = (OWLDataPropertyAssertionAxiom)axiom;
        if(!dpaxiom.getProperty().isAnonymous()
          && !hideDataProperties.contains(dpaxiom.getProperty().asOWLDataProperty().getURI())) {
          String dp = dpaxiom.getProperty().asOWLDataProperty().getURI().getFragment();
          if(!values.containsKey(dp)) {
            values.put(dp, new HashSet<String>());
          }
          values.get(dp).add(dpaxiom.getObject().getLiteral());
        }
      }
    }
    for(String property: values.keySet()) {
      buff.append("|{" + property + "|");
      boolean first = true;
      for(String value: values.get(property)) {
        if(first) first = false;
        else
          buff.append(", ");

        buff.append(value);
      }
      buff.append("}");
    }
    return buff.toString();
  }

  void buildDot(String dotFile) throws IOException {
    PrintWriter fp = new PrintWriter(new FileWriter(new File(dotFile)));
    fp.println("digraph G {");
    if(sizeXin != null && sizeYin != null) {
      fp.println("  size = \"" + sizeXin + "," + sizeYin + "\";");
    }
    for(OWLOntology closure: manager.getImportsClosure(ontology)) {
      buildDot(fp, closure);
    }
    fp.println("}");
  }
  
  void buildDot(PrintWriter fp, OWLOntology ont) {
    String ontologyPath = ont.getURI().getPath();
    String[] temp = ontologyPath.split("/");
    String ontologyName = temp[temp.length - 1];
    fp.println("  subgraph S" + subGraphCounter + " {");
    for(OWLEntity entity: ont.getReferencedEntities()) {
      buildDot(fp, entity, ont);
    }
    fp.println("    label = \"" + ontologyName + "\";");
    fp.println("  }");
  }
  
  void buildDot(PrintWriter fp, OWLEntity entity, OWLOntology ont) {
    if(entity instanceof OWLClass) {
      buildDot(fp, (OWLClass)entity, ont);
    }
    else if(entity instanceof OWLIndividual) {
      buildDot(fp, (OWLIndividual)entity, ont);
    }
  }
  
  void buildDot(PrintWriter fp, OWLClass owlClass, OWLOntology ont) {
    Set<OWLDescription> equivalentClasses = owlClass.getEquivalentClasses(ontology);
    Set<OWLAnnotation> annotations = owlClass.getAnnotations(ontology);
    Set<OWLIndividual> individuals = owlClass.getIndividuals(ontology);
    Set<OWLDescription> disjoints = owlClass.getDisjointClasses(ontology);
    Set<OWLDescription> subClasses = owlClass.getSubClasses(ontology);
    Set<OWLDescription> superClasses = owlClass.getSuperClasses(ontology);
    Set<OWLDataProperty> dataProperties = new HashSet<OWLDataProperty>();
    // Get all the object and data property assertions
    for(OWLAxiom axiom: ont.getReferencingAxioms(owlClass)) {
      if(axiom instanceof OWLDataPropertyDomainAxiom) {
        OWLDataPropertyDomainAxiom dpdaxiom = (OWLDataPropertyDomainAxiom)axiom;
        OWLDescription desc = dpdaxiom.getDomain();
        if(desc.equals(owlClass)) {
          dataProperties.add(dpdaxiom.getProperty().asOWLDataProperty());
        }
      }
      else if(axiom instanceof OWLEquivalentClassesAxiom) {
        
      }
      else {
        buildDot(fp, axiom);
      }
    }
    
  }
  
  void buildDot(PrintWriter fp, OWLIndividual ind, OWLOntology ont) {
    Set<OWLIndividual> sameInds = ind.getSameIndividuals(ont);
    Set<OWLAnnotation> annotations = ind.getAnnotations(ont);
    Set<OWLIndividual> diffInds = ind.getDifferentIndividuals(ont);
    Map<OWLDataPropertyExpression, Set<OWLConstant>> dataValues = ind.getDataPropertyValues(ont);
    Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectValues = ind.getObjectPropertyValues(ont);
    // Get all the object and data property assertions
    for(OWLAxiom axiom: ont.getReferencingAxioms(ind)) {
      buildDot(fp, axiom);
    }
  }
  
  void builtDot(PrintWriter fp, OWLDescription desc, OWLOntology ont) {
    if(desc instanceof OWLClass) {
      buildDot(fp, (OWLClass)desc, ont);
      return;
    }
  }

  void buildDot(PrintWriter fp, OWLAxiom axiom) {
    if(axiom instanceof OWLAntiSymmetricObjectPropertyAxiom) {
      buildDot(fp, (OWLAntiSymmetricObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLAxiomAnnotationAxiom) {
      buildDot(fp, (OWLAxiomAnnotationAxiom)axiom);
    }
    else if(axiom instanceof OWLClassAssertionAxiom) {
      buildDot(fp, (OWLClassAssertionAxiom)axiom);
    }
    else if(axiom instanceof OWLDataPropertyAssertionAxiom) {
      buildDot(fp, (OWLDataPropertyAssertionAxiom)axiom);
    }
    else if(axiom instanceof OWLDataPropertyDomainAxiom) {
      buildDot(fp, (OWLDataPropertyDomainAxiom)axiom);
    }
    else if(axiom instanceof OWLDataPropertyRangeAxiom) {
      buildDot(fp, (OWLDataPropertyRangeAxiom)axiom);
    }
    else if(axiom instanceof OWLDataSubPropertyAxiom) {
      buildDot(fp, (OWLDataSubPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLDeclarationAxiom) {
      buildDot(fp, (OWLDeclarationAxiom)axiom);
    }
    else if(axiom instanceof OWLDeprecatedClassAxiom) {
      buildDot(fp, (OWLDeprecatedClassAxiom)axiom);
    }
    else if(axiom instanceof OWLDeprecatedDataPropertyAxiom) {
      buildDot(fp, (OWLDeprecatedDataPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLDeprecatedObjectPropertyAxiom) {
      buildDot(fp, (OWLDeprecatedObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLDifferentIndividualsAxiom) {
      buildDot(fp, (OWLDifferentIndividualsAxiom)axiom);
    }
    else if(axiom instanceof OWLDisjointClassesAxiom) {
      buildDot(fp, (OWLDisjointClassesAxiom)axiom);
    }
    else if(axiom instanceof OWLDisjointDataPropertiesAxiom) {
      buildDot(fp, (OWLDisjointDataPropertiesAxiom)axiom);
    }
    else if(axiom instanceof OWLDisjointObjectPropertiesAxiom) {
      buildDot(fp, (OWLDisjointObjectPropertiesAxiom)axiom);
    }
    else if(axiom instanceof OWLDisjointUnionAxiom) {
      buildDot(fp, (OWLDisjointUnionAxiom)axiom);
    }
    else if(axiom instanceof OWLEntityAnnotationAxiom) {
      buildDot(fp, (OWLEntityAnnotationAxiom)axiom);
    }
    else if(axiom instanceof OWLEquivalentClassesAxiom) {
      buildDot(fp, (OWLEquivalentClassesAxiom)axiom);
    }
    else if(axiom instanceof OWLEquivalentDataPropertiesAxiom) {
      buildDot(fp, (OWLEquivalentDataPropertiesAxiom)axiom);
    }
    else if(axiom instanceof OWLEquivalentObjectPropertiesAxiom) {
      buildDot(fp, (OWLEquivalentObjectPropertiesAxiom)axiom);
    }
    else if(axiom instanceof OWLFunctionalDataPropertyAxiom) {
      buildDot(fp, (OWLFunctionalDataPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLFunctionalObjectPropertyAxiom) {
      buildDot(fp, (OWLFunctionalObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLImportsDeclaration) {
      buildDot(fp, (OWLImportsDeclaration)axiom);
    }
    else if(axiom instanceof OWLInverseFunctionalObjectPropertyAxiom) {
      buildDot(fp, (OWLInverseFunctionalObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLInverseObjectPropertiesAxiom) {
      buildDot(fp, (OWLInverseObjectPropertiesAxiom)axiom);
    }
    else if(axiom instanceof OWLIrreflexiveObjectPropertyAxiom) {
      buildDot(fp, (OWLIrreflexiveObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLNegativeDataPropertyAssertionAxiom) {
      buildDot(fp, (OWLNegativeDataPropertyAssertionAxiom)axiom);
    }
    else if(axiom instanceof OWLNegativeObjectPropertyAssertionAxiom) {
      buildDot(fp, (OWLNegativeObjectPropertyAssertionAxiom)axiom);
    }
    else if(axiom instanceof OWLObjectPropertyAssertionAxiom) {
      buildDot(fp, (OWLObjectPropertyAssertionAxiom)axiom);
    }
    else if(axiom instanceof OWLObjectPropertyChainSubPropertyAxiom) {
      buildDot(fp, (OWLObjectPropertyChainSubPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLObjectPropertyDomainAxiom) {
      buildDot(fp, (OWLObjectPropertyDomainAxiom)axiom);
    }
    else if(axiom instanceof OWLObjectPropertyRangeAxiom) {
      buildDot(fp, (OWLObjectPropertyRangeAxiom)axiom);
    }
    else if(axiom instanceof OWLObjectSubPropertyAxiom) {
      buildDot(fp, (OWLObjectSubPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLOntologyAnnotationAxiom) {
      buildDot(fp, (OWLOntologyAnnotationAxiom)axiom);
    }
    else if(axiom instanceof OWLReflexiveObjectPropertyAxiom) {
      buildDot(fp, (OWLReflexiveObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLSameIndividualsAxiom) {
      buildDot(fp, (OWLSameIndividualsAxiom)axiom);
    }
    else if(axiom instanceof OWLSubClassAxiom) {
      buildDot(fp, (OWLSubClassAxiom)axiom);
    }
    else if(axiom instanceof OWLSymmetricObjectPropertyAxiom) {
      buildDot(fp, (OWLSymmetricObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof OWLTransitiveObjectPropertyAxiom) {
      buildDot(fp, (OWLTransitiveObjectPropertyAxiom)axiom);
    }
    else if(axiom instanceof SWRLRule) {
      buildDot(fp, (SWRLRule)axiom);
    }
  }

  void buildDot(PrintWriter fp, OWLAntiSymmetricObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLAxiomAnnotationAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLClassAssertionAxiom axiom) {
    if(!axiom.getDescription().isAnonymous()) {
      if(!hideIndividuals.contains(axiom.getIndividual().getURI())
        && !nodesWritten.contains(axiom.getIndividual().getURI())) {
        fp.println("  " + labels.get(axiom.getIndividual().getURI()) + getInstanceNodeStyle(axiom.getIndividual())
          + ";");
        nodesWritten.add(axiom.getIndividual().getURI());
      }
      if(!hideClasses.contains(axiom.getDescription().asOWLClass().getURI())
        && !nodesWritten.contains(axiom.getDescription().asOWLClass().getURI())) {
        fp.println("  " + labels.get(axiom.getDescription().asOWLClass().getURI()) + getClassNodeStyle() + ";");
        nodesWritten.add(axiom.getDescription().asOWLClass().getURI());
      }
      fp.println("  " + labels.get(axiom.getIndividual().getURI()) + " -> "
        + labels.get(axiom.getDescription().asOWLClass().getURI()) + getInstanceOfClassStyle() + ";");
    }
  }

  void buildDot(PrintWriter fp, OWLDataPropertyAssertionAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDataPropertyDomainAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDataPropertyRangeAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDataSubPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDeclarationAxiom axiom) {
    OWLEntity entity = axiom.getEntity();
    if(nodesWritten.contains(entity.getURI())) return;
    if(entity instanceof OWLIndividual) {
      fp.println("  " + labels.get(entity.getURI()) + getInstanceNodeStyle((OWLIndividual)entity) + ";");
      nodesWritten.add(entity.getURI());
    }
    else if(entity instanceof OWLClass) {
      fp.println("  " + labels.get(entity.getURI()) + getClassNodeStyle() + ";");
      nodesWritten.add(entity.getURI());
    }
  }

  void buildDot(PrintWriter fp, OWLDeprecatedClassAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDeprecatedDataPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDeprecatedObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDifferentIndividualsAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDisjointClassesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDisjointDataPropertiesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDisjointObjectPropertiesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLDisjointUnionAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLEntityAnnotationAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLEquivalentClassesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLEquivalentDataPropertiesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLEquivalentObjectPropertiesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLFunctionalDataPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLFunctionalObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLImportsDeclaration axiom) {
  }

  void buildDot(PrintWriter fp, OWLInverseFunctionalObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLInverseObjectPropertiesAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLIrreflexiveObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLNegativeDataPropertyAssertionAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLNegativeObjectPropertyAssertionAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLObjectPropertyAssertionAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLObjectPropertyChainSubPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLObjectPropertyDomainAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLObjectPropertyRangeAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLObjectSubPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLOntologyAnnotationAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLReflexiveObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLSameIndividualsAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLSubClassAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLSymmetricObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, OWLTransitiveObjectPropertyAxiom axiom) {
  }

  void buildDot(PrintWriter fp, SWRLRule axiom) {
  }

}
