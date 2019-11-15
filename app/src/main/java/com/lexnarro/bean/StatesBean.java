package com.lexnarro.bean;

/**
 * Created by mobulous20 on 27/7/16.
 */
public class StatesBean {

    private String stateName;
    private String stateIsoCode;

    public StatesBean(String stateName, String stateIsoCode) {
        this.stateName = stateName;
        this.stateIsoCode = stateIsoCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateIsoCode() {
        return stateIsoCode;
    }

    public void setStateIsoCode(String stateIsoCode) {
        this.stateIsoCode = stateIsoCode;
    }


}
