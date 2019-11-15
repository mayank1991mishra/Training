package com.lexnarro.response;

import org.simpleframework.xml.Element;

import java.io.Serializable;

public class FinancialYear implements Serializable {

    @Element(name = "Disabled", required = false)
    private
    String disabled;
    @Element(name = "Selected", required = false)
    private
    String selected;
    @Element(name = "Text", required = false)
    private
    String text;
    @Element(name = "Value", required = false)
    private
    String value;




    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getDisabled() {
        return disabled.trim().equalsIgnoreCase("true");
    }

    public boolean getSelected() {
        return selected.trim().equalsIgnoreCase("true");
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
