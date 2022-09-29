package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste d'adresses dans le JSON
 */
@Data
public class PYAddressDTO {
    private String idTiers;
    private String idAddress;
    private String idAvoirAddress;
    private String domainAddress;
    private String typeAddress;
    private String country;
    private String street;
    private String streetNumber;
    private String attention;
    private String postalCode;
    private String locality;
    private String modificationDate;


    /**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     *
     * @return true si les données obligatoires d'une adresse sont présentes
     */
    @JsonIgnore
    public Boolean isValid(PYTiersDTO dto) {
        Map<String, String> mapForValidator = new HashMap<>();

        for (PYAddressDTO addressDTO : dto.getAddresses()) {
            if (addressDTO.getTypeAddress() != null || addressDTO.getStreet() != null || addressDTO.getStreetNumber() != null || addressDTO.getPostalCode() != null || addressDTO.getLocality() != null) {
                mapForValidator.put("typeAddress", this.getTypeAddress());
                mapForValidator.put("street", this.getStreet());
                mapForValidator.put("streetNumber", this.getStreetNumber());
                mapForValidator.put("postalCode", this.getPostalCode());
                mapForValidator.put("locality", this.getLocality());
                mapForValidator.put("country", this.getCountry());
            }
        }
        return true;
    }

    @JsonIgnore
    public Boolean isValidForCreation() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiers", this.getIdTiers());
        mapForValidator.put("modificationDate", this.getModificationDate());
        mapForValidator.put("typeAddress", this.getTypeAddress());
        mapForValidator.put("street", this.getStreet());
        mapForValidator.put("streetNumber", this.getStreetNumber());
        mapForValidator.put("postalCode", this.getPostalCode());
        mapForValidator.put("locality", this.getLocality());
        mapForValidator.put("country", this.getCountry());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        PYValidateDTO.isValidForAddress(this);

        return true;
    }

    @JsonIgnore
    public Boolean isValidForUpdate() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiers", this.getIdTiers());
        mapForValidator.put("modificationDate", this.getModificationDate());
        mapForValidator.put("idAddress", this.getIdAddress());
        mapForValidator.put("idAvoirAddress", this.getIdAvoirAddress());
        mapForValidator.put("typeAddress", this.getTypeAddress());
        mapForValidator.put("street", this.getStreet());
        mapForValidator.put("streetNumber", this.getStreetNumber());
        mapForValidator.put("postalCode", this.getPostalCode());
        mapForValidator.put("locality", this.getLocality());
        mapForValidator.put("country", this.getCountry());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        PYValidateDTO.isValidForAddress(this);

        return true;
    }

    @JsonIgnore
    public Boolean isValidForDelete() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idAvoirAddress", this.getIdAvoirAddress());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }


}
