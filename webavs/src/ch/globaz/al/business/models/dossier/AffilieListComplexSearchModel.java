package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Modèle de recherche utilisé pour la récupération des numéros d'affilié lors de la génération globale de prestations
 * 
 * @author jts
 * 
 */
public class AffilieListComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Date d'affiliation
     */
    private String forDate = null;
    /**
     * Date de début d'année de la date <code>forDate</code>
     */
    private String forDateDebutAnnee = null;
    /**
     * Date de début du trimestre de la date <code>forDate</code>
     */
    private String forDateDebutTrimestre = null;
    /**
     * Recherche par numéro d'affilié
     */
    private String forNumeroAffilie = null;

    /**
     * Recherche sur une ou plusieurs activité(s)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    private Collection<String> inActivites = null;

    /**
     * Recherche sur une ou plusieurs périodicité(s)
     * 
     * @see ch.globaz.al.business.constantes.ALCSAffilie#PERIODICITE_ANN
     * @see ch.globaz.al.business.constantes.ALCSAffilie#PERIODICITE_MEN
     * @see ch.globaz.al.business.constantes.ALCSAffilie#PERIODICITE_TRI
     */
    private Collection<String> inPeriodicites = null;

    public String getForDate() {
        return forDate;
    }

    /**
     * @return the forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @return the inActivites
     */
    public Collection<String> getInActivites() {
        return inActivites;
    }

    /**
     * @return the inPeriodicites
     */
    public Collection<String> getInPeriodicites() {
        return inPeriodicites;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;

    }

    /**
     * @param forNumeroAffilie
     *            the forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * @param inActivites
     *            the inActivites to set
     */
    public void setInActivites(Collection<String> inActivites) {
        this.inActivites = inActivites;
    }

    /**
     * Recherche sur une ou plusieurs périodicité(s)
     * 
     * @see ch.globaz.al.business.constantes.ALCSAffilie#PERIODICITE_ANN
     * @see ch.globaz.al.business.constantes.ALCSAffilie#PERIODICITE_MEN
     * @see ch.globaz.al.business.constantes.ALCSAffilie#PERIODICITE_TRI
     * 
     * @param inPeriodicites
     *            the forPeriodiciteIn to set
     */
    public void setInPeriodicites(Collection<String> inPeriodicites) {
        this.inPeriodicites = inPeriodicites;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AffilieListComplexModel> whichModelClass() {
        return AffilieListComplexModel.class;
    }

    public void setForDateDebutAnnee(String forDateDebutAnnee) {
        this.forDateDebutAnnee = forDateDebutAnnee;
    }

    public String getForDateDebutAnnee() {
        return forDateDebutAnnee;
    }

    public void setForDateDebutTrimestre(String forDateDebutTrimestre) {
        this.forDateDebutTrimestre = forDateDebutTrimestre;
    }

    public String getForDateDebutTrimestre() {
        return forDateDebutTrimestre;
    }
}