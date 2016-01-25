package globaz.corvus.vb.historiques;

import globaz.corvus.db.historiques.REHistoriqueRentes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * 
 * @author SCR
 * 
 */
public class REHistoriqueRentesJoinTiersViewBean extends REHistoriqueRentes implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public final static String MODE_AFFICHAGE_UPDATE = "UPDATE";

    private String idTiersIn = "";
    private String idTiersRequerant = "";
    private boolean isModeCreation = false;
    private boolean isReloadHistorique = true;
    private String modeAffichage = "";
    private String nom = "";
    private String prenom = "";

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        nom = statement.dbReadString(REHistoriqueRentesJoinTiersViewBean.FIELDNAME_NOM);
        prenom = statement.dbReadString(REHistoriqueRentesJoinTiersViewBean.FIELDNAME_PRENOM);
    }

    public String getIdTiersIn() {
        return idTiersIn;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public String getModeAffichage() {
        return modeAffichage;
    }

    public String getNom() {
        return nom;
    }

    public String getNssBeneficiaire() {

        // En principe identique, idTiers requérant est utilisé dans le viewbean
        // et est stocké dans la navigation
        // entre les différente page (au lieu de la request).
        String idt = getIdTiersRequerant();
        if (JadeStringUtil.isBlankOrZero(idt)) {
            idt = getIdTiers();
        }

        try {
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), idt);
            return tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } catch (Exception e) {
            return "Error: NSS not found !!!";
        }

    }

    public String getPrenom() {
        return prenom;
    }

    public String getRequerantInfo() {
        try {

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), getIdTiersRequerant());
            return tw.getDescription(getSession());
        } catch (Exception e) {
            return "Tiers not found for idTiers : " + idTiersRequerant;
        }
    }

    public boolean isModeCreation() {
        return isModeCreation;
    }

    public boolean isReloadHistorique() {
        return isReloadHistorique;
    }

    public void reloadHistorique(boolean reloadHistorique) {
        isReloadHistorique = reloadHistorique;
    }

    public void setIdTiersIn(String list) {
        idTiersIn = list;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setModeAffichage(String modeAffichage) {
        this.modeAffichage = modeAffichage;
    }

    public void setModeCreation(boolean isModeCreation) {
        this.isModeCreation = isModeCreation;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
