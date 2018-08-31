package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

public class AdresseInfoGSON implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2256392708831523002L;
    String rue;
    String rueNumero;
    String npa;
    String localite;
    String casePostale;

    public String getCasePostale() {
        return casePostale;
    }

    public void setCasePostale(String casePostale) {
        this.casePostale = casePostale;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getRueNumero() {
        return rueNumero;
    }

    public void setRueNumero(String rueNumero) {
        this.rueNumero = rueNumero;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

}
