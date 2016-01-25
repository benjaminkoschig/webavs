package ch.globaz.pegasus.business.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentrale;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentraleSearch;

public interface SimpleDemandeCentraleService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDemandeCentraleSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleDemandeCentrale
     * 
     * @param SimpleDemandeCentrale La simpleDemandeCentrale � cr�er
     * @return simpleDemandeCentrale cr��
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemandeCentrale create(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleDemandeCentrale
     * 
     * @param SimpleDemandeCentrale La simpleDemandeCentrale � supprimer
     * @return supprim�
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemandeCentrale delete(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleDemandeCentrale PC
     * 
     * @param idsimpleDemandeCentrale L'identifiant de simpleDemandeCentrale � charger en m�moire
     * @return simpleDemandeCentrale charg�e en m�moire
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemandeCentrale read(String idSimpleDemandeCentrale) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleDemandeCentrale selon un mod�le de crit�res.
     * 
     * @param simpleDemandeCentraleSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemandeCentraleSearch search(SimpleDemandeCentraleSearch simpleDemandeCentraleSearch)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleDemandeCentrale
     * 
     * @param SimpleDemandeCentrale Le modele � mettre � jour
     * @return simpleDemandeCentrale mis � jour
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemandeCentrale update(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException;

}
