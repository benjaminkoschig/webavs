package globaz.pavo.db.splitting;

import globaz.globall.db.BManager;

/**
 * Manager des revenus concernant le splitting. Date de création : (15.10.2002 10:45:56)
 * 
 * @author: dgi
 */
public class CIRevenuSplittingManager extends BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdMandatSplitting = new String();
    private java.lang.String fromAnnee = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CISPREP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "KFANNE DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdMandatSplitting().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KEID=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandatSplitting());
        }
        // traitement du positionnement
        if (getFromAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KFANNE=" + _dbWriteNumeric(statement.getTransaction(), getFromAnnee());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CIRevenuSplitting();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdMandatSplitting() {
        return forIdMandatSplitting;
    }

    public java.lang.String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * Setter
     */
    public void setForIdMandatSplitting(java.lang.String newForIdMandatSplitting) {
        forIdMandatSplitting = newForIdMandatSplitting;
    }

    public void setFromAnnee(java.lang.String newFromAnnee) {
        fromAnnee = newFromAnnee;
    }
}
