package globaz.prestation.enums.ged;

/**
 * Ce type énuméré est la pour lister les différentes propriétés liées à la GED
 * 
 * @author lga
 */
public enum PRGedProperties {

    /**
     * Numéro AVS non formaté
     */
    PYXIS_NO_AVS_NON_FORMATTE("pyxis.tiers.numero.avs.non.formatte"),
    ANNEE("annee");

    private String propertyName;

    PRGedProperties(final String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
