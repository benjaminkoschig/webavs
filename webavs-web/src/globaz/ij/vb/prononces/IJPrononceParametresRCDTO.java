/*
 * Créé le 14 mars 06
 */
package globaz.ij.vb.prononces;

import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker les valeurs des listes de l'écran de recherche d'un prononcé utilisable pour
 *         stocker les données, et les ré-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la sélection du bouton "rechercher"
 */
public class IJPrononceParametresRCDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatDemande = "";
    private String etatPrononce = "";
    private String orderBy = "";
    private String responsable = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     */
    public IJPrononceParametresRCDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     * 
     * @param paramPrononceDTO
     */
    public IJPrononceParametresRCDTO(IJPrononceParametresRCDTO paramPrononceDTO) {
        responsable = paramPrononceDTO.responsable;
        etatDemande = paramPrononceDTO.etatDemande;
        etatPrononce = paramPrononceDTO.etatPrononce;
        orderBy = paramPrononceDTO.orderBy;
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getEtatDemande() {
        return etatDemande;
    }

    /**
     * @return
     */
    public String getEtatPrononce() {
        return etatPrononce;
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
    public void setEtatPrononce(String string) {
        etatPrononce = string;
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

}
