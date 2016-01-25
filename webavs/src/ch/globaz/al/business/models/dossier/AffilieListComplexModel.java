package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Mod�le permettant la s�lection du num�ro d'affili� uniquement. Il est utilis� pour charger la liste des affili�s lors
 * d'une g�n�ration globale
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
     * Num�ro de l'affili�
     */
    private String numeroAffilie = null;
    /**
     * P�riodicit� de l'affili�
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
     * @return la p�riodicit� de l'affili�
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
     *            the p�riodicit� to set
     */
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }
}
