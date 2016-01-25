package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Manager de recaps (ALRecap)
 */
public class ALRecapManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idRecap = null;
    private String etatRecap = null;

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALRecap();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (idRecap != null && !idRecap.equals("")) {
            if (where == null) {
                where = "LID = " + _dbWriteNumeric(statement.getTransaction(), idRecap);
            } else {
                where += " AND LID = " + _dbWriteNumeric(statement.getTransaction(), idRecap);
            }
        }

        if (etatRecap != null && !etatRecap.equals("")) {
            if (where == null) {
                where = "LETAT = " + _dbWriteString(statement.getTransaction(), etatRecap);
            } else {
                where += " AND LETAT = " + _dbWriteString(statement.getTransaction(), etatRecap);
            }
        }

        return where;
    }

    /**
     * @return
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @param string
     */
    public void setIdRecap(String string) {
        idRecap = string;
    }

    public void setEtatRecap(String etat) {
        etatRecap = etat;
    }

    public String getEtatRecap() {
        return etatRecap;
    }
}