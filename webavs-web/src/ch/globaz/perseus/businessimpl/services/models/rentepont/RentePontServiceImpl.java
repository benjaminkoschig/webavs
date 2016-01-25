package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.rentepont.QDRentePontException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePont;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.QDRentePont;
import ch.globaz.perseus.business.models.rentepont.QDRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.SimpleRentePont;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Requerant;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.rentepont.RentePontService;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.businessimpl.services.models.decision.DecisionServiceImpl;

public class RentePontServiceImpl implements RentePontService {

    @Override
    public Float calculerRetro(RentePont rentePont) throws JadePersistenceException, RentePontException {
        // String dateDebut = rentePont.getSimpleRentePont().getDateDebut();
        // String dateFin = null;
        // try {
        // dateFin = "01." + PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
        // } catch (PaiementException e) {
        // throw new RentePontException("Paiement exception during calculerRetro : " + e.toString(), e);
        // } catch (JadeApplicationServiceNotAvailableException e) {
        // throw new RentePontException("Service not available during calculerRetro : " + e.toString(), e);
        // }
        //
        // int nbMois = JadeDateUtil.getNbMonthsBetween(dateDebut, dateFin);
        //
        // Float montantDu = nbMois * Float.parseFloat(rentePont.getSimpleRentePont().getMontant());

        Float montantRetro = new Float(0);
        if (!JadeStringUtil.isEmpty(rentePont.getSimpleRentePont().getMontantRetroactif())) {
            montantRetro = Float.parseFloat(rentePont.getSimpleRentePont().getMontantRetroactif());
        }

        return montantRetro;
    }

