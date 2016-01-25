package globaz.naos.db.beneficiairepc;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * ViewBean de la classe Quittance
 * 
 * @author jpa
 */
public class AFQuittanceViewBean extends AFQuittance implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ETAT_CI = "850006";
    public final static String ETAT_CI_PARTIEL = "850007";
    public final static String ETAT_ERREUR_CI = "850004";
    public final static String ETAT_ERREUR_FACTU = "850003";
    public final static String ETAT_FACTURE = "850002";
    public final static String ETAT_FACTURE_PARTIEL = "850005";
    public final static String ETAT_OUVERT = "850001";
    private String isProcessRunning = "";

    /**
     * Constructeur AFQuittanceViewBean.
     */
    public AFQuittanceViewBean() {
        super();
    }

    public String getIsProcessRunning() {
        return isProcessRunning;
    }

    public void setIsProcessRunning(String isProcessRunning) {
        this.isProcessRunning = isProcessRunning;
    }
}
