package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimplePCFAccordee extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private byte[] calcul = null;
    private String csEtat = null;
    private String dateCalcul = null;
    private String dateDecision = null;
    private String dateDiminution = null;
    private String dateDecisionValidation = null;
    private String timeDecisionValidation = null;
    private String excedantRevenu = null;
    private String idDemande = null;
    private String idPCFAccordee = null;
    private String montant = null;
    private Boolean onError = false;

    /**
     * @return the calcul
     */
    public byte[] getCalcul() {
        return calcul;
    }

    /**
     * @return the csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return the dateCalcul
     */
    public String getDateCalcul() {
        return dateCalcul;
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return the dateDiminution
     */
    public String getDateDiminution() {
        return dateDiminution;
    }

    /**
     * @return the dateTimeValidation
     */
    public String getDateTimeDecisionValidation() {
        String dateConcat = "";

        dateConcat = getDateDecisionValidation();
        if ("0".equals(getTimeDecisionValidation()) == false) {
            if (getTimeDecisionValidation().length() < 6) {
                // Si la date etait tomber sur des heure avec un 0 au début, le time ne sera plus avec 6 chiffres.
                // C'est pour cela que nous les ajoutons a nouveau selon le contexte.
                // Exemple : 00:05:25 => 525 en BD. Il faut donc ajouter 3x0 au début pour concatener.
                for (int i = 0; i < (6 - getTimeDecisionValidation().length()); i++) {
                    dateConcat += "0";
                }
            }

            dateConcat += getTimeDecisionValidation();
        }

        return dateConcat;
    }

    public String getDateDecisionValidation() {
        return dateDecisionValidation;
    }

    public void setDateDecisionValidation(String dateDecisionValidation) {
        this.dateDecisionValidation = dateDecisionValidation;
    }

    public String getTimeDecisionValidation() {
        return timeDecisionValidation;
    }

    public void setTimeDecisionValidation(String timeDecisionValidation) {
        this.timeDecisionValidation = timeDecisionValidation;
    }

    /**
     * @return the excedantRevenu
     */
    public String getExcedantRevenu() {
        return excedantRevenu;
    }

    @Override
    public String getId() {
        return idPCFAccordee;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idPCFAccordee
     */
    public String getIdPCFAccordee() {
        return idPCFAccordee;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the onError
     */
    public Boolean getOnError() {
        return onError;
    }

    /**
     * @param calcul
     *            the calcul to set
     */
    public void setCalcul(byte[] calcul) {
        this.calcul = calcul;
    }

    /**
     * @param csEtat
     *            the csEtat to set
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * @param dateCalcul
     *            the dateCalcul to set
     */
    public void setDateCalcul(String dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @param dateDiminution
     *            the dateDiminution to set
     */
    public void setDateDiminution(String dateDiminution) {
        this.dateDiminution = dateDiminution;
    }

    /**
     * @param dateTimeValidation
     *            the dateTimeValidation to set
     */
    public void setDateTimeDecisionValidation(String dateTimeDecisionValidation) {
        if (JadeStringUtil.isBlankOrZero(dateTimeDecisionValidation) == false) {
            setDateDecisionValidation(JadeStringUtil.substring(dateTimeDecisionValidation, 0, 8));
            setTimeDecisionValidation(JadeStringUtil.substring(dateTimeDecisionValidation, 8, 6));
        } else {
            setDateDecisionValidation("");
            setTimeDecisionValidation("");
        }
    }

    /**
     * @param excedantRevenu
     *            the excedantRevenu to set
     */
    public void setExcedantRevenu(String excedantRevenu) {
        this.excedantRevenu = excedantRevenu;
    }

    @Override
    public void setId(String id) {
        idPCFAccordee = id;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idPCFAccordee
     *            the idPCFAccordee to set
     */
    public void setIdPCFAccordee(String idPCFAccordee) {
        this.idPCFAccordee = idPCFAccordee;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param onError
     *            the onError to set
     */
    public void setOnError(Boolean onError) {
        this.onError = onError;
    }
}
