package ch.globaz.pegasus.tests.util;

import globaz.corvus.api.lots.IRELot;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
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
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.UtilsLotForTestRetroAndOV;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class CasTestUtil {

    public static String dateLotToUpdate = null;
    public static String idLotToUpdate = null;
    public static String leNoirmontIdLocalite = "4906";

    private static Demande createDemande(Dossier dossier, String dateDepot, String idGestionnaire, String dateRevision)
            throws Exception {
        Demande demande = new Demande();
        demande.setDossier(dossier);
        demande.getSimpleDemande().setIdDossier(dossier.getDossier().getIdDossier());
        demande.getSimpleDemande().setDateDepot(dateDepot);
        // demande.getSimpleDemande().setDateDebut(dateDepot);
        demande.getSimpleDemande().setDateProchaineRevision(dateRevision);
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

    public static CasTest createDossierAndDemandeAndDroitInitial(final CasTest cas) throws Exception {
        final CasTest casTest = cas;

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {

                PersonneEtendueComplexModel personneEtendue = TIBusinessServiceLocator.getPersonneEtendueService()
                        .read(cas.getIdTiers());
                Dossier dossier = CasTestUtil.createDossier(casTest.getIdTiers(), casTest.getIdGestionnaire());
                dossier.getDemandePrestation().setPersonneEtendue(personneEtendue);
                displayLog();
                Demande demande = CasTestUtil.createDemande(dossier, casTest.getDemande().getSimpleDemande()
                        .getDateDepot(), casTest.getIdGestionnaire(), cas.getDemande().getSimpleDemande()
                        .getDateProchaineRevision());
                displayLog();
                Droit droit = CasTestUtil.createDroitIinit(demande);
                displayLog();

                casTest.setDemande(demande);
                casTest.setDossier(dossier);
                casTest.setDroit(droit);

                CasTestUtil.setDateProchainPaiement(casTest.getDateProchainePaiement());

                // Update date prochain paiement

            }
        }.run();

        return casTest;
    }

    private static Droit createDroitIinit(Demande demande) throws Exception {
        Droit droit = PegasusServiceLocator.getDroitService().createDroitInitial(demande);
        return droit;
    }

    protected static RenteAvsAi createRenteAvsAi(DroitMembreFamille droitMembreFamille, Droit droit, String montant,
            String dateDebut) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException {

        // On refait une lecture pour ne pas avoir de probleme de concurent access
        ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = CasTestUtil
                .findModificateurDroitDonneeFinanciereRequerant(droit.getSimpleVersionDroit().getIdVersionDroit());

        RenteAvsAi renteAvsAi = new RenteAvsAi();
        renteAvsAi.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
        renteAvsAi.getSimpleRenteAvsAi().setMontant(montant);
        renteAvsAi.getSimpleRenteAvsAi().setCsTypeRente(IPCRenteAvsAi.CS_TYPE_RENTE_10);

        renteAvsAi = PegasusServiceLocator.getDroitService().createRenteAvsAi(modificateurDroitDonneeFinanciere,
                droitMembreFamille, renteAvsAi);

        return renteAvsAi;

    }

    protected static RenteAvsAi createRenteAvsAiAndUpdateDonneesPersonnel(Droit droit, String montant,
            String dateDebut, String idLocaliteDernierDomicilelegale) throws Exception {

        List<DonneesPersonnelles> list = CasTestUtil.findDonneesPersonnelles(droit);
        RenteAvsAi renteAvsAi = new RenteAvsAi();

        for (DonneesPersonnelles donneesPersonnelles : list) {
            CasTestUtil.updateDonneesPersonnel(idLocaliteDernierDomicilelegale, donneesPersonnelles);

            DroitMembreFamille droitMembreFamille = CasTestUtil.readDroitMembreFamille(donneesPersonnelles);

            renteAvsAi = CasTestUtil.createRenteAvsAi(droitMembreFamille, droit, montant, dateDebut);
        }
        return renteAvsAi;
    }

    public static void createTaxeJournaliere(final String idVersionDroit, final DroitMembreFamille droitMembreFamille,
            final String idHome, final String idTypeChambre, final String dateDebut) throws Exception {

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {

                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = CasTestUtil
                        .findModificateurDroitDonneeFinanciereRequerant(idVersionDroit);

                TaxeJournaliereHome taxeJournaliereHome = new TaxeJournaliereHome();
                taxeJournaliereHome.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
                taxeJournaliereHome.getSimpleTaxeJournaliereHome().setIdHome(idHome);
                taxeJournaliereHome.getSimpleTaxeJournaliereHome().setIdTypeChambre(idTypeChambre);

                PegasusServiceLocator.getDroitService().createTaxeJournaliereHome(modificateurDroitDonneeFinanciere,
                        droitMembreFamille, taxeJournaliereHome);

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

    public static List<DonneesPersonnelles> findDonneesPersonnelles(Droit droit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DonneesPersonnellesSearch search = CasTestUtil.searchDonneesPersonneByDroit(droit);
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

    public static DroitMembreFamille findDroitMembreFamilleConjoint(String idDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return CasTestUtil.findDroitMembreFamille(idDroit, IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    public static DroitMembreFamille findDroitMembreFamilleRequerant(String idDroit) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return CasTestUtil.findDroitMembreFamille(idDroit, IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
    }

    protected static ModificateurDroitDonneeFinanciere findModificateurDroitDonneeFinanciereRequerant(
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

    public static DroitMembreFamille readDroitMembreFamille(DonneesPersonnelles donneesPersonnelles)
            throws DroitException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        DroitMembreFamille droitMembreFamille = PegasusServiceLocator.getDroitService().readDroitMembreFamille(
                donneesPersonnelles.getDroitMbrFam().getIdDroitMembreFamille());
        return droitMembreFamille;
    }

    public static void reUpadteLot() throws Exception {
        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                SimpleLot simpleLot = CorvusServiceLocator.getLotService().read(CasTestUtil.idLotToUpdate);
                UtilsLotForTestRetroAndOV.updateDateLot(simpleLot, CasTestUtil.dateLotToUpdate);
            }
        }.run();

    }

    private static DonneesPersonnellesSearch searchDonneesPersonneByDroit(Droit droit) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        DonneesPersonnellesSearch search = new DonneesPersonnellesSearch();
        search.setForIdDroit(droit.getId());
        search.setWhereKey(DonneesPersonnellesSearch.FOR_DROIT);
        search = PegasusServiceLocator.getDroitService().searchDonneesPersonnelles(search);
        return search;
    }

    public static void setDateProchainPaiement(final String dateProchainPaiement) throws Exception {

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                // TODO Auto-generated method stub
                if ((dateProchainPaiement != null) && JadeDateUtil.isGlobazDate(dateProchainPaiement)) {
                    // // Recherche du lot prochaine paiement
                    // RELotManager mgr = new RELotManager();
                    // mgr.setSession(BSessionUtil.getSessionFromThreadContext());
                    // mgr.setForCsEtatIn(IRELot.CS_ETAT_LOT_VALIDE + ", " + IRELot.CS_ETAT_LOT_PARTIEL);
                    // mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
                    //
                    // mgr.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
                    // mgr.setOrderBy(RELot.FIELDNAME_DATE_ENVOI + " desc ");
                    // mgr.find(1);
                    //
                    // RELot lot = (RELot) mgr.getEntity(0);
                    // CasTestInitUtil.dateLotToUpdate = lot.getDateEnvoiLot();
                    // CasTestInitUtil.idLotToUpdate = lot.getId();
                    //
                    // lot.retrieve();
                    //
                    // lot.setDateCreationLot(dateProchainPaiement);
                    // lot.setDateEnvoiLot(dateProchainPaiement);
                    // lot.update();
                    SimpleLotSearch searchLot = new SimpleLotSearch();
                    searchLot.setForCsType(IRELot.CS_TYP_LOT_MENSUEL);
                    searchLot.setForCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
                    searchLot.setForCsProprietaire(IRELot.CS_LOT_OWNER_RENTES);
                    searchLot = CorvusServiceLocator.getLotService().search(searchLot);
                    SimpleLot simpleLotOrinigal = (SimpleLot) searchLot.getSearchResults()[0];

                    CasTestUtil.dateLotToUpdate = simpleLotOrinigal.getDateEnvoi();
                    CasTestUtil.idLotToUpdate = simpleLotOrinigal.getId();
                    simpleLotOrinigal.setDateCreation(dateProchainPaiement);
                    simpleLotOrinigal.setDateEnvoi(dateProchainPaiement);
                    CorvusServiceLocator.getLotService().update(simpleLotOrinigal);
                }
            }
        }.run();

    }

    protected static void updateDonneesPersonnel(final String idLocaliteDernierDomicilelegale,
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

    String idRelotToUpdate = null;

}
