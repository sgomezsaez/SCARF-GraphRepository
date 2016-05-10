package org.eclipse.Service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.topology.dao.AlphaTopologyIndexDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.HistoryDao;
import org.eclipse.topology.dao.InstanceNodeDao;
import org.eclipse.topology.dao.MuTopologyDao;
import org.eclipse.topology.dao.PerformanceDao;
import org.eclipse.topology.dao.RelationDao;
import org.eclipse.topology.dao.WorkloadDao;
import org.eclipse.topology.domain.AlphaTopology;
import org.eclipse.topology.domain.MuTopology;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public class MuTopologyService {

	//Get DaoFactory
	private final DaoFactory daoFactory = new DaoFactory();
	private final MuTopologyDao muTopologyDao = daoFactory.getMuTopologyDao();

	public enum MuTopologyRelationships implements RelationshipType{
		HAS_MUTOPOLOGY;
	}
	
	GraphDatabaseService db = DbCon.GetDbConnect();
	
	public Node AddMuTopology(MuTopology muTopology){

		Node muTopologyNode = muTopologyDao.create(muTopology);
		Node alphaTopologyIndex = muTopologyDao.findAlphaTopologybyMu(muTopologyNode);
	    muTopologyDao.maintainMuTopology(muTopologyNode,alphaTopologyIndex);
		
		return muTopologyNode;
	}
	
}
