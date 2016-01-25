package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSChoixDecision;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSEtatPcfaccordee;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.models.decision.AnnexeDecision;
import ch.globaz.perseus.business.models.decision.AnnexeDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.CopieDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleCopieDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleDecision;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.demande.SimpleDemandeSearchModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.decision.DecisionService;
import ch.globaz.perseus.businessimpl.checkers.decision.DecisionChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class DecisionServiceImpl extends PerseusAbstractServiceImpl implements DecisionService {

    private String typeDecision = "";
    private String dateDebutDecision = "";
    private String dateFinDecision = "";

    @Override
    public int count(DecisionSearchModel search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count decision, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Decision create(Decision decision) throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to create decision, the given model is null!");
        }

        try {
            DecisionChecker.checkForCreate(decision);
            // Création de la decision
            SimpleDecision simpleDecision = decision.getSimpleDecision();
            simpleDecision = PerseusImplServiceLocator.getSimpleDecisionService().create(simpleDecision);
            decision.setSimpleDecision(simpleDecision);

            // Ajout copies si pas null et pas vide
            if ((decision.getListCopies() != null) && (decision.getListCopies().size() != 0)) {
                // pour chaques annexes
                for (CopieDecision copieDecision : decision.getListCopies()) {
                    copieDecision.getSimpleCopieDecision().setIdDecision(decision.getId());
                    PerseusImplServiceLocator.getCopieDecisionService().create(copieDecision);
                }
            }
            // Ajout annexes si pas null et pas vide
            if ((decision.getListAnnexes() != null) && (decision.getListAnnexes().size() != 0)) {
                // pour chaques annexes
                for (AnnexeDecision annexesDecision : decision.getListAnnexes()) {
                    annexesDecision.getSimpleAnnexeDecision().setIdDecision(decision.getId());
                    PerseusImplServiceLocator.getAnnexeDecisionService().create(annexesDecision);
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.toString());
        } catch (DemandeException e) {
            throw new DecisionException("DemandeException during decision create : " + e.toString());
        }

        return decision;
    }

    @Override
    public Decision createNewDecisionAfterProjetValidation(Decision decision) throws Exception {

        Decision newDecision = new Decision();
        newDecision.setDemande(decision.getDemande());
        // newDecision.getSimpleDecision().setCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        newDecision.getSimpleDecision().setCsEtat(CSEtatDecision.PRE_VALIDE.getCodeSystem());
        newDecision.getSimpleDecision().setDateDocument(decision.getSimpleDecision().getDateDocument());
        newDecision.getSimpleDecision().setDatePreparation(decision.getSimpleDecision().getDatePreparation());
        newDecision.getSimpleDecision().setIdDemande(decision.getSimpleDecision().getIdDemande());
        newDecision.getSimpleDecision().setIdDomaineApplicatifAdresseCourrier(
                decision.getSimpleDecision().getIdDomaineApplicatifAdresseCourrier());
        newDecision.getSimpleDecision().setIdDomaineApplicatifAdressePaiement(
                decision.getSimpleDecision().getIdDomaineApplicatifAdressePaiement());
        newDecision.getSimpleDecision().setIdTiersAdresseCourrier(
                decision.getSimpleDecision().getIdTiersAdresseCourrier());
        newDecision.getSimpleDecision().setIdTiersAdressePaiement(
                decision.getSimpleDecision().getIdTiersAdressePaiement());
        newDecision.getSimpleDecision().setNumeroDecision(decision.getSimpleDecision().getNumeroDecision());
        newDecision.getSimpleDecision().setUtilisateurPreparation(
                decision.getSimpleDecision().getUtilisateurPreparation());
        newDecision.getSimpleDecision().setMontantToucheAuRI(decision.getSimpleDecision().getMontantToucheAuRI());
        if (CSChoixDecision.POSITIVE_REPONSE.getCodeSystem().equals(decision.getSimpleDecision().getCsChoix())) {
            newDecision.getSimpleDecision().setCsTypeDecision(definitTypeDecision(newDecision, false, false));
        } else {
            newDecision.getSimpleDecision().setCsTypeDecision(CSTypeDecision.RENONCIATION.getCodeSystem());
        }
        // On reprend uniquement les copies
        newDecision.setListCopies(decision.getListCopies());
        newDecision.setListAnnexes(decision.getListAnnexes());
        newDecision = PerseusServiceLocator.getDecisionService().create(newDecision);
        newDecision = PerseusServiceLocator.getDecisionService().valider(newDecision,
                decision.getSimpleDecision().getUtilisateurValidation());
        newDecision = PerseusServiceLocator.getDecisionService().update(newDecision);
        return newDecision;
    }

    @Override
    public String definitTypeDecision(Decision decision, boolean isNewProject, boolean isNewSuppressionVolontaire)
            throws JadePersistenceException, PCFAccordeeException, JadeApplicationServiceNotAvailableException {
        String typeDecision = "";
        if (decision.getDemande().getSimpleDemande().getRefusForce()) {
            typeDecision = CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem();
        } else if (decision.getDemande().getSimpleDemande().getNonEntreeEnMatiere()) {
            typeDecision = CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem();
        } else {
            if (isNewSuppressionVolontaire) {
                typeDecision = CSTypeDecision.SUPPRESSION.getCodeSystem();
            } else {
                PCFAccordeeSearchModel pcfasm = new PCFAccordeeSearchModel();
                pcfasm.setForIdDemande(decision.getDemande().getId());
                pcfasm = PerseusServiceLocator.getPCFAccordeeService().search(pcfasm);
                if (pcfasm.getSearchResults().length == 0) {
                    typeDecision = CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem();
                } else if (pcfasm.getSearchResults().length > 0) {
                    PCFAccordee pcfAccordee = (PCFAccordee) pcfasm.getSearchResults()[0];
                    double montantAccorde = JadeStringUtil.parseDouble(pcfAccordee.getSimplePCFAccordee().getMontant(),
                            -1);
                    if (decision.getDemande().getSimpleDemande().getFromRI() && isNewProject) {
                        typeDecision = CSTypeDecision.PROJET.getCodeSystem();
                    } else {
                        if (montantAccorde > 0) {
                            typeDecision = CSTypeDecision.OCTROI_COMPLET.getCodeSystem();
                        } else if (montantAccorde == 0) {
                            typeDecision = CSTypeDecision.OCTROI_PARTIEL.getCodeSystem();
                        } else {
                            throw new PCFAccordeeException("Amount of PCFAccordee is below zero");
                        }
                    }
                }
            }
        }
        return typeDecision;
    }

    @Override
    public Decision delete(Decision decision) throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to delete a decision, the model passed is null!");
        }

        try {
            // Suppression de la decision
            decision.setSimpleDecision(PerseusImplServiceLocator.getSimpleDecisionService().delete(
                    decision.getSimpleDecision()));

            // Suppression des annexes
            SimpleAnnexeDecisionSearchModel ads = new SimpleAnnexeDecisionSearchModel();
            ads.setForIdDecision(decision.getSimpleDecision().getIdDecision());
            PerseusServiceLocator.getAnnexeDecisionService().delete(ads);
            // Suppression des copies
            SimpleCopieDecisionSearchModel cds = new SimpleCopieDecisionSearchModel();
            cds.setForIdDecision(decision.getSimpleDecision().getIdDecision());
            PerseusServiceLocator.getCopieDecisionService().delete(cds);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return decision;
    }

    @Override
    public Demande getDemandePrecedenteValideDecisionOCtroiPrecedanteForNumOFS(Demande demande)
            throws DemandeException, JadePersistenceException {
        if (demande == null) {
            throw new DemandeException("Unable to find demande precedante, the model passed is null");
        }
        Demande demandePrecedante = null;
        try {

            // Retrouver la date à laquelle on va rechercher une demande active
            String dateFin = "";
            if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) {
                // Cas où la demande sera active après
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            } else if (!JadeDateUtil.isDateMonthYearAfter(demande.getSimpleDemande().getDateFin().substring(3),
                    PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
                // Cas où la demande est purement retro active
                dateFin = demande.getSimpleDemande().getDateFin();
            } else {
                // cas normalement impossible puisque ca voudrait dire que la demande se ferme dans le future et se
                // n'est pas possible
                // mais on prend quand même le dernier paiement mensuel
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            }

            DecisionSearchModel ds = new DecisionSearchModel();
            ds.setForIdDossier(demande.getSimpleDemande().getIdDossier());
            ds.setForNotIdDemande(demande.getId());
            ds.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
            ds.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            ds.setForDateValable(dateFin);
            ds.setOrderKey(DecisionSearchModel.ORDER_BY_DATETIME_DECISION_DESC);
            ds = PerseusServiceLocator.getDecisionService().search(ds);

            // On peut considérer que la première est la dernière décision prise
            if (ds.getSize() > 0) {
                Decision decision = (Decision) ds.getSearchResults()[0];
                demandePrecedante = decision.getDemande();
            } else {
                DecisionSearchModel desearch = new DecisionSearchModel();
                desearch.setForIdDossier(demande.getSimpleDemande().getIdDossier());
                desearch.setForNotIdDemande(demande.getId());
                desearch.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                desearch.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
                desearch.setBetweenDateDebut("01.01.2011");
                desearch.setBetweenDateFin(dateFin);
                desearch.setOrderKey(DecisionSearchModel.ORDER_BY_DATE_FIN_AND_DATETIME_DECISION_DESC);
                desearch = PerseusServiceLocator.getDecisionService().search(desearch);

                if (desearch.getSize() > 0) {
                    Decision decision = (Decision) desearch.getSearchResults()[0];
                    if (!JadeStringUtil.isEmpty(decision.getDemande().getSimpleDemande().getDateFin())) {
                        demandePrecedante = decision.getDemande();
                    }

                }

            }

        } catch (Exception e) {
            throw new DemandeException("Exception during getDemandePrecedante : " + e.toString(), e);
        }

        return demandePrecedante;
    }

    @Override
    public String getNumeroDemandeCalculee(String annee) throws JadePersistenceException {
        String numeroDemandeCalculee = "";
        String cleDeCalcul = IPFConstantes.DECISION_CLE_INCREMENTATION;
        // Recuperation de l'increment
        String increment = JadePersistenceManager.incIndentifiant(cleDeCalcul);
        increment = JadeStringUtil.fillWithZeroes(increment, 6);
        numeroDemandeCalculee = annee + "-" + increment;

        return numeroDemandeCalculee;
    }

    private void putDecisionInLot(Decision decision, Boolean purementRetroactive) throws Exception {
        // Récupération du lot de rétro
        Lot lot = PerseusServiceLocator.getLotService().getLotCourant(CSTypeLot.LOT_DECISION);

        // Calcul du rétro
        BigDecimal montantRetro = new BigDecimal(PerseusServiceLocator.getDemandeService().calculerRetro(
                decision.getDemande()));

        BigDecimal montantRetroMesureCoaching = new BigDecimal(PerseusServiceLocator.getDemandeService()
                .calculerRetroMesureCoaching(decision.getDemande()));

        // Création de la prestation (négative ou positive)
        Prestation prestation = new Prestation();
        prestation.setLot(lot);
        prestation.getSimplePrestation().setIdDecisionPcf(decision.getId());
        prestation.getSimplePrestation().setDatePrestation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.DEFINITIF.getCodeSystem());
        prestation.getSimplePrestation().setMontantTotal(montantRetro.toString());
        prestation.getSimplePrestation().setMontantMesureCoaching(montantRetroMesureCoaching.toString());
        prestation.getSimplePrestation().setDateDebut(decision.getDemande().getSimpleDemande().getDateDebut());
        String dateFinPres;
        if (purementRetroactive) {
            dateFinPres = decision.getDemande().getSimpleDemande().getDateFin();
        } else {
            // Si le mois courant est le mois du paiement
            if (JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3)
                    .equals(PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt())) {
                // La date de fin de la prestation est le dernier jour du mois précédant
                dateFinPres = JadeDateUtil.addDays(
                        "01." + JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3), -1);
            } else {
                // Si non la date de fin est le dernier jour du mois précédant le prochain paiement
                // mensuel
                dateFinPres = JadeDateUtil.addDays("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt(), -1);
            }
        }
        prestation.getSimplePrestation().setDateFin(dateFinPres);
        prestation = PerseusServiceLocator.getPrestationService().create(prestation);

        if (montantRetro.intValue() > 0) {
            // Créer les ordres de versements au créanciers + bénéficiaire
            CreancierSearchModel creancierSearch = new CreancierSearchModel();
            creancierSearch.setForIdDemande(decision.getDemande().getId());
            creancierSearch = PerseusServiceLocator.getCreancierService().search(creancierSearch);
            // BZ 6449
            BigDecimal sommeCreancesAccordees = new BigDecimal("0.00");
            for (JadeAbstractModel model : creancierSearch.getSearchResults()) {
                Creancier creancier = (Creancier) model;
                OrdreVersement ordreVersement = new OrdreVersement();
                ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
                ordreVersement.getSimpleOrdreVersement().setNumFacture(
                        creancier.getSimpleCreancier().getReferencePaiement());
                ordreVersement.getSimpleOrdreVersement().setMontantVersement(
                        creancier.getSimpleCreancier().getMontantAccorde());
                ordreVersement.getSimpleOrdreVersement().setIdTiers(creancier.getSimpleCreancier().getIdTiers());
                ordreVersement.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                        creancier.getSimpleCreancier().getIdTiers());
                ordreVersement.getSimpleOrdreVersement().setIdDomaineApplication(
                        creancier.getSimpleCreancier().getIdDomaineApplicatif());
                if (CSTypeCreance.TYPE_CREANCE_ASSURANCER_SOCIALE.getCodeSystem().equals(
                        creancier.getSimpleCreancier().getCsTypeCreance())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.ASSURANCE_SOCIALE.getCodeSystem());
                } else if (CSTypeCreance.TYPE_CREANCE_TIERS.getCodeSystem().equals(
                        creancier.getSimpleCreancier().getCsTypeCreance())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.AUTRE_TIERS.getCodeSystem());
                } else if (CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(
                        creancier.getSimpleCreancier().getCsTypeCreance())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem());
                }

                ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);
                // BZ 6449
                BigDecimal sommeCreance = new BigDecimal(creancier.getSimpleCreancier().getMontantAccorde());
                sommeCreancesAccordees = sommeCreancesAccordees.add(sommeCreance);

            }
            // Contrôler la répartition des créanciers qu'elle ne soit pas plus grande que le rétro
            if (sommeCreancesAccordees.intValue() > montantRetro.intValue()) {
                JadeThread.logError(DecisionServiceImpl.class.getName(),
                        "perseus.decisions.validation.creancierArepartir");
            }
            // Verser le solde au requérant
            if (montantRetro.intValue() - sommeCreancesAccordees.intValue() > 0) {
                // BZ 6449
                BigDecimal versementBeneficiaire = new BigDecimal(montantRetro.toString());
                versementBeneficiaire = versementBeneficiaire.subtract(sommeCreancesAccordees);
                OrdreVersement ordreVersement = new OrdreVersement();
                ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
                ordreVersement.getSimpleOrdreVersement().setMontantVersement(versementBeneficiaire.toString());
                ordreVersement.getSimpleOrdreVersement().setIdTiers(
                        decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                                .getPersonneEtendue().getIdTiers());
                ordreVersement.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                        decision.getSimpleDecision().getIdTiersAdressePaiement());
                ordreVersement.getSimpleOrdreVersement().setIdDomaineApplication(
                        decision.getSimpleDecision().getIdDomaineApplicatifAdressePaiement());
                ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(CSTypeVersement.REQUERANT.getCodeSystem());

                ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);
            }
        } else if (montantRetro.intValue() < 0) {
            // Création d'un OV negatif pour la restitution de l'impot à la source
            // Cet OV ne doit être créé que dans un cas de restitution avec un montant IS versé durant la période
            String dateFin = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            // Si la date de fin de la demande est avant prendre la date de fin de la demande
            if (JadeDateUtil.isDateBefore(decision.getDemande().getSimpleDemande().getDateFin(), dateFin)) {
                dateFin = decision.getDemande().getSimpleDemande().getDateFin();
            }

            // Calcul du montant versée à l'impôt à la source durant la période
            BigDecimal montantVerseeImpotSource = new BigDecimal(PerseusServiceLocator.getDemandeService()
                    .calculerMontantVerseImpotSource(decision.getDemande()));

            // Si un montant IS a été versé
            if (montantVerseeImpotSource.intValue() > 0) {
                OrdreVersement ordreVersement = new OrdreVersement();
                ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
                ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                        CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem());
                ordreVersement.getSimpleOrdreVersement().setMontantVersement(
                        montantVerseeImpotSource.multiply(new BigDecimal(-1)).toString());
                ordreVersement.getSimpleOrdreVersement().setIdTiers(
                        decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                                .getPersonneEtendue().getIdTiers());
                ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);

            }
        }
    }

    @Override
    public Decision read(String idDecision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to read a decision, the id passed is null!");
        }
        // Lecture de la décision
        Decision decision = new Decision();
        decision.setId(idDecision);
        decision = (Decision) JadePersistenceManager.read(decision);

        // Lecture de la demande
        Demande demande = new Demande();
        demande.getSimpleDemande().setId(decision.getSimpleDecision().getIdDemande());
        demande = (Demande) JadePersistenceManager.read(demande);

        // Récupération des annexes
        AnnexeDecisionSearchModel ads = new AnnexeDecisionSearchModel();
        ads.setForIdDecision(decision.getSimpleDecision().getIdDecision());
        ads = PerseusServiceLocator.getAnnexeDecisionService().search(ads);
        for (JadeAbstractModel searchModel : ads.getSearchResults()) {
            decision.addToListAnnexes((AnnexeDecision) searchModel);
        }
        // Récupération des copies
        CopieDecisionSearchModel cds = new CopieDecisionSearchModel();
        cds.setForIdDecision(decision.getSimpleDecision().getIdDecision());
        cds = PerseusServiceLocator.getCopieDecisionService().search(cds);
        for (JadeAbstractModel searchModel : cds.getSearchResults()) {
            decision.addToListCopies((CopieDecision) searchModel);
        }
        return decision;
    }

    @Override
    public DecisionSearchModel search(DecisionSearchModel searchModel) throws JadePersistenceException,
            DecisionException {
        if (searchModel == null) {
            throw new DecisionException("Unable to search a decision, the search model passed is null!");
        }

        return (DecisionSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public Decision update(Decision decision) throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to update decision, the given model is null!");
        }

        try {
            DecisionChecker.checkForUpdate(decision);
            // Mise à jour de la decision
            SimpleDecision simpleDecision = decision.getSimpleDecision();
            simpleDecision = PerseusImplServiceLocator.getSimpleDecisionService().update(simpleDecision);
            decision.setSimpleDecision(simpleDecision);
            // ************************ ANNEXES **********************
            AnnexeDecisionSearchModel annexesDecisionSearch = new AnnexeDecisionSearchModel();
            annexesDecisionSearch.setForIdDecision(decision.getSimpleDecision().getIdDecision());
            annexesDecisionSearch = PerseusServiceLocator.getAnnexeDecisionService().search(annexesDecisionSearch);
            // si annexes deja presentes, suppression par lots
            if (annexesDecisionSearch.getSearchResults().length != 0) {
                PerseusServiceLocator.getAnnexeDecisionService().deleteByLots(annexesDecisionSearch.getSearchResults());
            }
            // ajout des nouveau objets
            for (AnnexeDecision annexe : decision.getListAnnexes()) {
                annexe.getSimpleAnnexeDecision().setIdDecision(decision.getSimpleDecision().getIdDecision());
                PerseusServiceLocator.getAnnexeDecisionService().create(annexe);
            }

            // ************************ COPIES ************************
            CopieDecisionSearchModel copieDecisionSearch = new CopieDecisionSearchModel();
            copieDecisionSearch.setForIdDecision(decision.getSimpleDecision().getIdDecision());
            copieDecisionSearch = PerseusServiceLocator.getCopieDecisionService().search(copieDecisionSearch);
            // si annexes deja presentes, suppression par lots
            if (copieDecisionSearch.getSearchResults().length != 0) {
                PerseusServiceLocator.getCopieDecisionService().deleteByLots(copieDecisionSearch.getSearchResults());

            }

            // ajout des nouveau objets
            for (CopieDecision copie : decision.getListCopies()) {
                copie.getSimpleCopieDecision().setIdDecision(decision.getSimpleDecision().getIdDecision());
                PerseusServiceLocator.getCopieDecisionService().create(copie);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.toString());
        } catch (DemandeException e) {
            throw new DecisionException("DemandeException during decision create : " + e.toString());
        }

        return decision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.decision.DecisionService#valider(ch.globaz.perseus.business.models
     * .decision.Decision, java.lang.String)
     */
    @Override
    public Decision valider(Decision decision, String user) throws JadePersistenceException, DecisionException {
        return this.valider(decision, user, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.decision.DecisionService#valider(ch.globaz.perseus.business.models
     * .decision.Decision, java.lang.String, java.lang.Boolean)
     */
    @Override
    public Decision valider(Decision decision, String user, Boolean emptyAnnoncesChangement)
            throws JadePersistenceException, DecisionException {
        if (decision == null) {
            throw new DecisionException("Unable to validate decision, the given model is null !");
        }
        if (emptyAnnoncesChangement == null) {
            throw new DecisionException("Unable to validate decision, emptyAnnoncesChangement is null !");
        }
        if (JadeStringUtil.isEmpty(user)) {
            throw new DecisionException("Unable to validate decision, user is empty !");
        }
        try {
            // Contrôle que la décisions a bien été pré-validée avant
            if (!CSEtatDecision.PRE_VALIDE.getCodeSystem().equals(decision.getSimpleDecision().getCsEtat())) {
                JadeThread.logError(DecisionServiceImpl.class.getName(),
                        "perseus.decision.simpledecision.csEtat.validation");
            }
            // Contrôler qu'il est possible de valider des décisions à ce momemnt là
            if (!PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise()) {
                JadeThread.logError(DecisionServiceImpl.class.getName(), "perseus.decision.validation.interdite");
            }
            String dateDebut = decision.getDemande().getSimpleDemande().getDateDebut();
            String dateFin = decision.getDemande().getSimpleDemande().getDateFin();

            String prochainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            // Voir si la demande commence dans le future
            if (JadeDateUtil.isDateMonthYearAfter(dateDebut.substring(3), prochainPaiement)) {
                String[] t = new String[1];
                t[0] = prochainPaiement;
                JadeThread.logError(DecisionServiceImpl.class.getName(), "perseus.decision.validation.future", t);
            }

            // Voir si la demande est purement rétroactive
            boolean purementRetroactive = false;
            if (!JadeStringUtil.isEmpty(dateFin)
                    && JadeDateUtil.isDateMonthYearBefore(dateFin.substring(3), prochainPaiement)) {
                purementRetroactive = true;
            }

            // Si la demande n'est pas purement rétroactive, faire les contrôles de dates
            if (!purementRetroactive) {
                // Contrôler la supéerposition des demandes
                boolean demandeAfermee = false;
                SimpleDemandeSearchModel simpleDemandeSearchModel = new SimpleDemandeSearchModel();
                simpleDemandeSearchModel.setForDateDebutCheck(dateDebut);
                simpleDemandeSearchModel.setForDateFinCheck(dateFin);
                simpleDemandeSearchModel.setForIdDossier(decision.getDemande().getDossier().getId());
                simpleDemandeSearchModel.setForNotIdDemande(decision.getDemande().getId());
                simpleDemandeSearchModel = PerseusImplServiceLocator.getSimpleDemandeService().search(
                        simpleDemandeSearchModel);
                for (JadeAbstractModel model : simpleDemandeSearchModel.getSearchResults()) {
                    SimpleDemande simpleDemande = (SimpleDemande) model;
                    // On prend uniquement les demandes validées
                    if (CSEtatDemande.VALIDE.getCodeSystem().equals(simpleDemande.getCsEtatDemande())) {
                        // Voir si la PCFAccordee est superposé
                        PCFAccordee pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                                simpleDemande.getId());
                        // Si la PCFAccordée n'est pas nulle
                        if (pcfAccordee != null) {
                            // Si la pcfAccordée est encore en cours
                            if (JadeStringUtil.isEmpty(pcfAccordee.getSimplePCFAccordee().getDateDiminution())) {
                                // Voir que la date de fin de la demande soit inférieure à la décision ou la date de
                                // début
                                // si celle de fin est vide
                                if (JadeStringUtil.isEmpty(dateFin)) {
                                    demandeAfermee = true;
                                } else if (!JadeDateUtil.isDateMonthYearBefore(dateFin, pcfAccordee
                                        .getSimplePCFAccordee().getDateDecision())) {
                                    demandeAfermee = true;
                                }
                            } else {
                                // Si la demande n'a pas de date de fin, il ne peut rien y avoir après le début de la
                                // demande
                                if (JadeStringUtil.isEmpty(dateFin)) {
                                    if (!JadeDateUtil.isDateBefore(
                                            pcfAccordee.getSimplePCFAccordee().getDateDecision(), dateDebut)
                                            || !JadeDateUtil.isDateBefore(pcfAccordee.getSimplePCFAccordee()
                                                    .getDateDiminution(), dateDebut)) {
                                        // Si le paiement est encore en cours
                                        if (!JadeDateUtil.isDateMonthYearBefore(pcfAccordee.getSimplePCFAccordee()
                                                .getDateDiminution(), PerseusServiceLocator.getPmtMensuelService()
                                                .getDateProchainPmt())) {
                                            demandeAfermee = true;
                                        }
                                    }
                                } else {
                                    // Si non c'est que tout a une date de finet et qu'elle est en concurrence
                                    // Sauf si la pcfaccordée n'est plus active
                                    if (!JadeDateUtil.isDateMonthYearBefore(pcfAccordee.getSimplePCFAccordee()
                                            .getDateDiminution(), PerseusServiceLocator.getPmtMensuelService()
                                            .getDateProchainPmt())) {
                                        demandeAfermee = true;
                                    }
                                }
                            }
                        } else {
                            // Si non ca doit être une décision de refus
                            // Donc il faut la fermer d'abbords si elle a pas de date de fin
                            if (JadeStringUtil.isEmpty(simpleDemande.getDateFin())) {
                                demandeAfermee = true;
                            }
                        }
                    }
                }
                if (demandeAfermee) {
                    if (decision.getDemande().getSimpleDemande().getTypeDemande()
                            .equals(CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem())
                            && (JadeStringUtil.isEmpty(decision.getDemande().getSimpleDemande().getDateFin()))) {
                        // Update de la dernière demande afin de lui mettre automatiquement une date de fin
                        Demande dernierDemande = PerseusServiceLocator.getDemandeService().getDerniereDemande(
                                decision.getDemande().getDossier().getDossier().getIdDossier());
                        dernierDemande.getSimpleDemande().setDateFin(
                                JadeDateUtil.getLastDateOfMonth(PerseusServiceLocator.getPmtMensuelService()
                                        .getDateDernierPmt()));
                        dernierDemande = PerseusServiceLocator.getDemandeService().update(dernierDemande);
                    } else {
                        JadeThread.logError(DecisionServiceImpl.class.getName(),
                                "perseus.decision.validation.demandeouverte");
                    }
                }
            }

            // Dans le cas d'une décision avec calcul, comptabilisé le rétro
            if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
                    || CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(
                            decision.getSimpleDecision().getCsTypeDecision())) {

                PCFAccordee pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                        decision.getSimpleDecision().getIdDemande());

                if (pcfAccordee == null) {
                    throw new DecisionException(
                            "Erreur : Impossible de trouvé la pcfAccordée pour valider cette décision");
                }

                // Comptabiliser le retro dans le lot
                putDecisionInLot(decision, purementRetroactive);

                // Création des QDs pour l'année de début de la demande
                // String annee = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
                String annee = dateDebut.substring(6);
                PerseusServiceLocator.getQDAnnuelleService().createOrRead(pcfAccordee, annee);

                // Changement des états
                // - Demande, PCFAccordee et décision
                pcfAccordee.getSimplePCFAccordee().setDateDecision(
                        PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                pcfAccordee.getSimplePCFAccordee().setDateTimeDecisionValidation(sdf.format(new Date()));
                // Si c'est un cas purement retroactif, on ferme directement la pcfaccordée
                if (purementRetroactive) {
                    pcfAccordee.getSimplePCFAccordee().setDateDiminution(dateFin.substring(3));
                } else {
                    // Si la demande a déjà une date de fin on la ferme aussi directement
                    if (!JadeStringUtil.isEmpty(dateFin)) {
                        pcfAccordee.getSimplePCFAccordee().setDateDiminution(dateFin.substring(3));
                    } else {
                        // Si non elle sera en cours, on ne fait rien
                    }
                }
                pcfAccordee.getSimplePCFAccordee().setCsEtat(CSEtatPcfaccordee.VALIDE.getCodeSystem());
                pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().updateValidationDecision(pcfAccordee);

                if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(
                        decision.getSimpleDecision().getCsTypeDecision())) {
                    // Attribution d'un numéro OFS a la demande lors de la validation de la décision
                    decision.getDemande()
                            .getSimpleDemande()
                            .setNumeroOFS(
                                    PerseusServiceLocator.getDemandeService().getNumeroOFSCalculeeForDemande(
                                            decision.getDemande()));
                }

            } else if ((CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(decision.getSimpleDecision()
                    .getCsTypeDecision()))
                    || (CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(decision.getSimpleDecision()
                            .getCsTypeDecision()))) {

                putDecisionInLot(decision, purementRetroactive);
                String dateFinDem = "";
                if (purementRetroactive) {
                    dateFinDem = decision.getDemande().getSimpleDemande().getDateFin();
                } else {
                    // Fermer la demande
                    dateFinDem = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                            + JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3), 1), -1);
                    // BZ 7782 Correction de la détermination de la date de fin, pour plus de détails voir le BZ
                    if (prochainPaiement.equals(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(decision.getDemande()
                            .getSimpleDemande().getDateDebut()))) {
                        dateFinDem = JadeDateUtil.addDays(JadeDateUtil.addMonths("01." + prochainPaiement, 1), -1);
                    }
                }

                decision.getDemande().getSimpleDemande().setDateFin(dateFinDem);
            } else if (CSTypeDecision.PROJET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {

                PCFAccordee pcfAccordee = PerseusServiceLocator.getPCFAccordeeService().readForDemande(
                        decision.getDemande().getSimpleDemande().getId());
                if (pcfAccordee != null) {
                    pcfAccordee.getSimplePCFAccordee().setCsEtat(CSEtatPcfaccordee.VALIDE.getCodeSystem());
                    PerseusServiceLocator.getPCFAccordeeService().update(pcfAccordee);
                }

            } else if (CSTypeDecision.RENONCIATION.getCodeSystem().equals(
                    decision.getSimpleDecision().getCsTypeDecision())) {

                decision.getDemande().getSimpleDemande()
                        .setDateFin(JadeDateUtil.addDays(decision.getDemande().getSimpleDemande().getDateDebut(), 1));
                decision.setDemande(PerseusServiceLocator.getDemandeService()
                        .update(decision.getDemande(), true, false));

            } else if (CSTypeDecision.SUPPRESSION.getCodeSystem().equals(
                    decision.getSimpleDecision().getCsTypeDecision())) {

                // Correction de bug, autoriser la suppression volontaire sur une demande déjà fermée pour pouvoir
                // générer la décision sur une ancienne demande (cas du renouvellement annuel puis renonciation en
                // javnier)
                if (JadeStringUtil.isEmpty(decision.getDemande().getSimpleDemande().getDateFin())) {
                    // Fermeture de la demande
                    String fin = JadeDateUtil.addDays(
                            JadeDateUtil.addMonths("01."
                                    + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
                    decision.getDemande().getSimpleDemande().setDateFin(fin);
                    decision.setDemande(PerseusServiceLocator.getDemandeService().update(decision.getDemande()));
                }
            }

            // Modifier l'état de la demande si elle n'est pas déjà validée (dans le cas d'un projet ou d'une
            // suppression)
            if (!CSEtatDemande.VALIDE.getCodeSystem().equals(
                    decision.getDemande().getSimpleDemande().getCsEtatDemande())) {
                decision.getDemande().getSimpleDemande().setCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                decision.getDemande().getSimpleDemande().setDateTimeDecisionValidation(sdf.format(new Date()));
                decision.setDemande(PerseusServiceLocator.getDemandeService().update(
                        decision.getDemande(),
                        CSTypeDecision.RENONCIATION.getCodeSystem().equals(
                                decision.getSimpleDecision().getCsTypeDecision()), false));
            }

            // Modifier la date de révision du dossier si c'est une demande de type révision périodique
            // Ou vider le champs annonces de changement
            if (CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem().equals(
                    decision.getDemande().getSimpleDemande().getTypeDemande())
                    || emptyAnnoncesChangement) {
                Dossier dossier = decision.getDemande().getDossier();
                if (CSTypeDemande.REVISION_PERIODIQUE.getCodeSystem().equals(
                        decision.getDemande().getSimpleDemande().getTypeDemande())) {
                    dossier.getDossier().setDateRevision(
                            JadeDateUtil.addMonths(decision.getSimpleDecision().getDateDocument(), 12));
                }
                if (emptyAnnoncesChangement) {
                    dossier.getDossier().setAnnoncesChangements("");
                }
                decision.getDemande().setDossier(PerseusServiceLocator.getDossierService().update(dossier));
            }

            // Modifier l'état de la décision
            decision.getSimpleDecision().setCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            decision.getSimpleDecision().setUtilisateurValidation(user);
            decision.getSimpleDecision().setDateValidation(JadeDateUtil.getGlobazFormattedDate(new Date()));
            decision = PerseusServiceLocator.getDecisionService().update(decision);

        } catch (DemandeException e) {
            throw new DecisionException("DemandeException during Decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (LotException e) {
            throw new DecisionException("LotException during Decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available exception during decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (PCFAccordeeException e) {
            throw new DecisionException("PCFAccordeeException during Decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (QDException e) {
            throw new DecisionException("QDException during Decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (CreancierException e) {
            throw new DecisionException("CreancierException during Decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (JadeApplicationException e) {
            throw new DecisionException("JadeApplicationException during Decision validation : "
                    + e.getMessage()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        } catch (Exception e) {
            throw new DecisionException("Exception during Decision validation : "
                    + e.toString()
                    + " NSS : "
                    + decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel(), e);
        }

        return decision;
    }

    /**
     * Retourne la décision s'appliquant pour une facture donnée, ou null si aucune décision n'a été trouvée
     * 
     * @param la
     *            facture considérée
     * @return la décision recherchée
     */
    private Decision getDecision(String idDossier, String dateFacture) throws Exception {

        // Recherche les décisions liées par date de validation et numéro de décision
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setForIdDossier(idDossier);
        decisionSearchModel.setForDateValable(dateFacture);
        decisionSearchModel.setOrderKey("dateValidationAndNumeroDecisionAndIdDecisionDesc");

        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);

        // Si une décision compatible a été trouvée, la retourner, sinon retourner null
        if (decisionSearchModel.getSearchResults().length > 0) {
            return (Decision) decisionSearchModel.getSearchResults()[0];
        }

        return null;
    }

    /**
     * Rafraîchit les informations relatives à la décision liée à la facture
     * 
     * @throws Exception
     */
    @Override
    public List<String> getInformationSurLaDecision(String idDossier, String dateFacture) throws Exception {
        // Recherche la décision correspondant à la facture
        Decision decision = getDecision(idDossier, dateFacture);

        // Réinitialiser toutes les variables
        typeDecision = "";
        dateDebutDecision = "";
        dateFinDecision = "";

        List<String> listParam = new ArrayList<String>();
        if (decision != null) {
            // Si la décision a été trouvée, assigner les variables du view bean
            SimpleDemande simpleDemande = decision.getDemande().getSimpleDemande();

            dateDebutDecision = simpleDemande.getDateDebut();
            dateFinDecision = simpleDemande.getDateFin();
            typeDecision = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                    decision.getSimpleDecision().getCsTypeDecision());

            listParam.add(dateDebutDecision);
            listParam.add(dateFinDecision);
            listParam.add(typeDecision);
        } else {
            listParam.add("");
            listParam.add("");
            listParam.add("Il n'y a pas de décision existante pour la date de la facture.");
        }

        return listParam;
    }

}
