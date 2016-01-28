package ch.globaz.topaz.core;

import globaz.jade.print.client.JadePrintServerFacade;
import globaz.jade.print.message.JadePrintServerInfo;
import globaz.jade.print.server.JadePrintServer;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class TestOpenOfficeCrashWithBigDocuments {
    private static final String printServerConfig = "<globaz.jade.print.server.JadePrintServer verbose=\"true\">"
            + "        <pdf.merge.maxsize>20</pdf.merge.maxsize>" + "    </globaz.jade.print.server.JadePrintServer>";

    private static final TopazSystem instance;
    static {
        try {
            instance = TopazSystem.getInstance();
            JadePrintServer jps = JadePrintServer.getInstance();
            jps.initialize(DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ByteArrayInputStream(printServerConfig.getBytes("Cp1252"))).getDocumentElement());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Ignore
    @Test
    public void makeABigFile() throws Exception {
        DocumentData dd = new DocumentData();
        dd.addData("idDocument", "simulationPaiementDirectListeDetaillee");
        dd.addData("idEntete", "FPV");
        dd.addData("idSignature", "CCVD");

        dd.addData("info_processus_label", "test");
        dd.addData("info_processus_val", "pouet");
        dd.addData("info_traitement_label", "test");
        dd.addData("info_traitement_val", "ppopuet");

        Collection tableVerse = new Collection("listeDetaillee_versement");
        for (int j = 0; j < 25000; j++) {
            DataList lignePaiement = new DataList("colonne");
            lignePaiement.addData("col_allocataire", "pouet_" + j + " - 1234");
            lignePaiement.addData("col_solde_init", "123");
            lignePaiement.addData("col_debit", "123");
            lignePaiement.addData("col_credit", "123");

            lignePaiement.addData("col_nouv_solde", "123");
            lignePaiement.addData("col_odrdre_versement", "123");

            tableVerse.add(lignePaiement);
        }
        dd.add(tableVerse);

        Collection tableResti = new Collection("listeDetaillee_restitution");
        dd.add(tableResti);

        instance.createDocument(dd, "file:///d:/Temp/test.pdf");

        JadePrintServerInfo jpsi = JadePrintServerFacade.startServer();
        System.out.println(jpsi.getServerName());
    }
}
