package globaz.pegasus.enums;

import com.google.gson.JsonObject;
import globaz.globall.db.BSession;

public enum TypeDivestedWealth {

    /**
     *
     */
    SANS_CONTREPARTIE(1, "TYPE_DIVESTED_WEALTH_NOEQUIVALENT", "noEquivalentCompensation"),
    CONSO_EXCESSIVE(2, "TYPE_DIVESTED_WEALTH_EXCESSIVELY_CONSUME", "excessivelyConsume");

    private int code;
    private String labelKey;
    private String xsdKey;

    private TypeDivestedWealth(int code, String labelKey, String xsdKey) {
        this.code = code;
        this.labelKey = labelKey;
        this.xsdKey = xsdKey;

    }

    public final int getCode() {
        return code;
    }

    public final String getCodeAsString() {
        return String.valueOf(code);
    }

    public final String getLabelKey() {
        return labelKey;
    }

    public final String getXsdKey() {
        return xsdKey;
    }

    /**
     * Retourne le type énuméré {@link TypeDivestedWealth} correspondant au code système passé en paramètre
     *
     * @param code Le code système à tester
     * @return le type énuméré {@link TypeDivestedWealth} correspondant au code système passé en paramètre ou null si pas
     *         trouvé
     */
    public static TypeDivestedWealth fromCode(String code) {
        for (TypeDivestedWealth type : TypeDivestedWealth.values()) {
            if (type.getCodeAsString().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static JsonObject getJsonListTypeDivestedWealth (BSession session) {
        JsonObject json = new JsonObject();
        for(TypeDivestedWealth type : TypeDivestedWealth.values()){
            json.addProperty(session.getLabel(type.getLabelKey()), type.getCodeAsString());
        }
        return json;
    }

}
