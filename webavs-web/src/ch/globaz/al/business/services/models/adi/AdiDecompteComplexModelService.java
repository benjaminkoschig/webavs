package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.adi.AdiDecompteComplexSearchModel;

/**
 * service de la persistance des donn�es de AdiComplexModel
 * 
 * @author PTA
 * 
 */
public interface AdiDecompteComplexModelService extends JadeApplicationService {

    /**
     * Charge un d�compte dans le mod�le selon l'id pass� en param�tre
     * 
     * @param idDecompteComplexModel
     *            l'id du d�compte qu'on veut charger
     * @return AdiDecompteComplexModel le d�compte charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiDecompteComplexModel read(String idDecompteComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche sur le mod�le
     * 
     * @param decompteGlobal
     *            selon AdiDecompteComplexModel
     * @return AdiDecompteComplexModelSearch
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiDecompteComplexSearchModel search(AdiDecompteComplexSearchModel decompteGlobal)
            throws JadeApplicationException, JadePersistenceException;

}
