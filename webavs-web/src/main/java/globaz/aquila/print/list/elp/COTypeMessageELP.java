package globaz.aquila.print.list.elp;

public enum COTypeMessageELP {

    SC("SC"),
    SP("SP"),
    RC("RC"),
    CR("CR"),
    CC("CC"),
    RR("RR"),
    SR("SR"),
    SA("SA"),
    PN("PN"),
    PR("PR"),
    IN("IN"),
    SN("SN"),
    DI("DI"),
    DR("DR"),
    SI("SI"),
    SD("SD"),
    OTHER("");

    private String value;

    private COTypeMessageELP(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static COTypeMessageELP getFromCode(String code) {
        for (final COTypeMessageELP type : COTypeMessageELP.values()) {
            if (type.value.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
