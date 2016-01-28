package globaz.aquila.api;

/**
 * <H1>Description</H1>
 * <p>
 * Constantes pour un traitement sp�cifique.
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
     * Famille de codes syst�mes pour les types de structure du fichier de l'OP GE.
     */
    String CS_FAMILLE_TYPE_STR = "COTYPESTR";

    /** famille de cs pour le type de traitement s�cifique. */
    public String CS_FAMILLE_TYPE_TSP = "COTYPETSP";

    /** Traitement sp�cifique pour la cr�ation du fichier de l'OP GE. */
    public String CS_RDP_FICHIER_OP_GE = "5170001";

    /** structure type 71, texte compl�ment de cr�ance. */
    String CS_TEXTE_CREANCE_71 = "5180003";

    /** structure type 11, texte d�biteur. */
    String CS_TEXTE_DEBITEUR_11 = "5180001";
}
