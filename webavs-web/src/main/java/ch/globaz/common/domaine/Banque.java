/*
 * Globaz SA.
 */
package ch.globaz.common.domaine;

import java.io.Serializable;

public class Banque implements Serializable {

    private static final long serialVersionUID = 1L;
    private String adresse1;
    private String adresse2;
    private String adresse3;
    private String adresse4;
    private String canton;
    private String cantonCour;
    private String casePostal;
    private String ccp;
    private String codeOfas;
    private String compte;
    private String designation1;
    private String designation2;
    private String localite;
    private String npa;
    private String numero;
    private String pays;
    private String paysIso;
    private String rue;
    private String clearing;

    public String getAdresse1() {
        return adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public String getAdresse3() {
        return adresse3;
    }

    public String getAdresse4() {
        return adresse4;
    }

    public String getCanton() {
        return canton;
    }

    public String getCantonCour() {
        return cantonCour;
    }

    public String getCasePostal() {
        return casePostal;
    }

    public String getCcp() {
        return ccp;
    }

    public String getCodeOfas() {
        return codeOfas;
    }

    public String getCompte() {
        return compte;
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

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public void setAdresse3(String adresse3) {
        this.adresse3 = adresse3;
    }

    public void setAdresse4(String adresse4) {
        this.adresse4 = adresse4;
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

    public void setCcp(String ccp) {
        this.ccp = ccp;
    }

    public void setCodeOfas(String codeOfas) {
        this.codeOfas = codeOfas;
    }

    public void setCompte(String compte) {
        this.compte = compte;
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

    public String getClearing() {
        return clearing;
    }

    public void setClearing(String clearing) {
        this.clearing = clearing;
    }
}
