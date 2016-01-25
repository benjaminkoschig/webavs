package globaz.babel.test;

import globaz.babel.db.cat.CTTypeDocument;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTInsertableTypeDocument extends CTTypeDocument {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * retourne vrai juste pour les tests
     * 
     * @return vrai juste pour les tests
     */
    @Override
    protected boolean _allowAdd() {
        return true;
    }

    /**
     * retourne vrai juste pour les tests
     * 
     * @return vrai juste pour les tests
     */
    @Override
    protected boolean _allowDelete() {
        return true;
    }

    /**
     * retourne vrai juste pour les tests
     * 
     * @return vrai juste pour les tests
     */
    @Override
    protected boolean _allowUpdate() {
        return true;
    }

    /**
     * retourne faux juste pour les tests
     * 
     * @return faux juste pour les tests
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }
}
