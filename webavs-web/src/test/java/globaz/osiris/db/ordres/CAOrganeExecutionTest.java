package globaz.osiris.db.ordres;

import static org.junit.Assert.*;
import globaz.framework.util.FWMessageFormat;
import globaz.osiris.db.ordres.sepa.CACamt054DefinitionType;
import globaz.osiris.db.ordres.sepa.CACamt054GroupTxMessage;
import globaz.osiris.db.ordres.sepa.CACamt054Notification;
import globaz.osiris.db.ordres.sepa.CACamt054VersionResolver;
import globaz.osiris.db.ordres.sepa.CAJaxbUtil;
import globaz.osiris.db.ordres.sepa.exceptions.CACamt054UnsupportedVersionException;
import java.util.List;
import java.util.logging.Level;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class CAOrganeExecutionTest {

    private static CAOrganeExecution organeExecution;
    private static List<CACamt054Notification> notifications;

    @Test
    public void testCheckIBANFaux() throws Exception {
        final CACamt054Notification notification = notifications.get(0);
        final String messageAAfficher = "Votre iban n'est pas en concordance";

        CACamt054GroupTxMessage groupTxMessage = new CACamt054GroupTxMessage(notification);

        organeExecution.checkIBAN(notification.getIdentification(), "FAUX_IBAN", groupTxMessage,
                FWMessageFormat.prepareQuotes(messageAAfficher, false));

        assertTrue(!groupTxMessage.getMessageFromLevel(Level.INFO).isEmpty());
        assertEquals(messageAAfficher, groupTxMessage.getMessageFromLevel(Level.INFO).get(0));
    }

    @Test
    public void testCheckIBANBon() throws Exception {
        final CACamt054Notification notification = notifications.get(0);
        final String messageAAfficher = "Votre iban n'est pas en concordance";

        CACamt054GroupTxMessage groupTxMessage = new CACamt054GroupTxMessage(notification);

        organeExecution.checkIBAN(notification.getIdentification(), "CH0309000000250090342", groupTxMessage,
                messageAAfficher);

        assertTrue(groupTxMessage.getMessageFromLevel(Level.INFO).isEmpty());
    }

    @BeforeClass
    public static void init() throws CACamt054UnsupportedVersionException {
        organeExecution = new CAOrganeExecution();
        Document camt054Bvr = CAJaxbUtil.parseDocument(CAOrganeExecutionTest.class
                .getResourceAsStream("/globaz/osiris/db/ordres/sepa/camt.054-BVR.xml"));
        notifications = CACamt054VersionResolver.resolveDocument(camt054Bvr, "", CACamt054DefinitionType.BVR);
    }
}
