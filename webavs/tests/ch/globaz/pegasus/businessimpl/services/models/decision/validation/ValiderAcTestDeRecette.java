package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.hera.business.exceptions.HeraException;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.lot.OrdreVersement;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.tests.pcAccordee.DonneeForDonneeFinanciere;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.DonneeUtilsForTestRetroAndOV;

public class ValiderAcTestDeRecette {

    public static final String ID_HOME = "309"; // 4 Fondation Clair-Logis 309 - Fondation Clair-Logis 309
    public static final String ID_TYPE_CHAMBRE = "40";

    // 756.0474.6966.57 personne seul
    public Droit corrigerDroit(String dateAnnonce, Droit droit) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException {

        droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce,
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
        return droit;
    }

    public Droit corrigerDroit(String dateAnnonce, String idTiers) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException {
        Droit droit = DonneeUtilsForTestRetroAndOV.findCurrentDroitByIdTiers(idTiers);

        droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce,
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
        return droit;
    }

    private void createAndValideDecision(DonneeForDonneeFinanciere data) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, Exception, DecisionException, JadePersistenceException,
            JadeApplicationException {
        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(data.getDroit());

        DonneeUtilsForTestRetroAndOV.validerToutesLesDesionsionApresCalcule(data.getDroit());
    }

    public void executeAllForValidation(Droit droit) throws PegasusException, HeraException, JadePersistenceException,
            JadeApplicationException, NoSuchMethodException, JadeApplicationServiceNotAvailableException,
            PmtMensuelException, Exception, DecisionException {

        this.executeAllForValidation(droit.getSimpleVersionDroit().getIdVersionDroit());
    }

    private void executeAllForValidation(String idVersionDroit) throws PegasusException, HeraException,
            JadePersistenceException, JadeApplicationException, NoSuchMethodException,
            JadeApplicationServiceNotAvailableException, PmtMensuelException, Exception, DecisionException {

        PegasusServiceLocator.getDroitService().calculerDroit(idVersionDroit, true, null);

        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(idVersionDroit);

        PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(idVersionDroit,
                false);
    }

    @Ignore
    @Test
    public void testAvsAi() throws Exception {
        String montantRenteV1 = "600";
        String montantRenteV2 = "450";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        /*************** Création du droit initial ************************/
        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRenteV1, "756.0000.1615.50", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());

