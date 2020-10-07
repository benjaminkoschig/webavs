package ch.globaz.pegasus.business.services.models.creancier;

import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.*;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.math.BigDecimal;

public interface SimpleCreancierHystoriqueService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     *
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleCreancierHystoriqueSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleCreancierHystorique
     *
     * @param SimpleCreancierHystorique
     *            La SimpleCreancierHystorique � cr�er
     * @return SimpleCreancierHystorique cr��
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancierHystorique create(SimpleCreancier simpleCreancier, SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� creanceAccordeeHyst
     *
     * @param SimpleCreancierHystorique
     *            La creanceAccordeeHyst � supprimer
     * @return supprim�
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancierHystorique delete(SimpleCreancierHystorique creanceAccordeeHyst) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;




    /**
     * Permet de charger en m�moire une creanceAccordeeHyst PC
     *
     * @param idcreanceAccordeeHyst
     *            L'identifiant de creanceAccordeeHyst � charger en m�moire
     * @return creanceAccordeeHyst charg�e en m�moire
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreancierHystorique read(String idSimpleCreancierHystorique) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleCreancierHystorique selon un mod�le de crit�res.
     *
     * @param SimpleCreancierHystorique
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreancierHystoriqueSearch search(SimpleCreancierHystoriqueSearch simpleCreancierHystoriqueSearch) throws CreancierException,
            JadePersistenceException;

    /**
     *
     * Permet la mise � jour d'une entit� SimpleCreancierHystorique
     *
     * @param SimpleCreancierHystorique
     *            Le modele � mettre � jour
     * @return creanceAccordeeHyst mis � jour
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistences
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancierHystorique update(SimpleCreancierHystorique creanceAccordeeHyst) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;
}
