package ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions;

import ch.globaz.common.document.babel.BabelTextDefinition;

public enum PlanCalculTextDefinition implements BabelTextDefinition {

    HOME_LABEL(2, 1, "Label du home. Exemple: Home"),
    HOME_DESCRIPTION(2, 2,
            "Variables existantes: {DESIGNATION} {NPA} {LOCALITE} {NOM_BATIMENT} {NUMERO} {EST_HORS_CANTON} {ID}");

    private int niveau;
    private int position;
    private String description;

    private PlanCalculTextDefinition(int niveau, int position) {
        this.niveau = niveau;
        this.position = position;
    }

    private PlanCalculTextDefinition(int niveau, int position, String description) {
        this.niveau = niveau;
        this.position = position;
        this.description = description;
    }

    @Override
    public int getNiveau() {
        return niveau;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String getKey() {
        return toString();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isForcedValue() {
        return false;
    }

    @Override
    public String getValue() {
        return null;
    }

}
