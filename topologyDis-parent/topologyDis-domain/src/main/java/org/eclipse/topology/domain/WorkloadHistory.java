//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.26 at 10:57:31 AM CET 
//


package org.eclipse.topology.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="workLoadId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unemployDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="databaseId" type="{http://www.w3.org/2001/XMLSchema}byte"/>
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
    "workLoadId",
    "employDate",
    "unemployDate",
    "databaseId"
})
@XmlRootElement(name = "WorkloadHistory")
public class WorkloadHistory {

    @XmlElement(required = true)
    protected String workLoadId;
    @XmlElement(required = true)
    protected String employDate;
    @XmlElement(required = true)
    protected String unemployDate;
    protected long databaseId;

    /**
     * Gets the value of the workLoadId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkLoadId() {
        return workLoadId;
    }

    /**
     * Sets the value of the workLoadId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkLoadId(String value) {
        this.workLoadId = value;
    }

    /**
     * Gets the value of the employDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployDate() {
        return employDate;
    }

    /**
     * Sets the value of the employDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployDate(String value) {
        this.employDate = value;
    }

    /**
     * Gets the value of the unemployDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnemployDate() {
        return unemployDate;
    }

    /**
     * Sets the value of the unemployDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnemployDate(String value) {
        this.unemployDate = value;
    }

    /**
     * Gets the value of the databaseId property.
     * 
     */
    public long getDatabaseId() {
        return databaseId;
    }

    /**
     * Sets the value of the databaseId property.
     * 
     */
    public void setDatabaseId(long value) {
        this.databaseId = value;
    }

}