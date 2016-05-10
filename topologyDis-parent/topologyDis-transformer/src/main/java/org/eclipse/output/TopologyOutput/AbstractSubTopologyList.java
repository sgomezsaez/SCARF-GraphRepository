package org.eclipse.output.TopologyOutput;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.inputModel.AbstractSubTopology;




	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
	    "abstractSubTopologyWithDatabaseID"
	})
	@XmlRootElement(name = "AbstractSubTopologyList")
	public class AbstractSubTopologyList {

		 protected List<AbstractSubTopologyWithDatabaseID> abstractSubTopologyWithDatabaseID;


		    public List<AbstractSubTopologyWithDatabaseID> GetAbstractSubTopologyWithDatabaseIDList() {
		        if (abstractSubTopologyWithDatabaseID == null) {
		        	abstractSubTopologyWithDatabaseID = new ArrayList<AbstractSubTopologyWithDatabaseID>();
		        }
		        return this.abstractSubTopologyWithDatabaseID;
		    }

	    @XmlAccessorType(XmlAccessType.FIELD)
	    @XmlType(name = "", propOrder = {
	    		"databaseId","abstractSubTopology"
	    })
	    public static class AbstractSubTopologyWithDatabaseID {

	    	@XmlElement(name = "abstractSubTopology", required = true)
	        protected AbstractSubTopology abstractSubTopology;
	        @XmlAttribute(name = "databaseId", required = true)
	        protected long databaseId;
			public AbstractSubTopology getAbstractSubTopology() {
				return abstractSubTopology;
			}
			public void setAbstractSubTopology(AbstractSubTopology abstractSubTopology) {
				this.abstractSubTopology = abstractSubTopology;
			}
			public long getDatabaseId() {
				return databaseId;
			}
			public void setDatabaseId(long databaseId) {
				this.databaseId = databaseId;
			}
            
	        
	        
	    }
	  
}
