//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.26 at 10:51:07 PM CET 
//


package org.eclipse.output.others;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.topology.domain.Performance;
import org.eclipse.topology.domain.Workload;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Workload" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="pattern" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="arrival" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="behavioral" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="avg_users" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="avg_transactions" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="startTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="endTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "performanceWithDatabaseID"
})
@XmlRootElement(name = "PerformanceList")
public class PerformanceList {

	 protected List<PerformanceList.PerformanceWithDatabaseID> performanceWithDatabaseID;


	    public List<PerformanceList.PerformanceWithDatabaseID> getPerformanceWithDatabaseID() {
	        if (performanceWithDatabaseID == null) {
	        	performanceWithDatabaseID = new ArrayList<PerformanceList.PerformanceWithDatabaseID>();
	        }
	        return this.performanceWithDatabaseID;
	    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
    		"databaseId","performance"
    })
    public static class PerformanceWithDatabaseID {

    	@XmlElement(name = "performance", required = true)
        protected Performance performance;
        @XmlAttribute(name = "databaseId", required = true)
        protected long databaseId;
		public Performance getPerformance() {
			return performance;
		}
		public void setPerformance(Performance performance) {
			this.performance = performance;
		}
		public long getDatabaseId() {
			return databaseId;
		}
		public void setDatabaseId(long databaseId) {
			this.databaseId = databaseId;
		}

        
        
    }
  

}