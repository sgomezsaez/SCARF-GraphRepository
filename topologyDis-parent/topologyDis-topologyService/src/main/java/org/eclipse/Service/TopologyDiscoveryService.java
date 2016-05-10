package org.eclipse.Service;

import java.util.ArrayList;
import java.util.List;














import org.eclipse.Service.TopologyExplorer.MuTopologyExplorer;
import org.eclipse.output.TopologyOutput.MuTopologyOutput;
import org.eclipse.output.TopologyOutput.MuTopologyOutput.Specification;
import org.eclipse.topology.dao.AlphaTopologyIndexDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.InstanceNodeDao;
import org.eclipse.topology.dao.RelationDao;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TDefinitions;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class TopologyDiscoveryService {

	private long alphaTopologyId;
	private MuTopologyExplorer muTopologyExplorer;
	public List<Definitions> muTopologyDefinitions;
	private final DaoFactory daoFactory = new DaoFactory();
	private final AlphaTopologyIndexDao alphaTopologyIndexDao = daoFactory.getAlphaTopologyIndexDao();

	public TopologyDiscoveryService(long id) throws NotFoundException, NullPointerException, PropertyNotFoundException{
		this.alphaTopologyId = id;
		NotFoundException e = new NotFoundException();
		boolean result = alphaTopologyIndexDao.isAlphaTopology(this.alphaTopologyId);
        if(!result){
        	throw  e;
        }
		muTopologyExplorer = new MuTopologyExplorer();
		muTopologyExplorer.setAlphaTopologyId(this.alphaTopologyId);
	    muTopologyExplorer.initGammaTopologyExplorer();
	    muTopologyDefinitions= muTopologyExplorer.getMuTopologyDefinition();
	}



    public MuTopologyOutput getMuTopologiesTest(){
    	MuTopologyOutput muTopologies = new MuTopologyOutput();
   	    List<MuTopologyOutput.Specification> speList = muTopologies.getSpecification();
    	for(int i=0;i<this.muTopologyDefinitions.size();i++){
    	Specification newspec = new MuTopologyOutput.Specification();
        newspec.setDefinitions(this.muTopologyDefinitions.get(i));
        newspec.setId(i+1);
        speList.add(newspec);
    	}

    	//muTopologies.setDefinitions(muTopologyDefinitions);
    	//muTopologies.setOneDefinition(muTopologyDefinitions.get(0));
    	return muTopologies;
    }


}
