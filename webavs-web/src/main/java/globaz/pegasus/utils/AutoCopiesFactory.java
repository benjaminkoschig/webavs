package globaz.pegasus.utils;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.decision.IPCAutoCopies;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;

/**
 * Factory retournant les copies à générer automatiquemnet pour les décision Retourne une instance de
 * SimpleCopiesDecision, préparamèté avec les valeurs demandés
 * 
 * @author SCE
 * 
 */
public class AutoCopiesFactory {
    private final static Map<IPCAutoCopies.TYPE_COPIE, SimpleCopiesDecision> autoCopies = new HashMap<IPCAutoCopies.TYPE_COPIE, SimpleCopiesDecision>();

    static {
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.AGENCES_COMMUNALES_AVS, new SimpleCopiesDecision(
                true, true, true, true, false, true, true, true, true, true));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.PRO_SENECTUTE_JU, new SimpleCopiesDecision(false,
                false, false, false, false, true, false, false, false, false));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_PG, new SimpleCopiesDecision(true,
                true, true, true, true, true, true, true, true, true));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.SERVICE_SOCIAL_SANS_PG, new SimpleCopiesDecision(
                true, true, true, true, true, true, true, true, true, true));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.HOME_SANS_PCAL, new SimpleCopiesDecision(true, true,
                true, false, false, false, true, true, true, false));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.HOME_PCAL, new SimpleCopiesDecision(true, true, true,
                false, false, true, true, true, true, false));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.HOME_PARTIEL_SANS_PCAL, new SimpleCopiesDecision(true, true,
                true, false, true, false, true, true, true, false));
        AutoCopiesFactory.autoCopies.put(IPCAutoCopies.TYPE_COPIE.HOME_PARTIEL, new SimpleCopiesDecision(true, true, true,
                false, true, true, true, true, true, false));
    }

    public static SimpleCopiesDecision getAutoCopies(IPCAutoCopies.TYPE_COPIE type) {
        return AutoCopiesFactory.autoCopies.get(type);
    };
}
