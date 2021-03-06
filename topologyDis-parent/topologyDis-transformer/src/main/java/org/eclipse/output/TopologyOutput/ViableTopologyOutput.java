//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.29 at 11:50:28 AM CET 
//


package org.eclipse.output.TopologyOutput;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.toscaModel.Definitions;
import org.eclipse.toscaModel.TDefinitions;


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
 *         &lt;element name="alphaTopologyId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="alphaTopologyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="alphaTopologyNameSpace" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="specification" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="obsolete" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "alphaTopologyId",
    "alphaTopologyName",
    "alphaTopologyNameSpace",
    "definitions",
    "obsolete",
    "createDate",
    "endDate"
})
@XmlRootElement(name = "MuTopology")
public class ViableTopologyOutput {

    @XmlElement(required = true)
    protected String alphaTopologyId;
    @XmlElement(required = true)
    protected String alphaTopologyName;
    @XmlElement(required = true)
    protected String alphaTopologyNameSpace;
    @XmlElement(name = "Definitions", required = true,namespace="http://docs.oasis-open.org/tosca/ns/2011/12")
    protected org.eclipse.toscaModel.Definitions definitions;
    @XmlElement(required = true)
    protected String obsolete;
    @XmlElement(required = true)
    protected String createDate;
    @XmlElement(required = true)
    protected String endDate;

    /**
     * Gets the value of the alphaTopologyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaTopologyId() {
        return alphaTopologyId;
    }

    /**
     * Sets the value of the alphaTopologyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaTopologyId(String value) {
        this.alphaTopologyId = value;
    }

    /**
     * Gets the value of the alphaTopologyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaTopologyName() {
        return alphaTopologyName;
    }

    /**
     * Sets the value of the alphaTopologyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaTopologyName(String value) {
        this.alphaTopologyName = value;
    }

    /**
     * Gets the value of the alphaTopologyNameSpace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaTopologyNameSpace() {
        return alphaTopologyNameSpace;
    }

    /**
     * Sets the value of the alphaTopologyNameSpace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaTopologyNameSpace(String value) {
        this.alphaTopologyNameSpace = value;
    }

    /**
     * Gets the value of the specification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public org.eclipse.toscaModel.Definitions getSpecification() {
        return definitions;
    }

    /**
     * Sets the value of the specification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecification(org.eclipse.toscaModel.Definitions value) {
        this.definitions = value;
    }

    /**
     * Gets the value of the obsolete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObsolete() {
        return obsolete;
    }

    /**
     * Sets the value of the obsolete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObsolete(String value) {
        this.obsolete = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Definitions
        extends TDefinitions
    {


    }

}
