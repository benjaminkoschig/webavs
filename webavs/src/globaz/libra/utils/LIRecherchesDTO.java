package globaz.libra.utils;

import java.io.Serializable;

/**
 * Classe LIRecherchesDTO
 * 
 * ==> Permet de stocker � travers la session de l'utilisateur diff�rents param�tres de la recherche pour faciliter la
 * transitions entre les �crans de l'application
 * 
 * @author HPE
 * 
 */
public class LIRecherchesDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Pour domaine et utilisateur
    private String idDomaine = new String();
    // Pour la gestion de la recherche sur un dossier dans les �ch�ances et les
    // journalisations
    private String idDossier = new String();
    private String idGroupe = new String();

    // Pour la gestion de la recerche sur un tiers dans les journalisations
    private String idTiers = new String();

    // Pour la gestion de la recherche sur un utilisateur dans les
    // journalisations
    private String idUser = new String();
    private String idUtilisateur = new String();

    private boolean isRechercheDossier = false;
    private boolean isRechercheTiers = false;

    private boolean isRechercheUser = false;
    // Pour lien "Retour � votre application"
    private String urlRetour = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe LIRecherchesDTO.
     */
    public LIRecherchesDTO() {
    }

    /**
     * Cr�e une nouvelle instance de la classe LIRecherchesDTO.
     * 
     * @param LIRecherchesDTO
     */
    public LIRecherchesDTO(LIRecherchesDTO paramRecherchesDTO) {

        idDomaine = paramRecherchesDTO.idDomaine;
        idGroupe = paramRecherchesDTO.idGroupe;
        idUtilisateur = paramRecherchesDTO.idUtilisateur;
        urlRetour = paramRecherchesDTO.urlRetour;
        idDossier = paramRecherchesDTO.idDossier;
        isRechercheDossier = paramRecherchesDTO.isRechercheDossier;
        idTiers = paramRecherchesDTO.idTiers;
        isRechercheTiers = paramRecherchesDTO.isRechercheTiers;

    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getUrlRetour() {
        return urlRetour;
    }

    public boolean isRechercheDossier() {
        return isRechercheDossier;
    }

    public boolean isRechercheTiers() {
        return isRechercheTiers;
    }

    public boolean isRechercheUser() {
        return isRechercheUser;
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public void setRechercheDossier(boolean isRechercheDossier) {
        this.isRechercheDossier = isRechercheDossier;
    }

    public void setRechercheTiers(boolean isRechercheTiers) {
        this.isRechercheTiers = isRechercheTiers;
    }

    public void setRechercheUser(boolean isRechercheUser) {
        this.isRechercheUser = isRechercheUser;
    }

    public void setUrlRetour(String urlRetour) {
        this.urlRetour = urlRetour;
    }

}
