package org.applab.digdata.client.mob.admin.model;

import java.io.Serializable;

/**
 *
 */
public class Vsla implements Serializable {

    private static final long serialVersionUID = 3185099950959560099L;

    private String name;
    private String id;
    private String passkey;

    public Vsla() {

    }

    public Vsla(String id, String name, String passkey) {
        setId(id);
        setName(name);
        setPasskey(passkey);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
