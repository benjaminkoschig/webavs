package ch.globaz.al.test.businessimpl.service.models.attribut;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Test sur les attributs entité
 * 
 * @author GMO
 * 
 */
public class AttributEntiteModelServiceImplTest {

    /**
     * Test method for
     * 
     * {@link ch.globaz.al.businessimpl.services.models.attribut. AttributEntiteModelServiceImpl #getAttributAffilie(String,String)}
     */
    @Ignore
    @Test
    public void testGetAttributAffilie() {

        try {
            AttributEntiteModel attrEcheance = ALServiceLocator.getAttributEntiteModelService().getAttributAffilie(
                    "DESTAVISECH", "18336");
            assertEquals(attrEcheance.getValeurNum(), ALCSAffilie.ATTRIBUT_AVIS_ECH_AFFILIE);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
