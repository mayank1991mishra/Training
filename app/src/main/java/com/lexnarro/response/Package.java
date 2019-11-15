package com.lexnarro.response;

import org.simpleframework.xml.Element;

/**
 * Created by ubnt on 15/10/17.
 */

public class Package {

    @Element(name = "PackageName", required = false)
    private String PackageName;

    @Element(name = "MinimumKMS", required = false)

    private String MinimumKMS;

    @Element(name = "MinimumHRS", required = false)
    private String MinimumHRS;
    @Element(name = "MinimumDays", required = false)
    private String MinimumDays;
    @Element(name = "BaseRate", required = false)
    private String BaseRate;
    @Element(name = "ExtraKmsrate", required = false)
    private String ExtraKmsrate;
    @Element(name = "ExtraHrsrate", required = false)
    private String ExtraHrsrate;
    @Element(name = "NightCharges", required = false)
    private String NightCharges;

    public String getDriverAllowance() {
        return DriverAllowance;
    }

    public String getNightStartTime() {
        return NightStartTime;
    }

    public String getNightEndTime() {
        return NightEndTime;
    }

    @Element(name = "DriverAllowance", required = false)
    private String DriverAllowance;
    @Element(name = "NightStartTime", required = false)
    private String NightStartTime;
    @Element(name = "NightEndTime", required = false)
    private String NightEndTime;

    public String getPackageName() {
        return PackageName;
    }

    public String getMinimumKMS() {
        return MinimumKMS;
    }

    public String getMinimumHRS() {
        return MinimumHRS;
    }

    public String getMinimumDays() {
        return MinimumDays;
    }

    public String getBaseRate() {
        return BaseRate;
    }

    public String getExtraKmsrate() {
        return ExtraKmsrate;
    }

    public String getExtraHrsrate() {
        return ExtraHrsrate;
    }

    public String getNightCharges() {
        return NightCharges;
    }
}
