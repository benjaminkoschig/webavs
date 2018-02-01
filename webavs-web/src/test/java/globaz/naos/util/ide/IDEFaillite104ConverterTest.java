package globaz.naos.util.ide;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.admin.bfs.xmlns.bfs_5051_000104._1.NoticeToObligeesType;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.IDEDataBean;

public class IDEFaillite104ConverterTest {

    @Test
    public void testFormatdata() throws Exception {
        NoticeToObligeesType mockMsgFaillite = new NoticeToObligeesType();
        IDEDataBean dataBean = IDEFaillite104Converter.formatdata(mockMsgFaillite);
        assertTrue(dataBean != null);
        assertTrue(CodeSystem.TYPE_ANNONCE_IDE_FAILLITE.equals(dataBean.getTypeAnnonceIde()));
    }

}
