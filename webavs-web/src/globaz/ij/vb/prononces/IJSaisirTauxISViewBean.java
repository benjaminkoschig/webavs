package globaz.ij.vb.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

public class IJSaisirTauxISViewBean extends IJPrononce implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String noAVS = "";

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

    public String getNoAVS() {
        return noAVS;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }
}
