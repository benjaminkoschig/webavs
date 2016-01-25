package ch.globaz.pegasus.businessimpl.tests.retroAndOv;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import ch.globaz.pegasus.business.constantes.IPCCreancier;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciereSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.lot.OrdreVersement;
import ch.globaz.pegasus.business.models.lot.OrdreVersementSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.CalculMontantRetroActif;
import ch.globaz.pegasus.businessimpl.tests.pcAccordee.DonneeForDonneeFinanciere;
import ch.globaz.pegasus.tests.util.LogTemplate;
import ch.globaz.pegasus.tests.util.LogTemplateReturn;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class DonneeUtilsForTestRetroAndOV {

    private static void addCreance(String montant, String idCreancier, String idPCAccordee) throws CreancierException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        CreanceAccordee creanceAccordee = new CreanceAccordee();
        creanceAccordee.getSimpleCreanceAccordee().setIdCreancier(idCreancier);
        creanceAccordee.getSimpleCreanceAccordee().setIdPCAccordee(idPCAccordee);
        creanceAccordee.getSimpleCreanceAccordee().setMontant(montant);
        PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);
    }

    private static Creancier addCreancier(String idDemande, String idTiers, String montant, String idTiersOwner)
            throws CreancierException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        Creancier creancier = new Creancier();
        creancier.getSimpleCreancier().setCsEtat(IPCDroits.CS_ETAT_CREANCE_A_PAYE);
        creancier.getSimpleCreancier().setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
        creancier.getSimpleCreancier().setIdDemande(idDemande);
        creancier.getSimpleCreancier().setIdDomaineApplicatif(
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT);
        creancier.getSimpleCreancier().setIdTiers(idTiers);
        creancier.getSimpleCreancier().setIdTiersAdressePaiement(idTiers);
        creancier.getSimpleCreancier().setIdTiersRegroupement(idTiersOwner);
        creancier.getSimpleCreancier().setMontant(montant);
        creancier = PegasusServiceLocator.getCreancierService().create(creancier);
        return creancier;
    }

    public static Creancier addCreancierWithCreance(String idDemande, String idTiersCreancier, String montantCerancier,
            String idPCAccordee, String montantCreance, String idTiersOwner) throws CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Creancier creancier = DonneeUtilsForTestRetroAndOV.addCreancier(idDemande, idTiersCreancier, montantCerancier,
                idTiersOwner);
        DonneeUtilsForTestRetroAndOV.addCreance(montantCreance, creancier.getId(), idPCAccordee);
        return creancier;
    }

    public static void cloreTaxeJournaliere(final TaxeJournaliereHome taxeJournaliereHome, final String idVersionDroit,
            final String dateCloture) throws Exception {
        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                taxeJournaliereHome.getSimpleDonneeFinanciereHeader().setDateFin(dateCloture);
                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = DonneeUtilsForTestRetroAndOV
                        .findModificateurDroitDonneeFinanciereRequerant(idVersionDroit);
                PegasusServiceLocator.getDroitService().createAndCloseTaxeJournaliereHome(
                        modificateurDroitDonneeFinanciere, taxeJournaliereHome, true);
            }
        }.run();

    }

    public static void createAndCloseRenteAI(final Droit droit, final DroitMembreFamille droitMembreFamille,
            final String montantRente, final String dateModif) throws Exception {
        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamille, montantRente, dateModif,
                IPCRenteAvsAi.CS_TYPE_RENTE_50);
    }

    public static void createAndCloseRenteAvs(final Droit droit, final DroitMembreFamille droitMembreFamille,
            final String montantRente, final String dateModif) throws Exception {
        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamille, montantRente, dateModif,
                IPCRenteAvsAi.CS_TYPE_RENTE_10);
    }

    public static void createAndCloseRenteAvsAi(final Droit droit, final DroitMembreFamille droitMembreFamille,
            final String montantRente, final String dateModif, final String csTypeRente) throws Exception {

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamille, montantRente, dateModif, null,
                csTypeRente, false);
    }

    public static void createAndCloseRenteAvsAi(final Droit droit, final DroitMembreFamille droitMembreFamille,
            final String montantRente, final String dateModif, final String dateFin, final String csTypeRente,
            final boolean forceClose) throws Exception {

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = DonneeUtilsForTestRetroAndOV
                        .findModificateurDroitDonneeFinanciereRequerant(droit.getSimpleVersionDroit()
                                .getIdVersionDroit());

                RenteAvsAiSearch renteAvsAiSearch = new RenteAvsAiSearch();
                renteAvsAiSearch.setForNumeroVersion(droit.getSimpleVersionDroit().getNoVersion());
                renteAvsAiSearch.setWhereKey("forVersioned");
                renteAvsAiSearch.setIdDroitMembreFamille(droitMembreFamille.getSimpleDroitMembreFamille().getId());
                renteAvsAiSearch = PegasusServiceLocator.getDroitService().searchRenteAvsAi(renteAvsAiSearch);
                RenteAvsAi renteAvsAiOld = (RenteAvsAi) renteAvsAiSearch.getSearchResults()[0];

                RenteAvsAi renteAvsAi = new RenteAvsAi();
                renteAvsAi.setSimpleDonneeFinanciereHeader(renteAvsAiOld.getSimpleDonneeFinanciereHeader());
                renteAvsAi.getSimpleDonneeFinanciereHeader().setDateDebut(dateModif);
                renteAvsAi.getSimpleDonneeFinanciereHeader().setDateFin(dateFin);
                renteAvsAi.getSimpleRenteAvsAi().setMontant(montantRente);
                renteAvsAi.getSimpleRenteAvsAi().setCsTypeRente(csTypeRente);

                PegasusServiceLocator.getDroitService().createAndCloseRenteAvsAi(modificateurDroitDonneeFinanciere,
                        renteAvsAi, forceClose);

            }
        }.run();
    }

    public static void createAndCloseRenteAvsToAi(final Droit droit, final DroitMembreFamille droitMembreFamille,
            final String montantRente, final String dateModif) throws Exception {
        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamille, montantRente, dateModif,
                IPCRenteAvsAi.CS_TYPE_RENTE_50);
    }

    public static void createCompteCcp(Droit droit, DroitMembreFamille droitMembreFamille, String montant,
            String dateDebut) throws Exception {
        DonneeUtilsForTestRetroAndOV.createCompteCcp(droit.getSimpleVersionDroit().getIdVersionDroit(),
                droitMembreFamille, montant, dateDebut);
    }

    public static void createCompteCcp(final String idVersionDroit, final DroitMembreFamille droitMembreFamille,
            final String montant, final String dateDebut) throws Exception {

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {

                // On refait une lecture pour ne pas avoir de probleme de concurent access
                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = DonneeUtilsForTestRetroAndOV
                        .findModificateurDroitDonneeFinanciereRequerant(idVersionDroit);

                CompteBancaireCCP ccp = new CompteBancaireCCP();
                ccp.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
                ccp.getSimpleCompteBancaireCCP().setMontant(montant);
                ccp.getSimpleCompteBancaireCCP().setIban("CHkk BBBB BCCC CCCC CCCC C");
                ccp.getSimpleCompteBancaireCCP().setPartProprieteDenominateur("1");
                ccp.getSimpleCompteBancaireCCP().setPartProprieteNumerateur("1");
                ccp.getSimpleCompteBancaireCCP().setCsTypePropriete("64009001");
                PegasusServiceLocator.getDroitService().createCompteBancaireCCP(modificateurDroitDonneeFinanciere,
                        droitMembreFamille, ccp);

            }
        }.run();

    }

    public static void createContratEntretienViager(final Droit droit, final DroitMembreFamille droitMembreFamille,
            final String montant, final String dateDebut) throws Exception {
        DonneeUtilsForTestRetroAndOV.createContratEntretienViager(droit.getSimpleVersionDroit().getIdVersionDroit(),
                droitMembreFamille, montant, dateDebut);
    }

    public static void createContratEntretienViager(final String idVersionDroit,
            final DroitMembreFamille droitMembreFamille, final String montant, final String dateDebut) throws Exception {
        // DonneeUtilsForTestRetroAndOV.displayLog();
        //
        // DonneeUtilsForTestRetroAndOV.displayLog();

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {

                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = DonneeUtilsForTestRetroAndOV
                        .findModificateurDroitDonneeFinanciereRequerant(idVersionDroit);

                ContratEntretienViager contratEntretienViager = new ContratEntretienViager();
                contratEntretienViager.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
                contratEntretienViager.getSimpleContratEntretienViager().setMontantContrat(montant);

                PegasusServiceLocator.getDroitService().createContratEntretienViager(modificateurDroitDonneeFinanciere,
                        droitMembreFamille, contratEntretienViager);

            }
        }.run();

    }

    // new
    public static List<DecisionApresCalcul> createDecisionAc(final String idVersionDroit, final String dateDecision)
            throws Exception {
        final List<DecisionApresCalcul> list = new ArrayList<DecisionApresCalcul>();

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {

                DecisionApresCalcul decisionApresCalcul = new DecisionApresCalcul();

                decisionApresCalcul.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                        idVersionDroit));
                decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().setDateDecision(dateDecision);

                decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                        .setPreparationPar(BSessionUtil.getSessionFromThreadContext().getUserId());

                // Recherche si decision apres calcul existante pour cette version du droit en courant --> pour retro
                DecisionApresCalculSearch decisionSearch = new DecisionApresCalculSearch();
                decisionSearch.setForIdVersionDroit(decisionApresCalcul.getVersionDroit().getSimpleVersionDroit()
                        .getIdVersionDroit());

                PegasusServiceLocator.getDecisionApresCalculService().createStandardDecision(decisionApresCalcul);

                // Recherche les decisions et les prés valide
                DecisionApresCalculSearch decisionApresCalculSearch = new DecisionApresCalculSearch();
                decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
                decisionApresCalculSearch = PegasusServiceLocator.getDecisionApresCalculService().search(
                        decisionApresCalculSearch);
                for (JadeAbstractModel model : decisionApresCalculSearch.getSearchResults()) {
                    DecisionApresCalcul dec = (DecisionApresCalcul) model;
                    list.add(dec);
                    PegasusServiceLocator.getValidationDecisionService().preValidDecisionApresCalcul(dec);
                    displayLog();
                }

                PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                        idVersionDroit, false);

            }
        }.run();
        return list;
    }

    public static List<DecisionApresCalcul> createDecisionAcCaluleAndPreValide(final Droit droit)
            throws PmtMensuelException, JadeApplicationServiceNotAvailableException, Exception {
        return DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndPreValide(droit.getSimpleVersionDroit()
                .getIdVersionDroit(), "01." + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());

    }

    public static List<DecisionApresCalcul> createDecisionAcCaluleAndPreValide(final String idVersionDroit)
            throws PmtMensuelException, JadeApplicationServiceNotAvailableException, Exception {
        return DonneeUtilsForTestRetroAndOV.createDecisionApresClaculeAndPreValide(idVersionDroit, "01."
                + PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());

    }

    public static List<DecisionApresCalcul> createDecisionApresClaculeAndPreValide(final String idVersionDroit,
            final String dateDecision) throws Exception {
        final List<DecisionApresCalcul> list = new ArrayList<DecisionApresCalcul>();

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {

                DecisionApresCalcul decisionApresCalcul = new DecisionApresCalcul();

                decisionApresCalcul.setVersionDroit(PegasusServiceLocator.getDroitService().readVersionDroit(
                        idVersionDroit));
                decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().setDateDecision(dateDecision);

                decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                        .setPreparationPar(BSessionUtil.getSessionFromThreadContext().getUserId());

                // Recherche si decision apres calcul existante pour cette version du droit en courant --> pour retro
                DecisionApresCalculSearch decisionSearch = new DecisionApresCalculSearch();
                decisionSearch.setForIdVersionDroit(decisionApresCalcul.getVersionDroit().getSimpleVersionDroit()
                        .getIdVersionDroit());

                PegasusServiceLocator.getDecisionApresCalculService().createStandardDecision(decisionApresCalcul);

                // Recherche les decisions et les prés valide
                DecisionApresCalculSearch decisionApresCalculSearch = new DecisionApresCalculSearch();
                decisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
                decisionApresCalculSearch = PegasusServiceLocator.getDecisionApresCalculService().search(
                        decisionApresCalculSearch);
                for (JadeAbstractModel model : decisionApresCalculSearch.getSearchResults()) {
                    DecisionApresCalcul dec = (DecisionApresCalcul) model;
                    list.add(dec);
                    PegasusServiceLocator.getValidationDecisionService().preValidDecisionApresCalcul(dec);
                    displayLog();
                }

                // PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                // idVersionDroit);

            }
        }.run();
        return list;
    }

    // new
    public static DonneeForDonneeFinanciere createDemandDossierWithRenteAndExecuteCalcule(String montantRente,
            String nss, String dateDepotDemande) throws Exception {

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(nss,
                dateDepotDemande);

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
                montantRente);

        PegasusServiceLocator.getDroitService().calculerDroit(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), true, null);

        return data;
    }

    public static DonneeForDonneeFinanciere createDemandDossierWithRenteAndExecuteCalculeAndVerifRetro(
            double montantTotalRetro, String montantRente, String nss, String dateDepotDemande) throws Exception {

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(nss,
                dateDepotDemande);

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
                montantRente);

        DonneeUtilsForTestRetroAndOV.executeCalculeAndVerifRetro(montantTotalRetro, data.getDroit());
        return data;
    }

    private static Demande createDemande(Dossier dossier, String dateDepot, String idGestionnaire) throws Exception {
        Demande demande = new Demande();
        demande.setDossier(dossier);
        demande.getSimpleDemande().setIdDossier(dossier.getDossier().getIdDossier());
        demande.getSimpleDemande().setDateDepot(dateDepot);
        // demande.getSimpleDemande().setDateDebut(dateDepot);
        demande.getSimpleDemande().setDateProchaineRevision("01.2050");
        demande.getSimpleDemande().setIdGestionnaire(idGestionnaire);
        demande.getSimpleDemande().setTypeDemande(IPCDemandes.CS_FORM_OFFI_ATTEST);

        demande = PegasusServiceLocator.getDemandeService().create(demande);
        return demande;
    }

    private static Dossier createDossier(String idTiersRequerant, String idGestionnaire) throws Exception {
        Dossier dossier = new Dossier();
        dossier.getDemandePrestation().getPersonneEtendue().getTiers().setId(idTiersRequerant);
        dossier.getDossier().setIdGestionnaire(idGestionnaire);
        dossier = PegasusServiceLocator.getDossierService().create(dossier);
        return dossier;
    }

    public static DonneeForDonneeFinanciere createDossierAndDemandeAndDroitInitial(final String nss,
            final String dateDepotDemande) throws Exception {
        final DonneeForDonneeFinanciere donneeFinanciere = new DonneeForDonneeFinanciere();
        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                String idGestionnaire = BSessionUtil.getSessionFromThreadContext().getUserId();
                // PersonneEtendueComplexModel personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService()
                // .read(idTiersRequerant);

                PersonneEtendueComplexModel personneEtendue = DonneeUtilsForTestRetroAndOV.findIdTiersByNss(nss);
                donneeFinanciere.setIdTiersRequerant(personneEtendue.getTiers().getIdTiers());

                Dossier dossier = DonneeUtilsForTestRetroAndOV.createDossier(donneeFinanciere.getIdTiersRequerant(),
                        idGestionnaire);
                dossier.getDemandePrestation().setPersonneEtendue(personneEtendue);
                displayLog();
                Demande demande = DonneeUtilsForTestRetroAndOV.createDemande(dossier, dateDepotDemande, idGestionnaire);
                displayLog();
                Droit droit = DonneeUtilsForTestRetroAndOV.createDroitIinit(demande);
                displayLog();

                donneeFinanciere.setIdTiersRequerant(personneEtendue.getTiers().getIdTiers());
                donneeFinanciere.setDemande(demande);
                donneeFinanciere.setDossier(dossier);
                donneeFinanciere.setDroit(droit);

            }
        }.run();

        return donneeFinanciere;
    }

    private static Droit createDroitIinit(Demande demande) throws Exception {
        Droit droit = PegasusServiceLocator.getDroitService().createDroitInitial(demande);
        return droit;
    }

    public static void createRenteAvsAi(DroitMembreFamille droitMembreFamille, Droit droit, String montant,
            String dateDebut, String typeRente) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException {

        // On refait une lecture pour ne pas avoir de probleme de concurent access
        ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = DonneeUtilsForTestRetroAndOV
                .findModificateurDroitDonneeFinanciereRequerant(droit.getSimpleVersionDroit().getIdVersionDroit());

        RenteAvsAi renteAvsAi = new RenteAvsAi();
        renteAvsAi.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
        renteAvsAi.getSimpleRenteAvsAi().setMontant(montant);
        renteAvsAi.getSimpleRenteAvsAi().setCsTypeRente(typeRente);

        PegasusServiceLocator.getDroitService().createRenteAvsAi(modificateurDroitDonneeFinanciere, droitMembreFamille,
                renteAvsAi);
    }

    private static void createRenteAvsAiAndUpdateDonneesPersonnel(Droit droit, String montant,
            String idLocaliteDernierDomicilelegale, String typeRente) throws Exception {

        List<DonneesPersonnelles> list = DonneeUtilsForTestRetroAndOV.findDonneesPersonnelles(droit);

        for (DonneesPersonnelles donneesPersonnelles : list) {
            DonneeUtilsForTestRetroAndOV.updateDonneesPersonnel(idLocaliteDernierDomicilelegale, donneesPersonnelles);

            DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV
                    .readDroitMembreFamille(donneesPersonnelles);

            DonneeUtilsForTestRetroAndOV.createRenteAvsAi(droitMembreFamille, droit, montant,
                    DonneeUtilsForTestRetroAndOV.getDateMonth(), typeRente);
        }
    }

    public static void createRentes(Droit droit, String montant) throws Exception {
        DonneeUtilsForTestRetroAndOV.createRentes(droit.getSimpleVersionDroit().getIdVersionDroit(), montant);
    }

    public static void createRentes(final String idVersionDroit, final String montant) throws Exception {
        DonneeUtilsForTestRetroAndOV.createRentesForAllMembreFamille(idVersionDroit, montant,
                IPCRenteAvsAi.CS_TYPE_RENTE_10);
    }

    public static void createRentesAI(final String idVersionDroit, final String montant) throws Exception {
        DonneeUtilsForTestRetroAndOV.createRentesForAllMembreFamille(idVersionDroit, montant,
                IPCRenteAvsAi.CS_TYPE_RENTE_50);
    }

    /*
     * Sette la localite pour le noirmont
     */
    public static void createRentesForAllMembreFamille(final String idVersionDroit, final String montant,
            final String typeRente) throws Exception {
        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(idVersionDroit);
                DonneeUtilsForTestRetroAndOV
                        .createRenteAvsAiAndUpdateDonneesPersonnel(droit, montant, "436", typeRente);
            }
        }.run();
    }

    public static TaxeJournaliereHome createTaxeJournaliere(Droit droit, DroitMembreFamille droitMembreFamille,
            String idHome, String idTypeChambre, final String dateDebut) throws Exception {
        return DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(droit.getSimpleVersionDroit().getIdVersionDroit(),
                droitMembreFamille, idHome, idTypeChambre, dateDebut);
    }

    public static TaxeJournaliereHome createTaxeJournaliere(final String idVersionDroit,
            final DroitMembreFamille droitMembreFamille, final String idHome, final String idTypeChambre,
            final String dateDebut) throws Exception {

        return ((TaxeJournaliereHome) new LogTemplateReturn() {

            @Override
            protected Object execute() throws Exception {
                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = DonneeUtilsForTestRetroAndOV
                        .findModificateurDroitDonneeFinanciereRequerant(idVersionDroit);

                TaxeJournaliereHome taxeJournaliereHome = new TaxeJournaliereHome();
                taxeJournaliereHome.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
                taxeJournaliereHome.getSimpleTaxeJournaliereHome().setIdHome(idHome);
                taxeJournaliereHome.getSimpleTaxeJournaliereHome().setIdTypeChambre(idTypeChambre);

                return PegasusServiceLocator.getDroitService().createTaxeJournaliereHome(
                        modificateurDroitDonneeFinanciere, droitMembreFamille, taxeJournaliereHome);
            }

        }.run());

    }

    public static void executeCalcule(final Droit droit) throws Exception {
        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                // on clacule le retro (true)
                PegasusServiceLocator.getDroitService().calculerDroit(
                        droit.getSimpleVersionDroit().getIdVersionDroit(), true, null);

                PegasusServiceLocator.getPCAccordeeService().getCalculMontantRetroActif(
                        droit.getDemande().getSimpleDemande().getIdDemande(),
                        droit.getSimpleVersionDroit().getNoVersion());
            }
        }.run();
    }

    public static void executeCalculeAndVerifRetro(final double montantExpected, final Droit droit) throws Exception {

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                // on clacule le retro (true)
                PegasusServiceLocator.getDroitService().calculerDroit(
                        droit.getSimpleVersionDroit().getIdVersionDroit(), true, null);

                CalculMontantRetroActif calculMontantRetroActif = PegasusServiceLocator.getPCAccordeeService()
                        .getCalculMontantRetroActif(droit.getDemande().getSimpleDemande().getIdDemande(),
                                droit.getSimpleVersionDroit().getNoVersion());

                double montantRetro = calculMontantRetroActif.getTotalRetro().doubleValue();

                Assert.assertEquals("Retro: " + "Montant retro calculé", montantExpected, montantRetro);
            }
        }.run();

    }

    public static Droit findCurrentDroitByIdTiers(String idTiersRequerant) throws JadePersistenceException,
            DroitException, JadeApplicationServiceNotAvailableException {
        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForIdTiers(idTiersRequerant);
        droitSearch.setWhereKey(DroitSearch.CURRENT_VERSION);

        PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        Droit droit = (Droit) droitSearch.getSearchResults()[0];
        return droit;
    }

    public static Droit findCurrentDroitByNss(String nss) throws Exception {
        // PersonneEtendueComplexModel personneEtendue = DonneeUtilsForTestRetroAndOV.findIdTiersByNss(nss);

        DroitSearch droitSearch = new DroitSearch();
        droitSearch.setForNss(nss);
        droitSearch.setWhereKey(DroitSearch.CURRENT_VERSION);

        PegasusServiceLocator.getDroitService().searchDroit(droitSearch);
        Droit droit = (Droit) droitSearch.getSearchResults()[0];

        return droit;
    }

    public static List<DonneesPersonnelles> findDonneesPersonnelles(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DonneesPersonnellesSearch search = DonneeUtilsForTestRetroAndOV.searchDonneesPersonneByDroit(droit);
        List<DonneesPersonnelles> list = new ArrayList<DonneesPersonnelles>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            DonneesPersonnelles donneesPersonnelles = (DonneesPersonnelles) model;
            list.add(donneesPersonnelles);
        }
        return list;
    }

    private static DroitMembreFamille findDroitMembreFamille(String idDroit, String csRoleFamille)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DroitMembreFamilleSearch search = new DroitMembreFamilleSearch();
        search.setForIdDroit(idDroit);
        List<String> list = new ArrayList<String>();
        list.add(csRoleFamille);
        search.setForCsRoletMembreFamilleIn(list);
        search = PegasusServiceLocator.getDroitService().searchDroitMembreFamille(search);
        return (DroitMembreFamille) search.getSearchResults()[0];
    }

    public static DroitMembreFamille findDroitMembreFamilleConjoint(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(droit.getSimpleDroit().getIdDroit());
    }

    public static DroitMembreFamille findDroitMembreFamilleConjoint(String idDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return DonneeUtilsForTestRetroAndOV.findDroitMembreFamille(idDroit, IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    public static DroitMembreFamille findDroitMembreFamilleRequerant(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(droit.getSimpleDroit().getIdDroit());
    }

    public static DroitMembreFamille findDroitMembreFamilleRequerant(String idDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return DonneeUtilsForTestRetroAndOV.findDroitMembreFamille(idDroit, IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
    }

    public static PersonneEtendueComplexModel findIdTiersByNss(String nss) throws Exception {

        PersonneEtendueSearchComplexModel model = new PersonneEtendueSearchComplexModel();
        model.setForNumeroAvsActuel(nss);
        model = TIBusinessServiceLocator.getPersonneEtendueService().find(model);
        if (model.getSearchResults().length != 1) {
            throw new Exception("Tiers non trouvé avec ce NSS:" + nss);
        }

        return ((PersonneEtendueComplexModel) model.getSearchResults()[0]);
    }

    private static ModificateurDroitDonneeFinanciere findModificateurDroitDonneeFinanciereRequerant(
            String idVersionDroit) throws DroitException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        ModificateurDroitDonneeFinanciereSearch droitSearch = new ModificateurDroitDonneeFinanciereSearch();
        // droitSearch.setForIdDroit(droit.getId());
        droitSearch.setForIdVersionDroit(idVersionDroit);
        droitSearch.setForRoleMembreFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        droitSearch = PegasusServiceLocator.getDroitService().searchDroitDonneeFinanciere(droitSearch);

        ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = null;
        if (droitSearch.getSize() == 1) {
            modificateurDroitDonneeFinanciere = (ModificateurDroitDonneeFinanciere) droitSearch.getSearchResults()[0];
        } else {
            throw new DroitException("can't retrieve droit. " + droitSearch.getSize()
                    + " element(s) was found for the same no version droit and id droit");
        }
        return modificateurDroitDonneeFinanciere;
    }

    /**
     * Permet de rechercher les pca pour une version de droit trié par date de début
     * 
     * @param Droit
     * @return
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static List<PCAccordee> findPCa(Droit droit) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return DonneeUtilsForTestRetroAndOV.findPCa(droit.getSimpleVersionDroit().getIdVersionDroit());
    }

    /**
     * Permet de rechercher les pca pour une version de droit trié par date de début
     * 
     * @param idVersionDroit
     * @return
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static List<PCAccordee> findPCa(String idVersionDroit) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PCAccordeeSearch pcAccordeeSearch = new PCAccordeeSearch();
        pcAccordeeSearch.setForVersionDroit(idVersionDroit);
        pcAccordeeSearch.setOrderKey(PCAccordeeSearch.ORDER_BY_DATE_DEBUT);
        pcAccordeeSearch = PegasusServiceLocator.getPCAccordeeService().search(pcAccordeeSearch);
        List<PCAccordee> list = new ArrayList<PCAccordee>();

        for (JadeAbstractModel model : pcAccordeeSearch.getSearchResults()) {
            list.add((PCAccordee) model);
        }

        Collections.reverse(list);

        return list;
    }

    public static String getDate() {
        return DonneeUtilsForTestRetroAndOV.getDate(-12);
    }

    public static String getDate(int month) {
        return JadeDateUtil.addMonths(JACalendar.todayJJsMMsAAAA(), month);
    }

    public static String getDateMonth() {
        return DonneeUtilsForTestRetroAndOV.getDateMonth(-12);
    }

    public static String getDateMonth(int month) {
        return DonneeUtilsForTestRetroAndOV.getDate(month).substring(3);
    }

    private static DroitMembreFamille readDroitMembreFamille(DonneesPersonnelles donneesPersonnelles)
            throws DroitException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        DroitMembreFamille droitMembreFamille = PegasusServiceLocator.getDroitService().readDroitMembreFamille(
                donneesPersonnelles.getDroitMbrFam().getIdDroitMembreFamille());
        return droitMembreFamille;
    }

    public static Map<String, List<OrdreVersement>> searchAndGroupByType(String idVersionDroit)
            throws OrdreVersementException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        OrdreVersementSearch ordreVersementSearch = new OrdreVersementSearch();
        ordreVersementSearch.setForIdVersionDroit(idVersionDroit);

        Map<String, List<OrdreVersement>> mapOv = PegasusServiceLocator.getOrdreVersementService()
                .searchAndGroupByType(ordreVersementSearch);
        return mapOv;
    }

    public static Map<String, List<OrdreVersement>> searchAndGroupByTypeAndTestNbFound(Droit droit, int nbOvExcpected)
            throws OrdreVersementException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit.getSimpleVersionDroit()
                .getIdVersionDroit(), nbOvExcpected);
    }

    public static Map<String, List<OrdreVersement>> searchAndGroupByTypeAndTestNbFound(String idVersionDroit,
            int nbOvExcpected) throws OrdreVersementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByType(idVersionDroit);

        int nb = 0;
        for (String key : mapOv.keySet()) {
            List<OrdreVersement> listOv = mapOv.get(key);
            nb = nb + listOv.size();
        }

        if ((mapOv.size() > 0) || (nbOvExcpected == 0)) {
            Assert.assertEquals("Nombre d'ordre de versement: ", nbOvExcpected, nb);
        } else {
            Assert.fail("Auncun ordre de versement n'a été trouvé");
        }

        return mapOv;
    }

    private static DonneesPersonnellesSearch searchDonneesPersonneByDroit(Droit droit) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        DonneesPersonnellesSearch search = new DonneesPersonnellesSearch();
        search.setForIdDroit(droit.getId());
        search.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
        search = PegasusServiceLocator.getDroitService().searchDonneesPersonnelles(search);
        return search;
    }

    // public static Map<String, List<OrdreVersement>> searchOvAndGroupByDecision(String idVersionDroit)
    // throws OrdreVersementException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
    // OrdreVersementSearch search = new OrdreVersementSearch();
    // search.setForIdVersionDroit(idVersionDroit);
    // search.setOrderKey(OrdreVersementSearch.ORDRE_BY_IDLOT_CS_TYPE);
    // if (search.getSize() == 0) {
    // search = PegasusServiceLocator.getOrdreVersementService().search(search);
    // }
    // List<OrdreVersement> listOv = PersistenceUtil.typeSearch(search, search.whichModelClass());
    //
    // Map<String, List<OrdreVersement>> hashMap = JadeListUtil.groupBy(listOv,
    // new JadeListUtil.Key<OrdreVersement>() {
    // public String exec(OrdreVersement e) {
    // return e.getSimplePrestation().getIdDecisionHeader();
    // }
    // });
    //
    // return hashMap;
    //
    // }

    private static BigDecimal sommeOv(List<OrdreVersement> listOvBeneFiciairePrincipale) {
        BigDecimal montantTotalOvBeneficiaire = new BigDecimal(0);

        if (listOvBeneFiciairePrincipale != null) {
            for (OrdreVersement ov : listOvBeneFiciairePrincipale) {
                montantTotalOvBeneficiaire = montantTotalOvBeneficiaire.add(new BigDecimal(ov.getSimpleOrdreVersement()
                        .getMontant()));
            }
        }

        return montantTotalOvBeneficiaire;
    }

    // public static void testOvBeneficiairePrincipal(Map<String, List<OrdreVersement>> mapOv, Double
    // montantBeneficiaire,
    // Double montantBeneficiaireConjoint, int nbOv) {
    //
    // DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, montantBeneficiaire,
    // montantBeneficiaireConjoint, nbOv, null);
    // }

    public static void testOvBeneficiairePrincipal(Map<String, List<OrdreVersement>> mapOv, int nbOv,
            double montantBeneficiaire) {
        DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, nbOv, montantBeneficiaire, null, null);
    }

    /**
     * La clé de la map des ov doit correspondre au au type de l'ov
     * 
     * @param mapOv
     * @param nbOv
     * @param montantRetro
     * @param nbOvRestitution
     * @param montantRestitution
     */
    public static void testOvBeneficiairePrincipal(Map<String, List<OrdreVersement>> mapOv, int nbOv,
            Double montantRetro, Integer nbOvRestitution, Double montantRestitution) {

        List<OrdreVersement> listOvBeneFiciairePrincipale = mapOv
                .get(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);

        List<OrdreVersement> listOvRestitution = mapOv.get(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);

        if (listOvBeneFiciairePrincipale != null) {
            BigDecimal montantTotalOv = DonneeUtilsForTestRetroAndOV.sommeOv(listOvBeneFiciairePrincipale);
            BigDecimal montantTotalRestitution = DonneeUtilsForTestRetroAndOV.sommeOv(listOvRestitution);

            if (nbOvRestitution != null) {
                if (listOvRestitution != null) {
                    montantTotalOv = montantTotalOv.subtract(montantTotalRestitution);
                    if ((listOvRestitution.size() != listOvBeneFiciairePrincipale.size()) && (nbOvRestitution == nbOv)) {
                        Assert.fail("Nombre d'OV de type beneficiare et le nombre d'OV de type resitution n'est pas égale");
                    } else {
                        Assert.assertEquals("Nombre d'ordres de versements de type restitution ",
                                nbOvRestitution.intValue(), listOvRestitution.size());
                    }
                } else {
                    Assert.fail("Aucun ordre de type restitution à été trouvé");
                }
            }
            Assert.assertEquals("Fait la somme des ordres de verssements ", montantRetro, montantTotalOv.doubleValue());

            Assert.assertEquals("Nombre d'ordres de versements: ", nbOv, listOvBeneFiciairePrincipale.size());

        } else {
            Assert.fail("Auncun ordre de versement de type bénéficiaire principale n'a été trouvé ");
        }
    }

    public static void updateAllDonneesPersonnel(Droit droit, String idLocaliteDernierDomicilelegale) throws Exception {
        List<DonneesPersonnelles> list = DonneeUtilsForTestRetroAndOV.findDonneesPersonnelles(droit);
        for (DonneesPersonnelles donneesPersonnelles : list) {
            DonneeUtilsForTestRetroAndOV.updateDonneesPersonnel(idLocaliteDernierDomicilelegale, donneesPersonnelles);
        }
    }

    private static void updateDonneesPersonnel(final String idLocaliteDernierDomicilelegale,
            final DonneesPersonnelles donneesPersonnelles) throws Exception {

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                donneesPersonnelles.getSimpleDonneesPersonnelles().setIdDernierDomicileLegale(
                        idLocaliteDernierDomicilelegale);
                PegasusServiceLocator.getDroitService().updateDonneesPersonnelles(donneesPersonnelles);

            }
        }.run();
    }

    public static void validerToutesLesDesionsionApresCalcule(Droit droit) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                droit.getSimpleVersionDroit().getIdVersionDroit(), false);
    }

    public static void verifiOVPourCasSimpleSansCreancierEtDette(double montantTotalRetro,
            Map<String, List<OrdreVersement>> mapOv, int nbOVBeneficiaire) {

        DonneeUtilsForTestRetroAndOV.verifiOvSansCreancierEtDette(mapOv, nbOVBeneficiaire, montantTotalRetro, null,
                null);
    }

    public static void verifiOvSansCreancierEtDette(Map<String, List<OrdreVersement>> mapOv, int nbOVBeneficiaire,
            double montantTotalRetro, Integer nbOVResitution, Double montantResitution) {
        List<OrdreVersement> listTiers = mapOv.get(IREOrdresVersements.CS_TYPE_TIERS);

        List<OrdreVersement> listRestitution = mapOv.get(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);

        List<OrdreVersement> listDetteComptat = mapOv.get(IREOrdresVersements.CS_TYPE_DETTE);

        if (listTiers != null) {
            Assert.fail("Une dette de type creancier existe! Mais cela ne devrait pas arriver");
        }

        if (listDetteComptat != null) {
            Assert.fail("Une dette de type comptat auxilière existe! Mais cela ne devrait pas arriver");
        }

        if (nbOVResitution != null) {
            if (listRestitution != null) {
                Assert.assertEquals("Nombre d'ordres de versements de type restitution: ", nbOVResitution.intValue(),
                        listRestitution.size());
            } else {
                Assert.fail("Auncun ordre de versement de type restitution n'a été trouvé");
            }
        } else {
            if (listRestitution != null) {
                Assert.fail("Des ordres de type restitution on été trouvée mais cela ne devrais pas arrivé");
            }
        }

        DonneeUtilsForTestRetroAndOV.testOvBeneficiairePrincipal(mapOv, nbOVBeneficiaire, montantTotalRetro,
                nbOVResitution, montantResitution);
    }

    // public static void verifOvWithRestitutionSansCreancierEtDette(Map<String, List<OrdreVersement>> mapOv,
    // int nbOVBeneficiaire, double montantTotalRetro, String csPcaBeneficiaire, Integer nbOVResitution,
    // Double montantResitution, String csPcaResitution) {
    //
    // DonneeUtilsForTestRetroAndOV.verifiOvSansCreancierEtDette(mapOv, nbOVBeneficiaire, montantTotalRetro,
    // nbOVResitution, montantResitution);
    //
    // List<OrdreVersement> listRestitution = mapOv.get(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
    //
    // List<OrdreVersement> listOvBeneFiciairePrincipale = mapOv
    // .get(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
    //
    // for (OrdreVersement ov : listRestitution) {
    // Assert.assertEquals("Test type pca : ", Integer.valueOf(csPcaResitution).intValue(),
    // Integer.valueOf(ov.getSimpleOrdreVersement().getCsTypePcAccordee()).intValue());
    // }
    //
    // for (OrdreVersement ov : listOvBeneFiciairePrincipale) {
    // Assert.assertEquals("Test type pca : ", Integer.valueOf(IPCOrdresVersements.CS_DOMAINE_AVS).intValue(),
    // Integer.valueOf(ov.getSimpleOrdreVersement().getCsTypePcAccordee()).intValue());
    // }
    // }

}
