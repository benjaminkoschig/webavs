package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 10 févr. 06
 * 
 * @author dch
 * 
 *         Manager de détails de prestations (ALDetailPrestation)
 */
public class ALDetailPrestationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDetailPrestation = null;
    private String idEntetePrestation = null;

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALDetailPrestation();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
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