package globaz.naos.web.DTO;

import ch.globaz.jade.JadeBusinessServiceLocator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.naos.web.exceptions.AFInternalException;
import lombok.Data;

import java.util.Vector;

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


    // Famille de codes systmes pour vrification
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
        verifyCodeSystem(this.getGenreAffiliation(), FAMILLE_CS_GENRE_AFFILIATION);
        verifyCodeSystem(this.getMotifCreation(), FAMILLE_CS_MOTIF_CREATION);
        verifyCodeSystem(this.getPersonnaliteJuridique(), FAMILLE_CS_PERSONNALITE_JURIDIQUE);
        verifyCodeSystem(this.getPeriodicite(), FAMILLE_CS_PERIODICITE);
        verifyCodeSystem(this.getBrancheEconomique(), FAMILLE_CS_BRANCHE_ECONOMIQUE);
        verifyCodeSystem(this.getCodeNoga(), FAMILLE_CS_CODE_NOGA);
        verifyCodeSystem(this.getFacturationCodeFacturation(), FAMILLE_CS_CODE_FACTURATION);
        verifyCodeSystem(this.getMotifFin(), FAMILLE_CS_MOTIF_FIN);
        verifyCodeSystem(this.getDeclarationSalaire(), FAMILLE_CS_DECLARATION_SALAIRE);
        verifyCodeSystem(this.getAffiliationSecurisee(), FAMILLE_CS_ACCESS_SECURISE);

        return true;
    }

    private void verifyCodeSystem(String idCodeSystem, String famille) {
        if (!JadeStringUtil.isEmpty(idCodeSystem)) {
            try {
                //TODO Faut-il accepter de mettre un code systme plus actif ? (FWCOSP.PCODFI = 1)
                JadeBusinessServiceLocator.getCodeSystemeService()
                        .getFamilleCodeSysteme(famille).stream()
                        .filter(cs -> idCodeSystem.equals(cs.getIdCodeSysteme()))
                        .findFirst()
                        .orElseThrow(() -> new AFBadRequestException("Le code \"" + idCodeSystem + "\" ne fait pas partie de la famille \"" + famille + "\""));
            } catch (JadePersistenceException e) {
                throw new AFInternalException("Erreur lors de la vérification du code system \" " + idCodeSystem + " \" => ", e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new AFInternalException("Erreur lors de la vérification du code system \" " + idCodeSystem + " \" => ", e);
            }
        }
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