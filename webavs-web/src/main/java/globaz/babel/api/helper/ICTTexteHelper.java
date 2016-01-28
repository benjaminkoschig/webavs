package globaz.babel.api.helper;

import globaz.babel.api.ICTTexte;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.webavs.common.CommonProperties;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe qui aide a rechercher des valeurs dans un value object de type CTDocumentAPIAdapter.
 * </p>
 * 
 * @author vre
 */
public class ICTTexteHelper extends GlobazHelper implements ICTTexte {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final String PROP_DESCRIPTION = "description";
    static final String PROP_IS_STYLED_TEXT = "isStyledDocument";
    static final String PROP_POSITION = "position";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe ICTTexteHelper.
     * 
     * @param valueObject
     *            DOCUMENT ME!
     */
    public ICTTexteHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Crée une nouvelle instance de la classe ICTTexteHelper.
     * 
     * @param implementationClassName
     *            DOCUMENT ME!
     */
    public ICTTexteHelper(String implementationClassName) {
        super(implementationClassName);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut code iso langue
     * 
     * @return la valeur courante de l'attribut code iso langue
     */
    @Override
    public String getCodeIsoLangue() {
        return getProp(ICTDocumentHelper.PROP_CODE_ISO_LANGUE);
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    @Override
    public String getDescription() {

        Boolean isStyledText = (Boolean) _getValueObject().getProperty(ICTTexteHelper.PROP_IS_STYLED_TEXT);

        if (isStyledText.booleanValue()) {
            return CTHtmlConverter.htmlToIText(getProp(ICTTexteHelper.PROP_DESCRIPTION),
                    getProp(CommonProperties.KEY_NO_CAISSE));
        } else {
            return getProp(ICTTexteHelper.PROP_DESCRIPTION);
        }
    }

    @Override
    public String getDescriptionBrut() {
        return getProp(ICTTexteHelper.PROP_DESCRIPTION);
    }

    /**
     * getter pour l'attribut niveau
     * 
     * @return la valeur courante de l'attribut niveau
     */
    @Override
    public String getNiveau() {
        return getProp(ICTDocumentHelper.PROP_NIVEAU);
    }

    /**
     * getter pour l'attribut position
     * 
     * @return la valeur courante de l'attribut position
     */
    @Override
    public String getPosition() {
        return getProp(ICTTexteHelper.PROP_POSITION);
    }

    private String getProp(String name) {
        return (String) _getValueObject().getProperty(name);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        try {
            return getDescription();
        } catch (Exception e) {
            return "Error retrieving description " + e.getMessage();
        }
    }
}
