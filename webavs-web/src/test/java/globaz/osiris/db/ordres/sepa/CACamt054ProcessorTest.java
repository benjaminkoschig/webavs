package globaz.osiris.db.ordres.sepa;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class CACamt054ProcessorTest {

    private Document camt054Bvr = null;
    private Document pain002 = null;

    @Test
    public void testIsCamt054BVRAndWantedType() throws Exception {
        boolean isCamt054Bvr = new CACamt054Processor().isCamt054AndWantedType(CACamt054DefinitionType.BVR, camt054Bvr);

        assertTrue("Except to have a camt054 BVR type of file", isCamt054Bvr);

        boolean isNotACamt054Bvr = new CACamt054Processor()
                .isCamt054AndWantedType(CACamt054DefinitionType.BVR, pain002);

        assertFalse("Except to not have a camt054 BVR type of file", isNotACamt054Bvr);
    }

    @Before
    public void initFile() {
        camt054Bvr = CAJaxbUtil.parseDocument(getClass().getResourceAsStream(
                "/globaz/osiris/db/ordres/sepa/camt.054-BVR.xml"));

        pain002 = CAJaxbUtil.parseDocument(getClass().getResourceAsStream(
                "/globaz/osiris/db/ordres/sepa/pain_001_Beispiel_1.xml"));
    }

}
