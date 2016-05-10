package org.eclipse.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.output.TopologyOutput.ConcreteNodeOutput;
import org.eclipse.output.TopologyOutput.InstanceNodeOutput;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.InstanceNodeDao;
import org.eclipse.topology.domain.InstanceNode;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.ConcreteNodeTransformer;
import org.eclipse.transformer.Topology.InstanceNodeTransformer;
import org.neo4j.graphdb.Node;

public class InstanceNodeService {

	//Get DaoFactory
	private final DaoFactory daoFactory = new DaoFactory();
	//Get NodeTypeDao
	private final InstanceNodeDao instanceNodeDao = daoFactory.getInstanceNodeDao();

	private Node node = null;


	public Node create(InstanceNode topologyElement) {
		// TODO Auto-generated method stub
        node = instanceNodeDao.create(topologyElement);

		return node;
	}

	public Definitions getOneInstanceNodeDefinitionNodeById (long id) throws InputWrongType{
		Node instanceNode = null;
		Definitions instanceNodeDefinitions = null;
		instanceNode = instanceNodeDao.getInstanceNodeById(id);
		if(instanceNode!=null){
		String specification = instanceNodeDao.getSpecification(instanceNode);
		InputStream streamOfSpec = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
		InstanceNodeTransformer instanceNodeTransformer = new InstanceNodeTransformer(streamOfSpec);
		instanceNodeDefinitions = instanceNodeTransformer.getToscaDefinition();
		}
		return instanceNodeDefinitions;
	}

	public InstanceNodeOutput getAllinstanceNode () throws InputWrongType{
		InstanceNodeOutput instanceNodeOutput = new InstanceNodeOutput();
		List<InstanceNodeOutput.Specification>specificaiton = instanceNodeOutput.getSpecification();
		List<Node> allInstanceNodeList = instanceNodeDao.getAllInstanceNodes();
		for(Node oneNode:allInstanceNodeList){
			Definitions instanceNodeDefinitions = getOneInstanceNodeDefinitionNodeById(oneNode.getId());
			InstanceNodeOutput.Specification newSpec = new InstanceNodeOutput.Specification();
			newSpec.setId(oneNode.getId());
			newSpec.setDefinitions(instanceNodeDefinitions);
			specificaiton.add(newSpec);
		}
		return instanceNodeOutput;
	}

	public boolean deleteInstanceNode(long instanceNodeId) throws org.neo4j.graphdb.NotFoundException{
		boolean deleteResult = false;
		deleteResult = instanceNodeDao.deleteOneInstanceNodeById(instanceNodeId);
		return deleteResult;
	}
}
