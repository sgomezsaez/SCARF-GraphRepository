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

import org.eclipse.Exception.InputWrongType;
import org.eclipse.Service.ConcreteNodeService;
import org.eclipse.Service.InstanceNodeService;
import org.eclipse.output.TopologyOutput.ConcreteNodeOutput;
import org.eclipse.output.TopologyOutput.InstanceNodeOutput;
import org.eclipse.topology.domain.InstanceNode;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.InstanceNodeTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.neo4j.graphdb.Node;

/**
 * this class is used to provide REST API for instance node
 * @author Hao
 *
 */
@Path("instancenode")
public class InstanceNodeResource {

	public InstanceNodeResource(){
	};

    /**
     * persist an instance node
     * @param is
     * @return the newly created ID of instance node
     * @throws IOException
     */
	@POST
	@Consumes("application/xml")
	public Response createNodetype( InputStream is) throws IOException {
		InstanceNode instanceNode = null;
		try {
			@SuppressWarnings("rawtypes")
			TopologyTransformer transformer = new InstanceNodeTransformer(is,"null");
			instanceNode = (InstanceNode) transformer.getDomainType();
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InstanceNodeService service = new InstanceNodeService();
		Node node = (Node) service.create(instanceNode);
	   return Response.created(URI.create("/instancenode/"+node.getId())).build();

	  }
    /**
     * get all instance nodes in database
     * @return a list which contain all instance nodes in database
     * @throws InputWrongType
     */
	@GET
	@Produces("application/xml")
	public InstanceNodeOutput getAllInstanceNode( ) throws InputWrongType{
		InstanceNodeService service = new InstanceNodeService();
		InstanceNodeOutput instanceNodeOutput = service.getAllinstanceNode();
		if(instanceNodeOutput.getSpecification().isEmpty()){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	    return instanceNodeOutput;
	}

	/**
	 * get one instance node by its ID
	 * @param id
	 * @return TOSCA definition of the instance node
	 * @throws InputWrongType
	 */
	@GET
	@Produces("application/xml")
	@Path("{instanceNodeId}")
	public Definitions getOneInstanceNode( @PathParam("instanceNodeId") long id) throws InputWrongType{
		InstanceNodeService service = new InstanceNodeService();
		Definitions instanceNodeDefinitions = service.getOneInstanceNodeDefinitionNodeById(id);
		if(instanceNodeDefinitions==null){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	    return instanceNodeDefinitions;
	}

	/**
	 * delete one instance node by ID
	 * @param id
	 * @return if delete successfully return accepted or return not_implemented.
	 * @throws InputWrongType
	 */
	@Path("{instanceNodeId}")
	@DELETE
	@Produces("application/xml")
	public Response deleteOneInstanceNodeById(@PathParam("instanceNodeId") long id ) throws InputWrongType{
		InstanceNodeService service = new InstanceNodeService();
		boolean result = false;
		try{
		result = service.deleteInstanceNode(id);
		}
		catch(org.neo4j.graphdb.NotFoundException e){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
		if(result==false){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
	    return Response.accepted().build();
	}


}
