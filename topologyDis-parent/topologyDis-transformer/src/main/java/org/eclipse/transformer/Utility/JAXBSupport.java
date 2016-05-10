package org.eclipse.transformer.Utility;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.inputModel.AbstractSubTopology;
import org.eclipse.inputModel.AlphaTopologyTemplate;
import org.eclipse.inputModel.PerformanceId;
import org.eclipse.inputModel.WorkloadId;
import org.eclipse.output.TopologyOutput.AlphaTopologyOutput;
import org.eclipse.output.TopologyOutput.MuTopologyOutput;
import org.eclipse.output.others.PerformanceList;
import org.eclipse.topology.domain.NameSpace;
import org.eclipse.topology.domain.Performance;
import org.eclipse.topology.domain.Workload;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TDefinitions;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class JAXBSupport {
	
	public final static JAXBContext context = JAXBSupport.initContext();
	
	private final static PrefixMapper prefixMapper = new PrefixMapper();

	
private static class PrefixMapper extends NamespacePrefixMapper {
		


    
		@Override
		public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
			if (namespaceUri.equals("")) {
				return "";
			}
			
			
			// this does not work to get TOSCA elements without prefix
			// possibly because the attribute "name" is present without prefix
			//	if (namespaceUri.equals(Namespaces.TOSCA_NAMESPACE)) {
			//		return "";
			//	}
			
			//String prefix = NamespacesResource.getPrefix(namespaceUri);
			String prefix = "tosca";
			return prefix;
		}
	}
	
	
	private static JAXBContext initContext() {
		JAXBContext context;
		try {
			// For winery classes, eventually the package+jaxb.index method could be better. See http://stackoverflow.com/a/3628525/873282
			// @formatter:off
			context = JAXBContext.newInstance(
					
					TDefinitions.class,
					AbstractSubTopology.class,
					AlphaTopologyTemplate.class,
					MuTopologyOutput.class,
					Definitions.class,
					Workload.class,
					Performance.class,
					WorkloadId.class,
					PerformanceId.class,
					PerformanceList.class,
					AlphaTopologyOutput.class,
					NameSpace.class
					
                    // all other elements are referred by "@XmlSeeAlso"
);
			// @formatter:on
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		}
		return context;
	}
	
	/**
	 * Creates a marshaller
	 * 
	 * @throws IllegalStateException if marshaller could not be instantiated
	 */
	public static Marshaller createMarshaller(boolean includeProcessingInstruction) {
		Marshaller m; 
		try {
			m = JAXBSupport.context.createMarshaller();
			// pretty printed output is required as the XML is sent 1:1 to the browser for editing
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty("com.sun.xml.bind.namespacePrefixMapper", JAXBSupport.prefixMapper);
			if (!includeProcessingInstruction) {
				// side effect of JAXB_FRAGMENT property (when true): processing instruction is not included
				m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			}
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		}		
		return m;
	}
	
	/**
	 * Creates an unmarshaller
	 * 
	 * @throws IllegalStateException if unmarshaller could not be instantiated
	 */
	public static Unmarshaller createUnmarshaller() {
		try {
			return JAXBSupport.context.createUnmarshaller();
		} catch (JAXBException e) {
			//JAXBSupport.logger.error("Could not instantiate unmarshaller", e);
			throw new IllegalStateException(e);
		}
	}
	
}
