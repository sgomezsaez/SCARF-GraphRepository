package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;




import org.eclipse.Exception.InputWrongType;
import org.eclipse.inputModel.AbstractSubTopology;
import org.eclipse.inputModel.AbstractSubTopology.AbstractNode;
import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.eclipse.transformer.Utility.SpecificationReader;

public class AbstractSkeletonTransformer extends TopologyTransformer<AbstractSubTopologyDomain> {

	public AbstractSkeletonTransformer(InputStream is) {
		super(is);
		// TODO Auto-generated constructor stub
	}



	static String specification = null;   
	
    public AbstractSubTopology getAbstractSkeleton(){
    	AbstractSubTopology abstractSkeleton = new AbstractSubTopology();
    	InputStream bufferedIs = new BufferedInputStream(is);
    	bufferedIs.mark(0);   		
    	Unmarshaller um = JAXBSupport.createUnmarshaller();	
	 	try { 
	 	 abstractSkeleton =  (AbstractSubTopology) um.unmarshal(bufferedIs);
	 	  //Node
	    } catch (JAXBException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        return abstractSkeleton;
	 	
    }

	@Override
	public AbstractSubTopologyDomain getDomainType() throws InputWrongType {
		// TODO Auto-generated method stub
    	InputStream bufferedIs = new BufferedInputStream(is);
    	bufferedIs.mark(0);
    	specification = SpecificationReader.GetSpecification(bufferedIs);
    	try {
			bufferedIs.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    		
    	Unmarshaller um = JAXBSupport.createUnmarshaller();	
    	AbstractSubTopologyDomain  abstractSkeletonDomain = new AbstractSubTopologyDomain() ;
	 	try { 
	 		AbstractSubTopology abstractSubTopology =  (AbstractSubTopology) um.unmarshal(bufferedIs);
	 	  //Node
	 	  Iterator<AbstractNode> iteratorNode = abstractSubTopology.getAbstractNode().iterator();
	 	  AbstractNode nextNode = null;
	 	 while (iteratorNode.hasNext()) {
	 		nextNode = iteratorNode.next();
	 		AbstractSubTopologyDomain.AbstractNode newNode = new AbstractSubTopologyDomain.AbstractNode();;
            newNode.setId(nextNode.getId());
            newNode.setLevel(nextNode.getLevel());
            newNode.setName(nextNode.getName());
            newNode.setType(nextNode.getType());
            newNode.setLevel(nextNode.getLevel());
            abstractSkeletonDomain.getAbstractNode().add(newNode);
 	     }
	 	 //Relation
	 	 Iterator<AbstractSubTopology.RelationshipOfAbstractNode> iteratorRelation = abstractSubTopology.getRelationshipOfAbstractNode().iterator();
	 	AbstractSubTopology.RelationshipOfAbstractNode nextRelation = null;
	 	 while (iteratorRelation.hasNext()) {
	 		nextRelation = iteratorRelation.next();
	 		AbstractSubTopologyDomain.RelationshipOfAbstractNode newRelation = new AbstractSubTopologyDomain.RelationshipOfAbstractNode();
	 		System.out.println(nextRelation.getSourceElement().getRef());
	 		AbstractSubTopologyDomain.RelationshipOfAbstractNode.SourceElement newSourceElement = new AbstractSubTopologyDomain.RelationshipOfAbstractNode.SourceElement();
	 		AbstractSubTopologyDomain.RelationshipOfAbstractNode.TargetElement newTargetElement = new AbstractSubTopologyDomain.RelationshipOfAbstractNode.TargetElement();
	 		newSourceElement.setRef((nextRelation.getSourceElement().getRef()));   
	 		newSourceElement.setValue((nextRelation.getSourceElement().getValue()));
	 		newTargetElement.setRef((nextRelation.getTargetElement().getRef()));   
	 		newTargetElement.setValue((nextRelation.getTargetElement().getValue()));
	 		newRelation.setSourceElement(newSourceElement);
	 		newRelation.setTargetElement(newTargetElement);
	 		newRelation.setType(nextRelation.getType());
           abstractSkeletonDomain.getRelationshipOfAbstractNode().add(newRelation);
	     }
	 	 //Specification
	 	abstractSkeletonDomain.setSpecification(specification);
	 	abstractSkeletonDomain.setAbstractSkeletonId(abstractSubTopology.getId());
	 	abstractSkeletonDomain.setAbstractSkeletonName(abstractSubTopology.getName());
	 	Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
	 	abstractSkeletonDomain.setCreatDate(timeStamp.toString());
    	} catch (JAXBException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        return abstractSkeletonDomain;
	}

}
