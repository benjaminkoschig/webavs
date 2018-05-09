package globaz.cygnus.services.validerDecision;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REInfoCompta;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeDev19JointAssDemandeDev19Ftd15;
import globaz.cygnus.db.demandes.RFDemandeDev19JointAssDemandeDev19Ftd15Manager;
import globaz.cygnus.db.ordresversements.RFPrestationJointOrdreVersement;
import globaz.cygnus.db.ordresversements.RFPrestationJointOrdreVersementManager;
import globaz.cygnus.db.paiement.RFDecisionJointPrestation;
import globaz.cygnus.db.paiement.RFDecisionJointPrestationManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.cygnus.db.paiement.RFPrestationAccordeeManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdAssureManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdPrincipaleManager;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Montant;

public class RFValiderDecisionMiseAJourEntitiesService {

    private boolean isDemandeTraitementDentaire15(RFDemandeValidationData demandeCourante) {

        if (demandeCourante.getIdTypeSoin().equals(IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15)) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isDevisConcerneAutresDemandesEtatEnregistreAutresGestionnaires(
            List<RFDecisionDocumentData> decisionArray, String idDevis, String idGestionnaire, BSession session)
            throws Exception {
        // Recherche des devis a ignorer (car seront passé en validé)
        Set<String> demandeFtd15AIgnorer = new HashSet<String>();
        for (RFDecisionDocumentData decisionData : decisionArray) {
            for (RFDemandeValidationData demandeCourante : decisionData.getDecisionDemande()) {
                if (demandeCourante != null) {
                    if (isDemandeTraitementDentaire15(demandeCourante)) {
                        demandeFtd15AIgnorer.add(demandeCourante.getIdDemande());
                    }
                }
            }
        }

        RFDemandeDev19JointAssDemandeDev19Ftd15Manager mgr = new RFDemandeDev19JointAssDemandeDev19Ftd15Manager();
        mgr.setSession(session);
        mgr.setForIdDevis(idDevis);
        mgr.setForIdsDemande15ToIgnore(demandeFtd15AIgnorer);
        mgr.changeManagerSize(0);
        mgr.find();

        Iterator<RFDemandeDev19JointAssDemandeDev19Ftd15> mgrItr = mgr.iterator();

        while (mgrItr.hasNext()) {

            RFDemandeDev19JointAssDemandeDev19Ftd15 demDev19JoiAssDemDev19Ftd15 = mgrItr.next();

            if (null != demDev19JoiAssDemDev19Ftd15) {

                if (!JadeStringUtil.isBlankOrZero(demDev19JoiAssDemDev19Ftd15.getIdDemandeFtd15Ass())) {
                    // Solution de facilité ...
                    RFDemande demandeFtd15 = new RFDemande();
                    demandeFtd15.setSession(session);
                    demandeFtd15.setIdDemande(demDev19JoiAssDemDev19Ftd15.getIdDemandeFtd15Ass());
                    demandeFtd15.retrieve();

                    if (!demandeFtd15.isNew()) {
                        if (IRFDemande.ENREGISTRE.equals(demandeFtd15.getCsEtat())
                                && !demandeFtd15.getIdGestionnaire().equals(idGestionnaire)) {
                            return true;
                        }

                    } else {
                        throw new Exception(
                                "RFValiderDecisionProcess.isDevisConcerneAutresDemandesEtatEnregistreAutresGestionnaires: demande introuvable");
                    }

                } else {
                    throw new Exception(
                            "RFValiderDecisionProcess.isDevisConcerneAutresDemandesEtatEnregistreAutresGestionnaires: demande introuvable");
                }
            }
        }

        return false;
    }

    protected void updateDecisionsQdsDemandesRfmAccordees(ArrayList<RFDecisionDocumentData> decisionArray,
            String dateSurDocument, BSession session, String idGestionnaire, BTransaction transaction) throws Exception {

        Set<String> devisTraiteSet = new HashSet<String>();
        Set<String> rfmAccordeeTraiteeSet = new HashSet<String>();
        Set<String> idsQdTraiteeSet = new HashSet<String>();

        for (RFDecisionDocumentData decisionData : decisionArray) {

            // la décision et ces demandes passe dans l'état validé
            RFDecision decision = new RFDecision();
            decision.setSession(session);
            decision.setIdDecision(decisionData.getIdDecision());
            decision.retrieve();

            if (!decision.isNew()) {
                decision.setEtatDecision(IRFDecisions.VALIDE);
                decision.setDateValidation(JACalendar.todayJJsMMsAAAA());
                decision.setDateSurDocument(dateSurDocument);

                if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
                    decision.setIdValidePar(idGestionnaire);
                } else {
                    decision.setIdValidePar(session.getUserId());
                }

                decision.update(transaction);

                idsQdTraiteeSet.add(decision.getIdQdPrincipale());

                for (RFDemandeValidationData demandeCourante : decisionData.getDecisionDemande()) {
                    if (null != demandeCourante) {
                        RFDemande demande = new RFDemande();
                        demande.setSession(session);
                        demande.setIdDemande(demandeCourante.getIdDemande());

                        demande.retrieve();

                        if (!demande.isNew()) {
                            demande.setCsEtat(IRFDemande.VALIDE);
                            demande.update(transaction);

                            // MàJ des RFM mis à charge initial pour l'annulation du calcul préparer décision
                            if (!JadeStringUtil.isBlankOrZero(demande.getIdQdAssure())) {
                                idsQdTraiteeSet.add(demande.getIdQdAssure());
                            }

                            if (!JadeStringUtil.isBlankOrZero(demande.getIdQdPrincipale())) {
                                idsQdTraiteeSet.add(demande.getIdQdPrincipale());
                            }

                            // mettre le père en annule
                            if (!JadeStringUtil.isEmpty(demande.getIdDemandeParent())
                                    && !"0".equals(demande.getIdDemandeParent())) {
                                RFDemande demandeParent = new RFDemande();
                                demandeParent.setSession(session);
                                demandeParent.setIdDemande(demande.getIdDemandeParent());
                                demandeParent.retrieve();

                                if (!demandeParent.isNew()) {
                                    demandeParent.setCsEtat(IRFDemande.ANNULE);
                                    demandeParent.update(transaction);
                                } else {
                                    throw new Exception(
                                            "RFValiderDecisionProcess.run():Demande parent (entité) introuvable");
                                }
                            }

                            // Si la demande de traitement dentaire est liéee à des devis soldés, on passe ceux-ci en
                            // validé
                            if (isDemandeTraitementDentaire15(demandeCourante)) {

                                RFDemandeDev19JointAssDemandeDev19Ftd15Manager mgr = new RFDemandeDev19JointAssDemandeDev19Ftd15Manager();
                                mgr.setSession(session);
                                mgr.setForIdDemandeFtd15(demandeCourante.getIdDemande());
                                mgr.changeManagerSize(0);
                                mgr.find();

                                Iterator<RFDemandeDev19JointAssDemandeDev19Ftd15> mgrItr = mgr.iterator();

                                while (mgrItr.hasNext()) {

                                    RFDemandeDev19JointAssDemandeDev19Ftd15 demDev19JoiAssDemDev19Ftd15 = mgrItr.next();

                                    if (null != demDev19JoiAssDemDev19Ftd15) {

                                        if (!devisTraiteSet.contains(demDev19JoiAssDemDev19Ftd15.getIdDemandeDevis19())) {

                                            // On s'assure que le devis n'est pas rattaché à d'autres demandes dans
                                            // l'état
                                            // "enregistré" d'un autre gestionnaire.
                                            if (!(isDevisConcerneAutresDemandesEtatEnregistreAutresGestionnaires(
                                                    decisionArray, demDev19JoiAssDemDev19Ftd15.getIdDemandeDevis19(),
                                                    demDev19JoiAssDemDev19Ftd15.getIdGestionnaire(), session) && (RFPropertiesUtils
                                                    .verificationDevisRattacheAutreDemande()))) {

                                                if (!demDev19JoiAssDemDev19Ftd15.getCsEtat().equals(IRFDemande.VALIDE)) {

                                                    BigDecimal solde = new BigDecimal(demDev19JoiAssDemDev19Ftd15
                                                            .getMontantAcceptation().replace("'", "")).add(RFUtils
                                                            .retrieveMontantAffecteDevis(
                                                                    demDev19JoiAssDemDev19Ftd15.getIdDemandeDevis19(),
                                                                    session).negate());

                                                    if (solde.compareTo(new BigDecimal("0")) <= 0) {

                                                        RFDemande devis = new RFDemande();
                                                        devis.setSession(session);
                                                        devis.setIdDemande(demDev19JoiAssDemDev19Ftd15
                                                                .getIdDemandeDevis19());

                                                        devis.retrieve();

                                                        if (!devis.isNew()) {
                                                            devis.setCsEtat(IRFDemande.VALIDE);
                                                            devis.update(transaction);
                                                        } else {
                                                            throw new Exception(
                                                                    "RFValiderDecisionProcess.run():Impossible de mettre à jour le devis en validé");
                                                        }

                                                    }
                                                }
                                            }

                                            devisTraiteSet.add(demandeCourante.getIdDemande());
                                        }

                                    } else {
                                        throw new Exception(
                                                "RFValiderDecisionProcess.run():Impossible de retrouver le devis");
                                    }
                                }
                            }
                        } else {
                            throw new Exception("RFValiderDecisionProcess.run():Demande (entité) introuvable");
                        }
                    } else {
                        throw new Exception("RFValiderDecisionProcess.run():Demande introuvable");
                    }
                }

                // MàJ des prestations
                RFDecisionJointPrestationManager rfDecJoiPreMgr = new RFDecisionJointPrestationManager();
                rfDecJoiPreMgr.setSession(session);
                rfDecJoiPreMgr.setForIdDecision(decisionData.getIdDecision());
                rfDecJoiPreMgr.changeManagerSize(0);
                rfDecJoiPreMgr.find();

                if (rfDecJoiPreMgr.size() != 0) {

                    Iterator<RFDecisionJointPrestation> rfDecJoiPreItr = rfDecJoiPreMgr.iterator();

                    while (rfDecJoiPreItr.hasNext()) {

                        RFDecisionJointPrestation rfDecJoiPre = rfDecJoiPreItr.next();

                        if (null != rfDecJoiPre) {

                            RFPrestation rfPrest = new RFPrestation();
                            rfPrest.setSession(session);
                            rfPrest.setIdPrestation(rfDecJoiPre.getIdPrestation());

                            rfPrest.retrieve(transaction);

                            if (!rfPrest.isNew()) {

                                rfPrest.setCsEtatPrestation(IRFPrestations.CS_ETAT_PRESTATION_VALIDE);
                                rfPrest.update(transaction);

                                // pour chaque OV de la prest
                                RFPrestationJointOrdreVersementManager rfPrestJoiOVMgr = new RFPrestationJointOrdreVersementManager();
                                rfPrestJoiOVMgr.setSession(session);
                                rfPrestJoiOVMgr.setForIdPrestation(rfDecJoiPre.getIdPrestation());
                                rfPrestJoiOVMgr.changeManagerSize(0);
                                rfPrestJoiOVMgr.find();

                                if (rfPrestJoiOVMgr.size() >= 1) {

                                    for (Iterator<RFPrestationJointOrdreVersement> it = rfPrestJoiOVMgr.iterator(); it
                                            .hasNext();) {

                                        String idTiers = (it.next()).getIdTiers();

                                        // pour chaque OV, on cré le compte annexe
                                        BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");
                                        APICompteAnnexe cptAnnexe = (APICompteAnnexe) sessionOsiris
                                                .getAPIFor(APICompteAnnexe.class);

                                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiers);
                                        if ((tw == null)
                                                || JadeStringUtil.isEmpty(tw
                                                        .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                                            throw new Exception("Tiers not found, or NSS is blank. idTiers = "
                                                    + idTiers);
                                        }
                                        cptAnnexe.createCompteAnnexe(sessionOsiris, transaction, idTiers,
                                                IntRole.ROLE_RENTIER,
                                                tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                                    }
                                } else if (!RFPropertiesUtils.miseEnGedDesDecisionsAZero()
                                        || !Montant.valueOf(rfDecJoiPre.getMontantTotal()).isZero()) {
                                    throw new FWIException("RFValiderDecisionProcess.run(): OV introuvable");
                                }

                            } else {
                                throw new Exception(
                                        "RFValiderDecisionProcess.run(): Préstation introuvable (RFPrestation)");
                            }
                        } else {
                            throw new Exception(
                                    "RFValiderDecisionProcess.run(): Préstation null (RFDecisionJointPrestation)");
                        }

                    }
                } else {
                    if (rfDecJoiPreMgr.size() != 0) {
                        throw new Exception(
                                "RFValiderDecisionProcess.run(): Décision introuvable (RFDecisionJointPrestationManager)");
                    }
                }

                // Màj des RFAccordées
                updateRfmAccordee(decisionData.getIdDecision(), rfmAccordeeTraiteeSet, session, transaction);

            } else {
                throw new Exception("RFValiderDecisionProcess.run(): Décision introuvable");
            }
        }

        // MàJ des Qds
        updateQd(idsQdTraiteeSet, idGestionnaire, session, transaction);

        // Màj des RFAccordées corrigées
        updateRfmAccordeeCorrigees(rfmAccordeeTraiteeSet, idGestionnaire, session, transaction);
    }

    private void updateQd(Set<String> idsQdTraiteeSet, String idGestionnaire, BSession session, BTransaction transaction)
            throws Exception {

        // Ajout des petites Qds à annuler correspondant à une restitution d'un régime sur plusieurs années
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
                idsQdTraiteeSet.add(qdCourante.getIdQdAssure());
            } else {
                throw new Exception("RFValiderDecisionProcess.updateQd: QD introuvable");
            }
        }

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
                idsQdTraiteeSet.add(qdCourante.getIdQdPrincipale());
            } else {
                throw new Exception("RFAnnulerPreparationDecisionService.majQdPrincipaleRestitution: QD introuvable");
            }
        }

        // Recherche des Qds
        for (String idQd : idsQdTraiteeSet) {

            if (!JadeStringUtil.isBlankOrZero(idQd)) {

                RFQd qdBase = new RFQd();
                qdBase.setSession(session);
                qdBase.setIdQd(idQd);

                qdBase.retrieve();

                if (!qdBase.isNew()) {

                    qdBase.setMontantInitialChargeRfm("");
                    qdBase.update(transaction);

                    if (qdBase.getCsGenreQd().equals(IRFQd.CS_GRANDE_QD)) {

                        RFQdPrincipale rfQdPrincipale = new RFQdPrincipale();
                        rfQdPrincipale.setSession(session);
                        rfQdPrincipale.setIdQdPrincipale(idQd);

                        rfQdPrincipale.retrieve();

                        if (!rfQdPrincipale.isNew()) {

                            rfQdPrincipale.setIdFamModAugmentationDeQd("");
                            rfQdPrincipale.setIdGesModSoldeExcedentAugmentationQdPreDec("");
                            rfQdPrincipale.setIdFamModSoldeExcedentPreDec("");

                            rfQdPrincipale.update(transaction);

                        } else {
                            throw new Exception(
                                    "RFAnnulerPreparationDecisionService.updateQd(): Impossible de retrouver la qd de la demande parent");
                        }

                    } else if (qdBase.getCsGenreQd().equals(IRFQd.CS_PETITE_QD)) {

                        // Suppression de l'augmentation de Qd
                        RFQdAssure rfQdAssure = new RFQdAssure();
                        rfQdAssure.setSession(session);
                        rfQdAssure.setIdQdAssure(idQd);
                        rfQdAssure.retrieve();

                        if (!rfQdAssure.isNew()) {

                            rfQdAssure.setIdGesModSoldeExcedentAugmentationQdPreDec("");
                            rfQdAssure.setIdFamilleAugmentation("");

                            rfQdAssure.update(transaction);

                        } else {
                            throw new Exception("RFAnnulerPreparationDecisionService.updateQd(): Petite QD introuvable");
                        }
                    }

                } else {
                    throw new Exception("RFAnnulerPreparationDecisionService.updateQd(): Qd base introuvable");
                }
            }
        }

    }

    private void updateRfmAccordee(String idDecision, Set<String> rfmAccordeeTraiteeSet, BSession session,
            BTransaction transaction) throws Exception {

        RFPrestationAccordeeJointREPrestationAccordeeManager rfPreAccMgr = new RFPrestationAccordeeJointREPrestationAccordeeManager();
        rfPreAccMgr.setForIdDecision(idDecision);
        rfPreAccMgr.setSession(session);
        rfPreAccMgr.changeManagerSize(0);
        rfPreAccMgr.find();

        Iterator<RFPrestationAccordeeJointREPrestationAccordee> rfPreAccItr = rfPreAccMgr.iterator();

        if (rfPreAccItr.hasNext()) {

            RFPrestationAccordeeJointREPrestationAccordee rfPreAcc = rfPreAccItr.next();

            if (null != rfPreAcc) {

                REInformationsComptabilite ic = new REInformationsComptabilite();
                ic.setIdInfoCompta(rfPreAcc.getIdInfoCompta());
                ic.setSession(session);
                ic.retrieve(transaction);

                if (JadeStringUtil.isBlankOrZero(ic.getIdCompteAnnexe())) {
                    REInfoCompta.initCompteAnnexe_noCommit(session, transaction, rfPreAcc.getIdTiers(), ic,
                            IREValidationLevel.VALIDATION_LEVEL_ALL);
                }

                REPrestationsAccordees rePrestationAccordee = new REPrestationsAccordees();
                rePrestationAccordee.setSession(session);
                rePrestationAccordee.setIdPrestationAccordee(rfPreAcc.getIdRFMAccordee());

                rePrestationAccordee.retrieve();

                if (!rePrestationAccordee.isNew()) {

                    rfmAccordeeTraiteeSet.add(rePrestationAccordee.getIdPrestationAccordee());

                    rePrestationAccordee.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                    rePrestationAccordee.update(transaction);

                    RFPrestationAccordee rfPrestationAccordee = new RFPrestationAccordee();
                    rfPrestationAccordee.setSession(session);
                    rfPrestationAccordee.setIdRFMAccordee(rfPreAcc.getIdRFMAccordee());

                    rfPrestationAccordee.retrieve();

                    if (!rfPrestationAccordee.isNew()) {

                        rfPrestationAccordee.setIdGestionnairePreparerDecision("");
                        rfPrestationAccordee.setDateFinDroitInitiale("");
                        rfPrestationAccordee.setDateDiminutionInitiale("");

                        rfPrestationAccordee.update(transaction);

                    } else {
                        throw new Exception("RFValiderDecisionProcess.updateRfmAccordee: RFM accordée non trouvée");
                    }
                } else {
                    throw new Exception("RFValiderDecisionProcess.updateRfmAccordee: Rente accordée non trouvée");
                }

            } else {
                throw new Exception("RFValiderDecisionProcess.updateRfmAccordee: "
                        + "Impossible de retrouver la prestation accordee");
            }
        }
    }

    private void updateRfmAccordeeCorrigees(Set<String> rfmAccordeeTraiteeSet, String idGestionnaire, BSession session,
            BTransaction transaction) throws Exception {

        RFPrestationAccordeeManager rfPreAccMgr = new RFPrestationAccordeeManager();
        rfPreAccMgr.setSession(session);

        if (RFPropertiesUtils.utiliserGestionnaireViewBean()) {
            rfPreAccMgr.setForIdGestionnairePreparerDecision(idGestionnaire);
        } else {
            rfPreAccMgr.setForIdGestionnairePreparerDecisionNotNull(Boolean.TRUE);
        }

        rfPreAccMgr.setForNotIdsRfPrestationAccordee(rfmAccordeeTraiteeSet);

        rfPreAccMgr.changeManagerSize(0);
        rfPreAccMgr.find();

        Iterator<RFPrestationAccordee> rfPreAccItr = rfPreAccMgr.iterator();

        if (rfPreAccItr.hasNext()) {

            RFPrestationAccordee rfPreAcc = rfPreAccItr.next();

            if (null != rfPreAcc) {

                RFPrestationAccordee rfPrestationAccordee = new RFPrestationAccordee();
                rfPrestationAccordee.setSession(session);
                rfPrestationAccordee.setIdRFMAccordee(rfPreAcc.getIdRFMAccordee());

                rfPrestationAccordee.retrieve();

                if (!rfPrestationAccordee.isNew()) {

                    rfPrestationAccordee.setIdGestionnairePreparerDecision("");
                    rfPrestationAccordee.setDateFinDroitInitiale("");
                    rfPrestationAccordee.setDateDiminutionInitiale("");

                    rfPrestationAccordee.update(transaction);

                } else {
                    throw new Exception("RFValiderDecisionProcess.updateRfmAccordeeCorrigees: RFM accordée non trouvée");
                }

            } else {
                throw new Exception("RFValiderDecisionProcess.updateRfmAccordeeCorrigees: RFM accordée null");
            }
        }
    }

}
