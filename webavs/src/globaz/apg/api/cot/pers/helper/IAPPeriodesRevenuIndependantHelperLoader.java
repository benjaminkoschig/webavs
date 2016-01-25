/*
 * Créé le 5 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.api.cot.pers.helper;

import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant;
import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependantLoader;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class IAPPeriodesRevenuIndependantHelperLoader extends GlobazHelper implements
        IAPPeriodesRevenuIndependantLoader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ENTITY_CLASS_NAME = "globaz.apg.db.cot.pers.APPeriodesRevenuIndependant";

    /**
     * Crée une nouvelle instance de la classe IAPPeriodesRevenuIndependantHelper.
     */
    public IAPPeriodesRevenuIndependantHelperLoader() {
        super(IAPPeriodesRevenuIndependantHelperLoader.ENTITY_CLASS_NAME);
    }

    /**
     * @param valueObject
     */
    public IAPPeriodesRevenuIndependantHelperLoader(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant#load(java.lang.String , java.lang.String,
     * java.lang.String)
     */
    @Override
    public IAPPeriodesRevenuIndependant[] load(String idTiers, String dateDebut, String dateFin) throws Exception {
        return (IAPPeriodesRevenuIndependant[]) _getObject("load", new Object[] { idTiers, dateDebut, dateFin });
    }

}
