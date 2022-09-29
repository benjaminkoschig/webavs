package globaz.pyxis.web.DTO;

import ch.globaz.pyxis.domaine.DomaineApplication;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste des moyens de communication d'un contact dans le JSON
 */
@Data
public class PYMeanOfCommunicationDTO {
    private String idContact;
    private String meanOfCommunicationType;
    private String meanOfCommunicationValue;
    private String applicationDomain;

    private static final Logger logger = LoggerFactory.getLogger(PYValidateDTO.class);

    @JsonIgnore
    public Boolean isValid() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("meanOfCommunicationType", this.getMeanOfCommunicationType());
        mapForValidator.put("applicationDomain", this.getApplicationDomain());
        mapForValidator.put("meanOfCommunicationValue", this.getMeanOfCommunicationValue());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }

    @JsonIgnore
    public Boolean isValidForCreationAndUpdate() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idContact", this.getIdContact());
        mapForValidator.put("meanOfCommunicationType", this.getMeanOfCommunicationType());
        mapForValidator.put("applicationDomain", this.getApplicationDomain());
        mapForValidator.put("meanOfCommunicationValue", this.getMeanOfCommunicationValue());

        PYValidateDTO.checkIfEmpty(mapForValidator);
        checkApplicationDomain(this.getApplicationDomain());
        checkMeanOfCommuncationType(this.getMeanOfCommunicationType());

        return true;
    }

    @JsonIgnore
    public Boolean isValidForDeletion() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idContact", this.getIdContact());
        mapForValidator.put("meanOfCommunicationType", this.getMeanOfCommunicationType());
        mapForValidator.put("applicationDomain", this.getApplicationDomain());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }

    /**
     * Méthode pour vérifier que applicationDomain est un code système valide
     *
     * @param applicationDomain
     */
    private static final void checkApplicationDomain(String applicationDomain) {
        try {
            DomaineApplication.parse(applicationDomain); // If this goes through without error, applicationDomain has a valid value
        } catch (IllegalArgumentException e) {
            logger.error("Erreur dans le domaine d'application lors de l'ajout d'un contact.");
            throw new PYBadRequestException("Erreur dans le domaine d'application lors de l'ajout d'un contact.", e);
        }
    }

    /**
     * Méthode pour vérifier que type est un code système valide
     *
     * @param type
     */
    private static final void checkMeanOfCommuncationType(String type) {
        if (!TypeContact.isValid(type)) {
            logger.error("Erreur dans le type de moyen de communication lors de l'ajout d'un contact.");
            throw new PYBadRequestException("Erreur dans le type de moyen de communication lors de l'ajout d'un contact.");
        }
    }
}
