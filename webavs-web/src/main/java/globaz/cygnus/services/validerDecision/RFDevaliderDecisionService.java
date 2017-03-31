package globaz.cygnus.services.validerDecision;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.db.paiement.RFPrestation;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeManager;
import globaz.cygnus.db.paiement.RFPrestationManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenu;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * D�valide une d�cision en r�allouant la somme et l'ex�dent de revenu sur les Qds concern�es
 * 
 * author JJE
 */
public class RFDevaliderDecisionService {

    private static String idGestionnaire = "";
    private static BISession session = null;
    private static BITransaction transaction = null;

    public static void devaliderDecision(String idDecision, RFDecisionJointTiersViewBean vb) throws Exception {

        RFDecision rfDec = new RFDecision();
        rfDec.setSession((BSession) RFDevaliderDecisionService.session);
        rfDec.setIdDecision(idDecision);
        rfDec.retrieve();

        if (!rfDec.isNew()) {

            String idsMotifDeRefusSystemExcedent = RFUtils.getIdsMotifDeRefusSysteme(
                    (BSession) RFDevaliderDecisionService.session,
                    (BTransaction) RFDevaliderDecisionService.transaction).get(
                    IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU)[0];

            BigDecimal montantAReattribuerGrandeQdBigDec = new BigDecimal("0");
            Map<String, BigDecimal> idsPetiteQdMap = new HashMap<String, BigDecimal>();

            // modification de l'�tat de la d�cision
            rfDec.setEtatDecision(IRFDecisions.NON_VALIDE);
            rfDec.setDateSurDocument("");
            rfDec.setDateValidation("");
            rfDec.setIdValidePar("");
            rfDec.update(RFDevaliderDecisionService.transaction);

            RFDevaliderDecisionService.modfificationEtatPrestations(rfDec.getIdDecision());

            // modification de l'�tat des demandes de la d�cision
            RFDemandeManager rfDemMgr = new RFDemandeManager();
            rfDemMgr.setSession((BSession) RFDevaliderDecisionService.session);
            rfDemMgr.setForIdDecision(idDecision);

            rfDemMgr.find();

            Iterator<RFDemande> rfDemItr = rfDemMgr.iterator();
            while (rfDemItr.hasNext()) {

                RFDemande rfDemCourante = rfDemItr.next();

                if (rfDemCourante != null) {

                    if (rfDemCourante.getCsEtat().equals(IRFDemande.VALIDE)) {

                        RFDemande rfDem = new RFDemande();
                        rfDem.setSession((BSession) RFDevaliderDecisionService.session);
                        rfDem.setId(rfDemCourante.getIdDemande());

                        rfDem.retrieve();
                        if (!rfDem.isNew()) {

                            rfDem.setCsEtat(IRFDemande.CALCULE);
                            rfDem.update(RFDevaliderDecisionService.transaction);

                            // Si la demande a une demande parent
                            if (rfDem.getIdDemandeParent() != null) {
                                modifierEtatDemParent(rfDem);
                            }

                            // doit-on prendre en compte le montant de cette demande pour la grande Qd
                            String[] codesTypeDeSoinTab = RFUtils.getCodesTypeDeSoin(rfDem.getIdSousTypeDeSoin(),
                                    (BSession) RFDevaliderDecisionService.session);
                            if (!RFUtils.isSousTypeDeSoinNonImputeSurGrandeQd(codesTypeDeSoinTab[0],
                                    codesTypeDeSoinTab[1]) && !JadeStringUtil.isBlankOrZero(rfDem.getIdQdPrincipale())) {
                                montantAReattribuerGrandeQdBigDec = montantAReattribuerGrandeQdBigDec
                                        .add(new BigDecimal(rfDem.getMontantAPayer()));
                            }

                            // r�attribtution de l'argent sur la petite Qd
                            if (!JadeStringUtil.isBlankOrZero(rfDem.getIdQdAssure())) {
                                // Recherche du montant de l'exc�dent de revenu
                                RFAssMotifsRefusDemandeManager rfAssMotRefDemMgr = new RFAssMotifsRefusDemandeManager();
                                rfAssMotRefDemMgr.setSession((BSession) RFDevaliderDecisionService.session);
                                rfAssMotRefDemMgr.setForIdDemande(rfDem.getIdDemande());
                                rfAssMotRefDemMgr.setForIdMotifDeRefus(idsMotifDeRefusSystemExcedent);
                                rfAssMotRefDemMgr.changeManagerSize(0);
                                rfAssMotRefDemMgr.find();

                                BigDecimal montantMotifDeRefusExcedentBigDec = new BigDecimal("0");

                                if (rfAssMotRefDemMgr.size() == 1) {
                                    montantMotifDeRefusExcedentBigDec = new BigDecimal(
                                            ((RFAssMotifsRefusDemande) rfAssMotRefDemMgr.getFirstEntity())
                                                    .getMntMotifsDeRefus());
                                } else {
                                    if (rfAssMotRefDemMgr.size() != 0) {
                                        throw new Exception(
                                                "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver le montant du motif de refus");
                                    }
                                }

                                BigDecimal montantAReattribuerPetiteQdBigDec = new BigDecimal(rfDem.getMontantAPayer())
                                        .add(montantMotifDeRefusExcedentBigDec);

                                if (idsPetiteQdMap.containsKey(rfDem.getIdQdAssure())) {

                                    BigDecimal totalMontantDemandesARetribuer = idsPetiteQdMap.get(
                                            rfDem.getIdQdAssure()).add(montantAReattribuerPetiteQdBigDec);

                                    idsPetiteQdMap.put(rfDem.getIdQdAssure(), totalMontantDemandesARetribuer);

                                } else {
                                    idsPetiteQdMap.put(rfDem.getIdQdAssure(), montantAReattribuerPetiteQdBigDec);
                                }
                            }
                        } else {
                            throw new Exception(
                                    "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la demande");
                        }
                    } else {
                        throw new Exception(
                                "RFDevaliderDecisionService.devaliderDecision(): une demande n'est pas valid�e");
                    }
                } else {
                    throw new Exception(
                            "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la demande courante");
                }
            }

            // R�attribution de l'argent sur la grande Qd
            RFDevaliderDecisionService.reallocationGrandeQd(rfDec, montantAReattribuerGrandeQdBigDec, vb);
            // R�attribution de l'argent sur les petites Qds
            RFDevaliderDecisionService.reallocationPetiteQd(idsPetiteQdMap);

        } else {
            throw new Exception("RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la d�cision");
        }
    }

