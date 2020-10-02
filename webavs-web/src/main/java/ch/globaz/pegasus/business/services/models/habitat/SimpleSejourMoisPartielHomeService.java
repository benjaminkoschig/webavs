package ch.globaz.pegasus.business.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHomeSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

public interface SimpleSejourMoisPartielHomeService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param simpleSejourMoisPartielHome
     *            La simpleSejourMoisPartielHome � cr�er
     * @return le simpleSejourMoisPartielHome cr��
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SimpleSejourMoisPartielHome create(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome)
            throws SejourMoisPartielHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param simpleSejourMoisPartielHome
     *            le simpleSejourMoisPartielHome � supprimer
     * @return le simpleSejourMoisPartielHome supprim�
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SimpleSejourMoisPartielHome delete(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException;

    /**
     * Permet l'effacement de simpleSejourMoisPartielHome en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleSejourMoisPartielHome
     * @throws JadePersistenceException
     */
    void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idSimpleSejourMoisPartielHome
     *            L'identifiant de le simpleSejourMoisPartielHomee � charger en m�moire
     * @return le simpleSejourMoisPartielHome charg� en m�moire
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SimpleSejourMoisPartielHome read(String idSimpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException;

    /**
     * Permet la recherche sur les simpel taxes journaliere Home
     * 
     * @param simpleTaxesJournaliereSearch
     * @return
     * @throws SejourMoisPartielHomeException
     * @throws JadePersistenceException
     */
    SimpleSejourMoisPartielHomeSearch search(SimpleSejourMoisPartielHomeSearch simpleTaxesJournaliereSearch)
            throws SejourMoisPartielHomeException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param simpleSejourMoisPartielHome
     *            le simpleSejourMoisPartielHome � mettre � jour
     * @return le simpleSejourMoisPartielHome mis � jour
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SimpleSejourMoisPartielHome update(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
