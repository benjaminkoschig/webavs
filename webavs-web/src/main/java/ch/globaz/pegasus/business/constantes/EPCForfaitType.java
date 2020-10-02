package ch.globaz.pegasus.business.constantes;

public enum EPCForfaitType {

    LAMAL("LAMAL", 1),
    LOYER("LOYER", 2);

    public static EPCForfaitType getEnumByCode(Integer code) {
        if (EPCForfaitType.LAMAL.code == code) {
            return EPCForfaitType.LAMAL;
        } else if ((EPCForfaitType.LOYER.code == code)) {
            return EPCForfaitType.LOYER;
        }
        throw new IllegalArgumentException("No Enum specified for this code " + code);
    }

    public static EPCForfaitType getEnumByName(String name) {
        if (EPCForfaitType.LAMAL.name.equals(name)) {
            return EPCForfaitType.LAMAL;
        } else if (EPCForfaitType.LOYER.name.equals(name)) {
            return EPCForfaitType.LOYER;
        }
        throw new IllegalArgumentException("No Enum specified for this name " + name);
    }

    private Integer code;
    private String name;

    EPCForfaitType(String name, Integer code) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