    /**
     * Modifie l'�tat de la demande parent dans le cas o� cette derni�re est ANNULE
     * 
     * @author bmu
     * 
     * @param rfDem
     * @throws Exception
     */
    private static void modifierEtatDemParent(RFDemande rfDem) throws Exception {
        RFDemande rfDemParent = new RFDemande();
        rfDemParent.setSession((BSession) RFDevaliderDecisionService.session);
        rfDemParent.setId(rfDem.getIdDemandeParent());

        rfDemParent.retrieve();

        if (rfDemParent.getCsEtat().equals(IRFDemande.ANNULE)) {
            // Changement de l'�tat ANNULE vers PAYE
            rfDemParent.setCsEtat(IRFDemande.PAYE);
            rfDemParent.update(RFDevaliderDecisionService.transaction);
        }
    }

    public static String getIdGestionnaire() {
        return RFDevaliderDecisionService.idGestionnaire;
    }

    public static BISession getSession() {
        return RFDevaliderDecisionService.session;
    }

    public static BITransaction getTransaction() {
        return RFDevaliderDecisionService.transaction;
    }

    private static void modfificationEtatPrestations(String idDecision) throws Exception {

        // modification des prestations
        RFPrestationManager rfPrestMgr = new RFPrestationManager();
        rfPrestMgr.setSession((BSession) RFDevaliderDecisionService.getSession());
        rfPrestMgr.setForIdDecision(idDecision);
        rfPrestMgr.find();
        Iterator<RFPrestation> rfPrestItr = rfPrestMgr.iterator();

        while (rfPrestItr.hasNext()) {
            RFPrestation prestationCourante = rfPrestItr.next();

            RFPrestation rfPrest = new RFPrestation();
            rfPrest.setSession((BSession) RFDevaliderDecisionService.getSession());
            rfPrest.setIdPrestation(prestationCourante.getIdPrestation());
            rfPrest.retrieve();

            if (!rfPrest.isNew()) {
                rfPrest.setCsEtatPrestation(IRFPrestations.CS_ETAT_PRESTATION_MIS_EN_LOT);
                rfPrest.update(RFDevaliderDecisionService.transaction);
            } else {
                throw new Exception(
                        "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la prestation");
            }
        }

        // modification des prestations accord�es
        RFPrestationAccordeeManager rfPreAccMgr = new RFPrestationAccordeeManager();
        rfPreAccMgr.setSession((BSession) RFDevaliderDecisionService.getSession());
        rfPreAccMgr.setForIdDecision(idDecision);
        rfPreAccMgr.find();
        Iterator<RFPrestationAccordee> rfPrestAccItr = rfPreAccMgr.iterator();

        while (rfPrestAccItr.hasNext()) {
            RFPrestationAccordee prestationAccordeeCourante = rfPrestAccItr.next();

            REPrestationsAccordees rfPreAcc = new REPrestationsAccordees();
            rfPreAcc.setSession((BSession) RFDevaliderDecisionService.getSession());
            rfPreAcc.setIdPrestationAccordee(prestationAccordeeCourante.getIdRFMAccordee());
            rfPreAcc.retrieve();

            if (!rfPreAcc.isNew()) {
                rfPreAcc.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
                rfPreAcc.update(RFDevaliderDecisionService.transaction);
            } else {
                throw new Exception(
                        "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la prestation");
            }
        }
    }

