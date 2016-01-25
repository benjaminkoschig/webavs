package ch.globaz.perseus.businessimpl.services.paiement;

import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.parameters.FWParameters;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.perseus.utils.PFUserHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSEtatPcfaccordee;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.paiement.PmtMensuelService;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dde
 * 
 */
public class PmtMensuelServiceImpl extends AbstractPmtServiceImpl implements PmtMensuelService {

    public static String APPLICATION_NAME = "PERSEUS";
    public static String PARAM_NAME = "VALDEC";
    public static String VALIDATION_ACTIVE = "active";
    public static String VALIDATION_DESACTIVE = "desactive";

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#activerValidationDecision()
     */
    @Override
    public void activerValidationDecision() throws PaiementException {
        // contrôler que tous les lots sont validés
        try {
            LotSearchModel lotSearchModel = new LotSearchModel();
            lotSearchModel.setForEtatCs(CSEtatLot.LOT_PARTIEL.getCodeSystem());
            lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);

            if (PerseusServiceLocator.getLotService().count(lotSearchModel) == 0) {
                changerValidationDecision(true);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during paiementMensuel : " + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PaiementException("JadeApplicationException during paiementMensuel : " + e.toString(), e);
        } catch (Exception e) {
            throw new PaiementException("Exception during paiementMensuel : " + e.toString(), e);
        }

    }

