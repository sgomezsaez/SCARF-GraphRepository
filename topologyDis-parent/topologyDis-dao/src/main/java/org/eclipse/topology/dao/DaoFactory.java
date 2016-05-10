package org.eclipse.topology.dao;

/**
 * this class is used to generate DAO object
 * @author Hao
 *
 */
public class DaoFactory {

	//List of DAO types supported by the factory



    //
	private static DaoFactory daofactory = new DaoFactory();



	public static DaoFactory getFatory(){
		return daofactory;
	}

	public RequirementDao getRequirementDao(){

		return new RequirementDao();
	}

	public ConcreteNodeDao getNodeTypeDao(){
		return new ConcreteNodeDao();
	}

	public AbstractNodeDao getAbstractNodeDao( ){
		return new AbstractNodeDao( );
	}

	public RelationDao getRelationDao(){
		return new RelationDao();
	}

	public AbstractNodeIndexDao getAbstractNodeIndexDao(){
		return new AbstractNodeIndexDao();
	}

	public PreFixDao getPreFixDao(){
		return new PreFixDao();
	}

	public InstanceNodeDao getInstanceNodeDao(){
		return new InstanceNodeDao();
	}

	public RelationshipTypeDao getRelationshipTypeDao(){
		return new RelationshipTypeDao();
	}


	public AlphaTopologyIndexDao getAlphaTopologyIndexDao(){
		return new AlphaTopologyIndexDao();
	}

	public AlphaTopologyDao getAlphaTopologyDao(){
		return new AlphaTopologyDao();
	}

	public NodeDao getNodeDao(){
		return new NodeDao();
	}

	public WorkloadDao getWorkloadDao(){
		return new WorkloadDao();
	}

	public ConstraintDao getConstraintDao(){
		return new ConstraintDao();
	}

	public PerformanceDao getPerformanceDao(){
		return new PerformanceDao();
	}

	public HistoryDao getHistoryDao(){
		return new HistoryDao();
	}

	public MuTopologyDao getMuTopologyDao(){
		return new MuTopologyDao();
	}

	public CapabilityDao getCapabilityDao(){
		return new CapabilityDao();
	}

	public NameSpaceDao getNameSpaceDao(){
		return new NameSpaceDao();
	}
}
