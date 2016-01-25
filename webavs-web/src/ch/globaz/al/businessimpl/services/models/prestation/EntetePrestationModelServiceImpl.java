package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.exceptions.model.prestation.ALEntetePrestationModelException;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.prestation.EntetePrestationModelService;
import ch.globaz.al.businessimpl.checker.model.prestation.EntetePrestationModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe d'implémentation des services de EntetePrestationModel
 * 
 * @author PTA
 * 
 */

public class EntetePrestationModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EntetePrestationModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.EntetePrestationModelService
     * #count(ch.globaz.al.business.models.prestation.EntetePrestationSearch)
     */
    @Override
    public int count(EntetePrestationSearchModel enteteSearch) throws JadePersistenceException,
            JadeApplicationException {

        if (enteteSearch == null) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#count : Unable to count, enteteSearch is null");
        }

        return JadePersistenceManager.count(enteteSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.EntetePrestationModelService
     * #create(ch.globaz.al.business.model.prestation.EntetePrestationModel)
     */
    @Override
    public EntetePrestationModel create(EntetePrestationModel enTetePrestModel) throws JadeApplicationException,
            JadePersistenceException {
        if (enTetePrestModel == null) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#create : Unable to create enTetePrestationModel-the model passed is null");
        }
        // validation du model
        EntetePrestationModelChecker.validate(enTetePrestModel);
        // création du model en persistence
        return (EntetePrestationModel) JadePersistenceManager.add(enTetePrestModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.EntetePrestationModelService
     * #delete(ch.globaz.al.business.model.prestation.EntetePrestationModel)
     */
    @Override
    public EntetePrestationModel delete(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException {

        if (entetePrestModel == null) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#delete : Unable to delete enTetePrestationModel-the model passed is null");
        }

        if (entetePrestModel.isNew()) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#delete : Unable to delete enTetePrestationModel-the model passed is new");
        }

        // validation avant suppression
        EntetePrestationModelChecker.validateForDelete(entetePrestModel);

        // suppression des détails de la prestation
        ALImplServiceLocator.getDetailPrestationModelService().deleteForIdEntete(entetePrestModel.getIdEntete());

        // On supprimer la récap si elle n'a plus qu'une prestation (celle qui
        // sera effacée juste après)
        if (!JadeNumericUtil.isEmptyOrZero(entetePrestModel.getIdRecap())) {
            ALServiceLocator.getRecapitulatifEntrepriseModelService().deleteIfSizeEquals(entetePrestModel.getIdRecap(),
                    1);
        }

        // Supression de la référence dans le décompte ADI
        if (ALCSPrestation.STATUT_ADI.equals(entetePrestModel.getStatut())) {

            DecompteAdiSearchModel searchDecompte = new DecompteAdiSearchModel();
            searchDecompte.setForIdPrestationAdi(entetePrestModel.getIdEntete());
            searchDecompte = ALServiceLocator.getDecompteAdiModelService().search(searchDecompte);
            if (searchDecompte.getSize() > 0) {
                DecompteAdiModel decompteToUpd = ((DecompteAdiModel) searchDecompte.getSearchResults()[0]);
                decompteToUpd.setIdPrestationAdi("0");
                ALServiceLocator.getDecompteAdiModelService().update(decompteToUpd);
            }

        }

        // suppression de l'en-tête
        return (EntetePrestationModel) JadePersistenceManager.delete(entetePrestModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.EntetePrestationModelService
     * #deleteForIdRecap(java.lang.String)
     */
    @Override
    public void deleteForIdRecap(String idRecap) throws JadePersistenceException, JadeApplicationException {
        if (JadeNumericUtil.isEmptyOrZero(idRecap)) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#deleteForIdRecap : Unable to read idRecap- the id passed is empty");
        }

        // On efface les entêtes puis les détails sans appeler le service delete
        // car on ne veut pas supprimer
        // la récap dans ce service
        EntetePrestationSearchModel searchEntete = new EntetePrestationSearchModel();
        searchEntete.setForIdRecap(idRecap);
        searchEntete = (EntetePrestationSearchModel) JadePersistenceManager.search(searchEntete);

        DetailPrestationSearchModel searchDetail = new DetailPrestationSearchModel();
        // pour chaque entête, on supprime ses détails de prestations
        for (int i = 0; i < searchEntete.getSize(); i++) {
            searchDetail.setForIdEntetePrestation(searchEntete.getSearchResults()[i].getId());
            JadePersistenceManager.delete(searchDetail);
        }

        // une fois qu'on a supprimé tout les détails, on peut supprimer
        // effectivement l'entête
        JadePersistenceManager.delete(searchEntete);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.EntetePrestationModelService #read(java.lang.String)
     */
    @Override
    public EntetePrestationModel read(String idEntetePrestModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idEntetePrestModel)) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#read : Unable to read idEntetePrestationModel- the id passed is empty");
        }
        EntetePrestationModel enTetePrestModel = new EntetePrestationModel();
        enTetePrestModel.setId(idEntetePrestModel);
        return (EntetePrestationModel) JadePersistenceManager.read(enTetePrestModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.EntetePrestationModelService #
     * search(ch.globaz.al.business.models.prestation.EntetePrestationSearchModel )
     */
    @Override
    public EntetePrestationSearchModel search(EntetePrestationSearchModel entetePrestationSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (entetePrestationSearchModel == null) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#search : unable to search with entetePrestationSearchModel - the model passed is null");
        }
        return (EntetePrestationSearchModel) JadePersistenceManager.search(entetePrestationSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.EntetePrestationModelService
     * #update(ch.globaz.al.business.model.prestation.EntetePrestationModel)
     */
    @Override
    public EntetePrestationModel update(EntetePrestationModel enTetePrestModel) throws JadeApplicationException,
            JadePersistenceException {
        if (enTetePrestModel == null) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#update : Unable to update EntetePrestationModel- the model passed is null");
        }
        if (enTetePrestModel.isNew()) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelServiceImpl#update : Unable to update EntetePrestationModel-the model passed is new");
        }
        // contrôle l'intégrité du modèle
        EntetePrestationModelChecker.validate(enTetePrestModel);

        // ajoute la mise à jour dans la persistence et le retourne
        return (EntetePrestationModel) JadePersistenceManager.update(enTetePrestModel);
    }
}
