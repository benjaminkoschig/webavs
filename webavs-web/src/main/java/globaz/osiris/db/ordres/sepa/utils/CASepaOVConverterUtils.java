package globaz.osiris.db.ordres.sepa.utils;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CategoryPurpose1CHCode;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.GenericAccountIdentification1CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.LocalInstrument2Choice;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentMethod3Code;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ServiceLevel8Choice;

public class CASepaOVConverterUtils {

    private final static Logger logger = LoggerFactory.getLogger(CASepaOVConverterUtils.class);

    public static final String INSTRUCTION_ID_PREFIX = "OV-";
    public static final String ENDTOEND_ID_PREFIX = "TR-";

    public static final String ORDRE_VERSEMENT_BVR = "BVR";
    public static final String ORDRE_VERSEMENT_VIREMENT = "virement";

    public static final String PAYS_DESTINATION_SUISSE = "ch";
    public static final String PAYS_DESTINATION_INTERNATIONNAL = "int";

    public static final String B_LEVEL_ID_VIR_BANQ_POST_CH = "GT-01";
    public static final String B_LEVEL_ID_MANDAT_CH = "GT-02";
    public static final String B_LEVEL_ID_BVR = "GT-03";
    public static final String B_LEVEL_ID_VIR_BANQ_POST_INT = "GT-11";
    public static final String B_LEVEL_ID_MANDAT_INT = "GT-12";

    public static final String ExternalServiceLevel1_SEPA = "SEPA";

    public static final String ExternalLocalInstrument1_CPP = "CPP";
    public static final String ExternalLocalInstrument1_CH01 = "CH01";// FIXME, not from ExternalCodeSet

