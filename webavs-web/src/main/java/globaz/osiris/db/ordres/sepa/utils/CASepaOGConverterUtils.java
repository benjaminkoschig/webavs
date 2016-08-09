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

    /**
     * type d'avis determinée au niveau de l'OG pour les blevels
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
     * priorité determinée au niveau de l'OG pour les blevels
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

    public static String getNomCaisse70(APIOrdreGroupe og) throws Exception {
        String name = og.getOrganeExecution().getAdressePaiement().getNomTiersAdrPmt();
        return CASepaCommonUtils.limit70(name);
    }

    public static String getDbtrIBAN(APIOrdreGroupe og) throws Exception {
        return getIntAdressePaiementIBAN(og.getOrganeExecution().getAdressePaiement());
    }

    public static String getChrgsIBAN(APIOrdreGroupe og) throws Exception {
        return getIntAdressePaiementIBAN(og.getOrganeExecution().getAdresseDebitTaxes());
    }

    private static String getIntAdressePaiementIBAN(IntAdressePaiement adp) {
        return CASepaCommonUtils.getIntAdressePaiementIBAN(adp);
    }

    public static GenericAccountIdentification1CH getDbtrNotIBAN(APIOrdreGroupe og) throws Exception {
        return getNotIban(og.getOrganeExecution().getAdressePaiement());
    }

    public static GenericAccountIdentification1CH getChrgsNotIBAN(APIOrdreGroupe og) throws Exception {
        return getNotIban(og.getOrganeExecution().getAdresseDebitTaxes());
    }

    private static GenericAccountIdentification1CH getNotIban(IntAdressePaiement adp) {
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

    public static String getDbtrAgtBIC(APIOrdreGroupe og) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(og.getOrganeExecution().getAdressePaiement());
        if (CASepaCommonUtils.getTypeVirement(adp).equals(CASepaCommonUtils.TYPE_VIREMENT_POSTAL)) {
            return "POFICHBEXXX";
        } else {
            return CASepaCommonUtils.getAgtBIC(og.getOrganeExecution().getAdressePaiement());
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
