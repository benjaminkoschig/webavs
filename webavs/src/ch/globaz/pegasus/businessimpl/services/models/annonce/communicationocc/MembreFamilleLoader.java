package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class MembreFamilleLoader {

    public Map<String, List<PlanDeCalculWitMembreFamille>> loadMembreFamilleCompirsDansLeCalcul(List<String> idsPca) {
        List<PlanDeCalculWitMembreFamille> membresFamille = searchMembreFamilleRetenue(idsPca);
        return groupByIdPca(membresFamille);
    }

    public Map<String, List<PlanDeCalculWitMembreFamille>> loadMembreFamilleByDecisionAc(
            List<DecisionApresCalcul> decisions) {
        return loadMembreFamilleCompirsDansLeCalcul(resolveIdsPcaForDecision(decisions));
    }

    public Map<String, List<PlanDeCalculWitMembreFamille>> loadMembreFamilleByPcaDecompte(List<PcaForDecompte> pcas) {
        return loadMembreFamilleCompirsDansLeCalcul(resolveIdsPlanCalculForPca(pcas));
    }

    public List<PlanDeCalculWitMembreFamille> searchMembreFamilleRetenue(List<String> idsPca) {
        if (idsPca.size() > 0) {
            PlanDeCalculWitMembreFamilleSearch planDeCalculWitMembreFamilleSearch = new PlanDeCalculWitMembreFamilleSearch();
            planDeCalculWitMembreFamilleSearch.setInIdPCAccordee(idsPca);
            planDeCalculWitMembreFamilleSearch.setForComprisPcal(true);
            planDeCalculWitMembreFamilleSearch.setForIsPlanRetenu(true);
            try {
                return PersistenceUtil.search(planDeCalculWitMembreFamilleSearch);
            } catch (JadePersistenceException e) {
                throw new RuntimeException("Impossible de charger les personne comprise dans le calcul", e);
            }
        } else {
            return new ArrayList<PlanDeCalculWitMembreFamille>();
        }
    }

    static public String resolveIdDonneePersonnelRequerant(List<PlanDeCalculWitMembreFamille> membreFamilleRetenues) {
        String idDonneesPersonnellesRequerant = null;
        for (PlanDeCalculWitMembreFamille membreFamille : membreFamilleRetenues) {
            SimpleDroitMembreFamille droitMembreFamille = membreFamille.getDroitMembreFamille()
                    .getSimpleDroitMembreFamille();
            RoleMembreFamille role = RoleMembreFamille.fromValue(droitMembreFamille.getCsRoleFamillePC());
            if (role.isRequerant()) {
                idDonneesPersonnellesRequerant = droitMembreFamille.getIdDonneesPersonnelles();
                break;
            }
        }
        return idDonneesPersonnellesRequerant;
    }

    Map<String, List<PlanDeCalculWitMembreFamille>> groupByIdPca(List<PlanDeCalculWitMembreFamille> membresFamille) {
        Map<String, List<PlanDeCalculWitMembreFamille>> map = JadeListUtil.groupBy(membresFamille,
                new Key<PlanDeCalculWitMembreFamille>() {
                    @Override
                    public String exec(PlanDeCalculWitMembreFamille e) {
                        return e.getSimplePlanDeCalcul().getIdPCAccordee();
                    }
                });
        return map;
    }

    private List<String> resolveIdsPcaForDecision(List<DecisionApresCalcul> decisions) {
        List<String> ids = new ArrayList<String>();

        for (DecisionApresCalcul dc : decisions) {
            ids.add(dc.getPcAccordee().getId());
        }

        return ids;
    }

    private List<String> resolveIdsPlanCalculForPca(List<PcaForDecompte> pcas) {
        List<String> ids = new ArrayList<String>();

        for (PcaForDecompte pca : pcas) {
            if (JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                ids.add(pca.getSimplePCAccordee().getId());
            } else {
                ids.add(pca.getSimplePCAccordee().getIdPcaParent());
            }
        }

        return ids;
    }

    public List<PlanDeCalculWitMembreFamille> searchMembreFamilleRetenue(String idsPCa) {
        List<String> ids = new ArrayList<String>();
        ids.add(idsPCa);
        return this.searchMembreFamilleRetenue(ids);
    }
}
