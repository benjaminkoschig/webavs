package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDecision;

public class PrepareAnnonceLapramsServiceImplTestCase {

    @Test
    public void testResolveDestinationSortie() throws Exception {
        PrepareAnnonceLapramsServiceImpl annonceLapramsServiceImpl = new PrepareAnnonceLapramsServiceImpl();
        Assert.assertEquals("DC",
                annonceLapramsServiceImpl.resolveDestinationSortie(IPCDecision.CS_MOTIF_SUPPRESSION_DECES, ""));
    }

}
