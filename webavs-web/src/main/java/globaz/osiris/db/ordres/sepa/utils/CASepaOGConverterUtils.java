package globaz.osiris.db.ordres.sepa.utils;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.AccountIdentification4ChoiceCH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CashAccount16CHIdAndCurrency;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.GenericAccountIdentification1CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.Priority2Code;

public class CASepaOGConverterUtils {

    public static final String DEBTORACCOUNT_TYPE_PRTRY_NO_ADVICE = "NOA";
    public static final String DEBTORACCOUNT_TYPE_PRTRY_SINGLE_ADVICE = "SIA";
    public static final String DEBTORACCOUNT_TYPE_PRTRY_COLLECTIVE_ADVICE_NO_DETAILS = "CND";
    public static final String DEBTORACCOUNT_TYPE_PRTRY_COLLECTIVE_ADVICE_W_DETAILS = "CWD";

    public static final String POST_FINANCE_BIC = "POFICHBEXXX";

    /**
     * type d'avis déterminée au niveau de l'OG pour les blevels
     * 
     * @param og
     * @return
     */
    public static Boolean getBtchBook(APIOrdreGroupe og) {
        return Boolean.valueOf(isTypeAvisCollectif(og.getTypeAvis()));
    }

    protected static boolean isTypeAvisCollectif(String typeAvis) {
        return (APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_AVEC.equals(typeAvis) || APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS
                .equals(typeAvis));
    }

    /**
     * priorité déterminée au niveau de l'OG pour les blevels
     * 
     * @param og
     * @return
     */
    public static Priority2Code getInstrPrty(APIOrdreGroupe og) {
        if ("1".equals(og.getIsoHighPriority())) {
            return Priority2Code.HIGH;
        }
        return Priority2Code.NORM;
    }

    public static XMLGregorianCalendar getReqdExctnDt(APIOrdreGroupe og) throws DatatypeConfigurationException,
            JAException {
        JADate date = new JADate();
        date.fromString(og.getDateEcheance());
        XMLGregorianCalendar returnCalendar = CASepaCommonUtils.convertToXMLGregorianCalendar(date.toStrAMJ());
        // ignore time fields
        returnCalendar.setHour(DatatypeConstants.FIELD_UNDEFINED);
        returnCalendar.setMinute(DatatypeConstants.FIELD_UNDEFINED);
        returnCalendar.setSecond(DatatypeConstants.FIELD_UNDEFINED);
        returnCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
        return returnCalendar;
    }

    /**
     * remonter le nom de la caisse depuis d'adresse de paiement de l'OG au format de la xsd (max70)
     * 
     * @param og
     * @return
     * @throws Exception
     */
    public static String getNomCaisse70(APIOrdreGroupe og) throws Exception {
        String name = og.getOrganeExecution().getAdressePaiement().getNomTiersAdrPmt();
        return CASepaCommonUtils.limit70(name);
    }

    /**
     * retourne l'IBAN du débiteur de l'OG si l'info est de type IBAN
     * 
     * @param og
     * @return l'iban format électronique ou null
     * @throws Exception
     */
    public static String getDbtrIBAN(APIOrdreGroupe og) throws Exception {
        return getIntAdressePaiementIBAN(og.getOrganeExecution().getAdressePaiement());
    }

    /**
     * retourne l'IBAN du débit-taxe de l'OG si l'info est de type IBAN
     * 
     * @param og
     * @return l'iban format électronique ou null
     * @throws Exception
     */
    public static String getChrgsIBAN(APIOrdreGroupe og) throws Exception {
        return getIntAdressePaiementIBAN(og.getOrganeExecution().getAdresseDebitTaxes());
    }

    private static String getIntAdressePaiementIBAN(IntAdressePaiement adp) {
        return CASepaCommonUtils.getIntAdressePaiementIBAN(adp);
    }

    /**
     * @return l'objet de la xsd pour les comptes format hors IBAN, null si iban
     * @throws Exception
     */
    public static GenericAccountIdentification1CH getDbtrNotIBAN(APIOrdreGroupe og) throws Exception {
        return getNotIban(og.getOrganeExecution().getAdressePaiement());
    }

    /**
     * @return l'objet de la xsd pour les comptes format hors IBAN, null si iban
     * @throws Exception
     */
    public static GenericAccountIdentification1CH getChrgsNotIBAN(APIOrdreGroupe og) throws Exception {
        return getNotIban(og.getOrganeExecution().getAdresseDebitTaxes());
    }

    private static GenericAccountIdentification1CH getNotIban(IntAdressePaiement adp) throws Exception {
        return CASepaCommonUtils.getNotIban(adp);
    }

    public static String getTpProprietary(APIOrdreGroupe og) {
        if (og.getTypeAvis().equals(APIOrdreGroupe.ISO_TYPE_AVIS_AUCUN)) {
            return DEBTORACCOUNT_TYPE_PRTRY_NO_ADVICE;
        } else if (og.getTypeAvis().equals(APIOrdreGroupe.ISO_TYPE_AVIS_DETAIL)) {
            return DEBTORACCOUNT_TYPE_PRTRY_SINGLE_ADVICE;
        } else if (og.getTypeAvis().equals(APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_SANS)) {
            return DEBTORACCOUNT_TYPE_PRTRY_COLLECTIVE_ADVICE_NO_DETAILS;
        } else if (og.getTypeAvis().equals(APIOrdreGroupe.ISO_TYPE_AVIS_COLLECT_AVEC)) {
            return DEBTORACCOUNT_TYPE_PRTRY_COLLECTIVE_ADVICE_W_DETAILS;
        }
        return null;
    }

    /**
     * obtenir la valeur du BIC du débiteur de l'OG (si compte postal, retourne la valeur connue POFICHBEXXX).
     * 
     * @param og
     * @return
     * @throws Exception
     */
    public static String getDbtrAgtBIC(APIOrdreGroupe og) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(og.getOrganeExecution().getAdressePaiement());
        if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
            return POST_FINANCE_BIC;
        } else {
            return CASepaCommonUtils.getAdpBIC(og.getOrganeExecution().getAdressePaiement());
        }
    }

    public static CashAccount16CHIdAndCurrency getChrgsAcct(APIOrdreGroupe og) throws Exception {
        if (og.getOrganeExecution().getAdresseDebitTaxes().isNew()
                || og.getOrganeExecution().getAdresseDebitTaxes().getId()
                        .equals(og.getOrganeExecution().getAdressePaiement().getId())) {
            return null;
        }
        CashAccount16CHIdAndCurrency chrgsAcct = new CashAccount16CHIdAndCurrency();
        AccountIdentification4ChoiceCH id = new AccountIdentification4ChoiceCH();
        id.setIBAN(getChrgsIBAN(og));
        id.setOthr(getChrgsNotIBAN(og));
        chrgsAcct.setId(id);
        return chrgsAcct;
    }
}
