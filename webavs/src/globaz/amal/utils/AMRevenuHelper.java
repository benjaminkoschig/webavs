package globaz.amal.utils;

public class AMRevenuHelper {

    public static String groupChildrens(String nbEnfants, String nbEnfantsSuspens) {
        if (nbEnfants == null) {
            nbEnfants = "0";
        }
        if (nbEnfantsSuspens == null) {
            nbEnfantsSuspens = "0";
        }
        Double nbEnfants_double = Double.parseDouble(nbEnfants);
        Double nbEnfantsSuspens_double = Double.parseDouble(nbEnfantsSuspens);
        return String.valueOf(nbEnfants_double + (nbEnfantsSuspens_double / 2));
    }

}
