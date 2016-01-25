package ch.globaz.al.business.models.personne;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Mod�le de recherche d'un repr�sentant d'une personne AF (enfant ou allocataire)
 * 
 * @author GMO
 * 
 */
public class PersonneAFComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur la date
     */
    private String forDate = null;
    /**
     * Recherche sur le nom partiel
     */
    private String likeNom = null;
    /**
     * Recherche sur le num�ro partiel du Nss
     */
    private String likeNss = null;
    /**
     * Rcherche sur le pr�nom partiel
     */
    private String likePrenom = null;

    /**
     * 
     * @return forDate
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * 
     * @return likeNom
     */

    public String getLikeNom() {
        return likeNom;
    }

    /**
     * 
     * @return likeNss
     */
    public String getLikeNss() {
        return likeNss;
    }

    /**
     * 
     * @return likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * 
     * @param forDate
     *            : the forDate to set
     */

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * 
     * @param likeNom
     *            : the likeNom to set
     */

    public void setLikeNom(String likeNom) {

        if (!JadeStringUtil.isEmpty(likeNom)) {
            this.likeNom = JadeStringUtil.convertSpecialChars(likeNom).toUpperCase();
        }

    }

    /**
     * 
     * @param likeNss
     *            : the likeNss to set
     */
    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * 
     * @param likePrenom
     *            : the likePrenom to set
     */
    public void setLikePrenom(String likePrenom) {

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            this.likePrenom = JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase();
        }

    }

    @Override
    public Class<PersonneAFComplexModel> whichModelClass() {
        return PersonneAFComplexModel.class;
    }

}
