package org.eclipse.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.Service.AbstractSubTopologyService;
import org.eclipse.Service.PerformanceService;
import org.eclipse.inputModel.AbstractSubTopology;
import org.eclipse.output.TopologyOutput.AbstractSubTopologyList;
import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.eclipse.transformer.Topology.AbstractSkeletonTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;
/**
 *
 * @author Hao
 * This class is used to build REST API for abstract sub topology
 */

@Path("topology/abstractsubtopology")
public class AbstracSubTopologyResource {

	public AbstracSubTopologyResource(){
	}
    /**
     *
     * @param is input stream
     * @return return the newly created id of abstract sub-topology in the location header
     * @throws IOException
     */
	@POST
	@Consumes("application/xml")
	public Response createAbstractSubTopology(InputStream is) throws IOException {

		AbstractSubTopologyDomain abstractSkeletonDomain = null;
		try {
			@SuppressWarnings("rawtypes")
			TopologyTransformer transformer = new AbstractSkeletonTransformer(is);
			abstractSkeletonDomain = (AbstractSubTopologyDomain) transformer.getDomainType();
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AbstractSubTopologyService service = new AbstractSubTopologyService();
	   Node abstractSkeletonIndex = service.AddAbstractSkeleton(abstractSkeletonDomain);
	   return Response.created(URI.create("/abstractsubtopology/"+abstractSkeletonIndex.getId())).build();

	  }
    /**
     * Retrieve all abstract sub-topologies in database
     * @return abstract sub-topology list which contain all existed abstract sub-topologies in database
     * @throws IOException
     * @throws JAXBException
     * @throws InputWrongType
     * @throws PropertyNotFoundException
     * @throws NullPointerException
     */
	@GET
	@Produces("application/xml")
	public AbstractSubTopologyList getAllAbstractSubTopologies( ) throws IOException, JAXBException, InputWrongType, NullPointerException, PropertyNotFoundException {

		AbstractSubTopologyService service = new AbstractSubTopologyService();
		AbstractSubTopologyList abstractSubTopologyList= service.getAllAbstractSubTopologies();
		if(abstractSubTopologyList.GetAbstractSubTopologyWithDatabaseIDList().isEmpty()) {
		    throw new WebApplicationException(Response.Status.NOT_FOUND);
		    }
	    return abstractSubTopologyList;
	  }

	/**
	 * Retrieve one abstract sub-topology by its ID
	 * @param id
	 * @return one abstract sub-topology
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InputWrongType
	 * @throws PropertyNotFoundException
	 */
	@GET
	@Produces("application/xml")
	@Path("{id}")
	public AbstractSubTopology getOneAbstractSubTopology(@PathParam("id") long id) throws IOException, JAXBException, InputWrongType, PropertyNotFoundException {
		AbstractSubTopology abstractSubTopology = new AbstractSubTopology();
		try{
		AbstractSubTopologyService service = new AbstractSubTopologyService();
		 abstractSubTopology= service.getOneAbstractSubTopology(id);
		}
		catch(java.lang.NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	    return abstractSubTopology;
	  }
}
