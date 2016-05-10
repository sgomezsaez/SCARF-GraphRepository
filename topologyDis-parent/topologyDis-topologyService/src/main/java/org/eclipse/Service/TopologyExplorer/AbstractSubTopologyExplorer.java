package org.eclipse.Service.TopologyExplorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.Utility.Combination;
import org.eclipse.topology.dao.AbstractNodeDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

/**
 * This class is used to find all possible abstract sub-topologies by traversing an existing abstract topology provided by 
 * cloud topology developer
 * @author Hao
 *
 */
public class AbstractSubTopologyExplorer {
	
	private long abstractSubTopologyIndexId;
	private List<Node> abstractSubtopologyRootNodes = new ArrayList<Node>();
	private List<Node> abstractSubtopologyLeafNodes = new ArrayList<Node>();
	protected List<Node> allAbstractSubtopologyNodes = new ArrayList<Node>();
	private List<List<Path>> pathListOfRootNodes ;
	protected List<List<Path>> foundAbstractSubTopologies;
	protected Map<Node,List<Node>> ConcreteNodeContainer = new HashMap();


	private GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final AbstractNodeDao abstractNodeDao = daoFactory.getAbstractNodeDao();

	
    

	public List<List<Path>> getFoundAbstractSubTopologies() {
		return foundAbstractSubTopologies;
	}


	public List<Node> getAllAbstractSubtopologyNodes() {
		return allAbstractSubtopologyNodes;
	}


	public long getAbstractSubTopologyIndexId() {
		return abstractSubTopologyIndexId;
	}


	public void setAbstractSubTopologyIndexId(long abstractSubTopologyIndexId) {
		this.abstractSubTopologyIndexId = abstractSubTopologyIndexId;
	}

	
	/**
	 * this method make a abstract nodes <<===>> its concretenode, put it to a Map
	 * So there no need to access database every time
	 */
	protected void mapAbstractAndConreteNodes(){
		
		for( Node abstractNode:this.allAbstractSubtopologyNodes){
			List<Node> concreteNodes = abstractNodeDao.getConcreteNodes(abstractNode);
			ConcreteNodeContainer.put(abstractNode, concreteNodes);
		}
		
	}
	

	public List<List<Path>> getAbstractSubTopologyExploerResult() {
		
		getRootAndLeafNodeListByTopologyIndex(this.getAbstractSubTopologyIndexId(),this.abstractSubtopologyRootNodes,this.abstractSubtopologyLeafNodes,this.allAbstractSubtopologyNodes);
		
		pathListOfRootNodes = getAllPathsFromEachRootToLeafNode(this.abstractSubtopologyRootNodes, this.abstractSubtopologyLeafNodes);
		
		foundAbstractSubTopologies = getAllAbstractSubTopologies(this.pathListOfRootNodes);
		
		return foundAbstractSubTopologies;
	}

	
	protected void getRootAndLeafNodeListByTopologyIndex(long topologyIndexId,List<Node> subtopologyRootNodes,List<Node> subtopologyLeafNodes,List<Node> allSubtopologyNodes){
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
		
/*		String queryLeaf = "MATCH (a)-[r:Includes]->(b{level:\"leaf\"}) WHERE id(a)="+ this.getAbstractSubTopologyIndexId()+" "+"RETURN b";
		Result resultLeaf = db.execute(queryLeaf);
		while ( resultLeaf.hasNext() )
	    {  
	        Map<String,Object> row = resultLeaf.next();

	         for ( String key : resultLeaf.columns() )
	         {            	             
	             Node rootNode=  (Node) row.get( key );
	             this.abstractSubtopologyLeafNodes.add(rootNode);           	                        	    
	         }
	    }*/


		
		}
	}
	/**
	 * This method is used to get all possible abstract sub topologies 
	 * 
	 * @param pathList This input parameters contains list of list of one root node to each leaf node 
	 * @return list of list of one abstract sub topology
	 */
	
	private List<List<Path>> getAllAbstractSubTopologies( List<List<Path>> pathList){		
		Combination<Path> combination = new Combination<Path>(pathList);		
		List<List<Path>> resultBoxes = new ArrayList<List<Path>>(combination.getCombinationsResults());		
		return resultBoxes;
	}
	
    
	/**
	 * This method is used get all paths from each root node to each leaf node
	 * @param rootNodes the node set of root node
	 * @param leafNodes the node set of leaf node
	 * @return the list of list of one root node to each leaf node
	 */
	private List<List<Path>>  getAllPathsFromEachRootToLeafNode(List<Node> rootNodes,List<Node> leafNodes ){
        
        int rootNodeNumber = rootNodes.size();
        int leafNodeNumber = leafNodes.size();
        List<List<Path>> pathList = new ArrayList<List<Path>>();
        
        for(int i=0;i<rootNodeNumber;i++){
        	Node root = rootNodes.get(i);  
        	List<Path> PathListOfOneRootNode= new ArrayList<Path>();
        	for(int j=0;j<leafNodeNumber;j++){
        		Node leaf = leafNodes.get(j);
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
