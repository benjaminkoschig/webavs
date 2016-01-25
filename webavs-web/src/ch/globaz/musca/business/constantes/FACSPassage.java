package ch.globaz.musca.business.constantes;

public interface FACSPassage {
    /**
     * CS : groupe "status du passage"
     * 
     * @see FACSPassage#STATUS_ANNULE
     * @see FACSPassage#STATUS_IMPRIME
     * @see FACSPassage#STATUS_COMPTABILISE
     * @see FACSPassage#STATUS_NON_COMPTABILISE
     * @see FACSPassage#STATUS_OUVERT
     * @see FACSPassage#STATUS_TRAITEMENT
     * @see FACSPassage#STATUS_VALIDE
     * 
     */
    public static final String GROUP_STATUS = "10900002";
    /**
     * CS : groupe "type de facturation"
     * 
     * @see FACSPassage#TYPE_FACTU_EXTERNE
     * @see FACSPassage#TYPE_FACTU_INTERNE
     * @see FACSPassage#TYPE_FACTU_PERIODIQUE
     */

    public static final String GROUP_TYPE_FACTU = "10900001";
    /**
     * CS : status annulé
     */
    public static final String STATUS_ANNULE = "902006";
    /**
     * CS : status comptabilisé
     */
    public static final String STATUS_COMPTABILISE = "902003";
    /**
     * CS : status imprimé
     */
    public static final String STATUS_IMPRIME = "902004";
    /**
     * CS : status non comptabilisé
     */
    public static final String STATUS_NON_COMPTABILISE = "902005";
    /**
     * CS : status ouvert
     */
    public static final String STATUS_OUVERT = "902001";
    /**
     * CS : status en traitement
     */
    public static final String STATUS_TRAITEMENT = "902002";
    /**
     * CS : status validé
     */
    public static final String STATUS_VALIDE = "902007";
    /**
     * CS : type de facturation externe
     */
    public static final String TYPE_FACTU_EXTERNE = "901002";

    /**
     * CS : type de facturation interne
     */
    public static final String TYPE_FACTU_INTERNE = "901001";
    /**
     * CS : type de facturation périodique
     */
    public static final String TYPE_FACTU_PERIODIQUE = "901003";

}
