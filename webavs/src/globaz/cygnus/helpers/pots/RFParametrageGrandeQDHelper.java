// créé le 24 mars 2010
package globaz.cygnus.helpers.pots;

import globaz.cygnus.db.pots.RFPotsPC;
import globaz.cygnus.db.pots.RFPotsPCManager;
import globaz.cygnus.db.qds.RFQdPrincipaleJointDossierManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.pots.RFParametrageGrandeQDViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;

/**
 * author fha
 */
public class RFParametrageGrandeQDHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validate(viewBean, (BSession) session);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;

            RFParametrageGrandeQDViewBean potPCVB = (RFParametrageGrandeQDViewBean) viewBean;

            if (!validationAjout(potPCVB, session)) {

                try {
                    transaction = ((BSession) session).newTransaction();
                    transaction.openTransaction();

                    // ajout des montants pour cette période et type de soin
                    RFPotsPC potpc = new RFPotsPC();
                    potpc.setSession(potPCVB.getSession());
                    potpc.setDateDebut(potPCVB.getForDateDebut());
                    potpc.setDateFin(potPCVB.getForDateFin());
                    potpc.setMontantPlafondCoupleDomicile(potPCVB.getMontantPlafondCoupleDomicile());
                    potpc.setMontantPlafondAdulteEnfants(potPCVB.getMontantPlafondAdulteEnfants());
                    potpc.setMontantPlafondCoupleEnfants(potPCVB.getMontantPlafondCoupleEnfants());
                    potpc.setMontantPlafondEnfantsSepares(potPCVB.getMontantPlafondEnfantsSepares());
                    potpc.setMontantPlafondPersonneSeule(potPCVB.getMontantPlafondPersonneSeule());
                    potpc.setMontantPlafondSepareDomicile(potPCVB.getMontantPlafondSepareDomicile());
                    potpc.setMontantPlafondPourTous(potPCVB.getMontantPlafondPourTous());
                    potpc.setMontantPlafondEnfantsEnfants(potPCVB.getMontantPlafondEnfantsEnfants());
                    potpc.setMontantPlafondPensionnaireEnfantsEnfants(potPCVB
                            .getMontantPlafondPensionnaireEnfantsEnfants());
                    potpc.setMontantPlafondPensionnaireEnfantsSepares(potPCVB
                            .getMontantPlafondPensionnaireEnfantsSepares());
                    potpc.setMontantPlafondPensionnairePersonneSeule(potPCVB
                            .getMontantPlafondPensionnairePersonneSeule());
                    potpc.setMontantPlafondPensionnaireSepareDomicile(potPCVB
                            .getMontantPlafondPensionnaireSepareDomicile());
                    potpc.setMontantPlafondPensionnaireSepareHome(potPCVB.getMontantPlafondPensionnaireSepareHome());
                    potpc.setMontantPlafondPensionnairePourTous(potPCVB.getMontantPlafondPensionnairePourTous());

                    potpc.add(transaction);

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
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BITransaction transaction = null;

        RFParametrageGrandeQDViewBean rfPotPCVb = (RFParametrageGrandeQDViewBean) viewBean;

        validationSuppression(rfPotPCVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFPotsPC potpc = new RFPotsPC();
                potpc.setSession(rfPotPCVb.getSession());
                potpc.setIdPotPC(rfPotPCVb.getIdPotPC());
                potpc.retrieve(transaction);

                if (!potpc.isNew()) {
                    potpc.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfPotPCVb, "_delete()", "RFParametrageGrandeQDHelper");
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

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
    }

    /**
     * on charge la ligne qu'on a dans le viewBean
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            RFParametrageGrandeQDViewBean vb = (RFParametrageGrandeQDViewBean) viewBean;
            vb.setIsUpdate(Boolean.TRUE);
            super._retrieve(viewBean, action, session);
        } catch (Exception e) {
            RFUtils.setMsgErreurViewBean(viewBean, e.getMessage());
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validate(viewBean, (BSession) session);

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFParametrageGrandeQDViewBean potPCVB = (RFParametrageGrandeQDViewBean) viewBean;

            if (!validationUpdate(potPCVB, session)) {

                RFPotsPC potpc = new RFPotsPC();
                potpc.setSession(potPCVB.getSession());
                potpc.setIdPotPC(potPCVB.getIdPotPC());
                potpc.retrieve(transaction);

                if (!potpc.isNew()) {

                    potpc.setDateDebut(potPCVB.getForDateDebut());
                    potpc.setDateFin(potPCVB.getForDateFin());
                    potpc.setMontantPlafondCoupleDomicile(potPCVB.getMontantPlafondCoupleDomicile());
                    potpc.setMontantPlafondAdulteEnfants(potPCVB.getMontantPlafondAdulteEnfants());
                    potpc.setMontantPlafondCoupleEnfants(potPCVB.getMontantPlafondCoupleEnfants());
                    potpc.setMontantPlafondEnfantsSepares(potPCVB.getMontantPlafondEnfantsSepares());
                    potpc.setMontantPlafondPersonneSeule(potPCVB.getMontantPlafondPersonneSeule());
                    potpc.setMontantPlafondSepareDomicile(potPCVB.getMontantPlafondSepareDomicile());
                    potpc.setMontantPlafondPourTous(potPCVB.getMontantPlafondPourTous());
                    potpc.setMontantPlafondEnfantsEnfants(potPCVB.getMontantPlafondEnfantsEnfants());
                    potpc.setMontantPlafondPensionnaireEnfantsEnfants(potPCVB
                            .getMontantPlafondPensionnaireEnfantsEnfants());
                    potpc.setMontantPlafondPensionnaireEnfantsSepares(potPCVB
                            .getMontantPlafondPensionnaireEnfantsSepares());
                    potpc.setMontantPlafondPensionnairePersonneSeule(potPCVB
                            .getMontantPlafondPensionnairePersonneSeule());
                    potpc.setMontantPlafondPensionnaireSepareDomicile(potPCVB
                            .getMontantPlafondPensionnaireSepareDomicile());
                    potpc.setMontantPlafondPensionnaireSepareHome(potPCVB.getMontantPlafondPensionnaireSepareHome());
                    potpc.setMontantPlafondPensionnairePourTous(potPCVB.getMontantPlafondPensionnairePourTous());

                    potpc.update(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(potPCVB, "_update()", "RFParametrageGrandeQDHelper");
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

    protected boolean areMontantsVide(RFParametrageGrandeQDViewBean viewBean) {

        boolean b = "0.00".equals(viewBean.getMontantPlafondCoupleDomicile())
                && "0.00".equals(viewBean.getMontantPlafondAdulteEnfants())
                && "0.00".equals(viewBean.getMontantPlafondCoupleEnfants())
                && "0.00".equals(viewBean.getMontantPlafondEnfantsEnfants())
                && "0.00".equals(viewBean.getMontantPlafondEnfantsSepares())
                && "0.00".equals(viewBean.getMontantPlafondPersonneSeule())
                && "0.00".equals(viewBean.getMontantPlafondSepareDomicile())
                && "0.00".equals(viewBean.getMontantPlafondPourTous());

        boolean c = "0.00".equals(viewBean.getMontantPlafondPensionnaireEnfantsEnfants())
                && "0.00".equals(viewBean.getMontantPlafondPensionnaireEnfantsSepares())
                && "0.00".equals(viewBean.getMontantPlafondPensionnairePersonneSeule())
                && "0.00".equals(viewBean.getMontantPlafondPensionnaireSepareDomicile())
                && "0.00".equals(viewBean.getMontantPlafondPensionnaireSepareHome())
                && "0.00".equals(viewBean.getMontantPlafondPensionnairePourTous());

        return b || c;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /*
     * test si le String en paramètre représente un montant positif ou nul renvoi vrai si positif ou nul faux si négatif
     */
    private Boolean isNumericPositifOuNul(String montant) {
        // enlever le formatage : '
        montant = montant.replace("'", "");
        Float floatMontant = Float.parseFloat(montant);
        return floatMontant >= 0;
    }

    /**
     * Méthode de validation des périodes des types de soins
     * 
     * @param session
     * @param statement
     * @throws Exception
     */
    private void validate(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFParametrageGrandeQDViewBean rfPotPCVb = (RFParametrageGrandeQDViewBean) viewBean;

        // Vérifier que ce soit bien dans le format MM.AAAA
        if (rfPotPCVb.getForDateDebut().length() != 10) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_FORMAT_DATE_DEBUT");
        }

        // comparaison de date debut et fin (si date de fin non vide)
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getForDateDebut()) && !JadeStringUtil.isEmpty(rfPotPCVb.getForDateFin())) {
            if (JadeDateUtil.isDateAfter(rfPotPCVb.getForDateDebut().toString(), rfPotPCVb.getForDateFin().toString())) {
                RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_COMPARAISON_DATE");
            }
        }

        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPourTous())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPourTous())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondCoupleDomicile())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondCoupleDomicile())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondAdulteEnfants())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondAdulteEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondCoupleEnfants())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondCoupleEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondEnfantsEnfants())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondEnfantsEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondEnfantsSepares())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondEnfantsSepares())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPersonneSeule())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPersonneSeule())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondSepareDomicile())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondSepareDomicile())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPensionnairePourTous())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPensionnairePourTous())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPensionnaireEnfantsSepares())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPensionnaireEnfantsSepares())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPensionnairePersonneSeule())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPensionnairePersonneSeule())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPensionnaireSepareDomicile())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPensionnaireSepareDomicile())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPensionnaireSepareHome())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPensionnaireSepareHome())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPotPCVb.getMontantPlafondPensionnaireEnfantsEnfants())
                && !isNumericPositifOuNul(rfPotPCVb.getMontantPlafondPensionnaireEnfantsEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPotPCVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }

    }

    // on ne peut ajouter une période identique ou qui se chevauche
    // que si toute la table RFAssTypeDeSoinPotAssure est remplie
    protected Boolean validationAjout(RFParametrageGrandeQDViewBean viewBean, BISession session) throws Exception {
        RFPotsPCManager potPCManager = new RFPotsPCManager();
        potPCManager.setSession(viewBean.getSession());
        potPCManager.changeManagerSize(0);
        potPCManager.find();
        String dateFin = "";
        if (JadeStringUtil.isEmpty(viewBean.getForDateFin())) {
            dateFin = "01.12.9999";
        } else {
            dateFin = viewBean.getForDateFin();
        }

        // regarde si pas déja dans les résultats
        for (Iterator<RFPotsPC> it = potPCManager.iterator(); it.hasNext();) {
            RFPotsPC potPC = it.next();
            // si une des dates de fin est vide on compare avec 12.9999
            if (JadeStringUtil.isEmpty(potPC.getDateFin())) {
                potPC.setDateFin("01.12.9999");
            }

            if (!((JadeDateUtil.isDateAfter(viewBean.getForDateDebut(), potPC.getDateFin())) || (JadeDateUtil
                    .isDateAfter(potPC.getDateDebut(), dateFin)))) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_EXISTE_DEJA");
                return true;
            }
        }
        // il faut qu'au moins un des montants soit renseigné
        if (areMontantsVide(viewBean)) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_MONTANTS_VIDES");
            return true;
        }

        return false;
    }

    private void validationSuppression(RFParametrageGrandeQDViewBean viewBean) throws Exception {

        RFQdPrincipaleJointDossierManager rfQdPriJointDosMgr = new RFQdPrincipaleJointDossierManager();
        rfQdPriJointDosMgr.setSession(viewBean.getSession());
        rfQdPriJointDosMgr.setForIdPot(viewBean.getIdPotPC());
        rfQdPriJointDosMgr.changeManagerSize(0);
        rfQdPriJointDosMgr.find();

        if (rfQdPriJointDosMgr.size() > 0) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_QD");
        }

    }

    // ne pas comparer avec l'element en paramètre
    protected Boolean validationUpdate(RFParametrageGrandeQDViewBean viewBean, BISession session) throws Exception {
        RFPotsPCManager potPCManager = new RFPotsPCManager();
        potPCManager.setSession(viewBean.getSession());
        // assManager.setForIdSoinPot(viewBean.getIdSoinPot());
        potPCManager.changeManagerSize(0);
        potPCManager.find();
        String dateFin = "";
        if (JadeStringUtil.isEmpty(viewBean.getForDateFin())) {
            dateFin = "01.12.9999";
        } else {
            dateFin = viewBean.getForDateFin(); // regarde si pas déja dans les
        }
        // résultats
        for (Iterator<RFPotsPC> it = potPCManager.iterator(); it.hasNext();) {
            RFPotsPC potsPC = it.next();
            if (JadeStringUtil.isEmpty(potsPC.getDateFin())) {
                potsPC.setDateFin("01.12.9999");
            }

            if (!viewBean.getForIdPotPC().equals(potsPC.getIdPotPC())
                    && !((JadeDateUtil.isDateAfter(viewBean.getForDateDebut(), potsPC.getDateFin())) || (JadeDateUtil
                            .isDateAfter(potsPC.getDateDebut(), dateFin)))) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_EXISTE_DEJA");
                return true;
            }
            // il faut qu'au moins un des montants de chaque soit renseigné
            if (areMontantsVide(viewBean)) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_MONTANTS_VIDES");
                return true;
            }
        }
        return false;
    }

}
