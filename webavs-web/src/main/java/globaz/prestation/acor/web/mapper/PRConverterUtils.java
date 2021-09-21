package globaz.prestation.acor.web.mapper;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRStringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class PRConverterUtils {

    private static final String VALUE_00_00 = "00.00";
    private static final String MESSAGE_ERREUR_CAST_BIGDECIMAL = "Impossible de parser la valeur : {} en BigDecimal. ";
    private static final String MESSAGE_ERREUR_CAST_SHORT = "Impossible de parser la valeur : {} en Short. ";
    private static final String MESSAGE_ERREUR_CAST_INTEGER = "Impossible de parser la valeur : {} en Integer. ";
    private static final String MESSAGE_ERREUR_CAST_LONG = "Impossible de parser la valeur : {} en Long. ";
    public static final String FORMAT_S_S = "%s.%s";

    private PRConverterUtils() {
    }

    public static String formatIntToStringWithTwoChar(Integer value) {
        if (Objects.nonNull(value)) {
            DecimalFormat df = new DecimalFormat("00");
            return df.format(value);
        }
        return StringUtils.EMPTY;
    }

    public static String formatIntToStringWithSixChar(Integer value) {
        if (Objects.nonNull(value)) {
            DecimalFormat df = new DecimalFormat("000000");
            return df.format(value);
        }
        return StringUtils.EMPTY;
    }

    public static String formatFloatToStringWithTwoDecimal(Float value) {
        if (Objects.nonNull(value)) {
            DecimalFormat df = new DecimalFormat(VALUE_00_00);
            return df.format(value);
        }
        return StringUtils.EMPTY;
    }

    public static long formatRequiredLong(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return 0l;
            }
            return Long.valueOf(fieldToParse);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_LONG, fieldToParse, e);
        }
        return 0l;
    }

    public static Integer formatRequiredInteger(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return 0;
            }
            return Integer.valueOf(fieldToParse);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_INTEGER, fieldToParse, e);
        }
        return 0;
    }

    public static BigDecimal formatRequiredBigDecimal(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(fieldToParse);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_BIGDECIMAL, fieldToParse, e);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal formatRequiredBigDecimalDuree(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return new BigDecimal(VALUE_00_00);
            }
            return new BigDecimal(fieldToParse);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_BIGDECIMAL, fieldToParse, e);
        }
        return new BigDecimal(VALUE_00_00);
    }

    public static BigDecimal formatRequiredBigDecimalNoDecimal(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(fieldToParse).setScale(0, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_BIGDECIMAL, fieldToParse, e);
        }
        return BigDecimal.ZERO;
    }

    public static String formatStringWithoutDots(String numero) {
        if (Objects.nonNull(numero)) {
            return numero.replace(".", "");
        }
        return StringUtils.EMPTY;
    }

    public static short formatRequiredShort(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return 0;
            }
            return Short.valueOf(fieldToParse);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_SHORT, fieldToParse, e);
        }
        return 0;
    }

    public static Short formatOptionalShort(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return null;
            }
            return Short.valueOf(fieldToParse);
        } catch (Exception e) {
            LOG.error(MESSAGE_ERREUR_CAST_SHORT, fieldToParse, e);
        }
        return null;
    }


    public static String formatDateToAAAAMM(XMLGregorianCalendar date) {
        if (date == null) {
            return StringUtils.EMPTY;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(date.getYear());
            if (date.getMonth() < 10) {
                sb.append("0");
            }
            sb.append(date.getMonth());
            return sb.toString();
        }
    }

    /**
     * Format la date en MMAA
     *
     * @param value La date à formatter
     * @return Une chaîne de caractère représentant la date sous la forme MMAA
     */
    public static String formatDateToMMAA(XMLGregorianCalendar value) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else {
            StringBuilder date = new StringBuilder();
            if (value.getMonth() < 10) {
                date.append("0");
            }
            date.append(value.getMonth());
            String annee = String.valueOf(value.getYear());
            if (annee.length() == 4) {
                date.append(annee, 2, 4);
            }
            return date.toString();
        }
    }

    public static long formatNssToLong(String strNss) {
        long nss = 0l;
        if (!JadeStringUtil.isBlankOrZero(strNss)) {
            nss = Long.valueOf(NSUtil.unFormatAVS(strNss));
        }
        return nss;
    }

    public static String formatAAAAtoAA(String annee) {
        if (annee.length() == 4) {
            return annee.substring(2, 4);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Conversion et formatage d'un nombre de mois MM en années et mois AA.MM
     *
     * @param mois les mois à convertir
     * @return le nb d'années et de mois au format AA.MM
     */
    public static String formatMMtoAAxMM(Integer mois) {
        if (Objects.nonNull(mois)) {
            LocalDate now = LocalDate.now();
            LocalDate delay = now.plusMonths(mois);
            Period period = Period.between(now, delay);
            return String.format(FORMAT_S_S, formatIntToStringWithTwoChar(period.getYears()), formatIntToStringWithTwoChar(period.getMonths()));
        }
        return StringUtils.EMPTY;
    }

    /**
     * Conversion et formatage d'un Integer correspondant à AAMM ou AMM en String correspondant à AA.MM
     *
     * @param duree durée au format AAMM ou AMM
     * @return la durée au format AA.MM
     */
    public static String formatAAMMtoAAxMM(Integer duree) {
        if (Objects.nonNull(duree)) {
            String value = String.valueOf(duree);
            if (value.length() == 4) {
                return String.format(FORMAT_S_S, value.substring(0, 2), value.substring(2, 4));
            } else if (value.length() == 3) {
                return String.format(FORMAT_S_S, "0" + value.substring(0, 1), value.substring(1, 3));
            } else {
                return StringUtils.EMPTY;
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Conversion et formatage d'un nombre de mois MM en années et mois A.MM
     *
     * @param mois les mois à convertir
     * @return le nb d'années et de mois au format A.MM
     */
    public static String formatMMtoAxMM(Integer mois) {
        if (Objects.nonNull(mois)) {
            LocalDate now = LocalDate.now();
            LocalDate delay = now.plusMonths(mois);
            Period period = Period.between(now, delay);
            return String.format(FORMAT_S_S, period.getYears(), formatIntToStringWithTwoChar(period.getMonths()));
        }
        return StringUtils.EMPTY;
    }

    /**
     * Conversion d'un nombre de mois MM en années A
     *
     * @param mois les mois à convertir
     * @return le nb d'années
     */
    public static String convertMMtoA(Integer mois) {
        if (Objects.nonNull(mois)) {
            LocalDate now = LocalDate.now();
            LocalDate delay = now.plusMonths(mois);
            Period period = Period.between(now, delay);
            return String.valueOf(period.getYears());
        }
        return StringUtils.EMPTY;
    }

    public static String formatBigDecimalToString(BigDecimal value) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else {
            return String.valueOf(value);
        }
    }

    public static String formatBooleanToString(Boolean value) {
        if (value == null) {
            return null;
        } else if (value) {
            return "1";
        } else {
            return "0";
        }
    }

    public static String formatIntegerToString(Integer value) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else {
            return String.valueOf(value);
        }
    }

    public static String formatShortToString(Short value) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else {
            return String.valueOf(value);
        }
    }


    public static String formatCodeCasSpecial(Short ccs) {
        if (ccs != null) {
            String value = String.valueOf(ccs);
            return indentLeftWithZero(value, 2);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Format une chaîne de caractère en lui ajoutant des zéro à gauche</br>
     * Example : value = '218', indentValue = '5'
     * ==> 00218
     *
     * @param value       La chaîne de caractère à formater
     * @param indentValue Le nombre de caractère de la chaîne final
     * @return Une chaîne de caractère indentée avec des zéros à gauche
     */
    public static String indentLeftWithZero(String value, Integer indentValue) {
        return PRStringFormatter.indentLeft(value, indentValue, "0");
    }

    /**
     * Format un BigDecimal sur 4 position selon les règles suivantes :</br>
     * </br>
     * x : nb années</br>
     * y : nb mois</br>
     * </br>
     * ==> Cas 1 : x.y est reçu en entrée, Cas 2 : seulement x est reçu.</br>
     * </br>
     * <tab>Cas du x.y :</br>
     * </br>
     * &nbsp;&nbsp;&nbsp;&nbsp;Cas du x : si x < 9 alors formatter en 09 sinon laisser tel quel</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;Cas du y : si y < 9 alors formatter en 09 sinon laisser tel quel</br>
     * </br>
     * &nbsp;&nbsp;&nbsp;&nbsp;Alors : Concaténer les deux valeurs afin d'obtenir une valeur sur 4 positions.</br>
     * </br>
     * &nbsp;&nbsp;Cas du x :</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;Cas du x : si x <= 9 alors formatter en 0900</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;Cas du x : si x > 9 alors formatter en x00 (par exemple si on reçoit 12 alors on sort
     * avec 1200)</br>
     * </br>
     * &nbsp;&nbsp;Alors : Concaténer les deux valeurs afin d'obtenir une valeur sur 4
     * positions.</br>
     * </br>
     * </br>
     * Exemple :</br>
     * - null : chaîne vide</br>
     * - 0.0 : 0000</br>
     * - 1.0 : 0100</br>
     * - 1.1 :
     * 0101</br>
     * - 10.1 : 1001</br>
     * - 10.10 : 1010</br>
     * - 0.11 : 0011</br>
     * - 12.12 : 1212</br>
     *
     * @param bd Le BigDecimal à formatter
     * @return La valeur du BigDecimal formatter
     */
    public static String formatBigDecimal(BigDecimal bd) {
        if (bd == null) {
            return StringUtils.EMPTY;
        }

        String nombre = bd.toString();

        // Pattern représentant notre règle précédemment décrite
        Pattern p = Pattern.compile("^[0-9]{1,2}\\.{1}[0-9]{0,2}$");
        Matcher matcher = p.matcher(nombre);

        // Est-ce que le format du nombre reçu correspond à la règle ?
        if (matcher.matches()) {
            // Est-ce le cas 1 ?
            p = Pattern.compile("^[0-9]{1,2}$");
            matcher = p.matcher(nombre);
            if (!matcher.matches()) {
                // Cas 1
                String[] nombreSplit = nombre.split("\\.");

                if (Integer.parseInt(nombreSplit[0]) < 10) {
                    nombreSplit[0] = "0" + Integer.parseInt(nombreSplit[0]);
                }

                if (Integer.parseInt(nombreSplit[1]) < 10) {
                    nombreSplit[1] = "0" + Integer.parseInt(nombreSplit[1]);
                }

                return nombreSplit[0] + nombreSplit[1];

            } else {
                // Cas 2
                if (Integer.parseInt(nombre) >= 10) {
                    return nombre + "00";
                } else {
                    return "0" + nombre + "00";
                }
            }
        } else {
            throw new IllegalArgumentException(
                    String.format("The argument bd = %f doesn't respect the rule established.", bd));
        }
    }

}
