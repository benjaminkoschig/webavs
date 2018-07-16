package globaz.hermes.utils;

import globaz.globall.util.JAUtil;
import java.io.File;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Fournit des utilitaires pour manipuler les String
 * 
 * @author ado
 * @deprecated Utiliser le FW
 */
@Deprecated
public class StringUtils {
    /**
     * Cr�er un r�pertoire
     * 
     * @param file
     *            Le chemin absolu
     * @return true si cr��, sinon false
     */
    public static boolean createDirectory(String file) {
        File f = new File(file);
        return f.getParentFile().mkdirs();
    }

    /**
     * Que pour les ARC
     * 
     * @param text
     *            enregistrement
     * @param length
     *            la longueur
     * @return un enregistrement de la bonne longueur
     */
    public static String createRecords(String text, int length) {
        String returnString = "";
        for (int i = 0; i < text.length() / 120; i++) {
            returnString += text.substring(i * 120, (i * 120) + 120) + "\n";
        }
        return returnString;
    }

    /**
     * Formatte un nom aux normes AVS en envlevant les blancs autour de la virgule Dostes, Arnaud -> Dostes,Arnaud
     * 
     * @param name
     *            le nom
     * @return le nom sans les blancs
     */
    public static String fixName(String name) {
        /*
         * name = JAUtil.replaceString(name, " ,", ","); name = JAUtil.replaceString(name, ", ", ",");
         */
        try {
            if (name.indexOf(",") != name.lastIndexOf(",")) {
                // il y a plusieur virgule dans le nom ---> on ne fixe rien...
                return name;
            }
            StringTokenizer st = new StringTokenizer(name, ",");
            return ((String) st.nextElement()).trim() + "," + ((String) st.nextElement()).trim();
        } catch (Exception e) {
            // pas de virgule
            // e.printStackTrace();
            return name;
        }
    }

    public static String formatAvsAtBlank(String avs) {
        avs = JAUtil.formatAvs(avs);
        if (avs.startsWith("000")) {
            return "";
        }
        return avs;
    }

    public static String formatCaisseAgence(String caisse, String agence) {
        if (agence.trim().length() == 0 || removeUnsignigicantZeros(agence).trim().length() == 0) {
            return removeUnsignigicantZeros(caisse);
        } else { // l'agence existe
            return removeUnsignigicantZeros(caisse) + "." + removeUnsignigicantZeros(agence);
        }
    }

    public static String formatCodeEnregistrement(int codeEnr) {
        if (codeEnr < 10) {
            return "00" + codeEnr;
        }
        if (codeEnr < 100) {
            return "0" + codeEnr;
        }
        return "" + codeEnr;
    }

    /**
     * Method formatEnregistrement.
     * 
     * @param string
     * @return String
     */
    public static String formatEnregistrement(String enr) {
        enr = enr.toUpperCase();
        // umlaut
        enr = enr.replace('�', 'U');
        enr = enr.replace('�', 'A');
        enr = enr.replace('�', 'O');
        enr = enr.replace('�', 'I');
        // accents
        enr = enr.replace('�', 'E');
        enr = enr.replace('�', 'E');
        enr = enr.replace('�', 'A');
        // circonflexes
        enr = enr.replace('�', 'A');
        enr = enr.replace('�', 'E');
        enr = enr.replace('�', 'O');
        enr = enr.replace('�', 'I');
        //
        enr = enr.replace('�', '\'');
        //
        return enr;
    }

    public static String getAgence(String numCaisseAgence) {
        int sep = numCaisseAgence.indexOf('.');
        if (sep != -1) {
            return numCaisseAgence.substring(sep + 1);
        } else {
            return "000";
        }
    }

    public static String getCaisse(String numCaisseAgence) {
        int sep = numCaisseAgence.indexOf('.');
        if (sep != -1) {
            return numCaisseAgence.substring(0, sep);
        } else {
            return numCaisseAgence;
        }
    }

