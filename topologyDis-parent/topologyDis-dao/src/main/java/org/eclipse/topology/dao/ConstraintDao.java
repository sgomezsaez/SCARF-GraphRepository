package org.eclipse.topology.dao;


import org.eclipse.topology.domain.Constraint;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

public class ConstraintDao {

	public enum constraintLabel implements Label {
		CONSTRAINT;
	}

	public Node create(Constraint constraint) {
		// TODO Auto-generated method stub
		GraphDatabaseService db = DbCon.GetDbConnect();

		Node constraintDb = db.createNode(constraintLabel.CONSTRAINT);
		constraintDb.setProperty( "Type", constraint.getConstraintType());
		constraintDb.setProperty( "Content",constraint.getAny().toString());
		constraintDb.setProperty("Property", constraint.getProperty());
		return constraintDb;

	}

}
