package ch.globaz.al.businessimpl.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;
import ch.globaz.al.business.services.models.processus.TemplateTraitementListComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services de TemplateTraitementListComplexModel
 * 
 * @author GMO
 * 
 */
public class TemplateTraitementListComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        TemplateTraitementListComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus. TemplateTraitementListComplexModelService
     * #search(ch.globaz.al.business.models .processus.TemplateTraitementListComplexSearchModel)
     */
    @Override
    public TemplateTraitementListComplexSearchModel search(
            TemplateTraitementListComplexSearchModel templateTraitementListComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (templateTraitementListComplexSearchModel == null) {
            throw new ALProcessusException(
                    "TemplateTraitementListComplexModelServiceImpl#search: unable to search TemplateTraitementListComplexModelServiceImpl - the model passed is null");
        }

        return (TemplateTraitementListComplexSearchModel) JadePersistenceManager
                .search(templateTraitementListComplexSearchModel);
    }
}
