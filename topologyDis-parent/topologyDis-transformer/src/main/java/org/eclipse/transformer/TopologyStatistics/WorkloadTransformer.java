package org.eclipse.transformer.TopologyStatistics;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.inputModel.WorkloadId;
import org.eclipse.topology.domain.Workload;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.eclipse.transformer.Utility.SpecificationReader;

public class WorkloadTransformer {
	
	Workload workload = new Workload();
	InputStream inputstream = null;
	
	public WorkloadTransformer(InputStream is){
		this.inputstream = is;
	}
	
	public Workload getWorkload() throws JAXBException{
		String specification = null;
    	InputStream bufferedIs = new BufferedInputStream(this.inputstream);
    	bufferedIs.mark(0);
    	specification = SpecificationReader.GetSpecification(bufferedIs);
    	try {
			bufferedIs.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
	    this.workload = (Workload) um.unmarshal(bufferedIs);
		return this.workload;				
	}
	
	public long getWorkloadId() throws JAXBException{
		long employWorkloadId;
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
		WorkloadId employWorkload = (WorkloadId) um.unmarshal(inputstream);
		employWorkloadId = employWorkload.getId();		
		return employWorkloadId;		
	}
	


}
