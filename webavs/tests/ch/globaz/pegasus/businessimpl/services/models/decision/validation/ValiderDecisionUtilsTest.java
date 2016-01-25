package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

public class ValiderDecisionUtilsTest {

    @BeforeClass
    public static void initJadeTread() {
        // JadeThreadContextPool sc = Mockito.mock(JadeThreadContextPool.class);
        // JadeThreadContextPool.getInstance();
    }

    @Test
    public void testIsDroitInitialString() {
        Assert.assertEquals(true, ValiderDecisionUtils.isDroitInitial("1"));
    }

    @Test
    public void testIsDroitInitialValiderDecisionAcData() {
        ValiderDecisionAcData data = new ValiderDecisionAcData();
        data.setSimpleVersionDroitNew(new SimpleVersionDroit());
        data.getSimpleVersionDroitNew().setNoVersion("1");
        Assert.assertEquals(true, ValiderDecisionUtils.isDroitInitial(data));
    }

    @Test
    public void testResoloveLastDecisionRequerant() throws DecisionException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100", "06.2013"));
        DecisionApresCalcul dc = ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150", "06.2013");
        decisions.add(dc);
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "110", "06.2013"));

        decisions.add(ValidationAcFactory.generateDecisionAcConjoint("06.2012", "06.2012", "100", "06.2013"));
        decisions.add(ValidationAcFactory.generateDecisionAcConjoint("01.2013", null, "150", "06.2013"));
        decisions.add(ValidationAcFactory.generateDecisionAcConjoint("07.2012", "12.2012", "110", "06.2013"));

        Assert.assertEquals(dc, ValiderDecisionUtils.resolvedLastDecisionRequerant(decisions, "01.2013"));
    }

    @Test
    public void testResolveLastDecisions() throws DecisionException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100", "06.2013"));
        DecisionApresCalcul dc = ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150", "06.2013");
        decisions.add(dc);
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "110", "06.2013"));
        Assert.assertEquals(dc, ValiderDecisionUtils.resolvedLastDecisionRequerant(decisions, "01.2013"));
    }

    // @Test(expected = DecisionException.class)
    public void testResolveLastDecisionsErreur() throws DecisionException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100", "06.2013"));
        DecisionApresCalcul dc = ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150", "06.2013");
        decisions.add(dc);
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "110", "06.2013"));
        Assert.assertEquals(dc, ValiderDecisionUtils.resolvedLastDecisionRequerant(decisions, null));
    }

}
