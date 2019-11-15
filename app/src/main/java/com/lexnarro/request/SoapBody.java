package com.lexnarro.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by Saurabh Srivastva on 21/12/18.
 */

@Root(name = "soap12:Body", strict = false)
public class SoapBody {

    @Element(name = "UserLogin", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Login.asmx")
    private SoapRequestData login;

    @Element(name = "UserRegistration", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Registration.asmx")
    private SoapRequestData signUp;


    @Element(name = "TrainingSummary", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Dashboard.asmx")
    private SoapRequestData dashboard;

    @Element(name = "CreateTraining", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData createTraining;

    @Element(name = "DeleteTraining", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData deleteTraining;

    @Element(name = "DetailTraining", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData detailTraining;

    @Element(name = "EditTraining", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData editTraining;

    @Element(name = "GetActivities", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData getActivities;

    @Element(name = "GetCategories", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData getCategories;

    @Element(name = "GetSubActivities", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData GetSubActivities;

    @Element(name = "GetTraining", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Training.asmx")
    private SoapRequestData getTraining;

    public void setTransactions(SoapRequestData getTransactions) {
        this.getTransactions = getTransactions;
    }

    @Element(name = "GetTransactions", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/UserTransaction.asmx")
    private SoapRequestData getTransactions;


    public void setMailPassword(SoapRequestData mailPassword) {
        this.mailPassword = mailPassword;
    }

    @Element(name = "mailPassword", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/ForgotPassword.asmx")
    private SoapRequestData mailPassword;

    public void setAllStates(SoapRequestData allStates) {
        this.allStates = allStates;
    }

    @Element(name = "GetAllStates", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/States.asmx")
    private SoapRequestData allStates;


    public void setUpdateProfile(SoapRequestData updateProfile) {
        this.updateProfile = updateProfile;
    }

    @Element(name = "UpdateUserProfile", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/UpdateProfile.asmx")
    private SoapRequestData updateProfile;

    public void setPlans(SoapRequestData plans) {
        this.plans = plans;
    }

    @Element(name = "GetPlans", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/PlanMaster.asmx")
    private SoapRequestData plans;

    public void setPostTransaction(SoapRequestData postTransaction) {
        this.postTransaction = postTransaction;
    }

    @Element(name = "PostTransaction", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/UserTransaction.asmx")
    private SoapRequestData postTransaction;

    public void setEmailInvoice(SoapRequestData emailInvoice) {
        this.emailInvoice = emailInvoice;
    }

    @Element(name = "EmailInvoice", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/DownloadAndEmailService.asmx")
    private SoapRequestData emailInvoice;

    public void setDownloadTrainingReport(SoapRequestData downloadTrainingReport) {
        this.downloadTrainingReport = downloadTrainingReport;
    }

    @Element(name = "DownloadTrainingReport", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/DownloadAndEmailService.asmx")
    private SoapRequestData downloadTrainingReport;

    public void setDownloadInvoice(SoapRequestData downloadInvoice) {
        this.downloadInvoice = downloadInvoice;
    }

    @Element(name = "DownloadInvoice", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/DownloadAndEmailService.asmx")
    private SoapRequestData downloadInvoice;

    public void setEmailTrainingReport(SoapRequestData emailTrainingReport) {
        this.emailTrainingReport = emailTrainingReport;
    }

    @Element(name = "EmailTrainingReport", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/DownloadAndEmailService.asmx")
    private SoapRequestData emailTrainingReport;

    public void setGetCarryOverRecords(SoapRequestData getCarryOverRecords) {
        this.getCarryOverRecords = getCarryOverRecords;
    }

    public void setDoCarryOver(SoapRequestData doCarryOver) {
        this.doCarryOver = doCarryOver;
    }

    @Element(name = "GetCarryOverRecords", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/carryOver.asmx")
    private SoapRequestData getCarryOverRecords;

    @Element(name = "DoCarryOver", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/carryOver.asmx")
    private SoapRequestData doCarryOver;

    public void setCreateTraining(SoapRequestData createTraining) {
        this.createTraining = createTraining;
    }

    public void setDeleteTraining(SoapRequestData deleteTraining) {
        this.deleteTraining = deleteTraining;
    }

    public void setDetailTraining(SoapRequestData detailTraining) {
        this.detailTraining = detailTraining;
    }

    public void setEditTraining(SoapRequestData editTraining) {
        this.editTraining = editTraining;
    }

    public void setGetActivities(SoapRequestData getActivities) {
        this.getActivities = getActivities;
    }

    public void setGetCategories(SoapRequestData getCategories) {
        this.getCategories = getCategories;
    }

    public void setGetSubActivities(SoapRequestData getSubActivities) {
        GetSubActivities = getSubActivities;
    }

    public void setGetTraining(SoapRequestData getTraining) {
        this.getTraining = getTraining;
    }

    public void setSignUp(SoapRequestData signUp) {
        this.signUp = signUp;
    }

    public void setLogin(SoapRequestData login) {
        this.login = login;
    }

    public void setDashboard(SoapRequestData dashboard) {
        this.dashboard = dashboard;
    }

}
