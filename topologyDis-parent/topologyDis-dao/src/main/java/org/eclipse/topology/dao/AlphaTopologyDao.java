package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.topology.domain.Capability;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class AlphaTopologyDao {

  static GraphDatabaseService db = DbCon.GetDbConnect();



  public void getRootAndLeafNodeListByTopologyIndex(long topologyIndexId,List<Node> subtopologyRootNodes,List<Node> subtopologyLeafNodes,List<Node> allSubtopologyNodes){
		try ( Transaction tx = db.beginTx();){
		String queryRoot = "MATCH (a)-[r:Includes]->(b) WHERE id(a)="+ topologyIndexId+" "+"RETURN b";
		Result resultRoot = db.execute(queryRoot);
		while ( resultRoot.hasNext() )
	    {
	        Map<String,Object> row = resultRoot.next();

	         for ( String key : resultRoot.columns() )
	         {
	             Node node=  (Node) row.get( key );
	             if(node.getProperty("level").equals("root")){
	               subtopologyRootNodes.add(node);
	               allSubtopologyNodes.add(node);
	             }
	             else if(node.getProperty("level").equals("leaf")){
	               subtopologyLeafNodes.add(node);
	   	           allSubtopologyNodes.add(node);
	             }
	             else{
	               allSubtopologyNodes.add(node);
	             }
	         }
	    }
		}
  }

  public List<List<Path>>  getAllPathsFromEachStartToEndNode(List<Node> startNodes,List<Node> endNodes ){

      int rootNodeNumber = startNodes.size();
      int leafNodeNumber = endNodes.size();
      List<List<Path>> pathList = new ArrayList<List<Path>>();

      for(int i=0;i<rootNodeNumber;i++){
      	Node root = startNodes.get(i);
      	List<Path> PathListOfOneRootNode= new ArrayList<Path>();
      	for(int j=0;j<leafNodeNumber;j++){
      		Node leaf = endNodes.get(j);
      		String query = "START a=node("+root.getId()+"), d=node("+leaf.getId()+") MATCH p=a-[*]->d RETURN p";
      		try ( Transaction tx = db.beginTx();Result result = db.execute(query);){

          	    while ( result.hasNext() )
          	    {
          	        Map<String,Object> row = result.next();

          	         for ( String key : result.columns() )
          	         {
          	             Path pathTest=  (Path) row.get( key );
          	             PathListOfOneRootNode.add(pathTest);
          	         }
          	    }
          	    tx.success();
      		}
      	}
  	    if(PathListOfOneRootNode.size()!=0){
  	    	pathList.add(PathListOfOneRootNode);
  	    }

      }
      return pathList;
  }



}
