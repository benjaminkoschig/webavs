package ch.globaz.vulpecula.documents;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Role;

/**
 * Test la génération du numéro de référence BVR
 * 
 * @since WebBMS 0.6
 */
public class NumeroReferenceFactoryTest {
    @Test
    @Ignore
    public void testCreateNumeroReference() {
        // Param
        String numeroBanque = "107387";

        // Result
        final String reference = "10738739027050081201501000";

        // Appel
        NumeroReference numRef = null;
        try {
            numRef = NumeroReferenceFactory.createNumeroReference(Role.AFFILIE_PARITAIRE, "0002705.00-12",
                    TypeSection.BULLETIN_NEUTRE, new NumeroDecompte("201501000"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test
        assertEquals(reference, numRef.getValue());
    }
}
