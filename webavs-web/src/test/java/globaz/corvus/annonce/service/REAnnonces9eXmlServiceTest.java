package globaz.corvus.annonce.service;

import static org.junit.Assert.*;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
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
import ch.admin.zas.rc.RRMeldung9Type;
import ch.globaz.naos.ree.sedex.ValidationException;

public class REAnnonces9eXmlServiceTest {

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
    private REAnnonces9eXmlService testService;

    @Before
    public void setUp() throws Exception {

        // enregistrement 1
        numeroCaisse = "059";
        numeroAgence = "000";
        referenceCaisseInterne = "AUGCICIGLO";
        noAssAyantDroit = "7563985732784";
        premierNoAssComplementaire = "7566499521103";
        etatCivil = "2";
        isRefugie = "0";
        cantonEtatDomicile = "010";
        genrePrestation = "10";
        debutDroit = "0311";
        mensualitePrestationsFrancs = "02320";
        idAnnonce = "1546129";
        codeEnregistrement01 = "01";
        secondNoAssComplementaire = "7565024495087";
        mensualiteRenteOrdRemp = "00000";
        moisRapport = "0117";
        // enregistrement 2
        codeApplication = "44";
        codeEnregistrement01 = "02";
        echelleRente = "44";
        dureeCoEchelleRenteAv73 = "1000";
        dureeCoEchelleRenteDes73 = "1000";
        dureeCotManquante48_72 = "00";
        dureeCotManquante73_78 = "00";
        anneeCotClasseAge = "20";
        ramDeterminant = "44760";
        codeRevenuSplitte = "0";
        dureeCotPourDetRAM = "2000";
        anneeNiveau = "96";
        nombreAnneeBTE = "0500";
        // FIXME pourquoi 350 non valable?
        officeAICompetent = "35";
        degreInvalidite = "47";
        codeInfirmite = "64665";
        survenanceEvenAssure = "0208";
        ageDebutInvalidite = "0";
        genreDroitAPI = "";
        casSpecial1 = "02";
        codeMutation = "1";
        revenuAnnuelMoyenSansBTE = "200";
        revenuPrisEnCompte = "1";
        bteMoyennePrisEnCompte = "400";

        testService = Mockito.spy(new REAnnonces9eXmlService());
        testInstance = Mockito.spy(new REEnvoyerAnnoncesXMLProcess());
        Mockito.doReturn("059000").when(testService).retourneCaisseAgence();
    }

