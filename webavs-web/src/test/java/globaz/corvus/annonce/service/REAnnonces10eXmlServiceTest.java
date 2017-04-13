package globaz.corvus.annonce.service;

import static org.junit.Assert.*;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.process.REEnvoyerAnnoncesXMLProcess;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import ch.admin.zas.rc.RRMeldung10Type;
import ch.globaz.common.exceptions.ValidationException;

public class REAnnonces10eXmlServiceTest {
    private String ageDebutInvalidite = "";
    private String ageDebutInvaliditeEpouse = "";
    private String anneeCotClasseAge = "";
    private String anneeNiveau = "";
    private String bteMoyennePrisEnCompte = "";
    private String cantonEtatDomicile = "";
    private String casSpecial1 = "";
    private String casSpecial2 = "";
    private String casSpecial3 = "";
    private String casSpecial4 = "";
    private String casSpecial5 = "";
    private String codeApplication;
    private String codeEnregistrement01 = "";
    private String codeInfirmite = "";
    private String codeInfirmiteEpouse = "";
    private String codeMutation = "";
    private String codeRevenuSplitte = "";
    private String dateDebutAnticipation = "";
    private String dateRevocationAjournement = "";
    private String debutDroit = "";
    private String degreInvalidite = "";
    private String degreInvaliditeEpouse = "";
    private String dureeAjournement = "";
    private String dureeCoEchelleRenteAv73 = "";
    private String dureeCoEchelleRenteDes73 = "";
    private String dureeCotManquante48_72 = "";
    private String dureeCotManquante73_78 = "";
    private String dureeCotPourDetRAM = "";
    private String echelleRente = "";
    private String etatCivil = "";
    private String finDroit = "";
    private String genreDroitAPI = "";
    private String genrePrestation = "";
    private String isLimiteRevenu = "";
    private String isMinimumGaranti = "";
    private String isRefugie = "";
    private String isSurvivant = "";
    private String mensualitePrestationsFrancs = "";
    private String mensualiteRenteOrdRemp = "";
    private String moisRapport = "";
    private String nbreAnneeAnticipation = "";
    private String nbreAnneeBonifTrans = "";
    private String nbreAnneeBTA = "";
    private String noAssAyantDroit = "";
    private String nombreAnneeBTE = "";
    private String nouveauNoAssureAyantDroit = "";
    private String numeroAgence = "";
    private String numeroCaisse = "";
    private String officeAiCompEpouse = "";
    private String officeAICompetent = "";
    private String premierNoAssComplementaire = "";
    private String ramDeterminant = "";
    private String reduction = "";
    private String reductionAnticipation = "";
    private String referenceCaisseInterne = "";
    private String revenuAnnuelMoyenSansBTE = "";
    private String revenuPrisEnCompte = "";
    private String secondNoAssComplementaire = "";
    private String supplementAjournement = "";
    private String survenanceEvenAssure = "";
    private String survenanceEvtAssureEpouse = "";
    private String idAnnonce = "";
    private String codeEnregistrement02 = "";

    @Spy
    private REEnvoyerAnnoncesXMLProcess testInstance;
    @Spy
    private REAnnonces10eXmlService testService;

    @Before
    public void setUp() throws Exception {
        // enregistrement 1
        idAnnonce = "1546129";
        codeEnregistrement01 = "01";
        numeroCaisse = "150";
        numeroAgence = "000";
        referenceCaisseInterne = "AUGJUCBA";
        noAssAyantDroit = "7563364333014";
        premierNoAssComplementaire = "7560644640800";
        secondNoAssComplementaire = "7565024495087";
        etatCivil = "1";
        isRefugie = "0";
        cantonEtatDomicile = "050";
        genrePrestation = "55";
        debutDroit = "1016";
        mensualitePrestationsFrancs = "00181";
        moisRapport = "0117";
        // enregistrement 2
        codeApplication = "44";
        codeEnregistrement02 = "02";
        echelleRente = "44";
        dureeCoEchelleRenteAv73 = "0000";
        dureeCoEchelleRenteDes73 = "1700";
        dureeCotManquante48_72 = "00";
        dureeCotManquante73_78 = "00";
        anneeCotClasseAge = "17";
        ramDeterminant = "00054990";
        codeRevenuSplitte = "1";
        dureeCotPourDetRAM = "1700";
        anneeNiveau = "08";
        nombreAnneeBTE = "0500";
        officeAICompetent = "350";
        degreInvalidite = "47";
        codeInfirmite = "64665";
        survenanceEvenAssure = "0208";
        ageDebutInvalidite = "0";
        genreDroitAPI = "";
        casSpecial1 = "02";
        codeMutation = "1";

        testInstance = Mockito.spy(new REEnvoyerAnnoncesXMLProcess());
        testService = Mockito.spy(new REAnnonces10eXmlService());

        Mockito.doReturn("059000").when(testService).retourneCaisseAgence();

    }

