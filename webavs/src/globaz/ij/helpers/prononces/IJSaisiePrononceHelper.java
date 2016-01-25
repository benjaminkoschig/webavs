/*
 * Cr�� le 23 sept. 05
 */
package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.ij.db.prononces.IJRevenu;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.utils.IJUtils;
import globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJSaisiePrononceHelper extends PRAbstractHelper {

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

                // ajout ou update du revenu readaptation.
                IJRevenu revenu = prononceProxyViewBean.loadRevenuReadaptation();

                if (revenu == null) {
                    revenu = new IJRevenu();
                    revenu.setSession((BSession) session);
                }

                // vidage des erreurs
                session.getErrors();

                revenu.setRevenu(prononceProxyViewBean.getMontantRevenuReadaptation());
                revenu.setCsPeriodiciteRevenu(prononceProxyViewBean.getCsPeriodiciteRevenuReadaptation());
                revenu.setNbHeuresSemaine(prononceProxyViewBean.getHeuresSemaineRevenuReadaptation());
                revenu.setAnnee(prononceProxyViewBean.getAnneeNiveauRevenuReadaptation());

                revenu.save(transaction);

                if (transaction.hasErrors()) {
                    prononceProxyViewBean.getSession().addError(transaction.getErrors().toString());
                } else {
                    prononceProxyViewBean.setIdRevenuReadaptation(revenu.getIdRevenu());

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
