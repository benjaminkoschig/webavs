package ch.globaz.common.domaine;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

public class AdresseTest {

    @Test
    public void testResolveDesignation1() throws Exception {
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"),
                "csTitre", "rue", "rueNumero", "designation1", "designation2", "designation3", "designation4",
                "idAdresse", "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4",
                "csTitreTiers");
        assertEquals("designation1", adresse.resolveDesignation1());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "", "designation2", "designation3", "designation4", "idAdresse",
                "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("designationTiers1", adresse.resolveDesignation1());

    }

    @Test
    @Ignore
    public void testResolveDesignation2() throws Exception {
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"),
                "csTitre", "rue", "rueNumero", "designation1", "designation2", "designation3", "designation4",
                "idAdresse", "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4",
                "csTitreTiers");
        assertEquals("designation2", adresse.resolveDesignation2());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "designation1", "", "designation3", "designation4", "idAdresse",
                "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("designationTiers2", adresse.resolveDesignation2());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "designation1", " ", "designation3", "designation4", "idAdresse",
                "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("designationTiers2", adresse.resolveDesignation2());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "designation1", null, "designation3", "designation4", "idAdresse",
                "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("designationTiers2", adresse.resolveDesignation2());
    }

    @Ignore
    @Test
    public void testResolveDesignation3() throws Exception {
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"),
                "csTitre", "rue", "rueNumero", "designation1", "designation2", "designation3", "designation4",
                "idAdresse", "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4",
                "csTitreTiers");
        assertEquals("designation3", adresse.resolveDesignation3());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "designation1", "designation2", "", "designation4", "idAdresse",
                "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("designationTiers3", adresse.resolveDesignation3());

    }

    @Ignore
    @Test
    public void testResolveDesignation4() throws Exception {
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"),
                "csTitre", "rue", "rueNumero", "designation1", "designation2", "designation3", "designation4",
                "idAdresse", "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4",
                "csTitreTiers");
        assertEquals("designation4", adresse.resolveDesignation4());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "designation1", "designation2", "designation3", "", "idAdresse",
                "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("designationTiers4", adresse.resolveDesignation4());
    }

    @Test
    public void testResolveCsTitre() throws Exception {
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"),
                "csTitre", "rue", "rueNumero", "designation1", "designation2", "designation3", "designation4",
                "idAdresse", "designationTiers1", "designationTiers2", "designationTiers3", "designationTiers4",
                "csTitreTiers");
        assertEquals("csTitre", adresse.resolveCsTitre());

        adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", new Canton("NE"), "csTitre",
                "rue", "rueNumero", "", "designation2", "designation3", "", "idAdresse", "designationTiers1",
                "designationTiers2", "designationTiers3", "designationTiers4", "csTitreTiers");
        assertEquals("csTitreTiers", adresse.resolveCsTitre());
    }

}
