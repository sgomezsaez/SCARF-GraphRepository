package org.eclipse.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.inputModel.AbstractSubTopology;
import org.eclipse.output.TopologyOutput.AbstractSubTopologyList;
import org.eclipse.output.TopologyOutput.AbstractSubTopologyList.AbstractSubTopologyWithDatabaseID;
import org.eclipse.topology.dao.AbstractNodeDao;
import org.eclipse.topology.dao.AbstractNodeIndexDao;
import org.eclipse.topology.dao.RelationDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.eclipse.topology.domain.Performance;
import org.eclipse.topology.domain.AbstractSubTopologyDomain.AbstractNode;
import org.eclipse.topology.domain.AbstractSubTopologyDomain.RelationshipOfAbstractNode;
import org.eclipse.transformer.Topology.AbstractSkeletonTransformer;
import org.eclipse.transformer.TopologyStatistics.PerformanceTransformer;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class AbstractSubTopologyService {

	private List<Node> AbstractNodeListDb = new  ArrayList<Node>();
	//Get DaoFactory
	private final DaoFactory daoFactory = new DaoFactory();
	private final AbstractNodeDao abstractNodeDao = daoFactory.getAbstractNodeDao();
	private final RelationDao abstractRelationDao = daoFactory.getRelationDao();
	private final AbstractNodeIndexDao abstractNodeIndexDao = daoFactory.getAbstractNodeIndexDao();

	public enum abstractSkeletonRelationships implements RelationshipType{
		Includes
	}
	GraphDatabaseService db = DbCon.GetDbConnect();

	public Node AddAbstractSkeleton(AbstractSubTopologyDomain abstractSkeletonDomain){
		addAbstractNode(abstractSkeletonDomain);
		addAbstractRelation(abstractSkeletonDomain);
		Node abstractSkeletonIndex = addAbstractSkeletonIndex(abstractSkeletonDomain);


		return abstractSkeletonIndex;
	}

	private Node addAbstractSkeletonIndex(AbstractSubTopologyDomain abstractSkeletonDomain) {
		// TODO Auto-generated method stub
		Node abstractNodeIndexDb = abstractNodeIndexDao.createIndex(abstractSkeletonDomain);
		//Connect the index to all abstractskeleton Nodes
		Iterator<Node>it = AbstractNodeListDb.iterator();
		Node nextNode = null;
		while(it.hasNext()){
			nextNode = it.next();
				abstractRelationDao.addRelation(abstractNodeIndexDb, nextNode,abstractSkeletonRelationships.Includes.toString(),abstractSkeletonRelationships.Includes.toString());
		}
		return abstractNodeIndexDb;
	}

	private void addAbstractRelation(AbstractSubTopologyDomain abstractskeletonDomain) {
		Iterator <RelationshipOfAbstractNode> iteratorAbstractRelation = abstractskeletonDomain.getRelationshipOfAbstractNode().iterator();
		RelationshipOfAbstractNode nextRelation = null;
	     while (iteratorAbstractRelation.hasNext()) {
	    	 nextRelation = iteratorAbstractRelation.next();
             Node sourceNode = findNode(nextRelation.getSourceElement().getRef());
             Node targetNode = findNode(nextRelation.getTargetElement().getRef());
             String type = nextRelation.getType();
             String name = nextRelation.getType();
	    	 abstractRelationDao.addRelation(sourceNode, targetNode, type,name);
	     }

	}

	private Node findNode(String nodeId){

		Iterator<Node>it = AbstractNodeListDb.iterator();
		Node nextNode = null;
		while(it.hasNext()){
			nextNode = it.next();
			try ( Transaction tx = db.beginTx()){

			if( nextNode.getProperty("id").equals(nodeId) ){
				return nextNode;
			}
			tx.success();
			}
		}

		return nextNode;

	}

	private void addAbstractNode(AbstractSubTopologyDomain abstractSkeletonDomain){


		Iterator <AbstractNode> iteratorAbstractNode = abstractSkeletonDomain.getAbstractNode().iterator();
		AbstractNode nextAbstractNode = null;
	     while (iteratorAbstractNode.hasNext()) {
	    	 nextAbstractNode = iteratorAbstractNode.next();
             Node tempNodeDb = abstractNodeDao.create(nextAbstractNode);
             AbstractNodeListDb.add(tempNodeDb);
	     }
	}

	public AbstractSubTopologyList getAllAbstractSubTopologies() throws InputWrongType, NullPointerException, PropertyNotFoundException{

		AbstractSubTopologyList abstractSubTopologyList = new AbstractSubTopologyList();
		List<AbstractSubTopologyWithDatabaseID> abstractSubTopologyWithDatabaseIDList = abstractSubTopologyList.GetAbstractSubTopologyWithDatabaseIDList();
		List<Node> abstractSubTopologyIndexNodes = abstractNodeIndexDao.getAllAbstractTopologyIndexNodes();
		for(Node indexNode: abstractSubTopologyIndexNodes){
			AbstractSubTopology abstractSubTopology = new AbstractSubTopology();
	    			String specification = abstractNodeIndexDao.getSpecification(indexNode);
					InputStream stream = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
					AbstractSkeletonTransformer abstractSkeletonTransformer = new AbstractSkeletonTransformer(stream);
					abstractSubTopology = abstractSkeletonTransformer.getAbstractSkeleton();
					AbstractSubTopologyWithDatabaseID abstractSubTopologyWithDatabaseID = new AbstractSubTopologyWithDatabaseID();
					abstractSubTopologyWithDatabaseID.setDatabaseId(indexNode.getId());
					abstractSubTopologyWithDatabaseID.setAbstractSubTopology(abstractSubTopology);
					abstractSubTopologyWithDatabaseIDList.add(abstractSubTopologyWithDatabaseID);
	    		}

	return abstractSubTopologyList;

	}

	public AbstractSubTopology getOneAbstractSubTopology(long id) throws InputWrongType, java.lang.NullPointerException, PropertyNotFoundException{


		Node abstractSubTopologyIndexNode = abstractNodeIndexDao.getAbstractTopologyIndexNodeById(id);
	    AbstractSubTopology abstractSubTopology = new AbstractSubTopology();
	    String specification = abstractNodeIndexDao.getSpecification(abstractSubTopologyIndexNode);
		InputStream stream = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
		AbstractSkeletonTransformer abstractSkeletonTransformer = new AbstractSkeletonTransformer(stream);
		abstractSubTopology = abstractSkeletonTransformer.getAbstractSkeleton();

	return abstractSubTopology;

	}

}
