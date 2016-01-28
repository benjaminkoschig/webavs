/*
 * Créé le 18 décembre 2010
 */
package globaz.cygnus.services.preparerDecision;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.decisions.RFAssDossierDecisionManager;
import globaz.cygnus.db.decisions.RFCopieDecisionManager;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefusManager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.ordresversements.RFOrdresVersementsManager;
import globaz.cygnus.db.paiement.RFAssDecOv;
import globaz.cygnus.db.paiement.RFAssDecOvManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeManager;
import globaz.cygnus.db.paiement.RFPrestationManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdAssureManager;
import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author jje
 * 
 *         Annule le calcul du processus préparer décision.
 * 
 * 
 *         Boucle sur les décision:
 * 
 *         Màj demande: - Màj des demandes - Ajout idQdAssure dans idsQdAssureSet - Ajout idQdPrincipale demande parent
 *         dans idsQdPrincipaleSet
 * 
 *         Supprimer décision: - Suppresions des décisions - Màj des Qds de idsQdPrincipaleSet -> Solde Exc,
 *         Augmentation de Qd, Montant initial - Suppression des RFM accordées
 * 
 *         Fin Boucle décision
 * 
 *         Màj Qd assuré: - Ajout des Qds assuré issues d'une correction dans idQdAssureSet - màj des Qds de
 *         idQdAssureSet -> Mnt init, augmentation de Qd
 * 
 *         Màj Qd principale: - Ajout des Qds principale issues d'une correction - Màj des Qds de idsQdPrincipaleSet
 *         issu d'une correction -> Mnt init, Augmentation
 * 
 * 
 *         Màj des RFmAccordes issues d'une correction: - màj date de fin initiale
 */
public class RFAnnulerPreparationDecisionService {

    private static Set<String> idsQdAssureSet = null;
    private static Set<String> idsQdDecisionSet = null;
    private static Set<String> idsQdPrincipaleSet = null;
    private static Set<String> idsRfAccordeeSet = null;

