/*
 * Créé le 21 juin 06
 */
package globaz.apg.api.prestation.helper;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPPrestationLoader;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;

/**
 * @author hpe
 * 
 *         Helper de liaison entre APG et APG Interface pour les prestations
 * 
 */
public class IAPPrestationLoaderHelper extends GlobazHelper implements IAPPrestationLoader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ENTITY_CLASS_NAME = "globaz.apg.db.prestation.APPrestation";

    /**
     * Crée une nouvelle instance de la classe IAPPrestationHelper.
     */
    public IAPPrestationLoaderHelper() {
        super(IAPPrestationLoaderHelper.ENTITY_CLASS_NAME);
    }

    /**
     * @param valueObject
     */
    public IAPPrestationLoaderHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.prestation.IAPPrestation#load(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public IAPPrestation[] load(String idDroit, String genrePrestation, String orderBy) throws Exception {
        return (IAPPrestation[]) _getObject("load", new Object[] { idDroit, genrePrestation, orderBy });
    }
}
