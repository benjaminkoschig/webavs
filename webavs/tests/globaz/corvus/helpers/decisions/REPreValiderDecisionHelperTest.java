package globaz.corvus.helpers.decisions;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteInvalidite;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.prestation.domaine.CodePrestation;

public class REPreValiderDecisionHelperTest {

    @Test
    public void testRetrievePeriodeRetro() throws Exception {
        DemandeRente demande = new DemandeRenteInvalidite();

        RenteAccordee rente1 = new RenteAccordee();
        rente1.setId(1l);
        rente1.setCodePrestation(CodePrestation.CODE_50);
        rente1.setMoisDebut("01.2000");
        rente1.setMoisFin("12.2010");

        demande.setRentesAccordees(Arrays.asList(rente1));

        Assert.assertEquals(new Periode("01.2000", "12.2010"),
                REPreValiderDecisionHelper.retrievePeriodeRetro(demande, "03.2014"));

        RenteAccordee rente2 = new RenteAccordee();
        rente2.setId(2l);
        rente2.setCodePrestation(CodePrestation.CODE_50);
        rente2.setMoisDebut("06.2011");
        rente2.setMoisFin("12.2013");

        demande.setRentesAccordees(Arrays.asList(rente1, rente2));

        Assert.assertEquals(new Periode("01.2000", "12.2013"),
                REPreValiderDecisionHelper.retrievePeriodeRetro(demande, "03.2014"));

        RenteAccordee rente3 = new RenteAccordee();
        rente3.setId(3l);
        rente3.setCodePrestation(CodePrestation.CODE_50);
        rente3.setMoisDebut("01.2014");

        demande.setRentesAccordees(Arrays.asList(rente1, rente2, rente3));

        Assert.assertEquals(new Periode("01.2000", "03.2014"),
                REPreValiderDecisionHelper.retrievePeriodeRetro(demande, "03.2014"));

        rente3.setMoisFin("06.2014");
        Assert.assertEquals(new Periode("01.2000", "03.2014"),
                REPreValiderDecisionHelper.retrievePeriodeRetro(demande, "03.2014"));

        // décision future
        RenteAccordee rente4 = new RenteAccordee();
        rente4.setId(4l);
        rente4.setCodePrestation(CodePrestation.CODE_50);
        rente4.setMoisDebut("04.2014");

        demande.setRentesAccordees(Arrays.asList(rente4));
        Assert.assertNull(REPreValiderDecisionHelper.retrievePeriodeRetro(demande, "03.2014"));
    }

}
