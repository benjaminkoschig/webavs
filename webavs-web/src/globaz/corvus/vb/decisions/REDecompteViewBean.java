package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Map;
import java.util.TreeMap;

public class REDecompteViewBean extends PRAbstractViewBeanSupport {

    // ~ Variables
    // -------------------------------------------------------------------------------------------------

    private String csTypeDecision = "";
    private String decompteHTML = "";
    private String idDecision = "";
    private String idDemandeRente = "";
    private String idDomAdrPmt = "";
    private String idExtAdrPmt = "";
    private String idLot;
    private String idTierAdrPmt = "";

    private String idTierBeneficiaire = "";

    private String idTierRequerant = "";
    private Map listeCreanciers = new TreeMap();
    private Map listePeriodes = new TreeMap();

    private String montantFactureACompenser = "";

    private String montantImpotSource = "";
    private String montantInteretsMoratoires = "";
    private String montantRentesDejaVersees = "";

    private String montantTotalFinal = "";

    private String montantTotalIntermediaire = "";

    private BSession session;

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------

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

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getDecompteHTML() {
        return decompteHTML;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdDomAdrPmt() {
        return idDomAdrPmt;
    }

    public String getIdExtAdrPmt() {
        return idExtAdrPmt;
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

    public String getIdTierAdrPmt() {
        return idTierAdrPmt;
    }

    public String getIdTierBeneficiaire() {
        return idTierBeneficiaire;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getLibelleTypeDecision() {
        return getSession().getCodeLibelle(getCsTypeDecision());
    }

    public Map getListeCreanciers() {
        return listeCreanciers;
    }

    public Map getListePeriodes() {
        return listePeriodes;
    }

    public String getMontantFactureACompenser() {
        return montantFactureACompenser;
    }

    public String getMontantImpotSource() {
        return montantImpotSource;
    }

    public String getMontantInteretsMoratoires() {
        return montantInteretsMoratoires;
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

    public String getMontantRentesDejaVersees() {
        return montantRentesDejaVersees;
    }

    public String getMontantTotalFinal() {
        return montantTotalFinal;
    }

    public String getMontantTotalIntermediaire() {
        return montantTotalIntermediaire;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setDecompteHTML(String decompteHTML) {
        this.decompteHTML = decompteHTML;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdDomAdrPmt(String idDomAdrPmt) {
        this.idDomAdrPmt = idDomAdrPmt;
    }

    public void setIdExtAdrPmt(String idExtAdrPmt) {
        this.idExtAdrPmt = idExtAdrPmt;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdTierAdrPmt(String idTierAdrPmt) {
        this.idTierAdrPmt = idTierAdrPmt;
    }

    public void setIdTierBeneficiaire(String idTierBeneficiaire) {
        this.idTierBeneficiaire = idTierBeneficiaire;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setListeCreanciers(Map listeCreanciers) {
        this.listeCreanciers = listeCreanciers;
    }

    public void setListePeriodes(Map listePeriodes) {
        this.listePeriodes = listePeriodes;
    }

    public void setMontantFactureACompenser(String montantFactureACompenser) {
        this.montantFactureACompenser = montantFactureACompenser;
    }

    public void setMontantImpotSource(String montantImpotSource) {
        this.montantImpotSource = montantImpotSource;
    }

    public void setMontantInteretsMoratoires(String montantInteretsMoratoires) {
        this.montantInteretsMoratoires = montantInteretsMoratoires;
    }

    public void setMontantRentesDejaVersees(String montantRentesDejaVersees) {
        this.montantRentesDejaVersees = montantRentesDejaVersees;
    }

    public void setMontantTotalFinal(String montantTotalFinal) {
        this.montantTotalFinal = montantTotalFinal;
    }

    public void setMontantTotalIntermediaire(String montantTotalIntermediaire) {
        this.montantTotalIntermediaire = montantTotalIntermediaire;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    @Override
    public boolean validate() {
        return false;
    }

}
