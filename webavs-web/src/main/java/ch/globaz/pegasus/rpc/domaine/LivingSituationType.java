package ch.globaz.pegasus.rpc.domaine;

public enum LivingSituationType {
    NORMAL("normal"),
    USUFRUCTUARY("usufructuary"),
    CONGREGATION("congregation");

    String type;

    LivingSituationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
