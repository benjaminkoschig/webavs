package ch.globaz.pegasus.factory.pca;

import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class SimplePlanDeCalculFactory {
    public static SimplePlanDeCalcul generate() {
        SimplePlanDeCalcul planDeCalcul = new SimplePlanDeCalcul();
        return planDeCalcul;
    }
}
