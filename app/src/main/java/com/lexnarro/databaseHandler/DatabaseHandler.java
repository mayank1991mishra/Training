package com.lexnarro.databaseHandler;

/**
 * Created by saurabhsriomi on 13/10/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lexnarro.response.Profile;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DatabaseHandler extends SQLiteOpenHelper implements DatabaseInterface {

    /**
     * TABLE_NAME FOR LOCALS DATABASE
     */
    private static final String TABLE_PROFILE = "profile";
    /**
     * DATABASE VERSION
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * DATABASE NAME
     */

    private static final String DATABASE_NAME = "lexnarrow";


    //    Id Common Key For Multi Table
    private static final String KEY_ID = "id";
    private static final String KEY_FirstName = "f_name";
    private static final String KEY_LastName = "l_name";
    private static final String KEY_OtherName = "o_name";
    private static final String KEY_StreetNumber = "street_no";
    private static final String KEY_StreetName = "street_name";
    private static final String KEY_PostCode = "pin_code";
    private static final String KEY_Suburb = "suburb";
    private static final String KEY_StateID = "state_id";
    private static final String KEY_StateName = "state_name";
    private static final String KEY_CountryID = "country_id";
    private static final String KEY_CountryName = "country_name";
    private static final String KEY_StateEnrolled = "state_enrolled";
    private static final String KEY_StateEnrolledName = "state_en_name";
    private static final String KEY_StateEnrolledShortName = "state_en_sh_name";
    private static final String KEY_LawSocietyNumber = "law_society_no";
    private static final String KEY_EmailAddress = "email";
    private static final String KEY_PhoneNumber = "phone";
    private static final String KEY_Date = "date";
    private static final String KEY_Address = "address";
    private static final String KEY_Device_Imei = "imei";
    private static final String KEY_Device_Token = "token";
    private static final String KEY_Device_Type = "type";
    private static final String KEY_IsDeleted = "deleted";
    private static final String KEY_AccountConfirmed = "confirmed";
    private static final String KEY_ActivationCode = "code";
    private static final String KEY_MailUnsubscribed = "unsubscribed";
    private static final String KEY_Firm = "firm";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE_PROFILE +
                " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FirstName + " TEXT ,"
                + KEY_LastName + " TEXT ,"
                + KEY_OtherName + " TEXT,"
                + KEY_StreetNumber + " TEXT,"
                + KEY_StreetName + " TEXT,"
                + KEY_PostCode + " TEXT,"
                + KEY_Suburb + " TEXT,"
                + KEY_StateID + " TEXT,"
                + KEY_StateName + " TEXT,"
                + KEY_CountryID + " TEXT,"
                + KEY_CountryName + " TEXT,"
                + KEY_StateEnrolled + " TEXT,"
                + KEY_StateEnrolledName + " TEXT,"
                + KEY_StateEnrolledShortName + " TEXT,"
                + KEY_LawSocietyNumber + " TEXT,"
                + KEY_EmailAddress + " TEXT,"
                + KEY_PhoneNumber + " TEXT,"
                + KEY_Date + " TEXT,"
                + KEY_Address + " TEXT,"
                + KEY_Device_Imei + " TEXT,"
                + KEY_Device_Token + " TEXT,"
                + KEY_Device_Type + " TEXT,"
                + KEY_IsDeleted + " TEXT,"
                + KEY_AccountConfirmed + " TEXT,"
                + KEY_ActivationCode + " TEXT,"
                + KEY_MailUnsubscribed + " TEXT,"
                + KEY_Firm + " TEXT)";
        db.execSQL(CREATE_TABLE_PROFILE);

    }


//    Create Table Operation For Block User


