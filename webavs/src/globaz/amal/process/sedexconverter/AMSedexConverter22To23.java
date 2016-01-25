package globaz.amal.process.sedexconverter;

import java.util.HashMap;
import java.util.Map;

public class AMSedexConverter22To23 {
    private static final Map<String, String> URI_NAMESPACE_CONCORDANCE_MAP = initUriNamespaceConcordanceMap();

    private static Map<String, String> initUriNamespaceConcordanceMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pv-common/2", "pv-common/3");
        map.put("pv-5201-000101/2", "pv-5201-000101/3");
        map.put("pv-5201-000201/2", "pv-5201-000201/3");
        map.put("pv-5202-000401/2", "pv-5202-000401/3");
        map.put("pv-5203-000501/2", "pv-5203-000501/3");
        map.put("pv-5211-000102/2", "pv-5211-000102/3");
        map.put("pv-5211-000103/2", "pv-5211-000103/3");
        map.put("pv-5211-000202/2", "pv-5211-000202/3");
        map.put("pv-5211-000203/2", "pv-5211-000203/3");
        map.put("pv-5211-000301/2", "pv-5211-000301/3");
        map.put("pv-5212-000402/2", "pv-5212-000402/3");
        map.put("pv-5213-000601/2", "pv-5213-000601/3");
        map.put("pv-5214-000701/2", "pv-5214-000701/3");
        return map;
    }

    /**
     * Converti un message sedex version 2.2 en version 2.3
     * 
     * @param sedexMsgToConvert
     * @return
     */
    public static String convert(String sedexMsgToConvert) {
        String newSedexMsg = new String(sedexMsgToConvert);
        for (Map.Entry<String, String> concordanceEntry : URI_NAMESPACE_CONCORDANCE_MAP.entrySet()) {
            String concordanceV2 = concordanceEntry.getKey();
            String concordanceV3 = concordanceEntry.getValue();

            // si l'élément recherché est trouvé on le remplace par sa concordance (en fonction de la map de
            // concordance)
            if (newSedexMsg.contains(concordanceV2)) {
                newSedexMsg = newSedexMsg.replaceAll(concordanceV2, concordanceV3);
            }
        }
        return newSedexMsg;
    }
}
