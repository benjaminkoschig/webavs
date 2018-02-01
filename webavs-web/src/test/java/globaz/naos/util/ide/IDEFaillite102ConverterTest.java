package globaz.naos.util.ide;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.admin.bfs.xmlns.bfs_5051_000102._1.DebtRestructuringAgreementType;
import globaz.naos.util.IDEDataBean;

public class IDEFaillite102ConverterTest {

    @Test
    public void testFormatdata() throws Exception {
        DebtRestructuringAgreementType mockMsgFaillite = new DebtRestructuringAgreementType();
        IDEDataBean dataBean = IDEFaillite102Converter.formatdata(mockMsgFaillite);
        assertTrue(dataBean != null);
    }

}
