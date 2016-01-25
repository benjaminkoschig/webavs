package ch.globaz.musca.business.services.models.passage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.models.PassageSearchModel;

/**
 * service de gestion de la persistance des donn�es li�es aux passage facturation
 * 
 * @author GMO
 */
public interface PassageModelService extends JadeApplicationService {
    /**
     * Charge un passage selon l'id pass� en param�tre
     * 
     * @param idPassage
     *            l'id du passage qu'on veut charg�
     * @return PassageModel le mod�le charg� depuis la DB
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public PassageModel read(String idPassage) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche sur les passages facturation
     * 
     * @param searchModel
     *            mod�le de recherche PassageSearchModel
     * @return les r�sultats stock�s dans le mod�le de recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PassageSearchModel search(PassageSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;
}
