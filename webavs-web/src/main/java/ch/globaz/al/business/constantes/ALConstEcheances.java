package ch.globaz.al.business.constantes;

/**
 * Constantes li�es � l'impression des �ch�ances
 * 
 * @author PTA
 * 
 */
public interface ALConstEcheances {

    /**
     * jour fin mois f�vrier 28 jours
     */
    public static final String JOUR_FIN_FEVR = "28";
    /**
     * jour fin mois f�vrier bissextile 29 jours
     */
    public static final String JOUR_FIN_FEVR_BISS = "29";

    /**
     * jour fin mois 30 JOURS
     */
    public static final String JOUR_FIN_MOIS_TRENTE = "30";
    /**
     * jour fin mois 31 JOURS
     */
    public static final String JOUR_FIN_MOIS_TRENTE_UN = "31";

    /**
     * liste des �ch�ances sans avis (protocole d'�ch�ances)
     */
    public static final String LISTE_AUTRES_ECHEANCES = "2";

    /**
     * liste des �ch�ances avec avis d'�ch�ances (impression de la liste)
     */
    public static final String LISTE_AVIS_ECHEANCES = "1";

    /**
     * liste destin� � l'impression (
     */
    public static final String LISTE_IMPRESSION = "1";
    /**
     * liste provisoire
     */
    public static final String LISTE_PROVISOIRE = "2";
    /**
     * nombre de jour 0 pour validite d'un dossier
     */

    public static final String NBRE_JOUR_DOSSIER_ZERO = "0";
    /**
     * nombre de jour comptablisation pour un mois af : 30
     */

    public static final int NBRE_JOUR_MOIS_AF = 30;

    /**
     * nombre de jour 1
     */
    public static final String NBRE_JOUR_UN = "1";
    /**
     * 
     * nombre de jour fin � ajouter pour les mois en dessous de 30 jour
     ***/

    public static int NOMBRE_JOUR_AJOUT = 2;
    /**
     * type de paiement (Bonification): direct et indirect
     */
    public static final String TYPE_ALL = "3";
    /**
     * type de paiement direct
     */
    public static final String TYPE_DIRECT = "1";
    /**
     * type de paiement indirect
     */
    public static final String TYPE_INDIRECT = "2";
    
    public static final String MOTIF_E411 = "al.motifE411";
    
}
