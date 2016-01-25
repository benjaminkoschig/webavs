package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALLienDossierModelException;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.dossier.LienDossierSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.dossier.LienDossierModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class LienDossierModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        ch.globaz.al.business.services.models.dossier.LienDossierModelService {

    @Override
    public LienDossierModel create(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException {
        if (lienDossierModel == null) {
            throw new ALLienDossierModelException("Unable to add model (lienDossierModel) - the model passed is null!");
        }
        // Valide le modèle
        LienDossierModelChecker.validate(lienDossierModel);
        // L'ajoute en persistence
        return (LienDossierModel) JadePersistenceManager.add(lienDossierModel);
    }

    @Override
    public LienDossierModel delete(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException {
        if (lienDossierModel == null) {
            throw new ALLienDossierModelException(
                    "Unable to remove model (lienDossierModel) - the model passed is null!");
        }
        if (lienDossierModel.isNew()) {
            throw new ALLienDossierModelException(
                    "Unable to remove model (lienDossierModel) - the model passed is new!");
        }

        LienDossierModelChecker.validateForDelete(lienDossierModel);

        if (!ALAbstractChecker.hasError()) {

            // suppression des choses liées éventuelles

        }

        // Le supprime en DB
        return (LienDossierModel) JadePersistenceManager.delete(lienDossierModel);

    }

    @Override
    public LienDossierModel read(String idLienDossierModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idLienDossierModel)) {
            throw new ALLienDossierModelException(
                    "Unable to read model (lienDossierModel) - the id passed is not defined!");
        }
        LienDossierModel lienDossierModel = new LienDossierModel();
        lienDossierModel.setId(idLienDossierModel);
        return (LienDossierModel) JadePersistenceManager.read(lienDossierModel);
    }

    @Override
    public LienDossierSearchModel search(LienDossierSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALLienDossierModelException("LienDossierModelServiceImpl#search : searchModel is null");
        }

        return (LienDossierSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public LienDossierModel update(LienDossierModel lienDossierModel) throws JadeApplicationException,
            JadePersistenceException {
        if (lienDossierModel == null) {
            throw new ALLienDossierModelException(
                    "Unable to update model (lienDossierModel) - the model passed is null!");
        }
        if (lienDossierModel.isNew()) {
            throw new ALLienDossierModelException(
                    "Unable to update model (lienDossierModel) - the model passed is new!");
        }
        // Valide l'integrity
        LienDossierModelChecker.validate(lienDossierModel);

        // Le met à jour en DB
        return (LienDossierModel) JadePersistenceManager.update(lienDossierModel);
    }

}
