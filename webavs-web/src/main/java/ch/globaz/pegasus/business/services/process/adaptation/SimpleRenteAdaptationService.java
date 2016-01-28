package ch.globaz.pegasus.business.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptation;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptationSearch;

public interface SimpleRenteAdaptationService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleRenteAdaptationSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simpleRenteAdaptation
     * 
     * @param simpleRenteAdaptation La simpleRenteAdaptation � cr�er
     * @return simpleRenteAdaptation cr��
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAdaptation create(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleRenteAdaptation
     * 
     * @param simpleRenteAdaptation La simpleRenteAdaptation � supprimer
     * @return supprim�
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAdaptation delete(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleRenteAdaptation PC
     * 
     * @param idsimpleRenteAdaptation L'identifiant de simpleRenteAdaptation � charger en m�moire
     * @return simpleRenteAdaptation charg�e en m�moire
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAdaptation read(String idsimpleRenteAdaptation) throws RenteAdapationDemandeException,
            JadePersistenceException;

    /**
     * Permet de chercher des simpleRenteAdaptation selon un mod�le de crit�res.
     * 
     * @param simpleRenteAdaptationSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAdaptationSearch search(SimpleRenteAdaptationSearch simpleRenteAdaptationSearch)
            throws RenteAdapationDemandeException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleRenteAdaptation
     * 
     * @param simpleRenteAdaptation Le modele � mettre � jour
     * @return simpleRenteAdaptation mis � jour
     * @throws RenteAdapationDemandeException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAdaptation update(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException;

}