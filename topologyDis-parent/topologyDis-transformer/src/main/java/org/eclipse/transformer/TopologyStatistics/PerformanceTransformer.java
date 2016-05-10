package org.eclipse.transformer.TopologyStatistics;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.inputModel.PerformanceId;
import org.eclipse.inputModel.WorkloadId;
import org.eclipse.topology.domain.Performance;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.eclipse.transformer.Utility.SpecificationReader;

public class PerformanceTransformer {
	
	Performance performance = new Performance();
	InputStream inputstream = null;
	String specification = null;
	
	
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public PerformanceTransformer(InputStream is){
		this.inputstream = is;
	}
	
	public Performance getPerformance() throws JAXBException{
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
	    this.performance = (Performance) um.unmarshal(bufferedIs);
	    this.specification = specification;
		return this.performance;				
	}
	
	public long getPerformanceId() throws JAXBException{
		long id;
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
		PerformanceId performanceId = (PerformanceId) um.unmarshal(inputstream);
		id = performanceId.getId();		
		return id;		
	}

}