    @Test
    public void testPreparerAugmentation10Eme() throws Exception {
        // data
        REAnnoncesAugmentationModification10Eme enregistrement01 = new REAnnoncesAugmentationModification10Eme();
        REAnnoncesAugmentationModification10Eme enregistrement02 = Mockito
                .spy(new REAnnoncesAugmentationModification10Eme());

        // enregistrement 1
        enregistrement01.setIdAnnonce(idAnnonce);
        enregistrement01.setCodeApplication(codeApplication);
        enregistrement01.setCodeEnregistrement01(codeEnregistrement01);
        enregistrement01.setNumeroCaisse(numeroCaisse);
        enregistrement01.setNumeroAgence(numeroAgence);
        enregistrement01.setReferenceCaisseInterne(referenceCaisseInterne);
        enregistrement01.setNoAssAyantDroit(noAssAyantDroit);
        enregistrement01.setPremierNoAssComplementaire(premierNoAssComplementaire);
        enregistrement01.setSecondNoAssComplementaire(secondNoAssComplementaire);
        enregistrement01.setNouveauNoAssureAyantDroit(nouveauNoAssureAyantDroit);
        enregistrement01.setEtatCivil(etatCivil);
        enregistrement01.setIsRefugie(isRefugie);
        enregistrement01.setCantonEtatDomicile(cantonEtatDomicile);
        enregistrement01.setGenrePrestation(genrePrestation);
        enregistrement01.setDebutDroit(debutDroit);
        enregistrement01.setMensualitePrestationsFrancs(mensualitePrestationsFrancs);
        enregistrement01.setFinDroit(finDroit);
        enregistrement01.setMoisRapport(moisRapport);
        enregistrement01.setCodeMutation(codeMutation);

        // enregistrement 2
        Mockito.when(enregistrement02.getSpy()).thenReturn(new BSpy("20110101120000test"));
        enregistrement02.setCodeApplication(codeApplication);
        enregistrement02.setCodeEnregistrement01(codeEnregistrement02);
        enregistrement02.setEchelleRente(echelleRente);
        enregistrement02.setDureeCoEchelleRenteAv73(dureeCoEchelleRenteAv73);
        enregistrement02.setDureeCoEchelleRenteDes73(dureeCoEchelleRenteDes73);
        enregistrement02.setDureeCotManquante48_72(dureeCotManquante48_72);
        enregistrement02.setDureeCotManquante73_78(dureeCotManquante73_78);
        enregistrement02.setAnneeCotClasseAge(anneeCotClasseAge);
        enregistrement02.setRamDeterminant(ramDeterminant);
        enregistrement02.setCodeRevenuSplitte(codeRevenuSplitte);
        enregistrement02.setDureeCotPourDetRAM(dureeCotPourDetRAM);
        enregistrement02.setAnneeNiveau(anneeNiveau);
        enregistrement02.setNombreAnneeBTE(nombreAnneeBTE);
        enregistrement02.setNbreAnneeBTA(nbreAnneeBTA);
        enregistrement02.setNbreAnneeBonifTrans(nbreAnneeBonifTrans);
        enregistrement02.setOfficeAICompetent(officeAICompetent);
        enregistrement02.setDegreInvalidite(degreInvalidite);
        enregistrement02.setCodeInfirmite(codeInfirmite);
        enregistrement02.setSurvenanceEvenAssure(survenanceEvenAssure);
        enregistrement02.setAgeDebutInvalidite(ageDebutInvalidite);
        enregistrement02.setGenreDroitAPI(genreDroitAPI);
        enregistrement02.setReduction(reduction);
        enregistrement02.setCasSpecial1(casSpecial1);
        enregistrement02.setCasSpecial2(casSpecial2);
        enregistrement02.setCasSpecial3(casSpecial3);
        enregistrement02.setCasSpecial4(casSpecial4);
        enregistrement02.setCasSpecial5(casSpecial5);
        enregistrement02.setNbreAnneeAnticipation(nbreAnneeAnticipation);
        enregistrement02.setReductionAnticipation(reductionAnticipation);
        enregistrement02.setDateDebutAnticipation(dateDebutAnticipation);
        enregistrement02.setDureeAjournement(dureeAjournement);
        enregistrement02.setSupplementAjournement(supplementAjournement);
        enregistrement02.setDateRevocationAjournement(dateRevocationAjournement);
        enregistrement02.setIsSurvivant(isSurvivant);

        try {
            // Augmentation Ordinaire
            testInstance.validateUnitMessage(testService.genererZuwachmeldungOrdentliche(enregistrement01,
                    enregistrement02));
            // Modification Ordinaire
            testInstance.validateUnitMessage(testService.genererAenderungsmeldungOrdentliche(enregistrement01,
                    enregistrement02));

            // Extraordinaire
            enregistrement01.setGenrePrestation("74");
            // Augmentation Extraordinaire
            testInstance.validateUnitMessage(testService.genererZuwachmeldungAusserordentliche(enregistrement01,
                    enregistrement02));
            // Modification Extraordinaire
            testInstance.validateUnitMessage(testService.genererAenderungsmeldungAusserordentliche(enregistrement01,
                    enregistrement02));

            // API
            enregistrement01.setGenrePrestation("81");
            enregistrement02.setGenreDroitAPI("1");
            // Augmentation API
            testInstance.validateUnitMessage(testService.genererZuwachmeldungHilflosenentschaedigung(enregistrement01,
                    enregistrement02));
            // Modification API
            testInstance.validateUnitMessage(testService.genererAenderungsmeldungHilflosenentschaedigung(
                    enregistrement01, enregistrement02));

        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }
    }