    /**
     * 
     * Recherche toutes les demandes des décisions pré-validé qui concerne un gestionnaire. Ces demandes sont ensuite
     * remises dans l'état calculé puis désimputer de leur grande et petite Qd. Ensuite les motifs de refus systèmes de
     * chaque demandes sont supprimés puis les décisions concernées sont supprimées ainsi que les associations ovs
     * restitution - décisions.
     * 
     * @param idDemande
     * @return
     * @throws Exception
     */
    public static void annulerPreparationDecision(String idGestionnaire, String forIdExecutionProcessAvasadDecision,
            BSession session, BTransaction transaction) throws Exception {

        // Initialisation de la liste des Qds à mettre à jour
        RFAnnulerPreparationDecisionService.idsQdPrincipaleSet = new HashSet<String>();
        RFAnnulerPreparationDecisionService.idsQdDecisionSet = new HashSet<String>();
        RFAnnulerPreparationDecisionService.idsQdAssureSet = new HashSet<String>();
        RFAnnulerPreparationDecisionService.idsRfAccordeeSet = new HashSet<String>();

        RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefusManager rfDecJointDemJointAssJointMotMgr = new RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefusManager();
        rfDecJointDemJointAssJointMotMgr.setSession(session);
        rfDecJointDemJointAssJointMotMgr.setForCsEtatDecision(IRFDecisions.NON_VALIDE);

        if (JadeStringUtil.isBlankOrZero(forIdExecutionProcessAvasadDecision)) {
            rfDecJointDemJointAssJointMotMgr.setForIsNotDecisionAvasad(true);
        } else {
            rfDecJointDemJointAssJointMotMgr.setForIsNotDecisionAvasad(false);
            rfDecJointDemJointAssJointMotMgr
                    .setForIdExecutionProcessAvasadDecision(forIdExecutionProcessAvasadDecision);
        }

        if (RFPropertiesUtils.utiliserGestionnaireViewBeanAnnulationDemandes()) {
            rfDecJointDemJointAssJointMotMgr.setForIdGestionnaire(idGestionnaire);
        }
        rfDecJointDemJointAssJointMotMgr.changeManagerSize(0);
        rfDecJointDemJointAssJointMotMgr.find();

        Iterator<RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus> rfDecJointDemJointAssJointMotItr = rfDecJointDemJointAssJointMotMgr
                .iterator();

        String idDecision = "";
        String idDemande = "";
        boolean isPremiereIteration = true;
        BigDecimal montantAPayer = null;

        while (rfDecJointDemJointAssJointMotItr.hasNext()) {

            RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus rfDecJointDemJointAssJointMot = rfDecJointDemJointAssJointMotItr
                    .next();

            if (null != rfDecJointDemJointAssJointMot) {

                if (isPremiereIteration) {
                    montantAPayer = RFAnnulerPreparationDecisionService
                            .setMontantFacture(rfDecJointDemJointAssJointMot);
                    idDecision = rfDecJointDemJointAssJointMot.getIdDecision();
                    idDemande = rfDecJointDemJointAssJointMot.getIdDemande();
                    isPremiereIteration = false;
                }

                if (idDecision.equals(rfDecJointDemJointAssJointMot.getIdDecision())) {

                    if (!idDemande.equals(rfDecJointDemJointAssJointMot.getIdDemande())) {

                        RFAnnulerPreparationDecisionService.majDemande(session, transaction, idDemande, montantAPayer);
                        montantAPayer = RFAnnulerPreparationDecisionService
                                .setMontantFacture(rfDecJointDemJointAssJointMot);
                        idDemande = rfDecJointDemJointAssJointMot.getIdDemande();

                    }

                } else {
                    RFAnnulerPreparationDecisionService.majDemande(session, transaction, idDemande, montantAPayer);
                    RFAnnulerPreparationDecisionService.supprimerDecision(session, transaction, idDecision);

                    montantAPayer = RFAnnulerPreparationDecisionService
                            .setMontantFacture(rfDecJointDemJointAssJointMot);
                    idDemande = rfDecJointDemJointAssJointMot.getIdDemande();
                    idDecision = rfDecJointDemJointAssJointMot.getIdDecision();

                }

                montantAPayer = montantAPayer.add(RFAnnulerPreparationDecisionService.supprimerAssMotifRefusDemande(
                        session, transaction, rfDecJointDemJointAssJointMot, montantAPayer).negate());

                if ((montantAPayer.compareTo(new BigDecimal("0")) == 0)
                        || (montantAPayer.compareTo(new BigDecimal("0")) == -1)) {
                    montantAPayer = new BigDecimal("0");
                }

                if (!rfDecJointDemJointAssJointMotItr.hasNext()) {
                    RFAnnulerPreparationDecisionService.majDemande(session, transaction,
                            rfDecJointDemJointAssJointMot.getIdDemande(), montantAPayer);
                    RFAnnulerPreparationDecisionService.supprimerDecision(session, transaction,
                            rfDecJointDemJointAssJointMot.getIdDecision());
                }

            } else {
                throw new Exception("RFAnnulerPreparationDecisionService.annulerPreparationDecision: "
                        + "Impossible de retrouver l'association decision, demande, motifs de refus");
            }
        }

        RFAnnulerPreparationDecisionService.majQdAssure(session, idGestionnaire, transaction);
        RFAnnulerPreparationDecisionService.majQdPrincipaleRestitution(session, idGestionnaire, transaction);
        RFAnnulerPreparationDecisionService.majRfmPrestationsAccordees(session, idGestionnaire, transaction);
    }

    private static void majDemande(BSession session, BTransaction transaction, String idDemande,
            BigDecimal montantAPayer) throws Exception {

        RFDemande rfDemande = new RFDemande();
        rfDemande.setSession(session);
        rfDemande.setIdDemande(idDemande);

        rfDemande.retrieve();

        if (!rfDemande.isNew()) {
            if (!JadeStringUtil.isBlankOrZero(rfDemande.getIdQdAssure())) {
                RFAnnulerPreparationDecisionService.idsQdAssureSet.add(rfDemande.getIdQdAssure());
            }

            rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
            rfDemande.setIdQdAssure("");

            if (!rfDemande.getIsForcerImputationSurQd()) {
                rfDemande.setIdQdPrincipale("");
            }

            rfDemande.setIdDecision("");
            rfDemande.setMontantAPayer(montantAPayer.toString());

            rfDemande.update(transaction);

            // Annulation de la réallocation sur la Qd de la demande parent
            if (!JadeStringUtil.isBlankOrZero(rfDemande.getIdDemandeParent())) {

                RFDemande rfDemandeParent = new RFDemande();
                rfDemandeParent.setSession(session);
                rfDemandeParent.setIdDemande(rfDemande.getIdDemandeParent());

                rfDemandeParent.retrieve();

                if (!rfDemandeParent.isNew()) {

                    if (!JadeStringUtil.isBlankOrZero(rfDemandeParent.getIdQdPrincipale())) {

                        RFAnnulerPreparationDecisionService.idsQdPrincipaleSet.add(rfDemandeParent.getIdQdPrincipale());
                    }

                } else {
                    throw new Exception(
                            "RFAnnulerPreparationDecisionService.majDemande(): Impossible de retrouver la demande parent");
                }
            }

        } else {
            throw new Exception(
                    "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver la décision");
        }

    }

