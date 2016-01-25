package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * 
 * Modèle de recherche des processus / traitement d'un template configuration
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
     * Critère CS du business processus
     */
    private String forCSBusinessProcessus = null;
    /**
     * Critère date début mm.YYYY
     */
    private String forDateDebut = null;
    /**
     * Critère date fin mm.YYYY
     */
    private String forDateFin = null;
    /**
     * Critère période
     */
    private String forDatePeriode = null;

    /**
     * Critère état du processus périodique
     */
    private String forEtatProcessusPeriodique = null;

    /**
     * Critère de recherche id processus périodique
     */
    private String forIdProcessusPeriodique = null;

    /**
     * Critère de recherche id traitement périodique
     */
    private String forIdTraitementPeriodique = null;

    /**
     * Critère de recherche template
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
     *            le critère CS business Processus
     */
    public void setForCSBusinessProcessus(String forCSBusinessProcessus) {
        this.forCSBusinessProcessus = forCSBusinessProcessus;
    }

    /**
     * 
     * @param forDateDebut
     *            critère date début
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            critère date fin
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forDatePeriode
     *            le critère période
     */
    public void setForDatePeriode(String forDatePeriode) {
        this.forDatePeriode = forDatePeriode;
    }

    /**
     * 
     * @param forEtatProcessusPeriodique
     *            critère forEtatProcessusPeriodique (ALCSProcessus.GROUP_ETAT_PROCESSUS)
     */
    public void setForEtatProcessusPeriodique(String forEtatProcessusPeriodique) {
        this.forEtatProcessusPeriodique = forEtatProcessusPeriodique;
    }

    /**
     * 
     * @param forIdProcessusPeriodique
     *            le critère id processus periodique
     */
    public void setForIdProcessusPeriodique(String forIdProcessusPeriodique) {
        this.forIdProcessusPeriodique = forIdProcessusPeriodique;
    }

    /**
     * @param forIdTraitementPeriodique
     *            le critère id traitement périodique
     */
    public void setForIdTraitementPeriodique(String forIdTraitementPeriodique) {
        this.forIdTraitementPeriodique = forIdTraitementPeriodique;
    }

    /**
     * @param forTemplate
     *            critère forTemplate
     */
    public void setForTemplate(String forTemplate) {
        this.forTemplate = forTemplate;
    }

    /**
     * 
     * @param inCSTraitement
     *            critère inCSTraitement
     */
    public void setInCSTraitement(Collection<String> inCSTraitement) {
        this.inCSTraitement = inCSTraitement;
    }

    /**
     * 
     * @param notInCSProcessus
     *            critère notInCSProcessus
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
