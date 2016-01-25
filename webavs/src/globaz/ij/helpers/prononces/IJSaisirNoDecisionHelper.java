package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.utils.IJUtils;
import globaz.ij.vb.prononces.IJSaisirNoDecisionViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class IJSaisirNoDecisionHelper extends PRAbstractHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Met a jours le n° de decision AI du prononce
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface modiferNoDecision(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BTransaction transaction = null;

        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            IJSaisirNoDecisionViewBean sndViewBean = (IJSaisirNoDecisionViewBean) viewBean;

            IJPrononce p = new IJPrononce();
            p.setSession(session);
            p.setIdPrononce(sndViewBean.getIdPrononce());
            p.retrieve(transaction);

            // validation du n° de decision AI
            validate(transaction, sndViewBean, p);

            p.setNoDecisionAI(sndViewBean.getNewNoDecisionAI());
            p.update(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(transaction.getErrors().toString());
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return viewBean;
    }

    /**
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface saisirNoDecision(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        return viewBean;
    }

    private void validate(BTransaction transaction, IJSaisirNoDecisionViewBean viewBean, IJPrononce p) throws Exception {

        int officeAI = Integer.parseInt(p.getOfficeAI());
        if (officeAI < 301 || (officeAI > 325 && officeAI != 327 && officeAI != 350)) {
            transaction.addErrors(viewBean.getSession().getLabel("ERREUR_OFFICE_AI"));
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getNewNoDecisionAI())) {
            // si le numéro de décision est rempli, vérification de l'intégrité
            // du chiffre rentré

            // Format du chiffre : AAAANNNNNNC
            // AAAA => Année
            // NNNNNN => Incrément de 6
            // C => Chiffre clé
            if (viewBean.getNewNoDecisionAI().length() == 11) {

                // On créer la clé
                String cleCrée = IJUtils.getChiffreCleDecision(viewBean.getSession(), viewBean.getNoAVS(),
                        p.getOfficeAI(), viewBean.getNewNoDecisionAI().substring(0, 10));

                // on compare la clé saisie et la clé crée
                String cleSaisie = viewBean.getNewNoDecisionAI().substring(10);

                if (!cleCrée.equals(cleSaisie)) {
                    transaction.addErrors(viewBean.getSession().getLabel("NO_DECISION_ERR"));
                }

            } else {
                transaction.addErrors(viewBean.getSession().getLabel("NO_DECISION_ERR"));
            }
        } else {
            transaction.addErrors(viewBean.getSession().getLabel("NO_DECISION_OBLIGATOIRE"));
        }
    }
}
