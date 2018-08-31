package ch.globaz.vulpecula.business.models.absencejustifiee;

import ch.globaz.vulpecula.domain.models.common.Date;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AbsenceJustifieeSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -1591049846604973459L;

    public static final String ORDERBY_ID_PASSAGE_FACTURATION_DESC = "idPassageFacturationDesc";
    public static final String ORDER_BY_CONVENTION_ASC = "conventionAsc";
    public static final String ORDERBY_CONVENTION_RAISONSOCIALE_ASC = "conventionEmployeurRaisonSocialeAsc";

    public static final String WHERE_WITHDATE = "withPeriode";
    public static final String WHERE_WITH_PERIODE_ABSENCE = "withPeriodeAbsence";
    public static final String WHERE_WITH_PERIODE_PASSAGE_FACTURATION = "withPeriodePassage";
    public static final String WHERE_WITH_PERIODE_VERSEMENT = "withPeriodeVersement";
    public static final String WHERE_WITH_PERIODE_VERSEMENT_TRAITEMENT_SALAIRES = "withPeriodeVersementTraitementSalaires";

    private String forId;
    private String forIdTravailleur;
    private String forIdPassage;
    private String forIdEmployeur;
    private String forIdConvention;
    private String forDateDebut;
    private String forDateFin;
    private String forDateDebutPassageFacturation;
    private String forDateFinPassageFacturation;

    private String forDateDebutVersement;
    private String forDateFinVersement;

    private String forTraitementSalaires;

    public String getForTraitementSalaires() {
        return forTraitementSalaires;
    }

    public void setForTraitementSalaires(String forTraitementSalaires) {
        this.forTraitementSalaires = forTraitementSalaires;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public String getForDateDebutPassageFacturation() {
        return forDateDebutPassageFacturation;
    }

    public void setForDateDebutPassageFacturation(String forDateVersementDebut) {
        forDateDebutPassageFacturation = forDateVersementDebut;
    }

    public String getForDateFinPassageFacturation() {
        return forDateFinPassageFacturation;
    }

    public void setForDateFinPassageFacturation(String forDateVersementFin) {
        forDateFinPassageFacturation = forDateVersementFin;
    }

    public String getForDateFinVersement() {
        return forDateFinVersement;
    }

    public void setForDateFinVersement(String forDateFinVersement) {
        this.forDateFinVersement = forDateFinVersement;
    }

    public void setForDateFinVersement(Date forDateFinVersement) {
        this.forDateFinVersement = forDateFinVersement.getSwissValue();
    }

    public String getForDateDebutVersement() {
        return forDateDebutVersement;
    }

    public void setForDateDebutVersement(String forDateDebutVersement) {
        this.forDateDebutVersement = forDateDebutVersement;
    }

    public void setForDateDebutVersement(Date forDateDebutVersement) {
        this.forDateDebutVersement = forDateDebutVersement.getSwissValue();
    }

    @Override
    public Class<AbsenceJustifieeComplexModel> whichModelClass() {
        return AbsenceJustifieeComplexModel.class;
    }
}
