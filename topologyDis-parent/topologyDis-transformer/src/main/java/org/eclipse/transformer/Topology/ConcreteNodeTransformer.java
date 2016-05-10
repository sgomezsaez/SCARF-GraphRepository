package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.domain.Capability;
import org.eclipse.topology.domain.ConcreteNode;
import org.eclipse.topology.domain.ConcreteNode;
import org.eclipse.topology.domain.ConcreteNode.Capabilities;
import org.eclipse.topology.domain.ConcreteNode.Requirements;
import org.eclipse.topology.domain.Requirement;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TCapabilityDefinition;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TNodeType;
import org.eclipse.toscaModel.TRequirementDefinition;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.eclipse.transformer.Utility.SpecificationReader;

public class ConcreteNodeTransformer extends TopologyTransformer<ConcreteNode> {

	ConcreteNode concreteNode = new ConcreteNode();
	
	public ConcreteNodeTransformer(InputStream is, String urlPara) throws InputWrongType {
		
		super(is, urlPara);
		// TODO Auto-generated constructor stub
	}
	
	public ConcreteNodeTransformer(InputStream is) throws InputWrongType {
		
		super(is);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ConcreteNode getDomainType() 
			throws InputWrongType {
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
			if (next instanceof TNodeType) {
                this.concreteNode = processToscaNodeType((TNodeType) next,specification);

 				}
			else
				throw new InputWrongType(next);
			}  
        return concreteNode;
	}



	private ConcreteNode processToscaNodeType(TNodeType toscaNodeType, String specification){
		

		//name
		concreteNode.setName(toscaNodeType.getName());
        //targetNameSpace
		concreteNode.setTargetNamespace(toscaNodeType.getTargetNamespace());
		//Capability
		
		if(toscaNodeType.getCapabilityDefinitions() != null){
		Iterator<TCapabilityDefinition> iteratorCapability =  toscaNodeType.getCapabilityDefinitions().getCapabilityDefinition().iterator();
		
		TCapabilityDefinition nextCapability = null;
		Capability capability_domain = null;
		   while (iteratorCapability.hasNext()) {
			nextCapability = iteratorCapability.next();
			capability_domain = new Capability();
			int i = nextCapability.getLowerBound();
			Integer iInteger = new Integer(i);
			capability_domain.setLowerBound(iInteger);
			capability_domain.setUpperBound(nextCapability.getUpperBound());
			capability_domain.setName(nextCapability.getName());
			capability_domain.setCapabilityType(nextCapability.getCapabilityType());
			concreteNode.capabilities = new Capabilities();
			concreteNode.capabilities.getCapability().add(capability_domain);

		   }
		}
		//Requirement
		if(toscaNodeType.getRequirementDefinitions() != null){
		Iterator<TRequirementDefinition> iteratorRequirement =  toscaNodeType.getRequirementDefinitions().getRequirementDefinition().iterator();
		
		TRequirementDefinition nextRequirement = null;
		Requirement requirement_domain = null;
		   while (iteratorRequirement.hasNext()) {
			nextRequirement = iteratorRequirement.next();
			requirement_domain = new Requirement();
			int i = nextRequirement.getLowerBound();
			Integer iInteger = new Integer(i);
			requirement_domain.setLowerBound(iInteger);
			requirement_domain.setUpperBound(nextRequirement.getUpperBound());
			requirement_domain.setName(nextRequirement.getName());
			requirement_domain.setRequirementType(nextRequirement.getRequirementType());
			concreteNode.requirements = new Requirements();
			concreteNode.requirements.getRequirement().add(requirement_domain);

		   }
		}
		
		//Specification
		concreteNode.setSpecification(specification);
		if(this.urlPara!=null){
		concreteNode.setAbstractType(urlPara);
		}
		return concreteNode;
	}


}
