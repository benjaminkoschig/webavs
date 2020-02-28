package ch.globaz.al.businessimpl.calcul.types;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.calcul.modes.CalculMode;

/**
 * Interface devant être implémentés par les types de calcul.
 * 
 * Un type de calcul définit les opérations devant être effectuées pendant le calcul.
 * 
 * Généralement, la méthode <code>CalculMode</code> exécute la méthode <code>compute</code> du mode de calcul (
 * <code>CalculMode</code>) qui lui est passé en paramètre puis effectue d'autres opérations propres au type de calcul
 * (ex : traitement de la famille nombreuse dans le cas du canton de Vaud)
 * 
 * @author jts
 * @see ch.globaz.al.businessimpl.calcul.modes.CalculMode
 */
public interface CalculType {

    /**
     * Exécute le calcul des droit pour <code>dossier</code> selon le mode <code>calcMode</code> à la date
     * <code>dateCalcul</code>
     * 
     * @param context
     *            contexte contenant les données nécessaire à l'exécution du calcul
     * @param calcMode
     *            Mode de calcul à utiliser. Utiliser la factory
     *            {@link ch.globaz.al.businessimpl.calcul.modes.CalculModeFactory} pour déterminer le mode à utiliser
     * @return Une <code>ArrayList</code> contenant des objets <code>CalculBusinessModel</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> compute(ContextCalcul context, CalculMode calcMode)
            throws JadeApplicationException, JadePersistenceException;
}
