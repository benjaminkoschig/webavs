package ch.globaz.pegasus.rpc.businessImpl.converter;

import static org.fest.assertions.api.Assertions.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;

@RunWith(Parameterized.class)
public class ConverterPensionKindTest {

    @Parameters(name = " IPCTypeRenteCS: {0}, Expected : {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { IPCRenteAvsAi.CS_TYPE_RENTE_10, 10 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_13, 13 }, { IPCRenteAvsAi.CS_TYPE_RENTE_14, 14 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_15, 15 }, { IPCRenteAvsAi.CS_TYPE_RENTE_16, 16 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_20, 20 }, { IPCRenteAvsAi.CS_TYPE_RENTE_23, 23 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_24, 24 }, { IPCRenteAvsAi.CS_TYPE_RENTE_25, 25 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_26, 26 }, { IPCRenteAvsAi.CS_TYPE_RENTE_33, 33 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_34, 34 }, { IPCRenteAvsAi.CS_TYPE_RENTE_35, 35 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_45, 45 }, { IPCRenteAvsAi.CS_TYPE_RENTE_50, 50 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_54, 54 }, { IPCRenteAvsAi.CS_TYPE_RENTE_55, 55 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_70, 70 }, { IPCRenteAvsAi.CS_TYPE_RENTE_74, 74 },
                { IPCRenteAvsAi.CS_TYPE_RENTE_75, 75 }, { IPCApiAvsAi.CS_TYPE_API_81, 81 },
                { IPCApiAvsAi.CS_TYPE_API_82, 82 }, { IPCApiAvsAi.CS_TYPE_API_83, 83 },
                { IPCApiAvsAi.CS_TYPE_API_84, 84 }, { IPCApiAvsAi.CS_TYPE_API_88, 88 },
                { IPCApiAvsAi.CS_TYPE_API_91, 91 }, { IPCApiAvsAi.CS_TYPE_API_92, 92 },
                { IPCApiAvsAi.CS_TYPE_API_93, 93 }, { IPCApiAvsAi.CS_TYPE_API_85, 85 },
                { IPCApiAvsAi.CS_TYPE_API_86, 86 }, { IPCApiAvsAi.CS_TYPE_API_87, 87 },
                { IPCApiAvsAi.CS_TYPE_API_89, 89 }, { IPCApiAvsAi.CS_TYPE_API_95, 95 },
                { IPCApiAvsAi.CS_TYPE_API_96, 96 }, { IPCApiAvsAi.CS_TYPE_API_97, 97 },
                // 991 = Sans rente vieillesse (AV)
                { IPCRenteAvsAi.CS_TYPE_SANS_RENTE_VIEILLESSE, 991 },
                // 992 = Sans rente de survivant (AS)
                { IPCRenteAvsAi.CS_TYPE_SANS_RENTE_SURVIVANT, 992 },
                // 993 = Sans rente AI
                { IPCRenteAvsAi.CS_TYPE_SANS_RENTE_INVALIDITE, 993 },
                // 994 = Indemnités journalières
                // TODO, K141111_003

                // 999 = Pas de rente
                { IPCRenteAvsAi.CS_TYPE_SANS_RENTE, 999 } });
    }

    private final String typeRenteCS;
    private final int expected;

    public ConverterPensionKindTest(String typeRenteCS, int expected) {
        this.typeRenteCS = typeRenteCS;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(convert(typeRenteCS)).isEqualTo(expected);
    }

    private int convert(String typeRenteCS) {
        return ConverterPensionKind.convert(typeRenteCS);
    }

}
