package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.process.facture.FactureWrapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.constantes.CSEtatFacture;
import ch.globaz.perseus.business.constantes.CSEtatPrestation;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.exceptions.models.facture.FactureException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.qd.FactureSearchModel;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.qd.FactureService;
import ch.globaz.perseus.businessimpl.checkers.qd.FactureChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * Implémentation des services disponibles pour les factures.
 * 
 * @author JSI
 * 
 */
public class FactureServiceImpl extends PerseusAbstractServiceImpl implements FactureService {

    @Override
    public int count(FactureSearchModel search) throws FactureException, JadePersistenceException {
        if (search == null) {
            throw new FactureException("Unable to count facture, FactureSearchModel given is null.");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Facture create(Facture facture) throws JadePersistenceException, FactureException {
        return this.create(facture, false);
    }

    @Override
    public Facture create(Facture facture, boolean forcerAcceptation) throws JadePersistenceException, FactureException {
        if (facture == null) {
            throw new FactureException("Unable to create facture, Facture given is null.");
        }
        try {
            // Préparation de la facture avec les QD complétements chargés
            facture.setQd(PerseusServiceLocator.getQDService().read(facture.getQd().getId()));
            // Préparation des montants pour les calaculs
            // BZ 7768 : Arrondir les montants saisis de l'écran de saisie des factures
            facture.getSimpleFacture().setMontantRembourse(
                    JANumberFormatter.round(
                            JadeStringUtil.change(facture.getSimpleFacture().getMontantRembourse(), "'", ""), 0.05, 2,
                            JANumberFormatter.NEAR));
            facture.getSimpleFacture().setExcedantRevenuCompense(
                    JANumberFormatter.round(
                            JadeStringUtil.change(facture.getSimpleFacture().getExcedantRevenuCompense(), "'", ""),
                            0.05, 2, JANumberFormatter.NEAR));
            facture.getSimpleFacture().setMontant(
                    JANumberFormatter.round(JadeStringUtil.change(facture.getSimpleFacture().getMontant(), "'", ""),
                            0.05, 2, JANumberFormatter.NEAR));

            // Mise à jour de l'excédant de revenu compensé
            if (Float.parseFloat(facture.getSimpleFacture().getExcedantRevenuCompense()) > 0) {
                QDAnnuelle qdAnnuelle = facture.getQd().getQdAnnuelle();
                Float excedantDejaCompense = new Float(0);
                if (!JadeStringUtil.isEmpty(qdAnnuelle.getSimpleQDAnnuelle().getExcedantRevenuCompense())) {
                    excedantDejaCompense += Float.parseFloat(qdAnnuelle.getSimpleQDAnnuelle()
                            .getExcedantRevenuCompense());
                }
                Float nouveauExcedantRevenuCompense = excedantDejaCompense
                        + Float.parseFloat(facture.getSimpleFacture().getExcedantRevenuCompense());
                qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenuCompense(nouveauExcedantRevenuCompense.toString());
                // On update la QD Annuelle
                facture.getQd().setQdAnnuelle(PerseusServiceLocator.getQDAnnuelleService().update(qdAnnuelle));
            }
            // Création de la facture
            SimpleFacture simpleFacture = facture.getSimpleFacture();
            simpleFacture.setIdQD(facture.getQd().getId());
            simpleFacture.setAcceptationForcee(forcerAcceptation);
            facture.setSimpleFacture(simpleFacture);

            if ((null == simpleFacture.getHygienisteDentaire()) || (false == simpleFacture.getHygienisteDentaire())) {
                simpleFacture.setHygienisteDentaire(false);
                simpleFacture.setMinutesHygieniste(null);
            }

            if (!forcerAcceptation) {
                FactureChecker.checkForCreate(facture);
            }
            // Mise à jour des qd
            facture.setQd(PerseusServiceLocator.getQDService().utiliserMontant(facture.getQd(),
                    facture.getSimpleFacture().getMontantRembourse()));

            // Mise à 0 du dépassement de la qd car en cas de modification de facture, seuls les cas de forcer paiement
            // dont le montant utilisé est plus grand que la limite de la qd doivent être affiché.
            facture.getSimpleFacture().setMontantDepassant("0");

            // Enregistré le montant de la qd dépassé
            if (forcerAcceptation) {
                Float montantUtilise = Float.parseFloat(facture.getQd().getMontantUtilise());
                Float montantLimite = Float.parseFloat(facture.getQd().getMontantLimite());
                if (montantUtilise > montantLimite) {
                    Float montantDepasse = montantUtilise - montantLimite;
                    facture.getSimpleFacture().setMontantDepassant(montantDepasse.toString());
                }
            }

            FactureChecker.checkNumReferenceFacture(facture);

            facture.setSimpleFacture(PerseusImplServiceLocator.getSimpleFactureService().create(simpleFacture));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.getMessage());
        } catch (Exception e) {
            throw new FactureException("Exception during facture creation : " + e.getMessage(), e);
        }
        return facture;
    }

    private Facture createPrestationAndOVforLot(Facture facture, Boolean restitution, Lot lot)
            throws JadePersistenceException, FactureException {
        try {
            // Création de la prestation
            Prestation prestation = new Prestation();
            prestation.setLot(lot);
            prestation.getSimplePrestation().setDatePrestation(JadeDateUtil.getGlobazFormattedDate(new Date()));
            prestation.getSimplePrestation().setEtatPrestation(CSEtatPrestation.DEFINITIF.getCodeSystem());
            if (restitution) {
                prestation.getSimplePrestation()
                        .setMontantTotal("-" + facture.getSimpleFacture().getMontantRembourse());
            } else {
                prestation.getSimplePrestation().setMontantTotal(facture.getSimpleFacture().getMontantRembourse());
            }
            prestation.getSimplePrestation().setIdFacture(facture.getId());
            String anneeQd = facture.getQd().getQdAnnuelle().getSimpleQDAnnuelle().getAnnee();
            prestation.getSimplePrestation().setDateDebut("01.01." + anneeQd);
            prestation.getSimplePrestation().setDateFin("31.12." + anneeQd);
            prestation = PerseusServiceLocator.getPrestationService().create(prestation);

            if (!restitution && (Float.parseFloat(facture.getSimpleFacture().getMontantRembourse()) > 0)) {
                // Créer l'ordre de versement
                OrdreVersement ordreVersement = new OrdreVersement();
                ordreVersement.setSimplePrestation(prestation.getSimplePrestation());
                ordreVersement.getSimpleOrdreVersement().setNumFacture(facture.getSimpleFacture().getNumRefFacture());
                ordreVersement.getSimpleOrdreVersement().setMontantVersement(
                        facture.getSimpleFacture().getMontantRembourse());
                ordreVersement.getSimpleOrdreVersement().setIdTiers(
                        facture.getSimpleFacture().getIdTiersAdressePaiement());
                ordreVersement.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                        facture.getSimpleFacture().getIdTiersAdressePaiement());
                ordreVersement.getSimpleOrdreVersement().setIdDomaineApplication(
                        facture.getSimpleFacture().getIdApplicationAdressePaiement());
                if (facture
                        .getSimpleFacture()
                        .getIdTiersAdressePaiement()
                        .equals(facture.getQd().getQdAnnuelle().getDossier().getDemandePrestation()
                                .getPersonneEtendue().getTiers().getId())) {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.REQUERANT.getCodeSystem());
                } else {
                    ordreVersement.getSimpleOrdreVersement().setCsTypeVersement(
                            CSTypeVersement.AUTRE_TIERS.getCodeSystem());
                }

                ordreVersement = PerseusServiceLocator.getOrdreVersementService().create(ordreVersement);
            }
        } catch (LotException e) {
            throw new FactureException("LotException during facture validation - " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.toString(), e);
        }

