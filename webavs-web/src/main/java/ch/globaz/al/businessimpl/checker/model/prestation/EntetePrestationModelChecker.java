package ch.globaz.al.businessimpl.checker.model.prestation;

import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.exceptions.model.prestation.ALEntetePrestationModelException;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALImportUtils;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * contrôle la validité des données de EntetePrestation
 * 
 * @author PTA
 * 
 */
public abstract class EntetePrestationModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données de l'en-tête
     * 
     * @param entetePrest
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(EntetePrestationModel entetePrest) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification de l'existence du dossier
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdDossier(entetePrest.getIdDossier());
        if (0 == ALImplServiceLocator.getDossierFkModelService().count(sd)) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idDossier.businessIntegrity.ExistingId");
        }

        // l'id de la récap est obligatoire sauf pour les ADI
        if (JadeNumericUtil.isEmptyOrZero(entetePrest.getIdRecap())
                && !ALCSPrestation.STATUT_ADI.equals(entetePrest.getStatut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idRecap.businessIntegrity.mandatoryIfNotAdi");
        }

        // vérification de l'existence de la récap
        RecapitulatifEntrepriseSearchModel sre = new RecapitulatifEntrepriseSearchModel();
        sre.setForIdRecap(entetePrest.getIdRecap());
        if (!JadeNumericUtil.isEmptyOrZero(entetePrest.getIdRecap())
                && (0 == ALServiceLocator.getRecapitulatifEntrepriseModelService().count(sre))) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idRecap.businessIntegrity.ExistingId");
        }

        // TODO (lot 2) Réactiver la méthode quand la liste des caisses AF sera
        // OK
        // vérification de l'existence de idTiers
        // PersonneEtendueSearchComplexModel ts = new
        // PersonneEtendueSearchComplexModel();
        // ts.setForIdTiers(enTetePrest.getIdTiersCaisseAF());
        // if (0 ==
        // TIBusinessServiceLocator.getPersonneEtendueService().count(ts)) {
        // JadeThread
        // .logError(
        // EntetePrestationModelChecker.class.getName(),
        // "al.prestation.entetePrestationModel.idTierCaisseAF.businessIntegrity.ExistingId");
        // }

        if (JadeDateUtil.isDateMonthYearBefore(entetePrest.getPeriodeA(), entetePrest.getPeriodeDe())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.periode.businessIntegrity.chronology");

        }

        // la date de versement/compensation est obligatoire si la prestation a
        // l'état CO
        if (ALCSPrestation.ETAT_CO.equals(entetePrest.getEtatPrestation())
                && JadeStringUtil.isBlankOrZero(entetePrest.getDateVersComp())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.etatPrestation.comptabiliseDateMandatory");
        }

        // le numéro de journal ou de passage de facturation est obligatoire si
        // la prestation est comptabilisée
        if (ALCSPrestation.ETAT_CO.equals(entetePrest.getEtatPrestation())
                && JadeNumericUtil.isEmptyOrZero(entetePrest.getIdJournal())
                && JadeNumericUtil.isEmptyOrZero(entetePrest.getIdPassage())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.etatPrestation.comptabiliseIdJournalMandatory");
        }
    }

    /**
     * vérifie l'intégrité des codes système
     * 
     * @param entetePrest
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(EntetePrestationModel entetePrest) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // état de la prestation
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_ETAT, entetePrest.getEtatPrestation())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.etatPrestation.codesystemIntegrity");
            }

            // unité
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_UNITE_CALCUL, entetePrest.getUnite())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.unite.codesystemIntegrity");
            }

            // type de génération
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_GENERATION_TYPE,
                    entetePrest.getTypeGeneration())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.typeGeneration.codesystemIntegrity");
            }

            // canton de l'affilié
            if (!JadeStringUtil.isEmpty(entetePrest.getCantonAffilie())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSCantons.GROUP_CANTONS,
                            entetePrest.getCantonAffilie())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.cantonAffilie.codesystemIntegrity");
            }

            // bonification
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_BONI, entetePrest.getBonification())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.bonification.codesystemIntegrity");
            }

            // statut
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSPrestation.GROUP_STATUT, entetePrest.getStatut())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.statut.codesystemIntegrity");
            }

        } catch (Exception e) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationModelChecker#checkCodesystemIntegrity : problem during checking codes system integrity",
                    e);
        }

    }

    /**
     * vérifie l'intégrité des données, si non respectée lance un message sur l'intégrité
     * 
     * @param entetePrest
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(EntetePrestationModel entetePrest) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // type de génération
        if (!JadeNumericUtil.isIntegerPositif(entetePrest.getTypeGeneration())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.typeGeneration.databaseIntegrity.type");
        }

        // taux d'allocation
        if (!JadeNumericUtil.isNumericPositif(entetePrest.getTauxVersement())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.tauxVersement.databaseIntegrity.type");
        }

        // id dossier
        if (!JadeNumericUtil.isIntegerPositif(entetePrest.getIdDossier())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idDossier.databaseIntegrity.type");
        }

        // canton de l'affilié
        if (!JadeStringUtil.isBlankOrZero(entetePrest.getCantonAffilie())
                && !JadeNumericUtil.isIntegerPositif(entetePrest.getCantonAffilie())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.cantonAffilie.databaseIntegrity.type");
        }

        // id de la récap
        if (!JadeNumericUtil.isInteger(entetePrest.getIdRecap())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idRecap.databaseIntegrity.type");
        }

        // id de la coti
        if (!JadeStringUtil.isEmpty(entetePrest.getIdCotisation())
                && !JadeNumericUtil.isInteger(entetePrest.getIdCotisation())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idCotisation.databaseIntegrity.type");
        }

        // id du tiers
        // TODO (lot 2) Réactiver la méthode quand la liste des caisses AF sera
        // if (!JadeNumericUtil.isIntegerPositif(entetePrest.getIdTiersCaisseAF())) {
        // JadeThread.logError(EntetePrestationModelChecker.class.getName(),
        // "al.prestation.entetePrestationModel.idTiersCaisseAF.databaseIntegrity.type");
        // }

        // état de la prestation
        if (!JadeNumericUtil.isIntegerPositif(entetePrest.getEtatPrestation())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.etatPrestation.databaseIntegrity.type");
        }

        // unité
        if (!JadeNumericUtil.isIntegerPositif(entetePrest.getUnite())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.unite.databaseIntegrity.type");
        }

        // nombre d'unité
        if (!JadeNumericUtil.isZeroValue(entetePrest.getNombreUnite())
                && !JadeNumericUtil.isNumericPositif(entetePrest.getNombreUnite())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.nombreUnite.databaseIntegrity.type");
        }

        // Nombre d'enfant
        if (!JadeStringUtil.isBlankOrZero(entetePrest.getNombreEnfants())
                && !JadeNumericUtil.isInteger(entetePrest.getNombreEnfants())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.nombreEnfant.databaseIntegrity.type");
        }

        // numéro de passage
        if (!JadeNumericUtil.isEmptyOrZero(entetePrest.getNumPsgGeneration())
                && !JadeNumericUtil.isIntegerPositif(entetePrest.getNumPsgGeneration())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.numPsgGeneration.databaseIntegrity.type");
        }

        // montant total
        if (!JadeNumericUtil.isNumeric(entetePrest.getMontantTotal())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.montantTotal.databaseIntegrity.type");
        }

        // date versement
        if (!JadeStringUtil.isBlankOrZero(entetePrest.getDateVersComp())
                && !JadeDateUtil.isGlobazDate(entetePrest.getDateVersComp())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.dateVersComp.databaseIntegrity.dateFormat");

        }

        // période de début
        if (!JadeDateUtil.isGlobazDateMonthYear(entetePrest.getPeriodeDe())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.periodeDe.databaseIntegrity.dateFormat");
        }

        // période de fin
        if (!JadeDateUtil.isGlobazDateMonthYear(entetePrest.getPeriodeA())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.periodeA.databaseIntegrity.dateFormat");
        }

        // jour début mutation
        if (!JadeNumericUtil.isEmptyOrZero(entetePrest.getJourDebutMut())
                && !JadeDateUtil.isGlobazDateDay(entetePrest.getJourDebutMut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.jourDebutMut.databaseIntegrity.dateFormat");
        }

        // jour fin mutation
        if (!JadeNumericUtil.isEmptyOrZero(entetePrest.getJourFinMut())
                && !JadeDateUtil.isGlobazDateDay(entetePrest.getJourFinMut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.jourFinMut.databaseIntegrity.dateFormat");
        }

        // mois début mutation
        if (!JadeNumericUtil.isEmptyOrZero(entetePrest.getMoisDebutMut())
                && !JadeDateUtil.isGlobazDateMonth(entetePrest.getMoisDebutMut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.moisDebutMut.databaseIntegrity.dateFormat");
        }

        // mois fin mutation
        if (!JadeNumericUtil.isEmptyOrZero(entetePrest.getJourFinMut())
                && !JadeDateUtil.isGlobazDateMonth(entetePrest.getMoisFinMut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.moisFinMut.databaseIntegrity.dateFormat");
        }

        // bonification
        if (!JadeNumericUtil.isIntegerPositif(entetePrest.getBonification())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.bonification.databaseIntegrity.type");
        }

        // statut
        if (!JadeNumericUtil.isIntegerPositif(entetePrest.getStatut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.statut.databaseIntegrity.type");
        }
    }

    /**
     * Effectue les vérifications
     * 
     * @param entetePrestationModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void  checkDeleteIntegrity(EntetePrestationModel entetePrestationModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // On ne peut pas supprimer des prestations en état CO ou TR
        if (ALCSPrestation.ETAT_CO.equals(entetePrestationModel.getEtatPrestation())
                || ALCSPrestation.ETAT_TR.equals(entetePrestationModel.getEtatPrestation())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idEntete.deleteIntegrity.etat");
        }
        // si on est dans une horlogère et que la récap est déjà liée, on peut pas supprimer cette entête de
        // prestation
        if (!JadeStringUtil.isBlankOrZero(entetePrestationModel.getIdRecap())) {
            if (ALServiceLocator.getRecapitulatifEntrepriseBusinessService().isRecapVerouillee(
                    ALServiceLocator.getRecapitulatifEntrepriseModelService().read(entetePrestationModel.getIdRecap()))) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.recapEntrepriseModel.etat.businessIntegrity.verrouille");
            }
        }

        // Vérifie si les annonces des prestations ADI ont été envoyées
        if (ALCSPrestation.STATUT_ADI.equals(entetePrestationModel.getStatut())
            && !ALCSPrestation.ETAT_TMP.equals(entetePrestationModel.getEtatPrestation())) {
            DetailPrestationSearchModel search = new DetailPrestationSearchModel();
            search.setForIdEntetePrestation(entetePrestationModel.getIdEntete());
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
                // Si une annonce est à l'état A_TRANSMETTRE pour le droit alors l'annonce n'a pas été envoyée
                boolean aTransmettre = false;
                for(JadeAbstractModel abstractModel : rafamModel.getSearchResults()) {
                    AnnonceRafamModel annonce = (AnnonceRafamModel) abstractModel;
                    if(hasPrestationForAnnonce(search, annonce)) {
                        aTransmettre = true;
                    }
                }
                if(!aTransmettre) {
                    JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                            "al.prestation.entetePrestationModel.idEntete.deleteIntegrity.adi.rafam.envoyee");
                }
            }
        }
    }

    private static boolean hasPrestationForAnnonce(DetailPrestationSearchModel search, AnnonceRafamModel annonce) {
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

    /**
     * vérifie l'obligation des données si non respectée lance un message sur l'obligation
     * 
     * @param entetePrest
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(EntetePrestationModel entetePrest) throws JadeApplicationException,
            JadePersistenceException {

        // id du dossier
        if (JadeStringUtil.isEmpty(entetePrest.getIdDossier())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idDossier.mandatory");
        }

        // canton de l'affilié
        if (!ALImportUtils.importFromAlfaGest) {
            if (JadeStringUtil.isEmpty(entetePrest.getCantonAffilie())) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.entetePrestationModel.cantonAffilie.mandatory");
            }
        }

        // id de la récap
        if (JadeStringUtil.isEmpty(entetePrest.getIdRecap())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.idRecap.mandatory");
        }

        // id tiers de la caisse AF
        // TODO (lot 2) Réactiver la méthode quand la liste des caisses AF sera
        // if (JadeStringUtil.isEmpty(entetePrest.getIdTiersCaisseAF())) {
        // JadeThread.logError(EntetePrestationModelChecker.class.getName(),
        // "al.prestation.entetePrestationModel.idTiersCaisseAF.mandatory");
        // }

        // état de la prestation
        if (JadeStringUtil.isEmpty(entetePrest.getEtatPrestation())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.etatPrestation.mandatory");
        }

        // unité
        if (JadeStringUtil.isEmpty(entetePrest.getUnite())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.unite.mandatory");
        }

        // nombre d'unité
        if (JadeStringUtil.isEmpty(entetePrest.getNombreUnite())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.nombreUnite.mandatory");
        }

        // // nombre d'enfant
        // if (JadeStringUtil.isEmpty(entetePrest.getNombreEnfants())) {
        //
        // JadeThread
        // .logError(EntetePrestationModelChecker.class.getName(),
        // "al.prestation.entetePrestationModel.nombreEnfant.mandatory");
        // }

        // montant total
        if (JadeStringUtil.isEmpty(entetePrest.getMontantTotal())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.montantTotal.mandatory");
        }

        // type de génération
        if (JadeStringUtil.isEmpty(entetePrest.getTypeGeneration())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.typeGeneration.mandatory");
        }

        // bonification
        if (JadeStringUtil.isEmpty(entetePrest.getBonification())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.bonification.mandatory");
        }

        // statut
        if (JadeStringUtil.isEmpty(entetePrest.getStatut())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.statut.mandatory");
        }

        // taux de versement de l'allocation
        if (JadeStringUtil.isEmpty(entetePrest.getTauxVersement())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.tauxVersement.mandatory");
        }

        // Période de début
        if (JadeStringUtil.isEmpty(entetePrest.getPeriodeDe())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.periodeDe.mandatory");
        }

        // Période de fin
        if (JadeStringUtil.isEmpty(entetePrest.getPeriodeA())) {
            JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                    "al.prestation.entetePrestationModel.periodeA.mandatory");
        }
    }

    /**
     * valide l'intégité et l'obligation des données
     * 
     * @param enTetePrest
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(EntetePrestationModel enTetePrest) throws JadeApplicationException,
            JadePersistenceException {
        EntetePrestationModelChecker.checkMandatory(enTetePrest);
        EntetePrestationModelChecker.checkDatabaseIntegrity(enTetePrest);
        EntetePrestationModelChecker.checkCodesystemIntegrity(enTetePrest);
        EntetePrestationModelChecker.checkBusinessIntegrity(enTetePrest);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param entetePrestationModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validateForDelete(EntetePrestationModel entetePrestationModel) throws JadePersistenceException,
            JadeApplicationException {
        EntetePrestationModelChecker.checkDeleteIntegrity(entetePrestationModel);
    }

}
