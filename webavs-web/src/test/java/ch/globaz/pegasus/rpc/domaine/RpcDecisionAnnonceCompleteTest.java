package ch.globaz.pegasus.rpc.domaine;

import org.junit.Ignore;
import org.junit.Test;

public class RpcDecisionAnnonceCompleteTest {

    /**
     * 0 = pas de besoins vitaux (Home) (NO_NEEDS)
     * 1 = Personne seule (ALONE)
     * 2 = Couple (COUPLE)
     * 3 = Orphelin / Enfant (CHILD)
     * La cat�gorie de besoin vital est communiqu�e, de sorte qu'un orphelin ou un enfant peut par ex. se voir attribuer
     * la cat�gorie 1 (personne seule). .
     */
    @Ignore("not yet implemented")
    @Test
    public void testGetVitalNeedsCategory() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

}
