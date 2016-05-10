package org.eclipse.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.Service.AlphaTopologyService;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput;
import org.eclipse.output.TopologyOutput.ViableTopologyList;
import org.eclipse.output.TopologyOutput.ViableTopologyOutput;
import org.eclipse.output.others.PerformanceList;
import org.eclipse.output.others.WorkloadList;
import org.eclipse.topology.domain.AlphaTopology;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.AlphaTopologyTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.eclipse.transformer.TopologyStatistics.PerformanceTransformer;
import org.eclipse.transformer.TopologyStatistics.WorkloadTransformer;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

/**
 * This class is used to build REST API for alpha topology
 * @author Hao
 *
 */
@Path("topology/alphatopology")
public class AlphaTopologyResource {

	public AlphaTopologyResource(){
	}

	/**
	 * Create one alpha topology
	 *
	 * @param is inputStream of XML
	 * @return
	 * @throws IOException
	 */
	@POST
	@Consumes("application/xml")
	public Response createAlphaTopology(InputStream is) throws IOException {

		AlphaTopology alphaTopology = null;
		try {
			AlphaTopologyTransformer transformer = new AlphaTopologyTransformer(is);
			alphaTopology = transformer.getDomainType();
		} catch (InputWrongType e) {
			e.printStackTrace();
		}
		AlphaTopologyService service = new AlphaTopologyService();
	    Node alphaTopologyIndex = service.AddAlphaTopology(alphaTopology);

	   return Response.status(201).type("application/xml")
               .entity( "<?xml version=\"1.0\" encoding=\"utf-8\" ?> <id>"+alphaTopologyIndex.getId()+"</id>").build();
	   /*	   return Response.created(URI.create(("/alphatopology/")+alphaTopologyIndex.getId())).build();
			    .header("Access-Control-Allow-Origin", "*")//to support CROS feature
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.header("Access-Control-Allow-Headers", "*")
				.allow("OPTIONS").build();*/
	   /*try solve the issue of javascript can not access the location header but does not working, leave my solution here*/
	  }

