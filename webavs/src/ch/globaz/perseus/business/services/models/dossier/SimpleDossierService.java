package ch.globaz.perseus.business.services.models.dossier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.models.dossier.SimpleDossier;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public interface SimpleDossierService extends JadeApplicationService {
    public SimpleDossier create(SimpleDossier dossier) throws JadePersistenceException, DossierException;

    public SimpleDossier delete(SimpleDossier dossier) throws JadePersistenceException, DossierException;

    public SimpleDossier read(String idDossier) throws JadePersistenceException, DossierException;

    public SimpleDossier update(SimpleDossier dossier) throws JadePersistenceException, DossierException;
}
