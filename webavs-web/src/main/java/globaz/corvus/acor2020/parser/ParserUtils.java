package globaz.corvus.acor2020.parser;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class ParserUtils {

    private static final Logger LOG = Logger.getLogger(ParserUtils.class);

    public static String formatIntToStringWithTwoChar(int value) {
        if (Objects.nonNull(value)) {
            DecimalFormat df = new DecimalFormat("00");
            return df.format(value);
        }
        return StringUtils.EMPTY;
    }

    public static String formatFloatToStringWithTwoDecimal(Float value) {
        if (Objects.nonNull(value)) {
            DecimalFormat df = new DecimalFormat("00.00");
            return df.format(value);
        }
        return StringUtils.EMPTY;
    }

    public static Long formatRequiredLong(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return new Long(0);
            }
            return Long.valueOf(fieldToParse);
        } catch (Exception e) {
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en Integer");
        }
        return new Long(0);
    }

    public static Integer formatRequiredInteger(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return 0;
            }
            return Integer.valueOf(fieldToParse);
        } catch (Exception e) {
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en Integer");
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
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en BigDecimal");
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal formatRequiredBigDecimalDuree(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return new BigDecimal("00.00");
            }
            return new BigDecimal(fieldToParse);
        } catch (Exception e) {
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en BigDecimal");
        }
        return new BigDecimal("00.00");
    }

    public static BigDecimal formatRequiredBigDecimalNoDecimal(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(fieldToParse).setScale(0, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en BigDecimal");
        }
        return BigDecimal.ZERO;
    }

    public static Short formatRequiredShort(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return 0;
            }
            return new Short(fieldToParse);
        } catch (Exception e) {
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en Short");
        }
        return 0;
    }

    public static Short formatOptionalShort(String fieldToParse) {
        try {
            if (JadeStringUtil.isBlankOrZero(fieldToParse)) {
                return null;
            }
            return new Short(fieldToParse);
        } catch (Exception e) {
            LOG.error("Impossible de parser la valeur : " + fieldToParse + " en Short");
        }
        return null;
    }

    public static XMLGregorianCalendar formatDate(String date, String formatDate) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(formatDate);
        try {
            Date dateFormat = format.parse(date);

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dateFormat);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (ParseException | DatatypeConfigurationException e) {
            LOG.error("Erreur lors du formatage d'une date.", e);
        }
        return null;
    }

    public static long formatNssToLong(String strNss) {
        Long nss = new Long(0);
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
    public static String formatMMtoAAxMM(int mois) {
        int nbMoisDansAnnee = 12;
        int moisRestant = mois % nbMoisDansAnnee;
        int annee = (mois - moisRestant) / nbMoisDansAnnee;
        return String.format("%s.%s", formatIntToStringWithTwoChar(annee), formatIntToStringWithTwoChar(moisRestant));
    }

}
