package globaz.cygnus.process.financementSoin;

import java.util.ArrayList;
import java.util.List;

public class TableauxSimulationFichierExcelTests {

    /**
     * une ligne standard sans erreur
     * 
     * @return
     */
    public static String[] generateTableau() {
        String tab[] = new String[10];
        tab[0] = "204";
        tab[1] = "Résidence Les Cerisier";
        tab[2] = "756.3157.2415.19";
        tab[3] = "ALTERMATH-WILLEMIN Denise";
        tab[4] = "21.60";
        tab[5] = "01.06.2012";
        tab[6] = "30.06.2012";
        tab[7] = "30";
        tab[8] = "648.00";
        tab[9] = "06.07.2012";
        return tab;

    }

    /**
     * Une ligne avec erreur métier (frais journalier supérieur à 21.60)
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecDepassementFraisJournalier() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[4] = "21.65";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec erreur de format sur la date de début
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurDateDebut() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[5] = "01.06.122";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;

    }

    /**
     * Une ligne avec erreur de format de date de décompte
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurDateDecompte() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[9] = "01.06.122";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;

    }

    /**
     * Une ligne avec erreur de format sur dateFin
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurDateFin() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[6] = "01.06.122";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;

    }

    /**
     * Une ligne avec erreur de format sur montantTotal
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurFormatMontantTotal() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[8] = "29f.80";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec erreur de format sur nbJours
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurFormatNbJours() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[7] = "3b";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec erreur de format sur nss
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurFormatNss() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[2] = "12323412341";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec erreur format sur fraisJournalier
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecErreurFraisJournalier() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[4] = "21.4a";

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec dateDebut null
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullDateDebut() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[5] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;

    }

    /**
     * Une ligne avec dateDecompte null
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullDateDecompte() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[9] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;

    }

    /**
     * Une ligne avec dateFin null
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullDateFin() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[6] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;

    }

    /**
     * Une ligne avec nss null
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullFormatNss() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[2] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec fraisJournalier
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullFraisJournalier() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[4] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec montantTotal null
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullMontantTotal() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[8] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Une ligne avec nbJours null
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeAvecNullNbJours() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne nulle et vide
        String tab[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab[7] = null;

        listeCasTest.add(new RFLigneFichierExcel(tab, "1"));
        return listeCasTest;
    }

    /**
     * Plusieurs lignes avec erreurs multiples
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeMultipleAvecErreurs() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne 1 : format nss faux
        String tab0[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab0[2] = "756.1234.000117";
        tab0[3] = "BERDAT Marthe";
        listeCasTest.add(new RFLigneFichierExcel(tab0, "1"));

        // ligne 2 : montant journalier dépassé
        String tab1[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab1[2] = "756.1234.0002.27";
        tab1[3] = "CHENAL Olivier";
        tab1[4] = "21.65";
        listeCasTest.add(new RFLigneFichierExcel(tab1, "2"));

        // ligne 3 : format montant journalier faux
        String tab3[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab3[2] = "756.1234.0003.37";
        tab3[3] = "BRANDT Raphael";
        tab3[4] = "21.60a";
        listeCasTest.add(new RFLigneFichierExcel(tab3, "3"));

        // ligne 4 : montant journalier NULL
        String tab4[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab4[2] = "756.7678.0004.47";
        tab4[3] = "BUNZ Fabienne";
        tab4[4] = null;
        listeCasTest.add(new RFLigneFichierExcel(tab4, "4"));

        // ligne 5 : dateDebut format faux
        String tab5[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab5[2] = "756.7678.0005.57";
        tab5[3] = "BILAT Ruth";
        tab5[5] = "12.213";
        listeCasTest.add(new RFLigneFichierExcel(tab5, "5"));

        // ligne 6 : dateDebut NULL
        String tab6[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab6[2] = "756.7678.0006.67";
        tab6[3] = "FAIVET Pascal";
        tab6[5] = null;
        listeCasTest.add(new RFLigneFichierExcel(tab6, "6"));

        // ligne 7 date dateDébut après dateFin
        String tab7[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab7[2] = "756.7678.0007.77";
        tab7[3] = "CORVET Raphael";
        tab7[5] = "01.07.2013";
        listeCasTest.add(new RFLigneFichierExcel(tab7, "7"));

        // ligne 8 : dateFin fausse
        String tab8[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab8[2] = "756.7678.0008.87";
        tab8[3] = "CIFEL Raphael";
        tab8[6] = "12.213";
        listeCasTest.add(new RFLigneFichierExcel(tab8, "8"));

        // ligne 9 : dateFin NULL
        String tab9[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab9[2] = "756.7678.0009.97";
        tab9[3] = "CURCHOD Lionel";
        tab9[6] = null;
        listeCasTest.add(new RFLigneFichierExcel(tab9, "9"));

        // ligne 10 : dateFin avant dateDebut
        String tab10[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab10[2] = "756.7678.0010.07";
        tab10[3] = "CHEVRE Laurent";
        tab10[6] = "10.01.2011";
        listeCasTest.add(new RFLigneFichierExcel(tab10, "10"));

        // ligne 11 : nb jours faux
        String tab11[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab11[2] = "756.7678.0011.17";
        tab11[3] = "CHEVRE Laurent";
        tab11[7] = "2";
        listeCasTest.add(new RFLigneFichierExcel(tab10, "11"));

        // ligne 12 : nb jours NULL
        String tab12[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab12[2] = "756.7678.0012.27";
        tab12[3] = "CHEVRE Laurent";
        tab12[7] = null;
        listeCasTest.add(new RFLigneFichierExcel(tab12, "12"));

        // ligne 13 : montantTotal faux
        String tab13[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab13[2] = "756.7678.0013.37";
        tab13[3] = "CATTIN Simone";
        tab13[8] = "101.10";
        listeCasTest.add(new RFLigneFichierExcel(tab13, "13"));

        // ligne 14 : montantTotal NULL
        String tab14[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab14[2] = "756.7678.0014.47";
        tab14[3] = "CATTIN Simone";
        tab14[8] = null;
        listeCasTest.add(new RFLigneFichierExcel(tab14, "14"));

        // ligne 15 : dateDecompte mauvais format
        String tab15[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab15[2] = "756.7678.0015.57";
        tab15[3] = "CATTIN Simone";
        tab15[9] = "06.712";
        listeCasTest.add(new RFLigneFichierExcel(tab15, "15"));

        // ligne 16 : dateDecompte NULL
        String tab16[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab16[2] = "756.7678.0016.67";
        tab16[3] = "CATTIN Simone";
        tab16[9] = null;
        listeCasTest.add(new RFLigneFichierExcel(tab16, ""));

        return listeCasTest;

    }

    /**
     * Plusieurs lignes avec aucune erreurs
     * 
     * @return
     */
    public static List<RFLigneFichierExcel> getListeMultipleSansErreurs() {

        List<RFLigneFichierExcel> listeCasTest = new ArrayList<RFLigneFichierExcel>();

        // ligne 1
        String tab0[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab0[2] = "756.1234.5678.07";
        tab0[3] = "BERDAT Marthe";
        listeCasTest.add(new RFLigneFichierExcel(tab0, ""));

        // ligne 2
        String tab1[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab1[2] = "756.1234.0001.17";
        tab1[3] = "CHENAL Olivier";
        listeCasTest.add(new RFLigneFichierExcel(tab1, ""));

        // ligne 3
        String tab2[] = TableauxSimulationFichierExcelTests.generateTableau();
        tab2[2] = "756.7678.4586.27";
        tab2[3] = "BUNZ Fabienne";
        listeCasTest.add(new RFLigneFichierExcel(tab2, ""));

        return listeCasTest;
    }
}
