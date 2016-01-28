package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;

/**
 * Interface des modes de calcul.
 * 
 * Un mode de calcul d�finit les op�rations successives � effectuer lors de la r�cup�ration des tarifs
 * 
 * @author jts
 * 
 */
public interface CalculMode {

    /**
     * Ex�cute la r�cup�ration des montants pour les droits li�s au dossier pass� en param�tre
     * 
     * @param context
     *            contexte contenant les donn�es n�cessaire � l'ex�cution du calcul
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel}
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException;
}
