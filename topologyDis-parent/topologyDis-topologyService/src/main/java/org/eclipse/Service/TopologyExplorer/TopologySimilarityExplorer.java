package org.eclipse.Service.TopologyExplorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.topology.dao.AlphaTopologyDao;
import org.eclipse.topology.dao.AlphaTopologyIndexDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.RelationshipTypeDao;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;

public class TopologySimilarityExplorer {
	
	long topologyIndexId; // the topology giving to find similar Topologies of it
	List<Long> FoundedSimilarTopology = new ArrayList<Long>(); ; // the Founded similar topology ID list
	static private GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final AlphaTopologyIndexDao alphaTopologyIndexDao = daoFactory.getAlphaTopologyIndexDao();
	private final AlphaTopologyDao alphaTopologyDao = daoFactory.getAlphaTopologyDao();
	private List<Long> allAlphaTopologyIndex;
	private List<Node> topologyRootNodes = new ArrayList<Node>();
	private List<Node> topologyLeafNodes = new ArrayList<Node>();
	protected List<Node> allTopologyNodes = new ArrayList<Node>();
	private List<List<Path>> pathListOfRootNodes ;
	
	
	public TopologySimilarityExplorer( long id){
		this.topologyIndexId = id;
		alphaTopologyDao.getRootAndLeafNodeListByTopologyIndex(this.topologyIndexId,this.topologyRootNodes, this.topologyLeafNodes, this.allTopologyNodes);
		pathListOfRootNodes = alphaTopologyDao.getAllPathsFromEachStartToEndNode(this.topologyRootNodes, this.topologyLeafNodes);
	}

	
	public void startExploringSimilarTopology(){
		allAlphaTopologyIndex = alphaTopologyIndexDao.getAllAlphaTopologyIndex();
		allAlphaTopologyIndex.remove(this.topologyIndexId);//remove itself

      for(long oneAlphaTopologyIndex:allAlphaTopologyIndex ){		
  		  List<Node> tempTopologyRootNodes = new ArrayList<Node>();
  		  List<Node> tempTopologyLeafNodes = new ArrayList<Node>();
  		  List<Node> tempAllTopologyNodes = new ArrayList<Node>();
  		  List<List<Path>> tempPathListOfRootNodes;
  		alphaTopologyDao.getRootAndLeafNodeListByTopologyIndex(oneAlphaTopologyIndex,tempTopologyRootNodes, tempTopologyLeafNodes, tempAllTopologyNodes);
  		tempPathListOfRootNodes = alphaTopologyDao.getAllPathsFromEachStartToEndNode(tempTopologyRootNodes, tempTopologyLeafNodes);

		    if(IfStructureSimilar(tempTopologyRootNodes,tempTopologyLeafNodes,tempAllTopologyNodes,tempPathListOfRootNodes)){
			  FoundedSimilarTopology.add(oneAlphaTopologyIndex);//found one similar topology by comparing structure
		    }
      }
	
	}
	
	private boolean IfStructureSimilar(List<Node> tempTopologyRootNodes,
			                           List<Node> tempTopologyLeafNodes, 
			                           List<Node> tempAllTopologyNodes,
			                           List<List<Path>> tempPathListOfRootNodes) {
		// TODO Auto-generated method stub
		//compare basic characteristic
		if(!tempTopologyRootNodes.isEmpty()&&!tempTopologyLeafNodes.isEmpty()&&!tempAllTopologyNodes.isEmpty()
		 &&(tempTopologyRootNodes.size()==this.topologyRootNodes.size())
		 &&(tempTopologyLeafNodes.size()==this.topologyLeafNodes.size())
		 &&(tempAllTopologyNodes.size()==this.allTopologyNodes.size())
		 &&(tempPathListOfRootNodes.size()==this.pathListOfRootNodes.size())){
			 for(List<Path> oneRootNodePaths: this.pathListOfRootNodes){
				String nodeType = getNodeStringProperty(oneRootNodePaths.get(0).startNode(),"type"); //get node type
				for(int i=0;i<tempPathListOfRootNodes.size();i++){
					String tempNodeType = getNodeStringProperty(tempPathListOfRootNodes.get(i).get(0).startNode(),"type"); //get  node type to compare
					if(nodeType.equals(tempNodeType)){
						if(compareTwoPathListOfOneRootNode(oneRootNodePaths,tempPathListOfRootNodes.get(i))){//to compare the path list of one root node
							tempPathListOfRootNodes.remove(i);
							break;
						}
						else{
							return false;
						}							
					}
					else{//does find the same node type
						if(i==(tempPathListOfRootNodes.size())){
							return false;
						}							
					}
				}
			 }
			
		 }
		else{
			return false;
		}
		
		return true;
	}


   
	private boolean compareTwoPathListOfOneRootNode(List<Path> oneRootNodePaths,
			List<Path> oneTempRootNodePaths) {
		for(Path oneRootNodePath: oneRootNodePaths){
			for(int i=0;i<oneTempRootNodePaths.size();i++){
				if(compareTwoPath(oneRootNodePath,oneTempRootNodePaths.get(i))){
					oneTempRootNodePaths.remove(oneTempRootNodePaths.get(i));
					break;
				}
				else{//finish comparing all path,still not found a same one
					if(i==(oneTempRootNodePaths.size()-1)){
						return false;
					}
				}
			}
			
		}
		return true;
	}


	private boolean compareTwoPath(Path oneRootNodePath,
			Path oneTempRootNodePath) {
		 try(Transaction tx=db.beginTx();){
		System.out.println("begin to compare two paths:");
		System.out.println(oneRootNodePath.toString());
		System.out.println(oneTempRootNodePath.toString());
		 }
		if(oneRootNodePath.length()==oneTempRootNodePath.length()){
			Iterator<PropertyContainer>oneRootNodePathIterator = oneRootNodePath.iterator();
			Iterator<PropertyContainer>oneTempRootNodePathIterator = oneTempRootNodePath.iterator();
			while(oneRootNodePathIterator.hasNext()&&oneTempRootNodePathIterator.hasNext()){
			 try(Transaction tx=db.beginTx();){
				if( oneRootNodePathIterator.next().getProperty("type").equals(oneTempRootNodePathIterator.next().getProperty("type"))){
					
				}
				else{
				return false;
				}
			 }
				
			}
		}
		else{
			return false;
		}
		
		
		return true;
	}


	private String getNodeStringProperty(Node node, String propertyName){
		String property = null;
		try(Transaction tx=db.beginTx();){			
			property=(String) node.getProperty(propertyName);
		}	
		return property;
	}
	
	

	public List<Long> getFoundedSimilarTopology() {
		return FoundedSimilarTopology;
	}
	
	   public static void main(String [] args)
	  {
		   TopologySimilarityExplorer exploer = new TopologySimilarityExplorer(3);
		   exploer.startExploringSimilarTopology();
		   List<Long> result = exploer.getFoundedSimilarTopology();
		   System.out.println("Similer Topology in database is:");
		   for(long one:result){
			   System.out.print(one+",");
		   }
	  }


}