    private static void reallocationGrandeQd(RFDecision rfDec, BigDecimal montantAReattribuerGrandeQdBigDec,
            RFDecisionJointTiersViewBean vb) throws Exception {

        // R�attribution de l'argent sur la grande Qd
        if (!JadeStringUtil.isBlankOrZero(rfDec.getIdQdPrincipale())) {
            RFQd rfQd = new RFQd();
            rfQd.setSession((BSession) RFDevaliderDecisionService.session);
            rfQd.setIdQd(rfDec.getIdQdPrincipale());

            rfQd.retrieve();
            if (!rfQd.isNew()) {

                RFQdPrincipale rfQdPri = new RFQdPrincipale();
                rfQdPri.setSession((BSession) RFDevaliderDecisionService.session);
                rfQdPri.setIdQdPrincipale(rfDec.getIdQdPrincipale());

                // On test si la Qd n'est pas utilis�e par le caclul d'un autre gestionnaire
                if (!JadeStringUtil.isBlankOrZero(rfQdPri.getIdGesModSoldeExcedentAugmentationQdPreDec())) {
                    throw new Exception(
                            "RFDevaliderDecisionService.devaliderDecision(): la Qd est utilis�e par le calcul d'un autre gestionnaire");
                }

                rfQdPri.retrieve();
                if (!rfQdPri.isNew()) {

                    String soldeExcedentStr = RFUtils.getSoldeExcedentDeRevenu(rfDec.getIdQdPrincipale(),
                            (BSession) RFDevaliderDecisionService.session);

                    BigDecimal montantExcedentDecBigDec = new BigDecimal(rfDec.getMontantExcedentDeRecette());
                    BigDecimal montantChargeQdBigDec = new BigDecimal(rfQd.getMontantChargeRfm());
                    BigDecimal montantChargeActuelleMoinsAReatribuerBigDec = montantChargeQdBigDec
                            .add(montantAReattribuerGrandeQdBigDec.negate());

                    // Si la Qd n'a actuellement pas de solde exc�dent de revenu
                    if (JadeStringUtil.isBlankOrZero(soldeExcedentStr)) {

                        // Si la d�cision poss�de un exc�dent de revenu
                        if (montantExcedentDecBigDec.compareTo(new BigDecimal("0")) > 0) {

                            // Si une partie de la d�cision n'a pas �t� absorb�e par l'exc�dent de revenu
                            // -> une r�allocation de la charge doit s'effectuer
                            if (montantAReattribuerGrandeQdBigDec.compareTo(new BigDecimal("0")) > 0) {

                                // Si la charge actuelle de la Qd moins la charge � r�attribuer vaut null,
                                // On peut r�allouer l'exc�dent de revenu
                                if (montantChargeActuelleMoinsAReatribuerBigDec.compareTo(new BigDecimal("0")) <= 0) {

                                    rfQd.setMontantInitialChargeRfm("0.00");
                                    rfQdPri.setIdGesModSoldeExcedentAugmentationQdPreDec(RFDevaliderDecisionService.session
                                            .getUserId());

                                    RFDevaliderDecisionService.reattribuerSoldeExcedent(rfQdPri,
                                            montantExcedentDecBigDec);

                                    RFUtils.setMsgExceptionWarningViewBean(vb,
                                            "La d�validation c'est d�roul�e avec succ�s");

                                }// Il reste une charge sur la Qd malgr� la r�attribution
                                 // -> impossible de r�allouer l'exc�dent de revenu
                                else {
                                    // on r�alloue le montant � r�atribuer
                                    rfQd.setMontantInitialChargeRfm(montantChargeActuelleMoinsAReatribuerBigDec
                                            .toString());
                                    rfQdPri.setIdGesModSoldeExcedentAugmentationQdPreDec(RFDevaliderDecisionService.session
                                            .getUserId());

                                    RFUtils.setMsgExceptionWarningViewBean(vb,
                                            "Cas 1: La r�allocation de l'exc�dent de revenu n'a pas pu s'effectuer (charge RFM > 0 apr�s r�allocation)");

                                }
                            }// Toute la d�cision c'est faite absorb�e par l'exc�dent de revenu -> pas de
                             // r�allocation
                             // de charge
                            else if (montantAReattribuerGrandeQdBigDec.compareTo(new BigDecimal("0")) <= 0) {
                                // Si la Qd n'a actuellement pas de charge RFM
                                if (montantChargeQdBigDec.compareTo(new BigDecimal("0")) <= 0) {
                                    // Ajout de deux lignes dans l'historique du solde exc�dent
                                    RFDevaliderDecisionService.reattribuerSoldeExcedent(rfQdPri,
                                            montantExcedentDecBigDec);

                                    RFUtils.setMsgExceptionWarningViewBean(vb,
                                            "La d�validation c'est d�roul�e avec succ�s");
                                } else {
                                    RFUtils.setMsgExceptionWarningViewBean(vb,
                                            "Cas 2: La r�allocation de l'exc�dent de revenu n'a pas pu s'effectuer (charge RFM > 0 apr�s r�allocation)");
                                }
                            }
                        }// Si la d�cision n'a pas d'exc�dent de revenu
                        else {

                            BigDecimal montantInitialChargeRfmTemp = new BigDecimal(
                                    rfQdPri.getMontantInitialChargeRfm());

                            if (montantInitialChargeRfmTemp.compareTo(new BigDecimal(0)) == 0) {
                                rfQd.setMontantInitialChargeRfm(montantChargeActuelleMoinsAReatribuerBigDec.toString());
                            } else {
                                rfQd.setMontantInitialChargeRfm(montantInitialChargeRfmTemp.subtract(
                                        montantAReattribuerGrandeQdBigDec).toString());
                            }

                            rfQdPri.setIdGesModSoldeExcedentAugmentationQdPreDec(RFDevaliderDecisionService.session
                                    .getUserId());
                            RFUtils.setMsgExceptionWarningViewBean(vb, "La d�validation c'est d�roul�e avec succ�s");
                        }
                    }// Si la Qd a actuellement un solde exc�dent de revenu
                    else {
                        // Si une partie de la d�cision n'a pas �t� absorb�e par l'exc�dent de revenu
                        // -> msg erreur car impossible, l'exc�dent devrait �tre � z�ro puisqu'une partie a �t�
                        // pay�e
                        if (montantAReattribuerGrandeQdBigDec.compareTo(new BigDecimal("0")) > 0) {
                            RFUtils.setMsgExceptionWarningViewBean(
                                    vb,
                                    "Cas 3: La r�allocation de l'exc�dent de revenu et de la charge RFM n'a pas pu s'effectuer (une partie de la d�cision � �t� accept�e, ce qui est impossible car la Qd comporte un exc�dent de revenu)");
                        }// Toute la d�cision c'est faite absorb�e par l'exc�dent de revenu -> pas de r�allocation
                         // de charge
                        else {

                            // Si la Qd n'a actuellement pas de charge RFM
                            if (montantChargeQdBigDec.compareTo(new BigDecimal("0")) <= 0) {
                                // Ajout de deux lignes dans l'historique du solde exc�dent
                                // TODO: a voir si faut pas ajouter NQdEx+ ExDec
                                if (montantExcedentDecBigDec.compareTo(new BigDecimal("0")) != 0) {
                                    RFDevaliderDecisionService.reattribuerSoldeExcedent(rfQdPri,
                                            montantExcedentDecBigDec);
                                }
                                RFUtils.setMsgExceptionWarningViewBean(vb, "La d�validation c'est d�roul�e avec succ�s");

                            }// Si la Qd a une charge RFM
                            else {
                                RFUtils.setMsgExceptionWarningViewBean(
                                        vb,
                                        "Cas 4: La r�allocation de l'exc�dent de revenu et de la charge RFM n'a pas pu s'effectuer (la Qd a un solde exc�dent de revenu et une charge RFM, ce qui est impossible)");
                            }
                        }
                    }

                    rfQdPri.update(RFDevaliderDecisionService.transaction);
                    rfQd.update(RFDevaliderDecisionService.transaction);

                } else {
                    throw new Exception("RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la Qd");
                }

            } else {
                throw new Exception(
                        "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la Qd principale");

            }
        }
    }