        createAndValideDecision(data);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsToAi(droit, droitMembreFamille, montantRenteV2,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);
    }

    @Ignore
    @Test
    public void testCasSimpleAvecCreancierAuxConjoint() throws Exception {
        String montantRenteV1 = "600";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();
        String idTierCreancier = "164289";

        /*************** Création du droit initial ************************/

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.6916.5971.17", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit(), montantRenteV1);

        PegasusServiceLocator.getDroitService().calculerDroit(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), true, null);

        List<PCAccordee> listPca = DonneeUtilsForTestRetroAndOV.findPCa(data.getDroit());

        DonneeUtilsForTestRetroAndOV.addCreancierWithCreance(data.getDemande().getId(), idTierCreancier, "500", listPca
                .get(0).getSimplePCAccordee().getId(), "300", droitMembreFamille.getMembreFamille()
                .getPersonneEtendue().getTiers().getIdTiers());

        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(data.getDroit());

        PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), false);

        Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
                data.getDroit(), 3);
    }

    @Ignore
    @Test
    public void testCasSimpleAvecCreancierAuxDeux() throws Exception {
        String montantRenteV1 = "600";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();
        String idTierCreancier = "164289";

        /*************** Création du droit initial ************************/

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.6916.5971.17", dateDepotDemande);

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit(), montantRenteV1);

        PegasusServiceLocator.getDroitService().calculerDroit(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), true, null);

        List<PCAccordee> listPca = DonneeUtilsForTestRetroAndOV.findPCa(data.getDroit());
        DonneeUtilsForTestRetroAndOV.addCreancierWithCreance(data.getDemande().getId(), idTierCreancier, "500", listPca
                .get(0).getSimplePCAccordee().getId(), "301", null);

        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(data.getDroit());

        PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), false);

        Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
                data.getDroit(), 3);
    }

    @Ignore
    @Test
    public void testCasSimpleAvecCreancierAuxRequerant() throws Exception {
        String montantRenteV1 = "600";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();
        String idTierCreancier = "164289";

        /*************** Création du droit initial ************************/

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.6916.5971.17", dateDepotDemande);

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit(), montantRenteV1);

        PegasusServiceLocator.getDroitService().calculerDroit(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), true, null);

        List<PCAccordee> listPca = DonneeUtilsForTestRetroAndOV.findPCa(data.getDroit());
        DonneeUtilsForTestRetroAndOV.addCreancierWithCreance(data.getDemande().getId(), idTierCreancier, "500", listPca
                .get(0).getSimplePCAccordee().getId(), "300", data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(data.getDroit());

        PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                data.getDroit().getSimpleVersionDroit().getIdVersionDroit(), false);

        Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
                data.getDroit(), 3);

    }

    @Ignore
    @Test
    public void testCasSimpleMultipleCorrectionDroit() throws Exception {
        String montantRenteV1 = "600";
        String montantRenteV2 = "450";
        String dateAnnonce = DonneeUtilsForTestRetroAndOV.getDate();
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        /*************** Création du droit initial ************************/
        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRenteV1, "756.0000.1615.50", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());

        createAndValideDecision(data);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(dateAnnonce, data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, montantRenteV2,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);

        JadeThread.commitSession();
        /*************** Modification 2 du droit ************************/

        droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-2), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, "200",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-3));

        this.executeAllForValidation(droit);

        /*************** Modification 3 du droit ************************/

        droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createCompteCcp(droit, droitMembreFamille, "1000",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-2));

        this.executeAllForValidation(droit);

    }

    @Ignore
    @Test
    public void testCoupleSepare() throws Exception {
        String montantRente = "400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.6916.5971.17", dateDepotDemande);

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit(), montantRente);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit(), droitMembreFamille,
                ValiderAcTestDeRecette.ID_HOME, ValiderAcTestDeRecette.ID_TYPE_CHAMBRE,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(data.getDroit());

        // // TODO verif
        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 4);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, "350",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);

        // DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit, 4);

    }

    @Ignore
    @Test
    public void testDom2R() throws Exception {

        String montantRente = "800";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRente, "756.6916.5971.17", dateDepotDemande);

        createAndValideDecision(data);

        Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
                data.getDroit(), 2);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-3), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(droit);

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, "700",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);

        DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit, 2);

        /*************** Modification 2 du droit initial et création du nouveau droit ************************/

        droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.createCompteCcp(droit, droitMembreFamille, "1000",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-3));

        this.executeAllForValidation(droit);

        DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(droit, 2);

    }

    @Ignore
    @Test
    public void testDom2RToDom() throws Exception {

        String montantRente = "900";
        String montantRentev2 = "400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRente, "756.1159.3702.37", dateDepotDemande);

        JadeThread.commitSession();
        this.executeAllForValidation(data.getDroit());

        /*************** Modification ajout d'une nouvelle rente pour le conjoint ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamille, montantRentev2,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6), DonneeUtilsForTestRetroAndOV.getDateMonth(-3),
                IPCRenteAvsAi.CS_TYPE_RENTE_10, true);

        // DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamille, montantRentev2, "06.2012",
        // "09.2012", IPCRenteAvsAi.CS_TYPE_RENTE_10, true);

        this.executeAllForValidation(droit);

    }

    @Ignore
    @Test
    public void testDom2RToSepMalConjointHomeDeuxOctrois() throws Exception {

        String montantRente = "400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRente, "756.5695.1410.30", dateDepotDemande);

        createAndValideDecision(data);

        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 3);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(droit, droitMembreFamille, ValiderAcTestDeRecette.ID_HOME,
                ValiderAcTestDeRecette.ID_TYPE_CHAMBRE, DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);
    }

    @Ignore
    @Test
    public void testDom2RToSepMalConjointHomeRefusRequerant() throws Exception {

        String montantRente = "1400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRente, "756.5695.1410.30", dateDepotDemande);

        createAndValideDecision(data);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());
        DroitMembreFamille droitMembreFamilleR = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());
        DonneeUtilsForTestRetroAndOV.createContratEntretienViager(droit, droitMembreFamilleR, "30000",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));
        DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(droit, droitMembreFamille, ValiderAcTestDeRecette.ID_HOME,
                ValiderAcTestDeRecette.ID_TYPE_CHAMBRE, DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);
    }

    @Ignore
    @Test
    public void testDom2RToSepMalRequerantHomeDeuxOctrois() throws Exception {

        String montantRente = "400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRente, "756.5695.1410.30", dateDepotDemande);

        createAndValideDecision(data);

        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 3);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(droit, droitMembreFamille, ValiderAcTestDeRecette.ID_HOME,
                ValiderAcTestDeRecette.ID_TYPE_CHAMBRE, DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);
    }

    @Ignore
    @Test
    public void testDom2RToSepMalRequerantHomeRefusConjoint() throws Exception {

        String montantRente = "1400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRente, "756.5695.1410.30", dateDepotDemande);

        createAndValideDecision(data);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamilleC = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());
        DroitMembreFamille droitMembreFamilleR = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());
        DonneeUtilsForTestRetroAndOV.createContratEntretienViager(droit, droitMembreFamilleC, "30000",
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6));
        DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(droit, droitMembreFamilleR, ValiderAcTestDeRecette.ID_HOME,
                ValiderAcTestDeRecette.ID_TYPE_CHAMBRE, DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);
    }

    @Ignore
    @Test
    public void testDomToDom2R() throws Exception {

        String montantRente = "900";
        String montantRentev2 = "400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.5072.6254.50", dateDepotDemande);

        DonneeUtilsForTestRetroAndOV.updateAllDonneesPersonnel(data.getDroit(), "436");

        DroitMembreFamille droitMembreFamilleRequerant = DonneeUtilsForTestRetroAndOV
                .findDroitMembreFamilleRequerant(data.getDroit());

        DonneeUtilsForTestRetroAndOV.createRenteAvsAi(droitMembreFamilleRequerant, data.getDroit(), montantRente,
                DonneeUtilsForTestRetroAndOV.getDateMonth(), IPCRenteAvsAi.CS_TYPE_RENTE_10);

        this.executeAllForValidation(data.getDroit());

        /*************** Modification ajout d'une nouvelle rente pour le conjoint ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DroitMembreFamille droitMembreFamilleConjoint = DonneeUtilsForTestRetroAndOV
                .findDroitMembreFamilleConjoint(droit);

        // DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamilleRequerant, montantRentev2,
        // "06.2012", IPCRenteAvsAi.CS_TYPE_RENTE_10);

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvsAi(droit, droitMembreFamilleRequerant, montantRentev2,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6), IPCRenteAvsAi.CS_TYPE_RENTE_10);

        DonneeUtilsForTestRetroAndOV.createRenteAvsAi(droitMembreFamilleConjoint, droit, montantRentev2,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-6), IPCRenteAvsAi.CS_TYPE_RENTE_10);

        this.executeAllForValidation(droit);

    }

    @Ignore
    @Test
    public void testSepMalToDom2RConjointEnHomeRequerantRefus() throws Exception {

        String montantRente = "1750";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.5082.6274.99", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
                montantRente);

        DonneeUtilsForTestRetroAndOV.createCompteCcp(data.getDroit(), droitMembreFamille, "50000",
                DonneeUtilsForTestRetroAndOV.getDateMonth());

        TaxeJournaliereHome taxeJournaliereHome = DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit(),
                droitMembreFamille, ValiderAcTestDeRecette.ID_HOME, ValiderAcTestDeRecette.ID_TYPE_CHAMBRE,
                DonneeUtilsForTestRetroAndOV.getDateMonth());

        this.executeAllForValidation(data.getDroit());

        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 3);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.cloreTaxeJournaliere(taxeJournaliereHome, droit.getSimpleVersionDroit()
                .getIdVersionDroit(), DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);

    }

    @Ignore
    @Test
    public void testSepMalToDom2RConjointHomeOctrois() throws Exception {

        String montantRente = "500";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();
        String idHome = "309"; // 4 Fondation Clair-Logis 309 - Fondation Clair-Logis 309
        String idTypeChembre = "40";

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.5082.6274.99", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
                montantRente);

        TaxeJournaliereHome taxeJournaliereHome = DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit(),
                droitMembreFamille, idHome, idTypeChembre, DonneeUtilsForTestRetroAndOV.getDateMonth());

        this.executeAllForValidation(data.getDroit());

        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 3);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.cloreTaxeJournaliere(taxeJournaliereHome, droit.getSimpleVersionDroit()
                .getIdVersionDroit(), DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);

    }

    @Ignore
    @Test
    public void testSepMalToDom2RRequerantHomeOctrois() throws Exception {

        String montantRente = "400";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.5082.6274.99", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
                montantRente);

        TaxeJournaliereHome taxeJournaliereHome = DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit(),
                droitMembreFamille, ValiderAcTestDeRecette.ID_HOME, ValiderAcTestDeRecette.ID_TYPE_CHAMBRE,
                DonneeUtilsForTestRetroAndOV.getDateMonth());

        this.executeAllForValidation(data.getDroit());

        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 3);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.cloreTaxeJournaliere(taxeJournaliereHome, droit.getSimpleVersionDroit()
                .getIdVersionDroit(), DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);

    }

    @Ignore
    @Test
    public void testSepMalToDom2RRequerantHomeRefus() throws Exception {

        String montantRente = "1500";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.5082.6274.99", dateDepotDemande);

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(data
                .getDroit());

        DonneeUtilsForTestRetroAndOV.createRentes(data.getDroit().getSimpleVersionDroit().getIdVersionDroit(),
                montantRente);

        TaxeJournaliereHome taxeJournaliereHome = DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit(),
                droitMembreFamille, ValiderAcTestDeRecette.ID_HOME, ValiderAcTestDeRecette.ID_TYPE_CHAMBRE,
                DonneeUtilsForTestRetroAndOV.getDateMonth());

        this.executeAllForValidation(data.getDroit());

        // Map<String, List<OrdreVersement>> mapOv = DonneeUtilsForTestRetroAndOV.searchAndGroupByTypeAndTestNbFound(
        // data.getDroit(), 3);

        /*************** Modification 1 du droit initial et création du nouveau droit ************************/

        Droit droit = this.corrigerDroit(DonneeUtilsForTestRetroAndOV.getDate(-1), data.getIdTiersRequerant());

        DonneeUtilsForTestRetroAndOV.cloreTaxeJournaliere(taxeJournaliereHome, droit.getSimpleVersionDroit()
                .getIdVersionDroit(), DonneeUtilsForTestRetroAndOV.getDateMonth(-6));

        this.executeAllForValidation(droit);
    }

    @Ignore
    @Test
    public void testVersionInitialePcaEnRefus() throws Exception {
        String montantRenteV1 = "2500";
        String dateDepotDemande = DonneeUtilsForTestRetroAndOV.getDate();

        /*************** Création du droit initial ************************/
        DonneeForDonneeFinanciere data = DonneeUtilsForTestRetroAndOV.createDemandDossierWithRenteAndExecuteCalcule(
                montantRenteV1, "756.0000.1615.50", dateDepotDemande);

        createAndValideDecision(data);
    }

}
