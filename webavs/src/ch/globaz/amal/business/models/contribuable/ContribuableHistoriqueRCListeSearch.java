/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author CBU
 * 
 */
public class ContribuableHistoriqueRCListeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateNaissance = null;
    private String forIdContribuableInfo = null;
    private String forNoContribuable = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdContribuableInfo
     */
    public String getForIdContribuableInfo() {
        return forIdContribuableInfo;
    }

    public String getForNoContribuable() {
        return forNoContribuable;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdContribuableInfo
     *            the forIdContribuableInfo to set
     */
    public void setForIdContribuableInfo(String forIdContribuableInfo) {
        this.forIdContribuableInfo = forIdContribuableInfo;
    }

    public void setForNoContribuable(String forNoContribuable) {
        this.forNoContribuable = forNoContribuable;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ContribuableHistoriqueRCListe.class;
    }
}
