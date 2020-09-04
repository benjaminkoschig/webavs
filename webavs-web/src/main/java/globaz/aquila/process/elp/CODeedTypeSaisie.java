package globaz.aquila.process.elp;

import globaz.aquila.db.access.batch.COEtapeInfoConfig;

/**
 * Enum des type de saisie.
 */
public enum CODeedTypeSaisie {

    MOBILIER("mv", COEtapeInfoConfig.CS_TYPE_SAISIE_BIENS_MOBILIERS),
    IMMOBILIER("re", COEtapeInfoConfig.CS_TYPE_SAISIE_BIENS_IMMOBILIERS),
    SALAIRE("in", COEtapeInfoConfig.CS_TYPE_SAISIE_SALAIRE);

    private String codeXml;
    private String codeSystem;

    CODeedTypeSaisie(String codeXml, String codeSystem) {
        this.codeXml = codeXml;
        this.codeSystem = codeSystem;
    }

    public static String getCodeSystemFromCodeXml(String codeXml) {
        for (final CODeedTypeSaisie type : CODeedTypeSaisie.values()) {
            if (type.codeXml.equals(codeXml)) {
                return type.codeSystem;
            }
        }
        return null;
    }

    public static String getCodeXmlFromCodeSystem(String codeSystem) {
        for (final CODeedTypeSaisie type : CODeedTypeSaisie.values()) {
            if (type.codeSystem.equals(codeSystem)) {
                return type.codeXml;
            }
        }
        return null;
    }

}
