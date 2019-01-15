package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;

/**
 * On garde que la dernière version de droit.
 * Le but est de filtrer toutes les autres version de droit.
 * 
 * @author dma
 * 
 */
class RpcDatasFilter {

    private int nbRestant;

    public Map<String, List<RPCDecionsPriseDansLeMois>> filtreAndGroupByIdVersionDroit(
            List<RPCDecionsPriseDansLeMois> decisions) {
        return filtre(decisions);
    }

    public int getNbRestant() {
        return nbRestant;
    }

    Map<String, List<RPCDecionsPriseDansLeMois>> filtre(List<RPCDecionsPriseDansLeMois> decisions) {
        Map<String, TreeMap<String, List<RPCDecionsPriseDansLeMois>>> map = groupByIdDemandeAndVersionDroit(decisions);
        Map<String, List<RPCDecionsPriseDansLeMois>> result = new HashMap<String, List<RPCDecionsPriseDansLeMois>>();
        for (Entry<String, TreeMap<String, List<RPCDecionsPriseDansLeMois>>> entry : map.entrySet()) {
            Entry<String, List<RPCDecionsPriseDansLeMois>> last = entry.getValue().lastEntry();
            result.put(last.getValue().get(0).getSimpleVersionDroit().getIdVersionDroit(), last.getValue());
        }
        
        // S180426_009 - RPC - Pi-066 - ajouter un tri par idDossier avant le retour
        Map<String, List<RPCDecionsPriseDansLeMois>> resultByIdDossier = new HashMap<String, List<RPCDecionsPriseDansLeMois>>();
        for (Entry<String, List<RPCDecionsPriseDansLeMois>> entry : result.entrySet()) {
            if (!resultByIdDossier.containsKey(entry.getValue().get(0).getIdDossier())) {
                resultByIdDossier.put(entry.getValue().get(0).getIdDossier(), entry.getValue());
            } else {
                resultByIdDossier.get(entry.getValue().get(0).getIdDossier()).addAll(entry.getValue());
            }
        }
        return resultByIdDossier;

    }

    private Map<String, TreeMap<String, List<RPCDecionsPriseDansLeMois>>> groupByIdDemandeAndVersionDroit(
            List<RPCDecionsPriseDansLeMois> decisions) {
        List<RPCDecionsPriseDansLeMois> decisionsFiltre = filtreByPca(decisions);
        
        Map<String, TreeMap<String, List<RPCDecionsPriseDansLeMois>>> map = new HashMap<String, TreeMap<String, List<RPCDecionsPriseDansLeMois>>>();
        for (RPCDecionsPriseDansLeMois decision : decisionsFiltre) {
            String key = decision.getIdDemande();
            String idVersionDroit = decision.getSimpleVersionDroit().getNoVersion()+decision.getSimpleVersionDroit().getIdVersionDroit();
            if (!map.containsKey(key)) {
                map.put(key, new TreeMap<String, List<RPCDecionsPriseDansLeMois>>(newComparatorNoVersion()));
            }
            if (!map.get(key).containsKey(idVersionDroit)) {
                map.get(key).put(idVersionDroit, new ArrayList<RPCDecionsPriseDansLeMois>());
            }
            map.get(key).get(idVersionDroit).add(decision);
        }
        return map;
    }
    
    // s'arrure qu'il n'y a qu'une seule et même décision par pca
    private List<RPCDecionsPriseDansLeMois> filtreByPca(List<RPCDecionsPriseDansLeMois> decisions) {
        Map<String, RPCDecionsPriseDansLeMois> decisionMap= new HashMap<>();
        for(RPCDecionsPriseDansLeMois decision: decisions) {
            decisionMap.put(decision.getIdPCAccordee(), decision);
        }
        return new ArrayList<>(decisionMap.values());
    }
    
    private Comparator<String> newComparatorNoVersion() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
    }
    

    private Comparator<RPCDecionsPriseDansLeMois> newComparator() {
        return new Comparator<RPCDecionsPriseDansLeMois>() {
            @Override
            public int compare(RPCDecionsPriseDansLeMois o1, RPCDecionsPriseDansLeMois o2) {
                return o1.getSimpleVersionDroit().getNoVersion().compareTo(o2.getSimpleVersionDroit().getNoVersion());
            }
        };
    }

    private Map<String, List<RPCDecionsPriseDansLeMois>> groupByIdVersionDroit(List<RPCDecionsPriseDansLeMois> decisions) {
        Map<String, List<RPCDecionsPriseDansLeMois>> map = new HashMap<String, List<RPCDecionsPriseDansLeMois>>();
        for (RPCDecionsPriseDansLeMois decisionModel : decisions) {
            String key = decisionModel.getSimpleVersionDroit().getIdVersionDroit();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<RPCDecionsPriseDansLeMois>());
            }
            map.get(key).add(decisionModel);
        }
        return map;
    }

}
