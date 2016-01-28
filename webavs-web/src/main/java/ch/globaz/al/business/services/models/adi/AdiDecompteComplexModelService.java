package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.adi.AdiDecompteComplexSearchModel;

/**
 * service de la persistance des données de AdiComplexModel
 * 
 * @author PTA
 * 
 */
public interface AdiDecompteComplexModelService extends JadeApplicationService {

    /**
     * Charge un décompte dans le modèle selon l'id passé en paramètre
     * 
     * @param idDecompteComplexModel
     *            l'id du décompte qu'on veut charger
     * @return AdiDecompteComplexModel le décompte chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiDecompteComplexModel read(String idDecompteComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche sur le modèle
     * 
     * @param decompteGlobal
     *            selon AdiDecompteComplexModel
     * @return AdiDecompteComplexModelSearch
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiDecompteComplexSearchModel search(AdiDecompteComplexSearchModel decompteGlobal)
            throws JadeApplicationException, JadePersistenceException;

}
