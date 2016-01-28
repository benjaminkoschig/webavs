package globaz.corvus.vb.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CARoleViewBean;
import globaz.osiris.db.comptes.CARubrique;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererListeRestitutionsMouvementsViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateA = "";
    private String dateDe = "";
    private String eMailAddress = "";
    private String forRole = "";
    private String idRubrique = "";

    /**
     * équivalent à createOptionsTags(session, selectedId, true).
     * 
     * @see #createOptionsTags(BSession, String, boolean)
     */
    public String createOptionsTags(BSession session, String selectedId) throws Exception {
        return CARoleViewBean.createOptionsTags(session, selectedId, true);
    }

    /**
     * @return the dateA
     */
    public String getDateA() {
        return dateA;
    }

    /**
     * @return the dateDe
     */
    public String getDateDe() {
        return dateDe;
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
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return
     */
    public CARubrique getRubrique() {
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            return null;
        }

        CARubrique rubrique = new CARubrique();
        rubrique.setISession(getSession());
        rubrique.setIdRubrique(getIdRubrique());
        try {
            rubrique.retrieve();
            if (rubrique.isNew()) {
                rubrique = null;
            }
        } catch (Exception e) {
            rubrique = null;
        }

        return rubrique;
    }

    /**
     * @return le premier janvier de l'année courante
     */
    public String premierjanvier() {
        JADate date = new JADate(1, 1, JACalendar.today().getYear());
        return JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY);
    }

    /**
     * @param dateA
     *            the dateA to set
     */
    public void setDateA(String dateA) {
        this.dateA = dateA;
    }

    /**
     * @param dateDe
     *            the dateDe to set
     */
    public void setDateDe(String dateDe) {
        this.dateDe = dateDe;
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

    /**
     * @param idRubrique
     *            the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {

        if (JadeStringUtil.isBlankOrZero(dateDe)) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("JSP_RESTITUTIONS_MOUVEMENTS_ERREUR_DATEDE_OBLIGATOIRE"));
            return false;
        }
        if (JadeStringUtil.isBlankOrZero(dateA)) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("JSP_RESTITUTIONS_MOUVEMENTS_ERREUR_DATEA_OBLIGATOIRE"));
            return false;
        }

        if (JadeDateUtil.isDateBefore(dateA, dateDe)) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("JSP_RESTITUTIONS_MOUVEMENTS_ERREUR_DATEA_INFERIEUR_DATEDE"));
            return false;
        }

        try {
            BSessionUtil.checkDateGregorian(getSession(), dateDe);
            BSessionUtil.checkDateGregorian(getSession(), dateA);
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.toString());
            return false;
        }

        return true;
    }
}
