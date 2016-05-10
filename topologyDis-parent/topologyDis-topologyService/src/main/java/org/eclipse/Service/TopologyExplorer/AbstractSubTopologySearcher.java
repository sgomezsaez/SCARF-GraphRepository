package org.eclipse.Service.TopologyExplorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.eclipse.topology.dao.AbstractNodeDao;
import org.eclipse.topology.dao.AbstractNodeIndexDao;
import org.eclipse.topology.dao.AlphaTopologyDao;
import org.eclipse.topology.dao.ConcreteNodeDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.InstanceNodeDao;
import org.eclipse.topology.dao.RequirementDao;
import org.eclipse.topology.domain.Capability;
import org.eclipse.topology.domain.Requirement;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 *
 * @author Hao
 * This class is used to search abstract sub-topology for a given alpha topology
 */
public class AbstractSubTopologySearcher {
	private long alphaTopologyId;
	private List<Long> abstractSubTopologyIdList;
	protected List<Node> alphaTopologyRootNodes = new ArrayList<Node>();
	protected List<Node> alphaTopologyLeafNodes = new ArrayList<Node>();
	protected List<Node> allAlphaTopologyNodes = new ArrayList<Node>();
	private GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final InstanceNodeDao instanceNodeDao = daoFactory.getInstanceNodeDao();
	private final ConcreteNodeDao concreteNodeDao = daoFactory.getNodeTypeDao();
	private final AbstractNodeDao abstractNodeDao  = daoFactory.getAbstractNodeDao();
	private final AlphaTopologyDao alphaTopologyDao = daoFactory.getAlphaTopologyDao();
	private final RequirementDao requirementDao = daoFactory.getRequirementDao();
	private final AbstractNodeIndexDao abstractNodeIndexDao = daoFactory.getAbstractNodeIndexDao();
	protected  Map<Long,List<Capability>> concreteNodeToItsCapabilitesMapper = new HashMap<Long, List<Capability>>();
	protected  Map<Node,List<Requirement>> alphaTopologyLeafNodesToItsRequirementsMapper = new HashMap<Node, List<Requirement>>();
	protected  Map<Long,List<Capability>> alphaTopologyLeafNodesToItsCapabilitesMapper = new HashMap<Long, List<Capability>>();
	protected  Map<Node,List<Node>> AbstractRootNodeToItsSearchedConcreteNodeMapper = new HashMap<Node, List<Node>>();
	protected  Map<Node,Node> alphaTopologyLeafNodesToAbstractRootNodesMapper = new HashMap<Node, Node>();

	AbstractSubTopologySearcher(long alphaTopologyId){
		this.alphaTopologyId = alphaTopologyId;
		//get all root and leaf nodes for one alpha topology according to the ID
		alphaTopologyDao.getRootAndLeafNodeListByTopologyIndex(this.alphaTopologyId, this.alphaTopologyRootNodes, this.alphaTopologyLeafNodes, this.allAlphaTopologyNodes);
		//Get requirements for each alphaSubTopology leaf node
		for(Node oneAlphaTopologyLeafNode:this.alphaTopologyLeafNodes){
		List<Requirement> requirementsForOneLeafNode = new ArrayList<Requirement>();
		Node concreteNodeOfLeafNode = instanceNodeDao.getConcreteNode(oneAlphaTopologyLeafNode);
		List<Node> requirementNodes = concreteNodeDao.getRequirementNodes(concreteNodeOfLeafNode);
		      for(Node oneRequirement:requirementNodes){
		    	 Requirement newRequirement = requirementDao.getRequirment(oneRequirement);
		    	 requirementsForOneLeafNode.add(newRequirement);
		      }
		this.alphaTopologyLeafNodesToItsRequirementsMapper.put(oneAlphaTopologyLeafNode, requirementsForOneLeafNode);
		}

	}



