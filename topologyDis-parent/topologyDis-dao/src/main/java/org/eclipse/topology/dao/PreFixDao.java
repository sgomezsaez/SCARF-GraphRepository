package org.eclipse.topology.dao;

import java.util.Map;

import org.eclipse.topology.dao.DbCon;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class PreFixDao {
	
	Node resultNode = null;
    
	GraphDatabaseService db = DbCon.GetDbConnect();
	
	
	 public String getNameSpaceUrlByPrefix(String prefix){
		String NameSpaceUrl = null;
		
		String query = "match (n:PreFixMapper {prefix:"+"'"+prefix+"'"+"}) return n";
				
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){
			
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {
	    	             
	    	             resultNode = (Node) row.get( key );
	    	             
	    	             NameSpaceUrl = (String) resultNode.getProperty("NameSpaceUrl");
	    	             
	    	    
	    	         }
	    	    }
	    	    tx.success();
			
			
		}
		
		
		return NameSpaceUrl;
		
	}
	
	
	public String getPrefixByNameSpaceUrl(String NameSpaceUrl){
		String Prefix = null;
		String query = "match (n:PreFixMapper {namespaceurl:"+"'"+NameSpaceUrl+"'"+"}) return n";
		
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){
			
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {
	    	             
	    	             resultNode = (Node) row.get( key );
	    	             
	    	             Prefix = (String) resultNode.getProperty("prefix");
	    	             
	    	    
	    	         }
	    	    }
	    	    tx.success();
			
			
		}
		
		
		
		return Prefix;
		
	}
	
}
