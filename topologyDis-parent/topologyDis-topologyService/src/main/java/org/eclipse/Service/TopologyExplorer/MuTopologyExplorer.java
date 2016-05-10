package org.eclipse.Service.TopologyExplorer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.topology.dao.DbCon;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TDefinitions;
import org.eclipse.toscaModel.TEntityTemplate;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TNodeTemplate;
import org.eclipse.toscaModel.TRelationshipTemplate;
import org.eclipse.toscaModel.TServiceTemplate;
import org.eclipse.toscaModel.TTopologyTemplate;
import org.eclipse.transformer.Topology.AlphaTopologyTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class MuTopologyExplorer {

	protected GammaTopologyExplorer gammaExplorer;
	protected AbstractSubTopologySearcher abstractSubTopologySearcher;
	private List<List<Path>> abstractSubTopologiesResultBoxes;

	private TTopologyTemplate muTopologyTemplate;
	private List<List<TTopologyTemplate>> gammaTopology;
	private Map<Node,Node> alphaTopologyLeafNodesToAbstractRootNodesMapper = new HashMap();
	private long alphaTopologyId;
	private long abstractSubTopologyId;
	private List<Node> alphaTopologyRootNodes = new ArrayList<Node>();
	private List<Node> alphaTopologyLeafNodes = new ArrayList<Node>();
	private List<Node> allAlphaTopologyNodes = new ArrayList<Node>();
	private List<Definitions> MuTopologyDefinition = new ArrayList<>();
    private Filter muFilter;
	static private GraphDatabaseService db = DbCon.GetDbConnect();




	public List<Definitions> getMuTopologyDefinition() {
		return MuTopologyDefinition;
	}


	public void setAlphaTopologyId(long alphaTopologyId) {
		this.alphaTopologyId = alphaTopologyId;
	}


	public void setAbstractSubTopologyId(long abstractSubTopologyId) {
		this.abstractSubTopologyId = abstractSubTopologyId;
	}

	/**
	 * This method return the abstract abstractSubTopologyId for a given AlphaTopology, then
	 * build the map:  leaf node of alpha topology<===> root node of abstractSubTopologyId
	 * in HashMap AlphaTopologyLeafToAbstractRootNodesMapper
	 * @param alphaTopologyId the input is existed abstractSubTopologyId in database
	 */
	public void findAbstractSubTopologyForOneAlphaTopology(){

	//here to find abstract subtopology logic
	this.abstractSubTopologySearcher = new AbstractSubTopologySearcher(this.alphaTopologyId);
	this.abstractSubTopologyId  = abstractSubTopologySearcher.getMatchedAbstractSubTopology();


    /*//alphaTopologyLeafNodesToAbstractRootNodesMapper.put(key, value)
    try(Transaction tx = db.beginTx();){
    //Node alphaNode1 = db.getNodeById(49);
    //Node abstractNode1 = db.getNodeById(0);
    Node alphaNode1 = db.getNodeById(140);
    Node abstractNode1 = db.getNodeById(4);

    //Node alphaNode2 = db.getNodeById(48);
    //Node abstractNode2= db.getNodeById(5);
    Node alphaNode2 = db.getNodeById(141);
    Node abstractNode2= db.getNodeById(0);

    alphaTopologyLeafNodesToAbstractRootNodesMapper.put(alphaNode1, abstractNode1);
    alphaTopologyLeafNodesToAbstractRootNodesMapper.put(alphaNode2, abstractNode2);

    return abstractSubTopologyId;
	}*/
	}

	private void buildMuTopology(List<TTopologyTemplate> topologyTemplates) throws NullPointerException, PropertyNotFoundException{
        TTopologyTemplate alphaTopologyTemplate = new TTopologyTemplate();
    	Definitions alphaTopologyDefinition = new Definitions();//change here , important
    	alphaTopologyDefinition = buildDeifinitionByAlphaTopology(this.alphaTopologyId);
    	TExtensibleElements next = null;
    	Iterator<TExtensibleElements> iterator = alphaTopologyDefinition.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().iterator();
		while (iterator.hasNext()) {
			next = iterator.next();
			if (next instanceof TServiceTemplate) {
				alphaTopologyTemplate = ((TServiceTemplate) next).getTopologyTemplate();
			}
		}
	  //find the match between instance nodes of Gamma Topology and Alpha Topologies
 		gammaExplorer.abstractSubTopologyExplorer.getRootAndLeafNodeListByTopologyIndex(this.alphaTopologyId,this.alphaTopologyRootNodes,this.alphaTopologyLeafNodes,this.allAlphaTopologyNodes);
            //connect alpha topology with gamma topology by creating relationship template
        	for(TTopologyTemplate oneTopologyTemaplate:topologyTemplates){

        	//the first node in the list is the instance node comes from the root node of abstract sub topology
        		String instanceNodeId = oneTopologyTemaplate.getNodeTemplateOrRelationshipTemplate().get(0).getId();
        		TNodeTemplate endNodeTemplate = (TNodeTemplate) oneTopologyTemaplate.getNodeTemplateOrRelationshipTemplate().get(0);
        		TNodeTemplate startNodeTemplate = null;
        		Node instanceNode = gammaExplorer.instanceNodeIdToDatabaseIdMapper.get(instanceNodeId);
        		Node abstractNode = gammaExplorer.instanceNodeToAbstractNodeMapper.get(instanceNode);
        		Node leafNodeOfAlphaTopologyToBeConnected = null;
        		try(Transaction tx = db.beginTx();){
        	        for(Node oneLeafNode:this.alphaTopologyLeafNodes){
        	        	Node matchedAbstractRootNode;
        	        	matchedAbstractRootNode = alphaTopologyLeafNodesToAbstractRootNodesMapper.get(oneLeafNode);
        	        	if(matchedAbstractRootNode.equals(abstractNode)){

        	        		leafNodeOfAlphaTopologyToBeConnected = oneLeafNode;
        	        		break;
        	        	}
        	        }
        	    //begin build the relationship template between leafNodeOfAlphaTopologyToBeConnected and instanceNode
        	        //System.out.print("begin connecting leafNodeOfAlphaTopologyToBeConnected to instancenode!");
        	        String typeOfRelationship = "tbt:HostedOn";//here can be a function to decide in the future which type of relationship should be built


        	        List<TEntityTemplate> nodeTemplateOrRelationshipTemplate = alphaTopologyTemplate.getNodeTemplateOrRelationshipTemplate();
        	        for(TEntityTemplate oneTemplate:nodeTemplateOrRelationshipTemplate){

        	        	if(oneTemplate.getId().equals(leafNodeOfAlphaTopologyToBeConnected.getProperty("id"))){
        	        		startNodeTemplate = (TNodeTemplate) oneTemplate;
        	        		break;
        	        	}

        	        }
        	       TRelationshipTemplate relatioinshipTemplate = gammaExplorer.buildRelationshipTemplate(startNodeTemplate,endNodeTemplate,typeOfRelationship);
        	       alphaTopologyTemplate.getNodeTemplateOrRelationshipTemplate().add(relatioinshipTemplate);

        	}


    		}
          	//add existing node and relationship template of gamma topology
       	for(TTopologyTemplate topologyTemaplateExisting:topologyTemplates){
       		List<TEntityTemplate> entityTemplate = topologyTemaplateExisting.getNodeTemplateOrRelationshipTemplate();
       		for(int i=0;i<entityTemplate.size();i++){
       			 alphaTopologyTemplate.getNodeTemplateOrRelationshipTemplate().add(entityTemplate.get(i));
       		}

       	}

          	//delete duplicated node template or relationship template by ID
       	 List<TEntityTemplate> entityTemplate = deleteDuplicateById( alphaTopologyTemplate.getNodeTemplateOrRelationshipTemplate());
       	 alphaTopologyTemplate.getNodeTemplateOrRelationshipTemplate().clear();
       	 for(int i=0;i<entityTemplate.size();i++){
       		 alphaTopologyTemplate.getNodeTemplateOrRelationshipTemplate().add(entityTemplate.get(i));
       	 }

    		MuTopologyDefinition.add(alphaTopologyDefinition);


	}

  private   List<TEntityTemplate> deleteDuplicateById(List<TEntityTemplate> duplicateList){

		List<TEntityTemplate> noDuplicateList = new  ArrayList<TEntityTemplate>() ;
		noDuplicateList.add(duplicateList.get(0));
		boolean contains = false;
		for(int i=0;i<duplicateList.size();i++){

			for(int j=0;j<noDuplicateList.size();j++){

				if(noDuplicateList.get(j).getId().equals(duplicateList.get(i).getId())){
				contains = true;
				}
			}
		    if(!contains){
		    	noDuplicateList.add(duplicateList.get(i));
		    }
	    	contains = false;
		}
		return noDuplicateList;

	}

	private  Definitions buildDeifinitionByAlphaTopology(long alphaTopologyId) {
		// TODO Auto-generated method stub
		Definitions alphaTopologyTemplate = new Definitions();
		try ( Transaction tx = db.beginTx()){
		Node alphaTopologyNodeIndex = db.getNodeById(alphaTopologyId);

		String specification = (String) alphaTopologyNodeIndex.getProperty("specification");

		try {
			InputStream stream = new ByteArrayInputStream (specification.getBytes("UTF-16"));//the encoding only support utf-16 in Java DOM when transfering
			AlphaTopologyTransformer transformer = new AlphaTopologyTransformer(stream);
			alphaTopologyTemplate = transformer.getToscaDefinition();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tx.success();
		}
		return alphaTopologyTemplate;

	}

	private Node findAbstractNodeFromInstanceNodeToAbstractNodeMapper(Node instanceNode){

		Node abstractNode = gammaExplorer.instanceNodeToAbstractNodeMapper.get(instanceNode);
		 return abstractNode;
	}

	public void initGammaTopologyExplorer() throws NullPointerException, PropertyNotFoundException{
	findAbstractSubTopologyForOneAlphaTopology();
	this.muFilter = createFilterForConcreteNode(this.abstractSubTopologySearcher.AbstractRootNodeToItsSearchedConcreteNodeMapper);
	this.gammaExplorer = new GammaTopologyExplorer();
    this.gammaExplorer.initAbstractSubTopologyExplorer(this.abstractSubTopologyId);
    //set gamma filter
    this.gammaExplorer.setFilter(this.muFilter);
    //find root node and leaf node of alphaTopology
    gammaExplorer.abstractSubTopologyExplorer.getRootAndLeafNodeListByTopologyIndex(this.alphaTopologyId,this.alphaTopologyRootNodes,this.alphaTopologyLeafNodes,this.allAlphaTopologyNodes);
    this.alphaTopologyLeafNodesToAbstractRootNodesMapper = this.abstractSubTopologySearcher.alphaTopologyLeafNodesToAbstractRootNodesMapper;
    AbstractSubTopologyExplorer abstractSubTopologyExplorer = gammaExplorer.getAbstractSubTopologyExplorer();

    this.abstractSubTopologiesResultBoxes = abstractSubTopologyExplorer.getFoundAbstractSubTopologies();
    printPath(abstractSubTopologiesResultBoxes);
    for(int j=0;j<abstractSubTopologiesResultBoxes.size();j++){
    this.gammaTopology = this.gammaExplorer.generateGammaTopologiesForOneAbstractTopology(abstractSubTopologiesResultBoxes.get(j));
    for(int i=0;i<gammaTopology.size();i++){
    buildMuTopology(gammaTopology.get(i));
    }
    }
    /*Marshaller am = JAXBSupport.createMarshaller(true);
    for(int j=0;j<MuTopologyDefinition.size();j++){
    try {

		am.marshal(MuTopologyDefinition.get(j), System.out);

	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
    }
    }*/
     }






	private Filter createFilterForConcreteNode(
			Map<Node, List<Node>> abstractRootNodeToItsSearchedConcreteNodeMapper) {
		// TODO Auto-generated method stub
		Filter filter = new Filter();
/*		List<Node> concretNodeFilter = new ArrayList<Node>();
		for (Entry<Node, List<Node>> entry : abstractRootNodeToItsSearchedConcreteNodeMapper.entrySet()) {
		    System.out.println("AbstractNodeId = " + entry.getKey() + ", ConcreteNodeId = ");
		      for(Node oneNode:entry.getValue()){
		    	  System.out.println(oneNode.getId());
		    	  concretNodeFilter.add(oneNode);
		      }
		}
		filter.setConcreteNodeFilter(concretNodeFilter);*/
		filter.setAbstractRootNodeToItsSearchedConcreteNodeMapper(abstractRootNodeToItsSearchedConcreteNodeMapper);
		return filter;

	}


	private  void printPath(List<List<Path>> resultBoxes) {
		// TODO Auto-generated method stub
		int i = 0;
		for(List<Path> pathResult:resultBoxes){

			for(Path onePath:pathResult){

				Iterator<Node> nodesLiter = onePath.nodes().iterator();

				Node nextNode = null;
				while(nodesLiter.hasNext()){
					nextNode = nodesLiter.next();
				   System.out.print("Node"+""+nextNode.getId()+"====>");
				}

				 System.out.println("");
			}

			 System.out.println("");
			 System.out.println("------------------");
			 i++;
		}
		System.out.println("---------"+"Totaly there are "+i+" "+"combinations");
	}

}
