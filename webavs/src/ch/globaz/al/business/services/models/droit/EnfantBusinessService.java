package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service fournissant des m�thodes m�tier li�es � l'enfant
 * 
 * @author GMO
 */
public interface EnfantBusinessService extends JadeApplicationService {
    /**
     * D�termine si l'enfant est d�j� li� � un droit actif ( donne d�j� droit)
     * 
     * @param idEnfant
     *            id de l'enfant � v�rifier
     * @return Nombre de droit actifs pour l'enfant pass� en param�tre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int getNombreDroitsActifs(String idEnfant) throws JadeApplicationException, JadePersistenceException;

    /**
     * D�termine si l'enfant est d�j� li� � un droit actif ( donne d�j� droit)
     * 
     * @param idEnfant
     *            id de l'enfant � v�rifier
     * @return Nombre de droit actifs pour l'enfant pass� en param�tre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int getNombreDroitsFormationActifs(String idEnfant) throws JadeApplicationException,
            JadePersistenceException;
}
