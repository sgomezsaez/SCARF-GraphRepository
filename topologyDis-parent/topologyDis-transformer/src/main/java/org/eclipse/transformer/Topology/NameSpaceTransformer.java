package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.topology.domain.NameSpace;
import org.eclipse.transformer.Utility.JAXBSupport;

public class NameSpaceTransformer {
	
	NameSpace nameSpace = new NameSpace();
	InputStream inputstream = null;
	
	public NameSpaceTransformer(InputStream is){
		this.inputstream = is;
	}
	
	public NameSpace getNameSpace() throws JAXBException{
    	InputStream bufferedIs = new BufferedInputStream(this.inputstream);
    	bufferedIs.mark(0);
    	try {
			bufferedIs.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
	    this.nameSpace = (NameSpace) um.unmarshal(bufferedIs);
		return this.nameSpace;				
	}
	
}
