package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Cr�� le 10 f�vr. 06
 * 
 * @author dch
 * 
 *         Manager d'historiques de rubriques (ALHistoriqueRubrique)
 */
public class ALHistoriqueRubriquesManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idHistoriqueRubriques = null;
    private String idDetailPrestation = null;

    /**
     * Cr�e une nouvelle entit�.
     * 
     * @return la nouvelle entit�
     * @exception java.lang.Exception si la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALHistoriqueRubriques();
    }

    /**
     * Renvoie la composante de s�lection de la requ�te SQL (sans le mot-cl� WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = null;

        if (idHistoriqueRubriques != null && !idHistoriqueRubriques.equals("")) {
            if (where == null) {
                where = "A7ID = " + _dbWriteNumeric(statement.getTransaction(), idHistoriqueRubriques);
            } else {
                where += " AND A7ID = " + _dbWriteNumeric(statement.getTransaction(), idHistoriqueRubriques);
            }
        }

        if (idDetailPrestation != null && !idDetailPrestation.equals("")) {
            if (where == null) {
                where = "A7IDDT = " + _dbWriteNumeric(statement.getTransaction(), idDetailPrestation);
            } else {
                where += " AND A7IDDT = " + _dbWriteNumeric(statement.getTransaction(), idDetailPrestation);
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
    public String getIdHistoriqueRubriques() {
        return idHistoriqueRubriques;
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
    public void setIdHistoriqueRubriques(String string) {
        idHistoriqueRubriques = string;
    }
}