    private static void reallocationPetiteQd(Map<String, BigDecimal> idsPetiteQdMap) throws Exception {

        for (String key : idsPetiteQdMap.keySet()) {

            BigDecimal montantAReattribuerBigDec = idsPetiteQdMap.get(key);

            RFQd rfQdAssure = new RFQd();
            rfQdAssure.setSession((BSession) RFDevaliderDecisionService.session);
            rfQdAssure.setIdQd(key);

            rfQdAssure.retrieve();
            if (!rfQdAssure.isNew()) {

                BigDecimal montantInitialChargeRfm = new BigDecimal(rfQdAssure.getMontantInitialChargeRfm());
                BigDecimal rfQdAssMontantInitialBigDec = new BigDecimal(rfQdAssure.getMontantChargeRfm())
                        .add(montantAReattribuerBigDec.negate());
                BigDecimal rfQdAssMontantInitialTempBigDec = montantInitialChargeRfm.add(montantAReattribuerBigDec
                        .negate());

                if (montantInitialChargeRfm.compareTo(new BigDecimal(0)) == 0) {
                    rfQdAssure.setMontantInitialChargeRfm(rfQdAssMontantInitialBigDec.toString());
                } else {
                    rfQdAssure.setMontantInitialChargeRfm(rfQdAssMontantInitialTempBigDec.toString());
                }

                rfQdAssure.update(RFDevaliderDecisionService.transaction);

            } else {
                throw new Exception(
                        "RFDevaliderDecisionService.devaliderDecision(): Impossible de retrouver la petite Qd");
            }
        }

    }

