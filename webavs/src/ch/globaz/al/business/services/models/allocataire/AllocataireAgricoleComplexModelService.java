package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;

/**
 * service de la persistance des données de AllocataireAgricoleComplexModel
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel
 */
public interface AllocataireAgricoleComplexModelService extends JadeApplicationService {
    /**
     * Enregistre le modèle d'allocataire passé en paramètre en persistance
     * 
     * @param allocataireAgricoleComplex
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireAgricoleComplexModel create(AllocataireAgricoleComplexModel allocataireAgricoleComplex)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime le modèle d'allocataire passé en paramètre de la persistance
     * 
     * @param allocataireAgricoleComplexModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AllocataireAgricoleComplexModel delete(AllocataireAgricoleComplexModel allocataireAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge un allocataire agricole
     * 
     * @param idAllocatairAgricoleComplexModel
     *            id de l'allocataire à chargé
     * @return Allocataire chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AllocataireAgricoleComplexModel read(String idAllocatairAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour le modèle d'allocataire passé en paramètre en persistance
     * 
     * @param allocataireAgricoleComplexModel
     *            modèle à mettre à jour
     * @return le modèle mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireAgricoleComplexModel update(AllocataireAgricoleComplexModel allocataireAgricoleComplexModel)
            throws JadeApplicationException, JadePersistenceException;

}
