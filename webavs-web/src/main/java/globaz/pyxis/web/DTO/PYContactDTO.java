package globaz.pyxis.web.DTO;

import lombok.Data;

import java.util.Vector;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste de contacts dans le JSON
 */
@Data
public class PYContactDTO {
    private String id;
    private String firstName;
    private String lastName;
    private Vector<PYMeanOfCommunicationDTO> meansOfCommunication = new Vector();
}
