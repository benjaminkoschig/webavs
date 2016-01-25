package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface TaxeJournaliereHomeService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(TaxeJournaliereHomeSearch search) throws TaxeJournaliereHomeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param taxeJournaliereHome
     *            La renteAvsAi � cr�er
     * @return le taxeJournaliereHome cr��
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TaxeJournaliereHome create(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param taxeJournaliereHome
     *            le taxeJournaliereHome � supprimer
     * @return le taxeJournaliereHome supprim�
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public TaxeJournaliereHome delete(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de trouver le prix d'une chambre en fonction d'une donn� financi�re
     * 
     * @param SimpleDonneeFinanciereHeader
     *            donn�e financi�re permet d'avoir la periode
     * @return le PrixChambre
     */
    public PrixChambre getPrixTypeChambre(SimpleDonneeFinanciereHeader donneeFinanciereHeader, TypeChambre typeChambre)
            throws PrixChambreException, HomeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Retourne le prix de la chambre qui se trouve dans la map;
     * 
     * @param taxeJournaliereHome
     *            le taxeJournaliereHome
     */
    public String getPrixTypeChambreInMap(TaxeJournaliereHome taxeJournaliereHome);

    /**
     * Met le prix trouv� en fonction du param�tre habitat dans une map
     * 
     * @param Habitat
     *            le taxeJournaliereHome � mettre � jour
     */
    public void putPrixTypeChambreInMap(Habitat habitat) throws PrixChambreException, HomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idTaxeJournaliereHome
     *            L'identifiant de le taxeJournaliereHomee � charger en m�moire
     * @return le taxeJournaliereHome charg� en m�moire
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TaxeJournaliereHome read(String idTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Chargement d'une TaxeJournaliereHome via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws TaxeJournaliereHomeException
     * @throws JadePersistenceException
     */
    public TaxeJournaliereHome readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws TaxeJournaliereHomeException, JadePersistenceException;

    /**
     * Permet la recherche d'apr�s les param�tres de recherche
     * 
     * @param taxeJournaliereHomeSearch
     * @return La recherche effectu�
     * @throws TaxeJournaliereHomeException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public TaxeJournaliereHomeSearch search(TaxeJournaliereHomeSearch taxeJournaliereHomeSearch)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param taxeJournaliereHome
     *            le taxeJournaliereHome � mettre � jour
     * @return le taxeJournaliereHome mis � jour
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TaxeJournaliereHome update(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
