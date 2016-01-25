package globaz.pegasus.utils.calculmoissuivant;

/**
 * POJO encapsulant les proprietes des donnes financieres a afficher avant le calcul PC
 * 
 * @author sce
 * 
 */
public class DonneeFinancierePropertiesDescriptor implements Comparable {

    private String i18nLabel = null;// label a afficher
    private boolean isCSToTranslate = false; // définit si la valeur est un code système à traduire
    private String propertyName = null;// nom de la propriete pour reflection
    private int propertyPosition;// position de la propriete dans le flux d'affuchage html

    /**
     * Constructeur standard Par défaut la gestion de la valeur est défini comme n'étant pas un code système
     * 
     * @param propertyName
     * @param i18nLabel
     * @param propertyPosition
     */
    public DonneeFinancierePropertiesDescriptor(String propertyName, String i18nLabel, int propertyPosition) {
        super();
        this.propertyName = propertyName;
        this.i18nLabel = i18nLabel;
        this.propertyPosition = propertyPosition;
    }

    /**
     * Constructeur permettant de définir que la valeur est un code système à traduire, ou pas. Par défaut si c'est une
     * valeur non code système, utiliser le constructeur standard
     * 
     * @param propertyName
     * @param i18nLabel
     * @param propertyPosition
     * @param isCSToTranslate
     */
    public DonneeFinancierePropertiesDescriptor(String propertyName, String i18nLabel, int propertyPosition,
            boolean isCSToTranslate) {
        super();
        this.propertyName = propertyName;
        this.i18nLabel = i18nLabel;
        this.propertyPosition = propertyPosition;
        this.isCSToTranslate = isCSToTranslate;
    }

    @Override
    public int compareTo(Object o) {
        int position_1 = ((DonneeFinancierePropertiesDescriptor) o).getPropertyPosition();
        int position_2 = getPropertyPosition();

        if (position_1 > position_2) {
            return -1;
        } else if (position_1 == position_2) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getI18nLabel() {
        return i18nLabel;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public int getPropertyPosition() {
        return propertyPosition;
    }

    public boolean isCSToTranslate() {
        return isCSToTranslate;
    }

    public void setCSToTranslate(boolean isCSToTranslate) {
        this.isCSToTranslate = isCSToTranslate;
    }

    public void setI18nLabel(String i18nLabel) {
        this.i18nLabel = i18nLabel;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPropertyPosition(int propertyPosition) {
        this.propertyPosition = propertyPosition;
    }

}
