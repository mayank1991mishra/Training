/*
 * Copyright 2016. Alejandro Sánchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.lexnarro.response;


import com.lexnarro.network.RootList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Class Description.
 *
 * @author Saurabh Srivastava.
 * @version 1.0.
 * @since 15/6/18.
 */

@RootList(value = {@Root(name = "UserLoginResult", strict = false), @Root(name = "TrainingSummaryResult", strict = false), @Root(name = "GetCategoriesResult", strict = false), @Root(name = "GetActivitiesResult", strict = false), @Root(name = "GetSubActivitiesResult", strict = false), @Root(name = "GetTrainingResult", strict = false), @Root(name = "CreateTrainingResult", strict = false), @Root(name = "EditTrainingResult", strict = false), @Root(name = "DeleteTrainingResult", strict = false), @Root(name = "GetTransactionsResult", strict = false), @Root(name = "mailPasswordResult", strict = false), @Root(name = "GetAllStatesResult", strict = false),
        @Root(name = "GetPlansResult", strict = false),
        @Root(name = "DownloadTrainingReportResult", strict = false),
        @Root(name = "EmailTrainingReportResult", strict = false),
        @Root(name = "DownloadInvoiceResult", strict = false),
        @Root(name = "EmailInvoiceResult", strict = false),
        @Root(name = "GetCarryOverRecordsResult", strict = false),
        @Root(name = "DoCarryOverResult", strict = false),
        @Root(name = "DetailTrainingResult", strict = false),
        @Root(name = "UpdateUserProfileResult", strict = false)})
public class SoapResult implements Serializable {


    @Element(name = "Token", required = false)
    private String Token;
    @ElementList(name = "categ", required = false)
    private List<MyCategory> category;
    @ElementList(name = "execCateg", required = false)
    private List<MyCategory> existingCategory;

    public List<StateName> getStates() {
        return states;
    }

    @ElementList(name = "States", required = false)
    private List<StateName> states;

    public List<FinancialYear> getFinancialYears() {
        return financialYears;
    }

    @ElementList(name = "FinancialYear", required = false)
    private List<FinancialYear> financialYears;

    public List<MyCategory> getUserCategories() {
        return userCategories;
    }

    @ElementList(name = "UserCategories", required = false)
    private List<MyCategory> userCategories;

    public List<TransactionRecord> getTransactions() {
        return transactions;
    }

    @ElementList(name = "Transactions", required = false)
    private List<TransactionRecord> transactions;

    public List<TrainingRecord> getUserTrainings() {
        return userTrainings;
    }

    @ElementList(name = "UserTraining", required = false)
    private List<TrainingRecord> userTrainings;

    public List<MyCategory> getUserActivity() {
        return userActivity;
    }

    public List<MyCategory> getUserSubActivity() {
        return userSubActivity;
    }

    @ElementList(name = "UserActivities", required = false)
    private List<MyCategory> userActivity;

    @ElementList(name = "UserSubActivities", required = false)
    private List<MyCategory> userSubActivity;

    public List<Plans> getPlans() {
        return plans;
    }

    @ElementList(name = "Plans", required = false)
    private List<Plans> plans;

    public List<MyCategory> getMyCategory() {
        return category;
    }

    public List<MyCategory> getExistingCategory() {
        return existingCategory;
    }

    @Element(name = "Status", required = false)
    private String Status;
    @Element(name = "Message", required = false)
    private String Message;
    @Element(name = "Requestkey", required = false)
    private String Requestkey;

    public String getMaximumUnitsAllowed() {
        return maximumUnitsAllowed;
    }

    @Element(name = "MaximumUnitsAllowed", required = false)
    private String maximumUnitsAllowed;

    public Profile getProfile() {
        return profile;
    }

    @Element(name = "Profile", required = false)
    private Profile profile;

    public Profile getUserProfile() {
        return userProfile;
    }

    @Element(name = "userProfile", required = false)
    private Profile userProfile;

    public String getDocumentUrl() {
        if (documentUrl==null){
            return "";
        }
        return documentUrl;
    }

    @Element(name = "DocumentUrl", required = false)
    private String documentUrl;


    public String getToken() {
        return Token;
    }

    public String getStatus() {
        if (Status == null) {
            return "FAILURE";
        }
        return Status;
    }

    public String getMessage() {
        if (Message == null) {
            return "Please try again!";
        }
        return Message;
    }

    public String getRequestkey() {
        return Requestkey;
    }

}
