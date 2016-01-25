package globaz.corvus.annonce;

import globaz.corvus.annonce.domain.REAnnonce1A;
import globaz.corvus.annonce.domain.REAnnonce2A;
import globaz.corvus.annonce.domain.REAnnonce3A;
import globaz.corvus.annonce.domain.REAnnonce3B;
import globaz.corvus.annonce.domain.REEnteteAnnonce;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle10EmeRevision;
import globaz.corvus.annonce.domain.annonce.REAnnoncePonctuelle9EmeRevision;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel3A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel3B;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.globall.util.JADate;
import globaz.prestation.utils.PRStringFormatter;

/**
 * Classe utilisable pour les tests afin d'imprimer les valeurs des données et des annonces générée
 * 
 * @author lga
 * 
 */
public class REAnnonceRentePrinter {

    private static final int nameSize = 30;
    private static final int dbNameSize = 20;
    private static final int inputValueSize = 20;
    private static final int sizeValue01 = 20;
    private static final int sizeValue02 = 20;

    public void print(REAnnoncePonctuelle10EmeRevision annonce, REAnnoncesAPersister annoncesAPersister) {
        REAnnoncesAugmentationModification10Eme annonce01 = (REAnnoncesAugmentationModification10Eme) annoncesAPersister
                .getAnnonce01();
        REAnnoncesAugmentationModification10Eme annonce02 = (REAnnoncesAugmentationModification10Eme) annoncesAPersister
                .getAnnonce02();

        System.out.println("Impression d'une annonce ponctuelle 10ème révision");
        printDotLine();

        // HEADER
        printHeader();
        printSpaceLine();

        // REAHHEA
        printREAHEA(annonce, annonce01, annonce02);
        printSpaceLine();

        // REANHEA
        printREAAL1A(annonce, annonce01, annonce02);
        printSpaceLine();

        // REAAL2A
        printREAAL2A(annonce, annonce01, annonce02);
        printSpaceLine();

        // REAAL3A
        printREAAL3A(annonce, annonce01, annonce02);
        printSpaceLine();

        // REANN44
        printREANN44(annonce, annonce01, annonce02);
        printDotLine();
    }

    public void print(REAnnoncePonctuelle9EmeRevision annonce, REAnnoncesAPersister annoncesAPersister) {
        REAnnoncesAugmentationModification9Eme annonce01 = (REAnnoncesAugmentationModification9Eme) annoncesAPersister
                .getAnnonce01();
        REAnnoncesAugmentationModification9Eme annonce02 = (REAnnoncesAugmentationModification9Eme) annoncesAPersister
                .getAnnonce02();

        System.out.println("Impression d'une annonce ponctuelle 9ème révision");
        printDotLine();

        // HEADER
        printHeader();
        printSpaceLine();

        // REAHHEA
        printREAHEA(annonce, annonce01, annonce02);
        printSpaceLine();

        // REANHEA
        printREAAL1A(annonce, annonce01, annonce02);
        printSpaceLine();

        // REAAL2A
        printREAAL2A(annonce, annonce01, annonce02);
        printSpaceLine();

        // REAAL3A
        printREAAL3B(annonce, annonce01, annonce02);
        printSpaceLine();

        // REANN44
        printREANN41(annonce, annonce01, annonce02);
        printDotLine();
    }

    /**
     * 
     */
    private void printSpaceLine() {
        System.out.println();
    }

    /**
     * 
     */
    private void printDotLine() {
        System.out.println("------------------------------------------------------------");
    }

    /**
     * @param annonce
     * @param annonce01
     * @param annonce02
     */
    private void printREANN41(REAnnoncePonctuelle9EmeRevision annonce,
            REAnnoncesAugmentationModification9Eme annonce01, REAnnoncesAugmentationModification9Eme annonce02) {

        printline("nouveauNoAssureAyantDroit", "REANN41.ZGNNA", annonce.getNouveauNoAssureAyantDroit(),
                annonce01.getNouveauNoAssureAyantDroit(), annonce02.getNouveauNoAssureAyantDroit());

        printline("montantRenteOrdinaireRemplace", "REANN41.ZGMMEN", annonce.getMontantRenteOrdinaireRemplace(),
                annonce01.getMensualiteRenteOrdRemp(), annonce02.getMensualiteRenteOrdRemp());

        // ATTENTION au piège !
        // annonce01.getMensualiteRenteOrdRemp() REANN41.ZGMMEN --> utilisé
        // annonce01.getMntRenteOrdinaireRempl() REAAL3B.ZCMROR --> pas utilisé
    }