	public Long getMatchedAbstractSubTopology(){
		Long abstractSubTopologyIdList = null;
		List<Long> allAbstractSubTopologyIndexIdList = getAllAbstractSubTopologyIndexInDatabase();
		for(long abstractSubTopologyIndexId:allAbstractSubTopologyIndexIdList){//to handle one abstractsubTopology
			if(compareCapabilityAndRequirementBetweenTwoTopologies(abstractSubTopologyIndexId))
			{
				abstractSubTopologyIdList = abstractSubTopologyIndexId;
			}


		}


	  return abstractSubTopologyIdList;
	}

	private boolean compareCapabilityAndRequirementBetweenTwoTopologies(long abstractSubTopologyIndexId) {
		// TODO Auto-generated method stub
		List<Node> abstractSubTopologyRootNodes = new ArrayList<Node>();
		List<Node> abstractSubTopologyLeafNodes = new ArrayList<Node>();
		List<Node> allAbstractSubTopologyNodes = new ArrayList<Node>();
		//get root nodes of abstract sub topology
		alphaTopologyDao.getRootAndLeafNodeListByTopologyIndex(abstractSubTopologyIndexId, abstractSubTopologyRootNodes, abstractSubTopologyLeafNodes, allAbstractSubTopologyNodes);
		if(abstractSubTopologyRootNodes.size()!=this.alphaTopologyLeafNodes.size()){
			return false;//the basic structure is not same,for a mactching, at least the number of root and leaf nodes should be same
			}
		else{//if at least the structure is same(number of root nodes equal to number of leaf nodes)
		   //compare node by node
		   for(Node oneAlphaTopologyLeafNode:this.alphaTopologyLeafNodes){//one leaf node
			  for(int i=0;i<abstractSubTopologyRootNodes.size();i++){	// one root noode
				Map<Node,List<Capability>> tempMapper= abstractNodeDao.getAggregatingCapabilityFromOneRootNode(abstractSubTopologyRootNodes.get(i));
				List<Requirement> requirementsOfOneLeafNode = alphaTopologyLeafNodesToItsRequirementsMapper.get(oneAlphaTopologyLeafNode);
				  if(compareRequirementsAndCapabilities(requirementsOfOneLeafNode,tempMapper,abstractSubTopologyRootNodes.get(i))){//tempMapper saved the map of concrete node and its capabilities,
                                                                                               //these concrete nodes come from one root node of abstract sub topology
					  alphaTopologyLeafNodesToAbstractRootNodesMapper.put(oneAlphaTopologyLeafNode, abstractSubTopologyRootNodes.get(i));
					  break;
				  }
				  else{
					  if(i==abstractSubTopologyRootNodes.size()-1){
					      AbstractRootNodeToItsSearchedConcreteNodeMapper.clear();//clear the mapper, because this abstract topology is not fit
					  return false;
					  }
				  }
			   }
			}
		}

		return true;
	}



	private boolean compareRequirementsAndCapabilities(List<Requirement> requirementsOfOneLeafNode,Map<Node, List<Capability>> tempMapper,Node oneAbstractTopologyRootNode) {
		 List<Node> fittedConcreteNodeList = new ArrayList<Node>();
         boolean result = false;
		   for(Requirement oneRequirement:requirementsOfOneLeafNode){//check every requirement of this leaf node
			//iterate the map
			   Iterator<Map.Entry<Node, List<Capability>>>  entries = tempMapper.entrySet().iterator();
			   while (entries.hasNext()) {//for every concrete node
				   Map.Entry<Node, List<Capability>> entry = entries.next();
                     if(ifOneRequirementFitCapabilitiyList(oneRequirement,entry.getValue())){
                    	 fittedConcreteNodeList.add(entry.getKey());
                    	 result = true;
                     }
                     else
                     {
                    	 if(!entries.hasNext()&&result==false){//the last element and not fit found
                    		return false;
                    	 }

                     }
				}
			}



		if(result == true){
       	 AbstractRootNodeToItsSearchedConcreteNodeMapper.put(oneAbstractTopologyRootNode, fittedConcreteNodeList);
		}
		return result;


	}