    public static void reattribuerSoldeExcedent(RFQdPrincipale rfQdPri, BigDecimal montantAReattribuerBigDec)
            throws Exception {
        int famModifCompteur = 0;

        // calcule le nouvel id unique de famille de modification
        RFQdSoldeExcedentDeRevenuManager mgr = new RFQdSoldeExcedentDeRevenuManager();
        mgr.setSession((BSession) RFDevaliderDecisionService.session);
        mgr.setForIdFamilleMax(true);
        mgr.changeManagerSize(0);
        mgr.find();

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
        rfsoEx.setSession((BSession) RFDevaliderDecisionService.session);
        // rfsoEx.setVisaGestionnaire(qdCourante.idGestionnaire);
        rfsoEx.setVisaGestionnaire(RFDevaliderDecisionService.session.getUserId());
        rfsoEx.setRemarque("D�validation de la d�cision");
        rfsoEx.setConcerne("Montant n�gatif (sera supprim� par l'annulation du calcul)"/*
                                                                                        * ((BSession)
                                                                                        * RFDevaliderDecisionService
                                                                                        * .session) .getLabel(
                                                                                        * "PROCESS_PREPARER_DECISIONS_AJOUT_SOLDE_EXCEDENT_CONCERNE"
                                                                                        * )
                                                                                        */);
        rfsoEx.setMontantSoldeExcedent(montantAReattribuerBigDec.negate().toString());

        rfsoEx.setIdFamilleModification(Integer.toString(famModifCompteur));
        rfsoEx.setIdQd(rfQdPri.getIdQdPrincipale());
        rfsoEx.setDateModification(JadeDateUtil.getDMYDate(new Date()));
        rfsoEx.setTypeModification(IRFQd.CS_AJOUT);
        rfsoEx.setIdSoldeExcedentModifie("");

        rfsoEx.add(RFDevaliderDecisionService.transaction);

        // 2eme ligne

        RFQdSoldeExcedentDeRevenu rfsoEx2 = new RFQdSoldeExcedentDeRevenu();
        rfsoEx2.setSession((BSession) RFDevaliderDecisionService.session);
        rfsoEx2.setVisaGestionnaire(RFDevaliderDecisionService.session.getUserId());
        rfsoEx2.setRemarque("D�validation de la d�cision");
        rfsoEx2.setConcerne("Montant positif"/*
                                              * ((BSession) RFDevaliderDecisionService .session) .getLabel(
                                              * "PROCESS_PREPARER_DECISIONS_AJOUT_SOLDE_EXCEDENT_CONCERNE" )
                                              */);
        rfsoEx2.setMontantSoldeExcedent(montantAReattribuerBigDec.toString());

        rfsoEx2.setIdFamilleModification(Integer.toString(famModifCompteur + 1));
        rfsoEx2.setIdQd(rfQdPri.getIdQdPrincipale());
        rfsoEx2.setDateModification(JadeDateUtil.getDMYDate(new Date()));
        rfsoEx2.setTypeModification(IRFQd.CS_AJOUT);
        rfsoEx2.setIdSoldeExcedentModifie("");

        rfsoEx2.add(RFDevaliderDecisionService.transaction);

        rfQdPri.setIdFamModSoldeExcedentPreDec(Integer.toString(famModifCompteur));
    }

    public static void setIdGestionnaire(String idGestionnaire) {
        RFDevaliderDecisionService.idGestionnaire = idGestionnaire;
    }

    public static void setSession(BISession session) {
        RFDevaliderDecisionService.session = session;
    }

    public static void setTransaction(BITransaction transaction) {
        RFDevaliderDecisionService.transaction = transaction;
    }

}
