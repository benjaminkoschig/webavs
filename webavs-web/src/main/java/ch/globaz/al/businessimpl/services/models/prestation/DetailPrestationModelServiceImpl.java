package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.prestation.ALDetailPrestationModelException;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchModel;
import ch.globaz.al.business.services.models.prestation.DetailPrestationModelService;
import ch.globaz.al.businessimpl.checker.model.prestation.DetailPrestationModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe d'implémentation des services DetailPrestationModel
 * 
 * @author PTA
 * 
 */
public class DetailPrestationModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DetailPrestationModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.DetailPrestationModelService
     * #count(ch.globaz.al.business.models.prestation.DetailPrestationSearch)
     */
    @Override
    public int count(DetailPrestationSearchModel detailPrestSearch) throws JadeApplicationException,
            JadePersistenceException {

        if (detailPrestSearch == null) {
            throw new ALDetailPrestationModelException(
                    "DetailPrestationModelServiceImpl#count : detailPrestSearch is null");
        }

        return JadePersistenceManager.count(detailPrestSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.DetailPrestationModelService
     * #create(ch.globaz.al.business.model.prestation.DetailPrestationModel)
     */
    @Override
    public DetailPrestationModel create(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException {
        if (detailPrestationModel == null) {
            throw new ALDetailPrestationModelException(
                    "Unable to create detailPrestationModel- the model passed is null!");
        }
        // validation du modèle DetailPrestationModel
        DetailPrestationModelChecker.validate(detailPrestationModel);
        // Ajoute le DetailPrestation dans la persistance
        return (DetailPrestationModel) JadePersistenceManager.add(detailPrestationModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.DetailPrestationModelService
     * #delete(ch.globaz.al.business.models.prestation.DetailPrestationModel)
     */
    @Override
    public DetailPrestationModel delete(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException {
        if (detailPrestationModel == null) {
            throw new ALDetailPrestationModelException(
                    "Unable to delete detailPrestationModel- the model passed is null!");
        }
        // si le modele n'existe pas encore
        if (detailPrestationModel.isNew()) {
            throw new ALDetailPrestationModelException("Unable to delete detailPrestaionModel-the entity is new");
        }

        // suppression des données tucana
        TransfertTucanaSearchModel tucana = new TransfertTucanaSearchModel();
        tucana.setForIdDetailPrestation(detailPrestationModel.getId());
        tucana = ALImplServiceLocator.getTransfertTucanaModelService().search(tucana);

        if (tucana.getSize() == 1) {
            ALImplServiceLocator.getTransfertTucanaModelService().delete(
                    (TransfertTucanaModel) tucana.getSearchResults()[0]);
        } else if (tucana.getSize() > 1) {
            throw new ALDetailPrestationModelException(
                    "DetailPrestationModelServiceImpl#delete : Plusieurs rubrique Tucana ont été trouvées pour ce détail de prestation : "
                            + detailPrestationModel.getId());
        }

        DetailPrestationModelChecker.validateForDelete(detailPrestationModel);

        return (DetailPrestationModel) JadePersistenceManager.delete(detailPrestationModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.DetailPrestationModelService
     * #deleteForIdEntete(java.lang.String)
     */
    @Override
    public void deleteForIdEntete(String idEntete) throws JadePersistenceException, JadeApplicationException {

        if (!JadeNumericUtil.isIntegerPositif(idEntete)) {
            throw new ALDetailPrestationModelException(
                    "DetailPrestationModelServiceImpl#deleteForIdEntete : idEntete is null or zero");
        }

        DetailPrestationSearchModel search = new DetailPrestationSearchModel();
        search.setForIdEntetePrestation(idEntete);

        search = search(search);

        for (int i = 0; i < search.getSize(); i++) {
            DetailPrestationModel detail = ((DetailPrestationModel) (search.getSearchResults()[i]));

            TransfertTucanaSearchModel tucana = new TransfertTucanaSearchModel();
            tucana.setForIdDetailPrestation(detail.getId());
            tucana = ALImplServiceLocator.getTransfertTucanaModelService().search(tucana);

            if (tucana.getSize() == 1) {
                ALImplServiceLocator.getTransfertTucanaModelService().delete(
                        (TransfertTucanaModel) tucana.getSearchResults()[0]);
            } else if (tucana.getSize() > 1) {
                throw new ALDetailPrestationModelException(
                        "DetailPrestationModelServiceImpl#deleteForIdEntete : Plusieurs rubrique Tucana ont été trouvées pour ce détail de prestation : "
                                + detail.getId());
            }

            // FIXME (lot 2) à confirmer
            // if (ALCSDroit.TYPE_NAIS.equals(detail.getTypePrestation())
            // || ALCSDroit.TYPE_ACCE.equals(detail.getTypePrestation())) {
            // DroitComplexModel droit = ALImplServiceLocator
            // .getDroitComplexModelService()
            // .read(detail.getIdDroit());
            // EnfantModel enfant = droit.getEnfantComplexModel()
            // .getEnfantModel();
            // enfant.setAllocationNaissanceVersee(false);
            // ALImplServiceLocator.getEnfantModelService().update(enfant);
            // }
        }

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.DetailPrestationModelService #read(java.lang.String)
     */
    @Override
    public DetailPrestationModel read(String idDetailPrestationModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idDetailPrestationModel)) {
            throw new ALDetailPrestationModelException("Unable to read idDetailPrestationModel- the id passed is empty");

        }
        DetailPrestationModel detailPrestationModel = new DetailPrestationModel();
        detailPrestationModel.setId(idDetailPrestationModel);
        return (DetailPrestationModel) JadePersistenceManager.read(detailPrestationModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.DetailPrestationModelService #
     * search(ch.globaz.al.business.models.prestation.DetailPrestationSearchModel )
     */
    @Override
    public DetailPrestationSearchModel search(DetailPrestationSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALDetailPrestationModelException("DetailPrestationModelServiceImpl#search : searchModel is null");
        }

        return (DetailPrestationSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.DetailPrestationModelService
     * #update(ch.globaz.al.business.models.prestation.DetailPrestationModel)
     */
    @Override
    public DetailPrestationModel update(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException {
        if (detailPrestationModel == null) {
            throw new ALDetailPrestationModelException(
                    "Unable to update detailPrestationModel-the model passed is null");
        }

        // si l'entité n'existe pas encore
        if (detailPrestationModel.isNew()) {
            throw new ALDetailPrestationModelException("Unable to update detailPrestationModel-the entity is new");
        }
        // valide l'integrité
        DetailPrestationModelChecker.validate(detailPrestationModel);
        // fait la mise à jour en BD
        return (DetailPrestationModel) JadePersistenceManager.update(detailPrestationModel);
    }
}
