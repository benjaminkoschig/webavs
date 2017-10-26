package ch.globaz.pegasus.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.domaine.decision.Decision;

public class RpcUtil {

    private static String SUFFIXE_DECISION_ID_REQUERANT = "R";
    private static String SUFFIXE_DECISION_ID_CONJOINT = "C";
    private static final Logger LOG = LoggerFactory.getLogger(RpcUtil.class);

    public static InfoCaisse createInfosCaisse() {
        try {
            String numeroCaisseFormate = EPCProperties.RPC_ELOFFICE.getValue();
            String numeroAgenceFormate = CommonProperties.NUMERO_AGENCE.getValue();
            int numeroCaisse = Integer.parseInt(numeroCaisseFormate);
            int numeroAgence = Integer.parseInt(numeroAgenceFormate);
            return new InfoCaisse(numeroCaisse, numeroAgence, numeroCaisseFormate, numeroAgenceFormate);
        } catch (PropertiesException exception) {
            throw new IllegalArgumentException("Property Exception " + exception.toString(), exception);
        }
    }

    public static void suffixDecisionId(Decision decisionRequerant, Decision decisionConjoint) {
        if (decisionRequerant.getId().equals(decisionConjoint.getId())) {
            decisionRequerant.setId(decisionRequerant.getId() + SUFFIXE_DECISION_ID_REQUERANT);
            decisionConjoint.setId(decisionConjoint.getId() + SUFFIXE_DECISION_ID_CONJOINT);
        }
    }

    public static void deleteSuffixDecisionId(Decision decisionRequerant, Decision decisionConjoint) {
        if (decisionRequerant != null
                && (decisionRequerant.getId().endsWith(SUFFIXE_DECISION_ID_REQUERANT) || decisionRequerant.getId()
                        .endsWith(SUFFIXE_DECISION_ID_CONJOINT))) {
            decisionRequerant.setId(decisionRequerant.getId().substring(0, decisionRequerant.getId().length() - 1));
        }
        if (decisionConjoint != null
                && (decisionConjoint.getId().endsWith(SUFFIXE_DECISION_ID_REQUERANT) || decisionConjoint.getId()
                        .endsWith(SUFFIXE_DECISION_ID_CONJOINT))) {
            decisionConjoint.setId(decisionConjoint.getId().substring(0, decisionConjoint.getId().length() - 1));
        }
    }

    public static String formatNss(String nss) {
        String formatedNss = nss;
        try {
            Long numericNss = new Long(nss);
            if (nss.length() >= 13) {
                formatedNss = nss.substring(0, 3) + "." + nss.substring(3, 7) + "." + nss.substring(7, 11) + "."
                        + nss.substring(11, 13);
            }
        } catch (NumberFormatException e) {
            LOG.info("RpcUtils.formatNss(): Error when trying to parse NSS : {}", nss);
        }
        return formatedNss;
    }

}
