package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;

import org.eclipse.Exception.InputWrongType;
import org.eclipse.topology.domain.MuTopology;
import org.eclipse.toscaModel.TDefinitions;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TNodeType;
import org.eclipse.toscaModel.TServiceTemplate;
import org.eclipse.transformer.Utility.Constants;
import org.eclipse.transformer.Utility.SpecificationReader;

public class MuTopologyTransformer extends TopologyTransformer<MuTopology> {
	String specification = null;
	MuTopology muTopology = new MuTopology();
	
	public MuTopologyTransformer(InputStream is) throws InputWrongType {
		
		super(is);
		// TODO Auto-generated constructor stub
	}
	
	public MuTopologyTransformer() throws InputWrongType {
		
	
		// TODO Auto-generated constructor stub
	}


	@Override
	public MuTopology getDomainType() 
			throws InputWrongType {
		// TODO Auto-generated method stub
		int bufferSize = 2 * 8192;//the default size is 8192 bytes, some tosca template is larger than that
    	InputStream bufferedIs = new BufferedInputStream(is,bufferSize);
    	bufferedIs.mark(0);
    	this.specification = SpecificationReader.GetSpecification(bufferedIs);
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
                this.muTopology = processToscaServiceTemplate((TServiceTemplate) next,specification);

 				}
			else
				throw new InputWrongType(next);
			}  
        return muTopology;
	}



	private MuTopology processToscaServiceTemplate(TServiceTemplate serviceTemplate, String specification){
		MuTopology muTopology = new MuTopology();

				muTopology.setAlphaTopologyId(serviceTemplate.getId());
				muTopology.setAlphaTopologyName(serviceTemplate.getName());
				muTopology.setAlphaTopologyNameSpace(serviceTemplate.getTargetNamespace());
				muTopology.setObsolete(Constants.LogicBooleanNo);
				muTopology.setSpecification(this.specification);
				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
				muTopology.setCreateDate(timeStamp.toString());

		return muTopology;
	}

}
