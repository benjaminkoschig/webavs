package ch.globaz.vulpecula.businessimpl.services.syndicat;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.business.services.syndicat.AffiliationSyndicatService;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.AffiliationSyndicatRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class AffiliationSyndicatServiceImpl implements AffiliationSyndicatService {
    private AffiliationSyndicatRepository affiliationSyndicatRepository = VulpeculaRepositoryLocator
            .getAffiliationSyndicatRepository();
    private DecompteSalaireService decompteSalaireService = VulpeculaServiceLocator.getDecompteSalaireService();
    private TravailleurService travailleurService = VulpeculaServiceLocator.getTravailleurService();

    @Override
    public Map<Administration, List<AffiliationSyndicat>> findByAnneeWithCumulSalairesGroupByCaisseMetier(
            String idSyndicat, String idCaisseMetier, Annee annee, List<String> listeErreur) {
        Map<Administration, List<AffiliationSyndicat>> affiliationsSyndicatsGroupByCaisseMetier = new HashMap<Administration, List<AffiliationSyndicat>>();
        List<AffiliationSyndicat> affiliations = affiliationSyndicatRepository.findByAnnee(idSyndicat, annee);
        for (AffiliationSyndicat affiliationSyndicat : affiliations) {
            try {

                Map<Administration, Montant> montantsParCaisseMetier = decompteSalaireService
                        .findMontantsByCaisseMetier(affiliationSyndicat.getIdTravailleur(), annee.getFirstDayOfYear(),
                                annee.getLastDayOfYear());
                for (Map.Entry<Administration, Montant> caisseMetierByMontant : montantsParCaisseMetier.entrySet()) {
                    Administration caisseMetier = caisseMetierByMontant.getKey();
                    if (JadeStringUtil.isEmpty(idCaisseMetier) || idCaisseMetier.equals(caisseMetier.getId())) {
                        AffiliationSyndicat affiliationClone = affiliationSyndicat.clone();
                        affiliationClone.setCumulSalaires(caisseMetierByMontant.getValue());
                        if (!affiliationsSyndicatsGroupByCaisseMetier.containsKey(caisseMetier)) {
                            affiliationsSyndicatsGroupByCaisseMetier.put(caisseMetier,
                                    new ArrayList<AffiliationSyndicat>());
                        }
                        affiliationsSyndicatsGroupByCaisseMetier.get(caisseMetier).add(affiliationClone);
                    }
                }
            } catch (Exception e) {
                listeErreur.add(affiliationSyndicat.getTravailleur().getDesignation1() + " "
                        + affiliationSyndicat.getTravailleur().getDesignation2());
            }

        }
        return affiliationsSyndicatsGroupByCaisseMetier;
    }

    @Override
    public Map<Administration, Map<Administration, List<AffiliationSyndicat>>> findByAnneeWithCumulSalaireGroupBySyndicatAndCaisseMetier(
            String idSyndicat, String idCaisseMetier, Annee annee, List<String> listeErreur) {
        Map<Administration, Map<Administration, List<AffiliationSyndicat>>> result = new HashMap<Administration, Map<Administration, List<AffiliationSyndicat>>>();
        Map<Administration, List<AffiliationSyndicat>> affiliationSyndicatGroupByCaisseMetier = findByAnneeWithCumulSalairesGroupByCaisseMetier(
                idSyndicat, idCaisseMetier, annee, listeErreur);
        for (Map.Entry<Administration, List<AffiliationSyndicat>> entry : affiliationSyndicatGroupByCaisseMetier
                .entrySet()) {
            Administration caisseMetier = entry.getKey();
            List<AffiliationSyndicat> affiliations = entry.getValue();
            Map<Administration, Collection<AffiliationSyndicat>> affiliationsGroupBySyndicat = Multimaps.index(
                    affiliations, new Function<AffiliationSyndicat, Administration>() {
                        @Override
                        public Administration apply(AffiliationSyndicat affiliationSyndicat) {
                            return affiliationSyndicat.getSyndicat();
                        }
                    }).asMap();
            for (Map.Entry<Administration, Collection<AffiliationSyndicat>> affiliationsBySyndicat : affiliationsGroupBySyndicat
                    .entrySet()) {
                Administration syndicat = affiliationsBySyndicat.getKey();
                if (!result.containsKey(syndicat)) {
                    result.put(syndicat, new HashMap<Administration, List<AffiliationSyndicat>>());
                }
                if (!result.get(syndicat).containsKey(caisseMetier)) {
                    result.get(syndicat).put(caisseMetier, new ArrayList<AffiliationSyndicat>());
                }
                result.get(syndicat).get(caisseMetier).addAll(affiliationsBySyndicat.getValue());
            }
        }
        return result;
    }

    @Override
    public List<Travailleur> findTravailleursSansSyndicats(Annee annee) {
        List<Travailleur> travailleurs = travailleurService.findActifs(annee);
        List<Travailleur> travailleursAvecSyndicats = findTravailleursAvecSyndicats(annee);
        travailleurs.removeAll(travailleursAvecSyndicats);
        return travailleurs;
    }

    private List<Travailleur> findTravailleursAvecSyndicats(Annee annee) {
        Set<Travailleur> travailleurs = new HashSet<Travailleur>();
        List<AffiliationSyndicat> affiliations = affiliationSyndicatRepository.findByAnnee(annee);
        for (AffiliationSyndicat affiliation : affiliations) {
            if (affiliation.getPeriode().contains(annee.getLastDayOfYear())) {
                travailleurs.add(affiliation.getTravailleur());
            }
        }
        return new ArrayList<Travailleur>(travailleurs);
    }
}
