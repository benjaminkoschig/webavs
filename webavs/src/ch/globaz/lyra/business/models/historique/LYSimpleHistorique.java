package ch.globaz.lyra.business.models.historique;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LYSimpleHistorique extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtat;
    private String dateExecution;
    private String idEcheance;
    private String idHistorique;
    private String idLog;
    private String visaUtilisateur;

    public LYSimpleHistorique() {
        super();
        csEtat = "";
        dateExecution = "";
        idEcheance = "";
        idHistorique = "";
        idLog = "";
        visaUtilisateur = "";
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getDateExecution() {
        return dateExecution;
    }

    @Override
    public String getId() {
        return getIdHistorique();
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    public String getIdHistorique() {
        return idHistorique;
    }

    public String getIdLog() {
        return idLog;
    }

    public String getVisaUtilisateur() {
        return visaUtilisateur;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

    @Override
    public void setId(String id) {
        setIdHistorique(id);
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public void setIdHistorique(String idHistorique) {
        this.idHistorique = idHistorique;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public void setVisaUtilisateur(String visaUtilisateur) {
        this.visaUtilisateur = visaUtilisateur;
    }
}