    private static void majQdAssure(BSession session, String idGestionnaire, BTransaction transaction) throws Exception {

        // Ajout des grandes Qds à annuler correspondant à une restitution d'un régime sur plusieurs années
        RFQdAssureManager rfQdAssMgr = new RFQdAssureManager();
        rfQdAssMgr.setSession(session);

        if (RFPropertiesUtils.utiliserGestionnaireViewBeanAnnulationDemandes()) {
            rfQdAssMgr.setForIdGesModSolExcAugQdPreDec(idGestionnaire);
        } else {
            rfQdAssMgr.setForIdGesModSolExcAugQdPreDecNotNull(Boolean.TRUE);
        }

        rfQdAssMgr.changeManagerSize(0);
        rfQdAssMgr.find();

        Iterator<RFQdAssure> rfQdAssItr = rfQdAssMgr.iterator();
        while (rfQdAssItr.hasNext()) {
            RFQdAssure qdCourante = rfQdAssItr.next();
            if (qdCourante != null) {
                RFAnnulerPreparationDecisionService.idsQdAssureSet.add(qdCourante.getIdQdAssure());
            } else {
                throw new Exception("RFAnnulerPreparationDecisionService.majQdPrincipaleRestitution: QD introuvable");
            }
        }

        for (String idQdAssure : RFAnnulerPreparationDecisionService.idsQdAssureSet) {
            if (!JadeStringUtil.isBlankOrZero(idQdAssure)) {
                RFQd rfQdAss = new RFQd();
                rfQdAss.setSession(session);
                rfQdAss.setIdQd(idQdAssure);
                rfQdAss.retrieve();

                if (!rfQdAss.isNew()) {
                    rfQdAss.setMontantChargeRfm(rfQdAss.getMontantInitialChargeRfm().equals(
                            RFUtils.montantInitialChargeRfmZero) ? "0" : rfQdAss.getMontantInitialChargeRfm());
                    rfQdAss.setMontantInitialChargeRfm("");

                    rfQdAss.update(transaction);

                    // Suppression de l'augmentation de Qd
                    RFQdAssure rfQdAssure = new RFQdAssure();
                    rfQdAssure.setSession(session);
                    rfQdAssure.setIdQdAssure(idQdAssure);
                    rfQdAssure.retrieve();

                    if (!rfQdAssure.isNew()) {

                        // Màj de l'augmentation de Qd
                        if (!JadeStringUtil.isBlankOrZero(rfQdAssure.getIdFamilleAugmentation())) {
                            RFQdAugmentationManager rfQdAugMgr = new RFQdAugmentationManager();
                            rfQdAugMgr.setSession(session);
                            rfQdAugMgr.changeManagerSize(0);
                            rfQdAugMgr.setForIdFamilleModif(rfQdAssure.getIdFamilleAugmentation());

                            rfQdAugMgr.delete(transaction);
                        }

                        rfQdAssure.setIdFamilleAugmentation("");
                        rfQdAssure.setIdGesModSoldeExcedentAugmentationQdPreDec("");

                        rfQdAssure.update(transaction);

                    } else {
                        throw new Exception("RFAnnulerPreparationDecisionService.majQdAssure petite QD introuvable");
                    }
                } else {
                    throw new Exception("RFAnnulerPreparationDecisionService.majQdAssure petite QD introuvable");
                }
            }
        }

    }

