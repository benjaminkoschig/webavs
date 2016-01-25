package globaz.apg.api.alfa.helper;

import globaz.apg.api.alfa.IAPBouclementAlfa;
import globaz.apg.api.alfa.IAPBouclementAlfaLoader;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IAPBouclementAlfaLoaderHelper extends GlobazHelper implements IAPBouclementAlfaLoader {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ENTITY_CLASS_NAME = "globaz.apg.db.alfa.APBouclementAlfa";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IAPBouclementAlfaLoaderHelper.
     */
    public IAPBouclementAlfaLoaderHelper() {
        super(IAPBouclementAlfaLoaderHelper.ENTITY_CLASS_NAME);
    }

    /**
     * Crée une nouvelle instance de la classe IAPBouclementAlfaLoaderHelper.
     * 
     * @param valueObject
     *            DOCUMENT ME!
     */
    public IAPBouclementAlfaLoaderHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge toutes les informations necessaires au bouclement ALFA des caisses horlogeres.
     * 
     * @param mois
     *            DOCUMENT ME!
     * @param annee
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IAPBouclementAlfa[] load(String mois, String annee) throws Exception {
        return (IAPBouclementAlfa[]) _getObject("load", new Object[] { mois, annee });
    }

}
