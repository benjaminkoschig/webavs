package globaz.pegasus.utils;

import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;

public class PCPCAccordeeHandler {

    /**
     * Définit si un plan de calcul est accessible selon les rêgles suivantes:</br>
     * 
     * Si le plan à la propriété isAccessible --> ok Tous les autres cas --> ko
     * 
     * Le plan de calcul affiché sera celui de la pca, OU celui de la pca parente pour le cas de copie de pca
     * 
     * @return si le plan de calcul est défini comme accessible
     * @throws PCAccordeeException
     *             si prblème avec la pc
     */
    public static boolean getIsPlanCalculAccessible(String csMotif, boolean isPcaAccessible) throws PCAccordeeException {

        if (PCDroitHandler.isDroitFromReprise(csMotif)) {
            return false;
        } else {
            return isPcaAccessible;
        }
    }
}
