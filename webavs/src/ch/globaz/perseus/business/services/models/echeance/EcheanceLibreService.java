package ch.globaz.perseus.business.services.models.echeance;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.EcheanceLibreException;
import ch.globaz.perseus.business.models.echeance.EcheanceLibre;
import ch.globaz.perseus.business.models.echeance.EcheanceLibreSearchModel;

public interface EcheanceLibreService extends JadeApplicationService {
    public int count(EcheanceLibreSearchModel search) throws EcheanceLibreException, JadePersistenceException;

    public EcheanceLibre create(EcheanceLibre echeanceLibre) throws JadePersistenceException, EcheanceLibreException,
            JadeApplicationServiceNotAvailableException;

    public EcheanceLibre delete(EcheanceLibre echeanceLibre) throws JadePersistenceException, EcheanceLibreException,
            JadeApplicationServiceNotAvailableException;

    public EcheanceLibre read(String idEcheanceLibre) throws JadePersistenceException, EcheanceLibreException;

    public EcheanceLibreSearchModel search(EcheanceLibreSearchModel searchModel) throws JadePersistenceException,
            EcheanceLibreException;

    public EcheanceLibre update(EcheanceLibre echeanceLibre) throws JadePersistenceException, EcheanceLibreException,
            JadeApplicationServiceNotAvailableException;
}
