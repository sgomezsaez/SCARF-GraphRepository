package org.eclipse.Service.TopologyExplorer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.Utility.Combination;
import org.eclipse.topology.dao.AbstractNodeDao;
import org.eclipse.topology.dao.ConcreteNodeDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.InstanceNodeDao;
import org.eclipse.topology.dao.PreFixDao;
import org.eclipse.topology.dao.RelationshipTypeDao;
import org.eclipse.topology.dao.RelationDao;
import org.eclipse.toscaModel.TEntityTemplate;
import org.eclipse.toscaModel.TNodeTemplate;
import org.eclipse.toscaModel.TRelationshipTemplate;
import org.eclipse.toscaModel.TRelationshipType;
import org.eclipse.toscaModel.TTopologyTemplate;
import org.eclipse.transformer.Topology.InstanceNodeTransformer;
import org.eclipse.transformer.Topology.RelationshipTypeTransformer;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;
import org.apache.commons.collections.IteratorUtils;

public class GammaTopologyExplorer {

	protected AbstractSubTopologyExplorer abstractSubTopologyExplorer = null;
	private List<TTopologyTemplate> gammaTopologies;
	private List<Path> abstractSubTopology;
	protected Map<Node,Node> instanceNodeToAbstractNodeMapper = new HashMap();
	protected Map<String,Node> instanceNodeIdToDatabaseIdMapper = new HashMap();

	static private GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();
	private final AbstractNodeDao abstractNodeDao = daoFactory.getAbstractNodeDao();
	private final ConcreteNodeDao concreteNodeDao = daoFactory.getNodeTypeDao();
	private final InstanceNodeDao instanceNodeDao = daoFactory.getInstanceNodeDao();
	private final RelationDao RelationDao = daoFactory.getRelationDao();
	private final RelationshipTypeDao relationshipTyepDao = daoFactory.getRelationshipTypeDao();
	private final PreFixDao preFixDao = daoFactory.getPreFixDao();
	private Filter filter;









	public void initAbstractSubTopologyExplorer(long abstractSubTopologyIndexId){
	abstractSubTopologyExplorer = new AbstractSubTopologyExplorer();
	abstractSubTopologyExplorer.setAbstractSubTopologyIndexId(abstractSubTopologyIndexId);
	abstractSubTopologyExplorer.getAbstractSubTopologyExploerResult();
	abstractSubTopologyExplorer.mapAbstractAndConreteNodes();
	}




	public AbstractSubTopologyExplorer getAbstractSubTopologyExplorer() {
		return abstractSubTopologyExplorer;
	}




	public List<List<TTopologyTemplate>> generateGammaTopologiesForOneAbstractTopology(List<Path> oneAbstractTopology) throws NullPointerException, PropertyNotFoundException{
		    //the final set of Gamma Topologies
		    List<TTopologyTemplate> finalSetOfGammaTopologies = new ArrayList<TTopologyTemplate>();
		    //get the blank topology template
			TTopologyTemplate topologyTemplate = new TTopologyTemplate();
			List<TEntityTemplate> nodeTemplateOrRelationshipTemplate = topologyTemplate.getNodeTemplateOrRelationshipTemplate();

			List<List<TTopologyTemplate>>gammaTopologiesForAllPaths = new ArrayList<List<TTopologyTemplate>>();
			//there could be several paths in one topology, manage it one by one
			   for(Path onePath: oneAbstractTopology ){
				   // get the boxes for combination
				   List<List<Node>> concreteNodesBoxesForOnePath = buildConcreteNodeBoxForOnePath(onePath);
				   // get the concrete nodes combination for one path
				   Combination<Node> combination = new Combination<Node>(concreteNodesBoxesForOnePath);
				   List<List<Node>> resultBoxes = new ArrayList<List<Node>>(combination.getCombinationsResults());
				   // get possible gamma topologies for one paths
				   List<TTopologyTemplate> gammaTopologiesForOnePath = generateGammaTopologiesForOnePath(resultBoxes, onePath);
				   gammaTopologiesForAllPaths.add(gammaTopologiesForOnePath);
			   }

			   Combination<TTopologyTemplate> combination = new Combination<TTopologyTemplate>(gammaTopologiesForAllPaths);
			   List<List<TTopologyTemplate>> resultBoxes = new ArrayList<List<TTopologyTemplate>>(combination.getCombinationsResults());
	         //  for(List<TTopologyTemplate> oneResult:resultBoxes ){

	        //	   TTopologyTemplate topologyTemplateResult = combineTopologies(oneResult);
	        //	   finalSetOfGammaTopologies.add(topologyTemplate);
	         //  }


		//return finalSetOfGammaTopologies;
			   return resultBoxes;


	}

	private TTopologyTemplate combineTopologies(List<TTopologyTemplate> oneResult) {
		// TODO Auto-generated method stub
		return null;
	}


