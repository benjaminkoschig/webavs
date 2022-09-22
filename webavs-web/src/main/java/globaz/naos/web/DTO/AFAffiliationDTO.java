package globaz.naos.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AFAffiliationDTO {

    private String idTiers = "";
    private String raisonSocialeLong;
    private String numeroAffilie;
    private String ancien_numero_affilie;
    private String dateDebutAffiliation;
    private String dateFinAffiliation;
    private String genreAffiliation;
    private String raisonSocialeCourt;
    private String motifCreation;
    private String personnaliteJuridique;
    private String periodicite;
    private String brancheEconomique;
    private String codeNoga;
    private Boolean facturationParReleve;
    private Boolean envoieAutomatiqueReleve;
    private Boolean facturationAcompteCarte;
    private String facturationCodeFacturation;
    private Boolean exoneration;
    private Boolean personnelOccasionnel;
    private Boolean affiliationProvisoire;
    private String dateDemandeAffiliation;
    private String motifFin;
    private String declarationSalaire;
    private String activite;
    private String numeroIDE;
    private Boolean entiteIDENonAnnoncante;

    //Page 2
    private String affiliationSecurisee;


    //Controle LAA LPP ?
    //TODO valider par le client
    private Boolean LAA = true;
    private Boolean LPP = false;


    public AFAffiliationDTO() {

    }

    /**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     * <p>
     * isValidForCreation vérifie l'absence des champs et lance une erreur en cas de problème.
     *
     * @return true si les données du DTO sont bonnes pour une création
     */
    @JsonIgnore
    public Boolean isValidForCreation() {
        //TODO

//        Vector<String> mandatoryParameters = new Vector<>();
//        mandatoryParameters.add(surname);
//        mandatoryParameters.add(language);
//        mandatoryParameters.add(isPhysicalPerson.toString());
//
//
//        //Il faut surement pas faire ça ici. L'objet est vide du coup... isValid sera toujours bon
//        PYAddressDTO pyAddressDTO = new PYAddressDTO();
//        PYPaymentAddressDTO pyPaymentAddressDTO = new PYPaymentAddressDTO();
//
//        if (Boolean.FALSE.equals(isPhysicalPerson)) {
//            return (
//                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
//                            && pyAddressDTO.isValid(this)
//                            && pyPaymentAddressDTO.isValid(this)
//                            && PYValidateDTO.isValidForCreation(this)
//            );
//        } else if (Boolean.TRUE.equals(isPhysicalPerson)) {
//            mandatoryParameters.add(title);
//            mandatoryParameters.add(name);
//            mandatoryParameters.add(nss);
//            mandatoryParameters.add(birthDate);
//            mandatoryParameters.add(civilStatus);
//            return (
//                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
//                            && pyAddressDTO.isValid(this)
//                            && pyPaymentAddressDTO.isValid(this)
//                            && PYValidateDTO.isValidForCreation(this)
//            );
//        }
//        return false;
        return true;
    }

    @JsonIgnore
    public Boolean isValidForUpdate() {
        return true;
    }

    @JsonIgnore
    public Boolean isValidForDeletion() {
        return true;
    }
}