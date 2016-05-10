package org.eclipse.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput.Specification;
import org.eclipse.output.TopologyOutput.ViableTopologyList;
import org.eclipse.output.TopologyOutput.ViableTopologyList.ViableTopologyWithDababaseID;
import org.eclipse.output.TopologyOutput.ViableTopologyOutput;
import org.eclipse.output.others.PerformanceList;
import org.eclipse.output.others.PerformanceList.PerformanceWithDatabaseID;
import org.eclipse.output.others.WorkloadList;
import org.eclipse.output.others.WorkloadList.WorkloadWithDatabaseID;
import org.eclipse.topology.dao.AlphaTopologyIndexDao;
import org.eclipse.topology.dao.HistoryDao;
import org.eclipse.topology.dao.InstanceNodeDao;
import org.eclipse.topology.dao.MuTopologyDao;
import org.eclipse.topology.dao.PerformanceDao;
import org.eclipse.topology.dao.RelationDao;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.WorkloadDao;
import org.eclipse.topology.domain.AlphaTopology;
import org.eclipse.topology.domain.DomainTemplate;
import org.eclipse.topology.domain.InstanceNode;
import org.eclipse.topology.domain.MuTopology;
import org.eclipse.topology.domain.Performance;
import org.eclipse.topology.domain.RelationshipInstance;
import org.eclipse.topology.domain.Workload;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.transformer.Topology.AlphaTopologyTransformer;
import org.eclipse.transformer.Topology.MuTopologyTransformer;
import org.eclipse.transformer.TopologyStatistics.PerformanceTransformer;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class AlphaTopologyService {

	private List<Node> AlphaTopologyNodeListDb = new  ArrayList<Node>();
	//Get DaoFactory
	private final DaoFactory daoFactory = new DaoFactory();
	private final InstanceNodeDao instanceNodeDao = daoFactory.getInstanceNodeDao();
	private final RelationDao alphaTopologyRelationDao = daoFactory.getRelationDao();
	private final AlphaTopologyIndexDao alphaTopologyIndexDao = daoFactory.getAlphaTopologyIndexDao();
	private final HistoryDao historyDao = daoFactory.getHistoryDao();
	private final WorkloadDao workloadDao = daoFactory.getWorkloadDao();
	private final PerformanceDao performanceDao = daoFactory.getPerformanceDao();
	private final MuTopologyDao muTopologyDao = daoFactory.getMuTopologyDao();

	public enum AlphaTopologyRelationships implements RelationshipType{
		Includes
	}
	GraphDatabaseService db = DbCon.GetDbConnect();

	public Node AddAlphaTopology(AlphaTopology alphaTopology){
		addAlphaTopologyNode(alphaTopology);
		addAlphaTopologyRelation(alphaTopology);
		Node alphaTopologyIndex = addAlphaTopologyIndex(alphaTopology);


		return alphaTopologyIndex;
	}


	public boolean deleteAlphaTopologyById(long alphaTopologyId) throws org.neo4j.graphdb.QueryExecutionException{
		boolean result = false;
		result = alphaTopologyIndexDao.deleteAlphaTopologyById( alphaTopologyId);
		return result;

	}


	public boolean performWorkload(long alphaTopologyId, long workloadId){
		boolean result = false;
		result = alphaTopologyIndexDao.performWorkload( alphaTopologyId,  workloadId);
		return result;

	}

	public boolean performPerformance(long alphaTopologyId, long performanceId) throws java.lang.NullPointerException{
		boolean result = false;
		result = alphaTopologyIndexDao.performPerformance( alphaTopologyId,  performanceId);
		return result;
	}

	public boolean unperformWorkload(long alphaTopologyId, long workloadId) throws NotFoundException{
		boolean result = false;
		result = alphaTopologyIndexDao.unperformWorkload( alphaTopologyId,  workloadId);
		return result;

	}

	public WorkloadList queryWorkloadHistory(long alphaTopologyId, String from, String to) throws org.neo4j.graphdb.QueryExecutionException{
		if(from==null){
		from = "0001-01-01 00:00:00.000";
		}
		if(to==null){
		to = "9999-12-30 12:59:59.000";
		}

		WorkloadList workloadHistoryList = new WorkloadList();
		List<WorkloadWithDatabaseID> workloadList = workloadHistoryList.getWorkloadWithDatabaseID();
		List<Node> workLoadNode = workloadDao.getAllWorkloadNodesForOneTopology(alphaTopologyId);
		    for(Node oneHistory: workLoadNode){
		    	String fromDate = workloadDao.getProperty(oneHistory, "startTime");
		    	String toDate = workloadDao.getProperty(oneHistory, "endTime");
		    	Timestamp employDateTimeStamp = Timestamp.valueOf(fromDate);
		    	Timestamp unemployDateTimeStamp = Timestamp.valueOf(toDate);
		    	Timestamp fromTimeStamp = Timestamp.valueOf(from);
		    	Timestamp toTimeStamp = Timestamp.valueOf(to);

		    	if( fromTimeStamp.before(employDateTimeStamp)&&toTimeStamp.after(unemployDateTimeStamp)){
		    		Workload workload = new Workload();
		    		try ( Transaction tx = db.beginTx();){
		            workload.setArrival((String) oneHistory.getProperty("arrival"));
		            workload.setAvgTransactions((short) oneHistory.getProperty("avg_transactions"));
		            workload.setPattern((String) oneHistory.getProperty("pattern"));
		            workload.setBehavioral((String) oneHistory.getProperty("behavioral"));
		            workload.setAvgUsers((short) oneHistory.getProperty("avg_users"));
		            workload.setId((String) oneHistory.getProperty("id"));
		            workload.setStartTime((String) oneHistory.getProperty("startTime"));
		            workload.setEndTime((String) oneHistory.getProperty("endTime"));
		            WorkloadWithDatabaseID workloadWithDatabaseID = new WorkloadWithDatabaseID();
		            workloadWithDatabaseID.setDatabaseId(oneHistory.getId());
		            workloadWithDatabaseID.setWorkload(workload);
		            workloadList.add(workloadWithDatabaseID);
		    		}
		    	}

		    }
		return workloadHistoryList;

	}

	public PerformanceList queryPerformanceHistory(long alphaTopologyId, String from, String to) throws org.neo4j.graphdb.QueryExecutionException, NullPointerException, PropertyNotFoundException{
		if(from==null){
		from = "0001-01-01 00:00:00.000";
		}
		if(to==null){
		to = "9999-12-30 12:59:59.000";
		}
		PerformanceList performanceHistoryList = new PerformanceList();
		List<PerformanceWithDatabaseID> performanceList = performanceHistoryList.getPerformanceWithDatabaseID();
		List<Node> performanceNodes = performanceDao.getAllPerformanceNodesForOneAlphaTopologyByID(alphaTopologyId);
		    for(Node oneHistory: performanceNodes){
		    	String fromDate = performanceDao.getProperty(oneHistory, "startTime");
		    	String toDate = performanceDao.getProperty(oneHistory, "endTime");
		    	Timestamp employDateTimeStamp = Timestamp.valueOf(fromDate);
		    	Timestamp unemployDateTimeStamp = Timestamp.valueOf(toDate);
		    	Timestamp fromTimeStamp = Timestamp.valueOf(from);
		    	Timestamp toTimeStamp = Timestamp.valueOf(to);
		    	if( fromTimeStamp.before(employDateTimeStamp)&&toTimeStamp.after(unemployDateTimeStamp)){
		    		Performance performance = new Performance();
		    		try ( Transaction tx = db.beginTx();){
		    			String specification = performanceDao.getSpecification(oneHistory);
			            //Lets get the performance in a quick way, my god, who define the performance>?!!!!
						InputStream stream = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
						PerformanceTransformer performanceTransformer = new PerformanceTransformer(stream);
						try {
							performance = performanceTransformer.getPerformance();
							PerformanceWithDatabaseID performanceWithDatabaseID = new PerformanceWithDatabaseID();
							performanceWithDatabaseID.setDatabaseId(oneHistory.getId());
							performanceWithDatabaseID.setPerformance(performance);
							performanceList.add(performanceWithDatabaseID);
						} catch (JAXBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
		    	}

		    }
		return performanceHistoryList;
	}


	public WorkloadList queryAllWorkloadHistory(long alphaTopologyId) throws org.neo4j.graphdb.QueryExecutionException{
		WorkloadList workloadHistoryList = new WorkloadList();
		List<WorkloadWithDatabaseID> workloadList = workloadHistoryList.getWorkloadWithDatabaseID();
		List<Node> workLoadNode = historyDao.getAllWorkloadsHistory(alphaTopologyId);
		    for(Node oneHistory: workLoadNode){


		    		Workload workload = new Workload();
		    		try ( Transaction tx = db.beginTx();){
		            workload.setArrival((String) oneHistory.getProperty("arrival"));
		            workload.setAvgTransactions((short) oneHistory.getProperty("avg_transactions"));
		            workload.setPattern((String) oneHistory.getProperty("pattern"));
		            workload.setBehavioral((String) oneHistory.getProperty("behavioral"));
		            workload.setAvgUsers((short) oneHistory.getProperty("avg_users"));
		            workload.setId((String) oneHistory.getProperty("id"));
		            workload.setStartTime((String) oneHistory.getProperty("startTime"));
		            workload.setEndTime((String) oneHistory.getProperty("endTime"));
		            WorkloadWithDatabaseID workloadWithDatabaseID = new WorkloadWithDatabaseID();
		            workloadWithDatabaseID.setDatabaseId(oneHistory.getId());
		            workloadWithDatabaseID.setWorkload(workload);
		            workloadList.add(workloadWithDatabaseID);
		    		}

		    }
		return workloadHistoryList;

	}


	private Node addAlphaTopologyIndex(AlphaTopology alphaTopology) {
		// TODO Auto-generated method stub
		Node abstractNodeIndexDb = alphaTopologyIndexDao.createIndex(alphaTopology);
		//Connect the index to all abstractskeleton Nodes
		Iterator<Node>it = AlphaTopologyNodeListDb.iterator();
		Node nextNode = null;
		while(it.hasNext()){
			nextNode = it.next();
			alphaTopologyRelationDao.addRelation(abstractNodeIndexDb, nextNode,AlphaTopologyRelationships.Includes.toString(),AlphaTopologyRelationships.Includes.toString());
		}
		return abstractNodeIndexDb;
	}

	private void addAlphaTopologyRelation(AlphaTopology alphaTopology) {
		Iterator<DomainTemplate> iteratorAlphaTopologyNodeOrTopologyRelationship = alphaTopology.getNodeTemplateOrRelationshipTemplate().iterator();
		DomainTemplate nextAlphaTopologyNodeOrTopologyRelationship = null;
	     while (iteratorAlphaTopologyNodeOrTopologyRelationship.hasNext()) {
	    	 nextAlphaTopologyNodeOrTopologyRelationship = iteratorAlphaTopologyNodeOrTopologyRelationship.next();
	    	 if( nextAlphaTopologyNodeOrTopologyRelationship instanceof RelationshipInstance){
	    		 Node sourceNode = findNode( ((RelationshipInstance) nextAlphaTopologyNodeOrTopologyRelationship).getSourceElement().getRef().toString());
	    		 Node targetNode = findNode(((RelationshipInstance) nextAlphaTopologyNodeOrTopologyRelationship).getTargetElement().getRef().toString());
	    		 String relation = nextAlphaTopologyNodeOrTopologyRelationship.getType().getPrefix()+":"+nextAlphaTopologyNodeOrTopologyRelationship.getType().getLocalPart();
	    		 alphaTopologyRelationDao.addRelation(sourceNode, targetNode, relation,relation);
	    	 }
	     }

	}

	private Node findNode(String nodeId){

		Iterator<Node>it = AlphaTopologyNodeListDb.iterator();
		Node nextNode = null;
		while(it.hasNext()){
			nextNode = it.next();
			try ( Transaction tx = db.beginTx()){

			if( nextNode.getProperty("id").equals(nodeId) ){
				return nextNode;
			}
			tx.success();
			}
		}

		return nextNode;

	}



	private void addAlphaTopologyNode(AlphaTopology alphaTopology){


		Iterator<DomainTemplate> iteratorAlphaTopologyNodeOrTopologyRelationship = alphaTopology.getNodeTemplateOrRelationshipTemplate().iterator();
		DomainTemplate nextAlphaTopologyNodeOrTopologyRelationship = null;
	     while (iteratorAlphaTopologyNodeOrTopologyRelationship.hasNext()) {
	    	 nextAlphaTopologyNodeOrTopologyRelationship = iteratorAlphaTopologyNodeOrTopologyRelationship.next();
	    	 if( nextAlphaTopologyNodeOrTopologyRelationship instanceof InstanceNode){
             Node tempNodeDb = instanceNodeDao.create((InstanceNode)nextAlphaTopologyNodeOrTopologyRelationship);
             AlphaTopologyNodeListDb.add(tempNodeDb);
	    	 }
	     }
	}

	public ViableTopologyList queryMuTopologyHistory(long alphaTopologyId, String from, String to) throws NullPointerException, PropertyNotFoundException{
		if(from==null){
		from = "0001-01-01 00:00:00.000";
		}
		if(to==null){
		to = "9999-12-30 12:59:59.999";
		}
		ViableTopologyList viableTopologyList = new ViableTopologyList();
		List<ViableTopologyWithDababaseID> viableTopologyWithDababaseIDList = viableTopologyList.getViableTopologyWithDababaseID();
		List<Node> viablTopologyNodes = muTopologyDao.getAllMuTopologyForOneAlphaTopologyById(alphaTopologyId);
		    for(Node oneHistory: viablTopologyNodes){
		    	String fromDate = muTopologyDao.getProperty(oneHistory, "createDate");
		    	String toDate = muTopologyDao.getProperty(oneHistory, "endDate");
		    	if(toDate.equals("")){
		    		toDate = "9999-12-30 12:59:59.998";
		    	}
		    	Timestamp employDateTimeStamp = Timestamp.valueOf(fromDate);
		    	Timestamp unemployDateTimeStamp = Timestamp.valueOf(toDate);
		    	Timestamp fromTimeStamp = Timestamp.valueOf(from);
		    	Timestamp toTimeStamp = Timestamp.valueOf(to);
		    	if( fromTimeStamp.before(employDateTimeStamp)&&toTimeStamp.after(unemployDateTimeStamp)){

		    		try ( Transaction tx = db.beginTx();){
		    			ViableTopologyWithDababaseID viableTopologyWithDababaseID = new ViableTopologyWithDababaseID();
						ViableTopologyOutput viableTopologyOutput = getViableTopologyOutputFromMuNode(oneHistory);
						viableTopologyWithDababaseID.setDatabaseId(oneHistory.getId());
						viableTopologyWithDababaseID.setViableTopology(viableTopologyOutput);
						viableTopologyWithDababaseIDList.add(viableTopologyWithDababaseID);
		    		}
		    	}

		    }
		return viableTopologyList;
	}

	private ViableTopologyOutput getViableTopologyOutputFromMuNode(Node oneHistory) throws java.lang.NullPointerException, PropertyNotFoundException {
		MuTopology muTopology = new MuTopology();
		ViableTopologyOutput viableTopologyOutput = new ViableTopologyOutput();
		MuTopologyTransformer muTopologyTransformer;
		try {
			muTopologyTransformer = new MuTopologyTransformer();
		muTopology.setAlphaTopologyId(muTopologyDao.getProperty(oneHistory, "alphaTopologyId"));
		muTopology.setAlphaTopologyName(muTopologyDao.getProperty(oneHistory, "alphaTopologyName"));
		muTopology.setAlphaTopologyNameSpace(muTopologyDao.getProperty(oneHistory, "alphaTopologyNameSpace"));
		muTopology.setObsolete(muTopologyDao.getProperty(oneHistory, "obsolete"));
		muTopology.setEndDate(muTopologyDao.getProperty(oneHistory, "endDate"));
		muTopology.setCreateDate(muTopologyDao.getProperty(oneHistory, "createDate"));
		//transfer specification from string to tosca definition
		String specification = muTopologyDao.getSpecification(oneHistory);
		InputStream streamOfSpec = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
		muTopologyTransformer.Unmarshaller(streamOfSpec);
		viableTopologyOutput.setSpecification(muTopologyTransformer.getDefi_type());
		viableTopologyOutput.setAlphaTopologyId(muTopology.getAlphaTopologyId());
		viableTopologyOutput.setAlphaTopologyName(muTopology.getAlphaTopologyName());
		viableTopologyOutput.setAlphaTopologyNameSpace(muTopology.getAlphaTopologyNameSpace());
		viableTopologyOutput.setCreateDate(muTopology.getCreateDate());
		viableTopologyOutput.setEndDate(muTopology.getEndDate());
		viableTopologyOutput.setObsolete(muTopology.getObsolete());
		} catch (InputWrongType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return viableTopologyOutput;
	}

	/*public MuTopologies queryMuTopologyHistory(long alphaTopologyId, String from, String to){
		if(from==null){
		from = "0001-01-01 00:00:00.000";
		}
		if(to==null){
		to = "9999-12-30 12:59:59.000";
		}
		MuTopologies viableTopologyList = new MuTopologies();
		List<Specification> viableTopologyWithDababaseIDList = viableTopologyList.getSpecification();
		List<Node> viablTopologyNodes = muTopologyDao.getAllMuTopologyForOneAlphaTopologyById(alphaTopologyId);
		    for(Node oneHistory: viablTopologyNodes){
		    	String fromDate = muTopologyDao.getProperty(oneHistory, "createDate");
		    	String toDate = muTopologyDao.getProperty(oneHistory, "endDate");
		    	if(toDate.equals("")){
		    		toDate = "9999-12-30 12:59:59.999";
		    	}
		    	Timestamp employDateTimeStamp = Timestamp.valueOf(fromDate);
		    	Timestamp unemployDateTimeStamp = Timestamp.valueOf(toDate);
		    	Timestamp fromTimeStamp = Timestamp.valueOf(from);
		    	Timestamp toTimeStamp = Timestamp.valueOf(to);
		    	if( fromTimeStamp.before(employDateTimeStamp)&&toTimeStamp.after(unemployDateTimeStamp)){

		    		try ( Transaction tx = db.beginTx();){
		    			String specification = muTopologyDao.getSpecification(oneHistory);
						InputStream stream = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
						MuTopologyTransformer muTopologyTransformer = new MuTopologyTransformer(stream);
						try {
							MuTopology muTopology = muTopologyTransformer.getDomainType();
							Specification spec = new Specification();
							//transfer specification from string to tosca definition
							InputStream streamOfSpec = new ByteArrayInputStream(muTopology.getSpecification().getBytes(StandardCharsets.UTF_16));
							//muTopologyTransformer.Unmarshaller(streamOfSpec);
							AlphaTopologyTransformer transformer = new AlphaTopologyTransformer(streamOfSpec);
							Definitions tempDef = transformer.getAlphaTopologyDefinition();
							spec.setDefinitions(tempDef);
							Marshaller am = JAXBSupport.createMarshaller(true);
							//Definitions oneDef = muTopologyTransformer.getDefi_type();
					    	try {
								am.marshal(tempDef, System.out);
							} catch (JAXBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							spec.setId(1);
							viableTopologyWithDababaseIDList.add(spec);
						} catch (InputWrongType e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		} catch (InputWrongType e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}

		    }
		return viableTopologyList;
	}*/


	public static void main(String [] args) throws NullPointerException, PropertyNotFoundException{
		AlphaTopologyService service = new AlphaTopologyService();
		service.queryMuTopologyHistory(353, "2015-11-26 00:00:00.000", "2015-12-26 23:59:59.000");
	}


	public ViableTopologyOutput getOneViableTopology(long alphaTopologyId,
			long viableTopologId) throws java.lang.NullPointerException, PropertyNotFoundException{
		ViableTopologyOutput viableTopologyOutput = new ViableTopologyOutput();
	    Node oneMu = muTopologyDao.getOneMuTopologyForOneAlphaTopologyById(alphaTopologyId,viableTopologId);
	    viableTopologyOutput = getViableTopologyOutputFromMuNode(oneMu);
		return viableTopologyOutput;
	}

	public AlphaTopologyOutput getAllAlphaTopology() throws NullPointerException, PropertyNotFoundException{
		AlphaTopologyOutput alphaTopologyOutput = new AlphaTopologyOutput();
		List<AlphaTopologyOutput.Specification>specificaiton = alphaTopologyOutput.getSpecification();
		List<Long> allAlphaTopologyIndex = alphaTopologyIndexDao.getAllAlphaTopologyIndex();
		for(long oneId:allAlphaTopologyIndex){
			Definitions alphaTopologyDefinitions = null;
			Node indexNode = alphaTopologyIndexDao.getNodeById(oneId);
			String specification = alphaTopologyIndexDao.getSpecification(indexNode);
			InputStream streamOfSpec = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_16));
			AlphaTopologyTransformer alphaTopologyTransformer = new AlphaTopologyTransformer(streamOfSpec);
			alphaTopologyDefinitions = alphaTopologyTransformer.getToscaDefinition();
			Specification newSpec = new Specification();
			newSpec.setId(indexNode.getId());
			newSpec.setDefinitions(alphaTopologyDefinitions);
			specificaiton.add(newSpec);
		}
		return alphaTopologyOutput;
	}

	public Definitions getAlphaTopologyDefinitionById(long id) throws NullPointerException, PropertyNotFoundException{
		List<Long> allAlphaTopologyIndex = alphaTopologyIndexDao.getAllAlphaTopologyIndex();
		Definitions alphaTopologyDefinitions = null;
		for(long oneId:allAlphaTopologyIndex){
			if(id==oneId){
				Node indexNode = alphaTopologyIndexDao.getNodeById(oneId);
				String specification = alphaTopologyIndexDao.getSpecification(indexNode);
				InputStream streamOfSpec = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_16));
				AlphaTopologyTransformer alphaTopologyTransformer = new AlphaTopologyTransformer(streamOfSpec);
				alphaTopologyDefinitions = alphaTopologyTransformer.getToscaDefinition();
				break;
			}
		}
		return alphaTopologyDefinitions;

	}

}
