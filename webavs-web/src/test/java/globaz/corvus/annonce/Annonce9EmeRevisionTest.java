package globaz.corvus.annonce;

import static org.junit.Assert.*;
import org.junit.Test;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle9EmeRevision;
import globaz.corvus.annonce.formatter.RECreationAnnonceFormatterException;
import globaz.corvus.annonce.reader.REBaseDeCalcul9EmeRevisionReader;
import globaz.corvus.annonce.reader.RECreationAnnonceGeneralReader;
import globaz.corvus.annonce.reader.RERenteAccordeeReader;
import globaz.corvus.annonce.service.RECreationAnnonceService;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.acor.PRACORConst;

public class Annonce9EmeRevisionTest {

    // /**
    // * Impression d'une annonce ponctuelle correctement remplie et test du formatage des valeurs
    // */
    //
    // public void testAnnoncePonctuelle1() {
    // REAnnoncePonctuelle9EmeRevision annonceACreer = getAnnoncePonctuelle1();
    // RECreationAnnonceService serviceCreationAnnonce = new RECreationAnnonceService();
    // REAnnoncesAPersister annoncesAPersister = null;
    //
    // try {
    // annoncesAPersister = serviceCreationAnnonce.creerAnnonce(annonceACreer);
    // } catch (IllegalArgumentException e) {
    // assertTrue(false);
    // } catch (RECreationAnnonceFormatterException e) {
    // assertTrue(false);
    // }
    //
    // System.out.println("Impression d'une annonce ponctuelle remplie [1]");
    // REAnnonceRentePrinter printer = new REAnnonceRentePrinter();
    // printer.print(annonceACreer, annoncesAPersister);
    // }

    /**
     * Impression d'une annonce ponctuelle vide
     */
    @Test
    public void testAnnoncePonctuelleVide() {
        REAnnoncePonctuelle9EmeRevision annonceACreer = new REAnnoncePonctuelle9EmeRevision();
        annonceACreer.setGenrePrestation(10);
        RECreationAnnonceService serviceCreationAnnonce = new RECreationAnnonceService();
        REAnnoncesAPersister annoncesAPersister = null;

        try {
            annoncesAPersister = serviceCreationAnnonce.creerAnnonce(annonceACreer);
        } catch (IllegalArgumentException e) {
            assertTrue(false);
        } catch (RECreationAnnonceFormatterException e) {
            assertTrue(false);
        }

        System.out.println("Impression d'une annonce ponctuelle vide");
        REAnnonceRentePrinter printer = new REAnnonceRentePrinter();
        printer.print(annonceACreer, annoncesAPersister);
        System.out.println();
    }

