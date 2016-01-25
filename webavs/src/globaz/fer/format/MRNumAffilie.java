package globaz.fer.format;

import globaz.globall.format.IFormatData;

/**
 * Gestion du format du numéro d'affiliation de la FER.
 * 
 * @author: OCA
 */
public class MRNumAffilie implements IFormatData {

    /**
     * Constructeur.
     */
    public MRNumAffilie() {
        super();
    }

    @Override
    public String check(Object value) throws Exception {
        String num = (String) value;
        if (value == null) {
            return "Le numéro ne doit pas être 'null'";
        }
        if (!num.startsWith("MR")) {
            return "Le numéro doit commencer par MR";
        }
        if (num.length() != "MR000.000".length()) {
            return "Le numéro doit avoir " + "MR000.000".length() + " positions";
        }
        int[] digits = new int[] { 2, 3, 4, 6, 7, 8 };
        // test des positions numériques
        for (int d : digits) {
            char c = num.charAt(d);
            if (c > '9' || c < '0') {
                return "Le numéro n'est pas valable";
            }
        }
        if (!(num.charAt(5) == '.')) {
            return "Le numéro n'est pas valable";
        }
        return "";
    }

    // 123456 -> MR123.456
    @Override
    public String format(String value) {
        value = unformat(value);
        return "MR" + value.substring(0, 3) + "." + value.substring(3, 6);
    }

    /*
     * Ne garde que les digits, donc MR et . sont supprimés
     * (non-Javadoc)
     * 
     * @see globaz.globall.format.IFormatData#unformat(java.lang.String)
     */
    @Override
    public String unformat(String value) {
        StringBuffer v = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isDigit(c)) {
                v.append(c);
            }
        }

        return v.toString();
    }

    public static void main(String[] args) throws Exception {
        MRNumAffilie n = new MRNumAffilie();
        // KO
        System.out.println("KO :");
        System.out.println(n.check(null));
        System.out.println(n.check("A"));
        System.out.println(n.check("MR"));
        System.out.println(n.check("MR1"));
        System.out.println(n.check("MRx00.000"));
        System.out.println(n.check("MR0x0.000"));
        System.out.println(n.check("MR00x.000"));
        System.out.println(n.check("MR000.x00"));
        System.out.println(n.check("MR000.0x0"));
        System.out.println(n.check("MR000.00x"));
        System.out.println(n.check("MR000x000"));

        // OK
        System.out.println("");
        System.out.println("OK :");
        System.out.println("OK > " + n.check("MR000.000"));
        System.out.println("OK > " + n.check("MR123.456"));
        System.out.println("OK > " + n.check("MR090.909"));

        // unformat
        System.out.println("");
        System.out.println("Unformat :");
        System.out.println(n.unformat("MR123.456"));
        System.out.println(n.unformat("123456"));
        System.out.println(n.unformat("MR123456"));
        System.out.println(n.unformat("123.456"));

        // format
        System.out.println("");
        System.out.println("format:");
        System.out.println(n.format("123456"));
        System.out.println(n.format(n.format("123456"))); // le formateur doit être re-entrant.

    }

}
