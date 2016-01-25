package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Cr�� le 18 janv. 06
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
     * Cr�e une nouvelle entit�.
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALRecap();
    }

    /**
     * Renvoie la composante de s�lection de la requ�te SQL (sans le mot-cl� WHERE).
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