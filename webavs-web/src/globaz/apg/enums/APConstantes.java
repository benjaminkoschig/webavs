package globaz.apg.enums;

/**
 * Classe �num�r�e pour le stockage des constantes li�es aux APG et � la maternit�
 * 
 * @author lga
 */
public enum APConstantes {

    /**
     * Cl� utilis�e pour stocker/r�cup�rer le DTO dans la session.
     */
    ANNONCE_DTO("APANNONCE_DTO"),
    /**
     * Param�tre transmis dans la requ�te pour sp�cifier si oui ou non il faut r�cup�rer les DTO lors de l'affichage de
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