    private void printREANN44(REAnnoncePonctuelle10EmeRevision annonce,
            REAnnoncesAugmentationModification10Eme annonce01, REAnnoncesAugmentationModification10Eme annonce02) {

        printline("nouveauNoAssureAyantDroit", "REANN41.ZENNNA", annonce.getNouveauNoAssureAyantDroit(),
                annonce01.getNouveauNoAssureAyantDroit(), annonce02.getNouveauNoAssureAyantDroit());
    }

    private void printREAAL3B(REAnnonce3B annonce, REAnnoncesAbstractLevel3B annonce01,
            REAnnoncesAbstractLevel3B annonce02) {

        printline("revenuPrisEnCompte", "REAAL3B.ZCMREV", annonce.getRevenuPrisEnCompte(),
                annonce01.getRevenuPrisEnCompte(), annonce02.getRevenuPrisEnCompte());
        printline("isLimiteRevenu", "REAAL3B.ZCBLIM", annonce.getIsLimiteRevenu(), annonce01.getIsLimiteRevenu(),
                annonce02.getIsLimiteRevenu());
        printline("isMinimumGaranti", "REAAL3B.ZCBMIN", annonce.getIsMinimumGaranti(), annonce01.getIsMinimumGaranti(),
                annonce02.getIsMinimumGaranti());
        printline("revenuAnnuelMoyenSansBTE", "REAAL3B.ZCNRAM",
                annonce.getRamDeterminant() + ", " + annonce.getBteMoyennePrisEnCompte(),
                annonce01.getRevenuAnnuelMoyenSansBTE(), annonce02.getRevenuAnnuelMoyenSansBTE());
        printline("bteMoyennePrisEnCompte", "REAAL3B.ZCMBTE", annonce.getBteMoyennePrisEnCompte(),
                annonce01.getBteMoyennePrisEnCompte(), annonce02.getBteMoyennePrisEnCompte());

    }

    /**
     * @param annonce
     * @param annonce01
     * @param annonce02
     */
    private void printREAAL3A(REAnnonce3A annonce, REAnnoncesAbstractLevel3A annonce01,
            REAnnoncesAbstractLevel3A annonce02) {
        printline("codeRevenuSplitte", "REAAL3A.YZTCOD", annonce.getCodeRevenuSplitte(),
                annonce01.getCodeRevenuSplitte(), annonce02.getCodeRevenuSplitte());
        printline("dateDebutAnticipation", "REAAL3A.YZDDEB", annonce.getDateDebutAnticipation(),
                annonce01.getDateDebutAnticipation(), annonce02.getDateDebutAnticipation());
        printline("isSurvivant", "REAAL3A.YZBSUR", annonce.isSurvivant(), annonce01.getIsSurvivant(),
                annonce02.getIsSurvivant());
        printline("nbreAnneeAnticipation", "REAAL3A.YZNANT", annonce.getNbreAnneeAnticipation(),
                annonce01.getNbreAnneeAnticipation(), annonce02.getNbreAnneeAnticipation());
        printline("nbreAnneeBonifTrans", "REAAL3A.YZNBON", annonce.getNbreAnneeBonifTrans_nombreAnnee() + ", "
                + annonce.getNbreAnneeBonifTrans_isDemiAnnee(), annonce01.getNbreAnneeBonifTrans(),
                annonce02.getNbreAnneeBonifTrans());

        printline("nbreAnneeBTA", "REAAL3A.YZNBTA",
                annonce.getBTA_valeurEntiere() + ", " + annonce.getBTA_valeurDecimal(), annonce01.getNbreAnneeBTA(),
                annonce02.getNbreAnneeBTA());

        printline("reductionAnticipation", "REAAL3A.YZNRED", annonce.getReductionAnticipation(),
                annonce01.getReductionAnticipation(), annonce02.getReductionAnticipation());
    }

