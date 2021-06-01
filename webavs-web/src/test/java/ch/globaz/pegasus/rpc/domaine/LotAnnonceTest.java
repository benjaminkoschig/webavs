package ch.globaz.pegasus.rpc.domaine;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.exception.PegasusBusinessDomainException;

public class LotAnnonceTest {

    @Test
    public void testAddToDayAtDateEnvoi() throws Exception {
        LotAnnonceRpc lotAnnonce = new LotAnnonce();
        lotAnnonce.addTodayAtDateEnvoi();
        assertThat(lotAnnonce.getDateEnvoi().getSwissValue()).isEqualTo(Date.now().getSwissValue());
    }

    @Test(expected = PegasusBusinessDomainException.class)
    public void testAddToDayAtDateEnvoiWithValue() throws Exception {
        LotAnnonceRpc lotAnnonce = new LotAnnonce(Date.now(), EtatLot.ERROR, "2", TypeLot.INITIAL);
        lotAnnonce.addTodayAtDateEnvoi();
    }

}
