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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Class Description.
 *
 * @author asanchezyu@gmail.com.
 * @version 1.0.
 * @since 15/6/16.
 */
@Root(name = "Body", strict = false)
public class SoapResponseBody {


    @Element(name = "UserLoginResponse", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au/services/Login.asmx")
    private SoapResponse loginResponse;

    @Element(name = "UserRegistrationResponse", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au.Registration/")
    private SoapResponse signUpResponse;

    public SoapResponse getTrainingResponse() {
        return trainingResponse;
    }

    @Element(name = "TrainingSummaryResponse", required = false)
    @Namespace(reference = "http://www.lexnarro.com.au.Dashboard/")
    private SoapResponse trainingResponse;


    @Element(name = "GetCategoriesResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse categoryResponse;

    @Element(name = "GetActivitiesResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse activityResponse;

    @Element(name = "GetSubActivitiesResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse subActivityResponse;

    public SoapResponse getGetTrainingResponse() {
        return getTrainingResponse;
    }

    @Element(name = "GetTrainingResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse getTrainingResponse;

    @Element(name = "CreateTrainingResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse createTrainingResponse;

    @Element(name = "EditTrainingResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse editTrainingResponse;

    @Element(name = "DeleteTrainingResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse deleteTrainingResponse;

    public SoapResponse getGetTransactionsResponse() {
        return getTransactionsResponse;
    }

    @Element(name = "GetTransactionsResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/UserTransaction.asmx")
    private SoapResponse getTransactionsResponse;

    public SoapResponse getMailPasswordResponse() {
        return mailPasswordResponse;
    }

    @Element(name = "mailPasswordResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/ForgotPassword.asmx")
    private SoapResponse mailPasswordResponse;

    public SoapResponse getAllStatesResponse() {
        return allStatesResponse;
    }

    @Element(name = "GetAllStatesResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse allStatesResponse;

    public SoapResponse getUpdateUserProfileResponse() {
        return updateUserProfileResponse;
    }

    @Element(name = "UpdateUserProfileResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/UpdateProfile.asmx")
    private SoapResponse updateUserProfileResponse;

    public SoapResponse getPlansResponse() {
        return plansResponse;
    }

    @Element(name = "GetPlansResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/PlanMaster.asmx")
    private SoapResponse plansResponse;


    public SoapResponse getPostTransactionResponse() {
        return PostTransactionResponse;
    }

    @Element(name = "PostTransactionResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/PostTransactionResponse.asmx")
    private SoapResponse PostTransactionResponse;

    public SoapResponse getDownloadTrainingReportResponse() {
        return downloadTrainingReportResponse;
    }

    public SoapResponse getEmailTrainingReportResponse() {
        return emailTrainingReportResponse;
    }

    public SoapResponse getDownloadInvoiceResponse() {
        return downloadInvoiceResponse;
    }

    public SoapResponse getEmailInvoiceResponse() {
        return emailInvoiceResponse;
    }

    @Element(name = "DownloadTrainingReportResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/DownloadAndEmailService.asmx")
    private SoapResponse downloadTrainingReportResponse;
    @Element(name = "EmailTrainingReportResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/DownloadAndEmailService.asmx")
    private SoapResponse emailTrainingReportResponse;
    @Element(name = "DownloadInvoiceResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/DownloadAndEmailService.asmx")
    private SoapResponse downloadInvoiceResponse;
    @Element(name = "EmailInvoiceResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/DownloadAndEmailService.asmx")
    private SoapResponse emailInvoiceResponse;

    public SoapResponse getDetailTrainingResponse() {
        return detailTrainingResponse;
    }

    @Element(name = "DetailTrainingResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/Training.asmx")
    private SoapResponse detailTrainingResponse;

    public SoapResponse getDoCarryOverResponse() {
        return doCarryOverResponse;
    }

    public SoapResponse getCarryOverRecordsResponse() {
        return CarryOverRecordsResponse;
    }

    @Element(name = "DoCarryOverResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/DownloadAndEmailService.asmx")
    private SoapResponse doCarryOverResponse;
    @Element(name = "GetCarryOverRecordsResponse", required = false)
    @Namespace(reference = "http://www.jajtechnologies.com/serives/DownloadAndEmailService.asmx")
    private SoapResponse CarryOverRecordsResponse ;

    public SoapResponse getCategoryResponse() {
        return categoryResponse;
    }

    public SoapResponse getActivityResponse() {
        return activityResponse;
    }

    public SoapResponse getSubActivityResponse() {
        return subActivityResponse;
    }

    public SoapResponse getLoginResponse() {
        return loginResponse;
    }

    public SoapResponse getSignUpResponse() {
        return signUpResponse;
    }


    public SoapResponse getCreateTrainingResponse() {
        return createTrainingResponse;
    }

    public SoapResponse getEditTrainingResponse() {
        return editTrainingResponse;
    }

    public SoapResponse getDeleteTrainingResponse() {
        return deleteTrainingResponse;
    }
}
