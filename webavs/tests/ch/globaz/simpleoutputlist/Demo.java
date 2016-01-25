package ch.globaz.simpleoutputlist;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.tests.util.Init;

public class Demo {
    private static final Object OBJECT = new Object();

    @Test
    @Ignore
    public void testSimple() {
        // List<StatPresation> stats = QueryExecutor
        // .execute(
        // "select schema.REPRACC.ZTLCPR as code , count(schema.REPRACC.ZTLCPR) as nb, sum(schema.REPRACC.ZTMPRE) as montant from schema.REPRACC where (ZTDFDR=0 or ZTDFDR is null) group by schema.REPRACC.ZTLCPR",
        // StatPresation.class);
        //
        // SimpleOutPutListGenerator<StatPresation> csv = SimpleOuptutListFactory.buildCsv(StatPresation.class,
        // Locale.FRENCH);
        //
        // csv.generateFileWithHeader("D:/demo/demo", stats);
        //
        // SimpleOutPutListGenerator<StatPresation> pdf = SimpleOuptutListFactory.buildPdf(StatPresation.class,
        // Locale.FRENCH);
        // pdf.addTitle("Test", Align.LEFT);
        // Details details = new Details();
        // details.add("Nb Ligne", String.valueOf(stats.size()));
        // details.addEmpty();
        // details.addEmpty();
        // details.addEmpty();
        // details.newLigne();
        // pdf.addDetails(details);
        // pdf.generateFileWithHeader("D:/demo/demo", stats);
        //
        // SimpleOutPutListGenerator<StatPresation> xls = SimpleOuptutListFactory.buildXls(StatPresation.class,
        // Locale.FRENCH);
        // xls.addDetails(details);
        // File file = xls.generateFileWithHeader("D:/demo/demo", stats);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Object ctx = JadeThread.currentContext();
        if (ctx == null) {
            Jade.getInstance();
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
                    .newSession("ccjuglo", "glob4az");
            JadeThreadActivator.startUsingJdbcContext(OBJECT, Init.initContext(session).getContext());
            JadeThread.currentContext().storeTemporaryObject("bsession", session);
        }
    }

    @AfterClass
    public static void tearDown() {

        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        System.out.println("STOPING CONTEXT");
        JadeThreadActivator.stopUsingContext(OBJECT);
    }

}
