package ch.globaz.naos.business.constantes;

public enum AFAffiliationType {

    CAP_EMPLOYEUR("804014"),
    CAP_INDEPENDANT("804015"),
    CGAS_EMPLOYEUR("804016"),
    CGAS_INDEPENDANT("804017");

    public static AFAffiliationType getEnumFromCodeSystem(String codeSystem) {

        for (AFAffiliationType type : AFAffiliationType.values()) {
            if (type.getCodeSystem().equals(codeSystem)) {
                return type;
            }
        }

        return null;

    }

    public static boolean isTypeCAP(String codeSystemType) {
        return AFAffiliationType.CAP_EMPLOYEUR.equals(codeSystemType)
                || AFAffiliationType.CAP_INDEPENDANT.equals(codeSystemType);
    }

    public static boolean isTypeCAPCGAS(String codeSystemType) {
        return AFAffiliationType.isTypeCAP(codeSystemType) || AFAffiliationType.isTypeCGAS(codeSystemType);
    }

    public static boolean isTypeCAPCGASEmployeur(String codeSystemType) {
        return AFAffiliationType.CAP_EMPLOYEUR.equals(codeSystemType)
                || AFAffiliationType.CGAS_EMPLOYEUR.equals(codeSystemType);
    }

    public static boolean isTypeCAPCGASIndependant(String codeSystemType) {
        return AFAffiliationType.CAP_INDEPENDANT.equals(codeSystemType)
                || AFAffiliationType.CGAS_INDEPENDANT.equals(codeSystemType);
    }

    public static boolean isTypeCGAS(String codeSystemType) {
        return AFAffiliationType.CGAS_EMPLOYEUR.equals(codeSystemType)
                || AFAffiliationType.CGAS_INDEPENDANT.equals(codeSystemType);
    }

    private String codeSystem;

    private AFAffiliationType(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(AFAffiliationType.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
