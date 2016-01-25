package globaz.prestation.enums.ged;

/**
 * Ce type �num�r� est la pour lister les diff�rentes propri�t�s li�es � la GED
 * 
 * @author lga
 */
public enum PRGedProperties {

    /**
     * Num�ro AVS non format�
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
