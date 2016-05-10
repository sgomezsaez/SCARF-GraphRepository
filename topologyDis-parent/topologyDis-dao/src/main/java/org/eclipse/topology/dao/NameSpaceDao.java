package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.domain.NameSpace;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class NameSpaceDao extends NodeDao {
	GraphDatabaseService db = DbCon.GetDbConnect();
	public enum nameSpaceLabel implements Label {
		PreFixMapper,namespaceindex;
	}
	public enum nameSpaceRelationships implements RelationshipType{
		HAS_NAMESPACE;
	}
    
	
	
	public Node create(NameSpace namespace){
		Node nameSpaceIndex = null;
		Node newNameSpace = null;
		nameSpaceIndex = getNameSpaceIndex();
		
		try ( Transaction tx = db.beginTx();){    
			newNameSpace = db.createNode(nameSpaceLabel.PreFixMapper);
			newNameSpace.setProperty("id",namespace.getId());
			newNameSpace.setProperty("prefix",namespace.getPrefix() );
			newNameSpace.setProperty("namespaceurl",namespace.getNamespaceurl());
		    tx.success();
			//create relation to namespaceindex
    	}
		createRelationToNamespaceIndex(newNameSpace, nameSpaceIndex);
		return newNameSpace;			
	}
	
	private void createRelationToNamespaceIndex(Node newNameSpace, Node nameSpaceIndex){
		try ( Transaction tx = db.beginTx();){       	
			//create relation to namespaceindex
			nameSpaceIndex.createRelationshipTo(newNameSpace,nameSpaceRelationships.HAS_NAMESPACE);
		    tx.success();
    	}		
	}
	
	private Node getNameSpaceIndex(){
		Node nameSpaceIndex = null;
		String queryNamespaceIndex = "match(n:namespaceindex) return n";
		try ( Transaction tx = db.beginTx();){ 
			 Result result = db.execute(queryNamespaceIndex);
		        while ( result.hasNext() )
			    {
			        Map<String,Object> row = result.next();
			         for ( String key : result.columns() )
			         {	  	             
			         nameSpaceIndex = (Node) row.get( key ); 
			         }
			    }			
		}
		if(nameSpaceIndex==null){
			nameSpaceIndex = createNameSpaceIndex();
		}
	return nameSpaceIndex;
	}
	
   private Node createNameSpaceIndex(){
	   try ( Transaction tx = db.beginTx();){       	
	   Node nameSpaceIndex = db.createNode(nameSpaceLabel.namespaceindex);
	   tx.success();
	   return nameSpaceIndex;
	   }
   }
   
	public List<Node> getAllNameSpaces(){
		List<Node> allNameSpaces = new ArrayList<Node>();
		String query = "MATCH (a:PreFixMapper) RETURN a";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{   

			  while ( result.hasNext() )
      	    {  
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {            	             
      	        	Node node=  (Node) row.get( key );  
      	        	allNameSpaces.add(node);
      	         }
      	    }
	    }		
		return allNameSpaces;		
	}
	
	   public boolean deleteNameSpace(long nameSpaceId){
		   boolean deleteResult = false;
		   Node relationshipTypeNode = this.getNodeById(nameSpaceId);
		   String deletePrefixMapper = "START n=node("+nameSpaceId+") MATCH n-[r]-()  DELETE r, n";
			try ( Transaction tx = db.beginTx();Result result = db.execute(deletePrefixMapper);)
	    	{   
               tx.success();
               deleteResult = true;
		    }
			return deleteResult;	

       }
	   
}
