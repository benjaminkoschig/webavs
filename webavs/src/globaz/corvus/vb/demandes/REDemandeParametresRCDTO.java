/*
 * Créé le 10 avril 08
 */
package globaz.corvus.vb.demandes;

import java.io.Serializable;

/**
 * @author jje
 * 
 *         DTO permettant de stocker les valeurs des listes de l'écran de recherche d'une demande utilisable pour
 *         stocker les données, et les ré-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la sélection du bouton "rechercher"
 */
public class REDemandeParametresRCDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean enCours = false;
    private String forCsEtatDemande = "";

    private String forCsSexe = "";

    private String forCsType = "";

    private String forDateNaissance = "";

    private String forDroitAu = "";

    // private String forDateDebut = "";
    private String forDroitDu = "";
    private String forIdGestionnaire = "";
    private String idDemande = "";
    private String idRenteAccordee = "";
    private String isDemandesHistorisees = "";
    private String isRechercheFamille = "";
    private String likeNom = "";
    private String likePrenom = "";

    /**
     * Crée une nouvelle instance de la classe REDemandeParametresRCDTO.
     */
    public REDemandeParametresRCDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe REDemandeParametresRCDTO.
     * 
     * @param paramPrononceDTO
     */
    public REDemandeParametresRCDTO(REDemandeParametresRCDTO paramDemandeDTO) {
        // forDateDebut = paramDemandeDTO.forDateDebut;
        forDroitDu = paramDemandeDTO.forDroitDu;
        forDroitAu = paramDemandeDTO.forDroitAu;
        enCours = paramDemandeDTO.enCours;
        likeNom = paramDemandeDTO.likeNom;
        likePrenom = paramDemandeDTO.likePrenom;
        forDateNaissance = paramDemandeDTO.forDateNaissance;
        forCsSexe = paramDemandeDTO.forCsSexe;
        forCsType = paramDemandeDTO.forCsType;
        forCsEtatDemande = paramDemandeDTO.forCsEtatDemande;
        forIdGestionnaire = paramDemandeDTO.forIdGestionnaire;
        isDemandesHistorisees = paramDemandeDTO.isDemandesHistorisees;
        isRechercheFamille = paramDemandeDTO.isRechercheFamille;
        idDemande = paramDemandeDTO.idDemande;
        idRenteAccordee = paramDemandeDTO.idRenteAccordee;
    }

    public boolean getEnCours() {
        return enCours;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsType() {
        return forCsType;
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDroitAu() {
        return forDroitAu;
    }

    public String getForDroitDu() {
        return forDroitDu;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIsDemandesHistorisees() {
        return isDemandesHistorisees;
    }

    public String getIsRechercheFamille() {
        return isRechercheFamille;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setEnCours(boolean enCours) {
        this.enCours = enCours;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDroitAu(String forDroitAu) {
        this.forDroitAu = forDroitAu;
    }

    public void setForDroitDu(String forDroitDu) {
        this.forDroitDu = forDroitDu;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIsDemandesHistorisees(String isDemandesHistorisees) {
        this.isDemandesHistorisees = isDemandesHistorisees;
    }

    public void setIsRechercheFamille(String isRechercheFamille) {
        this.isRechercheFamille = isRechercheFamille;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

}
