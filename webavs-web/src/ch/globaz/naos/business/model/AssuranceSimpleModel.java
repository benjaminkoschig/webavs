package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AssuranceSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String assurance13;
    private String assuranceCanton;
    private String assuranceGenre;
    private String assuranceId;
    private String assuranceLibelleAl;
    private String assuranceLibelleCourtAl;
    private String assuranceLibelleCourtFr;
    private String assuranceLibelleCourtIt;
    private String assuranceLibelleFr;
    private String assuranceLibelleIt;
    private String idAssuranceReference;
    private String rubriqueId;
    private String surDocAcompte;
    private String tauxParCaisse;
    private String typeAssurance;
    private String typeCalcul;

    public String getAssurance13() {
        return assurance13;
    }

    public String getAssuranceCanton() {
        return assuranceCanton;
    }

    public String getAssuranceGenre() {
        return assuranceGenre;
    }

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getAssuranceLibelleAl() {
        return assuranceLibelleAl;
    }

    public String getAssuranceLibelleCourtAl() {
        return assuranceLibelleCourtAl;
    }

    public String getAssuranceLibelleCourtFr() {
        return assuranceLibelleCourtFr;
    }

    public String getAssuranceLibelleCourtIt() {
        return assuranceLibelleCourtIt;
    }

    public String getAssuranceLibelleFr() {
        return assuranceLibelleFr;
    }

    public String getAssuranceLibelleIt() {
        return assuranceLibelleIt;
    }

    @Override
    public String getId() {
        return getAssuranceId();
    }

    public String getIdAssuranceReference() {
        return idAssuranceReference;
    }

    public String getRubriqueId() {
        return rubriqueId;
    }

    public String getSurDocAcompte() {
        return surDocAcompte;
    }

    public String getTauxParCaisse() {
        return tauxParCaisse;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public String getTypeCalcul() {
        return typeCalcul;
    }

    public void setAssurance13(String assurance13) {
        this.assurance13 = assurance13;
    }

    public void setAssuranceCanton(String assuranceCanton) {
        this.assuranceCanton = assuranceCanton;
    }

    public void setAssuranceGenre(String assuranceGenre) {
        this.assuranceGenre = assuranceGenre;
    }

    public void setAssuranceId(String assuranceId) {
        this.assuranceId = assuranceId;
    }

    public void setAssuranceLibelleAl(String assuranceLibelleAl) {
        this.assuranceLibelleAl = assuranceLibelleAl;
    }

    public void setAssuranceLibelleCourtAl(String assuranceLibelleCourtAl) {
        this.assuranceLibelleCourtAl = assuranceLibelleCourtAl;
    }

    public void setAssuranceLibelleCourtFr(String assuranceLibelleCourtFr) {
        this.assuranceLibelleCourtFr = assuranceLibelleCourtFr;
    }

    public void setAssuranceLibelleCourtIt(String assuranceLibelleCourtIt) {
        this.assuranceLibelleCourtIt = assuranceLibelleCourtIt;
    }

    public void setAssuranceLibelleFr(String assuranceLibelleFr) {
        this.assuranceLibelleFr = assuranceLibelleFr;
    }

    public void setAssuranceLibelleIt(String assuranceLibelleIt) {
        this.assuranceLibelleIt = assuranceLibelleIt;
    }

    @Override
    public void setId(String id) {
        setAssuranceId(id);
    }

    public void setIdAssuranceReference(String idAssuranceReference) {
        this.idAssuranceReference = idAssuranceReference;
    }

    public void setRubriqueId(String rubriqueId) {
        this.rubriqueId = rubriqueId;
    }

    public void setSurDocAcompte(String surDocAcompte) {
        this.surDocAcompte = surDocAcompte;
    }

    public void setTauxParCaisse(String tauxParCaisse) {
        this.tauxParCaisse = tauxParCaisse;
    }

    public void setTypeAssurance(String typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public void setTypeCalcul(String typeCalcul) {
        this.typeCalcul = typeCalcul;
    }

}
