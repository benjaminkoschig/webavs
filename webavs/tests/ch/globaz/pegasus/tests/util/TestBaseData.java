package ch.globaz.pegasus.tests.util;

import java.util.HashMap;
import ch.globaz.pegasus.tests.util.calcul.CalculCasTest;

public class TestBaseData {

    /* Hash map contenat les cas tests */
    public static HashMap<String, CasTest> casForTest = new HashMap<String, CasTest>();

    static {

        try {
            // -----------------------------------------------------------
            CalculCasTest casCalcul = new CalculCasTest("756.4560.1707.48", "Champrenaud xuan",
                    "Test Calcul rentes simples", "01.12.2011");
            casCalcul
                    .setDescription("Cas test avec Champrenaud xuan \n date de prochain paiement: 01.12.2011, \n date annonce droit : 01.01.2011");
            casCalcul.getDemande().getSimpleDemande().setDateDepot("01.01.2011");
            casCalcul.getDemande().getSimpleDemande().setDateProchaineRevision("01.2020");
            TestBaseData.casForTest.put(casCalcul.getNSS(), casCalcul);
            // -----------------------------------------------------------
            casCalcul = new CalculCasTest("756.7937.1338.41", "Chablaix Claudine", "Test Calcul rentes avec fortune",
                    "01.12.2011");
            casCalcul
                    .setDescription("Cas test avec Bigler Françoise \n date de prochain paiement: 01.12.2011, \n date annonce droit : 01.01.2011");
            casCalcul.getDemande().getSimpleDemande().setDateDepot("01.01.2011");
            casCalcul.getDemande().getSimpleDemande().setDateProchaineRevision("01.2020");
            TestBaseData.casForTest.put(casCalcul.getNSS(), casCalcul);
            // -----------------------------------------------------------
            casCalcul = new CalculCasTest("756.1234.4366.81", "Brandsma Paul", "Test Calcul périodes", "01.12.2011");
            casCalcul
                    .setDescription("Cas test avec Brandsma Paul \n date de prochain paiement: 01.12.2011, \n date annonce droit : 01.01.2011");
            casCalcul.getDemande().getSimpleDemande().setDateDepot("01.01.2011");
            casCalcul.getDemande().getSimpleDemande().setDateProchaineRevision("01.2020");
            TestBaseData.casForTest.put(casCalcul.getNSS(), casCalcul);

        } catch (Exception e) {
            System.out.println(">Problem during creating casTest: " + e.getMessage());
        }
    }

}
