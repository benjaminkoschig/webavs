// créé le 24 mars 2010
package globaz.cygnus.helpers.typeDeSoins;

import globaz.cygnus.db.qds.RFQdAssureJointPotJointDossierJointTiersManager;
import globaz.cygnus.db.typeDeSoins.RFAssTypeDeSoinPotAssure;
import globaz.cygnus.db.typeDeSoins.RFAssTypeDeSoinPotAssureManager;
import globaz.cygnus.db.typeDeSoins.RFPotAssure;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinJointAssPeriodeJointPotAssure;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.typeDeSoins.RFParametrageTypeSoinsRecherchePeriodeViewBean;
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
public class RFParametrageTypeSoinsRecherchePeriodeHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validate(viewBean, (BSession) session);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;

            RFParametrageTypeSoinsRecherchePeriodeViewBean periodeVB = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;

            if (!validationAjout(periodeVB, session)) {

                try {
                    transaction = ((BSession) session).newTransaction();
                    transaction.openTransaction();

                    // ajout des montants pour cette période et type de soin
                    RFPotAssure potas = new RFPotAssure();

                    potas.setSession(periodeVB.getSession());

                    // potas.setForcerPayement(periodeVB.getForcerPayement());
                    // potas.setImputerGrandeQd(periodeVB.getImputerGrandeQd());

                    potas.setMontantPlafondPourTous(periodeVB.getMontantPlafondPourTous());
                    potas.setMontantPlafondPensionnairePourTous(periodeVB.getMontantPlafondPensionnairePourTous());

                    potas.setMontantPlafondPersonneSeule(periodeVB.getMontantPlafondPersonneSeule());
                    potas.setMontantPlafondPensionnairePersonneSeule(periodeVB
                            .getMontantPlafondPensionnairePersonneSeule());

                    potas.setMontantPlafondPensionnaireSepareHome(periodeVB.getMontantPlafondPensionnaireSepareHome());

                    potas.setMontantPlafondSepareDomicile(periodeVB.getMontantPlafondSepareDomicile());
                    potas.setMontantPlafondPensionnaireSepareDomicile(periodeVB
                            .getMontantPlafondPensionnaireSepareDomicile());

                    potas.setMontantPlafondCoupleDomicile(periodeVB.getMontantPlafondCoupleDomicile());

                    potas.setMontantPlafondEnfantsSepares(periodeVB.getMontantPlafondEnfantsSepares());
                    potas.setMontantPlafondPensionnaireEnfantsSepares(periodeVB
                            .getMontantPlafondPensionnaireEnfantsSepares());

                    potas.setMontantPlafondAdulteEnfants(periodeVB.getMontantPlafondAdulteEnfants());
                    potas.setMontantPlafondCoupleEnfant(periodeVB.getMontantPlafondCoupleEnfant());

                    potas.setMontantPlafondEnfantsEnfants(periodeVB.getMontantPlafondEnfantsEnfants());
                    potas.setMontantPlafondPensionnaireEnfantsEnfants(periodeVB
                            .getMontantPlafondPensionnaireEnfantsEnfants());

                    potas.add(transaction);

                    // ajout d'une période pour un type de soin
                    RFAssTypeDeSoinPotAssure astpo = new RFAssTypeDeSoinPotAssure();
                    astpo.setSession(periodeVB.getSession());
                    astpo.setDateDebut(periodeVB.getDateDebut());
                    astpo.setDateFin(periodeVB.getDateFin());
                    astpo.setIdSousTypeSoin(periodeVB.getIdSousTypeSoin());
                    astpo.setIdPotAssure(potas.getIdPotAssure());

                    astpo.add(transaction);

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
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            RFParametrageTypeSoinsRecherchePeriodeViewBean vb = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;
            // on init l'idSousTypeDeSoin à partir des codes type et sous type de soin
            String idSousTypeDeSoin = RFUtils.getIdSousTypeDeSoin(vb.getCodeTypeDeSoinList(),
                    vb.getCodeSousTypeDeSoinList(), (BSession) session);

            vb.setIdSousTypeSoin(idSousTypeDeSoin);
            vb.setForIdSousTypeSoin(idSousTypeDeSoin);
        } catch (Exception e) {
            RFUtils.setMsgErreurViewBean(viewBean, e.getMessage());
        }
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BITransaction transaction = null;

        RFParametrageTypeSoinsRecherchePeriodeViewBean rfPeriodeVb = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;

        validationSuppression(rfPeriodeVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                // il faut aussi supprimer les sts et montants liés à cette période : appel du Manager + find
                RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager SoinsPots = new RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager();
                SoinsPots.setSession(rfPeriodeVb.getSession());
                SoinsPots.setForIdSoinPot(rfPeriodeVb.getIdSoinPot());
                SoinsPots.changeManagerSize(0);
                SoinsPots.find();
                if (SoinsPots.getSize() > 0) {
                    // on parcours les éléments retournés: normalement il n'y en a qu'un
                    for (Iterator<RFSousTypeDeSoinJointAssPeriodeJointPotAssure> it = SoinsPots.iterator(); it
                            .hasNext();) {
                        RFSousTypeDeSoinJointAssPeriodeJointPotAssure SPO = it.next();
                        RFPotAssure potas = new RFPotAssure();
                        potas.setSession(rfPeriodeVb.getSession());
                        potas.setIdPotAssure(SPO.getIdPotAssure());
                        potas.retrieve(transaction);

                        if (!potas.isNew()) {
                            potas.delete(transaction);
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(rfPeriodeVb, "_delete",
                                    "RFParametrageTypeSoinsRecherchePeriodeHelper");
                        }

                    }
                }

                // recherche des périodes
                if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                    RFAssTypeDeSoinPotAssure astpo = new RFAssTypeDeSoinPotAssure();
                    astpo.setSession(rfPeriodeVb.getSession());
                    astpo.setIdSoinPot(rfPeriodeVb.getIdSoinPot());
                    astpo.retrieve(transaction);

                    if (!astpo.isNew()) {
                        astpo.delete(transaction);
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(rfPeriodeVb, "_delete",
                                "RFParametrageTypeSoinsRecherchePeriodeHelper");
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
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            RFParametrageTypeSoinsRecherchePeriodeViewBean vb = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;

            if (RFSetEtatProcessService.getEtatProcessPreparerDecision(session)) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_PARAMETRAGE_PREPARER_DECISION_DEMARRE");
            }

            vb.setIsUpdate(Boolean.FALSE);

        } catch (Exception e) {
            RFUtils.setMsgErreurViewBean(viewBean, e.getMessage());
        }
    }

    /**
     * on charge la ligne qu'on a dans le viewBean
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            RFParametrageTypeSoinsRecherchePeriodeViewBean vb = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;
            vb.setIsUpdate(Boolean.TRUE);
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

            RFParametrageTypeSoinsRecherchePeriodeViewBean periodeVB = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;

            // recherche des période
            RFAssTypeDeSoinPotAssure astpo = new RFAssTypeDeSoinPotAssure();

            if (!validationUpdate(periodeVB, session)) {

                astpo.setSession(periodeVB.getSession());
                astpo.setIdSoinPot(periodeVB.getIdSoinPot());
                astpo.retrieve(transaction);
                if (!astpo.isNew()) {
                    astpo.setDateDebut(periodeVB.getDateDebut());
                    astpo.setDateFin(periodeVB.getDateFin());

                    astpo.update(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(periodeVB, "_update()",
                            "RFParametrageTypeSoinsRecherchePeriodeHelper");
                }

                RFPotAssure potas = new RFPotAssure();
                potas.setSession(periodeVB.getSession());
                potas.setIdPotAssure(astpo.getIdPotAssure());
                potas.retrieve(transaction);

                if (!potas.isNew()) {
                    // potas.setForcerPayement(periodeVB.getForcerPayement());
                    // potas.setImputerGrandeQd(periodeVB.getImputerGrandeQd());

                    potas.setMontantPlafondPourTous(periodeVB.getMontantPlafondPourTous());
                    potas.setMontantPlafondPensionnairePourTous(periodeVB.getMontantPlafondPensionnairePourTous());

                    potas.setMontantPlafondPersonneSeule(periodeVB.getMontantPlafondPersonneSeule());
                    potas.setMontantPlafondPensionnairePersonneSeule(periodeVB
                            .getMontantPlafondPensionnairePersonneSeule());

                    potas.setMontantPlafondPensionnaireSepareHome(periodeVB.getMontantPlafondPensionnaireSepareHome());

                    potas.setMontantPlafondSepareDomicile(periodeVB.getMontantPlafondSepareDomicile());
                    potas.setMontantPlafondPensionnaireSepareDomicile(periodeVB
                            .getMontantPlafondPensionnaireSepareDomicile());

                    potas.setMontantPlafondCoupleDomicile(periodeVB.getMontantPlafondCoupleDomicile());

                    potas.setMontantPlafondEnfantsSepares(periodeVB.getMontantPlafondEnfantsSepares());
                    potas.setMontantPlafondPensionnaireEnfantsSepares(periodeVB
                            .getMontantPlafondPensionnaireEnfantsSepares());

                    potas.setMontantPlafondAdulteEnfants(periodeVB.getMontantPlafondAdulteEnfants());
                    potas.setMontantPlafondCoupleEnfant(periodeVB.getMontantPlafondCoupleEnfant());

                    potas.setMontantPlafondEnfantsEnfants(periodeVB.getMontantPlafondEnfantsEnfants());
                    potas.setMontantPlafondPensionnaireEnfantsEnfants(periodeVB
                            .getMontantPlafondPensionnaireEnfantsEnfants());

                    potas.update(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(periodeVB, "_update()", "RFTypeDeSoinPiedDePageHelper");
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

    protected boolean areMontantsVide(RFParametrageTypeSoinsRecherchePeriodeViewBean viewBean) {
        return (!viewBean.getCodeTypeDeSoinList().equals(RFUtils.CODE_TYPE_DE_SOIN_FINANCEMENT_DES_SOINS_STR)
                && "0.00".equals(viewBean.getMontantPlafondAdulteEnfants())
                && "0.00".equals(viewBean.getMontantPlafondCoupleEnfant())
                && "0.00".equals(viewBean.getMontantPlafondCoupleDomicile())
                && "0.00".equals(viewBean.getMontantPlafondEnfantsEnfants())
                && "0.00".equals(viewBean.getMontantPlafondEnfantsSepares())
                && "0.00".equals(viewBean.getMontantPlafondPersonneSeule())
                && "0.00".equals(viewBean.getMontantPlafondSepareDomicile()) && "0.00".equals(viewBean
                .getMontantPlafondPourTous()))

                || (!viewBean.getCodeTypeDeSoinList().equals(RFUtils.CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE_STR)
                        && !viewBean.getCodeTypeDeSoinList().equals(RFUtils.CODE_TYPE_DE_SOIN_REGIME_2_STR) && ("0.00"
                        .equals(viewBean.getMontantPlafondPensionnaireEnfantsEnfants())
                        && "0.00".equals(viewBean.getMontantPlafondPensionnaireEnfantsSepares())
                        && "0.00".equals(viewBean.getMontantPlafondPensionnairePersonneSeule())
                        && "0.00".equals(viewBean.getMontantPlafondPensionnaireSepareDomicile())
                        && "0.00".equals(viewBean.getMontantPlafondPensionnaireSepareHome()) && "0.00".equals(viewBean
                        .getMontantPlafondPensionnairePourTous())));
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
     * Test si le String en paramètre représente un montant positif ou nul renvoi vrai si positif ou nul faux si négatif
     * 
     * @param montant
     */
    private Boolean isNumericPositifOuNul(String montant) {
        // enleve le formatage
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

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(session)) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_PARAMETRAGE_PREPARER_DECISION_DEMARRE");
        }

        RFParametrageTypeSoinsRecherchePeriodeViewBean rfPeriodeVb = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean;

        // Vérifier que ce soit bien dans le format MM.AAAA
        if (rfPeriodeVb.getDateDebut().length() != 10) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_FORMAT_DATE_DEBUT");
        }

        // comparaison de date debut et fin (si date de fin non vide)
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getDateDebut()) && !JadeStringUtil.isEmpty(rfPeriodeVb.getDateFin())) {
            if (JadeDateUtil.isDateAfter(rfPeriodeVb.getDateDebut().toString(), rfPeriodeVb.getDateFin().toString())) {
                RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_COMPARAISON_DATE");
            }
        }

        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPourTous())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPourTous())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondAdulteEnfants())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondAdulteEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondCoupleEnfant())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondCoupleEnfant())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondCoupleDomicile())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondCoupleDomicile())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondEnfantsSepares())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondEnfantsSepares())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondEnfantsEnfants())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondEnfantsEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPersonneSeule())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPersonneSeule())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondSepareDomicile())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondSepareDomicile())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPensionnaireSepareHome())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPensionnaireSepareHome())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPensionnairePourTous())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPensionnairePourTous())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPensionnaireEnfantsSepares())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPensionnaireEnfantsSepares())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPensionnaireEnfantsEnfants())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPensionnaireEnfantsEnfants())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPensionnairePersonneSeule())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPensionnairePersonneSeule())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }
        if (!JadeStringUtil.isEmpty(rfPeriodeVb.getMontantPlafondPensionnaireSepareDomicile())
                && !isNumericPositifOuNul(rfPeriodeVb.getMontantPlafondPensionnaireSepareDomicile())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_MNT_NEGATIF");
        }

    }

    /**
     * on ne peut ajouter une période identique ou qui se chevauche que si toute la table RFAssTypeDeSoinPotAssure est
     * remplie
     * 
     * @param viewBean
     * @param session
     * @return
     * @throws Exception
     */
    private Boolean validationAjout(RFParametrageTypeSoinsRecherchePeriodeViewBean viewBean, BISession session)
            throws Exception {
        RFAssTypeDeSoinPotAssureManager assManager = new RFAssTypeDeSoinPotAssureManager();
        assManager.setSession(viewBean.getSession());
        assManager.changeManagerSize(0);
        assManager.find();
        String dateFin = "";
        if (JadeStringUtil.isEmpty(viewBean.getDateFin())) {
            dateFin = "01.12.9999";
        } else {
            dateFin = viewBean.getDateFin();
        }

        // regarde si pas déja dans les résultats
        for (Iterator<RFAssTypeDeSoinPotAssure> it = assManager.iterator(); it.hasNext();) {
            RFAssTypeDeSoinPotAssure astpo = it.next();
            // si une des dates de fin est vide on compare avec 12.9999
            if (JadeStringUtil.isEmpty(astpo.getDateFin())) {
                astpo.setDateFin("01.12.9999");
            }

            if (astpo.getIdSousTypeSoin().equals(viewBean.getIdSousTypeSoin())
                    && !((JadeDateUtil.isDateAfter(viewBean.getDateDebut(), astpo.getDateFin())) || (JadeDateUtil
                            .isDateAfter(astpo.getDateDebut(), dateFin)))) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_EXISTE_DEJA");
                return true;
            }
            // il faut qu'au moins un des montants soit renseigné
            if (areMontantsVide(viewBean)) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_MONTANTS_VIDES");
                return true;
            }
        }

        return false;
    }

    private void validationSuppression(RFParametrageTypeSoinsRecherchePeriodeViewBean rfPeriodeVb) throws Exception {

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(rfPeriodeVb.getSession())) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_PARAMETRAGE_PREPARER_DECISION_DEMARRE");
        }

        RFQdAssureJointPotJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = new RFQdAssureJointPotJointDossierJointTiersManager();

        rfQdAssJointDosJointTieMgr.setSession(rfPeriodeVb.getSession());
        rfQdAssJointDosJointTieMgr.setForIdPotAssure(rfPeriodeVb.getIdPotAssure());
        rfQdAssJointDosJointTieMgr.changeManagerSize(0);
        rfQdAssJointDosJointTieMgr.find();

        if (rfQdAssJointDosJointTieMgr.size() > 0) {
            RFUtils.setMsgErreurViewBean(rfPeriodeVb, "ERREUR_RF_SAISIE_PERIODE_QD");
        }

    }

    // ne pas comparer avec l'element en paramètre
    private Boolean validationUpdate(RFParametrageTypeSoinsRecherchePeriodeViewBean viewBean, BISession session)
            throws Exception {
        RFAssTypeDeSoinPotAssureManager assManager = new RFAssTypeDeSoinPotAssureManager();
        assManager.setSession(viewBean.getSession());
        assManager.setForIdSousTypeSoin(viewBean.getIdSousTypeSoin());
        assManager.changeManagerSize(0);
        assManager.find();
        String dateFin = "";
        if (JadeStringUtil.isEmpty(viewBean.getDateFin())) {
            dateFin = "01.12.9999";
        } else {
            dateFin = viewBean.getDateFin(); // regarde si pas déja dans les
        }
        // résultats
        for (Iterator<RFAssTypeDeSoinPotAssure> it = assManager.iterator(); it.hasNext();) {
            RFAssTypeDeSoinPotAssure astpo = it.next();
            if (JadeStringUtil.isEmpty(astpo.getDateFin())) {
                astpo.setDateFin("01.12.9999");
            }

            if (!viewBean.getIdSoinPot().equals(astpo.getIdSoinPot())
                    && !((JadeDateUtil.isDateAfter(viewBean.getDateDebut(), astpo.getDateFin())) || (JadeDateUtil
                            .isDateAfter(astpo.getDateDebut(), dateFin)))) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_EXISTE_DEJA");
                return true;
            }
            // il faut qu'au moins un des montants soit renseigné
            if (areMontantsVide(viewBean)) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_PERIODE_MONTANTS_VIDES");
                return true;
            }
        }
        return false;
    }

}
