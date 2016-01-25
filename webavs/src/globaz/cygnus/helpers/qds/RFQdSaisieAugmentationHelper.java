/*
 * Créé le 11 décembre 2009
 */
package globaz.cygnus.helpers.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAugmentation;
import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFQdSaisieAugmentationViewBean;
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
public class RFQdSaisieAugmentationHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajout historisé d'une augmentation de Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;
        BITransaction transaction = null;

        RFQdSaisieAugmentationViewBean rfAugVb = (RFQdSaisieAugmentationViewBean) viewBean;
        RFQdAugmentation aug = new RFQdAugmentation();

        validate(rfAugVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            try {
                transaction = currentSession.newTransaction();
                transaction.openTransaction();

                // calcule le nouvel id unique de famille de modification
                int famModifCompteur = 0;
                RFQdAugmentationManager mgr = new RFQdAugmentationManager();
                mgr.setSession(currentSession);
                mgr.setForIdFamilleMax(true);
                mgr.changeManagerSize(0);
                mgr.find(transaction);

                if (!mgr.isEmpty()) {
                    RFQdAugmentation sc = (RFQdAugmentation) mgr.getFirstEntity();
                    if (null != sc) {
                        famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                    } else {
                        famModifCompteur = 1;
                    }
                } else {
                    famModifCompteur = 1;
                }

                aug.setSession(currentSession);
                aug.setConcerne(rfAugVb.getConcerne());
                aug.setRemarque(rfAugVb.getRemarque());
                aug.setMontantAugmentationQd(rfAugVb.getMontantAugmentationQd());
                aug.setVisaGestionnaire(currentSession.getUserName());
                aug.setIdFamilleModification(Integer.toString(famModifCompteur));
                aug.setIdQd(rfAugVb.getIdQd());
                aug.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                aug.setTypeModification(IRFQd.CS_AJOUT);
                aug.setIdAugmentationQdModifiePar("");

                aug.add(transaction);

                if (isMontantResiduelNegatif(rfAugVb.getIdQd(), currentSession)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_AUG_H_MONTANT_RESIDUEL_NEGATIF");
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
     * Supression historisée d'une augmentation de Qd
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

            RFQdSaisieAugmentationViewBean rfAugVb = (RFQdSaisieAugmentationViewBean) viewBean;

            RFQdAugmentation rfQdAug = new RFQdAugmentation();
            rfQdAug.setSession(currentSession);
            rfQdAug.setConcerne(currentSession.getLabel("JSP_RF_QD_S_SUPPRESSION_MANUELLE"));
            rfQdAug.setRemarque(rfAugVb.getRemarque());
            rfQdAug.setMontantAugmentationQd(rfAugVb.getMontantAugmentationQd());
            rfQdAug.setVisaGestionnaire(currentSession.getUserName());
            rfQdAug.setIdFamilleModification(rfAugVb.getIdFamilleModification());
            rfQdAug.setIdQd(rfAugVb.getIdQd());
            rfQdAug.setDateModification(JadeDateUtil.getDMYDate(new Date()));
            rfQdAug.setTypeModification(IRFQd.CS_SUPPRESSION);
            rfQdAug.setIdAugmentationQdModifiePar(rfAugVb.getIdQdHistorique());

            rfQdAug.add(transaction);

            if (isMontantResiduelNegatif(rfAugVb.getIdQd(), currentSession)) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_AUG_H_MONTANT_RESIDUEL_NEGATIF");
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
     * Modification historisée d'une augmentation de Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;
        BITransaction transaction = null;

        RFQdSaisieAugmentationViewBean rfAugVb = (RFQdSaisieAugmentationViewBean) viewBean;

        validate(rfAugVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFQdAugmentation rfQdAug = new RFQdAugmentation();
                rfQdAug.setSession(currentSession);
                rfQdAug.setConcerne(rfAugVb.getConcerne());
                rfQdAug.setRemarque(rfAugVb.getRemarque());
                rfQdAug.setMontantAugmentationQd(rfAugVb.getMontantAugmentationQd());
                rfQdAug.setVisaGestionnaire(currentSession.getUserName());
                rfQdAug.setIdFamilleModification(rfAugVb.getIdFamilleModification());
                rfQdAug.setIdQd(rfAugVb.getIdQd());
                rfQdAug.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                rfQdAug.setTypeModification(IRFQd.CS_MODIFICATION);
                rfQdAug.setIdAugmentationQdModifiePar(rfAugVb.getIdQdHistorique());

                rfQdAug.add(transaction);

                if (isMontantResiduelNegatif(rfAugVb.getIdQd(), currentSession)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_AUG_H_MONTANT_RESIDUEL_NEGATIF");
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
            throw new Exception("RFQdSaisieAugmentationHelper.isMontantResiduelNegatif(): Qd introuvable");
        }

        BigDecimal montantResiduelBigDec = new BigDecimal(RFUtils.getMntResiduel(limiteAnnuelleStr,
                RFUtils.getAugmentationQd(idQd, (BSession) session),
                RFUtils.getSoldeDeCharge(idQd, (BSession) session), rfQd.getMontantChargeRfm()));

        return montantResiduelBigDec.compareTo(new BigDecimal(0)) < 0;
    }

    /*
     * private void suppressionAugmentation(FWViewBeanInterface viewBean, BSession currentSession, String
     * idAugmentation) throws Exception {
     * 
     * BITransaction transaction = null;
     * 
     * try {
     * 
     * transaction = currentSession.newTransaction(); transaction.openTransaction();
     * 
     * RFQdAugmentation augSup = new RFQdAugmentation(); augSup.setSession(currentSession);
     * augSup.setId(idAugmentation); augSup.retrieve();
     * 
     * if (!augSup.isNew()) { augSup.delete(transaction); RFUtils.setMsgErreurViewBean(viewBean,
     * "ERREUR_RF_S_AUG_H_MONTANT_RESIDUEL_NEGATIF"); }
     * 
     * } catch (Exception e) { if (transaction != null) { transaction.setRollbackOnly(); } throw e; } finally { if
     * (transaction != null) { try { if (transaction.hasErrors() || transaction.isRollbackOnly()) {
     * transaction.rollback(); } else { transaction.commit(); } } catch (Exception e) { e.printStackTrace(); throw e; }
     * finally { transaction.closeTransaction(); } } } }
     */

    private void validate(RFQdSaisieAugmentationViewBean viewBean) throws Exception {

        if (JadeStringUtil.isEmpty(viewBean.getMontantAugmentationQd())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_AUG_H_MONTANT_AUG");
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            if (!RFUtils.isMontantArrondiCinqCts(viewBean.getMontantAugmentationQd()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_S_AUG_H_ARRONDI_AUG");
            }
        }
    }
}