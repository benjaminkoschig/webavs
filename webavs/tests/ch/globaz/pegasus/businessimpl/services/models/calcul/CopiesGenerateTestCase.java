package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalcul;
import ch.globaz.pegasus.business.services.models.calcul.CalculPersistanceService;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.factory.droit.SimpleVersionDroitFactory;
import ch.globaz.pegasus.factory.pca.CalculPcaReplaceFactory;

public class CopiesGenerateTestCase {
    private CopiesGenerate generate;
    private List<CalculPcaReplace> list;

    private void generate(JadeAbstractModel[] t) throws DroitException {
        SimpleVersionDroit droit = SimpleVersionDroitFactory.generateVersion2();
        generate = newCopies();

        CalculPcaReplaceSearch search = new CalculPcaReplaceSearch();
        search.setSearchResults(t);
        // this.list = this.generate.generate(search, "01.05.2013", droit);
    }

    private CopiesGenerate newCopies() {
        CopiesGenerate generate = new CopiesGenerate(new CalculPersistanceService() {

            @Override
            public void clearPCAccordee(Droit droit) throws JadePersistenceException, PCAccordeeException,
                    JadeApplicationServiceNotAvailableException, JadeApplicationException {
            }

            @Override
            public TupleDonneeRapport deserialiseDonneesCcXML(String donneeSerialisee) {
                return null;
            }

            @Override
            public void recupereAnciensPCAccordee(String dateDebut, Droit droit,
                    Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws PCAccordeeException,
                    JadePersistenceException, CalculException {
            }

            @Override
            public List<PCAccordeePlanCalcul> sauvePCAccordee(Droit droit, PeriodePCAccordee periode,
                    CalculPcaReplaceSearch anciennesPCAccordees) throws PCAccordeeException,
                    JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
                return null;
            }

            @Override
            public void sauvePCAccordeeToCopie(CalculPcaReplace pcaToSave) throws PCAccordeeException,
                    JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
            }

            @Override
            public String serialiseDonneesCcXML(CalculComparatif cc) {
                return null;
            }
        });
        return generate;
    }

    @Test
    @Ignore
    public void testGenerateAvecUneCopiePcaCourant() throws Exception {
        JadeAbstractModel[] t = new CalculPcaReplace[4];
        t[0] = CalculPcaReplaceFactory.generateInitial("01.2010", "12.2010");
        t[1] = CalculPcaReplaceFactory.generateInitial("01.2011", "01.2011");
        t[2] = CalculPcaReplaceFactory.generateInitial("01.2012", "01.2013");
        t[3] = CalculPcaReplaceFactory.generateInitial("01.2013", null); // Pca Courrante

        generate(t);
        Assert.assertEquals(1, list.size());
        Assert.assertFalse(generate.getHasMoreCopieHasExpected());
    }

    @Test
    @Ignore
    public void testGenerateCoupleSeparer2Copies() throws Exception {
        JadeAbstractModel[] t = new CalculPcaReplace[5];
        t[0] = CalculPcaReplaceFactory.generateInitial("01.2010", "12.2010");
        t[1] = CalculPcaReplaceFactory.generateInitial("01.2011", "01.2011");
        t[2] = CalculPcaReplaceFactory.generateInitial("01.2012", "01.2013");
        t[3] = CalculPcaReplaceFactory.generateForRequerant("01.2013", null, "1");
        t[4] = CalculPcaReplaceFactory.generateForConjoint("01.2013", null, "2");

        generate(t);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("1", list.get(0).getId());
        Assert.assertEquals("2", list.get(1).getId());
        Assert.assertFalse(generate.getHasMoreCopieHasExpected());

    }

    @Test
    public void testGenerateDroitInitial() throws Exception {

        SimpleVersionDroit droit = SimpleVersionDroitFactory.generateVersionInitial();
        CopiesGenerate generate = newCopies();

        JadeAbstractModel[] t = new CalculPcaReplace[1];
        t[0] = CalculPcaReplaceFactory.generateInitial("01.2013", null);

        CalculPcaReplaceSearch search = new CalculPcaReplaceSearch();
        search.setSearchResults(t);
        List<CalculPcaReplace> list = generate.generate(search, "01.05.2013", droit);
        Assert.assertEquals(0, list.size());
        Assert.assertFalse(generate.getHasMoreCopieHasExpected());
    }

    @Test
    @Ignore
    public void testGetHasMoreCopieHasExpected() throws Exception {
        JadeAbstractModel[] t = new CalculPcaReplace[2];
        t[0] = CalculPcaReplaceFactory.generateForRequerant("01.2013", null, "1");
        t[1] = CalculPcaReplaceFactory.generateForRequerant("01.2012", null, "2");

        generate(t);
        Assert.assertTrue(generate.getHasMoreCopieHasExpected());
    }
}
