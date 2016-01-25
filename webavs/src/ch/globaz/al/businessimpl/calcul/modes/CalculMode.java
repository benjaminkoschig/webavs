package ch.globaz.al.businessimpl.calcul.modes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;

/**
 * Interface des modes de calcul.
 * 
 * Un mode de calcul définit les opérations successives à effectuer lors de la récupération des tarifs
 * 
 * @author jts
 * 
 */
public interface CalculMode {

    /**
     * Exécute la récupération des montants pour les droits liés au dossier passé en paramètre
     * 
     * @param context
     *            contexte contenant les données nécessaire à l'exécution du calcul
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel}
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList<CalculBusinessModel> compute(ContextCalcul context) throws JadeApplicationException,
            JadePersistenceException;
}
