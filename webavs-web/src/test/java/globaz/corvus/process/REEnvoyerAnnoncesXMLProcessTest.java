package globaz.corvus.process;

import static org.junit.Assert.*;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.globall.db.BSpy;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Map;
import javax.xml.datatype.DatatypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import ch.admin.zas.rc.DJE9BeschreibungType;
import ch.admin.zas.rc.Gutschriften9Type;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRMeldung9Type;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.ZuwachsmeldungO9Type;
import ch.globaz.naos.ree.sedex.ValidationException;

public class REEnvoyerAnnoncesXMLProcessTest {

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

    public REEnvoyerAnnoncesXMLProcessTest() {

    }

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

        testInstance = Mockito.spy(new REEnvoyerAnnoncesXMLProcess());

        // on retourne le numéro de la caisse défini en dur plutôt que d'aller le chercher dans la BApplication
        Mockito.doReturn(numeroCaisse).when(testInstance).getNumeroCaisseFromApplication();
        Mockito.doReturn(numeroAgence).when(testInstance).getNumeroAgenceFromApplication();

        // bypass du logger
        Mockito.doNothing()
                .when(testInstance)
                .logMessageAvecInfos(Matchers.any(REAnnoncesAbstractLevel1A.class), Matchers.anyString(),
                        Matchers.anyString(), Matchers.anyString());

    }

    // FIXME refact this test for XML version test
    @Ignore
    @Test
    public void testPreparerAugmentation10Eme() throws Exception {
        // data
        REAnnoncesAugmentationModification10Eme enregistrement01 = new REAnnoncesAugmentationModification10Eme();
        REAnnoncesAugmentationModification10Eme enregistrement02 = Mockito
                .spy(new REAnnoncesAugmentationModification10Eme());

        // enregistrement 1
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
        enregistrement02.setCodeEnregistrement01(codeEnregistrement01);
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

        // run
        Map<String, String> result = testInstance.preparerAugmentation10EmeEnregistrement01(enregistrement01);
        result.putAll(testInstance.preparerAugmentation10EmeEnregistrement02(enregistrement02));

        // test
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);

        // Enregistrement 01
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(20, false, referenceCaisseInterne),
                result.get(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, noAssAyantDroit),
                result.get(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, premierNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, secondNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, nouveauNoAssureAyantDroit),
                result.get(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, etatCivil),
                result.get(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, isRefugie),
                result.get(IHEAnnoncesViewBean.CS_REFUGIE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, true, cantonEtatDomicile),
                result.get(IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, genrePrestation),
                result.get(IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, debutDroit),
                result.get(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, mensualitePrestationsFrancs),
                result.get(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, finDroit),
                result.get(IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, moisRapport),
                result.get(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, codeMutation),
                result.get(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION));

        Assert.assertEquals(codeApplication, result.get(IHEAnnoncesViewBean.CODE_APPLICATION));
        Assert.assertEquals(codeEnregistrement01, result.get(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, echelleRente),
                result.get(IHEAnnoncesViewBean.CS_ECHELLE_DE_RENTES));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, dureeCoEchelleRenteAv73),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_AV_1973_AAMM));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, dureeCoEchelleRenteDes73),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_DES_1973_AAMM));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, dureeCotManquante48_72),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1948_72));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, dureeCotManquante73_78),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1973_78));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, anneeCotClasseAge),
                result.get(IHEAnnoncesViewBean.CS_ANNEE_COTISATIONS_DE_LA_CLASSE_AGE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(8, true, ramDeterminant),
                result.get(IHEAnnoncesViewBean.CS_REVENU_ANNUEL_MOYEN_DETERMINANT_EN_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, codeRevenuSplitte),
                result.get(IHEAnnoncesViewBean.CS_CODE_DE_REVENUS_SPLITTES));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, dureeCotPourDetRAM),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_POUR_DETERMINER_REVENU_ANNUEL_MOYEN_AAMM));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, anneeNiveau),
                result.get(IHEAnnoncesViewBean.CS_ANNEE_DE_NIVEAU));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, true, nbreAnneeBonifTrans),
                result.get(IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_EDUCATIVES_AADD));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, true, nbreAnneeAnticipation),
                result.get(IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_ASSISTANCE_AADD));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, nbreAnneeBonifTrans),
                result.get(IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_TRANSITOIREs_AD));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, true, officeAICompetent),
                result.get(IHEAnnoncesViewBean.CS_OFFICE_AI));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, false, degreInvalidite),
                result.get(IHEAnnoncesViewBean.CS_DEGRE_INVALIDITE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, codeInfirmite),
                result.get(IHEAnnoncesViewBean.CS_CODE_INFIRMITE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, survenanceEvenAssure),
                result.get(IHEAnnoncesViewBean.CS_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, ageDebutInvalidite),
                result.get(IHEAnnoncesViewBean.CS_AGE_DEBUT_INVALIDITE_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, genreDroitAPI),
                result.get(IHEAnnoncesViewBean.CS_GENRE_DROIT_API));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, JadeStringUtil.removeChar(reduction, '.')),
                result.get(IHEAnnoncesViewBean.CS_REDUCTION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, casSpecial1),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_1_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, casSpecial2),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_2_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, casSpecial3),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_3_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, casSpecial4),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_4_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, casSpecial5),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_5_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, nbreAnneeAnticipation),
                result.get(IHEAnnoncesViewBean.CS_NB_ANNEE_ANTICIPATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, reductionAnticipation),
                result.get(IHEAnnoncesViewBean.CS_REDUCTION_ANTICIPATION_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, dateDebutAnticipation),
                result.get(IHEAnnoncesViewBean.CS_DATE_DEBUT_ANTICIPATION_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, false, dureeAjournement),
                result.get(IHEAnnoncesViewBean.CS_DUREE_AJOURNEMENT_AMM));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, supplementAjournement),
                result.get(IHEAnnoncesViewBean.CS_SUPPLEMENT_AJOURNEMENT_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, dateRevocationAjournement),
                result.get(IHEAnnoncesViewBean.CS_DATE_REVOCATION_AJOURNEMENT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, isSurvivant),
                result.get(IHEAnnoncesViewBean.CS_CODE_SURVIVANT_INVALIDE));
    }

    // FIXME refact this test for XML version test
    @Ignore
    @Test
    public void testPreparerAugmentation9Eme() throws Exception {
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

        // run
        Map<String, String> result = testInstance.preparerAugmentation9EmeEnregistrement01(enregistrement01);
        result.putAll(testInstance.preparerAugmentation9EmeEnregistrement02(enregistrement02));

        // test
        // enregistrement 01
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(20, false, referenceCaisseInterne),
                result.get(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, noAssAyantDroit),
                result.get(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, premierNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, secondNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, nouveauNoAssureAyantDroit),
                result.get(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, etatCivil),
                result.get(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, isRefugie),
                result.get(IHEAnnoncesViewBean.CS_REFUGIE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, true, cantonEtatDomicile),
                result.get(IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, genrePrestation),
                result.get(IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, debutDroit),
                result.get(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, mensualitePrestationsFrancs),
                result.get(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, mensualiteRenteOrdRemp),
                result.get(IHEAnnoncesViewBean.CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, finDroit),
                result.get(IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, moisRapport),
                result.get(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, codeMutation),
                result.get(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION));

        // Enregistrement 02
        Assert.assertEquals(enregistrement02.getCodeApplication(), result.get(IHEAnnoncesViewBean.CODE_APPLICATION));
        Assert.assertEquals(enregistrement02.getCodeEnregistrement01(),
                result.get(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(8, true, enregistrement02.getRamDeterminant()),
                result.get(IHEAnnoncesViewBean.CS_REVENU_ANNUEL_MOYEN_DETERMINANT_EN_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCotPourDetRAM()),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_POUR_DETERMINER_REVENU_ANNUEL_MOYEN_AAMM));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, false, enregistrement02.getAnneeNiveau()),
                result.get(IHEAnnoncesViewBean.CS_ANNEE_DE_NIVEAU));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, enregistrement02.getRevenuPrisEnCompte()),
                result.get(IHEAnnoncesViewBean.CS_REVENUS_PRIS_EN_COMPTE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getEchelleRente()),
                result.get(IHEAnnoncesViewBean.CS_ECHELLE_DE_RENTES));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCoEchelleRenteAv73()),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_AV_1973_AAMM));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCoEchelleRenteDes73()),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_DES_1973_AAMM));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getDureeCotManquante48_72()),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1948_72));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getAnneeCotClasseAge()),
                result.get(IHEAnnoncesViewBean.CS_ANNEE_COTISATIONS_DE_LA_CLASSE_AGE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, false, enregistrement02.getDureeAjournement()),
                result.get(IHEAnnoncesViewBean.CS_DUREE_AJOURNEMENT_AMM));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(5, true, enregistrement02.getSupplementAjournement()),
                result.get(IHEAnnoncesViewBean.CS_SUPPLEMENT_AJOURNEMENT_FRANCS));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(4, false, enregistrement02.getDateRevocationAjournement()),
                result.get(IHEAnnoncesViewBean.CS_DATE_REVOCATION_AJOURNEMENT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, enregistrement02.getIsLimiteRevenu()),
                result.get(IHEAnnoncesViewBean.CS_LIMITES_DE_REVENU));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, enregistrement02.getIsMinimumGaranti()),
                result.get(IHEAnnoncesViewBean.CS_MINIMUM_GARANTIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getOfficeAICompetent()),
                result.get(IHEAnnoncesViewBean.CS_OFFICEAI_COMPETENT_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getOfficeAiCompEpouse()),
                result.get(IHEAnnoncesViewBean.CS_OFFICEAI_COMPETENT_EPOUSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, true, enregistrement02.getDegreInvalidite()),
                result.get(IHEAnnoncesViewBean.CS_DEGREINVALIDITE_AYANT_DROIT));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(3, true, enregistrement02.getDegreInvaliditeEpouse()),
                result.get(IHEAnnoncesViewBean.CS_DEGREINVALIDITE_EPOUSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, false, enregistrement02.getCodeInfirmite()),
                result.get(IHEAnnoncesViewBean.CS_CODEINFIRMITE_AYANT_DROIT));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(5, false, enregistrement02.getCodeInfirmiteEpouse()),
                result.get(IHEAnnoncesViewBean.CS_CODEINFIRMITE_EPOUSE));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(4, false, enregistrement02.getSurvenanceEvenAssure()),
                result.get(IHEAnnoncesViewBean.CS_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT_MMAA));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(4, false, enregistrement02.getSurvenanceEvtAssureEpouse()),
                result.get(IHEAnnoncesViewBean.CS_SURVENANCE_EVENEMENT_ASSURE_EPOUSE_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, enregistrement02.getAgeDebutInvalidite()),
                result.get(IHEAnnoncesViewBean.CS_AGE_DEBUT_INVALIDITE_AYANT_DROIT));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(1, false, enregistrement02.getAgeDebutInvaliditeEpouse()),
                result.get(IHEAnnoncesViewBean.CS_AGE_DEBUT_INVALIDITE_EPOUSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, enregistrement02.getGenreDroitAPI()),
                result.get(IHEAnnoncesViewBean.CS_GENRE_DROIT_API));
        Assert.assertEquals(
                result.get(IHEAnnoncesViewBean.CS_REDUCTION),
                testInstance.formatXPosAppendWithBlank(2, true,
                        JadeStringUtil.removeChar(enregistrement02.getReduction(), '.')));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial1()),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_1_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial2()),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_2_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial3()),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_3_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial4()),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_4_CODE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial5()),
                result.get(IHEAnnoncesViewBean.CS_CAS_SPECIAL_5_CODE));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getDureeCotManquante73_78()),
                result.get(IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1973_78));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(8, true, enregistrement02.getRevenuAnnuelMoyenSansBTE()),
                result.get(IHEAnnoncesViewBean.CS_REVENU_ANNUEL_MOYEN_SANS_BONIFI_TACHES_EDUC_FRANCS));
        Assert.assertEquals(
                testInstance.formatXPosAppendWithBlank(6, true, enregistrement02.getBteMoyennePrisEnCompte()),
                result.get(IHEAnnoncesViewBean.CS_BONIFI_TACHES_EDUC_MOYENNES_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, enregistrement02.getNombreAnneeBTE()),
                result.get(IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_EDUC));
    }

    // FIXME refact this test for XML version test
    @Ignore
    @Test
    public void testPreparerDiminution10Eme() throws Exception {
        // data
        REAnnoncesDiminution10Eme enregistrement01 = new REAnnoncesDiminution10Eme();

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

        // run
        Map<String, String> result = testInstance.preparerDiminution10Eme(enregistrement01);

        // test
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);

        // Enregistrement 01
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(20, false, referenceCaisseInterne),
                result.get(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, noAssAyantDroit),
                result.get(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, premierNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, secondNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, nouveauNoAssureAyantDroit),
                result.get(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, etatCivil),
                result.get(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, isRefugie),
                result.get(IHEAnnoncesViewBean.CS_REFUGIE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, true, cantonEtatDomicile),
                result.get(IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, genrePrestation),
                result.get(IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, debutDroit),
                result.get(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, mensualitePrestationsFrancs),
                result.get(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, finDroit),
                result.get(IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, moisRapport),
                result.get(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, codeMutation),
                result.get(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION));
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

        // run
        Map<String, String> result = testInstance.preparerDiminution9Eme(enregistrement01);

        // test
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);

        // Enregistrement 01
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(20, false, referenceCaisseInterne),
                result.get(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, noAssAyantDroit),
                result.get(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, premierNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, secondNoAssComplementaire),
                result.get(IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(11, false, nouveauNoAssureAyantDroit),
                result.get(IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, etatCivil),
                result.get(IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(1, false, isRefugie),
                result.get(IHEAnnoncesViewBean.CS_REFUGIE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(3, true, cantonEtatDomicile),
                result.get(IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, genrePrestation),
                result.get(IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, debutDroit),
                result.get(IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(5, true, mensualitePrestationsFrancs),
                result.get(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, finDroit),
                result.get(IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(4, false, moisRapport),
                result.get(IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT));
        Assert.assertEquals(testInstance.formatXPosAppendWithBlank(2, true, codeMutation),
                result.get(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION));
    }

    @Test
    public void testValidateUnitMessage() throws Exception {
        // je ne sais pas dans quoi je me lance e je corrige au fur et a mesure que la validation pète pour obtenir un
        // test OK
        ch.admin.zas.rc.ObjectFactory of = new ch.admin.zas.rc.ObjectFactory();
        RRMeldung9Type rrm9 = of.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente rrm9o = of.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type zmt9 = of.createZuwachsmeldungO9Type();
        // numero de caisse et agence
        zmt9.setKasseZweigstelle("059000");
        // max inclusive
        zmt9.setMeldungsnummer(999999L);
        RRLeistungsberechtigtePersonAuslType lbp = of.createRRLeistungsberechtigtePersonAuslType();
        lbp.setVersichertennummer("7560000000002");
        lbp.setFamilienAngehoerige(of.createFamilienAngehoerigeType());
        // 1 = ?
        lbp.setZivilstand(Short.parseShort("1", 10));
        // 100 = ?
        lbp.setWohnkantonStaat("100");
        zmt9.setLeistungsberechtigtePerson(lbp);
        ZuwachsmeldungO9Type.Leistungsbeschreibung lb = of.createZuwachsmeldungO9TypeLeistungsbeschreibung();
        lb.setLeistungsart("10");
        lb.setAnspruchsbeginn(DatatypeFactory.newInstance().newXMLGregorianCalendar(2017, 01, 01, 01, 01, 01, 01, 01));
        lb.setMonatsbetrag(new BigDecimal(0));
        ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen zlb = of
                .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        zlb.setNiveaujahr(DatatypeFactory.newInstance().newXMLGregorianCalendar(1955, 01, 01, 01, 01, 01, 01, 01));
        SkalaBerechnungType ska = of.createSkalaBerechnungType();
        ska.setBeitragsdauerAb1973(BigDecimal.valueOf(43.00));
        ska.setBeitragsdauerVor1973(BigDecimal.valueOf(20.00));
        ska.setSkala(Short.valueOf("44", 10));
        zlb.setSkalaBerechnung(ska);

        DJE9BeschreibungType dje = of.createDJE9BeschreibungType();
        dje.setDurchschnittlichesJahreseinkommen(BigDecimal.valueOf(1234));
        dje.setBeitragsdauerDurchschnittlichesJahreseinkommen(BigDecimal.valueOf(21));
        dje.setAngerechneteEinkommen(Short.valueOf("1", 10));
        zlb.setDJEBeschreibung(dje);
        Gutschriften9Type gut = of.createGutschriften9Type();
        zlb.setGutschriften(gut);
        lb.setBerechnungsgrundlagen(zlb);
        zmt9.setLeistungsbeschreibung(lb);
        zmt9.setBerichtsmonat(DatatypeFactory.newInstance().newXMLGregorianCalendar(2017, 01, 01, 01, 01, 01, 01, 01));
        rrm9o.setZuwachsmeldung(zmt9);
        rrm9.setOrdentlicheRente(rrm9o);
        try {
            testInstance.validateUnitMessage(rrm9);
        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }

    }

    @Test
    public void testFailValidateUnitMessage() throws Exception {
        ch.admin.zas.rc.ObjectFactory of = new ch.admin.zas.rc.ObjectFactory();
        RRMeldung9Type rrm9 = of.createRRMeldung9Type();
        try {
            testInstance.validateUnitMessage(rrm9);
            fail("Doit dans ce cas lever une exception et ne jamais passer ici");
        } catch (ValidationException ve) {
            assertNotNull(ve);
            System.err.println(ve.getFormattedMessage());
        }

    }

}
