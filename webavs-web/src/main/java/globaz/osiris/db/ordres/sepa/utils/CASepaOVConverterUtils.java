package globaz.osiris.db.ordres.sepa.utils;

import globaz.globall.util.JACCP;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import globaz.osiris.db.ordres.sepa.exceptions.ISODataMissingXMLException;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CategoryPurpose1CHCode;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ClearingSystemMemberIdentification2;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.GenericAccountIdentification1CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.LocalInstrument2Choice;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentMethod3Code;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ServiceLevel8Choice;

public class CASepaOVConverterUtils {

    private final static Logger logger = LoggerFactory.getLogger(CASepaOVConverterUtils.class);

    public static final String INSTRUCTION_ID_PREFIX = "OV-";
    public static final String ENDTOEND_ID_PREFIX = "TR-";

    // caution, must be unique to identify BVR
    public static final String ORDRE_VERSEMENT_BVR = "BVR";
    public static final String ORDRE_VERSEMENT_VIREMENT = "virement";

    public static final String PAYS_DESTINATION_SUISSE = "CH";
    public static final String PAYS_DESTINATION_INTERNATIONNAL = "INT";

    public static final String CLEARING_POSTFINANCE = "9000";

    public static final String CLEARING_SUISSE_BCC_SYS_ID = "CHBCC";

    public static final String ExternalServiceLevel1_SEPA = "SEPA";

    public static final String ExternalLocalInstrument1_CPP = "CPP";
    public static final String ExternalLocalInstrument1_CH01 = "CH01";// FIXME, not from ExternalCodeSet

