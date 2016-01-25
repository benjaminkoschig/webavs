/*
 * Créé le 30 juil. 07
 */

package globaz.corvus.vb.prestations;

import globaz.corvus.db.prestations.REPrestationsJointDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.vb.lots.RELotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Iterator;

/**
 * @author BSC
 * 
 */
public class REPrestationsJointDemandeRenteViewBean extends REPrestationsJointDemandeRente implements
        FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FROM_ECRAN_DECISIONS = "2";
    public static final String FROM_ECRAN_LOTS = "1";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatLot = "";
    private String csTypeLot = "";

    private RELotViewBean lot = null;

    private String provenance = "";
    private PRTiersWrapper tiersPrestataire = null;

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * 
     * @return le libelle du csEtatLot
     */
    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    /**
     * @return
     */
    public String getCsEtatLot() {
        return csEtatLot;
    }

    /**
     * @return
     */
    public String getCsEtatLotLibelle() {
        if (loadLot()) {
            return lot.getCsEtatLotLibelle();
        } else {
            return "";
        }
    }

    /**
     * 
     * @return le libelle du csTypeLot
     */
    public String getCsTypeLibelle() {
        return getSession().getCodeLibelle(getCsType());
    }

    /**
     * @return
     */
    public String getCsTypeLot() {
        return csTypeLot;
    }

    /**
     * @return
     */
    public String getCsTypeLotLibelle() {
        if (loadLot()) {
            return lot.getCsTypeLotLibelle();
        } else {
            return "";
        }

    }

    /**
     * 
     * @return
     */
    public String getLotDescription() {

        if (loadLot()) {
            return lot.getIdLot() + " - " + lot.getDescription();
        } else {
            return "";
        }
    }

    public String getMoisAnneeFormate() {
        return PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(getMoisAnnee());
    }

    public String getMontantPrestationFormate() {
        return new FWCurrency(getMontantPrestation()).toStringFormat();
    }

    public String getProvenance() {
        return provenance;
    }

    /**
     * 
     * @return
     */
    public String getTiersPrestataireDescription() {
        if (loadTiersPrestataire()) {
            return PRNSSUtil.formatDetailRequerantListe(
                    tiersPrestataire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiersPrestataire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiersPrestataire.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiersPrestataire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiersPrestataire.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                    getSession().getCodeLibelle(
                            getSession().getSystemCode("CIPAYORI",
                                    tiersPrestataire.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))));
        } else {
            return "";
        }
    }

    /**
	 *
	 */
    private boolean loadLot() {
        if (lot == null && !JadeStringUtil.isIntegerEmpty(getIdLot())) {
            RELotViewBean l = new RELotViewBean();
            l.setSession(getSession());
            l.setIdLot(getIdLot());
            try {
                l.retrieve();
                lot = l;
            } catch (Exception e) {
                // on ne fait rien
            }
        }
        return lot != null;
    }

    /**
	 *
	 */
    private boolean loadTiersPrestataire() {
        if (tiersPrestataire == null && !JadeStringUtil.isIntegerEmpty(getIdTiersPrestataire())) {
            try {
                PRTiersWrapper t = PRTiersHelper.getTiersParId(getSession(), getIdTiersPrestataire());
                tiersPrestataire = t;
            } catch (Exception e) {
                // on ne fait rien
            }
        }
        return tiersPrestataire != null;
    }

    public String retrieveCodesPrestations() {

        String result = "";
        try {
            RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager raManager = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
            raManager.setSession(getSession());
            raManager.setForIdDecision(getIdDecision());
            raManager.find();

            Iterator iter = raManager.iterator();
            while (iter.hasNext()) {
                RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions ra = (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) iter
                        .next();

                if (0 > result.indexOf(ra.getCodePrestation())) {
                    if (!JadeStringUtil.isEmpty(result)) {
                        result = result + " - ";
                    }
                    result = result + ra.getCodePrestation();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param string
     */
    public void setCsEtatLot(String string) {
        csEtatLot = string;
    }

    /**
     * @param string
     */
    public void setCsTypeLot(String string) {
        csTypeLot = string;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

}