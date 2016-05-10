package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.domain.RelationshipInstance;
import org.eclipse.toscaModel.TEntityTemplate;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TRelationshipTemplate;
import org.eclipse.toscaModel.TServiceTemplate;
import org.eclipse.toscaModel.TTopologyTemplate;
import org.eclipse.transformer.Utility.SpecificationReader;

public class RelationshipInstanceTransformer extends TopologyTransformer<RelationshipInstance>{
	
	RelationshipInstance relationshipInstance = null;


	public RelationshipInstanceTransformer(InputStream is, String urlPara) {
		super(is, urlPara);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RelationshipInstance getDomainType() throws InputWrongType {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String specification = null;
    	InputStream bufferedIs = new BufferedInputStream(is);
    	bufferedIs.mark(0);
    	specification = SpecificationReader.GetSpecification(bufferedIs);
    	try {
			bufferedIs.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Iterator<TExtensibleElements> iterator = null;
    	
    	iterator = Unmarshaller(bufferedIs);
    	TExtensibleElements next = null;
		while (iterator.hasNext()) {
			next = iterator.next();
			if (next instanceof TServiceTemplate) {
				TTopologyTemplate topologyTemplate = ((TServiceTemplate) next).getTopologyTemplate();
				
				if(topologyTemplate.getNodeTemplateOrRelationshipTemplate()!= null){
					
					Iterator<TEntityTemplate> nodeTemplateIterator =  topologyTemplate.getNodeTemplateOrRelationshipTemplate().iterator();
					
					TExtensibleElements nextRelationshipInstance = null;
					while (nodeTemplateIterator.hasNext()) {
						nextRelationshipInstance = nodeTemplateIterator.next();
						if (nextRelationshipInstance instanceof TRelationshipTemplate){
		                    this.relationshipInstance = processToscaRelationshipTemaplate((TRelationshipTemplate) nextRelationshipInstance);
		                	this.relationshipInstance.setSpecification(specification);
						}
						else
							throw new InputWrongType(nextRelationshipInstance);
					}
				
 				}
			else
				throw new InputWrongType(next);
			}  

	   }
        return this.relationshipInstance;
	}
	
        public RelationshipInstance processToscaRelationshipTemaplate(TRelationshipTemplate relationshipTemplate){
    		
        	RelationshipInstance relationshipInstance = new RelationshipInstance();
    		

    		//general
        	relationshipInstance.setName(relationshipTemplate.getName());
        	relationshipInstance.setId(relationshipTemplate.getId());
        	relationshipInstance.setType(relationshipTemplate.getType());
    	    //SourceElement
        	RelationshipInstance.SourceElement sourceElement = new RelationshipInstance.SourceElement();
        	relationshipInstance.setSourceElement(sourceElement);
            System.out.println(relationshipInstance.getSourceElement());
        	relationshipInstance.getSourceElement().setRef(relationshipTemplate.getSourceElement().getRef());       	
        	//TargetElement
        	RelationshipInstance.TargetElement targetElement = new RelationshipInstance.TargetElement();
        	relationshipInstance.setTargetElement(targetElement);
        	relationshipInstance.getTargetElement().setRef(relationshipTemplate.getTargetElement().getRef());
    		return relationshipInstance;
    		
    		
    	}
}
