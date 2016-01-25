/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.adi;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.adi.ALDecompteAdiModelException;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.adi.DecompteAdiModelService;
import ch.globaz.al.businessimpl.checker.model.adi.DecompteAdiModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe implémentation des services de DecompteAdiModel
 * 
 * @author PTA
 * 
 */
public class DecompteAdiModelServiceImpl extends ALAbstractBusinessServiceImpl implements DecompteAdiModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.DecompteAdiModelService#count(ch.globaz
     */
    @Override
    public int count(DecompteAdiSearchModel decompteAdiSearch) throws JadePersistenceException,
            JadeApplicationException {

        if (decompteAdiSearch == null) {
            throw new ALDecompteAdiModelException("Unable to count, the passed model is null");
        }

        return JadePersistenceManager.count(decompteAdiSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.DecompteAdiModelService#create(ch.globaz
     * .al.business.model.adi.DecompteAdiModel)
     */
    @Override
    public DecompteAdiModel create(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException {
        if (decompteAdiModel == null) {
            throw new ALDecompteAdiModelException("Unable to create DecompteAdiModel - the model passed is null");
        }
        // lance le contrôle de la validité des données
        DecompteAdiModelChecker.validate(decompteAdiModel);
        // ajoute le decompte et le retourne
        return (DecompteAdiModel) JadePersistenceManager.add(decompteAdiModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.DecompteAdiModelService#delete(ch.globaz
     * .al.business.model.adi.DecompteAdiModel)
     */
    @Override
    public DecompteAdiModel delete(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException {
        if (decompteAdiModel == null) {
            throw new ALDecompteAdiModelException("Unable to delete DecompteAdiModel - the model passed is null");
        } else if (decompteAdiModel.isNew()) {
            throw new ALDecompteAdiModelException("Unable to delete DecomptAdiModel - the model passed is new");
        }
        DecompteAdiModelChecker.validateForDelete(decompteAdiModel);
        return (DecompteAdiModel) JadePersistenceManager.delete(decompteAdiModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.DecompteAdiModelService#initModel
     * (ch.globaz.al.business.models.adi.DecompteAdiModel)
     */
    @Override
    public void deleteForIdEntete(String idEntete) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idEntete)) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiModelServiceImpl#deleteForIdEntete : idEntete is empty or zero");
        }

        DecompteAdiSearchModel search = new DecompteAdiSearchModel();
        search.setForIdPrestationAdi(idEntete);
        search = ALServiceLocator.getDecompteAdiModelService().search(search);

        for (int i = 0; i < search.getSize(); i++) {
            delete((DecompteAdiModel) search.getSearchResults()[i]);
        }
    }

    @Override
    public DecompteAdiModel initModel(DecompteAdiModel decompte) throws JadePersistenceException,
            JadeApplicationException {

        if (decompte == null) {
            throw new ALDecompteAdiModelException("DecompteAdiModelServiceImpl#initModel : decompte is null");
        }
        decompte.setPeriodeDebut("");
        decompte.setPeriodeFin("");
        decompte.setDateEtat("");
        decompte.setDateReception("");
        decompte.setAnneeDecompte("");
        return decompte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.DecompteAdiModelService#read(java.lang .String)
     */
    @Override
    public DecompteAdiModel read(String idDecompteAdiModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idDecompteAdiModel)) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiModelServiceImpl#read : Unable to read DecompteAdiModel- the id passed is empty");
        }
        DecompteAdiModel decompteAdiModel = new DecompteAdiModel();
        decompteAdiModel.setId(idDecompteAdiModel);
        return (DecompteAdiModel) JadePersistenceManager.read(decompteAdiModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.DecompteAdiModelService#search
     * (ch.globaz.al.business.models.adi.DecompteAdiSearchModel)
     */
    @Override
    public DecompteAdiSearchModel search(DecompteAdiSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException {

        if (searchModel == null) {
            throw new ALDecompteAdiModelException(
                    "DecompteAdiModelServiceImpl#search : unable to search - the model passed is null");
        }

        return (DecompteAdiSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.DecompteAdiModelService#update(ch.globaz
     * .al.business.model.adi.DecompteAdiModel)
     */
    @Override
    public DecompteAdiModel update(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException {
        if (decompteAdiModel == null) {
            throw new ALDecompteAdiModelException("Unable to update DecompteAdiModel - the model passed is null");
        } else if (decompteAdiModel.isNew()) {
            throw new ALDecompteAdiModelException("Unable to update DecompteAdiModel - the model passed is new");
        }
        // lance la validation de l'intégrité métier et des données
        DecompteAdiModelChecker.validate(decompteAdiModel);
        // ajoute le décompte à modifier dans la persistance et le retourne
        return (DecompteAdiModel) JadePersistenceManager.update(decompteAdiModel);
    }

}
