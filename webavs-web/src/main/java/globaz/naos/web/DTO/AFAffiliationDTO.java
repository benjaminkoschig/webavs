package globaz.naos.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.web.exceptions.AFBadRequestException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Data
public class AFAffiliationDTO {
    private String id;
    private String idTiers;
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


    // Famille de codes systèmes pour vérification
    public static final String FAMILLE_CS_MOTIF_CREATION = "VEMOTIFAFF";
    public static final String FAMILLE_CS_GENRE_AFFILIATION = "VETYPEAFFI";
    public static final String FAMILLE_CS_PERSONNALITE_JURIDIQUE = "VEPERSONNA";
    public static final String FAMILLE_CS_PERIODICITE = "VEPERIODIC";
    public static final String FAMILLE_CS_BRANCHE_ECONOMIQUE = "VEBRANCHEE";
    public static final String FAMILLE_CS_CODE_NOGA = "VENOGAVAL";
    public static final String FAMILLE_CS_CODE_FACTURATION = "VECODEFACT";
    public static final String FAMILLE_CS_MOTIF_FIN = "VEMOTIFFIN";
    public static final String FAMILLE_CS_DECLARATION_SALAIRE = "VEDECLARAT";
    public static final String FAMILLE_CS_ACCESS_SECURISE = "CISECURI";

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
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(idTiers);
        mandatoryParameters.add(numeroAffilie);
        mandatoryParameters.add(dateDebutAffiliation);
        mandatoryParameters.add(genreAffiliation);
        mandatoryParameters.add(motifCreation);
        mandatoryParameters.add(personnaliteJuridique);
        mandatoryParameters.add(periodicite);
        mandatoryParameters.add(brancheEconomique);
        mandatoryParameters.add(affiliationSecurisee);

        return (mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty) && isValidCodesSystem());
    }

    private Boolean isValidCodesSystem() {
        AFValidateDTO.verifyCodeSystem(this.getGenreAffiliation(), FAMILLE_CS_GENRE_AFFILIATION);
        AFValidateDTO.verifyCodeSystem(this.getMotifCreation(), FAMILLE_CS_MOTIF_CREATION);
        AFValidateDTO.verifyCodeSystem(this.getPersonnaliteJuridique(), FAMILLE_CS_PERSONNALITE_JURIDIQUE);
        AFValidateDTO.verifyCodeSystem(this.getPeriodicite(), FAMILLE_CS_PERIODICITE);
        AFValidateDTO.verifyCodeSystem(this.getBrancheEconomique(), FAMILLE_CS_BRANCHE_ECONOMIQUE);
        AFValidateDTO.verifyCodeSystem(this.getCodeNoga(), FAMILLE_CS_CODE_NOGA);
        AFValidateDTO.verifyCodeSystem(this.getFacturationCodeFacturation(), FAMILLE_CS_CODE_FACTURATION);
        AFValidateDTO.verifyCodeSystem(this.getMotifFin(), FAMILLE_CS_MOTIF_FIN);
        AFValidateDTO.verifyCodeSystem(this.getDeclarationSalaire(), FAMILLE_CS_DECLARATION_SALAIRE);
        AFValidateDTO.verifyCodeSystem(this.getAffiliationSecurisee(), FAMILLE_CS_ACCESS_SECURISE);

        return true;
    }

    @JsonIgnore
    public Boolean isValidForUpdate() {
        // On vérifie que l'on aie bien un id OU un numéro d'affilié
        // Si on a les 2, c'est l'id qui prendra le dessus (voir dans AFExecuteService.retrieveAffiliation())
        Map<String, String> mapForValidator = new HashMap<>();
        if (!JadeStringUtil.isEmpty(this.getId())) {
            mapForValidator.put("id", this.getId());
        } else if (!JadeStringUtil.isEmpty(this.getNumeroAffilie())) {
            mapForValidator.put("numeroAffilie", this.getNumeroAffilie());
        } else {
            mapForValidator.put("id", this.getId());
            mapForValidator.put("numeroAffilie", this.getNumeroAffilie());
        }
        mapForValidator.put("idTiers", this.getIdTiers());
        AFValidateDTO.checkIfEmpty(mapForValidator);

        // On vérifie que les codes systèmes passés correspondent aux bonnes familles de CS
        AFValidateDTO.verifyCodeSystem(this.getGenreAffiliation(), FAMILLE_CS_GENRE_AFFILIATION);
        AFValidateDTO.verifyCodeSystem(this.getMotifCreation(), FAMILLE_CS_MOTIF_CREATION);
        AFValidateDTO.verifyCodeSystem(this.getPersonnaliteJuridique(), FAMILLE_CS_PERSONNALITE_JURIDIQUE);
        AFValidateDTO.verifyCodeSystem(this.getPeriodicite(), FAMILLE_CS_PERIODICITE);
        AFValidateDTO.verifyCodeSystem(this.getBrancheEconomique(), FAMILLE_CS_BRANCHE_ECONOMIQUE);
        AFValidateDTO.verifyCodeSystem(this.getCodeNoga(), FAMILLE_CS_CODE_NOGA);
        AFValidateDTO.verifyCodeSystem(this.getFacturationCodeFacturation(), FAMILLE_CS_CODE_FACTURATION);
        AFValidateDTO.verifyCodeSystem(this.getMotifFin(), FAMILLE_CS_MOTIF_FIN);
        AFValidateDTO.verifyCodeSystem(this.getDeclarationSalaire(), FAMILLE_CS_DECLARATION_SALAIRE);
        AFValidateDTO.verifyCodeSystem(this.getAffiliationSecurisee(), FAMILLE_CS_ACCESS_SECURISE);

        return true;
    }



    @JsonIgnore
    public Boolean isValidForDeletion() {
        return true;
    }
}