package globaz.apg.enums;

/**
 * Classe énumérée pour le stockage des constantes liées aux APG et à la maternité
 * 
 * @author lga
 */
public enum APConstantes {

    /**
     * Clé utilisée pour stocker/récupérer le DTO dans la session.
     */
    ANNONCE_DTO("APANNONCE_DTO"),
    /**
     * Paramètre transmis dans la requête pour spécifier si oui ou non il faut récupérer les DTO lors de l'affichage de
     * la page des annonces.
     */
    PARAMETER_DO_LOAD_ANNONCE_DTO("loadAPAnnonceDTO");

    private String value;

    private APConstantes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
