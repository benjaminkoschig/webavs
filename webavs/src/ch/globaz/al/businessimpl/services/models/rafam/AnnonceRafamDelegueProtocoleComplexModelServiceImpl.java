package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueProtocoleComplexSearchModel;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamDelegueProtocoleComplexModelService;

public class AnnonceRafamDelegueProtocoleComplexModelServiceImpl implements
        AnnonceRafamDelegueProtocoleComplexModelService {

    @Override
    public AnnonceRafamDelegueProtocoleComplexSearchModel search(AnnonceRafamDelegueProtocoleComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamDelegueProtocoleComplexModelServiceImpl#search : search is null");
        }

        return (AnnonceRafamDelegueProtocoleComplexSearchModel) JadePersistenceManager.search(search);
    }

}