	private List<TTopologyTemplate> generateGammaTopologiesForOnePath(List<List<Node>> resultBoxes,Path onePath) throws NullPointerException, PropertyNotFoundException{
		List<TTopologyTemplate> gammaTopologies = new ArrayList<>();


		   for(List<Node> oneConcreteNodesBox: resultBoxes){


                    //get all instancenodes combination for one combination of concrete nodes
			    	List<List<Node>> instanceNodesBox;
			    	instanceNodesBox = buildInstanceNodeBoxForOneConcreteNode(oneConcreteNodesBox);
			    	Combination<Node> combination = new Combination<Node>(instanceNodesBox);
					List<List<Node>> instanceNodeCombinations = new ArrayList<List<Node>>(combination.getCombinationsResults());

					for(List<Node> oneInstanceNodeCombination:instanceNodeCombinations){
				    	TTopologyTemplate topologyTemplate = new TTopologyTemplate();
				    	List<TEntityTemplate> nodeTemplateOrRelationshipTemplate = topologyTemplate.getNodeTemplateOrRelationshipTemplate();
						for(int j=0;j<oneInstanceNodeCombination.size()-1;j++){
			    	      //get the start node and end node of one relationship
			    	      Node startNode =  oneInstanceNodeCombination.get(j);
			    	      Node endNode = oneInstanceNodeCombination.get(j+1);
			    	      //get all relationships in order from one path, convert it into a list
			    	      Iterator<Relationship> relationIterator = onePath.relationships().iterator();
			    	      List<Relationship> relationshipListOfOnePath = IteratorUtils.toList(relationIterator);
			    	      //build node template for start node
			    	      TNodeTemplate startNodeTemplate = buildNodeTemplate(startNode);
			    	      //build node template for last node
			    	      TNodeTemplate endNodeTemplate = buildNodeTemplate(endNode);
			    	      //build relationship template for relationship
			    	      try(Transaction tx= db.beginTx();){
					      String typeOfRelationship = (String) relationshipListOfOnePath.get(j).getProperty("type");
			    	      TRelationshipTemplate relatioinshipTemplate = buildRelationshipTemplate(startNodeTemplate, endNodeTemplate,typeOfRelationship);
			    	      //add the startnode template and realtiionship template
			    	      nodeTemplateOrRelationshipTemplate.add(startNodeTemplate);
			    	      nodeTemplateOrRelationshipTemplate.add(relatioinshipTemplate);
			    	      }
			    	      //if the end node is the last node of one path
			    	     if(j == oneInstanceNodeCombination.size()-2 ){
			    	      //add the last node template to topology
			             nodeTemplateOrRelationshipTemplate.add(endNodeTemplate);
			    	     }
			          }
			          gammaTopologies.add(topologyTemplate);
		           }
			    }


		return gammaTopologies;


	}

	protected TRelationshipTemplate buildRelationshipTemplate( TNodeTemplate startNodeTemplate, TNodeTemplate endNodeTemplate,String typeOfRelationship) throws NullPointerException, PropertyNotFoundException {
		TRelationshipType relationshipType = new TRelationshipType();
		TRelationshipTemplate relationshipTemplate = new TRelationshipTemplate();
		try ( Transaction tx = db.beginTx();){
		String abstractRelationshipType = typeOfRelationship;
		Node relationTypeNode = RelationDao.findRelation(abstractRelationshipType);

		String specification = relationshipTyepDao.getSpecification(relationTypeNode);

			InputStream stream;
			stream = new ByteArrayInputStream (specification.getBytes("UTF-8"));
			RelationshipTypeTransformer relationshipTypeTransformer = new RelationshipTypeTransformer( stream);
			relationshipType = relationshipTypeTransformer.getRelationshipTypeOfTosca();


		//source and target element
		TRelationshipTemplate.SourceElement sourceElement = new TRelationshipTemplate.SourceElement();
		TRelationshipTemplate.TargetElement targetElemmet = new TRelationshipTemplate.TargetElement();
		sourceElement.setRef(startNodeTemplate.getId());
		targetElemmet.setRef(endNodeTemplate.getId());
		relationshipTemplate.setSourceElement(sourceElement);
		relationshipTemplate.setTargetElement(targetElemmet);

		//name
		String relationshipName = sourceElement.getRef()+"_"+abstractRelationshipType+"_"+targetElemmet.getRef();
		String result = relationshipName.replaceAll("[-+.^:,]","");

		relationshipTemplate.setName(result);

		//id
		relationshipTemplate.setId(result);
		//type
		String targetNamespace = relationshipType.getTargetNamespace();
		String prefix = preFixDao.getPrefixByNameSpaceUrl(targetNamespace);
		String name = relationshipType.getName();
		QName qName = new QName(targetNamespace, name, prefix);


		relationshipTemplate.setType(qName);
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		  catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}

		return relationshipTemplate;
	}

