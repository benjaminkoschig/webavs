package ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere;

public class MonnaieEtrangereType /* implements CodeSystemEnum<MonnaieEtrangereType> */implements
        Comparable<MonnaieEtrangereType> {
    public static final MonnaieEtrangereType FRANC_SUISSE = new MonnaieEtrangereType("510002");
    public static final MonnaieEtrangereType INDEFINIT = new MonnaieEtrangereType("0");
    public static final MonnaieEtrangereType EURO = new MonnaieEtrangereType("510003");

    // EURO("510003"),
    // DOLLAR_US("510004"),
    // DOLLAR_AUSTRALIEN("510013"),
    // DOLLAR_CANADIEN("510014"),
    // COURONNE_TCHEQUE("510041"),
    // COURONNE_DANOISE("510044"),
    // DINAR_ALGERIEN("510046"),
    // LIVRE_STERLING("510057"),
    // DOLLAR_DE_HONG_KONG("510066"),
    // COURONNE_ISLANDAISE("510077"),
    // YEN("510081"),
    // DIRHAM("510100"),
    // COURONNE_NORVEGIENNE("510119"),
    // ZLOTY("510130"),
    // COURONNE_SUEDOISE("510139"),
    // DOLLAR_DE_SINGAPOUR("510140"),
    // COURONNE_SLOVAQUE("510143"),
    // DINAR_TUNISIEN("510154"),
    // RAND("510175"),
    // INDEFINIT("0");

    private final String value;

    MonnaieEtrangereType(final String value) {
        this.value = value;
    }

    public static MonnaieEtrangereType fromValue(final String value) {
        return new MonnaieEtrangereType(value);
        // return CodeSystemEnumUtils.valueOfById(value, MonnaieEtrangereType.class);
    }

    public boolean isFrancSuisse() {
        return equals(FRANC_SUISSE);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MonnaieEtrangereType other = (MonnaieEtrangereType) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonnaieEtrangereType [value=" + value + "]";
    }

    @Override
    public int compareTo(MonnaieEtrangereType o) {
        return Integer.valueOf(value).compareTo(Integer.valueOf(o.getValue()));
    }

}
