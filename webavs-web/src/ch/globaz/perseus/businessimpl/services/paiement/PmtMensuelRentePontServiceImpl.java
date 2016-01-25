package ch.globaz.perseus.businessimpl.services.paiement;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.paiement.PmtMensuelRentePontService;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author dde
 * 
 */
public class PmtMensuelRentePontServiceImpl implements PmtMensuelRentePontService {

    public static String APPLICATION_NAME = "PFPerseus";
    public static String PARAM_NAME = "validationDecision";
    public static String VALIDATION_ACTIVE = "active";
    public static String VALIDATION_DESACTIVE = "desactive";

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
                    if (CSTypeLot.LOT_MENSUEL_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())
                            && CSEtatLot.LOT_PARTIEL.getCodeSystem().equals(lot.getSimpleLot().getEtatCs())
                            && (lotMensuel == null)) {
                        lotMensuel = lot;
                    } else {
                        if (CSTypeLot.LOT_MENSUEL_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())
                                || CSTypeLot.LOT_DECISION_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())
                                || CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(lot.getSimpleLot().getTypeLot())) {
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
                    // Création du journal et commit de la transaction
                    JournalSimpleModel journal;
                    journal = CABusinessServiceLocator.getJournalService().createJournal(
                            "Rente-pont Paiement mensuel " + getDateProchainPmt(), dateComptable);

                    // Si on a pas trouvé de lot mensuel on en créé un nouveau
                    Boolean isPaiementPartiel = false;
                    if (lotMensuel == null) {
                        // Créer le lot de paiement mensuel
                        lotMensuel = new Lot();
                        lotMensuel.getSimpleLot().setDateCreation(dateComptable);
                        lotMensuel.getSimpleLot().setDateEnvoi(dateComptable);
                        lotMensuel.getSimpleLot().setEtatCs(CSEtatLot.LOT_ERREUR.getCodeSystem());
                        lotMensuel.getSimpleLot().setIdJournal(journal.getId());
                        lotMensuel.getSimpleLot().setTypeLot(CSTypeLot.LOT_MENSUEL_RP.getCodeSystem());
                        String params[] = new String[1];
                        params[0] = getDateProchainPmt();
                        lotMensuel.getSimpleLot().setDescription(CSTypeLot.LOT_MENSUEL_RP.getDescription(params));
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

                    // Liste des RP à payer
                    List<RentePont> listRentePonts = listRentePontEnCours(getDateProchainPmt(), isPaiementPartiel);

                    Boolean hasError = false;
                    // Parcours des PCF à verser
                    for (RentePont rentePont : listRentePonts) {
                        if (!"0".equals(rentePont.getSimpleRentePont().getMontant())) {
                            String nss = "";
                            try {
                                nss = rentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                                        .getPersonneEtendue().getNumAvsActuel();
                                String idTiersBeneficiaire = rentePont.getDossier().getDemandePrestation()
                                        .getPersonneEtendue().getTiers().getId();

                                AdresseTiersDetail adr = PFUserHelper.getAdressePaiementAssure(rentePont
                                        .getSimpleRentePont().getIdTiersAdressePaiement(), rentePont
                                        .getSimpleRentePont().getIdDomaineApplicatifAdressePaiement(), dateComptable);
                                if ((adr == null) || (adr.getFields() == null)) {
                                    throw new PaiementException(
                                            "Impossible de créer l'ordre de versement sans adresse de paiement");
                                }

                                // compte annexe
                                CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService()
                                        .getCompteAnnexe(journal.getIdJournal(), idTiersBeneficiaire, IntRole.ROLE_PCF,
                                                nss, true);

                                String idExterne = getDateProchainPmt().substring(3);
                                idExterne += APISection.CATEGORIE_SECTION_PCF_PP;
                                idExterne += "000";
                                // section
                                SectionSimpleModel section = CABusinessServiceLocator.getSectionService()
                                        .getSectionByIdExterne(ca.getIdCompteAnnexe(),
                                                APISection.CATEGORIE_SECTION_PCF_PP, idExterne, journal);

                                // écriture
                                CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(journal.getId(),
                                        "Paiement mensuel rente-pont (" + nss + ")", APIEcriture.CREDIT,
                                        ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                        APIReferenceRubrique.RENTE_PONT_PRESTATION,
                                        rentePont.getSimpleRentePont().getMontant());

                                // Gestion de l'impoôt à la source
                                Float impotSource = JadeStringUtil.parseFloat(rentePont.getSimpleRentePont()
                                        .getMontantImpotSource(), 0);
                                Float montantPrestation = Float.parseFloat(rentePont.getSimpleRentePont().getMontant());
                                montantPrestation = montantPrestation - impotSource;
                                if (impotSource > 0) {
                                    // On paie un IMPOT A LA SOURCE
                                    CABusinessServiceLocator.getJournalService().addEcritureByRefRubrique(
                                            journal.getId(), "Retenue sur paiement mensuel rente-pont (" + nss + ")",
                                            APIEcriture.DEBIT, ca.getIdCompteAnnexe(), section.getId(), dateComptable,
                                            APIReferenceRubrique.PCF_IMPOT_SOURCE, impotSource.toString());
                                }

                                // ordre de versement
                                CABusinessServiceLocator.getJournalService().addOrdreVersement(journal.getId(),
                                        ca.getId(), section.getId(),
                                        adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE),
                                        dateComptable, montantPrestation.toString(), "CHF", "CHF",
                                        APIOperationOrdreVersement.VIREMENT, CAOrdreGroupe.NATURE_PCF,
                                        "Paiement mensuel rente-pont (" + nss + ")");

                                // Si c'est un paiement partiel il faut remettre la pcfa sans erreur
                                if (isPaiementPartiel) {
                                    rentePont.getSimpleRentePont().setOnError(false);
                                    rentePont = PerseusServiceLocator.getRentePontService().update(rentePont);
                                }

                                // On commit la transaction
                                JadeThread.commitSession();
                            } catch (Exception e) {
                                hasError = true;
                                // On log l'erreur
                                logSession.error(this.getClass().getName(), "Erreur : " + e.toString()
                                        + " ! idRentePont : " + rentePont.getId() + "(NSS : " + nss + ")");
                                // JadeThread.logError(this.getClass().getName(), "Erreur : " + e.toString()
                                // + " ! IdPcfAccordée : " + pcfa.getId() + "(NSS : " + nss + ")");
                                // On rollback tout ce qui aurait pu être fait en compta
                                JadeThread.rollbackSession();
                                // On marque la pcfa en erreur (en relisant la pcfa pour ne pas avoir de problème avec
                                // le
                                // blob)
                                rentePont.getSimpleRentePont().setOnError(true);
                                rentePont = PerseusServiceLocator.getRentePontService().update(rentePont);
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

    @Override
    public String getDateDernierPmt() throws PaiementException, JadePersistenceException {
        try {
            LotSearchModel searchModel = new LotSearchModel();
            searchModel.setForTypeLot(CSTypeLot.LOT_MENSUEL_RP.getCodeSystem());
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

    @Override
    public String getDateProchainPmt() throws PaiementException, JadePersistenceException {
        String dateProchainPmt = getDateDernierPmt();
        dateProchainPmt = JadeDateUtil.addMonths("01." + dateProchainPmt, 1);
        return dateProchainPmt.substring(3);
    }

    @Override
    public boolean isValidationDecisionAuthorise() throws PaiementException, JadePersistenceException {
        try {
            return PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise();
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during isValidationDecisionAuthorise : " + e.toString(),
                    e);
        }
    }

    @Override
    public boolean isValidationDecisionAuthorise(BSession session) throws PaiementException, JadePersistenceException {
        try {
            return PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise(session);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Service not available during isValidationDecisionAuthorise : " + e.toString(),
                    e);
        }
    }

    @Override
    public List<RentePont> listRentePontEnCours(String moisAnnee, Boolean onError) throws RentePontException,
            PaiementException, JadePersistenceException {
        if (!JadeDateUtil.isGlobazDateMonthYear(moisAnnee)) {
            throw new PaiementException("Unable to list rentes-pont, month is empty or not well formatted");
        }
        ArrayList<RentePont> rentespont = new ArrayList<RentePont>();
        // TODO, ne pendre en compte que les demandes en cours en payement menseul et non en retro-actif à l'aide du
        // nouveau champs !!! Attention aussi utiliser dans le paiement, voir si problème avec DAVID
        // !!!!!!!!!!!!!!!!!!!!
        RentePontSearchModel rentePontSearchModel = new RentePontSearchModel();
        // rentePontSearchModel.setForDateValable("01." + moisAnnee);
        rentePontSearchModel.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
        rentePontSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        if (onError) {
            rentePontSearchModel.setForOnError(true);
        }

        rentePontSearchModel.setForMoisEnCours(moisAnnee);

        try {
            rentePontSearchModel = PerseusServiceLocator.getRentePontService().search(rentePontSearchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PaiementException("Application Service not available : " + e.toString(), e);
        }
        for (JadeAbstractModel abstractModel : rentePontSearchModel.getSearchResults()) {
            rentespont.add((RentePont) abstractModel);
        }
        return rentespont;
    }
}
