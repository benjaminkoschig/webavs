package ch.globaz.pegasus.process.statistiquesOFAS.step1;

import globaz.pyxis.constantes.IConstantes;
import java.util.HashMap;
import java.util.Map;

public abstract class TIUtil {

    public final static Map<String, String> codesCantonOfs = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put(IConstantes.CS_LOCALITE_CANTON_ZURICH, "1");
            put(IConstantes.CS_LOCALITE_CANTON_BERNE, "2");
            put(IConstantes.CS_LOCALITE_CANTON_LUCERNE, "3");
            put(IConstantes.CS_LOCALITE_CANTON_URI, "4");
            put(IConstantes.CS_LOCALITE_CANTON_SCHWYZ, "5");
            put(IConstantes.CS_LOCALITE_CANTON_OBWALD, "6");
            put(IConstantes.CS_LOCALITE_CANTON_NIDWALD, "7");
            put(IConstantes.CS_LOCALITE_CANTON_GLARIS, "8");
            put(IConstantes.CS_LOCALITE_CANTON_ZOUG, "9");
            put(IConstantes.CS_LOCALITE_CANTON_FRIBOURG, "10");
            put(IConstantes.CS_LOCALITE_CANTON_SOLEURE, "11");
            put(IConstantes.CS_LOCALITE_CANTON_BALE_VILLE, "12");
            put(IConstantes.CS_LOCALITE_CANTON_BALE_CAMPAGNE, "13");
            put(IConstantes.CS_LOCALITE_CANTON_SCHAFFOUSE, "14");
            put(IConstantes.CS_LOCALITE_CANTON_APPENZELL_AR, "15");
            put(IConstantes.CS_LOCALITE_CANTON_APPENZELL_AI, "16");
            put(IConstantes.CS_LOCALITE_CANTON_SAINT_GALL, "17");
            put(IConstantes.CS_LOCALITE_CANTON_GRISONS, "18");
            put(IConstantes.CS_LOCALITE_CANTON_ARGOVIE, "19");
            put(IConstantes.CS_LOCALITE_CANTON_THURGOVIE, "20");
            put(IConstantes.CS_LOCALITE_CANTON_TESSIN, "21");
            put(IConstantes.CS_LOCALITE_CANTON_VAUD, "22");
            put(IConstantes.CS_LOCALITE_CANTON_VALAIS, "23");
            put(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL, "24");
            put(IConstantes.CS_LOCALITE_CANTON_GENEVE, "25");
            put(IConstantes.CS_LOCALITE_CANTON_JURA, "26");
        }
    };

    /**
     * Permet d'encoder la chaîne de caractère passée en paramètre
     * 
     * @param stringToEncode La chaîne à encoder
     * 
     * @return La chaîne correspondante encodée, compatible javascript
     */
    public static String encode(String stringToEncode) {
        StringBuffer encoded = new StringBuffer();
        while (stringToEncode.length() > 0) {
            String nextChar = stringToEncode.substring(0, 1);
            stringToEncode = stringToEncode.substring(1);
            encoded.append(TIUtil.getCharEncoding(nextChar));
        }
        return encoded.toString();
    }

    /**
     * Permet de récupérer le caractère encodé correspondant au caractère passé en paramètre, si l'encodage n'est pas
     * nécessaire, renvoie le caractère passé en paramtère
     * 
     * @param myChar Le caractère à encoder
     * 
     * @return La chaîne de caractère correspondant à l'encodage ou le caractère si celui-ci n'est pas à encoder.
     */
    public static String getCharEncoding(String myChar) {
        HashMap<String, String> correspondance = new HashMap<String, String>();
        correspondance.put("\"", "\\\"");
        correspondance.put("'", "\\'");
        correspondance.put("\"", "\\\"");
        if (correspondance.get(myChar) != null) {
            return correspondance.get(myChar);
        } else {
            return myChar;
        }
    }

    /**
     * Retourne le code ofs d'un canton en fonction du code systéme du canton
     * 
     * @param csCanton
     * @return
     */
    public static String getCodeCantonOFS(String csCanton) {
        return TIUtil.codesCantonOfs.get(csCanton);
    }
}
