package ch.globaz.corvus.businessimpl.services.models.lots;

import globaz.corvus.db.lots.RELot;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabiliteSearch;
import ch.globaz.pyxis.common.Messages;

public class SimpleLotServiceImpl {
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
        model.setDateCreation(bean.getDateCreationLot());
        model.setDateEnvoi(bean.getDateEnvoiLot());
        model.setDescription(bean.getDescription());
        model.setIdJournalCA(bean.getIdJournalCA());

        // TODO check if spy from BEntity to jademodel spy
        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    private RELot _adapt(SimpleLot model) throws RentesAccordeesException {
        RELot bean = new RELot(); // destination

        // Préconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new RentesAccordeesException(Messages.MAPPING_ERROR + " - " + this.getClass().getName()
                    + "._adapt(...)");
        }

        // Mapping des données
        bean.setId(model.getId());

        bean.setId(model.getId());
        bean.setCsEtatLot(model.getCsEtat());
        bean.setCsTypeLot(model.getCsTypeLot());
        bean.setDateCreationLot(model.getDateCreation());
        bean.setDateEnvoiLot(model.getDateEnvoi());
        bean.setDescription(model.getDescription());
        bean.setIdJournalCA(model.getIdJournalCA());

        bean.populateSpy(model.getSpy());
        return bean;
    }

    private SimpleLot _perform(OPERATION operation, SimpleLot model) throws RentesAccordeesException {
        if (model == null) {
            throw new RentesAccordeesException(Messages.MODEL_IS_NULL + " - SimpleInformationComptabiliteServiceImpl."
                    + operation + "(...)");
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
            throw new RentesAccordeesException(Messages.TECHNICAL, e);
        }

        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #count(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabiliteSearch)
     */
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
    public SimpleLot create(SimpleLot simpleInformationsComptabilite) throws RentesAccordeesException,
            JadePersistenceException {
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
    public SimpleLot delete(SimpleLot simpleInformationsComptabilite) throws RentesAccordeesException,
            JadePersistenceException {
        if (simpleInformationsComptabilite == null) {
            throw new RentesAccordeesException("Unable to delete SimpleLot, the model passed is null!");
        }
        return _perform(OPERATION.DELETE, simpleInformationsComptabilite);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees.
     * SimpleInformationsComptabiliteService#read(java.lang.String)
     */
    public SimpleLot read(String idInformationsComptabilite) throws RentesAccordeesException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idInformationsComptabilite)) {
            throw new RentesAccordeesException("Unable to read SimpleLot, the id passed is not defined!");
        }
        SimpleLot simpleInfoCompta = new SimpleLot();
        simpleInfoCompta.setId(idInformationsComptabilite);
        return (SimpleLot) JadePersistenceManager.read(simpleInfoCompta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.corvus.business.services.models.rentesaccordees. SimpleInformationsComptabiliteService
     * #update(ch.globaz.corvus.business.models .rentesaccordees.SimpleInformationsComptabilite)
     */
    public SimpleLot update(SimpleLot simpleInformationsComptabilite) throws RentesAccordeesException,
            JadePersistenceException {
        if (simpleInformationsComptabilite == null) {
            throw new RentesAccordeesException("Unable to update SimpleLot, the model passed is null!");
        }

        return _perform(OPERATION.UPDATE, simpleInformationsComptabilite);
    }
}
