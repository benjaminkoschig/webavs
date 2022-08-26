package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
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
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     *
     * @return true si les données obligatoires d'une adresse sont présentes
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
