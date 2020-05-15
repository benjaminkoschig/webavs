/*
 * Cr�� le 23 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.api.droits;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public interface IAPDroitMaternite {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * le nom du groupe de codes syst�mes repr�sentant le type de situation familiale pour un droit maternit�
     */
    public static final String CS_GROUPE_TYPE_SIT_FAM = "APTYPESITF";

    public final static String CS_REVISION_MATERNITE_2005 = "52002004";

    /** type enfant */
    public static final String CS_TYPE_ENFANT = "52004002";

    /** type p�re */
    public static final String CS_TYPE_PERE = "52004001";

    /** type conjoint */
    public static final String CS_TYPE_CONJOINT = "52004003";

}
