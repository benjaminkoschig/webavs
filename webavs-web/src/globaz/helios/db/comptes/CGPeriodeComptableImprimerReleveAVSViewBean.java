package globaz.helios.db.comptes;

import globaz.helios.itext.list.CGProcessImpressionReleveAVS;

public class CGPeriodeComptableImprimerReleveAVSViewBean extends CGProcessImpressionReleveAVS implements
        globaz.framework.bean.FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CGExerciceComptableImprimerPlanComptableViewBean.
     * 
     * @param session
     * @param filenameRoot
     * @param companyName
     * @param documentTitle
     * @param manager
     */
    public CGPeriodeComptableImprimerReleveAVSViewBean() throws Exception {
        super(new globaz.globall.db.BSession(globaz.helios.application.CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * La période comptable est-elle clôturé ?
     * 
     * @return True si clotûré. False si non clôturé ou problème.
     */
    public boolean isPeriodeCloture() {
        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());

        periode.setIdPeriodeComptable(getIdPeriodeComptable());

        try {
            periode.retrieve(getTransaction());
        } catch (Exception e) {
            return false;
        }

        if (periode.isNew() || periode.hasErrors()) {
            return false;
        } else {
            return periode.isEstCloture().booleanValue();
        }
    }

    /**
     * La période comptable est-elle de type clôture ?
     * 
     * @return True si clotûre. False si non clôture ou problème.
     */
    public boolean isPeriodeCodeCloture() {
        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(getSession());

        periode.setIdPeriodeComptable(getIdPeriodeComptable());

        try {
            periode.retrieve(getTransaction());
        } catch (Exception e) {
            return false;
        }

        if (periode.isNew() || periode.hasErrors()) {
            return false;
        } else {
            return periode.isCloture();
        }
    }
}
