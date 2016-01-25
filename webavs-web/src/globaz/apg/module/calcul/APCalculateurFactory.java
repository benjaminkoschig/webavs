/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul;

import globaz.apg.module.calcul.interfaces.IAPCalculateur;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;

/**
 * Description Factory retournant le module de calcul approprié pour la calcul de la prestation APG
 * 
 * @author scr
 * 
 */
public class APCalculateurFactory {

    // Class statique, référence par cette instance
    private static APCalculateurFactory instance = null;

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static synchronized APCalculateurFactory getInstance() throws Exception {
        if (instance == null) {
            instance = new APCalculateurFactory();
        }
        return instance;
    }

    private APCalculateurFactory() throws Exception {
    }

    /**
     * 
     * Retourne le calculateur approprié, définit selon les données contenues dans IAPReferenceDataPrestataion
     * 
     * @param referenceDataPrestation
     *            Contient les données nécessaire
     * @return le calculateur
     * @throws Exception
     */
    public IAPCalculateur getCalculateur(IAPReferenceDataPrestation referenceDataPrestation) throws Exception {
        Class c = Class.forName(referenceDataPrestation.getCalculateurClassName());
        IAPCalculateur instance = (IAPCalculateur) c.newInstance();
        instance.setReferenceData(referenceDataPrestation);
        return instance;
    }
}
