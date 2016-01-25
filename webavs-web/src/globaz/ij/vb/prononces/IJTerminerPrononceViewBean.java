/*
 * Créé le 11 juil. 06
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.db.prononces.IJRecapitulatifPrononce;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author hpe
 * 
 */
public class IJTerminerPrononceViewBean extends IJRecapitulatifPrononce implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    boolean isBaseIndAfterEnd = false;

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVS(), tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut nom prenom
     * 
     * @return la valeur courante de l'attribut prenom nom
     */
    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    /**
     * @return
     */
    public boolean isBaseIndAfterEnd() {
        return isBaseIndAfterEnd;
    }

    /**
     * @param b
     */
    public void setBaseIndAfterEnd(boolean b) {
        isBaseIndAfterEnd = b;
    }

}
