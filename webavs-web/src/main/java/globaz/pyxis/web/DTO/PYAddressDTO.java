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
        Vector<String> mandatoryParameters = new Vector<>();

        for (PYAddressDTO addressDTO : dto.getAddresses()) {
            if (addressDTO.getTypeAddress() != null || addressDTO.getStreet() != null || addressDTO.getStreetNumber() != null || addressDTO.getPostalCode() != null || addressDTO.getLocality() != null) {
                mandatoryParameters.add(addressDTO.getTypeAddress());
                mandatoryParameters.add(addressDTO.getStreet());
                mandatoryParameters.add(addressDTO.getStreetNumber());
                mandatoryParameters.add(addressDTO.getPostalCode());
                mandatoryParameters.add(addressDTO.getLocality());
                mandatoryParameters.add(addressDTO.getCountry());
            }
        }
        return (mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty));
    }

    @JsonIgnore
    public Boolean isValidForCreation() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdTiers());
        mandatoryParameters.add(this.getModificationDate());
        mandatoryParameters.add(this.getTypeAddress());
        mandatoryParameters.add(this.getStreet());
        mandatoryParameters.add(this.getStreetNumber());
        mandatoryParameters.add(this.getPostalCode());
        mandatoryParameters.add(this.getLocality());
        mandatoryParameters.add(this.getCountry());

        PYValidateDTO.isValidForAddress(this);

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }

    @JsonIgnore
    public Boolean isValidForUpdate() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdTiers());
        mandatoryParameters.add(this.getModificationDate());
        mandatoryParameters.add(this.getIdAddress());
        mandatoryParameters.add(this.getIdAvoirAddress());
        mandatoryParameters.add(this.getTypeAddress());
        mandatoryParameters.add(this.getStreet());
        mandatoryParameters.add(this.getStreetNumber());
        mandatoryParameters.add(this.getPostalCode());
        mandatoryParameters.add(this.getLocality());
        mandatoryParameters.add(this.getCountry());

        PYValidateDTO.isValidForAddress(this);

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }

    @JsonIgnore
    public Boolean isValidForDelete() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdAvoirAddress());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }


}
