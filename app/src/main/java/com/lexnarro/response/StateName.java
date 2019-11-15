package com.lexnarro.response;

import org.simpleframework.xml.Element;

import java.io.Serializable;

/**
 * Created by ubnt on 15/10/17.
 */
public class StateName implements Serializable {



    @Element(name = "Name", required = false)
    private String name;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Element(name = "ShortName", required = false)
    private String shortName;

    @Element(name = "Id", required = false)
    private String Id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
