package ch.globaz.pegasus.businessimpl.tests.calcul;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import junit.framework.Assert;
import ch.globaz.pegasus.tests.util.CasTestUtil;
import ch.globaz.pegasus.tests.util.End;

public class CalculEnd extends End {
    @Override
    public void tearDown() {
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        try {
            if (CasTestUtil.idLotToUpdate != null) {
                CasTestUtil.reUpadteLot();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JadeThreadActivator.stopUsingContext(this);
        System.out.println("Completed");
    }

    @Override
    public void test() {
        Assert.assertTrue(true);
    }

}
