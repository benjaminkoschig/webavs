package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;

/**
 * interface de déclaration des services de CopieComplexModel
 * 
 * @author JER
 * 
 */
public interface CopieComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre de copies correspondant au modèle de recherche
     * 
     * @param copieComplexSearchModel
     *            Modèle de recherche de copie
     * @return Modèle de recherche de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(CopieComplexSearchModel copieComplexSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Enregistre la copie passée en paramètre
     * 
     * @param copieComplex
     *            Modèle complexe de copie
     * @return Modèle complexe de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel create(CopieComplexModel copieComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime la copie passée en paramètre
     * 
     * @param copieComplex
     *            Modèle complexe de copie
     * @return Modèle complexe de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel delete(CopieComplexModel copieComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise une copie
     * 
     * @param copieComplexModel
     *            Modèle complexe de copie
     * @return Modèle complexe de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel initModel(CopieComplexModel copieComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * @param idCopieComplexModel
     *            ID de copie complex model
     * @return Modèle complexe de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel read(String idCopieComplexModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les copies correspondant au modèle de recherche
     * 
     * @param copieComplexSearchModel
     *            Modèle de recherche de copie
     * @return Modèle de recherche de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexSearchModel search(CopieComplexSearchModel copieComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour le modèle passé en paramètre
     * 
     * @param copieComplex
     *            Modèle complexe de copie
     * @return Modèle complexe de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel update(CopieComplexModel copieComplex) throws JadeApplicationException,
            JadePersistenceException;
}
