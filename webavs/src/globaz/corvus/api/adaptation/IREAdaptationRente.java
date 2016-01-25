package globaz.corvus.api.adaptation;

public interface IREAdaptationRente {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // TYPE PARAM POUR CONTROLE SAVE ADAPTATION
    public static final String CLE_CONTROLE_SAVE_ADAPTATION = "CTRLSAVEAD";
    // TYPE_STOCKAGE_DONNEES
    public static final String CS_GROUPE_TYPE_STOCKAGE_DONNES = "RETYDONAD";
    // TYPE_RENTES_AUGMENTEES
    public static final String CS_GROUPES_TYPE_RENTES_AUGMENTEES = "RETYRENAU";

    public static final String CS_TYPE_AUG_ADAPTATION_AUTO = "52857001";
    public static final String CS_TYPE_AUG_DECISIONS_DECEMBRE = "52857002";
    public static final String CS_TYPE_AUG_TRAITEMENT_MANUEL = "52857003";
    public static final String CS_TYPE_NON_AUGMENTEES = "52857004";
    public static final String CS_TYPE_PAIEMENT_FICTIF = "52856001";

    public static final String CS_TYPE_RECAP_AUGMENTATION = "52856002";

}
