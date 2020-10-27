package ch.globaz.pegasus.business.constantes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class TypeDeplafonnement {

    public static final Map<String, String> mapVariableMetierDeplafonnement;


    static {
        Map<String, String> map = new HashMap<>();
        map.put(IPCVariableMetier.CS_DEPLAFONNEMENT_SEULE_STUDIO, IPCVariableMetier.CS_REFORME_DEPLAFONNEMENT_SEULE_STUDIO);
        map.put(IPCVariableMetier.CS_DEPLAFONNEMENT_SEULE_2_25_PIECES, IPCVariableMetier.CS_REFORME_DEPLAFONNEMENT_SEULE_2_25_PIECES);
        map.put(IPCVariableMetier.CS_DEPLAFONNEMENT_SEULE_3_PLUS_PIECES, IPCVariableMetier.CS_REFORME_DEPLAFONNEMENT_SEULE_3_PLUS_PIECES);
        map.put(IPCVariableMetier.CS_DEPLAFONNEMENT_COUPLE_STUDIO, IPCVariableMetier.CS_REFORME_DEPLAFONNEMENT_COUPLE_STUDIO);
        map.put(IPCVariableMetier.CS_DEPLAFONNEMENT_COUPLE_2_25_PIECES, IPCVariableMetier.CS_REFORME_DEPLAFONNEMENT_COUPLE_2_25_PIECES);
        map.put(IPCVariableMetier.CS_DEPLAFONNEMENT_COUPLE_3_PLUS_PIECES, IPCVariableMetier.CS_REFORME_DEPLAFONNEMENT_COUPLE_3_PLUS_PIECES);
        mapVariableMetierDeplafonnement = Collections.unmodifiableMap(map);
    }


}
