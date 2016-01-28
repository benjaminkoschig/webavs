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
         * Une d�cision standard non valid�e, il doit �tre possible de modifier les rentes vers�es � tort
         */
        REDecisionJointDemandeRente uneDecisionStandardNonValidee = new REDecisionJointDemandeRente();
        uneDecisionStandardNonValidee.setIdDecision(genererUnIdUniqueToString());
        uneDecisionStandardNonValidee.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_STANDARD);
        uneDecisionStandardNonValidee.setCsEtat(IREDecision.CS_ETAT_ATTENTE);

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionStandardNonValidee)));

        /*
         * Une deuxi�me d�cision d�j� valid�e, il doit encore �tre possible de modifier les rentes vers�es � tort
         */
        REDecisionJointDemandeRente uneDecisionStandardValidee = new REDecisionJointDemandeRente();
        uneDecisionStandardValidee.setIdDecision(genererUnIdUniqueToString());
        uneDecisionStandardValidee.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_STANDARD);
        uneDecisionStandardValidee.setCsEtat(IREDecision.CS_ETAT_VALIDE);

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionStandardNonValidee,
                uneDecisionStandardValidee)));

        /*
         * Seulement la d�cision valid�e, il ne doit plus �tre possible de modifier les rentes vers�es � tort
         */
        Assert.assertFalse(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionStandardValidee)));

        /*
         * Une d�cision courante, non valid�e, il doit �tre possible de modifier les rentes vers�es � tort
         */
        REDecisionJointDemandeRente uneDecisionCouranteNonValidee = new REDecisionJointDemandeRente();
        uneDecisionCouranteNonValidee.setIdDecision(genererUnIdUniqueToString());
        uneDecisionCouranteNonValidee.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
        uneDecisionCouranteNonValidee.setCsEtat(IREDecision.CS_ETAT_ATTENTE);

        Assert.assertTrue(RERenteVerseeATortHelper.modificationPossible(Arrays.asList(uneDecisionCouranteNonValidee)));
    }
}
