package ch.globaz.musca.business.services.models.passage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.business.models.PassageModuleComplexSearchModel;

/**
 * 
 * Services persistence d'un mod�le <code>PasasgeModuleComplexModel</code>
 * 
 */
public interface PassageModuleComplexModelService extends JadeApplicationService {
    /**
     * R�cup�re les donn�es du passage correspondant � <code>idPassage</code>
     * 
     * @param idPassage
     *            Id du passage � charger
     * @return Le mod�le du passage charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             fair
     */
    public PassageModuleComplexModel read(String idPassage) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            mod�le de recherche de passage
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PassageModuleComplexSearchModel search(PassageModuleComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}
