//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.06 at 11:26:50 AM CEST 
//


package org.eclipse.toscaModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tDeploymentArtifact complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDeploymentArtifact">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/tosca/ns/2011/12}tExtensibleElements">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="artifactType" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="artifactRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDeploymentArtifact")
public class TDeploymentArtifact
    extends TExtensibleElements
{

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "artifactType", required = true)
    protected QName artifactType;
    @XmlAttribute(name = "artifactRef")
    protected QName artifactRef;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the artifactType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getArtifactType() {
        return artifactType;
    }

    /**
     * Sets the value of the artifactType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setArtifactType(QName value) {
        this.artifactType = value;
    }

    /**
     * Gets the value of the artifactRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getArtifactRef() {
        return artifactRef;
    }

    /**
     * Sets the value of the artifactRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setArtifactRef(QName value) {
        this.artifactRef = value;
    }

}
