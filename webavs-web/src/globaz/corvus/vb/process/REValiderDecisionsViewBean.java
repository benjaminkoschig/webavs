/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.vb.decisions.REDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author scr
 */

public class REValiderDecisionsViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface {

    private String decisionATraiter = "";
    private List decisionsList = new ArrayList();
    public List documentsPreview = new ArrayList();
    private String eMailAddress = "";
    private String idDecision = "";
    private String idDemandeRente = null;
    private String idLot;

    private String idTiersRequerant = null;

    private String requerantInfo = null;
    private String typeTTT = "";

    public void addDecision(REDecisionsViewBean decision) {
        decisionsList.add(decision);
    }

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

    public String getCurrentDate() {
        return JACalendar.todayJJsMMsAAAA();

    }

    public String getDateDecision() throws Exception {

        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(getSession());
        decision.setIdDecision(getIdDecision());
        decision.retrieve();

        if (decision.isNew()) {
            return "";
        } else {
            return decision.getDatePreparation();
        }

    }

    public String getDecisionATraiter() {
        return decisionATraiter;
    }

    public Iterator getDecisionsIterator() throws Exception {

        return getDecisionsList().iterator();

    }

    public List getDecisionsList() {
        return decisionsList;
    }

    public List getDocumentsPreview() {
        return documentsPreview;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return the idDemandeRente
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdLot() {
        return idLot;
    }

    /**
     * Méthode pour affichages des ordres de versement
     */
    public String getIdPrestation() {
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

    /**
     * @return the idTiersRequerant
     */
    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    /**
     * Méthode pour affichages des ordres de versement
     */
    public String getMontantPrestation() {
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
     */
    public String getRequerantInfo() {
        return requerantInfo;
    }

    public String getTypeTTT() {
        return typeTTT;
    }

    public void setDecisionATraiter(String decisionATraiter) {
        this.decisionATraiter = decisionATraiter;
    }

    public void setDecisionsList(List decisionsList) {
        this.decisionsList = decisionsList;
    }

    public void setDocumentsPreview(List documentsPreview) {
        this.documentsPreview = documentsPreview;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idDemandeRente the idDemandeRente to set
     */
    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * @param idTiersRequerant the idTiersRequerant to set
     */
    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    /**
     * @param requerantInfo the requerantInfo to set
     */
    public void setRequerantInfo(String requerantInfo) {
        this.requerantInfo = requerantInfo;
    }

    public void setTypeTTT(String typeTTT) {
        this.typeTTT = typeTTT;
    }

    @Override
    public boolean validate() {
        // TODO Auto-generated method stub
        return false;
    }

}
