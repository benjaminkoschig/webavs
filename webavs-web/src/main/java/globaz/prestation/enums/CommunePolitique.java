package globaz.prestation.enums;

import ch.globaz.al.business.constantes.ALCSTiers;

public enum CommunePolitique {

    /**
     * Retourne la cl� du label : commune politique non trouv�e
     * key = cl� du label de traduction
     */
    LABEL_COMMUNE_POLITIQUE_NOT_FOUND("COMMUNE_POLITIQUE_NON_TROUVEE"),
    LABEL_COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT("COMMUNE_POLITIQUE_PLUSIEURS_RESULTAT"),
    /**
     * La cl� de label pour les tires de colonne : Commune politique
     */
    LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE("COMMUNE_POLITIQUE_TITRE_COLONNE"),

    LABEL_COMMUNE_POLITIQUE_UTILISATEUR("COMMUNE_POLITIQUE_UTILISATEUR"),

    /**
     * Le code syst�me pyxis pour les liens 'communes politiques'
     * key = valeur du code syst�me
     */
    // la valeur du code syst�me est pas correcte est agence communale
    CS_PYXIS_COMMUNE_POLITIQUE(ALCSTiers.TYPE_LIAISON_AG_COMMUNALE);

    private String key;

    private CommunePolitique(String labelKey) {
        key = labelKey;
    }

    public String getKey() {
        return key;
    }
}
