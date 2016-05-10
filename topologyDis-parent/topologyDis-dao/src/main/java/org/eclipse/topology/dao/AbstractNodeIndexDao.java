package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class AbstractNodeIndexDao extends NodeDao {
	GraphDatabaseService db = DbCon.GetDbConnect();
	
	public enum abstractNodeIndexLabel implements Label {
		abstractSkeletonIndex;
	}
	


	public Node createIndex(AbstractSubTopologyDomain abstractSkeletonDomain) {
		// TODO Auto-generated method stub

		try ( Transaction tx = db.beginTx())
    	{   

    	    Node abstractSkeletonIndexDb = db.createNode(abstractNodeIndexLabel.abstractSkeletonIndex);
    	    abstractSkeletonIndexDb.setProperty("abstractSkeletonName", abstractSkeletonDomain.getAbstractSkeletonName());
    	    abstractSkeletonIndexDb.setProperty("abstractSkeletonId", abstractSkeletonDomain.getAbstractSkeletonId());
    	    abstractSkeletonIndexDb.setProperty("specification", abstractSkeletonDomain.getSpecification());
    	    abstractSkeletonIndexDb.setProperty("createDate", abstractSkeletonDomain.getCreatDate());
    	    //abstractSkeletonIndexDb.setProperty("createdBy", abstractSkeletonDomain.getCreatedBy());


    
    	    tx.success();
    		return abstractSkeletonIndexDb;
	    }
	}
	
	
	
	public List<Long> getAllAbstractTopologyIndex() {
		// TODO Auto-generated method stub
        List<Long> allAbstractTopologyIndex = new ArrayList<Long>();
        String query = "match (n:abstractSkeletonIndex) return n";
		
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){			
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {	Node tempNode;   	             
	    	         tempNode = (Node) row.get( key ); 
	    	         allAbstractTopologyIndex.add(tempNode.getId());
	    	         }
	    	    }
	    	    tx.success();			
		}
		return allAbstractTopologyIndex;
	}
	
	public List<Node> getAllAbstractTopologyIndexNodes() {
		// TODO Auto-generated method stub
        List<Node> allAbstractTopologyIndexNodes= new ArrayList<Node>();
        String query = "match (n:abstractSkeletonIndex) return n";
		
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){			
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {	Node tempNode;   	             
	    	         tempNode = (Node) row.get( key ); 
	    	         allAbstractTopologyIndexNodes.add(tempNode);
	    	         }
	    	    }
	    	    tx.success();			
		}
		return allAbstractTopologyIndexNodes;
	}
	
	public Node getAbstractTopologyIndexNodeById(long id){
		Node abstractTopologyIndexId = null;
		List<Long> allAbstractTopologyIndex = this.getAllAbstractTopologyIndex();
		for(long tempId:allAbstractTopologyIndex){
		   if(tempId == id){
			   abstractTopologyIndexId = this.getNodeById(tempId);
			   break;
		   }
		}		
		return abstractTopologyIndexId;				
	}
}
