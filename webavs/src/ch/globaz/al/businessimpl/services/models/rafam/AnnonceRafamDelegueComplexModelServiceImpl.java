package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamDelegueComplexSearchModel;
import ch.globaz.al.business.services.models.rafam.AnnonceRafamDelegueComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class AnnonceRafamDelegueComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        AnnonceRafamDelegueComplexModelService {

    @Override
    public AnnonceRafamDelegueComplexModel read(String idAnnonce) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException(
                    "AnnonceRafamDelegueComplexModelServiceImpl#read : idAnnonce is not an integer");
        }

        AnnonceRafamDelegueComplexModel model = new AnnonceRafamDelegueComplexModel();
        model.setId(idAnnonce);
        return (AnnonceRafamDelegueComplexModel) JadePersistenceManager.read(model);

    }

    @Override
    public AnnonceRafamDelegueComplexSearchModel search(AnnonceRafamDelegueComplexSearchModel search)
            throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new ALAnnonceRafamException("AnnonceRafamDelegueComplexModelServiceImpl#search : search is null");
        }

        return (AnnonceRafamDelegueComplexSearchModel) JadePersistenceManager.search(search);

    }

}
