package ch.globaz.lyra.business.models.echeance;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LYSimpleEcheance extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDomaineApplicatif;
    private String descriptionEcheance;
    private String descriptionEcheance_fr;
    private String descriptionEcheance_de;
    private String descriptionEcheance_it;
    private String idEcheance;
    private String jspProcessEcheance;
    private String numeroOrdre;
    private String processEcheance;

    public LYSimpleEcheance() {
        super();

        descriptionEcheance = "";
        descriptionEcheance_fr = "";
        descriptionEcheance_de = "";
        descriptionEcheance_it = "";
        csDomaineApplicatif = "";
        idEcheance = "";
        jspProcessEcheance = "";
        numeroOrdre = "";
        processEcheance = "";
    }

    public String getCsDomaineApplicatif() {
        return csDomaineApplicatif;
    }

    public String getDescriptionEcheance() {
        return descriptionEcheance;
    }

    public String getDescriptionEcheance_fr() {
        return descriptionEcheance_fr;
    }

    public String getDescriptionEcheance_de() {
        return descriptionEcheance_de;
    }

    public String getDescriptionEcheance_it() {
        return descriptionEcheance_it;
    }

    @Override
    public String getId() {
        return getIdEcheance();
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    public String getJspProcessEcheance() {
        return jspProcessEcheance;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public String getProcessEcheance() {
        return processEcheance;
    }

    public void setCsDomaineApplicatif(String csDomaineApplicatif) {
        this.csDomaineApplicatif = csDomaineApplicatif;
    }

    public void setDescriptionEcheance(String descriptionEcheance) {
        this.descriptionEcheance = descriptionEcheance;
    }

    public void setDescriptionEcheance_fr(String descriptionEcheance_fr) {
        this.descriptionEcheance_fr = descriptionEcheance_fr;
    }

    public void setDescriptionEcheance_de(String descriptionEcheance_de) {
        this.descriptionEcheance_de = descriptionEcheance_de;
    }

    public void setDescriptionEcheance_it(String descriptionEcheance_it) {
        this.descriptionEcheance_it = descriptionEcheance_it;
    }

    @Override
    public void setId(String id) {
        setIdEcheance(id);
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public void setJspProcessEcheance(String jspProcessEcheance) {
        this.jspProcessEcheance = jspProcessEcheance;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public void setProcessEcheance(String processEcheance) {
        this.processEcheance = processEcheance;
    }
}
