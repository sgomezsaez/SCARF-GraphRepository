package org.eclipse.topology.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.Utility.Constants;
import org.eclipse.topology.domain.MuTopology;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class MuTopologyDao extends NodeDao {
	
	GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final AlphaTopologyIndexDao alphaTopologyIndexDao = daoFactory.getAlphaTopologyIndexDao();

	public enum MuTopologyLabel implements Label {
		mutopology;
	}
    
	public Node findAlphaTopologybyMu(Node mu){
		Node alphaTopologyIndex = null;
		try ( Transaction tx = db.beginTx();){
			String alphaTopologyId = this.getProperty(mu, "alphaTopologyId");
			String alphaTopologyNameSpace = this.getProperty(mu, "alphaTopologyNameSpace");
			alphaTopologyIndex = alphaTopologyIndexDao.getAlphaTopologyNodeIndexByIdAndNameSpace(alphaTopologyId, alphaTopologyNameSpace);
    	}
	   return alphaTopologyIndex;
	}
	
	public void maintainMuTopology(Node mu, Node alphaTopologyIndex){
		try ( Transaction tx = db.beginTx();){
			DynamicRelationshipType dynamicRelation = DynamicRelationshipType.withName(Constants.mutopologyRelation);
			alphaTopologyIndex.createRelationshipTo(mu, dynamicRelation);		
			List<Node> MuTopologyNodes = alphaTopologyIndexDao.getAllMuTopologyNodes(alphaTopologyIndex.getId());
			for(Node oneMu:MuTopologyNodes){
				if(this.getProperty(oneMu, "obsolete").equals(Constants.LogicBooleanNo)&&mu.getId()!=oneMu.getId()){//this mutopology is the current using mu
						oneMu.setProperty("obsolete", Constants.LogicBooleanYes);
						Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
						oneMu.setProperty("endDate",timeStamp.toString());
				}			
			}		
		    tx.success();
    	}
		
	}
	
	public Node create(MuTopology muTopology){
		Node mutopologyDB = null;
		try ( Transaction tx = db.beginTx();){       	
			mutopologyDB = db.createNode(MuTopologyLabel.mutopology);
			mutopologyDB.setProperty("alphaTopologyId",muTopology.getAlphaTopologyId() );
			mutopologyDB.setProperty("alphaTopologyName",muTopology.getAlphaTopologyName());
			mutopologyDB.setProperty("alphaTopologyNameSpace",muTopology.getAlphaTopologyNameSpace());
			mutopologyDB.setProperty("specification",muTopology.getSpecification() );
			mutopologyDB.setProperty("obsolete",muTopology.getObsolete());
			mutopologyDB.setProperty("createDate", muTopology.getCreateDate());
			mutopologyDB.setProperty("endDate", "");//not ended yet						
		    tx.success();
    	}
		return mutopologyDB;				
	}
	
	public List<Node> getAllMuTopologyForOneAlphaTopologyById(long alphaTopologyId){
		List<Node> MuTopologiesOfOneAlphaTopology = new ArrayList<Node>();
		String findAllMutopology = "match (n), n-[r:"+Constants.mutopologyRelation+"]->b where id(n)="+alphaTopologyId+" return b";
		try ( Transaction tx = db.beginTx();Result result = db.execute(findAllMutopology);){			
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();
	    	         for ( String key : result.columns() )
	    	         {	  	             
	    	        	 Node oneNode = (Node) row.get( key ); 
	    	        	 MuTopologiesOfOneAlphaTopology.add(oneNode);
	    	         }
	    	    }
		}
		return MuTopologiesOfOneAlphaTopology;		
	}
	
	public Node getOneMuTopologyForOneAlphaTopologyById(long alphaTopologyId, long muTopologyId){
		Node tempNode = null;
		List<Node> muTopologiesOfOneAlphaTopology = new ArrayList<Node>();
		muTopologiesOfOneAlphaTopology = getAllMuTopologyForOneAlphaTopologyById(alphaTopologyId);
         for(Node oneNode: muTopologiesOfOneAlphaTopology){
        	 if(oneNode.getId()==muTopologyId);{
        		 tempNode = oneNode;
        		 break;
        	 }
		}
		return tempNode;		
	}
}
