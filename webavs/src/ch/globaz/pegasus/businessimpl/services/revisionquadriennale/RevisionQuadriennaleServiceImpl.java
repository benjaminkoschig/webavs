package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.pegasus.business.exceptions.models.revisionquadriennale.RevisionQuadriennaleException;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereSearch;
import ch.globaz.pegasus.business.services.revisionquadriennale.RevisionQuadriennaleService;
import ch.globaz.pegasus.businessimpl.services.doc.FullListeExcelRevision;
import ch.globaz.pegasus.businessimpl.services.doc.excel.impl.ListeExcelRevisions;

public class RevisionQuadriennaleServiceImpl implements RevisionQuadriennaleService {

    @Override
    public String generateListRevisionComplete(String annee, String moisAnnee) throws Exception {

        FullListeExcelRevision listeRevisionComplete = new FullListeExcelRevision(annee);
        return listeRevisionComplete.generate();
    }

    @Override
    public String generateListRevisionSimple(String annee, String moisAnnee) throws Exception {
        ListeExcelRevisions listeRevisions = new ListeExcelRevisions();
        return listeRevisions.createDocAndSave(annee, moisAnnee);
    }

    @Override
    public DonneeFinanciereSearch search(DonneeFinanciereSearch searchModel) throws RevisionQuadriennaleException,
            JadePersistenceException {
        Checkers.checkNotNull(searchModel, "donneesRevisionSearch");
        return (DonneeFinanciereSearch) JadePersistenceManager.search(searchModel);
    }
}
