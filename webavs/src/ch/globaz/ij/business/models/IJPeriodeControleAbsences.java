package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.List;

public class IJPeriodeControleAbsences extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<IJSimpleAbsence> absences;
    private IJSimpleDossierControleAbsences dossier;
    private String joursPayes;
    /**
     * Simple message d'information traduit par le service
     */
    private String messageWarningSoldeNegatif;
    private IJSimplePeriodeControleAbsence periode;

    private String solde;

    public IJPeriodeControleAbsences() {
        super();

        absences = new ArrayList<IJSimpleAbsence>();
        dossier = new IJSimpleDossierControleAbsences();
        joursPayes = "";
        periode = new IJSimplePeriodeControleAbsence();
        solde = "";
        messageWarningSoldeNegatif = "";
    }

    public List<IJSimpleAbsence> getAbsences() {
        return absences;
    }

    public final String getDateDeDebut() {
        return periode.getDateDeDebut();
    }

    public final String getDateDeFin() {
        return periode.getDateDeFin();
    }

    public final String getDelaisAttente() {
        return periode.getDelaisAttente();
    }

    public IJSimpleDossierControleAbsences getDossier() {
        return dossier;
    }

    public final String getDroitIj() {
        return periode.getDroitIj();
    }

    @Override
    public String getId() {
        return periode.getIdPeriodeControleAbsence();
    }

    public String getIdPeriodeControleAbsence() {
        return periode.getIdPeriodeControleAbsence();
    }

    public String getJoursPayes() {
        return joursPayes;
    }

    public final String getJoursPayesSolde() {
        return periode.getJoursPayesSolde();
    }

    public final String getMessageWarningSoldeNegatif() {
        return messageWarningSoldeNegatif;
    }

    public final String getOrdre() {
        return periode.getOrdre();
    }

    public IJSimplePeriodeControleAbsence getPeriode() {
        return periode;
    }

    public String getSolde() {
        return solde;
    }

    @Override
    public String getSpy() {
        return periode.getSpy();
    }

    public void setAbsences(List<IJSimpleAbsence> absences) {
        this.absences = absences;
    }

    public void setDossier(IJSimpleDossierControleAbsences dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        periode.setIdPeriodeControleAbsence(id);
    }

    public void setJoursPayes(String joursPayes) {
        this.joursPayes = joursPayes;
    }

    public final void setMessageWarningSoldeNegatif(String messageWarningSoldeNegatif) {
        this.messageWarningSoldeNegatif = messageWarningSoldeNegatif;
    }

    public void setPeriode(IJSimplePeriodeControleAbsence periode) {
        this.periode = periode;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }

    @Override
    public void setSpy(String spy) {
        periode.setSpy(spy);
    }
}
