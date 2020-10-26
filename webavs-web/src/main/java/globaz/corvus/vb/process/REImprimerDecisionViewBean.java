package globaz.corvus.vb.process;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REImprimerDecisionViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private String dateDocument = "";
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private String emailObject = "";
    private String idDecision = "";
    private String idDemandeRente = "";
    private String idTiersRequerant = "";
    private Boolean isSendToGed = Boolean.FALSE;
    private Boolean isTypeCaisseCorrect = Boolean.TRUE;
    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Donne l'etat de la decision
     *
     * @return
     */
    public String getCsEtatDecision() {

        REDecisionEntity d = new REDecisionEntity();
        d.setSession(getSession());
        d.setIdDecision(getIdDecision());
        try {
            d.retrieve();

            if (d != null) {
                return d.getCsEtat();
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
        }

        return "";
    }

    public String getDateDocument() throws Exception {

        if (JadeStringUtil.isBlankOrZero(dateDocument)) {
            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(getSession());
            decision.setIdDecision(idDecision);
            decision.retrieve();

            return decision.getDateDecision();

        } else {

            return dateDocument;

        }

    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    public String getEMailAddress() {
        if (JadeStringUtil.isEmpty(eMailAddress) && (getSession() != null)) {
            eMailAddress = getSession().getUserEMail();
        }

        return eMailAddress;
    }

    public String getEmailObject() {
        return emailObject;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() throws Exception {

        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(getSession());
        decision.setIdDecision(getIdDecision());
        decision.retrieve();

        return decision.getIdDemandeRente();
    }

    /**
     * Méthode pour affichages des ordres de versement
     *
     * @throws Exception
     */
    public String getIdPrestation() throws Exception {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(getIdDecision());
        prestMgr.setForIdDemandeRente(getIdDemandeRente());

        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();

        return prest.getIdPrestation();
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * Méthode pour affichages des ordres de versement
     *
     * @throws Exception
     */
    public String getMontantPrestation() throws Exception {
        REPrestationsManager prestMgr = new REPrestationsManager();
        prestMgr.setSession(getSession());
        prestMgr.setForIdDecision(getIdDecision());
        prestMgr.setForIdDemandeRente(getIdDemandeRente());

        try {
            prestMgr.find();
        } catch (Exception e) {
            return "";
        }

        REPrestations prest = (REPrestations) prestMgr.getFirstEntity();

        return prest.getMontantPrestation();
    }

    /**
     * @return the requerantInfo
     * @throws Exception
     */
    public String getRequerantInfo(String idTierBenef) throws Exception {

        if (null == idTierBenef) {
            idTierBenef = getIdTiersRequerant();
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTierBenef);

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI",
                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI",
                        tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setEmailObject(String emailObject) {
        this.emailObject = emailObject;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    @Override
    public boolean validate() {
        return true;
    }

    public Boolean getIsTypeCaisseCorrect(){
        return isTypeCaisseCorrect;

    }

    public void setIsTypeCaisseCorrect(Boolean isTypeCaisseCorrect) {
        this.isTypeCaisseCorrect = isTypeCaisseCorrect;
    }

}
