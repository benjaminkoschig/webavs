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
     * Effectue une recherche de traitements / processus selon les critères contenu dans le modèle de recherche
     * <code>templateTraitementListComplexSearchModel</code>
     * 
     * @param templateTraitementListComplexSearchModel
     *            selon modèle TemplateTraitementListComplexSearchModel
     * @return résultat de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public TemplateTraitementListComplexSearchModel search(
            TemplateTraitementListComplexSearchModel templateTraitementListComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

}
