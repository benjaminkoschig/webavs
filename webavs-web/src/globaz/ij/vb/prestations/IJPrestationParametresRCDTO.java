/*
 * Créé le 15 mars 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.vb.prestations;

import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker les valeurs des listes de l'écran de recherche d'une prestation utilisable pour
 *         stocker les données, et les ré-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la sélection du bouton "rechercher"
 */
public class IJPrestationParametresRCDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String datePaiement = "";
    private String etatPrestation = "";
    private String noIndemnisation = "";
    private String noLot = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     */
    public IJPrestationParametresRCDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe IJPrononceParametresRCDTO.
     * 
     * @param paramPrononceDTO
     */
    public IJPrestationParametresRCDTO(IJPrestationParametresRCDTO paramPrestationDTO) {
        dateDebut = paramPrestationDTO.dateDebut;
        datePaiement = paramPrestationDTO.datePaiement;
        etatPrestation = paramPrestationDTO.etatPrestation;
        noLot = paramPrestationDTO.noLot;
        noIndemnisation = paramPrestationDTO.noIndemnisation;
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDatePaiement() {
        return datePaiement;
    }

    /**
     * @return
     */
    public String getEtatPrestation() {
        return etatPrestation;
    }

    /**
     * @return
     */
    public String getNoIndemnisation() {
        return noIndemnisation;
    }

    /**
     * @return
     */
    public String getNoLot() {
        return noLot;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDatePaiement(String string) {
        datePaiement = string;
    }

    /**
     * @param string
     */
    public void setEtatPrestation(String string) {
        etatPrestation = string;
    }

    /**
     * @param string
     */
    public void setNoIndemnisation(String string) {
        noIndemnisation = string;
    }

    /**
     * @param string
     */
    public void setNoLot(String string) {
        noLot = string;
    }

}
