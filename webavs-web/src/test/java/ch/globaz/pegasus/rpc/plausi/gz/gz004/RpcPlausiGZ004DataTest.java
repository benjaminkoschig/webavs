package ch.globaz.pegasus.rpc.plausi.gz.gz004;

import static org.fest.assertions.api.Assertions.*;
import java.util.Arrays;
import org.junit.Test;

public class RpcPlausiGZ004DataTest {

    @Test
    public void testIsValide() throws Exception {
        RpcPlausiGZ004Data plausi = new RpcPlausiGZ004Data(null);
        plausi.loadList(Arrays.asList("19999", "19999"));
        assertThat(plausi.isValide()).isFalse();
        plausi.loadList(Arrays.asList("19999", "19998"));
        assertThat(plausi.isValide()).isTrue();
    }
}
