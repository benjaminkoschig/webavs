package globaz.corvus.utils;

import java.util.HashMap;
import java.util.Map;

public class REMappingGroupPrestations {

    public static String GROUPE_AI_API = "13";

    public static String GROUPE_AI_REO = "12";
    public static String GROUPE_AI_RO = "11";
    public static String GROUPE_AVS_API = "3";
    public static String GROUPE_AVS_REO = "2";
    public static String GROUPE_AVS_RO = "1";
    public static String GROUPE_PC_AI = "14";
    public static String GROUPE_PC_AVS = "4";
    public static String GROUPE_RFM_AI = "15";
    public static String GROUPE_RFM_AVS = "5";
    /**
     * @param args
     */

    static Map mapConvertion = null;

    /**
     * Retourne le numéro du groupe de prestations pour un genre de prestation donné
     * 
     * @param genrePrestation
     * @return
     */
    public static String getGroupPrestation(final String genrePrestation) {
        REMappingGroupPrestations.initMap();
        String genre = genrePrestation;

        if ((genre != null) && (genre.indexOf(".") != -1)) {
            genre = genre.substring(0, genre.indexOf(".") - 1);
        }

        return (String) REMappingGroupPrestations.mapConvertion.get(genre);
    }

    static void initMap() {
        if (REMappingGroupPrestations.mapConvertion == null) {
            REMappingGroupPrestations.mapConvertion = new HashMap();

            // Groupe de prestation
            REMappingGroupPrestations.mapConvertion.put("2", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("3", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("4", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("5", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("6", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("7", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("8", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("9", REMappingGroupPrestations.GROUPE_AVS_REO);

            REMappingGroupPrestations.mapConvertion.put("02", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("03", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("04", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("05", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("06", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("07", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("08", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("09", REMappingGroupPrestations.GROUPE_AVS_REO);

            REMappingGroupPrestations.mapConvertion.put("10", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("11", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("12", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("13", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("14", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("15", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("16", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("20", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("21", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("22", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("23", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("24", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("25", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("26", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("33", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("34", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("35", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("36", REMappingGroupPrestations.GROUPE_AVS_RO);
            REMappingGroupPrestations.mapConvertion.put("43", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("44", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("45", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("46", REMappingGroupPrestations.GROUPE_AVS_REO);
            REMappingGroupPrestations.mapConvertion.put("48", REMappingGroupPrestations.GROUPE_RFM_AVS);
            REMappingGroupPrestations.mapConvertion.put("49", REMappingGroupPrestations.GROUPE_PC_AVS);
            REMappingGroupPrestations.mapConvertion.put("50", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("51", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("52", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("53", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("54", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("55", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("56", REMappingGroupPrestations.GROUPE_AI_RO);
            REMappingGroupPrestations.mapConvertion.put("70", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("71", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("72", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("73", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("74", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("75", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("76", REMappingGroupPrestations.GROUPE_AI_REO);
            REMappingGroupPrestations.mapConvertion.put("81", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("82", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("83", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("84", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("85", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("86", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("87", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("89", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("88", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("91", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("92", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("93", REMappingGroupPrestations.GROUPE_AI_API);
            REMappingGroupPrestations.mapConvertion.put("94", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("95", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("96", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("97", REMappingGroupPrestations.GROUPE_AVS_API);
            REMappingGroupPrestations.mapConvertion.put("98", REMappingGroupPrestations.GROUPE_RFM_AI);
            REMappingGroupPrestations.mapConvertion.put("99", REMappingGroupPrestations.GROUPE_PC_AI);
        }
    }

}
