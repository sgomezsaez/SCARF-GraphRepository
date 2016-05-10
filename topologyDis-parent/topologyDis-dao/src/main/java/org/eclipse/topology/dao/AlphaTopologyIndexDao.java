package org.eclipse.topology.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.Utility.Constants;
import org.eclipse.topology.domain.AlphaTopology;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class AlphaTopologyIndexDao extends NodeDao{
	GraphDatabaseService db = DbCon.GetDbConnect();

	public enum alphaTopologyIndexLabel implements Label {
		alphaTopologyIndex;
	}

	private final DaoFactory daoFactory = new DaoFactory();
	private final HistoryDao historyDao = daoFactory.getHistoryDao();
	private final PerformanceDao performanceDao = daoFactory.getPerformanceDao();
	private final WorkloadDao workloadDao = daoFactory.getWorkloadDao();
	  public boolean isAlphaTopology(long alphaTopologyId) throws NotFoundException {

		  boolean result = false;
		  try ( Transaction tx = db.beginTx()){
		   Node node = db.getNodeById(alphaTopologyId);
		   if(node != null){
			   Iterator<Label> labels =  node.getLabels().iterator();
			   while(labels.hasNext()){
	             Label oneLable = labels.next();
	             if(oneLable.name().equals("alphaTopologyIndex")){
	             result = true;
	             }
			   }
		   }
		  return result;
	  }

	  }

	public Node createIndex(AlphaTopology alphaTopology) {
		// TODO Auto-generated method stub

		try ( Transaction tx = db.beginTx())
    	{

    	    Node alphaTopologyIndexDb = db.createNode(alphaTopologyIndexLabel.alphaTopologyIndex);
    	    alphaTopologyIndexDb.setProperty("alphaTopologyName", alphaTopology.getName());
    	    alphaTopologyIndexDb.setProperty("alphaTopologyId", alphaTopology.getId());
    	    alphaTopologyIndexDb.setProperty("specification", alphaTopology.getSepcification());
    	    alphaTopologyIndexDb.setProperty("createDate", alphaTopology.getCreatDate());
    	    alphaTopologyIndexDb.setProperty("specificationType", alphaTopology.getSpecificationType());
    	    alphaTopologyIndexDb.setProperty("targetNamespace", alphaTopology.getTargetNamespace());
    	    //alphaTopologyIndexDb.setProperty("createdBy", abstractSkeletonDomain.getCreatedBy());



    	    tx.success();
    		return alphaTopologyIndexDb;
	    }
	}

	public List<Long> getAllAlphaTopologyIndex() {
		// TODO Auto-generated method stub
        List<Long> allAlphaTopologyIndex = new ArrayList<Long>();
        String query = "match (n:alphaTopologyIndex) return n";

		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {	Node tempNode;
	    	         tempNode = (Node) row.get( key );
	    	         allAlphaTopologyIndex.add(tempNode.getId());
	    	         }
	    	    }
	    	    tx.success();
		}
		return allAlphaTopologyIndex;
	}

	public boolean  performWorkload(long alphaTopologyId, long workloadId) throws NotFoundException {
		// TODO Auto-generated method stub
		String employDate = null;
		boolean result = false;
	       if(!workloadDao.isWorkload(workloadId)){
	        	return false;
	        }
	        if(!this.isAlphaTopology(alphaTopologyId)){
	        	return false;
	        }
		try ( Transaction tx = db.beginTx()){
		String checkIfWorkloadExisted = "match (n), n-[r:"+Constants.workloadRelation+"]-b where id(n)="+alphaTopologyId+" return b";
		Result queryResult = db.execute(checkIfWorkloadExisted);
		  while ( queryResult.hasNext() )
    	    {
    	        Map<String,Object> row = queryResult.next();

    	         for ( String key : queryResult.columns() )
    	        {
    	        	 Node oneNode=  (Node) row.get( key );
   	        	     if(oneNode.getId()==workloadId){//if there is an existed performance with the same id to be performed
   	        		  return false;
   	        		  }
    	         }
    	   }

		Node sourceNode = this.getNodeById(alphaTopologyId);
		Node targetNode = this.getNodeById(workloadId);
	       if(sourceNode == null || targetNode == null){
	        	return false;
	        }
        //create time stamp
		//Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		//employDate = timeStamp.toString();//Timestamp newtimeStamp = timeStamp.valueOf(employDate) to convert it back
		//build relation
		DynamicRelationshipType dynamicRelation = DynamicRelationshipType.withName(Constants.workloadRelation);
		Relationship relationship = sourceNode.createRelationshipTo(targetNode,dynamicRelation);
		//relationship.setProperty("employDate", employDate);
	   	tx.success();
	   	result = true;
		}

		return result;
	}

	public List<Node> getAllMuTopologyNodes(long id){
		List<Node> muTopologyNodes = new ArrayList<Node>();

		String getAllMuTopologies = "match (n:alphaTopologyIndex), n-[r:"+Constants.mutopologyRelation+"]-b where id(n)="+id+" return b";
		Result queryResult = db.execute(getAllMuTopologies);
		  while ( queryResult.hasNext() )
    	    {
    	        Map<String,Object> row = queryResult.next();

    	         for ( String key : queryResult.columns() )
    	         {
    	        	 Node oneNode=  (Node) row.get( key );
    	        	 muTopologyNodes.add(oneNode);
    	         }
    	    }

		return muTopologyNodes;
	}

	public Node getAlphaTopologyNodeIndexByIdAndNameSpace(String id, String namespace) {
		// TODO Auto-generated method stub
        Node node = null;
        String query = "match (n:alphaTopologyIndex) where n.alphaTopologyId ="+"'"+id+"'"+" "+"and n.targetNamespace = "+"'"+namespace+"'"+" "+"return n";

		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();

	    	         for ( String key : result.columns() )
	    	         {
	    	         node = (Node) row.get( key );
	    	         }
	    	    }
		}
		return node;
	}


	public boolean deleteAlphaTopologyById(long id) throws org.neo4j.graphdb.QueryExecutionException {
		// TODO Auto-generated method stub
        boolean resultOfDelete= false;
        boolean deleteWorkloadResult = deleteWorkloadAndPerformanceRelation(id);
        if(deleteWorkloadResult==false){
      	  return false;
        }
        List<Node> allAlphaTopologyNodes = getAllAlphaTopologyNode(id);

        for(Node node:allAlphaTopologyNodes)	{
          boolean deleteResult = deleteAllRelations(node);
          if(deleteResult==false){
        	  return false;
          }
          deleteNode(node);
        }
        Node AlphaTopologyIndex = this.getNodeById(id);
        boolean deleteResult = deleteAllRelations(AlphaTopologyIndex);
        if(deleteResult==false){
      	  return false;
        }
        resultOfDelete = deleteNode(AlphaTopologyIndex);
		return resultOfDelete;
	}

	private boolean deleteNode(Node node){
		boolean deleteResult = false;
		  try ( Transaction tx = db.beginTx();){
			  node.delete();
			  tx.success();
			  deleteResult = true;
		  }
		return deleteResult;
	}

	private boolean deleteAllRelations(Node node){
		boolean deleteResult = false;
		try ( Transaction tx = db.beginTx();){
			 String getAllRelations ="start a=node("+node.getId()+") match a-[r]-(b) return r";
			 Result result = db.execute(getAllRelations);
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();
	    	         for ( String key : result.columns() )
	    	         {
	    	        	 Relationship rel = (Relationship) row.get(key);
	    	        	 rel.delete();
	    	         }
	    	    }

		tx.success();
		deleteResult=true;
		}
		return deleteResult;
	}

	private List<Node> getAllAlphaTopologyNode(long id){
	  List<Node> allAlphaTopologyNodes = new ArrayList<Node>();
	  try ( Transaction tx = db.beginTx();){
		  String getAllAlphaTopologyNodes ="start a=node("+id+") match a-[r:Includes]->(b) where b:α return b";
		  Result result = db.execute(getAllAlphaTopologyNodes);
		  while ( result.hasNext() ){
			  Map<String,Object> row = result.next();
	           for ( String key : result.columns() ){
	        	   Node oneAlphaNode = (Node) row.get( key );
	        	   allAlphaTopologyNodes.add(oneAlphaNode);
	           }
		  }
	  }
	  return allAlphaTopologyNodes;
	}

	private boolean deleteWorkloadAndPerformanceRelation(long id) throws org.neo4j.graphdb.QueryExecutionException{
		boolean deleteResult = false;
		//get all relationship of workload and performance, delete relation only. as workload and performance can be reused,must keep the node
		String getRelationsOfWorkLoadAndPerformance = "start a=node("+id+") match a-[r]->(b) where b:workload or b:performance return r";
		try ( Transaction tx = db.beginTx();Result result = db.execute(getRelationsOfWorkLoadAndPerformance);){
			  while ( result.hasNext() )
	    	    {
	    	        Map<String,Object> row = result.next();
	    	         for ( String key : result.columns() )
	    	         {
	    	        	 Relationship rel = (Relationship) row.get( key );
	    	        	 rel.delete();
	    	         }
	    	    }
			  deleteResult =  true;
		}
	  return deleteResult;
	}

	public boolean  performPerformance(long alphaTopologyId, long performanceId) throws java.lang.NullPointerException {
		// TODO Auto-generated method stub
		String employDate = null;
		boolean result = false;
        if(!performanceDao.isPerformance(performanceId)){
        	return false;
        }
        if(!this.isAlphaTopology(alphaTopologyId)){
        	return false;
        }
		try ( Transaction tx = db.beginTx()){
		Node sourceNode = this.getNodeById(alphaTopologyId);
		Node targetNode = this.getNodeById(performanceId);
        if(sourceNode == null || targetNode == null){
        	return false;
        }


		String checkIfPerformanceExisted = "match (n), n-[r:"+Constants.performanceRelation+"]-b where id(n)="+alphaTopologyId+" return b";
		Result queryResult = db.execute(checkIfPerformanceExisted);
		  while ( queryResult.hasNext() )
    	    {
    	        Map<String,Object> row = queryResult.next();

    	         for ( String key : queryResult.columns() )
    	         {
    	        	 Node oneNode=  (Node) row.get( key );
    	        	  if(oneNode.getId()==performanceId){//if there is an existed performance with the same id to be performed
    	        		  return false;
    	        		  }
    	         }
    	    }


		DynamicRelationshipType dynamicRelation = DynamicRelationshipType.withName(Constants.performanceRelation);
		Relationship relationship = sourceNode.createRelationshipTo(targetNode,dynamicRelation);
		//relationship.setProperty("employDate", employDate);
	   	tx.success();
	   	result = true;
		}

		return result;
	}

	public boolean  unperformWorkload(long alphaTopologyId, long workloadId) {
		// TODO Auto-generated method stub
		boolean result = false;
		//check if there is a workload existed already
		try ( Transaction tx = db.beginTx()){
			String checkIfWorkloadExisted = "match (n), n-[r:"+Constants.workloadRelation+"]->b where id(n)="+alphaTopologyId+" return r,b";
			Result queryResult = db.execute(checkIfWorkloadExisted);
		     String idOfWorkLoad = null;
		     String employDate = null;
		     String unemployDate = null;
			  while ( queryResult.hasNext() ){

	    	         Map<String,Object> row = queryResult.next();

	    	         for ( String key : queryResult.columns() )
	    	         {  switch(key){
	    	              case "r"://relation
	    	        	    Relationship rel = (Relationship) row.get( key );
	    	        	     employDate = (String) rel.getProperty("employDate");
	    	        	    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
	    	        	     unemployDate = timeStamp.toString();
	    	        	     rel.delete();//un-employ workload
	    	        	     break;
	    	              case "b"://workload
	    	            	Node workload = (Node) row.get( key );
		    	        	 idOfWorkLoad = (String) workload.getProperty("id");
		    	        	 break;
	    	           }
	    	         }
		        }
					Node history = historyDao.createWorkLoadHistory(idOfWorkLoad, employDate, unemployDate);
					Node alphaTopologyIndex = this.getNodeById(alphaTopologyId);
					DynamicRelationshipType dynamicRelation = DynamicRelationshipType.withName(Constants.workloadHistoryRelation);
					Relationship relationship = alphaTopologyIndex.createRelationshipTo(history,dynamicRelation);

	    	 	 tx.success();
	    		 result = true;

		}

		return result;
	}

	public void testTime(){
	       //create time stamp
			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
			String employDate = timeStamp.toString();// to convert it back
			String newString = "2009-10-20 00:00:00.000";
			Timestamp newtimeStamp = Timestamp.valueOf(newString);
			System.out.println(newtimeStamp.toString());

	}

	public static void main(String [] args){
		AlphaTopologyIndexDao dao = new AlphaTopologyIndexDao();
		dao.deleteAlphaTopologyById(264);
		//dao.employWorkload(75, 122);
		//dao.unemployWorkload(75, 122);
		//dao.testTime();
	}
}
