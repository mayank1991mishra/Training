package com.lexnarro.response;

import org.simpleframework.xml.Element;

import java.io.Serializable;

public class Plans implements Serializable {

    public String getPlan_ID() {
        return Plan_ID;
    }

    public String getPlan() {
        return Plan;
    }

    public String getRate_Id() {
        return Rate_Id;
    }

    public String getAmount() {
        return Amount;
    }

    @Element(name = "Plan_ID", required = false)
    private
    String Plan_ID;
    @Element(name = "Plan", required = false)
    private
    String Plan;
    @Element(name = "Rate_Id", required = false)
    private
    String Rate_Id;
    @Element(name = "Amount", required = false)
    private
    String Amount;
}
