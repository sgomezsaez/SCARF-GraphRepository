package org.eclipse.Service.TopologyExplorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;

/**
 * @author Hao
 *
 */
public class Filter {

/*	List<Node> ConcreteNodeFilter = new ArrayList<Node>();
	List<Node> InstanceNodeFilter = new ArrayList<Node>();
	
	
	public List<Node> getConcreteNodeFilter() {
		return ConcreteNodeFilter;
	}
	public void setConcreteNodeFilter(List<Node> concreteNodeFilter) {
		ConcreteNodeFilter = concreteNodeFilter;
	}
	public List<Node> getInstanceNodeFilter() {
		return InstanceNodeFilter;
	}
	public void setInstanceNodeFilter(List<Node> instanceNodeFilter) {
		InstanceNodeFilter = instanceNodeFilter;
	}
	*/
	
   private Map<Node, List<Node>> abstractRootNodeToItsSearchedConcreteNodeMapper;

   public boolean ifConcreteNodeFiltered(Node abstractNode, Node concreteNode){
	   
	   if(abstractRootNodeToItsSearchedConcreteNodeMapper.containsKey(abstractNode)){
		    List<Node> concreteNodeList = abstractRootNodeToItsSearchedConcreteNodeMapper.get(abstractNode);
		     if(concreteNodeList.contains(concreteNode)){
		    	 return false;
		     }
		     else{
		    	 return true;
		     }
		   
	   }
	   else{
		   return false;//if the abstract node is not existed in mapper, it means all its concrete node should be considered
	   }
    
	   
   }
   
   
   
   
   
   
   
   
   public Map<Node, List<Node>> getAbstractRootNodeToItsSearchedConcreteNodeMapper() {
	  return abstractRootNodeToItsSearchedConcreteNodeMapper;
   }

   public void setAbstractRootNodeToItsSearchedConcreteNodeMapper(
		Map<Node, List<Node>> abstractRootNodeToItsSearchedConcreteNodeMapper) {
	  this.abstractRootNodeToItsSearchedConcreteNodeMapper = abstractRootNodeToItsSearchedConcreteNodeMapper;
   }
  
  
	
	
}
