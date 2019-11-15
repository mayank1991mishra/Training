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
import org.simpleframework.xml.Root;

/**
 * Class Description.
 *
 * @author asanchezyu@gmail.com.
 * @version 1.0.
 * @since 15/6/16.
 */
@RootList(value = {@Root(name = "UserLoginResponse", strict = false),
        @Root(name = "UserRegistrationResponse", strict = false),
        @Root(name = "GetCategoriesResponse", strict = false),
        @Root(name = "GetActivitiesResponse", strict = false),
        @Root(name = "GetSubActivitiesResponse", strict = false),
        @Root(name = "GetTrainingResponse", strict = false),
        @Root(name = "CreateTrainingResponse", strict = false),
        @Root(name = "DeleteTrainingResponse", strict = false),
        @Root(name = "EditTrainingResponse", strict = false),
        @Root(name = "GetTransactionsResponse", strict = false),
        @Root(name = "mailPasswordResponse", strict = false),
        @Root(name = "GetAllStatesResponse", strict = false),
        @Root(name = "UpdateUserProfileResponse", strict = false),
        @Root(name = "GetPlansResponse", strict = false),
        @Root(name = "PostTransactionResponse", strict = false),
        @Root(name = "EmailInvoiceResponse", strict = false),
        @Root(name = "DownloadInvoiceResponse", strict = false),
        @Root(name = "EmailTrainingReportResponse", strict = false),
        @Root(name = "DownloadTrainingReportResponse", strict = false),
        @Root(name = "GetCarryOverRecordsResponse", strict = false),
        @Root(name = "DoCarryOverResponse", strict = false),
        @Root(name = "DetailTrainingResponse", strict = false),
        @Root(name = "TrainingSummaryResponse", strict = false)})
public class SoapResponse {


    @Element(name = "UserLoginResult", required = false)
    private SoapResult loginResult;

    @Element(name = "UserRegistrationResult", required = false)
    private SoapResult signUpResult;

    public SoapResult getTrainingResult() {
        return trainingResult;
    }

    @Element(name = "TrainingSummaryResult", required = false)
    private SoapResult trainingResult;

    public SoapResult getCategoryResult() {
        return categoryResult;
    }

    @Element(name = "GetCategoriesResult", required = false)
    private SoapResult categoryResult;

    @Element(name = "GetSubActivitiesResult", required = false)
    private SoapResult subActivityResult;

    @Element(name = "GetActivitiesResult", required = false)
    private SoapResult activityResult;

    public SoapResult getGetTrainingResult() {
        return getTrainingResult;
    }

    @Element(name = "GetTrainingResult", required = false)
    private SoapResult getTrainingResult;

    @Element(name = "CreateTrainingResult", required = false)
    private SoapResult createTrainingResult;

    @Element(name = "DeleteTrainingResult", required = false)
    private SoapResult deleteTrainingResult;
    @Element(name = "EditTrainingResult", required = false)
    private SoapResult editTrainingResult;

    public SoapResult getGetTransactionsResult() {
        return getTransactionsResult;
    }

    @Element(name = "GetTransactionsResult", required = false)
    private SoapResult getTransactionsResult;

    public SoapResult getMailPasswordResult() {
        return mailPasswordResult;
    }

    @Element(name = "mailPasswordResult", required = false)
    private SoapResult mailPasswordResult;

    public SoapResult getGetAllStatesResult() {
        return getAllStatesResult;
    }

    @Element(name = "GetAllStatesResult", required = false)
    private SoapResult getAllStatesResult;

    public SoapResult getUpdateUserProfileResult() {
        return updateUserProfileResult;
    }

    @Element(name = "UpdateUserProfileResult", required = false)
    private SoapResult updateUserProfileResult;

    public SoapResult getPlansResult() {
        return plansResult;
    }

    @Element(name = "GetPlansResult", required = false)
    private SoapResult plansResult;

    public SoapResult getPostTransactionResult() {
        return PostTransactionResult;
    }

    @Element(name = "PostTransactionResult", required = false)
    private SoapResult PostTransactionResult;

    public SoapResult getDownloadTrainingReportResult() {
        return downloadTrainingReportResult;
    }

    public SoapResult getEmailTrainingReportResult() {
        return emailTrainingReportResult;
    }

    public SoapResult getDownloadInvoiceResult() {
        return downloadInvoiceResult;
    }

    public SoapResult getEmailInvoiceResult() {
        return emailInvoiceResult;
    }

    @Element(name = "DownloadTrainingReportResult", required = false)
    private SoapResult downloadTrainingReportResult;

    @Element(name = "EmailTrainingReportResult", required = false)
    private SoapResult emailTrainingReportResult;

    @Element(name = "DownloadInvoiceResult", required = false)
    private SoapResult downloadInvoiceResult;

    @Element(name = "EmailInvoiceResult", required = false)
    private SoapResult emailInvoiceResult;

    public SoapResult getDetailTrainingResult() {
        return detailTrainingResult;
    }

    @Element(name = "DetailTrainingResult", required = false)
    private SoapResult detailTrainingResult;

    public SoapResult getDoCarryOverResult() {
        return doCarryOverResult;
    }

    public SoapResult getCarryOverRecordsResult() {
        return carryOverRecordsResult;
    }

    @Element(name = "DoCarryOverResult", required = false)
    private SoapResult doCarryOverResult;

    @Element(name = "GetCarryOverRecordsResult", required = false)
    private SoapResult carryOverRecordsResult;

    public SoapResult getSubActivityResult() {
        return subActivityResult;
    }

    public SoapResult getActivityResult() {
        return activityResult;
    }

    public SoapResult getLoginResult() {
        return loginResult;
    }

    public SoapResult getSignUpResult() {
        return signUpResult;
    }


    public SoapResult getDeleteTrainingResult() {
        return deleteTrainingResult;
    }

    public SoapResult getEditTrainingResult() {
        return editTrainingResult;
    }

    public SoapResult getCreateTrainingResult() {
        return createTrainingResult;
    }
}
