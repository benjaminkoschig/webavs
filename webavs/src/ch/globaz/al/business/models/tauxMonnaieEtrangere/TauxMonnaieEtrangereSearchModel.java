package ch.globaz.al.business.models.tauxMonnaieEtrangere;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modele simple permettant la recherche de taux des monnaies étrangères
 * 
 * @author PTA
 * 
 */

public class TauxMonnaieEtrangereSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * recherche sur le début de validité du taux
     */
    private String forDebutValiditeTaux = null;
    /**
     * recherche sur la fin de validité du taux
     */
    private String forFinValiditeTaux = null;
    /**
     * recherche sur le type de monnaie
     */
    private String forTypeMonnaie = null;

    /**
     * @return the forDebutValiditeTaux
     */
    public String getForDebutValiditeTaux() {
        return forDebutValiditeTaux;
    }

    /**
     * @return the forFinValiditeTaux
     */
    public String getForFinValiditeTaux() {
        return forFinValiditeTaux;
    }

    /**
     * @return the forTypeMonnaie
     */
    public String getForTypeMonnaie() {
        return forTypeMonnaie;
    }

    /**
     * @param forDebutValiditeTaux
     *            the forDebutValiditeTaux to set
     */
    public void setForDebutValiditeTaux(String forDebutValiditeTaux) {
        this.forDebutValiditeTaux = forDebutValiditeTaux;
    }

    /**
     * @param forFinValiditeTaux
     *            the forFinValiditeTaux to set
     */
    public void setForFinValiditeTaux(String forFinValiditeTaux) {
        this.forFinValiditeTaux = forFinValiditeTaux;
    }

    /**
     * @param forTypeMonnaie
     *            the forTypeMonnaie to set
     */
    public void setForTypeMonnaie(String forTypeMonnaie) {
        this.forTypeMonnaie = forTypeMonnaie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */

    @Override
    public Class whichModelClass() {

        return TauxMonnaieEtrangereModel.class;
    }

}
