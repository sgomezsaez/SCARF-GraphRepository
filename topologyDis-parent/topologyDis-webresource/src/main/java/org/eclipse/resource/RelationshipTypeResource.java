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
import org.eclipse.Service.RelationshipTypeService;
import org.eclipse.output.TopologyOutput.ConcreteNodeOutput;
import org.eclipse.output.TopologyOutput.InstanceNodeOutput;
import org.eclipse.output.TopologyOutput.RelationshipOutput;
import org.eclipse.topology.domain.RelationshipType;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.RelationshipTypeTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;
/**
 * this class is used for REST API of relatioinship type
 * @author Hao
 *
 */
@Path("relationshiptype")
public class RelationshipTypeResource {

	public RelationshipTypeResource(){
	};

    /**
     * persist a relationship Type
     * @param is
     * @return newly created ID of relationship type in location header
     * @throws IOException
     */
	@POST
	@Consumes("application/xml")
	public Response createNodetype( InputStream is) throws IOException {
		RelationshipType relationshipType = null;
		try {
			@SuppressWarnings("rawtypes")
			TopologyTransformer transformer = new RelationshipTypeTransformer(is);
			relationshipType = (RelationshipType) transformer.getDomainType();
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RelationshipTypeService service = new RelationshipTypeService();
		Node node = (Node) service.create(relationshipType);
	   return Response.created(URI.create("/relationshiptype/"+node.getId())).build();

	  }

    /**
     * get one relationship type by its ID
     * @param id
     * @return TOSCA definition of relationship Type
     * @throws InputWrongType
     * @throws PropertyNotFoundException
     */
	@GET
	@Produces("application/xml")
	@Path("{relationshipTypeId}")
	public Definitions getOneRelationshipType( @PathParam("relationshipTypeId") long id) throws InputWrongType, PropertyNotFoundException{

		Definitions relationshipTypeDefinitions = new Definitions();
		try{
		RelationshipTypeService service = new RelationshipTypeService();
	    relationshipTypeDefinitions = service.getOneRelationshipTypeByID(id);

		if(relationshipTypeDefinitions.getId()==null){

		}
		}
		catch(java.lang.NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		catch(org.neo4j.graphdb.NotFoundException e2){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	    return relationshipTypeDefinitions;
	}

	/**
	 * get all relationship type in database
	 * @return a list which contains all realtionship type in database
	 * @throws InputWrongType
	 * @throws PropertyNotFoundException
	 */
	@GET
	@Produces("application/xml")
	public RelationshipOutput getAllRelationshipType( ) throws InputWrongType, PropertyNotFoundException{
		RelationshipTypeService service = new RelationshipTypeService();
		RelationshipOutput relaitonshipOutput = service.getAllRelationshipType();
		if(relaitonshipOutput.getSpecification().isEmpty()){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

	    return relaitonshipOutput;
	}
    /**
     * delete one relationship type by its ID
     * @param id
     * @return
     * @throws InputWrongType
     */
	@Path("{relationshipTypeId}")
	@DELETE
	@Produces("application/xml")
	public Response deleteOneRelationshipTypeById(@PathParam("relationshipTypeId") long id ) throws InputWrongType{
		RelationshipTypeService service = new RelationshipTypeService();
		boolean result = service.deleteRelationshipType(id);
		if(result==false){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
	    return Response.accepted().build();
	}
}
