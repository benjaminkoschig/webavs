package globaz.osiris.db.ordres.sepa;

import static org.junit.Assert.*;
import globaz.osiris.db.ordres.sepa.exceptions.CACamt054UnsupportedVersionException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class CACamt054BVRVersionResolverTest {

    private Document camt054Bvr = null;
    private Document pain002 = null;

    @Test
    public void testIsSupportedVersion() throws Exception {
        final String nameSpaceSupported = camt054Bvr.getDocumentElement().getNamespaceURI();
        boolean isSupported = CACamt054BVRVersionResolver.isSupportedVersion(nameSpaceSupported);

        assertTrue("Le document doit être supporté", isSupported);

        final String nameSpaceUnsupported = pain002.getDocumentElement().getNamespaceURI();
        boolean isUnsupported = CACamt054BVRVersionResolver.isSupportedVersion(nameSpaceUnsupported);

        assertFalse("Le document ne doit pas être supporté", isUnsupported);
    }

    @Test
    public void testResolveDocument() throws Exception {
        try {
            List<CACamt054Notification> notifications = CACamt054BVRVersionResolver.resolveDocument(camt054Bvr, "");

            assertFalse("La liste ne doit pas être vide", notifications.isEmpty());
        } catch (CACamt054UnsupportedVersionException exception) {
            fail("Une erreure n'est pas attendu à ce niveau");
        }

        try {
            CACamt054BVRVersionResolver.resolveDocument(pain002, "");

            fail("Une erreure est attendu à ce niveau");
        } catch (CACamt054UnsupportedVersionException exception) {
            // We except that
        }
    }

    @Before
    public void initFile() {
        camt054Bvr = AbstractSepa.parseDocument(getClass().getResourceAsStream(
                "/globaz/osiris/db/ordres/sepa/camt.054-BVR.xml"));

        pain002 = AbstractSepa.parseDocument(getClass().getResourceAsStream(
                "/globaz/osiris/db/ordres/sepa/pain_001_Beispiel_1.xml"));
    }
}
