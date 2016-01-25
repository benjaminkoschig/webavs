package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.facture.FactureException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.rentepont.FactureRentePontException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationDecision;
import ch.globaz.perseus.business.models.lot.PrestationFacture;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersementSearch;
import ch.globaz.perseus.business.models.lot.SimplePrestation;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.lot.PrestationService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PrestationServiceImpl extends PerseusAbstractServiceImpl implements PrestationService {

    @Override
    public int count(PrestationSearchModel search) throws LotException, JadePersistenceException {
        if (search == null) {
            throw new LotException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Prestation create(Prestation prestation) throws JadePersistenceException, LotException {
        if (prestation == null) {
            throw new LotException("Unable to create prestation, the given model is null !");
        }
        try {
            SimplePrestation simplePrestation = prestation.getSimplePrestation();
            simplePrestation.setIdLot(prestation.getLot().getId());
            simplePrestation = PerseusImplServiceLocator.getSimplePrestationService().create(simplePrestation);
            prestation.setSimplePrestation(simplePrestation);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available -" + e.getMessage());
        }
        return prestation;
    }

    @Override
    public Prestation delete(Prestation prestation) throws JadePersistenceException, LotException {
        if (prestation == null) {
            throw new LotException("Unable to delete Prestation, the given model is null !");
        }
        try {
            // On ne permet pas de supprimer une prestation comptabilise à moins qu'elle soit à 0
            if (CSEtatPrestation.COMPTABILISE.getCodeSystem().equals(
                    prestation.getSimplePrestation().getEtatPrestation())
                    & !JadeNumericUtil.isEmptyOrZero(prestation.getSimplePrestation().getMontantTotal())) {
                JadeThread.logError(this.getClass().getName(), "perseus.lot.prestation.comptabilise.delete");
            }
            SimpleOrdreVersementSearch search = new SimpleOrdreVersementSearch();
            search.setForIdPrestation(prestation.getId());
            PerseusImplServiceLocator.getSimpleOrdreVersementService().delete(search);
            // Si c'est une facture, supprimer la facture
            if (!JadeNumericUtil.isEmptyOrZero(prestation.getSimplePrestation().getIdFacture())) {
                if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(prestation.getLot().getSimpleLot().getTypeLot())) {
                    Facture facture = PerseusServiceLocator.getFactureService().read(
                            prestation.getSimplePrestation().getIdFacture());
                    facture = PerseusServiceLocator.getFactureService().delete(facture);
                } else {
                    // Si rente pont
                    FactureRentePont facture = PerseusServiceLocator.getFactureRentePontService().read(
                            prestation.getSimplePrestation().getIdFacture());
                    facture = PerseusServiceLocator.getFactureRentePontService().delete(facture);

                }
            }

            prestation.setSimplePrestation(PerseusImplServiceLocator.getSimplePrestationService().delete(
                    prestation.getSimplePrestation()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available - " + e.toString());
        } catch (FactureException e) {
            throw new LotException("Facture exception - " + e.toString());
        } catch (FactureRentePontException e) {
            throw new LotException("FactureRentePont exception - " + e.toString());
        }
        return prestation;
    }

    @Override
    public Prestation read(String idPrestation) throws JadePersistenceException, LotException {
        if (JadeStringUtil.isEmpty(idPrestation)) {
            throw new LotException("Unable to read Prestation, the id passed is null !");
        }
        Prestation prestation = new Prestation();
        prestation.setId(idPrestation);
        return (Prestation) JadePersistenceManager.read(prestation);
    }

    @Override
    public JadeAbstractSearchModel search(JadeAbstractSearchModel searchModel) throws JadePersistenceException,
            LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search Prestation, the search model passed is null !");
        }
        return JadePersistenceManager.search(searchModel);
    }

    @Override
    public PrestationSearchModel search(PrestationSearchModel searchModel) throws JadePersistenceException,
            LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search Prestation, the search model passed is null !");
        }
        // Dans le cas où on cherche des prestations pour un type de lot précis, optimisation de la recherche
        if (searchModel.getInTypeLot().size() == 1) {
            if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(searchModel.getInTypeLot().get(0))) {
                searchModel.setModelClass(PrestationDecision.class);
            }
            if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(searchModel.getInTypeLot().get(0))) {
                searchModel.setModelClass(PrestationFacture.class);
            }
        }
        return (PrestationSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public Prestation update(Prestation prestation) throws JadePersistenceException, LotException {
        if (prestation == null) {
            throw new LotException("Unable to update prestation, the given model is null!");
        }

        try { // Mise à jour de la prestation
            SimplePrestation simplePrestation = prestation.getSimplePrestation();
            simplePrestation.setIdLot(prestation.getSimplePrestation().getIdLot());
            prestation.setSimplePrestation(PerseusImplServiceLocator.getSimplePrestationService().update(
                    simplePrestation));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available - " + e.getMessage());
        }
        return prestation;
    }

    @Override
    public List<Prestation> getPrestationsAsList(Lot lot, boolean isAgenceGestionnaire, List<String> listeDAgence)
            throws JadePersistenceException {

        List<Prestation> listePrestation = new ArrayList<Prestation>();

        if (!(listeDAgence.isEmpty() && isAgenceGestionnaire)) {
            PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
            prestationSearchModel.setForIdLot(lot.getId());
            prestationSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_FACTURES.getCodeSystem());

            if (isAgenceGestionnaire) {
                prestationSearchModel.setForIdGestionnaireFactureIn(listeDAgence);
            } else {
                prestationSearchModel.setForIdGestionnaireFactureNotIn(listeDAgence);
            }

            if (prestationSearchModel.getInTypeLot().size() == 1) {
                if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(prestationSearchModel.getInTypeLot().get(0))) {
                    prestationSearchModel.setModelClass(PrestationDecision.class);
                }
                if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(prestationSearchModel.getInTypeLot().get(0))) {
                    prestationSearchModel.setModelClass(PrestationFacture.class);
                }
            }

            prestationSearchModel = (PrestationSearchModel) JadePersistenceManager.search(prestationSearchModel);

            for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
                listePrestation.add((Prestation) model);
            }
        }
        return listePrestation;
    }
}