    private static void majQdPrincipaleRestitution(BSession session, String idGestionnaire, BTransaction transaction)
            throws Exception {

        // Ajout des grandes Qds à annuler correspondant à une restitution d'un régime sur plusieurs années
        RFQdPrincipaleManager rfQdPriMgr = new RFQdPrincipaleManager();
        rfQdPriMgr.setSession(session);

        if (RFPropertiesUtils.utiliserGestionnaireViewBeanAnnulationDemandes()) {
            rfQdPriMgr.setForIdGesModSolExcAugQdPreDec(idGestionnaire);
        } else {
            rfQdPriMgr.setForIdGesModSolExcAugQdPreDecNotNull(Boolean.TRUE);
        }

        rfQdPriMgr.changeManagerSize(0);
        rfQdPriMgr.find();

        Iterator<RFQdPrincipale> rfQdPriItr = rfQdPriMgr.iterator();
        while (rfQdPriItr.hasNext()) {
            RFQdPrincipale qdCourante = rfQdPriItr.next();
            if (qdCourante != null) {
                RFAnnulerPreparationDecisionService.idsQdPrincipaleSet.add(qdCourante.getIdQdPrincipale());
            } else {
                throw new Exception("RFAnnulerPreparationDecisionService.majQdPrincipaleRestitution: QD introuvable");
            }
        }

        for (String idQdPrincipale : RFAnnulerPreparationDecisionService.idsQdPrincipaleSet) {

            if (!RFAnnulerPreparationDecisionService.idsQdDecisionSet.contains(idQdPrincipale)) {

                // L'annulation de l'augmentation de la qd de la décision est déjà traitée lors de la suppression de la
                // décision
                if (!RFAnnulerPreparationDecisionService.idsQdDecisionSet.contains(idQdPrincipale)) {

                    RFQdPrincipale rfQdPrincipale = new RFQdPrincipale();
                    rfQdPrincipale.setSession(session);
                    rfQdPrincipale.setIdQdPrincipale(idQdPrincipale);

                    rfQdPrincipale.retrieve();

                    if (!rfQdPrincipale.isNew()) {

                        // Màj de l'augmentation de Qd
                        if (!JadeStringUtil.isBlankOrZero(rfQdPrincipale.getIdFamModAugmentationDeQd())) {
                            RFQdAugmentationManager rfQdAugMgr = new RFQdAugmentationManager();
                            rfQdAugMgr.setSession(session);
                            rfQdAugMgr.setForIdFamilleModif(rfQdPrincipale.getIdFamModAugmentationDeQd());
                            rfQdAugMgr.changeManagerSize(0);
                            rfQdAugMgr.delete(transaction);
                        }

                        rfQdPrincipale.setIdFamModAugmentationDeQd("");
                        rfQdPrincipale.setIdGesModSoldeExcedentAugmentationQdPreDec("");

                        rfQdPrincipale.update(transaction);

                        RFQd rfQd = new RFQd();
                        rfQd.setSession(session);
                        rfQd.setIdQd(idQdPrincipale);
                        rfQd.retrieve();

                        if (!rfQd.isNew()) {
                            rfQd.setMontantChargeRfm(rfQd.getMontantInitialChargeRfm().equals(
                                    RFUtils.montantInitialChargeRfmZero) ? "0" : rfQd.getMontantInitialChargeRfm());
                            rfQd.setMontantInitialChargeRfm("");
                            rfQd.update(transaction);
                        } else {
                            throw new Exception(
                                    "RFAnnulerPreparationDecisionService.majQdPrincipaleRestitution: Impossible de retrouver la qd principale de la décision");
                        }

                    } else {
                        throw new Exception(
                                "RFAnnulerPreparationDecisionService.majQdPrincipaleRestitution: Impossible de retrouver la qd de la demande parent");
                    }
                }
            }
        }
    }

    private static void majRfmPrestationsAccordees(BSession session, String idGestionnaire, BTransaction transaction)
            throws Exception {

        RFPrestationAccordeeManager rfPreAccMgr = new RFPrestationAccordeeManager();
        rfPreAccMgr.setSession(session);

        if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
            rfPreAccMgr.setForIdGestionnairePreparerDecision(idGestionnaire);
        } else {
            rfPreAccMgr.setForIdGestionnairePreparerDecisionNotNull(Boolean.TRUE);
        }

        rfPreAccMgr.changeManagerSize(0);
        rfPreAccMgr.find();

        Iterator<RFPrestationAccordee> rfPreAccItr = rfPreAccMgr.iterator();

