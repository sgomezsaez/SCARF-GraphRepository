package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.domain.RelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class RelationshipTypeDao extends NodeDao {

	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final PreFixDao preFixDao = daoFactory.getPreFixDao();

	public enum relationshipType implements Label {
		RelationshipType;
	}
    public boolean isRelationshipType(long id){
    	boolean result = false;
    	try ( Transaction tx = db.beginTx();){
    	Node relationshipTypeNode = this.getNodeById(id);
    	Iterator<Label> relationshiptype = relationshipTypeNode.getLabels().iterator();
    	while(relationshiptype.hasNext()){
    		Label oneLable = relationshiptype.next();
    		if(oneLable.name().equals("RelationshipType")){
    			result = true;
    		}
    	}
    	return result;
    }

    }
	public Node create(RelationshipType relationshipType) {
		// TODO Auto-generated method stub

		Node relationshipTypeDb = null;
		try ( Transaction tx = db.beginTx();)

    	{


            relationshipTypeDb = db.createNode(RelationshipTypeDao.relationshipType.RelationshipType);
            relationshipTypeDb.setProperty( "name", relationshipType.getName() );
    	    //to handle the prefix
    	    String prefix = preFixDao.getPrefixByNameSpaceUrl(relationshipType.getTargetNamespace());
    	    if(prefix != null){
    	    	relationshipTypeDb.setProperty( "type", prefix+":"+relationshipType.getName() );
    	    }
    	    relationshipTypeDb.setProperty( "specification", relationshipType.getSpecification() );

            //valid resource

    	    //valid target

    	    tx.success();

    		return relationshipTypeDb;
    	}
	}

/*    public String getSpecification(Node node) throws org.neo4j.graphdb.NotFoundException{
    	String specification;
    	try ( Transaction tx = db.beginTx();){

    		specification = (String) node.getProperty("specification");

    	}

    	return specification;
    }*/

	public List<Node> getAllRelationshipType(){
		List<Node> allRelationshipType = new ArrayList<Node>();
		String query = "MATCH (a:RelationshipType) RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	Node node=  (Node) row.get( key );
      	        	allRelationshipType.add(node);
      	         }
      	    }
	    }
		return allRelationshipType;
	}

	public Node getRelationshipTypeById(long id){
		Node relationshipTypeNode = null;
		String query = "MATCH (a:RelationshipType) where id(a)="+id+" "+"RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{

			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	        	relationshipTypeNode =  (Node) row.get( key );
      	         }
      	    }
	    }
		return relationshipTypeNode;
	}

	   public boolean deleteRelationshipType(long relationshipTypeNodeId){
		   boolean deleteResult = false;
		   Node relationshipTypeNode = this.getNodeById(relationshipTypeNodeId);
		     if(relationshipTypeNode!=null){
		    	   try(Transaction tx = db.beginTx();){
		    		   relationshipTypeNode.delete();
		    	   tx.success();
		    	   }
		    	  deleteResult = true;
		        }
		   return deleteResult;
	   }


}
