package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche permettant la recherche de critères de tarif
 * 
 * @author jts
 * 
 */
public class CritereTarifSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur le critère de sélection de tarif
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CRITERE
     */
    private String forCritereTarif = null;
    /**
     * Recherche sur le début de la plage de valeurs
     */
    private String forDebut = null;
    /**
     * Recherche sur la fin de la plage de valeurs
     */
    private String forFin = null;

    /**
     * @return the forCritereTarif
     */
    public String getForCritereTarif() {
        return forCritereTarif;
    }

    /**
     * @return the forDebut
     */
    public String getForDebut() {
        return forDebut;
    }

    /**
     * @return the forFin
     */
    public String getForFin() {
        return forFin;
    }

    /**
     * @param forCritereTarif
     *            the forCritereTarif to set
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CRITERE
     */
    public void setForCritereTarif(String forCritereTarif) {
        this.forCritereTarif = forCritereTarif;
    }

    /**
     * @param forDebut
     *            the forDebut to set
     */
    public void setForDebut(String forDebut) {
        this.forDebut = forDebut;
    }

    /**
     * @param forFin
     *            the forFin to set
     */
    public void setForFin(String forFin) {
        this.forFin = forFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<CritereTarifModel> whichModelClass() {
        return CritereTarifModel.class;
    }

}
