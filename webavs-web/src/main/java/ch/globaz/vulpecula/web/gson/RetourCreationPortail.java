package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

public class RetourCreationPortail implements Serializable {
    private String message;
    private String idPosteTravail;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    };

}
