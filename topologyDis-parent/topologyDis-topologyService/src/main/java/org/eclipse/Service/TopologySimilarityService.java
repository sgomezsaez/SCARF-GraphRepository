package org.eclipse.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.Service.TopologyExplorer.TopologySimilarityExplorer;
import org.eclipse.output.TopologyOutput.AlphaTopologySimilarityList;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.NodeDao;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class TopologySimilarityService {
	private final DaoFactory daoFactory = new DaoFactory();
	private final NodeDao nodeDao = daoFactory.getNodeDao();
	private long alphaTopologyId;
	private Unmarshaller um = JAXBSupport.createUnmarshaller();
	public TopologySimilarityService(long id){
		this.alphaTopologyId = id;
	}

	public AlphaTopologySimilarityList getSimilarTopologList() throws JAXBException, NullPointerException, PropertyNotFoundException{
		AlphaTopologySimilarityList alphaTopologySimilarityList = new AlphaTopologySimilarityList();
		TopologySimilarityExplorer exploer = new TopologySimilarityExplorer(this.alphaTopologyId);
		   exploer.startExploringSimilarTopology();
		   List<Long> result = exploer.getFoundedSimilarTopology();
		   if(result.isEmpty()){
			   return null;
		   }
		   List<AlphaTopologySimilarityList.Specification> speList = alphaTopologySimilarityList.getSpecification();

		   for(int i=0;i<result.size();i++){
			  Node oneNode = nodeDao.getNodeById(result.get(i));
			  String specification = nodeDao.getSpecification(oneNode);
				AlphaTopologySimilarityList.Specification newspec = new AlphaTopologySimilarityList.Specification();
				InputStream is = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_16));
				Definitions newDef = (Definitions) um.unmarshal(is);
		        newspec.setDefinitions(newDef);
		        newspec.setId(result.get(i));
		        speList.add(newspec);
		   }
		return alphaTopologySimilarityList;
	}

	  public static void main(String [] args) throws JAXBException, NullPointerException, PropertyNotFoundException
	  {
		  TopologySimilarityService topologySimilarityService = new TopologySimilarityService(3);
		  AlphaTopologySimilarityList list = topologySimilarityService.getSimilarTopologList();
	  }

}
