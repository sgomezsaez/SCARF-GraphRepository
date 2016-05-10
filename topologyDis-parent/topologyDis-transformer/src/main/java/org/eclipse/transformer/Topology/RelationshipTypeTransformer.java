package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.domain.RelationshipType;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TRelationshipType;
import org.eclipse.transformer.Utility.SpecificationReader;

public class RelationshipTypeTransformer extends TopologyTransformer<RelationshipType> {
	
	RelationshipType relationshipType = new RelationshipType();
	String specification = null;
	TRelationshipType relationshipTypeOfTosca = null;
	
	

	public TRelationshipType getRelationshipTypeOfTosca() {
		return relationshipTypeOfTosca;
	}

	public RelationshipTypeTransformer(InputStream is) throws InputWrongType {
		super(is);
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
			if (next instanceof TRelationshipType) {
                this.relationshipTypeOfTosca = (TRelationshipType) next;;

 				}
			else
				throw new InputWrongType(next);
			}  
	}

	@Override
	public RelationshipType getDomainType() throws InputWrongType {
		
        this.relationshipType = processToscaNodeRelationshipType(this.relationshipTypeOfTosca,specification);
        this.relationshipType.setSpecification(specification);

        return relationshipType;
	}
   
	
private RelationshipType processToscaNodeRelationshipType(TRelationshipType TrelationshipType,String specification){
		
	   RelationshipType relationshipType = new RelationshipType();
		
		//general
	    relationshipType.setName(TrelationshipType.getName());
	    relationshipType.setSpecification(specification);
	    relationshipType.setTargetNamespace(TrelationshipType.getTargetNamespace());

		//valid Source
	    
	    //valid target

		
		return relationshipType;
		
		
	}
}
