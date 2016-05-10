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

import org.eclipse.Service.PerformanceService;
import org.eclipse.Service.WorkloadService;
import org.eclipse.output.others.PerformanceList;
import org.eclipse.topology.domain.Performance;
import org.eclipse.transformer.TopologyStatistics.PerformanceTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

/**
 * This class is used for REST API of performance resource
 * @author Hao
 *
 */
@Path("performance")
public class PerformanceResource {

public PerformanceResource(){
};
/**
 * persist a performance
 * @param is
 * @return
 * @throws IOException
 */
@POST
@Consumes("application/xml")
public Response createPerformance( InputStream is) throws IOException {

	Performance performance = null;
	PerformanceService service = new PerformanceService();
	PerformanceTransformer transformer = new PerformanceTransformer(is);
		try {
			performance = transformer.getPerformance();
			service.setPerformanceSpecification(transformer.getSpecification());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	Node node = (Node) service.create(performance);
   return Response.created(URI.create("/performance/"+node.getId())).build();
  }
/**
 * get a performance by ID or performance
 * @param id
 * @return
 * @throws IOException
 * @throws JAXBException
 * @throws PropertyNotFoundException
 * @throws NullPointerException
 */
@GET
@Produces("application/xml")
@Path("{id}")
public Performance getPerformance(@PathParam("id") String id) throws IOException, JAXBException, NullPointerException, PropertyNotFoundException {

	Performance performance = null;
	PerformanceService service = new PerformanceService();
	performance = service.get(id);
	if(performance==null) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
    return performance;
  }

/**
 * get all performance in the database
 * @return
 * @throws IOException
 * @throws JAXBException
 * @throws PropertyNotFoundException
 * @throws NullPointerException
 */
@GET
@Produces("application/xml")
public PerformanceList getAllPerformance( ) throws IOException, JAXBException, NullPointerException, PropertyNotFoundException {

	PerformanceService service = new PerformanceService();
	PerformanceList performanceList= service.getall();
	if(performanceList.getPerformanceWithDatabaseID().isEmpty()) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
    return performanceList;
  }


/**
 * delete a performance by database ID
 * @param id
 * @return
 * @throws IOException
 */
@DELETE
@Path("{id}")
public String deletePerformance(@PathParam("id") long id) throws IOException {

	PerformanceService service = new PerformanceService();
	boolean result = service.delete(id);
	if(result){
	String string ="Delete successfully";
    return string;}
	else{
    throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
	}

  }

}
