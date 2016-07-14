package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementFactory;

public class GeneratePrestationOperationsTestCase {

    private List<OrdreVersementForList> generateListOvForPrestaion(String idPrestation) {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("2"));
        for (OrdreVersementForList ov : ovs) {
            ov.getSimpleOrdreVersement().setIdPrestation(idPrestation);
        }
        return ovs;
    }

    private GeneratePrestationOperations generatePresationEcritures() throws JadeApplicationException {
        GeneratePrestationOperations generate = new GeneratePrestationOperations();
        GeneratePrestationOperations spy = Mockito.spy(generate);
        Operations operations = new Operations();
        Mockito.doReturn(operations)
                .when(spy)
                .generateOperations(Matchers.anyListOf(OrdreVersementForList.class),
                        Matchers.anyListOf(SectionSimpleModel.class), Matchers.anyString(), Matchers.anyString(),
                        Matchers.any(PCLotTypeOperationFactory.class));
        return spy;
    }

    @Test
    public void testGeneratePrestationEcritures() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = generateListOvForPrestaion("1");
        GeneratePrestationOperations generate = generatePresationEcritures();
        List<PrestationOperations> prestationsEcritures = generate.generateAllOperationsPrestations(ovs,
                new ArrayList<SectionSimpleModel>(), new ArrayList<CompteAnnexeSimpleModel>(), "", "",
                PCLotTypeOperationFactory.DECISION);
        PrestationOperations prestationEcritures = prestationsEcritures.get(0);
        Assert.assertNotNull(prestationEcritures);
        Assert.assertNotNull(prestationEcritures.getMontantPresation());
        Assert.assertNotNull(prestationEcritures.getEcritures());
        Assert.assertEquals(new BigDecimal(ovs.get(0).getSimpleOrdreVersement().getMontant()),
                prestationEcritures.getMontantPresation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratePrestationException() throws JadeApplicationException {

        GeneratePrestationOperations generate = new GeneratePrestationOperations();
        List<OrdreVersementForList> ovs = generateListOvForPrestaion("1");
        generate.generateAllOperationsPrestations(ovs, new ArrayList<SectionSimpleModel>(),
                new ArrayList<CompteAnnexeSimpleModel>(), "", "", null);
    }

    @Test
    public void testGenrateAllPrestationsEcritures() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.addAll(generateListOvForPrestaion("1"));
        ovs.addAll(generateListOvForPrestaion("2"));
        ovs.addAll(generateListOvForPrestaion("3"));
        ovs.addAll(generateListOvForPrestaion("4"));
        GeneratePrestationOperations generate = generatePresationEcritures();
        List<PrestationOperations> prestationsEcritures = generate.generateAllOperationsPrestations(ovs,
                new ArrayList<SectionSimpleModel>(), new ArrayList<CompteAnnexeSimpleModel>(), "", "",
                PCLotTypeOperationFactory.DECISION);
        Assert.assertNotNull(prestationsEcritures);
        Assert.assertEquals(4, prestationsEcritures.size());
    }

    @Test
    public void testValuesPreastion() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.addAll(generateListOvForPrestaion("1"));
        GeneratePrestationOperations generate = generatePresationEcritures();
        List<PrestationOperations> prestationsEcritures = generate.generateAllOperationsPrestations(ovs,
                new ArrayList<SectionSimpleModel>(), new ArrayList<CompteAnnexeSimpleModel>(), "", "",
                PCLotTypeOperationFactory.DECISION);

        PrestationOperations prestation = prestationsEcritures.get(0);
        Assert.assertNotNull(prestation.getNom());
        Assert.assertNotNull(prestation.getPrenom());
        Assert.assertNotNull(prestation.getNumAvsRequerant());
        Assert.assertNotNull(prestation.getIdPrestation());
        Assert.assertNotNull(prestation.getMontantPresation());
        Assert.assertEquals(ovs.get(0).getDesignationRequerant1(), prestation.getNom());
        Assert.assertEquals(ovs.get(0).getDesignationRequerant2(), prestation.getPrenom());
        Assert.assertEquals(ovs.get(0).getNumAvs(), prestation.getNumAvsRequerant());
        Assert.assertEquals(ovs.get(0).getSimpleOrdreVersement().getIdPrestation(), prestation.getIdPrestation());
        Assert.assertEquals(new BigDecimal(ovs.get(0).getMontantPresation()), prestation.getMontantPresation());

    }
}
