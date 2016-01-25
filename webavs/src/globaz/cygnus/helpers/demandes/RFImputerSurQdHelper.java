/*
 * Créé le 11 février 2010
 */
package globaz.cygnus.helpers.demandes;

import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossierManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.demandes.RFImputerSurQdViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jje
 */
public class RFImputerSurQdHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        validate((RFImputerSurQdViewBean) viewBean, session);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFDemande rfDemande = new RFDemande();
                rfDemande.setSession((BSession) session);
                rfDemande.setIdDemande(((RFImputerSurQdViewBean) viewBean).getIdDemande());

                rfDemande.retrieve();

                if (!rfDemande.isNew()) {

                    if (JadeStringUtil.isBlankOrZero(((RFImputerSurQdViewBean) viewBean).getIdQdPrincipale())) {
                        rfDemande.setIsForcerImputationSurQd(Boolean.FALSE);
                        rfDemande.setIdQdPrincipale("");
                    } else {
                        rfDemande.setIsForcerImputationSurQd(Boolean.TRUE);
                        rfDemande.setIdQdPrincipale(((RFImputerSurQdViewBean) viewBean).getIdQdPrincipale());
                    }

                    rfDemande.update(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(viewBean, "_update()", "RFImputerSurQdHelper");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Méthode vérifiant si la Qd principale existe et concerne le même dossier que la demande
     * 
     * @param FWViewBeanInterface
     *            , BSession
     * @throws Exception
     */
    private void validate(RFImputerSurQdViewBean viewBean, BISession session) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(viewBean.getIdQdPrincipale())) {
            RFQdPrincipaleJointDossierManager rfQdPriJoiDosMgr = new RFQdPrincipaleJointDossierManager();
            rfQdPriJoiDosMgr.setSession((BSession) session);
            Set<String> idsQdSet = new HashSet<String>();
            idsQdSet.add(viewBean.getIdQdPrincipale());
            rfQdPriJoiDosMgr.setForIdsQd(idsQdSet);
            rfQdPriJoiDosMgr.setForIdDossier(viewBean.getIdDossier());
            rfQdPriJoiDosMgr.changeManagerSize(0);
            rfQdPriJoiDosMgr.find();

            if (rfQdPriJoiDosMgr.size() != 1) {
                RFUtils.setMsgErreurViewBean(viewBean,
                        "ERREUR_RF_DEM_IMPUTER_SUR_QD_CONCERNE_AUTRE_DOSSIER_OU_QD_INEXISTANTE");
            }
        }
    }
}
