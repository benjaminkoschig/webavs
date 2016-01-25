package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;

/**
 * Interface des services de TemplateTraitementListComplexModel
 * 
 * @author GMO
 * 
 */
public interface TemplateTraitementListComplexModelService extends JadeApplicationService {

    /**
     * Effectue une recherche de traitements / processus selon les crit�res contenu dans le mod�le de recherche
     * <code>templateTraitementListComplexSearchModel</code>
     * 
     * @param templateTraitementListComplexSearchModel
     *            selon mod�le TemplateTraitementListComplexSearchModel
     * @return r�sultat de la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public TemplateTraitementListComplexSearchModel search(
            TemplateTraitementListComplexSearchModel templateTraitementListComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

}