    @Override
    public int count(RentePontSearchModel search) throws RentePontException, JadePersistenceException {
        if (search == null) {
            throw new RentePontException("Unable to count rentePonts, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public RentePont create(RentePont rentePont) throws JadePersistenceException, RentePontException {
        if (rentePont == null) {
            throw new RentePontException("Unable to create rentePont, the given model is null!");
        }

        try {
            // Création du requerant
            Requerant requerant = new Requerant();
            requerant
                    .getMembreFamille()
                    .getSimpleMembreFamille()
                    .setIdTiers(
                            rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne()
                                    .getIdTiers());
            requerant = PerseusServiceLocator.getRequerantService().create(requerant);

            // Création de la situation familiale
            SituationFamiliale situationFamiliale = new SituationFamiliale();
            situationFamiliale.setRequerant(requerant);
            situationFamiliale = PerseusServiceLocator.getSituationFamilialeService().create(situationFamiliale);
            rentePont.setSituationFamiliale(situationFamiliale);

            // Création de la rentePont
            SimpleRentePont simpleRentePont = rentePont.getSimpleRentePont();
            simpleRentePont.setIdDossier(rentePont.getDossier().getId());
            simpleRentePont.setIdSituationFamiliale(situationFamiliale.getId());
            simpleRentePont = PerseusImplServiceLocator.getSimpleRentePontService().create(simpleRentePont);
            rentePont.setSimpleRentePont(simpleRentePont);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available - " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new RentePontException("SituationFamilleException during creating : " + e.getMessage(), e);
        }

        return rentePont;
    }

    private void createQDRentePont(RentePont rentePont) throws RentePontException {
        try {
            VariableMetier QD_MONTANT_LIMITE_CELIBATAIRE = PerseusServiceLocator.getVariableMetierService().getFromCS(
                    CSVariableMetier.QD_RENTEPONT_MONTANT_LIMITE_CELIBATAIRE.getCodeSystem(), new Date());
            VariableMetier QD_MONTANT_LIMITE_COUPLE = PerseusServiceLocator.getVariableMetierService().getFromCS(
                    CSVariableMetier.QD_RENTEPONT_MONTANT_LIMITE_COUPLE.getCodeSystem(), new Date());
            if ((QD_MONTANT_LIMITE_CELIBATAIRE == null) || (QD_MONTANT_LIMITE_COUPLE == null)) {
                throw new RentePontException("Var métiers for QD RentePont undefined !");
            }
            QDRentePontSearchModel searchModel = new QDRentePontSearchModel();
            searchModel.setForIdDossier(rentePont.getSimpleRentePont().getIdDossier());
            searchModel.setForAnnee(rentePont.getSimpleRentePont().getDateDebut().substring(6));

            searchModel = PerseusServiceLocator.getQDRentePontService().search(searchModel);
            // On regarde si on a déjà une QD RP pour ce dossier.
            if (searchModel.getSize() > 0) {
                QDRentePont qdRentePont = (QDRentePont) searchModel.getSearchResults()[0];
                // On augmente la limite que si on passe d'un cas de célibataire à un cas de couple
                if (!JadeStringUtil.isBlank(rentePont.getSituationFamiliale().getConjoint().getId())) {
                    qdRentePont.getSimpleQDRentePont().setMontantLimite(
                            QD_MONTANT_LIMITE_COUPLE.getMontant().toString());
                }
                // Mais on met l'excedant de revenu a jour dans tous les cas
                qdRentePont.getSimpleQDRentePont()
                        .setExcedantRevenu(rentePont.getSimpleRentePont().getExcedantRevenu());
                PerseusServiceLocator.getQDRentePontService().update(qdRentePont);
            } else {
                QDRentePont qdRentePont = new QDRentePont();
                qdRentePont.getSimpleQDRentePont().setAnnee(rentePont.getSimpleRentePont().getDateDebut().substring(6));
                qdRentePont.getSimpleQDRentePont().setIdDossier(rentePont.getDossier().getId());
                qdRentePont.getSimpleQDRentePont()
                        .setExcedantRevenu(rentePont.getSimpleRentePont().getExcedantRevenu());
                if (!JadeStringUtil.isBlank(rentePont.getSituationFamiliale().getConjoint().getId())) {
                    qdRentePont.getSimpleQDRentePont().setMontantLimite(
                            QD_MONTANT_LIMITE_COUPLE.getMontant().toString());
                } else {
                    qdRentePont.getSimpleQDRentePont().setMontantLimite(
                            QD_MONTANT_LIMITE_CELIBATAIRE.getMontant().toString());
                }
                PerseusServiceLocator.getQDRentePontService().create(qdRentePont);
            }
        } catch (QDRentePontException e) {
            throw new RentePontException("SituationFamilleException during creating : " + e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new RentePontException("SituationFamilleException during creating : " + e.getMessage(), e);
        } catch (VariableMetierException e) {
            throw new RentePontException("SituationFamilleException during creating : " + e.getMessage(), e);
        }

    }

    @Override
    public RentePont delete(RentePont rentePont) throws JadePersistenceException, RentePontException {
        SimpleRentePont simpleRentePont = rentePont.getSimpleRentePont();
        try {
            // Suppression de la simple rentePont
            simpleRentePont = PerseusImplServiceLocator.getSimpleRentePontService().delete(simpleRentePont);
            rentePont.setSimpleRentePont(simpleRentePont);

            // Si la rentePont a été supprimée
            if (simpleRentePont.isNew()) {
                // Suppression de la situation familiale
                rentePont.setSituationFamiliale(PerseusServiceLocator.getSituationFamilialeService().delete(
                        rentePont.getSituationFamiliale()));
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available : " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new RentePontException("SituationFamilleException during rentePont deleting : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Enfant> getListEnfants(RentePont rentePont) throws JadePersistenceException, RentePontException {
        try {
            ArrayList<Enfant> listEnfants = new ArrayList<Enfant>();
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(rentePont.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);

            for (JadeAbstractModel abstractModel : enfantFamilleSearchModel.getSearchResults()) {
                EnfantFamille enfantfamille = (EnfantFamille) abstractModel;
                listEnfants.add(enfantfamille.getEnfant());
            }

            return listEnfants;

        } catch (SituationFamilleException e) {
            throw new RentePontException("SituationFamilleException during getListEnfants : " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not available : " + e.toString(), e);
        }
    }

    @Override
    public boolean hasDossierRentePontValidee(String idDossier) throws JadePersistenceException, RentePontException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new RentePontException("Unable to read a dossier, the id passed is null!");
        }
        RentePontSearchModel searchModel = new RentePontSearchModel();
        searchModel.setForIdDossier(idDossier);
        searchModel.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
        try {
            searchModel = PerseusServiceLocator.getRentePontService().search(searchModel);
            if (searchModel.getSize() > 0) {
                return true;
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not avaiable : " + e.toString(), e);
        }
        return false;
    }

    private void putRentePontDansLot(RentePont rentePont) throws Exception {
        // Récupération du lot de rétro
        Lot lot = PerseusServiceLocator.getLotService().getLotCourant(CSTypeLot.LOT_DECISION_RP);

        // Calcul du rétro
        // BZ 6449
        BigDecimal montantRetro = new BigDecimal(JadeStringUtil.change(rentePont.getSimpleRentePont()
                .getMontantRetroactif(), "'", ""));

        // Création de la prestation (négative ou positive)
        Prestation prestation = new Prestation();
        prestation.setLot(lot);
        prestation.getSimplePrestation().setIdDecisionPcf(rentePont.getId());
        prestation.getSimplePrestation().setDatePrestation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.DEFINITIF.getCodeSystem());
        prestation.getSimplePrestation().setMontantTotal(montantRetro.toString());
        prestation.getSimplePrestation().setDateDebut(rentePont.getSimpleRentePont().getDateDebut());
        String dateFinPres;
        // Si le mois courant est le mois du paiement
        if (JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3)
                .equals(PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt())) {
            // La date de fin de la prestation est le dernier jour du mois précédant
            dateFinPres = JadeDateUtil
                    .addDays("01." + JadeDateUtil.getGlobazFormattedDate(new Date()).substring(3), -1);
        } else {
            // Si non la date de fin est le dernier jour du mois précédant le prochain paiement
            // mensuel
            dateFinPres = JadeDateUtil.addDays("01."
                    + PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt(), -1);
        }

        prestation.getSimplePrestation().setDateFin(dateFinPres);
        prestation = PerseusServiceLocator.getPrestationService().create(prestation);

        if (montantRetro.intValue() > 0) {
            // Créer les ordres de versements au créanciers + bénéficiaire
            CreancierRentePontSearchModel creancierSearch = new CreancierRentePontSearchModel();
            creancierSearch.setForIdRentePont(rentePont.getId());
            creancierSearch = PerseusServiceLocator.getCreancierRentePontService().search(creancierSearch);
            // BZ 6449
            BigDecimal sommeCreancesAccordees = new BigDecimal("0.00");
            for (JadeAbstractModel model : creancierSearch.getSearchResults()) {
                CreancierRentePont creancier = (CreancierRentePont) model;
                OrdreVersement ordreVersement = new OrdreVersement();
                ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
                ordreVersement.getSimpleOrdreVersement().setNumFacture(
                        creancier.getSimpleCreancierRentePont().getReferencePaiement());
                ordreVersement.getSimpleOrdreVersement().setMontantVersement(
                        creancier.getSimpleCreancierRentePont().getMontantAccorde());
                ordreVersement.getSimpleOrdreVersement().setIdTiers(
                        creancier.getSimpleCreancierRentePont().getIdTiers());
                ordreVersement.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                        creancier.getSimpleCreancierRentePont().getIdTiers());
                ordreVersement.getSimpleOrdreVersement().setIdDomaineApplication(
                        creancier.getSimpleCreancierRentePont().getIdDomaineApplicatif());
                if (CSTypeCreance.TYPE_CREANCE_ASSURANCER_SOCIALE.getCodeSystem().equals(
                        creancier.getSimpleCreancierRentePont().getCsTypeCreance())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.ASSURANCE_SOCIALE.getCodeSystem());
                } else if (CSTypeCreance.TYPE_CREANCE_TIERS.getCodeSystem().equals(
                        creancier.getSimpleCreancierRentePont().getCsTypeCreance())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.AUTRE_TIERS.getCodeSystem());
                } else if (CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(
                        creancier.getSimpleCreancierRentePont().getCsTypeCreance())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem());
                }

                ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);
                // BZ 6449
                BigDecimal sommeCreance = new BigDecimal(creancier.getSimpleCreancierRentePont().getMontantAccorde());
                sommeCreancesAccordees = sommeCreancesAccordees.add(sommeCreance);
            }
            // Contrôler la répartition des créanciers qu'elle ne soit pas plus grande que le rétro
            if (sommeCreancesAccordees.intValue() > montantRetro.intValue()) {
                JadeThread.logError(DecisionServiceImpl.class.getName(),
                        "perseus.decisions.validation.creancierArepartir");
            }
            // Verser le solde au requérant
            if (montantRetro.intValue() - sommeCreancesAccordees.intValue() > 0) {
                // BZ 6449
                BigDecimal versementBeneficiaire = new BigDecimal(montantRetro.toString());
                versementBeneficiaire = versementBeneficiaire.subtract(sommeCreancesAccordees);
                OrdreVersement ordreVersement = new OrdreVersement();
                ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
                ordreVersement.getSimpleOrdreVersement().setMontantVersement(versementBeneficiaire.toString());
                ordreVersement.getSimpleOrdreVersement().setIdTiers(
                        rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                                .getIdTiers());
                ordreVersement.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                        rentePont.getSimpleRentePont().getIdTiersAdressePaiement());
                ordreVersement.getSimpleOrdreVersement().setIdDomaineApplication(
                        rentePont.getSimpleRentePont().getIdDomaineApplicatifAdressePaiement());
                ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(CSTypeVersement.REQUERANT.getCodeSystem());

                ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);
            }
        }
    }

    @Override
    public RentePont read(String idRentePont) throws JadePersistenceException, RentePontException {
        if (JadeStringUtil.isEmpty(idRentePont)) {
            throw new RentePontException("Unable to read a rentePont, the id passed is null!");
        }
        RentePont rentePont = new RentePont();
        rentePont.setId(idRentePont);
        return (RentePont) JadePersistenceManager.read(rentePont);
    }

    @Override
    public RentePontSearchModel search(RentePontSearchModel searchModel) throws JadePersistenceException,
            RentePontException {
        if (searchModel == null) {
            throw new RentePontException("Unable to search rentePonts, the search model passed is null!");
        }
        return (RentePontSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public RentePont update(RentePont rentePont) throws JadePersistenceException, RentePontException {
        if ((rentePont == null) || rentePont.isNew()) {
            throw new RentePontException("Unable to update rentePont, the model passed is null or new!");
        }
        try {
            rentePont.setSimpleRentePont(PerseusImplServiceLocator.getSimpleRentePontService().update(
                    rentePont.getSimpleRentePont()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RentePontException("Service not avaiable : " + e.toString(), e);
        }
        return rentePont;
    }

    @Override
    public RentePont validate(RentePont rentePont) throws JadePersistenceException, RentePontException {
        if ((rentePont == null) || rentePont.isNew()) {
            throw new RentePontException("Unable to update rentePont, the model passed is null or new!");
        }
        createQDRentePont(rentePont);
        try {
            if (!CSEtatRentePont.VALIDE.getCodeSystem().equals(rentePont.getSimpleRentePont().getCsEtat())) {
                // Si la rentepont a déjà été validée, on ne la remet pas dans le lot
                putRentePontDansLot(rentePont);
            }
            rentePont.getSimpleRentePont().setCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
            rentePont.getSimpleRentePont().setDateDecision(
                    PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt());

            rentePont = update(rentePont);
        } catch (Exception e) {
            throw new RentePontException("Exception during Decision validation : " + e.toString(), e);
        }
        return rentePont;
    }

}