    @Test
    public void testAnnonceAugmentationOrdinaire9e() throws Exception {

        // data
        REAnnoncesAugmentationModification9Eme enregistrement01 = new REAnnoncesAugmentationModification9Eme();
        REAnnoncesAugmentationModification9Eme enregistrement02 = Mockito
                .spy(new REAnnoncesAugmentationModification9Eme());

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
        enregistrement01.setEtatCivil(etatCivil);
        enregistrement01.setIsRefugie(isRefugie);
        enregistrement01.setCantonEtatDomicile(cantonEtatDomicile);
        enregistrement01.setGenrePrestation(genrePrestation);
        enregistrement01.setDebutDroit(debutDroit);
        enregistrement01.setMensualitePrestationsFrancs(mensualitePrestationsFrancs);
        enregistrement01.setMensualiteRenteOrdRemp(mensualiteRenteOrdRemp);
        enregistrement01.setFinDroit(finDroit);
        enregistrement01.setMoisRapport(moisRapport);
        enregistrement01.setCodeMutation(codeMutation);

        // enregistrement 2
        Mockito.when(enregistrement02.getSpy()).thenReturn(new BSpy("20110101120000test"));
        enregistrement02.setCodeApplication(codeApplication);
        enregistrement02.setCodeEnregistrement01(codeEnregistrement02);
        enregistrement02.setDureeCotPourDetRAM(dureeCotPourDetRAM);
        enregistrement02.setDureeCoEchelleRenteAv73(dureeCoEchelleRenteAv73);
        enregistrement02.setDureeCoEchelleRenteDes73(dureeCoEchelleRenteDes73);
        enregistrement02.setEchelleRente(echelleRente);
        enregistrement02.setDureeCotManquante48_72(dureeCotManquante48_72);
        enregistrement02.setDureeCotManquante73_78(dureeCotManquante73_78);
        enregistrement02.setAnneeCotClasseAge(anneeCotClasseAge);
        enregistrement02.setRamDeterminant(ramDeterminant);
        enregistrement02.setAnneeNiveau(anneeNiveau);
        enregistrement02.setNombreAnneeBTE(nombreAnneeBTE);
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
        enregistrement02.setDureeAjournement(dureeAjournement);
        enregistrement02.setSupplementAjournement(supplementAjournement);
        enregistrement02.setDateRevocationAjournement(dateRevocationAjournement);
        enregistrement02.setRevenuAnnuelMoyenSansBTE(revenuAnnuelMoyenSansBTE);
        enregistrement02.setRevenuPrisEnCompte(revenuPrisEnCompte);
        enregistrement02.setBteMoyennePrisEnCompte(bteMoyennePrisEnCompte);

        try {
            // Augmentation Ordinaire
            testInstance.validateUnitMessage(testService.annonceAugmentationOrdinaire9e(enregistrement01,
                    enregistrement02));
            // Modification Ordinaire
            testInstance.validateUnitMessage(testService.annonceChangementOrdinaire9e(enregistrement01,
                    enregistrement02));

            // Extraordinaire
            enregistrement01.setGenrePrestation("74");
            // Augmentation Extraordinaire
            testInstance.validateUnitMessage(testService.annonceAugmentationExtraOrdinaire9e(enregistrement01,
                    enregistrement02));
            // Modification Extraordinaire
            testInstance.validateUnitMessage(testService.annonceChangementExtraOrdinaire9e(enregistrement01,
                    enregistrement02));

            // API
            enregistrement01.setGenrePrestation("91");
            enregistrement02.setGenreDroitAPI("1");
            // Augmentation API
            testInstance.validateUnitMessage(testService.annonceAugmentationAPI9e(enregistrement01, enregistrement02));
            // Modification API
            testInstance.validateUnitMessage(testService.annonceChangementAPI9e(enregistrement01, enregistrement02));

        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }

    }

