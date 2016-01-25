package ch.globaz.pegasus.businessimpl.services.models.transfert;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppressionSearch;
import ch.globaz.pegasus.business.services.models.transfert.TransfertDossierSuppressionService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class TransfertDossierSuppressionServiceImpl extends PegasusAbstractServiceImpl implements
        TransfertDossierSuppressionService {

    @Override
    public int count(TransfertDossierSuppressionSearch search) throws TransfertDossierException,
            JadePersistenceException {
        if (search == null) {
            throw new TransfertDossierException(
                    "Unable to count transfertDossierSuppression, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public TransfertDossierSuppression create(TransfertDossierSuppression transfert) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException {
        if (transfert == null) {
            throw new TransfertDossierException(
                    "Unable to create transfertDossierSuppression, the model passed is null!");
        }
        transfert.setSimpleTransfertDossierSuppression(PegasusImplServiceLocator.getSimpleTransfertDossierSuppression()
                .create(transfert.getSimpleTransfertDossierSuppression()));

        // this.synchronizeCopies(transfert);

        return transfert;

    }

    @Override
    public TransfertDossierSuppression delete(TransfertDossierSuppression transfert) throws TransfertDossierException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (transfert == null) {
            throw new TransfertDossierException(
                    "Unable to delete transfertDossierSuppression, the model passed is null!");
        }
        // this.deleteCopies(transfert);

        transfert.setSimpleTransfertDossierSuppression(PegasusImplServiceLocator.getSimpleTransfertDossierSuppression()
                .delete(transfert.getSimpleTransfertDossierSuppression()));

        return transfert;
    }

    // private void deleteCopies(TransfertDossierSuppression transfert) throws TransfertDossierException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException {
    //
    // for (SimpleTransfertDossierSuppressionCopie copie : transfert.getCopies()) {
    // if (!copie.isNew()) {
    // PegasusImplServiceLocator.getSimpleTransfertDossierSuppressionCopieService().delete(copie);
    // }
    // }
    //
    // }

    @Override
    public TransfertDossierSuppression read(String idTransfertDossierSuppression) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException {
        if (idTransfertDossierSuppression == null) {
            throw new TransfertDossierException("Unable to read transfertDossierSuppression, the id passed is null!");
        }
        TransfertDossierSuppression transfert = new TransfertDossierSuppression();
        transfert.setId(idTransfertDossierSuppression);
        transfert = (TransfertDossierSuppression) JadePersistenceManager.read(transfert);

        // recherche des copies de tiers liés au transfert
        // SimpleTransfertDossierSuppressionCopieSearch copieSearchModel = new
        // SimpleTransfertDossierSuppressionCopieSearch();
        // copieSearchModel.setForIdTransfertDossierSuppression(idTransfertDossierSuppression);
        // copieSearchModel = PegasusImplServiceLocator.getSimpleTransfertDossierSuppressionCopieService().search(
        // copieSearchModel);
        //
        // for (JadeAbstractModel absDonnee : copieSearchModel.getSearchResults()) {
        // SimpleTransfertDossierSuppressionCopie donnee = (SimpleTransfertDossierSuppressionCopie) absDonnee;
        // transfert.getCopies().add(donnee);
        // }

        return transfert;
    }

    @Override
    public TransfertDossierSuppressionSearch search(TransfertDossierSuppressionSearch transfertSearch)
            throws JadePersistenceException, TransfertDossierException {
        if (transfertSearch == null) {
            throw new TransfertDossierException(
                    "Unable to search transfertDossierSuppression, the search model passed is null!");
        }
        return (TransfertDossierSuppressionSearch) JadePersistenceManager.search(transfertSearch);
    }

    // private void synchronizeCopies(TransfertDossierSuppression transfert) throws TransfertDossierException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException {
    // for (SimpleTransfertDossierSuppressionCopie copie : transfert.getCopies()) {
    // if (copie.isNew()) {
    // PegasusImplServiceLocator.getSimpleTransfertDossierSuppressionCopieService().create(copie);
    // } else {
    // PegasusImplServiceLocator.getSimpleTransfertDossierSuppressionCopieService().update(copie);
    // }
    // }
    //
    // }

    @Override
    public TransfertDossierSuppression update(TransfertDossierSuppression transfert) throws JadePersistenceException,
            TransfertDossierException, JadeApplicationServiceNotAvailableException {
        if (transfert == null) {
            throw new TransfertDossierException(
                    "Unable to update transfertDossierSuppression, the model passed is null!");
        }

        transfert.setSimpleTransfertDossierSuppression(PegasusImplServiceLocator.getSimpleTransfertDossierSuppression()
                .update(transfert.getSimpleTransfertDossierSuppression()));
        // this.synchronizeCopies(transfert);

        return transfert;
    }

}
