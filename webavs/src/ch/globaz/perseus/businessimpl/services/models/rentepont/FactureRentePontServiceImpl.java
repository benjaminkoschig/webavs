package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import ch.globaz.perseus.business.constantes.CSEtatFacture;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.exceptions.models.rentepont.FactureRentePontException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.FactureRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.SimpleFactureRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.rentepont.FactureRentePontService;
import ch.globaz.perseus.businessimpl.checkers.rentepont.FactureRentePontChecker;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author jsi
 * 
 */
public class FactureRentePontServiceImpl implements FactureRentePontService {

    @Override
    public int count(FactureRentePontSearchModel search) throws FactureRentePontException, JadePersistenceException {
        if (search == null) {
            throw new FactureRentePontException("Unable to count factureRentePonts, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public FactureRentePont create(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to create factureRentePont, the given model is null!");
        }
        try {
            FactureRentePontChecker.checkForCreate(factureRentePont);

            factureRentePont.getSimpleFactureRentePont().setIdQDRentePont(factureRentePont.getQdRentePont().getId());

            // BZ 7768 : Arrondir les montants saisis de l'écran de saisie des factures
            factureRentePont.getSimpleFactureRentePont().setMontant(
                    JANumberFormatter.round(
                            JadeStringUtil.change(factureRentePont.getSimpleFactureRentePont().getMontant(), "'", ""),
                            0.05, 2, JANumberFormatter.NEAR));
            factureRentePont.getSimpleFactureRentePont().setMontantRembourse(
                    JANumberFormatter.round(JadeStringUtil.change(factureRentePont.getSimpleFactureRentePont()
                            .getMontantRembourse(), "'", ""), 0.05, 2, JANumberFormatter.NEAR));
            factureRentePont.getSimpleFactureRentePont().setExcedantRevenuCompense(
                    JANumberFormatter.round(JadeStringUtil.change(factureRentePont.getSimpleFactureRentePont()
                            .getExcedantRevenuCompense(), "'", ""), 0.05, 2, JANumberFormatter.NEAR));
            // Mise à jour de la QD
            factureRentePont
                    .getQdRentePont()
                    .getSimpleQDRentePont()
                    .setExcedantRevenuCompense(
                            new Double(JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                                    .getSimpleQDRentePont().getExcedantRevenuCompense(), 0.0)
                                    + JadeStringUtil.parseDouble(factureRentePont.getSimpleFactureRentePont()
                                            .getExcedantRevenuCompense().replace("'", ""), 0.0)).toString());
            factureRentePont
                    .getQdRentePont()
                    .getSimpleQDRentePont()
                    .setMontantUtilise(
                            new Double(JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                                    .getSimpleQDRentePont().getMontantUtilise(), 0.0)
                                    + JadeStringUtil.parseDouble(factureRentePont.getSimpleFactureRentePont()
                                            .getMontantRembourse().replace("'", ""), 0.0)).toString());