    @Test
    public void testPreparerDiminution9Eme() throws Exception {
        // data
        finDroit = "1217";
        REAnnoncesDiminution9Eme enregistrement01 = new REAnnoncesDiminution9Eme();

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
            testInstance.validateUnitMessage(testService.annonceDiminutionOrdinaire(enregistrement01));
            // extraordinaire
            enregistrement01.setGenrePrestation("20");
            testInstance.validateUnitMessage(testService.annonceDiminutionExtraOrdinaire(enregistrement01));
            // api
            enregistrement01.setGenrePrestation("91");
            testInstance.validateUnitMessage(testService.annonceDiminutionAPI(enregistrement01));
        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }
    }

    @Test
    public void testGetAnnonceXmlDirections() throws Exception {

        // SetUp
        // Mute DAO
        Mockito.doReturn(new REAnnoncesAugmentationModification9Eme()).when(testService)
                .retrieveAnnonceAugModif9(Matchers.any(REAnnoncesAbstractLevel1A.class), Matchers.any(BSession.class));
        Mockito.doReturn(new REAnnoncesAugmentationModification9Eme())
                .when(testService)
                .retrieveAnnonceAugModif9_2(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(BSession.class));
        Mockito.doReturn(new REAnnoncesDiminution9Eme()).when(testService)
                .retrieveAnnonceDimi9(Matchers.any(REAnnoncesAbstractLevel1A.class), Matchers.any(BSession.class));
        Mockito.doNothing()
                .when(testService)
                .checkAndUpdate9eme(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(BITransaction.class));
        Mockito.doNothing()
                .when(testService)
                .checkAndUpdateDimi9eme(Matchers.any(REAnnoncesDiminution9Eme.class), Matchers.any(BITransaction.class));
        // Mute ANAKIN
        Mockito.doNothing()
                .when(testService)
                .parseAugmentationAvecAnakin(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class), Matchers.any(BSession.class),
                        Matchers.any(String.class));
        Mockito.doNothing()
                .when(testService)
                .parseDiminutionAvecAnakin(Matchers.any(REAnnoncesDiminution9Eme.class), Matchers.any(BSession.class),
                        Matchers.any(String.class));

        // Temoins
        RRMeldung9Type temoinAugmentationOrdinaire = new RRMeldung9Type();
        Mockito.doReturn(temoinAugmentationOrdinaire)
                .when(testService)
                .annonceAugmentationOrdinaire9e(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class));
        RRMeldung9Type temoinAugmentationExtraordinaire = new RRMeldung9Type();
        Mockito.doReturn(temoinAugmentationExtraordinaire)
                .when(testService)
                .annonceAugmentationExtraOrdinaire9e(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class));
        RRMeldung9Type temoinAugmentationAPI = new RRMeldung9Type();
        Mockito.doReturn(temoinAugmentationAPI)
                .when(testService)
                .annonceAugmentationAPI9e(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class));

        RRMeldung9Type temoinModificationOrdinaire = new RRMeldung9Type();
        Mockito.doReturn(temoinModificationOrdinaire)
                .when(testService)
                .annonceChangementOrdinaire9e(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class));
        RRMeldung9Type temoinModificationExtraordinaire = new RRMeldung9Type();
        Mockito.doReturn(temoinModificationExtraordinaire)
                .when(testService)
                .annonceChangementExtraOrdinaire9e(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class));
        RRMeldung9Type temoinModificationAPI = new RRMeldung9Type();
        Mockito.doReturn(temoinModificationAPI)
                .when(testService)
                .annonceChangementAPI9e(Matchers.any(REAnnoncesAugmentationModification9Eme.class),
                        Matchers.any(REAnnoncesAugmentationModification9Eme.class));

        RRMeldung9Type temoinDiminutionOrdinaire = new RRMeldung9Type();
        Mockito.doReturn(temoinDiminutionOrdinaire).when(testService)
                .annonceDiminutionOrdinaire(Matchers.any(REAnnoncesDiminution9Eme.class));

        RRMeldung9Type temoinDiminutionExtrardinaire = new RRMeldung9Type();
        Mockito.doReturn(temoinDiminutionExtrardinaire).when(testService)
                .annonceDiminutionExtraOrdinaire(Matchers.any(REAnnoncesDiminution9Eme.class));

        RRMeldung9Type temoinDiminutionAPI = new RRMeldung9Type();
        Mockito.doReturn(temoinDiminutionAPI).when(testService)
                .annonceDiminutionAPI(Matchers.any(REAnnoncesDiminution9Eme.class));

        // TEST
        // Augmentation
        REAnnoncesAbstractLevel1A annonceMock = Mockito.spy(new REAnnoncesAbstractLevel1A());
        Mockito.when(annonceMock.getCodeApplication()).thenReturn("41");
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("10");
        assertEquals(temoinAugmentationOrdinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("20");
        assertEquals(temoinAugmentationExtraordinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("91");
        assertEquals(temoinAugmentationAPI, getAnnonceXml(annonceMock));
        // Modification
        Mockito.when(annonceMock.getCodeApplication()).thenReturn("43");
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("10");
        assertEquals(temoinModificationOrdinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("20");
        assertEquals(temoinModificationExtraordinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("91");
        assertEquals(temoinModificationAPI, getAnnonceXml(annonceMock));
        // Diminution
        Mockito.when(annonceMock.getCodeApplication()).thenReturn("42");
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("13");
        assertEquals(temoinDiminutionOrdinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("20");
        assertEquals(temoinDiminutionExtrardinaire, getAnnonceXml(annonceMock));
        Mockito.when(annonceMock.getGenrePrestation()).thenReturn("91");
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
