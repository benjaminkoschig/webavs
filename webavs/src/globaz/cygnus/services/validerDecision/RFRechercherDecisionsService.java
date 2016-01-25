package globaz.cygnus.services.validerDecision;

import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidation;
import globaz.cygnus.db.decisions.RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager;
import globaz.cygnus.db.paiement.RFDecisionJointPrestationJointOrdreVersement;
import globaz.cygnus.db.paiement.RFDecisionJointPrestationJointOrdreVersementManager;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class RFRechercherDecisionsService {

    /**
     * Methode pour récupérer les detttes de toutes les décisions non validées.
     * 
     * @param decisionArray
     * @throws Exception
     */
    private void rechercheDecisionsAvecDettes(Map<String, ArrayList<String>> listeDecisionsAvecDettes, BSession session)
            throws Exception {
        RFDecisionJointPrestationJointOrdreVersementManager decJointPrestJointOvMgr = new RFDecisionJointPrestationJointOrdreVersementManager();
        decJointPrestJointOvMgr.setSession(session);
        decJointPrestJointOvMgr.setForDettesDecisions(true);
        decJointPrestJointOvMgr.changeManagerSize(0);
        decJointPrestJointOvMgr.find();

        BigDecimal montantDette = new BigDecimal(0);

        Iterator itrDecisionAvecDettes = decJointPrestJointOvMgr.iterator();
        while (itrDecisionAvecDettes.hasNext()) {
            RFDecisionJointPrestationJointOrdreVersement decisionDette = (RFDecisionJointPrestationJointOrdreVersement) itrDecisionAvecDettes
                    .next();

            String idDecision = decisionDette.getIdDecision();
            montantDette = new BigDecimal(decisionDette.getMontantDette());

            // Si la décision est déjà présente dans la liste
            if (listeDecisionsAvecDettes.containsKey(idDecision)) {
                // addition des montants
                montantDette = montantDette.add(new BigDecimal(listeDecisionsAvecDettes.get(idDecision).get(0)));
                // Suppression de l'ancien montant
                listeDecisionsAvecDettes.get(idDecision).remove(0);
                // Insertion du nouveau montant (additionné)
                listeDecisionsAvecDettes.get(idDecision).add(0, montantDette.toString());
            } else {
                // Sinon, insertion de la décsion et de sa dette
                ArrayList<String> element = new ArrayList<String>();
                element.add(0, montantDette.toString());

                listeDecisionsAvecDettes.put(idDecision, element);
            }
        }
    }

    /**
     * Recherche des décisions dans l'état non validé et récupération des idTiers. Ne prend pas en compte les demandes
     * 12.7.
     */
    public int rechercherDecisions(
            RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager decisionJointTiersManager,
            List<RFDecisionDocumentData> decisionArray, BSession session) throws Exception {

        Map<String, ArrayList<String>> listeDecisionsAvecDettes = new HashMap<String, ArrayList<String>>();

        // recherche de dettes pour toutes les décisions non validées
        rechercheDecisionsAvecDettes(listeDecisionsAvecDettes, session);

        /************************************************************************************************
         * Algorithme de tri du resultSet * Pour chaque decision on initialise toutes ces demande. * Et pour chaque
         * demande tous ces motifs de refus. * On commence par parcourir tous les enregistrement renvoyés par la
         * requête. * A chaque iteration on mémorise la decision, demande, motif que l'on vient de parcourir. *
         ************************************************************************************************/
        // initialisation des variables
        RFDecisionJointDemandeJointMotifRefusJointTiersValidation itDecision = null;
        String currentDecisionId = "";
        String currentDemandeId = "";
        String currentMotifRefusId = "";
        String currentCopieId = "";

        ArrayList<RFDemandeValidationData> demandesSet = new ArrayList<RFDemandeValidationData>();
        ArrayList<RFCopieDecisionsValidationData> copiesSet = new ArrayList<RFCopieDecisionsValidationData>();
        Set<RFMotifRefusDemandeValidationData> motifsSet = new HashSet<RFMotifRefusDemandeValidationData>();
        Set<String> idsMotifsRefus = new HashSet<String>();
        int i = 0;

        for (Iterator<RFDecisionJointDemandeJointMotifRefusJointTiersValidation> it = decisionJointTiersManager
                .iterator(); it.hasNext();) {
            itDecision = it.next();
            if (!currentDecisionId.equals(itDecision.getIdDecision())) {
                // si il y avait d'autres motifs de refus avant il est temps de les ajouter
                if (!JadeStringUtil.isEmpty(currentMotifRefusId)) {
                    // numero de la decision à ajouter
                    RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                            .getContainer().get(i - 1);

                    if (!idsMotifsRefus.contains(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus())) {
                        idsMotifsRefus.add(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus());
                        motifsSet.add(new RFMotifRefusDemandeValidationData(rfDecJoiDemJoiMotRefJoiTieVal
                                .getIdMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusFr(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusDe(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusIt(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getMontantMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal
                                        .getMotifRefusSysteme()));
                    }
                }

                // si il y avait d'autres demande avant il est temps de les ajouter
                if (!JadeStringUtil.isEmpty(currentDemandeId)) {
                    // numero de la decision à ajouter
                    RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                            .getContainer().get(i - 1);

                    demandesSet.add(new RFDemandeValidationData(rfDecJoiDemJoiMotRefJoiTieVal.getIdDemande(),
                            motifsSet, rfDecJoiDemJoiMotRefJoiTieVal.getDateFacture(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDateDebutTraitementDemande(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDateFinTraitementDemande(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDateReceptionDemande(), rfDecJoiDemJoiMotRefJoiTieVal.getIdTiers(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIdFournisseur(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdSousTypeSoin(), rfDecJoiDemJoiMotRefJoiTieVal.getIdRubriqueAI(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIdRubriqueAVS(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdRubriqueInvalidite(), rfDecJoiDemJoiMotRefJoiTieVal.getIsPP(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIsTexteRedirection(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdTypeSoin(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantFacture(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getMontantAccepte(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIsForcerPayement(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantMotifRefusOAI(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getMontantMensuel(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdTiersAssureConcerne(), rfDecJoiDemJoiMotRefJoiTieVal.getIdDemandeParent(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIdQdPrincipale(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getRemarqueFournisseur()));

                }

                // si il y avait d'autres copies avant il est temps de les ajouter
                if (!JadeStringUtil.isEmpty(currentCopieId)) {

                    // numero de la decision à ajouter
                    RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                            .getContainer().get(i - 1);

                    copiesSet.add(new RFCopieDecisionsValidationData(rfDecJoiDemJoiMotRefJoiTieVal.getIdCopie(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIdTiersCopie(), "", rfDecJoiDemJoiMotRefJoiTieVal
                                    .getHasPageGarde(), rfDecJoiDemJoiMotRefJoiTieVal.getHasVersement(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getHasDecompte(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getHasRemarques(), rfDecJoiDemJoiMotRefJoiTieVal.getHasMoyensDroit(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getHasSignature(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getHasAnnexes(), rfDecJoiDemJoiMotRefJoiTieVal.getHasCopies()));

                }

                // si il y avait une autre decision avant il est temps de l'ajouter
                if (!JadeStringUtil.isEmpty(currentDecisionId)) {
                    // numero de la decision à ajouter
                    RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                            .getContainer().get(i - 1);
                    decisionArray.add(new RFDecisionDocumentData(rfDecJoiDemJoiMotRefJoiTieVal.getIdDecision(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIdTiers(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdGestionnaire(), rfDecJoiDemJoiMotRefJoiTieVal.getDatePreparation(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getAnneeQD(), demandesSet, rfDecJoiDemJoiMotRefJoiTieVal
                                    .getTexteRemarque(), rfDecJoiDemJoiMotRefJoiTieVal.getTexteAnnexe(), copiesSet,
                            rfDecJoiDemJoiMotRefJoiTieVal.getDecompteFactureRetour(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getBulletinVersementRetour(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getBordereauAccompagnement(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getMontantExcedentDeRecette(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getMontantDepassementQd(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantTotalRFM(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getTypePaiement(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDateDebutRetro(), rfDecJoiDemJoiMotRefJoiTieVal.getDateFinRetro(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getMontantCourantPartieRetroactive(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getMontantCourantPartieFuture(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getNumeroDecision(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getGenrePrestation(), rfDecJoiDemJoiMotRefJoiTieVal.getIdTypeSoin(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getReferencePaiement(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getMontantARembourserParLeDsas(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdAdressePaiement(), rfDecJoiDemJoiMotRefJoiTieVal.getIdQdPrincipale(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getIncitationDepotNouvelleDemande(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getRetourBV(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDateSurDocument()));
                }
                currentDecisionId = itDecision.getIdDecision();

                // si c'est une nouvelle demande
                if (!currentDemandeId.equals(itDecision.getIdDemande())) {
                    // commencer un nouveau tableau de demande
                    demandesSet = new ArrayList<RFDemandeValidationData>();
                    // commencer un nouveau tableau de motifs de refus
                    motifsSet = new HashSet<RFMotifRefusDemandeValidationData>();
                    idsMotifsRefus = new HashSet<String>();
                } else {
                    // c'est une nouveau tableau de motifs de refus
                    if (!currentMotifRefusId.equals(itDecision.getIdMotifRefus())) {
                        // commencer un nouveau tableau de motifs de refus
                        motifsSet = new HashSet<RFMotifRefusDemandeValidationData>();
                        idsMotifsRefus = new HashSet<String>();
                    }
                }
                // nouvelle copie
                copiesSet = new ArrayList<RFCopieDecisionsValidationData>();

                currentDemandeId = itDecision.getIdDemande();
                currentMotifRefusId = itDecision.getIdMotifRefus();
                currentCopieId = itDecision.getIdCopie();
                // même décision donc on ajoute un enregistrement au tableauourant de demande OU de copie!!
            } else {
                // si c'est une nouvelle demande nouvelle demande pour même décision
                if (!((RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                        .getContainer().get(i - 1)).getIdDemande().equals(itDecision.getIdDemande())) {

                    // si il y avait d'autres motifs de refus avant il est temps de les ajouter
                    if (!JadeStringUtil.isEmpty(currentMotifRefusId)) {
                        RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                                .getContainer().get(i - 1);// numero de la
                                                           // decision à
                                                           // ajouter
                        if (!idsMotifsRefus.contains(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus())) {
                            idsMotifsRefus.add(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus());
                            motifsSet.add(new RFMotifRefusDemandeValidationData(rfDecJoiDemJoiMotRefJoiTieVal
                                    .getIdMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusFr(),
                                    rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusDe(),
                                    rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusIt(),
                                    rfDecJoiDemJoiMotRefJoiTieVal.getMontantMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal
                                            .getMotifRefusSysteme()));
                        }
                    }

                    // si il y avait une autre demande avant il est temps de
                    // l'ajouter
                    if (!JadeStringUtil.isEmpty(currentDemandeId)) {
                        RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                                .getContainer().get(i - 1);// numero de la
                                                           // demande à ajouter
                        demandesSet.add(new RFDemandeValidationData(rfDecJoiDemJoiMotRefJoiTieVal.getIdDemande(),
                                motifsSet, rfDecJoiDemJoiMotRefJoiTieVal.getDateFacture(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getDateDebutTraitementDemande(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getDateFinTraitementDemande(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getDateReceptionDemande(), rfDecJoiDemJoiMotRefJoiTieVal
                                        .getIdTiers(), rfDecJoiDemJoiMotRefJoiTieVal.getIdFournisseur(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getIdSousTypeSoin(), rfDecJoiDemJoiMotRefJoiTieVal
                                        .getIdRubriqueAI(), rfDecJoiDemJoiMotRefJoiTieVal.getIdRubriqueAVS(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getIdRubriqueInvalidite(), rfDecJoiDemJoiMotRefJoiTieVal
                                        .getIsPP(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getIsTexteRedirection(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getIdTypeSoin(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getMontantFacture(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getMontantAccepte(),
                                // rfDecJoiDemJoiMotRefJoiTieVal.getMontantPeriodeDeTraitement(),
                                // rfDecJoiDemJoiMotRefJoiTieVal.getMontantAcceptePeriodeDeTraitement(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getIsForcerPayement(), rfDecJoiDemJoiMotRefJoiTieVal
                                        .getMontantMotifRefusOAI(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantMensuel(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getIdTiersAssureConcerne(), rfDecJoiDemJoiMotRefJoiTieVal
                                        .getIdDemandeParent(), rfDecJoiDemJoiMotRefJoiTieVal.getIdQdPrincipale(),
                                rfDecJoiDemJoiMotRefJoiTieVal.getRemarqueFournisseur()));
                    }
                    currentDemandeId = itDecision.getIdDemande();

                    // nouveau tableau de motifs de refus
                    // copiesSet = new ArrayList<RFCopieDecisions>();
                    motifsSet = new HashSet<RFMotifRefusDemandeValidationData>();
                    idsMotifsRefus = new HashSet<String>();
                    currentMotifRefusId = itDecision.getIdMotifRefus();
                } else {
                    if (itDecision.getIdCopie().equals(
                            ((RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                                    .getContainer().get(i - 1)).getIdCopie())) {
                        if (!JadeStringUtil.isEmpty(currentMotifRefusId)) {
                            RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                                    .getContainer().get(i - 1);// numero de la
                                                               // demande à
                                                               // ajouter
                            if (!idsMotifsRefus.contains(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus())) {
                                idsMotifsRefus.add(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus());
                                motifsSet.add(new RFMotifRefusDemandeValidationData(rfDecJoiDemJoiMotRefJoiTieVal
                                        .getIdMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusFr(),
                                        rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusDe(),
                                        rfDecJoiDemJoiMotRefJoiTieVal.getDescriptionMotifRefusIt(),
                                        rfDecJoiDemJoiMotRefJoiTieVal.getMontantMotifRefus(),
                                        rfDecJoiDemJoiMotRefJoiTieVal.getMotifRefusSysteme()));
                            }
                        }
                        currentMotifRefusId = itDecision.getIdMotifRefus();
                    } else {
                        if (!currentCopieId.equals(itDecision.getIdCopie())) {// nouvelle
                                                                              // copie
                                                                              // pour
                                                                              // même
                                                                              // demande
                            // si il y avait une autre copie avant il est temps
                            // de l'ajouter
                            if (!JadeStringUtil.isBlankOrZero(currentCopieId)) {
                                RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                                        .getContainer().get(i - 1);// numero de
                                                                   // la
                                                                   // demande à
                                                                   // ajouter
                                copiesSet.add(new RFCopieDecisionsValidationData(rfDecJoiDemJoiMotRefJoiTieVal
                                        .getIdCopie(), rfDecJoiDemJoiMotRefJoiTieVal.getIdTiersCopie(), "",
                                        rfDecJoiDemJoiMotRefJoiTieVal.getHasPageGarde(), rfDecJoiDemJoiMotRefJoiTieVal
                                                .getHasVersement(), rfDecJoiDemJoiMotRefJoiTieVal.getHasDecompte(),
                                        rfDecJoiDemJoiMotRefJoiTieVal.getHasRemarques(), rfDecJoiDemJoiMotRefJoiTieVal
                                                .getHasMoyensDroit(), rfDecJoiDemJoiMotRefJoiTieVal.getHasSignature(),
                                        rfDecJoiDemJoiMotRefJoiTieVal.getHasAnnexes(), rfDecJoiDemJoiMotRefJoiTieVal
                                                .getHasCopies()));
                            }
                            currentCopieId = itDecision.getIdCopie();
                            motifsSet = new HashSet<RFMotifRefusDemandeValidationData>();
                            idsMotifsRefus = new HashSet<String>();
                        }
                    }
                }
            }

            i++;

        }

        // on a tout parcouru : on ajoute la dernière décision
        RFDecisionJointDemandeJointMotifRefusJointTiersValidation rfDecJoiDemJoiMotRefJoiTieVal = new RFDecisionJointDemandeJointMotifRefusJointTiersValidation();
        if (!JadeStringUtil.isEmpty(currentDecisionId)) {
            rfDecJoiDemJoiMotRefJoiTieVal = (RFDecisionJointDemandeJointMotifRefusJointTiersValidation) decisionJointTiersManager
                    .getContainer().get(i - 1);
            // numero de la decision à ajouter si la derniere est une nouvelle demande
            if (!JadeStringUtil.isEmpty(itDecision.getIdMotifRefus())) {
                if (!idsMotifsRefus.contains(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus())) {
                    idsMotifsRefus.add(rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus());
                    motifsSet.add(new RFMotifRefusDemandeValidationData(
                            rfDecJoiDemJoiMotRefJoiTieVal.getIdMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDescriptionMotifRefusFr(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDescriptionMotifRefusDe(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getDescriptionMotifRefusIt(),
                            rfDecJoiDemJoiMotRefJoiTieVal.getMontantMotifRefus(), rfDecJoiDemJoiMotRefJoiTieVal
                                    .getMotifRefusSysteme()));
                }
            }
            if (!JadeStringUtil.isEmpty(itDecision.getIdDemande())) {
                demandesSet.add(new RFDemandeValidationData(rfDecJoiDemJoiMotRefJoiTieVal.getIdDemande(), motifsSet,
                        rfDecJoiDemJoiMotRefJoiTieVal.getDateFacture(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getDateDebutTraitementDemande(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getDateFinTraitementDemande(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getDateReceptionDemande(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getIdTiers(), rfDecJoiDemJoiMotRefJoiTieVal.getIdFournisseur(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getIdSousTypeSoin(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getIdRubriqueAI(), rfDecJoiDemJoiMotRefJoiTieVal.getIdRubriqueAVS(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getIdRubriqueInvalidite(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getIsPP(), rfDecJoiDemJoiMotRefJoiTieVal.getIsTexteRedirection(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getIdTypeSoin(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getMontantFacture(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantAccepte(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getIsForcerPayement(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getMontantMotifRefusOAI(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantMensuel(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getIdTiersAssureConcerne(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getIdDemandeParent(), rfDecJoiDemJoiMotRefJoiTieVal.getIdQdPrincipale(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getRemarqueFournisseur()));
            }

            if (!JadeStringUtil.isEmpty(itDecision.getIdCopie())) {
                copiesSet.add(new RFCopieDecisionsValidationData(rfDecJoiDemJoiMotRefJoiTieVal.getIdCopie(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getIdTiersCopie(), "", rfDecJoiDemJoiMotRefJoiTieVal
                                .getHasPageGarde(), rfDecJoiDemJoiMotRefJoiTieVal.getHasVersement(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getHasDecompte(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getHasRemarques(), rfDecJoiDemJoiMotRefJoiTieVal
                                .getHasMoyensDroit(), rfDecJoiDemJoiMotRefJoiTieVal.getHasSignature(),
                        rfDecJoiDemJoiMotRefJoiTieVal.getHasAnnexes(), rfDecJoiDemJoiMotRefJoiTieVal.getHasCopies()));
            }

            decisionArray.add(new RFDecisionDocumentData(rfDecJoiDemJoiMotRefJoiTieVal.getIdDecision(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getIdTiers(), rfDecJoiDemJoiMotRefJoiTieVal.getIdGestionnaire(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getDatePreparation(), rfDecJoiDemJoiMotRefJoiTieVal.getAnneeQD(),
                    demandesSet, rfDecJoiDemJoiMotRefJoiTieVal.getTexteRemarque(), rfDecJoiDemJoiMotRefJoiTieVal
                            .getTexteAnnexe(), copiesSet, rfDecJoiDemJoiMotRefJoiTieVal.getDecompteFactureRetour(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getBulletinVersementRetour(), rfDecJoiDemJoiMotRefJoiTieVal
                            .getBordereauAccompagnement(), rfDecJoiDemJoiMotRefJoiTieVal.getMontantExcedentDeRecette(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getMontantDepassementQd(), rfDecJoiDemJoiMotRefJoiTieVal
                            .getMontantTotalRFM(), rfDecJoiDemJoiMotRefJoiTieVal.getTypePaiement(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getDateDebutRetro(), rfDecJoiDemJoiMotRefJoiTieVal.getDateFinRetro(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getMontantCourantPartieRetroactive(), rfDecJoiDemJoiMotRefJoiTieVal
                            .getMontantCourantPartieFuture(), rfDecJoiDemJoiMotRefJoiTieVal.getNumeroDecision(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getGenrePrestation(), rfDecJoiDemJoiMotRefJoiTieVal.getIdTypeSoin(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getReferencePaiement(), rfDecJoiDemJoiMotRefJoiTieVal
                            .getMontantARembourserParLeDsas(), rfDecJoiDemJoiMotRefJoiTieVal.getIdAdressePaiement(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getIdQdPrincipale(), rfDecJoiDemJoiMotRefJoiTieVal
                            .getIncitationDepotNouvelleDemande(), rfDecJoiDemJoiMotRefJoiTieVal.getRetourBV(),
                    rfDecJoiDemJoiMotRefJoiTieVal.getDateSurDocument()));
        }

        for (RFDecisionDocumentData dec : decisionArray) {

            if (!JadeStringUtil.isBlankOrZero(dec.getIdQdPrincipale())) {

                RFAssQdDossierJointDossierJointTiersManager rfAssQdDosJoiDosJoiTieMgr = new RFAssQdDossierJointDossierJointTiersManager();
                rfAssQdDosJoiDosJoiTieMgr.setSession(session);
                rfAssQdDosJoiDosJoiTieMgr.setForIdQd(dec.getIdQdPrincipale());
                rfAssQdDosJoiDosJoiTieMgr.setForCsTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                rfAssQdDosJoiDosJoiTieMgr.changeManagerSize(0);
                rfAssQdDosJoiDosJoiTieMgr.find();

                if (rfAssQdDosJoiDosJoiTieMgr.size() == 1) {

                    RFAssQdDossierJointDossierJointTiers rfAssQdDosJoiDosJoiTie = (RFAssQdDossierJointDossierJointTiers) rfAssQdDosJoiDosJoiTieMgr
                            .getFirstEntity();
                    if (null != rfAssQdDosJoiDosJoiTie) {

                        dec.setIdTiers(rfAssQdDosJoiDosJoiTie.getIdTiers());
                        dec.setMontantSoldeExcedantRevenu(RFUtils.getSoldeExcedentDeRevenu(
                                rfAssQdDosJoiDosJoiTie.getIdQd(), session));

                    } else {
                        throw new Exception("RFValiderDecisionProcess.rechercheDecisionsNonValider(): Qd introuvable");
                    }
                } else {
                    throw new Exception("RFValiderDecisionProcess.rechercheDecisionsNonValider(): Qd introuvable");
                }

            }

            // Log
            for (RFDemandeValidationData d : dec.getDecisionDemande()) {
                System.out.println("Demande " + d.getIdDemande());
                for (RFMotifRefusDemandeValidationData m : d.getMotifsRefus()) {
                    System.out.println("Motif " + m.getIdMotifRefus());
                }
            }
            for (RFCopieDecisionsValidationData d : dec.getCopieDecision()) {
                System.out.println("Copie " + d.getIdCopie());
            }
        }

        // Ajout de la dette à chaque décision correspondante.
        for (RFDecisionDocumentData decision : decisionArray) {
            if (listeDecisionsAvecDettes.containsKey(decision.getIdDecision())) {
                if (!JadeStringUtil.isEmpty(listeDecisionsAvecDettes.get(decision.getIdDecision()).get(0))) {
                    decision.setMontantDette(listeDecisionsAvecDettes.get(decision.getIdDecision()).get(0));
                } else {
                    decision.setMontantDette("0");
                }
            } else {
                decision.setMontantDette("0");
            }
        }

        return decisionArray.size();
    }
}
