package ch.globaz.vulpecula.documents.rectificatif;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * Imprime un rectificatif
 * 
 * @since WebBMS 1.0
 */
public class DocumentRectificatifPrinterTest {

    @Test
    @Ignore
    public void documentTest() {
        try {
            BSession session = new BSession("VULPECULA");
            session.connect("BMSGLO", "BMSGLO");

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(session);
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            List<Decompte> listDecomptesRectificatif = new ArrayList<Decompte>();

            ArrayList<String> liste = new ArrayList<String>(Arrays.asList("310132", "309655", "305308", "308622",
                    "308624", "309926", "305201", "307630", "308632"));
            for (String string : liste) {
                Decompte decompte = new Decompte();
                decompte.setId(string);
                listDecomptesRectificatif.add(decompte);
            }

            DocumentRectificatifPrinter docs = new DocumentRectificatifPrinter(
                    DocumentPrinter.getIds(listDecomptesRectificatif));
            docs.setEMailAddress("sel@globaz.ch");
            docs.setSession(session);
            try {
                docs.executeProcess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        // System.exit(1);
    }
}
