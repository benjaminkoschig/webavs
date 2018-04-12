package globaz.phenix.util;

import static org.junit.Assert.*;
import globaz.phenix.db.principale.CPDecision;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.common.domaine.Date;

public class CPUtilTest {
    Integer anneeCourante;

    @Before
    public void setUp() {
        anneeCourante = Date.getCurrentYear();
    }

    @Test
    public void testDeterminerTypeDecisionIfTypeDecisionActuelleIsNull() {
        Integer anneeDemande = anneeCourante;
        String type = CPUtil.determinerTypeDecision(anneeDemande, null);
        assertEquals(CPDecision.CS_ACOMPTE, type);

        type = CPUtil.determinerTypeDecision(anneeDemande - 1, null);
        assertEquals(CPDecision.CS_PROVISOIRE, type);

        type = CPUtil.determinerTypeDecision(anneeDemande + 1, null);
        assertEquals(CPDecision.CS_PROVISOIRE, type);
    }

    @Test
    public void testDeterminerTypeDecisionIfTypeDecisionActuelleIsProvisoire() {
        Integer anneeDemande = anneeCourante;
        String type = CPUtil.determinerTypeDecision(anneeDemande, CPDecision.CS_PROVISOIRE);
        assertEquals(CPDecision.CS_CORRECTION, type);

        type = CPUtil.determinerTypeDecision(anneeDemande - 1, CPDecision.CS_PROVISOIRE);
        assertEquals(CPDecision.CS_CORRECTION, type);

        type = CPUtil.determinerTypeDecision(anneeDemande + 1, CPDecision.CS_PROVISOIRE);
        assertEquals(CPDecision.CS_CORRECTION, type);
    }

    @Test
    public void testDeterminerTypeDecisionIfTypeDecisionActuelleIsCorrection() {
        Integer anneeDemande = anneeCourante;
        String type = CPUtil.determinerTypeDecision(anneeDemande, CPDecision.CS_CORRECTION);
        assertEquals(CPDecision.CS_CORRECTION, type);

        type = CPUtil.determinerTypeDecision(anneeDemande - 1, CPDecision.CS_CORRECTION);
        assertEquals(CPDecision.CS_CORRECTION, type);

        type = CPUtil.determinerTypeDecision(anneeDemande + 1, CPDecision.CS_CORRECTION);
        assertEquals(CPDecision.CS_CORRECTION, type);
    }

    @Test
    public void testDeterminerTypeDecisionIfTypeDecisionActuelleIsNotCorrectionAndNotProvisoire() {
        Integer anneeDemande = anneeCourante;
        String type = CPUtil.determinerTypeDecision(anneeDemande, CPDecision.CS_ACOMPTE);
        assertEquals(CPDecision.CS_ACOMPTE, type);

        type = CPUtil.determinerTypeDecision(anneeDemande - 1, CPDecision.CS_ACOMPTE);
        assertEquals(CPDecision.CS_PROVISOIRE, type);

    }
}
