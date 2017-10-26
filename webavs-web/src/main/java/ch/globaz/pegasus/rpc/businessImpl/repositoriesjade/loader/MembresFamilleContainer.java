package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;

class MembresFamilleContainer {
    Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles = new HashMap<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>>();
    Set<String> idsTiersDomicile = new HashSet<String>();
    Set<String> idsTiersCourrier = new HashSet<String>();

    public MembresFamilleContainer() {
    }

    public MembresFamilleContainer(List<MembresFamilleContainer> containers) {
        addAll(containers);
    }

    public Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> getMapMembresFamilles() {
        return mapMembresFamilles;
    }

    public void setMapMembresFamilles(
            Map<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> mapMembresFamilles) {
        this.mapMembresFamilles = mapMembresFamilles;
    }

    public Set<String> getIdsTiersDomicile() {
        return idsTiersDomicile;
    }

    public void setIdsTiersDomicile(Set<String> idsTiersDomicile) {
        this.idsTiersDomicile = idsTiersDomicile;
    }

    public Set<String> getIdsTiersCourrier() {
        return idsTiersCourrier;
    }

    public Set<String> resovleIdTiers() {
        Set<String> ids = new HashSet<String>();
        for (Entry<String, Map<String, List<MembreFamilleWithDonneesFinanciere>>> entry : mapMembresFamilles.entrySet()) {
            for (Entry<String, List<MembreFamilleWithDonneesFinanciere>> e : entry.getValue().entrySet()) {
                for (MembreFamilleWithDonneesFinanciere membre : e.getValue()) {
                    ids.add(String.valueOf(membre.getFamille().getPersonne().getId()));
                }
            }
        }
        return ids;
    }

    public Set<String> getAllIdTiersForAdresse() {
        Set<String> ids = new HashSet<String>();
        ids.addAll(idsTiersCourrier);
        ids.addAll(idsTiersDomicile);
        return ids;
    }

    public void setIdsTiersCourrier(Set<String> idsTiersCourrier) {
        this.idsTiersCourrier = idsTiersCourrier;
    }

    public void addAll(List<MembresFamilleContainer> containers) {
        for (MembresFamilleContainer container : containers) {
            idsTiersDomicile.addAll(container.getIdsTiersDomicile());
            idsTiersCourrier.addAll(container.getIdsTiersCourrier());
            mapMembresFamilles.putAll(container.getMapMembresFamilles());
        }
    }

}
