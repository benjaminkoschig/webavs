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

        // Rentes API-AVS � domicile (10�me r�v)
        rente.setCodePrestation("85"); // impotence de degr� faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("86"); // impotence de degr� moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("87"); // impotence de degr� grave
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("89"); // impotence de degr� faible avec accompagnement
        assertFalse(module, entity, "01.2012");

        // Rentes API-AI � domicile (10�me r�v)
        rente.setCodePrestation("81"); // impotence de degr� faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("82"); // impotence de degr� moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("83"); // impotence de degr� grave
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("84"); // impotence de degr� faible avec accompagnement
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("88"); // impotence de degr� moyen avec accompagnement
        assertFalse(module, entity, "01.2012");

        // Rentes API-AVS en cas de s�jour � domicile ou dans un home (10 et 9�me r�v)
        rente.setCodePrestation("94"); // impotence de degr� faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("95"); // impotence de degr� faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("96"); // impotence de degr� moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("97"); // impotence de degr� grave
        assertFalse(module, entity, "01.2012");

        // Rentes API-AI en cas de s�jour � domicile ou dans un home (10 et 9�me r�v)
        rente.setCodePrestation("91"); // impotence de degr� faible
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("92"); // impotence de degr� moyen
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("93"); // impotence de degr� grave
        assertFalse(module, entity, "01.2012");
    }

    @Test
    public void renteInvalidite() {

        // Rentes AI ordinaires
        rente.setCodePrestation("50"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("52"); // moiti� de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("53"); // compl�mentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("54"); // compl�mentaire li�e � la rente du p�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("55"); // compl�mentaire li�e � la rente de la m�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("56"); // compl�mentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);

        // Rentes AI extraordinaires
        rente.setCodePrestation("70"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("72"); // moiti� de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("73"); // compl�mentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("74"); // compl�mentaire li�e � la rente du p�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("75"); // compl�mentaire li�e � la rente de la m�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("76"); // compl�mentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);
    }

    @Test
    public void renteSurvivant() {

        // Rentes survivant ordinaires
        rente.setCodePrestation("13"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("14"); // compl�mentaire li�e � la rente du p�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("15"); // compl�mentaire li�e � la rente de la m�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("16"); // compl�mentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);

        // Rentes survivant extraordinaires
        rente.setCodePrestation("23"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("24"); // compl�mentaire li�e � la rente du p�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("25"); // compl�mentaire li�e � la rente de la m�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("26"); // compl�mentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);
    }

    @Test
    public void renteVieillesse() {

        // Rentes vieillesse ordinaires
        rente.setCodePrestation("10"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("12"); // moiti� de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("33"); // compl�mentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("34"); // compl�mentaire li�e � la rente du p�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("35"); // compl�mentaire li�e � la rente de la m�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("36"); // compl�mentaire double (pour orphelin)
        assertTrue(module, entity, "01.2012", null);

        // Rentes vieillesse extraordinaires
        rente.setCodePrestation("20"); // principale
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("22"); // moiti� de rente
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("43"); // compl�mentaire pour conjoint
        assertFalse(module, entity, "01.2012");
        rente.setCodePrestation("44"); // compl�mentaire li�e � la rente du p�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("45"); // compl�mentaire li�e � la rente de la m�re
        assertTrue(module, entity, "01.2012", null);
        rente.setCodePrestation("46"); // compl�mentaire double (pour orphelin)
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
