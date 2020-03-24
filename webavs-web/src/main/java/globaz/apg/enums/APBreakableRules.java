/**
 * 
 */
package globaz.apg.enums;

/**
 * @author dde
 */
public enum APBreakableRules {

    R_500(500),
    R_501(501),
    R_502(502),
    R_503(503),
    R_504(504),
    R_505(505),
    R_506(506),
    R_507(507),
    R_508(508),
    R_509(509),
    R_510(510),
    R_600(600),
    R_1509(1509);

    private int code;

    private APBreakableRules(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getCodeAsString() {
        return String.valueOf(code);
    }

    /**
     * Recherche la Rule lié au code passé en paramètre. Si le code n'est pas valid, <code>null</code> dera retourné
     * 
     * @param code
     *            Le code de la règle
     * @return la Rule lié au code passé en paramètre. Si le code n'est pas valid, <code>null</code> dera retourné
     */
    public static APBreakableRules valueOfCode(String code) {
        APBreakableRules rule = null;
        try {
            rule = APBreakableRules.valueOf("R_" + code);
        } catch (Exception e) {
            // Nothing to do,
        }
        return rule;
    }
}
