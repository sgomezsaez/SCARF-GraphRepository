package org.eclipse.topology.dao;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.eclipse.topology.domain.Constraint;
import org.eclipse.topology.domain.Requirement;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
public class RequirementDao {
	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final PreFixDao preFixDao = daoFactory.getPreFixDao();
	private final ConstraintDao constraintDao = daoFactory.getConstraintDao();
	public enum requirementNode implements Label {
		REQUIREMENT;
	}
	

	public Node create(Requirement requirement) {
		// TODO Auto-generated method stub
		Node nodeRequirementDb = db.createNode(requirementNode.REQUIREMENT);
		nodeRequirementDb.setProperty( "Name", requirement.getName());
		nodeRequirementDb.setProperty( "Type", requirement.getRequirementType().getNamespaceURI()+":"+requirement.getRequirementType().getLocalPart());
		nodeRequirementDb.setProperty( "Namespace", requirement.getRequirementType().getNamespaceURI());
		nodeRequirementDb.setProperty( "Prefix", preFixDao.getPrefixByNameSpaceUrl(requirement.getRequirementType().getNamespaceURI()));
		nodeRequirementDb.setProperty( "Localpart", requirement.getRequirementType().getLocalPart());
		nodeRequirementDb.setProperty( "LowerBound", requirement.getLowerBound());
		nodeRequirementDb.setProperty( "UpperBound", requirement.getUpperBound());   
		if(requirement.getConstraints() != null){
		Iterator<Constraint> iteratorConstraint = requirement.getConstraints().getConstraint().iterator();
	    Constraint nextConstraint = null;
	    while (iteratorConstraint.hasNext()) {
	    	nextConstraint = iteratorConstraint.next();
	    	constraintDao.create(nextConstraint);
	    }
		}
		return nodeRequirementDb;
	}


	public Requirement getRequirment(Node requirementNode) {
		      Requirement requirement = new Requirement();
              try(Transaction tx = db.beginTx();){
			  requirement = new Requirement();		
			  requirement.setName((String) requirementNode.getProperty("Name"));	  
			  String namespace = (String) requirementNode.getProperty("Namespace");
			  String localpart = (String) requirementNode.getProperty("Localpart");
			  String prefix = (String) requirementNode.getProperty("Prefix");
			  QName qName = new QName(namespace, localpart, prefix);
			  requirement.setType(qName);
			  requirement.setLowerBound((Integer) requirementNode.getProperty("LowerBound"));
			  requirement.setUpperBound((String) requirementNode.getProperty("UpperBound"));
              }
			return requirement;
		}
	}



