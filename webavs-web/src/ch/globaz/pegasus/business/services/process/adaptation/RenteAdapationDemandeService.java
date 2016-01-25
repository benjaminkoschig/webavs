package ch.globaz.pegasus.business.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemandeSearch;

public interface RenteAdapationDemandeService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public int count(RenteAdapationDemandeSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité RenteAdapationDemande <b>Attention<b> si la simpleDemandeCentral existe en bd on
     * créer seulement la simpleRenteAadaptation et on la lie a la simpleDemandeCentrle
     * 
     * @param RenteAdapationDemande La renteAdapationDemande à créer
     * @return renteAdapationDemande créé
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RenteAdapationDemande create(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité renteAdapationDemande
     * 
     * @param RenteAdapationDemande La renteAdapationDemande à supprimer
     * @return supprimé
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RenteAdapationDemande delete(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de retrouver les demandes à la central par l'idprocess
     * 
     * @param idExecutionProcess
     * @return Un map(idDemande) de liste de rente
     * @throws RenteAdapationDemandeException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Map<String, List<RenteAdapationDemande>> findByIdProcess(String idExecutionProcess)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une renteAdapationDemande PC
     * 
     * @param idrenteAdapationDemande L'identifiant de renteAdapationDemande à charger en mémoire
     * @return renteAdapationDemande chargée en mémoire
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public RenteAdapationDemande read(String idRenteAdapationDemande) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet de chercher des RenteAdapationDemande selon un modèle de critères.
     * 
     * @param renteAdapationDemandeSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     */
    public RenteAdapationDemandeSearch search(RenteAdapationDemandeSearch renteAdapationDemandeSearch)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité RenteAdapationDemande
     * 
     * @param RenteAdapationDemande Le modele à mettre à jour
     * @return renteAdapationDemande mis à jour
     * @throws RenteAdapationDemandeException Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RenteAdapationDemande update(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}
