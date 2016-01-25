/*
 * Créé le 11 décembre 2009
 */
package globaz.cygnus.helpers.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenu;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFQdSaisieSoldeExcedentDeRevenuViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Date;

/**
 * @author jje
 */
public class RFQdSaisieSoldeExcedentDeRevenuHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajout historisé du solde excédent de revenu d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;

        BITransaction transaction = null;

        RFQdSaisieSoldeExcedentDeRevenuViewBean rfsoExVb = (RFQdSaisieSoldeExcedentDeRevenuViewBean) viewBean;

        validate(rfsoExVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            try {
                transaction = currentSession.newTransaction();
                transaction.openTransaction();

                // calcule le nouvel id unique de famille de modification
                int famModifCompteur = 0;
                RFQdSoldeExcedentDeRevenuManager mgr = new RFQdSoldeExcedentDeRevenuManager();
                mgr.setSession(currentSession);
                mgr.setForIdFamilleMax(true);
                mgr.changeManagerSize(0);
                mgr.find(transaction);

                if (!mgr.isEmpty()) {
                    RFQdSoldeExcedentDeRevenu sc = (RFQdSoldeExcedentDeRevenu) mgr.getFirstEntity();
                    if (null != sc) {
                        famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                    } else {
                        famModifCompteur = 1;
                    }
                } else {
                    famModifCompteur = 1;
                }

                RFQdSoldeExcedentDeRevenu rfsoEx = new RFQdSoldeExcedentDeRevenu();
                rfsoEx.setSession(currentSession);
                rfsoEx.setConcerne(rfsoExVb.getConcerne());
                rfsoEx.setRemarque(rfsoExVb.getRemarque());
                rfsoEx.setMontantSoldeExcedent(rfsoExVb.getMontantSoldeExcedent());
                rfsoEx.setVisaGestionnaire(currentSession.getUserName());
                rfsoEx.setIdFamilleModification(Integer.toString(famModifCompteur));
                rfsoEx.setIdQd(rfsoExVb.getIdQd());
                rfsoEx.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                rfsoEx.setTypeModification(IRFQd.CS_AJOUT);
                rfsoEx.setIdSoldeExcedentModifie("");

                rfsoEx.add(transaction);

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()
                                || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
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
     * Suppression historisé du solde excédent de revenu d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BSession currentSession = (BSession) session;

        BITransaction transaction = null;

        try {

            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFQdSaisieSoldeExcedentDeRevenuViewBean rfsoExVb = (RFQdSaisieSoldeExcedentDeRevenuViewBean) viewBean;

            RFQdSoldeExcedentDeRevenu rfsoEx = new RFQdSoldeExcedentDeRevenu();
            rfsoEx.setSession(currentSession);
            rfsoEx.setConcerne(currentSession.getLabel("JSP_RF_QD_S_SUPPRESSION_MANUELLE"));
            rfsoEx.setRemarque(rfsoExVb.getRemarque());
            rfsoEx.setMontantSoldeExcedent(rfsoExVb.getMontantSoldeExcedent());
            rfsoEx.setVisaGestionnaire(currentSession.getUserName());
            rfsoEx.setIdFamilleModification(rfsoExVb.getIdFamilleModification());
            rfsoEx.setIdQd(rfsoExVb.getIdQd());
            rfsoEx.setDateModification(JadeDateUtil.getDMYDate(new Date()));
            rfsoEx.setTypeModification(IRFQd.CS_SUPPRESSION);
            rfsoEx.setIdSoldeExcedentModifie(rfsoExVb.getIdQdHistorique());

            rfsoEx.add(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
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

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_find(globaz.globall.db. BIPersistentObjectList,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        super._find(persistentList, action, session);
    }

    /**
     * Modification historisée du solde excédent de revenu d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;

        BITransaction transaction = null;

        RFQdSaisieSoldeExcedentDeRevenuViewBean rfsoExVb = (RFQdSaisieSoldeExcedentDeRevenuViewBean) viewBean;

        validate(rfsoExVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFQdSoldeExcedentDeRevenu rfsoEx = new RFQdSoldeExcedentDeRevenu();
                rfsoEx.setSession(currentSession);
                rfsoEx.setConcerne(rfsoExVb.getConcerne());
                rfsoEx.setRemarque(rfsoExVb.getRemarque());
                rfsoEx.setMontantSoldeExcedent(rfsoExVb.getMontantSoldeExcedent());
                rfsoEx.setVisaGestionnaire(currentSession.getUserName());
                rfsoEx.setIdFamilleModification(rfsoExVb.getIdFamilleModification());
                rfsoEx.setIdQd(rfsoExVb.getIdQd());
                rfsoEx.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                rfsoEx.setTypeModification(IRFQd.CS_MODIFICATION);
                rfsoEx.setIdSoldeExcedentModifie(rfsoExVb.getIdQdHistorique());

                rfsoEx.add(transaction);

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()
                                || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

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

    private void validate(RFQdSaisieSoldeExcedentDeRevenuViewBean viewBean) {

        if (JadeStringUtil.isEmpty(viewBean.getMontantSoldeExcedent())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_SOEXR_H_MONTANT");
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            if (!RFUtils.isMontantArrondiCinqCts(viewBean.getMontantSoldeExcedent()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_SOEXR_H_ARRONDI");
            }
        }
    }

}