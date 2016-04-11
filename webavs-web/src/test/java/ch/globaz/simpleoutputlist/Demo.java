package ch.globaz.simpleoutputlist;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.pegasus.tests.util.Init;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;
import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

public class Demo {
    private static final Object OBJECT = new Object();

    @Test
    public void testSimple() {
        // List<StatPresation> statPresations = QueryExecutor
        // .execute(
        // "select schema.REPRACC.ZTLCPR as code , schema.REPRACC.ZTMPRE as montant from schema.REPRACC where (ZTDFDR=0 or ZTDFDR is null) AND schema.REPRACC.ZTLCPR IN ('110') ",
        // StatPresation.class);
        //
        // // SimpleOutPutListGenerator<StatPresation> csv = SimpleOuptutListFactory.buildCsv(StatPresation.class,
        // // Locale.FRENCH);
        // // File file = xls.generateFileWithHeader("D:/demo/demo", stats);
        // File file = SimpleOutputListBuilderJade.newInstance().outputNameAndAddPath("demo").asXls()
        // .addList(statPresations).addTitle("Title", Align.LEFT).build();
        // System.out.println(file.getAbsolutePath());

        List<StatPresation> statPresations = QueryExecutor
                .execute(
                        "select schema.REPRACC.ZTLCPR as code , count(schema.REPRACC.ZTLCPR) as nb, sum(schema.REPRACC.ZTMPRE) as montant from schema.REPRACC where (ZTDFDR=0 or ZTDFDR is null) group by schema.REPRACC.ZTLCPR",
                        StatPresation.class);

        ListMultimap<String, StatPresation> multimap = Multimaps.index(statPresations,
                new Function<StatPresation, String>() {
                    @Override
                    public String apply(StatPresation stat) {
                        if ("-110-150-113-".contains("-" + stat.getCode() + "-")) {
                            return "PC";
                        } else {
                            return "Autre";
                        }
                    }
                });

        SimpleOutputListBuilder builder = SimpleOutputListBuilderJade.newInstance().outputNameAndAddPath("demoJump")
                .asCsv();
        for (Entry<String, Collection<StatPresation>> entry : multimap.asMap().entrySet()) {
            builder.addList(entry.getValue()).addSubTitle(entry.getKey()).jump();
        }
        builder.build();
        builder.asPdf().build();

        // Details details = new Details();
        // details.add("Nb Ligne", String.valueOf(stats.size()));
        // details.addEmpty();
        // details.addEmpty();
        // details.addEmpty();
        // details.newLigne();
        // pdf.addDetails(details);

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