    /**
     * @param annonce
     * @param annonce01
     * @param annonce02
     */
    private void printREAAL2A(REAnnonce2A annonce, REAnnoncesAbstractLevel2A annonce01,
            REAnnoncesAbstractLevel2A annonce02) {
        printline("ageDebutInvalidite", "REAAL2A.YYNAGE", annonce.getAgeDebutInvalidite(),
                annonce01.getAgeDebutInvalidite(), annonce02.getAgeDebutInvalidite());
        printline("anneeCotClasseAge", "REAAL2A.YYDACC", annonce.getAnneeCotClasseAge(),
                annonce01.getAnneeCotClasseAge(), annonce02.getAnneeCotClasseAge());
        printline("anneeNiveau", "REAAL2A.YYDANI", annonce.getAnneeNiveau(), annonce01.getAnneeNiveau(),
                annonce02.getAnneeNiveau());
        printline("casSpecial1", "REAAL2A.YYLCS1", annonce.getCasSpecial1(), annonce01.getCasSpecial1(),
                annonce02.getCasSpecial1());
        printline("casSpecial2", "REAAL2A.YYLCS2", annonce.getCasSpecial2(), annonce01.getCasSpecial2(),
                annonce02.getCasSpecial2());
        printline("casSpecial3", "REAAL2A.YYLCS3", annonce.getCasSpecial3(), annonce01.getCasSpecial3(),
                annonce02.getCasSpecial3());
        printline("casSpecial4", "REAAL2A.YYLCS4", annonce.getCasSpecial4(), annonce01.getCasSpecial4(),
                annonce02.getCasSpecial4());
        printline("casSpecial5", "REAAL2A.YYLCS5", annonce.getCasSpecial5(), annonce01.getCasSpecial5(),
                annonce02.getCasSpecial5());
        printline("codeInfirmite", "REAAL2A.YYNCOI",
                annonce.getCodeInfirmite() + ", " + annonce.getCodeAtteinteFonctionnelle(),
                annonce01.getCodeInfirmite(), annonce02.getCodeInfirmite());
        printline("dateRevocationAjournement", "REAAL2A.YYDREV", annonce.getDateRevocationAjournement(),
                annonce01.getDateRevocationAjournement(), annonce02.getDateRevocationAjournement());
        printline("degreInvalidite", "REAAL2A.YYNDIN", annonce.getDegreInvalidite(), annonce01.getDegreInvalidite(),
                annonce02.getDegreInvalidite());
        printline("dureeAjournement", "REAAL2A.YYNDUR",
                annonce.getDureeAjournementValeurEntiere() + ", " + annonce.getDureeAjournementValeurDecimal(),
                annonce01.getDureeAjournement(), annonce02.getDureeAjournement());
        printline("dureeCoEchelleRenteAv73", "REAAL2A.YYDCEC", annonce.getDureeCoEchelleRenteAv73_nombreAnnee() + ", "
                + annonce.getDureeCoEchelleRenteAv73_nombreMois(), annonce01.getDureeCoEchelleRenteAv73(),
                annonce02.getDureeCoEchelleRenteAv73());
        printline("dureeCoEchelleRenteDes73", "REAAL2A.YYDECH", annonce.getDureeCoEchelleRenteDes73_nombreAnnee()
                + ", " + annonce.getDureeCoEchelleRenteDes73_nombreMois(), annonce01.getDureeCoEchelleRenteDes73(),
                annonce02.getDureeCoEchelleRenteDes73());
        printline("dureeCotManquante48_72", "REAAL2A.YYDCM1", annonce.getDureeCotManquante48_72(),
                annonce01.getDureeCotManquante48_72(), annonce02.getDureeCotManquante48_72());
        printline("dureeCotManquante73_78", "REAAL2A.YYDCM2", annonce.getDureeCotManquante73_78(),
                annonce01.getDureeCotManquante73_78(), annonce02.getDureeCotManquante73_78());
        printline("dureeCotPourDetRAM", "REAAL2A.YYNDCO",
                annonce.getDureeCotPourDetRAM_nombreAnnee() + ", " + annonce.getDureeCotPourDetRAM_nombreMois(),
                annonce01.getDureeCotPourDetRAM(), annonce02.getDureeCotPourDetRAM());
        printline("echelleRente", "REAAL2A.YYLECR", annonce.getEchelleRente(), annonce01.getEchelleRente(),
                annonce02.getEchelleRente());
        printline("genreDroitAPI", "REAAL2A.YYTGEN", annonce.getGenreDroitAPI(), annonce01.getGenreDroitAPI(),
                annonce02.getGenreDroitAPI());
        printline("nombreAnneeBTE", "REAAL2A.YYNANN",
                annonce.getNombreAnneeBTE_valeurEntiere() + ", " + annonce.getNombreAnneeBTE_valeurDecimal(),
                annonce01.getNombreAnneeBTE(), annonce02.getNombreAnneeBTE());
        printline("officeAICompetent", "REAAL2A.YYLOAI", annonce.getOfficeAICompetent(),
                annonce01.getOfficeAICompetent(), annonce02.getOfficeAICompetent());
        printline("ramDeterminant", "REAAL2A.YYMRAM", annonce.getRamDeterminant(), annonce01.getRamDeterminant(),
                annonce02.getRamDeterminant());
        printline("reduction", "REAAL2A.YYLRED", annonce.getReduction(), annonce01.getReduction(),
                annonce02.getReduction());
        printline("supplementAjournement", "REAAL2A.YYNSUP", annonce.getSupplementAjournement(),
                annonce01.getSupplementAjournement(), annonce02.getSupplementAjournement());
        printline("survenanceEvenAssure", "REAAL2A.YYNSUR", annonce.getSurvenanceEvenAssure(),
                annonce01.getSurvenanceEvenAssure(), annonce02.getSurvenanceEvenAssure());
    }

