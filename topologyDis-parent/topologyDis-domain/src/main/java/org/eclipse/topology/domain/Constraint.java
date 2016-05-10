package org.eclipse.topology.domain;

import org.w3c.dom.Element;

/**
 * <p>Java class for tConstraint complex type.
 * @author Hao
 *
 */

public class Constraint {
	

		protected Object any;
	    protected String constraintType;
	    protected String property;
	    
	    /**
	     * Get the value of Constrains property
	     * @return
	     */
	    public String getProperty() {
		return property;
	    }
	    
	    /**
	     * Set the value of Constrains property
	     * @param property
	     */
	    public void setProperty(String property) {
		this.property = property;
	    }
	    /**
	     * Gets the value of the any property.
	     * 
	     * @return
	     *     possible object is
	     *     {@link Object }
	     *     {@link Element }
	     *     
	     */
	    public Object getAny() {
	        return any;
	    }
	    /**
	     * Sets the value of the any property.
	     * 
	     * @param value
	     *     allowed object is
	     *     {@link Object }
	     *     {@link Element }
	     *     
	     */
	    public void setAny(Object value) {
	        this.any = value;
	    }
        
	    /**
	     * Gets the value of the constraintType property.
	     * 
	     * @return
	     *     possible object is
	     *     {@link String }
	     *     
	     */
	    public String getConstraintType() {
	        return constraintType;
	    }
	    /**
	     * Sets the value of the constraintType property.
	     * 
	     * @param value
	     *     allowed object is
	     *     {@link String }
	     *     
	     */
	    public void setConstraintType(String value) {
	        this.constraintType = value;
	    }

}