        return facture;
    }

    private Facture createPrestationAndOV(Facture facture, Boolean restitution) throws JadePersistenceException,
            FactureException {
        try {
            // Comptabilisé la facture dans le lot
            Lot lot = PerseusServiceLocator.getLotService().getLotCourant(CSTypeLot.LOT_FACTURES);
            // Création de la prestation
            createPrestationAndOVforLot(facture, restitution, lot);

        } catch (LotException e) {
            throw new FactureException("LotException during facture validation - " + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.toString(), e);
        }

        return facture;
    }

    @Override
    public Facture delete(Facture facture) throws JadePersistenceException, FactureException {
        if (facture == null) {
            throw new FactureException("Unable to delete facture, Facture given is null.");
        }
        try {
            facture = updateQDStateForDelete(facture);

            // Suppression de la facture
            facture.setSimpleFacture(PerseusImplServiceLocator.getSimpleFactureService().delete(
                    facture.getSimpleFacture()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.getMessage(), e);
        } catch (QDException e) {
            throw new FactureException("QDException during Facture delete : " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new FactureException("NumberFormatException during Facture delete : " + e.getMessage(), e);
        }
        return facture;
    }

    /**
     * Cherche les informations nécessaire afin d'indiquer à l'utilisateur le nombre de facture et le nombres de minutes
     * totaux de ceux ci qui ont été de type hygieniste dentaire pour le membre de la famille pour l'année de la
     * nouvelle facture (etat : enregistré et validé).
     * 
     * @param dateFacture
     *            Date de la facture
     * @param idMembreFamille
     *            L'id du membre de la famille
     * @return Nb de factures et Nb de minutes totaux
     */
    @Override
    public Map<String, String> getInformationsAboutHygienisteDentaire(String datePriseEnCharge, String dateFacture,
            String idMembreFamille) throws JadePersistenceException {

        if ((dateFacture == null) || (idMembreFamille == null)) {
            return null;
        }
        String dateAVerifier = dateFacture;

        // Si la date de prise en charge est renseigné, c est elle qui fait fois
        // Nous faisons un trim car nous passons avec ajax un espace si la date n est pas renseigné
        if (!JadeStringUtil.isEmpty(datePriseEnCharge.trim())) {
            dateAVerifier = datePriseEnCharge;
        }

        Integer nbResults = 0;
        Integer totalMinutes = 0;
        String annee = JadeStringUtil.substring(dateAVerifier, 6);

        List<String> inEtat = new ArrayList<String>();
        inEtat.add(CSEtatFacture.ENREGISTRE.getCodeSystem());
        inEtat.add(CSEtatFacture.VALIDE.getCodeSystem());

        FactureSearchModel searchFactures = new FactureSearchModel();
        searchFactures.setForAnnee(annee);
        searchFactures.setForIdMembreFamille(idMembreFamille);
        searchFactures.setInCsEtatFacture(inEtat);
        searchFactures.setForIsHygienisteDentaire(true);

        try {
            searchFactures = search(searchFactures);
            nbResults = searchFactures.getNbOfResultMatchingQuery();

            for (JadeAbstractModel model : searchFactures.getSearchResults()) {
                Facture facture = (Facture) model;
                totalMinutes += new Integer(facture.getSimpleFacture().getMinutesHygieniste());
            }

            Map<String, String> informationsRetours = new HashMap<String, String>();
            informationsRetours.put("nbFactures", nbResults.toString());
            informationsRetours.put("nbMinutes", totalMinutes.toString());

            return informationsRetours;
        } catch (FactureException e) {
            throw new JadePersistenceException("Cannot search any results with this statements");
        }
    }

    @Override
    public Facture read(String idFacture) throws JadePersistenceException, FactureException {
        if (idFacture == null) {
            throw new FactureException("Unable to read facture, idFacture given is null.");
        }
        Facture facture = new Facture();
        facture.setId(idFacture);
        return (Facture) JadePersistenceManager.read(facture);
    }

    @Override
    public Facture restituer(Facture facture) throws JadePersistenceException, FactureException {
        if (facture == null) {
            throw new FactureException("Unable to validate Facture, the given model is null");
        }
        try {
            if (!CSEtatFacture.VALIDE.getCodeSystem().equals(facture.getSimpleFacture().getCsEtat())) {
                JadeThread.logError(this.getClass().getName(), "perseus.facture.restitution.jamaisvalide");
            }

            // Si le lot n'a pas été comptabilisé, on ne peut pas demander la restitution
            Lot lot = PerseusServiceLocator.getLotService().getLotForFacture(facture.getId());

            if (JadeStringUtil.isEmpty(lot.getSimpleLot().getDateEnvoi())) {
                JadeThread.logError(this.getClass().getName(), "perseus.facture.restitution.lotouvert");
            }

            facture = createPrestationAndOV(facture, true);

            facture = updateQDStateForDelete(facture);

            facture.getSimpleFacture().setCsEtat(CSEtatFacture.RESTITUE.getCodeSystem());
            facture = PerseusServiceLocator.getFactureService().update(facture);

        } catch (LotException e) {
            throw new FactureException("LotException during facture restituer - " + e.toString(), e);
        } catch (QDException e) {
            throw new FactureException("QDException during Facture restituer : " + e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.toString(), e);
        }

        return facture;
    }

    @Override
    public FactureSearchModel search(FactureSearchModel searchModel) throws JadePersistenceException, FactureException {
        if (searchModel == null) {
            throw new FactureException("Unable to search facture, FactureSearchModel given is null.");
        }
        return (FactureSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public Facture update(Facture facture) throws JadePersistenceException, FactureException {
        if (facture == null) {
            throw new FactureException("Unable to update facture, Facture given is null.");
        }
        try {
            facture.setSimpleFacture(PerseusImplServiceLocator.getSimpleFactureService().update(
                    facture.getSimpleFacture()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.getMessage());
        }
        return facture;
    }

    /**
     * Permet de remettre en état les QD lors de la suppression ou la restitution d'une facture
     * 
     * @param facture
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws NumberFormatException
     * @throws QDException
     * @throws Exception
     */
    private Facture updateQDStateForDelete(Facture facture) throws QDException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Remise en état des QDs
        facture.setQd(PerseusServiceLocator.getQDService().annulerMontantUtilise(facture.getQd(),
                facture.getSimpleFacture().getMontantRembourse()));
        // Remise en état de l'excédant de revenu compensé
        if (Float.parseFloat(facture.getSimpleFacture().getExcedantRevenuCompense()) > 0) {
            QDAnnuelle qdAnnuelle = facture.getQd().getQdAnnuelle();

            // BZ 8397 Problème d'arrondi lors de la soustraction de l'excedent de revenu de la qd par celui de la
            // facture.
            // Utilisation de FWCurrency à la place des Float.
            FWCurrency newExcedantRevenuCompense = new FWCurrency(qdAnnuelle.getSimpleQDAnnuelle()
                    .getExcedantRevenuCompense());
            newExcedantRevenuCompense.sub(new FWCurrency(facture.getSimpleFacture().getExcedantRevenuCompense()));
            // Float newExcedantRevenuCompense = Float.parseFloat(qdAnnuelle.getSimpleQDAnnuelle()
            // .getExcedantRevenuCompense())
            // - Float.parseFloat(facture.getSimpleFacture().getExcedantRevenuCompense());
            qdAnnuelle.getSimpleQDAnnuelle().setExcedantRevenuCompense(newExcedantRevenuCompense.toString());
            facture.getQd().setQdAnnuelle(PerseusServiceLocator.getQDAnnuelleService().update(qdAnnuelle));
        }

        return facture;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.qd.FactureService#valider(ch.globaz.perseus.business.models.qd.Facture
     * )
     */
    @Override
    public Facture valider(Facture facture) throws JadePersistenceException, FactureException {
        return valider(facture, null).getFacture();
    }

    private FactureWrapper valider(Facture facture, Lot lot) throws JadePersistenceException, FactureException {
        String errorMessage = null;
        if (facture == null) {
            throw new FactureException("Unable to validate Facture, the given model is null");
        }
        try {
            if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(facture.getSimpleFacture().getCsEtat())) {
                if (lot != null) {
                    facture = createPrestationAndOVforLot(facture, false, lot);
                } else {
                    facture = createPrestationAndOV(facture, false);
                }
                // Passé la facture en état validé
                facture.getSimpleFacture().setCsEtat(CSEtatFacture.VALIDE.getCodeSystem());
                facture.getSimpleFacture().setDateValidation(JACalendar.todayJJsMMsAAAA());

                facture = PerseusServiceLocator.getFactureService().update(facture);
            } else {
                errorMessage = "PF_VALIDATION_FACTURE_ERREUR_FACTURE_ALLREADY_VALIDATED";
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FactureException("Service facture not available - " + e.toString(), e);
        }
        return new FactureWrapper(facture, errorMessage);
    }

    @Override
    public List<FactureWrapper> validerMultiple(List<String> idFactures) throws JadePersistenceException,
            FactureException {
        if (idFactures == null) {
            throw new RuntimeException("Unable to execute the vlaidation facture the ids facture is null");
        }
        List<FactureWrapper> listFacture = new ArrayList<FactureWrapper>();
        FactureSearchModel searchFactures = new FactureSearchModel();
        if (!idFactures.isEmpty()) {
            searchFactures.setInIdFacture(idFactures);
            searchFactures = search(searchFactures);
            try {
                Lot lot = PerseusServiceLocator.getLotService().getLotCourant(CSTypeLot.LOT_FACTURES);

                for (JadeAbstractModel jadeAbstractModel : searchFactures.getSearchResults()) {
                    Facture facture = (Facture) jadeAbstractModel;
                    FactureWrapper wrapper = new FactureWrapper(facture,
                            "PF_VALIDATION_FACTURE_ERREUR_FACTURE_ECHEC_PROCESS");
                    try {
                        wrapper = valider(facture, lot);
                    } finally {
                        listFacture.add(wrapper);
                    }
                }
            } catch (LotException e) {
                throw new FactureException("LotException during facture validation - " + e.toString(), e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new FactureException("Service facture not available - " + e.toString(), e);
            }
        }
        return listFacture;
        // // TODO Auto-generated method stub
        // if (((objSession.hasRight("perseus.facture.validation", FWSecureConstants.UPDATE)) || (objSession.hasRight(
        // "perseus.qd.detailfacture", FWSecureConstants.ADD)
        // && Float.valueOf(viewBean.getSimpleFacture()
        // .getMontantRembourse()) < montantMaxValidationUserVM.getMontant()))
        // && PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise()) {
        // }

    }
}
