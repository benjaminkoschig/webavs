package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 10 févr. 06
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
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALHistoriqueRubriques();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
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