	private TNodeTemplate buildNodeTemplate(Node instanceNode) {
		// TODO Auto-generated method stub
		TNodeTemplate nodeTemplate = new TNodeTemplate();

		InstanceNodeTransformer instanceNodeTransformer;
		String specification = instanceNodeDao.getSpecification(instanceNode);
		try {
			InputStream stream;
			stream = new ByteArrayInputStream (specification.getBytes("UTF-8"));
			instanceNodeTransformer = new InstanceNodeTransformer( stream,"null");
			nodeTemplate = instanceNodeTransformer.getNodeTemplate();
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		  catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}

		return nodeTemplate;
	}

/*	private TNodeTemplate buildNodeTemplate(Node oneConcreteNode) {
		// TODO Auto-generated method stub
		TNodeTemplate nodeTemplate = new TNodeTemplate();
		List<Node> instanceNodes = concreteNodeDao.getInstanceNodes(oneConcreteNode);
		//build the mapper between instanceNodes ID and Database Node Id
		try(Transaction tx = db.beginTx();){
		for(Node instanceNode:instanceNodes){
			instanceNodeIdToDatabaseIdMapper.put((String)instanceNode.getProperty("id"), instanceNode);
		}
		}
		//Here to build the mapper for instance node to abstract node
		Node abstractNode= concreteNodeDao.getAbstractNode(oneConcreteNode);
		for(int i=0;i<instanceNodes.size();i++){
		instanceNodeToAbstractNodeMapper.put(instanceNodes.get(i), abstractNode);
		}

		//it is assumed that for each concrete node only one node template is existed at moment
		Node instanceNode = instanceNodes.get(0);
		InstanceNodeTransformer instanceNodeTransformer;
		String specification = instanceNodeDao.getSpecification(instanceNode);
		try {
			InputStream stream;
			stream = new ByteArrayInputStream (specification.getBytes("UTF-8"));
			instanceNodeTransformer = new InstanceNodeTransformer( stream,"null");
			nodeTemplate = instanceNodeTransformer.getNodeTemplate();
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		  catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}

		return nodeTemplate;
	}*/


	/**
	 * this method first for  each abstract node in the path, get all its concrete nodes, put it in one box
	 * @param onePath one path in one abstract topology
	 * @return  return all boxes
	 */
    private List<List<Node>> buildConcreteNodeBoxForOnePath(Path onePath){

    	List<List<Node>> concreteNodeBoxes = new ArrayList<List<Node>>();
    	Iterator<Node> nodeIterator = onePath.nodes().iterator();
    	Node nextNode;
    	 while(nodeIterator.hasNext()){
    		 nextNode = nodeIterator.next();//next node here is an abstract node
    		 List<Node> tempOneConcreteNodesBox = new ArrayList<Node>();
    		 //oneConcreteNodesBox = abstractNodeDao.getConcreteNodes(nextNode);
    		 tempOneConcreteNodesBox = abstractSubTopologyExplorer.ConcreteNodeContainer.get(nextNode);
    		 List<Node> oneConcreteNodesBox = new ArrayList<Node>();
    		 //here can add filtering function for founded concreteNode
    		 for(Node oneNode: tempOneConcreteNodesBox){
    		  if(!this.filter.ifConcreteNodeFiltered(nextNode,oneNode)){
    			  oneConcreteNodesBox.add(oneNode);
    		  }

    		 }
    		 concreteNodeBoxes.add(oneConcreteNodesBox);
    	 }
    	return concreteNodeBoxes;


    }


	/**
	 * this method first for  each concrete node in the path, get all its instance nodes, put it in one box
	 * @param concreteNode one concrete Node
	 * @return  return all boxes
	 */
    private List<List<Node>> buildInstanceNodeBoxForOneConcreteNode(List<Node> oneConcreteNodeBox){

    	List<List<Node>> instanceNodeBoxes = new ArrayList<List<Node>>();
    	Iterator<Node> nodeIterator = oneConcreteNodeBox.iterator();
    	Node nextNode;
    	 while(nodeIterator.hasNext()){
    		 nextNode = nodeIterator.next();
    		 List<Node> oneInstanceNodesBox;
    		 //get all instance nodes for one concrete node
    		 oneInstanceNodesBox = concreteNodeDao.getInstanceNodes(nextNode);

    			//build the mapper between instanceNodes ID and Database Node Id
    			try(Transaction tx = db.beginTx();){
    			for(Node instanceNode:oneInstanceNodesBox){
    				instanceNodeIdToDatabaseIdMapper.put((String)instanceNode.getProperty("id"), instanceNode);
    			}
    			}
    			//Here to build the mapper for instance node to abstract node
    			Node abstractNode= concreteNodeDao.getAbstractNode(nextNode);
    			for(int i=0;i<oneInstanceNodesBox.size();i++){
    			instanceNodeToAbstractNodeMapper.put(oneInstanceNodesBox.get(i), abstractNode);
    			}

    		 instanceNodeBoxes.add(oneInstanceNodesBox);
    	 }

    	return instanceNodeBoxes;


    }





	public Filter getFilter() {
		return filter;
	}




	public void setFilter(Filter filter) {
		this.filter = filter;
	}














}