        while (rfPreAccItr.hasNext()) {

            RFPrestationAccordee rfPreAcc = rfPreAccItr.next();

            if (null != rfPreAcc) {
                if (!RFAnnulerPreparationDecisionService.idsRfAccordeeSet.contains(rfPreAcc.getIdRFMAccordee())) {

                    REPrestationsAccordees renteAccordee = new REPrestationsAccordees();
                    renteAccordee.setSession(session);
                    renteAccordee.setIdPrestationAccordee(rfPreAcc.getIdRFMAccordee());
                    renteAccordee.retrieve();

                    if (!renteAccordee.isNew()) {

                        RFPrestationAccordee rfPrestationAccordee = new RFPrestationAccordee();
                        rfPrestationAccordee.setSession(session);
                        rfPrestationAccordee.setIdRFMAccordee(renteAccordee.getIdPrestationAccordee());

                        rfPrestationAccordee.retrieve();

                        if (!rfPrestationAccordee.isNew()) {

                            rfPrestationAccordee.setIdGestionnairePreparerDecision("");
                            renteAccordee
                                    .setDateFinDroit(rfPrestationAccordee.getDateFinDroitInitiale().length() == 10 ? PRDateFormater
                                            .convertDate_JJxMMxAAAA_to_MMxAAAA(rfPrestationAccordee
                                                    .getDateFinDroitInitiale()) : rfPrestationAccordee
                                            .getDateFinDroitInitiale());
                            rfPrestationAccordee.setDateFinDroitInitiale("");
                            rfPrestationAccordee.setDateDiminution(rfPrestationAccordee.getDateDiminutionInitiale());
                            rfPrestationAccordee.setDateDiminutionInitiale("");

                            renteAccordee.update(transaction);
                            rfPrestationAccordee.update(transaction);

                        } else {
                            throw new Exception(
                                    "RFAnnulerPreparationDecisionService.majRfmPrestationsAccordees: Rente RFM accordée non trouvée");
                        }

                    } else {
                        throw new Exception(
                                "RFAnnulerPreparationDecisionService.majRfmPrestationsAccordees: Rente accordée non trouvée");
                    }

                }

            } else {
                throw new Exception("RFAnnulerPreparationDecisionService.majRfmPrestationsAccordees: "
                        + "Impossible de retrouver la prestation accordee");
            }

        }
    }

    private static BigDecimal setMontantFacture(
            RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus rfDecJointDemJointAssJointMot) {

        BigDecimal montantFacture = new BigDecimal(0);

        if (IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES.equals(rfDecJointDemJointAssJointMot.getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES.equals(rfDecJointDemJointAssJointMot
                        .getCodeTypeDeSoin())
                || IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI
                        .equals(rfDecJointDemJointAssJointMot.getCodeTypeDeSoin())) {

            montantFacture = montantFacture.add(new BigDecimal(rfDecJointDemJointAssJointMot.getMontantFacture44()));

            if (IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES.equals(rfDecJointDemJointAssJointMot.getCodeTypeDeSoin())) {
                montantFacture = montantFacture.add(new BigDecimal(rfDecJointDemJointAssJointMot.getMontantVerseOAI())
                        .negate());
            }
        } else {
            montantFacture = montantFacture.add(new BigDecimal(rfDecJointDemJointAssJointMot.getMontantFacture()));
        }

        return montantFacture;

    }

    private static BigDecimal supprimerAssMotifRefusDemande(BSession session, BTransaction transaction,
            RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus rfDecJointDemJointAssJointMot,
            BigDecimal montantInitialAPayer) throws Exception {

        BigDecimal montantMotifDeRefus = new BigDecimal("0");

        if (!JadeStringUtil.isBlankOrZero(rfDecJointDemJointAssJointMot.getIdAssMotifDeRefus())) {

            if (rfDecJointDemJointAssJointMot.getIsMotifDeRefusSysteme()) {

                RFAssMotifsRefusDemande rfAssMotRefDem = new RFAssMotifsRefusDemande();
                rfAssMotRefDem.setSession(session);
                rfAssMotRefDem.setIdAssMotifsRefus(rfDecJointDemJointAssJointMot.getIdAssMotifDeRefus());

                rfAssMotRefDem.retrieve();

                if (!rfAssMotRefDem.isNew()) {
                    rfAssMotRefDem.delete(transaction);
                } else {
                    throw new Exception("RFAnnulerPreparationDecisionService.annulerPreparationDecision: "
                            + "Impossible de retrouver l'association demande motif de refus");
                }
            } else {
                if (rfDecJointDemJointAssJointMot.getHasMontant()) {
                    montantMotifDeRefus = new BigDecimal(rfDecJointDemJointAssJointMot.getMntMotifsDeRefus());
                } else {
                    montantMotifDeRefus = montantInitialAPayer;
                }
            }

        }

        return montantMotifDeRefus;

    }

    private static void supprimerDecision(BSession session, BTransaction transaction, String idDecision)
            throws Exception {

        RFDecision rfDec = new RFDecision();
        rfDec.setSession(session);
        rfDec.setIdDecision(idDecision);

        rfDec.retrieve();

        if (!rfDec.isNew()) {
            if (!JadeStringUtil.isBlankOrZero(rfDec.getIdQdPrincipale())) {

                RFQdPrincipale rfQdPrincipale = new RFQdPrincipale();
                rfQdPrincipale.setSession(session);

                rfQdPrincipale.setIdQdPrincipale(rfDec.getIdQdPrincipale());

                rfQdPrincipale.retrieve();

                if (!rfQdPrincipale.isNew()) {

                    // On ne veut pas traiter la réstitution de la qd de la décision courante 2 fois
                    RFAnnulerPreparationDecisionService.idsQdDecisionSet.add(rfDec.getIdQdPrincipale());

                    // Màj du solde excedent de revenu
                    if (!JadeStringUtil.isBlankOrZero(rfQdPrincipale.getIdFamModSoldeExcedentPreDec())) {
                        RFQdSoldeExcedentDeRevenuManager rfQdSolExcDeRevMgr = new RFQdSoldeExcedentDeRevenuManager();
                        rfQdSolExcDeRevMgr.setSession(session);
                        rfQdSolExcDeRevMgr.setForIdFamilleModif(rfQdPrincipale.getIdFamModSoldeExcedentPreDec());
                        rfQdSolExcDeRevMgr.changeManagerSize(0);
                        rfQdSolExcDeRevMgr.delete(transaction);
                    }

                    // Màj de l'augmentation de Qd
                    if (!JadeStringUtil.isBlankOrZero(rfQdPrincipale.getIdFamModAugmentationDeQd())) {
                        RFQdAugmentationManager rfQdAugMgr = new RFQdAugmentationManager();
                        rfQdAugMgr.setSession(session);
                        rfQdAugMgr.changeManagerSize(0);
                        rfQdAugMgr.setForIdFamilleModif(rfQdPrincipale.getIdFamModAugmentationDeQd());

                        rfQdAugMgr.delete(transaction);
                    }

                    rfQdPrincipale.setIdFamModSoldeExcedentPreDec("");
                    rfQdPrincipale.setIdFamModAugmentationDeQd("");
                    rfQdPrincipale.setIdGesModSoldeExcedentAugmentationQdPreDec("");

                    rfQdPrincipale.update(transaction);

                    RFQd rfQd = new RFQd();
                    rfQd.setSession(session);
                    rfQd.setIdQd(rfDec.getIdQdPrincipale());
                    rfQd.retrieve();

                    if (!rfQd.isNew()) {
                        // Soustraction dans les champs 'charge' et 'chargeInitiale' du montant de la décision en cours
                        // d'annulation.
                        BigDecimal montantDecision = new BigDecimal(rfDec.getMontantTotalRFM());
                        BigDecimal soldeChargeQd = new BigDecimal(rfQd.getMontantChargeRfm());

                        rfQd.setMontantChargeRfm(soldeChargeQd.subtract(montantDecision).toString());
                        rfQd.setMontantInitialChargeRfm("");

                        rfQd.update(transaction);

                    } else {
                        throw new Exception(
                                "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver la qd principale de la décision");
                    }

                } else {
                    throw new Exception(
                            "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver la qd principale de la décision");
                }
            }

            rfDec.delete(transaction);

            RFAssDossierDecisionManager rfAssDossierDecisionMgr = new RFAssDossierDecisionManager();
            rfAssDossierDecisionMgr.setSession(session);
            rfAssDossierDecisionMgr.setForIdDecision(rfDec.getIdDecision());
            rfAssDossierDecisionMgr.changeManagerSize(0);
            rfAssDossierDecisionMgr.delete(transaction);

            // Suppression des copies liées à la décision
            RFCopieDecisionManager rfCopDecMgr = new RFCopieDecisionManager();
            rfCopDecMgr.setSession(session);
            rfCopDecMgr.changeManagerSize(0);
            rfCopDecMgr.setForIdDecision(rfDec.getIdDecision());

            rfCopDecMgr.delete(transaction);

            // Puis on supprime la prestation et les ordres de versement associés à la décision
            RFPrestationManager rfPrestMgr = new RFPrestationManager();
            rfPrestMgr.setSession(session);
            rfPrestMgr.setForIdDecision(rfDec.getIdDecision());
            rfPrestMgr.changeManagerSize(0);
            rfPrestMgr.find();

            Iterator<RFPrestation> rfPrestItr = rfPrestMgr.iterator();

            while (rfPrestItr.hasNext()) {
                RFPrestation rfPrestCourante = rfPrestItr.next();
                if (null != rfPrestCourante) {
                    RFOrdresVersementsManager rfOrdVerMgr = new RFOrdresVersementsManager();
                    rfOrdVerMgr.setSession(session);
                    rfOrdVerMgr.setForIdPrestation(rfPrestMgr.getFirstEntity().getId());
                    rfOrdVerMgr.changeManagerSize(0);
                    rfOrdVerMgr.delete(transaction);

                    rfPrestMgr.delete(transaction);
                } else {
                    throw new Exception(
                            "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver la prestation");
                }
            }

            // Suppression des RFMAccordées
            RFPrestationAccordeeManager rfPrestAccRfmMgr = new RFPrestationAccordeeManager();
            rfPrestAccRfmMgr.setSession(session);
            rfPrestAccRfmMgr.setForIdDecision(rfDec.getIdDecision());
            rfPrestAccRfmMgr.changeManagerSize(0);
            rfPrestAccRfmMgr.find();
            Iterator<RFPrestationAccordee> rfPrestAccRfmItr = rfPrestAccRfmMgr.iterator();

            while (rfPrestAccRfmItr.hasNext()) {

                RFPrestationAccordee rfPrestAcc = rfPrestAccRfmItr.next();

                if (rfPrestAcc != null) {

                    REPrestationsAccordees rePrestAcc = new REPrestationsAccordees();
                    rePrestAcc.setSession(session);
                    rePrestAcc.setIdPrestationAccordee(rfPrestAcc.getIdRFMAccordee());

                    rePrestAcc.retrieve();

                    if (!rePrestAcc.isNew()) {
                        RFAnnulerPreparationDecisionService.idsRfAccordeeSet.add(rfPrestAcc.getIdRFMAccordee());
                        rePrestAcc.delete(transaction);
                    } else {
                        throw new Exception(
                                "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver la prestation (REPRACC)");
                    }
                }
            }

            // Suppression des associations décisions - Ovs restitution
            RFAssDecOvManager rfAssDecOvMgr = new RFAssDecOvManager();
            rfAssDecOvMgr.setSession(session);
            rfAssDecOvMgr.setForIdDecision(rfDec.getIdDecision());
            rfAssDecOvMgr.changeManagerSize(0);
            rfAssDecOvMgr.find();

            Iterator<RFAssDecOv> rfAssDecOvItr = rfAssDecOvMgr.iterator();

            while (rfAssDecOvItr.hasNext()) {

                RFAssDecOv rfAssDecOvM = rfAssDecOvItr.next();

                if (rfAssDecOvM != null) {

                    RFAssDecOv rfAssDecOv = new RFAssDecOv();
                    rfAssDecOv.setSession(session);
                    rfAssDecOv.setIdDecOv(rfAssDecOvM.getIdDecOv());

                    rfAssDecOv.retrieve();

                    if (!rfAssDecOv.isNew()) {
                        rfAssDecOv.delete(transaction);
                    } else {
                        throw new Exception(
                                "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver l'association dec-ov restit");
                    }
                }
            }

        } else {
            throw new Exception(
                    "RFAnnulerPreparationDecisionService.annulerPreparationDecision: Impossible de retrouver la décision");
        }
    }
}