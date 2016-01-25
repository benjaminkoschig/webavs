package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class IJSimpleDossierControleAbsences extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutFPI;
    private String dateDebutIJAI;
    private String idDossierControleAbsences;
    private String idTiers;
    private Boolean isHistorise;

    public IJSimpleDossierControleAbsences() {
        super();

        dateDebutFPI = "";
        dateDebutIJAI = "";
        idDossierControleAbsences = "";
        idTiers = "";
        isHistorise = Boolean.FALSE;
    }

    public String getDateDebutFPI() {
        return dateDebutFPI;
    }

    public String getDateDebutIJAI() {
        return dateDebutIJAI;
    }

    @Override
    public String getId() {
        return idDossierControleAbsences;
    }

    public final String getIdDossierControleAbsences() {
        return idDossierControleAbsences;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsHistorise() {
        return isHistorise;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public void setDateDebutFPI(String dateDebutFPI) {
        this.dateDebutFPI = dateDebutFPI;
    }

    public void setDateDebutIJAI(String dateDebutIJAI) {
        this.dateDebutIJAI = dateDebutIJAI;
    }

    @Override
    public void setId(String id) {
        idDossierControleAbsences = id;
    }

    public void setIdDossierControleAbsences(String idDossierControleAbsences) {
        this.idDossierControleAbsences = idDossierControleAbsences;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * Définit si le dossier de contrôle des absences est historisé
     * 
     * @param isHistorise
     *            si le dossier de contrôle des absences est historisé
     */
    public final void setIsHistorise(Boolean isHistorise) {
        this.isHistorise = isHistorise;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        toStringBuilder.append(this.getClass().getName());
        toStringBuilder.append("(idDossier=").append(idDossierControleAbsences).append(")");

        return toStringBuilder.toString();
    }
}
