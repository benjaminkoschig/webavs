package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;

/**
 * interface de d�claration des services de CopieComplexModel
 * 
 * @author JER
 * 
 */
public interface CopieComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre de copies correspondant au mod�le de recherche
     * 
     * @param copieComplexSearchModel
     *            Mod�le de recherche de copie
     * @return Mod�le de recherche de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(CopieComplexSearchModel copieComplexSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Enregistre la copie pass�e en param�tre
     * 
     * @param copieComplex
     *            Mod�le complexe de copie
     * @return Mod�le complexe de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel create(CopieComplexModel copieComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime la copie pass�e en param�tre
     * 
     * @param copieComplex
     *            Mod�le complexe de copie
     * @return Mod�le complexe de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel delete(CopieComplexModel copieComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise une copie
     * 
     * @param copieComplexModel
     *            Mod�le complexe de copie
     * @return Mod�le complexe de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel initModel(CopieComplexModel copieComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * @param idCopieComplexModel
     *            ID de copie complex model
     * @return Mod�le complexe de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel read(String idCopieComplexModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les copies correspondant au mod�le de recherche
     * 
     * @param copieComplexSearchModel
     *            Mod�le de recherche de copie
     * @return Mod�le de recherche de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexSearchModel search(CopieComplexSearchModel copieComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour le mod�le pass� en param�tre
     * 
     * @param copieComplex
     *            Mod�le complexe de copie
     * @return Mod�le complexe de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieComplexModel update(CopieComplexModel copieComplex) throws JadeApplicationException,
            JadePersistenceException;
}
