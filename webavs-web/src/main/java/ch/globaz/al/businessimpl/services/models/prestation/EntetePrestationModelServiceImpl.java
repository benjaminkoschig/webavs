package ch.globaz.al.businessimpl.services.models.prestation;

import ch.globaz.al.business.exceptions.model.prestation.ALDetailPrestationModelException;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
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
import globaz.jade.persistence.model.JadeAbstractModel;

import java.util.ArrayList;
import java.util.List;

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
        // sera effacée juste aprčs)
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

        // suppression de l'en-tęte
        return (EntetePrestationModel) JadePersistenceManager.delete(entetePrestModel);
    }

    private void deleteRAFAMForIdEntete(String idEntete) throws JadePersistenceException, JadeApplicationException {

        if (!JadeNumericUtil.isIntegerPositif(idEntete)) {
            throw new ALDetailPrestationModelException(
                    "DetailPrestationModelServiceImpl#deleteForIdEntete : idEntete is null or zero");
        }

        DetailPrestationSearchModel search = new DetailPrestationSearchModel();
        search.setForIdEntetePrestation(idEntete);
        search = (DetailPrestationSearchModel) JadePersistenceManager.search(search);
        List<String> listIdDroit = new ArrayList<>();
        for (JadeAbstractModel abstractEnteteModel : search.getSearchResults()) {
            DetailPrestationModel detail = (DetailPrestationModel) abstractEnteteModel;
            if(!listIdDroit.contains(detail.getIdDroit())) {
                listIdDroit.add(detail.getIdDroit());
            }
        }

        for (String idDroit : listIdDroit) {
            AnnonceRafamSearchModel rafamModel = ALImplServiceLocator.getAnnoncesRafamSearchService()
                    .loadAnnoncesToSendForDroit(idDroit);
            for(JadeAbstractModel model : rafamModel.getSearchResults()) {
                AnnonceRafamModel annonce = (AnnonceRafamModel) model;
                if(hasPrestationForAnnonce(search, annonce)) {
                    ALImplServiceLocator.getAnnonceRafamBusinessService().deleteNotSentForRecordNumber(annonce.getRecordNumber());
                }
            }
        }
    }

    private boolean hasPrestationForAnnonce(DetailPrestationSearchModel search, AnnonceRafamModel annonce) {
        for (JadeAbstractModel abstractEnteteModel : search.getSearchResults()) {
            DetailPrestationModel detail = (DetailPrestationModel) abstractEnteteModel;
            String dateToTest = new Date(detail.getPeriodeValidite()).getSwissValue();
            if (annonce.getIdDroit().equals(detail.getIdDroit())
                && new Periode(annonce.getDebutDroit(), annonce.getEcheanceDroit()).isDateDansLaPeriode(dateToTest)) {
                return true;
            }
        }
        return false;
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

        // On efface les entętes puis les détails sans appeler le service delete
        // car on ne veut pas supprimer
        // la récap dans ce service
        EntetePrestationSearchModel searchEntete = new EntetePrestationSearchModel();
        searchEntete.setForIdRecap(idRecap);
        searchEntete = (EntetePrestationSearchModel) JadePersistenceManager.search(searchEntete);

        DetailPrestationSearchModel searchDetail = new DetailPrestationSearchModel();
        // pour chaque entęte, on supprime ses détails de prestations
        for (int i = 0; i < searchEntete.getSize(); i++) {
            searchDetail.setForIdEntetePrestation(searchEntete.getSearchResults()[i].getId());
            JadePersistenceManager.delete(searchDetail);
        }

        // une fois qu'on a supprimé tout les détails, on peut supprimer
        // effectivement l'entęte
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
        // contrôle l'intégrité du modčle
        EntetePrestationModelChecker.validate(enTetePrestModel);

        // ajoute la mise ŕ jour dans la persistence et le retourne
        return (EntetePrestationModel) JadePersistenceManager.update(enTetePrestModel);
    }

    @Override
    public List<EntetePrestationModel> searchEntetesPrestationsComptabilisees(String idDossier, String periode) throws JadeApplicationException, JadePersistenceException {
        List<EntetePrestationModel> entetesPrestations = new ArrayList<>();
        EntetePrestationSearchModel searchPrest = new EntetePrestationSearchModel();
        searchPrest.setForIdDossier(idDossier);
        searchPrest.setForPeriode(periode);
        searchPrest.setForEtat(ALCSPrestation.ETAT_CO);
        searchPrest.setWhereKey("prestationExistanteSelonEtat");
        searchPrest = ALServiceLocator.getEntetePrestationModelService().search(searchPrest);
        for (int j = 0; j < searchPrest.getSize(); j++) {
            entetesPrestations.add((EntetePrestationModel) searchPrest.getSearchResults()[j]);
        }
        return entetesPrestations;
    }
}
