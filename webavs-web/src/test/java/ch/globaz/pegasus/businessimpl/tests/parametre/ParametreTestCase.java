package ch.globaz.pegasus.businessimpl.tests.parametre;

import junit.framework.Assert;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteService;

public class ParametreTestCase {

    public final void testGetMontant() throws Exception {
        /*
         * ForfaitPrimeAssuranceMaladieLocaliteService fofaitLocaliteService = PegasusServiceLocator
         * .getParametreServicesLocator().getForfaitPrimeAssuranceMaladieLocaliteService();
         * 
         * Assert.assertNotNull(fofaitLocaliteService);
         * 
         * // 5296 = 1000 Lausanne en région 2 String montant = fofaitLocaliteService.getMontant("01.01.2000",
         * "01.11.2008", IPCParametre.CS_TYPE_PRIME_ADULTE, "5296"); Assert.assertNotNull(montant);
         * Assert.assertEquals("4416.00", montant);
         * 
         * // 5296 = 1000 Lausanne en région 1 montant = fofaitLocaliteService.getMontant("01.01.2009", null,
         * IPCParametre.CS_TYPE_PRIME_ADULTE, "5296"); Assert.assertEquals("4596.00", montant);
         * 
         * try { montant = fofaitLocaliteService.getMontant("01.01.1900", null, IPCParametre.CS_TYPE_PRIME_ADULTE,
         * "5296"); Assert.fail("Exception - > Unable to find montant fail"); } catch (Exception e) {
         * 
         * }
         */
        // Assert.assertEquals("4692.00", montant);

    }

    public final void testSearchMontant() throws Exception {
        ForfaitPrimeAssuranceMaladieLocaliteService fofaitLocaliteService = PegasusServiceLocator
                .getParametreServicesLocator().getForfaitPrimeAssuranceMaladieLocaliteService();
        ForfaitPrimeAssuranceMaladieLocaliteSearch search = new ForfaitPrimeAssuranceMaladieLocaliteSearch();
        search.setForDateDebut("01.01.2008");
        search.setForDateFin("01.11.2008");
        search.setForIdLocalite("5296");
        search = fofaitLocaliteService.search(search);
        Assert.assertNotNull(search.getSearchResults());
        Assert.assertEquals(3, search.getSize());
    }
}