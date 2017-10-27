package ch.globaz.naos.ree.domain;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Canton;

public class AdresseLoaderTest {

    @Test
    public void testSubStringField() throws Exception {
        Canton canton = new Canton("JU");
        Adresse adresse = new Adresse("localite", "casePostale", "attention", "npa", "pays", canton, "csTitre", "rue",
                AdresseLoader.subStringField("rueNumerorueNumero", 0, 12), "designation1", "designation2",
                "designation3", "designation4", "idAdresse", "designationTiers1", "designationTiers2",
                "designationTiers3", "designationTiers4", "csTitreTiers");

        assertEquals("123456789012", AdresseLoader.subStringField("123456789012", 0, 12));
        assertEquals("123456789012", AdresseLoader.subStringField("12345678901234", 0, 12));
        assertEquals("12345678901", AdresseLoader.subStringField("12345678901", 0, 12));
        assertEquals("", AdresseLoader.subStringField("", 0, 12));
        assertEquals(null, AdresseLoader.subStringField(null, 0, 12));

        assertEquals("rueNumerorue", adresse.getRueNumero());
    }

}
