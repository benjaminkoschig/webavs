package ch.globaz.lyra.business.models.echeance;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LYSimpleEcheance extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDomaineApplicatif;
    private String descriptionEcheance;
    private String idEcheance;
    private String jspProcessEcheance;
    private String numeroOrdre;
    private String processEcheance;

    public LYSimpleEcheance() {
        super();

        descriptionEcheance = "";
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
