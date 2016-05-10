package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.topology.domain.Capability;
import org.eclipse.topology.domain.AbstractSubTopologyDomain.AbstractNode;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class AbstractNodeDao{
	

	 GraphDatabaseService db = DbCon.GetDbConnect();
	public enum abstractNodeLabel implements Label {
		abstractNode;
	}
	




	public Node create(AbstractNode abstractNode) {
		// TODO Auto-generated method stub

		try ( Transaction tx = db.beginTx())
    	{   

    	    Node abstractNodeDb = db.createNode(abstractNodeLabel.abstractNode);
			abstractNodeDb.setProperty("name", abstractNode.getName());
			abstractNodeDb.setProperty("id", abstractNode.getId());
			abstractNodeDb.setProperty("type", abstractNode.getType());
			abstractNodeDb.setProperty("level", abstractNode.getLevel());
    		String nodeName =  abstractNode.getName();

    
    	    tx.success();
    		return abstractNodeDb;
	    }
	}



	public List<Node> getConcreteNodes(Node abstractNode) {
		// TODO Auto-generated method stub
		List<Node> concreteNodes = new ArrayList<Node>();
		String query = "MATCH (a)-[r:REFIEND_AS]->(b) WHERE id(a)="+ abstractNode.getId()+" "+"RETURN b";
		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{   

			  while ( result.hasNext() )
      	    {  
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {            	             
      	           Node concreteNode=  (Node) row.get( key );
      	           concreteNodes.add(concreteNode);           	                        	    
      	         }
      	    }

    
    	    tx.success();
	    }
		return concreteNodes;
	}
	
	public  Map<Node,List<Capability>> getAggregatingCapabilityFromOneRootNode(Node rootNodeOfAbstractSubTopology){
		Map<Node,List<Capability>> concreteNodeToItsCapabilitesMapper = new HashMap();

		try ( Transaction tx = db.beginTx();){  
			long nodeId = rootNodeOfAbstractSubTopology.getId();
			String query="START a=node("+nodeId+") MATCH a-[r:REFIEND_AS]->d RETURN d;";
			//get All concrete nodes of this root nodes
		    Result result = db.execute(query);
	          Node oneNode = null;
	      	  while ( result.hasNext() )
	   	       {  
	   	         Map<String,Object> row = result.next();
	   	         for ( String key : result.columns() )
	   	         {            	             
	   	             oneNode=  (Node) row.get( key );//one concrete node
	   	             //now get capability nodes of this node
	   	             long concreteNodeId = oneNode.getId();
	   	             String queryCapability="START a=node("+concreteNodeId+") MATCH a-[r:HAS_CAPABILITY]->d RETURN d;";
	   	             Result resultOfCapabilities = db.execute(queryCapability);
		             Node oneCapability = null;
		             List<Capability> capabilitesOfOneConcreteNode = new ArrayList<Capability>();
		      	        while ( resultOfCapabilities.hasNext() ){  
		   	               Map<String,Object> row2= resultOfCapabilities.next();
		   	                 for ( String key2 : resultOfCapabilities.columns() ) {            	             
		   	                	oneCapability=  (Node) row2.get( key2 );//one capability node
		   	                    Capability capability = new Capability();		
		   		  		        capability.setName((String) oneCapability.getProperty("Name"));	  
		   		  		        String namespace = (String) oneCapability.getProperty("Namespace");
		   		  		        String localpart = (String) oneCapability.getProperty("Localpart");
		   		  		        String prefix = (String) oneCapability.getProperty("Prefix");
		   		  		        QName qName = new QName(namespace, localpart, prefix);
		   		  		        capability.setType(qName);
		   		  		        int tempLowerBound = (int) oneCapability.getProperty("LowerBound");
		   		  		        Integer lowerBound = new Integer(tempLowerBound);
		   		  		        capability.setLowerBound(lowerBound);
		   		  		        capability.setUpperBound((String) oneCapability.getProperty("UpperBound"));
		   		  		        capabilitesOfOneConcreteNode.add(capability);
	   	                     }
		   	              concreteNodeToItsCapabilitesMapper.put(oneNode, capabilitesOfOneConcreteNode);
		      	        }
	   	         }
	   	       }
			
		}
		return concreteNodeToItsCapabilitesMapper;
	}


	
	public void delete(AbstractNode id) {
		// TODO Auto-generated method stub
		//delete abstract is not supported
	}

	/*public  List<Capability> getAggregatingCapabilityFromRootNode(Node rootNodeOfAbstractSubTopology){
		  Map<Long,List<Capability>> concreteNodeToItsCapabilitesMapper = new HashMap();
		  List<Capability> capabilitesOfOneAbstarctTopologyNode = new ArrayList<Capability>();
		  try ( Transaction tx = db.beginTx();){  
			long [] nodeIdOfCapability = null;    	            	
	      	nodeIdOfCapability = (long[]) rootNodeOfAbstractSubTopology.getProperty("IdOfCapabilityNode", nodeIdOfCapability);
	      	if(nodeIdOfCapability!=null){
	      	  for(int i=0;i<nodeIdOfCapability.length;i++){
	      	  String query="start n=node("+nodeIdOfCapability[i]+") return n";
	          Result result = db.execute(query);
	          Node oneNode = null;
	      	  while ( result.hasNext() )
	   	       {  
	   	         Map<String,Object> row = result.next();
	   	         for ( String key : result.columns() )
	   	         {            	             
	   	             oneNode=  (Node) row.get( key );     	                        	    
	   	         }
	   	       }
	  		  Capability capability = new Capability();		
	  		  capability.setName((String) oneNode.getProperty("Name"));	  
	  		  String namespace = (String) oneNode.getProperty("Namespace");
	  		  String localpart = (String) oneNode.getProperty("Localpart");
	  		  String prefix = (String) oneNode.getProperty("Prefix");
	  		  QName qName = new QName(namespace, localpart, prefix);
	  		  capability.setType(qName);
	  		  int tempLowerBound = (int) oneNode.getProperty("LowerBound");
	  		  Integer lowerBound = new Integer(tempLowerBound);
	  		  capability.setLowerBound(lowerBound);
	  		  capability.setUpperBound((String) oneNode.getProperty("UpperBound"));
	  		  capabilitesOfOneAbstarctTopologyNode.add(capability);
	      	  }
	      	}
		  }
		  
		  return capabilitesOfOneAbstarctTopologyNode;
		  	  
	  }*/
	  //TEST
/*	 public static void main(String [] args){
		  String query="start n=node(5) return n";
	      //Result result = db.execute(query);
	      Node oneNode = null;
	  	  while ( result.hasNext() )
		       {  
		         Map<String,Object> row = result.next();
		         for ( String key : result.columns() )
		         {            	             
		             oneNode=  (Node) row.get( key );     	                        	    
		         }
		       }
	  	//getAggregatingCapabilityFromOneRootNode(oneNode);
		  
		}
*/



}
