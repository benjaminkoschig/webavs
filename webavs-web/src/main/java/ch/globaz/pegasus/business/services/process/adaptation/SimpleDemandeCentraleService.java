package ch.globaz.pegasus.business.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentrale;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentraleSearch;

public interface SimpleDemandeCentraleService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleDemandeCentraleSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleDemandeCentrale
     * 
     * @param SimpleDemandeCentrale La simpleDemandeCentrale à créer
     * @return simpleDemandeCentrale créé
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemandeCentrale create(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleDemandeCentrale
     * 
     * @param SimpleDemandeCentrale La simpleDemandeCentrale à supprimer
     * @return supprimé
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemandeCentrale delete(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleDemandeCentrale PC
     * 
     * @param idsimpleDemandeCentrale L'identifiant de simpleDemandeCentrale à charger en mémoire
     * @return simpleDemandeCentrale chargée en mémoire
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemandeCentrale read(String idSimpleDemandeCentrale) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleDemandeCentrale selon un modèle de critères.
     * 
     * @param simpleDemandeCentraleSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemandeCentraleSearch search(SimpleDemandeCentraleSearch simpleDemandeCentraleSearch)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleDemandeCentrale
     * 
     * @param SimpleDemandeCentrale Le modele à mettre à jour
     * @return simpleDemandeCentrale mis à jour
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleDemandeCentrale update(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException;

}
