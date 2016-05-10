package org.eclipse.topology.domain;

import javax.xml.namespace.QName;

public class Policy

{


protected String name;

protected QName policyType;

protected QName policyRef;


public String getName() {
    return name;
}


public void setName(String value) {
    this.name = value;
}


public QName getPolicyType() {
    return policyType;
}


public void setPolicyType(QName value) {
    this.policyType = value;
}


public QName getPolicyRef() {
    return policyRef;
}


public void setPolicyRef(QName value) {
    this.policyRef = value;
}

}
