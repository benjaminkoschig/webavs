package globaz.naos.util.ide;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.admin.bfs.xmlns.bfs_5051_000103._1.DebtCollectionType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFaillite103ConverterTest {

    @Test
    public void testFormatdata() throws Exception {
        DebtCollectionType mockMsgFaillite = new DebtCollectionType();
        IDEDataBean dataBean = IDEFaillite103Converter.formatdata(mockMsgFaillite);
        assertTrue(dataBean != null);
        assertTrue(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE.equals(dataBean.getTypeAnnonceIde()));
    }

}
