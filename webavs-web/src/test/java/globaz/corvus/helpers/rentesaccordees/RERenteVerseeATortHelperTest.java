package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;

public class RERenteVerseeATortHelperTest extends TestUnitaireAvecGenerateurIDUnique {

    @Test
    public void testModificationPossible() throws Exception {

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(new ArrayList<REDecisionJointDemandeRente>()));

        /*
         * Une décision standard non validée, il doit être possible de modifier les rentes versées à tort
         */
        REDecisionJointDemandeRente uneDecisionStandardNonValidee = new REDecisionJointDemandeRente();
        uneDecisionStandardNonValidee.setIdDecision(genererUnIdUniqueToString());
        uneDecisionStandardNonValidee.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_STANDARD);
        uneDecisionStandardNonValidee.setCsEtat(IREDecision.CS_ETAT_ATTENTE);

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionStandardNonValidee)));

        /*
         * Une deuxième décision déjà validée, il doit encore être possible de modifier les rentes versées à tort
         */
        REDecisionJointDemandeRente uneDecisionStandardValidee = new REDecisionJointDemandeRente();
        uneDecisionStandardValidee.setIdDecision(genererUnIdUniqueToString());
        uneDecisionStandardValidee.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_STANDARD);
        uneDecisionStandardValidee.setCsEtat(IREDecision.CS_ETAT_VALIDE);

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionStandardNonValidee,
                uneDecisionStandardValidee)));

        /*
         * Seulement la décision validée, il ne doit plus être possible de modifier les rentes versées à tort
         */
        Assert.assertFalse(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionStandardValidee)));

        /*
         * Une décision courante, non validée, il doit être possible de modifier les rentes versées à tort
         */
        REDecisionJointDemandeRente uneDecisionCouranteNonValidee = new REDecisionJointDemandeRente();
        uneDecisionCouranteNonValidee.setIdDecision(genererUnIdUniqueToString());
        uneDecisionCouranteNonValidee.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
        uneDecisionCouranteNonValidee.setCsEtat(IREDecision.CS_ETAT_ATTENTE);

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionCouranteNonValidee)));
    }
}
