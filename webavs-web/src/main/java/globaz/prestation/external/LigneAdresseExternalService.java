package globaz.prestation.external;

import java.io.Serializable;

public class LigneAdresseExternalService implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String newValue;
    private String oldValue;
    private boolean isModified;

    public LigneAdresseExternalService(String ancienneValeur, String nouvelleValeur, boolean estModifie) {
        oldValue = ancienneValeur;
        newValue = nouvelleValeur;
        isModified = estModifie;

    }

    public String getNewValue() {
        return newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public boolean isModified() {
        return isModified;
    }

}
