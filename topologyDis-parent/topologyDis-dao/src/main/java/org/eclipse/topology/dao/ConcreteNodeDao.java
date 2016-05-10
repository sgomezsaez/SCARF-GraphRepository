package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.eclipse.topology.domain.*;




public class ConcreteNodeDao extends NodeDao {

	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final PreFixDao preFixDao = daoFactory.getPreFixDao();
    private final RequirementDao requirementDao = daoFactory.getRequirementDao();
    private final CapabilityDao capabilityDao = daoFactory.getCapabilityDao();




	public enum concreteNodeLabel implements Label {
		concreteNode,γ;
	}

	public enum capabilityNode implements Label {
		Capability;
	}

	public enum concreteNodeRelationships implements RelationshipType{
		HAS_CAPABILITY,REFIEND_AS,REFERS_TO,HAS_REQUIREMENT;
	}


	public Node create(ConcreteNode concreteNode){

    	String query = "match (n:abstractNode {type:"+"'"+concreteNode.getAbstractType()+"'"+"}) return n";

    	try ( Transaction tx = db.beginTx();Result result = db.execute(query);)



    	{
    		String nodeName =  concreteNode.getName();
    	    List<Node> capabilityNode = new ArrayList<>();
    		Label dynamicLabel = DynamicLabel.label(nodeName);
    	    Node nodeTypeDb = db.createNode(dynamicLabel,ConcreteNodeDao.concreteNodeLabel.concreteNode,ConcreteNodeDao.concreteNodeLabel.γ);
    	    nodeTypeDb.setProperty( "name", concreteNode.getName() );
    	    //to handle the prefix
    	    String prefix = preFixDao.getPrefixByNameSpaceUrl(concreteNode.getTargetNamespace());
    	    if(prefix != null){
    	    	nodeTypeDb.setProperty( "type", prefix+":"+concreteNode.getName() );
    	    }
    	    nodeTypeDb.setProperty( "specification", concreteNode.getSpecification() );

    	    if(concreteNode.getCapabilities() != null){
    	    Iterator<Capability> iteratorCapability = concreteNode.getCapabilities().getCapability().iterator();
    	    Capability nextCapability = null;
    	     while (iteratorCapability.hasNext()) {
    	    	nextCapability = iteratorCapability.next();
    	    	Node nodeCapabilityDb = capabilityDao.create(nextCapability);
    			nodeTypeDb.createRelationshipTo(nodeCapabilityDb,concreteNodeRelationships.HAS_CAPABILITY);
    			capabilityNode.add(nodeCapabilityDb);
    	     }
    	    }
    	    if(concreteNode.getRequirements() != null){
    	    Iterator<Requirement> iteratorRequirement = concreteNode.getRequirements().getRequirement().iterator();
    	    Requirement nextRequirement = null;
    	     while (iteratorRequirement.hasNext()) {
    	    	nextRequirement = iteratorRequirement.next();
    	    	Node nodeRequirementDb = requirementDao.create(nextRequirement);
    			nodeTypeDb.createRelationshipTo(nodeRequirementDb,concreteNodeRelationships.HAS_REQUIREMENT);
    	     }
    	    }

    	    //Try to attached to an existing abstract Node

    	    while ( result.hasNext() )
    	    {
    	        Map<String,Object> row = result.next();

    	         for ( String key : result.columns() )
    	         {

    	             Node abstractNode = (Node) row.get( key );

    	             //Aggregate the capability node id to the corresponding abstract Node only if the abstract node is the top level node(level == "root")

    	            System.out.println(abstractNode.getProperty("level"));
    	            if( abstractNode.getProperty("level").equals("root")){

    	             //get the array of  capability node id

    	            	//Object ob = new Object();

    	            	long [] nodeIdOfCapability = null;
    	            	nodeIdOfCapability = (long[]) abstractNode.getProperty("IdOfCapabilityNode", nodeIdOfCapability);

    	            	if( nodeIdOfCapability == null){// no capability yet,create capability

    	            		 long[] newNodeIdOfCapability = new long[capabilityNode.size()];

    	            		 for(int i=0; i<capabilityNode.size();i++){
    	            			 newNodeIdOfCapability[i] = capabilityNode.get(i).getId();
    	            		 }
    	    	             abstractNode.setProperty("IdOfCapabilityNode", newNodeIdOfCapability);

    	            	}
    	            	else{//if has capability existed, get it and add new one
   	            		     long[] newNodeIdOfCapability = new long[capabilityNode.size()+nodeIdOfCapability.length];

   	            		 for(int i=0;i<nodeIdOfCapability.length;i++){
   	            			newNodeIdOfCapability[i] = nodeIdOfCapability[i];
   	            		 }

   	            		 for(int i=nodeIdOfCapability.length,j=0; i<newNodeIdOfCapability.length;i++,j++){
   	            			newNodeIdOfCapability[i] = capabilityNode.get(j).getId();
   	            		 }

   	            		  abstractNode.setProperty("IdOfCapabilityNode", newNodeIdOfCapability);

    	            	}

    	            }


    	             abstractNode.createRelationshipTo(nodeTypeDb,concreteNodeRelationships.REFIEND_AS);


    	         }
    	    }
    	    tx.success();

    		return nodeTypeDb;
    	}



    }
    /**
     * For a give concreteNode, this method return all instance nodes which refiend_as it
     * @return instance nodes list of this concrete node
     */

