package org.applab.ledgerlink.client.mob.admin.model;

import java.util.Date;

/**
 *
 */
public class VslaKit {

    private String kitId;
    private String phoneNumber;
    private String phoneImei;
    private String phoneModel;
    private String phoneManufacturer;
    private String chargeSetSerialNumber;
    private String chargeSetManufacturer;
    private String receivedBy;
    private Date lastModifiedDate;

    public String getKitId() {
        return kitId;
    }

    public void setKitId(String kitId) {
        this.kitId = kitId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneImei() {
        return phoneImei;
    }

    public void setPhoneImei(String phoneImei) {
        this.phoneImei = phoneImei;
    }

    public String getChargeSetSerialNumber() {
        return chargeSetSerialNumber;
    }

    public void setChargeSetSerialNumber(String chargeSetSerialNumber) {
        this.chargeSetSerialNumber = chargeSetSerialNumber;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getPhoneManufacturer() {
        return phoneManufacturer;
    }

    public void setPhoneManufacturer(String phoneManufacturer) {
        this.phoneManufacturer = phoneManufacturer;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getChargeSetManufacturer() {
        return chargeSetManufacturer;
    }

    public void setChargeSetManufacturer(String chargeSetManufacturer) {
        this.chargeSetManufacturer = chargeSetManufacturer;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
