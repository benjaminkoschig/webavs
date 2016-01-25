package globaz.pegasus.utils;

import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;

public class PCPCAccordeeHandler {

    /**
     * D�finit si un plan de calcul est accessible selon les r�gles suivantes:</br>
     * 
     * Si le plan � la propri�t� isAccessible --> ok Tous les autres cas --> ko
     * 
     * Le plan de calcul affich� sera celui de la pca, OU celui de la pca parente pour le cas de copie de pca
     * 
     * @return si le plan de calcul est d�fini comme accessible
     * @throws PCAccordeeException
     *             si prbl�me avec la pc
     */
    public static boolean getIsPlanCalculAccessible(String csMotif, boolean isPcaAccessible) throws PCAccordeeException {

        if (PCDroitHandler.isDroitFromReprise(csMotif)) {
            return false;
        } else {
            return isPcaAccessible;
        }
    }
}
