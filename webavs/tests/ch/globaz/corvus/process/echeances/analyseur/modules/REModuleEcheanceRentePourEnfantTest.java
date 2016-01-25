package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import org.junit.Before;
import org.junit.Test;

public class REModuleEcheanceRentePourEnfantTest extends REModuleAnalyseEcheanceTest {

    private RERenteJoinDemandeEcheance rente;

    public REModuleEcheanceRentePourEnfantTest() {
        super();
    }

    @Test
    public void renteAPI() {

        // Rentes API-AVS à domicile (10ème rév)
        rente.setCodePrestation("85"); // impotence de degré faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("86"); // impotence de degré moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("87"); // impotence de degré grave
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("89"); // impotence de degré faible avec accompagnement
        assertFalse(module, entity, "01.2012");

        // Rentes API-AI à domicile (10ème rév)
        rente.setCodePrestation("81"); // impotence de degré faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("82"); // impotence de degré moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("83"); // impotence de degré grave
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("84"); // impotence de degré faible avec accompagnement
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("88"); // impotence de degré moyen avec accompagnement
        assertFalse(module, entity, "01.2012");

        // Rentes API-AVS en cas de séjour à domicile ou dans un home (10 et 9ème rév)
        rente.setCodePrestation("94"); // impotence de degré faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("95"); // impotence de degré faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("96"); // impotence de degré moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("97"); // impotence de degré grave
        assertFalse(module, entity, "01.2012");

        // Rentes API-AI en cas de séjour à domicile ou dans un home (10 et 9ème rév)
        rente.setCodePrestation("91"); // impotence de degré faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("92"); // impotence de degré moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("93"); // impotence de degré grave
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void renteInvalidite() {

        // Rentes AI ordinaires
        rente.setCodePrestation("50"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("52"); // moitié de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("53"); // complémentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("54"); // complémentaire liée à la rente du père
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("55"); // complémentaire liée à la rente de la mère
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("56"); // complémentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);

        // Rentes AI extraordinaires
        rente.setCodePrestation("70"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("72"); // moitié de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("73"); // complémentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("74"); // complémentaire liée à la rente du père
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("75"); // complémentaire liée à la rente de la mère
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("76"); // complémentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);
    }

    @Test
    public void renteSurvivant() {

        // Rentes survivant ordinaires
        rente.setCodePrestation("13"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("14"); // complémentaire liée à la rente du père
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("15"); // complémentaire liée à la rente de la mère
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("16"); // complémentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);

        // Rentes survivant extraordinaires
        rente.setCodePrestation("23"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("24"); // complémentaire liée à la rente du père
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("25"); // complémentaire liée à la rente de la mère
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("26"); // complémentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);
    }

    @Test
    public void renteVieillesse() {

        // Rentes vieillesse ordinaires
        rente.setCodePrestation("10"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("12"); // moitié de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("33"); // complémentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("34"); // complémentaire liée à la rente du père
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("35"); // complémentaire liée à la rente de la mère
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("36"); // complémentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);

        // Rentes vieillesse extraordinaires
        rente.setCodePrestation("20"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("22"); // moitié de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("43"); // complémentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("44"); // complémentaire liée à la rente du père
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("45"); // complémentaire liée à la rente de la mère
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("46"); // complémentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);
    }

    @Before
    public void setUp() {
        module = new REModuleEcheanceRentePourEnfant(session, "01.2012");

        entity = new REEcheancesEntity();

        rente = new RERenteJoinDemandeEcheance();
        rente.setIdPrestationAccordee("1");
        entity.getRentesDuTiers().add(rente);
    }
}
