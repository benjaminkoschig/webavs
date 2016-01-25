/*
 * Cr�� le 13 mars 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.prestation;

import java.io.Serializable;

/**
 * @author hpe
 * 
 *         DTO permettant de stocker les valeurs des listes de l'�cran de recherche d'un droit utilisable pour stocker
 *         les donn�es, et les r�-afficher lorsque l'utilisateur revient sur la page...
 * 
 *         Validation lors de la s�lection du bouton "rechercher"
 */
public class APPrestationParametresRCDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatPrestation = "";
    private String noDroit = "";
    private String noLot = "";
    private String periodeDateDebut = "";
    private String periodeDateFin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe APDroitDTO.
     */
    public APPrestationParametresRCDTO() {
    }

    /**
     * Cr�e une nouvelle instance de la classe APDroitDTO.
     * 
     * @param droitDTO
     *            DOCUMENT ME!
     */
    public APPrestationParametresRCDTO(APPrestationParametresRCDTO paramDPrestDTO) {
        periodeDateDebut = paramDPrestDTO.periodeDateDebut;
        periodeDateFin = paramDPrestDTO.periodeDateFin;
        etatPrestation = paramDPrestDTO.etatPrestation;
        noLot = paramDPrestDTO.noLot;
        noDroit = paramDPrestDTO.noDroit;
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
    public String getNoDroit() {
        return noDroit;
    }

    /**
     * @return
     */
    public String getNoLot() {
        return noLot;
    }

    public String getPeriodeDateDebut() {
        return periodeDateDebut;
    }

    public String getPeriodeDateFin() {
        return periodeDateFin;
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
    public void setNoDroit(String string) {
        noDroit = string;
    }

    /**
     * @param string
     */
    public void setNoLot(String string) {
        noLot = string;
    }

    public void setPeriodeDateDebut(String periodeDateDebut) {
        this.periodeDateDebut = periodeDateDebut;
    }

    public void setPeriodeDateFin(String periodeDateFin) {
        this.periodeDateFin = periodeDateFin;
    }

}
