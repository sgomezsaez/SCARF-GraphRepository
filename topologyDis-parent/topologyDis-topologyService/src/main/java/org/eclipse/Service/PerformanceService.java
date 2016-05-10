package org.eclipse.Service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.output.others.PerformanceList;
import org.eclipse.output.others.PerformanceList.PerformanceWithDatabaseID;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.PerformanceDao;
import org.eclipse.topology.domain.Performance;
import org.eclipse.transformer.TopologyStatistics.PerformanceTransformer;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.PropertyNotFoundException;

public class PerformanceService {

	    //Get DaoFactory
		private final DaoFactory daoFactory = new DaoFactory();
		//Get NodeTypeDao
		private final PerformanceDao performanceDao = daoFactory.getPerformanceDao();

		private String performanceSpecification = null;


		public String getPerformanceSpecification() {
			return performanceSpecification;
		}

		public void setPerformanceSpecification(String performanceSpecification) {
			this.performanceSpecification = performanceSpecification;
		}

		public Node create(Performance performance) {
			Node node = performanceDao.create(performance);
			performanceDao.setSpecification(this.performanceSpecification, node);
			return node;
		}

		public Performance get(String id) throws JAXBException, NullPointerException, PropertyNotFoundException {
			Node node = performanceDao.getPerformanceByPerformanceID(id);//the input may be string
			if(node == null){
				  try{
				     long databaseID = Long.parseLong(id, 10); //the input may be digital
						node = performanceDao.getNodeById(databaseID);
				  }
				   catch(NumberFormatException e){
					return null;
				   }
				}

			Performance performance = null;
			if(node!=null){
			  Iterator<Label> labelIterator = performanceDao.getNodeLabelById(node.getId());
			  while(labelIterator.hasNext()){
				Label onelabel = labelIterator.next();
				if(onelabel.name().equals("performance")){

					String specification = performanceDao.getSpecification(node);
		            //Lets get the performance in a quick way, my god, who define the performance>?!!!!
					InputStream stream = new ByteArrayInputStream(specification.getBytes(StandardCharsets.UTF_8));
					PerformanceTransformer performanceTransformer = new PerformanceTransformer(stream);
					performance = performanceTransformer.getPerformance();
					break;
				}
			  }

			}
			return performance;
		}

		public boolean delete(long id) {
			Node node = performanceDao.getNodeById(id);
			if(node!=null){
			performanceDao.deletePerformanceById(id);
			return true;
			}
			return false;
		}

		public PerformanceList getall() throws NullPointerException, PropertyNotFoundException {
			// TODO Auto-generated method stub
			PerformanceList performanceHistoryList = new PerformanceList();
			List<PerformanceWithDatabaseID> performanceList = performanceHistoryList.getPerformanceWithDatabaseID();
			List<Node> performanceNodes = performanceDao.getAllPerformanceNodes();
			for(Node oneHistory: performanceNodes){
		    		Performance performance = new Performance();
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

		return performanceHistoryList;
	}



}
