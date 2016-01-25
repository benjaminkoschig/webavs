package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationRP;
import ch.globaz.perseus.business.models.lot.PrestationRentePontSearchModel;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.lot.SimpleLot;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.lot.LotService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class LotServiceImpl extends PerseusAbstractServiceImpl implements LotService {

    @Override
    public int count(LotSearchModel search) throws LotException, JadePersistenceException {
        if (search == null) {
            throw new LotException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Lot create(Lot lot) throws JadePersistenceException, LotException {
        if (lot == null) {
            throw new LotException("Unable to create lot, the given model is null !");
        }

        try {
            SimpleLot simpleLot = lot.getSimpleLot();
            simpleLot = PerseusImplServiceLocator.getSimpleLotService().create(simpleLot);
            lot.setSimpleLot(simpleLot);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available -" + e.getMessage());
        }
        return lot;
    }

    @Override
    public Lot delete(Lot lot) throws JadePersistenceException, LotException {
        if (lot == null) {
            throw new LotException("Unable to delete Lot, the given model is null !");
        }

        try {
            lot.setSimpleLot(PerseusImplServiceLocator.getSimpleLotService().delete(lot.getSimpleLot()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available - " + e.getMessage());
        }
        return lot;
    }

    @Override
    public Lot getLotCourant(CSTypeLot typeLot) throws JadePersistenceException, LotException {
        Lot lot = null;
        try {

            String moisCourant = "";
            // BZ 7941 Utilisation de la date du prochain pmt mensuel en fonction du type de lot
            if (CSTypeLot.LOT_DECISION.getCodeSystem().equals(typeLot.getCodeSystem())
                    || CSTypeLot.LOT_FACTURES.getCodeSystem().equals(typeLot.getCodeSystem())) {
                moisCourant = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            } else {
                moisCourant = PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt();
            }
            LotSearchModel lotSearchModel = new LotSearchModel();
            lotSearchModel.setForTypeLot(typeLot.getCodeSystem());
            lotSearchModel.setForMoisValable(moisCourant);
            lotSearchModel.setForEtatCs(CSEtatLot.LOT_OUVERT.getCodeSystem());
            lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
            // Si il y'a plus d'un lot du même type d'ouvert il y'a un problème
            if (lotSearchModel.getSize() > 1) {
                JadeThread.logError(LotServiceImpl.class.getName(), "perseus.lot.type.ouvert.multiple");
            }
            // Si il n'y a pas de lot en créer un (mais pas pour les lots mensuels)
            if ((lotSearchModel.getSize() == 0)
                    && !CSTypeLot.LOT_MENSUEL.getCodeSystem().equals(typeLot.getCodeSystem())) {
                lot = new Lot();
                if (moisCourant.equals(JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3))) {
                    lot.getSimpleLot().setDateCreation(JadeDateUtil.getGlobazFormattedDate(new Date()));
                } else {
                    lot.getSimpleLot().setDateCreation("01." + moisCourant);
                }
                // Définition de la description
                String params[] = new String[2];
                params[0] = moisCourant;
                LotSearchModel sm = new LotSearchModel();
                sm.setForTypeLot(typeLot.getCodeSystem());
                sm.setForMoisValable(moisCourant);
                params[1] = Integer.toString(PerseusServiceLocator.getLotService().count(sm) + 1);
                lot.getSimpleLot().setDescription(typeLot.getDescription(params));
                lot.getSimpleLot().setTypeLot(typeLot.getCodeSystem());
                lot.getSimpleLot().setEtatCs(CSEtatLot.LOT_OUVERT.getCodeSystem());
                lot = PerseusServiceLocator.getLotService().create(lot);
            }
            // Si non retourner le lot déjà ouvert
            if (lotSearchModel.getSize() == 1) {
                lot = (Lot) lotSearchModel.getSearchResults()[0];
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available exception during getLotCourant : " + e.toString(), e);
        } catch (PaiementException e) {
            throw new LotException("PaiementException during getLotCourant : " + e.toString(), e);
        }

        return lot;
    }

    @Override
    public Lot getLotForDemande(String demandeId) throws JadePersistenceException, LotException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(demandeId)) {
            throw new LotException("Unable to find Lot, the demande id passed is empty !");
        }
        PrestationSearchModel searchModel = new PrestationSearchModel();
        searchModel.setForIdDemande(demandeId);
        searchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
        searchModel = PerseusServiceLocator.getPrestationService().search(searchModel);
        if (searchModel.getSize() > 0) {
            Prestation prestation = (Prestation) searchModel.getSearchResults()[0];
            return prestation.getLot();
        } else {
            return null;
        }
    }

    @Override
    public Lot getLotForFacture(String factureId) throws JadePersistenceException, LotException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(factureId)) {
            throw new LotException("Unable to find Lot, the facture id passed is empty !");
        }
        PrestationSearchModel searchModel = new PrestationSearchModel();
        searchModel.setForIdFacture(factureId);
        searchModel.getInTypeLot().add(CSTypeLot.LOT_FACTURES.getCodeSystem());
        searchModel = PerseusServiceLocator.getPrestationService().search(searchModel);
        if (searchModel.getSize() > 0) {
            Prestation prestation = (Prestation) searchModel.getSearchResults()[0];
            return prestation.getLot();
        } else {
            return null;
        }
    }

    @Override
    public Lot getLotForFactureRP(String factureId) throws JadePersistenceException, LotException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(factureId)) {
            throw new LotException("Unable to find Lot, the facture id passed is empty !");
        }
        PrestationRentePontSearchModel searchModel = new PrestationRentePontSearchModel();
        searchModel.setForIdFacture(factureId);
        searchModel.getInTypeLot().add(CSTypeLot.LOT_FACTURES_RP.getCodeSystem());
        searchModel = PerseusServiceLocator.getPrestationRentePontService().search(searchModel);
        if (searchModel.getSize() > 0) {
            PrestationRP prestationRP = (PrestationRP) searchModel.getSearchResults()[0];
            return prestationRP.getLot();
        } else {
            return null;
        }
    }

    @Override
    public Lot read(String idLot) throws JadePersistenceException, LotException {
        if (JadeStringUtil.isEmpty(idLot)) {
            throw new LotException("Unable to read Lot, the id passed is null !");
        }
        Lot lot = new Lot();
        lot.setId(idLot);
        return (Lot) JadePersistenceManager.read(lot);
    }

    @Override
    public LotSearchModel search(LotSearchModel searchModel) throws JadePersistenceException, LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search Lot, the search model passed is null !");
        }
        return (LotSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public Lot update(Lot lot) throws JadePersistenceException, LotException {
        if (lot == null) {
            throw new LotException("Unable to update Lot, the given model is null!");
        }

        try {
            // Mise à jour du lien localite
            SimpleLot simpleLot = lot.getSimpleLot();
            simpleLot.setIdJournal(lot.getSimpleLot().getIdJournal());
            lot.setSimpleLot(PerseusImplServiceLocator.getSimpleLotService().update(simpleLot));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available - " + e.getMessage());
        }

        return lot;
    }

}