    /**
     * Test toute la cha?ne de cr?ation d'annonces
     * - Reader, AnnonceDomain, entityGenerator, formatter
     *
     * @throws JAException
     * @throws REIllegalNSSFormatException
     */
    @Test
    public void testTouteLaChaine() throws JAException, REIllegalNSSFormatException {
        REAnnoncePonctuelle9EmeRevision annoncePonctuelle10Eme = new REAnnoncePonctuelle9EmeRevision();
        RECreationAnnonceGeneralReader generalReader = new RECreationAnnonceGeneralReader();
        RERenteAccordeeReader renteAccordeeReader = new RERenteAccordeeReader();
        REBaseDeCalcul9EmeRevisionReader baseDeCalculReader = new REBaseDeCalcul9EmeRevisionReader();
        // ---------------------------------------------------------------------------//

        // MM.AAAA
        String dateDernierPaiement = "11.2014";

        annoncePonctuelle10Eme.setNumeroCaisse(generalReader.convertNumeroCaisse("150"));
        annoncePonctuelle10Eme.setNumeroAgence(generalReader.convertNumeroAgence("0"));
        annoncePonctuelle10Eme.setUtilisateurPourReferenceCaisseInterne("ccjuglo");

        annoncePonctuelle10Eme.setDebutDroit(new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1)));
        annoncePonctuelle10Eme.setFinDroit(new JADate(new JACalendarGregorian().addYears(dateDernierPaiement, 1)));
        annoncePonctuelle10Eme.setMoisRapport(new JADate(new JACalendarGregorian().addMonths(dateDernierPaiement, 1)));
        annoncePonctuelle10Eme.setCodeMutation(78);

        annoncePonctuelle10Eme.setNoAssAyantDroit(generalReader.convertFormatedNss("756.8987.4676.72"));
        annoncePonctuelle10Eme.setMensualitePrestationsFrancs(generalReader.convertMensualitePrestationsFrancs("758"));

        // Valeurs r?cup?r? depuis les rentes accord?es
        annoncePonctuelle10Eme.setIdTiers(renteAccordeeReader.convertIdTiers("658423"));
        annoncePonctuelle10Eme.setRefugie(renteAccordeeReader.convertIsRefugie(""));
        annoncePonctuelle10Eme.setGenrePrestation(renteAccordeeReader.convertGenrePrestation("10"));
        annoncePonctuelle10Eme.setSupplementAjournement(renteAccordeeReader.convertSupplementAjournement("112"));
        annoncePonctuelle10Eme.setReduction(renteAccordeeReader.convertReduction("40"));

        annoncePonctuelle10Eme.setCasSpecial1(renteAccordeeReader.convertCasSpecial("8"));
        annoncePonctuelle10Eme.setCasSpecial2(renteAccordeeReader.convertCasSpecial("12"));
        annoncePonctuelle10Eme.setCasSpecial3(renteAccordeeReader.convertCasSpecial("99"));
        annoncePonctuelle10Eme.setCasSpecial4(renteAccordeeReader.convertCasSpecial("02"));
        annoncePonctuelle10Eme.setCasSpecial5(renteAccordeeReader.convertCasSpecial("68"));

        annoncePonctuelle10Eme
                .setDateRevocationAjournement(renteAccordeeReader.convertDateRevocationAjournement("06.2005"));

        annoncePonctuelle10Eme.setDureeAjournement(renteAccordeeReader.convertDureeAjournementValeurEntiere("1.02"),
                renteAccordeeReader.convertDureeAjournementValeurDecimal("1.02"));

        Integer etatCivil = renteAccordeeReader.convertEtatCivil(PRACORConst.csEtatCivilToAcorForRentes("515002"));
        assertTrue(2 == etatCivil);
        annoncePonctuelle10Eme.setEtatCivil(etatCivil);

        String canton = PRACORConst.csCantonToAcor("505026");
        annoncePonctuelle10Eme.setCantonEtatDomicile(generalReader.convertCantonEtatDomicile(canton));
        assertTrue("050".equals(canton));

        annoncePonctuelle10Eme.setPremierNoAssComplementaire(generalReader.convertFormatedNss("756.0592.5773.88"));
        annoncePonctuelle10Eme.setSecondNoAssComplementaire(generalReader.convertFormatedNss("756.9999.5773.88"));

        // Si API Saisir le genre droit API
        // genre droit API codeSystem = 52809001 -> donne le code 1
        annoncePonctuelle10Eme.setGenreDroitAPI(generalReader.convertGenreDroitAPI("1"));

        // Pour tous les autre type de rentes sauf API
        annoncePonctuelle10Eme.setEchelleRente(baseDeCalculReader.readEchelleRente("44"));

        annoncePonctuelle10Eme.setDureeCotManquante48_72(baseDeCalculReader.readDureeCotManquante48_72("12"));
        annoncePonctuelle10Eme.setDureeCotManquante73_78(baseDeCalculReader.readDureeCotManquante73_78("24"));
        annoncePonctuelle10Eme.setRamDeterminant(baseDeCalculReader.readRamDeterminant("29484"));
        annoncePonctuelle10Eme.setAnneeCotClasseAge(baseDeCalculReader.readAnneeCotClasseAge("43"));

        annoncePonctuelle10Eme.setAnneeNiveau(baseDeCalculReader.readAnneeNiveau("41"));

        annoncePonctuelle10Eme.setDureeCoEchelleRenteAv73(
                baseDeCalculReader.readDureeCoEchelleRenteAv73_nombreAnnee("06.11"),
                baseDeCalculReader.readDureeCoEchelleRenteAv73_nombreMois("06.11"));

        annoncePonctuelle10Eme.setDureeCoEchelleRenteDes73(
                baseDeCalculReader.readDureeCoEchelleRenteDes73_nombreAnnee("03.05"),
                baseDeCalculReader.readDureeCoEchelleRenteDes73_nombreMois("03.05"));

        annoncePonctuelle10Eme.setDureeCotPourDetRAM(baseDeCalculReader.readDureeCotPourDetRAM_nombreAnnee("04.13"),
                baseDeCalculReader.readDureeCotPourDetRAM_nombreMois("04.13"));

        // FIXME
        annoncePonctuelle10Eme.setNombreAnneeBTE(baseDeCalculReader.readNombreAnneeBTE_valeurEntiere("11"),
                baseDeCalculReader.readNombreAnneeBTE_valeurDecimal("11"));

        // REANN41
        annoncePonctuelle10Eme
                .setMontantRenteOrdinaireRemplace(renteAccordeeReader.readMontantRenteOrdinaireRemplace("826"));

        // REAAL3B
        annoncePonctuelle10Eme.setRevenuPrisEnCompte(baseDeCalculReader.readRevenuPrisEnCompte("3"));
        annoncePonctuelle10Eme.setIsLimiteRevenu(baseDeCalculReader.readIsLimiteRevenu(false));
        annoncePonctuelle10Eme.setIsMinimumGaranti(baseDeCalculReader.readIsMinimumGaranti(true));
        annoncePonctuelle10Eme.setRamDeterminant(baseDeCalculReader.readRevenuAnnuelMoyen("85120"));
        annoncePonctuelle10Eme.setBteMoyennePrisEnCompte(baseDeCalculReader.readBonificationTacheEducative("20154"));

        // Que pour des rente invalidit?
        annoncePonctuelle10Eme.setAgeDebutInvalidite(false);
        annoncePonctuelle10Eme.setDegreInvalidite(baseDeCalculReader.readDegreInvalidite("70"));

        // Pour toute les rentes
        annoncePonctuelle10Eme.setOfficeAICompetent(baseDeCalculReader.readOfficeAICompetent("22"));
        annoncePonctuelle10Eme.setSurvenanceEvenAssure(baseDeCalculReader.readSurvenanceEvenAssure("10.1999"));

        annoncePonctuelle10Eme.setCodeInfirmite(baseDeCalculReader.readCodeInfirmite("64691"));
        annoncePonctuelle10Eme.setCodeAtteinteFonctionnelle(baseDeCalculReader.readCodeAtteinteFonctionnelle("64691"));

        annoncePonctuelle10Eme.setNouveauNoAssureAyantDroit(generalReader.convertFormatedNss("756.8652.3475.28"));
        // ---------------------------------------------------------------------------//
        RECreationAnnonceService serviceCreationAnnonce = new RECreationAnnonceService();
        REAnnoncePonctuelle9EmeRevision annonceACreer = new REAnnoncePonctuelle9EmeRevision();
        REAnnoncesAPersister annoncesAPersister = null;

        try {
            annoncesAPersister = serviceCreationAnnonce.creerAnnonce(annoncePonctuelle10Eme, false);
        } catch (IllegalArgumentException e) {
            assertTrue(false);
        } catch (RECreationAnnonceFormatterException e) {
            assertTrue(false);
        }

        System.out.println("Les champs ne sont pas mis a blanc selon les directives");
        REAnnonceRentePrinter printer = new REAnnonceRentePrinter();
        printer.print(annoncePonctuelle10Eme, annoncesAPersister);

        try {
            annoncesAPersister = serviceCreationAnnonce.creerAnnonce(annoncePonctuelle10Eme, true);
        } catch (IllegalArgumentException e) {
            assertTrue(false);
        } catch (RECreationAnnonceFormatterException e) {
            assertTrue(false);
        }

        System.out.println("Les champs sont mis a blanc selon les directives");
        printer.print(annoncePonctuelle10Eme, annoncesAPersister);
    }

    /**
     * Retourne une annonce ponctuelle correctement remplie
     *
     * @return une annonce ponctuelle correctement remplie
     */
    private REAnnoncePonctuelle9EmeRevision getAnnoncePonctuelle1() {
        REAnnoncePonctuelle9EmeRevision annonceACreer = new REAnnoncePonctuelle9EmeRevision();

        annonceACreer.setIdRenteAccordee(12l);

        // REAnnonceHeader
        annonceACreer.setNumeroCaisse(150);
        annonceACreer.setNumeroAgence(0);

        // REAAL1A
        annonceACreer.setCantonEtatDomicile(24);
        annonceACreer.setCodeMutation(8);
        annonceACreer.setDebutDroit(new JADate(0, 10, 2012));
        annonceACreer.setEtatCivil(5);
        annonceACreer.setFinDroit(new JADate(0, 11, 2012));
        annonceACreer.setGenrePrestation(50);
        annonceACreer.setIdTiers(16853l);
        annonceACreer.setRefugie(false);
        annonceACreer.setMensualitePrestationsFrancs(738);
        annonceACreer.setMoisRapport(new JADate(01, 01, 2015));
        annonceACreer.setNoAssAyantDroit(new RENSS(684, 8427, 2, false));
        annonceACreer.setNumeroAnnonce(59463l);
        annonceACreer.setPremierNoAssComplementaire(new RENSS(9999, 9999, 99, false));
        annonceACreer.setSecondNoAssComplementaire(null);
        annonceACreer.setReferenceCaisseInterne("LuCiEn");

        // REAAL2A
        annonceACreer.setAgeDebutInvalidite(true);
        annonceACreer.setAnneeCotClasseAge(28);
        annonceACreer.setAnneeNiveau(2012);
        annonceACreer.setCasSpecial1(1);
        annonceACreer.setCasSpecial2(2);
        annonceACreer.setCasSpecial3(3);
        annonceACreer.setCasSpecial4(4);
        annonceACreer.setCasSpecial5(5);
        annonceACreer.setCodeAtteinteFonctionnelle(2);
        annonceACreer.setCodeInfirmite(738);
        annonceACreer.setDateRevocationAjournement(new JADate(0, 9, 2005));
        annonceACreer.setDegreInvalidite(65);
        annonceACreer.setDureeAjournement(1, 8);
        annonceACreer.setDureeCoEchelleRenteAv73(1, 1);
        annonceACreer.setDureeCoEchelleRenteDes73(20, 4);
        annonceACreer.setDureeCotManquante48_72(0);
        annonceACreer.setDureeCotManquante73_78(0);
        annonceACreer.setDureeCotPourDetRAM(22, 4);
        annonceACreer.setEchelleRente(32);
        annonceACreer.setGenreDroitAPI(1);
        annonceACreer.setNombreAnneeBTE(10, 11);
        annonceACreer.setOfficeAICompetent(324);
        annonceACreer.setRamDeterminant(45800);
        annonceACreer.setReduction(33);
        annonceACreer.setSupplementAjournement(76);
        annonceACreer.setSurvenanceEvenAssure(new JADate(0, 10, 2012));

        // REAAL3B
        annonceACreer.setRevenuPrisEnCompte(1);
        annonceACreer.setIsLimiteRevenu(true);
        annonceACreer.setIsMinimumGaranti(false);
        // annonceACreer.setRevenuAnnuelMoyen(45800);
        annonceACreer.setBteMoyennePrisEnCompte(12500);

        // REANN41
        annonceACreer.setNouveauNoAssureAyantDroit(new RENSS(1234, 5678, 90, false));
        annonceACreer.setMontantRenteOrdinaireRemplace(790);

        return annonceACreer;
    }
}
