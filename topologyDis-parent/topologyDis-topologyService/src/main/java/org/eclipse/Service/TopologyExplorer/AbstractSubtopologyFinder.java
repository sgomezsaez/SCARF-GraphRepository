package org.eclipse.Service.TopologyExplorer;

/**
 * this class is used for testing only, can be ignored
 */
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.topology.Utility.Combination;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.eclipse.toscaModel.TDefinitions;
import org.eclipse.toscaModel.TEntityTemplate;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TServiceTemplate;
import org.eclipse.toscaModel.TTopologyTemplate;
import org.eclipse.transformer.Topology.AlphaTopologyTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;
import org.eclipse.toscaModel.TExtensibleElements;

public class AbstractSubtopologyFinder {

	static List<Node> AbstractSubtopologyRootNodes = new ArrayList<Node>();
	static List<Node> AbstractSubtopologyLeafNodes = new ArrayList<Node>();
	static List<List<Path>> PathListOfRootNodes = new ArrayList<List<Path>>();
	List<AbstractSubTopologyDomain> abstractSubetopologyResults = null;
	static List<List<Path>> outPutPathListResult = new ArrayList<List<Path>>();

	static private GraphDatabaseService db = DbCon.GetDbConnect();
	private final DaoFactory daoFactory = new DaoFactory();

	public static void main(String [] args) throws NullPointerException, PropertyNotFoundException
	{

      //----------------test to call from abstractSubTopology
        //List<List<Path>> abstractSubTopologiesResultBoxes;
        //AbstractSubTopologyExplorer abstractSubTopologyExplorer = new AbstractSubTopologyExplorer();
        //abstractSubTopologyExplorer.setAbstractSubTopologyIndexId(5);
        //abstractSubTopologyExplorer.setAbstractSubTopologyIndexId(106);
        //abstractSubTopologyExplorer.setAbstractSubTopologyIndexId(137);
        //abstractSubTopologiesResultBoxes = abstractSubTopologyExplorer.getAbstractSubTopologyExploerResult();
		//printPath(abstractSubTopologiesResultBoxes);

        //----------------test to call from GammaTopologyExplorer
        //GammaTopologyExplorer gammaExplorer = new GammaTopologyExplorer();
        //gammaExplorer.initAbstractSubTopologyExplorer(5);
        //AbstractSubTopologyExplorer abstractSubTopologyExplorer = gammaExplorer.getAbstractSubTopologyExplorer();
        //abstractSubTopologiesResultBoxes = abstractSubTopologyExplorer.getFoundAbstractSubTopologies();
		//printPath(abstractSubTopologiesResultBoxes);
		//List<List<TTopologyTemplate>> gammaTopology = gammaExplorer.generateGammaTopologiesForOneAbstractTopology(abstractSubTopologiesResultBoxes.get(0));

        //----------------test to call from MuTopologyExplorer
        MuTopologyExplorer muTopologyExplorer = new MuTopologyExplorer();
        muTopologyExplorer.setAlphaTopologyId(75);
        muTopologyExplorer.initGammaTopologyExplorer();
		//----------------test to delete duplicated nodetemplate and relationtemplate

		//List<TTopologyTemplate> oneGamma = gammaTopology.get(0);
		//TTopologyTemplate first = oneGamma.get(0);
		//List<TEntityTemplate> test = first.getNodeTemplateOrRelationshipTemplate();
		//TTopologyTemplate second = oneGamma.get(1);
		//List<TEntityTemplate> templateNode = second.getNodeTemplateOrRelationshipTemplate();
		//for(int i=0;i<templateNode.size();i++){
		//	test.add(templateNode.get(i));
		//}
		// delete duplicated
		//List<TEntityTemplate> newOne = deleteDuplicateById(test);
		//----------------test to delete duplicated nodetemplate and relationtemplate
       // GenericTest();
       // getSpecification(34);
	}

