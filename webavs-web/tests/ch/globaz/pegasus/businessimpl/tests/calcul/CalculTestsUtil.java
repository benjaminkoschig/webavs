package ch.globaz.pegasus.businessimpl.tests.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciereSearch;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.tests.util.CasTestUtil;
import ch.globaz.pegasus.tests.util.LogTemplate;
import ch.globaz.pegasus.tests.util.calcul.CalculCasTest;

public class CalculTestsUtil extends CasTestUtil {
    public static CalculCasTest calculVersionDroit(final CalculCasTest cas) throws Exception {

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                // TODO Auto-generated method stub
                // on clacule le retro (true)
                cas.setDroit(PegasusServiceLocator.getDroitService().calculerDroit(
                        cas.getDroit().getSimpleVersionDroit().getIdVersionDroit(), true, null));

            }
        }.run();
        return cas;
    }

    public static CalculCasTest createAndCloseRenteAvsAi(final CalculCasTest casTest, final String montantRente,
            final String dateModif) throws Exception {

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = CalculTestsUtil
                        .findModificateurDroitDonneeFinanciereRequerant(casTest.getDroit().getSimpleVersionDroit()
                                .getIdVersionDroit());

                RenteAvsAi renteAvsAi = new RenteAvsAi();
                renteAvsAi.setSimpleDonneeFinanciereHeader(casTest.getRenteAvsAi().getSimpleDonneeFinanciereHeader());
                renteAvsAi.getSimpleDonneeFinanciereHeader().setDateDebut(dateModif);
                renteAvsAi.getSimpleRenteAvsAi().setMontant(montantRente);
                renteAvsAi.getSimpleRenteAvsAi().setCsTypeRente(IPCRenteAvsAi.CS_TYPE_RENTE_10);

                casTest.setRenteAvsAi(PegasusServiceLocator.getDroitService().createAndCloseRenteAvsAi(
                        modificateurDroitDonneeFinanciere, renteAvsAi, false));

            }
        }.run();
        return casTest;

    }

    public static CalculCasTest createAndCloseVehicule(final CalculCasTest casTest, final String montant,
            final String dateModif) throws Exception {

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = CalculTestsUtil
                        .findModificateurDroitDonneeFinanciereRequerant(casTest.getDroit().getSimpleVersionDroit()
                                .getIdVersionDroit());

                Vehicule vehicule = new Vehicule();
                vehicule.setSimpleDonneeFinanciereHeader(casTest.getVehicule().getSimpleDonneeFinanciereHeader());
                vehicule.getSimpleDonneeFinanciereHeader().setDateDebut(dateModif);
                vehicule.getSimpleVehicule().setMontant(montant);
                vehicule.getSimpleVehicule().setCsTypePropriete("64009001");
                vehicule.getSimpleVehicule().setPartProprieteDenominateur("1");
                vehicule.getSimpleVehicule().setPartProprieteNumerateur("1");
                vehicule.getSimpleVehicule().setDesignation("Vehicule pour test JUnit");

                casTest.setVehicule(PegasusServiceLocator.getDroitService().createAndCloseVehicule(
                        modificateurDroitDonneeFinanciere, vehicule, false));

            }
        }.run();
        return casTest;

    }

    /*
     * Sette la localite pour le noirmont
     */
    public static RenteAvsAi createRentes(final CalculCasTest casTest, final String montant, final String dateDebut)
            throws Exception {

        new LogTemplate() {
            @Override
            protected void execute() throws Exception {
                Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(
                        casTest.getDroit().getSimpleVersionDroit().getIdVersionDroit());
                casTest.setRenteAvsAi(CasTestUtil.createRenteAvsAiAndUpdateDonneesPersonnel(droit, montant, dateDebut,
                        CasTestUtil.leNoirmontIdLocalite));
            }
        }.run();

        return casTest.getRenteAvsAi();
    }

    protected static Vehicule createVehicule(DroitMembreFamille droitMembreFamille, Droit droit, String montant,
            String dateDebut) throws DroitException, JadePersistenceException, RenteAvsAiException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException, VehiculeException {

        // On refait une lecture pour ne pas avoir de probleme de concurent access
        ModificateurDroitDonneeFinanciere modificateurDroitDonneeFinanciere = CasTestUtil
                .findModificateurDroitDonneeFinanciereRequerant(droit.getSimpleVersionDroit().getIdVersionDroit());

        Vehicule vehicule = new Vehicule();
        vehicule.getSimpleDonneeFinanciereHeader().setDateDebut(dateDebut);
        vehicule.getSimpleVehicule().setMontant(montant);
        vehicule.getSimpleVehicule().setCsTypePropriete("64009001");
        vehicule.getSimpleVehicule().setPartProprieteDenominateur("1");
        vehicule.getSimpleVehicule().setPartProprieteNumerateur("1");
        vehicule.getSimpleVehicule().setDesignation("Vehicule pour test JUnit");
        vehicule = PegasusServiceLocator.getDroitService().createVehicule(modificateurDroitDonneeFinanciere,
                droitMembreFamille, vehicule);// createRenteAvsAi(modificateurDroitDonneeFinanciere,
        // droitMembreFamille, renteAvsAi);

        return vehicule;

    }

    public static ModificateurDroitDonneeFinanciere findModificateurDroitDonneeFinanciereRequerant(String idVersionDroit)
            throws DroitException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
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

    public static ArrayList findPcaForDemande(Demande demande) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ArrayList<ListPCAccordee> listeRetour = new ArrayList<ListPCAccordee>();

        ListPCAccordeeSearch search = new ListPCAccordeeSearch();
        search.setForCacherHistorique("true");
        search.setForIdDemande(demande.getId());
        search.setForIdDossier(demande.getDossier().getId());
        search.setWhereKey("forVersionnedPca");
        search = PegasusServiceLocator.getPCAccordeeService().searchForList(search);
        for (JadeAbstractModel pca : search.getSearchResults()) {
            listeRetour.add((ListPCAccordee) pca);
        }

        return listeRetour;
    }

    public static ArrayList<PCAccordee> findPCAForVersionDroit(String idDroit, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ArrayList<PCAccordee> listePcas = new ArrayList<PCAccordee>();

        ListPCAccordeeSearch search = new ListPCAccordeeSearch();
        search.setForIdDroit(idDroit);
        search.setForNoVersion(noVersionDroit);
        search.setForCacherHistorique("true");
        search = PegasusServiceLocator.getPCAccordeeService().searchForList(search);

        for (JadeAbstractModel pca : search.getSearchResults()) {

            PCAccordee pCa = new PCAccordee();
            pCa.setSimplePCAccordee(((ListPCAccordee) pca).getSimplePCAccordee());
            listePcas.add(pCa);
        }

        return listePcas;

    }

    public static DroitMembreFamille getDroitMembreFamille(CalculCasTest casTest) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        List<DonneesPersonnelles> list = CasTestUtil.findDonneesPersonnelles(casTest.getDroit());
        DonneesPersonnelles donneesPersonnelles = list.get(0);
        DroitMembreFamille droitMembreFamille = CasTestUtil.readDroitMembreFamille(donneesPersonnelles);
        return droitMembreFamille;
    }

    public static CalculCasTest preValidAndValidDroit(final CalculCasTest cas) throws PCAccordeeException, Exception {

        new LogTemplate() {

            @Override
            protected void execute() throws Exception {
                // TODO Auto-generated method stub
                // Creation decision
                VersionDroit vd = new VersionDroit();
                vd.setDemande(cas.getDemande());
                vd.setSimpleDroit(cas.getDroit().getSimpleDroit());
                vd.setSimpleVersionDroit(cas.getDroit().getSimpleVersionDroit());

                DecisionApresCalcul decision = new DecisionApresCalcul();
                decision.setVersionDroit(vd);
                decision.getDecisionHeader().getSimpleDecisionHeader().setDateDecision("01.01.2011");
                decision.getDecisionHeader().getSimpleDecisionHeader().setPreparationPar(cas.getIdGestionnaire());
                PegasusServiceLocator.getDecisionApresCalculService().createStandardDecision(decision);

                // Prévalidation
                decision = PegasusServiceLocator.getValidationDecisionService().preValidDecisionApresCalcul(decision);
                // Validation
                decision.getDecisionHeader().getSimpleDecisionHeader().setDateValidation("01.01.2011");
                decision.getDecisionHeader().getSimpleDecisionHeader().setValidationPar(cas.getIdGestionnaire());
                PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                        cas.getDroit().getSimpleVersionDroit().getId(), false);
                cas.getDroit().getSimpleVersionDroit().setCsEtatDroit(IPCDroits.CS_VALIDE);
            }
        }.run();

        return cas;
        // OCtroye droit
        // cas.getDroit().getSimpleVersionDroit().setCsEtatDroit(IPCDemandes.CS_OCTROYE);
        // PegasusServiceLocator.getDroitService().updateDroit(cas.getDroit());
    }
}
