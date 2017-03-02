package ch.globaz.naos.ree.domain.pojo;

import java.io.Serializable;

public class ProcessProperties implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String department;
    private String phone;
    private String email;
    private String other;
    private boolean validation;
    private String recipientId;

    private int tailleLot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getTailleLot() {
        return tailleLot;
    }

    public void setTailleLot(int paquet) {
        tailleLot = paquet;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public boolean isValidation() {
        return validation;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

}
