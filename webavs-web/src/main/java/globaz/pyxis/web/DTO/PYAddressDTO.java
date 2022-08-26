package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * On utilise cette classe comme un struct simplement pour contenir les donn�es
 * de la liste d'adresses dans le JSON
 */
@Data
public class PYAddressDTO {
    private String idAddress;
    private String domainAddress;
    private String typeAddress;
    private String country;
    private String street;
    private String streetNumber;
    private String attention;
    private String postalCode;
    private String locality;


    /**
     * M�thode pour valider la pr�sence/absence de champs dans le DTO et appeler la m�thode de validation des donn�es.
     *
     * @return true si les donn�es obligatoires d'une adresse sont pr�sentes
     */
    @JsonIgnore
    public Boolean isValid(PYTiersDTO dto) {
        Vector<String> mandatoryParameters = new Vector<>();

        for (PYAddressDTO addressDTO : dto.getAddresses()) {
            if (addressDTO.getTypeAddress() != null || addressDTO.getStreet() != null || addressDTO.getStreetNumber() != null || addressDTO.getPostalCode() != null || addressDTO.getLocality() != null) {
                mandatoryParameters.add(addressDTO.getTypeAddress());
                mandatoryParameters.add(addressDTO.getStreet());
                mandatoryParameters.add(addressDTO.getStreetNumber());
                mandatoryParameters.add(addressDTO.getPostalCode());
                mandatoryParameters.add(addressDTO.getLocality());
            }
        }
        return (mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty));
    }
}
