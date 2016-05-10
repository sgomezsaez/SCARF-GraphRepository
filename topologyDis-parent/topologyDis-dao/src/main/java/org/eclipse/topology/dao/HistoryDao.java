package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class HistoryDao extends NodeDao{

	public enum workloadHistoryLabel implements Label {
		workloadHistory;
	}

	public Node createWorkLoadHistory(String workloadId, String employDate, String unemployDate ) throws org.neo4j.graphdb.QueryExecutionException{
		Node history = null;
		try ( Transaction tx = db.beginTx();){
		history = db.createNode(workloadHistoryLabel.workloadHistory);
		history.setProperty("workloadid", workloadId);
		history.setProperty("employdate", employDate);
		history.setProperty("unemploydate", unemployDate);
		tx.success();
		}

		return history;
	}

	public List<Node> getAllWorkloadsHistory(long alphaTotpologyID){
		List<Node> allWorkloadsHistoryNodes = new ArrayList<Node>();
		String getAllHistory ="start n=node("+alphaTotpologyID+") match n-[r:PERFORM_WORKLOAD]->b return b";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllHistory);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {	Node tempNode;
	         tempNode = (Node) row.get( key );
	         allWorkloadsHistoryNodes.add(tempNode);
	         }
	    }
		tx.success();
		}
		return allWorkloadsHistoryNodes;
	}
}
