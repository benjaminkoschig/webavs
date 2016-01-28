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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RenteAdapationDemandeSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� RenteAdapationDemande <b>Attention<b> si la simpleDemandeCentral existe en bd on
     * cr�er seulement la simpleRenteAadaptation et on la lie a la simpleDemandeCentrle
     * 
     * @param RenteAdapationDemande La renteAdapationDemande � cr�er
     * @return renteAdapationDemande cr��
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RenteAdapationDemande create(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� renteAdapationDemande
     * 
     * @param RenteAdapationDemande La renteAdapationDemande � supprimer
     * @return supprim�
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RenteAdapationDemande delete(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de retrouver les demandes � la central par l'idprocess
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
     * Permet de charger en m�moire une renteAdapationDemande PC
     * 
     * @param idrenteAdapationDemande L'identifiant de renteAdapationDemande � charger en m�moire
     * @return renteAdapationDemande charg�e en m�moire
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public RenteAdapationDemande read(String idRenteAdapationDemande) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet de chercher des RenteAdapationDemande selon un mod�le de crit�res.
     * 
     * @param renteAdapationDemandeSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public RenteAdapationDemandeSearch search(RenteAdapationDemandeSearch renteAdapationDemandeSearch)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� RenteAdapationDemande
     * 
     * @param RenteAdapationDemande Le modele � mettre � jour
     * @return renteAdapationDemande mis � jour
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RenteAdapationDemande update(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}
