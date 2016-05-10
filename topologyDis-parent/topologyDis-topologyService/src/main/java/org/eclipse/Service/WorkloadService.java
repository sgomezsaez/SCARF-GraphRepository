package org.eclipse.Service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.output.others.WorkloadList;
import org.eclipse.output.others.WorkloadList.WorkloadWithDatabaseID;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.WorkloadDao;
import org.eclipse.topology.domain.Workload;
import org.eclipse.transformer.TopologyStatistics.PerformanceTransformer;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class WorkloadService {
	    	
	    //Get DaoFactory
		private final DaoFactory daoFactory = new DaoFactory();
		//Get NodeTypeDao
		private final WorkloadDao workloadDao = daoFactory.getWorkloadDao();
		GraphDatabaseService db = DbCon.GetDbConnect();
		
		public Node create(Workload workload) {
			Node node = workloadDao.create(workload);			
			return node;
		}

		public Workload get(String id) {
			Node node = workloadDao.getWorkloadByWorkLoadID(id);//the input may be string 
			if(node == null){
			  try{
			     long databaseID = Long.parseLong(id, 10); //the input may be digital 
				 node = workloadDao.getNodeById(databaseID);
			  }
			   catch(NumberFormatException e){
				return null;
			   }
			}

			Workload workload = null;
			if(node!=null){
				  Iterator<Label> labelIterator = workloadDao.getNodeLabelById(node.getId());
				  while(labelIterator.hasNext()){
					Label onelabel = labelIterator.next();
					if(onelabel.name().equals("workload")){
						workload = workloadDao.getWorkload(node);
						break;
					}
				  }

				}
		
			return workload;
		}
		
		public boolean delete(long id) {
			Node node = workloadDao.getNodeById(id);
			if(node!=null){
			workloadDao.deleteNodeById(id);		
			return true;
			}
			return false;
		}
		

		public WorkloadList getall() {

			WorkloadList workloadHistoryList = new WorkloadList();
			List<Node>allWorkLoadNodes = workloadDao.getAllWorkloadNodes();
			List<WorkloadWithDatabaseID> workloadList = workloadHistoryList.getWorkloadWithDatabaseID();
			for(Node oneHistory: allWorkLoadNodes){
		    	
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

}
