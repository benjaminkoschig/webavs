package ch.globaz.al.businessimpl.generation.prestations;

import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe effectuant une g�n�ration de prestation temporaire pour un dossier ADI. Elle est utilis� dans le cas d'une
 * g�n�ration de prestation de travail pour un dossier ADI.
 * 
 * Si une prestation existe d�j� pour la p�riode contenue dans le contexte d'ex�cution. Si c'est le cas, une extourne
 * est effectu�e.
 * 
 * @author jts
 * 
 */
public class GenPrestationADITemporaire extends GenPrestationAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationADITemporaire(ContextAffilie context) {
        super(context);
    }

}
