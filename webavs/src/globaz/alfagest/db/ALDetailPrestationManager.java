package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Cr�� le 10 f�vr. 06
 * 
 * @author dch
 * 
 *         Manager de d�tails de prestations (ALDetailPrestation)
 */
public class ALDetailPrestationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDetailPrestation = null;
    private String idEntetePrestation = null;

    /**
     * Cr�e une nouvelle entit�.
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDetailPrestation();
    }

    /**
     * Renvoie la composante de s�lection de la requ�te SQL (sans le mot-cl� WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (idDetailPrestation != null && !idDetailPrestation.equals("")) {
            if (where == null) {
                where = "NID = " + _dbWriteNumeric(statement.getTransaction(), idDetailPrestation);
            } else {
                where += " AND NID = " + _dbWriteNumeric(statement.getTransaction(), idDetailPrestation);
            }
        }

        if (idEntetePrestation != null && !idEntetePrestation.equals("")) {
            if (where == null) {
                where = "NIDE = " + _dbWriteNumeric(statement.getTransaction(), idEntetePrestation);
            } else {
                where += " AND NIDE = " + _dbWriteNumeric(statement.getTransaction(), idEntetePrestation);
            }
        }

        return where;
    }

    /**
     * @return
     */
    public String getIdDetailPrestation() {
        return idDetailPrestation;
    }

    /**
     * @return
     */
    public String getIdEntetePrestation() {
        return idEntetePrestation;
    }

    /**
     * @param string
     */
    public void setIdDetailPrestation(String string) {
        idDetailPrestation = string;
    }

    /**
     * @param string
     */
    public void setIdEntetePrestation(String string) {
        idEntetePrestation = string;
    }
}