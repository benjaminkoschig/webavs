package globaz.corvus.helpers.process;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author SCR
 */
public class REValiderDecisionVO {

    private Map<String, String> mapDemandesPourDiminution = null;
    private Map<String, String> mapDemandesPourMAJEtat = null;

    public void add(REValiderDecisionVO elm) {
        if (mapDemandesPourDiminution == null) {
            mapDemandesPourDiminution = new HashMap<String, String>();
        }

        if (mapDemandesPourMAJEtat == null) {
            mapDemandesPourMAJEtat = new HashMap<String, String>();
        }

        if (elm.getMapDemandesPourDiminution() != null) {
            mapDemandesPourDiminution.putAll(elm.getMapDemandesPourDiminution());
        }

        if (elm.getMapDemandesPourMAJEtat() != null) {
            mapDemandesPourMAJEtat.putAll(elm.getMapDemandesPourMAJEtat());
        }

    }

    public void addDemandePourDiminution(String idDemande, String dateFinJJxMMxAAAA) {
        if (mapDemandesPourDiminution == null) {
            mapDemandesPourDiminution = new HashMap<String, String>();
        }
        mapDemandesPourDiminution.put(idDemande, dateFinJJxMMxAAAA);
    }

    public void addDemandePourMAJEtat(String idDemande, String csEtat) {
        if (mapDemandesPourMAJEtat == null) {
            mapDemandesPourMAJEtat = new HashMap<String, String>();
        }
        mapDemandesPourMAJEtat.put(idDemande, csEtat);
    }

    public Map<String, String> getMapDemandesPourDiminution() {
        return mapDemandesPourDiminution;
    }

    public Map<String, String> getMapDemandesPourMAJEtat() {
        return mapDemandesPourMAJEtat;
    }
}
