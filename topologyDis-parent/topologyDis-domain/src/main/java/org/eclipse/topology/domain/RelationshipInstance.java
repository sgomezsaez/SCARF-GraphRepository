package org.eclipse.topology.domain;

import java.util.ArrayList;
import java.util.List;



public class RelationshipInstance extends DomainTemplate {

	    protected RelationshipInstance.SourceElement sourceElement;

	    protected RelationshipInstance.TargetElement targetElement;

	    protected RelationshipInstance.RelationshipConstraints relationshipConstraints;

	    protected String name;
	    
	    protected String specification;
	    
	    


	    public String getSpecification() {
			return specification;
		}


		public void setSpecification(String specification) {
			this.specification = specification;
		}


		public RelationshipInstance.SourceElement getSourceElement() {
	        return sourceElement;
	    }


	    public void setSourceElement(RelationshipInstance.SourceElement value) {
	        this.sourceElement = value;
	    }


	    public RelationshipInstance.TargetElement getTargetElement() {
	        return targetElement;
	    }


	    public void setTargetElement(RelationshipInstance.TargetElement value) {
	        this.targetElement = value;
	    }


	    public RelationshipInstance.RelationshipConstraints getRelationshipConstraints() {
	        return relationshipConstraints;
	    }


	    public void setRelationshipConstraints(RelationshipInstance.RelationshipConstraints value) {
	        this.relationshipConstraints = value;
	    }


	    public String getName() {
	        return name;
	    }


	    public void setName(String value) {
	        this.name = value;
	    }




	    public static class RelationshipConstraints {


	        protected List<RelationshipInstance.RelationshipConstraints.RelationshipConstraint> relationshipConstraint;


	        public List<RelationshipInstance.RelationshipConstraints.RelationshipConstraint> getRelationshipConstraint() {
	            if (relationshipConstraint == null) {
	                relationshipConstraint = new ArrayList<RelationshipInstance.RelationshipConstraints.RelationshipConstraint>();
	            }
	            return this.relationshipConstraint;
	        }



	        public static class RelationshipConstraint {


	            protected Object any;

	            protected String constraintType;


	            public Object getAny() {
	                return any;
	            }


	            public void setAny(Object value) {
	                this.any = value;
	            }


	            public String getConstraintType() {
	                return constraintType;
	            }


	            public void setConstraintType(String value) {
	                this.constraintType = value;
	            }

	        }

	    }



	    public static class SourceElement {


	        protected Object ref;


	        public Object getRef() {
	            return ref;
	        }


	        public void setRef(Object value) {
	            this.ref = value;
	        }

	    }



	    public static class TargetElement {


	        protected Object ref;


	        public Object getRef() {
	            return ref;
	        }


	        public void setRef(Object value) {
	            this.ref = value;
	        }

	    }
}
