package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.InitTestForBd;
import ch.globaz.pegasus.businessimpl.tests.pcAccordee.DonneeForDonneeFinanciere;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.DonneeUtilsForTestRetroAndOV;

public class ValiderDecisionAcLoaderTestCase extends InitTestForBd {

    @Ignore
    @Test
    public void verif1CasSimpleInitial() throws Exception {

        DonneeForDonneeFinanciere donnee = DonneeUtilsForTestRetroAndOV.createDossierAndDemandeAndDroitInitial(
                "756.0000.1615.50", DonneeUtilsForTestRetroAndOV.getDate());

        DonneeUtilsForTestRetroAndOV.createRentes(donnee.getDroit(), "400");

        PegasusServiceLocator.getDroitService().calculerDroit(
                donnee.getDroit().getSimpleVersionDroit().getIdVersionDroit(), true, null);
        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(donnee.getDroit());

        ValiderDecisionAcLoader loader = new ValiderDecisionAcLoader();
        ValiderDecisionAcData data = loader.load(donnee.getDroit().getSimpleVersionDroit().getIdVersionDroit());
        Assert.assertNotNull(data.getDateProchainPaiement());
        Assert.assertNotNull(data.getIdDemande());
        Assert.assertNotNull(data.getCurrentUserId());
        Assert.assertNotNull(data.getIdDroit());
        Assert.assertNotNull(data.getNoVersionDroit());
        Assert.assertNotNull(data.getDecisionsApresCalcul());
        Assert.assertNotNull(data.getPcasNew());
        Assert.assertEquals(2, data.getPcasNew().size());

        Assert.assertNotNull(data.getRequerant());
        Assert.assertEquals("756.0000.1615.50", data.getRequerant().getPersonneEtendue().getNumAvsActuel());
        Assert.assertNotNull(data.getSimpleDemande());
        Assert.assertNotNull(data.getSimpleVersionDroitNew());

        // DonneeUtilsForTestRetroAndOV.validerToutesLesDesionsionApresCalcule(donnee.getDroit());
    }

}
