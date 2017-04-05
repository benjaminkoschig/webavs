package globaz.corvus.annonce.service;

import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.process.REEnvoyerAnnoncesXMLProcess;
import globaz.globall.db.BSpy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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

    @Spy
    private REEnvoyerAnnoncesXMLProcess testInstance;
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

        testService = new REAnnonces9eXmlService();
        testInstance = Mockito.spy(new REEnvoyerAnnoncesXMLProcess());
    }

    @Ignore
    @Test
    public void testAnnonceAugmentationOrdinaire9e() throws Exception {

        // data
        REAnnoncesAugmentationModification9Eme enregistrement01 = new REAnnoncesAugmentationModification9Eme();
        REAnnoncesAugmentationModification9Eme enregistrement02 = Mockito
                .spy(new REAnnoncesAugmentationModification9Eme());

        // enregistrement 01
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
        enregistrement01.setMensualiteRenteOrdRemp(mensualiteRenteOrdRemp);
        enregistrement01.setFinDroit(finDroit);
        enregistrement01.setMoisRapport(moisRapport);
        enregistrement01.setCodeMutation(codeMutation);

        // enregistrement 02
        Mockito.when(enregistrement02.getSpy()).thenReturn(new BSpy("20110101120000test"));
        enregistrement02.setCodeApplication(codeApplication);
        enregistrement02.setCodeEnregistrement01(codeEnregistrement01);
        enregistrement02.setRamDeterminant(ramDeterminant);
        enregistrement02.setDureeCotPourDetRAM(dureeCotPourDetRAM);
        enregistrement02.setAnneeNiveau(anneeNiveau);
        enregistrement02.setRevenuPrisEnCompte(revenuPrisEnCompte);
        enregistrement02.setEchelleRente(echelleRente);
        enregistrement02.setDureeCoEchelleRenteAv73(dureeCoEchelleRenteAv73);
        enregistrement02.setDureeCoEchelleRenteDes73(dureeCoEchelleRenteDes73);
        enregistrement02.setDureeCotManquante48_72(dureeCotManquante48_72);
        enregistrement02.setAnneeCotClasseAge(anneeCotClasseAge);
        enregistrement02.setDureeAjournement(dureeAjournement);
        enregistrement02.setSupplementAjournement(supplementAjournement);
        enregistrement02.setDateRevocationAjournement(dateRevocationAjournement);
        enregistrement02.setIsLimiteRevenu(isLimiteRevenu);
        enregistrement02.setIsMinimumGaranti(isMinimumGaranti);
        enregistrement02.setOfficeAICompetent(officeAICompetent);
        enregistrement02.setOfficeAiCompEpouse(officeAiCompEpouse);
        enregistrement02.setDegreInvalidite(degreInvalidite);
        enregistrement02.setDegreInvaliditeEpouse(degreInvaliditeEpouse);
        enregistrement02.setCodeInfirmite(codeInfirmite);
        enregistrement02.setCodeInfirmiteEpouse(codeInfirmiteEpouse);
        enregistrement02.setSurvenanceEvenAssure(survenanceEvenAssure);
        enregistrement02.setSurvenanceEvtAssureEpouse(survenanceEvtAssureEpouse);
        enregistrement02.setAgeDebutInvalidite(ageDebutInvalidite);
        enregistrement02.setAgeDebutInvaliditeEpouse(ageDebutInvaliditeEpouse);
        enregistrement02.setGenreDroitAPI(genreDroitAPI);
        enregistrement02.setReduction(reduction);
        enregistrement02.setCasSpecial1(casSpecial1);
        enregistrement02.setCasSpecial2(casSpecial2);
        enregistrement02.setCasSpecial3(casSpecial3);
        enregistrement02.setCasSpecial4(casSpecial4);
        enregistrement02.setCasSpecial5(casSpecial5);
        enregistrement02.setDureeCotManquante73_78(dureeCotManquante73_78);
        enregistrement02.setRevenuAnnuelMoyenSansBTE(revenuAnnuelMoyenSansBTE);
        enregistrement02.setBteMoyennePrisEnCompte(bteMoyennePrisEnCompte);

        RRMeldung9Type annonce = testService.annonceAugmentationOrdinaire9e(enregistrement01, enregistrement02);

        try {
            testInstance.validateUnitMessage(annonce);
        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }

    }

    // FIXME refact this test for XML version test
    @Ignore
    @Test
    public void testPreparerDiminution9Eme() throws Exception {
        // data
        REAnnoncesDiminution9Eme enregistrement01 = new REAnnoncesDiminution9Eme();

        // enregistrement 1
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

    }

}
