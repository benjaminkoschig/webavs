package ch.globaz.pegasus.rpc.plausi.intra.pi044;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;

public class RpcPlausiPI044DataTest {

    @Test
    public void testIsValide() throws Exception {
        RpcPlausiPI044Data plausi = new RpcPlausiPI044Data(null);
        plausi.FC20 = new Montant(1);
        plausi.FC12 = new Montant(0);
        assertThat(plausi.isValide()).isFalse();
    }
}
