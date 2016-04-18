package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.vb.prestations.IJIJCalculeeJointGrandePetiteViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Descpription.
 * 
 * @author scr Date de création 16 sept. 05
 */
public class IJCalculeeAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJBaseIndemnisationAction.
     * 
     * @param servlet
     */
    public IJCalculeeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        IJIJCalculeeJointGrandePetiteViewBean ijcViewBean = (IJIJCalculeeJointGrandePetiteViewBean) viewBean;

        String dest = getUserActionURL(request, IIJActions.ACTION_IJ_CALCULEES);

        return dest + ".chercher&idPrononce=" + ijcViewBean.getIdPrononce() + "&csTypeIJ=" + ijcViewBean.getCsTypeIJ();
    }

    /**
     * Receperation de valeurs des indemnite journaliere avant la mise a jours dans le helper
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        IJIJCalculeeJointGrandePetiteViewBean ijcViewBean = (IJIJCalculeeJointGrandePetiteViewBean) viewBean;
        // on set la valeur a la main car le "1" n'est pas pris par Boolean
        if (!JadeStringUtil.isNull(request.getParameter("isDroitPrestationPourEnfant"))
                && "1".equals(request.getParameter("isDroitPrestationPourEnfant"))) {
            ijcViewBean.setIsDroitPrestationPourEnfant(Boolean.TRUE);
        }

        // ////////////////////////////////////////////////////////////////////////////////////////////////////
        // indemnite journalieres interne
        // ////////////////////////////////////////////////////////////////////////////////////////////////////
        IJIndemniteJournaliere ijInt = ijcViewBean.getIndemniteJournaliereInterne();
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantSupplementaireReadaptation"))) {
            ijInt.setMontantSupplementaireReadaptation(request
                    .getParameter("ijInterneMontantSupplementaireReadaptation"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantGarantiAANonReduit"))) {
            ijInt.setMontantGarantiAANonReduit(request.getParameter("ijInterneMontantGarantiAANonReduit"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneIndemniteAvantReduction"))) {
            ijInt.setIndemniteAvantReduction(request.getParameter("ijInterneIndemniteAvantReduction"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneDeductionRenteAI"))) {
            ijInt.setDeductionRenteAI(request.getParameter("ijInterneDeductionRenteAI"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneFractionReductionSiRevenuAvantReadaptation"))) {
            ijInt.setFractionReductionSiRevenuAvantReadaptation(request
                    .getParameter("ijInterneFractionReductionSiRevenuAvantReadaptation"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantReductionSiRevenuAvantReadaptation"))) {
            ijInt.setMontantReductionSiRevenuAvantReadaptation(request
                    .getParameter("ijInterneMontantReductionSiRevenuAvantReadaptation"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantJournalierIndemnite"))) {
            ijInt.setMontantJournalierIndemnite(request.getParameter("ijInterneMontantJournalierIndemnite"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantGarantiAAReduit"))) {
            ijInt.setMontantGarantiAAReduit(request.getParameter("ijInterneMontantGarantiAAReduit"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantComplet"))) {
            ijInt.setMontantComplet(request.getParameter("ijInterneMontantComplet"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantPlafonne"))) {
            ijInt.setMontantPlafonne(request.getParameter("ijInterneMontantPlafonne"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijInterneMontantPlafonneMinimum"))) {
            ijInt.setMontantPlafonneMinimum(request.getParameter("ijInterneMontantPlafonneMinimum"));
        }

        // ////////////////////////////////////////////////////////////////////////////////////////////////////
        // indemnite journalieres externe
        // ////////////////////////////////////////////////////////////////////////////////////////////////////
        IJIndemniteJournaliere ijExt = ijcViewBean.getIndemniteJournaliereExterne();
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantSupplementaireReadaptation"))) {
            ijExt.setMontantSupplementaireReadaptation(request
                    .getParameter("ijExterneMontantSupplementaireReadaptation"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantGarantiAANonReduit"))) {
            ijExt.setMontantGarantiAANonReduit(request.getParameter("ijExterneMontantGarantiAANonReduit"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneIndemniteAvantReduction"))) {
            ijExt.setIndemniteAvantReduction(request.getParameter("ijExterneIndemniteAvantReduction"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneDeductionRenteAI"))) {
            ijExt.setDeductionRenteAI(request.getParameter("ijExterneDeductionRenteAI"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneFractionReductionSiRevenuAvantReadaptation"))) {
            ijExt.setFractionReductionSiRevenuAvantReadaptation(request
                    .getParameter("ijExterneFractionReductionSiRevenuAvantReadaptation"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantReductionSiRevenuAvantReadaptation"))) {
            ijExt.setMontantReductionSiRevenuAvantReadaptation(request
                    .getParameter("ijExterneMontantReductionSiRevenuAvantReadaptation"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantJournalierIndemnite"))) {
            ijExt.setMontantJournalierIndemnite(request.getParameter("ijExterneMontantJournalierIndemnite"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantGarantiAAReduit"))) {
            ijExt.setMontantGarantiAAReduit(request.getParameter("ijExterneMontantGarantiAAReduit"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantComplet"))) {
            ijExt.setMontantComplet(request.getParameter("ijExterneMontantComplet"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantPlafonne"))) {
            ijExt.setMontantPlafonne(request.getParameter("ijExterneMontantPlafonne"));
        }
        if (!JadeStringUtil.isNull(request.getParameter("ijExterneMontantPlafonneMinimum"))) {
            ijExt.setMontantPlafonneMinimum(request.getParameter("ijExterneMontantPlafonneMinimum"));
        }

        return viewBean;

    }

}