	private static  List<TEntityTemplate> deleteDuplicateById(List<TEntityTemplate> duplicateList){

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

	private static void getSpecification(long id) {
		// TODO Auto-generated method stub

		try ( Transaction tx = db.beginTx()){
		Node abstractNodeIndex = db.getNodeById(id);

		String specification = (String) abstractNodeIndex.getProperty("specification");


		try {
			InputStream stream = new ByteArrayInputStream (specification.getBytes("UTF-8"));
			TopologyTransformer transformer = new AlphaTopologyTransformer(stream);
			//Iterator<TExtensibleElements> iterator = null;
	    	//iterator = (Iterator<TExtensibleElements>) transformer.Unmarshaller(stream);
	    	//TExtensibleElements next = null;
			Iterator<TDefinitions> iterator = null;
			Unmarshaller um = JAXBSupport.createUnmarshaller();
			Marshaller am = JAXBSupport.createMarshaller(true);
			TDefinitions defi_type;
			try {
				defi_type = (TDefinitions) um.unmarshal(stream);
				System.out.println(defi_type.getId());

				am.marshal(defi_type, System.out);

			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}




		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tx.success();
		}
	}


	private static void printPath(List<List<Path>> resultBoxes) {
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


	private static void GenericTest() {
		// TODO Auto-generated method stub
		List<String> stringBox1 = new ArrayList<String>();
		List<String> stringBox2 = new ArrayList<String>();
		List<String> stringBox3 = new ArrayList<String>();

		stringBox1.add("A1");
		stringBox1.add("B1");
		stringBox1.add("C1");
		stringBox1.add("D1");
		stringBox1.add("E1");

		stringBox2.add("A2");
		stringBox2.add("B2");
	    stringBox2.add("C2");

		stringBox3.add("A3");
		stringBox3.add("B3");
		stringBox3.add("C3");
		stringBox3.add("D3");
		stringBox3.add("E3");

		List<List<String>> boxes = new ArrayList<List<String>>();

		boxes.add(stringBox1);
		boxes.add(stringBox2);
		boxes.add(stringBox3);

		Combination<String> combination = new Combination<String>(boxes);

		List<List<String>> resultBoxes = new ArrayList<List<String>>(combination.getCombinationsResults());
		  int i = 0;
		for(List<String> oneResult:resultBoxes ){

			System.out.println("one of the combination is:");
			for(String oneString:oneResult ){
				System.out.print(oneString+",");
			}
			System.out.println("");
			System.out.println("---------------------------------");
			i++;
		}
		   System.out.println("---------"+"Totaly there are "+i+" "+"combinations");

	}

	public static void generate(List<List<Path>> PathListOfRootNodes, List<Path> outPutPath) {
        List<Path> list = PathListOfRootNodes.get(0);

        for(Path onePath : list) {
            List<List<Path>> newPathListOfRootNodes = new ArrayList<List<Path>>(PathListOfRootNodes);
            newPathListOfRootNodes.remove(list);

            if(PathListOfRootNodes.size() > 1) {
            	outPutPath.add(onePath);
                generate(newPathListOfRootNodes, outPutPath);
             } else {
            	 outPutPath.add(onePath);
            	 List<Path> tempPathList = new ArrayList<Path>(outPutPath);
            	 outPutPathListResult.add(tempPathList);
            	 Path lastPath = list.get(list.size()-1);
            	 if(onePath.equals(lastPath)){
            	 outPutPath.clear();
            	 }
            	 else{
                 outPutPath.remove(onePath);
            	 }
             }
        }
    }

	public AbstractSubtopologyFinder( List<Node> rootNodes, List<Node> leafNodes){

		Iterator<Node> rootIterator = rootNodes.iterator();
		Iterator<Node> leafIterator = leafNodes.iterator();
		Node nextRoot = null;
		Node nextLeaf = null;
		while (rootIterator.hasNext()){
			  nextRoot = rootIterator.next();
			  AbstractSubtopologyRootNodes.add(nextRoot);
		}
		while (leafIterator.hasNext()){
			nextLeaf = leafIterator.next();
			AbstractSubtopologyLeafNodes.add(nextLeaf);
		}

	}

	public static void GetAbstractSubTopologies(){

        int rootNodeNumber = AbstractSubtopologyRootNodes.size();
        int leafNodeNumber = AbstractSubtopologyLeafNodes.size();

        for(int i=0;i<rootNodeNumber;i++){
        	Node root = AbstractSubtopologyRootNodes.get(i);
        	List<Path> PathListOfOneRootNode= new ArrayList<Path>();
        	for(int j=0;j<leafNodeNumber;j++){
        		Node leaf = AbstractSubtopologyLeafNodes.get(j);
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
	         PathListOfRootNodes.add(PathListOfOneRootNode);
    	    }

        }

}
	static public void Init(){
		try ( Transaction tx = db.beginTx();){
		Node rootNode1 = db.getNodeById(0);
		Node rootNode2 = db.getNodeById(5);
		List<Node> rootNodes = new ArrayList<Node>();
		rootNodes.add(rootNode1);
		rootNodes.add(rootNode2);


		Node LeafNode1 = db.getNodeById(1);
		//Node LeafNode2 = db.getNodeById(61);
		//Node LeafNode3 = db.getNodeById(62);
		List<Node> leafNodes = new ArrayList<Node>();
		leafNodes.add(LeafNode1);
		//leafNodes.add(LeafNode2);
		//leafNodes.add(LeafNode3);

		AbstractSubtopologyFinder abstractFinder = new AbstractSubtopologyFinder(rootNodes, leafNodes);
		 tx.success();
		}

	}
}
