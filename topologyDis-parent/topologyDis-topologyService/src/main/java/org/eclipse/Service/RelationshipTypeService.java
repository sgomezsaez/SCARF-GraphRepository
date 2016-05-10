package org.eclipse.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.output.TopologyOutput.ConcreteNodeOutput;
import org.eclipse.output.TopologyOutput.RelationshipOutput;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.RelationshipTypeDao;
import org.eclipse.topology.domain.RelationshipType;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TRelationshipType;
import org.eclipse.transformer.Topology.ConcreteNodeTransformer;
import org.eclipse.transformer.Topology.RelationshipTypeTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class RelationshipTypeService{

	//Get DaoFactory
	private final DaoFactory daoFactory = new DaoFactory();
	//Get NodeTypeDao
	private final RelationshipTypeDao relationshipTypeDao = daoFactory.getRelationshipTypeDao();

	private Node node = null;



	public Node create(RelationshipType topologyElement) {
		// TODO Auto-generated method stub
        node = relationshipTypeDao.create(topologyElement);

		return node;
	}

	public Definitions getOneRelationshipTypeByID(long id) throws InputWrongType,org.neo4j.kernel.api.exceptions.PropertyNotFoundException{

		Node relationshipTypeNode = null;
		Definitions relationshipTypeDefinitions = null;
		NotFoundException e = new NotFoundException();
		relationshipTypeNode = relationshipTypeDao.getNodeById(id);
		if(relationshipTypeNode!=null){
	    if(!relationshipTypeDao.isRelationshipType(id)){
	    	throw e;
	    }
		String specification = relationshipTypeDao.getSpecification(relationshipTypeNode);
		InputStream streamOfSpec = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
		RelationshipTypeTransformer relationshipTypeTransformer = new RelationshipTypeTransformer(streamOfSpec);
		relationshipTypeDefinitions = relationshipTypeTransformer.getDefi_type();
		}
		return relationshipTypeDefinitions;

	}

	public RelationshipOutput getAllRelationshipType () throws InputWrongType, PropertyNotFoundException{
		RelationshipOutput relationshipOutput = new RelationshipOutput();
		List<RelationshipOutput.Specification>specificaiton = relationshipOutput.getSpecification();
		List<Node> allRelationshipList = relationshipTypeDao.getAllRelationshipType();
		for(Node oneNode:allRelationshipList){
			Definitions concreteNodeDefinitions = getOneRelationshipTypeByID(oneNode.getId());
			RelationshipOutput.Specification newSpec = new RelationshipOutput.Specification();
			newSpec.setId(oneNode.getId());
			newSpec.setDefinitions(concreteNodeDefinitions);
			specificaiton.add(newSpec);
		}
		return relationshipOutput;

    }

	public boolean deleteRelationshipType(long id){
		boolean deleteResult = false;
		deleteResult = relationshipTypeDao.deleteRelationshipType(id);
		return deleteResult;
	}

}
