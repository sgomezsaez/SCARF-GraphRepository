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

import org.eclipse.Service.NameSpaceService;
import org.eclipse.Service.PerformanceService;
import org.eclipse.Service.WorkloadService;
import org.eclipse.output.others.NamespaceList;
import org.eclipse.topology.domain.NameSpace;
import org.eclipse.transformer.Topology.NameSpaceTransformer;
import org.neo4j.graphdb.Node;

/**
 * this class is used for REST API of name space
 * @author Hao
 *
 */
@Path("namespace")
public class NameSpaceResource {

	public NameSpaceResource(){

	}
    /**
     * create one name space
     * @param is
     * @return the newly created id of name space
     * @throws IOException
     */
	@POST
	@Consumes("application/xml")
	public Response createNameSpace( InputStream is) throws IOException {

		NameSpace nameSpace = null;

		NameSpaceTransformer transformer = new NameSpaceTransformer(is);
			try {
				nameSpace = transformer.getNameSpace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			NameSpaceService service = new NameSpaceService();
		   Node node = (Node) service.create(nameSpace);
	   return Response.created(URI.create("/namespace/"+node.getId())).build();
	  }
    /**
     * retrieve all name spaces in database
     * @return the list which contains all name spaces in database
     * @throws IOException
     */
	@GET
	@Produces("application/xml")
	public NamespaceList getAllNamespace() throws IOException {
		NameSpaceService service = new NameSpaceService();
		NamespaceList namespaceList = service.getAllNamespace();
		if(namespaceList.getNameSpaceWithDatabaseID().isEmpty()) {
		    throw new WebApplicationException(Response.Status.NOT_FOUND);
		    }
	   return namespaceList;
	  }
    /**
     * retrieve on name space by ID
     * @param id
     * @return One name space
     * @throws IOException
     */
	@GET
	@Produces("application/xml")
	@Path("{id}")
	public NameSpace getOneNamespace(@PathParam("id") long id) throws IOException {
		NameSpaceService service = new NameSpaceService();
		NameSpace namespace = service.getOneNamespace(id);
		if(namespace==null) {
		    throw new WebApplicationException(Response.Status.NOT_FOUND);
		    }
	   return namespace;
	  }

	/**
	 * delete one name space by ID
	 * @param id
	 * @return if delete successfully return accepted or return not_implemented.
	 * @throws IOException
	 */
	@DELETE
	@Produces("application/xml")
	@Path("{id}")
	public Response deleteOneNameSpace(@PathParam("id") long id) throws IOException {

		NameSpaceService service = new NameSpaceService();
		boolean result = service.deleteOneNameSpace(id);
		if(result==false){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
	    return Response.accepted().build();
	}

}


