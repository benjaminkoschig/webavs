package ch.globaz.corvus.businessimpl.services.models.blocage;

import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.blocage.SimpleEnteteBlocage;
import ch.globaz.corvus.business.services.models.blocage.SimpleEnteteBlocageService;
import ch.globaz.pyxis.common.Messages;

public class SimpleEnteteBlocageServiceImpl implements SimpleEnteteBlocageService {
    private enum OPERATION {
        ADD,
        DELETE,
        UPDATE
    };

    private SimpleEnteteBlocage _adapt(REEnteteBlocage bean) throws RentesAccordeesException {
        SimpleEnteteBlocage model = new SimpleEnteteBlocage(); // destination

        // Preconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new RentesAccordeesException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        model.setId(bean.getId());
        model.setMontantBloque(bean.getMontantBloque());
        model.setMontantDebloque(bean.getMontantDebloque());

        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    private REEnteteBlocage _adapt(SimpleEnteteBlocage model) throws RentesAccordeesException {
        REEnteteBlocage bean = new REEnteteBlocage(); // destination

        // Preconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new RentesAccordeesException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());

        bean.setId(model.getId());
        bean.setMontantBloque(model.getMontantBloque());
        bean.setMontantDebloque(model.getMontantDebloque());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    private SimpleEnteteBlocage _perform(OPERATION operation, SimpleEnteteBlocage model)
            throws RentesAccordeesException {
        if (model == null) {
            throw new RentesAccordeesException(Messages.MODEL_IS_NULL + " - SimpleEnteteBlocageServiceImpl."
                    + operation + "(...)");
        }
        // Execution de l'opération d'ajout ou de mise à jour
        REEnteteBlocage bean = null;
        try {
            bean = this._adapt(model);

            switch (operation) {
                case ADD:
                    bean.add();
                    model = this._adapt(bean);

                    break;
                case UPDATE:
                    bean.update();
                    model = this._adapt(bean);

                    break;
                case DELETE:
                    bean.delete();

                    break;

            }

        } catch (Exception e) {
            throw new RentesAccordeesException(Messages.TECHNICAL, e);
        }

        return model;
    }

    // public int count(SimpleEnteteBlocageSearch search) throws RentesAccordeesException,
    // JadePersistenceException {
    // if (search == null) {
    // throw new RentesAccordeesException(
    // "Unable to count SimpleInformationsComptabilite, the search model passed is null!");
    // }
    // return JadePersistenceManager.count(search);
    // }

    @Override
    public SimpleEnteteBlocage create(SimpleEnteteBlocage model) throws RentesAccordeesException,
            JadePersistenceException {
        if (model == null) {
            throw new RentesAccordeesException("Unable to create SimpleEnteteBlocage, the model passed is null!");
        }

        return _perform(OPERATION.ADD, model);
    }

    @Override
    public SimpleEnteteBlocage delete(SimpleEnteteBlocage model) throws RentesAccordeesException,
            JadePersistenceException {
        if (model == null) {
            throw new RentesAccordeesException("Unable to delete SimpleEnteteBlocage, the model passed is null!");
        }
        return _perform(OPERATION.DELETE, model);
    }

    @Override
    public SimpleEnteteBlocage read(String idEnteteBlocage) throws RentesAccordeesException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idEnteteBlocage)) {
            throw new RentesAccordeesException("Unable to read SimpleEnteteBlocage, the id passed is not defined!");
        }
        SimpleEnteteBlocage model = new SimpleEnteteBlocage();
        model.setId(idEnteteBlocage);
        return (SimpleEnteteBlocage) JadePersistenceManager.read(model);
    }

    @Override
    public SimpleEnteteBlocage update(SimpleEnteteBlocage model) throws RentesAccordeesException,
            JadePersistenceException {
        if (model == null) {
            throw new RentesAccordeesException("Unable to update SimpleEnteteBlocage, the model passed is null!");
        }

        return _perform(OPERATION.UPDATE, model);
    }

}
