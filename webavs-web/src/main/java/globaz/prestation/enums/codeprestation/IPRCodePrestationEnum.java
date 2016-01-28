package globaz.prestation.enums.codeprestation;

import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;

/**
 * Cette interface � pour but de d�finir les diff�rentes m�thodes permettant de connaitre les caract�ristiques d'une
 * prestation en fonction de son code
 * 
 * @see PRCodePrestationInvalidite
 * @see PRCodePrestationVieillesse
 * @see PRCodePrestationAPI
 * @see PRCodePrestationSurvivant
 * @author lga
 */
public interface IPRCodePrestationEnum {

    /**
     * Retourne le code prestation sous forme de valeur enti�re
     * 
     * @return le code prestation sous forme de valeur enti�re
     */
    public int getCodePrestation();

    /**
     * Retourne le code prestation sous forme de String
     * 
     * @return le code prestation sous forme de String
     */
    public String getCodePrestationAsString();

    /**
     * Retourne le domaine de la prestation associ� au code prestation
     * 
     * @return Le domaine de prestation associ� au code prestation
     */
    public PRDomainDePrestation getDomainDePrestation();

    /**
     * Retourne le type de la prestation associ� au code prestation
     * 
     * @return Retourne le domaine de prestation associ� au code prestation
     */
    public PRTypeCodePrestation getTypeDePrestation();

    /**
     * Renseigne si le code prestation est un code de prestation principale
     * 
     * @return True si le code prestation est un code de prestation principale
     */
    public boolean isPrestationPrincipale();

    /**
     * Renseigne si le code prestation est un code de prestation ordinaire
     * 
     * @return True si le code prestation est un code de prestation ordinaire
     */
    public boolean isPrestationOrdinaire();

    /**
     * Renseigne si le code prestation est un code de prestation pour enfant
     * 
     * @return True si le code prestation est un code de pour enfant
     */
    public boolean isPrestationPourEnfant();

}
