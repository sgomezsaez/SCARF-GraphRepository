package org.eclipse.topology.dao;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.eclipse.topology.domain.Capability;
import org.eclipse.topology.domain.Constraint;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

public class CapabilityDao{

	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final PreFixDao preFixDao = daoFactory.getPreFixDao();
	private final ConstraintDao constraintDao = daoFactory.getConstraintDao();
	public enum capabilityNode implements Label {
		CAPABILITY;
	}
	

	public Node create(Capability capability) {
		// TODO Auto-generated method stub
		Node nodeCapabilityDb = db.createNode(capabilityNode.CAPABILITY);
		nodeCapabilityDb.setProperty( "Name", capability.getName());
		nodeCapabilityDb.setProperty( "Type", capability.getCapabilityType().getNamespaceURI()+":"+capability.getCapabilityType().getLocalPart());
		nodeCapabilityDb.setProperty( "Namespace", capability.getCapabilityType().getNamespaceURI());
		nodeCapabilityDb.setProperty( "Prefix", preFixDao.getPrefixByNameSpaceUrl(capability.getCapabilityType().getNamespaceURI()));
		nodeCapabilityDb.setProperty( "Localpart", capability.getCapabilityType().getLocalPart());
		nodeCapabilityDb.setProperty( "LowerBound", capability.getLowerBound());
		nodeCapabilityDb.setProperty( "UpperBound", capability.getUpperBound());   
		
	    //constraint
	    if(capability.getConstraints() != null){
	    	Iterator<Constraint> iteratorConstraint = capability.getConstraints().getConstraint().iterator();
		    Constraint nextConstraint = null;
	     while (iteratorConstraint.hasNext()) {
	    	nextConstraint = iteratorConstraint.next();
	    	constraintDao.create(nextConstraint);
	     }
	    }
		return nodeCapabilityDb;
	}
	
	

	public Capability getCapability(Node concreteNode) {
		// TODO Auto-generated method stub
		  Capability capability = new Capability();		
		  capability.setName((String) concreteNode.getProperty("Name"));	  
		  capability.setType((QName) concreteNode.getProperty("Type"));
		  capability.setLowerBound((Integer) concreteNode.getProperty("LowerBound"));
		  capability.setUpperBound((String) concreteNode.getProperty("UpperBound"));
		return capability;
	}


	public void delete(Capability id) {
		// TODO Auto-generated method stub
		
	}

}
