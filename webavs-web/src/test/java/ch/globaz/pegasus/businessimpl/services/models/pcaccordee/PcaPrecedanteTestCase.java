package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import org.junit.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Periode;

public class PcaPrecedanteTestCase {

    @Test
    public void testResolveDateDebutMinFinMaxFromNewPca() throws Exception {
        GroupePeriodes periodes = new GroupePeriodes();
        periodes.add(new Periode("01.2012", "04.2012"));
        periodes.add(new Periode("01.2012", "04.2012"));
        periodes.add(new Periode("05.2012", "06.2012"));
        periodes.add(new Periode("01.01.2011", "01.12.2011"));
        Assert.assertEquals("06.2012", PcaPrecedante.resolveDateDebutMinDateFinMax(periodes).getDateFin());
    }

    @Test
    public void testResolveDateDebutMinFinMaxFromNewPcaWithDateFinNull() throws Exception {
        GroupePeriodes periodes = new GroupePeriodes();
        periodes.add(new Periode("01.2012", "04.2012"));
        periodes.add(new Periode("05.2012", ""));
        Assert.assertEquals("12.2012", PcaPrecedante.resolveDateDebutMinDateFinMax(periodes).getDateFin());
    }

}
