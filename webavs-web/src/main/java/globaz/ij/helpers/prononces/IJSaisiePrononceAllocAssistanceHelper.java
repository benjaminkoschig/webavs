/*
 * Cr�� le 24 oct. 07
 */
package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.utils.IJUtils;
import globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJSaisiePrononceAllocAssistanceHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJAbstractPrononceProxyViewBean prononceProxyViewBean = (IJAbstractPrononceProxyViewBean) viewBean;

        if (prononceProxyViewBean.isModifie()) {
            // HACK cr�ation d'une transaction pour avoir la m�me pour le
            // prononce et le revenu
            BTransaction transaction = null;
            try {
                transaction = (BTransaction) ((BSession) session).newTransaction();
                transaction.openTransaction();

                validate(transaction, prononceProxyViewBean);
                // reinitialisation du prononc
                IJPrononceRegles.reinitialiser((BSession) session, transaction, prononceProxyViewBean.getPrononce());

                if (transaction.hasErrors()) {
                    prononceProxyViewBean.getSession().addError(transaction.getErrors().toString());
                } else {
                    prononceProxyViewBean.update(transaction);
                }
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
                        } else {
                            transaction.commit();
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    public void calculer(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        // dans le cas d'une allocation d'assistance, on doit uniquement changer
        // l'etat a valide
        IJAbstractPrononceProxyViewBean prononceProxyViewBean = (IJAbstractPrononceProxyViewBean) viewBean;

        // HACK cr�ation d'une transaction pour avoir la m�me pour le prononce
        // et le revenu
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();
            if (prononceProxyViewBean.isModifie()) {

                validate(transaction, prononceProxyViewBean);

                // reinitialisation du prononc
                IJPrononceRegles.reinitialiser(session, transaction, prononceProxyViewBean.getPrononce());

                if (transaction.hasErrors()) {
                    prononceProxyViewBean.getSession().addError(transaction.getErrors().toString());
                } else {
                    prononceProxyViewBean.setCsEtat(IIJPrononce.CS_DECIDE);
                    prononceProxyViewBean.update(transaction);
                }
            } else {
                prononceProxyViewBean.setCsEtat(IIJPrononce.CS_DECIDE);
                prononceProxyViewBean.update(transaction);

                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }

            }
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
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

    /**
     * retrouver par introspection le nom de la methode de cette class qu'il faut executer.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private void validate(BTransaction transaction, IJAbstractPrononceProxyViewBean viewBean) throws Exception {

        int officeAI = Integer.parseInt(viewBean.getOfficeAI());
        if (officeAI < 301 || (officeAI > 325 && officeAI != 327 && officeAI != 350)) {
            transaction.addErrors(viewBean.getSession().getLabel("ERREUR_OFFICE_AI"));
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getNoDecisionAI())) {
            // si le num�ro de d�cision est rempli, v�rification de l'int�grit�
            // du chiffre rentr�

            // Format du chiffre : AAAANNNNNNC
            // AAAA => Ann�e
            // NNNNNN => Incr�ment de 6
            // C => Chiffre cl�

            // On cr�er la cl�

            if (viewBean.getNoDecisionAI().length() == 11) {
                String cleCr�e = IJUtils.getChiffreCleDecision(viewBean.getSession(), viewBean.getNoAVS(),
                        viewBean.getOfficeAI(), viewBean.getNoDecisionAI().substring(0, 10));

                // on compare la cl� saisie et la cl� cr�e
                String cleSaisie = viewBean.getNoDecisionAI().substring(10);

                if (!cleCr�e.equals(cleSaisie)) {
                    transaction.addErrors("Num�ro de d�cision AI incorrect ! la cl� doit �tre : "+cleCr�e);
                }
            } else {
                transaction.addErrors("Num�ro de d�cision AI incorrect ! Format : AAAANNNNNNC");
            }
        } else {
            transaction.addErrors("Num�ro de d�cision AI obligatoire!");
        }
    }

}