	private boolean ifOneRequirementFitCapabilitiyList(
			Requirement oneRequirement, List<Capability> value) {
		// TODO Auto-generated method stub
	       for(int i=0;i<value.size();i++){//compare one concrete node's capability and one alpha leaf node requirement
	    	 if(ifRequirementCapabilityFit(oneRequirement, value.get(i))){
	    		break;
	    	 }
	    	 else{
	    		 if(i==value.size()-1){
	    			 return false;//check all capabilities already, no fit
	    		 }
	    	 }
	       }
		return true;

	}



	private boolean ifRequirementCapabilityFit(Requirement requirement, Capability capability){

		if(requirement.getType().getNamespaceURI().equals(capability.getType().getNamespaceURI())&&
		   requirement.getType().getLocalPart().equals(capability.getType().getLocalPart()))
		{
		   int lowerOfReq = requirement.getLowerBound();
		   int lowerOfCa = capability.getLowerBound();
		   String upperOfReq = requirement.getUpperBound();
		   String upperOfCap = capability.getUpperBound();
		      if(upperOfCap.equals("unbounded")){//the capability upper bound is no limit
		    	  if(lowerOfReq>=lowerOfCa){
		    		  return true;
		    	  }
		    	  else{
		    		  return false;
		    	  }
		      }
		      else{//the capability upper bound has limit
		    	  int intUpperOfReq = Integer.parseInt(upperOfReq);
		    	  int intUpperOfCap = Integer.parseInt(upperOfCap);
		    	    if((intUpperOfReq<=intUpperOfCap)&&(lowerOfReq>=lowerOfCa)){
		    	    	return true;
		    	    }
		    	    else{
		    	    	return false;
		    	    }
		      }
		}
		else{
			return false;
		}

	}


	private List<Long> getAllAbstractSubTopologyIndexInDatabase(){

		List<Long>allAbstractTopologyIndex = abstractNodeIndexDao.getAllAbstractTopologyIndex();
		return allAbstractTopologyIndex;
	}

	private List<Node> getOneAbstractSubTopologyRootNodes(long abstactSubTopologyId){
		return null;

	}




	private boolean checkIfTypeMatch(){
		return false;

	}

	private boolean checkIfPropertiesMatch(Requirement requirement, Capability capability){
		return false;

	}











	public long getAlphaTopologyId() {
		return alphaTopologyId;
	}
	public void setAlphaTopologyId(long alphaTopologyId) {
		this.alphaTopologyId = alphaTopologyId;
	}
	public List<Long> getAbstractSubTopologyIdList() {
		return abstractSubTopologyIdList;
	}
	public void setAbstractSubTopologyIdList(List<Long> abstractSubTopologyIdList) {
		this.abstractSubTopologyIdList = abstractSubTopologyIdList;
	}


	//test
	public static void  main(String args[]){

		AbstractSubTopologySearcher seacher = new AbstractSubTopologySearcher(4);
		long result = seacher.getMatchedAbstractSubTopology();
		//System.out.println("The founded Abstract Subtopology ID is "+result);

		for (Entry<Node, List<Node>> entry : seacher.AbstractRootNodeToItsSearchedConcreteNodeMapper.entrySet()) {
		    //System.out.println("AbstractNodeId = " + entry.getKey() + ", ConcreteNodeId = ");
		      for(Node oneNode:entry.getValue()){
		    	  //System.out.println(oneNode.getId());
		      }
		}

		for (Entry<Node, Node> entry : seacher.alphaTopologyLeafNodesToAbstractRootNodesMapper.entrySet()) {
		   // System.out.println("alphaTopologyLeafNodesToAbstractRootNodesMapper = " + entry.getKey() + ", ConcreteNodeId = "+ entry.getValue());

		}


	}



}