	public List<Node> getInstanceNodes(Node concreteNode) throws java.lang.NullPointerException {
		List<Node> instanceNodes = new ArrayList<Node>();
		String query = "MATCH (a)-[r:REFERS_TO]->(b) WHERE id(b)="+ concreteNode.getId()+" "+"RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{
			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();
      	         for ( String key : result.columns() )
      	         {
      	           Node instanceNode=  (Node) row.get( key );
      	           instanceNodes.add(instanceNode);
      	         }
      	    }
    	    tx.success();
	    }
		return instanceNodes;
	}

   private List<Relationship> getInstanceRelation(Node concreteNode){
		List<Relationship> instanceRelationList = new ArrayList<Relationship>();
		String query = "MATCH (a)-[r:REFERS_TO]->(b) WHERE id(b)="+ concreteNode.getId()+" "+"RETURN r";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
   	{

			  while ( result.hasNext() )
     	    {
     	        Map<String,Object> row = result.next();

     	         for ( String key : result.columns() )
     	         {
     	        	Relationship instanceRelation=  (Relationship) row.get( key );
     	        	instanceRelationList.add(instanceRelation);
     	         }
     	    }
   	    tx.success();
	    }
		return instanceRelationList;

   }

	public List<Node> getAllConcreteNode(){
		List<Node> allConcreteNode = new ArrayList<Node>();
		String query = "MATCH (a:concreteNode) RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	Node node=  (Node) row.get( key );
      	        	allConcreteNode.add(node);
      	         }
      	    }
	    }
		return allConcreteNode;
	}

	public Node getConcreteNodeById(long id){
		Node concreteNode = null;
		String query = "MATCH (a:concreteNode) where id(a)="+id+" "+"RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	concreteNode =  (Node) row.get( key );
      	         }
      	    }
	    }
		return concreteNode;
	}


	public Node getAbstractNode(Node concreteNode) {
		// TODO Auto-generated method stub
		Node abstractNode = null;
		String query = "MATCH (a)-[r:REFIEND_AS]->(b) WHERE id(b)="+ concreteNode.getId()+" "+"RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	abstractNode=  (Node) row.get( key );
      	         }
      	    }

	    }
		return abstractNode;

	}


	   private List<Relationship> getAbstractRelation(Node concreteNode){
			List<Relationship> abstractRelationList = new ArrayList<Relationship>();
			String query = "MATCH (a)-[r:REFIEND_AS]->(b) WHERE id(b)="+ concreteNode.getId()+" "+"RETURN r";
			try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
	   	{

				  while ( result.hasNext() )
	     	    {
	     	        Map<String,Object> row = result.next();

	     	         for ( String key : result.columns() )
	     	         {
	     	        	Relationship abstractRelation=  (Relationship) row.get( key );
	     	        	abstractRelationList.add(abstractRelation);
	     	         }
	     	    }
	   	    tx.success();
		    }
			return abstractRelationList;

	   }

	public List<Node> getRequirementNodes(Node concreteNode) {
		// TODO Auto-generated method stub
		List<Node> requirementNodes = new ArrayList<Node>();
		Node requirementNodeDB = null;
		String query = "MATCH (a)-[r:HAS_REQUIREMENT]->(b) WHERE id(a)="+ concreteNode.getId()+" "+"RETURN b";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{
            //Suppose only one nodeType has only one capability node, here can be modified
			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	requirementNodeDB=  (Node) row.get( key );
      	        	requirementNodes.add(requirementNodeDB);
      	         }
      	    }


    	    tx.success();
	    }
		return requirementNodes;
	}

	   private List<Relationship> getRequirementRelation(Node concreteNode){
			List<Relationship> requirementRelationList = new ArrayList<Relationship>();
			String query = "MATCH (a)-[r:HAS_REQUIREMENT]->(b) WHERE id(a)="+ concreteNode.getId()+" "+"RETURN r";
			try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
	   	{

				  while ( result.hasNext() )
	     	    {
	     	        Map<String,Object> row = result.next();

	     	         for ( String key : result.columns() )
	     	         {
	     	        	Relationship requirementRelation=  (Relationship) row.get( key );
	     	        	requirementRelationList.add(requirementRelation);
	     	         }
	     	    }
	   	    tx.success();
		    }
			return requirementRelationList;

	   }


	public List<Node> getCapabilityNodes(Node concreteNode) {
		// TODO Auto-generated method stub
		Node capabilityNodeDB = null;
		List<Node> capabilityNodes = new ArrayList<Node>();
		String query = "MATCH (a)-[r:HAS_CAPABILITY]->(b) WHERE id(a)="+ concreteNode.getId()+" "+"RETURN b";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{
            //Suppose only one nodeType has only one capability node, here can be modified
			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	capabilityNodeDB=  (Node) row.get( key );
      	        	capabilityNodes.add(capabilityNodeDB);
      	         }
      	    }

    	    tx.success();
	    }
		return capabilityNodes;
	}

	   private List<Relationship> getCapabilityRelation(Node concreteNode){
			List<Relationship> capabilityRelationList = new ArrayList<Relationship>();
			String query = "MATCH (a)-[r:HAS_CAPABILITY]->(b) WHERE id(a)="+ concreteNode.getId()+" "+"RETURN r";
			try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
	   	{

				  while ( result.hasNext() )
	     	    {
	     	        Map<String,Object> row = result.next();

	     	         for ( String key : result.columns() )
	     	         {
	     	        	Relationship capabilityRelation=  (Relationship) row.get( key );
	     	        	capabilityRelationList.add(capabilityRelation);
	     	         }
	     	    }
	   	    tx.success();
		    }
			return capabilityRelationList;

	   }

	   public boolean deleteConcreteNode(long concreteNodeId){
		   boolean deleteResult = false;
		   Node concreteNode = this.getNodeById(concreteNodeId);
		     if(concreteNode!=null){
		     List<Node> instanceNodes = this.getInstanceNodes(concreteNode);
		     List<Node> requirementNodes = this.getRequirementNodes(concreteNode);
		     List<Node> capabilityNodes = this.getCapabilityNodes(concreteNode);
		       if( deleteInstanceRelation(concreteNode)&&
		           deleteAbstractRelation(concreteNode)&&
		           deleteRequirementRelation(concreteNode)&&
		           deleteCapabilityRelation(concreteNode)){
		    	   try(Transaction tx = db.beginTx();){
		    	     for(Node oneNode:requirementNodes){
		    		   oneNode.delete();
		    	     }
		    	     for(Node oneNode:instanceNodes){
		    		   oneNode.delete();
		    	     }
		    	     for(Node oneNode:capabilityNodes){
		    		   oneNode.delete();
		    	     }
		    	     concreteNode.delete();
		    	   tx.success();
		    	   }
		    	  deleteResult = true;
		        }
		     }
		   return deleteResult;
	   }

	   private boolean deleteInstanceRelation(Node concreteNode){
		   boolean deleteResult = false;
		      List<Relationship> instanceRelation = this.getInstanceRelation(concreteNode);
		      if(instanceRelation.isEmpty()){
		    	  deleteResult =true;
		      }
		      else{
		    	  try(Transaction tx = db.beginTx();){
		    	  for(Relationship oneRelation:instanceRelation){
		    		  oneRelation.delete();
		    	  }
		    	  tx.success();
		    	  }
		    	  deleteResult =true;
		      }
		   return deleteResult;
	   }

	   private boolean deleteAbstractRelation(Node concreteNode){
		   boolean deleteResult = false;
		      List<Relationship> abstractRelation = this.getAbstractRelation(concreteNode);
		      if(abstractRelation.isEmpty()){
		    	  deleteResult =true;
		      }
		      else{
		    	  try(Transaction tx = db.beginTx();){
		    	  for(Relationship oneRelation:abstractRelation){
		    		  oneRelation.delete();
		    	  }
		    	  tx.success();
		    	  }
		    	  deleteResult =true;
		      }
		   return deleteResult;
	   }

	   private boolean deleteRequirementRelation(Node concreteNode){
		   boolean deleteResult = false;
		      List<Relationship> requirementRelation = this.getRequirementRelation(concreteNode);
		      if(requirementRelation.isEmpty()){
		    	  deleteResult =true;
		      }
		      else{
		    	  try(Transaction tx = db.beginTx();){
		    	  for(Relationship oneRelation:requirementRelation){
		    		  oneRelation.delete();
		    	  }
		    	  tx.success();
		    	  }
		    	  deleteResult =true;
		      }
		   return deleteResult;
	   }

	   private boolean deleteCapabilityRelation(Node concreteNode){
		   boolean deleteResult = false;
		      List<Relationship> capabilityRelation = this.getCapabilityRelation(concreteNode);
		      if(capabilityRelation.isEmpty()){
		    	  deleteResult =true;
		      }
		      else{
		    	  try(Transaction tx = db.beginTx();){
		    	  for(Relationship oneRelation:capabilityRelation){
		    		  oneRelation.delete();
		    	  }
		    	  tx.success();
		    	  }
		    	  deleteResult =true;
		      }
		   return deleteResult;
	   }

	public Policy getPolicy(Node concreteNode) {
		// TODO Auto-generated method stub
		return null;
	}


	public Node getPolicyNode(Node concreteNode) {
		// TODO Auto-generated method stub
		return null;
	}

}

