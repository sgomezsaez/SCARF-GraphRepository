package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.domain.InstanceNode;
import org.eclipse.toscaModel.TEntityTemplate;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TNodeTemplate;
import org.eclipse.toscaModel.TServiceTemplate;
import org.eclipse.toscaModel.TTopologyTemplate;
import org.eclipse.transformer.Utility.Constants;
import org.eclipse.transformer.Utility.SpecificationReader;

public class InstanceNodeTransformer extends TopologyTransformer<InstanceNode> {
	
	InstanceNode instanceNode = null;
	TNodeTemplate nodeTemplate = null;
	String specification = null;
	
	public InstanceNodeTransformer(InputStream is) throws InputWrongType {
	super(is);
	}

	public TNodeTemplate getNodeTemplate() {
		return nodeTemplate;
	}

	public InstanceNodeTransformer(InputStream is, String urlPara) throws InputWrongType {
		super(is, urlPara);
		// TODO Auto-generated constructor stub

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
					
					TExtensibleElements nextNodeTemplate = null;
					while (nodeTemplateIterator.hasNext()) {
						nextNodeTemplate = nodeTemplateIterator.next();
						
						if (nextNodeTemplate instanceof TNodeTemplate){
							this.nodeTemplate = (TNodeTemplate) nextNodeTemplate;
						}
						else
							throw new InputWrongType(nextNodeTemplate);
					}
				
 				}
			else
				throw new InputWrongType(next);
			}  

	   }
  
	}
	
	public InstanceNodeTransformer() throws InputWrongType {


    	
  
	}
	
	

	@Override
	public InstanceNode getDomainType() throws InputWrongType {
		// TODO Auto-generated method stub		

		this.instanceNode = processToscaNodeTemplate(this.nodeTemplate,Constants.gammaTopology);
		this.instanceNode.setSpecification(specification);

        return this.instanceNode;
	}
	
	protected InstanceNode processToscaNodeTemplate(TNodeTemplate nodeTemplate,String topologyType){
		
		InstanceNode instanceNode = new InstanceNode();
		

		//general
		instanceNode.setName(nodeTemplate.getName());
		instanceNode.setId(nodeTemplate.getId());
		instanceNode.setType(nodeTemplate.getType());
		instanceNode.setTopologyType(topologyType);
		//Capability
		/*nodeTemplate.getCapabilities().getCapability();
		
		if(nodeTemplate.getCapabilities() != null){
			Iterator<TCapability> iteratorCapability = nodeTemplate.getCapabilities().getCapability().iterator();
			
			TCapability nextCapability = null;
			Capability capability_domain = null;
			while (iteratorCapability.hasNext()) {}
			}*/
		//Requirement
		
		//Properties
		
		return instanceNode;
		
		
	}
	
/*	public InstanceNode processToscaNodeTemplate(TNodeTemplate nodeTemplate){
		
		InstanceNode instanceNode = new InstanceNode();
		

		//general
		instanceNode.setName(nodeTemplate.getName());
		instanceNode.setId(nodeTemplate.getId());
		instanceNode.setType(nodeTemplate.getType());
		//Capability
		nodeTemplate.getCapabilities().getCapability();
		
		if(nodeTemplate.getCapabilities() != null){
			Iterator<TCapability> iteratorCapability = nodeTemplate.getCapabilities().getCapability().iterator();
			
			TCapability nextCapability = null;
			Capability capability_domain = null;
			while (iteratorCapability.hasNext()) {}
			}
		//Requirement
		
		//Properties
		
		return instanceNode;
		
		
	}*/

}
