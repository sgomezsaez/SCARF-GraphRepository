package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.topology.domain.InstanceNode;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class InstanceNodeDao extends NodeDao{

	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();

	public enum instanceNodeRelationships implements RelationshipType{
		REFERS_TO;
	}

	public enum instanceNode implements Label {
		instanceNode,γ,α;
	}

    public String getSpecification(Node node){
    	String specification;
    	try ( Transaction tx = db.beginTx();){

    		specification = (String) node.getProperty("specification");

    	}

    	return specification;
    }


	public Node create(InstanceNode instanceNode) {
		String query = "match (n:concreteNode {type:"+"'"+instanceNode.getType().getPrefix()+":"+instanceNode.getType().getLocalPart()+"'"+"}) return n";

    	try ( Transaction tx = db.beginTx();Result result = db.execute(query);)



    	{
    		String nodeName =  instanceNode.getName();
    		Label dynamicLabel = DynamicLabel.label(nodeName);
    		Node instanceNodeDb = null;
    		if(instanceNode.getTopologyType().equals("α")){
    	    instanceNodeDb = db.createNode(dynamicLabel,InstanceNodeDao.instanceNode.instanceNode,InstanceNodeDao.instanceNode.α );
    		}
    		else{
    	    instanceNodeDb = db.createNode(dynamicLabel,InstanceNodeDao.instanceNode.instanceNode,InstanceNodeDao.instanceNode.γ);
    		}
    	    instanceNodeDb.setProperty( "id", instanceNode.getId() );
    	    instanceNodeDb.setProperty( "name", instanceNode.getName() );
    	    if(instanceNode.getNodeLevel() != null){
    	    instanceNodeDb.setProperty( "level", instanceNode.getNodeLevel());   }
    	    if(instanceNode.getSpecification() != null){
    	    instanceNodeDb.setProperty( "specification", instanceNode.getSpecification() ); }
            QName qname = instanceNode.getType();
            instanceNodeDb.setProperty( "type", qname.getPrefix()+":"+qname.getLocalPart() );

            // Capability need re-write
/*    	    if(instanceNode.getCapabilities() != null){
    	    Iterator<Capability> iteratorCapability = instanceNode.getCapabilities().getCapability().iterator();
    	    Capability nextCapability = null;
    	     while (iteratorCapability.hasNext()) {
    	    	nextCapability = iteratorCapability.next();
    	    	IBaseDao<Capability,Node> localDao = new CapabilityDao();
    	    	Node nodeCapabilityDb = localDao.create(nextCapability);
    			Relationship relationship = instanceNodeDb.createRelationshipTo
    					(nodeCapabilityDb,concreteNodeRelationships.HAS_CAPABILITY);
    	     }
    	    }*/
    	    //Requirement need re-write
/*    	    if(instanceNode.getRequirements() != null){
    	    Iterator<Requirement> iteratorRequirement = instanceNode.getRequirements().getRequirement().iterator();
    	    Requirement nextRequirement = null;
    	     while (iteratorRequirement.hasNext()) {
    	    	nextRequirement = iteratorRequirement.next();
    	    	@SuppressWarnings("rawtypes")
				IBaseDao localDao = new RequirementDao();
    	    	Node nodeRequirementDb = (Node) localDao.create(nextRequirement);
    			Relationship relationship = instanceNodeDb.createRelationshipTo
    					(nodeRequirementDb,concreteNodeRelationships.HAS_REQUIREMENT);
    	     }
    	    }*/

    	    //Try to attached to an existing concrete Node
            if(!instanceNode.getTopologyType().equals("α")){//if add an alphatopology, we do not want it to link to a concrete node

    	    while ( result.hasNext() )
    	    {
    	        Map<String,Object> row = result.next();

    	         for ( String key : result.columns() )
    	         {

    	             Node concreteNode = (Node) row.get( key );

    	             instanceNodeDb.createRelationshipTo(concreteNode,instanceNodeRelationships.REFERS_TO);

    	            // concreteNode.createRelationshipTo(instanceNodeDb,instanceNodeRelationships.REFERS_TO);


    	         }
    	    }
            }
    	    tx.success();

    		return instanceNodeDb;
    	}

	}


	/**
	 * this method is used to get concrete node(node type) for a instance node
	 */
	public Node getConcreteNode(Node instanceNode) {
		Node concreteNode = null;
    	try ( Transaction tx = db.beginTx();){
    	    String type = (String) instanceNode.getProperty("type");
    	    if(type!=null){
    		String query = "match (n:concreteNode {type:"+"'"+type+"'"+"}) return n";
    		Result result = db.execute(query);
    		while ( result.hasNext() )
       	    {
       	        Map<String,Object> row = result.next();

       	         for ( String key : result.columns() )
       	         {
       	             concreteNode = (Node) row.get( key );

       	         }
       	    }
       	    tx.success();
    	    }

    	}
		return concreteNode;
	}

	public boolean deleteOneInstanceNodeById(long instanceNodeId) throws org.neo4j.graphdb.NotFoundException{
		boolean deleteResult = false;
        if(!this.isInstanceNode(instanceNodeId)){
        	return false;
        }
		//String deleteInstanceNodeAndRelation = "match (a)-[r]-(b) WHERE id(a)="+instanceNodeId+" "+"DELETE r,a";
		String deleteInstanceNodeAndRelation = "match (a) WHERE id(a)="+instanceNodeId+" "+"OPTIONAL MATCH (a)-[r]-() DELETE r,a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(deleteInstanceNodeAndRelation);){
			tx.success();
			deleteResult = true;
	    }
		return deleteResult;
	}

	public Node getInstanceNodeById(long id){
		Node instanceNode = null;
		String query = "MATCH (a:instanceNode) where id(a)="+id+" "+"RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	instanceNode =  (Node) row.get( key );
      	         }
      	    }
	    }
		return instanceNode;
	}

	public List<Node> getAllInstanceNodes(){
		List<Node> instanceNodeList = new ArrayList<Node>();
		String query = "MATCH (a:instanceNode:γ) RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	Node instanceNode =  (Node) row.get( key );
      	        	instanceNodeList.add(instanceNode);
      	         }
      	    }
	    }
		return instanceNodeList;
	}

	  public boolean isInstanceNode(long instanceNodeId) throws NotFoundException {

		  boolean result = false;
		  try ( Transaction tx = db.beginTx()){
		   Node node = db.getNodeById(instanceNodeId);
		   if(node != null){
			   Iterator<Label> labels =  node.getLabels().iterator();
			   while(labels.hasNext()){
	             Label oneLable = labels.next();
	             if(oneLable.name().equals("instanceNode")){
	             result = true;
	             }
			   }
		   }
		  return result;
	  }

	  }


}
