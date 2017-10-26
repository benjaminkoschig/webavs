package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;
import com.google.common.collect.Lists;

class IdsContainer {
    private final Map<String, List<IdContainer>> idContainers;

    private class IdContainer {
        private final String idPca;
        private final String idTiersDomicile;
        private final String idTiersCourrier;
        private final String idPlanCal;
        private final String idDroit;
        private final String idVersionDroit;
        private final String idPcaOriginale;

        public IdContainer(String idPca, String idTiersDomicile, String idTiersCourrier, String idPlanCal,
                String idDroit, String idVersionDroit, String idPcaOriginale) {
            this.idPca = idPca;
            this.idTiersDomicile = idTiersDomicile;
            this.idTiersCourrier = idTiersCourrier;
            this.idPlanCal = idPlanCal;
            this.idDroit = idDroit;
            this.idVersionDroit = idVersionDroit;
            this.idPcaOriginale = idPcaOriginale;
        }

        public String getIdPca() {
            return idPca;
        }

        public String getIdTiersDomicile() {
            return idTiersDomicile;
        }

        public String getIdTiersCourrier() {
            return idTiersCourrier;
        }

        public String getIdPlanCal() {
            return idPlanCal;
        }

        public String getIdDroit() {
            return idDroit;
        }

        public String getIdPcaOriginale() {
            return idPcaOriginale;
        }

        public String getIdVersionDroit() {
            return idVersionDroit;
        }
    }

    public IdsContainer() {
        idContainers = new HashMap<String, List<IdContainer>>();
    }

    private IdsContainer(Map<String, List<IdContainer>> idContainers) {
        this.idContainers = idContainers;
    }

    public Map<String, List<IdContainer>> getIdContainers() {
        return idContainers;
    }

    public void addAllIds(Map<String, List<RPCDecionsPriseDansLeMois>> mapDecision) {
        for (Entry<String, List<RPCDecionsPriseDansLeMois>> entry : mapDecision.entrySet()) {
            for (RPCDecionsPriseDansLeMois model : entry.getValue()) {
                String idPca;
                String idPlanCal;
                String idPcaOrignale = model.getSimplePCAccordee().getIdPCAccordee();

                // Dans le cadre du copie(idPCaParent !=null) les données ne sont pas liées à la pca mais à
                // l'idPcaParent
                if (model.getSimplePCAccordee().getIdPcaParent() == null
                        || "0".equals(model.getSimplePCAccordee().getIdPcaParent())) {
                    idPlanCal = model.getSimplePlanDeCalcul().getId();
                    idPca = idPcaOrignale;
                } else {
                    idPlanCal = model.getIdPlanDeCalculParent();
                    idPca = model.getSimplePCAccordee().getIdPcaParent();
                }

                add(idPca, model.getSimpleTiers().getId(), model.getSimpleDecisionHeader().getIdTiersCourrier(),
                        idPlanCal, model.getSimpleVersionDroit().getIdDroit(), model.getSimpleVersionDroit()
                                .getIdVersionDroit(), idPcaOrignale);

            }
        }
    }

    void add(String idPca, String idTiersDomicile, String idTiersCourrier, String idPlanCal, String idDroit,
            String idVersionDroit, String idPcaOriginale) {
        if (!idContainers.containsKey(idVersionDroit)) {
            idContainers.put(idVersionDroit, new ArrayList<IdsContainer.IdContainer>());
        }
        idContainers.get(idVersionDroit).add(
                new IdContainer(idPca, idTiersDomicile, idTiersCourrier, idPlanCal, idDroit, idVersionDroit,
                        idPcaOriginale));
    }

    public List<IdsContainer> partion(int size) {
        List<List<List<IdContainer>>> partition = Lists.partition(
                new ArrayList<List<IdContainer>>(idContainers.values()), size);
        List<IdsContainer> containers = new ArrayList<IdsContainer>();
        for (List<List<IdContainer>> listSplited : partition) {
            Map<String, List<IdContainer>> map = new HashMap<String, List<IdContainer>>();
            for (List<IdContainer> list : listSplited) {
                map.put(list.get(0).getIdVersionDroit(), list);
            }
            containers.add(new IdsContainer(map));
        }
        return containers;
    }

