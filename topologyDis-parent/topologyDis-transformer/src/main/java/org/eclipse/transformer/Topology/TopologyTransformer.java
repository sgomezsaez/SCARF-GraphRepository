package org.eclipse.transformer.Topology;


import java.io.InputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.domain.InstanceNode;
import org.eclipse.topology.domain.RelationshipInstance;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TDefinitions;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TNodeTemplate;
import org.eclipse.toscaModel.TRelationshipTemplate;
import org.eclipse.transformer.Utility.JAXBSupport; 


public abstract class  TopologyTransformer <T>{
	
	protected InputStream is = null;
    protected String urlPara = null;
    protected Definitions  defi_type = null;

    

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public Definitions getDefi_type() {
		return defi_type;
	}

	public TopologyTransformer(InputStream is, String urlPara) {
		// TODO Auto-generated constructor stub
		this.is = is;
        this.urlPara = urlPara;
	}
	
	public TopologyTransformer(InputStream is) {
		// TODO Auto-generated constructor stub
		this.is = is;
	}

	
	public TopologyTransformer() {
		// TODO Auto-generated constructor stub

	}



	public abstract  T getDomainType() throws InputWrongType;

	public Iterator<TExtensibleElements> Unmarshaller( InputStream is){
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
		Iterator<TExtensibleElements> iterator = null;
	 	try {
    	this.defi_type =  (Definitions) um.unmarshal(is);
   
    	 iterator = defi_type.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().iterator();

    	} catch (JAXBException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return iterator;
	}
	
    
    public Definitions getToscaDefinition(){
    	
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
		Iterator<TExtensibleElements> iterator = null;
		Definitions definition = null;;
	 	try {
	 		definition =  (Definitions) um.unmarshal(is);
	 	}
    	catch (JAXBException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
	 	
		return definition;  
    }

	


/*	protected void Marshaller(Object object){
		
	Marshaller ma = JAXBSupport.createMarshaller(false);
	
	
	
	try {

		ma.marshal(object, System.out);
        
		System.out.println(System.out.toString());

	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}    
	*/
/*	protected Iterator<TExtensibleElements> Unmarshaller( InputStream is){
		Unmarshaller um = JAXBSupport.createUnmarshaller();	
		Iterator<TExtensibleElements> iterator = null;
	 	try {
    	TDefinitions  defi_type =  (TDefinitions) um.unmarshal(is);
   
    	 iterator = defi_type.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().iterator();

    	} catch (JAXBException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return iterator;
	}*/
	
	
	
}
