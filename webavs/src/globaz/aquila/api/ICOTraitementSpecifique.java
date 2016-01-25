package globaz.aquila.api;

/**
 * <H1>Description</H1>
 * <p>
 * Constantes pour un traitement spécifique.
 * </p>
 * 
 * @author vre
 */
public interface ICOTraitementSpecifique {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** structure type 70, creance. */
    String CS_CREANCE_70 = "5180002";

    /**
     * Famille de codes systèmes pour les types de structure du fichier de l'OP GE.
     */
    String CS_FAMILLE_TYPE_STR = "COTYPESTR";

    /** famille de cs pour le type de traitement sécifique. */
    public String CS_FAMILLE_TYPE_TSP = "COTYPETSP";

    /** Traitement spécifique pour la création du fichier de l'OP GE. */
    public String CS_RDP_FICHIER_OP_GE = "5170001";

    /** structure type 71, texte complément de créance. */
    String CS_TEXTE_CREANCE_71 = "5180003";

    /** structure type 11, texte débiteur. */
    String CS_TEXTE_DEBITEUR_11 = "5180001";
}
