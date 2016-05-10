package org.eclipse.Service;

import java.util.List;




import org.eclipse.output.others.NamespaceList;
import org.eclipse.output.others.NamespaceList.NamespaceWithDatabaseID;
import org.eclipse.topology.dao.DaoFactory;
import org.eclipse.topology.dao.DbCon;
import org.eclipse.topology.dao.NameSpaceDao;
import org.eclipse.topology.domain.NameSpace;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class NameSpaceService {

	        //Get DaoFactory
			private final DaoFactory daoFactory = new DaoFactory();
			//Get NodeTypeDao
			private final NameSpaceDao nameSpaceDao = daoFactory.getNameSpaceDao();
			GraphDatabaseService db = DbCon.GetDbConnect();
			
			public Node create(NameSpace nameSpace) {
				Node node = nameSpaceDao.create(nameSpace);			
				return node;
			}

            public NamespaceList getAllNamespace(){
            	NamespaceList namespaceList = new NamespaceList();
            	List<Node> allNameSpace = nameSpaceDao.getAllNameSpaces();
            	List<NamespaceWithDatabaseID> namesapceWithDatabaseID  = namespaceList.getNameSpaceWithDatabaseID();
            	for(Node oneNode:allNameSpace){
            		NameSpace oneNamesapce = new NameSpace();
		    		try ( Transaction tx = db.beginTx();){       
		    			oneNamesapce.setNamespaceurl((String) oneNode.getProperty("namespaceurl"));
		    			oneNamesapce.setId((String) oneNode.getProperty("id"));
		    			oneNamesapce.setPrefix((String) oneNode.getProperty("prefix"));
		    			NamespaceWithDatabaseID namespaceWithDatabaseID = new NamespaceWithDatabaseID();
		    			namespaceWithDatabaseID.setDatabaseId(oneNode.getId());
		    			namespaceWithDatabaseID.setNameSpace(oneNamesapce);
		    			namesapceWithDatabaseID.add(namespaceWithDatabaseID);
            	    }
            	}           	
				return namespaceList;           	
            }
            
            
            public NameSpace getOneNamespace(long id){
            	    NameSpace oneNamesapce = null;
            		List<Node> allNameSpace = nameSpaceDao.getAllNameSpaces();
            		Node oneNamespaceNode = null;
            		for(Node oneNode:allNameSpace){
            			if(oneNode.getId() == id){
            				oneNamespaceNode = oneNode;
            			}
            		}
            		if(oneNamespaceNode!=null){
                		 oneNamesapce = new NameSpace();
		    		   try ( Transaction tx = db.beginTx();){       
		    			oneNamesapce.setNamespaceurl((String) oneNamespaceNode.getProperty("namespaceurl"));
		    			oneNamesapce.setId((String) oneNamespaceNode.getProperty("id"));
		    			oneNamesapce.setPrefix((String) oneNamespaceNode.getProperty("prefix"));
            	       }   
            		}
				return oneNamesapce;           	
            }

			public boolean deleteOneNameSpace(long id) {
				Node node = nameSpaceDao.getNodeById(id);
				if(node!=null){
					nameSpaceDao.deleteNameSpace(id);	
				return true;
				}
				return false;
			}
}
