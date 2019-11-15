package com.lexnarro.response;

import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionRecord implements Serializable {

    @Element(name = "Id", required = false)
    String Id;
    @Element(name = "Rate_ID", required = false)
    String Rate_ID;
    @Element(name = "User_ID", required = false)
    String User_ID;
    @Element(name = "FirstName", required = false)
    String FirstName;
    @Element(name = "LastName", required = false)
    String LastName;
    @Element(name = "PlanID", required = false)
    String PlanID;
    @Element(name = "PlanName", required = false)
    String PlanName;
    @Element(name = "Amount", required = false)
    String Amount;
    @Element(name = "Start_Date", required = false)
    String Start_Date;
    @Element(name = "End_Date", required = false)
    String End_Date;
    @Element(name = "Payment_Status", required = false)
    String Payment_Status;
    @Element(name = "Transection_ID", required = false)
    String Transection_ID;
    @Element(name = "Status", required = false)
    String Status;
    @Element(name = "Payment_Date", required = false)
    String Payment_Date;
    @Element(name = "Invoice_No", required = false)
    String Invoice_No;
    @Element(name = "Payment_Method", required = false)
    String Payment_Method;

    public String getId() {
        return Id;
    }

    public String getRate_ID() {
        return Rate_ID;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPlanID() {
        return PlanID;
    }

    public String getPlanName() {
        return PlanName;
    }

    public String getAmount() {
        return "AUD "+Amount;
    }

    public String getStart_Date() {
        if (Start_Date != null && !Start_Date.trim().equalsIgnoreCase("")) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat date_Format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date d = date_Format.parse(Start_Date.split("T")[0]);
                return dateFormat.format(d);
            } catch (Exception e) {
                //java.text.ParseException: Unparseable date: Geting error
                System.out.println("Excep" + e);
            }
        }
        return "";
    }

    public String getEnd_Date() {

        if (End_Date != null && !End_Date.trim().equalsIgnoreCase("")) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat date_Format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date d = date_Format.parse(End_Date.split("T")[0]);
                return dateFormat.format(d);
            } catch (Exception e) {
                //java.text.ParseException: Unparseable date: Geting error
                System.out.println("Excep" + e);
            }
        }
        return "";
    }

    public String getPayment_Status() {
        return Payment_Status;
    }

    public String getTransection_ID() {
        return Transection_ID;
    }

    public String getStatus() {
        return Status;
    }

    public String getPayment_Date() {
        if (Payment_Date != null && !Payment_Date.trim().equalsIgnoreCase("")) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat date_Format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date d = date_Format.parse(Payment_Date.split("T")[0]);
                return dateFormat.format(d);
            } catch (Exception e) {
                //java.text.ParseException: Unparseable date: Geting error
                System.out.println("Excep" + e);
            }
        }
        return "";
    }

    public String getInvoice_No() {
        return Invoice_No;
    }

    public String getPayment_Method() {
        return Payment_Method;
    }
}
