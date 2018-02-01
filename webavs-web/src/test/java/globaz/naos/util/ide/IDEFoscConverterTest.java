package globaz.naos.util.ide;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.admin.bfs.xmlns.bfs_5050_000101._1.SHABMsgType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFoscConverterTest {

    @Test
    public void testFormatdata() throws Exception {
        SHABMsgType mockMsgFosc = new SHABMsgType();
        IDEDataBean dataBean = IDEFoscConverter.formatdata(mockMsgFosc);
        assertTrue(dataBean != null);
        assertTrue(CodeSystem.TYPE_ANNONCE_IDE_FOSC.equals(dataBean.getTypeAnnonceIde()));
    }

}
