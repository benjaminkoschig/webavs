package ch.globaz.corvus.businessimpl.services.models.lots;

import globaz.corvus.db.lots.RELot;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.services.models.lots.LotService;
import ch.globaz.corvus.businessimpl.services.CorvusAbstractServiceImpl;
import ch.globaz.pyxis.common.Messages;

public class LotServiceImpl extends CorvusAbstractServiceImpl implements LotService {
    private enum OPERATION {
        ADD,
        DELETE,
        UPDATE
    };

    private SimpleLot _adapt(RELot bean) throws RentesAccordeesException {
        SimpleLot model = new SimpleLot(); // destination

        // Préconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new RentesAccordeesException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        model.setId(bean.getId());
        model.setCsEtat(bean.getCsEtatLot());
        model.setCsTypeLot(bean.getCsTypeLot());
        model.setCsProprietaire(bean.getCsLotOwner());
        model.setDateCreation(bean.getDateCreationLot());
        model.setDateEnvoi(bean.getDateEnvoiLot());
        model.setDescription(bean.getDescription());
        model.setIdJournalCA(bean.getIdJournalCA());

        // TODO check if spy from BEntity to jademodel spy
        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    private RELot _adapt(SimpleLot model) throws LotException {
        RELot bean = new RELot(); // destination

        // Préconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new LotException(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());

        bean.setId(model.getId());
        bean.setCsEtatLot(model.getCsEtat());
        bean.setCsTypeLot(model.getCsTypeLot());
        bean.setCsLotOwner(model.getCsProprietaire());
        bean.setDateCreationLot(model.getDateCreation());
        bean.setDateEnvoiLot(model.getDateEnvoi());
        bean.setDescription(model.getDescription());
        bean.setIdJournalCA(model.getIdJournalCA());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    private SimpleLot _perform(OPERATION operation, SimpleLot model) throws LotException {
        if (model == null) {
            throw new LotException(Messages.MODEL_IS_NULL + " - LotServiceImpl." + operation + "(...)");
        }
        // Execution de l'opération d'ajout ou de mise à jour
        RELot bean = null;
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
            throw new LotException(Messages.TECHNICAL, e);
        }

        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.lots.LotService#count(ch.globaz
     * .corvus.business.models.lots.SimpleLotSearch)
     */
    @Override
    public int count(SimpleLotSearch search) throws LotException, JadePersistenceException {
        if (search == null) {
            throw new LotException("Unable to count simpleLot, the search model passed is null!");
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
    public SimpleLot create(SimpleLot simpleLot) throws LotException, JadePersistenceException {
        if (simpleLot == null) {
            throw new LotException("Unable to create simpleLot, the model passed is null!");
        }

        return _perform(OPERATION.ADD, simpleLot);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #delete(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabilite)
     */
    @Override
    public SimpleLot delete(SimpleLot simpleLot) throws LotException, JadePersistenceException {
        if (simpleLot == null) {
            throw new LotException("Unable to delete SimpleLot, the model passed is null!");
        }
        return _perform(OPERATION.DELETE, simpleLot);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.lots.LotService#read(java.lang .String)
     */
    @Override
    public SimpleLot read(String idSimpleLot) throws JadePersistenceException, LotException {
        if (JadeStringUtil.isEmpty(idSimpleLot)) {
            throw new LotException("Unable to read simpleLot, the id passed is null!");
        }
        SimpleLot simpleLot = new SimpleLot();
        simpleLot.setId(idSimpleLot);
        return (SimpleLot) JadePersistenceManager.read(simpleLot);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.lots.LotService#search(ch.globaz
     * .corvus.business.models.lots.SimpleLotSearch)
     */
    @Override
    public SimpleLotSearch search(SimpleLotSearch search) throws JadePersistenceException, LotException {
        if (search == null) {
            throw new LotException("Unable to search simpleLot, the search model passed is null!");
        }
        return (SimpleLotSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #update(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabilite)
     */
    @Override
    public SimpleLot update(SimpleLot simpleLot) throws LotException, JadePersistenceException {
        if (simpleLot == null) {
            throw new LotException("Unable to update SimpleLot, the model passed is null!");
        }

        return _perform(OPERATION.UPDATE, simpleLot);
    }
}
