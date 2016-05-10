package org.eclipse.topology.dao;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
/**
 * this class is used to build the connection to the graph database, the directory of the graph database can be set here
 * @author Hao
 *
 */
public class DbCon {

	private static GraphDatabaseService  database;

    //private static final String DB_PATH = "C:/Users/Hao/Documents/Neo4j/SimilarTest";//Similarity Test
	//private static final String DB_PATH = "C:/Users/Hao/Documents/Neo4j/TGI_test_backup_3";// Testing for one concrete node with several instance node
    //private static final String DB_PATH = "C:/Users/Hao/Documents/Neo4j/TGI_test_backup";// Testing for one abstract node with several concrete node
	//private static final String DB_PATH = "C:/Users/Hao/Documents/Neo4j/TEST_EMPTY";// Testing for one concrete node with several instance node
	private static final String DB_PATH = "/var/lib/neo4j/data/graph.db";// for linux ubuntu default directory
    @SuppressWarnings("deprecation")
	public static GraphDatabaseService GetDbConnect(){

    	if(database!= null){
    		return database;
    	}

    	database = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
    	registerShutdownHook(database);
    	return database;
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

}
