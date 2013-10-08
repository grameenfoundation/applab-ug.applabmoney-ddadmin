package org.applab.ledgerlink.client.mob.admin.model;

import java.io.Serializable;

/**
 *
 */
public class Vsla implements Serializable {

    private static final long serialVersionUID = 3185099950959560099L;

    private String vslaId;
    private String name;
    private String vslaCode;
    private String passkey;
    private boolean kitDeliveryStatus;
    private String gpsLocation;
    private String regionId;

    public Vsla() {

    }

    public Vsla(String vslaCode, String name) {
        setVslaCode(vslaCode);
        setName(name);
    }

    public String getVslaId() {
        return vslaId;
    }

    public void setVslaId(String vslaId) {
        this.vslaId = vslaId;
    }

    public String getVslaCode() {
        return vslaCode;
    }

    public void setVslaCode(String vslaCode) {
        this.vslaCode = vslaCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public boolean isKitDeliveryStatus() {
        return kitDeliveryStatus;
    }

    public void setKitDeliveryStatus(boolean kitDeliveryStatus) {
        this.kitDeliveryStatus = kitDeliveryStatus;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
