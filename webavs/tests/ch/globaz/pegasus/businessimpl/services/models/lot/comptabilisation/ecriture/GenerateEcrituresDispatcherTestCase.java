package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

public class GenerateEcrituresDispatcherTestCase {

    private GenerateEcrituresDispatcher newDispatcher(int dom2rRequerant, int dom2rConjoint, int standardRequerant,
            int standardConjoint) throws JadeApplicationException {
        PrestationOvDecompte decompte = new PrestationOvDecompte();
        MontantDispo montantDispo = new MontantDispo(new BigDecimal(dom2rRequerant), new BigDecimal(dom2rConjoint),
                new BigDecimal(standardRequerant), new BigDecimal(standardConjoint));
        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        GenerateEcrituresDispatcher dispatcher = this.newDispatcher(decompte, montantDispo, ovs);
        return dispatcher;
    }

    private GenerateEcrituresDispatcher newDispatcher(PrestationOvDecompte decompte, MontantDispo montantDispo,
            List<OrdreVersement> ovs) throws JadeApplicationException {
        GenerateEcrituresDispatcher dispatcher = new GenerateEcrituresDispatcher(decompte, montantDispo, ovs) {

            @Override
            protected void addOperation(OrdreVersement ov, BigDecimal montant, CompteAnnexeSimpleModel compteAnnexe)
                    throws ComptabiliserLotException {
            }

            @Override
            protected BigDecimal resolveMontantOv(SimpleOrdreVersement ov) {
                // TODO Auto-generated method stub
                return null;
            }

        };

        return dispatcher;
    }

    @Test
    public void testGetMontantsDisponible() throws JadeApplicationException {
        GenerateEcrituresDispatcher dispatcher = this.newDispatcher(10, 20, 30, 40);
        Assert.assertEquals(new BigDecimal(10), dispatcher.getMontantsDisponible().getDom2RRequerant());
        Assert.assertEquals(new BigDecimal(20), dispatcher.getMontantsDisponible().getDom2RConjoint());
        Assert.assertEquals(new BigDecimal(30), dispatcher.getMontantsDisponible().getStandardRequerant());
        Assert.assertEquals(new BigDecimal(40), dispatcher.getMontantsDisponible().getStandarConjoint());
    }

    @Test
    public void testHasConjointByDom2RTrue() throws JadeApplicationException {
        GenerateEcrituresDispatcher dispatcher = this.newDispatcher(10, 20, 0, 0);
        Assert.assertTrue(dispatcher.hasConjoint());
    }

    @Test
    public void testHasConjointFalse() throws JadeApplicationException {
        GenerateEcrituresDispatcher dispatcher = this.newDispatcher(0, 0, 30, 40);
        Assert.assertFalse(dispatcher.hasConjoint());
    }

}