    /**
     * @param annonce
     * @param annonce01
     * @param annonce02
     */
    private void printREAAL1A(REAnnonce1A annonce, REAnnoncesAbstractLevel1A annonce01,
            REAnnoncesAbstractLevel1A annonce02) {

        // REAAL1A

        printline("numeroAnnonce", "REAAL1A.YXNNOA", annonce.getNumeroAnnonce(), annonce01.getNumeroAnnonce(),
                annonce02.getNumeroAnnonce());

        printline("cantonEtatDomicile", "REAAL1A.YXLCAN", annonce.getCantonEtatDomicile(),
                annonce01.getCantonEtatDomicile(), annonce02.getCantonEtatDomicile());
        printline("codeMutation", "REAAL1A.YXLCOM", annonce.getCodeMutation(), annonce01.getCodeMutation(),
                annonce02.getCodeMutation());
        printline("debutDroit", "REAAL1A.YXDDEB", annonce.getDebutDroit(), annonce01.getDebutDroit(),
                annonce02.getDebutDroit());
        printline("etatCivil", "REAAL1A.YXLETC", annonce.getEtatCivil(), annonce01.getEtatCivil(),
                annonce02.getEtatCivil());
        printline("finDroit", "REAAL1A.YXDFIN", annonce.getFinDroit(), annonce01.getFinDroit(), annonce02.getFinDroit());
        printline("genrePrestation", "REAAL1A.YXLGEN", annonce.getGenrePrestation(), annonce01.getGenrePrestation(),
                annonce02.getGenrePrestation());
        printline("idTiers", "REAAL1A.YXITIE", annonce.getIdTiers(), annonce01.getIdTiers(), annonce02.getIdTiers());
        printline("isRefugie", "REAAL1A.YXBREF", annonce.isRefugie(), annonce01.getIsRefugie(),
                annonce02.getIsRefugie());

        printline("mensualitePrestationsFrancs", "REAAL1A.YXMMEN", annonce.getMensualitePrestationsFrancs(),
                annonce01.getMensualitePrestationsFrancs(), annonce02.getMensualitePrestationsFrancs());
        printline("moisRapport", "REAAL1A.YXDMRA", annonce.getMoisRapport(), annonce01.getMoisRapport(),
                annonce02.getMoisRapport());
        printline("noAssAyantDroit", "REAAL1A.YXDMRA", annonce.getNoAssAyantDroit(), annonce01.getNoAssAyantDroit(),
                annonce02.getNoAssAyantDroit());
        printline("premierNoAssComplementaire", "REAAL1A.YXNPNA", annonce.getPremierNoAssComplementaire(),
                annonce01.getPremierNoAssComplementaire(), annonce02.getPremierNoAssComplementaire());
        printline("secondNoAssComplementaire", "REAAL1A.YXNDNA", annonce.getSecondNoAssComplementaire(),
                annonce01.getSecondNoAssComplementaire(), annonce02.getSecondNoAssComplementaire());
        printline("referenceCaisseInterne", "REAAL1A.YXLREI", annonce.getPrefixPourReferenceInterneCaisseProvider()
                .getPrefixPourReferenceInterneCaisse() + ", " + annonce.getUtilisateurPourReferenceCaisseInterne(),
                annonce01.getReferenceCaisseInterne(), annonce02.getReferenceCaisseInterne());
    }

