package globaz.cygnus.db.dossier;

import globaz.cygnus.db.dossiers.RFPeriodeCAAIWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class RFPeriodeCAAIWrapperTest {

    @Test
    public void testCompareTo() {
        RFPeriodeCAAIWrapper periode1 = new RFPeriodeCAAIWrapper();
        periode1.setIdContributionAssistanceAI("1");
        periode1.setDateDebutCAAI("01.01.2013");
        periode1.setDateFinCAAI("31.01.2013");

        RFPeriodeCAAIWrapper periode2 = new RFPeriodeCAAIWrapper();
        periode2.setIdContributionAssistanceAI("2");
        periode2.setDateDebutCAAI("01.12.2012");
        periode2.setDateFinCAAI("29.12.2012");

        RFPeriodeCAAIWrapper periode3 = new RFPeriodeCAAIWrapper();
        periode3.setIdContributionAssistanceAI("3");
        periode3.setDateDebutCAAI("01.02.2013");
        periode3.setDateFinCAAI("");

        RFPeriodeCAAIWrapper periode4 = new RFPeriodeCAAIWrapper();
        periode4.setIdContributionAssistanceAI("4");
        periode4.setDateDebutCAAI("30.12.2012");
        periode4.setDateFinCAAI("31.12.2012");

        List<RFPeriodeCAAIWrapper> listeTriee = new ArrayList<RFPeriodeCAAIWrapper>();
        listeTriee.add(periode4);
        listeTriee.add(periode3);
        listeTriee.add(periode2);
        listeTriee.add(periode1);
        Collections.sort(listeTriee);

        Assert.assertTrue(listeTriee.get(0) == periode2);
        Assert.assertTrue(listeTriee.get(1) == periode4);
        Assert.assertTrue(listeTriee.get(2) == periode1);
        Assert.assertTrue(listeTriee.get(3) == periode3);

        for (RFPeriodeCAAIWrapper unWrapper : listeTriee) {
            System.out.println(unWrapper.getIdContributionAssistanceAI() + " : " + unWrapper.getDateDebutCAAI() + " - "
                    + unWrapper.getDateFinCAAI());
        }
    }
}
