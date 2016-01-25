/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.rentepont.QDRentePontException;
import ch.globaz.perseus.business.models.rentepont.QDRentePont;
import ch.globaz.perseus.business.models.rentepont.QDRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.SimpleQDRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.rentepont.QDRentePontService;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author JSI
 * 
 */
public class QDRentePontServiceImpl implements QDRentePontService {

    @Override
    public int count(QDRentePontSearchModel search) throws QDRentePontException, JadePersistenceException {
        if (search == null) {
            throw new QDRentePontException("Unable to count qdRentePonts, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public QDRentePont create(QDRentePont qdRentePont) throws JadePersistenceException, QDRentePontException {
        if (qdRentePont == null) {
            throw new QDRentePontException("Unable to create qdRentePont, the given model is null!");
        }
        try {
            QDRentePontSearchModel searchModel = new QDRentePontSearchModel();
            searchModel.setForIdDossier(qdRentePont.getSimpleQDRentePont().getIdDossier());
            searchModel.setForAnnee(qdRentePont.getSimpleQDRentePont().getAnnee());
            searchModel = PerseusServiceLocator.getQDRentePontService().search(searchModel);
            if (searchModel.getSize() > 0) {
                throw new QDRentePontException("QD already exists !");
            } else {
                qdRentePont.setSimpleQDRentePont(PerseusImplServiceLocator.getSimpleQDRentePontService().create(
                        qdRentePont.getSimpleQDRentePont()));
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDRentePontException("Service not available - " + e.getMessage());
        }
        return qdRentePont;
    }

    @Override
    public QDRentePont delete(QDRentePont qdRentePont) throws JadePersistenceException, QDRentePontException {
        if (qdRentePont == null) {
            throw new QDRentePontException("Unable to delete qdRentePont, the given model is null!");
        }
        SimpleQDRentePont simpleQDRentePont = qdRentePont.getSimpleQDRentePont();
        try {
            simpleQDRentePont = PerseusImplServiceLocator.getSimpleQDRentePontService().delete(simpleQDRentePont);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDRentePontException("Service not avaiable : " + e.toString(), e);
        }
        qdRentePont.setSimpleQDRentePont(simpleQDRentePont);

        return qdRentePont;
    }

    @Override
    public float getMontantMaximalRemboursable(QDRentePont qdRentePont) {
        Float qdLimite = Float.parseFloat(qdRentePont.getSimpleQDRentePont().getMontantLimite());
        Float qdUtilise = Float.parseFloat(qdRentePont.getSimpleQDRentePont().getMontantUtilise());

        return qdLimite - qdUtilise;
    }

    @Override
    public QDRentePont read(String idQDRentePont) throws JadePersistenceException, QDRentePontException {
        if (idQDRentePont == null) {
            throw new QDRentePontException("Unable to read qdRentePont, the given id is null!");
        }
        QDRentePont qdRentePont = new QDRentePont();
        qdRentePont.setId(idQDRentePont);
        return (QDRentePont) JadePersistenceManager.read(qdRentePont);
    }

    @Override
    public QDRentePontSearchModel search(QDRentePontSearchModel searchModel) throws JadePersistenceException,
            QDRentePontException {
        if (searchModel == null) {
            throw new QDRentePontException("Unable to search qdRentePont, the given model is null!");
        }
        return (QDRentePontSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public QDRentePont update(QDRentePont qdRentePont) throws JadePersistenceException, QDRentePontException {
        if (qdRentePont == null) {
            throw new QDRentePontException("Unable to update qdRentePont, the given model is null!");
        }
        try {
            qdRentePont.setSimpleQDRentePont(PerseusImplServiceLocator.getSimpleQDRentePontService().update(
                    qdRentePont.getSimpleQDRentePont()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new QDRentePontException("Service not avaiable : " + e.toString(), e);
        }
        return qdRentePont;
    }

}
