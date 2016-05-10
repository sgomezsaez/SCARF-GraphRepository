package org.eclipse.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.Service.MuTopologyService;
import org.eclipse.topology.domain.MuTopology;
import org.eclipse.transformer.Topology.MuTopologyTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.neo4j.graphdb.Node;

/**
 * this class is used for REST API of viable topology
 * @author Hao
 *
 */
@Path("viabletopology")
public class MuTopologyResource {

	public MuTopologyResource(){
	}

	/**
	 * Persist one Mu-topology topology
	 *
	 * @param is inputStream of XML
	 * @return
	 * @throws IOException
	 */
	@POST
	@Consumes("application/xml")
	public Response createMuTopology(InputStream is) throws IOException {

		MuTopology muTopology = null;
		try {
			@SuppressWarnings("rawtypes")
			TopologyTransformer transformer = new MuTopologyTransformer(is);
			muTopology = (MuTopology) transformer.getDomainType();
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MuTopologyService muTopologyService = new MuTopologyService();
		Node muTopologyNode = muTopologyService.AddMuTopology(muTopology);
	    return Response.created(URI.create(("/mutopology/")+muTopologyNode.getId())).build();
	  }

}