    public static final String ExternalCategoryPurpose1_RENTE_AVS_AI = "PENS";
    public static final String ExternalCategoryPurpose1_AUTRE = "SSBE";
    public static final String ExternalPaymentPurpose1_ALLOC_FAM = "BECH"; // FIXME, not from ExternalCodeSet Category
                                                                           // purpose
    public static final String ExternalPaymentPurpose1_REMBOURSEMENT = "PENS";// FIXME, not from ExternalCodeSet
                                                                              // Category purpose

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare
     * @throws Exception
     */
    public static String getTypeVersement(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());
        if (!JadeStringUtil.isBlankOrZero(ov.getReferenceBVR())) {
            return ORDRE_VERSEMENT_BVR;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            return ORDRE_VERSEMENT_BVR;
        }
        return ORDRE_VERSEMENT_VIREMENT;
    }

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare
     * @throws Exception
     */
    public static String getTypeVirement(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());

        return CASepaCommonUtils.getTypeVirement(adp);
    }

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare
     * @throws Exception
     */
    public static String getPaysDestination(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());
        if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
            return PAYS_DESTINATION_SUISSE;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
            return PAYS_DESTINATION_SUISSE;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT)) {
            return PAYS_DESTINATION_SUISSE;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            return null;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            return PAYS_DESTINATION_INTERNATIONNAL;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL)) {
            return PAYS_DESTINATION_INTERNATIONNAL;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP_INTERNATIONAL)) {
            return PAYS_DESTINATION_INTERNATIONNAL;
        }
        return null;
    }

    /**
     * identifiant unique de l'enregistrement Blevel au niveau de l'ordre groupé
     * 
     * @param ov
     * @return id
     * @throws Exception
     */
    public static String getPmtInfId(APICommonOdreVersement ov) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(ov.getReferenceBVR())) {
            return B_LEVEL_ID_BVR;
        }
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());

        if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
            return B_LEVEL_ID_VIR_BANQ_POST_CH;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
            return B_LEVEL_ID_VIR_BANQ_POST_CH;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT)) {
            return B_LEVEL_ID_MANDAT_CH;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            return B_LEVEL_ID_BVR;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            return B_LEVEL_ID_VIR_BANQ_POST_INT;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL)) {
            return B_LEVEL_ID_MANDAT_INT;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP_INTERNATIONAL)) {
            return B_LEVEL_ID_VIR_BANQ_POST_INT;
        }
        return null;
    }

    public static ServiceLevel8Choice getSvcLvl(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());
        ServiceLevel8Choice svcLvl = new ServiceLevel8Choice();
        svcLvl.setCd(ExternalServiceLevel1_SEPA);
        if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            return svcLvl;
        } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL)) {
            return svcLvl;
        }
        return null;
    }

    public static LocalInstrument2Choice getLclInstrm(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());
        LocalInstrument2Choice lclInstrm = new LocalInstrument2Choice();
        lclInstrm.setCd(ExternalLocalInstrument1_CPP);
        if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT)) {
            return lclInstrm;
        }
        return null;
    }

    public static CategoryPurpose1CHCode getCtgyPurp(APICommonOdreVersement ov) {
        CategoryPurpose1CHCode ctgyPurp = new CategoryPurpose1CHCode();
        ctgyPurp.setCd(ExternalCategoryPurpose1_RENTE_AVS_AI);
        return ctgyPurp;
    }

    /**
     * identifiant unique de la transaction
     * 
     * @param ov
     * @return instructionId
     */
    public static String getInstrId(APICommonOdreVersement ov) {
        return INSTRUCTION_ID_PREFIX + ov.getIdOperation();
    }

    /**
     * identifiant unique de la transaction
     * 
     * @param ov
     * @return instructionId
     */
    public static String getEndToEndId(APICommonOdreVersement ov) {
        return ENDTOEND_ID_PREFIX + ov.getNumTransaction();
    }

    public static String getCreditorName70(APICommonOdreVersement ov) throws Exception {
        String name = ov.getAdressePaiement().getNomTiersAdrPmt();
        logger.debug("getCreditorName70 - {}", name);
        return CASepaCommonUtils.limit70(name);
    }

    public static PaymentMethod3Code getPmtMtd(APICommonOdreVersement ov) throws Exception {
        if (getTypeVirement(ov).equals(CASepaCommonUtils.TYPE_VIREMENT_MANDAT)) {
            return PaymentMethod3Code.CHK;
        }
        return PaymentMethod3Code.TRF;
    }

    public static String getCbtrIBAN(APICommonOdreVersement ov) throws Exception {
        return CASepaCommonUtils.getIntAdressePaiementIBAN(ov.getAdressePaiement());
    }

    public static GenericAccountIdentification1CH getCbtrNotIBAN(APICommonOdreVersement ov) throws Exception {
        return CASepaCommonUtils.getNotIban(ov.getAdressePaiement());
    }

    public static boolean isMandat(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());
        return CASepaCommonUtils.getTypeVirement(adp).equals(CASepaCommonUtils.TYPE_VIREMENT_MANDAT);
    }

    public static String getMotif140(APICommonOdreVersement ov) {
        String motif = ov.getMotif();
        if (motif.length() > 140) {
            motif = motif.substring(0, 139);
        }
        return motif;
    }

    public static boolean isBVR(APICommonOdreVersement ov) throws Exception {
        return CASepaOVConverterUtils.getTypeVersement(ov).equals(CASepaOVConverterUtils.ORDRE_VERSEMENT_BVR);
    }

    public static String getCbtrAgtBIC(APICommonOdreVersement ov) throws Exception {
        String bic = CASepaCommonUtils.getAgtBIC(ov.getAdressePaiement());
        if (bic != null && !bic.isEmpty()) {
            return bic;
        }
        return null;
    }

    public static boolean isCLevelBicRequired(APICommonOdreVersement ov) throws Exception {
        String blevelId = getPmtInfId(ov);
        return (blevelId.equals(B_LEVEL_ID_VIR_BANQ_POST_CH) || blevelId.equals(B_LEVEL_ID_VIR_BANQ_POST_INT));
    }
}
