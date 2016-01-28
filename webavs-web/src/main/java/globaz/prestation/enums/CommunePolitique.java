package globaz.prestation.enums;

public enum CommunePolitique {

    /**
     * Retourne la clé du label : commune politique non trouvée
     * key = clé du label de traduction
     */
    LABEL_COMMUNE_POLITIQUE_NOT_FOUND("COMMUNE_POLITIQUE_NON_TROUVEE"),
    LABEL_COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT("COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT"),
    /**
     * La clé de label pour les tires de colonne : Commune politique
     */
    LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE("COMMUNE_POLITIQUE_TITRE_COLONNE"),

    LABEL_COMMUNE_POLITIQUE_UTILISATEUR("COMMUNE_POLITIQUE_UTILISATEUR"),

    /**
     * Le code système pyxis pour les liens 'communes politiques'
     * key = valeur du code système
     */
    // FIXME la valeur du code système n'est pas correcte (agence communale)
    CS_PYXIS_COMMUNE_POLITIQUE("507007");

    private String key;

    private CommunePolitique(String labelKey) {
        key = labelKey;
    }

    public String getKey() {
        return key;
    }
}
