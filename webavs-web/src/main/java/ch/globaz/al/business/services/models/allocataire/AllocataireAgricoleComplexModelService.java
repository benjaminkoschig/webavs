package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;

/**
 * service de la persistance des donn�es de AllocataireAgricoleComplexModel
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel
 */
public interface AllocataireAgricoleComplexModelService extends JadeApplicationService {
    /**
     * Enregistre le mod�le d'allocataire pass� en param�tre en persistance
     * 
     * @param allocataireAgricoleComplex
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireAgricoleComplexModel create(AllocataireAgricoleComplexModel allocataireAgricoleComplex)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime le mod�le d'allocataire pass� en param�tre de la persistance
     * 
     * @param allocataireAgricoleComplexModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AllocataireAgricoleComplexModel delete(AllocataireAgricoleComplexModel allocataireAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge un allocataire agricole
     * 
     * @param idAllocatairAgricoleComplexModel
     *            id de l'allocataire � charg�
     * @return Allocataire charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AllocataireAgricoleComplexModel read(String idAllocatairAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour le mod�le d'allocataire pass� en param�tre en persistance
     * 
     * @param allocataireAgricoleComplexModel
     *            mod�le � mettre � jour
     * @return le mod�le mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireAgricoleComplexModel update(AllocataireAgricoleComplexModel allocataireAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException;

}
