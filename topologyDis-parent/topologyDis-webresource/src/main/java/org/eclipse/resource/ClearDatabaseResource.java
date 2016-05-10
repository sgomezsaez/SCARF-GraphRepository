package org.eclipse.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

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
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.eclipse.transformer.Topology.AbstractSkeletonTransformer;
import org.eclipse.transformer.Topology.TopologyTransformer;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
/**
 *
 * @author Hao
 * This class is used to clear completely the database, use it only when neccessary
 */

@Path("database/clear")
public class ClearDatabaseResource {

	public ClearDatabaseResource(){
	}

	@POST
	@Consumes("application/xml")
	public String createAbstractSubTopology(InputStream is) throws IOException {
		GraphDatabaseService db = DbCon.GetDbConnect();
		  String query = "match (n) OPTIONAL MATCH (n)-[r]-() delete n,r";

			try ( Transaction tx = db.beginTx();Result result = db.execute(query);){
		    	    tx.success();
			}
	   return "DataBase is completely cleared!";

	  }

}
