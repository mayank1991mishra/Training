package com.lexnarro.request;


import com.lexnarro.network.RootList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Saurabh Srivastva on 1/2/17.
 */
@RootList(value = {@Root(name = "UserLogin", strict = false), @Root(name = "UserRegistration", strict = false), @Root(name = "TrainingSummary", strict = false), @Root(name = "CreateTraining", strict = false), @Root(name = "GetTraining", strict = false), @Root(name = "GetTransactions", strict = false), @Root(name = "mailPassword", strict = false), @Root(name = "GetAllStates", strict = false), @Root(name = "UpdateUserProfile", strict = false), @Root(name = "PostTransaction", strict = false), @Root(name = "EmailInvoice", strict = false), @Root(name = "DownloadTrainingReport", strict = false), @Root(name = "DownloadInvoice", strict = false), @Root(name = "EmailTrainingReport", strict = false), @Root(name = "DoCarryOver", strict = false), @Root(name = "GetCarryOverRecords", strict = false)})

public class SoapRequestData {


    @Element(name = "User_Name", required = false)
    private String username;

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Element(name = "emailId", required = false)
    private String emailId;
    @Element(name = "User_Id", required = false)
    private String userId;

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    @Element(name = "user_Id", required = false)
    private String user_Id;


    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Element(name = "Password", required = false)
    private String password;
    @Element(name = "token", required = false)
    private String token;
    @Element(name = "FirstName", required = false)
    private String fname;
    @Element(name = "LastName", required = false)
    private String lname;
    @Element(name = "OtherName", required = false)
    private String oname;
    @Element(name = "StreetNumber", required = false)
    private String streetNumber;
    @Element(name = "StreetName", required = false)
    private String streetName;
    @Element(name = "PostCode", required = false)
    private String postCode;
    @Element(name = "Suburb", required = false)
    private String suburb;
    @Element(name = "stateId", required = false)
    private String stateId;

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Element(name = "ids", required = false)
    private String ids;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Element(name = "userId", required = false)
    private String userID;

    public void setUnits(String units) {
        this.units = units;
    }

    @Element(name = "units", required = false)
    private String units;

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Element(name = "stateName", required = false)
    private String stateName;

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Element(name = "countryId", required = false)
    private String countryId;
    @Element(name = "countryName", required = false)
    private String countryName;
    @Element(name = "StateEnrolledName", required = false)
    private String stateEnrolled;
    @Element(name = "LawSocietyNumber", required = false)
    private String lawSocietyNumber;
    @Element(name = "EmailAddress", required = false)
    private String email;
    @Element(name = "PhoneNumber", required = false)
    private String phonenumber;
    @Element(name = "Date", required = false)
    private String date;
    @Element(name = "Role_id", required = false)
    private String role;
    @Element(name = "Address", required = false)
    private String address;

    public void setState_Id(String state_Id) {
        this.state_Id = state_Id;
    }

    @Element(name = "State_Id", required = false)
    private String state_Id;

    public void setUserActivityId(String userActivityId) {
        this.userActivityId = userActivityId;
    }

    @Element(name = "activityId", required = false)
    private String userActivityId;
    @Element(name = "Category_Id", required = false)
    private String category_Id;

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }

    public void setActivity_Id(String activity_Id) {
        this.activity_Id = activity_Id;
    }

    @Element(name = "Activity_Id", required = false)
    private String activity_Id;

    @Element(name = "SubActivity_Id", required = false)
    private String subActivity_Id;

    public void setSubActivity_Id(String subActivity_Id) {
        this.subActivity_Id = subActivity_Id;
    }

    @Element(name = "Hours", required = false)
    private String hours;

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @Element(name = "recordId", required = false)
    private String recordId;

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Element(name = "Provider", required = false)
    private String provider;

    public void setYour_Role(String your_Role) {
        this.your_Role = your_Role;
    }

    @Element(name = "Your_Role", required = false)
    private String your_Role;

    @Element(name = "Forwardable", required = false)
    private String forwardable;

    @Element(name = "Has_been_Forwarded", required = false)
    private String has_been_forwarded;

    @Element(name = "Descrption", required = false)
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Element(name = "Device_Imei", required = false)
    private String imei;
    @Element(name = "Device_Token", required = false)
    private String deviceToken;
    @Element(name = "Device_Type", required = false)
    private String deviceType;

    @Element(name = "finYear", required = false)
    private String finYear;

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    @Element(name = "emailID", required = false)
    private String emailID;

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    @Element(name = "financialYear", required = false)
    private String financialYear;

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @Element(name = "email", required = false)
    private String user_email;

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Element(name = "userEmail", required = false)
    private String userEmail;

    public void setStateShortName(String stateShortName) {
        this.stateShortName = stateShortName;
    }

    @Element(name = "stateShortName", required = false)
    private String stateShortName;

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Element(name = "FileName", required = false)
    private String file_name;
    @Element(name = "File", required = false)
    private String file;

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Element(name = "invoiceId", required = false)
    private String invoiceId;

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public void setCountryName(String countryId) {
        this.countryName = countryId;
    }

    public void setStateEnrolled(String stateEnrolled) {
        this.stateEnrolled = stateEnrolled;
    }

    public void setLawSocietyNumber(String lawSocietyNumber) {
        this.lawSocietyNumber = lawSocietyNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public void setStreet_Name(String street_Name) {
        this.street_Name = street_Name;
    }

    public void setPost_Code(String post_Code) {
        this.post_Code = post_Code;
    }

    public void setSub_urb(String sub_urb) {
        this.sub_urb = sub_urb;
    }

    public void setLawSociety_Number(String lawSociety_Number) {
        this.lawSociety_Number = lawSociety_Number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfile_address(String profile_address) {
        this.profile_address = profile_address;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    @Element(name = "id", required = false)
    private String id;

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    @Element(name = "Id", required = false)
    private String deleteId;
    @Element(name = "firstName", required = false)
    private String firstName;
    @Element(name = "lastName", required = false)
    private String lastName;
    @Element(name = "otherName", required = false)
    private String otherName;
    @Element(name = "streetName", required = false)
    private String street_Name;

    public void setStreet_Number(String street_Number) {
        this.street_Number = street_Number;
    }

    @Element(name = "streetNumber", required = false)
    private String street_Number;

    @Element(name = "postCode", required = false)
    private String post_Code;
    @Element(name = "suburb", required = false)
    private String sub_urb;

    @Element(name = "lawSocietyNumber", required = false)
    private String lawSociety_Number;

    @Element(name = "phoneNumber", required = false)
    private String phoneNumber;
    @Element(name = "address", required = false)
    private String profile_address;

    @Element(name = "firm", required = false)
    private String firm;


    @Element(name = "Plan_ID", required = false)
    private String Plan_ID;


    @Element(name = "payerId", required = false)
    private String payerId;
    @Element(name = "Rate_Id", required = false)
    private String Rate_Id;
    @Element(name = "paymentId", required = false)
    private String paymentId;
    @Element(name = "Amount", required = false)
    private String Amount;
    @Element(name = "Transection_ID", required = false)
    private String Transection_ID;

    public void setPlan_ID(String plan_ID) {
        Plan_ID = plan_ID;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public void setRate_Id(String rate_Id) {
        Rate_Id = rate_Id;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setTransection_ID(String transection_ID) {
        Transection_ID = transection_ID;
    }
}
