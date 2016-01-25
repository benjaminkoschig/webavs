package ch.globaz.vulpecula.businessimpl.services.comptabilite;

import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.vulpecula.business.models.comptabilite.ReferenceRubriqueComplexModel;
import ch.globaz.vulpecula.business.models.comptabilite.ReferenceRubriqueSearchComplexModel;
import ch.globaz.vulpecula.business.services.comptabilite.ReferenceRubriqueServiceCRUD;

public class ReferenceRubriqueServiceCRUDImpl implements ReferenceRubriqueServiceCRUD {

    @Override
    public int count(ReferenceRubriqueSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public ReferenceRubriqueComplexModel create(ReferenceRubriqueComplexModel referenceRubriqueComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        CAReferenceRubrique referenceRubriqueEntity = new CAReferenceRubrique();
        referenceRubriqueEntity.setSession(BSessionUtil.getSessionFromThreadContext());
        referenceRubriqueEntity.setIdRubrique(referenceRubriqueComplexModel.getReferenceRubriqueSimpleModel()
                .getIdRubrique());
        referenceRubriqueEntity.setIdCodeReference(referenceRubriqueComplexModel.getReferenceRubriqueSimpleModel()
                .getIdCodeReference());
        try {
            referenceRubriqueEntity.add();
        } catch (Exception e) {
            throw new PerseusException("Problème lors de l'ajout de la référence rubrique");
        }
        referenceRubriqueComplexModel.setId(referenceRubriqueEntity.getId());
        return referenceRubriqueComplexModel;
    }

    @Override
    public ReferenceRubriqueComplexModel delete(ReferenceRubriqueComplexModel referenceRubriqueComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReferenceRubriqueComplexModel read(String id) throws JadeApplicationException, JadePersistenceException {
        ReferenceRubriqueSearchComplexModel searchModel = new ReferenceRubriqueSearchComplexModel();
        String[] splitted = id.split("\\|");
        searchModel.setForIdRubrique(splitted[0]);
        if (splitted.length > 1) {
            searchModel.setForReferenceRubrique(splitted[1]);
        }
        return (ReferenceRubriqueComplexModel) JadePersistenceManager.search(searchModel).getSearchResults()[0];
    }

    @Override
    public ReferenceRubriqueSearchComplexModel search(ReferenceRubriqueSearchComplexModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        return (ReferenceRubriqueSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public ReferenceRubriqueComplexModel update(ReferenceRubriqueComplexModel referenceRubriqueComplexModel)
            throws JadeApplicationException, JadePersistenceException {
        JadePersistenceManager.update(referenceRubriqueComplexModel.getReferenceRubriqueSimpleModel());
        return referenceRubriqueComplexModel;
    }

}