    /**
     * Method isStringEmpty.
     * 
     * @param string
     * @return boolean
     */
    public static boolean isStringEmpty(String string) {
        if (string == null) {
            return true;
        }
        if (string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2003 11:01:51)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method padAfterString, compl�te une chaine en ajoutant apr�s
     * 
     * @param stringToPad
     *            la chaine � completer
     * @param padString
     *            la chaine � utiliser pour la compl�tion
     * @param length
     *            la longueur finale voulue
     * @return String
     */
    public static String padAfterString(String stringToPad, String padString, int length) {
        while (stringToPad.length() < length) {
            stringToPad = stringToPad + padString;
        }
        return stringToPad;
    }

    /**
     * Method padBeforeString. compl�te la chaine en ajoutant avant
     * 
     * @param stringToPad
     *            la chaine � compl�ter
     * @param padString
     *            la chaine � utiliser pour la compl�tion
     * @param length
     *            la longueur finale voulue
     * @return String
     */
    public static String padBeforeString(String stringToPad, String padString, int length) {
        while (stringToPad.length() < length) {
            stringToPad = padString + stringToPad;
        }
        return stringToPad;
    }

    /**
     * Method randomSpace.
     * 
     * @return String
     */
    public static String randomSpace() {
        String retour = "";
        double d = (Math.random() * 100) % 10;
        int c = (int) Math.floor(d);
        for (int i = 0; i < c; i++) {
            retour += " ";
        }
        return retour;
    }

    public static String removeBeginChars(String st, char c) {
        if (st == null) {
            return "";
        }
        int index = st.length();
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) != c) {
                index = i;
                break;
            }
        }
        return st.substring(index, st.length());
    }

    public static String removeChar(String st, char c) {
        if (st == null || st.trim().length() == 0) {
            return st;
        }
        String retour = "";
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) != c) {
                retour += st.charAt(i);
            }
        }
        return retour;
    }

    /**
     * Method checkName. V�rifie la validit� du nom uniquement pour la virgule
     * 
     * @param name
     *            l'�tat nominatif � v�rifier
     * @return boolean true si ok, false si espace avant ou apr�s la virgule, ou si pas de virgule. m�thode d�plac�e
     *         dans HEUtils
     */
    // public static boolean checkName(String name) {
    // int i = -1;
    // if(name.indexOf(",")!=name.lastIndexOf(",")){
    // // il y a plusieur virgule dans le nom ---> pas permis
    // return false;
    // }
    // if ((i = name.lastIndexOf(",")) == -1) {
    // return false; // pas de virgule
    // } else {
    // if((i+1)<name.length()){
    // if (name.charAt(i - 1) == ' ' || name.charAt(i + 1) == ' ') {
    // return false; // espace avant ou apr�s la virgule
    // } else {
    // return true; // ok
    // }
    // }else{
    // // il n'y a aucun caract�re apr�s la virgule, donc faux
    // return false;
    // }
    // }
    // }
    /**
     * Method removeCharAt.
     * 
     * @param nom
     * @param i
     */
    public static String removeCharAt(String str, int i) {
        if (i < 0 || i > str.length()) {
            return str;
        } else {
            return str.substring(0, i) + str.substring(i + 1, str.length());
        }
    }

    /**
     * Method removeDots.
     * 
     * @param st
     * @return String
     */
    public static String removeDots(String st) {
        if (st == null) {
            return "";
        }
        String retour = "";
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) != '.') {
                retour += st.charAt(i);
            }
        }
        return retour.trim();
    }

    public static String removeDotsOnly(String st) {
        if (st == null) {
            return "";
        }
        String retour = "";
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) != '.') {
                retour += st.charAt(i);
            }
        }
        return retour;
    }

    /**
     * Enl�ve les z�ros non significatifs devant. ex : transforme "026.001" en "26.001" ou "000453" en "453" ou "1.5600"
     * en "1.56"
     * 
     * @param String
     *            la chaine � transformer
     * @return st la chaine transform�e
     */
    public static String removeUnsignigicantZeros(String st) {
        // enl�ve les espaces devant et derri�re
        st = st.trim();
        // enl�ve les z�ros devant
        while (st.startsWith("0")) {
            st = st.substring(1);
        }
        // si se termine par un point, j'enl�ve le point
        if (st.trim().endsWith(".")) {
            st = st.trim();
            st = st.substring(0, st.length() - 1);
        }
        return st;
    }

    /**
     * Renvoit null si la string est null ou si elle est vide (que des blancs)
     */
    public static String returnNullIfStringEmpty(String testedString) {
        if (testedString == null) {
            return null;
        } else if (testedString.trim().length() == 0) {
            return null;
        } else {
            return testedString;
        }
    }

    public static String transformAccent(String input) {
        String tmp = JAUtil.replaceString(input, "�", "E");
        tmp = JAUtil.replaceString(tmp, "�", "E");
        tmp = JAUtil.replaceString(tmp, "�", "O");
        tmp = JAUtil.replaceString(tmp, "�", "A");
        return tmp;
    }

    public static String transformDiphtAndAccent(String input) {
        return transformAccent(transformDiphtongues(input));
    }

    public static String transformDiphtongues(String input) {
        String tmp = JAUtil.replaceString(input, "�", "ae");
        tmp = JAUtil.replaceString(tmp, "�", "oe");
        tmp = JAUtil.replaceString(tmp, "�", "ue");
        tmp = JAUtil.replaceString(tmp, "�", "ue");
        tmp = JAUtil.replaceString(tmp, "�", "ue");
        tmp = JAUtil.replaceString(tmp, "�", "ue");
        return tmp;
    }

    public static String trimString(String str, int maxLength) {
        try {
            return str.substring(0, maxLength - 1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * Ne pas utiliser !!! Semble bugg�e En utilisant les classes java.text,<br>
     * enl�ve le formatage d'une valeure mon�taire<br>
     * 
     * @param currencyValue
     *            la valeur
     * @return un bug
     */
    public static String unformatCurrency(String currencyValue) {
        try {
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setParseIntegerOnly(true);
            return nf.parse(currencyValue).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return currencyValue;
        }
    }

    /**
     * La date est en JJMMAA ou en JJMMAAAA et on renvoit avec des JJ.MM.AAAA
     */
    public static String unformatDate(String unformattedDate) {
        unformattedDate = unformattedDate.trim();
        try {
            switch (unformattedDate.length()) {
                case 0:
                    return "";
                case 4: // MMAA
                    return DateUtils.convertDate(unformattedDate, DateUtils.MMAA, DateUtils.JJMMAA_DOTS);
                case 6: // JJMMAA
                    return DateUtils.convertDate(unformattedDate, DateUtils.JJMMAA, DateUtils.JJMMAAAA_DOTS);
                case 8: // JJMMAAAA
                    return DateUtils.convertDate(unformattedDate, DateUtils.JJMMAAAA, DateUtils.JJMMAAAA_DOTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
        return "";
    }

    /**
     * Method unPad. ???
     * 
     * @param st
     * @return String
     */
    public static String unPad(String st) {
        st = st.trim();
        try {
            int intStr = Integer.parseInt(st);
            if (intStr == 0) {
                return "";
            } else {
                return "" + intStr;
            }
        } catch (Exception e) {
            return st;
        }
    }

    public static String yytoyyyy(String yy) {
        Calendar c = Calendar.getInstance();
        if (Integer.parseInt(yy) < c.get(Calendar.YEAR)) {
            return "19" + yy;
        } else {
            return "20" + yy;
        }
    }

    /**
     * Method checkName. V�rifie la validit� du nom uniquement pour la virgule
     * 
     * @param name
     *            l'�tat nominatif � v�rifier
     * @return boolean true si ok, false si espace avant ou apr�s la virgule, ou si pas de virgule.
     */
    public StringUtils() {
        super();
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

}