    public static final String ExternalCategoryPurpose1_RENTE_AVS_AI = "PENS";
    public static final String ExternalCategoryPurpose1_AUTRE = "SSBE";
    public static final String ExternalPaymentPurpose1_ALLOC_FAM = "BECH"; // FIXME, not from ExternalCodeSet Category
                                                                           // purpose
    public static final String ExternalPaymentPurpose1_REMBOURSEMENT = "REFU";// FIXME, not from ExternalCodeSet
                                                                              // Category purpose

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare BVR/virement
     * @throws Exception
     */
    public static String getTypeVersement(APICommonOdreVersement ov, CAAdressePaiementFormatter adpf) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(ov.getReferenceBVR())) {
            return ORDRE_VERSEMENT_BVR;
        } else if (adpf.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            return ORDRE_VERSEMENT_BVR;
        }
        return ORDRE_VERSEMENT_VIREMENT;
    }

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare bankANDccp/mandat/null
     * @throws Exception
     */
    public static String getTypeVirement(CAAdressePaiementFormatter adpf) throws Exception {
        return CASepaCommonUtils.getTypeVirement(adpf);
    }

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare
     * @throws Exception
     */
    public static String getPaysDestination(CAAdressePaiementFormatter adp) throws Exception {
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

    public static ServiceLevel8Choice getSvcLvl(CAAdressePaiementFormatter adpf) throws Exception {
        ServiceLevel8Choice svcLvl = new ServiceLevel8Choice();
        svcLvl.setCd(ExternalServiceLevel1_SEPA);
        if (adpf.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            return svcLvl;
        } else if (adpf.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL)) {
            return svcLvl;
        }
        return null;
    }

    public static LocalInstrument2Choice getLclInstrm(CASepaGroupeOGKey key) throws Exception {
        LocalInstrument2Choice lclInstrm = new LocalInstrument2Choice();
        lclInstrm.setCd(ExternalLocalInstrument1_CPP);
        if (key.isTypeVirement(CASepaCommonUtils.TYPE_VIREMENT_MANDAT)) {
            return lclInstrm;
        }
        return null;
    }

    public static CategoryPurpose1CHCode getCtgyPurp(APICommonOdreVersement ov) {
        CategoryPurpose1CHCode ctgyPurp = new CategoryPurpose1CHCode();
        // TODO determiner sur quel critère lpurpose code change
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

        String name = getBeneficiaire(ov);

        if (name.isEmpty()) {
            logger.error("CreditorName cannot be null or empty for OV {} , transaction {}", ov.getIdOperation(),
                    ov.getNumTransaction());
        } else {
            logger.debug("getCreditorName70 - {}", name);
        }
        return CASepaCommonUtils.limit70(name);
    }

    /**
     * retourne le nom du bénéficiaire de l'OP/OV en question
     * 
     * @param ov
     * @return String, nom du bénéficiaire
     * @throws Exception
     */
    public static String getBeneficiaire(APICommonOdreVersement ov) throws Exception {
        String name;
        try {
            if (!JadeStringUtil.isBlank(ov.getAdressePaiement().getAdresseCourrier().getAutreNom())) {
                name = ov.getAdressePaiement().getAdresseCourrier().getAutreNom();
            } else {
                name = ov.getAdressePaiement().getNomTiersAdrPmt();
            }
        } catch (Exception e) {
            throw new ISODataMissingXMLException("exception during getBeneficiaire() " + e.getClass().getName(), e);
        }
        return name;
    }

    public static PaymentMethod3Code getPmtMtd(CASepaGroupeOGKey key) throws Exception {
        if (key.isTypeVirement(CASepaCommonUtils.TYPE_VIREMENT_MANDAT)) {
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

    public static boolean isMandat(CAAdressePaiementFormatter adpf) throws Exception {
        return CASepaCommonUtils.getTypeVirement(adpf).equals(CASepaCommonUtils.TYPE_VIREMENT_MANDAT);
    }

    public static String getMotif140(APICommonOdreVersement ov) {
        String motif = ov.getMotif();
        if (motif.length() > 140) {
            motif = motif.substring(0, 139);
        }
        if (motif.isEmpty()) {
            return null;
        }
        return CASepaCommonUtils.escapeInvalidBasicTextCH(motif);
    }

    public static boolean isBVR(CASepaGroupeOGKey key) throws Exception {
        return key.isTypeVersement(CASepaOVConverterUtils.ORDRE_VERSEMENT_BVR);
    }

    public static String getCbtrAgtBIC(APICommonOdreVersement ov) throws Exception {
        String bic = CASepaCommonUtils.getAdpBIC(ov.getAdressePaiement());
        if (bic != null && !bic.isEmpty()) {
            return bic;
        }
        return null;
    }

    public static boolean isCLevelBicRequired(CASepaGroupeOGKey ovKey) {
        return ovKey.isTypeVirement(CASepaCommonUtils.TYPE_VIREMENT_BANCAIRE);
    }

    public static ClearingSystemMemberIdentification2 getCbtrAgtClrSys(APICommonOdreVersement ov) throws Exception {

        String clr = ov.getAdressePaiement().getBanque().getClearing();
        if (clr != null && !clr.isEmpty()) {
            ClearingSystemMemberIdentification2 clrSys = new ClearingSystemMemberIdentification2();
            clrSys.setMmbId(clr);
            return clrSys;
        }
        return null;
    }

    public static boolean isCLevelCCP(CAAdressePaiementFormatter adpf) throws Exception {
        return adpf.getTypeAdresse().equals(IntAdressePaiement.CCP);
    }

    /**
     * Récupération selon l'ancien formateur du numéro d'ahdérent BVR dans le cas de paiement type 1 (BVR)
     * 
     * @param ov
     * @return
     * @throws Exception
     */
    public static GenericAccountIdentification1CH getNumAdherentBVR(CAAdressePaiementFormatter adpf) throws Exception {
        GenericAccountIdentification1CH other = new GenericAccountIdentification1CH();
        // Numéro d'adhérent à 5 ou 9 positions
        if (adpf.isAdherentBvr5()) {
            other.setId(adpf.getNumCompte());
        } else {
            try {
                other.setId(JACCP.formatNoDash(adpf.getNumCompte()));
            } catch (Exception e) {
                throw new SepaException(e);
            }
        }
        return other;
    }

    private static boolean isAdherentBvr5(IntAdressePaiement adp) {

        // Si no adhérent BVR à 5 positions
        if (adp.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            if (adp.getNumCompte().length() == 5) {
                return true;
            }
        }

        // Faux dans les autres cas
        return false;
    }
}
