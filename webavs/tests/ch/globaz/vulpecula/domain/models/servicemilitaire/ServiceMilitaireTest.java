package ch.globaz.vulpecula.domain.models.servicemilitaire;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;

public class ServiceMilitaireTest {
    private ServiceMilitaire serviceMilitaire;

    @Before
    public void setUp() {
        serviceMilitaire = new ServiceMilitaire();
    }

    @Test
    public void getMontantCongePaye_SalaireHoraire_27_30TauxCP13_64_ShouldBe3_72() {
        serviceMilitaire.setSalaireHoraire(new Montant(27.30));
        serviceMilitaire.setTauxCP(new Taux(13.64));
        assertThat(serviceMilitaire.getMontantCongePaye(), is(new Montant(3.72)));
    }

    @Test
    public void getMontantGratification_SalaireHoraire27_30TauxCP_13_64_Taux8_33_ShouldBe2_58() {
        serviceMilitaire.setSalaireHoraire(new Montant(27.30));
        serviceMilitaire.setTauxCP(new Taux(13.64));
        serviceMilitaire.setTauxGratification(new Taux(8.33));

        assertEquals(serviceMilitaire.getMontantGratification(), new Montant(2.58));
    }

    @Test
    public void getTotalSalaire() {
        serviceMilitaire.setSalaireHoraire(new Montant(27.30));
        serviceMilitaire.setTauxCP(new Taux(13.64));
        serviceMilitaire.setTauxGratification(new Taux(8.33));
        serviceMilitaire.setNbHeuresParJour(90.70);

        assertEquals(serviceMilitaire.getTotalSalaire(), new Montant(2710.10));
    }

    @Test
    public void getCouvertureAPG() {
        serviceMilitaire.setSalaireHoraire(new Montant(27.30));
        serviceMilitaire.setTauxCP(new Taux(13.64));
        serviceMilitaire.setTauxGratification(new Taux(8.33));
        serviceMilitaire.setNbHeuresParJour(90.70);
        serviceMilitaire.setCouvertureAPG(new Taux(50));

        assertEquals(serviceMilitaire.getMontantCouvertureAPG(), new Montant(1355.05));
    }
}
