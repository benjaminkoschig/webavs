package globaz.naos.util.ide;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.admin.bfs.xmlns.bfs_5051_000101._1.BankruptcyType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFaillite101ConverterTest {

    @Test
    public void testFormatdata() throws Exception {
        BankruptcyType mockMsgFaillite = new BankruptcyType();
        IDEDataBean dataBean = IDEFaillite101Converter.formatdata(mockMsgFaillite);
        assertTrue(dataBean != null);
        assertTrue(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE.equals(dataBean.getTypeAnnonceIde()));
    }

}
