package globaz.corvus.process.communicationMutation;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

public class RECommunicationMutationProcessParametersValidator {

    /**
     * Valide les paramètres du process {@link RECommunicationMutationProcess}
     * 
     * @param process Le process à valider
     * @return une liste d'erreur(s). Retourne une liste vide si pas d'erreur
     */
    public List<RECommunicationMutationParameterValidationError> validate(RECommunicationMutationProcess process) {
        if (process == null) {
            throw new IllegalArgumentException(
                    "RECommunicationMutationProcessParametersValidator.validate(...) : provided parameters to validate is null");

        }
        List<RECommunicationMutationParameterValidationError> errors = new ArrayList<RECommunicationMutationParameterValidationError>();

        if (JadeStringUtil.isEmpty(process.getEmailAdresse())) {
            errors.add(RECommunicationMutationParameterValidationError.ADRESSE_MAIL_EMPTY);
        }
        if (process.getSendToGed() == null) {
            errors.add(RECommunicationMutationParameterValidationError.ENVOI_GED_EMPTY);
        }

        if (JadeStringUtil.isEmpty(process.getCodeIsoLangueOfficeAI())) {
            errors.add(RECommunicationMutationParameterValidationError.LANGUE_OFFICE_AI_EMPTY);
        }
        if (JadeStringUtil.isEmpty(process.getAdresseOfficeAi())) {
            errors.add(RECommunicationMutationParameterValidationError.ADRESSE_OFFICE_AI_EMPTY);
        }

        return errors;
    }
}
