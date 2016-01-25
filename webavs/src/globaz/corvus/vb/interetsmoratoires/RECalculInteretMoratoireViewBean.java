/*
 * Créé le 2 août 07
 */

package globaz.corvus.vb.interetsmoratoires;

import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author BSC
 * 
 */

public class RECalculInteretMoratoireViewBean extends RECalculInteretMoratoire implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutDroit = "";
    private String dateDecision = "";
    private String dateDepotDemande = "";
    private String decisionDepuis = "";
    private String idDemandeRente = "";
    private String idTiersDemandeRente = "";

    private PRTiersWrapper tiers = null;

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getDateDebutDroit() {
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
        return dateDepotDemande;
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
    public String getIdTiersDemandeRente() {
        return idTiersDemandeRente;
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
    public void setIdTiersDemandeRente(String string) {
        idTiersDemandeRente = string;
    }

}