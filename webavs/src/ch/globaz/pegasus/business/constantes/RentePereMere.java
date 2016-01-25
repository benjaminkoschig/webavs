package ch.globaz.pegasus.business.constantes;

public class RentePereMere {

    public enum EPCRentesMeres {
        RENTE_ENFANT_LIE_RENTE_MERE("64006013"),
        RENTE_ENFANT_LIE_RENTE_MERE_55("64006017"),
        RENTE_EXTRA_ENFANT_LIE_RENTE_MERE("64006014"),
        RENTE_EXTRA_ENFANT_LIE_RENTE_MERE_75("64006020"),
        RENTE_EXTRA_ORPHELIN("64006009"),
        RENTE_ORPHELIN("64006004");

        private String csCode = null;

        private EPCRentesMeres(String csCode) {
            this.csCode = csCode;
        }

        public String getCsCode() {
            return csCode;
        }
    }

    public enum EPCRentesPeres {
        RENTE_ENFANT_LIE_RENTE_PERE("64006012"),
        RENTE_ENFANT_LIE_RENTE_PERE_54("64006016"),
        RENTE_EXTRA_ENFANT_LIE_RENTE_PERE("64006030"),
        RENTE_EXTRA_ENFANT_LIE_RENTE_PERE_74("64006019"),
        RENTE_EXTRA_ORPHELIN("64006008"),
        RENTE_ORPHELIN("64006003");

        private String csCode = null;

        private EPCRentesPeres(String csCode) {
            this.csCode = csCode;
        }

        public String getCsCode() {
            return csCode;
        }
    }

    public final static int TYPE_MERE = 2;;

    public final static int TYPE_PERE = 1;;

    public static Boolean isCsPresentsInEnumByType(int type, String csToTest) {

        // Si le type n'est pas du type constante défini, on retourne null
        if ((type != RentePereMere.TYPE_MERE) && (type != RentePereMere.TYPE_PERE)) {
            return null;
        }

        // iteraition
        if (type == RentePereMere.TYPE_MERE) {
            for (EPCRentesMeres csCode : EPCRentesMeres.values()) {
                if (csToTest.endsWith(csCode.getCsCode())) {
                    return true;
                }
            }
            return false;
        } else if (type == RentePereMere.TYPE_PERE) {
            for (EPCRentesPeres csCode : EPCRentesPeres.values()) {
                if (csToTest.endsWith(csCode.getCsCode())) {
                    return true;
                }
            }
            return false;
        }

        return null;
    }

}
