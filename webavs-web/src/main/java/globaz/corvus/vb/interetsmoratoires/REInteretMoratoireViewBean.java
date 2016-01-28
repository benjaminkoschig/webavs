/*
 * Créé le 2 août 07
 */

package globaz.corvus.vb.interetsmoratoires;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoireManager;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author BSC
 * 
 */

public class REInteretMoratoireViewBean extends REInteretMoratoire implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePmtDepuisPyxis", "getIdTiers" },
            new String[] { "idDomaineApplicatifDepuisPyxis", "idApplication" },
            new String[] { "numAffilieDepuisPyxis", "idExterneAvoirPaiement" } };

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String adresseFormattee = "";
    // infos relatives a l'adresse de paiement
    private String ccpOuBanqueFormatte = "";
    private RECalculInteretMoratoire cim = null;
    private String dateDebutDroit = "";
    private String dateDecision = "";
    private String dateDepotDemande = "";

    private String decisionDepuis = "";
    private String idDemandeRente = "";

    private String idTiersDemandeRente = "";
    private String isAdresseModifiee = "false";
    private String numAffilieDepuisPyxis = "";

    private boolean retourDepuisPyxis;
    private PRTiersWrapper tiers = null;

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    /**
     * @return
     */
    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    /**
     * @return
     */
    public String getDateDebut() {

        if (loadCalculInteretMoratoire()) {
            return cim.getDateDebut();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getDateDebutDroit() {

        if (JadeStringUtil.isEmpty(dateDebutDroit) && !JadeStringUtil.isIntegerEmpty(getIdDemandeRente())) {

            REDemandeRente d = new REDemandeRente();
            d.setSession(getSession());
            d.setIdDemandeRente(getIdDemandeRente());
            try {
                d.retrieve();
                dateDebutDroit = d.getDateDebut();
            } catch (Exception e) {
                // on ne fait rien
            }
        }

        return dateDebutDroit;
    }

    /**
     * @return
     */
    public String getDateDecision() {

        return dateDecision;
    }

    /**
     * @return
     */
    public String getDateDepotDemande() {

        if (JadeStringUtil.isEmpty(dateDepotDemande) && !JadeStringUtil.isIntegerEmpty(getIdDemandeRente())) {

            REDemandeRente d = new REDemandeRente();
            d.setSession(getSession());
            d.setIdDemandeRente(getIdDemandeRente());
            try {
                d.retrieve();
                dateDepotDemande = d.getDateDepot();
            } catch (Exception e) {
                // on ne fait rien
            }
        }

        return dateDepotDemande;
    }

    /**
     * @return
     */
    public String getDateFin() {

        if (loadCalculInteretMoratoire()) {
            return cim.getDateFin();
        } else {
            return "";
        }
    }

    public String getDecisionDepuis() {
        return decisionDepuis;
    }

    /**
     * @return
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdTiers() {

        if (loadCalculInteretMoratoire()) {
            return cim.getIdTiers();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getIdTiersDemandeRente() {
        return idTiersDemandeRente;
    }

    /**
     * @return
     */
    public String getIsAdresseModifiee() {
        return isAdresseModifiee;
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return METHODES_SEL_ADRESSE_PAIEMENT;
    }

    /**
     * @return
     */
    public String getMontantDette() {

        if (loadCalculInteretMoratoire()) {
            return cim.getMontantDette();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getMontantRetro() {

        if (loadCalculInteretMoratoire()) {
            return cim.getMontantRetro();
        } else {
            return "";
        }
    }

    public String getNumAffilieDepuisPyxis() {
        return numAffilieDepuisPyxis;
    }

    /**
     * 
     * @return
     */
    public String getTiersDescription() {
        if (loadTiers()) {
            return PRNSSUtil.formatDetailRequerantListe(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                    getSession().getCodeLibelle(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))));
        }

        return "";
    }

    public boolean isModifiable() {
        try {
            REDemandeRente reDemRente = new REDemandeRente();
            reDemRente.setSession(getSession());
            reDemRente.setIdDemandeRente(getIdDemandeRente());
            reDemRente.retrieve();

            // si la demande est dans l'état validé, aucunes modifications n'est
            // possible

            if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(reDemRente.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(reDemRente.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(reDemRente.getCsEtat())
                    || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(reDemRente.getCsEtat())) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * 
     * @return
     */
    private boolean loadCalculInteretMoratoire() {
        if (cim == null && !JadeStringUtil.isIntegerEmpty(getIdInteretMoratoire())) {

            try {
                RECalculInteretMoratoireManager cimManager = new RECalculInteretMoratoireManager();
                cimManager.setSession(getSession());
                cimManager.setForIdInteretMoratoire(getIdInteretMoratoire());
                cimManager.find();

                if (cimManager.getSize() > 0) {
                    cim = (RECalculInteretMoratoire) cimManager.getFirstEntity();
                }
            } catch (Exception e) {
                getSession().addError("L'intérêt moratoire " + getIdInteretMoratoire() + "ne peut pas être chargée.");
            }
        }
        return cim != null;
    }

    /**
     * 
     * @return
     */
    private boolean loadTiers() {
        if (tiers == null) {

            try {
                tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiers());

                if (tiers == null) {
                    tiers = PRTiersHelper.getAdministrationParId(getSession(), getIdTiers());
                }
            } catch (Exception e) {
                getSession().addError("Le Tiers " + getIdTiers() + "ne peut pas être chargée.");
            }
        }
        return tiers != null;
    }

    /**
     * @param string
     */
    public void setAdresseFormattee(String string) {
        adresseFormattee = string;
    }

    /**
     * @param string
     */
    public void setCcpOuBanqueFormatte(String string) {
        ccpOuBanqueFormatte = string;
    }

    /**
     * @param string
     */
    public void setDateDebutDroit(String string) {
        dateDebutDroit = string;
    }

    /**
     * @param string
     */
    public void setDateDecision(String string) {
        dateDecision = string;
    }

    /**
     * @param string
     */
    public void setDateDepotDemande(String string) {
        dateDepotDemande = string;
    }

    public void setDecisionDepuis(String decisionDepuis) {
        this.decisionDepuis = decisionDepuis;
    }

    /**
     * @param string
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdDomaineApplicatifDepuisPyxis(String string) {
        setCsDomaineAdrPmt(string);
    }

    /**
     * @param string
     */
    public void setIdTiersAdressePmtDepuisPyxis(String string) {
        setIdTiersAdrPmt(string);
        setRetourDepuisPyxis(true);
    }

    /**
     * @param string
     */
    public void setIdTiersDemandeRente(String string) {
        idTiersDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIsAdresseModifiee(String string) {
        isAdresseModifiee = string;
    }

    public void setNumAffilieDepuisPyxis(String numAffilieDepuisPyxis) throws Exception {
        this.numAffilieDepuisPyxis = numAffilieDepuisPyxis;

        IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(), numAffilieDepuisPyxis);

        if (affilie != null) {
            setIdAffilieAdrPmt(affilie.getIdAffilie());
        }
    }

    /**
     * @param b
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

}