    private void changerValidationDecision(boolean authoriser) throws PaiementException {
        // Récupère l'élément session du thread context - Nécessaire pour
        // l'interfaçage entre nouveau et ancien framework
        try {
            BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
            if (session == null) {
                throw new PaiementException("Unable to create transaction, session not defined in thread context!");
            }

            FWParameters param = new FWParameters();
            param.setSession(session);

            param.setIdAppl(PmtMensuelServiceImpl.APPLICATION_NAME);
            param.setIdCleDiffere(PmtMensuelServiceImpl.PARAM_NAME);
            param.setIdCodeSysteme("11111111");
            param.setDateDebutValidite("01.01.2000");
            param.retrieve();

            if (authoriser) {
                param.setValeurAlpha(PmtMensuelServiceImpl.VALIDATION_ACTIVE);
            } else {
                param.setValeurAlpha(PmtMensuelServiceImpl.VALIDATION_DESACTIVE);
            }
            param.save();
        } catch (Exception e) {
            throw new PaiementException("Unable to changeValidationDecision : " + e.toString(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#desactiverValidationDecision()
     */
    @Override
    public void desactiverValidationDecision() throws PaiementException {
        changerValidationDecision(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#executerPaiementMensuel()
     */
    @Override
    public void executerPaiementMensuel(BSession session, JadeBusinessLogSession logSession) throws PaiementException,
            JadePersistenceException {
        try {
            if (this.isValidationDecisionAuthorise(session)) {
                JadeThread.logError(this.getClass().getName(),
                        "Veuillez interdire la validation des décisions avant d'exécuter le paiement mensuel");
            } else {
                // Préparation du lot
                Lot lotMensuel = null;
                String dateComptable = JadeDateUtil.getGlobazFormattedDate(new Date());
                // Voir si un lot partiel ou en erreur existe
                LotSearchModel lotSearchModel = new LotSearchModel();
                lotSearchModel.setForNotEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
                lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
                Boolean areLotsOk = true;
                for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                    Lot lot = (Lot) model;
                    // Si le lot est un lot partiel de type mensuel on le prend mais seulement si on en a pas déjà
                    // trouvé dans ce cas il y'a une erreur
                    if (CSTypeLot.LOT_MENSUEL.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())
                            && CSEtatLot.LOT_PARTIEL.getCodeSystem().equals(lot.getSimpleLot().getEtatCs())
                            && (lotMensuel == null)) {
                        lotMensuel = lot;
                    } else {
                        if (CSTypeLot.LOT_MENSUEL.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())
                                || CSTypeLot.LOT_DECISION.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())
                                || CSTypeLot.LOT_FACTURES.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
                            // Si non n'importe quel lot rente-pont qui n'est pas validé ca veut dire qu'il y'a un
                            // problème
                            areLotsOk = false;
                        }
                    }
                }
                if (!areLotsOk) {
                    JadeThread.logError(this.getClass().getName(),
                            "Le paiement mensuel ne peut pas être lancé si d'autres lots ne sont pas validés !");
                } else {
                    String moisPaiement = getDateProchainPmt();
                    // Création du journal et commit de la transaction
                    JournalSimpleModel journal;
                    journal = CABusinessServiceLocator.getJournalService().createJournal(
                            "PF Paiement mensuel " + moisPaiement, dateComptable);

                    // Si on a pas trouvé de lot mensuel on en créé un nouveau
                    Boolean isPaiementPartiel = false;
                    if (lotMensuel == null) {
                        // Créer le lot de paiement mensuel
                        lotMensuel = new Lot();
                        lotMensuel.getSimpleLot().setDateCreation(dateComptable);
                        lotMensuel.getSimpleLot().setDateEnvoi(dateComptable);
                        lotMensuel.getSimpleLot().setEtatCs(CSEtatLot.LOT_ERREUR.getCodeSystem());
                        lotMensuel.getSimpleLot().setIdJournal(journal.getId());
                        lotMensuel.getSimpleLot().setTypeLot(CSTypeLot.LOT_MENSUEL.getCodeSystem());
                        String params[] = new String[1];
                        params[0] = moisPaiement;
                        lotMensuel.getSimpleLot().setDescription(CSTypeLot.LOT_MENSUEL.getDescription(params));
                        lotMensuel.getSimpleLot().setProprietaireLot(session.getUserName());
                        lotMensuel = PerseusServiceLocator.getLotService().create(lotMensuel);
                    } else {
                        lotMensuel.getSimpleLot().setEtatCs(CSEtatLot.LOT_ERREUR.getCodeSystem());
                        lotMensuel.getSimpleLot().setDateEnvoi(dateComptable);
                        lotMensuel.getSimpleLot().setIdJournal(journal.getId());
                        lotMensuel = PerseusServiceLocator.getLotService().update(lotMensuel);
                        isPaiementPartiel = true;
                    }

                    // Commit de la première partie (mise à jour ou création du lot + création du journal)
                    JadeThread.commitSession();

                    // Liste des pcfa à payer
                    List<PCFAccordee> listPcfa = this.listPCFAencours(moisPaiement, isPaiementPartiel);
                    // Récupération des décisions d'octroi complet pour les pcfa
                    HashMap<String, Decision> listDecisions = getDecisionForPcfa(listPcfa);

                    Boolean hasError = false;
                    // Parcours des PCF à verser
                    for (PCFAccordee pcfa : listPcfa) {
                        // Pour lire le BLOB (pour la mesure de coaching)
                        pcfa = PerseusServiceLocator.getPCFAccordeeService().read(pcfa.getId());
                        // Voir si il y'a une décision pour la PCFA
                        if (!listDecisions.containsKey(pcfa.getDemande().getId())) {
                            // Si il y'en a pas log l'erreur
                            JadeThread.logError(this.getClass().getName(),
                                    "Incoéhrance dans le paiement, une pcfAccordée n'a pas de décision ! IdPcfAccordée : "
                                            + pcfa.getId());
                        } else {
                            String nss = "";
                            // Si non c'est bon on peut la payer
                            try {
                                Decision decision = listDecisions.get(pcfa.getDemande().getId());

                                nss = pcfa.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                                        .getPersonneEtendue().getNumAvsActuel();
                                String idTiersBeneficiaire = pcfa.getDemande().getDossier().getDemandePrestation()
                                        .getPersonneEtendue().getTiers().getId();

                                AdresseTiersDetail adr = PFUserHelper.getAdressePaiementAssure(decision
                                        .getSimpleDecision().getIdTiersAdressePaiement(), decision.getSimpleDecision()
                                        .getIdDomaineApplicatifAdressePaiement(), dateComptable);
                                if ((adr == null) || (adr.getFields() == null)) {
                                    throw new PaiementException(
                                            "Impossible de créer l'ordre de versement sans adresse de paiement");
                                }

                                // compte annexe
                                CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService()
                                        .getCompteAnnexe(journal.getIdJournal(), idTiersBeneficiaire, IntRole.ROLE_PCF,
                                                nss, true);

                                String idExterne = moisPaiement.substring(3);
                                idExterne += APISection.CATEGORIE_SECTION_PCF_PP;
                                idExterne += "000";
                                // section
                                SectionSimpleModel section = CABusinessServiceLocator.getSectionService()
                                        .getSectionByIdExterne(ca.getIdCompteAnnexe(),
                                                APISection.CATEGORIE_SECTION_PCF_PP, idExterne, journal);

                                // On ajoute l'écriture de la prestation dans tous les cas, après on repartira le
                                // montant entre l'impoôt à la source et les ordres de veseements

                                // Mesure de coaching dans rubrique séparée
                                BigDecimal montantTotal = new BigDecimal(pcfa.getSimplePCFAccordee().getMontant());
                                BigDecimal mesureCoaching = new BigDecimal(pcfa.getCalcul().getDonnee(
                                        OutputData.MESURE_COACHING));

                                if (montantTotal.subtract(mesureCoaching).floatValue() > 0) {
                                    CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                            journal.getId(), "Paiement mensuel PC Familles (" + nss + ")",
                                            APIEcriture.CREDIT, ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                            APIReferenceRubrique.PCF_PRESTATION,
                                            montantTotal.subtract(mesureCoaching).toString());
                                } else if (montantTotal.subtract(mesureCoaching).floatValue() < 0) {
                                    CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                            journal.getId(), "Paiement mensuel PC Familles (" + nss + ")",
                                            APIEcriture.DEBIT, ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                            APIReferenceRubrique.PCF_PRESTATION,
                                            montantTotal.subtract(mesureCoaching).toString());
                                }

                                if (mesureCoaching.floatValue() > 0) {
                                    CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                            journal.getId(), "Paiement mensuel PC Familles (" + nss + ")",
                                            APIEcriture.CREDIT, ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                            APIReferenceRubrique.PCF_MESURE_COACHING, mesureCoaching.toString());
                                }

                                // On traite les retenues
                                SimpleRetenueSearchModel searchModel = new SimpleRetenueSearchModel();
                                searchModel.setForIdPcfAccordee(pcfa.getId());
                                searchModel.setForMoisValable(moisPaiement);
                                searchModel = PerseusImplServiceLocator.getSimpleRetenueService().search(searchModel);

                                Float totalDesRetenues = new Float(0);
                                for (JadeAbstractModel model : searchModel.getSearchResults()) {
                                    SimpleRetenue retenue = (SimpleRetenue) model;
                                    Float montantRetenu = Float.parseFloat(retenue.getMontantRetenuMensuel());
                                    if (CSTypeRetenue.IMPOT_SOURCE.getCodeSystem().equals(retenue.getCsTypeRetenue())) {

                                        // On paie un IMPOT A LA SOURCE
                                        CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                                journal.getId(),
                                                "Retenue sur paiement mensuel PC Familles (" + nss + ")",
                                                APIEcriture.DEBIT, ca.getIdCompteAnnexe(), section.getId(),
                                                dateComptable, APIReferenceRubrique.PCF_IMPOT_SOURCE,
                                                retenue.getMontantRetenuMensuel());

                                    } else if (CSTypeRetenue.ADRESSE_PAIEMENT.getCodeSystem().equals(
                                            retenue.getCsTypeRetenue())
                                            || CSTypeRetenue.FACTURE_EXISTANTE.getCodeSystem().equals(
                                                    retenue.getCsTypeRetenue())) {

                                        Float resteARetenir = Float.parseFloat(retenue.getMontantTotalARetenir())
                                                - Float.parseFloat(retenue.getMontantDejaRetenu());
                                        if (resteARetenir > 0) {
                                            if (resteARetenir < montantRetenu) {
                                                montantRetenu = resteARetenir;
                                            }
                                            if (CSTypeRetenue.ADRESSE_PAIEMENT.getCodeSystem().equals(
                                                    retenue.getCsTypeRetenue())) {
                                                // On paie à une ADRESSE DE PAIEMENT
                                                AdresseTiersDetail adrAutreTiers = TIBusinessServiceLocator
                                                        .getAdresseService().getAdressePaiementTiers(
                                                                retenue.getIdTiersAdressePmt(), true,
                                                                retenue.getIdDomaineApplicatif(), dateComptable, null);
                                                if ((adrAutreTiers == null) || (adrAutreTiers.getFields() == null)) {
                                                    throw new PaiementException(
                                                            "Impossible de créer l'ordre de versement sans adresse de paiement pour la retenue");
                                                }
                                                // ordre de versement pour le tiers bénéficiaire
                                                CABusinessServiceLocator.getJournalService().addOrdreVersement(
                                                        journal.getId(),
                                                        ca.getId(),
                                                        section.getId(),
                                                        adrAutreTiers.getFields().get(
                                                                AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE),
                                                        dateComptable, montantRetenu.toString(), "CHF", "CHF",
                                                        APIOperationOrdreVersement.VIREMENT, CAOrdreGroupe.NATURE_PCF,
                                                        "Retenue sur paiement mensuel PC Familles (" + nss + ")");

                                            } else if (CSTypeRetenue.FACTURE_EXISTANTE.getCodeSystem().equals(
                                                    retenue.getCsTypeRetenue())) {
                                                // Recherche la section
                                                SectionSimpleModel sectionRetenue = CABusinessServiceLocator
                                                        .getSectionService().getSectionByIdExterne(
                                                                retenue.getIdCompteAnnexe(),
                                                                retenue.getIdTypeSection(),
                                                                retenue.getIdExterneSection(), journal);
                                                // Créer l'écriture de compensation
                                                CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                                        journal.getId(),
                                                        "Retenue sur paiement mensuel PC Familles (" + nss + ")",
                                                        APIEcriture.DEBIT, ca.getIdCompteAnnexe(), section.getId(),
                                                        dateComptable, APIReferenceRubrique.COMPENSATION_PCFAMILLES,
                                                        montantRetenu.toString());
                                                // Créer l'écriture de compensation inverse
                                                CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                                        journal.getId(),
                                                        "Retenue sur paiement mensuel PC Familles (" + nss + ")",
                                                        APIEcriture.CREDIT, retenue.getIdCompteAnnexe(),
                                                        sectionRetenue.getId(), dateComptable,
                                                        APIReferenceRubrique.COMPENSATION_PCFAMILLES,
                                                        montantRetenu.toString());
                                            }
                                            // On ajoute le montant versé au montant déjà versé
                                            Float montantDejaRetenu = Float.parseFloat(retenue.getMontantDejaRetenu())
                                                    + montantRetenu;
                                            retenue.setMontantDejaRetenu(montantDejaRetenu.toString());
                                            retenue = PerseusImplServiceLocator.getSimpleRetenueService().update(
                                                    retenue);
                                        }
                                    }

                                    totalDesRetenues += montantRetenu;
                                }

                                Float montantRestantPrestation = Float.parseFloat(pcfa.getSimplePCFAccordee()
                                        .getMontant()) - totalDesRetenues;
                                if (montantRestantPrestation < 0) {
                                    throw new PaiementException(
                                            "Le montant des retenues dépasse la prestation mensuelle");
                                }

                                // Si le montant restant est supérieur à 0 on créer l'ordre de versement pour l'assuré
                                if (montantRestantPrestation > 0) {
                                    // ordre de versement
                                    CABusinessServiceLocator.getJournalService().addOrdreVersement(journal.getId(),
                                            ca.getId(), section.getId(),
                                            adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE),
                                            dateComptable, montantRestantPrestation.toString(), "CHF", "CHF",
                                            APIOperationOrdreVersement.VIREMENT, CAOrdreGroupe.NATURE_PCF,
                                            "Paiement mensuel PC Familles (" + nss + ")");
                                }

                                // Si c'est un paiement partiel il faut remettre la pcfa sans erreur
                                if (isPaiementPartiel) {
                                    // On marque la pcfa en erreur (en relisant la pcfa pour ne pas avoir de problème
                                    // avec le blob)
                                    pcfa = PerseusServiceLocator.getPCFAccordeeService().read(pcfa.getId());
                                    pcfa.getSimplePCFAccordee().setOnError(false);
                                    pcfa = PerseusServiceLocator.getPCFAccordeeService().update(pcfa);
                                }

                                // On commit la transaction
                                JadeThread.commitSession();
                            } catch (Exception e) {
                                hasError = true;
                                // On log l'erreur
                                logSession.error(this.getClass().getName(), "Erreur : " + e.toString()
                                        + " ! IdPcfAccordée : " + pcfa.getId() + "(NSS : " + nss + ")");
                                // JadeThread.logError(this.getClass().getName(), "Erreur : " + e.toString()
                                // + " ! IdPcfAccordée : " + pcfa.getId() + "(NSS : " + nss + ")");
                                // On rollback tout ce qui aurait pu être fait en compta
                                JadeThread.rollbackSession();
                                // On marque la pcfa en erreur (en relisant la pcfa pour ne pas avoir de problème avec
                                // le
                                // blob)
                                pcfa = PerseusServiceLocator.getPCFAccordeeService().read(pcfa.getId());
                                pcfa.getSimplePCFAccordee().setOnError(true);
                                pcfa = PerseusServiceLocator.getPCFAccordeeService().update(pcfa);
                                // On commit la transaction
                                JadeThread.commitSession();
                            }
                        }
                    }// On a fini de payer les pcfa

                    CABusinessServiceLocator.getJournalService().comptabilise(journal);

                    // On s'occupe du lot
                    if (hasError) {
                        lotMensuel.getSimpleLot().setEtatCs(CSEtatLot.LOT_PARTIEL.getCodeSystem());
                    } else {
                        lotMensuel.getSimpleLot().setEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
                    }
                    lotMensuel = PerseusServiceLocator.getLotService().update(lotMensuel);
                    JadeThread.commitSession();
                }

                // Il faut encore traiter toutes les erreurs qui peuvent se passer en dehors d'un paiement pour mettre
                // le paiement en erreur complet

            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during paiementMensuel : " + e.toString(), e);
        } catch (JadeApplicationException e) {
            throw new PaiementException("JadeApplicationException during paiementMensuel : " + e.toString(), e);
        } catch (Exception e) {
            throw new PaiementException("Exception during paiementMensuel : " + e.toString(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.parametre.PmtMensuelService#getDateDernierPmt()
     */
    @Override
    public String getDateDernierPmt() throws PaiementException, JadePersistenceException {
        try {
            LotSearchModel searchModel = new LotSearchModel();
            searchModel.setForTypeLot(CSTypeLot.LOT_MENSUEL.getCodeSystem());
            searchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
            searchModel.setOrderKey(LotSearchModel.ORDER_BY_DATE_COMPTABILISATION);
            searchModel = PerseusServiceLocator.getLotService().search(searchModel);

            if (searchModel.getSize() == 0) {
                return "01.1970";
            } else {
                Lot lot = (Lot) searchModel.getSearchResults()[0];
                return lot.getSimpleLot().getDateEnvoi().substring(3);
            }

        } catch (LotException e) {
            throw new PaiementException("LotException during getDateDernierPmnt : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("LotException during getDateDernierPmnt : " + e.toString(), e);
        } catch (JadePersistenceException e) {
            throw new PaiementException("LotException during getDateDernierPmnt : " + e.toString(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.parametre.PmtMensuelService#getDateProchainPmt()
     */
    @Override
    public String getDateProchainPmt() throws PaiementException, JadePersistenceException {
        String dateProchainPmt = getDateDernierPmt();
        dateProchainPmt = JadeDateUtil.addMonths("01." + dateProchainPmt, 1);
        return dateProchainPmt.substring(3);
    }

    @Override
    public HashMap<String, Decision> getDecisionForPcfa(List<PCFAccordee> listPcfa) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Récupération des décisions d'octroi complet pour les pcfa
        HashMap<String, Decision> listDecisions = new HashMap<String, Decision>();
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        for (PCFAccordee pcfa : listPcfa) {
            decisionSearchModel.getForListIdDemande().add(pcfa.getDemande().getId());
        }
        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
        for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
            Decision decision = (Decision) model;
            if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
                listDecisions.put(decision.getDemande().getId(), decision);
            }
        }
        return listDecisions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.parametre.PmtMensuelService#isValidationDecisionAuthorise()
     */
    @Override
    public boolean isValidationDecisionAuthorise() throws PaiementException, JadePersistenceException {
        // Récupère l'élément session du thread context - Nécessaire pour
        // l'interfaçage entre nouveau et ancien framework
        BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
        if (session == null) {
            throw new PaiementException("Unable to create transaction, session not defined in thread context!");
        }

        return this.isValidationDecisionAuthorise(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.paiement.PmtMensuelService#isValidationDecisionAuthorise(globaz.globall.db
     * .BSession)
     */
    @Override
    public boolean isValidationDecisionAuthorise(BSession session) throws PaiementException, JadePersistenceException {
        try {
            FWFindParameterManager mgr = new FWFindParameterManager();
            mgr.setSession(session);
            mgr.setIdCodeSysteme("11111111");
            mgr.setIdApplParametre(PmtMensuelServiceImpl.APPLICATION_NAME);
            mgr.setIdCleDiffere(PmtMensuelServiceImpl.PARAM_NAME);
            mgr.find();

            if (mgr.isEmpty()) {
                return true;
            } else {
                FWFindParameter e = (FWFindParameter) mgr.getFirstEntity();
                String val = e.getValeurAlphaParametre();
                if (PmtMensuelServiceImpl.VALIDATION_ACTIVE.equals(val)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#listPCFAdiminuees(java.lang.String)
     */
    @Override
    public List<PCFAccordee> listPCFAdiminuees(String mois) throws PaiementException, JadePersistenceException {
        if (!JadeDateUtil.isGlobazDateMonthYear(mois)) {
            throw new PaiementException("Unable to list PCFA, month is empty or not well formatted");
        }
        ArrayList<PCFAccordee> listPcfa = new ArrayList<PCFAccordee>();
        PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
        pcfAccordeeSearchModel.setForCsEtatPCFAccordee(CSEtatPcfaccordee.VALIDE.getCodeSystem());
        // Prestations qui ont été payées pour la dernière fois le mois précédent
        String moisPrecedant = JadeDateUtil.addMonths("01." + mois, -1).substring(3);
        pcfAccordeeSearchModel.setForDateDiminution(moisPrecedant);
        pcfAccordeeSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            pcfAccordeeSearchModel = PerseusServiceLocator.getPCFAccordeeService().search(pcfAccordeeSearchModel);
        } catch (PCFAccordeeException e) {
            throw new PaiementException("PCFAccordeeException during listPCFAdiminuees#PmtMensuelServiceImpl "
                    + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during listPCFAdiminuees#PmtMensuelServiceImpl "
                    + e.toString(), e);
        }
        for (JadeAbstractModel model : pcfAccordeeSearchModel.getSearchResults()) {
            PCFAccordee pcfa = (PCFAccordee) model;
            if (!JadeNumericUtil.isEmptyOrZero(pcfa.getSimplePCFAccordee().getMontant())) {
                // Si elle n'est pas purement retroactive
                if (!JadeDateUtil.isDateMonthYearBefore(pcfa.getSimplePCFAccordee().getDateDiminution(), pcfa
                        .getSimplePCFAccordee().getDateDecision())) {
                    if (!JadeStringUtil.isEmpty(pcfa.getSimplePCFAccordee().getDateDecision())) {
                        listPcfa.add((PCFAccordee) model);
                    }
                }
            }
        }

        return listPcfa;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#listPCFAencours(java.lang.String)
     */
    @Override
    public List<PCFAccordee> listPCFAencours(String mois) throws PaiementException, JadePersistenceException {
        return this.listPCFAencours(mois, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#listPCFAencours(java.lang.String,
     * java.lang.Boolean)
     */
    @Override
    public List<PCFAccordee> listPCFAencours(String mois, Boolean onlyOnError) throws PaiementException,
            JadePersistenceException {
        if (!JadeDateUtil.isGlobazDateMonthYear(mois)) {
            throw new PaiementException("Unable to list PCFA, month is empty or not well formatted");
        }
        ArrayList<PCFAccordee> listPcfa = new ArrayList<PCFAccordee>();
        PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
        pcfAccordeeSearchModel.setForCsEtatPCFAccordee(CSEtatPcfaccordee.VALIDE.getCodeSystem());
        pcfAccordeeSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (onlyOnError) {
            pcfAccordeeSearchModel.setForOnError(true);
        }
        pcfAccordeeSearchModel.setForMoisEnCours(mois);
        try {
            pcfAccordeeSearchModel = PerseusServiceLocator.getPCFAccordeeService().search(pcfAccordeeSearchModel);
        } catch (PCFAccordeeException e) {
            throw new PaiementException("PCFAccordeeException during listPCFAdiminuees#PmtMensuelServiceImpl "
                    + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during listPCFAdiminuees#PmtMensuelServiceImpl "
                    + e.toString(), e);
        }
        for (JadeAbstractModel model : pcfAccordeeSearchModel.getSearchResults()) {
            PCFAccordee pcfa = (PCFAccordee) model;
            if (willBePaid(pcfa, mois)) {
                listPcfa.add((PCFAccordee) model);
            }
        }

        return listPcfa;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#listPCFaugmentees(java.lang.String)
     */
    @Override
    public List<PCFAccordee> listPCFaugmentees(String mois) throws PaiementException, JadePersistenceException {
        if (!JadeDateUtil.isGlobazDateMonthYear(mois)) {
            throw new PaiementException("Unable to list PCFA, month is empty or not well formatted");
        }
        ArrayList<PCFAccordee> listPcfa = new ArrayList<PCFAccordee>();
        PCFAccordeeSearchModel pcfAccordeeSearchModel = new PCFAccordeeSearchModel();
        pcfAccordeeSearchModel.setForCsEtatPCFAccordee(CSEtatPcfaccordee.VALIDE.getCodeSystem());
        pcfAccordeeSearchModel.setForDateDecision(mois);
        pcfAccordeeSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            pcfAccordeeSearchModel = PerseusServiceLocator.getPCFAccordeeService().search(pcfAccordeeSearchModel);
        } catch (PCFAccordeeException e) {
            throw new PaiementException("PCFAccordeeException during listPCFaugmentees#PmtMensuelServiceImpl "
                    + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during listPCFaugmentees#PmtMensuelServiceImpl "
                    + e.toString(), e);
        }
        for (JadeAbstractModel model : pcfAccordeeSearchModel.getSearchResults()) {
            PCFAccordee pcfa = (PCFAccordee) model;
            // SI il y'a un versement
            if (!JadeNumericUtil.isEmptyOrZero(pcfa.getSimplePCFAccordee().getMontant())) {
                // Si elle n'est pas purement retroactive
                if (JadeStringUtil.isEmpty(pcfa.getSimplePCFAccordee().getDateDiminution())
                        || !JadeDateUtil.isDateMonthYearBefore(pcfa.getSimplePCFAccordee().getDateDiminution(), mois)) {
                    // Si elle ne commence pas dans le future
                    if (!JadeDateUtil.isDateMonthYearAfter(pcfa.getDemande().getSimpleDemande().getDateDebut()
                            .substring(3), mois)) {
                        listPcfa.add((PCFAccordee) model);
                    }
                }
            }
        }

        return listPcfa;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.paiement.PmtMensuelService#willBePaid(ch.globaz.perseus.business.models.
     * pcfaccordee.PCFAccordee, java.lang.String)
     */
    @Override
    public boolean willBePaid(PCFAccordee pcfAccordee, String mois) {
        boolean willBe = false;
        // La pcfaccordée doit être validée
        if (CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(pcfAccordee.getSimplePCFAccordee().getCsEtat())) {
            // Doit avoir une date de décision
            if (!JadeStringUtil.isEmpty(pcfAccordee.getSimplePCFAccordee().getDateDecision())) {
                // Ne doit pas être après la date de diminution
                if (JadeStringUtil.isEmpty(pcfAccordee.getSimplePCFAccordee().getDateDiminution())
                        || !JadeDateUtil.isDateMonthYearBefore(pcfAccordee.getSimplePCFAccordee().getDateDiminution(),
                                mois)) {
                    // Doit avoir un montant versé
                    if (!JadeNumericUtil.isEmptyOrZero(pcfAccordee.getSimplePCFAccordee().getMontant())) {
                        String dateDebut = pcfAccordee.getDemande().getSimpleDemande().getDateDebut();
                        // Controle que la demande est en cours
                        if (!JadeDateUtil.isDateMonthYearAfter(dateDebut.substring(3), mois)) {
                            String dateFin = pcfAccordee.getDemande().getSimpleDemande().getDateFin();
                            dateFin = (JadeStringUtil.isEmpty(dateFin)) ? "12.2999" : dateFin.substring(3);
                            if (!JadeDateUtil.isDateMonthYearBefore(dateFin, mois)) {
                                willBe = true;
                            }
                        }
                    }
                }
            }
        }

        return willBe;
    }
}
