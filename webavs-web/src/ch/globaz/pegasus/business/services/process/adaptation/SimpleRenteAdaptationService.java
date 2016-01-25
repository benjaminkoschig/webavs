package ch.globaz.pegasus.business.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptation;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptationSearch;

public interface SimpleRenteAdaptationService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleRenteAdaptationSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité simpleRenteAdaptation
     * 
     * @param simpleRenteAdaptation La simpleRenteAdaptation à créer
     * @return simpleRenteAdaptation créé
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAdaptation create(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleRenteAdaptation
     * 
     * @param simpleRenteAdaptation La simpleRenteAdaptation à supprimer
     * @return supprimé
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAdaptation delete(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleRenteAdaptation PC
     * 
     * @param idsimpleRenteAdaptation L'identifiant de simpleRenteAdaptation à charger en mémoire
     * @return simpleRenteAdaptation chargée en mémoire
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAdaptation read(String idsimpleRenteAdaptation) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet de chercher des simpleRenteAdaptation selon un modèle de critères.
     * 
     * @param simpleRenteAdaptationSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAdaptationSearch search(SimpleRenteAdaptationSearch simpleRenteAdaptationSearch)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité simpleRenteAdaptation
     * 
     * @param simpleRenteAdaptation Le modele à mettre à jour
     * @return simpleRenteAdaptation mis à jour
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAdaptation update(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException;

}