    @Test
    public void testPreparerDiminution10Eme() throws Exception {
        // data
        finDroit = "1217";
        REAnnoncesDiminution10Eme enregistrement01 = new REAnnoncesDiminution10Eme();

        // enregistrement 1
        enregistrement01.setIdAnnonce(idAnnonce);
        enregistrement01.setNumeroCaisse(numeroCaisse);
        enregistrement01.setNumeroAgence(numeroAgence);
        enregistrement01.setReferenceCaisseInterne(referenceCaisseInterne);
        enregistrement01.setNoAssAyantDroit(noAssAyantDroit);
        enregistrement01.setPremierNoAssComplementaire(premierNoAssComplementaire);
        enregistrement01.setSecondNoAssComplementaire(secondNoAssComplementaire);
        enregistrement01.setNouveauNumeroAssureAyantDroit(nouveauNoAssureAyantDroit);
        enregistrement01.setEtatCivil(etatCivil);
        enregistrement01.setIsRefugie(isRefugie);
        enregistrement01.setCantonEtatDomicile(cantonEtatDomicile);
        enregistrement01.setGenrePrestation(genrePrestation);
        enregistrement01.setDebutDroit(debutDroit);
        enregistrement01.setMensualitePrestationsFrancs(mensualitePrestationsFrancs);
        enregistrement01.setFinDroit(finDroit);
        enregistrement01.setMoisRapport(moisRapport);
        enregistrement01.setCodeMutation(codeMutation);

        try {
            // ordinaire
            testInstance.validateUnitMessage(testService.genererAbgangsOrdentliche(enregistrement01));
            // extraordinaire
            enregistrement01.setGenrePrestation("20");
            testInstance.validateUnitMessage(testService.genererAbgangsAusserordentliche(enregistrement01));
            // api
            enregistrement01.setGenrePrestation("81");
            testInstance.validateUnitMessage(testService.genererAbgangsHilflosenentschaedigung(enregistrement01));
        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }

    }

    @Test
    public void testGetAnnonceXmlDirections() throws Exception {

        // SetUp
        // Mute DAO
        Mockito.doReturn(new REAnnoncesAugmentationModification10Eme()).when(testService)
                .retrieveAnnonceAugModif10(Matchers.any(REAnnoncesAbstractLevel1A.class), Matchers.any(BSession.class));
        Mockito.doReturn(new REAnnoncesAugmentationModification10Eme())
                .when(testService)
                .retrieveAnnonceAugModif10eme2(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(BSession.class));
        Mockito.doReturn(new REAnnoncesDiminution10Eme()).when(testService)
                .retrieveAnnonceDimi10(Matchers.any(REAnnoncesAbstractLevel1A.class), Matchers.any(BSession.class));
        Mockito.doNothing()
                .when(testService)
                .checkAndUpdate10eme2(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(BITransaction.class));
        // Mute ANAKIN
        Mockito.doNothing()
                .when(testService)
                .parseAugmentationAvecAnakin(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class), Matchers.any(BSession.class),
                        Matchers.any(String.class));
        Mockito.doNothing()
                .when(testService)
                .parseDiminutionAvecAnakin(Matchers.any(REAnnoncesDiminution10Eme.class), Matchers.any(BSession.class),
                        Matchers.any(String.class));

