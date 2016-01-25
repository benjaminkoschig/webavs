package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;

/**
 * Bean de l'en-t�te des revenus de splitting. Date de cr�ation : (31.10.2002 13:07:08)
 * 
 * @author: dgi
 */
public class CIRevenuSplittingRCViewBean extends CIMandatSplitting implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CICompteIndividuel ciMandat = null;

    /**
     * Commentaire relatif au constructeur CIRevenuSplittingRCViewBean.
     */
    public CIRevenuSplittingRCViewBean() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return null;
    }

    private CICompteIndividuel getCI() throws Exception {
        if (ciMandat != null) {
            return ciMandat;
        }
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setForNumeroAvs(getIdTiersPartenaire());
        ciMgr.find();
        if (ciMgr.size() > 0) {
            ciMandat = (CICompteIndividuel) ciMgr.getFirstEntity();
            return ciMandat;
        }
        return null;
    }

    public String getDateNaissanceForEcran() {
        try {
            return getCI().getDateNaissance();

        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysForEcran() {
        try {
            return getCI().getPaysFormate();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSexeForEcran() {
        try {
            return getCI().getSexeLibelle();

        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
    }
}