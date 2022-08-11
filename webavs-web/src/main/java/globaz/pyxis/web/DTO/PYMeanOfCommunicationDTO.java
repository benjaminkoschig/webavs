package globaz.pyxis.web.DTO;

import lombok.Data;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste des moyens de communication d'un contact dans le JSON
 */
@Data
public class PYMeanOfCommunicationDTO {
    private String meanOfCommunicationType;
    private String meanOfCommunicationValue;
    private String applicationDomain;
}
