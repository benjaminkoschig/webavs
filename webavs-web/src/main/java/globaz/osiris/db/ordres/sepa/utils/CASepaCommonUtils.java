package globaz.osiris.db.ordres.sepa.utils;

import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.webavs.common.WebavsDocumentionLocator;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.GenericAccountIdentification1CH;

public class CASepaCommonUtils {

    public static final String TYPE_VIREMENT_BANCAIRE = "bank";
    public static final String TYPE_VIREMENT_POSTAL = "ccp";
    public static final String TYPE_VIREMENT_MANDAT = "mandat";

    /**
     * La longueur max autorisé par les XSD pour le numéro de version de l'application
     */
    private static final int VERSION_LENGTH = 35;
    private static final int NAME_LENGTH = 70;

    /**
     * Retourne la version de WebAvs, la longueur sera 10 caractères max (contrainte XSD) -> la version peut donc être
     * tronquée
     * 
     * @return
     */
    public static String getVersion() {
        String version = WebavsDocumentionLocator.getVersion();
        if (version.length() > VERSION_LENGTH) {
            version = version.substring(0, VERSION_LENGTH - 1);
        }
        return version;
    }

    public static String getAppName() {
        String name = WebavsDocumentionLocator.getName();
        if (name.length() > NAME_LENGTH) {
            name = name.substring(0, NAME_LENGTH - 1);
        }
        return name;
    }

    /**
     * Méthode permettant la conversion une date Globaz dans le format {@link XMLGregorianCalendar }
     * 
     * @param yyyyMMdd Une string représentant la date sous le format yyyyMMdd
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

    public static String getAgtBIC(IntAdressePaiement adressePaiement) {
        return adressePaiement.getBanque().getCodeSwiftWithoutSpaces();
    }

    public static String limit70(String name) {
        if (name.length() > 70) {
            name = name.substring(0, 69);
        }
        return name;
    }

    public static XMLGregorianCalendar getCurrentTime() throws DatatypeConfigurationException {
        XMLGregorianCalendar returnCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                new GregorianCalendar());
        return returnCalendar;
    }

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
        // FIXME isCompteIBAN est il fiable? CCP = IBAN??? wtf?
        if (adp.isCompteIBAN() && false) {
            // TODO unformat?
            return adp.getNumCompte();
        }
        return null;
    }

    protected static GenericAccountIdentification1CH getNotIban(IntAdressePaiement adp) {
        if (!adp.isCompteIBAN() || true) {
            // TODO unformat?
            GenericAccountIdentification1CH other = new GenericAccountIdentification1CH();
            other.setId(adp.getNumCompte());
            return other;
        }
        return null;
    }

    public static Long getOvMaxByOG(CAOrdreGroupe caOrdreGroupe) throws Exception {
        if (caOrdreGroupe.getOrganeExecution().getCSTypeTraitementOG().equals(APIOrganeExecution.OG_ISO_20022)) {
            return Long.parseLong(CAProperties.ISO_SEPA_MAX_OVPAROG.getValue(), 10);
        }
        return Long.MAX_VALUE;
    }

}
