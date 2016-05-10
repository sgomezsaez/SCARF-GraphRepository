package org.eclipse.Service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput.Specification;
import org.eclipse.output.TopologyOutput.ConcreteNodeOutput;
import org.eclipse.output.TopologyOutput.InstanceNodeOutput;
import org.eclipse.topology.dao.ConcreteNodeDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.domain.ConcreteNode;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.AlphaTopologyTransformer;
import org.eclipse.transformer.Topology.ConcreteNodeTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;


public class ConcreteNodeService{

	//Get DaoFactory
	private final DaoFactory daoFactory = new DaoFactory();
	//Get NodeTypeDao
	private final ConcreteNodeDao concreteNodeDao = daoFactory.getNodeTypeDao();

	private Node node = null;

	public Node create(ConcreteNode topologyElement) {
		// TODO Auto-generated method stub
		node = concreteNodeDao.create(topologyElement);

		return node;
	}

	public Definitions getOneConcreteNodeDefinitionById (long id) throws InputWrongType, NullPointerException, PropertyNotFoundException{
		Node concreteNode = null;
		Definitions concreteNodeDefinitions = null;
		concreteNode = concreteNodeDao.getConcreteNodeById(id);
		if(concreteNode!=null){
		String specification = concreteNodeDao.getSpecification(concreteNode);
		InputStream streamOfSpec = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
		ConcreteNodeTransformer concreteNodeTransformer = new ConcreteNodeTransformer(streamOfSpec);
		concreteNodeDefinitions = concreteNodeTransformer.getToscaDefinition();
		}
		return concreteNodeDefinitions;
	}

	public ConcreteNodeOutput getAllConreteNode () throws InputWrongType, NullPointerException, PropertyNotFoundException{
		ConcreteNodeOutput concreteNodeOutput = new ConcreteNodeOutput();
		List<ConcreteNodeOutput.Specification>specificaiton = concreteNodeOutput.getSpecification();
		List<Node> allConcreteNodeList = concreteNodeDao.getAllConcreteNode();
		for(Node oneNode:allConcreteNodeList){
			Definitions concreteNodeDefinitions = getOneConcreteNodeDefinitionById(oneNode.getId());
			ConcreteNodeOutput.Specification newSpec = new ConcreteNodeOutput.Specification();
			newSpec.setId(oneNode.getId());
			newSpec.setDefinitions(concreteNodeDefinitions);
			specificaiton.add(newSpec);
		}
		return concreteNodeOutput;

    }

	public InstanceNodeOutput getAllInstanceNode(long concreteNodeId) throws InputWrongType,java.lang.NullPointerException {
		// TODO Auto-generated method stub
		InstanceNodeOutput instanceNodeOutput = new InstanceNodeOutput();
		InstanceNodeService instanceNodeService = new InstanceNodeService();
		List<InstanceNodeOutput.Specification>specificaiton = instanceNodeOutput.getSpecification();
		Node concreteNode = concreteNodeDao.getNodeById(concreteNodeId);
		List<Node> allInstanceNodeList = concreteNodeDao.getInstanceNodes(concreteNode);
		for(Node oneNode:allInstanceNodeList){
			Definitions instanceNodeDefinitions = instanceNodeService.getOneInstanceNodeDefinitionNodeById(oneNode.getId());
			InstanceNodeOutput.Specification newSpec = new InstanceNodeOutput.Specification();
			newSpec.setId(oneNode.getId());
			newSpec.setDefinitions(instanceNodeDefinitions);
			specificaiton.add(newSpec);
		}
		return instanceNodeOutput;
	}

	public Definitions getOneInstanceNode(long concreteNodeId,
			long instanceNodeId) throws InputWrongType, java.lang.NullPointerException {
		// TODO Auto-generated method stub
		Definitions instanceNodeDef = null;
		InstanceNodeService instanceNodeService = new InstanceNodeService();
		Node concreteNode = concreteNodeDao.getNodeById(concreteNodeId);
		List<Node> allInstanceNodeList = concreteNodeDao.getInstanceNodes(concreteNode);
		for(Node oneNode:allInstanceNodeList){
			if(oneNode.getId()==instanceNodeId){
				instanceNodeDef = instanceNodeService.getOneInstanceNodeDefinitionNodeById(oneNode.getId());
			}
		}
		return instanceNodeDef;
	}

	public boolean deleteConcreteNode(long concreteNodeId){
		boolean deleteResult = false;
		deleteResult = concreteNodeDao.deleteConcreteNode(concreteNodeId);
		return deleteResult;
	}

}
