package ch.globaz.pegasus.business.domaine.membreFamille;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MembresFamilles {
    private Map<RoleMembreFamille, List<MembreFamille>> membresFamilles = new TreeMap<RoleMembreFamille, List<MembreFamille>>();

    public int countNbEnfant() {
        if (membresFamilles.containsKey(RoleMembreFamille.ENFANT)) {
            return membresFamilles.get(RoleMembreFamille.ENFANT).size();
        }
        return 0;
    }

    public int countRequerantConjoint() {
        int sum = 0;
        if (hasRequerant()) {
            sum = 1;
        }
        if (hasConjoint()) {
            sum = sum + 1;
        }
        return sum;
    }

    public boolean hasEnfant() {
        return membresFamilles.containsKey(RoleMembreFamille.ENFANT);
    }

    public boolean hasRequerant() {
        return membresFamilles.containsKey(RoleMembreFamille.REQUERANT);
    }

    public boolean hasConjoint() {
        return membresFamilles.containsKey(RoleMembreFamille.CONJOINT);
    }

    public boolean add(MembreFamille membreFamille) {
        RoleMembreFamille role = membreFamille.getRoleMembreFamille();
        if (!membresFamilles.containsKey(role)) {
            membresFamilles.put(role, new ArrayList<MembreFamille>());
            return membresFamilles.get(role).add(membreFamille);
        } else if (role.isEnfant()) {
            return membresFamilles.get(role).add(membreFamille);
        }
        return false;
    }

    public int size() {
        return getMembresFamilles().size();
    }

    public List<MembreFamille> getMembresFamilles() {
        List<MembreFamille> list = new ArrayList<MembreFamille>();
        if (!membresFamilles.isEmpty()) {
            for (List<MembreFamille> membres : membresFamilles.values()) {
                list.addAll(membres);
            }
        }
        return list;
    }

    public MembreFamille getRequerant() {
        if (hasRequerant()) {
            return membresFamilles.get(RoleMembreFamille.REQUERANT).get(0);
        } else {
            return new MembreFamille();
        }
    }

    public MembreFamille getConjoint() {
        if (hasConjoint()) {
            return membresFamilles.get(RoleMembreFamille.CONJOINT).get(0);
        } else {
            return new MembreFamille();
        }
    }

    public MembreFamille resolveByRole(RoleMembreFamille roleMembreFamille) {
        List<MembreFamille> membres = membresFamilles.get(roleMembreFamille);
        if (membres == null) {
            return null;
        }
        return membres.get(0);
    }
}
