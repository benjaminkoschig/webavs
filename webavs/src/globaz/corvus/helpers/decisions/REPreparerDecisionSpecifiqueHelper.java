package globaz.corvus.helpers.decisions;

import globaz.corvus.api.basescalcul.IRERenteAccordee;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1AManager;
import globaz.corvus.exceptions.REBusinessException;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.helpers.process.REValiderDecisionHandler;
import globaz.corvus.process.REDiminutionRenteAccordeeProcess;
import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.decisions.REPreparerDecisionSpecifiqueViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.PersonneAVS;

public abstract class REPreparerDecisionSpecifiqueHelper extends PRHybridHelper {

    static String dateDeDiminutionDeLaRente(final RenteAccordee renteADiminuer,
            final DemandeRente nouvelleDemandeDontLeDroitSupplenteLaRenteADiminuer) {

        String plusPetitMoisDesRentesAccordees = Periode.MOIS_MAX;

        Set<RenteAccordee> rentesDuBeneficiaireDansLaNouvelleDemande = REPreparerDecisionSpecifiqueHelper
                .filtrerParBeneficiaires(nouvelleDemandeDontLeDroitSupplenteLaRenteADiminuer.getRentesAccordees(),
                        new HashSet<PersonneAVS>(Arrays.asList(renteADiminuer.getBeneficiaire())));

        if (nouvelleDemandeDontLeDroitSupplenteLaRenteADiminuer
                .comporteUnTrouDansLesPeriodesDeDroitDesRentesAccordees()
                && (rentesDuBeneficiaireDansLaNouvelleDemande.size() > 1)) {

            SortedSet<RenteAccordee> rentesTrieesParDateDeFin = new TreeSet<RenteAccordee>(
                    new Comparator<RenteAccordee>() {
                        @Override
                        public int compare(final RenteAccordee rente1, final RenteAccordee rente2) {
                            return rente2.getPeriodeDuDroit().compareTo(rente1.getPeriodeDuDroit());
                        }
                    });
            rentesTrieesParDateDeFin.addAll(rentesDuBeneficiaireDansLaNouvelleDemande);

            Iterator<RenteAccordee> iterator = rentesTrieesParDateDeFin.iterator();
            RenteAccordee rentePlusRecente = iterator.next();
            while (iterator.hasNext()) {
                RenteAccordee rentePlusAncienne = iterator.next();

                String moisPrecedantLeDebutDeLaRentePlusRecente = JadeDateUtil.convertDateMonthYear(JadeDateUtil
                        .addMonths("01." + rentePlusRecente.getMoisDebut(), -1));
                if (!moisPrecedantLeDebutDeLaRentePlusRecente.equals(rentePlusAncienne.getMoisFin())) {
                    plusPetitMoisDesRentesAccordees = moisPrecedantLeDebutDeLaRentePlusRecente;
                    break;
                }

                rentePlusRecente = rentePlusAncienne;

            }
        } else {
            for (RenteAccordee uneRenteAccordee : rentesDuBeneficiaireDansLaNouvelleDemande) {
                if (uneRenteAccordee.getBeneficiaire().equals(renteADiminuer.getBeneficiaire())
                        && JadeDateUtil.isDateMonthYearAfter(plusPetitMoisDesRentesAccordees,
                                uneRenteAccordee.getMoisDebut())) {
                    plusPetitMoisDesRentesAccordees = uneRenteAccordee.getMoisDebut();
                }
            }

            plusPetitMoisDesRentesAccordees = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths("01."
                    + plusPetitMoisDesRentesAccordees, -1));

        }

        if (plusPetitMoisDesRentesAccordees.equals(Periode.MOIS_MAX)) {
            throw new IllegalArgumentException("Unable to find an ending month for this prestation : "
                    + renteADiminuer.toString());
        }

