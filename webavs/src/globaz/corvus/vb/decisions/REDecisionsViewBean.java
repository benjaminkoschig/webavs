/*
 * Créé le 26 juil. 07
 */

package globaz.corvus.vb.decisions;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author SCR
 */

public class REDecisionsViewBean extends REDecisionEntity implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idLot;

    private String idPrestation = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    public String getBeneficiaireInfo() throws Exception {

        PRTiersWrapper benef = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiairePrincipal());

        if (null != benef) {
            return benef.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                    + benef.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + benef.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            return "Bénéficiaire non défini";
        }

    }

    public String getCsEtatDecisionLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    public String getCsTypeDecisionLibelle() {
        return getSession().getCodeLibelle(getCsTypeDecision());
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public Boolean isDateDecisionInferieureMoisPaiement() throws JAException {
        JADate dateDecision = new JADate(getDateDecision());
        JADate datePaiement = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

        int moisDecision = dateDecision.getMonth();
        int moisPaiement = datePaiement.getMonth();

        if (moisDecision == moisPaiement) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

}