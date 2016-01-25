package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHomeSearch;

public interface SimpleTaxeJournaliereHomeService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param simpleTaxeJournaliereHome
     *            La simpleTaxeJournaliereHome � cr�er
     * @return le simpleTaxeJournaliereHome cr��
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome create(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param simpleTaxeJournaliereHome
     *            le simpleTaxeJournaliereHome � supprimer
     * @return le simpleTaxeJournaliereHome supprim�
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome delete(SimpleTaxeJournaliereHome simpleLoyer) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet l'effacement de simpleTaxeJournaliereHome en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleTaxeJournaliereHome
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idsimpleTaxeJournaliereHome
     *            L'identifiant de le simpleTaxeJournaliereHomee � charger en m�moire
     * @return le simpleTaxeJournaliereHome charg� en m�moire
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome read(String idSimpleTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet la recherche sur les simpel taxes journaliere Home
     * 
     * @param simpleTaxesJournaliereSearch
     * @return
     * @throws TaxeJournaliereHomeException
     * @throws JadePersistenceException
     */
    public SimpleTaxeJournaliereHomeSearch search(SimpleTaxeJournaliereHomeSearch simpleTaxesJournaliereSearch)
            throws TaxeJournaliereHomeException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param simpleTaxeJournaliereHome
     *            le simpleTaxeJournaliereHome � mettre � jour
     * @return le simpleTaxeJournaliereHome mis � jour
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome update(SimpleTaxeJournaliereHome simpleLoyer) throws TaxeJournaliereHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
