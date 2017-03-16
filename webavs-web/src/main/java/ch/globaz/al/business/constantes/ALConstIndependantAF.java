/**
 * 
 */
package ch.globaz.al.business.constantes;

/**
 * Constante lié aux indépendants ayant reçu des AF à tort
 * 
 * @author est
 * 
 */
public interface ALConstIndependantAF {
    /***
     * Constantes utiles pour le controle de l'année de début de la prise en compte
     */
    public static final String ANNEE_DECISION_MINIMALE = "2013";
    public static final String DATE_AMJ_DECISION_MINIMAL = "20130101";

    /***
     * CS pour les controles des genre d'affilies à prendre en compte
     */
    public static final String CS_GENRE_INDEPENDANT = "804001";
    public static final String CS_GENRE_INDEPENDANT_ET_EMPLOYEUR = "804005";

    /***
     * CS pour les controles des types de décisions
     */
    public static final String CS_TYPE_DECISION_DEFINITIF = "605002";
    public static final String CS_TYPE_DECISION_RECTIFICATION = "605004";

    /**
     * CS pour le controle du revenu determinant(net)
     */
    public static final String CS_REVENU_DETERMINANT = "600019";
}
