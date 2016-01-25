/*
 * Créé le 30 juil. 07
 */

package globaz.corvus.vb.prestations;

import globaz.corvus.db.prestations.IREPrestatationsDecisionsRCListViewBean;
import globaz.corvus.db.prestations.REPrestationsJointTiers;
import globaz.corvus.vb.lots.RELotViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author SCR
 */
public class REPrestationsJointTiersViewBean extends REPrestationsJointTiers implements
        IREPrestatationsDecisionsRCListViewBean, FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FROM_ECRAN_DECISIONS = "2";
    public static final String FROM_ECRAN_LOTS = "1";

    private String csEtatLot = "";
    private String csTypeLot = "";
    private String dateDebutAffichage = "";
    private String dateFinAffichage = "";
    private String genrePrestationAffichage = "";
    private RELotViewBean lot = null;
    private String provenance = "";

    public String getCsEtatLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getCsEtatLotLibelle() {
        if (loadLot()) {
            return lot.getCsEtatLotLibelle();
        } else {
            return "";
        }
    }

    public String getCsTypeLibelle() {
        return getSession().getCodeLibelle(getCsType());
    }

    public String getCsTypeLot() {
        return csTypeLot;
    }

    public String getCsTypeLotLibelle() {
        if (loadLot()) {
            return lot.getCsTypeLotLibelle();
        } else {
            return "";
        }

    }

    @Override
    public String getDateDebutAffichage() {
        return dateDebutAffichage;
    }

    @Override
    public String getDateFinAffichage() {
        return dateFinAffichage;
    }

    @Override
    public String getGenrePrestationAffichage() {
        return genrePrestationAffichage;
    }

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

    public String getTiersPrestataireDescription() {
        return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                getSession().getCodeLibelle(getCsSexe()),
                getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getIdNationalite())));
    }

    private boolean loadLot() {
        if ((lot == null) && !JadeStringUtil.isIntegerEmpty(getIdLot())) {
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

    public void setCsEtatLot(String string) {
        csEtatLot = string;
    }

    public void setCsTypeLot(String string) {
        csTypeLot = string;
    }

    @Override
    public void setDateDebutAffichage(String dateDebutAffichage) {
        this.dateDebutAffichage = dateDebutAffichage;
    }

    @Override
    public void setDateFinAffichage(String dateFinAffichage) {
        this.dateFinAffichage = dateFinAffichage;
    }

    @Override
    public void setGenrePrestationAffichage(String genrePrestationAffichage) {
        this.genrePrestationAffichage = genrePrestationAffichage;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }
}