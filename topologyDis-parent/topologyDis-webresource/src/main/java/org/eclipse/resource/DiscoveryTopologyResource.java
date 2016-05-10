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
import org.eclipse.output.TopologyOutput.MuTopologyOutput;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;
/**
 *  this class is for REST API of viable topology
 * @author Hao
 *
 */
@Path("discoverytopology")
public class DiscoveryTopologyResource {

	public DiscoveryTopologyResource(){

	};

	/**
	 * Discover the viable topologies for a given alpha topology ID
	 * @param id
	 * @return The list of viable topologies
	 * @throws PropertyNotFoundException
	 * @throws NullPointerException
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public MuTopologyOutput getMuTopologyDefinitions(@PathParam("id") long id) throws NullPointerException, PropertyNotFoundException {
		MuTopologyOutput output = new MuTopologyOutput();
        try{
		TopologyDiscoveryService topologyDiscoveryService = new TopologyDiscoveryService(id);

		output = topologyDiscoveryService.getMuTopologiesTest();
        if(output == null){
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

	    Marshaller am = JAXBSupport.createMarshaller(true);
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
	    	MuTopologyOutput muTopologies =  (MuTopologyOutput) um.unmarshal(stream);
	    	for(int i=0;i<muTopologies.getSpecification().size();i++){
	    	Definitions oneDef = muTopologies.getSpecification().get(i).getDefinitions();
	    	am.marshal(oneDef, System.out);
	    	System.out.println("----------------------------------------------------------");
	    	}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
        }
        catch(NotFoundException e){
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return output;
	}


}
//add the name of XmlType in Definition as when marschlling it, there is an exception:Instance of "org.eclipse.toscaModel.Definitions" is substituting "org.eclipse.toscaModel.TDefinitions", but "org.eclipse.toscaModel.Definitions" is bound to an anonymous type.]
//by following this post found the reason, http://stackoverflow.com/questions/21117879/com-sun-istack-saxexception2-instance-is-substituting-java-lang-object

