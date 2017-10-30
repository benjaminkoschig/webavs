package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.RepositoryJade;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.models.decision.ValidationDecision;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSearch;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.demande.SimpleDemandeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.rpc.business.models.LotDossierDecision;
import ch.globaz.pegasus.rpc.business.models.LotDossierDecisionSearch;
import ch.globaz.pegasus.rpc.business.models.RetourAnnonce;
import ch.globaz.pegasus.rpc.business.models.RetourAnnonceSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleLienAnnonceDecision;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonceSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleRetourAnnonce;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;
import ch.globaz.pegasus.rpc.domaine.Annonce;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.CodeTraitement;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotAnnonce;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;
import ch.globaz.pegasus.rpc.domaine.StatusRetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.TypeAnnonce;
import ch.globaz.pegasus.rpc.domaine.plausi.AnnoncePlausiRetour;
import ch.globaz.pegasus.rpc.domaine.plausi.PlausiRetour;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;

public class RetourAnnonceRepository extends RepositoryJade<RetourAnnonceRpc, SimpleRetourAnnonce> {

    @Override
    public RetourAnnonceRpc create(RetourAnnonceRpc entity) {
        RetourAnnonceRpc retourAnnonceRpc = super.create(entity);
        return retourAnnonceRpc;
    }

    @Override
    public DomaineConverterJade<RetourAnnonceRpc, SimpleRetourAnnonce> getConverter() {
        return new RetourAnnonceConverter();
    }

    public void updateRemarqueDetailAnnonce(String idDetailAnnonce, String remarque) {
        final RetourAnnonceRpc retourAnnonce = findById(idDetailAnnonce);
        retourAnnonce.setRemarque(remarque);
        new RetourAnnonceRepository().update(retourAnnonce);
    }

    public void updateEtatDetailAnnonce(String idDetailAnnonce, StatusRetourAnnonce status) {
        final RetourAnnonceRpc retourAnnonce = findById(idDetailAnnonce);
        retourAnnonce.changeStatusTo(status);
        new RetourAnnonceRepository().update(retourAnnonce);
    }

    public List<RetourAnnonceRpc> findRetoursAnnonce(AnnonceSearch annonceSearch) {
        RetourAnnonceConverter retourAnnonceConverter = new RetourAnnonceConverter();
        List<RetourAnnonceRpc> retoursAnnonce = new ArrayList<RetourAnnonceRpc>();
        RetourAnnonceSearch search = new RetourAnnonceSearch();
        search.setForIdAnnonce(annonceSearch.getAnnonceId());
        search = this.search(search);
        for (JadeAbstractModel model : search.getSearchResults()) {
            RetourAnnonce complexModel = (RetourAnnonce) model;
            SimpleRetourAnnonce retourAnnonceModel = complexModel.getSimpleRetourAnnonce();

            RetourAnnonceRpc retourAnnonceRpc = retourAnnonceConverter.convertToDomain(retourAnnonceModel);
            retoursAnnonce.add(retourAnnonceRpc);
        }
        return retoursAnnonce;
    }

