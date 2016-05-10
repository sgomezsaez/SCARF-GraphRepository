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

import org.eclipse.Service.WorkloadService;
import org.eclipse.output.others.WorkloadList;
import org.eclipse.topology.domain.Workload;
import org.eclipse.transformer.TopologyStatistics.WorkloadTransformer;
import org.neo4j.graphdb.Node;
/**
 * this class is used for REST API of workload
 * @author Hao
 *
 */
@Path("workload")
public class WorkloadResource {

public WorkloadResource(){
};
/**
 * persist a workload
 * @param is
 * @return
 * @throws IOException
 */
@POST
@Consumes("application/xml")
public Response createWorkload( InputStream is) throws IOException {

	Workload workload = null;

		WorkloadTransformer transformer = new WorkloadTransformer(is);
		try {
			workload = transformer.getWorkload();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	WorkloadService service = new WorkloadService();
	Node node = (Node) service.create(workload);
   return Response.created(URI.create("/workload/"+node.getId())).build();
  }
/**
 * get a workload by its database ID or workload ID
 * @param id
 * @return
 * @throws IOException
 */

@GET
@Produces("application/xml")
@Path("{id}")
public Workload getWorkload(@PathParam("id") String id) throws IOException {

	Workload workload = null;
	WorkloadService service = new WorkloadService();
	workload = service.get(id);
	if(workload==null) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
   return workload;
  }




/**
 * get all workload in database
 * @return
 * @throws IOException
 */
@GET
@Produces("application/xml")
public WorkloadList getAllWorkload() throws IOException {
	WorkloadService service = new WorkloadService();
	WorkloadList workloadList = service.getall();
	if(workloadList.getWorkloadWithDatabaseID().isEmpty()) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
   return workloadList;
  }

/**
 * delete a workload by its database ID
 * @param id the ID in database
 * @return
 * @throws IOException
 */
@DELETE
@Path("{id}")
public String deleteWorkload(@PathParam("id") long id) throws IOException {

	WorkloadService service = new WorkloadService();
	boolean result =service.delete(id);
	if(result){
		String string ="Delete successfully";
	    return string;}
		else{
	    throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		 }
  }

}
