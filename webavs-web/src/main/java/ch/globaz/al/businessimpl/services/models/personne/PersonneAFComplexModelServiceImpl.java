package ch.globaz.al.businessimpl.services.models.personne;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.personne.ALPersonneAFComplexModelException;
import ch.globaz.al.business.models.personne.PersonneAFComplexSearchModel;
import ch.globaz.al.business.services.models.personne.PersonneAFComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe de recherche pour le modèle PersonneAFComplexModel
 * 
 * @author GMO
 * 
 */
public class PersonneAFComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        PersonneAFComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.personne.PersonneAFComplexModelService #
     * search(ch.globaz.al.business.models.personne.PersonneAFComplexSearchModel )
     */
    @Override
    public PersonneAFComplexSearchModel search(PersonneAFComplexSearchModel personneAFComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        if (personneAFComplexModel == null) {
            throw new ALPersonneAFComplexModelException(
                    "unable to search personneAFComplexModel - the model passed is null");
        }
        return (PersonneAFComplexSearchModel) JadePersistenceManager.search(personneAFComplexModel);
    }

}
