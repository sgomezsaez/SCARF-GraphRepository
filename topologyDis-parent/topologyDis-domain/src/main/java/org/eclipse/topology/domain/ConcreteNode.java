package org.eclipse.topology.domain;

import java.util.ArrayList;
import java.util.List;


public class ConcreteNode extends DomainTemplate{
	
	
	    
	    protected String name;
	    
	    protected String specification;

		public Requirements requirements;

	    public Capabilities capabilities;

        protected String abstractType;
        
        protected String targetNamespace;
        
    

		public String getTargetNamespace() {
			return targetNamespace;
		}


		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}


		public String getAbstractType() {
			return abstractType;
		}


		public void setAbstractType(String abstractType) {
			this.abstractType = abstractType;
		}


		public String getSpecification() {
	    	System.out.println("fsdf");
			return specification;
		}


		public void setSpecification(String specification) {
			this.specification = specification;
		}
		
		
	    public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


	    public Requirements getRequirements() {
	        return requirements;
	    }


	    public void setRequirements(Requirements value) {
	        this.requirements = value;
	    }


	    public Capabilities getCapabilities() {
	        return capabilities;
	    }


	    public void setCapabilities(Capabilities value) {
	        this.capabilities = value;
	    }







		public static class Capabilities {

	        protected List<Capability> capability;

	        public List<Capability> getCapability() {
	            if (capability == null) {
	            	capability = new ArrayList<Capability>();
	            }
	            return this.capability;
	        }

	    }




	    public static class Requirements {

	        protected List<Requirement> requirement;


	        public List<Requirement> getRequirement() { 
	            if (requirement == null) {
	            	requirement = new ArrayList<Requirement>();
	            }
	            return this.requirement;
	        }

	    }
	
	
}
