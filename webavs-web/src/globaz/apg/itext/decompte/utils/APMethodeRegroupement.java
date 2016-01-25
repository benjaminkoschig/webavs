package globaz.apg.itext.decompte.utils;

/**
 * Cette classe d�finit les diff�rents mode de regroupement des prestations
 * 
 * @author lga
 */
public enum APMethodeRegroupement {
    /**
     * Chaque type de prestation poss�de son propre d�compte
     */
    SEPARE,
    /**
     * Les prestations Standard et ACM_NE sont regroup�s sur un m�me d�compte, les autres type de prestations sont sur
     * des d�comptes s�par�s
     */
    STANDARD_ACM_NE
}