    /**
     * @param annonce
     * @param annonce01
     * @param annonce02
     */
    private void printREAHEA(REEnteteAnnonce annonce, REAnnonceHeader annonce01, REAnnonceHeader annonce02) {
        printline("codeApplication", "REANHEA.ZAACOA", "--", annonce01.getCodeApplication(),
                annonce02.getCodeApplication());
        printline("codeEnregistrement01", "REANHEA.ZAACO1", "--", annonce01.getCodeEnregistrement01(),
                annonce02.getCodeEnregistrement01());
        printline("numeroCaisse", "REANHEA.ZAANOC", annonce.getNumeroCaisse(), annonce01.getNumeroCaisse(),
                annonce02.getNumeroCaisse());
        printline("numeroAgence", "REANHEA.ZAANOA", annonce.getNumeroAgence(), annonce01.getNumeroAgence(),
                annonce02.getNumeroAgence());
    }

    /**
     * 
     */
    private void printHeader() {
        printline("NOM CHAMP", "CHAMP DB", "VALEUR ENTREE", "VALEUR 01", "VALEUR 02");
    }

    private void printline(String nomDuChamp, String nomChampDB, Long valeurDonnee, String valeurAnn01,
            String valeurAnn02) {
        printline(nomDuChamp, nomChampDB, String.valueOf(valeurDonnee), valeurAnn01, valeurAnn02);
    }

    private void printline(String nomDuChamp, String nomChampDB, JADate valeurDonnee, String valeurAnn01,
            String valeurAnn02) {
        String value = valeurDonnee == null ? "null" : valeurDonnee.toStr(".");
        printline(nomDuChamp, nomChampDB, value, valeurAnn01, valeurAnn02);
    }

    private void printline(String nomDuChamp, String nomChampDB, RENSS valeurDonnee, String valeurAnn01,
            String valeurAnn02) {
        String value = valeurDonnee == null ? "null" : valeurDonnee.getFormatedNSS();
        printline(nomDuChamp, nomChampDB, value, valeurAnn01, valeurAnn02);
    }

    private void printline(String nomDuChamp, String nomChampDB, Integer valeurDonnee, String valeurAnn01,
            String valeurAnn02) {
        printline(nomDuChamp, nomChampDB, String.valueOf(valeurDonnee), valeurAnn01, valeurAnn02);
    }

    private void printline(String nomDuChamp, String nomChampDB, Boolean valeurDonnee, String valeurAnn01,
            String valeurAnn02) {
        printline(nomDuChamp, nomChampDB, String.valueOf(valeurDonnee), valeurAnn01, valeurAnn02);
    }

    private void printline(String nomDuChamp, String nomChampDB, String valeurDonnee, String valeurAnn01,
            String valeurAnn02) {
        print(ir(nomDuChamp, nameSize, " ") + il(nomChampDB, dbNameSize, " ") + il(valeurDonnee, inputValueSize, " ")
                + il(valeurAnn01, sizeValue01, " ") + il(valeurAnn02, sizeValue02, " "));
    }

    private String ir(String value, int indetValue, String spacer) {
        return PRStringFormatter.indentRight(value, indetValue, spacer);
    }

    private String il(String value, int indetValue, String spacer) {
        return PRStringFormatter.indentLeft(value, indetValue, spacer);
    }

    private void print(String value) {
        System.out.println(value);
    }

}
