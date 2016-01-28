package ch.globaz.al.businessimpl.generation.prestations;

import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe effectuant une génération de prestation temporaire pour un dossier ADI. Elle est utilisé dans le cas d'une
 * génération de prestation de travail pour un dossier ADI.
 * 
 * Si une prestation existe déjà pour la période contenue dans le contexte d'exécution. Si c'est le cas, une extourne
 * est effectuée.
 * 
 * @author jts
 * 
 */
public class GenPrestationADITemporaire extends GenPrestationAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
     */
    public GenPrestationADITemporaire(ContextAffilie context) {
        super(context);
    }

}
