package org.applab.ledgerlink.client.mob.admin.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * VslaAdminUser: Sarahk
 * Date: 9/25/13
 * Time: 8:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class VslaAdminUser implements Serializable {

    private String userId;
    private String userName;
    private String password;
    private String securityToken;
    private int activationStatus;

    public VslaAdminUser(String userName, String password, String securityToken, int activationStatus) {
        this.userName = userName;
        this.password = password;
        this.securityToken = securityToken;
        this.activationStatus = activationStatus;
    }

    public VslaAdminUser() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(int activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
