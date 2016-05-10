package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.NodeDao;
import org.eclipse.topology.domain.Workload;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class WorkloadDao extends NodeDao{

	GraphDatabaseService db = DbCon.GetDbConnect();

	public enum WorkloadLabel implements Label {
		workload,topologyStatistics;
	}



	public Node create(Workload workload){
		Node workloadDB = null;
		try ( Transaction tx = db.beginTx();){
			workloadDB = db.createNode(WorkloadLabel.workload,WorkloadLabel.topologyStatistics);
			workloadDB.setProperty("pattern",workload.getPattern() );
			workloadDB.setProperty("arrival",workload.getArrival());
			workloadDB.setProperty("behavioral",workload.getBehavioral() );
			workloadDB.setProperty("avg_users",workload.getAvgUsers() );
			workloadDB.setProperty("avg_transactions",workload.getAvgTransactions());
			workloadDB.setProperty("id", workload.getId());
			workloadDB.setProperty("startTime", workload.getStartTime());
			workloadDB.setProperty("endTime", workload.getEndTime());
		    tx.success();
    	}
		return workloadDB;

	}

	public Workload getWorkload(Node nodeDb){
		Workload workload = new Workload();
		try ( Transaction tx = db.beginTx();){
            workload.setArrival((String) nodeDb.getProperty("arrival"));
            workload.setAvgTransactions((short) nodeDb.getProperty("avg_transactions"));
            workload.setPattern((String) nodeDb.getProperty("pattern"));
            workload.setBehavioral((String) nodeDb.getProperty("behavioral"));
            workload.setAvgUsers((short) nodeDb.getProperty("avg_users"));
            workload.setId((String) nodeDb.getProperty("id"));
            workload.setStartTime((String) nodeDb.getProperty("startTime"));
            workload.setEndTime((String) nodeDb.getProperty("endTime"));
    	}
		return workload;
	}

	public List<Node> getAllWorkloadNodesForOneTopology(long alphaTotpologyID) throws org.neo4j.graphdb.QueryExecutionException{
		List<Node> allWorkloadsNodes = new ArrayList<Node>();
		String getAllHistory ="start n=node("+alphaTotpologyID+") match n-[r:PERFORM_WORKLOAD]->b return b";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllHistory);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {	Node tempNode;
	         tempNode = (Node) row.get( key );
	         allWorkloadsNodes.add(tempNode);
	         }
	    }
		tx.success();
		}
		return allWorkloadsNodes;
	}

	public List<Node> getAllWorkloadNodes(){
		List<Node> allWorkloadsNodes = new ArrayList<Node>();
		String getAllHistory ="MATCH (n:workload) RETURN n";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllHistory);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {	Node tempNode;
	         tempNode = (Node) row.get( key );
	         allWorkloadsNodes.add(tempNode);
	         }
	    }
		tx.success();
		}
		return allWorkloadsNodes;
	}


	public Node getWorkloadByWorkLoadID(String id){
		Node workload = null;
		String getAllHistory ="MATCH (n:workload) where n.id = '"+id+"'  RETURN n";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllHistory);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {
	        	 workload = (Node) row.get( key );
	         }
	    }
		}
		return workload;
	}

	  public boolean isWorkload(long workloadID) throws NotFoundException {

		  boolean result = false;
		  try ( Transaction tx = db.beginTx()){
		   Node node = db.getNodeById(workloadID);
		   if(node != null){
			   Iterator<Label> labels =  node.getLabels().iterator();
			   while(labels.hasNext()){
	             Label oneLable = labels.next();
	             if(oneLable.name().equals("workload")){
	             result = true;
	             }
			   }
		   }
		  tx.close();
		  return result;
	  }

	  }


}
