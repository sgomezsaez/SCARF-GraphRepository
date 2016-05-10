package org.eclipse.transformer.Topology;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import org.eclipse.Exception.InputWrongType;
import org.eclipse.inputModel.AlphaTopologyTemplate;
import org.eclipse.topology.domain.AbstractSubTopologyDomain;
import org.eclipse.topology.domain.AlphaTopology;
import org.eclipse.topology.domain.InstanceNode;
import org.eclipse.topology.domain.RelationshipInstance;
import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TDefinitions;
import org.eclipse.toscaModel.TEntityTemplate;
import org.eclipse.toscaModel.TExtensibleElements;
import org.eclipse.toscaModel.TNodeTemplate;
import org.eclipse.toscaModel.TNodeType;
import org.eclipse.toscaModel.TRelationshipTemplate;
import org.eclipse.toscaModel.TServiceTemplate;
import org.eclipse.toscaModel.TTopologyTemplate;
import org.eclipse.transformer.Utility.Constants;
import org.eclipse.transformer.Utility.JAXBSupport;
import org.eclipse.transformer.Utility.SpecificationReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class AlphaTopologyTransformer extends TopologyTransformer<AlphaTopology> {
	
	private AlphaTopology alphaTopology = new AlphaTopology();
	private String specificationType;
	private List<AlphaTopologyTemplate.Nodelevel.Node> nodeLevelList = new ArrayList<AlphaTopologyTemplate.Nodelevel.Node>();
    private AlphaTopologyTemplate.Specification.Definitions toscaDefinition;
	
	public AlphaTopologyTransformer(InputStream is) {
		super(is);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public AlphaTopology getDomainType() throws InputWrongType {

		String specification = null;

    	Unmarshaller um = JAXBSupport.createUnmarshaller();	
    	Marshaller am = JAXBSupport.createMarshaller(true);
	 	try { 
	 		//BufferedInputStream  bufferedIs = new BufferedInputStream(is);
	    	//bufferedIs.mark(0);
	 		ByteArrayOutputStream baos = new ByteArrayOutputStream();//cache the inputstream 
	 		byte[] buf = new byte[100000];
	 		int n = 0;
	 		while ((n = is.read(buf)) >= 0)
	 		    baos.write(buf, 0, n);
	 		byte[] content = baos.toByteArray();
	 		//specification
	 	   // create a new DocumentBuilderFactory
	 		InputStream is1 = new ByteArrayInputStream(content);
	 	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	 		try {
	 	         // use the factory to create a documentbuilder
	 	         DocumentBuilder builder = factory.newDocumentBuilder();

	 	         // create a new document from input stream
	 	         Document doc = builder.parse(is1);

	 	         // get the first element
	 	         Element element = doc.getDocumentElement();

	 	         // get all child nodes
	 	         NodeList nodes = element.getElementsByTagName("tosca:Definitions");
	 	         Node node = nodes.item(0);

	 	         // print the text content of each child
	 	        // for (int i = 0; i < nodes.getLength(); i++) {
	 	        //    System.out.println("" + nodes.item(i).getTextContent());
	 	        // }


	 		 
	 		Document document = node.getOwnerDocument();
	 		DOMImplementationLS domImplLS = (DOMImplementationLS) document
	 		    .getImplementation();
	 		LSSerializer serializer = domImplLS.createLSSerializer();
	 		String str = serializer.writeToString(node);//the encoding only support utf-16......
	        this.alphaTopology.setSepcification(str);
	 		//System.out.println(str);
	 	      } catch (Exception ex) {
		 	         ex.printStackTrace();
		 	      }
	 		//am.marshal(toscaDefinition, System.out);
	 		//bufferedIs.reset();
	 		InputStream is2 = new ByteArrayInputStream(content);
	 		AlphaTopologyTemplate alphaTopologyTemplate =  (AlphaTopologyTemplate) um.unmarshal(is2);

	 		//specificationType
	 		this.specificationType = alphaTopologyTemplate.getSpecificationType();
	 		//Node level
	 		nodeLevelList = alphaTopologyTemplate.getNodelevel().getNode();
	 		//definition
	 		toscaDefinition = alphaTopologyTemplate.getSpecification().getDefinitions();
	 		
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		Iterator<TExtensibleElements> iterator = null;
    	iterator = toscaDefinition.getServiceTemplateOrNodeTypeOrNodeTypeImplementation().iterator();
    	TExtensibleElements next = null;
		while (iterator.hasNext()) {
			next = iterator.next();
			if (next instanceof TServiceTemplate) {
				this.alphaTopology.setId(((TServiceTemplate) next).getId());
				this.alphaTopology.setName(((TServiceTemplate) next).getName());
				this.alphaTopology.setTargetNamespace(((TServiceTemplate) next).getTargetNamespace());
				this.alphaTopology.setSpecificationType(this.specificationType);
                //nodeLevel
				Map<String,String> nodeLevelMapper = new HashMap<String, String>();
				for(AlphaTopologyTemplate.Nodelevel.Node oneNode:this.nodeLevelList){
                       nodeLevelMapper.put(oneNode.getId(), oneNode.getLevel());
				}
				this.alphaTopology.setNodeLevelMapper(nodeLevelMapper);
	            //timeStamp
				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
				this.alphaTopology.setCreatDate(timeStamp.toString());
				TTopologyTemplate topologyTemplate = ((TServiceTemplate) next).getTopologyTemplate();
				
				if(topologyTemplate.getNodeTemplateOrRelationshipTemplate()!= null){
					
					Iterator<TEntityTemplate> nodeTemplateOrRelationshipTemplateIterator =  topologyTemplate.getNodeTemplateOrRelationshipTemplate().iterator();
					
					TExtensibleElements nextNodeTemplateOrRelationshipTemplate = null;
					while (nodeTemplateOrRelationshipTemplateIterator.hasNext()) {
						nextNodeTemplateOrRelationshipTemplate = nodeTemplateOrRelationshipTemplateIterator.next();
						if (nextNodeTemplateOrRelationshipTemplate instanceof TNodeTemplate){
							InstanceNodeTransformer instanceNodeTransformer = new InstanceNodeTransformer();
							InstanceNode instanceNode = instanceNodeTransformer.processToscaNodeTemplate((TNodeTemplate) nextNodeTemplateOrRelationshipTemplate,Constants.alphaTopology);
							instanceNode.setNodeLevel(this.alphaTopology.getNodeLevelMapper().get(instanceNode.getId()));//to set node level
							this.alphaTopology.getNodeTemplateOrRelationshipTemplate().add(instanceNode);
							
						}
						else if (nextNodeTemplateOrRelationshipTemplate instanceof TRelationshipTemplate){						 
							 RelationshipInstance relationshipInstance=  processToscaRelationshipTemaplate((TRelationshipTemplate) nextNodeTemplateOrRelationshipTemplate);	
							 this.alphaTopology.getNodeTemplateOrRelationshipTemplate().add(relationshipInstance);
						}
						else
							throw new InputWrongType(nextNodeTemplateOrRelationshipTemplate);
					}
				
 				}
			else
				throw new InputWrongType(next);
			}  
		

	   }
		

        return this.alphaTopology;
	}
	
	
    private RelationshipInstance processToscaRelationshipTemaplate(TRelationshipTemplate relationshipTemplate){
		
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
