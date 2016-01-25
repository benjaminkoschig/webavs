/*
 * Cr�� le 15 mars 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.ij.vb.prestations;

import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker les valeurs des listes de l'�cran de recherche d'une prestation utilisable pour
 *         stocker les donn�es, et les r�-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la s�lection du bouton "rechercher"
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
     * Cr�e une nouvelle instance de la classe IJPrononceParametresRCDTO.
     */
    public IJPrestationParametresRCDTO() {
    }

    /**
     * Cr�e une nouvelle instance de la classe IJPrononceParametresRCDTO.
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
