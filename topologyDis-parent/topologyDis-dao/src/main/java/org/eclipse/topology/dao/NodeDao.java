package org.eclipse.topology.dao;

import java.util.Iterator;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class NodeDao {

	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();

	public   Iterator<Label>  getNodeLabelById(long id){
		Iterator<Label> labelIterator = null;
		try ( Transaction tx = db.beginTx();){
	    Node node = getNodeById(id);
		labelIterator = node.getLabels().iterator();
		}
		return labelIterator;
	}

    public Node getNodeById(long id){
    	Node node =null;
    	String query = "MATCH (s) WHERE ID(s) = "+id+" RETURN s";
    	try ( Transaction tx = db.beginTx();Result result =db.execute(query);){
    		 while ( result.hasNext() )
     	    {
     	        Map<String,Object> row = result.next();

     	         for ( String key : result.columns() )
     	         {
     	            node  = (Node) row.get( key );
     	         }
     	    }
     	    tx.success();
    	}
    	return node;
    }

    public String getSpecification(Node node) throws java.lang.NullPointerException , org.neo4j.kernel.api.exceptions.PropertyNotFoundException{
    	String specification = null;
    	try ( Transaction tx = db.beginTx();){
            specification = (String) node.getProperty("specification");
     	    tx.success();
    	}
    	return specification;
    }

    public String getProperty(Node node,String propertyName){
    	String property = null;
    	try ( Transaction tx = db.beginTx();){
    		property = (String) node.getProperty(propertyName);
     	    tx.success();
    	}
    	return property;
    }



    public void deleteNodeById(long id){
    	Node node =null;
    	String query = "MATCH (s) WHERE ID(s) = "+id+" RETURN s";
    	try ( Transaction tx = db.beginTx();Result result =db.execute(query);){
    		 while ( result.hasNext() )
     	    {
     	        Map<String,Object> row = result.next();

     	         for ( String key : result.columns() )
     	         {
     	            node  = (Node) row.get( key );
     	         }
     	    }
    		  String deleteRel = "MATCH (n) WHERE id(n)= "+id+" OPTIONAL MATCH n-[r]-() DELETE r";
			  db.execute(deleteRel);
    		node.delete();
     	    tx.success();
    	}
    }

}
