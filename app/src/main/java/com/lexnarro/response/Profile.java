package com.lexnarro.response;

import org.simpleframework.xml.Element;

import java.io.Serializable;

public class Profile implements Serializable {


    @Element(name = "ID", required = false)
    private String ID;
    @Element(name = "FirstName", required = false)
    private String FirstName;
    @Element(name = "LastName", required = false)
    private String LastName;
    @Element(name = "OtherName", required = false)
    private String OtherName;
    @Element(name = "StreetNumber", required = false)
    private String StreetNumber;
    @Element(name = "StreetName", required = false)
    private String StreetName;
    @Element(name = "PostCode", required = false)
    private String PostCode;
    @Element(name = "Suburb", required = false)
    private String Suburb;
    @Element(name = "StateID", required = false)
    private String StateID;
    @Element(name = "StateName", required = false)
    private String StateName;
    @Element(name = "CountryID", required = false)
    private String CountryID;
    @Element(name = "CountryName", required = false)
    private String CountryName;
    @Element(name = "StateEnrolled", required = false)
    private String StateEnrolled;
    @Element(name = "StateEnrolledName", required = false)
    private String StateEnrolledName;
    @Element(name = "StateEnrolledShortName", required = false)
    private String StateEnrolledShortName;
    @Element(name = "LawSocietyNumber", required = false)
    private String LawSocietyNumber;
    @Element(name = "EmailAddress", required = false)
    private String EmailAddress;
    @Element(name = "PhoneNumber", required = false)
    private String PhoneNumber;
    @Element(name = "Date", required = false)
    private String Date;
    @Element(name = "Address", required = false)
    private String Address;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Element(name = "Password", required = false)
    private String password;

    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getOtherName() {
        return OtherName;
    }

    public String getStreetNumber() {
        return StreetNumber;
    }

    public String getStreetName() {
        return StreetName;
    }

    public String getPostCode() {
        return PostCode;
    }

    public String getSuburb() {
        if (Suburb==null){
            return "N/A";
        }
        return Suburb;
    }

    public String getStateID() {
        return StateID;
    }

    public String getStateName() {
        return StateName;
    }

    public String getCountryID() {
        return CountryID;
    }

    public String getCountryName() {
        return CountryName;
    }

    public String getStateEnrolled() {
        return StateEnrolled;
    }

    public String getStateEnrolledName() {
        return StateEnrolledName;
    }

    public String getStateEnrolledShortName() {
        return StateEnrolledShortName;
    }

    public String getLawSocietyNumber() {
        return LawSocietyNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getDate() {
        return Date;
    }

    public String getAddress() {
        return Address;
    }

    public String getDevice_Imei() {
        return Device_Imei;
    }

    public String getDevice_Token() {
        return Device_Token;
    }

    public String getDevice_Type() {
        return Device_Type;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public String getAccountConfirmed() {
        return AccountConfirmed;
    }

    public String getActivationCode() {
        return ActivationCode;
    }

    public String getMailUnsubscribed() {
        return MailUnsubscribed;
    }

    public String getFirm() {
        return Firm;
    }

    @Element(name = "Device_Imei", required = false)
    private String Device_Imei;
    @Element(name = "Device_Token", required = false)
    private String Device_Token;
    @Element(name = "Device_Type", required = false)
    private String Device_Type;
    @Element(name = "IsDeleted", required = false)
    private String IsDeleted;
    @Element(name = "AccountConfirmed", required = false)
    private String AccountConfirmed;
    @Element(name = "ActivationCode", required = false)
    private String ActivationCode;
    @Element(name = "MailUnsubscribed", required = false)
    private String MailUnsubscribed;
    @Element(name = "Firm", required = false)
    private String Firm;

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setOtherName(String otherName) {
        OtherName = otherName;
    }

    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
    }

    public void setStreetName(String streetName) {
        StreetName = streetName;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public void setCountryID(String countryID) {
        CountryID = countryID;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public void setStateEnrolled(String stateEnrolled) {
        StateEnrolled = stateEnrolled;
    }

    public void setStateEnrolledName(String stateEnrolledName) {
        StateEnrolledName = stateEnrolledName;
    }

    public void setStateEnrolledShortName(String stateEnrolledShortName) {
        StateEnrolledShortName = stateEnrolledShortName;
    }

    public void setLawSocietyNumber(String lawSocietyNumber) {
        LawSocietyNumber = lawSocietyNumber;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setDevice_Imei(String device_Imei) {
        Device_Imei = device_Imei;
    }

    public void setDevice_Token(String device_Token) {
        Device_Token = device_Token;
    }

    public void setDevice_Type(String device_Type) {
        Device_Type = device_Type;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public void setAccountConfirmed(String accountConfirmed) {
        AccountConfirmed = accountConfirmed;
    }

    public void setActivationCode(String activationCode) {
        ActivationCode = activationCode;
    }

    public void setMailUnsubscribed(String mailUnsubscribed) {
        MailUnsubscribed = mailUnsubscribed;
    }

    public void setFirm(String firm) {
        Firm = firm;
    }
}
