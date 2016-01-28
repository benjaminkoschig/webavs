/**
 * 
 */
package ch.globaz.corvus.businessimpl.services.models.rentesaccordees;

import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabiliteSearch;
import ch.globaz.corvus.business.services.models.rentesaccordees.SimpleInformationsComptabiliteService;
import ch.globaz.corvus.businessimpl.services.CorvusAbstractServiceImpl;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;
import ch.globaz.pyxis.common.Messages;

/**
 * @author ECO
 * 
 */
public class SimpleInformationsComptabiliteServiceImpl extends CorvusAbstractServiceImpl implements
        SimpleInformationsComptabiliteService {

    private enum OPERATION {
        ADD,
        DELETE,
        UPDATE
    };

    private SimpleInformationsComptabilite _adapt(REInformationsComptabilite bean) throws RentesAccordeesException {
        SimpleInformationsComptabilite model = new SimpleInformationsComptabilite(); // destination

        // Préconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new RentesAccordeesException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        model.setId(bean.getId());
        model.setIdTiersAdressePmt(bean.getIdTiersAdressePmt());
        model.setIdCompteAnnexe(bean.getIdCompteAnnexe());

        // TODO check if spy from BEntity to jademodel spy
        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    private REInformationsComptabilite _adapt(SimpleInformationsComptabilite model) throws RentesAccordeesException {
        REInformationsComptabilite bean = new REInformationsComptabilite(); // destination

        // Préconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new RentesAccordeesException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());
        bean.setIdTiersAdressePmt(model.getIdTiersAdressePmt());
        bean.setIdCompteAnnexe(model.getIdCompteAnnexe());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    private SimpleInformationsComptabilite _perform(final OPERATION operation,
            final SimpleInformationsComptabilite model) throws RentesAccordeesException {
        if (model == null) {
            throw new RentesAccordeesException(Messages.MODEL_IS_NULL + " - SimpleInformationComptabiliteServiceImpl."
                    + operation + "(...)");
        }

        // Execution de l'opération d'ajout ou de mise à jour
        OldPersistence<SimpleInformationsComptabilite> pers = new OldPersistence<SimpleInformationsComptabilite>() {
            @Override
            public SimpleInformationsComptabilite action() throws PCAccordeeException {

                REInformationsComptabilite bean = null;
                SimpleInformationsComptabilite modelReturn = null;
                try {
                    bean = SimpleInformationsComptabiliteServiceImpl.this._adapt(model);

                    switch (operation) {
                        case ADD:
                            bean.add();
                            modelReturn = SimpleInformationsComptabiliteServiceImpl.this._adapt(bean);
                            break;

                        case UPDATE:
                            bean.update();
                            modelReturn = SimpleInformationsComptabiliteServiceImpl.this._adapt(bean);
                            break;

                        case DELETE:
                            bean.delete();
                            break;

                    }

                } catch (Exception e) {
                    throw new PCAccordeeException(Messages.TECHNICAL, e);
                }

                return modelReturn;
            }
        };

        try {
            return pers.execute();
        } catch (Exception e) {
            throw new RentesAccordeesException("Unalble to creat the informationComptabilite", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #count(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabiliteSearch)
     */
    @Override
    public int count(SimpleInformationsComptabiliteSearch search) throws RentesAccordeesException,
            JadePersistenceException {
        if (search == null) {
            throw new RentesAccordeesException(
                    "Unable to count SimpleInformationsComptabilite, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #create(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabilite)
     */
    @Override
    public SimpleInformationsComptabilite create(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException {
        if (simpleInformationsComptabilite == null) {
            throw new RentesAccordeesException(
                    "Unable to create SimpleInformationsComptabilite, the model passed is null!");
        }

        return _perform(OPERATION.ADD, simpleInformationsComptabilite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #delete(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabilite)
     */
    @Override
    public SimpleInformationsComptabilite delete(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException {
        if (simpleInformationsComptabilite == null) {
            throw new RentesAccordeesException(
                    "Unable to delete SimpleInformationsComptabilite, the model passed is null!");
        }
        return _perform(OPERATION.DELETE, simpleInformationsComptabilite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees.
     * SimpleInformationsComptabiliteService#read(java.lang.String)
     */
    @Override
    public SimpleInformationsComptabilite read(String idInformationsComptabilite) throws RentesAccordeesException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idInformationsComptabilite)) {
            throw new RentesAccordeesException(
                    "Unable to read SimpleInformationsComptabilite, the id passed is not defined!");
        }
        SimpleInformationsComptabilite simpleInfoCompta = new SimpleInformationsComptabilite();
        simpleInfoCompta.setId(idInformationsComptabilite);
        return (SimpleInformationsComptabilite) JadePersistenceManager.read(simpleInfoCompta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #update(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabilite)
     */
    @Override
    public SimpleInformationsComptabilite update(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException {
        if (simpleInformationsComptabilite == null) {
            throw new RentesAccordeesException(
                    "Unable to update SimpleInformationsComptabilite, the model passed is null!");
        }

        return _perform(OPERATION.UPDATE, simpleInformationsComptabilite);
    }

}
