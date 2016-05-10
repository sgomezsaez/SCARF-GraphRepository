package org.eclipse.topology.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;


public class Requirement extends DomainTemplate{
	
    protected Constraints constraints;
    protected String name;
    protected QName requirementType;
    protected Integer lowerBound;
    protected String upperBound;


    public Constraints getConstraints() {
        return constraints;
    }


    public void setConstraints(Constraints value) {
        this.constraints = value;
    }


    public String getName() {
        return name;
    }


    public void setName(String value) {
        this.name = value;
    }


    public QName getRequirementType() {
        return requirementType;
    }


    public void setRequirementType(QName value) {
        this.requirementType = value;
    }


    public int getLowerBound() {
        if (lowerBound == null) {
            return  1;
        } else {
            return lowerBound;
        }
    }


    public void setLowerBound(Integer value) {
        this.lowerBound = value;
    }


    public String getUpperBound() {
        if (upperBound == null) {
            return "1";
        } else {
            return upperBound;
        }
    }


    public void setUpperBound(String value) {
        this.upperBound = value;
    }


    public static class Constraints {

        protected List<Constraint> constraint;


        public List<Constraint> getConstraint() {
            if (constraint == null) {
                constraint = new ArrayList<Constraint>();
            }
            return this.constraint;
        }

    }

}
