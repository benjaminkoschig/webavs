package ch.globaz.al.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALCopieModelException;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.models.dossier.CopieSearchModel;
import ch.globaz.al.business.services.models.dossier.CopieModelService;
import ch.globaz.al.businessimpl.checker.model.dossier.CopieModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de gestion de la persistance des données des copies de dossier
 * 
 * @author jts
 */
public class CopieModelServiceImpl extends ALAbstractBusinessServiceImpl implements CopieModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.CopieModelService#copy
     * (ch.globaz.al.business.models.dossier.CopieModel)
     */
    @Override
    public CopieModel copy(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        if (copieModel == null) {
            throw new ALCopieModelException("Unable to copy copieModel- the model passed is null");
        }

        CopieModel copieNew = new CopieModel();
        copieNew.setIdDossier(copieModel.getIdDossier());
        copieNew.setIdTiersDestinataire(copieModel.getIdTiersDestinataire());
        copieNew.setOrdreCopie(copieModel.getOrdreCopie());
        copieNew.setTypeCopie(copieModel.getTypeCopie());

        return copieNew;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.CopieModelService#count
     * (ch.globaz.al.business.models.dossier.CopieSearch)
     */
    @Override
    public int count(CopieSearchModel copieSearch) throws JadePersistenceException, JadeApplicationException {

        if (copieSearch == null) {
            throw new ALCopieModelException("CopieModelServiceImpl#count : copieSearch is null");
        }

        // pour les autres méthodes
        return JadePersistenceManager.count(copieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieModelService#create(
     * ch.globaz.al.business.model.dossier.CopieModel)
     */
    @Override
    public CopieModel create(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        if (copieModel == null) {
            throw new ALCopieModelException(
                    "CopieModelServiceImpl#create : Unable to add model (copieModel) - the model passed is null!");
        }

        CopieModelChecker.validate(copieModel);
        return (CopieModel) JadePersistenceManager.add(copieModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieModelService#delete(
     * ch.globaz.al.business.model.dossier.CopieModel)
     */
    @Override
    public CopieModel delete(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        if (copieModel == null) {
            throw new ALCopieModelException(
                    "CopieModelServiceImpl#delete : Unable to remove model (copieModel) - the model passed is null!");
        }

        if (copieModel.isNew()) {
            throw new ALCopieModelException(
                    "CopieModelServiceImpl#delete : Unable to remove model (copieModel) - the model passed is new!");
        }

        // Le supprime en DB
        return (CopieModel) JadePersistenceManager.delete(copieModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieModelService#update
     * (ch.globaz.al.business.model.dossier.CopieModel)
     */
    @Override
    public CopieModel initModel(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        if (copieModel == null) {
            throw new ALCopieModelException("CopieModelServiceImpl#initModel : copieModel is null");
        }

        copieModel.setImpressionBatch(false);
        return copieModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.dossier.CopieModelService#read(java .lang.String)
     */
    @Override
    public CopieModel read(String idCopie) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idCopie)) {
            throw new ALCopieModelException(
                    "CopieModelServiceImpl#read : Unable to read model (copieModel) - the id passed is not defined!");
        }

        CopieModel copieModel = new CopieModel();
        copieModel.setId(idCopie);
        return (CopieModel) JadePersistenceManager.read(copieModel);
    }

    @Override
    public CopieSearchModel search(CopieSearchModel copieSearchModel) throws JadeApplicationException,
            JadePersistenceException {

        if (copieSearchModel == null) {
            throw new ALCopieModelException("Unable to search copieSearchModel- the model passed is null");
        }

        return (CopieSearchModel) JadePersistenceManager.search(copieSearchModel);
    }

    @Override
    public CopieModel update(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException {

        if (copieModel == null) {
            throw new ALCopieModelException(
                    "CopieModelServiceImpl#update : Unable to update model (copieModel) - the model passed is null!");
        }

        if (copieModel.isNew()) {
            throw new ALCopieModelException(
                    "CopieModelServiceImpl#update : Unable to update model (copieModel) - the model passed is new!");
        }

        CopieModelChecker.validate(copieModel);
        return (CopieModel) JadePersistenceManager.update(copieModel);
    }
}
