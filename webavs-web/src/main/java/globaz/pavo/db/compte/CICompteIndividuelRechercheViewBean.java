/*
 * Cr�� le 28 mars 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author Herv� P�riat
 * 
 *         ViewBean utilis� dans l'aper�u des comptes individuels (RC), il permet notamment de stocker les param�tres de
 *         recherche lorsque l'on va chercher un affili� dans les tiers
 */
public class CICompteIndividuelRechercheViewBean extends CICompteIndividuel implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String forRegistre = "";

    public String forSexe = "";
    public String fromDateNaissance = "";
    public String fromNomPrenom = "";
    private String likeInIdAffiliation = new String();
    public String likeNumeroAvs = "";

    /**
     * Constructeur
     * 
     */
    public CICompteIndividuelRechercheViewBean() {
        super();
    }

    /**
     * @return
     */
    public String getForRegistre() {
        return forRegistre;
    }

    /**
     * @return
     */
    public String getForSexe() {
        return forSexe;
    }

    /**
     * @return
     */
    public String getFromDateNaissance() {
        return fromDateNaissance;
    }

    /**
     * @return
     */
    public String getFromNomPrenom() {
        return fromNomPrenom;
    }

    /**
     * @return
     */
    @Override
    public String getLikeInIdAffiliation() {
        return likeInIdAffiliation;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    /**
     * @param string
     */
    public void setForRegistre(String string) {
        forRegistre = string;
    }

    /**
     * @param string
     */
    public void setForSexe(String string) {
        forSexe = string;
    }

    /**
     * @param string
     */
    public void setFromDateNaissance(String string) {
        fromDateNaissance = string;
    }

    /**
     * @param string
     */
    public void setFromNomPrenom(String string) {
        fromNomPrenom = string;
    }

    /**
     * @param string
     */
    @Override
    public void setLikeInIdAffiliation(String string) {
        likeInIdAffiliation = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = string;
    }

}
