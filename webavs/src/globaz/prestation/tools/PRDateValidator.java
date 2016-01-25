/*
 * Créé le 17 octobre 07
 */
package globaz.prestation.tools;

import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <H1>Une classe pour valider des formats de dates exotiques</H1>
 * 
 * @author jje
 */
public class PRDateValidator {

    private static Matcher matcher;
    private static Pattern pattern;

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AAAAMM
     */
    public static boolean isDateFormat_AAAAMM(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d{4})(0[1-9]|1[012])");
        } else {
            return false;
        }

    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AAAA.MM
     */
    public static boolean isDateFormat_AAAAxMM(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d{4})[.](0[1-9]|1[012])");
        } else {
            return false;
        }

    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AAMM
     */
    public static boolean isDateFormat_AAMM(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d{2})(0[1-9]|1[012])");
        } else {
            return false;
        }

    }

    /**
     * Valide une date ACOR à partir d'une String. Année <=44 et mois <=12
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AAMM
     */
    public static boolean isDateFormat_AAMM_ACOR(String date) {
        try {
            if (!JadeStringUtil.isEmpty(date)) {
                if (Integer.parseInt(date.substring(0, 2)) > 45) {
                    return false;
                }
                if (Integer.parseInt(date.substring(0, 2)) == 45 && Integer.parseInt(date.substring(2, 4)) > 11) {
                    return false;
                }
                if (Integer.parseInt(date.substring(2, 4)) > 12) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AA.DD
     */
    public static boolean isDateFormat_AAxDD(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d{2})[.](\\d{2})");
        } else {
            return false;
        }

    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AA.MM
     */
    public static boolean isDateFormat_AAxMM(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d{2})[.](0[1-9]|0[012])");
        } else {
            return false;
        }

    }

    /**
     * Valide une date ACOR à partir d'une String. Année <=44 et mois <=12
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AA.MM
     */
    public static boolean isDateFormat_AAxMM_ACOR(String date) {
        if (!JadeStringUtil.isEmpty(date)) {
            List lst = JadeStringUtil.tokenize(date, ".");
            if (lst.size() == 2) {
                if (Integer.parseInt((String) lst.get(0)) > 44) {
                    return false;
                }
                if (Integer.parseInt((String) lst.get(0)) == 44 && Integer.parseInt((String) lst.get(1)) > 11) {
                    return false;
                }
                if (Integer.parseInt((String) lst.get(1)) > 12) {
                    return false;
                }
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format AMM
     */
    public static boolean isDateFormat_AMM(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d)(0[1-9]|1[012])");
        } else {
            return false;
        }

    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format A.D
     */
    public static boolean isDateFormat_AxD(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d)[.](\\d)");
        } else {
            return false;
        }

    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format A.MM
     */
    public static boolean isDateFormat_AxMM(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(\\d)[.](0[0-9]|1[0-12])");
        } else {
            return false;
        }

    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format MMAAAA
     */
    public static boolean isDateFormat_MMAAAA(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(0[1-9]|1[012])(\\d{4})");
        } else {
            return false;
        }
    }

    /**
     * Valide une date à partir d'une String.
     * 
     * @param date
     *            La date a valider
     * @return vrai si la date correspond au format MM.AAAA
     */
    public static boolean isDateFormat_MMxAAAA(String date) {

        if (!JadeStringUtil.isEmpty(date)) {
            return isMatching(date, "(0[1-9]|1[012])[.](\\d{4})");
        } else {
            return false;
        }
    }

    /**
     * Valide une date à partir d'une String et d'une expression reguliere
     * 
     * @param date
     *            La date a valider
     * @param regex
     *            L'expression reguliere
     * @return vrai si le format de la date correspond a l'expression
     */
    private static boolean isMatching(String date, String regex) {

        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(date);

        return matcher.matches();

    }
}