package ch.globaz.al.businessimpl.calcul.types;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.calcul.context.ContextCalcul;
import ch.globaz.al.businessimpl.calcul.modes.CalculMode;

/**
 * Interface devant �tre impl�ment�s par les types de calcul.
 * 
 * Un type de calcul d�finit les op�rations devant �tre effectu�es pendant le calcul.
 * 
 * G�n�ralement, la m�thode <code>CalculMode</code> ex�cute la m�thode <code>compute</code> du mode de calcul (
 * <code>CalculMode</code>) qui lui est pass� en param�tre puis effectue d'autres op�rations propres au type de calcul
 * (ex : traitement de la famille nombreuse dans le cas du canton de Vaud)
 * 
 * @author jts
 * @see ch.globaz.al.businessimpl.calcul.modes.CalculMode
 */
public interface CalculType {

    /**
     * Ex�cute le calcul des droit pour <code>dossier</code> selon le mode <code>calcMode</code> � la date
     * <code>dateCalcul</code>
     * 
     * @param context
     *            contexte contenant les donn�es n�cessaire � l'ex�cution du calcul
     * @param calcMode
     *            Mode de calcul � utiliser. Utiliser la factory
     *            {@link ch.globaz.al.businessimpl.calcul.modes.CalculModeFactory} pour d�terminer le mode � utiliser
     * @return Une <code>ArrayList</code> contenant des objets <code>CalculBusinessModel</code>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List<CalculBusinessModel> compute(ContextCalcul context, CalculMode calcMode)
            throws JadeApplicationException, JadePersistenceException;
}
