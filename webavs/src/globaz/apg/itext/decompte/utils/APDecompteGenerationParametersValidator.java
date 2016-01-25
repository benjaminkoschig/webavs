package globaz.apg.itext.decompte.utils;

import globaz.apg.itext.decompte.APDecompteGenerationProcess;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Valid tous les paramètres du process avant exécution
 * 
 * @author lga
 */
public final class APDecompteGenerationParametersValidator {

    /**
     * Valide tous les paramètres du process avant son exécution
     * 
     * @param process
     *            Le process à valider
     * @return une liste d'erreurs ou une liste vide si aucune erreurs détectées
     */
    public final List<APDecompteGenerationParameterValidationError> validProcess(
            final APDecompteGenerationProcess process) {
        final List<APDecompteGenerationParameterValidationError> errors = new ArrayList<APDecompteGenerationParameterValidationError>();
        // Id lot
        if (JadeStringUtil.isEmpty(process.getIdLot())) {
            errors.add(APDecompteGenerationParameterValidationError.ID_LOT_VIDE);
        } else if (!JadeNumericUtil.isNumericPositif(process.getIdLot())) {
            errors.add(APDecompteGenerationParameterValidationError.ID_LOT_INVALIDE);
        }

        // Date sur document
        if (process.getDateDocument() == null) {
            errors.add(APDecompteGenerationParameterValidationError.DATE_DOCUMENT_VIDE);
        }

        // Date sur comptable
        if (process.getDateComptable() == null) {
            errors.add(APDecompteGenerationParameterValidationError.DATE_COMPTABLE_VIDE);
        }

        // E-mail
        if (process.getEMailAddress() == null) {
            errors.add(APDecompteGenerationParameterValidationError.EMAIL_INVALID);
        } else if (!process.getEMailAddress().contains("@") || !process.getEMailAddress().contains(".")) {
            errors.add(APDecompteGenerationParameterValidationError.EMAIL_INVALID);
        }

        // Type de prestation ACM versée par la caisse
        if (process.getTypeDePrestationAcm() == null) {
            errors.add(APDecompteGenerationParameterValidationError.TYPE_DE_PRESTATION_ACM_NON_RENSEIGNE);
        }

        return errors;
    }
}