        return plusPetitMoisDesRentesAccordees;
    }

    private static Set<RenteAccordee> filtrerParBeneficiaires(final Collection<RenteAccordee> rentesAccordees,
            final Set<PersonneAVS> beneficiaires) {

        Set<RenteAccordee> rentesDesBeneficiaires = new HashSet<RenteAccordee>();

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            if (beneficiaires.contains(uneRenteAccordee.getBeneficiaire())) {
                rentesDesBeneficiaires.add(uneRenteAccordee);
            }
        }

        return rentesDesBeneficiaires;
    }

    private static Set<RenteAccordee> retirerLesRentesDeLaDemande(final Set<RenteAccordee> rentesAccordees,
            final DemandeRente demande) {
        Set<RenteAccordee> rentesHorsDeLaDemande = new HashSet<RenteAccordee>();

        for (RenteAccordee uneRenteAccordee : rentesAccordees) {
            if (!demande.getRentesAccordees().contains(uneRenteAccordee)) {
                rentesHorsDeLaDemande.add(uneRenteAccordee);
            }
        }

        return rentesHorsDeLaDemande;
    }

    public static void verifierQuAucuneRenteDesBeneficiairesDeLaDemandeNeSoitBloquee(final DemandeRente demande)
            throws REBusinessException {

        Set<PersonneAVS> beneficiairesDeLaDemande = demande.getBeneficiairesDeLaDemande();

        Set<RenteAccordee> rentesEnCoursDesBeneficiaires = REPreparerDecisionSpecifiqueHelper.filtrerParBeneficiaires(
                CorvusServiceLocator.getRenteAccordeeService()
                        .rentesAccordeesEnCoursDeLaFamille(demande.getRequerant()), beneficiairesDeLaDemande);

        for (RenteAccordee uneRenteDesBeneficiairesDeLaDemande : REPreparerDecisionSpecifiqueHelper
                .retirerLesRentesDeLaDemande(rentesEnCoursDesBeneficiaires, demande)) {
            if (uneRenteDesBeneficiairesDeLaDemande.estBloquee()
                    && uneRenteDesBeneficiairesDeLaDemande.comporteUnMontantBloque()) {
                throw new REBusinessException(BSessionUtil.getSessionFromThreadContext().getLabel(
                        "ERREUR_PREPARATION_DECISION_AVEC_RENTES_BLOQUEES_IMPOSSIBLE"));
            }
        }
    }

    @Override
    protected final void _start(final FWViewBeanInterface viewBean, final FWAction action, final BISession session) {

        REPreparerDecisionSpecifiqueViewBean preparerDecisionViewBean = (REPreparerDecisionSpecifiqueViewBean) viewBean;

        try {
            DemandeRente demande = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                    preparerDecisionViewBean.getIdDemandeRente());

            REPreparerDecisionSpecifiqueHelper.verifierQuAucuneRenteDesBeneficiairesDeLaDemandeNeSoitBloquee(demande);
            if (REProperties.COPIE_POUR_AGENCE_COMMUNALE.getBooleanValue()) {
                verifierQueLAgenceCommunaleSoitDefiniePourLeTiers(demande);
            }

            /**
             * ATTENTION il est important de d'abord diminuer avant de valider de nouvelle
             */
            diminuerLesAutresRentesSiBesoin(demande, preparerDecisionViewBean.getAdresseEmailGestionnaire());
            validerLaDemandeEtLesRentes(demande);
            creerOuModifierLesAnnonces(demande);

            actionSpecifiqueSupplementaire(preparerDecisionViewBean, demande);

        } catch (Exception ex) {
            JadeThread.logError(this.getClass().getName(), ex.toString());
            viewBean.setMessage(ex.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            throw new RETechnicalException(ex);
        }
    }

    protected abstract void actionSpecifiqueSupplementaire(REPreparerDecisionSpecifiqueViewBean viewBean,
            DemandeRente demande);

    private void creerOuModifierLesAnnonces(final DemandeRente demande) throws Exception {
        String moisRapport = REPmtMensuel.getDateDernierPmt(BSessionUtil.getSessionFromThreadContext());
        Periode periodeDroitDemande = demande.getPeriodeDuDroitDesRentesAccordees();

        // si le début des rentes accordées est dans le futur, on ne prend pas le mois comptable comme mois de rapport
        // mais le début des rentes accordées
        if (JadeDateUtil.isDateMonthYearAfter(periodeDroitDemande.getDateDebut(), moisRapport)) {
            moisRapport = periodeDroitDemande.getDateDebut();
        }

        // les rentes pour lesquelles une annonce est nécessaire
        Map<Long, RenteAccordee> rentesATraiteesParId = new HashMap<Long, RenteAccordee>();
        for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
            rentesATraiteesParId.put(uneRenteDeLaDemande.getId(), uneRenteDeLaDemande);
        }

        REAnnoncesAbstractLevel1AManager manager = new REAnnoncesAbstractLevel1AManager();
        manager.setForIdsRenteAccordeeIn(rentesATraiteesParId.keySet());
        manager.find(BManager.SIZE_NOLIMIT);

        // on vérifie la présente des annonces pour chaque rente accordée
        // si l'annonce est présente, on assigne le mois de rapport de l'annonce au mois comptable actuel
        for (REAnnoncesAbstractLevel1A uneAnnonce : manager.getContainerAsList()) {
            Long idRenteSurAnnonce = Long.parseLong(uneAnnonce.getIdRenteAccordee());

            uneAnnonce.setMoisRapport(moisRapport);
            uneAnnonce.update();

            // rente traitée, on la retire de la liste
            rentesATraiteesParId.remove(idRenteSurAnnonce);
        }

        // si l'annonce est absente, on la créer
        for (RenteAccordee uneRenteSansAnnonce : rentesATraiteesParId.values()) {
            String moisRapportPourCetteRente = moisRapport;

            // selon spec du mandat D0095 : mois de rapport = mois comptable sauf si la date de début de la rente est
            // plus grande que le mois comptable (commence dans le futur)
            if (JadeDateUtil.isDateMonthYearBefore(moisRapportPourCetteRente, uneRenteSansAnnonce.getMoisDebut())) {
                moisRapportPourCetteRente = uneRenteSansAnnonce.getMoisDebut();
            }

            REValiderDecisionHandler.createAnnonce4x(BSessionUtil.getSessionFromThreadContext(), BSessionUtil
                    .getSessionFromThreadContext().getCurrentThreadTransaction(), uneRenteSansAnnonce.getId(),
                    moisRapportPourCetteRente);
        }

    }

    private void diminuerLesAutresRentesSiBesoin(final DemandeRente demande, final String emailGestionnaire)
            throws Exception {
        for (RenteAccordee uneRenteDevantEtreDiminuee : CorvusServiceLocator.getRenteAccordeeService()
                .rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(demande)) {

            REDiminutionRenteAccordeeProcess diminutionRenteAccordeeProcess = new REDiminutionRenteAccordeeProcess();

            diminutionRenteAccordeeProcess.setTransaction(BSessionUtil.getSessionFromThreadContext()
                    .getCurrentThreadTransaction());
            diminutionRenteAccordeeProcess.setNoCommit(true);
            diminutionRenteAccordeeProcess.setEMailAddress(emailGestionnaire);

            diminutionRenteAccordeeProcess
                    .setCsCodeMutation(IRERenteAccordee.CS_CODE_MUTATION_EVENEMENT_TOUCHANT_PROCHE);
            diminutionRenteAccordeeProcess.setCsCodeTraitement(null);
            diminutionRenteAccordeeProcess.setDateFinDroit(REPreparerDecisionSpecifiqueHelper
                    .dateDeDiminutionDeLaRente(uneRenteDevantEtreDiminuee, demande));
            diminutionRenteAccordeeProcess.setIdRenteAccordee(uneRenteDevantEtreDiminuee.getId().toString());

            diminutionRenteAccordeeProcess.run();
        }
    }

    private void validerLaDemandeEtLesRentes(final DemandeRente demande)
            throws JadeApplicationServiceNotAvailableException {
        switch (demande.getEtat()) {

            case ENREGISTRE:
            case AU_CALCUL:
            case CALCULE:
                demande.setEtat(EtatDemandeRente.VALIDE);
                CorvusCrudServiceLocator.getDemandeRenteCrudService().update(demande);
                break;

            default:
                throw new IllegalArgumentException("Impossible to prepare a decision with a request in this state : "
                        + demande.getEtat());

        }

        for (RenteAccordee uneRenteDeLaDemande : demande.getRentesAccordees()) {
            uneRenteDeLaDemande.setEtat(EtatPrestationAccordee.VALIDE);
            CorvusCrudServiceLocator.getRenteAccordeeCrudService().update(uneRenteDeLaDemande);
        }
    }

    private void verifierQueLAgenceCommunaleSoitDefiniePourLeTiers(final DemandeRente demande) throws Exception {

        String idTierAdmin = null;
        TICompositionTiersManager compTiersMgr = new TICompositionTiersManager();
        compTiersMgr.setForIdTiersParent(demande.getRequerant().getId().toString());
        compTiersMgr.setForTypeLien("507007");
        compTiersMgr.setSession(BSessionUtil.getSessionFromThreadContext());

        compTiersMgr.find();

        for (int i = 0; i < compTiersMgr.size(); i++) {
            TICompositionTiers entity = (TICompositionTiers) compTiersMgr.get(i);

            TIAdministrationViewBean administrationCommunale = new TIAdministrationViewBean();
            administrationCommunale.setSession(BSessionUtil.getSessionFromThreadContext());
            administrationCommunale.setIdTiersAdministration(entity.getIdTiersEnfant());
            administrationCommunale.retrieve();

            if (!administrationCommunale.isNew()) {
                idTierAdmin = entity.getIdTiersEnfant();
            }
        }
        // Si on ne trouve pas l'admin, ce n'est pas normal ==> Exception
        if ((idTierAdmin == null)
                || (PRTiersHelper.getAdministrationParId(BSessionUtil.getSessionFromThreadContext(), idTierAdmin) == null)) {
            throw new RETechnicalException(FWMessageFormat.format(
                    BSessionUtil.getSessionFromThreadContext().getLabel("WARNING_AGENCE_COMM"), idTierAdmin));
        }
    }
}
