/*
 * Globaz SA
 */
package ch.globaz.common.domaine;

import java.io.Serializable;

public class Tiers implements Serializable {

    private static final long serialVersionUID = 1L;
    private String canton;
    private String cantonCour;
    private String casePostal;
    private String designation1;
    private String designation2;
    private String localite;
    private String npa;
    private String numero;
    private String pays;
    private String paysIso;
    private String rue;
    private String titre;

    public String getCanton() {
        return canton;
    }

    public String getCantonCour() {
        return cantonCour;
    }

    public String getCasePostal() {
        return casePostal;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getLocalite() {
        return localite;
    }

    public String getNpa() {
        return npa;
    }

    public String getNumero() {
        return numero;
    }

    public String getPays() {
        return pays;
    }

    public String getPaysIso() {
        return paysIso;
    }

    public String getRue() {
        return rue;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCantonCour(String cantonCour) {
        this.cantonCour = cantonCour;
    }

    public void setCasePostal(String casePostal) {
        this.casePostal = casePostal;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setPaysIso(String paysIso) {
        this.paysIso = paysIso;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
