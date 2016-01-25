/*
 * Cr�� le 3 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LUComplementDataSource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class complement {
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

    public void addComplement(String csTypeComplement, String valeurComplement) {
        complement cpl = new complement();
        cpl.setCsType(csTypeComplement);
        cpl.setValeur(valeurComplement);
        if (c == null) {
            c = new ArrayList();
        }
        c.add(cpl);
    }

    public complement getComplement(int i) {
        if (c == null) {
            return null;
        } else {
            return (complement) c.get(i);
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
