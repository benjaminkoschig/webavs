package ch.globaz.corvus.businessimpl.services.models.demande;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSessionUtil;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteCrudService;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteService;
import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteAPI;
import ch.globaz.corvus.domaine.DemandeRenteInvalidite;
import ch.globaz.corvus.domaine.DemandeRenteSurvivant;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.EtatDemandeRente;
import ch.globaz.pyxis.domaine.PersonneAVS;

/**
 * Implementation du service d'utilitaire pour les demandes de rente
 */
public class DemandeRenteServiceImpl implements DemandeRenteService {

    @Override
    public DemandeRente copier(final DemandeRente demande) {

        Checkers.checkNotNull(demande, "demande");

        DemandeRente copie;

        switch (demande.getTypeDemandeRente()) {
            case DEMANDE_API:
                DemandeRenteAPI copieAPI = new DemandeRenteAPI();

                DemandeRenteAPI demandeAPI = (DemandeRenteAPI) demande;
                copieAPI.setAtteinte(demandeAPI.getAtteinte());
                copieAPI.setAvecMotivation(demandeAPI.isAvecMotivation());
                copieAPI.setDateSuvenanceEvenementAssure(demandeAPI.getDateSuvenanceEvenementAssure());
                copieAPI.setInfirmite(demandeAPI.getInfirmite());
                copieAPI.setOfficeAI(demandeAPI.getOfficeAI());

                copie = copieAPI;

                break;
            case DEMANDE_INVALIDITE:
                DemandeRenteInvalidite copieInvalidite = new DemandeRenteInvalidite();

                DemandeRenteInvalidite demandeInvalidite = (DemandeRenteInvalidite) demande;
                copieInvalidite.setAtteinte(demandeInvalidite.getAtteinte());
                copieInvalidite.setAvecMotivation(demandeInvalidite.isAvecMotivation());
                copieInvalidite.setDateSuvenanceEvenementAssure(demandeInvalidite.getDateSuvenanceEvenementAssure());
                copieInvalidite.setInfirmite(demandeInvalidite.getInfirmite());
                copieInvalidite.setMoisDebutReductionPourNonCollaboration(demandeInvalidite
                        .getMoisDebutReductionPourNonCollaboration());
                copieInvalidite.setMoisFinReductionPourNonCollaboration(demandeInvalidite
                        .getMoisFinReductionPourNonCollaboration());
                copieInvalidite.setNombrePagesMotivation(demandeInvalidite.getNombrePagesMotivation());
                copieInvalidite.setOfficeAI(demandeInvalidite.getOfficeAI());
                copieInvalidite.setPourcentageReduction(demandeInvalidite.getPourcentageReduction());
                copieInvalidite.setPourcentageReductionPourFauteGrave(demandeInvalidite
                        .getPourcentageReductionPourFauteGrave());
                copieInvalidite.setPourcentageReductionPourNonCollaboration(demandeInvalidite
                        .getPourcentageReductionPourNonCollaboration());

                copie = copieInvalidite;

                break;
            case DEMANDE_SURVIVANT:
                DemandeRenteSurvivant copieSurvivant = new DemandeRenteSurvivant();

                DemandeRenteSurvivant demandeSurvivant = (DemandeRenteSurvivant) demande;
                copieSurvivant.setPourcentageReduction(demandeSurvivant.getPourcentageReduction());

                copie = copieSurvivant;

                break;
            case DEMANDE_VIEILLESSE:
                DemandeRenteVieillesse copieVieillesse = new DemandeRenteVieillesse();

                DemandeRenteVieillesse demandeVieillesse = (DemandeRenteVieillesse) demande;
                copieVieillesse.setAnticipation(demandeVieillesse.getAnticipation());
                copieVieillesse.setAvecAjournement(demandeVieillesse.isAvecAjournement());
                copieVieillesse.setDateRevocationAjournement(demandeVieillesse.getDateRevocationAjournement());

                copie = copieVieillesse;

                break;
            default:
                throw new IllegalArgumentException("[demande.etat] is invalid");
        }

        copie.setDateDebutDuDroitInitial(demande.getDateDebutDuDroitInitial());
        copie.setDateDepot(demande.getDateDepot());
        copie.setDateFinDuDroitInitial(demande.getDateFinDuDroitInitial());
        copie.setDateReception(demande.getDateReception());
        copie.setDateTraitement(demande.getDateTraitement());
        copie.setDossier(demande.getDossier());
        copie.setGestionnaire(demande.getGestionnaire());
        copie.setTypeCalcul(demande.getTypeCalcul());
        copie.setTypeDemandeRente(demande.getTypeDemandeRente());

        copie.setEtat(EtatDemandeRente.ENREGISTRE);

        // on ne copie pas les rentes accordées
        // on ne copie pas les informations complémentaires

        return copie;
    }

    @Override
    public Set<DemandeRente> demandesDuRequerantEtDeSaFamille(final PersonneAVS requerant) {

        Set<DemandeRente> demandesDuRequerantEtDeSaFamille = new HashSet<DemandeRente>();

        try {
            REDemandeRenteJointDemandeManager manager = new REDemandeRenteJointDemandeManager();
            manager.setSession(BSessionUtil.getSessionFromThreadContext());
            manager.setLikeNumeroAVS(requerant.getNss().toString());
            manager.setIsRechercheFamille(true);
            manager.find();

            DemandeRenteCrudService demandeRenteCrudService = CorvusCrudServiceLocator.getDemandeRenteCrudService();
            for (int i = 0; i < manager.size(); i++) {
                REDemandeRenteJointDemande uneDemande = (REDemandeRenteJointDemande) manager.get(i);
                demandesDuRequerantEtDeSaFamille.add(demandeRenteCrudService.read(Long.parseLong(uneDemande
                        .getIdDemandeRente())));
            }

        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }

        return demandesDuRequerantEtDeSaFamille;
    }

    @Override
    public void mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(final DemandeRente demande) {

        try {
            REDemandeRente demandeRenteEntity = new REDemandeRente();
            demandeRenteEntity.setSession(BSessionUtil.getSessionFromThreadContext());
            demandeRenteEntity.setIdDemandeRente(demande.getId().toString());
            demandeRenteEntity.retrieve();

            Periode periodeDroitRentesDeLaDemande = demande.getPeriodeDuDroitDesRentesAccordees();
            if (!periodeDroitRentesDeLaDemande.getDateDebut().isEmpty()) {
                demandeRenteEntity.setDateDebut(periodeDroitRentesDeLaDemande.getDateDebut());
            }
            demandeRenteEntity.setDateFin(periodeDroitRentesDeLaDemande.getDateFin());

            demandeRenteEntity.update();
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
    }

    @Override
    public Set<RenteAccordee> rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(final DemandeRente demande) {
        return CorvusServiceLocator.getRenteAccordeeService()
                .rentesAccordeesDevantEtreDiminueesLorsDeLaValidationDeLaDemande(demande);
    }
}
