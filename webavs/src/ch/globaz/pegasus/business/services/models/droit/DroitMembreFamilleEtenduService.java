package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;

public interface DroitMembreFamilleEtenduService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DroitMembreFamilleEtenduSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� DroitMembreFamilleEtendu
     * 
     * @param DroitMembreFamilleEtendu
     *            La droitMembreFamilleEtendu � cr�er
     * @return droitMembreFamilleEtendu cr��
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    /*
     * public DroitMembreFamilleEtendu create( DroitMembreFamilleEtendu droitMembreFamilleEtendu) throws DroitException,
     * JadePersistenceException;
     */

    /**
     * Permet la suppression d'une entit� droitMembreFamilleEtendu
     * 
     * @param DroitMembreFamilleEtendu
     *            La droitMembreFamilleEtendu � supprimer
     * @return supprim�
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    /*
     * public DroitMembreFamilleEtendu delete( DroitMembreFamilleEtendu droitMembreFamilleEtendu) throws DroitException,
     * JadePersistenceException;
     */

    /**
     * Permet de charger en m�moire une droitMembreFamilleEtendu PC
     * 
     * @param iddroitMembreFamilleEtendu
     *            L'identifiant de droitMembreFamilleEtendu � charger en m�moire
     * @return droitMembreFamilleEtendu charg�e en m�moire
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DroitMembreFamilleEtendu read(String idDroitMembreFamilleEtendu) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des DroitMembreFamilleEtendu selon un mod�le de crit�res.
     * 
     * @param droitMembreFamilleEtenduSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DroitMembreFamilleEtenduSearch search(DroitMembreFamilleEtenduSearch droitMembreFamilleEtenduSearch)
            throws DroitException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� DroitMembreFamilleEtendu
     * 
     * @param DroitMembreFamilleEtendu
     *            Le modele � mettre � jour
     * @return droitMembreFamilleEtendu mis � jour
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    /*
     * public DroitMembreFamilleEtendu update( DroitMembreFamilleEtendu droitMembreFamilleEtendu) throws DroitException,
     * JadePersistenceException;
     */

}