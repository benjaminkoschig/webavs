package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.facture.FactureException;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.business.services.models.qd.SimpleFactureService;
import ch.globaz.perseus.businessimpl.checkers.qd.SimpleFactureChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * Implémentation des services pour un simple modèle de facture.
 * 
 * @author JSI
 * 
 */
public class SimpleFactureServiceImpl extends PerseusAbstractServiceImpl implements SimpleFactureService {

    @Override
    public SimpleFacture create(SimpleFacture facture) throws JadePersistenceException, PerseusException,
            JadeApplicationServiceNotAvailableException {
        if (facture == null) {
            throw new FactureException("Unable to create a simple Facture, the model passed is null");
        }
        SimpleFactureChecker.checkForCreate(facture);
        return (SimpleFacture) JadePersistenceManager.add(facture);
    }

    @Override
    public SimpleFacture delete(SimpleFacture facture) throws JadePersistenceException, FactureException {
        if (facture == null) {
            throw new FactureException("Unable to delete a simple facture, the model passed is null!");
        } else if (facture.isNew()) {
            throw new FactureException("Unable to delete a simple facture, the model passed is new!");
        }
        SimpleFactureChecker.checkForDelete(facture);
        return (SimpleFacture) JadePersistenceManager.delete(facture);
    }

    @Override
    public SimpleFacture read(String idFacture) throws JadePersistenceException, FactureException {
        if (JadeStringUtil.isBlank(idFacture)) {
            throw new FactureException("Unable to read a simple facture, the idFacture passed is null.");
        }
        SimpleFacture facture = new SimpleFacture();
        facture.setId(idFacture);
        return (SimpleFacture) JadePersistenceManager.read(facture);
    }

    @Override
    public SimpleFacture update(SimpleFacture facture) throws JadePersistenceException, FactureException {
        if (facture == null) {
            throw new FactureException("Unable to update a simple facture, the model passed is null!");
        } else if (facture.isNew()) {
            throw new FactureException("Unable to update a simple facture, the model passed is new!");
        }
        SimpleFactureChecker.checkForUpdate(facture);
        return (SimpleFacture) JadePersistenceManager.update(facture);
    }

}
