package org.applab.ledgerlink.client.mob.admin.model;

/**
 * Created with IntelliJ IDEA.
 * VslaAdminUser: Sarahk
 * Date: 9/25/13
 * Time: 8:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class KitDelivery {

    private static final long serialVersionUID = 3185099950959560099L;

    private String vslaCode;
    private String adminUserName;
    private String vslaPhoneImei;
    private String vslaPhoneManufacturer;
    private String vslaPhoneMsisdn;
    private String vslaPhoneModel;
    private String vslaPhoneSerialNumber;
    private String recipientRole;
    private String deliveryDate;
    private String receivedBy;


    private int deliveryStatus;


    public KitDelivery() {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVslaCode() {
        return vslaCode;
    }

    public void setVslaCode(String vslaCode) {
        this.vslaCode = vslaCode;
    }

    public String getVslaPhoneImei() {
        return vslaPhoneImei;
    }

    public void setVslaPhoneImei(String vslaPhoneImei) {
        this.vslaPhoneImei = vslaPhoneImei;
    }

    public String getVslaPhoneManufacturer() {
        return vslaPhoneManufacturer;
    }

    public void setVslaPhoneManufacturer(String vslaPhoneManufacturer) {
        this.vslaPhoneManufacturer = vslaPhoneManufacturer;
    }

    public String getVslaPhoneMsisdn() {
        return vslaPhoneMsisdn;
    }

    public void setVslaPhoneMsisdn(String vslaPhoneMsisdn) {
        this.vslaPhoneMsisdn = vslaPhoneMsisdn;
    }

    public String getVslaPhoneModel() {
        return vslaPhoneModel;
    }

    public void setVslaPhoneModel(String vslaPhoneModel) {
        this.vslaPhoneModel = vslaPhoneModel;
    }

    public String getVslaPhoneSerialNumber() {
        return vslaPhoneSerialNumber;
    }

    public void setVslaPhoneSerialNumber(String vslaPhoneSerialNumber) {
        this.vslaPhoneSerialNumber = vslaPhoneSerialNumber;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}