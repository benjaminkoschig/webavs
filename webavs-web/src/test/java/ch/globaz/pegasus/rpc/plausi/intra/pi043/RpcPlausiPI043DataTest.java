package ch.globaz.pegasus.rpc.plausi.intra.pi043;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Montant;

public class RpcPlausiPI043DataTest {

    @Test
    public void testIsValide() throws Exception {
        RpcPlausiPI043Data plausi = new RpcPlausiPI043Data(null);
        plausi.FC20 = new Montant(1);
        plausi.FC12 = new Montant(0);
        assertThat(plausi.isValide()).isFalse();
        plausi.FC20 = new Montant(0);
        assertThat(plausi.isValide()).isTrue();
        plausi.FC12 = new Montant(1);
        assertThat(plausi.isValide()).isTrue();
    }

}
