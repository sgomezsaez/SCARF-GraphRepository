package org.eclipse.topology.domain;

import javax.xml.bind.annotation.XmlElement;

public class TopologyStatisticsHistory {

	 String employDate;
	 String unemployDate;
	 long databaseId;
	@XmlElement
	public String getEmployDate() {
		return employDate;
	}
	public void setEmployDate(String employDate) {
		this.employDate = employDate;
	}
	@XmlElement
	public String getUnemployDate() {
		return unemployDate;
	}
	public void setUnemployDate(String unemployDate) {
		this.unemployDate = unemployDate;
	}
	@XmlElement
	public long getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}
	 	 
    	
}
