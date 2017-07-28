package globaz.osiris.db.ordres.sepa.utils;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.sepa.exceptions.ISODataMissingXMLException;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntAdressePaiement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CASepaORConverterUtils {

    private final static Logger logger = LoggerFactory.getLogger(CASepaORConverterUtils.class);

    public static final String ORDRE_VERSEMENT_RECOUVREMENT = "recouvrement";
    public static final String INSTRUCTION_ID_PREFIX = "OR-";
    public static final String ENDTOEND_ID_PREFIX = "TR-";

    public static final String PAYS_DESTINATION_SUISSE = "CH";
    public static final String PAYS_DESTINATION_INTERNATIONNAL = "INT";

    /**
     * for hashmap key to regroup Blevels
     * 
     * @param ov
     * @return String to identify in hashing compare BVR/virement
     * @throws Exception
     */
    public static String getTypeVersement() throws Exception {
        return ORDRE_VERSEMENT_RECOUVREMENT;
    }

    /**
     * identifiant unique de la transaction
     * 
     * @param ov
     * @return instructionId
     */
    public static String getInstrId(APIOperationOrdreRecouvrement or) {
        return INSTRUCTION_ID_PREFIX + or.getId();
    }

    /**
     * identifiant unique de la transaction
     * 
     * @param ov
     * @return instructionId
     */
    public static String getEndToEndId(APIOperationOrdreRecouvrement or) {
        return ENDTOEND_ID_PREFIX + or.getNumTransaction();
    }

    public static String getDebtorName70(CAOperationOrdreRecouvrement or) throws Exception {

        String name = getBeneficiaire(or);

        if (name.isEmpty()) {
            logger.error("CreditorName cannot be null or empty for OV {} , transaction {}", or.getIdOperation(),
                    or.getNumTransaction());
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
    public static String getBeneficiaire(CAOperationOrdreRecouvrement or) throws Exception {
        String name;
        try {
            if (!JadeStringUtil.isBlank(or.getAdressePaiement().getAdresseCourrier().getAutreNom())) {
                name = or.getAdressePaiement().getAdresseCourrier().getAutreNom();
            } else {
                name = or.getAdressePaiement().getNomTiersAdrPmt();
            }
        } catch (Exception e) {
            throw new ISODataMissingXMLException("exception during getBeneficiaire() " + e.getClass().getName(), e);
        }
        return name;
    }

    /**
     * retourne la rue de l'ordre de recouvrement (Rue du créancier)
     * 
     * @param or
     * @return String, rue
     * @throws Exception
     */
    public static String getRue(final IntAdresseCourrier courrier) throws Exception {
        String rue = CASepaCommonUtils.limit70(courrier.getRue().trim());

        if (JadeStringUtil.isEmpty(rue)) {
            rue = CASepaCommonUtils.limit70(courrier.getAdresse()[0]);
            if (!JadeStringUtil.isEmpty(rue.trim())) {
                rue = rue.trim();
            } else {
                rue = null;
            }
        }

        return rue;
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

}