	/**
	 * one alpha topology performs  a workload
	 * @param is
	 * @param alphaTopologyId
	 * @return
	 * @throws JAXBException
	 */
	@POST
	@Consumes("application/xml")
	@Path("{alphaTopologyId}/workload")
	public String performWorkload(InputStream is,@PathParam("alphaTopologyId") long alphaTopologyId) throws JAXBException{
		WorkloadTransformer workloadTransformer = new WorkloadTransformer(is);
		long workloadId = workloadTransformer.getWorkloadId();
		AlphaTopologyService service = new AlphaTopologyService();
		try{
		if(service.performWorkload(alphaTopologyId, workloadId)){
			return "employ workload ID:"+workloadId+" "+"to AlphaTopology ID:"+alphaTopologyId+" "+"Successfully!";
		}
		else{
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
		}catch(NotFoundException e){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
	}

	/**
	 * one alpha topology performs a performance
	 * @param is
	 * @param alphaTopologyId
	 * @return
	 * @throws JAXBException
	 */
	@POST
	@Consumes("application/xml")
	@Path("{alphaTopologyId}/performance")
	public String performPerformance(InputStream is,@PathParam("alphaTopologyId") long alphaTopologyId) throws JAXBException{
		PerformanceTransformer performanceTransformer = new PerformanceTransformer(is);
		long performanceId = performanceTransformer.getPerformanceId();
		AlphaTopologyService service = new AlphaTopologyService();
		try{
		if(service.performPerformance(alphaTopologyId, performanceId)){
			return "employ performanace ID:"+performanceId+" "+"to AlphaTopology ID:"+alphaTopologyId+" "+"Successfully!";
		}
		else{
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
		}
		catch(java.lang.NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
        catch(NotFoundException e){
	        throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
        }
	}

	/**
	 * Delete a workload by its ID in database
	 * @param is
	 * @param alphaTopologyId
	 * @return
	 * @throws JAXBException
	 */

	@DELETE
	@Consumes("application/xml")
	@Path("{alphaTopologyId}/workload")
	public String unemployWorkload(InputStream is,@PathParam("alphaTopologyId") long alphaTopologyId) throws JAXBException{
		WorkloadTransformer workloadTransformer = new WorkloadTransformer(is);
		long workloadId = workloadTransformer.getWorkloadId();
		AlphaTopologyService service = new AlphaTopologyService();
		if(service.unperformWorkload(alphaTopologyId, workloadId)){
			return "unemploy workload ID:"+workloadId+" "+"from AlphaTopology ID:"+alphaTopologyId+" "+"Successfully!";
		}
		else{
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
	}

	/**
	 * Delete AlphaTopology by ID
	 * @param is
	 * @param alphaTopologyId
	 * @return
	 * @throws JAXBException
	 */
	@DELETE
	@Consumes("application/xml")
	@Path("{alphaTopologyId}")
	public String deleteAlphaTopologyById(InputStream is,@PathParam("alphaTopologyId") long alphaTopologyId) throws JAXBException{
		AlphaTopologyService service = new AlphaTopologyService();
		try{
		if(service.deleteAlphaTopologyById(alphaTopologyId)){
			return "delete alphaTopology ID:"+alphaTopologyId+" "+"from database Successfully!";
		}
		else{
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
		}
		catch(org.neo4j.graphdb.QueryExecutionException e){
			throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
		}
	}
	/**
	 * Query workload evolution of an alpha Topology by its id, start time and end time
	 * @param alphaTopologyId
	 * @param from
	 * @param to
	 * @return
	 * @throws JAXBException
	 */
	@GET
	@Produces("application/xml")
	@Path("{alphaTopologyId}/workload")
	public WorkloadList getWorkloadsHistory(  @PathParam("alphaTopologyId") long alphaTopologyId,
			                          @QueryParam("from") String from,
			                          @QueryParam("to") String to) throws JAXBException{
		WorkloadList workloadHistoryList = new WorkloadList();
		try{
		AlphaTopologyService service = new AlphaTopologyService();
		workloadHistoryList = service.queryWorkloadHistory(alphaTopologyId, from, to);
		}
		catch(org.neo4j.graphdb.QueryExecutionException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		if(workloadHistoryList.getWorkloadWithDatabaseID().isEmpty()){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return workloadHistoryList;

    }
	/**
	 * Get all viableTopology for an alhpatopology by alphaTOpology ID
	 * @param alphaTopologyId
	 * @param from
	 * @param to
	 * @return
	 * @throws JAXBException
	 * @throws PropertyNotFoundException
	 * @throws NullPointerException
	 */
	@GET
	@Produces("application/xml")
	@Path("{alphaTopologyId}/viabletopology")
	public ViableTopologyList getviableTopologyHistory(  @PathParam("alphaTopologyId") long alphaTopologyId,
			                          @QueryParam("from") String from,
			                          @QueryParam("to") String to) throws JAXBException, NullPointerException, PropertyNotFoundException{
		AlphaTopologyService service = new AlphaTopologyService();
		ViableTopologyList viableTopologyList = service.queryMuTopologyHistory(alphaTopologyId, from, to);
		if(viableTopologyList.getViableTopologyWithDababaseID().isEmpty()){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return viableTopologyList;

    }

	@GET
	@Produces("application/xml")
	@Path("{alphaTopologyId}/viabletopology/{viableTopologId}")
	public ViableTopologyOutput getOneViableTopology(  @PathParam("alphaTopologyId") long alphaTopologyId,
			                                         @PathParam("viableTopologId") long viableTopologId) throws JAXBException, PropertyNotFoundException{
		ViableTopologyOutput viableTopology = null;
		try{
		AlphaTopologyService service = new AlphaTopologyService();
		viableTopology = service.getOneViableTopology(alphaTopologyId, viableTopologId);
		}
		catch(java.lang.NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return viableTopology;

    }

	/**
	 * Query performance evolution of an alpha Topology by its id, start time and end time
	 * @param alphaTopologyId
	 * @param from
	 * @param to
	 * @return
	 * @throws JAXBException
	 * @throws PropertyNotFoundException
	 * @throws NullPointerException
	 */
	@GET
	@Produces("application/xml")
	@Path("{alphaTopologyId}/performance")
	public PerformanceList getPerformanceHistory(  @PathParam("alphaTopologyId") long alphaTopologyId,
			                          @QueryParam("from") String from,
			                          @QueryParam("to") String to) throws JAXBException, NullPointerException, PropertyNotFoundException{
		PerformanceList performanceList = new PerformanceList();
		try{
		AlphaTopologyService service = new AlphaTopologyService();
	    performanceList = service.queryPerformanceHistory(alphaTopologyId, from, to);
		}
		catch(org.neo4j.graphdb.QueryExecutionException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		if(performanceList.getPerformanceWithDatabaseID().isEmpty()){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return performanceList;

    }
	/**
	 * get all workloads for a alpha topology with its ID
	 * @param alphaTopology
	 * @return
	 * @throws JAXBException
	 */
	@GET
	@Produces("application/xml")
	@Path("{alphaTopologyId}/workload/all")
	public WorkloadList getAllWorkloadsHistory( @PathParam("alphaTopologyId") long alphaTopology) throws JAXBException{
		WorkloadList workloadHistoryList = new WorkloadList();
		try{
		AlphaTopologyService service = new AlphaTopologyService();
		 workloadHistoryList = service.queryAllWorkloadHistory(alphaTopology);
		}
		catch(org.neo4j.graphdb.QueryExecutionException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		if(workloadHistoryList.getWorkloadWithDatabaseID().isEmpty()){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return workloadHistoryList;

    }

	@GET
	@Produces("application/xml")
	@Path("{alphaTopologyId}")
	public Definitions getOneAlphaTopology( @PathParam("alphaTopologyId") long alphaTopology) throws JAXBException, NullPointerException, PropertyNotFoundException{
		AlphaTopologyService service = new AlphaTopologyService();
		Definitions alphaTopologyDefinitions = service.getAlphaTopologyDefinitionById(alphaTopology);
		if(alphaTopologyDefinitions==null){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return alphaTopologyDefinitions;
    }

	@GET
	@Produces("application/xml")
	public AlphaTopologyOutput getAllAlphaTopology( ) throws JAXBException, NullPointerException, PropertyNotFoundException{
		AlphaTopologyService service = new AlphaTopologyService();
		AlphaTopologyOutput alphaTopologyOutput = service.getAllAlphaTopology();
		if(alphaTopologyOutput==null){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
        return alphaTopologyOutput;
    }

}
