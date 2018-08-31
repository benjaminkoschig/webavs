package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

public class BanqueInfoGSON implements Serializable {

    private static final long serialVersionUID = -4108710914769844096L;

    String idPortail;
    String idTiersExistant;
    String iban;
    String idLocalite;

    public String getIdTiersExistant() {
        return idTiersExistant;
    }

    public String getIban() {
        return iban;
    }

    public String getIdPortail() {
        return idPortail;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

}
