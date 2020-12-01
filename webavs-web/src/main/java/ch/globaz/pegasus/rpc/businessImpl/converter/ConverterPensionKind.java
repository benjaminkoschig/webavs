package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCIJAI;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;

public class ConverterPensionKind {
    private static final Logger LOG = LoggerFactory.getLogger(ConverterPensionKind.class);
    private static final Map<String, Integer> returnValueOnCS;
    static {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_10, 10);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_13, 13);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_14, 14);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_15, 15);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_16, 16);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_20, 20);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_23, 23);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_24, 24);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_25, 25);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_26, 26);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_33, 33);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_34, 34);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_35, 35);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_45, 45);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_50, 50);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_54, 54);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_55, 55);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_70, 70);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_74, 74);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_75, 75);
        // ajout aux spec, mapping spécial anciens codes
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_56, 54);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_71, 70);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_72, 70);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_76, 74);

        aMap.put(IPCApiAvsAi.CS_TYPE_API_81, 81);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_82, 82);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_83, 83);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_84, 84);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_88, 88);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_91, 91);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_92, 92);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_93, 93);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_85, 85);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_86, 86);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_87, 87);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_89, 89);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_94, 94);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_95, 95);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_96, 96);
        aMap.put(IPCApiAvsAi.CS_TYPE_API_97, 97);
        // 991 = Sans rente vieillesse (AV)
        aMap.put(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_VIEILLESSE, 991);
        // 992 = Sans rente de survivant (AS)
        aMap.put(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_SURVIVANT, 992);
        // 993 = Sans rente AI
        aMap.put(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_INVALIDITE, 993);
        // 994 = Indemnités journalières
        aMap.put(IPCIJAI.CS_TYPE_DONNEE_FINANCIERE,994);
        // 999 = Pas de rente
        aMap.put(IPCRenteAvsAi.CS_TYPE_SANS_RENTE, 999);
        returnValueOnCS = Collections.unmodifiableMap(aMap);
    }
    private static final Map<String, Integer> notSupportedCS;
    static {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_51, 51);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_52, 52);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_53, 53);

        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_73, 73);

        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_11, 11);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_12, 12);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_21, 21);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_22, 22);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_36, 36);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_43, 43);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_44, 44);
        aMap.put(IPCRenteAvsAi.CS_TYPE_RENTE_46, 46);
        notSupportedCS = Collections.unmodifiableMap(aMap);
    }

    private static final List<String> rentAI = new ArrayList<>(Arrays.asList(
            IPCRenteAvsAi.CS_TYPE_RENTE_50,
            IPCRenteAvsAi.CS_TYPE_RENTE_54,
            IPCRenteAvsAi.CS_TYPE_RENTE_55,
            IPCRenteAvsAi.CS_TYPE_RENTE_70,
            IPCRenteAvsAi.CS_TYPE_RENTE_74,
            IPCRenteAvsAi.CS_TYPE_RENTE_75,
            IPCRenteAvsAi.CS_TYPE_SANS_RENTE_INVALIDITE
            ));

    public static boolean isRentAi(String rente) {
        return rentAI.contains(rente);
    };

    public static Integer convert(String typeRenteCS) {
        if (typeRenteCS.isEmpty()) {
            return returnValueOnCS.get(IPCRenteAvsAi.CS_TYPE_SANS_RENTE);
        } else {
            if (returnValueOnCS.containsKey(typeRenteCS)) {
                return returnValueOnCS.get(typeRenteCS);
            } else {
                String messageCode = "[ Can't find PensionType value for SystemCode" + typeRenteCS + "]";
                if (notSupportedCS.containsKey(typeRenteCS)) {
                    messageCode = notSupportedCS.get(typeRenteCS).toString();
                    LOG.info("L'utilisation de ce type de rente est obsolète : {}", messageCode);
                } else {
                    LOG.error(
                            "Le CodeSysteme {} ne correspond à aucun type de rente connu, vérifiez l'intégrité de données",
                            typeRenteCS);
                }
                throw new RpcBusinessException("pegasus.rpc.converter.pensionKing.introuvable", messageCode);
            }
        }
    }

}