    public void createRetourAnnonces(List<AnnoncePlausiRetour> annoncePlausiRetour) {
        RetourAnnonceConverter retourAnnonceConverter = new RetourAnnonceConverter();
        for (AnnoncePlausiRetour annonceRetour : annoncePlausiRetour) {
            for (PlausiRetour plausiRetour : annonceRetour.getPlausiRetours()) {
                SimpleLienAnnonceDecision lienDecisionAnnonce = findLienAnnonceDecision(annonceRetour, plausiRetour);
                List<SimpleRetourAnnonce> simpleRetourAnnonceList = retourAnnonceConverter
                        .convertToPersistence(plausiRetour);
                for (SimpleRetourAnnonce simpleRetour : simpleRetourAnnonceList) {
                    if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                        StringBuilder errorMessage = new StringBuilder();
                        for (JadeBusinessMessage message : JadeThread.logMessages()) {
                            errorMessage.append(message.getMessageId() + "\n");
                        }
                        throw new RpcTechnicalException(errorMessage.toString());
                    }
                    simpleRetour.setIdLien(lienDecisionAnnonce.getId());
                    simpleRetour.setIdLienAnnonceDecision(lienDecisionAnnonce.getId());
                    create(simpleRetour);
                }
            }
        }
    }

    private SimpleLienAnnonceDecision findLienAnnonceDecision(AnnoncePlausiRetour annonce, PlausiRetour decision) {
        LotDossierDecisionSearch search = new LotDossierDecisionSearch();
        // Remove C and R from ID Decision
        if (decision.getIdDecision().contains("C") || decision.getIdDecision().contains("R")) {
            search.setForIdDecision(decision.getIdDecision().replace("C", "").replace("R", ""));
        } else {
            search.setForIdDecision(decision.getIdDecision());
        }
        search.setForIdDossier(annonce.getBusinessCaseIdRPC());
        search.setForMonth(decision.getReceiptMonth().getSwissValue());
        List<LotDossierDecision> annoncesLien = RepositoryJade.searchForAndFetch(search);
        if (annoncesLien.isEmpty()) {
            // Pas de decision annoncée à cette date -> creation de l'annonce
            return createAnnonce(annonce, decision);
        } else {
            AnnonceRepositoryJade repo = new AnnonceRepositoryJade();
            SimpleAnnonce simpleAnnonce = repo.findModelById(annoncesLien.get(0).getSimpleAnnonce().getId());
            AnnonceRpc annonceRpc = repo.getConverter().convertToDomain(simpleAnnonce);
            annonceRpc.setEtat(EtatAnnonce.CORRECTION);
            annonceRpc.setCodeTraitement(getCodeTraitement(annonce));
            repo.update(annonceRpc);
        }
        return annoncesLien.get(0).getSimpleLienAnnonceDecision();
    }

    private SimpleLienAnnonceDecision createAnnonce(AnnoncePlausiRetour annonceRetour, PlausiRetour decision) {
        Annonce annonce = new Annonce();
        annonce.setEtat(EtatAnnonce.CORRECTION);
        annonce.setCodeTraitement(getCodeTraitement(annonceRetour));
        annonce.setType(TypeAnnonce.COMPLET);

        SimpleLotAnnonce lot = findLotByDate(decision.getReceiptMonth().getSwissValue());
        LotAnnonce lotAnnonce = new LotAnnonce();
        lotAnnonce.setId(lot.getId());
        annonce.setLot(lotAnnonce);

        SimpleDemande simpleDemande = findIdDemande(annonceRetour.getBusinessCaseIdRPC());

        Demande demande = new Demande();
        demande.setId(simpleDemande.getIdDemande());
        annonce.setDemande(demande);

        Dossier dossier = new Dossier();
        dossier.setId(simpleDemande.getIdDossier());
        annonce.setDossier(dossier);

        annonce.setDecisions(new ArrayList<RpcDecisionWithIdPlanCal>());

        AnnonceRpc annoncerpc = new AnnonceRepositoryJade().create(annonce);

        SimpleLienAnnonceDecision lien = new SimpleLienAnnonceDecision();
        lien.setIdAnnonce(annoncerpc.getId());
        lien.setIdDecision(decision.getIdDecision());
        lien.setIdPlanCalcul(findIdPlanDeCalCul(decision.getIdDecision()));

        try {
            lien = (SimpleLienAnnonceDecision) JadePersistenceManager.add(lien);
        } catch (JadePersistenceException e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        return lien;
    }

    private CodeTraitement getCodeTraitement(AnnoncePlausiRetour annonceRetour) {
        boolean isRetourAvertisement = true;
        if (annonceRetour != null && !annonceRetour.getPlausiRetours().isEmpty()) {
            for (PlausiRetour plausi : annonceRetour.getPlausiRetours()) {
                for (ch.globaz.pegasus.rpc.domaine.RetourAnnonce retour : plausi.getRetours()) {
                    if (retour.getCategoriePlausi() != RpcPlausiCategory.WARNING
                            && retour.getCategoriePlausi() != RpcPlausiCategory.INFO) {
                        isRetourAvertisement = false;
                    }
                }

            }
        }
        return isRetourAvertisement ? CodeTraitement.RETOUR_AVERTISEMENT : CodeTraitement.RETOUR_A_TRAITER;
    }

    private SimpleLotAnnonce findLotByDate(String date) {
        SimpleLotAnnonceSearch search = new SimpleLotAnnonceSearch();
        search.setForDate(date);
        List<SimpleLotAnnonce> lots = RepositoryJade.searchForAndFetch(search);
        if (lots.isEmpty()) {
            throw new RpcBusinessException("Lot not found with this date : {?} ", date);
        }
        return lots.get(0);
    }

    private SimpleDemande findIdDemande(String idDossier) {
        SimpleDemandeSearch search = new SimpleDemandeSearch();
        search.setForIdDossier(idDossier);
        List<SimpleDemande> demandes = RepositoryJade.searchForAndFetch(search);
        if (demandes.isEmpty()) {
            throw new RpcBusinessException("Dossier not found with this id : {?} ", idDossier);
        }
        return demandes.get(0);
    }

    private String findIdPlanDeCalCul(String idDecision) {
        ValidationDecisionSearch search = new ValidationDecisionSearch();
        search.setForIdDecision(idDecision);
        List<ValidationDecision> validationDecisions = RepositoryJade.searchForAndFetch(search);
        if (!validationDecisions.isEmpty()) {
            SimplePlanDeCalculSearch simpleSearch = new SimplePlanDeCalculSearch();
            simpleSearch.setForIdPCAccordee(validationDecisions.get(0).getSimpleValidationDecision().getIdPCAccordee());
            List<SimplePlanDeCalcul> planDeCalcul = RepositoryJade.searchForAndFetch(simpleSearch);
            if (!planDeCalcul.isEmpty()) {
                return planDeCalcul.get(0).getIdPlanDeCalcul();
            }
        }
        return null;
    }

    public void updateStatutRetour(SimpleRetourAnnonce model, StatusRetourAnnonce statut) {
        RetourAnnonceRpc retourAnnonceRpc = getConverter().convertToDomain(model);
        retourAnnonceRpc.changeStatusTo(statut);
        update(retourAnnonceRpc);
    }

    public void updateAnnonceByOldIdDecision(String idDecision) {
        RetourAnnonceSearch search = new RetourAnnonceSearch();
        search.setForIdDecision(idDecision);
        List<RetourAnnonce> retours = RepositoryJade.searchForAndFetch(search);

        if (!retours.isEmpty()) {
            for (RetourAnnonce retour : retours) {
                // mets à jour les retours en erreur à "traité"
                RetourAnnonceRpc retourAnnonce = getConverter().convertToDomain(retour.getSimpleRetourAnnonce());
                retourAnnonce.setIdLien(retour.getSimpleLienAnnonceDecision().getId());
                retourAnnonce.setIdLienAnnonceDecision(retour.getSimpleLienAnnonceDecision());
                if (StatusRetourAnnonce.MODIFICATION_DROIT.equals(retourAnnonce.getStatus())
                        || StatusRetourAnnonce.A_TRAITER.equals(retourAnnonce.getStatus())) {
                    retourAnnonce.changeStatusTo(StatusRetourAnnonce.TRAITE);
                    update(retourAnnonce);
                }
            }
            // met à jour l'annonce associé à l'état envoie pour correction
            new AnnonceRepositoryJade().updateEtatAnnonce(retours.get(0).getSimpleAnnonce(), EtatAnnonce.POUR_ENVOI);
        }
    }

    public boolean hasRetourAnnonceAtraite(List<RetourAnnonce> retours) {
        for (RetourAnnonce retour : retours) {
            RetourAnnonceRpc retourAnnonce = getConverter().convertToDomain(retour.getSimpleRetourAnnonce());
            if (StatusRetourAnnonce.A_TRAITER.equals(retourAnnonce.getStatus())) {
                return true;
            }
        }
        return false;
    }

}
