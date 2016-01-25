package ch.globaz.helios.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author sel
 * 
 */
public class MandatSimpleModelSearch extends JadeSearchComplexModel {
    private String likeLibelleDe = null;
    private String likeLibelleFr = null;
    private String likeLibelleIt = null;

    /**
     * @return the likeLibelleDe
     */
    public String getLikeLibelleDe() {
        return likeLibelleDe;
    }

    /**
     * @return the likeLibelleFr
     */
    public String getLikeLibelleFr() {
        return likeLibelleFr;
    }

    /**
     * @return the likeLibelleIt
     */
    public String getLikeLibelleIt() {
        return likeLibelleIt;
    }

    /**
     * @param likeLibelleDe
     */
    public void setLikeLibelleDe(String likeLibelleDe) {
        this.likeLibelleDe = likeLibelleDe;
    }

    /**
     * @param likeLibelleFr
     */
    public void setLikeLibelleFr(String likeLibelleFr) {
        this.likeLibelleFr = likeLibelleFr;
    }

    /**
     * @param likeLibelleIt
     */
    public void setLikeLibelleIt(String likeLibelleIt) {
        this.likeLibelleIt = likeLibelleIt;
    }

    @Override
    public Class<MandatSimpleModel> whichModelClass() {
        return MandatSimpleModel.class;
    }
}