//   All Database functionality starts *********************************************************************//`

    public static synchronized DatabaseInterface getInstance(Context context) {
        return new DatabaseHandler(context).create(DatabaseInterface.class);
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
        // Android (http://b.android.com/58753) but it forces composition of API declarations which is
        // the recommended pattern.
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> service) {
        validateServiceInterface(service);

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        if (method.getDeclaringClass() == InterfaceAddress.class) {
//                            return method.invoke(this, args);
//                        }
//                        return null;
                        return method.invoke(this, args);
                    }
                });
    }


    //Method for deleting all data at the time of login..
    public void clearAllTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILE, null, null);
    }

    //Methods for deleting data independently....
    public void clearComputerCourses() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILE, null, null);
        db.close();

    }


    public void deleteRecord(String id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_PROFILE, KEY_ID + " = " + id, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//Database Method implementation Starts


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        String DROP_TABLE_PROFILE = "DROP TABLE IF EXISTS " + TABLE_PROFILE;
        db.execSQL(DROP_TABLE_PROFILE);
        // Create tables again
        onCreate(db);
    }

    @Override
    public void addProfile(Profile profile) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, profile.getID());
        values.put(KEY_FirstName, profile.getFirstName());
        values.put(KEY_LastName, profile.getLastName());
        values.put(KEY_OtherName, profile.getOtherName());
        values.put(KEY_StreetNumber, profile.getStreetNumber());
        values.put(KEY_StreetName, profile.getStreetName());
        values.put(KEY_PostCode, profile.getPostCode());
        values.put(KEY_Suburb, profile.getSuburb());
        values.put(KEY_StateID, profile.getStateID());
        values.put(KEY_StateName, profile.getStateName());
        values.put(KEY_CountryID, profile.getCountryID());
        values.put(KEY_CountryName, profile.getCountryName());
        values.put(KEY_StateEnrolled, profile.getStateEnrolled());
        values.put(KEY_StateEnrolledName, profile.getStateEnrolledName());
        values.put(KEY_StateEnrolledShortName, profile.getStateEnrolledShortName());
        values.put(KEY_LawSocietyNumber, profile.getLawSocietyNumber());
        values.put(KEY_EmailAddress, profile.getEmailAddress());
        values.put(KEY_PhoneNumber, profile.getPhoneNumber());
        values.put(KEY_Date, profile.getDate());
        values.put(KEY_Address, profile.getAddress());
        values.put(KEY_Device_Imei, profile.getDevice_Imei());
        values.put(KEY_Device_Token, profile.getDevice_Token());
        values.put(KEY_Device_Type, profile.getDevice_Type());
        values.put(KEY_IsDeleted, profile.getIsDeleted());
        values.put(KEY_AccountConfirmed, profile.getAccountConfirmed());
        values.put(KEY_ActivationCode, profile.getActivationCode());
        values.put(KEY_MailUnsubscribed, profile.getMailUnsubscribed());
        values.put(KEY_Firm, profile.getFirm());
        db.insertWithOnConflict(TABLE_PROFILE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        this.close();

    }

    @Override
    public Profile getProfile() {

        SQLiteDatabase db = this.getReadableDatabase();
        Profile profile = new Profile();
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            profile.setID(cursor.getString(0));
            profile.setFirstName(cursor.getString(1));
            profile.setLastName(cursor.getString(2));
            profile.setOtherName(cursor.getString(3));
            profile.setStreetNumber(cursor.getString(4));
            profile.setStreetName(cursor.getString(5));
            profile.setPostCode(cursor.getString(6));
            profile.setSuburb(cursor.getString(7));
            profile.setStateID(cursor.getString(8));
            profile.setStateName(cursor.getString(9));
            profile.setCountryID(cursor.getString(10));
            profile.setCountryName(cursor.getString(11));
            profile.setStateEnrolled(cursor.getString(12));
            profile.setStateEnrolledName(cursor.getString(13));
            profile.setStateEnrolledShortName(cursor.getString(14));
            profile.setLawSocietyNumber(cursor.getString(15));
            profile.setEmailAddress(cursor.getString(16));
            profile.setPhoneNumber(cursor.getString(17));
            profile.setDate(cursor.getString(18));
            profile.setAddress(cursor.getString(19));
            profile.setDevice_Imei(cursor.getString(20));
            profile.setDevice_Token(cursor.getString(21));
            profile.setDevice_Type(cursor.getString(22));
            profile.setIsDeleted(cursor.getString(23));
            profile.setAccountConfirmed(cursor.getString(24));
            profile.setActivationCode(cursor.getString(25));
            profile.setMailUnsubscribed(cursor.getString(26));
            profile.setFirm(cursor.getString(27));

        }
        cursor.close();
        db.close();
        this.close();
        return profile;
    }


//    Locals Listener Interface Methods Implementation Starts


}