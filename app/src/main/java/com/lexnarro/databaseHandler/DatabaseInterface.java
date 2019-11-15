package com.lexnarro.databaseHandler;


import com.lexnarro.response.Profile;


/**
 * Created by saurabhsriomi on 14/10/15.
 */
public interface DatabaseInterface {

    void addProfile(Profile profile);

    Profile getProfile();

}
