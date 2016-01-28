package ch.globaz.vulpecula.util;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.vulpecula.domain.constants.Constants;
import ch.globaz.vulpecula.util.RubriqueUtil.Compte;
import ch.globaz.vulpecula.util.RubriqueUtil.Convention;
import ch.globaz.vulpecula.util.RubriqueUtil.Prestation;

/**
 * Classe de test de RubriqueUtil
 * 
 * @since WebBMS 0.01.04
 */
public class RubriqueUtilTest {

    @Test(expected = IllegalArgumentException.class)
    public void findReferenceRubriqueFor_GivenConventionNull_ShouldThrowIllegalArgument() throws Exception {
        RubriqueUtil.findReferenceRubriqueFor(Prestation.ABSENCE_JUSTIFIEE, null, Compte.PRESTATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findReferenceRubriqueFor_GivenPrestationNull_ShouldThrowIllegalArgument() throws Exception {
        RubriqueUtil.findReferenceRubriqueFor(null, Convention.BOIS, Compte.PRESTATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findReferenceRubriqueFor_GivenCompteNull_ShouldThrowIllegalArgument() throws Exception {
        RubriqueUtil.findReferenceRubriqueFor(Prestation.ABSENCE_JUSTIFIEE, Convention.BOIS, null);
    }

    @Test
    public void findReferenceRubriqueFor_GivenPrestationAJConventionBoisComptePrest_Should68903002() throws Exception {
        assertEquals(
                RubriqueUtil.findReferenceRubriqueFor(Prestation.ABSENCE_JUSTIFIEE, Convention.BOIS, Compte.PRESTATION),
                Constants.METIER_BOIS_AJ_PREST);
    }

    @Test
    public void findReferenceRubriqueFor_GivenPrestationCPConventionTECHBATComptePartCotCharge_Should68903025()
            throws Exception {
        assertEquals(RubriqueUtil.findReferenceRubriqueFor(Prestation.ABSENCE_JUSTIFIEE, Convention.TECHBAT,
                Compte.PARTS_PATRONALES_AVS), Constants.METIER_TECHBAT_AJ_PPAVS);
    }
}
