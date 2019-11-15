package com.lexnarro.response;

import android.text.TextUtils;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrainingRecord implements Serializable {

    @Element(name = "Id", required = false)
    String Id;


    public String getTransactionId() {
        return transactionId;
    }

    @Element(name = "TransactionId", required = false)
    String transactionId;
    @Element(name = "User_Id", required = false)
    String user_Id;
    @Element(name = "FirstName", required = false)
    String firstName;
    @Element(name = "Date", required = false)
    String date;
    @Element(name = "StateId", required = false)
    String stateId;
    @Element(name = "StateName", required = false)
    String stateName;
    @Element(name = "CategoryId", required = false)
    String categoryId;
    @Element(name = "CategoryName", required = false)
    String categoryName;
    @Element(name = "ActivityId", required = false)
    String activityId;
    @Element(name = "ActivityName", required = false)
    String activityName;
    @Element(name = "SubActivityId", required = false)
    String subActivityId;
    @Element(name = "SubActivityName", required = false)
    String subActivityName;
    @Element(name = "Hours", required = false)
    String hours;

    public String getImageLink() {
        if (imageLink==null && TextUtils.isEmpty(imageLink)){
            return "https://lexnarro.com.au/Content/img/top%20picture.jpg";
        }
        return imageLink;
    }

    public String getFileName() {
        if (fileName==null && TextUtils.isEmpty(fileName)){
            return "Dummy_Image.JPG";
        }
        return fileName;
    }

    @Element(name = "ImageLink", required = false)
    String imageLink;
    @Element(name = "FileName", required = false)
    String fileName;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getEntered_unit() {
        return entered_unit;
    }

    public void setEntered_unit(String entered_unit) {
        this.entered_unit = entered_unit;
    }

    boolean selected;
    String entered_unit;

    public String getUnits() {
        return units;
    }

    @Element(name = "Units", required = false)
    String units;
    @Element(name = "Provider", required = false)
    String provider;
    @Element(name = "Financial_Year", required = false)
    String financial_Year;
    @Element(name = "Your_Role", required = false)
    String your_Role;
    @Element(name = "Forwardable", required = false)
    String forwardable;
    @Element(name = "Has_been_Forwarded", required = false)
    String as_been_Forwarded;
    @Element(name = "Descrption", required = false)
    String descrption;

    public String getId() {
        return Id;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getDate() {
        if (date != null && !date.trim().equalsIgnoreCase("")) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat date_Format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date d = date_Format.parse(date.split("T")[0]);
                return dateFormat.format(d);
            } catch (Exception e) {
                //java.text.ParseException: Unparseable date: Geting error
                System.out.println("Excep" + e);
            }
        }
        return "";
    }

    public String getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getSubActivityId() {

        return subActivityId;
    }

    public String getSubActivityName() {
        if (subActivityName == null || subActivityName.trim().equalsIgnoreCase("")) {
            return "N/A";
        }
        return subActivityName;
    }

    public String getHours() {
        return hours;
    }

    public String getProvider() {
        return provider;
    }

    public String getFinancial_Year() {
        return financial_Year;
    }

    public String getYour_Role() {
        return your_Role;
    }

    public String getForwardable() {
        return forwardable;
    }

    public String getAs_been_Forwarded() {
        return as_been_Forwarded;
    }

    public String getDescrption() {
        return descrption;
    }
}
