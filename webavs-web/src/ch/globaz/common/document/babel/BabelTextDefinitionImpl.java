package ch.globaz.common.document.babel;

public class BabelTextDefinitionImpl implements BabelTextDefinition {

    private final int niveau;
    private final int position;
    private final String key;
    private final String description;
    private final String value;
    private boolean useForceValue = false;

    public BabelTextDefinitionImpl(int niveau, int position, String key, String description) {
        super();
        this.niveau = niveau;
        this.position = position;
        this.key = key;
        this.description = description;
        value = null;
    }

    public BabelTextDefinitionImpl(String key) {
        super();
        value = null;
        niveau = 0;
        position = 0;
        useForceValue = true;
        this.key = key;
        description = null;
    }

    public BabelTextDefinitionImpl(String value, String key, String description) {
        super();
        this.value = value;
        niveau = 0;
        position = 0;
        useForceValue = true;
        this.key = key;
        this.description = description;
    }

    @Override
    public int getNiveau() {
        return niveau;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "BabelTextDefinitionImpl [niveau=" + niveau + ", position=" + position + ", key=" + key
                + ", description=" + description + "]";
    }

    @Override
    public boolean isForcedValue() {
        return useForceValue;
    }

    @Override
    public String getValue() {
        return value;
    }

}