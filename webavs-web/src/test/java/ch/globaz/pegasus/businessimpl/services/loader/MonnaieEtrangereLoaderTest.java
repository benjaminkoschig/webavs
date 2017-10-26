package ch.globaz.pegasus.businessimpl.services.loader;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangere;

public class MonnaieEtrangereLoaderTest {
    @Test
    public void testConvert() throws Exception {
        SimpleMonnaieEtrangere simpleMonnaieEtrangere = new SimpleMonnaieEtrangere();

        simpleMonnaieEtrangere.setCsTypeMonnaie(MonnaieEtrangereType.FRANC_SUISSE.getValue());
        simpleMonnaieEtrangere.setDateDebut("01.2014");
        simpleMonnaieEtrangere.setDateFin(null);
        simpleMonnaieEtrangere.setId("1");
        simpleMonnaieEtrangere.setTaux("1.22");

        MonnaieEtrangereLoader loader = new MonnaieEtrangereLoader();
        MonnaieEtrangere monnaieEtrangere = loader.convert(simpleMonnaieEtrangere);

        assertEquals(MonnaieEtrangereType.FRANC_SUISSE, monnaieEtrangere.getType());
        assertEquals(new Date("01.2014"), monnaieEtrangere.getDateDebut());
        assertEquals(null, monnaieEtrangere.getDateFin());
        assertEquals("1", monnaieEtrangere.getId());
        assertEquals(new Taux(1.22), monnaieEtrangere.getTaux());
    }
}
