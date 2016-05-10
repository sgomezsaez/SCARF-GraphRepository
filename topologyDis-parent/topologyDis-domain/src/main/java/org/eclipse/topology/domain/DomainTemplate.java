package org.eclipse.topology.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public abstract class DomainTemplate

{


protected DomainTemplate.Properties properties;

protected DomainTemplate.PropertyConstraints propertyConstraints;

protected String id;

protected QName type;


public DomainTemplate.Properties getProperties() {
    return properties;
}


public void setProperties(DomainTemplate.Properties value) {
    this.properties = value;
}


public DomainTemplate.PropertyConstraints getPropertyConstraints() {
    return propertyConstraints;
}


public void setPropertyConstraints(DomainTemplate.PropertyConstraints value) {
    this.propertyConstraints = value;
}


public String getId() {
    return id;
}


public void setId(String value) {
    this.id = value;
}


public QName getType() {
    return type;
}


public void setType(QName value) {
    this.type = value;
}


public static class Properties {


    protected Object any;


    public Object getAny() {
        return any;
    }


    public void setAny(Object value) {
        this.any = value;
    }

}


public static class PropertyConstraints {

    protected List<Constraint> propertyConstraint;


    public List<Constraint> getPropertyConstraint() {
        if (propertyConstraint == null) {
            propertyConstraint = new ArrayList<Constraint>();
        }
        return this.propertyConstraint;
    }

}

}