package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;

public class EntetePrestationSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 8368762554395134626L;
    private String forId;
    private String forIdProcessus;
    private String forDateComptabilisationBeforeOrEquals;
    private String forDateComptabilisationAfterOrEquals;
    private String forCantonResidence;
    private String forIdAllocataire;
    private String forPeriodeDeAfterOrEquals;
    private String forPeriodeABeforeOrEquals;
    private Boolean forIsRetenueImpot;
    private String forIsRetenueImpotSomme;
    private String forNotRetenueImpotSomme;
    private String forEtat;
    private String forBonification;
    private String forPeriodeDeBeforeOrEquals;
    private String forAffiliationId;
    private String forIdDossier;

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public void setForAffiliationId(String forAffiliationId) {
        this.forAffiliationId = forAffiliationId;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdProcessus() {
        return forIdProcessus;
    }

    public void setForIdProcessus(String forIdProcessus) {
        this.forIdProcessus = forIdProcessus;
    }

    public String getForDateComptabilisationBeforeOrEquals() {
        return forDateComptabilisationBeforeOrEquals;
    }

    public void setForDateComptabilisationBeforeOrEquals(Date date) {
        forDateComptabilisationBeforeOrEquals = date.getSwissValue();
    }

    public void setForDateComptabilisationBeforeOrEquals(String forDateComptabilisationBeforeOrEquals) {
        this.forDateComptabilisationBeforeOrEquals = forDateComptabilisationBeforeOrEquals;
    }

    public String getForDateComptabilisationAfterOrEquals() {
        return forDateComptabilisationAfterOrEquals;
    }

    public void setForDateComptabilisationAfterOrEquals(Date date) {
        forDateComptabilisationAfterOrEquals = date.getSwissValue();
    }

    public void setForDateComptabilisationAfterOrEquals(String forDateComptabilisationAfterOrEquals) {
        this.forDateComptabilisationAfterOrEquals = forDateComptabilisationAfterOrEquals;
    }

    public void setForAnneeComptabilisation(Annee annee) {
        if (annee != null) {
            forDateComptabilisationBeforeOrEquals = annee.getLastDayOfYear().getSwissValue();
            forDateComptabilisationAfterOrEquals = annee.getFirstDayOfYear().getSwissValue();
        }
    }

    public String getForCantonResidence() {
        return forCantonResidence;
    }

    public void setForCantonResidence(String forCantonResidence) {
        this.forCantonResidence = forCantonResidence;
    }

    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    public String getForPeriodeDeAfterOrEquals() {
        return forPeriodeDeAfterOrEquals;
    }

    public void setForPeriodeDeAfterOrEquals(Date forPeriodeDeAfterOrEquals) {
        setForPeriodeDeAfterOrEquals(forPeriodeDeAfterOrEquals.getMoisAnneeFormatte());
    }

    public void setForPeriodeDeAfterOrEquals(String forPeriodeDeAfterOrEquals) {
        this.forPeriodeDeAfterOrEquals = forPeriodeDeAfterOrEquals;
    }

    public String getForPeriodeABeforeOrEquals() {
        return forPeriodeABeforeOrEquals;
    }

    public void setForPeriodeABeforeOrEquals(Date forPeriodeABeforeOrEquals) {
        setForPeriodeABeforeOrEquals(forPeriodeABeforeOrEquals.getMoisAnneeFormatte());
    }

    public void setForPeriodeABeforeOrEquals(String forPeriodeABeforeOrEquals) {
        this.forPeriodeABeforeOrEquals = forPeriodeABeforeOrEquals;
    }

    public Boolean getForIsRetenueImpot() {
        return forIsRetenueImpot;
    }

    public void setForIsRetenueImpot(Boolean forIsRetenueImpot) {
        this.forIsRetenueImpot = forIsRetenueImpot;
    }

    public String getForIsRetenueImpotSomme() {
        return forIsRetenueImpotSomme;
    }

    public void setForIsRetenueImpotSomme(String forIsRetenueImpotSomme) {
        this.forIsRetenueImpotSomme = forIsRetenueImpotSomme;
    }

    public String getForNotRetenueImpotSomme() {
        return forNotRetenueImpotSomme;
    }

    public void setForNotRetenueImpotSomme(String forNotRetenueImpotSomme) {
        this.forNotRetenueImpotSomme = forNotRetenueImpotSomme;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForBonification() {
        return forBonification;
    }

    public void setForBonification(String forBonification) {
        this.forBonification = forBonification;
    }

    public String getForPeriodeDeBeforeOrEquals() {
        return forPeriodeDeBeforeOrEquals;
    }

    public void setForPeriodeDeBeforeOrEquals(Date date) {
        forPeriodeDeBeforeOrEquals = date.getMoisAnneeFormatte();
    }

    public void setForPeriodeDeBeforeOrEquals(String forPeriodeDeBeforeOrEquals) {
        this.forPeriodeDeBeforeOrEquals = forPeriodeDeBeforeOrEquals;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class<EntetePrestationComplexModel> whichModelClass() {
        return EntetePrestationComplexModel.class;
    }
}
