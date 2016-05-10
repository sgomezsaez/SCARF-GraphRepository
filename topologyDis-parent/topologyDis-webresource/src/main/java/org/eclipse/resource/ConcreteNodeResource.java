package org.eclipse.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.Service.AlphaTopologyService;
import org.eclipse.Service.ConcreteNodeService;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput;
import org.eclipse.output.TopologyOutput.ConcreteNodeOutput;
import org.eclipse.output.TopologyOutput.InstanceNodeOutput;
import org.eclipse.topology.domain.ConcreteNode;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.ConcreteNodeTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

/**
 * This class is used to build REST API for concrete node
 * @author Hao
 *
 */
@Path("concretenode")
public class ConcreteNodeResource {

public ConcreteNodeResource(){
};
/**
 *  persist a concrete node
 * @param type
 * @param is
 * @return newly created id of concrete node in location header
 * @throws IOException
 */
@Path("{type}")
@POST
@Consumes("application/xml")
public Response createConcreteNode(@PathParam("type") String type, InputStream is) throws IOException {

	ConcreteNode nodetype = null;
	try {
		@SuppressWarnings("rawtypes")
		TopologyTransformer transformer = new ConcreteNodeTransformer(is,type);
		nodetype = (ConcreteNode) transformer.getDomainType();
	} catch (InputWrongType e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	ConcreteNodeService service = new ConcreteNodeService();
	Node node = (Node) service.create(nodetype);
   return Response.created(URI.create("/concretenode/"+node.getId())).build();

  }
/**
 * retrieve one concrete node by its id
 * @param id
 * @return the TOSCA definition of nodetype
 * @throws InputWrongType
 * @throws PropertyNotFoundException
 * @throws NullPointerException
 */
@GET
@Produces("application/xml")
@Path("{concreteNodeId}")
public Definitions getOneConcreteNode( @PathParam("concreteNodeId") long id) throws InputWrongType, NullPointerException, PropertyNotFoundException{
	ConcreteNodeService service = new ConcreteNodeService();
	Definitions concreteNodeDefinitions = service.getOneConcreteNodeDefinitionById(id);
	if(concreteNodeDefinitions==null){
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
    return concreteNodeDefinitions;
}

/**
 * to retrieve all concrete nodes in database
 * @return all concrete node list in database
 * @throws InputWrongType
 * @throws PropertyNotFoundException
 * @throws NullPointerException
 */
@GET
@Produces("application/xml")
public ConcreteNodeOutput getAllConcreteNode( ) throws InputWrongType, NullPointerException, PropertyNotFoundException{
	ConcreteNodeService service = new ConcreteNodeService();
	ConcreteNodeOutput concreteNodeOutput = service.getAllConreteNode();
	if(concreteNodeOutput.getSpecification().isEmpty()){
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
    return concreteNodeOutput;
}

/**
 * retrieve all instance nodes of one concrete node
 * @param id
 * @return the list of all instance nodes in database
 * @throws InputWrongType
 */
@Path("{concreteNodeId}/instancenode")
@GET
@Produces("application/xml")
public InstanceNodeOutput getAllInstanceNodeOfConcreteNode(@PathParam("concreteNodeId") long id ) throws InputWrongType{
	InstanceNodeOutput instanceNodeOutput = new InstanceNodeOutput();
	try{
	ConcreteNodeService service = new ConcreteNodeService();
    instanceNodeOutput = service.getAllInstanceNode(id);
	}
	catch(java.lang.NullPointerException e){
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
	if(instanceNodeOutput.getSpecification().isEmpty()){
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
    return instanceNodeOutput;
}

/**
 * Delete one concrete node by ID
 * @param id
 * @return if delete successfully, then return accepted or return not_implemented
 * @throws InputWrongType
 */
@Path("{concreteNodeId}")
@DELETE
@Produces("application/xml")
public Response deleteOneConcreteNodeById(@PathParam("concreteNodeId") long id ) throws InputWrongType{
	ConcreteNodeService service = new ConcreteNodeService();
	boolean result = service.deleteConcreteNode(id);
	if(result==false){
		throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
	}
    return Response.accepted().build();
}

/**
 * For a given concrete node, retrieve one instance nodes by the instance node id
 * @param concreteNodeId
 * @param instanceNodeId
 * @return the tosca definition of the instance node
 * @throws InputWrongType
 */
@Path("{concreteNodeId}/instancenode/{instanceNodeId}")
@GET
@Produces("application/xml")
public Definitions getOneInstanceNodeOfConcreteNode(@PathParam("concreteNodeId") long concreteNodeId,@PathParam("instanceNodeId") long instanceNodeId) throws InputWrongType{
	Definitions instanceNodeDefinitions = new Definitions();
	try{
	ConcreteNodeService service = new ConcreteNodeService();
	instanceNodeDefinitions= service.getOneInstanceNode(concreteNodeId,instanceNodeId);
	}
	catch(java.lang.NullPointerException e){
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
    return instanceNodeDefinitions;
}
}
