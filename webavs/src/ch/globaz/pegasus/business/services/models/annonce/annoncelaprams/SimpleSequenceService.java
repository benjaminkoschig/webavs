package ch.globaz.pegasus.business.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.SequenceException;
import ch.globaz.pegasus.business.models.annonce.SimpleSequence;
import ch.globaz.pegasus.business.models.annonce.SimpleSequenceSearch;

public interface SimpleSequenceService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleSequenceSearch search) throws SequenceException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleSequence
     * 
     * @param SimpleSequence
     *            La simpleSequence � cr�er
     * @return simpleSequence cr��
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSequence create(SimpleSequence simpleSequence) throws SequenceException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleSequence
     * 
     * @param SimpleSequence
     *            La simpleSequence � supprimer
     * @return supprim�
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSequence delete(SimpleSequence simpleSequence) throws SequenceException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleSequence selon un mod�le de crit�res.
     * 
     * @param simpleSequenceSearch
     *            Le mod�le de crit�res
     * @return Une liste typ�
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public List<SimpleSequence> find(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleSequence PC
     * 
     * @param idsimpleSequence
     *            L'identifiant de simpleSequence � charger en m�moire
     * @return simpleSequence charg�e en m�moire
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSequence read(String idSimpleSequence) throws SequenceException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleSequence selon un mod�le de crit�res.
     * 
     * @param simpleSequenceSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSequenceSearch search(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleSequence
     * 
     * @param SimpleSequence
     *            Le modele � mettre � jour
     * @return simpleSequence mis � jour
     * @throws SequenceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleSequence update(SimpleSequence simpleSequence) throws SequenceException, JadePersistenceException;
}
