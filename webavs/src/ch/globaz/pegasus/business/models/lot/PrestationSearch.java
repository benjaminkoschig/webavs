package ch.globaz.pegasus.business.models.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PrestationSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateNaissance = null;
    private String forIdLot = null;
    private String forSexe = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForSexe() {
        return forSexe;
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

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForSexe(String forSexe) {
        this.forSexe = forSexe;
    }

    /**
     * d�finit la condition de recherche sur le nom de l'assur�
     * 
     * @param likeNom
     *            nom de l'assur�
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * d�finit la condition de recherche sur le pr�nom de l'assur�
     * 
     * @param likePrenom
     *            pr�nom de l'assur�
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Prestation.class;
    }

}
