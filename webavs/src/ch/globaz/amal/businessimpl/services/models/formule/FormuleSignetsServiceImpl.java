package ch.globaz.amal.businessimpl.services.models.formule;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.services.models.formule.FormuleSignetsService;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.SignetException;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleSignetModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

public class FormuleSignetsServiceImpl implements FormuleSignetsService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.formule.FormuleSignetsService#getListeSignets(java.lang.String)
     */
    @Override
    public SimpleSignetModelSearch getListeSignets(String param) throws SignetException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleSignetModelSearch signets = new SimpleSignetModelSearch();
        signets.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        signets = ENServiceLocator.getSimpleSignetModelService().search(signets);
        return signets;
    }

}
