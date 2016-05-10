package org.eclipse.topology.domain;

import java.util.ArrayList;
import java.util.List;





public class InstanceNode extends DomainTemplate {


	    protected InstanceNode.Requirements requirements;

	    protected InstanceNode.Capabilities capabilities;
	    
	    protected InstanceNode.Policies policies;
	    
	    protected String name;

	    protected String specification;

	    protected String concreteType;
	    
	    protected String topologyType;
	    
	    protected String nodeLevel;
	    
	    
	    
	    
	    
	    public String getNodeLevel() {
			return nodeLevel;
		}


		public void setNodeLevel(String nodeLevel) {
			this.nodeLevel = nodeLevel;
		}


		public String getTopologyType() {
			return topologyType;
		}


		public void setTopologyType(String topologyType) {
			this.topologyType = topologyType;
		}


		public String getSpecification() {
			return specification;
		}


		public void setSpecification(String specification) {
			this.specification = specification;
		}


		public InstanceNode.Requirements getRequirements() {
	        return requirements;
	    }


	    public void setRequirements(InstanceNode.Requirements value) {
	        this.requirements = value;
	    }


	    public InstanceNode.Capabilities getCapabilities() {
	        return capabilities;
	    }


	    public void setCapabilities(InstanceNode.Capabilities value) {
	        this.capabilities = value;
	    }


	    public InstanceNode.Policies getPolicies() {
	        return policies;
	    }


	    public void setPolicies(InstanceNode.Policies value) {
	        this.policies = value;
	    }



	    public String getName() {
	        return name;
	    }


	    public void setName(String value) {
	        this.name = value;
	    }

        



	    public String getConcreteType() {
			return concreteType;
		}


		public void setConcreteType(String concreteType) {
			this.concreteType = concreteType;
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



	    public static class Policies {

	        protected List<Policy> policy;


	        public List<Policy> getPolicy() {
	            if (policy == null) {
	                policy = new ArrayList<Policy>();
	            }
	            return this.policy;
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
