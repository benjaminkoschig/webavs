/*
 * Créé le 7. sept. 07
 */
package globaz.corvus.helpers.decisions;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.decisions.REDecisionsContainer;
import globaz.corvus.vb.decisions.REDecisionsICViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParameters;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * 
 * 
 * 
 * @author SCR
 * 
 */
public class REDecisionsHelper extends PRAbstractHelper {

    /**
     * 
     * Charge la décision dans le container, avec toutes les infos nécessaires à son affichage
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            REDecisionsICViewBean vb = (REDecisionsICViewBean) viewBean;
            String idDecision = vb.getIdDecision();

            REDecisionEntity decision = new REDecisionEntity();
            decision.setIdDecision(idDecision);
            decision.setId(idDecision);
            decision.setISession(session);
            decision.retrieve(transaction);

            String idDemandeRente = decision.getIdDemandeRente();
            REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
            dem.setSession((BSession) session);
            dem.setIdDemandeRente(idDemandeRente);
            dem.retrieve(transaction);

            vb.setIdTiersRequerant(dem.getIdTiersRequerant());
            vb.setIdDemandeRente(dem.getIdDemandeRente());
            REDecisionsContainer decisionContainer = new REDecisionsContainer();
            decisionContainer.loadDecision((BSession) session, decision);
            decisionContainer.parcourDecisionsIC((BSession) session);
            decisionContainer.setIdDemandeRente(decision.getIdDemandeRente());
            vb.setDecisionsContainer(decisionContainer);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }

            viewBean.setMessage("Error " + e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            viewBean.setMessage(transaction.getErrors().toString());
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

    public FWViewBeanInterface activerValidationDecision(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        toggleParamAuthorisationValidationDecision(session, true);
        return viewBean;
    }

    public FWViewBeanInterface desactiverValidationDecision(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        toggleParamAuthorisationValidationDecision(session, false);
        return viewBean;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        return deleguerExecute(viewBean, action, session);
    }

    private void toggleParamAuthorisationValidationDecision(BSession session, boolean authoriser) throws Exception {

        FWParameters param = new FWParameters();
        param.setSession(session);

        param.setIdAppl(REPmtMensuel.PARAM_VAL_DEC_APP_NAME);
        param.setIdCleDiffere(REPmtMensuel.PARAM_VAL_DEC_ID);
        param.setIdCodeSysteme("11111111");
        param.setDateDebutValidite("01.01.2000");
        param.retrieve();

        if (authoriser) {
            param.setValeurAlpha(REPmtMensuel.AUTHORISER_VALIDATION_DES_DECISIONS);
        } else {
            param.setValeurAlpha(REPmtMensuel.INTERDIRE_VALIDATION_DES_DECISIONS);
        }
        param.save();
    }
}
