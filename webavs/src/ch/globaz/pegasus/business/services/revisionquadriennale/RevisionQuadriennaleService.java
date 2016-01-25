package ch.globaz.pegasus.business.services.revisionquadriennale;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revisionquadriennale.RevisionQuadriennaleException;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereSearch;

public interface RevisionQuadriennaleService extends JadeApplicationService {

    public String generateListRevisionSimple(String annee, String moisAnnee) throws Exception;

    public String generateListRevisionComplete(String annee, String moisAnnee) throws Exception;

    public DonneeFinanciereSearch search(DonneeFinanciereSearch searchModel) throws RevisionQuadriennaleException,
            JadePersistenceException;
}
