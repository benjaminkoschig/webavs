package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle permettant la sélection du numéro d'affilié uniquement. Il est utilisé pour charger la liste des affiliés lors
 * d'une génération globale
 * 
 * @author jts
 * 
 */
public class AffilieListComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Numéro de l'affilié
     */
    private String numeroAffilie = null;
    /**
     * Périodicité de l'affilié
     */
    private String periodicite = null;

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return the numeroAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return la périodicité de l'affilié
     */
    public String getPeriodicite() {
        return periodicite;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    /**
     * @param numeroAffilie
     *            the numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * @param periodicite
     *            the périodicité to set
     */
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }
}
