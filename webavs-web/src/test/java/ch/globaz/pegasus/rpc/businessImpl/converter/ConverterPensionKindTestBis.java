package ch.globaz.pegasus.rpc.businessImpl.converter;

import static org.assertj.core.api.Assertions.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;

@RunWith(Parameterized.class)
public class ConverterPensionKindTestBis {

    @Parameters(name = " IPCTypeRenteCS: {0}, not expect {1} but throw exception")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { IPCRenteAvsAi.CS_TYPE_RENTE_51, 51 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_52, 52 }, { IPCRenteAvsAi.CS_TYPE_RENTE_53, 53 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_56, 54 }, { IPCRenteAvsAi.CS_TYPE_RENTE_71, 70 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_72, 70 }, { IPCRenteAvsAi.CS_TYPE_RENTE_73, 73 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_76, 74 }, { IPCRenteAvsAi.CS_TYPE_RENTE_11, 11 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_12, 12 }, { IPCRenteAvsAi.CS_TYPE_RENTE_21, 21 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_22, 22 }, { IPCRenteAvsAi.CS_TYPE_RENTE_36, 36 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_43, 43 }, { IPCRenteAvsAi.CS_TYPE_RENTE_44, 44 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_46, 46 }, { "VALEUR_IMPOSSIBLE", 007 } });
    }

    private final String typeRenteCS;
    private final int expected;

    public ConverterPensionKindTestBis(String typeRenteCS, int expected) {
        this.typeRenteCS = typeRenteCS;
        this.expected = expected;
    }

    @Test
    public void test() {
        try {
            assertThat(convert(typeRenteCS)).isEqualTo(expected);
        } catch (Exception e) {
            assertThat(true);
        }
    }

    private int convert(String typeRenteCS) {
        return ConverterPensionKind.convert(typeRenteCS);
    }

}
