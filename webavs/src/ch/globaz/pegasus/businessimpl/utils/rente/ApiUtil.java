package ch.globaz.pegasus.businessimpl.utils.rente;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;

public class ApiUtil {

    public static final String AI = "AI";
    public static final String AVS = "AVS";
    private static final Map<String, String> mapCodeAvsAi = ApiUtil.initializeMapCode();
    private static final Map<String, String> mapCodeIdAvAi = ApiUtil.initializeMapCodeId();

    static {
        // ApiUtil.initializeMapCode();
    }

    /**
     * Permet de définir le type avs ou ai en fonction du code de l'API ATTENTION si rien n'est trouvée la fonction
     * retroune null!
     * 
     * @param code
     * @return
     */
    public static String getTypeRenteAvsOrAi(String code) {
        if (ApiUtil.mapCodeAvsAi.containsKey(code)) {
            return ApiUtil.mapCodeAvsAi.get(code);
        }
        return null;
    }

    /**
     * Permet de définir le type avs ou ai en fonction du code de l'API ATTENTION si rien n'est trouvée la fonction
     * retroune null!
     * 
     * @param code
     * @return
     */
    public static String getTypeRenteAvsOrAiByIdCode(String code) {
        if (ApiUtil.mapCodeIdAvAi.containsKey(code)) {
            return ApiUtil.mapCodeIdAvAi.get(code);
        }
        return null;
    }

    private static Map<String, String> initializeMapCode() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("81", ApiUtil.AI);
        map.put("82", ApiUtil.AI);
        map.put("83", ApiUtil.AI);
        map.put("84", ApiUtil.AI);
        map.put("85", ApiUtil.AVS);
        map.put("86", ApiUtil.AVS);
        map.put("87", ApiUtil.AVS);
        map.put("88", ApiUtil.AI);
        map.put("89", ApiUtil.AVS);
        map.put("91", ApiUtil.AI);
        map.put("92", ApiUtil.AI);
        map.put("93", ApiUtil.AI);
        map.put("94", ApiUtil.AI);
        map.put("95", ApiUtil.AVS);
        map.put("96", ApiUtil.AVS);
        map.put("97", ApiUtil.AVS);
        return map;
    }

    private static Map<String, String> initializeMapCodeId() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(IPCApiAvsAi.CS_TYPE_API_81, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_82, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_83, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_84, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_85, ApiUtil.AVS);
        map.put(IPCApiAvsAi.CS_TYPE_API_86, ApiUtil.AVS);
        map.put(IPCApiAvsAi.CS_TYPE_API_87, ApiUtil.AVS);
        map.put(IPCApiAvsAi.CS_TYPE_API_88, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_89, ApiUtil.AVS);
        map.put(IPCApiAvsAi.CS_TYPE_API_91, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_92, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_93, ApiUtil.AI);
        map.put(IPCApiAvsAi.CS_TYPE_API_95, ApiUtil.AVS);
        map.put(IPCApiAvsAi.CS_TYPE_API_96, ApiUtil.AVS);
        map.put(IPCApiAvsAi.CS_TYPE_API_97, ApiUtil.AVS);
        return map;
    }

}
