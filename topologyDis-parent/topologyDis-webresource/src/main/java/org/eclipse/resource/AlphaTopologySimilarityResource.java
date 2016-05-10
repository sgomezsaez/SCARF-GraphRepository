package org.eclipse.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.Service.TopologyDiscoveryService;
import org.eclipse.Service.TopologySimilarityService;
import org.eclipse.output.TopologyOutput.AlphaTopologySimilarityList;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;
/**
 * This class is used to build REST API for siimlar topology
 * @author Hao
 *
 */
@Path("similartopology")
	public class AlphaTopologySimilarityResource {

		public AlphaTopologySimilarityResource(){

		};

		@GET
		@Path("{id}")
		@Produces("application/xml")
		//public String getMuTopologyDefinitions(@PathParam("id") long id) throws JAXBException  {
		public AlphaTopologySimilarityList getMuTopologyDefinitions(@PathParam("id") long id) throws JAXBException, NullPointerException, PropertyNotFoundException  {
			  TopologySimilarityService topologySimilarityService = new TopologySimilarityService(id);
			  AlphaTopologySimilarityList output = topologySimilarityService.getSimilarTopologList();
/*		    if (output==null) {
		    throw new WebApplicationException(Response.Status.NOT_FOUND);
		    }*/
		    if (output==null) {
		    	return new AlphaTopologySimilarityList();
		    }
		    /*Marshaller am = JAXBSupport.createMarshaller(true);
	    	StringWriter sw = new StringWriter();
		    try {
				 am.marshal(output, sw);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }
		    //this part just check whether the definitions can be retrieved back for winery using, this part can be used in the client in the future
		    Unmarshaller um = JAXBSupport.createUnmarshaller();
		    System.out.println("Start to print the tosca definition for Winery using");
		    System.out.println("----------------------------------------------------------");
		    try {
		    	InputStream stream = new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8));
		    	MuTopologies muTopologies =  (MuTopologies) um.unmarshal(stream);
		    	for(int i=0;i<muTopologies.getSpecification().size();i++){
		    	Definitions oneDef = muTopologies.getSpecification().get(i).getDefinitions();
		    	am.marshal(oneDef, System.out);
		    	System.out.println("----------------------------------------------------------");
		    	}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }*/

	        return output;
		}

	}

