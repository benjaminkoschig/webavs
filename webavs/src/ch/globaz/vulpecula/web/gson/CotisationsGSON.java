package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

/**
 * @author Arnaud Geiser (AGE) | Créé le 30 avr. 2014
 * 
 */
public class CotisationsGSON implements Serializable, Comparable<CotisationsGSON> {
    private static final long serialVersionUID = 3837248260596799868L;

    private static final Map<TypeAssurance, Integer> mappingOrder = new HashMap<TypeAssurance, Integer>();
    static {
        mappingOrder.put(TypeAssurance.COTISATION_LPP, 10);
        mappingOrder.put(TypeAssurance.COTISATION_AVS_AI, 20);
        mappingOrder.put(TypeAssurance.ASSURANCE_CHOMAGE, 30);
        mappingOrder.put(TypeAssurance.ASSURANCE_MALADIE, 40);
        mappingOrder.put(TypeAssurance.COTISATION_RETAVAL, 50);
        mappingOrder.put(TypeAssurance.COTISATION_AF, 60);
    }

    private static final int DEFAULT_ORDER = 100;

    private final String idAssurance;
    private final String taux;
    private final String libelle;
    private final int order;

    public CotisationsGSON(String idAssurance, String taux, String libelle, TypeAssurance typeAssurance) {
        this.idAssurance = idAssurance;
        this.taux = taux;
        this.libelle = libelle;
        order = getOrder(typeAssurance);
    }

    private int getOrder(TypeAssurance typeAssurance) {
        if (mappingOrder.containsKey(typeAssurance)) {
            return mappingOrder.get(typeAssurance);
        } else {
            return DEFAULT_ORDER;
        }
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public String getTaux() {
        return taux;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idAssurance == null) ? 0 : idAssurance.hashCode());
        result = prime * result + ((taux == null) ? 0 : taux.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CotisationsGSON other = (CotisationsGSON) obj;
        if (idAssurance == null) {
            if (other.idAssurance != null) {
                return false;
            }
        } else if (!idAssurance.equals(other.idAssurance)) {
            return false;
        }
        if (taux == null) {
            if (other.taux != null) {
                return false;
            }
        } else if (!taux.equals(other.taux)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(CotisationsGSON o) {
        return order - o.order;
    }
}
