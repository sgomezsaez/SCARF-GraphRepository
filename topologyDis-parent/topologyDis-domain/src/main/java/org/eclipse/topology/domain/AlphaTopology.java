package org.eclipse.topology.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class AlphaTopology {
	
    protected String id;
   
    protected String name;
   
    protected String targetNamespace;
    
	protected String sepcification;
	protected String creatDate;
    protected String createdBy;
    protected String specificationType;
    protected Map<String,String> nodeLevelMapper;
    
    

	public Map<String, String> getNodeLevelMapper() {
		if(nodeLevelMapper==null){
			return new HashMap<String,String>();
		}
		return nodeLevelMapper;
	}

	public void setNodeLevelMapper(Map<String, String> nodeLevelMapper) {
		//this.nodeLevelMapper = new HashMap<String,String>(nodeLevelMapper);
		this.nodeLevelMapper = nodeLevelMapper;
	}

	public String getSpecificationType() {
		return specificationType;
	}

	public void setSpecificationType(String specificationType) {
		this.specificationType = specificationType;
	}

	protected List<DomainTemplate> InstanceNodeOrRelationshipInstance;

    /**
     * Gets the value of the nodeTemplateOrRelationshipTemplate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodeTemplateOrRelationshipTemplate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodeTemplateOrRelationshipTemplate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TNodeTemplate }
     * {@link TRelationshipTemplate }
     * 
     * 
     */
    
    
    public List<DomainTemplate> getNodeTemplateOrRelationshipTemplate() {
        if (InstanceNodeOrRelationshipInstance == null) {
        	InstanceNodeOrRelationshipInstance = new ArrayList<DomainTemplate>();
        }
        return this.InstanceNodeOrRelationshipInstance;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public String getSepcification() {
		return sepcification;
	}

	public void setSepcification(String sepcification) {
		this.sepcification = sepcification;
	}

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	


    
    
}
