package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * 
 * Mod�le de recherche des processus / traitement d'un template configuration
 * 
 * @author GMO
 * 
 */
public class TemplateTraitementListComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Crit�re CS du business processus
     */
    private String forCSBusinessProcessus = null;
    /**
     * Crit�re date d�but mm.YYYY
     */
    private String forDateDebut = null;
    /**
     * Crit�re date fin mm.YYYY
     */
    private String forDateFin = null;
    /**
     * Crit�re p�riode
     */
    private String forDatePeriode = null;

    /**
     * Crit�re �tat du processus p�riodique
     */
    private String forEtatProcessusPeriodique = null;

    /**
     * Crit�re de recherche id processus p�riodique
     */
    private String forIdProcessusPeriodique = null;

    /**
     * Crit�re de recherche id traitement p�riodique
     */
    private String forIdTraitementPeriodique = null;

    /**
     * Crit�re de recherche template
     */
    private String forTemplate = null;

    /**
     * recherche traitement
     */
    private Collection<String> inCSTraitement = null;
    /**
     * exclusion des processus
     */
    private Collection<String> notInCSProcessus = null;

    /**
     * @return forCSBusinessProcessus
     */
    public String getForCSBusinessProcessus() {
        return forCSBusinessProcessus;
    }

    /**
     * 
     * @return forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return forDatePeriode
     */
    public String getForDatePeriode() {
        return forDatePeriode;
    }

    /**
     * @return forEtatProcessusPeriodique
     */
    public String getForEtatProcessusPeriodique() {
        return forEtatProcessusPeriodique;
    }

    /**
     * @return forIdProcessusPeriodique
     */
    public String getForIdProcessusPeriodique() {
        return forIdProcessusPeriodique;
    }

    /**
     * @return forIdTraitementPeriodique
     */
    public String getForIdTraitementPeriodique() {
        return forIdTraitementPeriodique;
    }

    /**
     * @return forTemplate
     */
    public String getForTemplate() {
        return forTemplate;
    }

    /**
     * 
     * @return inCSTraitement
     */
    public Collection<String> getInCSTraitement() {
        return inCSTraitement;
    }

    /**
     * 
     * @return notInCSProcessus
     */
    public Collection<String> getNotInCSProcessus() {
        return notInCSProcessus;
    }

    /**
     * @param forCSBusinessProcessus
     *            le crit�re CS business Processus
     */
    public void setForCSBusinessProcessus(String forCSBusinessProcessus) {
        this.forCSBusinessProcessus = forCSBusinessProcessus;
    }

    /**
     * 
     * @param forDateDebut
     *            crit�re date d�but
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            crit�re date fin
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forDatePeriode
     *            le crit�re p�riode
     */
    public void setForDatePeriode(String forDatePeriode) {
        this.forDatePeriode = forDatePeriode;
    }

    /**
     * 
     * @param forEtatProcessusPeriodique
     *            crit�re forEtatProcessusPeriodique (ALCSProcessus.GROUP_ETAT_PROCESSUS)
     */
    public void setForEtatProcessusPeriodique(String forEtatProcessusPeriodique) {
        this.forEtatProcessusPeriodique = forEtatProcessusPeriodique;
    }

    /**
     * 
     * @param forIdProcessusPeriodique
     *            le crit�re id processus periodique
     */
    public void setForIdProcessusPeriodique(String forIdProcessusPeriodique) {
        this.forIdProcessusPeriodique = forIdProcessusPeriodique;
    }

    /**
     * @param forIdTraitementPeriodique
     *            le crit�re id traitement p�riodique
     */
    public void setForIdTraitementPeriodique(String forIdTraitementPeriodique) {
        this.forIdTraitementPeriodique = forIdTraitementPeriodique;
    }

    /**
     * @param forTemplate
     *            crit�re forTemplate
     */
    public void setForTemplate(String forTemplate) {
        this.forTemplate = forTemplate;
    }

    /**
     * 
     * @param inCSTraitement
     *            crit�re inCSTraitement
     */
    public void setInCSTraitement(Collection<String> inCSTraitement) {
        this.inCSTraitement = inCSTraitement;
    }

    /**
     * 
     * @param notInCSProcessus
     *            crit�re notInCSProcessus
     */
    public void setNotInCSProcessus(Collection<String> notInCSProcessus) {
        this.notInCSProcessus = notInCSProcessus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return TemplateTraitementListComplexModel.class;
    }

}
