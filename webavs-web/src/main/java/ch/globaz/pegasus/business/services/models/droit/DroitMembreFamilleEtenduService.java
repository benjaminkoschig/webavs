package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;

public interface DroitMembreFamilleEtenduService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DroitMembreFamilleEtenduSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la création d'une entité DroitMembreFamilleEtendu
     * 
     * @param DroitMembreFamilleEtendu
     *            La droitMembreFamilleEtendu à créer
     * @return droitMembreFamilleEtendu créé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    /*
     * public DroitMembreFamilleEtendu create( DroitMembreFamilleEtendu droitMembreFamilleEtendu) throws DroitException,
     * JadePersistenceException;
     */

    /**
     * Permet la suppression d'une entité droitMembreFamilleEtendu
     * 
     * @param DroitMembreFamilleEtendu
     *            La droitMembreFamilleEtendu à supprimer
     * @return supprimé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    /*
     * public DroitMembreFamilleEtendu delete( DroitMembreFamilleEtendu droitMembreFamilleEtendu) throws DroitException,
     * JadePersistenceException;
     */

    /**
     * Permet de charger en mémoire une droitMembreFamilleEtendu PC
     * 
     * @param iddroitMembreFamilleEtendu
     *            L'identifiant de droitMembreFamilleEtendu à charger en mémoire
     * @return droitMembreFamilleEtendu chargée en mémoire
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DroitMembreFamilleEtendu read(String idDroitMembreFamilleEtendu) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de chercher des DroitMembreFamilleEtendu selon un modèle de critères.
     * 
     * @param droitMembreFamilleEtenduSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DroitMembreFamilleEtenduSearch search(DroitMembreFamilleEtenduSearch droitMembreFamilleEtenduSearch)
            throws DroitException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité DroitMembreFamilleEtendu
     * 
     * @param DroitMembreFamilleEtendu
     *            Le modele à mettre à jour
     * @return droitMembreFamilleEtendu mis à jour
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    /*
     * public DroitMembreFamilleEtendu update( DroitMembreFamilleEtendu droitMembreFamilleEtendu) throws DroitException,
     * JadePersistenceException;
     */

}