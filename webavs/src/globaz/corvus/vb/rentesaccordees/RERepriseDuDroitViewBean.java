package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;

public class RERepriseDuDroitViewBean extends REPrestationsAccordees implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String montant1 = "";
    private String montant2 = "";
    private String periode_1_Au = "";
    private String periode_1_Du = "";
    private String periode_2_Au = "";
    private String periode_2_Du = "";

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiaire());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    public String getMontant1() {
        return montant1;
    }

    public String getMontant2() {
        return montant2;
    }

    public String getPeriode_1_Au() {
        return periode_1_Au;
    }

    public String getPeriode_1_Du() {
        return periode_1_Du;
    }

    public String getPeriode_2_Au() {
        return periode_2_Au;
    }

    public String getPeriode_2_Du() {
        return periode_2_Du;
    }

    public void setMontant1(String montant1) {
        this.montant1 = montant1;
    }

    public void setMontant2(String montant2) {
        this.montant2 = montant2;
    }

    public void setPeriode_1_Au(String periode_1_Au) {
        this.periode_1_Au = periode_1_Au;
    }

    public void setPeriode_1_Du(String periode_1_Du) {
        this.periode_1_Du = periode_1_Du;
    }

    public void setPeriode_2_Au(String periode_2_Au) {
        this.periode_2_Au = periode_2_Au;
    }

    public void setPeriode_2_Du(String periode_2_Du) {
        this.periode_2_Du = periode_2_Du;
    }

}
