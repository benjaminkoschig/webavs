package globaz.pavo.db.compte;

import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (16.07.2003 13:15:59)
 * 
 * @author: Administrator
 */
public class CIEcritureCounter extends CIEcritureManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CICountEcriture.
     */
    public CIEcritureCounter() {
        super();
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIECRIP";
    }
}
