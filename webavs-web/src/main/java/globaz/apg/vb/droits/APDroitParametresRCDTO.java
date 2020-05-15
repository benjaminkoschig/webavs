/*
 * Cr�� le 13 mars 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker les valeurs des listes de l'�cran de recherche d'un droit utilisable pour stocker
 *         les donn�es, et les r�-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la s�lection du bouton "rechercher"
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
     * Cr�e une nouvelle instance de la classe APDroitDTO.
     */
    public APDroitParametresRCDTO() {
    }

    /**
     * Cr�e une nouvelle instance de la classe APDroitDTO.
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
