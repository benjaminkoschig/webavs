/*
 * Créé le 11 décembre 2009
 */
package globaz.cygnus.helpers.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdSoldeCharge;
import globaz.cygnus.db.qds.RFQdSoldeChargeManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFQdSaisieSoldeChargeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jje
 */
public class RFQdSaisieSoldeChargeHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajout historisé du solde de charge d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;

        BITransaction transaction = null;
        RFQdSaisieSoldeChargeViewBean rfsocha = (RFQdSaisieSoldeChargeViewBean) viewBean;

        validate(rfsocha, RFUtils.add, session);
        if (!FWViewBeanInterface.ERROR.equals(rfsocha.getMsgType())) {
            try {
                transaction = currentSession.newTransaction();
                transaction.openTransaction();

                // calcule le nouvel id unique de famille de modification
                int famModifCompteur = 0;
                RFQdSoldeChargeManager mgr = new RFQdSoldeChargeManager();
                mgr.setSession(currentSession);
                mgr.setForIdFamilleMax(true);
                mgr.changeManagerSize(0);
                mgr.find(transaction);

                if (!mgr.isEmpty()) {
                    RFQdSoldeCharge sc = (RFQdSoldeCharge) mgr.getFirstEntity();
                    if (null != sc) {
                        famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                    } else {
                        famModifCompteur = 1;
                    }
                } else {
                    famModifCompteur = 1;
                }

                RFQdSoldeCharge socha = new RFQdSoldeCharge();
                socha.setSession(currentSession);
                socha.setConcerne(rfsocha.getConcerne());
                socha.setRemarque(rfsocha.getRemarque());
                socha.setMontantSolde(rfsocha.getMontantSolde());
                socha.setVisaGestionnaire(currentSession.getUserName());
                socha.setIdFamilleModification(Integer.toString(famModifCompteur));
                socha.setIdQd(rfsocha.getIdQd());
                socha.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                socha.setTypeModification(IRFQd.CS_AJOUT);
                socha.setIdSoldeChargeModifie("");

                socha.add(transaction);

                if (isMontantResiduelNegatif(socha.getIdQd(), currentSession)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_SOCHA_H_MONTANT_RESIDUEL_NEGATIF");
                }

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
     * Suppression historisé du solde de charge d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BSession currentSession = (BSession) session;

        BITransaction transaction = null;
        RFQdSaisieSoldeChargeViewBean rfsochaVb = (RFQdSaisieSoldeChargeViewBean) viewBean;

        validate(rfsochaVb, RFUtils.del, session);
        if (!FWViewBeanInterface.ERROR.equals(rfsochaVb.getMsgType())) {

            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFQdSoldeCharge socha = new RFQdSoldeCharge();
                socha.setSession(currentSession);
                socha.setConcerne(currentSession.getLabel("JSP_RF_QD_S_SUPPRESSION_MANUELLE"));
                socha.setRemarque(rfsochaVb.getRemarque());
                socha.setMontantSolde(rfsochaVb.getMontantSolde());
                socha.setVisaGestionnaire(currentSession.getUserName());
                socha.setIdFamilleModification(rfsochaVb.getIdFamilleModification());
                socha.setIdQd(rfsochaVb.getIdQd());
                socha.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                socha.setTypeModification(IRFQd.CS_SUPPRESSION);
                socha.setIdSoldeChargeModifie(rfsochaVb.getIdQdHistorique());

                socha.add(transaction);

                if (isMontantResiduelNegatif(socha.getIdQd(), currentSession)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_SOCHA_H_MONTANT_RESIDUEL_NEGATIF");
                }

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
     * Modification historisée du solde de charge d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;

        BITransaction transaction = null;
        RFQdSaisieSoldeChargeViewBean rfsochaVb = (RFQdSaisieSoldeChargeViewBean) viewBean;

        validate(rfsochaVb, RFUtils.upd, session);
        if (!FWViewBeanInterface.ERROR.equals(rfsochaVb.getMsgType())) {

            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFQdSoldeCharge socha = new RFQdSoldeCharge();
                socha.setSession(currentSession);
                socha.setConcerne(rfsochaVb.getConcerne());
                socha.setRemarque(rfsochaVb.getRemarque());
                socha.setMontantSolde(rfsochaVb.getMontantSolde());
                socha.setVisaGestionnaire(currentSession.getUserName());
                socha.setIdFamilleModification(rfsochaVb.getIdFamilleModification());
                socha.setIdQd(rfsochaVb.getIdQd());
                socha.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                socha.setTypeModification(IRFQd.CS_MODIFICATION);
                socha.setIdSoldeChargeModifie(rfsochaVb.getIdQdHistorique());

                socha.add(transaction);

                if (isMontantResiduelNegatif(socha.getIdQd(), currentSession)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_SOCHA_H_MONTANT_RESIDUEL_NEGATIF");
                }

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

    private boolean isMontantResiduelNegatif(String idQd, BISession session) throws Exception {

        String limiteAnnuelleStr = "";

        RFQd rfQd = new RFQd();
        rfQd.setSession((BSession) session);
        rfQd.setIdQd(idQd);

        rfQd.retrieve();

        if (!rfQd.isNew()) {
            limiteAnnuelleStr = rfQd.getLimiteAnnuelle();
        } else {
            throw new Exception("RFQdSaisieSoldeChargeHelper.isMontantResiduelNegatif(): Qd introuvable");
        }

        BigDecimal montantResiduelBigDec = new BigDecimal(RFUtils.getMntResiduel(limiteAnnuelleStr,
                RFUtils.getAugmentationQd(idQd, (BSession) session),
                RFUtils.getSoldeDeCharge(idQd, (BSession) session), rfQd.getMontantChargeRfm()));

        return montantResiduelBigDec.compareTo(new BigDecimal(0)) < 0;
    }

    /**
     * Méthode executant la validation de la demande
     * 
     * @param FWViewBeanInterface
     *            , BSession
     * @throws Exception
     */
    private void validate(RFQdSaisieSoldeChargeViewBean rfsochaVb, String method, BISession session) throws Exception {

        String soldeExcRev = RFUtils.getSoldeExcedentDeRevenu(rfsochaVb.getIdQd(), (BSession) session);

        if (!JadeStringUtil.isBlankOrZero(soldeExcRev) && !soldeExcRev.equals("0.00") && !soldeExcRev.equals("0.0")) {
            RFUtils.setMsgErreurViewBean(rfsochaVb, "ERREUR_RF_S_SOCHA_H_SOLDE_EXCEDENT");
        }

        if (JadeStringUtil.isEmpty(rfsochaVb.getMontantSolde())) {
            RFUtils.setMsgErreurViewBean(rfsochaVb, "ERREUR_RF_S_SOCHA_H_MONTANT");
        }

        if (method.equals(RFUtils.add) || method.equals(RFUtils.upd)) {
            if (!FWViewBeanInterface.ERROR.equals(rfsochaVb.getMsgType())
                    && !RFUtils.isMontantArrondiCinqCts(rfsochaVb.getMontantSolde()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(rfsochaVb, "ERREUR_RF_S_SOCHA_H_ARRONDI");
            }
        }

    }
}