    public Set<String> getIdsPca() {
        Set<String> idsPca = new HashSet<String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                idsPca.add(container.getIdPca());
            }
        }
        return idsPca;
    }

    public Set<String> getIdsTiersDomicile() {
        Set<String> idsTiersDomicile = new HashSet<String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                idsTiersDomicile.add(container.getIdTiersDomicile());
            }
        }
        return idsTiersDomicile;
    }

    public Set<String> getIdsTiersCourrier() {
        Set<String> idsTiersCourrier = new HashSet<String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                idsTiersCourrier.add(container.getIdTiersCourrier());
            }
        }
        return idsTiersCourrier;
    }

    public Set<String> getIdsPlanCal() {
        Set<String> idsPlanCal = new HashSet<String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                idsPlanCal.add(container.getIdPlanCal());
            }
        }

        return idsPlanCal;
    }

    public Set<String> getIdsDroit() {
        Set<String> idsDroit = new HashSet<String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                idsDroit.add(container.getIdDroit());
            }
        }

        return idsDroit;
    }

    public Set<String> getIdsVersionDroit() {
        Set<String> idsVersionDroit = new HashSet<String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                idsVersionDroit.add(container.getIdVersionDroit());
            }
        }

        return idsVersionDroit;
    }

    public Map<String, Set<String>> createMapIdPcaIdVersionDroit() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();

        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                if (!map.containsKey(container.getIdPca())) {
                    map.put(container.getIdPca(), new HashSet<String>());
                }
                map.get(container.getIdPca()).add(container.getIdVersionDroit());
            }
        }
        return map;
    }

    public Map<String, String> createMapIdPcaIdpcaOriginale() {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                map.put(container.getIdPcaOriginale() + "_" + container.getIdVersionDroit(),
                        container.getIdPcaOriginale());
                map.put(container.getIdPca() + "_" + container.getIdVersionDroit(), container.getIdPcaOriginale());
            }
        }
        return map;
    }

    public String resolveIdsVersionDroitByIdPlanCal(String id) {
        for (Entry<String, List<IdContainer>> entry : idContainers.entrySet()) {
            for (IdContainer container : entry.getValue()) {
                return container.getIdVersionDroit();
            }
        }
        return null;
    }

    static Set<String> resolveIdsTiersMembreFamille(
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles,
            Set<String> idsTiersInit) {
        Set<String> idsTiersFamille = idsTiersInit;
        for (Map<String, List<MembreFamilleWithDonneesFinanciere>> container : mapMembresFamilles.values()) {
            for (List<MembreFamilleWithDonneesFinanciere> membres : container.values()) {
                for (MembreFamilleWithDonneesFinanciere membre : membres) {
                    idsTiersFamille.add(String.valueOf(membre.getFamille().getPersonne().getId()));
                }
            }
        }
        return idsTiersFamille;
    }

    static Set<String> resolveIdsTypeChambreHome(
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles) {
        Set<String> ids = new HashSet<String>();
        for (Map<String, List<MembreFamilleWithDonneesFinanciere>> container : mapMembresFamilles.values()) {
            for (List<MembreFamilleWithDonneesFinanciere> membres : container.values()) {
                for (MembreFamilleWithDonneesFinanciere membre : membres) {
                    ids.addAll(membre.getDonneesFinancieres().getTaxesJournaliereHome().resovleIdsTypeChambre());
                }
            }
        }
        return ids;
    }

    static Set<String> resolveIdsLocaliteDernierDomicileLegale(
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles) {
        Set<String> ids = new HashSet<String>();
        for (Map<String, List<MembreFamilleWithDonneesFinanciere>> container : mapMembresFamilles.values()) {
            for (List<MembreFamilleWithDonneesFinanciere> membres : container.values()) {
                for (MembreFamilleWithDonneesFinanciere membre : membres) {
                    ids.add(membre.getFamille().getDonneesPersonnelles().getIdDernierDomicileLegale());
                }
            }
        }
        return ids;
    }

}
