package globaz.leo.process;

import ch.globaz.common.document.babel.BabelTextDefinition;

public enum TexteMailRappel implements BabelTextDefinition {

    SUBJECT(1, 1, "Sujet du mail"),
    BODY(1, 2, "Core du mail");

    private int niveau;
    private int position;
    private String description;

    private TexteMailRappel(int niveau, int position, String description) {
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
