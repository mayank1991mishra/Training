package com.lexnarro.response;

import com.lexnarro.network.RootList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@RootList(value = {@Root(name = "MyCategoryFragment", strict = false)})
public class MyCategory implements Serializable {


    @Element(name = "CategoryID", required = false)
    String CategoryID;
    @Element(name = "ShortName", required = false)
    String ShortName;
    @Element(name = "Category_Name", required = false)
    String Category_Name;
    @Element(name = "Category_Id", required = false)
    String Category_Id;
    @Element(name = "Units_Done", required = false)
    String Units_Done;
    @Element(name = "Financial_Year", required = false)
    String Financial_Year;
    @Element(name = "Short_Name", required = false)
    String Short_Name;

    public String getActivity_ID() {
        return Activity_ID;
    }

    public String getStateID() {
        return StateID;
    }

    @Element(name = "Activity_ID", required = false)
    String Activity_ID;

    @Element(name = "StateID", required = false)
    String StateID;

    @Element(name = "ID", required = false)
    String id;

    @Element(name = "Name", required = false)
    String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMyCategoryID() {
        return CategoryID;
    }

    public String getMyShortName() {
        return ShortName;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public String getExistingCategoryId() {
        return Category_Id;
    }

    public String getUnits_Done() {
        return Units_Done;
    }

    public String getFinancial_Year() {
        return Financial_Year;
    }

    public String getExistingShort_Name() {
        return Short_Name;
    }
}
