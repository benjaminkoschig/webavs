package globaz.apg.itext.decompte.utils;

/**
 * Cette classe définit les différents mode de regroupement des prestations
 * 
 * @author lga
 */
public enum APMethodeRegroupement {
    /**
     * Chaque type de prestation possède son propre décompte
     */
    SEPARE,
    /**
     * Les prestations Standard et ACM_NE sont regroupés sur un même décompte, les autres type de prestations sont sur
     * des décomptes séparés
     */
    STANDARD_ACM_NE
}
