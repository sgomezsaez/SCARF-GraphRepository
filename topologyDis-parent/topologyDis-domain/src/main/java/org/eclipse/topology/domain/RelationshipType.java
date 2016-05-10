package org.eclipse.topology.domain;

import javax.xml.namespace.QName;

public class RelationshipType {

    protected RelationshipType.ValidSource validSource;

    protected RelationshipType.ValidTarget validTarget;
    
    protected String specification;
    
    protected String targetNamespace;
    
    protected String name;
    



	public String getTargetNamespace() {
		return targetNamespace;
	}


	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSpecification() {
		return specification;
	}


	public void setSpecification(String specification) {
		this.specification = specification;
	}


	public RelationshipType.ValidSource getValidSource() {
        return validSource;
    }


    public void setValidSource(RelationshipType.ValidSource value) {
        this.validSource = value;
    }


    public RelationshipType.ValidTarget getValidTarget() {
        return validTarget;
    }


    public void setValidTarget(RelationshipType.ValidTarget value) {
        this.validTarget = value;
    }
    public static class ValidSource {

        protected QName typeRef;


        public QName getTypeRef() {
            return typeRef;
        }


        public void setTypeRef(QName value) {
            this.typeRef = value;
        }

    }



    public static class ValidTarget {

        protected QName typeRef;


        public QName getTypeRef() {
            return typeRef;
        }


        public void setTypeRef(QName value) {
            this.typeRef = value;
        }

    }
}
