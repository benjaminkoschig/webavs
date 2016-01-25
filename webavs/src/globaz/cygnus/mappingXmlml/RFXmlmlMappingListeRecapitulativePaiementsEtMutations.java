/*
 * Créé le 26 janvier 2012
 */
package globaz.cygnus.mappingXmlml;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager;
import globaz.cygnus.db.recapitulation.MutationsMensuellesData;
import globaz.cygnus.db.recapitulation.RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation;
import globaz.cygnus.db.recapitulation.RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager;
import globaz.cygnus.db.recapitulation.RFPrestationAccordeeJointREPrestationAccordeeRecapitulation;
import globaz.cygnus.db.recapitulation.RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager;
import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.process.RFListeRecapitulativePaiementsProcess;
import globaz.cygnus.services.RFListeRecapitulativePaiementsService;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;

/**
 * @author MBO
 */
public class RFXmlmlMappingListeRecapitulativePaiementsEtMutations {

    private static List<MutationsMensuellesData> listeMutationAvs;
    private static ArrayList<String> listeTypePrestationAi = new ArrayList();
    private static ArrayList<String> listeTypePrestationAvs = new ArrayList();

    /**
     * Methode qui permet de retourner toutes les prestations accordées qui ont une date d'augmentation au 01.01 de
     * l'année suivante
     * 
     * @param session
     * @param date
     * @return Iterator de rfPrestationJointREPrestations
     * @throws Exception
     */
    private static List<String[]> getAdaptationAnnuelle(BSession session, String dateFinAnnee, String dateDebutAnnee)
            throws Exception {

        // Liste pour stocker les prestations
        List<String[]> elementsTuple = new ArrayList<String[]>();

        // Récupération des adaptations (diminutions + adaptations)
        RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager rfPrestJoinRePrestJointTiersAdaptation = new RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager();
        rfPrestJoinRePrestJointTiersAdaptation.setSession(session);
        // rfPrestJoinRePrestJointTiersAdaptation.setForCsTypesPrestationsAccordees(typePrestation);
        rfPrestJoinRePrestJointTiersAdaptation.setForIsDiminutionsAndAdaptations(true);
        rfPrestJoinRePrestJointTiersAdaptation.setForIsAdaptation(true);
        rfPrestJoinRePrestJointTiersAdaptation.setForDateDiminution(dateFinAnnee);
        rfPrestJoinRePrestJointTiersAdaptation.changeManagerSize(0);
        rfPrestJoinRePrestJointTiersAdaptation.find();
        Iterator<RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation> rfPrestJointRePrestJointTiersAdaptationItr = rfPrestJoinRePrestJointTiersAdaptation
                .iterator();
        while (rfPrestJointRePrestJointTiersAdaptationItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation rfPrestAdaptation = rfPrestJointRePrestJointTiersAdaptationItr
                    .next();

            String[] elementTupleTabStr = new String[] { rfPrestAdaptation.getNssTiers(),
                    rfPrestAdaptation.getNomTiers(), rfPrestAdaptation.getPrenomTiers(),
                    rfPrestAdaptation.getMontantParent(), rfPrestAdaptation.getMontantFils(),
                    rfPrestAdaptation.getCs_source_adaptation(), rfPrestAdaptation.getCsTypePrestationParent(),
                    rfPrestAdaptation.getCsTypePrestationFils() };
            elementsTuple.add(elementTupleTabStr);

        }

        // Récupération des adaptations (augmentations)
        RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager rfPrestJoinRePrestJointTiersAugmentation = new RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager();
        rfPrestJoinRePrestJointTiersAugmentation.setSession(session);
        // rfPrestJoinRePrestJointTiersAugmentation.setForCsTypesPrestationsAccordees(typePrestation);
        rfPrestJoinRePrestJointTiersAugmentation.setForIsDiminutionsAndAdaptations(false);
        rfPrestJoinRePrestJointTiersAugmentation.setForDateAugmentation(dateDebutAnnee);
        rfPrestJoinRePrestJointTiersAugmentation.setForIsAdaptation(true);
        rfPrestJoinRePrestJointTiersAugmentation.changeManagerSize(0);
        rfPrestJoinRePrestJointTiersAugmentation.find();
        Iterator<RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation> rfPrestJointRePrestJointTiersAugmentationItr = rfPrestJoinRePrestJointTiersAugmentation
                .iterator();

        while (rfPrestJointRePrestJointTiersAugmentationItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation rfPrestAugmentation = rfPrestJointRePrestJointTiersAugmentationItr
                    .next();

            String[] elementTupleTabStr = new String[] { rfPrestAugmentation.getNssTiers(),
                    rfPrestAugmentation.getNomTiers(), rfPrestAugmentation.getPrenomTiers(),
                    rfPrestAugmentation.getMontantParent(), rfPrestAugmentation.getMontantFils(),
                    rfPrestAugmentation.getCs_source_adaptation(), rfPrestAugmentation.getCsTypePrestationParent(),
                    rfPrestAugmentation.getCsTypePrestationFils() };

            elementsTuple.add(elementTupleTabStr);

        }

        // Tri des élément du tableau par le nom du tiers.
        Collections.sort(elementsTuple, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                if (o1[1].compareTo(o2[1]) != 0) {
                    // Si noms différents, tri par nom.
                    return o1[1].compareTo(o2[1]);
                } else {
                    // Sinon, tri par prénom
                    return o1[2].compareTo(o2[2]);
                }
            }
        });

        return elementsTuple;

    }

    /**
     * Methode qui retourne un tableau de String contenant nss, nom, prenom, montant
     * 
     * @param session
     * @param rfOrdVerJointPrest
     * @return liste
     * @throws Exception
     */
    private static ArrayList<String> getDetailMutationsPonctuelles(
            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrest,
            BSession session) throws Exception {
        ArrayList<String> liste = new ArrayList<String>();

        if (null != rfOrdVerJointPrest) {

            // Récupération de l'id tiers
            String idTiers = rfOrdVerJointPrest.getIdTiersOv();

            PRTiersWrapper tiersWrapper = PRTiersHelper.getPersonneAVS(session, idTiers);

            // Chargement des données du tiers d'après son id
            liste.add(0, tiersWrapper.getNSS());
            liste.add(1, tiersWrapper.getNom());
            liste.add(2, tiersWrapper.getPrenom());
            liste.add(3, rfOrdVerJointPrest.getMontantOvs());
            liste.add(4, rfOrdVerJointPrest.getIdPrestation());

        } else {
            throw new Exception(
                    "Erreur de chargement dans loadResult / RFXmlmlMappingListeRecapitulativePaiementsEtMutations - getDetailMoyensAux");
        }

        return liste;
    }

    /**
     * Methode qui retourne un tableau de String contenant nss, nom, prenom, montant
     * 
     * @param session
     * @param rfOrdVerJointPrest
     * @return liste
     * @throws Exception
     */
    private static ArrayList<String> getDetailPrestationPonctuelle(
            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrest,
            BSession session) throws Exception {
        ArrayList<String> liste = new ArrayList<String>();

        if (null != rfOrdVerJointPrest) {

            // Récupération de l'id tiers
            String idTiers = rfOrdVerJointPrest.getIdTiersOv();

            PRTiersWrapper tiersWrapper = PRTiersHelper.getPersonneAVS(session, idTiers);

            // Chargement des données du tiers d'après son id
            liste.add(0, tiersWrapper.getNSS());
            liste.add(1, tiersWrapper.getNom());
            liste.add(2, tiersWrapper.getPrenom());
            liste.add(3, rfOrdVerJointPrest.getMontantOvs());
            liste.add(4, rfOrdVerJointPrest.getIdPrestation());

        } else {
            throw new Exception(
                    "Erreur de chargement dans loadResult / RFXmlmlMappingListeRecapitulativePaiementsEtMutations - getDetailPrestationsPonctuelles");
        }

        return liste;
    }

    /**
     * Methode permettant de retourner une liste des tuples de prestations unique entre les tables RFPrestations et
     * RFOrdresVersements
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @param ponctuel
     * @param moyensAuxiliaire
     * @return
     * @throws Exception
     */
    private static Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> getListePrestationsPonctuelles(
            BSession session, String datePeriode, ArrayList<String> typePrestation, boolean ponctuel,
            boolean moyensAuxiliaire, boolean financementSoins, String typeSoinsComplementaire,
            String sousTypeSoinsComplementaire, boolean avancesSas) throws Exception {

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager rfDecJointPrestMgr = new RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager();
        rfDecJointPrestMgr.setSession(session);
        rfDecJointPrestMgr.setFinancementSoins(financementSoins);
        rfDecJointPrestMgr.setMoyenAuxiliaire(moyensAuxiliaire);
        rfDecJointPrestMgr.setTypeSoinsComplementaire(typeSoinsComplementaire);
        rfDecJointPrestMgr.setSousTypeSoinsComplementaire(sousTypeSoinsComplementaire);
        rfDecJointPrestMgr.setAvancesSas(avancesSas);
        rfDecJointPrestMgr.setPonctuel(ponctuel);
        rfDecJointPrestMgr.setDatePeriode(datePeriode);
        rfDecJointPrestMgr.setMutations(true);
        rfDecJointPrestMgr.setTypePrestation(typePrestation);
        rfDecJointPrestMgr.changeManagerSize(0);
        rfDecJointPrestMgr.find();

        return rfDecJointPrestMgr.iterator();
    }

    /**
     * Methode permettant de retourner la somme des montant diminués en fin d'année ou la somme des montants augmentés
     * en début d'annee.
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @param isDiminution
     * @return FWCurrency montantAdaptation
     * @throws Exception
     */
    private static FWCurrency getMontantAdaptationFinAnneeRecap(BSession session, String datePeriode,
            ArrayList<String> typePrestation, boolean isDiminution) throws Exception {

        FWCurrency montantAdaptation = new FWCurrency(0);

        // Si l'on veut la somme des diminution de fin d'année
        if (isDiminution) {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager rfAccordeeMgr = new RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager();
            rfAccordeeMgr.setSession(session);
            rfAccordeeMgr.setDatePeriode(datePeriode);
            rfAccordeeMgr.setIsDiminution(true);
            rfAccordeeMgr.setTypePrestation(typePrestation);
            rfAccordeeMgr.changeManagerSize(0);
            rfAccordeeMgr.find();

            Iterator rfPrestAcc = rfAccordeeMgr.iterator();
            while (rfPrestAcc.hasNext()) {
                RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAcc = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) rfPrestAcc
                        .next();

                montantAdaptation.add(rfPrestAccJointREPrestAcc.getMontant());
            }
        }
        // Sinon, la sommes des augmentations de l'année suivante
        else {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager rfAccordeeMgr = new RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager();
            rfAccordeeMgr.setSession(session);
            rfAccordeeMgr.setDatePeriode(datePeriode);
            rfAccordeeMgr.setIsAugmentationAdaptation(true);
            rfAccordeeMgr.setTypePrestation(typePrestation);
            rfAccordeeMgr.changeManagerSize(0);
            rfAccordeeMgr.find();

            Iterator rfPrestAcc = rfAccordeeMgr.iterator();
            while (rfPrestAcc.hasNext()) {
                RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAcc = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) rfPrestAcc
                        .next();

                montantAdaptation.add(rfPrestAccJointREPrestAcc.getMontant());
            }
        }

        return montantAdaptation;
    }

    /**
     * Methode qui permet de récupérer l'ensemble des montants faisants partie d'une augmentation futur pour le mois
     * suivant le mois donné
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantAugmentationMensuelCourantEtFuturRecap(BSession session, String datePeriode,
            ArrayList<String> typePrestation) throws Exception {
        // Déclaration de variables pour stocker les montants
        FWCurrency montantTotalAugmentationFutur = new FWCurrency(0);

        // Récupération de la liste des régimes en augmentations futur normal
        RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager listeRecapAugmentationFuturMensuel = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(session, datePeriode, typePrestation,
                        true, false, false, false, false, false, false);

        Iterator listeRecapAugmentationFuturMensuelItr = listeRecapAugmentationFuturMensuel.iterator();

        // Iteration de la liste des régimes en augmentations pour récupérer les montants de chaque tuple
        while (listeRecapAugmentationFuturMensuelItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAccAugFuturNormal = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) listeRecapAugmentationFuturMensuelItr
                    .next();
            montantTotalAugmentationFutur.add(rfPrestAccJointREPrestAccAugFuturNormal.getMontant());

            if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs.contains(typePrestation
                    .get(0))) {

                RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                        rfPrestAccJointREPrestAccAugFuturNormal.getIdTiers(),
                        rfPrestAccJointREPrestAccAugFuturNormal.getMontant(), "", "", "", "", "");
            } else {

                RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                        rfPrestAccJointREPrestAccAugFuturNormal.getIdTiers(), "", "", "",
                        rfPrestAccJointREPrestAccAugFuturNormal.getMontant(), "", "");
            }

        }

        // Récupération de la liste des régimes en augmentations courant
        RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager listeRecapAugmentationCourantMensuel = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(session, datePeriode, typePrestation,
                        false, true, false, false, false, false, false);

        Iterator listeRecapAugmentationCourantMensuelItr = listeRecapAugmentationCourantMensuel.iterator();

        // Iteration de la liste des régimes en augmentations pour récupérer les montants de chaque tuple
        while (listeRecapAugmentationCourantMensuelItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAccAugCourant = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) listeRecapAugmentationCourantMensuelItr
                    .next();
            montantTotalAugmentationFutur.add(rfPrestAccJointREPrestAccAugCourant.getMontant());

            if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs.contains(typePrestation
                    .get(0))) {

                RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                        rfPrestAccJointREPrestAccAugCourant.getIdTiers(),
                        rfPrestAccJointREPrestAccAugCourant.getMontant(), "", "", "", "", "");
            } else {

                RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                        rfPrestAccJointREPrestAccAugCourant.getIdTiers(), "", "", "",
                        rfPrestAccJointREPrestAccAugCourant.getMontant(), "", "");
            }
        }

        return montantTotalAugmentationFutur;
    }

    /**
     * Methode permettant d'obtenir les montants des moyens auxilaires
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantAvancesSas(BSession session, String datePeriode,
            ArrayList<String> typePrestation) throws Exception {

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager rfDecJointPrestMgr = new RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager();
        rfDecJointPrestMgr.setSession(session);
        rfDecJointPrestMgr.setAvancesSas(true);
        rfDecJointPrestMgr.setDatePeriode(datePeriode);
        rfDecJointPrestMgr.setTypePrestation(typePrestation);
        rfDecJointPrestMgr.changeManagerSize(0);
        rfDecJointPrestMgr.find();

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfDecJointPrest = (RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation) rfDecJointPrestMgr
                .getFirstEntity();

        if (null != rfDecJointPrest) {
            if (!JadeStringUtil.isEmpty(rfDecJointPrest.getMontantOvs())) {
                return new FWCurrency(rfDecJointPrest.getMontantOvs());
            } else {
                return new FWCurrency(0);
            }
        } else {
            throw new Exception(
                    "Erreur dans la récupération du montant total des avances SAS / getMontantAvancesSas - RFXmlmlMappingListeRecapitulativePaiementsEtMutations ");
        }
    }

    /**
     * Methode permettant d'obtenir le montant total des nouveaux paiements (diminution) dans un mois donnée
     * 
     * @param session
     * @param dateDebut
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantDiminutionMensuel(BSession session, String dateFin,
            ArrayList<String> typePrestation) throws Exception {

        // Déclaration de variables pour stocker les montants
        FWCurrency montantTotalDiminutionsMensuelles = new FWCurrency(0);

        // Récupération de la liste des régimes en augmentations courant
        boolean isAdaptation = false;
        if (dateFin.substring(0, 2).equals("12")) {
            isAdaptation = true;
        }
        RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager listeRecapDiminutionMensuel = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(session, dateFin, typePrestation,
                        false, false, false, false, true, false, isAdaptation);

        Iterator listeRecapDiminutionMensuelItr = listeRecapDiminutionMensuel.iterator();

        while (listeRecapDiminutionMensuelItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAccDimCourant = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) listeRecapDiminutionMensuelItr
                    .next();

            if (rfPrestAccJointREPrestAccDimCourant != null) {
                if (!JadeStringUtil.isEmpty(rfPrestAccJointREPrestAccDimCourant.getMontant())) {
                    montantTotalDiminutionsMensuelles.add(rfPrestAccJointREPrestAccDimCourant.getMontant());

                    if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs
                            .contains(typePrestation.get(0))) {

                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                                rfPrestAccJointREPrestAccDimCourant.getIdTiers(), "",
                                rfPrestAccJointREPrestAccDimCourant.getMontant(), "", "", "", "");
                    } else {

                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                                rfPrestAccJointREPrestAccDimCourant.getIdTiers(), "", "", "", "",
                                rfPrestAccJointREPrestAccDimCourant.getMontant(), "");
                    }

                } else {
                    RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                            rfPrestAccJointREPrestAccDimCourant.getIdTiers(), "", "", "", "", "", "");

                    return new FWCurrency(0);
                }
            } else {
                throw new Exception(
                        "Erreur dans la récupération du montant total des diminutions / getMontantDiminutionMensuel - RFXmlmlMappingListeRecapitulativePaiementsEtMutations ");
            }

        }
        return montantTotalDiminutionsMensuelles;

    }

    /**
     * Methode permettant d'obtenir les montants des moyens auxilaires
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantFinancementSoins(BSession session, String datePeriode,
            ArrayList<String> typePrestation) throws Exception {

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager rfDecJointPrestMgr = new RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager();
        rfDecJointPrestMgr.setSession(session);
        rfDecJointPrestMgr.setFinancementSoins(true);
        rfDecJointPrestMgr.setDatePeriode(datePeriode);
        rfDecJointPrestMgr.setTypePrestation(typePrestation);
        rfDecJointPrestMgr.changeManagerSize(0);
        rfDecJointPrestMgr.find();

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfDecJointPrest = (RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation) rfDecJointPrestMgr
                .getFirstEntity();

        if (null != rfDecJointPrest) {
            if (!JadeStringUtil.isEmpty(rfDecJointPrest.getMontantOvs())) {
                return new FWCurrency(rfDecJointPrest.getMontantOvs());
            } else {
                return new FWCurrency(0);
            }
        } else {
            throw new Exception(
                    "Erreur dans la récupération du montant total des financements de soins / getMontantFiancementSoins - RFXmlmlMappingListeRecapitulativePaiementsEtMutations ");
        }
    }

    /**
     * Methode permettant d'obtenir les montants mensuels futurs
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantFuturMoisPlusUnMensuel(BSession session, String datePeriode,
            ArrayList<String> typePrestation) throws Exception {

        RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestJointPrest = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(session, datePeriode, typePrestation,
                        false, false, true, false, false, true, false).getFirstEntity();

        if (null != rfPrestJointPrest) {
            if (!JadeStringUtil.isEmpty(rfPrestJointPrest.getMontant())) {
                return new FWCurrency(rfPrestJointPrest.getMontant());
            } else {
                return new FWCurrency(0);
            }
        } else {
            throw new Exception(
                    "Erreur dans la récupération du montant total des retroactifs / getMontantRetroMensuel - RFXmlmlMappingListeRecapitulativePaiementsEtMutations ");
        }

    }

    /**
     * Methode permettant d'obtenir les montants des moyens auxilaires
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantMoyenAuxiliaire(BSession session, String datePeriode,
            ArrayList<String> typePrestation) throws Exception {

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager rfDecJointPrestMgr = new RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager();
        rfDecJointPrestMgr.setSession(session);
        rfDecJointPrestMgr.setMoyenAuxiliaire(true);
        rfDecJointPrestMgr.setDatePeriode(datePeriode);
        rfDecJointPrestMgr.setTypePrestation(typePrestation);
        rfDecJointPrestMgr.changeManagerSize(0);
        rfDecJointPrestMgr.find();

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfDecJointPrest = (RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation) rfDecJointPrestMgr
                .getFirstEntity();

        if (null != rfDecJointPrest) {
            if (!JadeStringUtil.isEmpty(rfDecJointPrest.getMontantOvs())) {
                return new FWCurrency(rfDecJointPrest.getMontantOvs());
            } else {
                return new FWCurrency(0);
            }
        } else {
            throw new Exception(
                    "Erreur dans la récupération du montant total des moyens auxiliaires / getMontantMoyenAuxiliaire - RFXmlmlMappingListeRecapitulativePaiementsEtMutations ");
        }
    }

    /**
     * Methode permettant d'obtenir les montants ponctuels
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantPonctuel(BSession session, String datePeriode, ArrayList<String> typePrestation)
            throws Exception {

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager rfDecJointPrestMgr = new RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager();
        rfDecJointPrestMgr.setSession(session);
        rfDecJointPrestMgr.setPonctuel(true);
        rfDecJointPrestMgr.setDatePeriode(datePeriode);
        rfDecJointPrestMgr.setTypePrestation(typePrestation);
        rfDecJointPrestMgr.changeManagerSize(0);
        rfDecJointPrestMgr.find();

        RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfDecJointPrest = (RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation) rfDecJointPrestMgr
                .getFirstEntity();

        if (null != rfDecJointPrest) {
            if (!JadeStringUtil.isEmpty(rfDecJointPrest.getMontantOvs())) {
                return new FWCurrency(rfDecJointPrest.getMontantOvs());
            } else {
                return new FWCurrency(0);
            }
        } else {
            throw new Exception(
                    "Erreur dans la récupération du montant total des retroactifs / getMontantRetroMensuel - RFXmlmlMappingListeRecapitulativePaiementsEtMutations ");
        }
    }

    /**
     * Methode qui va récupérer l'ensemble des montants retroactif sur les prestations accordées purement retro et
     * courantes
     * 
     * @param session
     * @param datePeriode
     * @param typePrestation
     * @return
     * @throws Exception
     */
    private static FWCurrency getMontantRetroRecap(BSession session, String datePeriode,
            ArrayList<String> typePrestation) throws Exception {
        // Déclaration de variables pour stocker les montants
        BigDecimal montantTotalRetro = new BigDecimal("0");

        // Récupération de la liste des régimes purement retroactif
        RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager listeRecapRetroMensuel = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(session, datePeriode, typePrestation,
                        false, false, false, true, false, false, false);

        Iterator listeRecapRetroMensuelItr = listeRecapRetroMensuel.iterator();

        // Iteration de la liste des régimes purement rétroactif pour récupérer les montants de chaque tuple
        while (listeRecapRetroMensuelItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAccRetro = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) listeRecapRetroMensuelItr
                    .next();

            // Calcul du nombre de mois entre la date de début et la date de fin
            int nbMoisRetro = JadeDateUtil.getNbMonthsBetween(
                    "01." + rfPrestAccJointREPrestAccRetro.getDateDebutDroit(),
                    "01." + rfPrestAccJointREPrestAccRetro.getDateFinDroit()) + 1;

            // Multiplication du nombre de mois en retro par le montant mensuel
            BigDecimal montantRetroPureTotal = new BigDecimal(0);
            if (nbMoisRetro > 0) {
                montantRetroPureTotal = new BigDecimal(rfPrestAccJointREPrestAccRetro.getMontant())
                        .multiply(new BigDecimal(nbMoisRetro));
            } else {
                montantRetroPureTotal = new BigDecimal(rfPrestAccJointREPrestAccRetro.getMontant());
            }

            // Ajout du montant total du retro dans la variable des montants totaux purements retro
            montantTotalRetro = montantTotalRetro.add(montantRetroPureTotal);

            if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs.contains(typePrestation
                    .get(0))) {
                RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                        rfPrestAccJointREPrestAccRetro.getIdTiers(), "0", "0", montantRetroPureTotal.toString(), "0",
                        "0", "0");
            } else {

                RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                        rfPrestAccJointREPrestAccRetro.getIdTiers(), "0", "0", "", "0", "0",
                        montantRetroPureTotal.toString());
            }
        }

        // Récupération de la liste des régimes courant
        RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager listeRecapCourantMensuel = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(session, datePeriode, typePrestation,
                        false, true, false, false, false, false, false);

        Iterator listeRecapCourantMensuelItr = listeRecapCourantMensuel.iterator();

        // Iteration sur la liste des régimes courants pour récupérer les montants retroactif de chaque tuple
        while (listeRecapCourantMensuelItr.hasNext()) {
            RFPrestationAccordeeJointREPrestationAccordeeRecapitulation rfPrestAccJointREPrestAccCourant = (RFPrestationAccordeeJointREPrestationAccordeeRecapitulation) listeRecapCourantMensuelItr
                    .next();

            // Instanciation d'un calendart pour comparer la date de fin et la date d'augmentation
            JACalendar cal = new JACalendarGregorian();

            // Si il n'y a pas de date de fin ou compare si la date de fin est plus haute que la date d'augmentation
            if (JadeStringUtil.isBlankOrZero(rfPrestAccJointREPrestAccCourant.getDateFinDroit())
                    || (cal.compare(new JADate(rfPrestAccJointREPrestAccCourant.getDateFinDroit()), new JADate(
                            rfPrestAccJointREPrestAccCourant.getDateAugmentation())) == JACalendar.COMPARE_FIRSTUPPER)) {

                // Calcul du nombre de mois entre la date de début et la date d'augmentation
                int nbMoisRetro = JadeDateUtil.getNbMonthsBetween(
                        "01." + rfPrestAccJointREPrestAccCourant.getDateDebutDroit(),
                        rfPrestAccJointREPrestAccCourant.getDateAugmentation());

                // Multiplication du nombre de mois en retro par le montant mensuel
                BigDecimal montantRetroTotal = new BigDecimal(rfPrestAccJointREPrestAccCourant.getMontant())
                        .multiply(new BigDecimal(nbMoisRetro));

                // Ajout du montant total du retro dans la variable des montants totaux retro sur des prestations
                // courantes
                montantTotalRetro = montantTotalRetro.add(montantRetroTotal);

                if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs
                        .contains(typePrestation.get(0))) {

                    RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                            rfPrestAccJointREPrestAccCourant.getIdTiers(), "0", "0", montantRetroTotal.toString(), "0",
                            "0", "0");
                } else {

                    RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getNewDetailMutation(typePrestation,
                            rfPrestAccJointREPrestAccCourant.getIdTiers(), "", "", "", "", "",
                            montantRetroTotal.toString());
                }
            }

        }

        return new FWCurrency(montantTotalRetro.toString());
    }

    private static void getNewDetailMutation(ArrayList<String> typePrestation, String idTiers,
            String montantAugmentationAvs, String montantDiminutionAvs, String montantRetroAvs,
            String montantAugmentationAi, String montantDiminutionAi, String montantRetroAi) {

        MutationsMensuellesData newMutation = new MutationsMensuellesData(typePrestation, idTiers,
                montantAugmentationAvs, montantDiminutionAvs, montantRetroAvs, montantAugmentationAi,
                montantDiminutionAi, montantRetroAi);
        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeMutationAvs.add(newMutation);
    }

    private static RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager getPrestationAccordeeJointREPrestationAccordeeRecapitulationIter(
            BSession session, String datePeriode, ArrayList<String> typePrestation, boolean isAugmentation,
            boolean isCourant, boolean isFuturMensuel, boolean isRetro, boolean isDiminution, boolean isSum,
            boolean isAdaptation) throws Exception {

        RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager rfPrestAccJointREPrestAccMgr = new RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager();
        rfPrestAccJointREPrestAccMgr.setSession(session);
        rfPrestAccJointREPrestAccMgr.setDatePeriode(datePeriode);
        rfPrestAccJointREPrestAccMgr.setTypePrestation(typePrestation);
        rfPrestAccJointREPrestAccMgr.setIsAugmentation(isAugmentation);
        rfPrestAccJointREPrestAccMgr.setIsCourant(isCourant);
        rfPrestAccJointREPrestAccMgr.setIsDiminution(isDiminution);
        rfPrestAccJointREPrestAccMgr.setIsFuturMensuel(isFuturMensuel);
        rfPrestAccJointREPrestAccMgr.setIsRetro(isRetro);
        rfPrestAccJointREPrestAccMgr.setIsSum(isSum);
        rfPrestAccJointREPrestAccMgr.setDatePeriodeAAAAMM(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(datePeriode));
        rfPrestAccJointREPrestAccMgr.setIsAdaptation(isAdaptation);
        rfPrestAccJointREPrestAccMgr.changeManagerSize(0);
        rfPrestAccJointREPrestAccMgr.find();

        return rfPrestAccJointREPrestAccMgr;
    }

    private String datePeriode = null;

    ArrayList<ArrayList<String>> elementMutationTrier = new ArrayList<ArrayList<String>>();

    ArrayList<ArrayList<String>> listeTableauMutationMensuelleTrier = new ArrayList<ArrayList<String>>();

    String nssRequerantMutation = null;

    private BSession session = null;

    BigDecimal totalParRequerantMutation = new BigDecimal(0);

    public RFXmlmlMappingListeRecapitulativePaiementsEtMutations() {
        super();
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    public BSession getSession() {
        return session;
    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     * @param process
     */
    private void loadHeader(RFXmlmlContainer container, RFListeRecapitulativePaiementsProcess process) {

        // Textes de la liste de récapitulation des paiements
        String titreDocument = session.getLabel("EXCEL_TITRE_DOCUMENT_LISTE_RECAPITULATIVE_PAIEMENT");
        String texteDateImpression = session.getLabel("EXCEL_TEXTE_DATE_DOCUMENT_LISTE_RECAPITULATIVE_PAIEMENT");
        String textePeriode = session.getLabel("EXCEL_TEXTE_PERIODE_DATE_DOCUMENT_LISTE_RECAPITULATIVE_PAIEMENT");
        String texteCaisse = session.getLabel("EXCEL_TEXTE_CAISSE_DOCUMENT_LISTE_RECAPITULATIVE_PAIEMENT");
        String numero = session.getLabel("EXCEL_TITRE_RFM_DOCUMENT_NUMERO");
        String libellesRfm = session.getLabel("EXCEL_TITRE_RFM_DOCUMENT_LIBELLES");
        String texteAvs = session.getLabel("EXCEL_TITRE_AVS_DOCUMENT_LISTE_RECAPITULATIVE_PAIEMENT");
        String texteAi = session.getLabel("EXCEL_TITRE_AI_DOCUMENT_LISTE_RECAPITULATIVE_PAIEMENT");
        String titrePaiementsMensuels = session.getLabel("EXCEL_TITRE_PAIEMENTS_MENSUELS_RECAP");
        String texteCourant = session.getLabel("EXCEL_TEXTE_RFM_EN_COURS_RECAP");
        String texteAugmentation = session.getLabel("EXCEL_TEXTE_AUGMENTATION_RECAP");
        String texteAdaptationFinAnnee = session.getLabel("EXCEL_TEXTE_ADAPTATION_FIN_ANNEE_RECAP");
        String texteSousTotal = session.getLabel("EXCEL_TEXTE_SOUS_TOTAL_RECAP");
        String texteDiminution = session.getLabel("EXCEL_TEXTE_DIMINUTION_RECAP");
        String texteEnCoursFinRapport = session.getLabel("EXCEL_TEXTE_EN_COURS_FIN_RAPPORT_RECAP");
        String texteRetroactif = session.getLabel("EXCEL_TEXTE_RETROACTIF_RECAP");
        String texteTotalMensuel = session.getLabel("EXCEL_TEXTE_TOTAL_MENSUEL_RECAP");
        String titreFutur = session.getLabel("EXCEL_TITRE_FUTUR_RECAP");
        String texteEnCours = session.getLabel("EXCEL_TEXTE_EN_COURS_RECAP");
        String texteAugmentationFutur = session.getLabel("EXCEL_TEXTE_AUGMENTATION_FUTUR_RECAP");
        String texteTotalFutur = session.getLabel("EXCEL_TEXTE_TOTAL_FUTUR_RECAP");
        String titreCourant = session.getLabel("EXCEL_TITRE_COURANT_RECAP");
        String textePonctuel = session.getLabel("EXCEL_TEXTE_PONCTUEL_RECAP");
        String texteTotalMens = session.getLabel("EXCEL_TEXTE_TOTAL_MENS_RECAP");
        String texteTotalRfm = session.getLabel("EXCEL_TEXTE_TOTAL_RFM_RECAP");
        String texteMoyenAuxiliaire = session.getLabel("TEXTE_MOYEN_AUXILIAIRE_RECAP");
        String texteFinancementSoins = session.getLabel("EXCEL_TEXTE_FINANCEMENT_SOINS_RECAP");
        String texteAvancesSas = session.getLabel("EXCEL_TEXTE_AVANCES_SAS_RECAP");
        String texteAugmentationMoisPrecedent = session.getLabel("EXCEL_TEXTE_AUGMENTATION_MOIS_PRECEDENT");
        String texteSousTotalRente = session.getLabel("EXCEL_TEXTE_SOUS_TOTAL_RENTE");

        // Insertion des titres et textes de la récapitulation de paiements
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_DOCUMENT, titreDocument);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_DATE, texteDateImpression);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_PERIODE_DATE, textePeriode);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_CAISSE, texteCaisse);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_NUMERO, numero);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_RFM, libellesRfm);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_AVS, texteAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_AI, texteAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_PAIEMENTS_MENSUELS, titrePaiementsMensuels);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_COURANT, texteCourant);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_AUGMENTATION, texteAugmentation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_ADAPTATION_FIN_ANNEE, texteAdaptationFinAnnee
                + JadeDateUtil.addYears("01.01." + datePeriode.substring(3), 1));
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_SOSUS_TOTAL, texteSousTotal);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_DIMINUTION, texteDiminution);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_EN_COURS_RAPPORT, texteEnCoursFinRapport);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_RETROACTIF, texteRetroactif);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_TOTAL_MENSUEL, texteTotalMensuel);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_FUTUR, titreFutur);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_EN_COURS, texteEnCours);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_AUGMENTATION_FUTUR, texteAugmentationFutur
                + " " + JadeDateUtil.addMonths("01." + datePeriode, 1).substring(3));
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_TOTAL_FUTUR, texteTotalFutur);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TITRE_COURANT, titreCourant);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_PONCTUEL, textePonctuel);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_TOTAL_MENS, texteTotalMens);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_TOTAL_RFM, texteTotalRfm);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_MOYEN_AUXILIAIRE, texteMoyenAuxiliaire);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_FINANCEMENT_SOINS, texteFinancementSoins);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_AVANCES_SAS, texteAvancesSas);

        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_AUGMENTATION_MOIS_PRECEDENT,
                texteAugmentationMoisPrecedent);
        container.put(IRFListeRecapitulationPaiementsListeColumns.A_TEXTE_SOUS_TOTAL_RENTE, texteSousTotalRente);

        // Textes de la liste des mutations mensuelles AVS
        String titreListeMutationMensuelleAvs = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_MENSUELLE_AVS");
        String texteNssMutationMensuelleAvs = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationMensuelleAvs = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationMensuelleAvs = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantAugmentationMutationMensuelleAvs = session
                .getLabel("EXCEL_TEXTE_MONTANT_AUGMENTATION_MUTATION");
        String texteMontantDiminutionMutationMensuelleAvs = session.getLabel("EXCEL_TEXTE_MONTANT_DIMINUTION_MUTATION");
        String texteMontantPonctuelMutationMensuelleAvs = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteTotalMensuelAvs = session.getLabel("EXCEL_TITRE_TOTAL_MENSUELLE_AVS");
        String texteTotalAugmentationMensAvs = session.getLabel("EXCEL_TEXTE_TOTAL_AUGMENT_MENS_AVS");
        String texteTotalDiminutionMensAvs = session.getLabel("EXCEL_TEXTE_TOTAL_DIMINUT_MENS_AVS");
        String texteTotalRetroMensAvs = session.getLabel("EXCEL_TEXTE_TOTAL_RETRO_MENS_AVS");

        // Insertion des titres et des textes dans la liste des mutations mensuelles AVS
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_LISTE_MUTATION_MENSUELLE_AVS,
                titreListeMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_NSS_MUTATION_MENSUELLE_AVS,
                texteNssMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_NOM_TIERS_MUTATION_MENSUELLE_AVS,
                texteNomTiersMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_PRENOM_TIERS_MUTATION_MENSUELLE_AVS,
                textePrenomTiersMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_MONTANT_AUGMENT_MUTATION_MENSUELLE_AVS,
                texteMontantAugmentationMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_MONTANT_DIM_MUTATION_MENSUELLE_AVS,
                texteMontantDiminutionMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_MONTANT_PONCTUEL_MUTATION_MENSUELLE_AVS,
                texteMontantPonctuelMutationMensuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_TOTAL_MUTATION_MENSUELLE_AVS,
                texteTotalMensuelAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_TOTAL_AUGMENTATION_MUTATION_MENSUELLE_AVS,
                texteTotalAugmentationMensAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_TOTAL_DIMINUTION_MUTATION_MENSUELLE_AVS,
                texteTotalDiminutionMensAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.C_TITRE_TOTAL_RETRO_MUTATION_MENSUELLE_AVS,
                texteTotalRetroMensAvs);

        // Textes de la liste des mutation ponctuelles AVS
        String titreListeMutationPonctuelleAvs = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_PONCTUELLE_AVS");
        String texteNssMutationPonctuelleAvs = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationPonctuelleAvs = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationPonctuelleAvs = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantPonctuelleAvs = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerant = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PAR_REQUERANT_PONCTUEL_MUTATION_AVS");
        String texteMontantTotal = session.getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");
        // Insertion des titres et des textes dans la liste des mutations ponctuelles AVS
        container.put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_LISTE_MUTATION_PONCTUELLE_AVS,
                titreListeMutationPonctuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_NSS_MUTATION_PONCTUELLE_AVS,
                texteNssMutationPonctuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_NOM_TIERS_MUTATION_PONCTUELLE_AVS,
                texteNomTiersMutationPonctuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_PRENOM_TIERS_MUTATION_PONCTUELLE_AVS,
                textePrenomTiersMutationPonctuelleAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_MONTANT_MUTATION_PONCTUELLE_AVS,
                texteMontantPonctuelleAvs);
        container
                .put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_PONCTUELLE_AVS,
                        texteMontantTotalParRequerant);
        container.put(IRFListeRecapitulationPaiementsListeColumns.E_TITRE_MONTANT_TOTAL_MUTATION_PONCTUELLE_AVS,
                texteMontantTotal);

        // Textes de la liste des mutations de moyens auxiliaires AVS
        String titreListeMutationMoyensAuxAvs = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_MOYENS_AUX_AVS");
        String texteNssMutationMoyensAuxAvs = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationMoyensAuxAvs = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationMoyensAuxAvs = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantMoyensAuxAvs = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantMoyensAuxAvs = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");
        String texteMontantTotalMoyensAuxAvs = session.getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");
        // Insertion des titres et des textes dans la liste des mutations de moyens auxiliaires AVS
        container.put(IRFListeRecapitulationPaiementsListeColumns.G_TITRE_LISTE_MUTATION_MOYENS_AUX_AVS,
                titreListeMutationMoyensAuxAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.G_NSS_MUTATION_MOYENS_AUX_AVS,
                texteNssMutationMoyensAuxAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.G_NOM_TIERS_MUTATION_MOYENS_AUX_AVS,
                texteNomTiersMutationMoyensAuxAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.G_PRENOM_TIERS_MUTATION_MOYENS_AUX_AVS,
                textePrenomTiersMutationMoyensAuxAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.G_MONTANT_MUTATION_MOYENS_AUX_AVS,
                texteMontantMoyensAuxAvs);
        container.put(
                IRFListeRecapitulationPaiementsListeColumns.G_MONTANT_TOTAL_MUTATION_PAR_REQUERANT_MOYENS_AUX_AVS,
                texteMontantTotalParRequerantMoyensAuxAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.G_MONTANT_TOTAL_MUTATION_MOYENS_AUX_AVS,
                texteMontantTotalMoyensAuxAvs);

        // Textes de la liste des mutations mensuelles AI
        String titreListeMutationMensuelleAi = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_MENSUELLE_AI");
        String texteNssMutationMensuelleAi = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationMensuelleAi = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationMensuelleAi = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantAugmentationMutationMensuelleAi = session
                .getLabel("EXCEL_TEXTE_MONTANT_AUGMENTATION_MUTATION");
        String texteMontantDiminutionMutationMensuelleAi = session.getLabel("EXCEL_TEXTE_MONTANT_DIMINUTION_MUTATION");
        String texteMontantPonctuelMutationMensuelleAi = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalMensAi = session.getLabel("EXCEL_TITRE_TOTAL_MENSUELLE_AI");
        String texteMontantTotalAugmentationMensAi = session.getLabel("EXCEL_TEXTE_TOTAL_AUGMENT_MENS_AI");
        String texteMontantTotalDiminutionMensAi = session.getLabel("EXCEL_TEXTE_TOTAL_DIMINUT_MENS_AI");
        String texteMontantTotalRetroMensAi = session.getLabel("EXCEL_TEXTE_TOTAL_RETRO_MENS_AI");

        // Insertion des titres et textes dans la liste des mutations mensuelles AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_LISTE_MUTATION_MENSUELLE_AI,
                titreListeMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_NSS_MUTATION_MENSUELLE_AI,
                texteNssMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_NOM_TIERS_MUTATION_MENSUELLE_AI,
                texteNomTiersMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_PRENOM_TIERS_MUTATION_MENSUELLE_AI,
                textePrenomTiersMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_MONTANT_AUGMENT_MUTATION_MENSUELLE_AI,
                texteMontantAugmentationMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_MONTANT_DIM_MUTATION_MENSUELLE_AI,
                texteMontantDiminutionMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TITRE_MONTANT_PONCTUEL_MUTATION_MENSUELLE_AI,
                texteMontantPonctuelMutationMensuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TEXTE_TOTAL_MENSUELLE_AI, texteMontantTotalMensAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TEXTE_TOTAL_AUGMENTATION_MENSUELLE_AI,
                texteMontantTotalAugmentationMensAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TEXTE_TOTAL_DIMINUTION_MENSUELLE_AI,
                texteMontantTotalDiminutionMensAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.I_TEXTE_TOTAL_RETRO_MENSUELLE_AI,
                texteMontantTotalRetroMensAi);

        // Textes de la liste des mutation ponctuelles AI
        String titreListeMutationPonctuelleAi = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_PONCTUELLE_AI");
        String texteNssMutationPonctuelleAi = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationPonctuelleAi = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationPonctuelleAi = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantPonctuelleAi = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantPonctuelleAi = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        String texteMontantTotalPonctuelleAi = session.getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        // Insertion des titres et des textes dans la liste des mutations ponctuelles AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.K_TITRE_LISTE_MUTATION_PONCTUELLE_AI,
                titreListeMutationPonctuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.K_TITRE_NSS_MUTATION_PONCTUELLE_AI,
                texteNssMutationPonctuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.K_TITRE_NOM_TIERS_MUTATION_PONCTUELLE_AI,
                texteNomTiersMutationPonctuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.K_TITRE_PRENOM_TIERS_MUTATION_PONCTUELLE_AI,
                textePrenomTiersMutationPonctuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.K_TITRE_MONTANT_MUTATION_PONCTUELLE_AI,
                texteMontantPonctuelleAi);
        container.put(
                IRFListeRecapitulationPaiementsListeColumns.K_TITRE_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_PONCTUELLE_AI,
                texteMontantTotalParRequerantPonctuelleAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.K_TITRE_MONTANT_TOTAL_MUTATION_PONCTUELLE_AI,
                texteMontantTotalPonctuelleAi);

        // Textes de la liste des mutations de moyens auxiliaires AI
        String titreListeMutationMoyensAuxAi = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_MOYENS_AUX_AI");
        String texteNssMutationMoyensAuxAi = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationMoyensAuxAi = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationMoyensAuxAi = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantMoyensAuxAi = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantMoyensAuxAi = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        String texteTotalMoyensAuxAi = session.getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        // Insertion des titres et des textes dans la liste des mutations de moyens auxiliaires AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TITRE_TITRE_LISTE_MUTATION_MOYENS_AUX_AI,
                titreListeMutationMoyensAuxAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TITRE_NSS_MUTATION_MOYENS_AUX_AI,
                texteNssMutationMoyensAuxAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TITRE_NOM_TIERS_MUTATION_MOYENS_AUX_AI,
                texteNomTiersMutationMoyensAuxAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TITRE_PRENOM_TIERS_MUTATION_MOYENS_AUX_AI,
                textePrenomTiersMutationMoyensAuxAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TITRE_MONTANT_MUTATION_MOYENS_AUX_AI,
                texteMontantMoyensAuxAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TITRE_MONTANT_TOTAL_MUTATION_MOYENS_AUX_AI,
                texteMontantTotalParRequerantMoyensAuxAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.M_TEXTE_TOTAL_MOYENS_AUX_AI, texteTotalMoyensAuxAi);

        // Textes de la liste des mutations de financement des soins AVS
        String titreListeMutationFinanceSoinsAvs = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_FINANCE_SOINS_AVS");
        String texteNssMutationFinanceSoinsAvs = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationFinanceSoinsAvs = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationFinanceSoinsAvs = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantFinanceSoinsAvs = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantFinanceSoinsAvs = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");
        String texteMontantTotalFinanceSoinsAvs = session.getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");
        // Insertion des titres et des textes dans la liste des mutations de moyens auxiliaires AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_LISTE_MUTATION_FINANCE_SOINS_AVS,
                titreListeMutationFinanceSoinsAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_NSS_MUTATION_FINANCE_SOINS_AVS,
                texteNssMutationFinanceSoinsAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_NOM_MUTATION_FINANCE_SOINS_AVS,
                texteNomTiersMutationFinanceSoinsAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_PRENOM_MUTATION_FINANCE_SOINS_AVS,
                textePrenomTiersMutationFinanceSoinsAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_MONTANT_MUTATION_FINANCE_SOINS_AVS,
                texteMontantFinanceSoinsAvs);
        container
                .put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_FINANCE_SOINS_AVS,
                        texteMontantTotalParRequerantFinanceSoinsAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.O_TITRE_MONTANT_TOTAL_MUTATION_FINANCE_SOINS_AVS,
                texteMontantTotalFinanceSoinsAvs);

        // Textes de la liste des mutations de financement des soins AI
        String titreListeMutationFinanceSoinsAi = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_FINANCE_SOINS_AI");
        String texteNssMutationFinanceSoinsAi = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationFinanceSoinsAi = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationFinanceSoinsAi = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantFinanceSoinsAi = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantFinanceSoinsAi = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        String texteMontantTotalFinanceSoinsAi = getSession()
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        // Insertion des titres et des textes dans la liste des mutations de moyens auxiliaires AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_LISTE_MUTATION_FINANCE_SOINS_AI,
                titreListeMutationFinanceSoinsAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_NSS_MUTATION_FINANCE_SOINS_AI,
                texteNssMutationFinanceSoinsAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_NOM_MUTATION_FINANCE_SOINS_AI,
                texteNomTiersMutationFinanceSoinsAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_PRENOM_MUTATION_FINANCE_SOINS_AI,
                textePrenomTiersMutationFinanceSoinsAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_MONTANT_MUTATION_FINANCE_SOINS_AI,
                texteMontantFinanceSoinsAi);
        container
                .put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_FINANCE_SOINS_AI,
                        texteMontantTotalParRequerantFinanceSoinsAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.Q_TITRE_MONTANT_TOTAL_MUTATION_FINANCE_SOINS_AI,
                texteMontantTotalFinanceSoinsAi);

        // Textes de la liste des mutations des avances SAS AVS
        String titreListeMutationAvancesSasAvs = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_AVANCES_SAS_AVS");
        String texteNssMutationAvancesSasAvs = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationAvancesSasAvs = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationAvancesSasAvs = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantAvancesSasAvs = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantAvancesSasAvs = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");
        String texteMontantTotalAvancesSasAvs = session.getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AVS");

        // Insertion des titres et des textes dans la liste des mutations des avances SAS AVS
        container.put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_LISTE_MUTATION_AVANCES_SAS_AVS,
                titreListeMutationAvancesSasAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_NSS_MUTATION_AVANCES_SAS_AVS,
                texteNssMutationAvancesSasAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_NOM_MUTATION_AVANCES_SAS_AVS,
                texteNomTiersMutationAvancesSasAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_PRENOM_MUTATION_AVANCES_SAS_AVS,
                textePrenomTiersMutationAvancesSasAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_MONTANT_MUTATION_AVANCES_SAS_AVS,
                texteMontantAvancesSasAvs);
        container
                .put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_AVANCES_SAS_AVS,
                        texteMontantTotalParRequerantAvancesSasAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.U_TITRE_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AVS,
                texteMontantTotalAvancesSasAvs);

        // Textes de la liste des mutations des avances SAS AI
        String titreListeMutationAvancesSasAi = session.getLabel("EXCEL_TITRE_LISTE_MUTATION_AVANCES_SAS_AI");
        String texteNssMutationAvancesSasAi = session.getLabel("EXCEL_TEXTE_NSS_MUTATION");
        String texteNomTiersMutationAvancesSasAi = session.getLabel("EXCEL_TEXTE_NOM_TIERS_MUTATION");
        String textePrenomTiersMutationAvancesSasAi = session.getLabel("EXCEL_TEXTE_PRENOM_TIERS_MUTATION");
        String texteMontantAvancesSasAi = session.getLabel("EXCEL_TEXTE_MONTANT_PONCTUEL_MUTATION");
        String texteMontantTotalParRequerantAvancesSasAi = session
                .getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");
        String texteMontantTotalAvancesSasAi = getSession().getLabel("EXCEL_TEXTE_MONTANT_TOTAL_PONCTUEL_MUTATION_AI");

        // Insertion des titres et des textes dans la liste des mutations des avances SAS AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_LISTE_MUTATION_AVANCES_SAS_AI,
                titreListeMutationAvancesSasAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_NSS_MUTATION_AVANCES_SAS_AI,
                texteNssMutationAvancesSasAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_NOM_MUTATION_AVANCES_SAS_AI,
                texteNomTiersMutationAvancesSasAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_PRENOM_MUTATION_AVANCES_SAS_AI,
                textePrenomTiersMutationAvancesSasAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_MONTANT_MUTATION_AVANCES_SAS_AI,
                texteMontantAvancesSasAi);
        container
                .put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_AVANCES_SAS_AI,
                        texteMontantTotalParRequerantAvancesSasAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.W_TITRE_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AI,
                texteMontantTotalAvancesSasAi);

        // Textes de la liste des adaptation annuelles AVS/AI
        String titreAdaptationAvsAi = session.getLabel("EXCEL_TITRE_LISTE_ADAPTATION_ANNUELLE_AVS");

        String titreNssAdaptation = session.getLabel("EXCEL_TITRE_NSS_LISTE_ADAPTATION_ANNUELLE");
        String titreNomAdaptation = session.getLabel("EXCEL_TITRE_NOM_LISTE_ADAPTATION_ANNUELLE");
        String titreSourceAdaptation = session.getLabel("EXCEL_TITRE_SOURCE_LISTE_ADAPTATION_ANNUELLE");
        String titrePrenomAdaptation = session.getLabel("EXCEL_TITRE_PRENOM_LISTE_ADAPTATION_ANNUELLE");
        String titreDiminutionAdaptation = session.getLabel("EXCEL_TITRE_DIMINUTION_LISTE_ADAPTATION_ANNUELLE");
        String titreAugmentationAdaptation = session.getLabel("EXCEL_TITRE_AUGMENTATION_LISTE_ADAPTATION_ANNUELLE");
        String titreEcartAdaptation = session.getLabel("EXCEL_TITRE_ECART_LISTE_ADAPTATION_ANNUELLE");
        String titreMontantTotauxAvsAi = session.getLabel("EXCEL_TITRE_MONTANTS_TOTAUX_ADAPTATION_ANNUELLE_AVS_AI");
        String titreMontantTotauxAvs = session.getLabel("EXCEL_TITRE_MONTANTS_TOTAUX_ADAPTATION_ANNUELLE_AVS");
        String titreMontantTotauxAi = session.getLabel("EXCEL_TITRE_MONTANTS_TOTAUX_ADAPTATION_ANNUELLE_AI");
        String titreTypeAugmentation = session.getLabel("EXCEL_TITRE_TYPE_AUGMENTATION_LISTE_ADAPTATION_ANNUELLE");
        String titreTypeDiminution = session.getLabel("EXCEL_TITRE_TYPE_DIMINUTION_LISTE_ADAPTATION_ANNUELLE");

        // Insertion des titres et des textes dans la liste des adaptations annuelle AVS / AI
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_ADAPTATION_AVS_AI, titreAdaptationAvsAi);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_NSS_ADAPTATION_AVS_AI, titreNssAdaptation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_NOM_ADAPTATION_AVS_AI, titreNomAdaptation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_PRENOM_ADAPTATION_AVS_AI,
                titrePrenomAdaptation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_DIMINUTION_ADAPTATION_AVS_AI,
                titreDiminutionAdaptation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_AUGMENTATION_ADAPTATION_AVS_AI,
                titreAugmentationAdaptation);
        container
                .put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_ECART_ADAPTATION_AVS_AI, titreEcartAdaptation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_SOURCE_ADAPTATION_AVS_AI,
                titreSourceAdaptation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_MONTANT_TOTAUX_AVS_AI,
                titreMontantTotauxAvsAi);

        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_MONTANT_TOTAUX_AVS, titreMontantTotauxAvs);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_MONTANT_TOTAUX_AI, titreMontantTotauxAi);

        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_TYPE_AUGMENTATION_ADAPTATION_AVS_AI,
                titreTypeAugmentation);
        container.put(IRFListeRecapitulationPaiementsListeColumns.S_TITRE_TYPE_DIMINUTION_ADAPTATION_AVS_AI,
                titreTypeDiminution);

    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return container RFXmlmlContainer
     * @throws RFXmlmlException
     * @throws Exception
     */
    public RFXmlmlContainer loadResults(RFListeRecapitulativePaiementsProcess process) throws RFXmlmlException,
            Exception {

        RFXmlmlContainer container = new RFXmlmlContainer();
        RFListeRecapitulativePaiementsService rfListeRecapService = new RFListeRecapitulativePaiementsService();

        loadHeader(container, process);

        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi.clear();// = new
                                                                                            // ArrayList<String>();
        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs.clear();// = new
                                                                                             // ArrayList<String>();
        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeMutationAvs = new ArrayList<MutationsMensuellesData>();
        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs
                .add(IPCPCAccordee.CS_TYPE_PC_VIELLESSE);
        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs
                .add(IPCPCAccordee.CS_TYPE_PC_SURVIVANT);
        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi
                .add(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);

        // RECAPITULATION AVS-AI
        // =====================
        RecapitulationPcRfm recap = PrestationCommonServiceLocator.getRecapitulationPcRfmService().findInfoRecapByDate(
                datePeriode);

        FWCurrency montantTotalAvs = new FWCurrency("0.00");
        FWCurrency montantTotalAi = new FWCurrency("0.00");

        if (recap != null) {

            // Récupération des montants
            montantTotalAvs = recap.getMontantTotalRFMAVS();
            montantTotalAi = recap.getMontantTotalRFMAI();

        }
        // I130321_000002
        // La correction de l'incident ci dessous, est fausse. Il faut utiliser le montant repris des rentes sans faire
        // de déduction des augmentations présentent.
        // ============================================================================================================
        // I121026_000025
        // Le montant récupéré dans la table des rentes, comprends l'augmentation du mois suivant. Il faut donc
        // sustraire ce montant pour être juste avec les RFM en cours.
        // Soustraction de l'augmentation du mois passé

        // Retrait des augmentations comprises dans le dernier paiement mensuel.
        String dateMoisPrecedent = JadeDateUtil.addMonths("01." + datePeriode, -1);
        dateMoisPrecedent = dateMoisPrecedent.substring(3);
        FWCurrency augmentationMoisPrecedentAVS = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantFuturMoisPlusUnMensuel(session, dateMoisPrecedent,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency augmentationMoisPrecedentAI = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantFuturMoisPlusUnMensuel(session, dateMoisPrecedent,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        // Soustraction des augmentations comprises dans le dernier paiement mensuel
        montantTotalAvs.sub(augmentationMoisPrecedentAVS);
        montantTotalAi.sub(augmentationMoisPrecedentAI);

        // Montant totaux du dernier paiement mensuel (augmentations comprises)
        FWCurrency sousTotalRenteAvs = new FWCurrency("0.00");
        sousTotalRenteAvs.add(augmentationMoisPrecedentAVS);
        sousTotalRenteAvs.add(montantTotalAvs);

        FWCurrency sousTotalRenteAi = new FWCurrency("0.00");
        sousTotalRenteAi.add(augmentationMoisPrecedentAI);
        sousTotalRenteAi.add(montantTotalAi);

        // Récupération des montants d'augmentation dans le mois du rapport
        FWCurrency montantAugmentationMensuelleAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantAugmentationMensuelCourantEtFuturRecap(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency montantAugmentationMensuelleAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantAugmentationMensuelCourantEtFuturRecap(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        // Récupération des montants d'augmentation au 01.01 de l'année suivante
        // Calcul de tous les montants de prestations accordées en diminution au 01.12 de l'année en cours
        String dateFinAnnee = "01." + "12." + datePeriode.substring(3);
        // Calcul de tous les montants de prestations accordées en augmentation au 01.01 de l'année suivante
        Integer anneeDatePeriodeSuivante = new Integer(datePeriode.substring(3)) + 1;
        String dateDebutAnnee = "01.01." + anneeDatePeriodeSuivante.toString();

        // ADAPTATION ANNUELLE AVS/AI
        // Création d'une liste de toutes les nouvelles prestations accordées
        List<String[]> rfPrestAccJoinRePrestAccAvsAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getAdaptationAnnuelle(session, dateFinAnnee, dateDebutAnnee);

        FWCurrency montantTotalDiminutionAvsAi = new FWCurrency(0);
        FWCurrency montantTotalAugmentationAvsAi = new FWCurrency(0);
        FWCurrency montantTotalEcartAvsAi = new FWCurrency(0);
        FWCurrency montantTotalDiminutionAvs = new FWCurrency(0);
        FWCurrency montantTotalAugmentationAvs = new FWCurrency(0);
        FWCurrency montantTotalDiminutionAi = new FWCurrency(0);
        FWCurrency montantTotalAugmentationAi = new FWCurrency(0);
        FWCurrency montantEcartAvsAi = new FWCurrency(0);

        String nssTiersAdaptAnnuelAvsAi = "";
        String nomTiersAdaptAnnuelAvsAi = "";
        String prenomTiersAdaptAnnuelAvsAi = "";
        String montantDiminutionAdaptAnnuelAvsAi = "";
        String montantAugmentationAdaptAnnuelAvsAi = "";
        String sourcePrestationAdaptAnnuelAvsAi = "";
        String typePrestationDiminutionAdaptAnnuelAvsAi = "";
        String typePrestationAugmentationAdaptAnnuelAvsAi = "";

        // Si liste non vide, on parcour chaque élément pour remplir le modèle
        if ((rfPrestAccJoinRePrestAccAvsAi.size() > 0) && (rfPrestAccJoinRePrestAccAvsAi != null)) {
            for (String[] elementTupleTabStr : rfPrestAccJoinRePrestAccAvsAi) {

                // Calcul de l'écart entre le montant de l'augmentation - le montant de la diminution
                montantEcartAvsAi = new FWCurrency(0);
                if (JadeStringUtil.isEmpty(elementTupleTabStr[4].toString())) {
                    montantEcartAvsAi.add(0);
                } else {
                    montantEcartAvsAi.add(Double.valueOf(elementTupleTabStr[4].toString()));
                }

                if (JadeStringUtil.isEmpty(elementTupleTabStr[3].toString())) {
                    montantEcartAvsAi.sub(0);
                } else {
                    montantEcartAvsAi.sub(Double.valueOf(elementTupleTabStr[3].toString()));
                }

                nssTiersAdaptAnnuelAvsAi = elementTupleTabStr[0];
                nomTiersAdaptAnnuelAvsAi = elementTupleTabStr[1];
                prenomTiersAdaptAnnuelAvsAi = elementTupleTabStr[2];
                montantDiminutionAdaptAnnuelAvsAi = elementTupleTabStr[3];
                montantAugmentationAdaptAnnuelAvsAi = elementTupleTabStr[4];
                sourcePrestationAdaptAnnuelAvsAi = elementTupleTabStr[5];
                typePrestationDiminutionAdaptAnnuelAvsAi = elementTupleTabStr[6];
                typePrestationAugmentationAdaptAnnuelAvsAi = elementTupleTabStr[7];

                if (JadeStringUtil.isEmpty(montantDiminutionAdaptAnnuelAvsAi)) {
                    montantDiminutionAdaptAnnuelAvsAi = "0";
                }
                if (JadeStringUtil.isEmpty(montantAugmentationAdaptAnnuelAvsAi)) {
                    montantAugmentationAdaptAnnuelAvsAi = "0";
                }

                // Remplissage des champs
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_NSS_ADAPTATION_AVS_AI,
                        nssTiersAdaptAnnuelAvsAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_NOM_ADAPTATION_AVS_AI,
                        nomTiersAdaptAnnuelAvsAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_PRENOM_ADAPTATION_AVS_AI,
                        prenomTiersAdaptAnnuelAvsAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_SOURCE_ADATATPION_AVS_AI,
                        getSession().getCodeLibelle(sourcePrestationAdaptAnnuelAvsAi));
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_DIMINUTION_ADAPTATION_AVS_AI,
                        montantDiminutionAdaptAnnuelAvsAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_AUGMENTATION_ADAPTATION_AVS_AI,
                        montantAugmentationAdaptAnnuelAvsAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_ECART_ADAPTATION_AVS_AI,
                        montantEcartAvsAi.toStringFormat());

                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TYPE_AUGMENTATION_AVS_AI,
                        getSession().getCodeLibelle(typePrestationAugmentationAdaptAnnuelAvsAi));
                container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TYPE_DIMINUTION_AVS_AI, getSession()
                        .getCodeLibelle(typePrestationDiminutionAdaptAnnuelAvsAi));

                // Ajout de chaque montant au variables total
                montantTotalDiminutionAvsAi.add(Double.valueOf(montantDiminutionAdaptAnnuelAvsAi));
                montantTotalAugmentationAvsAi.add(Double.valueOf(montantAugmentationAdaptAnnuelAvsAi));
                montantTotalEcartAvsAi.add(Double.valueOf(montantEcartAvsAi.toStringFormat()));

                if (typePrestationDiminutionAdaptAnnuelAvsAi.equals(IPCPCAccordee.CS_TYPE_PC_VIELLESSE)
                        || typePrestationDiminutionAdaptAnnuelAvsAi.equals(IPCPCAccordee.CS_TYPE_PC_SURVIVANT)) {
                    montantTotalDiminutionAvs.add(Double.valueOf(montantDiminutionAdaptAnnuelAvsAi));
                } else if (typePrestationDiminutionAdaptAnnuelAvsAi.equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)) {
                    montantTotalDiminutionAi.add(Double.valueOf(montantDiminutionAdaptAnnuelAvsAi));
                } else {
                    /*
                     * throw new Exception(
                     * "RFXmlmlMappingListeRecapitulativePaiementsEtMutations.loadResults():Impossible de détérminer le type de RFM Accordées"
                     * );
                     */
                }

                if (typePrestationAugmentationAdaptAnnuelAvsAi.equals(IPCPCAccordee.CS_TYPE_PC_VIELLESSE)
                        || typePrestationAugmentationAdaptAnnuelAvsAi.equals(IPCPCAccordee.CS_TYPE_PC_SURVIVANT)) {
                    montantTotalAugmentationAvs.add(Double.valueOf(montantAugmentationAdaptAnnuelAvsAi));
                } else if (typePrestationAugmentationAdaptAnnuelAvsAi.equals(IPCPCAccordee.CS_TYPE_PC_INVALIDITE)) {
                    montantTotalAugmentationAi.add(Double.valueOf(montantAugmentationAdaptAnnuelAvsAi));
                } else {
                    /*
                     * throw new Exception(
                     * "RFXmlmlMappingListeRecapitulativePaiementsEtMutations.loadResults():Impossible de détérminer le type de RFM Accordées"
                     * );
                     */
                }
            }

        }
        // Sinon, on charge une fois chaque variable du modèle pour avoir une ligne vide
        else {
            // Remplissage des champs vide
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_NSS_ADAPTATION_AVS_AI,
                    nssTiersAdaptAnnuelAvsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_NOM_ADAPTATION_AVS_AI,
                    nomTiersAdaptAnnuelAvsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_PRENOM_ADAPTATION_AVS_AI,
                    prenomTiersAdaptAnnuelAvsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_SOURCE_ADATATPION_AVS_AI, getSession()
                    .getCodeLibelle(sourcePrestationAdaptAnnuelAvsAi));
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_DIMINUTION_ADAPTATION_AVS_AI,
                    montantDiminutionAdaptAnnuelAvsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_AUGMENTATION_ADAPTATION_AVS_AI,
                    montantAugmentationAdaptAnnuelAvsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_ECART_ADAPTATION_AVS_AI,
                    montantEcartAvsAi.toStringFormat());

            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TYPE_AUGMENTATION_AVS_AI, getSession()
                    .getCodeLibelle(typePrestationAugmentationAdaptAnnuelAvsAi));
            container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TYPE_DIMINUTION_AVS_AI, getSession()
                    .getCodeLibelle(typePrestationDiminutionAdaptAnnuelAvsAi));

        }

        // Calcul de l'écart entre le montant total de l'augmentation AVS - le montant total de la diminution AVS
        FWCurrency montantTotalEcartAvs = new FWCurrency(0);
        montantTotalEcartAvs.add(montantTotalAugmentationAvs);
        montantTotalEcartAvs.sub(montantTotalDiminutionAvs);
        // Calcul de l'écart entre le montant total de l'augmentation AI - le montant total de la diminution AI
        FWCurrency montantTotalEcartAi = new FWCurrency(0);
        montantTotalEcartAi.add(montantTotalAugmentationAi);
        montantTotalEcartAi.sub(montantTotalDiminutionAi);

        // Insertion des montants totaux adaptation-AVS en bas de liste
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_DIMINUTION_AVS_AI,
                montantTotalDiminutionAvsAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_AUGMENTATION_AVS_AI,
                montantTotalAugmentationAvsAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_ECART_AVS_AI,
                montantTotalEcartAvsAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_DIMINUTION_AVS,
                montantTotalDiminutionAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_DIMINUTION_AI,
                montantTotalDiminutionAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_AUGMENTATION_AVS,
                montantTotalAugmentationAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_AUGMENTATION_AI,
                montantTotalAugmentationAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_ECART_AVS,
                montantTotalEcartAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.T_VALEUR_TOTAL_ECART_AI,
                montantTotalEcartAi.toStringFormat());

        FWCurrency sousTotalAvs = new FWCurrency(0);
        sousTotalAvs.add(montantTotalAvs);
        sousTotalAvs.add(montantAugmentationMensuelleAvs);

        FWCurrency sousTotalAi = new FWCurrency(0);
        sousTotalAi.add(montantTotalAi);
        sousTotalAi.add(montantAugmentationMensuelleAi);

        FWCurrency montantDiminutionMensuelleAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantDiminutionMensuel(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency montantDiminutionMensuelleAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantDiminutionMensuel(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        FWCurrency enCoursFAvs = new FWCurrency(0);
        enCoursFAvs.add(sousTotalAvs);
        enCoursFAvs.sub(montantDiminutionMensuelleAvs);

        FWCurrency enCoursFAi = new FWCurrency(0);
        enCoursFAi.add(sousTotalAi);
        enCoursFAi.sub(montantDiminutionMensuelleAi);

        FWCurrency retroMensuelAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantRetroRecap(
                session, datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency retroMensuelAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantRetroRecap(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        FWCurrency totalMensuelAvs = new FWCurrency(0);
        totalMensuelAvs.add(enCoursFAvs);
        totalMensuelAvs.add(retroMensuelAvs);

        FWCurrency totalMensuelAi = new FWCurrency(0);
        totalMensuelAi.add(enCoursFAi);
        totalMensuelAi.add(retroMensuelAi);

        String datePeriodePlusUnMois = JadeDateUtil.addMonths("01." + datePeriode, 1);

        FWCurrency futurMensuelAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantFuturMoisPlusUnMensuel(session, datePeriodePlusUnMois.substring(3),
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency futurMensuelAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getMontantFuturMoisPlusUnMensuel(session, datePeriodePlusUnMois.substring(3),
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        FWCurrency totalFuturAvs = new FWCurrency(0);
        totalFuturAvs.add(enCoursFAvs);
        if (montantTotalEcartAvs.isZero()) {
            totalFuturAvs.add(futurMensuelAvs);
        } else {
            futurMensuelAvs = new FWCurrency(0);
            totalFuturAvs.add(montantTotalEcartAvs);
        }

        FWCurrency totalFuturAi = new FWCurrency(0);
        totalFuturAi.add(enCoursFAi);
        if (montantTotalEcartAi.isZero()) {
            totalFuturAi.add(futurMensuelAi);
        } else {
            futurMensuelAi = new FWCurrency(0);
            totalFuturAi.add(montantTotalEcartAi);
        }

        FWCurrency ponctuelAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantPonctuel(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency ponctuelAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantPonctuel(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        FWCurrency totalAvsRfm = new FWCurrency(0);
        totalAvsRfm.add(ponctuelAvs);
        totalAvsRfm.add(enCoursFAvs);

        FWCurrency totalAiRfm = new FWCurrency(0);
        totalAiRfm.add(ponctuelAi);
        totalAiRfm.add(enCoursFAi);

        FWCurrency moyenAuxAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantMoyenAuxiliaire(
                session, datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency moyenAuxAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantMoyenAuxiliaire(
                session, datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        FWCurrency soinsAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantFinancementSoins(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);
        FWCurrency soinsAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantFinancementSoins(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        FWCurrency avancesSasAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantAvancesSas(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs);

        FWCurrency avancesSasAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getMontantAvancesSas(session,
                datePeriode, RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi);

        // Liste récapitulative des paiements : insertion des données d'en-tête
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_DATE_IMPRESSION, JACalendar.todayJJsMMsAAAA());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_DATE_PERIODE, datePeriode);
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_NUM_CAISSE,
                session.getApplication().getProperty("noCaisse"));
        // Liste récapitulative des paiements : insertion des montants mensuels
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_COURANT_AVS, montantTotalAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_COURANT_AI, montantTotalAi.toStringFormat());

        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AUGMENTATION_MOIS_PRECEDENT_AVS,
                augmentationMoisPrecedentAVS.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AUGMENTATION_MOIS_PRECEDENT_AI,
                augmentationMoisPrecedentAI.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_SOUS_TOTAL_RENTE_AVS,
                sousTotalRenteAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_SOUS_TOTAL_RENTE_AI,
                sousTotalRenteAi.toStringFormat());

        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AUGMENTATION_AVS,
                montantAugmentationMensuelleAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AUGMENTATION_AI,
                montantAugmentationMensuelleAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_ADAPTATION_FIN_ANNEE_AVS,
                montantTotalEcartAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_ADAPTATION_FIN_ANNEE_AI,
                montantTotalEcartAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_SOUS_TOTAL_AVS, sousTotalAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_SOUS_TOTAL_AI, sousTotalAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_DIMINUTION_AVS,
                montantDiminutionMensuelleAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_DIMINUTION_AI,
                montantDiminutionMensuelleAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_EN_COURS_FIN_AVS, enCoursFAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_EN_COURS_FIN_AI, enCoursFAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_RETRO_AVS, retroMensuelAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_RETRO_AI, retroMensuelAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_AVS, totalMensuelAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_AI, totalMensuelAi.toStringFormat());
        // Liste récapitulative des paiements : insertion des montants futurs
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_EN_COURS_AVS, enCoursFAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_EN_COURS_AI, enCoursFAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AUGMENTATION_FUTUR_AVS,
                futurMensuelAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AUGMENTATION_FUTUR_AI,
                futurMensuelAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_FUTUR_AVS, totalFuturAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_FUTUR_AI, totalFuturAi.toStringFormat());
        // Liste récapitulative des paiements : insertion des montants ponctuels
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_PONCTUEL_AVS, ponctuelAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_PONCTUEL_AI, ponctuelAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_MENS_AVS, enCoursFAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_MENS_AI, enCoursFAi.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_AVS_RFM, totalAvsRfm.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_TOTAL_AI_RFM, totalAiRfm.toStringFormat());
        // Liste récapitulative des paiements : insertion des montants de moyens auxiliaire
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_MOYEN_AUXILIAIRE_AVS, moyenAuxAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_MOYEN_AUXILIAIRE_AI, moyenAuxAi.toStringFormat());
        // Liste récapitulative des paiements : insertion des montants de financements des soins
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_FINANCEMENT_SOINS_AVS, soinsAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_FINANCEMENT_SOINS_AI, soinsAi.toStringFormat());
        // Liste récapitulative des paiements : insertion des montants de financements des soins
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AVANCES_SAS_AVS, avancesSasAvs.toStringFormat());
        container.put(IRFListeRecapitulationPaiementsListeColumns.B_AVANCES_SAS_AI, avancesSasAi.toStringFormat());

        // MUTATIONS MENSUELLE AVS
        // =======================
        for (MutationsMensuellesData mutationCourante : RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeMutationAvs) {

            if (null != mutationCourante) {
                ArrayList<String> listeMensAvs = new ArrayList<String>();

                if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs
                        .contains(mutationCourante.getTypePrestation().get(0))) {
                    String idTiers = mutationCourante.getIdTiers();

                    // Initialisation de variables poru récupérer les infos
                    String nssMutMensAvs = PRTiersHelper.getPersonneAVS(session, idTiers).getNSS();
                    String nomMutMensAvs = PRTiersHelper.getPersonneAVS(session, idTiers).getNom();
                    String prenomMutMensAvs = PRTiersHelper.getPersonneAVS(session, idTiers).getPrenom();
                    String montantAugmentationMutMensAvs = mutationCourante.getMontantAugmentationAvs();
                    String montantDiminutionMutMensAvs = mutationCourante.getMontantDiminutionAvs();
                    String montantRetroMutMensAvs = mutationCourante.getMontantRetroAvs();

                    listeMensAvs.add(0, nssMutMensAvs);
                    listeMensAvs.add(1, nomMutMensAvs);
                    listeMensAvs.add(2, prenomMutMensAvs);
                    listeMensAvs.add(3, montantAugmentationMutMensAvs);
                    listeMensAvs.add(4, montantDiminutionMutMensAvs);
                    listeMensAvs.add(5, montantRetroMutMensAvs);

                    listeTableauMutationMensuelleTrier.add(listeMensAvs);
                }
            } else {
                throw new Exception(
                        "RFXmlmlMappingListeRecapitulativePaiementsEtMutations.loadResults:Impossible de retrouver la mutation mensuelle AVS");
            }
        }

        Collections.sort(listeTableauMutationMensuelleTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else {
                    return prenom1.compareTo(prenom2);
                }
            }
        });

        BigDecimal totalMontantAugmentationMensAvs = new BigDecimal(0);
        BigDecimal totalMontantDiminutionMensAvs = new BigDecimal(0);
        BigDecimal totalMontantRetroMensAvs = new BigDecimal(0);

        String nssMutMensAvs = "";
        String nomMutMensAvs = "";
        String prenomMutMensAvs = "";
        String montantAugmentationMutMensAvs = "";
        String montantDiminutionMutMensAvs = "";
        String montantRetroMutMensAvs = "";

        // Si liste non vide, on la parcours pour charger les données dans les variables du modèle.
        if ((listeTableauMutationMensuelleTrier.size() > 0) && (listeTableauMutationMensuelleTrier != null)) {
            for (List<String> ligne : listeTableauMutationMensuelleTrier) {

                nssMutMensAvs = ligne.get(0);
                nomMutMensAvs = ligne.get(1);
                prenomMutMensAvs = ligne.get(2);
                montantAugmentationMutMensAvs = ligne.get(3);
                montantDiminutionMutMensAvs = ligne.get(4);
                montantRetroMutMensAvs = ligne.get(5);

                // Ajout de chaque montant dans les variables total
                if (!JadeStringUtil.isEmpty(montantAugmentationMutMensAvs)) {
                    totalMontantAugmentationMensAvs = totalMontantAugmentationMensAvs.add(new BigDecimal(
                            montantAugmentationMutMensAvs));
                }
                if (!JadeStringUtil.isEmpty(montantDiminutionMutMensAvs)) {
                    totalMontantDiminutionMensAvs = totalMontantDiminutionMensAvs.add(new BigDecimal(
                            montantDiminutionMutMensAvs));
                }
                if (!JadeStringUtil.isEmpty(montantRetroMutMensAvs)) {
                    totalMontantRetroMensAvs = totalMontantRetroMensAvs.add(new BigDecimal(montantRetroMutMensAvs));
                }

                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.D_NSS_MUTATION_MENSUELLE_AVS, nssMutMensAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.D_NOM_TIERS_MUTATION_MENSUELLE_AVS,
                        nomMutMensAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.D_PRENOM_TIERS_MUTATIONS_MENSUELLE_AVS,
                        prenomMutMensAvs);
                container.put(
                        IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_AUGMENTATION_MUTATION_MENSUELLE_AVS,
                        montantAugmentationMutMensAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_DIMINUTION_MUTATION_MENSUELLE_AVS,
                        montantDiminutionMutMensAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_PONCTUEL_MUTATION_MENSUELLE_AVS,
                        montantRetroMutMensAvs);

            }
        }
        // Si aucun éléments dans la liste, on remplis une fois les variables du modèle pour cacher le nom de variable.
        else {
            // Insertion du contenu de chaque variable dans le document
            container.put(IRFListeRecapitulationPaiementsListeColumns.D_NSS_MUTATION_MENSUELLE_AVS, nssMutMensAvs);
            container
                    .put(IRFListeRecapitulationPaiementsListeColumns.D_NOM_TIERS_MUTATION_MENSUELLE_AVS, nomMutMensAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.D_PRENOM_TIERS_MUTATIONS_MENSUELLE_AVS,
                    prenomMutMensAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_AUGMENTATION_MUTATION_MENSUELLE_AVS,
                    montantAugmentationMutMensAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_DIMINUTION_MUTATION_MENSUELLE_AVS,
                    montantDiminutionMutMensAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_PONCTUEL_MUTATION_MENSUELLE_AVS,
                    montantRetroMutMensAvs);
        }
        // Insertion des montants totaux en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_TOTAL_AUGENTATION_MUTATIONS_MENSUELLE_AVS,
                totalMontantAugmentationMensAvs.toString());
        container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_TOTAL_DIMINUTION_MUTATIONS_MENSUELLE_AVS,
                totalMontantDiminutionMensAvs.toString());
        container.put(IRFListeRecapitulationPaiementsListeColumns.D_MONTANT_TOTAL_RETRO_MUTATIONS_MENSUELLE_AVS,
                totalMontantRetroMensAvs.toString());

        // MUTATIONS PONCTUELLE AVS
        // ========================
        // Création d'une liste de toutes les prestations ponctuelles de type AVS correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listePrestationsPonctuelsAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, true, false,
                        false, null, null, false);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listePrestationsPonctuelsAvs.hasNext()) {
            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestPoncAvs = listePrestationsPonctuelsAvs
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsPrestationsPonctuellesAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getDetailPrestationPonctuelle(rfOrdVerJointPrestPoncAvs, session);
            elementMutationTrier.add(elementsPrestationsPonctuellesAvs);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une variable pour additionner l'ensemble de toutes les prestations
        BigDecimal montantTotalPonctuelleAvs = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssAvsPonctuel = ligne.get(0);
            String nomAvsPonctuel = ligne.get(1);
            String prenomAvsPonctuel = ligne.get(2);
            String montantAvsPonctuel = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssAvsPonctuel;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvsPonctuel));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssAvsPonctuel)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvsPonctuel));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                container.put(IRFListeRecapitulationPaiementsListeColumns.F_NSS_MUTATION_PONCTUELLE_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.F_NOM_TIERS_MUTATION_PONCTUELLE_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.F_PRENOM_TIERS_MUTATION_PONCTUELLE_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_MUTATION_PONCTUELLE_AVS, " ");

                container.put(
                        IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_TOTAL_REQUERANT_MUTATION_PONCTUELLE_AVS,
                        totalParRequerantMutation.toString());
                nssRequerantMutation = nssAvsPonctuel;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvsPonctuel));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.F_NSS_MUTATION_PONCTUELLE_AVS, nssAvsPonctuel);
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_NOM_TIERS_MUTATION_PONCTUELLE_AVS,
                    nomAvsPonctuel);
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_PRENOM_TIERS_MUTATION_PONCTUELLE_AVS,
                    prenomAvsPonctuel);
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_MUTATION_PONCTUELLE_AVS,
                    montantAvsPonctuel);
            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_TOTAL_REQUERANT_MUTATION_PONCTUELLE_AVS, " ");

            // Ajout de chaque montant dans la variable
            if (!JadeStringUtil.isEmpty(montantAvsPonctuel)) {
                montantTotalPonctuelleAvs = montantTotalPonctuelleAvs.add(new BigDecimal(montantAvsPonctuel));
            }
        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        int i = 0;
        for (i = 0; i < 1; i++) {
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_NSS_MUTATION_PONCTUELLE_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_NOM_TIERS_MUTATION_PONCTUELLE_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_PRENOM_TIERS_MUTATION_PONCTUELLE_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_MUTATION_PONCTUELLE_AVS, " ");

            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_TOTAL_REQUERANT_MUTATION_PONCTUELLE_AVS,
                    totalParRequerantMutation.toString());

            totalParRequerantMutation = new BigDecimal(0);
            nssRequerantMutation = null;
            elementMutationTrier = new ArrayList<ArrayList<String>>();
        }

        // Insertion du montant total pour l'ensemble de toutes les prestations
        container.put(IRFListeRecapitulationPaiementsListeColumns.F_MONTANT_TOTAL_MUTATION_PONCTUELLE_AVS,
                montantTotalPonctuelleAvs.toString());

        // MUTATIONS MOYENS AUXILIAIRE AVS
        // ===============================
        // Création d'une liste de tous les moyens auxiliaires de type AVS correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeMoyensAuxIterAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, true,
                        false, null, null, false);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listeMoyensAuxIterAvs.hasNext()) {

            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestMoyAuxAvs = listeMoyensAuxIterAvs
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsMoyensAuxAvs;
            elementsMoyensAuxAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getDetailMutationsPonctuelles(
                    rfOrdVerJointPrestMoyAuxAvs, session);
            elementMutationTrier.add(elementsMoyensAuxAvs);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une variable pour additionner l'ensemble des montants
        BigDecimal montantTotalMoyensAuxAvs = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssAvsMoyenAux = ligne.get(0);
            String nomAvsMoyenAux = ligne.get(1);
            String prenomAvsMoyenAux = ligne.get(2);
            String montantAvsMoyenAux = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssAvsMoyenAux;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvsMoyenAux));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssAvsMoyenAux)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvsMoyenAux));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                container.put(IRFListeRecapitulationPaiementsListeColumns.H_NSS_MUTATION_MOYENS_AUX_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.H_NOM_TIERS_MUTATION_MOYENS_AUX_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.H_PRENOM_TIERS_MUTATION_MOYENS_AUX_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_MUTATION_MOYENS_AUX_AVS, " ");

                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_MOYENS_AUX_AVS,
                                totalParRequerantMutation.toString());
                nssRequerantMutation = nssAvsMoyenAux;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvsMoyenAux));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.H_NSS_MUTATION_MOYENS_AUX_AVS, nssAvsMoyenAux);
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_NOM_TIERS_MUTATION_MOYENS_AUX_AVS,
                    nomAvsMoyenAux);
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_PRENOM_TIERS_MUTATION_MOYENS_AUX_AVS,
                    prenomAvsMoyenAux);
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_MUTATION_MOYENS_AUX_AVS,
                    montantAvsMoyenAux);
            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_MOYENS_AUX_AVS,
                    " ");

            // Ajout du montant dans la variable des montants totaux
            if (!JadeStringUtil.isEmpty(montantAvsMoyenAux)) {
                montantTotalMoyensAuxAvs = montantTotalMoyensAuxAvs.add(new BigDecimal(montantAvsMoyenAux));
            }

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        // TODO: passe une fois seulemement ??
        int j = 0;
        for (j = 0; j < 1; j++) {
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_NSS_MUTATION_MOYENS_AUX_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_NOM_TIERS_MUTATION_MOYENS_AUX_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_PRENOM_TIERS_MUTATION_MOYENS_AUX_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_MUTATION_MOYENS_AUX_AVS, " ");

            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_MOYENS_AUX_AVS,
                    totalParRequerantMutation.toString());

            totalParRequerantMutation = new BigDecimal(0);
            nssRequerantMutation = null;
            elementMutationTrier = new ArrayList<ArrayList<String>>();
        }

        // Insertion du montant total pour l'ensemble des prestations
        container.put(IRFListeRecapitulationPaiementsListeColumns.H_MONTANT_TOTAL_MUTATION_MOYENS_AUX_AVS,
                montantTotalMoyensAuxAvs.toString());

        listeTableauMutationMensuelleTrier = new ArrayList<ArrayList<String>>();
        for (MutationsMensuellesData mutationCourante : RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeMutationAvs) {

            if (null != mutationCourante) {
                ArrayList<String> listeMensAi = new ArrayList<String>();

                // MUTATIONS MENSUELLE AI
                // =======================
                if (RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi
                        .contains(mutationCourante.getTypePrestation().get(0))) {
                    String idTiers = mutationCourante.getIdTiers();

                    // Initialisation de variables pour récupérer les infos
                    String nssMutMensAi = PRTiersHelper.getPersonneAVS(session, idTiers).getNSS();
                    String nomMutMensAi = PRTiersHelper.getPersonneAVS(session, idTiers).getNom();
                    String prenomMutMensAi = PRTiersHelper.getPersonneAVS(session, idTiers).getPrenom();
                    String montantAugmentationMutMensAi = mutationCourante.getMontantAugmentationAi();
                    String montantDiminutionMutMensAi = mutationCourante.getMontantDiminutionAi();
                    String montantRetroMutMensAi = mutationCourante.getMontantRetroAi();

                    listeMensAi.add(0, nssMutMensAi);
                    listeMensAi.add(1, nomMutMensAi);
                    listeMensAi.add(2, prenomMutMensAi);
                    listeMensAi.add(3, montantAugmentationMutMensAi);
                    listeMensAi.add(4, montantDiminutionMutMensAi);
                    listeMensAi.add(5, montantRetroMutMensAi);

                    listeTableauMutationMensuelleTrier.add(listeMensAi);
                }
            } else {
                throw new Exception(
                        "RFXmlmlMappingListeRecapitulativePaiementsEtMutations.loadResults:Impossible de retrouver la mutation mensuelle AI");
            }
        }

        Collections.sort(listeTableauMutationMensuelleTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else {
                    return prenom1.compareTo(prenom2);
                }
            }
        });

        // Déclaration de variables pour les montants totaux
        BigDecimal montantTotalAugmentationMensAi = new BigDecimal(0);
        BigDecimal montantTotalDiminutionMensAi = new BigDecimal(0);
        BigDecimal montantTotalRetroMensAi = new BigDecimal(0);
        String nssMutationMensAi = "";
        String nomMutationMensAi = "";
        String prenomMutationMensAi = "";
        String montantAugmentationMutationMensAi = "";
        String montantDiminutionMutationMensAi = "";
        String montantRetroMutationMensAi = "";

        if ((listeTableauMutationMensuelleTrier.size() > 0) && (listeTableauMutationMensuelleTrier != null)) {
            for (List<String> ligne : listeTableauMutationMensuelleTrier) {

                nssMutationMensAi = ligne.get(0);
                nomMutationMensAi = ligne.get(1);
                prenomMutationMensAi = ligne.get(2);
                montantAugmentationMutationMensAi = ligne.get(3);
                montantDiminutionMutationMensAi = ligne.get(4);
                montantRetroMutationMensAi = ligne.get(5);

                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.J_NSS_MUTATION_MENSUELLE_AI,
                        nssMutationMensAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.J_NOM_TIERS_MUTATION_MENSUELLE_AI,
                        nomMutationMensAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.J_PRENOM_TIERS_MUTATIONS_MENSUELLE_AI,
                        prenomMutationMensAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_AUGMENTATION_MUTATION_MENSUELLE_AI,
                        montantAugmentationMutationMensAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_DIMINUTION_MUTATION_MENSUELLE_AI,
                        montantDiminutionMutationMensAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_PONCTUEL_MUTATION_MENSUELLE_AI,
                        montantRetroMutationMensAi);

                // Si pas vide, affectation des montants dans les variables de montants totaux
                if (!JadeStringUtil.isEmpty(montantAugmentationMutationMensAi)) {
                    montantTotalAugmentationMensAi = montantTotalAugmentationMensAi.add(new BigDecimal(
                            montantAugmentationMutationMensAi));
                }
                if (!JadeStringUtil.isEmpty(montantDiminutionMutationMensAi)) {
                    montantTotalDiminutionMensAi = montantTotalDiminutionMensAi.add(new BigDecimal(
                            montantDiminutionMutationMensAi));
                }
                if (!JadeStringUtil.isEmpty(montantRetroMutationMensAi)) {
                    montantTotalRetroMensAi = montantTotalRetroMensAi.add(new BigDecimal(montantRetroMutationMensAi));
                }

            }
        } else {
            // Insertion du contenu de chaque variable dans le document
            container.put(IRFListeRecapitulationPaiementsListeColumns.J_NSS_MUTATION_MENSUELLE_AI, nssMutationMensAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.J_NOM_TIERS_MUTATION_MENSUELLE_AI,
                    nomMutationMensAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.J_PRENOM_TIERS_MUTATIONS_MENSUELLE_AI,
                    prenomMutationMensAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_AUGMENTATION_MUTATION_MENSUELLE_AI,
                    montantAugmentationMutationMensAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_DIMINUTION_MUTATION_MENSUELLE_AI,
                    montantDiminutionMutationMensAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_PONCTUEL_MUTATION_MENSUELLE_AI,
                    montantRetroMutationMensAi);
        }
        // Insertion des montants totaux en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_TOTAL_AUGMENTATION_AI,
                montantTotalAugmentationMensAi.toString());
        container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_TOTAL_DIMINUTION_AI,
                montantTotalDiminutionMensAi.toString());
        container.put(IRFListeRecapitulationPaiementsListeColumns.J_MONTANT_TOTAL_RETRO_AI,
                montantTotalRetroMensAi.toString());

        // MUTATIONS PONCTUELLE AI
        // ========================
        // Création d'une liste de toutes les prestations ponctuelles de type AVS correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listePrestationsPonctuelsAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, true, false,
                        false, null, null, false);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listePrestationsPonctuelsAi.hasNext()) {
            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestPoncAi = listePrestationsPonctuelsAi
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsPrestationsPonctuellesAi;
            elementsPrestationsPonctuellesAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getDetailPrestationPonctuelle(rfOrdVerJointPrestPoncAi, session);
            elementMutationTrier.add(elementsPrestationsPonctuellesAi);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une variable pour additionner les montants
        BigDecimal montantTotalPonctuelleAi = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssAiPonctuel = ligne.get(0);
            String nomAiPonctuel = ligne.get(1);
            String prenomAiPonctuel = ligne.get(2);
            String montantAiPonctuel = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssAiPonctuel;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAiPonctuel));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssAiPonctuel)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAiPonctuel));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                container.put(IRFListeRecapitulationPaiementsListeColumns.L_NSS_MUTATION_PONCTUELLE_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.L_NOM_TIERS_MUTATION_PONCTUELLE_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.L_PRENOM_TIERS_MUTATION_PONCTUELLE_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_MUTATION_PONCTUELLE_AI, " ");

                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_PONCTUELLE_AI,
                                totalParRequerantMutation.toString());
                nssRequerantMutation = nssAiPonctuel;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAiPonctuel));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.L_NSS_MUTATION_PONCTUELLE_AI, nssAiPonctuel);
            container
                    .put(IRFListeRecapitulationPaiementsListeColumns.L_NOM_TIERS_MUTATION_PONCTUELLE_AI, nomAiPonctuel);
            container.put(IRFListeRecapitulationPaiementsListeColumns.L_PRENOM_TIERS_MUTATION_PONCTUELLE_AI,
                    prenomAiPonctuel);
            container.put(IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_MUTATION_PONCTUELLE_AI,
                    montantAiPonctuel);
            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_PONCTUELLE_AI,
                    " ");

            // Ajout du montant dans la variable total si pas vide
            if (!JadeStringUtil.isEmpty(montantAiPonctuel)) {
                montantTotalPonctuelleAi = montantTotalPonctuelleAi.add(new BigDecimal(montantAiPonctuel));
            }

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        // TODO: passe une fois seulemement ???
        int k = 0;
        for (k = 0; k < 1; k++) {
            container.put(IRFListeRecapitulationPaiementsListeColumns.L_NSS_MUTATION_PONCTUELLE_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.L_NOM_TIERS_MUTATION_PONCTUELLE_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.L_PRENOM_TIERS_MUTATION_PONCTUELLE_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_MUTATION_PONCTUELLE_AI, " ");

            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_PONCTUELLE_AI,
                    totalParRequerantMutation.toString());

            totalParRequerantMutation = new BigDecimal(0);
            nssRequerantMutation = null;
            elementMutationTrier = new ArrayList<ArrayList<String>>();
        }

        // Insertion du montant total en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.L_MONTANT_TOTAL_MUTATION_PONCTUELLE_AI,
                montantTotalPonctuelleAi.toString());

        // MUTATIONS MOYENS AUXILIAIRE AI
        // ===============================
        // Création d'une liste de tous les moyens auxiliaires de type AI correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeMoyensAuxIterAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, true,
                        false, null, null, false);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listeMoyensAuxIterAi.hasNext()) {

            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestMoyAuxAi = listeMoyensAuxIterAi
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsMoyensAuxAi;
            elementsMoyensAuxAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getDetailMutationsPonctuelles(
                    rfOrdVerJointPrestMoyAuxAi, session);
            elementMutationTrier.add(elementsMoyensAuxAi);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Création d'une variable pour additionner les montants
        BigDecimal montantTotalMoyensAuxAi = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssAiMoyenAux = ligne.get(0);
            String nomAiMoyenAux = ligne.get(1);
            String prenomAiMoyenAux = ligne.get(2);
            String montantAiMoyenAux = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssAiMoyenAux;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAiMoyenAux));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssAiMoyenAux)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAiMoyenAux));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.N_NSS_MUTATION_MOYENS_AUX_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.N_NOM_TIERS_MUTATION_MOYENS_AUX_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.N_PRENOM_TIERS_MUTATION_MOYENS_AUX_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_MUTATION_MOYENS_AUX_AI, " ");

                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_MOYENS_AUX_AI,
                                totalParRequerantMutation.toString());
                nssRequerantMutation = nssAiMoyenAux;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAiMoyenAux));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.N_NSS_MUTATION_MOYENS_AUX_AI, nssAiMoyenAux);
            container
                    .put(IRFListeRecapitulationPaiementsListeColumns.N_NOM_TIERS_MUTATION_MOYENS_AUX_AI, nomAiMoyenAux);
            container.put(IRFListeRecapitulationPaiementsListeColumns.N_PRENOM_TIERS_MUTATION_MOYENS_AUX_AI,
                    prenomAiMoyenAux);
            container.put(IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_MUTATION_MOYENS_AUX_AI,
                    montantAiMoyenAux);
            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_MOYENS_AUX_AI,
                    " ");

            // Insertion du montant dans la variable des montants totaux
            montantTotalMoyensAuxAi = montantTotalMoyensAuxAi.add(new BigDecimal(montantAiMoyenAux));

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        // TODO: passe une fois seulemement ???
        int l = 0;
        for (l = 0; l < 1; l++) {
            container.put(IRFListeRecapitulationPaiementsListeColumns.N_NSS_MUTATION_MOYENS_AUX_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.N_NOM_TIERS_MUTATION_MOYENS_AUX_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.N_PRENOM_TIERS_MUTATION_MOYENS_AUX_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_MUTATION_MOYENS_AUX_AI, " ");

            container.put(
                    IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_MOYENS_AUX_AI,
                    totalParRequerantMutation.toString());

            totalParRequerantMutation = new BigDecimal(0);
            nssRequerantMutation = null;
            elementMutationTrier = new ArrayList<ArrayList<String>>();
        }

        // Insertion du montant total dans le document
        container.put(IRFListeRecapitulationPaiementsListeColumns.N_MONTANT_TOTAL_MUTATION_MOYENS_AUX_AI,
                montantTotalMoyensAuxAi.toString());

        // MUTATIONS FINANCEMENT DES SOINS AVS
        // ===================================
        // Création d'une liste de tous les moyens auxiliaires de type AI correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeFinanceSoinsIterAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                        true, null, null, false);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listeFinanceSoinsIterAvs.hasNext()) {

            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestFinanceSoinsAvs = listeFinanceSoinsIterAvs
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsFinanceSoinsAvs;
            elementsFinanceSoinsAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getDetailMutationsPonctuelles(rfOrdVerJointPrestFinanceSoinsAvs, session);
            elementMutationTrier.add(elementsFinanceSoinsAvs);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une varaible pour additionner l'ensemble des montants
        BigDecimal montantTotalFinancementSoinsAvs = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssFinanceSoinsAvs = ligne.get(0);
            String nomFinanceSoinsAvs = ligne.get(1);
            String prenomFinanceSoinsAvs = ligne.get(2);
            String montantFinanceSoinsAvs = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssFinanceSoinsAvs;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantFinanceSoinsAvs));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssFinanceSoinsAvs)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantFinanceSoinsAvs));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.P_NSS_MUTATION_FINANCE_SOINS_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.P_NOM_MUTATION_FINANCE_SOINS_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.P_PRENOM_MUTATION_FINANCE_SOINS_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_MUTATION_FINANCE_SOINS_AVS, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_TOTAL_MUTATION_FINANCE_SOINS_AVS,
                        totalParRequerantMutation.toString());
                nssRequerantMutation = nssFinanceSoinsAvs;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantFinanceSoinsAvs));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.P_NSS_MUTATION_FINANCE_SOINS_AVS,
                    nssFinanceSoinsAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_NOM_MUTATION_FINANCE_SOINS_AVS,
                    nomFinanceSoinsAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_PRENOM_MUTATION_FINANCE_SOINS_AVS,
                    prenomFinanceSoinsAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_MUTATION_FINANCE_SOINS_AVS,
                    montantFinanceSoinsAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_TOTAL_MUTATION_FINANCE_SOINS_AVS, " ");

            // Ajout de chaque montant dans la variable total
            montantTotalFinancementSoinsAvs = montantTotalFinancementSoinsAvs
                    .add(new BigDecimal(montantFinanceSoinsAvs));

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        // TODO: passe une fois seulemement ???
        int m = 0;
        for (m = 0; m < 1; m++) {
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_NSS_MUTATION_FINANCE_SOINS_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_NOM_MUTATION_FINANCE_SOINS_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_PRENOM_MUTATION_FINANCE_SOINS_AVS, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_MUTATION_FINANCE_SOINS_AVS, " ");

            container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_TOTAL_MUTATION_FINANCE_SOINS_AVS,
                    totalParRequerantMutation.toString());

            totalParRequerantMutation = new BigDecimal(0);
            nssRequerantMutation = null;
            elementMutationTrier = new ArrayList<ArrayList<String>>();
        }

        // Insertion du montant total en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.P_MONTANT_TOTAL_FINANCE_SOINS_AVS,
                montantTotalFinancementSoinsAvs.toString());

        // MUTATIONS FINANCEMENT DES SOINS AI
        // ===================================
        // Création d'une liste de tous les moyens auxiliaires de type AI correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeFinanceSoinsIterAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                        true, null, null, false);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listeFinanceSoinsIterAi.hasNext()) {

            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestFinanceSoinsAi = listeFinanceSoinsIterAi
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsFinanceSoinsAi;
            elementsFinanceSoinsAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getDetailMutationsPonctuelles(rfOrdVerJointPrestFinanceSoinsAi, session);
            elementMutationTrier.add(elementsFinanceSoinsAi);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une variable pour addition les montants
        BigDecimal montantTotalFinanceSoinsAi = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssFinanceSoinsAi = ligne.get(0);
            String nomFinanceSoinsAi = ligne.get(1);
            String prenomFinanceSoinsAi = ligne.get(2);
            String montantFinanceSoinsAi = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssFinanceSoinsAi;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantFinanceSoinsAi));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssFinanceSoinsAi)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantFinanceSoinsAi));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.R_NSS_MUTATION_FINANCE_SOINS_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.R_NOM_MUTATION_FINANCE_SOINS_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.R_PRENOM_MUTATION_FINANCE_SOINS_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_MUTATION_FINANCE_SOINS_AI, " ");

                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_FINANCE_SOINS_AI,
                                totalParRequerantMutation.toString());
                nssRequerantMutation = nssFinanceSoinsAi;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantFinanceSoinsAi));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.R_NSS_MUTATION_FINANCE_SOINS_AI,
                    nssFinanceSoinsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_NOM_MUTATION_FINANCE_SOINS_AI,
                    nomFinanceSoinsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_PRENOM_MUTATION_FINANCE_SOINS_AI,
                    prenomFinanceSoinsAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_MUTATION_FINANCE_SOINS_AI,
                    montantFinanceSoinsAi);
            container
                    .put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_FINANCE_SOINS_AI,
                            " ");

            // Ajout du montant dans la variable des montants totaux
            montantTotalFinanceSoinsAi = montantTotalFinanceSoinsAi.add(new BigDecimal(montantFinanceSoinsAi));

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        // TODO: passe une fois seulemement ???
        int n = 0;
        for (n = 0; n < 1; n++) {
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_NSS_MUTATION_FINANCE_SOINS_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_NOM_MUTATION_FINANCE_SOINS_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_PRENOM_MUTATION_FINANCE_SOINS_AI, " ");
            container.put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_MUTATION_FINANCE_SOINS_AI, " ");

            container
                    .put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_TOTAL_PAR_REQUERANT_MUTATION_FINANCE_SOINS_AI,
                            totalParRequerantMutation.toString());

            totalParRequerantMutation = new BigDecimal(0);
            nssRequerantMutation = null;
            elementMutationTrier = new ArrayList<ArrayList<String>>();
        }

        // Insertion du montant total en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.R_MONTANT_TOTAL_MUTATION_FINANCE_SOINS_AI,
                montantTotalFinanceSoinsAi.toString());

        // MUTATIONS AVANCES SAS AVS
        // ===================================
        // Création d'une liste de tous les avances SAS de type AVS correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeAvancesSasIterAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                        false, null, null, true);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listeAvancesSasIterAvs.hasNext()) {

            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestAvancesSasAvs = listeAvancesSasIterAvs
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsAvancesSasAvs;
            elementsAvancesSasAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getDetailMutationsPonctuelles(rfOrdVerJointPrestAvancesSasAvs, session);
            elementMutationTrier.add(elementsAvancesSasAvs);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une varaible pour additionner l'ensemble des montants
        BigDecimal montantTotalAvancesSasAvs = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssAvancesSasAvs = ligne.get(0);
            String nomAvancesSasAvs = ligne.get(1);
            String prenomAvancesSasAvs = ligne.get(2);
            String montantAvancesSasAvs = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssAvancesSasAvs;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvancesSasAvs));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssAvancesSasAvs)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvancesSasAvs));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.V_NSS_MUTATION_AVANCES_SAS_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V_NOM_MUTATION_AVANCES_SAS_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V_PRENOM_MUTATION_AVANCES_SAS_AVS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_MUTATION_AVANCES_SAS_AVS, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AVS,
                        totalParRequerantMutation.toString());
                nssRequerantMutation = nssAvancesSasAvs;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvancesSasAvs));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.V_NSS_MUTATION_AVANCES_SAS_AVS, nssAvancesSasAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.V_NOM_MUTATION_AVANCES_SAS_AVS, nomAvancesSasAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.V_PRENOM_MUTATION_AVANCES_SAS_AVS,
                    prenomAvancesSasAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_MUTATION_AVANCES_SAS_AVS,
                    montantAvancesSasAvs);
            container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AVS, " ");

            // Ajout de chaque montant dans la variable total
            montantTotalAvancesSasAvs = montantTotalAvancesSasAvs.add(new BigDecimal(montantAvancesSasAvs));

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        container.put(IRFListeRecapitulationPaiementsListeColumns.V_NSS_MUTATION_AVANCES_SAS_AVS, " ");
        container.put(IRFListeRecapitulationPaiementsListeColumns.V_NOM_MUTATION_AVANCES_SAS_AVS, " ");
        container.put(IRFListeRecapitulationPaiementsListeColumns.V_PRENOM_MUTATION_AVANCES_SAS_AVS, " ");
        container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_MUTATION_AVANCES_SAS_AVS, " ");

        container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AVS,
                totalParRequerantMutation.toString());

        totalParRequerantMutation = new BigDecimal(0);
        nssRequerantMutation = null;
        elementMutationTrier = new ArrayList<ArrayList<String>>();

        // Insertion du montant total en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.V_MONTANT_TOTAL_AVANCES_SAS_AVS,
                montantTotalAvancesSasAvs.toString());

        // MUTATIONS AVANCES SAS AI
        // ===================================
        // Création d'une liste de tous les avances SAS de type AVS correspondant la période saisie
        Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeAvancesSasIterAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                .getListePrestationsPonctuelles(session, datePeriode,
                        RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                        false, null, null, true);

        // Parcour chaque élément de la liste pour récupérer les données
        while (listeAvancesSasIterAi.hasNext()) {

            RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestAvancesSasAi = listeAvancesSasIterAi
                    .next();

            // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
            ArrayList<String> elementsAvancesSasAi;
            elementsAvancesSasAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations.getDetailMutationsPonctuelles(
                    rfOrdVerJointPrestAvancesSasAi, session);
            elementMutationTrier.add(elementsAvancesSasAi);
        }

        Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                String nom1 = o1.get(1);
                String prenom1 = o1.get(2);
                String idPrestation1 = o1.get(4);

                String nom2 = o2.get(1);
                String prenom2 = o2.get(2);
                String idPrestation2 = o2.get(4);

                if (nom1.compareTo(nom2) != 0) {
                    return nom1.compareTo(nom2);
                } else if (prenom1.compareTo(prenom2) != 0) {
                    return prenom1.compareTo(prenom2);
                } else {
                    return idPrestation1.compareTo(idPrestation2);
                }
            }
        });

        // Déclaration d'une varaible pour additionner l'ensemble des montants
        BigDecimal montantTotalAvancesSasAi = new BigDecimal(0);

        for (List<String> ligne : elementMutationTrier) {

            // Affecte chaque élément du tableau à la variable respective
            String nssAvancesSasAi = ligne.get(0);
            String nomAvancesSasAi = ligne.get(1);
            String prenomAvancesSasAi = ligne.get(2);
            String montantAvancesSasAi = ligne.get(3);

            // Affectation du premier tuple
            if (nssRequerantMutation == null) {
                nssRequerantMutation = nssAvancesSasAi;
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvancesSasAi));

            }
            // Sinon addition du montant si même nss
            else if (nssRequerantMutation.equals(nssAvancesSasAi)) {
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvancesSasAi));
            }
            // Sinon insertion d'une ligne affichant le total
            else {
                // Insertion du contenu de chaque variable dans le document
                container.put(IRFListeRecapitulationPaiementsListeColumns.X_NSS_MUTATION_AVANCES_SAS_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.X_NOM_MUTATION_AVANCES_SAS_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.X_PRENOM_MUTATION_AVANCES_SAS_AI, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_MUTATION_AVANCES_SAS_AI, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AI,
                        totalParRequerantMutation.toString());
                nssRequerantMutation = nssAvancesSasAi;
                totalParRequerantMutation = new BigDecimal(0);
                totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAvancesSasAi));
            }

            container.put(IRFListeRecapitulationPaiementsListeColumns.X_NSS_MUTATION_AVANCES_SAS_AI, nssAvancesSasAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.X_NOM_MUTATION_AVANCES_SAS_AI, nomAvancesSasAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.X_PRENOM_MUTATION_AVANCES_SAS_AI,
                    prenomAvancesSasAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_MUTATION_AVANCES_SAS_AI,
                    montantAvancesSasAi);
            container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AI, " ");

            // Ajout de chaque montant dans la variable total
            montantTotalAvancesSasAi = montantTotalAvancesSasAi.add(new BigDecimal(montantAvancesSasAi));

        }

        // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et nssRequerantMutation
        // et du tableau elementMutationTrier
        container.put(IRFListeRecapitulationPaiementsListeColumns.X_NSS_MUTATION_AVANCES_SAS_AI, " ");
        container.put(IRFListeRecapitulationPaiementsListeColumns.X_NOM_MUTATION_AVANCES_SAS_AI, " ");
        container.put(IRFListeRecapitulationPaiementsListeColumns.X_PRENOM_MUTATION_AVANCES_SAS_AI, " ");
        container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_MUTATION_AVANCES_SAS_AI, " ");

        container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_TOTAL_MUTATION_AVANCES_SAS_AI,
                totalParRequerantMutation.toString());

        totalParRequerantMutation = new BigDecimal(0);
        nssRequerantMutation = null;
        elementMutationTrier = new ArrayList<ArrayList<String>>();

        // Insertion du montant total en bas de tableau
        container.put(IRFListeRecapitulationPaiementsListeColumns.X_MONTANT_TOTAL_AVANCES_SAS_AI,
                montantTotalAvancesSasAi.toString());

        // POUR LISTE COMPLETE
        if (RFPropertiesUtils.utiliserListeRecapComplete()) {

            // MUTATIONS FRANCHISE ET PARTICPATION AVS
            // =======================================
            // Création d'une liste de tous les franchises et participations de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeFranchiseParticpationAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeFranchiseParticpationAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestFranchiseParticpationAvs = listeFranchiseParticpationAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsFranchiseParticipationAvs;
                elementsFranchiseParticipationAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestFranchiseParticpationAvs, session);
                elementMutationTrier.add(elementsFranchiseParticipationAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalFranchiseParticipationAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssFranchiseParticipationAvs = ligne.get(0);
                String nomFranchiseParticipationAvs = ligne.get(1);
                String prenomFranchiseParticipationAvs = ligne.get(2);
                String montantFranchiseParticipationAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssFranchiseParticipationAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFranchiseParticipationAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssFranchiseParticipationAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFranchiseParticipationAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V01_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V01_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V01_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssFranchiseParticipationAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFranchiseParticipationAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_NSS_TIERS, nssFranchiseParticipationAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_NOM_TIERS, nomFranchiseParticipationAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_PRENOM_TIERS,
                        prenomFranchiseParticipationAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANT_UNITAIRE,
                        montantFranchiseParticipationAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalFranchiseParticipationAvs = montantTotalFranchiseParticipationAvs.add(new BigDecimal(
                        montantFranchiseParticipationAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int o = 0;
            for (o = 0; o < 1; o++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_MONTANTS_TOTAUX,
                    montantTotalFranchiseParticipationAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_FRANCHISE_PARTICIPATION_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V01_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS FRANCHISE ET PARTICPATION AI
            // =======================================
            // Création d'une liste de tous les franchises et participations de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeFranchiseParticpationAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeFranchiseParticpationAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestFranchiseParticpationAi = listeFranchiseParticpationAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsFranchiseParticipationAi;
                elementsFranchiseParticipationAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestFranchiseParticpationAi, session);
                elementMutationTrier.add(elementsFranchiseParticipationAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalFranchiseParticipationAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssFranchiseParticipationAi = ligne.get(0);
                String nomFranchiseParticipationAi = ligne.get(1);
                String prenomFranchiseParticipationAi = ligne.get(2);
                String montantFranchiseParticipationAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssFranchiseParticipationAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFranchiseParticipationAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssFranchiseParticipationAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFranchiseParticipationAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V02_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V02_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V02_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssFranchiseParticipationAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFranchiseParticipationAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_NSS_TIERS, nssFranchiseParticipationAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_NOM_TIERS, nomFranchiseParticipationAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_PRENOM_TIERS,
                        prenomFranchiseParticipationAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANT_UNITAIRE,
                        montantFranchiseParticipationAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalFranchiseParticipationAi = montantTotalFranchiseParticipationAi.add(new BigDecimal(
                        montantFranchiseParticipationAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int p = 0;
            for (p = 0; p < 1; p++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_MONTANTS_TOTAUX,
                    montantTotalFranchiseParticipationAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_FRANCHISE_PARTICIPATION_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V02_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS TRAITEMENT DENTAIRE AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeTraitementDentaireAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeTraitementDentaireAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestTraitementDentaireAvs = listeTraitementDentaireAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsTraitementDentaireAvs;
                elementsTraitementDentaireAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestTraitementDentaireAvs, session);
                elementMutationTrier.add(elementsTraitementDentaireAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalTraitementDentaireAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssTraitementDentaireAvs = ligne.get(0);
                String nomTraitementDentaireAvs = ligne.get(1);
                String prenomTraitementDentaireAvs = ligne.get(2);
                String montantTraitementDentaireAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssTraitementDentaireAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantTraitementDentaireAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssTraitementDentaireAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantTraitementDentaireAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V03_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V03_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V03_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssTraitementDentaireAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantTraitementDentaireAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_NSS_TIERS, nssTraitementDentaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_NOM_TIERS, nomTraitementDentaireAvs);
                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.V03_PRENOM_TIERS, prenomTraitementDentaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANT_UNITAIRE,
                        montantTraitementDentaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalTraitementDentaireAvs = montantTotalTraitementDentaireAvs.add(new BigDecimal(
                        montantTraitementDentaireAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int q = 0;
            for (q = 0; q < 1; q++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_MONTANTS_TOTAUX,
                    montantTotalTraitementDentaireAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_TRAITEMENT_DENTAIRE_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V03_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS TRAITEMENT DENTAIRE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeTraitementDentaireAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeTraitementDentaireAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestTraitementDentaireAi = listeTraitementDentaireAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsTraitementDentaireAi;
                elementsTraitementDentaireAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestTraitementDentaireAi, session);
                elementMutationTrier.add(elementsTraitementDentaireAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalTraitementDentaireAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssTraitementDentaireAi = ligne.get(0);
                String nomTraitementDentaireAi = ligne.get(1);
                String prenomTraitementDentaireAi = ligne.get(2);
                String montantTraitementDentaireAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssTraitementDentaireAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantTraitementDentaireAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssTraitementDentaireAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantTraitementDentaireAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V04_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V04_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V04_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssTraitementDentaireAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantTraitementDentaireAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_NSS_TIERS, nssTraitementDentaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_NOM_TIERS, nomTraitementDentaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_PRENOM_TIERS, prenomTraitementDentaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANT_UNITAIRE,
                        montantTraitementDentaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalTraitementDentaireAi = montantTotalTraitementDentaireAi.add(new BigDecimal(
                        montantTraitementDentaireAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int r = 0;
            for (r = 0; r < 1; r++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_MONTANTS_TOTAUX,
                    montantTotalTraitementDentaireAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_TRAITEMENT_DENTAIRE_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V04_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS AIDE AU MENAGE AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeAideAuMenageAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeAideAuMenageAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestAideAuMenageAvs = listeAideAuMenageAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsAideAuMenageAvs;
                elementsAideAuMenageAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestAideAuMenageAvs, session);
                elementMutationTrier.add(elementsAideAuMenageAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalAideAuMenageAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssAideAuMenageAvs = ligne.get(0);
                String nomAideAuMenageAvs = ligne.get(1);
                String prenomAideAuMenageAvs = ligne.get(2);
                String montantAideAuMenageAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssAideAuMenageAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAideAuMenageAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssAideAuMenageAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAideAuMenageAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V05_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V05_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V05_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssAideAuMenageAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAideAuMenageAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_NSS_TIERS, nssAideAuMenageAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_NOM_TIERS, nomAideAuMenageAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_PRENOM_TIERS, prenomAideAuMenageAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANT_UNITAIRE, montantAideAuMenageAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalAideAuMenageAvs = montantTotalAideAuMenageAvs.add(new BigDecimal(montantAideAuMenageAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int s = 0;
            for (s = 0; s < 1; s++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_MONTANTS_TOTAUX,
                    montantTotalAideAuMenageAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_AIDE_AU_MENAGE_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V05_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS AIDE AU MENAGE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeAideAuMenageAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeAideAuMenageAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestAideAuMenageAi = listeAideAuMenageAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsAideAuMenageAi;
                elementsAideAuMenageAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestAideAuMenageAi, session);
                elementMutationTrier.add(elementsAideAuMenageAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalAideAuMenageAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssAideAuMenageAi = ligne.get(0);
                String nomAideAuMenageAi = ligne.get(1);
                String prenomAideAuMenageAi = ligne.get(2);
                String montantAideAuMenageAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssAideAuMenageAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAideAuMenageAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssAideAuMenageAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAideAuMenageAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V06_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V06_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V06_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssAideAuMenageAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantAideAuMenageAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_NSS_TIERS, nssAideAuMenageAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_NOM_TIERS, nomAideAuMenageAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_PRENOM_TIERS, prenomAideAuMenageAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANT_UNITAIRE, montantAideAuMenageAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalAideAuMenageAi = montantTotalAideAuMenageAi.add(new BigDecimal(montantAideAuMenageAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int t = 0;
            for (t = 0; t < 1; t++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_MONTANTS_TOTAUX,
                    montantTotalAideAuMenageAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_AIDE_AU_MENAGE_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V06_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS TRANSPORT AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeTransportAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeTransportAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestTransportAvs = listeTransportAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsTransportAvs;
                elementsTransportAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestTransportAvs, session);
                elementMutationTrier.add(elementsTransportAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalTransportAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssTransportAvs = ligne.get(0);
                String nomTransportAvs = ligne.get(1);
                String prenomTransportAvs = ligne.get(2);
                String montantTransportAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssTransportAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantTransportAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssTransportAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantTransportAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V07_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V07_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V07_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssTransportAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantTransportAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_NSS_TIERS, nssTransportAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_NOM_TIERS, nomTransportAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_PRENOM_TIERS, prenomTransportAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANT_UNITAIRE, montantTransportAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalTransportAvs = montantTotalTransportAvs.add(new BigDecimal(montantTransportAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int u = 0;
            for (u = 0; u < 1; u++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_MONTANTS_TOTAUX,
                    montantTotalTransportAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_TRANSPORT_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V07_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS AIDE AU MENAGE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeTransportAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeTransportAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestTransportAi = listeTransportAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsTransportAi;
                elementsTransportAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestTransportAi, session);
                elementMutationTrier.add(elementsTransportAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalTransportAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssTransportAi = ligne.get(0);
                String nomTransportAi = ligne.get(1);
                String prenomTransportAi = ligne.get(2);
                String montantTransportAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssTransportAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantTransportAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssTransportAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantTransportAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V08_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V08_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V08_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssTransportAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantTransportAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_NSS_TIERS, nssTransportAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_NOM_TIERS, nomTransportAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_PRENOM_TIERS, prenomTransportAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANT_UNITAIRE, montantTransportAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalTransportAi = montantTotalTransportAi.add(new BigDecimal(montantTransportAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int v = 0;
            for (v = 0; v < 1; v++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_MONTANTS_TOTAUX,
                    montantTotalTransportAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_TRANSPORT_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V08_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS ENCADREMENT SOCIO EDUCATIF ET SECURITAIRE AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeEncadrSocioSecuAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeEncadrSocioSecuAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestEncadrSocioSecuAvs = listeEncadrSocioSecuAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsEncadrSocioSecuAvs;
                elementsEncadrSocioSecuAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestEncadrSocioSecuAvs, session);
                elementMutationTrier.add(elementsEncadrSocioSecuAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalEncadrSocioSecuAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssEncadrSocioSecuAvs = ligne.get(0);
                String nomEncadrSocioSecuAvs = ligne.get(1);
                String prenomEncadrSocioSecuAvs = ligne.get(2);
                String montantEncadrSocioSecuAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssEncadrSocioSecuAvs;
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantEncadrSocioSecuAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssEncadrSocioSecuAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantEncadrSocioSecuAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V09_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V09_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V09_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssEncadrSocioSecuAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantEncadrSocioSecuAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_NSS_TIERS, nssEncadrSocioSecuAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_NOM_TIERS, nomEncadrSocioSecuAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_PRENOM_TIERS, prenomEncadrSocioSecuAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANT_UNITAIRE,
                        montantEncadrSocioSecuAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalEncadrSocioSecuAvs = montantTotalEncadrSocioSecuAvs.add(new BigDecimal(
                        montantEncadrSocioSecuAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int x = 0;
            for (x = 0; x < 1; x++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_MONTANTS_TOTAUX,
                    montantTotalEncadrSocioSecuAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_ENCADREMENT_SOCIO_EDUC_ET_SECU_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V09_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS ENCADREMENT SOCIO EDUCATIF ET SECURITAIRE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeEncadrSocioSecuAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeEncadrSocioSecuAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestEncadrSocioSecuAi = listeEncadrSocioSecuAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsEncadrSocioSecuAi;
                elementsEncadrSocioSecuAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestEncadrSocioSecuAi, session);
                elementMutationTrier.add(elementsEncadrSocioSecuAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalEncadrSocioSecuAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssEncadrSocioSecuAi = ligne.get(0);
                String nomEncadrSocioSecuAi = ligne.get(1);
                String prenomEncadrSocioSecuAi = ligne.get(2);
                String montantEncadrSocioSecuAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssEncadrSocioSecuAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantEncadrSocioSecuAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssEncadrSocioSecuAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantEncadrSocioSecuAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V10_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V10_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V10_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssEncadrSocioSecuAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantEncadrSocioSecuAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_NSS_TIERS, nssEncadrSocioSecuAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_NOM_TIERS, nomEncadrSocioSecuAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_PRENOM_TIERS, prenomEncadrSocioSecuAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANT_UNITAIRE,
                        montantEncadrSocioSecuAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalEncadrSocioSecuAi = montantTotalEncadrSocioSecuAi.add(new BigDecimal(
                        montantEncadrSocioSecuAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int y = 0;
            for (y = 0; y < 1; y++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_MONTANTS_TOTAUX,
                    montantTotalEncadrSocioSecuAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_ENCADREMENT_SOCIO_EDUC_ET_SECU_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V10_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS COTISATION PARITAIRE AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeCotisationParitaireAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeCotisationParitaireAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestCotisationParitaireAvs = listeCotisationParitaireAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsCotisationParitaireAvs;
                elementsCotisationParitaireAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestCotisationParitaireAvs, session);
                elementMutationTrier.add(elementsCotisationParitaireAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalCotisationParitaireAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssCotisationParitaireAvs = ligne.get(0);
                String nomCotisationParitaireAvs = ligne.get(1);
                String prenomCotisationParitaireAvs = ligne.get(2);
                String montantCotisationParitaireAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssCotisationParitaireAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantCotisationParitaireAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssCotisationParitaireAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantCotisationParitaireAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V11_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V11_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V11_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssCotisationParitaireAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantCotisationParitaireAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_NSS_TIERS, nssCotisationParitaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_NOM_TIERS, nomCotisationParitaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_PRENOM_TIERS,
                        prenomCotisationParitaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANT_UNITAIRE,
                        montantCotisationParitaireAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalCotisationParitaireAvs = montantTotalCotisationParitaireAvs.add(new BigDecimal(
                        montantCotisationParitaireAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int z = 0;
            for (z = 0; z < 1; z++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_MONTANTS_TOTAUX,
                    montantTotalCotisationParitaireAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_COTISATION_PARITAIRE_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V11_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS COTISATION PARITAIRE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeCotisationParitaireAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01, null, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeCotisationParitaireAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestCotisationParitaireAi = listeCotisationParitaireAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsCotisationParitaireAi;
                elementsCotisationParitaireAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestCotisationParitaireAi, session);
                elementMutationTrier.add(elementsCotisationParitaireAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalCotisationParitaireAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssCotisationParitaireAi = ligne.get(0);
                String nomCotisationParitaireAi = ligne.get(1);
                String prenomCotisationParitaireAi = ligne.get(2);
                String montantCotisationParitaireAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssCotisationParitaireAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantCotisationParitaireAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssCotisationParitaireAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantCotisationParitaireAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V12_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V12_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V12_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssCotisationParitaireAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantCotisationParitaireAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_NSS_TIERS, nssCotisationParitaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_NOM_TIERS, nomCotisationParitaireAi);
                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.V12_PRENOM_TIERS, prenomCotisationParitaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANT_UNITAIRE,
                        montantCotisationParitaireAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalCotisationParitaireAi = montantTotalCotisationParitaireAi.add(new BigDecimal(
                        montantCotisationParitaireAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int a = 0;
            for (a = 0; a < 1; a++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_MONTANTS_TOTAUX,
                    montantTotalCotisationParitaireAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_COTISATION_PARITAIRE_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V12_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS SOINS PAR LA FAMILLE AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeSoinsFamilleAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13,
                            IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeSoinsFamilleAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestSoinsFamilleAvs = listeSoinsFamilleAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsSoinsFamilleAvs;
                elementsSoinsFamilleAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestSoinsFamilleAvs, session);
                elementMutationTrier.add(elementsSoinsFamilleAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalSoinsFamilleAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssSoinsFamilleAvs = ligne.get(0);
                String nomSoinsFamilleAvs = ligne.get(1);
                String prenomSoinsFamilleAvs = ligne.get(2);
                String montantSoinsFamilleAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssSoinsFamilleAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantSoinsFamilleAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssSoinsFamilleAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantSoinsFamilleAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V13_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V13_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V13_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssSoinsFamilleAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantSoinsFamilleAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_NSS_TIERS, nssSoinsFamilleAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_NOM_TIERS, nomSoinsFamilleAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_PRENOM_TIERS, prenomSoinsFamilleAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANT_UNITAIRE, montantSoinsFamilleAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalSoinsFamilleAvs = montantTotalSoinsFamilleAvs.add(new BigDecimal(montantSoinsFamilleAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int b = 0;
            for (b = 0; b < 1; b++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_MONTANTS_TOTAUX,
                    montantTotalSoinsFamilleAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_SOINS_FAMILLE_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V13_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS SOINS PAR LA FAMILLE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeSoinsFamilleAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13,
                            IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeSoinsFamilleAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestSoinsFamilleAi = listeSoinsFamilleAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsSoinsFamilleAi;
                elementsSoinsFamilleAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestSoinsFamilleAi, session);
                elementMutationTrier.add(elementsSoinsFamilleAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalSoinsFamilleAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssSoinsFamilleAi = ligne.get(0);
                String nomSoinsFamilleAi = ligne.get(1);
                String prenomSoinsFamilleAi = ligne.get(2);
                String montantSoinsFamilleAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssSoinsFamilleAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantSoinsFamilleAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssSoinsFamilleAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantSoinsFamilleAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V14_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V14_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V14_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssSoinsFamilleAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantSoinsFamilleAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_NSS_TIERS, nssSoinsFamilleAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_NOM_TIERS, nomSoinsFamilleAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_PRENOM_TIERS, prenomSoinsFamilleAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANT_UNITAIRE, montantSoinsFamilleAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalSoinsFamilleAi = montantTotalSoinsFamilleAi.add(new BigDecimal(montantSoinsFamilleAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int c = 0;
            for (c = 0; c < 1; c++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_MONTANTS_TOTAUX,
                    montantTotalSoinsFamilleAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_SOINS_FAMILLE_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V14_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS UNITE D'ACCUEIL TEMPORAIRE AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeUniteAccueilTempAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12,
                            IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeUniteAccueilTempAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestUniteAccueilTempAvs = listeUniteAccueilTempAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsUniteAccueilTempAvs;
                elementsUniteAccueilTempAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestUniteAccueilTempAvs, session);
                elementMutationTrier.add(elementsUniteAccueilTempAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalUniteAccueilTempAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssUniteAccueilTempAvs = ligne.get(0);
                String nomUniteAccueilTempAvs = ligne.get(1);
                String prenomUniteAccueilTempAvs = ligne.get(2);
                String montantUniteAccueilTempAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssUniteAccueilTempAvs;
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantUniteAccueilTempAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssUniteAccueilTempAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantUniteAccueilTempAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V15_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V15_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V15_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssUniteAccueilTempAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantUniteAccueilTempAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_NSS_TIERS, nssUniteAccueilTempAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_NOM_TIERS, nomUniteAccueilTempAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_PRENOM_TIERS, prenomUniteAccueilTempAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANT_UNITAIRE,
                        montantUniteAccueilTempAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalUniteAccueilTempAvs = montantTotalUniteAccueilTempAvs.add(new BigDecimal(
                        montantUniteAccueilTempAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int d = 0;
            for (d = 0; d < 1; d++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_MONTANTS_TOTAUX,
                    montantTotalUniteAccueilTempAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_UNITE_ACCUEIL_TEMP_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V15_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS UNITE D'ACCUEIL TEMPORAIRE AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeUniteAccueilTempAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12,
                            IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeUniteAccueilTempAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestUniteAccueilTempAi = listeUniteAccueilTempAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsUniteAccueilTempAi;
                elementsUniteAccueilTempAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestUniteAccueilTempAi, session);
                elementMutationTrier.add(elementsUniteAccueilTempAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalUniteAccueilTempAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssUniteAccueilTempAi = ligne.get(0);
                String nomUniteAccueilTempAi = ligne.get(1);
                String prenomUniteAccueilTempAi = ligne.get(2);
                String montantUniteAccueilTempAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssUniteAccueilTempAi;
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantUniteAccueilTempAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssUniteAccueilTempAi)) {
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantUniteAccueilTempAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V16_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V16_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V16_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssUniteAccueilTempAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantUniteAccueilTempAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_NSS_TIERS, nssUniteAccueilTempAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_NOM_TIERS, nomUniteAccueilTempAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_PRENOM_TIERS, prenomUniteAccueilTempAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANT_UNITAIRE,
                        montantUniteAccueilTempAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalUniteAccueilTempAi = montantTotalUniteAccueilTempAi.add(new BigDecimal(
                        montantUniteAccueilTempAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int e = 0;
            for (e = 0; e < 1; e++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_MONTANTS_TOTAUX,
                    montantTotalUniteAccueilTempAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_UNITE_ACCUEIL_TEMP_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V16_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS PENSION HOME DE JOUR AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listePensionHomeJourAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12,
                            IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listePensionHomeJourAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestPensionHomeJourAvs = listePensionHomeJourAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsPensionHomeJourAvs;
                elementsPensionHomeJourAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestPensionHomeJourAvs, session);
                elementMutationTrier.add(elementsPensionHomeJourAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalPensionHomeJourAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssPensionHomeJourAvs = ligne.get(0);
                String nomPensionHomeJourAvs = ligne.get(1);
                String prenomPensionHomeJourAvs = ligne.get(2);
                String montantPensionHomeJourAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssPensionHomeJourAvs;
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantPensionHomeJourAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssPensionHomeJourAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantPensionHomeJourAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V17_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V17_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V17_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssPensionHomeJourAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation
                            .add(new BigDecimal(montantPensionHomeJourAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_NSS_TIERS, nssPensionHomeJourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_NOM_TIERS, nomPensionHomeJourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_PRENOM_TIERS, prenomPensionHomeJourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANT_UNITAIRE,
                        montantPensionHomeJourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalPensionHomeJourAvs = montantTotalPensionHomeJourAvs.add(new BigDecimal(
                        montantPensionHomeJourAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int f = 0;
            for (f = 0; f < 1; f++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_MONTANTS_TOTAUX,
                    montantTotalPensionHomeJourAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_PENSION_HOME_JOUR_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V17_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS PENSION HOME DE JOUR AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listePensionHomeJourAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12,
                            IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listePensionHomeJourAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestPensionHomeJourAi = listePensionHomeJourAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsPensionHomeJourAi;
                elementsPensionHomeJourAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestPensionHomeJourAi, session);
                elementMutationTrier.add(elementsPensionHomeJourAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalPensionHomeJourAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssPensionHomeJourAi = ligne.get(0);
                String nomPensionHomeJourAi = ligne.get(1);
                String prenomPensionHomeJourAi = ligne.get(2);
                String montantPensionHomeJourAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssPensionHomeJourAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantPensionHomeJourAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssPensionHomeJourAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantPensionHomeJourAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V18_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V18_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V18_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssPensionHomeJourAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(montantPensionHomeJourAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_NSS_TIERS, nssPensionHomeJourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_NOM_TIERS, nomPensionHomeJourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_PRENOM_TIERS, prenomPensionHomeJourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANT_UNITAIRE,
                        montantPensionHomeJourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalPensionHomeJourAi = montantTotalPensionHomeJourAi.add(new BigDecimal(
                        montantPensionHomeJourAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int g = 0;
            for (g = 0; g < 1; g++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_MONTANTS_TOTAUX,
                    montantTotalPensionHomeJourAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_PENSION_HOME_JOUR_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V18_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS PARTICIPATION COURT SEJOUR AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeParticipationCourtSejourAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12, IRFTypesDeSoins.st_12_COURT_SEJOUR,
                            false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeParticipationCourtSejourAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestParticipationCourtSejourAvs = listeParticipationCourtSejourAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsParticipationCourtSejourAvs;
                elementsParticipationCourtSejourAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestParticipationCourtSejourAvs, session);
                elementMutationTrier.add(elementsParticipationCourtSejourAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalParticipationCourtSejourAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssParticipationCourtSejourAvs = ligne.get(0);
                String nomParticipationCourtSejourAvs = ligne.get(1);
                String prenomParticipationCourtSejourAvs = ligne.get(2);
                String montantParticipationCourtSejourAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssParticipationCourtSejourAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantParticipationCourtSejourAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssParticipationCourtSejourAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantParticipationCourtSejourAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V19_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V19_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V19_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssParticipationCourtSejourAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantParticipationCourtSejourAvs));
                }

                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.V19_NSS_TIERS, nssParticipationCourtSejourAvs);
                container
                        .put(IRFListeRecapitulationPaiementsListeColumns.V19_NOM_TIERS, nomParticipationCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_PRENOM_TIERS,
                        prenomParticipationCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANT_UNITAIRE,
                        montantParticipationCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalParticipationCourtSejourAvs = montantTotalParticipationCourtSejourAvs.add(new BigDecimal(
                        montantParticipationCourtSejourAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int h = 0;
            for (h = 0; h < 1; h++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_MONTANTS_TOTAUX,
                    montantTotalParticipationCourtSejourAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_PARTICIPATION_COURT_SEJOUR_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V19_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS PARTICIPATION COURT SEJOUR AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeParticipationCourtSejourAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12, IRFTypesDeSoins.st_12_COURT_SEJOUR,
                            false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeParticipationCourtSejourAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestParticipationCourtSejourAi = listeParticipationCourtSejourAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsParticipationCourtSejourAi;
                elementsParticipationCourtSejourAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestParticipationCourtSejourAi, session);
                elementMutationTrier.add(elementsParticipationCourtSejourAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalParticipationCourtSejourAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssParticipationCourtSejourAi = ligne.get(0);
                String nomParticipationCourtSejourAi = ligne.get(1);
                String prenomParticipationCourtSejourAi = ligne.get(2);
                String montantParticipationCourtSejourAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssParticipationCourtSejourAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantParticipationCourtSejourAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssParticipationCourtSejourAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantParticipationCourtSejourAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V20_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V20_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V20_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssParticipationCourtSejourAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantParticipationCourtSejourAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_NSS_TIERS, nssParticipationCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_NOM_TIERS, nomParticipationCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_PRENOM_TIERS,
                        prenomParticipationCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANT_UNITAIRE,
                        montantParticipationCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalParticipationCourtSejourAi = montantTotalParticipationCourtSejourAi.add(new BigDecimal(
                        montantParticipationCourtSejourAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int ii = 0;
            for (ii = 0; ii < 1; ii++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_MONTANTS_TOTAUX,
                    montantTotalParticipationCourtSejourAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_PARTICIPATION_COURT_SEJOUR_AI"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V20_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS FRAIS DE PENSION COURT SEJOUR AVS
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeFraisPensionCourtSejourAvsItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAvs, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12, IRFTypesDeSoins.st_12_LONG_SEJOUR, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeFraisPensionCourtSejourAvsItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestFraisPensionCourtSejourAvs = listeFraisPensionCourtSejourAvsItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsFraisPensionCourtSejourAvs;
                elementsFraisPensionCourtSejourAvs = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestFraisPensionCourtSejourAvs, session);
                elementMutationTrier.add(elementsFraisPensionCourtSejourAvs);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalFraisPensionCourtSejourAvs = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssFraisPensionCourtSejourAvs = ligne.get(0);
                String nomFraisPensionCourtSejourAvs = ligne.get(1);
                String prenomFraisPensionCourtSejourAvs = ligne.get(2);
                String montantFraisPensionCourtSejourAvs = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssFraisPensionCourtSejourAvs;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFraisPensionCourtSejourAvs));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssFraisPensionCourtSejourAvs)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFraisPensionCourtSejourAvs));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V21_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V21_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V21_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssFraisPensionCourtSejourAvs;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFraisPensionCourtSejourAvs));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_NSS_TIERS, nssFraisPensionCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_NOM_TIERS, nomFraisPensionCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_PRENOM_TIERS,
                        prenomFraisPensionCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANT_UNITAIRE,
                        montantFraisPensionCourtSejourAvs);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalFraisPensionCourtSejourAvs = montantTotalFraisPensionCourtSejourAvs.add(new BigDecimal(
                        montantFraisPensionCourtSejourAvs));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int jj = 0;
            for (jj = 0; jj < 1; jj++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_MONTANTS_TOTAUX,
                    montantTotalFraisPensionCourtSejourAvs.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_FRAIS_DE_PENSION_COURT_SEJOUR_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V21_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));

            // MUTATIONS FRAIS DE PENSION COURT SEJOUR AI
            // =======================================
            // Création d'une liste de tous les traitements dentaire de type AVS correspondant la période saisie
            Iterator<RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation> listeFraisPensionCourtSejourAiItr = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                    .getListePrestationsPonctuelles(session, datePeriode,
                            RFXmlmlMappingListeRecapitulativePaiementsEtMutations.listeTypePrestationAi, false, false,
                            false, IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12, IRFTypesDeSoins.st_12_LONG_SEJOUR, false);

            // Parcour chaque élément de la liste pour récupérer les données
            while (listeFraisPensionCourtSejourAiItr.hasNext()) {

                RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation rfOrdVerJointPrestFraisPensionCourtSejourAi = listeFraisPensionCourtSejourAiItr
                        .next();

                // Récupère les éléments (nss, nom, prenom et montant) dans un tableau
                ArrayList<String> elementsFraisPensionCourtSejourAi;
                elementsFraisPensionCourtSejourAi = RFXmlmlMappingListeRecapitulativePaiementsEtMutations
                        .getDetailMutationsPonctuelles(rfOrdVerJointPrestFraisPensionCourtSejourAi, session);
                elementMutationTrier.add(elementsFraisPensionCourtSejourAi);
            }

            Collections.sort(elementMutationTrier, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    String nom1 = o1.get(1);
                    String prenom1 = o1.get(2);
                    String idPrestation1 = o1.get(4);

                    String nom2 = o2.get(1);
                    String prenom2 = o2.get(2);
                    String idPrestation2 = o2.get(4);

                    if (nom1.compareTo(nom2) != 0) {
                        return nom1.compareTo(nom2);
                    } else if (prenom1.compareTo(prenom2) != 0) {
                        return prenom1.compareTo(prenom2);
                    } else {
                        return idPrestation1.compareTo(idPrestation2);
                    }
                }
            });

            // Déclaration d'une variable pour addition les montants
            BigDecimal montantTotalFraisPensionCourtSejourAi = new BigDecimal(0);

            for (List<String> ligne : elementMutationTrier) {

                // Affecte chaque élément du tableau à la variable respective
                String nssFraisPensionCourtSejourAi = ligne.get(0);
                String nomFraisPensionCourtSejourAi = ligne.get(1);
                String prenomFraisPensionCourtSejourAi = ligne.get(2);
                String montantFraisPensionCourtSejourAi = ligne.get(3);

                // Affectation du premier tuple
                if (nssRequerantMutation == null) {
                    nssRequerantMutation = nssFraisPensionCourtSejourAi;
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFraisPensionCourtSejourAi));

                }
                // Sinon addition du montant si même nss
                else if (nssRequerantMutation.equals(nssFraisPensionCourtSejourAi)) {
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFraisPensionCourtSejourAi));
                }
                // Sinon insertion d'une ligne affichant le total
                else {
                    // Insertion du contenu de chaque variable dans le document
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V22_NSS_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V22_NOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V22_PRENOM_TIERS, " ");
                    container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANT_UNITAIRE, " ");

                    container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANT_TOTAL,
                            totalParRequerantMutation.toString());

                    nssRequerantMutation = nssFraisPensionCourtSejourAi;
                    totalParRequerantMutation = new BigDecimal(0);
                    totalParRequerantMutation = totalParRequerantMutation.add(new BigDecimal(
                            montantFraisPensionCourtSejourAi));
                }

                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_NSS_TIERS, nssFraisPensionCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_NOM_TIERS, nomFraisPensionCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_PRENOM_TIERS,
                        prenomFraisPensionCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANT_UNITAIRE,
                        montantFraisPensionCourtSejourAi);
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANT_TOTAL, " ");

                // Ajout du montant dans la variable des montants totaux
                montantTotalFraisPensionCourtSejourAi = montantTotalFraisPensionCourtSejourAi.add(new BigDecimal(
                        montantFraisPensionCourtSejourAi));

            }

            // Insertion du dernier total et remise à zero des variables totalParRequerantMutation et
            // nssRequerantMutation
            // et du tableau elementMutationTrier
            int kk = 0;
            for (kk = 0; kk < 1; kk++) {
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_NSS_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_NOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_PRENOM_TIERS, " ");
                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANT_UNITAIRE, " ");

                container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANT_TOTAL,
                        totalParRequerantMutation.toString());

                totalParRequerantMutation = new BigDecimal(0);
                nssRequerantMutation = null;
                elementMutationTrier = new ArrayList<ArrayList<String>>();
            }

            // Insertion du montant total en bas de tableau
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_MONTANTS_TOTAUX,
                    montantTotalFraisPensionCourtSejourAi.toString());

            // Insertion des titres de chaque colonnes/rubriques
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_ONGLET,
                    session.getLabel("EXCEL_TITRE_FRAIS_DE_PENSION_COURT_SEJOUR_AVS"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_NSS_TIERS,
                    session.getLabel("EXCEL_NSS_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_NOM_TIERS,
                    session.getLabel("EXCEL_NOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_PRENOM_TIERS,
                    session.getLabel("EXCEL_PRENOM_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_MONTANT,
                    session.getLabel("EXCEL_MONTANT_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_MONTANT_TOTAL,
                    session.getLabel("EXCEL_MONTANT_TOTAL_MUTATION"));
            container.put(IRFListeRecapitulationPaiementsListeColumns.V22_TITRE_MONTANTS_TOTAUX,
                    session.getLabel("EXCEL_MONTANTS_TOTAUX_MUTATION"));
        }

        return container;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

}
