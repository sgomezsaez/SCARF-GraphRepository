package org.eclipse.topology.dao;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
public class RelationDao {

	GraphDatabaseService db = DbCon.GetDbConnect();

	
	public Relationship addRelation(Node sourceNode, Node targetNode, String type, String name){
		try ( Transaction tx = db.beginTx()){
		DynamicRelationshipType dynamicRelation = DynamicRelationshipType.withName(name);
		Relationship relationship = sourceNode.createRelationshipTo(targetNode,dynamicRelation);
		relationship.setProperty("type", type);
			    tx.success();
		return relationship;
	 }
	}

    public Node findRelation(String relationshipType){
    	Node relationshipTypeNode = null;;
    	String query = "match (n:RelationshipType {type:"+"'"+relationshipType+"'"+"}) return n";
		
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){
			
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {
	    	             
	    	        	relationshipTypeNode = (Node) row.get( key );
	    	             
    	            	    	    
	    	         }
	    	    }
	    	    tx.success();
			
			
		}
		return relationshipTypeNode;
		
    	
    }
}
