package ch.globaz.corvus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayementSearch;

public interface SimpleRetenuePayementService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws SimpleRetenuePayementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleRetenuePayementSearch search) throws SimpleRetenuePayementException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleRetenuePayement.
     * 
     * @param SimpleRetenuePayement
     *            Le SimpleRetenuePayement � cr�er
     * @return Le SimpleRetenuePayement cr��
     * @throws SimpleRetenuePayementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRetenuePayement create(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� SimpleRetenuePayement
     * 
     * @param SimpleRetenuePayement
     *            Le SimpleRetenuePayement � supprimer
     * @return Le SimpleRetenuePayement supprim�
     * @throws SimpleRetenuePayementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRetenuePayement delete(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException, JadePersistenceException;

    /**
     * Permet la suppression d'entit� SimpleRetenuePayement par idPrestationAccordee
     * 
     * @param SimpleRetenuePayement
     *            Le SimpleRetenuePayement � supprimer
     * @return Le SimpleRetenuePayement supprim�
     * @throws SimpleRetenuePayementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public void deleteByIdPrestationAccordee(String idPrestationAccordee) throws SimpleRetenuePayementException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un SimpleRetenuePayement
     * 
     * @param idSimpleRetenuePayement
     *            L'identifiant du InformationsComptabilite � charger en m�moire
     * @return Le SimpleRetenuePayement charg� en m�moire
     * @throws SimpleRetenuePayement
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRetenuePayement read(String idSimpleRetenuePayement) throws SimpleRetenuePayementException,
            JadePersistenceException;

    public SimpleRetenuePayementSearch search(SimpleRetenuePayementSearch search) throws JadePersistenceException,
            SimpleRetenuePayementException;

    /**
     * Permet la mise � jour d'une entit� InformationsComptabilite
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite � mettre � jour
     * @return Le InformationsComptabilite mis � jour
     * @throws InformationsComptabiliteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRetenuePayement update(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException;

}