            // Mise à 0 du dépassement de la qd car en cas de modification de facture, seuls les cas de forcer paiement
            // dont le montant utilisé est plus grand que la limite de la qd doivent être affiché.
            factureRentePont.getSimpleFactureRentePont().setMontantDepassant("0");
            // Si c'est une acceptation forcée, mettre le montant dans le dépassement
            if (factureRentePont.getSimpleFactureRentePont().getAcceptationForcee()) {
                Double montantUtilise = JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                        .getSimpleQDRentePont().getMontantUtilise(), 0.0);
                Double montantLimite = JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                        .getSimpleQDRentePont().getMontantLimite(), 0.0);
                if (montantUtilise > montantLimite) {
                    Double montantDepasse = montantUtilise - montantLimite;
                    factureRentePont.getSimpleFactureRentePont().setMontantDepassant(montantDepasse.toString());
                }
            }
            factureRentePont.setQdRentePont(PerseusServiceLocator.getQDRentePontService().update(
                    factureRentePont.getQdRentePont()));

            // Passer la facture en état enregistré
            factureRentePont.getSimpleFactureRentePont().setCsEtat(CSEtatFacture.ENREGISTRE.getCodeSystem());
            factureRentePont.setSimpleFactureRentePont(PerseusImplServiceLocator.getSimpleFactureRentePontService()
                    .create(factureRentePont.getSimpleFactureRentePont()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureRentePontException("Service not avaiable : " + e.toString(), e);
        } catch (Exception e) {
            throw new FactureRentePontException("Exception during facture creation : " + e.getMessage(), e);
        }
        return factureRentePont;
    }

    @Override
    public FactureRentePont delete(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to delete FactureRentePont, the given model is null!");
        }
        SimpleFactureRentePont simpleFactureRentePont = factureRentePont.getSimpleFactureRentePont();
        try {

            factureRentePont.getSimpleFactureRentePont().setIdQDRentePont(factureRentePont.getQdRentePont().getId());
            // Mise à jour de la QD
            factureRentePont
                    .getQdRentePont()
                    .getSimpleQDRentePont()
                    .setExcedantRevenuCompense(
                            new Double(JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                                    .getSimpleQDRentePont().getExcedantRevenuCompense(), 0.0)
                                    - JadeStringUtil.parseDouble(factureRentePont.getSimpleFactureRentePont()
                                            .getExcedantRevenuCompense().replace("'", ""), 0.0)).toString());
            factureRentePont
                    .getQdRentePont()
                    .getSimpleQDRentePont()
                    .setMontantUtilise(
                            new Double(JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                                    .getSimpleQDRentePont().getMontantUtilise(), 0.0)
                                    - JadeStringUtil.parseDouble(factureRentePont.getSimpleFactureRentePont()
                                            .getMontantRembourse().replace("'", ""), 0.0)).toString());

            factureRentePont.setQdRentePont(PerseusServiceLocator.getQDRentePontService().update(
                    factureRentePont.getQdRentePont()));
            simpleFactureRentePont = PerseusImplServiceLocator.getSimpleFactureRentePontService().delete(
                    simpleFactureRentePont);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureRentePontException("Service not avaiable : " + e.toString(), e);
        } catch (Exception e) {
            throw new FactureRentePontException("Exception during facture delete : " + e.getMessage(), e);
        }
        factureRentePont.setSimpleFactureRentePont(simpleFactureRentePont);

        return factureRentePont;
    }

    @Override
    public FactureRentePont read(String idFactureRentePont) throws JadePersistenceException, FactureRentePontException {
        if (idFactureRentePont == null) {
            throw new FactureRentePontException("Unable to read FactureRentePont, the given id is null!");
        }
        FactureRentePont FactureRentePont = new FactureRentePont();
        FactureRentePont.setId(idFactureRentePont);
        return (FactureRentePont) JadePersistenceManager.read(FactureRentePont);
    }

    @Override
    public FactureRentePont restituer(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to update FactureRentePont, the given model is null!");
        }
        try {
            if (!CSEtatFacture.VALIDE.getCodeSystem().equals(factureRentePont.getSimpleFactureRentePont().getCsEtat())) {
                JadeThread.logError(this.getClass().getName(), "perseus.facture.restitution.jamaisvalide");
            }

            // Si le lot n'a pas été comptabilisé, on ne peut pas demander la restitution
            Lot lot = PerseusServiceLocator.getLotService().getLotForFactureRP(factureRentePont.getId());

            if (JadeStringUtil.isEmpty(lot.getSimpleLot().getDateEnvoi())) {
                JadeThread.logError(this.getClass().getName(), "perseus.facture.restitution.lotouvert");
            }

            // Imputer le lot specifique
            lot = PerseusServiceLocator.getLotService().getLotCourant(CSTypeLot.LOT_FACTURES_RP);
            // Création de la prestation
            Prestation prestation = new Prestation();
            prestation.setLot(lot);
            prestation.getSimplePrestation().setDatePrestation(JadeDateUtil.getGlobazFormattedDate(new Date()));
            prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.DEFINITIF.getCodeSystem());
            prestation.getSimplePrestation().setMontantTotal(
                    "-" + factureRentePont.getSimpleFactureRentePont().getMontantRembourse());
            prestation.getSimplePrestation().setIdFacture(factureRentePont.getId());
            String anneeQd = factureRentePont.getQdRentePont().getSimpleQDRentePont().getAnnee();
            prestation.getSimplePrestation().setDateDebut("01.01." + anneeQd);
            prestation.getSimplePrestation().setDateFin("31.12." + anneeQd);
            prestation = PerseusServiceLocator.getPrestationService().create(prestation);

            // Mise à jour de la QD
            factureRentePont
                    .getQdRentePont()
                    .getSimpleQDRentePont()
                    .setExcedantRevenuCompense(
                            new Double(JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                                    .getSimpleQDRentePont().getExcedantRevenuCompense(), 0.0)
                                    - JadeStringUtil.parseDouble(factureRentePont.getSimpleFactureRentePont()
                                            .getExcedantRevenuCompense().replace("'", ""), 0.0)).toString());
            factureRentePont
                    .getQdRentePont()
                    .getSimpleQDRentePont()
                    .setMontantUtilise(
                            new Double(JadeStringUtil.parseDouble(factureRentePont.getQdRentePont()
                                    .getSimpleQDRentePont().getMontantUtilise(), 0.0)
                                    - JadeStringUtil.parseDouble(factureRentePont.getSimpleFactureRentePont()
                                            .getMontantRembourse().replace("'", ""), 0.0)).toString());
            factureRentePont.setQdRentePont(PerseusServiceLocator.getQDRentePontService().update(
                    factureRentePont.getQdRentePont()));

            // Passé la facture en état validé
            factureRentePont.getSimpleFactureRentePont().setCsEtat(CSEtatFacture.RESTITUE.getCodeSystem());
            factureRentePont = update(factureRentePont);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureRentePontException("Service not avaiable : " + e.toString(), e);
        } catch (Exception e) {
            throw new FactureRentePontException("Exception during facture update : " + e.getMessage(), e);
        }
        return factureRentePont;

    }

    @Override
    public FactureRentePontSearchModel search(FactureRentePontSearchModel searchModel) throws JadePersistenceException,
            FactureRentePontException {
        if (searchModel == null) {
            throw new FactureRentePontException("Unable to search FactureRentePont, the given model is null!");
        }
        return (FactureRentePontSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public FactureRentePont update(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to update FactureRentePont, the given model is null!");
        }
        try {
            factureRentePont.setSimpleFactureRentePont(PerseusImplServiceLocator.getSimpleFactureRentePontService()
                    .update(factureRentePont.getSimpleFactureRentePont()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureRentePontException("Service not avaiable : " + e.toString(), e);
        } catch (Exception e) {
            throw new FactureRentePontException("Exception during facture update : " + e.getMessage(), e);
        }
        return factureRentePont;
    }

    @Override
    public FactureRentePont validate(FactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to update FactureRentePont, the given model is null!");
        }
        try {
            // Imputer le lot specifique
            Lot lot = PerseusServiceLocator.getLotService().getLotCourant(CSTypeLot.LOT_FACTURES_RP);
            // Création de la prestation
            Prestation prestation = new Prestation();
            prestation.setLot(lot);
            prestation.getSimplePrestation().setDatePrestation(JadeDateUtil.getGlobazFormattedDate(new Date()));
            prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.DEFINITIF.getCodeSystem());
            prestation.getSimplePrestation().setMontantTotal(
                    factureRentePont.getSimpleFactureRentePont().getMontantRembourse());
            prestation.getSimplePrestation().setIdFacture(factureRentePont.getId());
            String anneeQd = factureRentePont.getQdRentePont().getSimpleQDRentePont().getAnnee();
            prestation.getSimplePrestation().setDateDebut("01.01." + anneeQd);
            prestation.getSimplePrestation().setDateFin("31.12." + anneeQd);
            prestation = PerseusServiceLocator.getPrestationService().create(prestation);
            // Créer l'ordre de versement
            OrdreVersement ordreVersement = new OrdreVersement();
            ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
            ordreVersement.getSimpleOrdreVersement().setNumFacture(
                    factureRentePont.getSimpleFactureRentePont().getNumRefFacture());
            ordreVersement.getSimpleOrdreVersement().setMontantVersement(
                    factureRentePont.getSimpleFactureRentePont().getMontantRembourse());
            ordreVersement.getSimpleOrdreVersement().setIdTiers(
                    factureRentePont.getSimpleFactureRentePont().getIdTiersAdressePaiement());
            ordreVersement.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                    factureRentePont.getSimpleFactureRentePont().getIdTiersAdressePaiement());
            ordreVersement.getSimpleOrdreVersement().setIdDomaineApplication(
                    factureRentePont.getSimpleFactureRentePont().getIdApplicationAdressePaiement());
            if (factureRentePont
                    .getSimpleFactureRentePont()
                    .getIdTiersAdressePaiement()
                    .equals(factureRentePont.getQdRentePont().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getTiers().getId())) {
                ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(CSTypeVersement.REQUERANT.getCodeSystem());
            } else {
                ordreVersement.getSimpleOrdreVersement()
                        .setCsTypeVersement(CSTypeVersement.AUTRE_TIERS.getCodeSystem());
            }

            ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);

            // Passé la facture en état validé
            factureRentePont.getSimpleFactureRentePont().setCsEtat(CSEtatFacture.VALIDE.getCodeSystem());

            factureRentePont.getSimpleFactureRentePont().setDateValidation(JACalendar.todayJJsMMsAAAA());

            factureRentePont = update(factureRentePont);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureRentePontException("Service not avaiable : " + e.toString(), e);
        } catch (Exception e) {
            throw new FactureRentePontException("Exception during facture update : " + e.getMessage(), e);
        }
        return factureRentePont;
    }

}
