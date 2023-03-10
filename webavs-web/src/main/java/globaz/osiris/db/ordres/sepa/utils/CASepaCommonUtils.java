package globaz.osiris.db.ordres.sepa.utils;

import globaz.globall.util.JACCP;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.util.TIIbanFormater;
import globaz.webavs.common.WebavsDocumentionLocator;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.GenericAccountIdentification1CH;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericAccountIdentification1;

public class CASepaCommonUtils {

    private static final String IBAN_REGEX_PAIN = "[A-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}";
    private static final Logger logger = LoggerFactory.getLogger(CASepaCommonUtils.class);
    // since the have to be regroup
    public static final String TYPE_VIREMENT_BANCAIRE = "bankANDccp";
    public static final String TYPE_VIREMENT_POSTAL = "bankANDccp";
    public static final String TYPE_VIREMENT_MANDAT = "mandat";
    public static final String CLEARING_POSTAL = "09000";

    private static final int QR_IID_MIN = 30000;
    private static final int QR_IID_MAX = 31999;

    private static TIIbanFormater formater = new TIIbanFormater();

    private CASepaCommonUtils() {
    }

    /**
     * Retourne la version de WebAvs, la longueur sera 10 caract?res max (contrainte XSD) -> la version peut donc ?tre
     * tronqu?e
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
     * M?thode permettant la conversion une date Globaz dans le format {@link XMLGregorianCalendar }
     * 
     * @param yyyyMMdd Une string repr?sentant la date sous le format yyyyMMdd
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
     * r?pond ? la contrainte de la xsd pain001 MaxText16 BasicTextCH
     */
    public static String limit16(String name) {
        return limit(16, name);
    }

    /**
     * r?pond ? la contrainte de la xsd pain001 MaxText35 BasicTextCH
     */
    public static String limit35(String name) {
        return limit(35, name);
    }

    /**
     * r?pond ? la contrainte de la xsd pain001 MaxText70 BasicTextCH
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
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
    }

    /**
     * type une adresse de payement des diff?rents types pris en charge en pain001
     * utilis? par la m?thode de regroupement.
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

    public static String getTypeAdresseWithIBANPostalEnable(CAAdressePaiementFormatter adp) {
        final String iban = getCAAdressePaiementIBAN(adp);
        if (iban != null) {
            final String numClearing = iban.substring(4, 9);
            // correspond-t-il ? un n? de clearing de PostFinance
            if (CASepaCommonUtils.CLEARING_POSTAL.equals(numClearing)) {
                return IntAdressePaiement.CCP;
            } else {
                return IntAdressePaiement.BANQUE;
            }
        }

        return adp.getTypeAdresse();
    }

    protected static String getCAAdressePaiementIBAN(CAAdressePaiementFormatter adp) {
        if (adp.getNumCompte() == null) {
            return null;
        }

        final String numCmp = StringUtils.deleteWhitespace(adp.getNumCompte());

        if (isFullValidIban(numCmp)) {
            return formater.unformat(numCmp);
        }
        return null;
    }

    protected static String getIntAdressePaiementIBAN(IntAdressePaiement adp) {
        if (adp.getNumCompte() == null) {
            return null;
        }

        final String numCmp = StringUtils.deleteWhitespace(adp.getNumCompte());

        if (isFullValidIban(numCmp)) {
            return formater.unformat(numCmp);
        }
        return null;
    }

    /**
     * V?rifie si l'adresse de paiement contient une adresse QR-IBAN. (IBAN avec un QR-IID)
     * R?gle : l'IID se trouve en position 5 ? 8 de l'IBAN. Si l'IID est entre 30000 et 31999
     * compris, il s'agit d'un QR IBAN.
     * Doc : https://www.postfinance.ch/content/dam/pfch/doc/480_499/499_78_fr.pdf page 7
     *
     * @param adp   Adresse de paiement
     * @return  True si c'est un QR IBAN, false l'IBAN est null, ou s'il n'a pas un QR-IID.
     */
    public static boolean isQRIBAN(IntAdressePaiement adp){
        String iban = getIntAdressePaiementIBAN(adp);
        if(iban == null || iban.length() < 9) return false;
        int qrIid = Integer.parseInt(iban.substring(4, 9));
        return (qrIid >= QR_IID_MIN && qrIid <= QR_IID_MAX);
    }

    private static boolean isXsdRegexIbanValid(String ibanStr) {
        Pattern pat = Pattern.compile(IBAN_REGEX_PAIN);
        Matcher mat = pat.matcher(ibanStr);
        return mat.matches();
    }

    protected static GenericAccountIdentification1CH getNotIban(IntAdressePaiement adp) {
        if (adp.getNumCompte() == null) {
            return null;
        }

        final String numCmp = StringUtils.deleteWhitespace(adp.getNumCompte());

        if (!isFullValidIban(numCmp)) {
            GenericAccountIdentification1CH other = new GenericAccountIdentification1CH();
            try {
                String numCmpFormated = numCmp;
                if (IntAdressePaiement.CCP.equals(adp.getTypeAdresse())) {
                    numCmpFormated = JACCP.formatNoDash(numCmp);
                }

                other.setId(numCmpFormated);
            } catch (Exception e) {
                logger.debug("cannot unformat this as a CCP:" + numCmp, e);
                other.setId(numCmp);
            }
            return other;
        }
        return null;
    }

    private static boolean isFullValidIban(String numCmp) {
        boolean isIbanOk = false;

        if (numCmp == null) {
            return false;
        }

        final String numCmpTrimed = StringUtils.deleteWhitespace(numCmp);

        isIbanOk |= isXsdRegexIbanValid(numCmpTrimed) && isValidIban(numCmpTrimed);
        isIbanOk |= isXsdRegexIbanValid(numCmpTrimed) && numCmpTrimed.startsWith("LI");

        return isIbanOk;
    }

    protected static GenericAccountIdentification1 getNotIbanPain008(IntAdressePaiement adp) {
        if (adp.getNumCompte() == null) {
            return null;
        }

        final String numCmp = StringUtils.deleteWhitespace(adp.getNumCompte());

        if (!isFullValidIban(numCmp)) {
            GenericAccountIdentification1 other = new GenericAccountIdentification1();
            try {
                String numCmpFormated = numCmp;
                if (IntAdressePaiement.CCP.equals(adp.getTypeAdresse())) {
                    numCmpFormated = JACCP.formatNoDash(numCmp);
                }

                other.setId(numCmpFormated);
            } catch (Exception e) {
                logger.debug("cannot unformat this as a CCP:" + numCmp, e);
                other.setId(numCmp);
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
                        "(?!([a-zA-Z0-9\\.,;:'\\+\\-/\\(\\)?\\*\\[\\]\\{\\}\\\\`?~ ]|[!&quot;#%&amp;&lt;&gt;?=@_$?]|[??????????????????????????????????????????????])).",
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
