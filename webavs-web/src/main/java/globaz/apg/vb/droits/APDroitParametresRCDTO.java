/*
 * Créé le 13 mars 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker les valeurs des listes de l'écran de recherche d'un droit utilisable pour stocker
 *         les données, et les ré-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la sélection du bouton "rechercher"
 */

public class APDroitParametresRCDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatDemande = "";
    private String etatDroit = "";
    private String genreService = "";
    private String orderBy = "";
    private String responsable = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitDTO.
     */
    public APDroitParametresRCDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe APDroitDTO.
     * 
     * @param paramDroitDTO
     *            DOCUMENT ME!
     */
    public APDroitParametresRCDTO(APDroitParametresRCDTO paramDroitDTO) {
        responsable = paramDroitDTO.responsable;
        etatDemande = paramDroitDTO.etatDemande;
        etatDroit = paramDroitDTO.etatDroit;
        genreService = paramDroitDTO.genreService;
        orderBy = paramDroitDTO.orderBy;
    }

    /**
     * @return
     */
    public String getEtatDemande() {
        return etatDemande;
    }

    /**
     * @return
     */
    public String getEtatDroit() {
        return etatDroit;
    }

    /**
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @return
     */
    public String getResponsable() {
        return responsable;
    }

    /**
     * @param string
     */
    public void setEtatDemande(String string) {
        etatDemande = string;
    }

    /**
     * @param string
     */
    public void setEtatDroit(String string) {
        etatDroit = string;
    }

    /**
     * @param string
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

    /**
     * @param string
     */
    public void setResponsable(String string) {
        responsable = string;
    }

    public String getGenreService() {
        return genreService;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }
}
