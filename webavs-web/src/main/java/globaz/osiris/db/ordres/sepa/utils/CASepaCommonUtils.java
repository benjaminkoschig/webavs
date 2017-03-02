package globaz.osiris.db.ordres.sepa.utils;

import globaz.globall.util.JACCP;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.util.TIIbanFormater;
import globaz.webavs.common.WebavsDocumentionLocator;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.GenericAccountIdentification1CH;

public class CASepaCommonUtils {

    private final static Logger logger = LoggerFactory.getLogger(CASepaCommonUtils.class);
    // since the have to be regroup
    public static final String TYPE_VIREMENT_BANCAIRE = "bankANDccp";
    public static final String TYPE_VIREMENT_POSTAL = "bankANDccp";
    public static final String TYPE_VIREMENT_MANDAT = "mandat";

    private static TIIbanFormater formater = new TIIbanFormater();

    private CASepaCommonUtils() {
    }

    /**
     * Retourne la version de WebAvs, la longueur sera 10 caractËres max (contrainte XSD) -> la version peut donc Ítre
     * tronquÈe
     * 
     * @return
     */
    public static String getVersion() {
        String version = WebavsDocumentionLocator.getVersion();
        return limit35(version);
    }

    public static String getAppName() {
        String name = WebavsDocumentionLocator.getName();
        return limit70(name);
    }

    /**
     * MÈthode permettant la conversion une date Globaz dans le format {@link XMLGregorianCalendar }
     * 
     * @param yyyyMMdd Une string reprÈsentant la date sous le format yyyyMMdd
     * @return Retourne une date sous le format {@link XMLGregorianCalendar }
     * @throws DatatypeConfigurationException
     */
    protected static XMLGregorianCalendar convertToXMLGregorianCalendar(String yyyyMMdd)
            throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar(Integer.parseInt(yyyyMMdd.substring(0, 4)),
                (Integer.parseInt(yyyyMMdd.substring(4, 6)) - 1), Integer.parseInt(yyyyMMdd.substring(6)));
        XMLGregorianCalendar returnCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        returnCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        return returnCalendar;
    }

    /**
     * obtenir la valeur BIC de l'adresse de paiement
     * 
     * @param adressePaiement
     * @return CodeSwiftWithoutSpaces
     */
    public static String getAdpBIC(IntAdressePaiement adressePaiement) {
        return adressePaiement.getBanque().getCodeSwiftWithoutSpaces().toUpperCase();
    }

    /**
     * rÈpond ‡ la contrainte de la xsd pain001 MaxText16 BasicTextCH
     */
    public static String limit16(String name) {
        return limit(16, name);
    }

    /**
     * rÈpond ‡ la contrainte de la xsd pain001 MaxText35 BasicTextCH
     */
    public static String limit35(String name) {
        return limit(35, name);
    }

    /**
     * rÈpond ‡ la contrainte de la xsd pain001 MaxText70 BasicTextCH
     */
    public static String limit70(String name) {
        return limit(70, name);
    }

    protected static String limit(int max, String name) {
        if (name.length() > max) {
            name = name.substring(0, max - 1);
        }
        return escapeInvalidBasicTextCH(name);
    }

    public static XMLGregorianCalendar getCurrentTime() throws DatatypeConfigurationException {
        XMLGregorianCalendar returnCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                new GregorianCalendar());
        return returnCalendar;
    }

    /**
     * type une adresse de payement des diffÈrents types pris en charge en pain001
     * utilisÈ par la mÈthode de regroupement.
     * 
     * @param adp
     * @return variable statique de la classe utilitaire bankANDccp/mandat/null
     * @throws Exception
     */
    public static String getTypeVirement(CAAdressePaiementFormatter adp) throws Exception {
        if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
            return TYPE_VIREMENT_POSTAL;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
            return TYPE_VIREMENT_BANCAIRE;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT)) {
            return TYPE_VIREMENT_MANDAT;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            return null;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            return TYPE_VIREMENT_BANCAIRE;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL)) {
            return TYPE_VIREMENT_MANDAT;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP_INTERNATIONAL)) {
            return TYPE_VIREMENT_POSTAL;
        }
        return null;

    }

    protected static String getIntAdressePaiementIBAN(IntAdressePaiement adp) {
        if (isValidIban(adp.getNumCompte())) {
            return formater.unformat(adp.getNumCompte());
        }
        return null;
    }

    protected static GenericAccountIdentification1CH getNotIban(IntAdressePaiement adp) {
        if (!isValidIban(adp.getNumCompte())) {
            GenericAccountIdentification1CH other = new GenericAccountIdentification1CH();
            try {
                other.setId(JACCP.formatNoDash(adp.getNumCompte()));
            } catch (Exception e) {
                logger.debug("cannot unformat this as a CCP:" + adp.getNumCompte(), e);
                other.setId(adp.getNumCompte());
            }
            return other;
        }
        return null;
    }

    /**
     * use fw formatter to validate IBAN format
     * 
     * @param ibanStr
     * @return
     */
    protected static boolean isValidIban(String ibanStr) {
        try {
            formater.check(ibanStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected static String escapeInvalidBasicTextCH(String txt) {
        String escStr = txt
                .replaceAll(
                        "(?!([a-zA-Z0-9\\.,;:'\\+\\-/\\(\\)?\\*\\[\\]\\{\\}\\\\`¥~ ]|[!&quot;#%&amp;&lt;&gt;˜=@_$£]|[‡·‚‰ÁËÈÍÎÏÌÓÔÒÚÛÙˆ˘˙˚¸˝ﬂ¿¡¬ƒ«»… ÀÃÕŒœ“”‘÷Ÿ⁄€‹—])).",
                        " ");
        if (!escStr.equals(txt)) {
            logger.debug("escaped char from this string {} - {}", txt, escStr);
        }
        return escStr;

    }

    //
    /**
     * utility to provide the value of property who set the max number of OV in one OG
     * 
     * @param caOrdreGroupe
     * @return the value of property, if not set, return Long.MAX_VALUE
     * @throws PropertiesException (jade exception as prop does not exist)
     * @throws NumberFormatException (parsing the prop value to Long)
     * @throws Exception (jade exception)
     */
    public static Long getOvMaxByOG(CAOrdreGroupe caOrdreGroupe) throws NumberFormatException, PropertiesException,
            Exception {
        if (caOrdreGroupe.getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022)) {
            return Long.parseLong(CAProperties.ISO_SEPA_MAX_OVPAROG.getValue(), 10);
        }
        return Long.MAX_VALUE;
    }

}
