package ch.globaz.pegasus.businessimpl.services.models.decision.ged;

import static junit.framework.Assert.*;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.tests.util.Init;

public class DACGedHandlerTest {

    @Before
    public void runJade() throws SQLException, Exception {

        Jade.getInstance();
        BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
                .newSession("ccjuglo", "glob4az");
        JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
        JadeThread.currentContext().storeTemporaryObject("bsession", session);

    }

    @After
    public void tearDownJade() {
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        JadeThreadActivator.stopUsingContext(this);
    }

    @Test
    @Ignore
    public void testIdsDecisionsForLotTypeDecisionOK() throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, LotException {

        String idLotCCJUQUATypeDecisionPC = "1010";

        DACGedHandler dacGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLotCCJUQUATypeDecisionPC,
                "ccjuglo", null, null);
        ArrayList<String> idsPrestationsForLot = dacGedHandler
                .getIdsPrestationsForLotDecision(idLotCCJUQUATypeDecisionPC);
        ArrayList<String> idsDecisionsForLot = dacGedHandler.getIdsDacToPutInGed();
        assertNotNull(idsDecisionsForLot);
        assertTrue(idsDecisionsForLot.size() > 0);
    }

    @Test
    @Ignore
    public void testIdsDecisionsForLotTypeDecisionPasOK() throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, LotException {

        String idLotCCJUQUATypeDecisionPC = "1";

        DACGedHandler dacGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLotCCJUQUATypeDecisionPC,
                "ccjuglo", null, null);
        ArrayList<String> idsPrestationsForLot = dacGedHandler
                .getIdsPrestationsForLotDecision(idLotCCJUQUATypeDecisionPC);
        ArrayList<String> idsDecisionsForLot = dacGedHandler.getIdsDacToPutInGed();
        assertNotNull(idsDecisionsForLot);
        assertTrue(idsDecisionsForLot.size() == 0);
    }

    @Test
    @Ignore
    public void testIdsPrestationsForLotTypeDecisionOK() throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, LotException {

        String idLotCCJUQUATypeDecisionPC = "2425";

        DACGedHandler dacGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLotCCJUQUATypeDecisionPC,
                "ccjuglo", null, null);
        ArrayList<String> idsPrestationsForLot = dacGedHandler
                .getIdsPrestationsForLotDecision(idLotCCJUQUATypeDecisionPC);
        assertNotNull(idsPrestationsForLot);
        assertTrue(idsPrestationsForLot.size() > 0);
    }

    @Test
    @Ignore
    public void testIdsPrestationsForLotTypeDecisionPasOK() throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, LotException {

        String idLotCCJUQUATypeDecisionPC = "1";

        DACGedHandler dacGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLotCCJUQUATypeDecisionPC,
                "ccjuglo", null, null);
        ArrayList<String> idsPrestationsForLot = dacGedHandler
                .getIdsPrestationsForLotDecision(idLotCCJUQUATypeDecisionPC);
        assertNotNull(idsPrestationsForLot);
        assertTrue(idsPrestationsForLot.size() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testIllegalParametersInstanciationWith0() throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, LotException {

        String idLot = "0";

        DACGedHandler dacGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLot, "ccjuglo", null, null);
        ArrayList<String> idsPrestationsForLot = dacGedHandler.getIdsPrestationsForLotDecision(idLot);
        assertNotNull(idsPrestationsForLot);
        assertTrue(idsPrestationsForLot.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void testIllegalParametersInstanciationWithNull() throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, LotException {

        String idLot = null;

        DACGedHandler dacGedHandler = DACGedHandler.getInstanceForTraitementPourLot(idLot, "ccjuglo", null, null);
        ArrayList<String> idsPrestationsForLot = dacGedHandler.getIdsPrestationsForLotDecision(idLot);
        assertNotNull(idsPrestationsForLot);
        assertTrue(idsPrestationsForLot.size() > 0);
    }
}
