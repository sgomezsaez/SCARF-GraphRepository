package org.eclipse.topology.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.NodeDao;
import org.eclipse.topology.domain.Performance;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class PerformanceDao extends NodeDao{

	GraphDatabaseService db = DbCon.GetDbConnect();

	public enum performanceLabel implements Label {
		performance,topologyStatistics,time_behaviour,capacity,resource_utilization,scalability,availability;
	}
	public enum timeBehaviourLabel implements Label {
		response_time,throughput,processing_time,avg_read_speed,avg_write_speed,avg_migration_time,latency,backup_time;
	}
	public enum capacityLabel implements Label {
		bandwith,processor_speed,storage_size,memory_allocation_vm,number_vm,number_processors,io_operations;
	}

	public enum resourceUtilizationLabel implements Label {
		network_utilization,memory_utilization,disk_utilization,cpu_utilization,vm_utilization,number_vm_perserver;
	}

	public enum scalabilityLabel implements Label {
		resource_acquisition_time,resource_provisioning_time,deployment_time,resource_release_time,vm_startup_time;
	}

	public enum availabilityLabel implements Label {
		cloud_service_uptime,cloud_resource_uptime,meantime_between_failures,meantime_repair;
	}

	public enum performanceRelationships implements RelationshipType{
		HAS_TIME_BEHAVIOUR,HAS_CAPACITY,HAS_RESOURCE_UTILIZATION,HAS_SCALABILITY,HAS_AVAILABILITY;
	}

	public void setSpecification(String spe,Node node){
		try ( Transaction tx = db.beginTx();){
			node.setProperty("specification", spe);
			tx.success();
		}

	}

	public Node create(Performance performance){
		Node performanceDB = null;

		try ( Transaction tx = db.beginTx();){
			performanceDB = db.createNode(performanceLabel.performance);
            performanceDB.setProperty("id", performance.getId());
            performanceDB.setProperty("startTime", performance.getStartTime());
            performanceDB.setProperty("endTime", performance.getEndTime());
            //time_behaviour
    		Node response_timeDB = null;
    		Node throughputDB = null;
    		Node processing_timeDB = null;
    		Node avg_read_speedDB = null;
    		Node avg_write_speedDB = null;
    		Node avg_migration_timeDB = null;
    		Node latencyDB = null;
    		Node backup_timeDB = null;
            response_timeDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.response_time,performanceLabel.topologyStatistics);
            response_timeDB.setProperty("min", performance.getTimeBehaviour().getResponseTime().getMin());
            response_timeDB.setProperty("max", performance.getTimeBehaviour().getResponseTime().getMax());
            response_timeDB.setProperty("avg", performance.getTimeBehaviour().getResponseTime().getAvg());
            response_timeDB.setProperty("st", performance.getTimeBehaviour().getResponseTime().getSt());

            throughputDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.throughput,performanceLabel.topologyStatistics);
            throughputDB.setProperty("min", performance.getTimeBehaviour().getThroughput().getMin());
            throughputDB.setProperty("max", performance.getTimeBehaviour().getThroughput().getMax());
            throughputDB.setProperty("avg", performance.getTimeBehaviour().getThroughput().getAvg());
            throughputDB.setProperty("st", performance.getTimeBehaviour().getThroughput().getSt());

            processing_timeDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.processing_time,performanceLabel.topologyStatistics);
            processing_timeDB.setProperty("min", performance.getTimeBehaviour().getProcessingTime().getMin());
            processing_timeDB.setProperty("max", performance.getTimeBehaviour().getProcessingTime().getMax());
            processing_timeDB.setProperty("avg", performance.getTimeBehaviour().getProcessingTime().getAvg());
            processing_timeDB.setProperty("st", performance.getTimeBehaviour().getProcessingTime().getSt());

            avg_read_speedDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.avg_read_speed,performanceLabel.topologyStatistics);
            avg_read_speedDB.setProperty("min", performance.getTimeBehaviour().getAvgReadSpeed().getMin());
            avg_read_speedDB.setProperty("max", performance.getTimeBehaviour().getAvgReadSpeed().getMax());
            avg_read_speedDB.setProperty("avg", performance.getTimeBehaviour().getAvgReadSpeed().getAvg());
            avg_read_speedDB.setProperty("st", performance.getTimeBehaviour().getAvgReadSpeed().getSt());

            avg_write_speedDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.avg_write_speed,performanceLabel.topologyStatistics);
            avg_write_speedDB.setProperty("min", performance.getTimeBehaviour().getAvgWriteSpeed().getMin());
            avg_write_speedDB.setProperty("max", performance.getTimeBehaviour().getAvgWriteSpeed().getMax());
            avg_write_speedDB.setProperty("avg", performance.getTimeBehaviour().getAvgWriteSpeed().getAvg());
            avg_write_speedDB.setProperty("st", performance.getTimeBehaviour().getAvgWriteSpeed().getSt());

            avg_migration_timeDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.avg_migration_time,performanceLabel.topologyStatistics);
            avg_migration_timeDB.setProperty("min", performance.getTimeBehaviour().getAvgMigrationTime().getMin());
            avg_migration_timeDB.setProperty("max", performance.getTimeBehaviour().getAvgMigrationTime().getMax());
            avg_migration_timeDB.setProperty("avg", performance.getTimeBehaviour().getAvgMigrationTime().getAvg());
            avg_migration_timeDB.setProperty("st", performance.getTimeBehaviour().getAvgMigrationTime().getSt());

            latencyDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.latency,performanceLabel.topologyStatistics);
            latencyDB.setProperty("min", performance.getTimeBehaviour().getLatency().getMin());
            latencyDB.setProperty("max", performance.getTimeBehaviour().getLatency().getMax());
            latencyDB.setProperty("avg", performance.getTimeBehaviour().getLatency().getAvg());
            latencyDB.setProperty("st", performance.getTimeBehaviour().getLatency().getSt());

            backup_timeDB = db.createNode(performanceLabel.time_behaviour,timeBehaviourLabel.backup_time,performanceLabel.topologyStatistics);
            backup_timeDB.setProperty("min", performance.getTimeBehaviour().getBackupTime().getMin());
            backup_timeDB.setProperty("max", performance.getTimeBehaviour().getBackupTime().getMax());
            backup_timeDB.setProperty("avg", performance.getTimeBehaviour().getBackupTime().getAvg());
            backup_timeDB.setProperty("st", performance.getTimeBehaviour().getBackupTime().getSt());

            performanceDB.createRelationshipTo(response_timeDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(throughputDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(processing_timeDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(avg_read_speedDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(avg_write_speedDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(avg_migration_timeDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(latencyDB,performanceRelationships.HAS_TIME_BEHAVIOUR);
            performanceDB.createRelationshipTo(backup_timeDB,performanceRelationships.HAS_TIME_BEHAVIOUR);

            //capacity
    		Node bandwithDB = null;
    		Node processor_speedDB = null;
    		Node storage_sizeDB = null;
    		Node memory_allocation_vmDB = null;
    		Node number_vmDB = null;
    		Node number_processorsDB = null;
    		Node io_operationsDB = null;
    		bandwithDB = db.createNode(performanceLabel.capacity,capacityLabel.bandwith,performanceLabel.topologyStatistics);
    		bandwithDB.setProperty("min", performance.getCapacity().getBandwith().getMin());
    		bandwithDB.setProperty("max", performance.getCapacity().getBandwith().getMax());
    		bandwithDB.setProperty("avg", performance.getCapacity().getBandwith().getAvg());
    		bandwithDB.setProperty("st", performance.getCapacity().getBandwith().getSt());

    		processor_speedDB = db.createNode(performanceLabel.capacity,capacityLabel.processor_speed,performanceLabel.topologyStatistics);
    		processor_speedDB.setProperty("min", performance.getCapacity().getProcessorSpeed().getMin());
    		processor_speedDB.setProperty("max", performance.getCapacity().getProcessorSpeed().getMax());
    		processor_speedDB.setProperty("avg", performance.getCapacity().getProcessorSpeed().getAvg());
    		processor_speedDB.setProperty("st", performance.getCapacity().getProcessorSpeed().getSt());

    		storage_sizeDB = db.createNode(performanceLabel.capacity,capacityLabel.storage_size,performanceLabel.topologyStatistics);
    		storage_sizeDB.setProperty("min", performance.getCapacity().getStorageSize().getMin());
    		storage_sizeDB.setProperty("max", performance.getCapacity().getStorageSize().getMax());
    		storage_sizeDB.setProperty("avg", performance.getCapacity().getStorageSize().getAvg());
    		storage_sizeDB.setProperty("st", performance.getCapacity().getStorageSize().getSt());

    		memory_allocation_vmDB = db.createNode(performanceLabel.capacity,capacityLabel.memory_allocation_vm,performanceLabel.topologyStatistics);
    		memory_allocation_vmDB.setProperty("min", performance.getCapacity().getMemoryAllocationVm().getMin());
    		memory_allocation_vmDB.setProperty("max", performance.getCapacity().getMemoryAllocationVm().getMax());
    		memory_allocation_vmDB.setProperty("avg", performance.getCapacity().getMemoryAllocationVm().getAvg());
    		memory_allocation_vmDB.setProperty("st", performance.getCapacity().getMemoryAllocationVm().getSt());

    		number_vmDB = db.createNode(performanceLabel.capacity,capacityLabel.number_vm,performanceLabel.topologyStatistics);
    		number_vmDB.setProperty("min", performance.getCapacity().getNumberVm().getMin());
    		number_vmDB.setProperty("max", performance.getCapacity().getNumberVm().getMax());
    		number_vmDB.setProperty("avg", performance.getCapacity().getNumberVm().getAvg());
    		number_vmDB.setProperty("st", performance.getCapacity().getNumberVm().getSt());

    		number_processorsDB = db.createNode(performanceLabel.capacity,capacityLabel.number_processors,performanceLabel.topologyStatistics);
    		number_processorsDB.setProperty("min", performance.getCapacity().getNumberProcessors().getMin());
    		number_processorsDB.setProperty("max", performance.getCapacity().getNumberProcessors().getMax());
    		number_processorsDB.setProperty("avg", performance.getCapacity().getNumberProcessors().getAvg());
    		number_processorsDB.setProperty("st", performance.getCapacity().getNumberProcessors().getSt());

    		io_operationsDB = db.createNode(performanceLabel.capacity,capacityLabel.io_operations,performanceLabel.topologyStatistics);
    		io_operationsDB.setProperty("min", performance.getCapacity().getIoOperations().getMin());
    		io_operationsDB.setProperty("max", performance.getCapacity().getIoOperations().getMax());
    		io_operationsDB.setProperty("avg", performance.getCapacity().getIoOperations().getAvg());
    		io_operationsDB.setProperty("st", performance.getCapacity().getIoOperations().getSt());

            performanceDB.createRelationshipTo(bandwithDB,performanceRelationships.HAS_CAPACITY);
            performanceDB.createRelationshipTo(processor_speedDB,performanceRelationships.HAS_CAPACITY);
            performanceDB.createRelationshipTo(storage_sizeDB,performanceRelationships.HAS_CAPACITY);
            performanceDB.createRelationshipTo(memory_allocation_vmDB,performanceRelationships.HAS_CAPACITY);
            performanceDB.createRelationshipTo(number_vmDB,performanceRelationships.HAS_CAPACITY);
            performanceDB.createRelationshipTo(number_processorsDB,performanceRelationships.HAS_CAPACITY);
            performanceDB.createRelationshipTo(io_operationsDB,performanceRelationships.HAS_CAPACITY);


            //resource_utilization
      		Node network_utilizationDB = null;
    		Node memory_utilizationDB = null;
    		Node disk_utilizationDB = null;
    		Node cpu_utilizationDB = null;
    		Node vm_utilizationDB = null;
    		Node number_vm_perserverDB = null;

    		network_utilizationDB = db.createNode(performanceLabel.resource_utilization,resourceUtilizationLabel.network_utilization,performanceLabel.topologyStatistics);
    		network_utilizationDB.setProperty("min", performance.getResourceUtilization().getNetworkUtilization().getMin());
    		network_utilizationDB.setProperty("max", performance.getResourceUtilization().getNetworkUtilization().getMax());
    		network_utilizationDB.setProperty("avg", performance.getResourceUtilization().getNetworkUtilization().getAvg());
    		network_utilizationDB.setProperty("st", performance.getResourceUtilization().getNetworkUtilization().getSt());

    		memory_utilizationDB = db.createNode(performanceLabel.resource_utilization,resourceUtilizationLabel.memory_utilization,performanceLabel.topologyStatistics);
    		memory_utilizationDB.setProperty("min", performance.getResourceUtilization().getMemoryUtilization().getMin());
    		memory_utilizationDB.setProperty("max", performance.getResourceUtilization().getMemoryUtilization().getMax());
    		memory_utilizationDB.setProperty("avg", performance.getResourceUtilization().getMemoryUtilization().getAvg());
    		memory_utilizationDB.setProperty("st", performance.getResourceUtilization().getMemoryUtilization().getSt());

    		disk_utilizationDB = db.createNode(performanceLabel.resource_utilization,resourceUtilizationLabel.disk_utilization,performanceLabel.topologyStatistics);
    		disk_utilizationDB.setProperty("min", performance.getResourceUtilization().getDiskUtilization().getMin());
    		disk_utilizationDB.setProperty("max", performance.getResourceUtilization().getDiskUtilization().getMax());
    		disk_utilizationDB.setProperty("avg", performance.getResourceUtilization().getDiskUtilization().getAvg());
    		disk_utilizationDB.setProperty("st", performance.getResourceUtilization().getDiskUtilization().getSt());

    		cpu_utilizationDB = db.createNode(performanceLabel.resource_utilization,resourceUtilizationLabel.cpu_utilization,performanceLabel.topologyStatistics);
    		cpu_utilizationDB.setProperty("min", performance.getResourceUtilization().getCpuUtilization().getMin());
    		cpu_utilizationDB.setProperty("max", performance.getResourceUtilization().getCpuUtilization().getMax());
    		cpu_utilizationDB.setProperty("avg", performance.getResourceUtilization().getCpuUtilization().getAvg());
    		cpu_utilizationDB.setProperty("st", performance.getResourceUtilization().getCpuUtilization().getSt());

    		vm_utilizationDB = db.createNode(performanceLabel.resource_utilization,resourceUtilizationLabel.vm_utilization,performanceLabel.topologyStatistics);
    		vm_utilizationDB.setProperty("min", performance.getResourceUtilization().getVmUtilization().getMin());
    		vm_utilizationDB.setProperty("max", performance.getResourceUtilization().getVmUtilization().getMax());
    		vm_utilizationDB.setProperty("avg", performance.getResourceUtilization().getVmUtilization().getAvg());
    		vm_utilizationDB.setProperty("st", performance.getResourceUtilization().getVmUtilization().getSt());

    		number_vm_perserverDB = db.createNode(performanceLabel.resource_utilization,resourceUtilizationLabel.number_vm_perserver,performanceLabel.topologyStatistics);
    		number_vm_perserverDB.setProperty("min", performance.getResourceUtilization().getNumberVmPerserver().getMin());
    		number_vm_perserverDB.setProperty("max", performance.getResourceUtilization().getNumberVmPerserver().getMax());
    		number_vm_perserverDB.setProperty("avg", performance.getResourceUtilization().getNumberVmPerserver().getAvg());
    		number_vm_perserverDB.setProperty("st", performance.getResourceUtilization().getNumberVmPerserver().getSt());

            performanceDB.createRelationshipTo(network_utilizationDB,performanceRelationships.HAS_RESOURCE_UTILIZATION);
            performanceDB.createRelationshipTo(memory_utilizationDB,performanceRelationships.HAS_RESOURCE_UTILIZATION);
            performanceDB.createRelationshipTo(disk_utilizationDB,performanceRelationships.HAS_RESOURCE_UTILIZATION);
            performanceDB.createRelationshipTo(cpu_utilizationDB,performanceRelationships.HAS_RESOURCE_UTILIZATION);
            performanceDB.createRelationshipTo(vm_utilizationDB,performanceRelationships.HAS_RESOURCE_UTILIZATION);
            performanceDB.createRelationshipTo(number_vm_perserverDB,performanceRelationships.HAS_RESOURCE_UTILIZATION);


            //scalability
      		Node resource_acquisition_timeDB = null;
    		Node resource_provisioning_timeDB = null;
    		Node deployment_timeDB = null;
    		Node resource_release_timeDB = null;
    		Node vm_startup_timeDB = null;

    		resource_acquisition_timeDB = db.createNode(performanceLabel.scalability,scalabilityLabel.resource_acquisition_time,performanceLabel.topologyStatistics);
    		resource_acquisition_timeDB.setProperty("min", performance.getScalability().getResourceAcquisitionTime().getMin());
    		resource_acquisition_timeDB.setProperty("max", performance.getScalability().getResourceAcquisitionTime().getMax());
    		resource_acquisition_timeDB.setProperty("avg", performance.getScalability().getResourceAcquisitionTime().getAvg());
    		resource_acquisition_timeDB.setProperty("st", performance.getScalability().getResourceAcquisitionTime().getSt());

    		resource_provisioning_timeDB = db.createNode(performanceLabel.scalability,scalabilityLabel.resource_provisioning_time,performanceLabel.topologyStatistics);
    		resource_provisioning_timeDB.setProperty("min", performance.getScalability().getResourceProvisioningTime().getMin());
    		resource_provisioning_timeDB.setProperty("max", performance.getScalability().getResourceProvisioningTime().getMax());
    		resource_provisioning_timeDB.setProperty("avg", performance.getScalability().getResourceProvisioningTime().getAvg());
    		resource_provisioning_timeDB.setProperty("st", performance.getScalability().getResourceProvisioningTime().getSt());

    		deployment_timeDB = db.createNode(performanceLabel.scalability,scalabilityLabel.deployment_time,performanceLabel.topologyStatistics);
    		deployment_timeDB.setProperty("min", performance.getScalability().getDeploymentTime().getMin());
    		deployment_timeDB.setProperty("max", performance.getScalability().getDeploymentTime().getMax());
    		deployment_timeDB.setProperty("avg", performance.getScalability().getDeploymentTime().getAvg());
    		deployment_timeDB.setProperty("st", performance.getScalability().getDeploymentTime().getSt());

    		resource_release_timeDB = db.createNode(performanceLabel.scalability,scalabilityLabel.resource_release_time,performanceLabel.topologyStatistics);
    		resource_release_timeDB.setProperty("min", performance.getScalability().getResourceReleaseTime().getMin());
    		resource_release_timeDB.setProperty("max", performance.getScalability().getResourceReleaseTime().getMax());
    		resource_release_timeDB.setProperty("avg", performance.getScalability().getResourceReleaseTime().getAvg());
    		resource_release_timeDB.setProperty("st", performance.getScalability().getResourceReleaseTime().getSt());

    		vm_startup_timeDB = db.createNode(performanceLabel.scalability,scalabilityLabel.vm_startup_time,performanceLabel.topologyStatistics);
    		vm_startup_timeDB.setProperty("min", performance.getScalability().getVmStartupTime().getMin());
    		vm_startup_timeDB.setProperty("max", performance.getScalability().getVmStartupTime().getMax());
    		vm_startup_timeDB.setProperty("avg", performance.getScalability().getVmStartupTime().getAvg());
    		vm_startup_timeDB.setProperty("st", performance.getScalability().getVmStartupTime().getSt());

            performanceDB.createRelationshipTo(resource_acquisition_timeDB,performanceRelationships.HAS_SCALABILITY);
            performanceDB.createRelationshipTo(resource_provisioning_timeDB,performanceRelationships.HAS_SCALABILITY);
            performanceDB.createRelationshipTo(deployment_timeDB,performanceRelationships.HAS_SCALABILITY);
            performanceDB.createRelationshipTo(resource_release_timeDB,performanceRelationships.HAS_SCALABILITY);
            performanceDB.createRelationshipTo(vm_startup_timeDB,performanceRelationships.HAS_SCALABILITY);



            //availability
      		Node cloud_service_uptimeDB = null;
    		Node cloud_resource_uptimeDB = null;
    		Node meantime_between_failuresDB = null;
    		Node meantime_repairDB = null;

    		cloud_service_uptimeDB = db.createNode(performanceLabel.availability,availabilityLabel.cloud_service_uptime,performanceLabel.topologyStatistics);
    		cloud_service_uptimeDB.setProperty("min", performance.getAvailability().getCloudServiceUptime().getMin());
    		cloud_service_uptimeDB.setProperty("max", performance.getAvailability().getCloudServiceUptime().getMax());
    		cloud_service_uptimeDB.setProperty("avg", performance.getAvailability().getCloudServiceUptime().getAvg());
    		cloud_service_uptimeDB.setProperty("st", performance.getAvailability().getCloudServiceUptime().getSt());

    		cloud_resource_uptimeDB = db.createNode(performanceLabel.availability,availabilityLabel.cloud_resource_uptime,performanceLabel.topologyStatistics);
    		cloud_resource_uptimeDB.setProperty("min", performance.getAvailability().getCloudResourceUptime().getMin());
    		cloud_resource_uptimeDB.setProperty("max", performance.getAvailability().getCloudResourceUptime().getMax());
    		cloud_resource_uptimeDB.setProperty("avg", performance.getAvailability().getCloudResourceUptime().getAvg());
    		cloud_resource_uptimeDB.setProperty("st", performance.getAvailability().getCloudResourceUptime().getSt());

    		meantime_between_failuresDB = db.createNode(performanceLabel.availability,availabilityLabel.meantime_between_failures,performanceLabel.topologyStatistics);
    		meantime_between_failuresDB.setProperty("min", performance.getAvailability().getMeantimeBetweenFailures().getMin());
    		meantime_between_failuresDB.setProperty("max", performance.getAvailability().getMeantimeBetweenFailures().getMax());
    		meantime_between_failuresDB.setProperty("avg", performance.getAvailability().getMeantimeBetweenFailures().getAvg());
    		meantime_between_failuresDB.setProperty("st", performance.getAvailability().getMeantimeBetweenFailures().getSt());

    		meantime_repairDB = db.createNode(performanceLabel.availability,availabilityLabel.meantime_repair,performanceLabel.topologyStatistics);
    		meantime_repairDB.setProperty("min", performance.getAvailability().getMeantimeRepair().getMin());
    		meantime_repairDB.setProperty("max", performance.getAvailability().getMeantimeRepair().getMax());
    		meantime_repairDB.setProperty("avg", performance.getAvailability().getMeantimeRepair().getAvg());
    		meantime_repairDB.setProperty("st", performance.getAvailability().getMeantimeRepair().getSt());

            performanceDB.createRelationshipTo(cloud_service_uptimeDB,performanceRelationships.HAS_AVAILABILITY);
            performanceDB.createRelationshipTo(cloud_resource_uptimeDB,performanceRelationships.HAS_AVAILABILITY);
            performanceDB.createRelationshipTo(meantime_between_failuresDB,performanceRelationships.HAS_AVAILABILITY);
            performanceDB.createRelationshipTo(meantime_repairDB,performanceRelationships.HAS_AVAILABILITY);


		    tx.success();
    	}
		return performanceDB;

	}

	public void deletePerformanceById(long id) {
		// TODO Auto-generated method stub
		String query = "start a=node("+id+") match a-[r]->(b:topologyStatistics) return b";
		//get all topology statistics node
		List<Node> statisticsNodes = new ArrayList<Node>();

		try ( Transaction tx = db.beginTx();Result result = db.execute(query);)
    	{
			  while ( result.hasNext() )
      	    {
      	        Map<String,Object> row = result.next();

      	         for ( String key : result.columns() )
      	         {
      	           Node oneNode=  (Node) row.get( key );
      	           statisticsNodes.add(oneNode);
      	         }
      	    }
			  String deleteRel = "MATCH (n) WHERE id(n)= "+id+" OPTIONAL MATCH n-[r]-() DELETE r";
			  db.execute(deleteRel);
			  for(Node oneNode: statisticsNodes){
				  oneNode.delete();
			  }
			  //delete performance root node
			  Node rootNode = this.getNodeById(id);
			  rootNode.delete();
			  tx.success();
	    }


	}


	public List<Node> getAllPerformanceNodesForOneAlphaTopologyByID(long alphaTotpologyID){
		List<Node> allPerformanceNodes = new ArrayList<Node>();
		String getAllPerformance ="start n=node("+alphaTotpologyID+") match n-[r:PERFORM_PERFORMANCE]->b return b";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllPerformance);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {	Node tempNode;
	         tempNode = (Node) row.get( key );
	         allPerformanceNodes.add(tempNode);
	         }
	    }
		tx.success();
		}
		return allPerformanceNodes;
	}

	public List<Node> getAllPerformanceNodes() throws org.neo4j.graphdb.QueryExecutionException{
		List<Node> allPerformanceNodes = new ArrayList<Node>();
		String getAllPerformance ="MATCH (n:performance) RETURN n";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllPerformance);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {	Node tempNode;
	         tempNode = (Node) row.get( key );
	         allPerformanceNodes.add(tempNode);
	         }
	    }
		tx.success();
		}
		return allPerformanceNodes;
	}

	public Node getPerformanceByPerformanceID(String id){
		Node performance = null;
		String getAllHistory ="MATCH (n:performance) where n.id = '"+id+"'  RETURN n";
		try ( Transaction tx = db.beginTx();){
        Result result = db.execute(getAllHistory);
        while ( result.hasNext() )
	    {
	        Map<String,Object> row = result.next();

	         for ( String key : result.columns() )
	         {
	        	 performance = (Node) row.get( key );
	         }
	    }
		}
		return performance;
	}

	  public boolean isPerformance(long performanceID) throws NotFoundException {

		  boolean result = false;
		  try ( Transaction tx = db.beginTx()){
		   Node node = db.getNodeById(performanceID);
		   if(node != null){
			   Iterator<Label> labels =  node.getLabels().iterator();
			   while(labels.hasNext()){
	             Label oneLable = labels.next();
	             if(oneLable.name().equals("performance")){
	             result = true;
	             }
			   }
		   }
		  tx.close();
		  return result;
	  }

	  }


	public static void main(String [] args){
		PerformanceDao dao = new PerformanceDao();
		dao.deletePerformanceById(91);
	}



}
