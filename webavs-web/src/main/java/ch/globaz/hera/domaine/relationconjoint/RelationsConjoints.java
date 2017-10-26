package ch.globaz.hera.domaine.relationconjoint;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import ch.globaz.common.domaine.Date;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RelationsConjoints {

    private TreeMap<String, TreeSet<RelationConjoint>> map = new TreeMap<String, TreeSet<RelationConjoint>>();

    public void add(RelationConjoint relationConjoint) {
        String key = String.valueOf(relationConjoint.getRequerant().getId());
        if (!map.containsKey(key)) {
            map.put(key, new TreeSet<RelationConjoint>(new Comparator<RelationConjoint>() {

                @Override
                public int compare(RelationConjoint o1, RelationConjoint o2) {
                    if (o1.getDateDebut() == null) {
                        return 1;
                    }
                    if (o2.getDateDebut() == null) {
                        return 1;
                    }
                    return -1 * o1.getDateDebut().compareTo(o2.getDateDebut());
                }
            }));
        }
        map.get(key).add(relationConjoint);
    }

    public RelationsConjoints filtreByPeriode(Date dateDebut) {
        RelationsConjoints relationsConjoints = new RelationsConjoints();
        if (dateDebut == null) {
            return this;
        }
        for (Entry<String, TreeSet<RelationConjoint>> entry : map.entrySet()) {
            long debut = dateDebut.getTime();
            Collection<RelationConjoint> relations = entry.getValue();
            for (RelationConjoint relation : relations) {
                // Si la date de la periode est plus grande ou egale
                if (debut >= relation.getDateDebut().getTime()) {
                    relationsConjoints.add(relation);
                }
            }
        }

        return relationsConjoints;
    }

    public RelationConjoint resolveMostRecent() {
        if (map.isEmpty() || !map.containsKey(map.firstKey())) {
            return new RelationConjoint();
        }
        return map.get(map.firstKey()).first();
    }

    public RelationsConjoints filtreForRequerant(PersonneAVS personneAVS) {
        String idTiers = String.valueOf(personneAVS.getId());
        return filtreForRequerant(idTiers);
    }

    public void addAll(Collection<RelationConjoint> relationsConjoints) {
        for (RelationConjoint relationConjoint : relationsConjoints) {
            add(relationConjoint);
        }
    }

    public RelationsConjoints filtreForRequerant(String idTiers) {
        RelationsConjoints relationsConjoints = new RelationsConjoints();
        Set<RelationConjoint> fitredByTiers = map.get(idTiers);
        if (fitredByTiers != null) {
            relationsConjoints.addAll(fitredByTiers);
        }
        return relationsConjoints;
    }

    public int size() {
        int size = 0;
        for (TreeSet<RelationConjoint> list : map.values()) {
            size = size + list.size();
        }
        return size;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
