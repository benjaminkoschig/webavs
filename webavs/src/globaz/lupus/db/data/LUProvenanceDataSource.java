/*
 * Créé le 18 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUProvenanceDataSource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class provenance {
        private String csType;
        private String valeur;

        public String getCsType() {
            return csType;
        }

        public String getValeur() {
            return valeur;
        }

        public void setCsType(String string) {
            csType = string;
        }

        public void setValeur(String string) {
            valeur = string;
        }
    }

    private ArrayList c;

    public void addProvenance(String csTypeProvenance, String valeur) {
        provenance p = new provenance();
        p.setCsType(csTypeProvenance);
        p.setValeur(valeur);
        if (c == null) {
            c = new ArrayList();
        }
        c.add(p);
    }

    public provenance getProvenance(int i) {
        if (c == null) {
            return null;
        } else {
            return (provenance) c.get(i);
        }
    }

    public int size() {
        if (c == null) {
            return 0;
        } else {
            return c.size();
        }
    }

}
