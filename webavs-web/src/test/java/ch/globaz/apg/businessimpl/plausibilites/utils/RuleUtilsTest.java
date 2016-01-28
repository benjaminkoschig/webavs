package ch.globaz.apg.businessimpl.plausibilites.utils;

import globaz.apg.enums.APTypeProtectionCivile;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RuleUtilsTest {

    private Map<String, APTypeProtectionCivile> valeursSelonDocumentOFAS;

    private void construireMapSelonDocument() {
        valeursSelonDocumentOFAS = new HashMap<String, APTypeProtectionCivile>();

        // Zürich - global
        // 1.xxxx.1
        valeursSelonDocumentOFAS.put("100001", APTypeProtectionCivile.CoursDeRepetition);
        // 1.99.1
        valeursSelonDocumentOFAS.put("1991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 1.99.2

        // Zürich - Andelfingen
        valeursSelonDocumentOFAS.put("1992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);
        // 1.11.1
        valeursSelonDocumentOFAS.put("1111", APTypeProtectionCivile.InstructionDeBase);
        // 1.11.2
        valeursSelonDocumentOFAS.put("1112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Zürich - Winterthur
        // 1.12.1
        valeursSelonDocumentOFAS.put("1121", APTypeProtectionCivile.InstructionDeBase);
        // 1.12.2
        valeursSelonDocumentOFAS.put("1122",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Zürich - Zürich
        // 1.13.1
        valeursSelonDocumentOFAS.put("1131", APTypeProtectionCivile.InstructionDeBase);
        // 1.13.2
        valeursSelonDocumentOFAS.put("1132",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Bern - global
        // 2.xxxx.1
        valeursSelonDocumentOFAS.put("200001", APTypeProtectionCivile.CoursDeRepetition);
        // 2.99.1
        valeursSelonDocumentOFAS.put("2991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 2.99.2
        valeursSelonDocumentOFAS.put("2992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Bern - Aawangen
        // 2.11.1
        valeursSelonDocumentOFAS.put("2111", APTypeProtectionCivile.InstructionDeBase);
        // 2.11.2
        valeursSelonDocumentOFAS.put("2112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Bern - Büren a.Aare
        // 2.12.1
        valeursSelonDocumentOFAS.put("2121", APTypeProtectionCivile.InstructionDeBase);
        // 2.12.2
        valeursSelonDocumentOFAS.put("2122",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Bern - Köniz
        // 2.13.1
        valeursSelonDocumentOFAS.put("2131", APTypeProtectionCivile.InstructionDeBase);
        // 2.13.2
        valeursSelonDocumentOFAS.put("2132",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Bern - Ostermundigen
        // 2.14.1
        valeursSelonDocumentOFAS.put("2141", APTypeProtectionCivile.InstructionDeBase);
        // 2.14.2
        valeursSelonDocumentOFAS.put("2142",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Bern - Spiez
        // 2.15.1
        valeursSelonDocumentOFAS.put("2151", APTypeProtectionCivile.InstructionDeBase);
        // 2.15.2
        valeursSelonDocumentOFAS.put("2152",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Bern - Tramelan
        // 2.16.1
        valeursSelonDocumentOFAS.put("2161", APTypeProtectionCivile.InstructionDeBase);
        // 2.16.2
        valeursSelonDocumentOFAS.put("2162",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Luzern - global
        // 3.xxxx.1
        valeursSelonDocumentOFAS.put("300001", APTypeProtectionCivile.CoursDeRepetition);
        // 3.99.1
        valeursSelonDocumentOFAS.put("3991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 3.99.2
        valeursSelonDocumentOFAS.put("3992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Luzern - Sempach
        // 3.11.1
        valeursSelonDocumentOFAS.put("3111", APTypeProtectionCivile.InstructionDeBase);
        // 3.11.2
        valeursSelonDocumentOFAS.put("3112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Uri - global
        // 4.xxxx.1
        valeursSelonDocumentOFAS.put("400002", APTypeProtectionCivile.CoursDeRepetition);
        // 4.99.1
        valeursSelonDocumentOFAS.put("4991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 4.99.2
        valeursSelonDocumentOFAS.put("4992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Uri - Erstfeld
        // 4.11.1
        valeursSelonDocumentOFAS.put("4111", APTypeProtectionCivile.InstructionDeBase);
        // 4.11.2
        valeursSelonDocumentOFAS.put("4112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Schwyz - global
        // 5.xxxx.1
        valeursSelonDocumentOFAS.put("500001", APTypeProtectionCivile.CoursDeRepetition);
        // 5.99.1
        valeursSelonDocumentOFAS.put("5991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 5.99.2
        valeursSelonDocumentOFAS.put("5992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Schwyz - Schwyz
        // 5.11.1
        valeursSelonDocumentOFAS.put("5111", APTypeProtectionCivile.InstructionDeBase);
        // 5.11.2
        valeursSelonDocumentOFAS.put("5112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Obwald - global
        // 6.11.1
        valeursSelonDocumentOFAS.put("6111", APTypeProtectionCivile.InstructionDeBase);
        // 6.11.2
        valeursSelonDocumentOFAS.put("6112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);
        // 6.xxxx.1
        valeursSelonDocumentOFAS.put("600001", APTypeProtectionCivile.CoursDeRepetition);
        // 6.99.1
        valeursSelonDocumentOFAS.put("6991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 6.99.2
        valeursSelonDocumentOFAS.put("6992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Nidwald - global
        // 7.xxxx.1
        valeursSelonDocumentOFAS.put("700001", APTypeProtectionCivile.CoursDeRepetition);
        // 7.99.1
        valeursSelonDocumentOFAS.put("7991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 7.99.2
        valeursSelonDocumentOFAS.put("7992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Nidwald - Wil-Stans
        // 7.11.1
        valeursSelonDocumentOFAS.put("7111", APTypeProtectionCivile.InstructionDeBase);
        // 7.11.2
        valeursSelonDocumentOFAS.put("7112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Glaris - global
        // 8.xxxx.1
        valeursSelonDocumentOFAS.put("800001", APTypeProtectionCivile.CoursDeRepetition);
        // 8.99.1
        valeursSelonDocumentOFAS.put("8991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 8.99.2
        valeursSelonDocumentOFAS.put("8992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Glaris - Glarus
        // 8.11.1
        valeursSelonDocumentOFAS.put("8111", APTypeProtectionCivile.InstructionDeBase);
        // 8.11.2
        valeursSelonDocumentOFAS.put("8112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Zug - global
        // 9.xxxx.1
        valeursSelonDocumentOFAS.put("900001", APTypeProtectionCivile.CoursDeRepetition);
        // 9.99.1
        valeursSelonDocumentOFAS.put("9991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 9.99.2
        valeursSelonDocumentOFAS.put("9992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Zug - Cham
        // 9.11.1
        valeursSelonDocumentOFAS.put("9111", APTypeProtectionCivile.InstructionDeBase);
        // 9.11.2
        valeursSelonDocumentOFAS.put("9112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Fribourg - global
        // 10.xxxx.1
        valeursSelonDocumentOFAS.put("1000001", APTypeProtectionCivile.CoursDeRepetition);
        // 10.99.1
        valeursSelonDocumentOFAS.put("10991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 10.99.2
        valeursSelonDocumentOFAS.put("10992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Fribourg - Sugiez
        // 10.11.1
        valeursSelonDocumentOFAS.put("10111", APTypeProtectionCivile.InstructionDeBase);
        // 10.11.2
        valeursSelonDocumentOFAS.put("10112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Solothurn - global
        // 11.xxxx.1
        valeursSelonDocumentOFAS.put("1100001", APTypeProtectionCivile.CoursDeRepetition);
        // 11.99.1
        valeursSelonDocumentOFAS.put("11991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 11.99.2
        valeursSelonDocumentOFAS.put("11992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Solothurn - ZIKO Balsthal
        // 11.11.1
        valeursSelonDocumentOFAS.put("11111", APTypeProtectionCivile.InstructionDeBase);
        // 11.11.2
        valeursSelonDocumentOFAS.put("11112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Basel Stadt - global
        // 12.xxxx.1
        valeursSelonDocumentOFAS.put("1200001", APTypeProtectionCivile.CoursDeRepetition);
        // 12.99.1
        valeursSelonDocumentOFAS.put("12991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 12.99.2
        valeursSelonDocumentOFAS.put("12992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Basel Stadt - Basel
        // 12.11.1
        valeursSelonDocumentOFAS.put("12111", APTypeProtectionCivile.InstructionDeBase);
        // 12.11.2
        valeursSelonDocumentOFAS.put("12112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Basel Land - global
        // 13.xxxx.1
        valeursSelonDocumentOFAS.put("1300001", APTypeProtectionCivile.CoursDeRepetition);
        // 13.99.1
        valeursSelonDocumentOFAS.put("13991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 13.99.2
        valeursSelonDocumentOFAS.put("13992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Basel Land - Langenbruck
        // 13.11.1
        valeursSelonDocumentOFAS.put("13111", APTypeProtectionCivile.InstructionDeBase);
        // 13.11.2
        valeursSelonDocumentOFAS.put("13112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Schaffhausen - global
        // 14.xxxx.1
        valeursSelonDocumentOFAS.put("1400001", APTypeProtectionCivile.CoursDeRepetition);
        // 14.99.1
        valeursSelonDocumentOFAS.put("14991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 14.99.2
        valeursSelonDocumentOFAS.put("14992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Schaffhausen - Schlitheim
        // 14.11.1
        valeursSelonDocumentOFAS.put("14111", APTypeProtectionCivile.InstructionDeBase);
        // 14.11.2
        valeursSelonDocumentOFAS.put("14112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Appenzell Ausserrhoden - global
        // 15.xxxx.1
        valeursSelonDocumentOFAS.put("1500001", APTypeProtectionCivile.CoursDeRepetition);
        // 15.99.1
        valeursSelonDocumentOFAS.put("15991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 15.99.2
        valeursSelonDocumentOFAS.put("15992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Appenzell Ausserrhoden - Teufen
        // 15.11.1
        valeursSelonDocumentOFAS.put("15111", APTypeProtectionCivile.InstructionDeBase);
        // 15.11.2
        valeursSelonDocumentOFAS.put("15112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Appenzell Innerrhoden
        // 16.11.1
        valeursSelonDocumentOFAS.put("16111", APTypeProtectionCivile.InstructionDeBase);
        // 16.11.2
        valeursSelonDocumentOFAS.put("16112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);
        // 16.xxxx.1
        valeursSelonDocumentOFAS.put("1600001", APTypeProtectionCivile.CoursDeRepetition);
        // 16.99.1
        valeursSelonDocumentOFAS.put("16991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 16.99.2
        valeursSelonDocumentOFAS.put("16992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // St. Gallen - global
        // 17.xxxx.1
        valeursSelonDocumentOFAS.put("170001", APTypeProtectionCivile.CoursDeRepetition);
        // 17.99.1
        valeursSelonDocumentOFAS.put("17991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 17.99.2
        valeursSelonDocumentOFAS.put("17992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // St. Gallen - Bütschwil
        // 17.11.1
        valeursSelonDocumentOFAS.put("17111", APTypeProtectionCivile.InstructionDeBase);
        // 17.11.2
        valeursSelonDocumentOFAS.put("17112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Graubünden - global
        // 18.xxxx.1
        valeursSelonDocumentOFAS.put("1800001", APTypeProtectionCivile.CoursDeRepetition);
        // 18.99.1
        valeursSelonDocumentOFAS.put("18991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 18.99.2
        valeursSelonDocumentOFAS.put("18992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Graubünden - Chur
        // 18.11.1
        valeursSelonDocumentOFAS.put("18111", APTypeProtectionCivile.InstructionDeBase);
        // 18.11.2
        valeursSelonDocumentOFAS.put("18112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Aargau - global
        // 19.xxxx.1
        valeursSelonDocumentOFAS.put("1900001", APTypeProtectionCivile.CoursDeRepetition);
        // 19.99.1
        valeursSelonDocumentOFAS.put("19991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 19.99.2
        valeursSelonDocumentOFAS.put("19992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Aargau - Eiken
        // 19.11.1
        valeursSelonDocumentOFAS.put("19111", APTypeProtectionCivile.InstructionDeBase);
        // 19.11.2
        valeursSelonDocumentOFAS.put("19112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Thurgau - global
        // 20.xxxx.1
        valeursSelonDocumentOFAS.put("2000001", APTypeProtectionCivile.CoursDeRepetition);
        // 20.99.1
        valeursSelonDocumentOFAS.put("20991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 20.99.2
        valeursSelonDocumentOFAS.put("20992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Thurgau - Frauenfeld
        // 20.11.1
        valeursSelonDocumentOFAS.put("20111", APTypeProtectionCivile.InstructionDeBase);
        // 20.11.2
        valeursSelonDocumentOFAS.put("20112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Ticino - global
        // 21.xxxx.1
        valeursSelonDocumentOFAS.put("2100001", APTypeProtectionCivile.CoursDeRepetition);
        // 21.99.1
        valeursSelonDocumentOFAS.put("21991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 21.99.2
        valeursSelonDocumentOFAS.put("21992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Ticino - Rivera
        // 21.11.1
        valeursSelonDocumentOFAS.put("21111", APTypeProtectionCivile.InstructionDeBase);
        // 21.11.2
        valeursSelonDocumentOFAS.put("21112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Vaud - global
        // 22.xxxx.1
        valeursSelonDocumentOFAS.put("2200001", APTypeProtectionCivile.CoursDeRepetition);
        // 22.99.1
        valeursSelonDocumentOFAS.put("22991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 22.99.2
        valeursSelonDocumentOFAS.put("22992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Vaud - Gollion
        // 22.11.1
        valeursSelonDocumentOFAS.put("22111", APTypeProtectionCivile.InstructionDeBase);
        // 22.11.2
        valeursSelonDocumentOFAS.put("22112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Valais - global
        // 23.xxxx.1
        valeursSelonDocumentOFAS.put("2300001", APTypeProtectionCivile.CoursDeRepetition);
        // 23.99.1
        valeursSelonDocumentOFAS.put("23991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 23.99.2
        valeursSelonDocumentOFAS.put("23992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Valais - Grône
        // 23.11.1
        valeursSelonDocumentOFAS.put("23111", APTypeProtectionCivile.InstructionDeBase);
        // 23.11.2
        valeursSelonDocumentOFAS.put("23112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Neuchâtel - global
        // 24.xxxx.1
        valeursSelonDocumentOFAS.put("2400001", APTypeProtectionCivile.CoursDeRepetition);
        // 24.99.1
        valeursSelonDocumentOFAS.put("24991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 24.99.2
        valeursSelonDocumentOFAS.put("24992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Neuchâtel - Couvet
        // 24.11.1
        valeursSelonDocumentOFAS.put("24111", APTypeProtectionCivile.InstructionDeBase);
        // 24.11.2
        valeursSelonDocumentOFAS.put("24112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Genève - global
        // 25.xxxx.1
        valeursSelonDocumentOFAS.put("2500001", APTypeProtectionCivile.CoursDeRepetition);
        // 25.99.1
        valeursSelonDocumentOFAS.put("25991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 25.99.2
        valeursSelonDocumentOFAS.put("25992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Genève - Bernex
        // 25.11.1
        valeursSelonDocumentOFAS.put("25111", APTypeProtectionCivile.InstructionDeBase);
        // 25.11.2
        valeursSelonDocumentOFAS.put("25112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Genève - Versoix
        // 25.12.1
        valeursSelonDocumentOFAS.put("25121", APTypeProtectionCivile.InstructionDeBase);
        // 25.12.2
        valeursSelonDocumentOFAS.put("25122",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Jura - global
        // 26.xxxx.1
        valeursSelonDocumentOFAS.put("2600001", APTypeProtectionCivile.CoursDeRepetition);
        // 26.99.1
        valeursSelonDocumentOFAS.put("26991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 26.99.2
        valeursSelonDocumentOFAS.put("26992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);

        // Jura - Tous
        // 26.11.1
        valeursSelonDocumentOFAS.put("26111", APTypeProtectionCivile.InstructionDeBase);
        // 26.11.2
        valeursSelonDocumentOFAS.put("26112",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);

        // Confédération
        // 26.00.1
        valeursSelonDocumentOFAS.put("26001",
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire);
        // 26.00.2
        valeursSelonDocumentOFAS.put("26002", APTypeProtectionCivile.ServiceAccompliDansAdministrationProtectionCivile);
        // 26.99.1
        valeursSelonDocumentOFAS.put("26991", APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat);
        // 26.99.2
        valeursSelonDocumentOFAS.put("26992", APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite);
    }

    @Before
    public void setUp() throws Exception {
        construireMapSelonDocument();
    }

    @Test
    public void testGetTypePCenFonctionDuRefNumber() {
        for (Entry<String, APTypeProtectionCivile> uneEntree : valeursSelonDocumentOFAS.entrySet()) {
            StringBuilder message = new StringBuilder();
            message.append("\n\n");
            message.append("RefNumber: ").append(uneEntree.getKey()).append("\n");
            message.append("Expected: ").append(uneEntree.getValue().name()).append("\n");

            APTypeProtectionCivile result = globaz.apg.utils.APRuleUtils.getTypePCenFonctionDuRefNumber(uneEntree
                    .getKey());
            message.append("Result: ").append(result).append("\n");

            message.append("\n");
            Assert.assertEquals(message.toString(), uneEntree.getValue(), result);
        }
    }
}
