package globaz.corvus.vb.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CARoleViewBean;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererListeRestitutionsSoldesViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateValeur = "";
    private String eMailAddress = "";
    private String forRole = "";

    /**
     * équivalent à createOptionsTags(session, selectedId, true).
     * 
     * @see #createOptionsTags(BSession, String, boolean)
     */
    public String createOptionsTags(BSession session, String selectedId) throws Exception {
        return CARoleViewBean.createOptionsTags(session, selectedId, true);
    }

    /**
     * @return the dateValeur
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * @return the forRole
     */
    public String getForRole() {
        return forRole;
    }

    /**
     * @param dateValeur
     *            the dateValeur to set
     */
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * @param forRole
     *            the forRole to set
     */
    public void setForRole(String forRole) {
        this.forRole = forRole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {

        if (JadeStringUtil.isBlankOrZero(dateValeur)) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("JSP_RESTITUTIONS_SOLDES_ERREUR_DATEVALEUR_OBLIGATOIRE"));
            return false;
        }

        if (JadeDateUtil.isDateBefore(globaz.globall.util.JACalendar.todayJJsMMsAAAA(), dateValeur)) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("JSP_RESTITUTIONS_SOLDES_ERREUR_DATEVALEUR_INFERIEUR_DATEJOUR"));
            return false;
        }

        try {
            BSessionUtil.checkDateGregorian(getSession(), dateValeur);
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.toString());
            return false;
        }

        return true;
    }
}
