package ch.globaz.perseus.businessimpl.services.models.dossier;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.db.recaprubriques.CARecapRubriquesExcel;
import globaz.osiris.db.recaprubriques.CARecapRubriquesExcelManager;
import globaz.osiris.external.IntRole;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.tools.PRDateFormater;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeEtendue;
import ch.globaz.perseus.business.models.demande.DemandeEtendueSearchModel;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationRP;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.dossier.DossierService;
import ch.globaz.perseus.businessimpl.checkers.dossier.DossierChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestation;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;

/**
 * @author vyj
 */
public class DossierServiceImpl extends PerseusAbstractServiceImpl implements DossierService {

    /**
     * Méthode permettant de calculer le montant pour le mesure de coaching.
     * 
     * @param dossier Le dossier à traiter.
     * @param dateDebut La date de début.
     * @param dateFin La date de fin.
     * @return Le montant en float.
     */
    @Override
    public Float calculerMontantMesureCoaching(Dossier dossier, String dateDebut, String dateFin)
            throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to calculerMontantMesureCoaching, model dossier is null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
            throw new DossierException("Unable to calculerMontantMesureCoaching, dateDebut is not a monthYear date");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(dateFin)) {
            throw new DossierException("Unable to calculerMontantMesureCoaching, dateFin is not a monthYear date");
        }

        // Faire une liste des mois de la demande pour les montants déjà versés dans le mois
        HashMap<String, Float> listMoisDejaVerse = new HashMap<String, Float>();
        ArrayList<String> listMois = new ArrayList<String>();
        String moisCourant = "01." + dateDebut;
        while (!JadeDateUtil.isDateMonthYearAfter(moisCourant.substring(3), dateFin)) {
            listMoisDejaVerse.put(moisCourant.substring(3), new Float(0));
            listMois.add(moisCourant.substring(3));
            moisCourant = JadeDateUtil.addMonths(moisCourant, 1);
        }
        try {

            // On précharges les prestations dans une map
            PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
            prestationSearchModel.setForIdDossier(dossier.getId());
            prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
            prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
            HashMap<String, Prestation> prestationsRetro = new HashMap<String, Prestation>();
            for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
                Prestation p = (Prestation) model;
                prestationsRetro.put(p.getDecision().getDemande().getId(), p);
            }

            // PRendre le mois du prochain paiement mensuel
            String moisProchainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // Pour toutes les demandes du dossier
            DemandeEtendueSearchModel demandeEtendueSearchModel = new DemandeEtendueSearchModel();
            demandeEtendueSearchModel.setForIdDossier(dossier.getId());
            demandeEtendueSearchModel.setForCsEtat(CSEtatDemande.VALIDE.getCodeSystem());
            demandeEtendueSearchModel.setOrderKey(DemandeEtendueSearchModel.ORDER_BY_DATETIME_DECISION);
            demandeEtendueSearchModel = (DemandeEtendueSearchModel) JadePersistenceManager
                    .search(demandeEtendueSearchModel);
            for (JadeAbstractModel model : demandeEtendueSearchModel.getSearchResults()) {
                DemandeEtendue demande = (DemandeEtendue) model;

                Float montantMensuelPcfa = new Float(0);
                // Si il y'a une pcfaccordée si non c'est un refu donc on prend 0
                if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())) {
                    // Reprendre la mesure de coaching dans le calcul
                    // On est obligé de lire la PCFA pour lire le blob
                    PCFAccordee pcfa = PerseusServiceLocator.getPCFAccordeeService().read(
                            demande.getSimplePcfAccordee().getId());
                    montantMensuelPcfa = pcfa.getCalcul().getDonnee(OutputData.MESURE_COACHING);
                }
                // Si il y'a une prestation retro, comptabiliser le retro pour chaque mois
                if (prestationsRetro.get(demande.getId()) != null) {
                    Prestation pRetro = prestationsRetro.get(demande.getId());
                    // Pour chaque mois
                    for (String mois : listMois) {
                        // On regarde si le mois est dans la prestation
                        if (!JadeDateUtil.isDateMonthYearBefore(mois, pRetro.getSimplePrestation().getDateDebut()
                                .substring(3))
                                && !JadeDateUtil.isDateMonthYearAfter(mois, pRetro.getSimplePrestation().getDateFin()
                                        .substring(3))) {
                            Float montantMensuel = montantMensuelPcfa - listMoisDejaVerse.get(mois);
                            // Mettre à jour le total mensuel
                            listMoisDejaVerse.put(mois, listMoisDejaVerse.get(mois) + montantMensuel);
                        }
                    }
                }
                // Comptabilise les prestations mensuelles versées si il y'a une pcfa et que la pcfa a une date de
                // décision
                if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())
                        && !JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getDateDecision())) {
                    for (String mois : listMois) {
                        // On regarde si le mois est dans la pcfa
                        if (!JadeDateUtil.isDateMonthYearBefore(mois, demande.getSimplePcfAccordee().getDateDecision())
                                && (!JadeDateUtil.isDateMonthYearAfter(mois, demande.getSimplePcfAccordee()
                                        .getDateDiminution()) || JadeStringUtil.isEmpty(demande.getSimplePcfAccordee()
                                        .getDateDiminution()))) {
                            // Voir si elle a déjà été payé (mois doit être avant le prochain paiement mensuel)
                            if (JadeDateUtil.isDateMonthYearBefore(mois, moisProchainPaiement)) {
                                // Mettre à jour le total mensuel
                                listMoisDejaVerse.put(mois, listMoisDejaVerse.get(mois) + montantMensuelPcfa);
                            }
                        }
                    }
                }
            }

        } catch (JadeApplicationException e) {
            throw new DossierException("JadeApplicationException during calculerMontantVerse : " + e.toString(), e);
        }

        Float montantDejaVerse = new Float(0);
        for (String mois : listMois) {
            montantDejaVerse += listMoisDejaVerse.get(mois);
        }

        return montantDejaVerse;
    }

    /**
     * Méthode permettant de calculer le montant retro mensuel.
     * 
     * @param prestation Une prestation.
     * @return Le montant en float.
     */
    private Float calculerMontantRetroMenseul(Prestation prestation) {
        Float montantMenseulRetro = Float.valueOf(prestation.getSimplePrestation().getMontantTotal())
                / JadeDateUtil.getNbMonthsBetween(prestation.getSimplePrestation().getDateDebut(), prestation
                        .getSimplePrestation().getDateFin());
        return montantMenseulRetro;
    }

    @Override
    public Float calculerMontantVerse(Dossier dossier, String dateDebut, String dateFin)
            throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to calculerMontantVerse, model dossier is null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
            throw new DossierException("Unable to calculerMontantVerse, dateDebut is not a monthYear date");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(dateFin)) {
            throw new DossierException("Unable to calculerMontantVerse, dateFin is not a monthYear date");
        }

        // Faire une liste des mois de la demande pour les montants déjà versés dans le mois
        HashMap<String, Float> listMoisDejaVerse = new HashMap<String, Float>();
        ArrayList<String> listMois = new ArrayList<String>();
        String moisCourant = "01." + dateDebut;
        while (!JadeDateUtil.isDateMonthYearAfter(moisCourant.substring(3), dateFin)) {
            listMoisDejaVerse.put(moisCourant.substring(3), new Float(0));
            listMois.add(moisCourant.substring(3));
            moisCourant = JadeDateUtil.addMonths(moisCourant, 1);
        }
        try {

            // On précharges les prestations dans une map
            PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
            prestationSearchModel.setForIdDossier(dossier.getId());
            prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
            prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
            HashMap<String, Prestation> prestationsRetro = new HashMap<String, Prestation>();
            for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
                Prestation p = (Prestation) model;
                prestationsRetro.put(p.getDecision().getDemande().getId(), p);
            }

            // PRendre le mois du prochain paiement mensuel
            String moisProchainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // Pour toutes les demandes du dossier
            DemandeEtendueSearchModel demandeEtendueSearchModel = new DemandeEtendueSearchModel();
            demandeEtendueSearchModel.setForIdDossier(dossier.getId());
            demandeEtendueSearchModel.setForCsEtat(CSEtatDemande.VALIDE.getCodeSystem());
            demandeEtendueSearchModel.setOrderKey(DemandeEtendueSearchModel.ORDER_BY_DATETIME_DECISION);
            demandeEtendueSearchModel = (DemandeEtendueSearchModel) JadePersistenceManager
                    .search(demandeEtendueSearchModel);
            for (JadeAbstractModel model : demandeEtendueSearchModel.getSearchResults()) {
                DemandeEtendue demande = (DemandeEtendue) model;

                Float montantMensuelPcfa = new Float(0);
                // Si il y'a une pcfaccordée si non c'est un refu donc on prend 0
                if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())) {
                    montantMensuelPcfa = Float.parseFloat(demande.getSimplePcfAccordee().getMontant());
                }
                // Si il y'a une prestation retro, comptabiliser le retro pour chaque mois
                if (prestationsRetro.get(demande.getId()) != null) {
                    Prestation pRetro = prestationsRetro.get(demande.getId());
                    // Pour chaque mois
                    for (String mois : listMois) {
                        // On regarde si le mois est dans la prestation
                        if (!JadeDateUtil.isDateMonthYearBefore(mois, pRetro.getSimplePrestation().getDateDebut()
                                .substring(3))
                                && !JadeDateUtil.isDateMonthYearAfter(mois, pRetro.getSimplePrestation().getDateFin()
                                        .substring(3))) {
                            Float montantMensuel = montantMensuelPcfa - listMoisDejaVerse.get(mois);
                            // Mettre à jour le total mensuel
                            listMoisDejaVerse.put(mois, listMoisDejaVerse.get(mois) + montantMensuel);
                        }
                    }
                }
                // Comptabilise les prestations mensuelles versées si il y'a une pcfa et que la pcfa a une date de
                // décision
                if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())
                        && !JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getDateDecision())) {
                    for (String mois : listMois) {
                        // On regarde si le mois est dans la pcfa
                        if (!JadeDateUtil.isDateMonthYearBefore(mois, demande.getSimplePcfAccordee().getDateDecision())
                                && (!JadeDateUtil.isDateMonthYearAfter(mois, demande.getSimplePcfAccordee()
                                        .getDateDiminution()) || JadeStringUtil.isEmpty(demande.getSimplePcfAccordee()
                                        .getDateDiminution()))) {
                            // Voir si elle a déjà été payé (mois doit être avant le prochain paiement mensuel)
                            if (JadeDateUtil.isDateMonthYearBefore(mois, moisProchainPaiement)) {
                                // Mettre à jour le total mensuel
                                listMoisDejaVerse.put(mois, listMoisDejaVerse.get(mois) + montantMensuelPcfa);
                            }
                        }
                    }
                }
            }

        } catch (JadeApplicationException e) {
            throw new DossierException("JadeApplicationException during calculerMontantVerse : " + e.toString(), e);
        }

        Float montantDejaVerse = new Float(0);
        for (String mois : listMois) {
            montantDejaVerse += listMoisDejaVerse.get(mois);
        }

        return montantDejaVerse;
    }

    @Override
    public FWCurrency calculerMontantVerseAttestationPCF(Dossier dossier, String anneeAttestation)
            throws JadePersistenceException, DossierException, JAException {
        if (dossier == null) {
            throw new DossierException("Unable to calculerMontantVerseAttestationPCF, model dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateYear(anneeAttestation)) {
            throw new DossierException("Unable to calculerMontantVerseAttestationPCF, anneeAttestation is not a year");
        }
        try {
            // listMoisPeriodeRetro contient tout les mois depuis le début des PC Familles jusqu'à la fin de l'année
            // d'attestation
            ArrayList<String> listMoisPeriodeRetro = getMoisBetweenTwoDate("09.2011", "12." + anneeAttestation);
            // listMoisNonImposeSourceRetro contient tout les mois retro actif non imposé à la source depuis le début
            // des PC Familles pour un dossier specifique
            ArrayList<String> listMoisNonImposeSourceRetro = new ArrayList<String>();
            // listMoisNonImposeeSourcePmtMensuel contient tout les mois de l'année d'attestation non imposé à la source
            // pour un dossier specifique
            ArrayList<String> listMoisNonImposeSourcePmtMensuel = new ArrayList<String>();

            getListMoisNonImposeSource(listMoisNonImposeSourceRetro, listMoisNonImposeSourcePmtMensuel,
                    listMoisPeriodeRetro, dossier.getDossier().getIdDossier(), anneeAttestation);

            FWCurrency montantVerseRetroPeriodeNonImposeSource = calculerMontantVerseeRetroPeriodeNonImposeSource(
                    dossier.getDossier().getIdDossier(), anneeAttestation, listMoisNonImposeSourceRetro);
            FWCurrency montantVerseMenseulPeriodeNonImposeSource = calculerMontantVerseeMensuelPeriodeNonImposeSource(
                    dossier, anneeAttestation, listMoisNonImposeSourcePmtMensuel);

            // Adition du montant versee en retro et en pmt menseul pour les mois non imposé à la source. Un arrondi est
            // effectué car
            // poru les périodes rétro active, je détermine le montant montant par mois alors que dans la prestation il
            // n'y a que le montant total
            FWCurrency montantVersee = new FWCurrency(
                    Math.round((montantVerseRetroPeriodeNonImposeSource.intValue() + montantVerseMenseulPeriodeNonImposeSource
                            .intValue())));
            return montantVersee;
        } catch (JadeApplicationException e) {
            throw new DossierException("JadeApplicationException during calculerMontantVerseAttestationRP : "
                    + e.toString(), e);
        }
    }

    @Override
    public FWCurrency calculerMontantVerseAttestationRP(Dossier dossier, String anneeAttestation)
            throws JadePersistenceException, DossierException, JAException {
        if (dossier == null) {
            throw new DossierException("Unable to calculerMontantVerseAttestationRP, model dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateYear(anneeAttestation)) {
            throw new DossierException("Unable to calculerMontantVerseAttestationRP, anneeAttestation is not a year");
        }

        try {
            // Faire une liste des mois de la demande pour les montants déjà versés dans le mois
            String moisCourant = "01.01." + anneeAttestation;
            HashMap<String, Float> listMoisDejaVerse = new HashMap<String, Float>();
            ArrayList<String> listMois = new ArrayList<String>();
            while (!JadeDateUtil.isDateMonthYearAfter(moisCourant.substring(3), "12." + anneeAttestation)) {
                listMoisDejaVerse.put(moisCourant.substring(3), new Float(0));
                listMois.add(moisCourant.substring(3));
                moisCourant = JadeDateUtil.addMonths(moisCourant, 1);
            }

            FWCurrency montantVersee = new FWCurrency();

            // On précharges les prestations
            PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
            prestationSearchModel.setForIdDossier(dossier.getId());
            prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION_RP.getCodeSystem());
            prestationSearchModel.setForEtatLot(CSEtatLot.LOT_VALIDE.getCodeSystem());
            prestationSearchModel.setModelClass(PrestationRP.class);
            prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
            for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
                PrestationRP p = (PrestationRP) model;
                // Si la date de comptabilisation du lot de la prestation est dans l'année de génération des
                // attestations fiscales
                // et que la demande de rente pont n'est pas imposée à la source, prise en compte de la prestation
                if (p.getLot().getSimpleLot().getDateEnvoi().substring(6).equals(anneeAttestation)
                        && ("0.00".equals(p.getRentePont().getSimpleRentePont().getMontantImpotSource()) || JadeStringUtil
                                .isEmpty(p.getRentePont().getSimpleRentePont().getMontantImpotSource()))) {
                    montantVersee.add(p.getSimplePrestation().getMontantTotal());
                }

            }

            // Prendre le mois du prochain paiement mensuel
            String moisProchainPaiement = PerseusServiceLocator.getPmtMensuelRentePontService().getDateProchainPmt();

            RentePontSearchModel rentePontSearchModel = new RentePontSearchModel();
            rentePontSearchModel.setForIdDossier(dossier.getId());
            rentePontSearchModel.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
            rentePontSearchModel.setOrderKey(RentePontSearchModel.ORDER_BY_DATE_DECISION);
            rentePontSearchModel = PerseusServiceLocator.getRentePontService().search(rentePontSearchModel);

            for (JadeAbstractModel model : rentePontSearchModel.getSearchResults()) {
                RentePont rentePont = (RentePont) model;
                // Si la demande n'est pas imposée à la source
                if ("0.00".equals(rentePont.getSimpleRentePont().getMontantImpotSource())) {

                    Float montantMensuelRP = new Float(0);

                    montantMensuelRP = Float.parseFloat(rentePont.getSimpleRentePont().getMontant());

                    // Comptabilise les prestations mensuelles versées si la rente pont a une date de décision et un
                    // montant différent de zéro
                    if (!"0.00".equals(rentePont.getSimpleRentePont().getMontant())
                            && !JadeStringUtil.isEmpty(rentePont.getSimpleRentePont().getDateDecision())) {
                        for (String mois : listMois) {
                            // On regarde si le mois est dans la rente pont
                            if (!JadeDateUtil.isDateMonthYearBefore(mois, rentePont.getSimpleRentePont()
                                    .getDateDecision())
                                    && (!JadeDateUtil.isDateMonthYearAfter(mois, PRDateFormater
                                            .convertDate_JJxMMxAAAA_to_MMxAAAA(rentePont.getSimpleRentePont()
                                                    .getDateFin())) || JadeStringUtil.isEmpty(rentePont
                                            .getSimpleRentePont().getDateFin()))) {
                                // Voir si elle a déjà été payé (mois doit être avant le prochain paiement mensuel)
                                if (JadeDateUtil.isDateMonthYearBefore(mois, moisProchainPaiement)) {
                                    // Mettre à jour le total mensuel
                                    montantVersee.add(montantMensuelRP);
                                }
                            }
                        }
                    }
                }
            }

            return montantVersee;
        } catch (JadeApplicationException e) {
            throw new DossierException("JadeApplicationException during calculerMontantVerseAttestationRP : "
                    + e.toString(), e);
        }

    }

    /**
     * Calcul du montant montant menseul versé pour tout les mois non imposé à la source
     * 
     * @throws LotException
     *             , JadeApplicationServiceNotAvailableException, JadePersistenceException
     */
    private FWCurrency calculerMontantVerseeMensuelPeriodeNonImposeSource(Dossier dossier, String anneeAttestation,
            ArrayList<String> listMoisNonImposeSourcePmtMensuel) throws JadePersistenceException, PaiementException,
            JadeApplicationServiceNotAvailableException {
        FWCurrency montantVersee = new FWCurrency();
        DemandeEtendueSearchModel demandeEtendueSearchModel = loadDemandeEtendueValideOrderByDateTimeDecision(dossier
                .getDossier().getIdDossier());
        for (JadeAbstractModel model : demandeEtendueSearchModel.getSearchResults()) {
            DemandeEtendue demande = (DemandeEtendue) model;
            if (isDemandeNonRefusSansCalculAndWithDateDecision(demande)) {
                montantVersee = calculerMontantVerseeMensuelPeriodeNonImposeSourcePourMoisDemande(demande,
                        anneeAttestation, listMoisNonImposeSourcePmtMensuel, montantVersee);
            }
        }
        return montantVersee;
    }

    private FWCurrency calculerMontantVerseeMensuelPeriodeNonImposeSourcePourMoisDemande(DemandeEtendue demande,
            String anneeAttestation, ArrayList<String> listMoisNonImposeSourcePmtMensuel, FWCurrency montantVersee)
            throws PaiementException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ArrayList<String> listMoisDemande = getListMoisMoisDemande(demande, anneeAttestation);
        // Déterminer
        // Déterminer si le mois de rétro et dans la liste des mois non imposé à la source
        for (String mois : listMoisDemande) {
            String moisProchainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            if (listMoisNonImposeSourcePmtMensuel.contains(mois)
                    && isPriseEnCompteDemandePourCalculerMontantVerseMenseul(demande, mois)
                    && JadeDateUtil.isDateMonthYearBefore(mois, moisProchainPaiement)) {

                // Mettre à jour le total mensuel
                Float montantMensuelPcfa = getMontantMenseulPCFA(demande);
                montantVersee.add(montantMensuelPcfa);

            }
        }
        return montantVersee;
    }

    /**
     * Calcul du montant retro actif versé
     * 
     * @throws LotException
     *             , JadeApplicationServiceNotAvailableException, JadePersistenceException
     */
    private FWCurrency calculerMontantVerseeRetro(String idDossier, String annee) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        FWCurrency montantVerse = new FWCurrency();
        PrestationSearchModel prestationSearchModel = this.loadPrestationDecisionValideeForIdDossier(idDossier, annee);
        for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
            Prestation p = (Prestation) model;
            if (isDateComptabilisationLotDansAnneeAttestation(p.getLot().getSimpleLot().getDateEnvoi(), annee)
                    && (Float.parseFloat(p.getSimplePrestation().getMontantTotal()) > 0)) {
                montantVerse.add(p.getSimplePrestation().getMontantTotal());
            }

        }
        return montantVerse;
    }

    /**
     * Calcul du montant retro actif versé pour tout les mois non imposé à la source
     * 
     * @throws LotException
     *             , JadeApplicationServiceNotAvailableException, JadePersistenceException
     */
    private FWCurrency calculerMontantVerseeRetroPeriodeNonImposeSource(String idDossier, String anneeAttestation,
            ArrayList<String> listMoisNonImposeSourceRetro) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        FWCurrency montantVerse = new FWCurrency();
        PrestationSearchModel prestationSearchModel = this.loadPrestationDecisionValideeForIdDossier(idDossier);
        for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
            Prestation p = (Prestation) model;
            if (isDateComptabilisationLotDansAnneeAttestation(p.getLot().getSimpleLot().getDateEnvoi(),
                    anneeAttestation)) {
                montantVerse = calculerMontantVerseeRetroPeriodeNonImposeSourcePourPrestation(p,
                        listMoisNonImposeSourceRetro, montantVerse);
            }

        }
        return montantVerse;
    }

    private FWCurrency calculerMontantVerseeRetroPeriodeNonImposeSourcePourPrestation(Prestation prestation,
            ArrayList<String> listMoisNonImposeSourceRetro, FWCurrency montantRetroPourMoisNonImposeSource) {
        // Déterminer le montant menuel du retro
        Float montantMenseulRetro = calculerMontantRetroMenseul(prestation);
        // Déterminer les mois en rétro
        // String moisRetro = p.getSimplePrestation().getDateDebut();
        ArrayList<String> listeMoisRetroDeLaPrestation = getMoisBetweenTwoDate(prestation.getSimplePrestation()
                .getDateDebut().substring(3), prestation.getSimplePrestation().getDateFin().substring(3));
        // Déterminer si le mois de rétro et dans la liste des mois non imposé à la source
        for (String mois : listeMoisRetroDeLaPrestation) {
            if (listMoisNonImposeSourceRetro.contains(mois)) {
                montantRetroPourMoisNonImposeSource.add(montantMenseulRetro);
            }
        }
        return montantRetroPourMoisNonImposeSource;
    }

    @Override
    public Float calculerMontantVerseImpotSource(Dossier dossier, String dateDebut, String dateFin)
            throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to calculerMontantVerseImpotSource, model dossier is null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
            throw new DossierException("Unable to calculerMontantVerseImpotSource, dateDebut is not a monthYear date");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(dateFin)) {
            throw new DossierException("Unable to calculerMontantVerseImpotSource, dateFin is not a monthYear date");
        }

        // Faire une liste des mois de la demande pour les montants déjà versés dans le mois
        HashMap<String, Float> listMoisDejaVerse = new HashMap<String, Float>();
        ArrayList<String> listMois = new ArrayList<String>();
        String moisCourant = "01." + dateDebut;
        while (!JadeDateUtil.isDateMonthYearAfter(moisCourant.substring(3), dateFin)) {
            listMoisDejaVerse.put(moisCourant.substring(3), new Float(0));
            listMois.add(moisCourant.substring(3));
            moisCourant = JadeDateUtil.addMonths(moisCourant, 1);
        }
        try {

            // On précharges les ov de types impot source dans une map
            PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
            prestationSearchModel.setForIdDossier(dossier.getId());
            prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
            prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
            HashMap<String, OrdreVersement> oVImpotSourceVerse = new HashMap<String, OrdreVersement>();
            for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
                Prestation p = (Prestation) model;
                OrdreVersementSearchModel ordreVersementSearchModel = new OrdreVersementSearchModel();
                ordreVersementSearchModel.setForIdPrestation(p.getSimplePrestation().getIdPrestation());
                ordreVersementSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                ordreVersementSearchModel = PerseusServiceLocator.getOrdreVersementService().search(
                        ordreVersementSearchModel);
                for (JadeAbstractModel ovModel : ordreVersementSearchModel.getSearchResults()) {
                    OrdreVersement ov = (OrdreVersement) ovModel;
                    if (Float.parseFloat(ov.getSimpleOrdreVersement().getMontantVersement()) > 0) {
                        if (CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem().equals(
                                ov.getSimpleOrdreVersement().getCsTypeVersement())) {
                            oVImpotSourceVerse.put(p.getDecision().getDemande().getId(), ov);
                        }
                    }
                }

            }

            // PRendre le mois du prochain paiement mensuel
            String moisProchainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

            // Pour toutes les demandes du dossier
            DemandeEtendueSearchModel demandeEtendueSearchModel = new DemandeEtendueSearchModel();
            demandeEtendueSearchModel.setForIdDossier(dossier.getId());
            demandeEtendueSearchModel.setForCsEtat(CSEtatDemande.VALIDE.getCodeSystem());
            demandeEtendueSearchModel.setOrderKey(DemandeEtendueSearchModel.ORDER_BY_DATETIME_DECISION);
            demandeEtendueSearchModel = (DemandeEtendueSearchModel) JadePersistenceManager
                    .search(demandeEtendueSearchModel);
            for (JadeAbstractModel model : demandeEtendueSearchModel.getSearchResults()) {
                DemandeEtendue demande = (DemandeEtendue) model;
                OrdreVersement ovRetro = oVImpotSourceVerse.get(demande.getId());

                // Si pas d'ov retro pas de retenue
                if (null != ovRetro) {

                    // montant total pris en compte pour la demande
                    Float montantDisponiblePlafond = Float.parseFloat(ovRetro.getSimpleOrdreVersement()
                            .getMontantVersement());

                    SimpleRetenueSearchModel retenueSearch = new SimpleRetenueSearchModel();
                    retenueSearch.setForIdPcfAccordee(demande.getSimplePcfAccordee().getIdPCFAccordee());
                    retenueSearch.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
                    retenueSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retenueSearch);

                    for (JadeAbstractModel retenueModel : retenueSearch.getSearchResults()) {
                        SimpleRetenue retenue = (SimpleRetenue) retenueModel;
                        // Si il y'a un OV retro, comptabiliser le montant de la retenue pour chaque mois
                        if (oVImpotSourceVerse.get(demande.getId()) != null) {

                            // Pour chaque mois
                            for (String mois : listMois) {
                                // On regarde si le mois est dans la prestation
                                if (!JadeDateUtil.isDateMonthYearBefore(mois, ovRetro.getSimplePrestation()
                                        .getDateDebut().substring(3))
                                        && !JadeDateUtil.isDateMonthYearAfter(mois, ovRetro.getSimplePrestation()
                                                .getDateFin().substring(3))) {
                                    // Définition du montant à retenir
                                    Float montantARetenir = Float.valueOf(retenue.getMontantRetenuMensuel())
                                            - listMoisDejaVerse.get(mois);
                                    // plaffonement du monta a retenir
                                    Float montantRetenu = Math.min(montantDisponiblePlafond, montantARetenir);

                                    // Mettre à jour le total mensuel
                                    listMoisDejaVerse.put(mois, listMoisDejaVerse.get(mois) + montantRetenu);

                                    // Mise à jour du montant dispo
                                    montantDisponiblePlafond = Math.max(montantDisponiblePlafond - montantARetenir, 0);

                                }
                            }
                        }
                    }

                }

                // Comptabilise les prestations mensuelles versées si il y'a une pcfa et que la pcfa a une date de
                // décision
                if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())
                        && !JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getDateDecision())) {
                    for (String mois : listMois) {
                        // On regarde si le mois est dans la pcfa
                        if (!JadeDateUtil.isDateMonthYearBefore(mois, demande.getSimplePcfAccordee().getDateDecision())
                                && (!JadeDateUtil.isDateMonthYearAfter(mois, demande.getSimplePcfAccordee()
                                        .getDateDiminution()) || JadeStringUtil.isEmpty(demande.getSimplePcfAccordee()
                                        .getDateDiminution()))) {
                            // Voir si elle a déjà été payé (mois doit être avant le prochain paiement mensuel)
                            if (JadeDateUtil.isDateMonthYearBefore(mois, moisProchainPaiement)) {
                                SimpleRetenueSearchModel retenueSearch2 = new SimpleRetenueSearchModel();
                                retenueSearch2.setForIdPcfAccordee(demande.getSimplePcfAccordee().getIdPCFAccordee());
                                retenueSearch2.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
                                retenueSearch2 = PerseusImplServiceLocator.getSimpleRetenueService().search(
                                        retenueSearch2);

                                for (JadeAbstractModel retenueModel : retenueSearch2.getSearchResults()) {
                                    SimpleRetenue retenue = (SimpleRetenue) retenueModel;

                                    if (!JadeStringUtil.isEmpty(retenue.getMontantRetenuMensuel())) {
                                        listMoisDejaVerse.put(
                                                mois,
                                                listMoisDejaVerse.get(mois)
                                                        + Float.parseFloat(retenue.getMontantRetenuMensuel()));
                                    }
                                }

                            }
                        }
                    }
                }
            }

        } catch (JadeApplicationException e) {
            throw new DossierException("JadeApplicationException during calculerMontantVerse : " + e.toString(), e);
        }

        Float montantDejaVerse = new Float(0);
        for (String mois : listMois) {
            montantDejaVerse += listMoisDejaVerse.get(mois);
        }

        return montantDejaVerse;
    }

    /**
     * Calcul du montant montant menseul versé
     * 
     * @throws LotException
     *             , JadeApplicationServiceNotAvailableException, JadePersistenceException
     */
    private FWCurrency calculerMontantVerseMensuel(Dossier dossier, String annee, ArrayList<String> listeMois)
            throws JadePersistenceException, PaiementException, JadeApplicationServiceNotAvailableException {
        FWCurrency montantVersee = new FWCurrency();
        DemandeEtendueSearchModel demandeEtendueSearchModel = loadDemandeEtendueValideOrderByDateTimeDecision(dossier
                .getDossier().getIdDossier());
        for (JadeAbstractModel model : demandeEtendueSearchModel.getSearchResults()) {
            DemandeEtendue demande = (DemandeEtendue) model;
            if (isDemandeNonRefusSansCalculAndWithDateDecision(demande)) {
                montantVersee = calculerMontantVerseMensuelPourDemande(demande, annee, listeMois, montantVersee);
            }
        }
        return montantVersee;
    }

    private FWCurrency calculerMontantVerseMensuelPourDemande(DemandeEtendue demande, String annee,
            ArrayList<String> listeMois, FWCurrency montantVersee) throws PaiementException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Déterminer si le mois de rétro et dans la liste des mois non imposé à la source
        for (String mois : listeMois) {
            String moisProchainPaiement = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            if (isPriseEnCompteDemandePourCalculerMontantVerseMenseul(demande, mois)
                    && JadeDateUtil.isDateMonthYearBefore(mois, moisProchainPaiement)) {
                // Mettre à jour le total mensuel
                Float montantMensuelPcfa = getMontantMenseulPCFA(demande);
                montantVersee.add(montantMensuelPcfa);

            }
        }
        return montantVersee;
    }

    @Override
    public FWCurrency calculerMontantVerseStatsOFS(Dossier dossier, String annee) throws JadePersistenceException,
            DossierException, JAException {
        if (dossier == null) {
            throw new DossierException("Unable to calculerMontantVerseStatsOFS, model dossier is null");
        }

        if (!JadeDateUtil.isGlobazDateYear(annee)) {
            throw new DossierException("Unable to calculerMontantVerseStatsOFS, annee is not a year");
        }
        try {
            ArrayList<String> listeMoisAnnee = getMoisBetweenTwoDate("01." + annee, "12." + annee);
            FWCurrency montantVerseRetro = calculerMontantVerseeRetro(dossier.getDossier().getIdDossier(), annee);
            FWCurrency montantVerseMensuel = calculerMontantVerseMensuel(dossier, annee, listeMoisAnnee);

            // Addition du montant verse retro et du montant verse en pmt mensuel pour les mois de l'année.
            FWCurrency montantVerse = new FWCurrency();
            montantVerse.add(montantVerseRetro);
            montantVerse.add(montantVerseMensuel);
            return montantVerse;
        } catch (JadeApplicationException e) {
            throw new DossierException(
                    "JadeApplicationException during calculerMontantVerseStatsOFS : " + e.toString(), e);
        }
    }

    @Override
    public int count(DossierSearchModel search) throws DossierException, JadePersistenceException {
        if (search == null) {
            throw new DossierException("Unable to count dossiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Dossier create(Dossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to create dossier, the given model is null!");
        }

        try {
            SimpleDemandePrestation simpleDm = dossier.getDemandePrestation().getDemandePrestation();
            simpleDm.setIdTiers(dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers());
            simpleDm.setEtat(IPRDemande.CS_ETAT_OUVERT);
            simpleDm.setTypeDemande(IPRDemande.CS_TYPE_PC_FAMILLES);

            dossier.setDemandePrestation(PrestationCommonServiceLocator.getDemandePrestationService().createOrRead(
                    dossier.getDemandePrestation()));
            dossier.getDossier().setIdDemandePrestation(dossier.getDemandePrestation().getId());
            DossierChecker.checkForCreate(dossier);

            PerseusImplServiceLocator.getSimpleDossierService().create(dossier.getDossier());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Service not available - " + e.getMessage());
        } catch (DemandePrestationException e) {
            throw new DossierException("DemandePrestationException during creating : " + e.getMessage(), e);
        }

        return dossier;
    }

    @Override
    public Dossier delete(Dossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to delete dossier, the given model is null!");
        }
        try {
            DossierChecker.checkForDelete(dossier);
            dossier.setDossier(PerseusImplServiceLocator.getSimpleDossierService().delete(dossier.getDossier()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Service not available - " + e.getMessage());
        }

        return dossier;
    }

    @Override
    public String findImpotSource(Dossier dossier, String dateDebut, String dateFin) throws JadePersistenceException,
            DossierException {
        if ((dossier == null) || dossier.isNew()) {
            throw new DossierException("Unable to findImportSource, the dossier passed is new or empty !");
        }
        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new DossierException("Unable to findImportSource, the dateDebut is not a well-formatted date");
        }
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new DossierException("Unable to findImportSource, the dateFin is not a well-formatted date");
        }

        String montant = "0";

        CARecapRubriquesExcelManager manager = new CARecapRubriquesExcelManager();
        manager.setSession(BSessionUtil.getSessionFromThreadContext());

        manager.setForSelectionRole(IntRole.ROLE_PCF);
        manager.setFromDateValeur(JadeDateUtil.getYMDDate(JadeDateUtil.getGlobazDate(dateDebut)));
        manager.setToDateValeur(JadeDateUtil.getYMDDate(JadeDateUtil.getGlobazDate(dateFin)));
        manager.setFromIdExterne(IPFConstantes.COMPTA_COMPTE_IMPOT_SOURCE);
        manager.setFromIdExterneRole(dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel());
        manager.setToIdExterneRole(dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel());

        try {

            manager.find();
            if (manager.size() > 0) {
                CARecapRubriquesExcel recap = (CARecapRubriquesExcel) manager.getFirstEntity();
                montant = recap.getSumMontant();
            }

        } catch (Exception e) {
            throw new DossierException("Exception pendant la lecture en compta auxiliaire : " + e.toString(), e);
        }

        return montant;
    }

    @Override
    public List<MembreFamille> getListAllMembresFamille(String idDossier) throws JadePersistenceException,
            DossierException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to get list all MembreFamilles, idDossier is empty !");
        }
        HashMap<String, MembreFamille> hm = new HashMap<String, MembreFamille>();
        try {
            DemandeSearchModel demandeSearchModel = new DemandeSearchModel();

            demandeSearchModel.setForIdDossier(idDossier);

            demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);

            for (JadeAbstractModel model : demandeSearchModel.getSearchResults()) {
                Demande demande = (Demande) model;
                hm.put(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId(), demande
                        .getSituationFamiliale().getRequerant().getMembreFamille());
                hm.put(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId(), demande
                        .getSituationFamiliale().getConjoint().getMembreFamille());
                List<Enfant> enfants = PerseusServiceLocator.getDemandeService().getListEnfants(demande);
                for (Enfant enf : enfants) {
                    hm.put(enf.getMembreFamille().getId(), enf.getMembreFamille());
                }
            }

        } catch (DemandeException e) {
            throw new DossierException("DemandeException during getListAllMembresFamille#DossierServiceImpl : "
                    + e.toString(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException(
                    "ApplicationServiceNotAvailable during getListAllMembresFamille#DossierServiceImpl : "
                            + e.toString(), e);
        }

        return new ArrayList<MembreFamille>(hm.values());
    }

    @Override
    public List<MembreFamille> getListAllMembresFamilleRentePont(String idDossier) throws JadePersistenceException,
            DossierException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to get list all MembreFamilles, idDossier is empty !");
        }
        HashMap<String, MembreFamille> hm = new HashMap<String, MembreFamille>();
        try {
            RentePontSearchModel rentePontSearchModel = new RentePontSearchModel();

            rentePontSearchModel.setForIdDossier(idDossier);

            rentePontSearchModel = PerseusServiceLocator.getRentePontService().search(rentePontSearchModel);

            for (JadeAbstractModel model : rentePontSearchModel.getSearchResults()) {
                RentePont rentePont = (RentePont) model;
                hm.put(rentePont.getSituationFamiliale().getRequerant().getMembreFamille().getId(), rentePont
                        .getSituationFamiliale().getRequerant().getMembreFamille());
                hm.put(rentePont.getSituationFamiliale().getConjoint().getMembreFamille().getId(), rentePont
                        .getSituationFamiliale().getConjoint().getMembreFamille());
                List<Enfant> enfants = PerseusServiceLocator.getRentePontService().getListEnfants(rentePont);
                for (Enfant enf : enfants) {
                    hm.put(enf.getMembreFamille().getId(), enf.getMembreFamille());
                }
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException(
                    "ApplicationServiceNotAvailable during getListAllMembresFamille#DossierServiceImpl : "
                            + e.toString(), e);
        } catch (RentePontException e) {
            throw new DossierException("RentePontException during getListAllMembresFamille#DossierServiceImpl : "
                    + e.toString(), e);
        }

        return new ArrayList<MembreFamille>(hm.values());
    }

    private ArrayList<String> getListMoisMoisDemande(Demande demande, String anneeAttestation) {
        // Déterminer les mois de la demande
        String moisDemande = demande.getSimpleDemande().getDateDebut();
        String dateFinDemande = "";

        if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) {
            dateFinDemande = "12." + anneeAttestation;
        } else {
            dateFinDemande = demande.getSimpleDemande().getDateFin().substring(3);
        }

        ArrayList<String> listMoisDemade = getMoisBetweenTwoDate(moisDemande.substring(3), dateFinDemande);
        return listMoisDemade;
    }

    /**
     * Va renseigner dans la liste litMoisNonImposeePmtMensuel tout les mois non imposé à la source pour l'année
     * d'attesatation fiscale pour un dossier specifique. Va aussi renseigner dans la liste listMoisNonImposeeRetro tout
     * les mois non imposé à la source depuis le début des PC Familles pour un dossier specfiique
     * 
     * @throws DemandeException
     *             , JadeApplicationServiceNotAvailableException, JadePersistenceException
     */
    private void getListMoisNonImposeSource(ArrayList<String> listMoisNonImposeeRetro,
            ArrayList<String> litMoisNonImposeePmtMensuel, ArrayList<String> listMoisPeriodeRetro, String idDossier,
            String anneeAttestation) throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        for (String mois : listMoisPeriodeRetro) {
            // Parcours de tout les mois pour déterminer si la dernière demande est imposée à la source ou non
            DemandeSearchModel dmSearch = new DemandeSearchModel();

            dmSearch.setForIdDossier(idDossier);
            dmSearch.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            dmSearch.setWhereKey(DemandeSearchModel.WITH_DATE_VALABLE_LE);
            dmSearch.setForDateValable("01." + mois);
            dmSearch.setOrderKey(DemandeSearchModel.ORDER_BY_DATETIME_DECISION_DESC);

            dmSearch = PerseusServiceLocator.getDemandeService().search(dmSearch);

            if (dmSearch.getSize() > 0) {
                Demande demande = (Demande) dmSearch.getSearchResults()[0];
                if (!demande.getSimpleDemande().getPermisB()) {
                    if (mois.substring(3).equals(anneeAttestation)) {
                        litMoisNonImposeePmtMensuel.add(mois);
                    }
                    listMoisNonImposeeRetro.add(mois);
                }
            }

        }
    }

    @Override
    public List<QD> getListQD(String idDossier, String dateFacture, String dateReception, String csTypeQD)
            throws JadePersistenceException, DossierException {
        return this.getListQD(idDossier, dateFacture, dateReception, csTypeQD, "");
    }

    private List<QD> getListQD(String idDossier, String dateFacture, String dateReception, String csTypeQD,
            String datePriseEnCharge) throws JadePersistenceException, DossierException {
        // Contrôle des paramètres
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to get list MembreFamilles, idDossier is empty !");
        }
        if (JadeStringUtil.isEmpty(dateFacture)) {
            throw new DossierException("Unable to get list MembreFamilles, dateFacture is empty !");
        }
        if (JadeStringUtil.isEmpty(dateReception)) {
            throw new DossierException("Unable to get list MembreFamilles, dateReception is empty !");
        }
        if (JadeStringUtil.isEmpty(csTypeQD)) {
            throw new DossierException("Unable to get list MembreFamilles, csTypeQD is empty !");
        }

        // Définir le nombre de mois que l'assuré a pour envoyer sa facture
        Float nbMoisLimite = null;
        boolean isFraisDeGarde = false;
        try {
            if (CSTypeQD.FRAIS_GARDE.getCodeSystem().equals(csTypeQD)) {
                nbMoisLimite = PerseusServiceLocator
                        .getVariableMetierService()
                        .getFromCS(CSVariableMetier.NB_MOIS_DELAIS_FACTURE_GARDE.getCodeSystem(),
                                Calendar.getInstance().getTime()).getMontant();
                isFraisDeGarde = true;
            } else {
                nbMoisLimite = PerseusServiceLocator
                        .getVariableMetierService()
                        .getFromCS(CSVariableMetier.NB_MOIS_DELAIS_FACTURE_MALADIE.getCodeSystem(),
                                Calendar.getInstance().getTime()).getMontant();
            }
        } catch (VariableMetierException e1) {
            throw new DossierException("Unable to load VariablesMetiers : " + e1.toString(), e1);
        } catch (JadeApplicationServiceNotAvailableException e1) {
            throw new DossierException("Unable to load VariablesMetiers : " + e1.toString(), e1);
        }
        // Définir la plus vieille date possible pour une date de facture selon la date d'envoie
        String dateLimiteDeFacture = JadeDateUtil.addMonths(dateReception, -nbMoisLimite.intValue());
        dateLimiteDeFacture = "01." + dateLimiteDeFacture.substring(3);

        String datePourCalculSelonContext = dateFacture;

        List<QD> listQD = new ArrayList<QD>();
        if (!JadeDateUtil.isDateBefore(dateFacture, dateLimiteDeFacture)) {

            // Si la date de prise en charge est renseigné, c est avec elle que l on fait les traitements sauf le nb de
            // mois. On fait un trim car on donne un espace due a ajax si la date n est pas renseigné.
            if (!JadeStringUtil.isEmpty(datePriseEnCharge.trim())) {
                datePourCalculSelonContext = datePriseEnCharge;
            }

            try {
                boolean hasValideDemande;
                if (isFraisDeGarde) {
                    hasValideDemande = hasValideOctroiPourFraisDeGarde(idDossier, datePourCalculSelonContext);
                } else {
                    hasValideDemande = hasValideOctroi(idDossier, datePourCalculSelonContext);
                }

                // Si il y'a des demandes, rechercher les QDs ouvertes pour cette période
                if (hasValideDemande) {
                    QDSearchModel qdSearchModel = new QDSearchModel();

                    Calendar c = Calendar.getInstance();
                    c.setTime(JadeDateUtil.getGlobazDate(datePourCalculSelonContext));
                    Integer year = c.get(Calendar.YEAR);
                    qdSearchModel.setForAnnee(year.toString());
                    qdSearchModel.setForIdDossier(idDossier);
                    qdSearchModel.setForCSTypeQD(csTypeQD);

                    qdSearchModel = PerseusServiceLocator.getQDService().search(qdSearchModel);
                    // Si il n'y a pas de qd pour l'année de la facture prendre l'année de la date de réception
                    if (qdSearchModel.getSize() == 0) {
                        Calendar cRec = Calendar.getInstance();
                        cRec.setTime(JadeDateUtil.getGlobazDate(dateReception));
                        Integer yearRec = cRec.get(Calendar.YEAR);
                        qdSearchModel.setForAnnee(yearRec.toString());
                        qdSearchModel = PerseusServiceLocator.getQDService().search(qdSearchModel);
                    }
                    for (JadeAbstractModel model : qdSearchModel.getSearchResults()) {
                        QD qd = (QD) model;

                        qd.setMontantLimite(qd.getMontantLimite());
                        qd.setMontantMaximalRemboursable(qd.getMontantMaximalRemboursable());
                        qd.setMontantUtilise(qd.getMontantUtilise());
                        qd.setMontantLimiteQdParente(qd.getQdParente().getSimpleQD().getMontantLimite());
                        qd.setMontantUtiliseQdParente(qd.getQdParente().getSimpleQD().getMontantUtilise());

                        // Si il s'agit de frais dentaires, ajouter seulement si l'enfant a moins de 18 ans
                        if (CSTypeQD.SQD_FRAIS_DENTAIRES_ASSURANCE_DENTAIRE.getCodeSystem().equals(
                                qd.getSimpleQD().getCsType())
                                || CSTypeQD.SQD_FRAIS_DENTAIRES_TRAITEMENT_ORTHODONTIQUE.getCodeSystem().equals(
                                        qd.getSimpleQD().getCsType())) {
                            String dateNaissance = qd.getMembreFamille().getPersonneEtendue().getPersonne()
                                    .getDateNaissance();
                            int ageEnfant = JadeDateUtil.getNbYearsBetween(dateNaissance, datePourCalculSelonContext);
                            // Si l'enfant à moins de 18 ans
                            if (ageEnfant < IPFConstantes.AGE_18ANS) {
                                listQD.add(qd);
                            }
                        } else {
                            listQD.add(qd);
                        }
                    }

                }
            } catch (DemandeException e) {
                throw new DossierException("DemandeException during getListMembreFamilles : " + e.getMessage(), e);
            } catch (DecisionException e) {
                throw new DossierException("DecisionException during getListMembreFamilles : " + e.getMessage(), e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DossierException("Service not available during getListMembreFamilles : " + e.getMessage(), e);
            } catch (QDException e) {
                throw new DossierException("QDException during getListMembreFamilles : " + e.getMessage(), e);
            }
        }
        return listQD;
    }

    @Override
    public QDSearchModel getListQDSearch(QDSearchModel searchModel) throws JadePersistenceException, DossierException {
        List<QD> list = this.getListQD(searchModel.getForIdDossier(), searchModel.getForDateFacture(),
                searchModel.getForDateReception(), searchModel.getForCSTypeQD());

        QD[] arrayQD = new QD[list.size()];
        int i = 0;

        for (QD qd : list) {
            arrayQD[i] = qd;
            i++;
        }
        searchModel.setSearchResults(arrayQD);

        return searchModel;
    }

    @Override
    public List<QD> getListQDSearchByParameters(String idDossier, String dateFacture, String dateReception,
            String csTypeQD, String datePriseEnCharge) throws JadePersistenceException, DossierException {

        return this.getListQD(idDossier, dateFacture, dateReception, csTypeQD, datePriseEnCharge);
    }

    private ArrayList<String> getMoisBetweenTwoDate(String dateDebut, String dateFin) {
        ArrayList<String> listMois = new ArrayList<String>();
        dateDebut = "01." + dateDebut;

        while (!JadeDateUtil.isDateMonthYearAfter(dateDebut.substring(3), dateFin)) {
            listMois.add(dateDebut.substring(3));
            dateDebut = JadeDateUtil.addMonths(dateDebut, 1);
        }

        return listMois;
    }

    private Float getMontantMenseulPCFA(DemandeEtendue demande) {
        Float montantMensuelPcfa = new Float(0);
        // Si il y'a une pcfaccordée si non c'est un refu donc on prend 0
        if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())) {
            montantMensuelPcfa = Float.parseFloat(demande.getSimplePcfAccordee().getMontant());
        }
        return montantMensuelPcfa;
    }

    @Override
    public Boolean hasDossier(String idTiers) throws JadePersistenceException, DossierException {
        DossierSearchModel searchModel = new DossierSearchModel();

        searchModel.setForIdTiers(idTiers);
        searchModel = search(searchModel);

        if (searchModel.getSize() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Permet de savoir si un octroi validé existe à une date
     * 
     * @param idDossier
     * @param date
     * @throws JadePersistenceException
     * @throws DossierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     * @throws DecisionException
     */
    private boolean hasValideOctroi(String idDossier, String date) throws JadePersistenceException, DossierException,
            DecisionException, JadeApplicationServiceNotAvailableException {
        boolean hasValideOctroi = false;
        List<String> typesDecision = new ArrayList<String>();
        typesDecision.add(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        typesDecision.add(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
        // BZ9028 - Rechercher les decisions validées à partir d'une date par ordre décroissant de validation.
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();

        decisionSearchModel.setForIdDossier(idDossier);
        decisionSearchModel.setForDateValable(date);
        decisionSearchModel.setForListCsTypes(typesDecision);
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setWhereKey(DemandeSearchModel.WITH_DATE_VALABLE_LE);
        decisionSearchModel.setOrderKey(DecisionSearchModel.ORDER_BY_DATETIME_DECISION_DESC);

        decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);

        if (decisionSearchModel.getSize() > 0) {
            // BZ9028 - Nous prenons que la dernière décision validée
            hasValideOctroi = true;
        }

        return hasValideOctroi;
    }

    /**
     * Permet de savoir si un octroi validé existe à une date
     * 
     * @param idDossier
     * @param date
     * @throws JadePersistenceException
     * @throws DossierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    private boolean hasValideOctroiPourFraisDeGarde(String idDossier, String date) throws JadePersistenceException,
            DossierException, DemandeException, JadeApplicationServiceNotAvailableException {

        String anneeDebut = JadeStringUtil.substring(date, 6);
        anneeDebut = "01.01." + anneeDebut;
        String anneeFin = JadeStringUtil.substring(date, 6);
        anneeFin = "31.12." + anneeFin;

        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();

        decisionSearchModel.setForIdDossier(idDossier);
        decisionSearchModel.setForDateDebut(anneeDebut);// Prendre année et add 01.01.xx
        decisionSearchModel.setForDateFin(anneeFin);// Prendre année et add 31.12.xx
        decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearchModel.setWhereKey(DecisionSearchModel.WITH_ANNEE_VALABLE);

        try {
            decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);
        } catch (DecisionException e) {
            e.printStackTrace();
        }

        String dateDeFinDecision = "";
        boolean isPasDeDateDeFin = false;

        if (decisionSearchModel.getSize() == 0) {
            DemandeSearchModel demandeSearchModel = new DemandeSearchModel();

            demandeSearchModel.setForIdDossier(idDossier);
            demandeSearchModel.setForCsTypeDemande(CSTypeDemande.PC_AVS_AI.getCodeSystem());
            demandeSearchModel.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            demandeSearchModel.setForDateDebut("01.01." + date.substring(6));
            demandeSearchModel.setForDateFin("31.12." + date.substring(6));
            demandeSearchModel.setWhereKey(DemandeSearchModel.WITH_ANNEE_VALABLE_FOR_ID_DOSSIER);
            demandeSearchModel.setOrderKey(DemandeSearchModel.ORDER_BY_DATE_DEBUT_DESC);

            demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);

            if (demandeSearchModel.getSize() > 0) {
                JadeAbstractModel monModel = demandeSearchModel.getSearchResults()[0];
                dateDeFinDecision = ((Demande) monModel).getSimpleDemande().getDateFin();

                if (JadeStringUtil.isEmpty(dateDeFinDecision)) {
                    isPasDeDateDeFin = true;
                }
            }
        }

        // check type = octroi ¦¦ octroi partiel
        for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
            Decision decision = (Decision) model;
            String csTypeDecision = decision.getSimpleDecision().getCsTypeDecision();

            if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(csTypeDecision)
                    || CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(csTypeDecision)) {

                if (JadeStringUtil.isEmpty(dateDeFinDecision)) {
                    dateDeFinDecision = decision.getDemande().getSimpleDemande().getDateFin();
                }

                if (JadeStringUtil.isEmpty(decision.getDemande().getSimpleDemande().getDateFin())) {
                    isPasDeDateDeFin = true;
                    break;
                } else if (JadeDateUtil.isDateAfter(decision.getDemande().getSimpleDemande().getDateFin(),
                        dateDeFinDecision)) {
                    dateDeFinDecision = decision.getDemande().getSimpleDemande().getDateFin();
                }
            }
        }

        if (isPasDeDateDeFin) {
            try {
                dateDeFinDecision = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            } catch (PaiementException e) {
                JadeLogger.error(this, e);
            }

            JACalendar CALENDAR = new JACalendarGregorian();
            int mois = Integer.parseInt(dateDeFinDecision.substring(0, 2));
            int annee = Integer.parseInt(dateDeFinDecision.substring(3, 7));
            int joursDansLeMois = CALENDAR.daysInMonth(mois, annee);

            dateDeFinDecision = String.valueOf(joursDansLeMois) + "." + dateDeFinDecision;
        }

        if (JadeDateUtil.isDateAfter(dateDeFinDecision, date) || dateDeFinDecision.equals(date)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDateComptabilisationLotDansAnneeAttestation(String dateComptabilisationLot,
            String anneeAttestation) {
        boolean isDateComptabilisationLotDansAnneeAttestation = false;
        if (dateComptabilisationLot.substring(6).equals(anneeAttestation)) {
            isDateComptabilisationLotDansAnneeAttestation = true;
        }
        return isDateComptabilisationLotDansAnneeAttestation;
    }

    private boolean isDemandeNonRefusSansCalculAndWithDateDecision(DemandeEtendue demande) {
        boolean isDemandeNonRefusSansCalculAndWithDateDecision = false;
        if (!JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getId())
                && !JadeStringUtil.isEmpty(demande.getSimplePcfAccordee().getDateDecision())) {
            isDemandeNonRefusSansCalculAndWithDateDecision = true;
        }

        return isDemandeNonRefusSansCalculAndWithDateDecision;
    }

    private boolean isPriseEnCompteDemandePourCalculerMontantVerseMenseul(DemandeEtendue demande, String mois) {
        boolean isPriseEnCompteDemandePourCalculerMontantVerseMenseul = false;
        if (!JadeDateUtil.isDateMonthYearBefore(mois, demande.getSimplePcfAccordee().getDateDecision())
                && (!JadeDateUtil.isDateMonthYearAfter(mois, demande.getSimplePcfAccordee().getDateDiminution()) || JadeStringUtil
                        .isEmpty(demande.getSimplePcfAccordee().getDateDiminution()))) {
            isPriseEnCompteDemandePourCalculerMontantVerseMenseul = true;
        }
        return isPriseEnCompteDemandePourCalculerMontantVerseMenseul;
    }

    private DemandeEtendueSearchModel loadDemandeEtendueValideOrderByDateTimeDecision(String idDossier)
            throws JadePersistenceException {
        DemandeEtendueSearchModel demandeEtendueSearchModel = new DemandeEtendueSearchModel();

        demandeEtendueSearchModel.setForIdDossier(idDossier);
        demandeEtendueSearchModel.setForCsEtat(CSEtatDemande.VALIDE.getCodeSystem());
        demandeEtendueSearchModel.setOrderKey(DemandeEtendueSearchModel.ORDER_BY_DATETIME_DECISION);

        return demandeEtendueSearchModel = (DemandeEtendueSearchModel) JadePersistenceManager
                .search(demandeEtendueSearchModel);
    }

    private PrestationSearchModel loadPrestationDecisionValideeForIdDossier(String idDossier) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PrestationSearchModel prestationSearchModel = new PrestationSearchModel();

        prestationSearchModel.setForIdDossier(idDossier);
        prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
        prestationSearchModel.setForEtatLot(CSEtatLot.LOT_VALIDE.getCodeSystem());
        prestationSearchModel.setModelClass(Prestation.class);

        return prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
    }

    private PrestationSearchModel loadPrestationDecisionValideeForIdDossier(String idDossier, String annee)
            throws LotException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PrestationSearchModel prestationSearchModel = new PrestationSearchModel();

        prestationSearchModel.setForIdDossier(idDossier);
        prestationSearchModel.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
        prestationSearchModel.setForEtatLot(CSEtatLot.LOT_VALIDE.getCodeSystem());
        prestationSearchModel.setBetweenDateComptabilisationDebut("01.01." + annee);
        prestationSearchModel.setBetweenDateComptabilisationFin("31.12." + annee);
        prestationSearchModel.setModelClass(Prestation.class);

        return prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
    }

    @Override
    public Dossier read(String idDossier) throws JadePersistenceException, DossierException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to read a dossier, the id passed is null!");
        }
        Dossier dossier = new Dossier();
        dossier.setId(idDossier);
        return (Dossier) JadePersistenceManager.read(dossier);
    }

    @Override
    public DossierSearchModel search(DossierSearchModel searchModel) throws JadePersistenceException, DossierException {
        if (searchModel == null) {
            throw new DossierException("Unable to search a dossier, the search model passed is null!");
        }
        return (DossierSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public List<Dossier> searchDossierForTiers(String idTiers, boolean rentePont) throws DossierException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idTiers)) {
            throw new DossierException("Unable to search dossier, idTiers is empty !");
        }
        List<Dossier> listDossier = new ArrayList<Dossier>();

        StringBuffer sb = new StringBuffer();
        if (rentePont) {
            sb.append("SELECT DISTINCT JBIDOS as IDDOSSIER FROM " + JadePersistenceUtil.getDbSchema() + ".PFREPONT ");
            sb.append("WHERE JBIDSF in ( ");
        } else {
            sb.append("SELECT DISTINCT IBIDOS as IDDOSSIER FROM " + JadePersistenceUtil.getDbSchema() + ".PFDEMPCF ");
            sb.append("WHERE IBIDSF in ( ");
        }
        sb.append("	SELECT IDIDSF FROM " + JadePersistenceUtil.getDbSchema() + ".PFSITFAM ");
        sb.append("	WHERE IDIDRE in (SELECT IHIDRE FROM " + JadePersistenceUtil.getDbSchema() + ".PFMEMFAM INNER JOIN "
                + JadePersistenceUtil.getDbSchema() + ".PFREQUER ON IHIDMF = IEIDMF WHERE IEIDTI = " + idTiers + ") ");
        sb.append("	OR IDIDCO in (SELECT IGIDCO FROM " + JadePersistenceUtil.getDbSchema() + ".PFMEMFAM INNER JOIN "
                + JadePersistenceUtil.getDbSchema() + ".PFCONJOI ON IGIDMF = IEIDMF WHERE IEIDTI = " + idTiers + ") ");
        sb.append("	OR IDIDSF in (SELECT IJIDSF FROM " + JadePersistenceUtil.getDbSchema() + ".PFENFFAM ");
        sb.append("					WHERE IJIDEN in (SELECT IFIDEN  FROM " + JadePersistenceUtil.getDbSchema()
                + ".PFMEMFAM INNER JOIN " + JadePersistenceUtil.getDbSchema()
                + ".PFENFANT ON IFIDMF = IEIDMF WHERE IEIDTI = " + idTiers + ") ");
        sb.append("				) ");
        sb.append(")");
        System.out.println(sb.toString());

        List<String> listIdDossier = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            rs = stmt.executeQuery(sb.toString());
            while (rs.next()) {
                listIdDossier.add(Integer.toString(rs.getInt("IDDOSSIER")));
            }
        } catch (SQLException e) {
            throw new DossierException("SQLException during searchDossierForTiers : " + e.toString(), e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(this, "Unable to close statement, reason :" + e.toString());
                }
            }
        }

        if (listIdDossier.size() > 0) {
            try {
                DossierSearchModel dsm = new DossierSearchModel();
                dsm.setInIdDossier(listIdDossier);
                dsm = PerseusServiceLocator.getDossierService().search(dsm);
                for (JadeAbstractModel model : dsm.getSearchResults()) {
                    listDossier.add((Dossier) model);
                }
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new DossierException("Service not available during searchDossierForTiers : " + e.toString(), e);
            }
        }

        return listDossier;
    }

    @Override
    public Dossier update(Dossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to update dossier, the given model is null!");
        }

        try {
            DossierChecker.checkForUpdate(dossier);
            PerseusImplServiceLocator.getSimpleDossierService().update(dossier.getDossier());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Service not available - " + e.getMessage());
        }

        return dossier;
    }

}
