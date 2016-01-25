package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import org.junit.Assert;
import org.junit.Test;

public class ComptabilisationLoaderTestCase {

    @Test
    public void testComptabilisationLoader() throws JadeApplicationException, JadePersistenceException, JAException {
        ComptabilisationLoader loader = new ComptabilisationLoader("1", "39", "40", "libelleJournal", "01.2012",
                "02.2012");
    }

    @Test
    public void testGetData() throws JadeApplicationException, JadePersistenceException, JAException {
        ComptabilisationLoader loader = new ComptabilisationLoader("1", "39", "40", "libelleJournal", "01.2012",
                "02.2012");
        Assert.assertEquals("39", loader.getData().getIdOrganeExecution());
        Assert.assertEquals("40", loader.getData().getNumeroOG());
        Assert.assertEquals("libelleJournal", loader.getData().getLibelleJournal());
        Assert.assertEquals("01.01.2012", loader.getData().getDateValeur().toStr("."));
        Assert.assertEquals("01.02.2012", loader.getData().getDateEchance().toStr("."));
    }

}
