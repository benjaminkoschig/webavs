package globaz.naos.db.cotisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.planCaisse.AFPlanCaisseJCouverture;
import globaz.naos.db.planCaisse.AFPlanCaisseJCouvertureManager;
import globaz.naos.translation.CodeSystem;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFCotisationViewBean extends AFCotisation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String exception = "false";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Constructeur d'AFCotisationViewBean.
     */
    public AFCotisationViewBean() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getException() {
        return exception;
    }

    /**
     * construit un vecteur avec tous les plans qui incluent l'assurance selectionnee pour cette cotisation.
     * 
     * @return la valeur courante de l'attribut plan caisse list
     */
    public Vector getPlanCaisseAdhesions() {
        Vector retValue = new Vector();
        retValue.add(new String[] { "", "" }); // ajouter une ligne vide de
        // toutes facons
        try {
            AFAdhesionManager mgr = new AFAdhesionManager();
            mgr.setSession(getSession());
            mgr.setForAffiliationId(getAffiliation().getAffiliationId());
            mgr.setForTypeAdhesionList(new String[] { CodeSystem.TYPE_ADHESION_CAISSE,
                    CodeSystem.TYPE_ADHESION_CAISSE_PRINCIPALE });
            mgr.find();
            for (int i = 0; i < mgr.size(); i++) {
                AFAdhesion adh = (AFAdhesion) mgr.getEntity(i);
                String libPlan = adh.getPlanCaisse().getLibelle();
                if (libPlan.length() > 20) {
                    libPlan = libPlan.substring(0, 19);
                }
                retValue.add(new String[] { adh.getPlanCaisseId(), adh.getAdministrationCaisseCode() + " - " + libPlan });
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return retValue;
        }
        return retValue;
    }

    /**
     * construit un vecteur avec tous les plans qui incluent l'assurance selectionnee pour cette cotisation.
     * 
     * @return la valeur courante de l'attribut plan caisse list
     */
    public Vector getPlanCaisseList() {
        Vector retValue = new Vector();

        retValue.add(new String[] { "", "" }); // ajouter une ligne vide de
        // toutes facons

        if (!JadeStringUtil.isIntegerEmpty(getAssuranceId())) {
            // BTransaction transaction = null;
            try {
                BSessionUtil.checkDateGregorian(getSession(), getDateDebut());

                // getSession().releaseAllTransactions();
                // transaction = new BTransaction(getSession());
                // transaction.openTransaction();
                // transaction.clearErrorBuffer();
                AFPlanCaisseJCouvertureManager plans = new AFPlanCaisseJCouvertureManager();

                plans.setForAssuranceId(getAssuranceId());
                plans.setForDateDebutBeforeDate(getDateDebut());
                plans.setForDateFinAfterDate(getDateDebut());
                plans.setSession(getSession());
                plans.changeManagerSize(BManager.SIZE_NOLIMIT);
                plans.find();
                for (int idPlan = 0; idPlan < plans.size(); ++idPlan) {
                    AFPlanCaisseJCouverture plan = (AFPlanCaisseJCouverture) plans.get(idPlan);
                    String libPlan = plan.getLibelle();
                    if (libPlan.length() > 20) {
                        libPlan = libPlan.substring(0, 19);
                    }
                    // TIAdministrationViewBean caisse = new
                    // TIAdministrationViewBean();
                    // caisse.setSession(getSession());
                    // caisse.setIdTiersAdministration(plan.getIdTiers());
                    // caisse.retrieve();
                    retValue.add(new String[] { plan.getPlanCaisseId(),
                            plan.getAdministration().getCodeAdministration() + " - " + libPlan });
                }

            } catch (Exception e) {
                // e.printStackTrace();
                return retValue;
            } /*
               * finally { try { transaction.closeTransaction(); } catch (Exception inEx) { // laisser tel quel
               * transaction = null; } }
               */
        }

        return retValue;
    }

    public boolean hasPlanAffiliation() {
        AFPlanAffiliationManager mgr = new AFPlanAffiliationManager();
        mgr.setSession(getSession());
        mgr.setForAffiliationId(getAffiliation().getAffiliationId());
        try {
            if (mgr.getCount() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void setException(String exception) {
        this.exception = exception;
    }

}
