package globaz.osiris.db.ordres;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;

/**
 * Description de la classe
 * 
 * @since WebBMS 0.6
 */
public class CARefBVRBanqueParserTest {
    // @Test
    public void setReferenceTest() {
        // Param
        String numeroReference = "391073870270500812015010003";
        String numInterneLsv = "";
        BSession session = BSessionUtil.getSessionFromThreadContext();

        // Result

        // Appel
        CARefBVRBanqueParser parser = new CARefBVRBanqueParser();
        try {
            parser.setReference(numeroReference, session, numInterneLsv);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Test
    }
}
