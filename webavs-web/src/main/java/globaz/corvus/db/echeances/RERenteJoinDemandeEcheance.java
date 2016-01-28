package globaz.corvus.db.echeances;

import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;

/**
 * Rente spécifique pour les liste d'échéances.<br/>
 * Reprend quelques champs de {@link RERenteAccordee} et encapsule quelques informations de la demande de rente (et
 * spécifiquement si c'est une demande de vieillesse).
 * 
 * @author PBA
 */
public class RERenteJoinDemandeEcheance implements IRERenteEcheances {

    private String anneeAnticipation;
    private String codePrestation;
    private Set<String> codesCasSpeciaux;
    private String csEtat;
    private String csEtatDemandeRente;
    private String csGenreDroitApi;
    private String csTypeInfoComplementaire;
    private String dateDebutDroit;
    private String dateEcheance;
    private String dateFinDroit;
    private String dateRevocationAjournement;
    private String idPrestationAccordee;
    private String idTiersBeneficiaire;
    private Boolean isPrestationBloquee;
    private String montant;

    public RERenteJoinDemandeEcheance() {
        super();

        anneeAnticipation = "";
        codePrestation = "";
        codesCasSpeciaux = new HashSet<String>();
        csEtat = "";
        csEtatDemandeRente = "";
        csGenreDroitApi = "";
        csTypeInfoComplementaire = "";
        dateDebutDroit = "";
        dateEcheance = "";
        dateFinDroit = "";
        dateRevocationAjournement = "";
        idPrestationAccordee = "";
        idTiersBeneficiaire = "";
        isPrestationBloquee = Boolean.FALSE;
        montant = "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IRERenteEcheances) {
            IRERenteEcheances rente = (IRERenteEcheances) obj;
            return getIdPrestationAccordee().equals(rente.getIdPrestationAccordee());
        }
        return false;
    }

    @Override
    public String getAnneeAnticipation() {
        return anneeAnticipation;
    }

    @Override
    public String getCodePrestation() {
        return codePrestation;
    }

    @Override
    public Set<String> getCodesCasSpeciaux() {
        return codesCasSpeciaux;
    }

    @Override
    public String getCsEtat() {
        return csEtat;
    }

    @Override
    public String getCsEtatDemandeRente() {
        return csEtatDemandeRente;
    }

    @Override
    public String getCsGenreDroitApi() {
        return csGenreDroitApi;
    }

    @Override
    public String getCsTypeInfoComplementaire() {
        return csTypeInfoComplementaire;
    }

    @Override
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    @Override
    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    @Override
    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    @Override
    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    @Override
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    @Override
    public String getMontant() {
        return montant;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean isPrestationBloquee() {
        return isPrestationBloquee;
    }

    public void setAnneeAnticipation(String anneeAnticipation) {
        this.anneeAnticipation = anneeAnticipation;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCodesCasSpeciaux(Set<String> codesCasSpeciaux) {
        this.codesCasSpeciaux = codesCasSpeciaux;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsEtatDemandeRente(String csEtatDemandeRente) {
        this.csEtatDemandeRente = csEtatDemandeRente;
    }

    public void setCsGenreDroitApi(String csGenreDroitApi) {
        this.csGenreDroitApi = csGenreDroitApi;
    }

    public void setCsTypeInfoComplementaire(String csTypeInfoComplementaire) {
        this.csTypeInfoComplementaire = csTypeInfoComplementaire;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateRevocationAjournement(String dateRevocationAjournement) {
        this.dateRevocationAjournement = dateRevocationAjournement;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIsPrestationBloquee(Boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(IRERenteEcheances.class.getName());
        toStringBuilder.append("(").append(idPrestationAccordee).append(")");
        return toStringBuilder.toString();
    }
}
