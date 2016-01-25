package ch.globaz.perseus.business.services.models.echeance;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.EcheanceLibreException;
import ch.globaz.perseus.business.models.echeance.SimpleEcheanceLibre;
import ch.globaz.perseus.business.models.echeance.SimpleEcheanceLibreSearchModel;

public interface SimpleEcheanceLibreService extends JadeApplicationService {
    public int count(SimpleEcheanceLibreSearchModel searchModel) throws JadePersistenceException,
            EcheanceLibreException;

    public SimpleEcheanceLibre create(SimpleEcheanceLibre echeanceLibre) throws JadePersistenceException,
            EcheanceLibreException;

    public SimpleEcheanceLibre delete(SimpleEcheanceLibre echeanceLibre) throws JadePersistenceException,
            EcheanceLibreException;

    public SimpleEcheanceLibre read(String idEcheanceLibre) throws JadePersistenceException, EcheanceLibreException;

    public SimpleEcheanceLibreSearchModel search(SimpleEcheanceLibreSearchModel searchModel)
            throws JadePersistenceException, EcheanceLibreException;

    public SimpleEcheanceLibre update(SimpleEcheanceLibre echeanceLibre) throws JadePersistenceException,
            EcheanceLibreException;
}
