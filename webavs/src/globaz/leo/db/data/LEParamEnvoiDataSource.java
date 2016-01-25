/*
 * Créé le 15 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEParamEnvoiDataSource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class paramEnvoi implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String csType;
        private String valeur;

        /**
         * @return
         */
        public String getCsType() {
            return csType;
        }

        /**
         * @return
         */
        public String getValeur() {
            return valeur;
        }

        /**
         * @param string
         */
        public void setCsType(String string) {
            csType = string;
        }

        /**
         * @param string
         */
        public void setValeur(String string) {
            valeur = string;
        }

    }

    ArrayList c;

    public void addParamEnvoi(String csTypeParam, String valeur) {
        paramEnvoi p = new paramEnvoi();
        p.setCsType(csTypeParam);
        p.setValeur(valeur);
        if (c == null) {
            c = new ArrayList();
        }
        if (c.indexOf(p) >= 0) {
            c.remove(c.indexOf(p));
        }
        c.add(p);
    }

    public paramEnvoi getParamEnvoi(int i) {
        if (c == null) {
            return null;
        } else {
            return (paramEnvoi) c.get(i);
        }
    }

    public paramEnvoi getParamEnvoi(String csType) {
        paramEnvoi param = null;
        if (c == null) {
            return null;
        } else {
            for (int i = 0; i < c.size(); i++) {
                paramEnvoi p = (paramEnvoi) c.get(i);
                if (p.getCsType().equals(csType)) {
                    param = p;
                    break;
                }
            }
        }

        return param;
    }

    public void remove(String csTypeParam) {
        if (c != null && csTypeParam != null) {
            for (int i = 0; i < c.size(); i++) {
                paramEnvoi p = (paramEnvoi) c.get(i);
                if (csTypeParam.equals(p.getCsType())) {
                    c.remove(i);
                    break;
                }
            }
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