        // Temoins
        RRMeldung10Type temoinAugmentationOrdinaire = new RRMeldung10Type();
        Mockito.doReturn(temoinAugmentationOrdinaire)
                .when(testService)
                .genererZuwachmeldungOrdentliche(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class));
        RRMeldung10Type temoinAugmentationExtraordinaire = new RRMeldung10Type();
        Mockito.doReturn(temoinAugmentationExtraordinaire)
                .when(testService)
                .genererZuwachmeldungAusserordentliche(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class));
        RRMeldung10Type temoinAugmentationAPI = new RRMeldung10Type();
        Mockito.doReturn(temoinAugmentationAPI)
                .when(testService)
                .genererZuwachmeldungHilflosenentschaedigung(
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class));

        RRMeldung10Type temoinModificationOrdinaire = new RRMeldung10Type();
        Mockito.doReturn(temoinModificationOrdinaire)
                .when(testService)
                .genererAenderungsmeldungOrdentliche(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class));
        RRMeldung10Type temoinModificationExtraordinaire = new RRMeldung10Type();
        Mockito.doReturn(temoinModificationExtraordinaire)
                .when(testService)
                .genererAenderungsmeldungAusserordentliche(Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class));
        RRMeldung10Type temoinModificationAPI = new RRMeldung10Type();
        Mockito.doReturn(temoinModificationAPI)
                .when(testService)
                .genererAenderungsmeldungHilflosenentschaedigung(
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification10Eme.class));

        RRMeldung10Type temoinDiminutionOrdinaire = new RRMeldung10Type();
        Mockito.doReturn(temoinDiminutionOrdinaire).when(testService)
                .genererAbgangsOrdentliche(Matchers.any(REAnnoncesDiminution10Eme.class));

        RRMeldung10Type temoinDiminutionExtrardinaire = new RRMeldung10Type();
        Mockito.doReturn(temoinDiminutionExtrardinaire).when(testService)
                .genererAbgangsAusserordentliche(Matchers.any(REAnnoncesDiminution10Eme.class));

        RRMeldung10Type temoinDiminutionAPI = new RRMeldung10Type();
        Mockito.doReturn(temoinDiminutionAPI).when(testService)
                .genererAbgangsHilflosenentschaedigung(Matchers.any(REAnnoncesDiminution10Eme.class));

        // TEST
        // Augmentation
        REAnnoncesAbstractLevel1A annonceMock = Mockito.spy(new REAnnoncesAbstractLevel1A());
        Mockito.when(annonceMock.getCodeApplication()).thenReturn("44");
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("10");
        assertEquals(temoinAugmentationOrdinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("20");
        assertEquals(temoinAugmentationExtraordinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("81");
        assertEquals(temoinAugmentationAPI, getAnnonceXml(annonceMock));
        // modif
        Mockito.when(annonceMock.getCodeApplication()).thenReturn("46");
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("10");
        assertEquals(temoinModificationOrdinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("20");
        assertEquals(temoinModificationExtraordinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("81");
        assertEquals(temoinModificationAPI, getAnnonceXml(annonceMock));
        // Diminution
        Mockito.when(annonceMock.getCodeApplication()).thenReturn("45");
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("13");
        assertEquals(temoinDiminutionOrdinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("20");
        assertEquals(temoinDiminutionExtrardinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("81");
        assertEquals(temoinDiminutionAPI, getAnnonceXml(annonceMock));

        // juste pour être sûr
        assertNotEquals(temoinDiminutionExtrardinaire, getAnnonceXml(annonceMock));
        assertNotEquals(temoinDiminutionOrdinaire, getAnnonceXml(annonceMock));
        assertNotEquals(temoinModificationAPI, getAnnonceXml(annonceMock));
        assertNotEquals(temoinAugmentationAPI, getAnnonceXml(annonceMock));

    }

    private Object getAnnonceXml(REAnnoncesAbstractLevel1A annonceMock) throws Exception {
        return testService.getAnnonceXml(annonceMock, null, null, null);
    }
}
