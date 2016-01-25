package globaz.pavo.db.splitting;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

/**
 * Manage les domiciles concernant le splitting. Date de création : (15.10.2002 10:48:34)
 * 
 * @author: dgi
 */
public class CIDomicileSplittingManager extends BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDossierSplitting = new String();
    private String forIdTiersPartenaire = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CISPDOP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "KGDFIN DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdDossierSplitting().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KDID=" + _dbWriteNumeric(statement.getTransaction(), getForIdDossierSplitting());
        }
        // traitement du positionnement
        if (getForIdTiersPartenaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KGITIE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTiersPartenaire());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIDomicileSplitting();
    }

    public java.lang.String getForIdDossierSplitting() {
        return forIdDossierSplitting;
    }

    public java.lang.String getForIdTiersPartenaire() {
        return forIdTiersPartenaire;
    }

    public void setForIdDossierSplitting(java.lang.String newForIdDossierSplitting) {
        forIdDossierSplitting = newForIdDossierSplitting;
    }

    public void setForIdTiersPartenaire(java.lang.String newForIdTiersPartenaire) {
        forIdTiersPartenaire = newForIdTiersPartenaire;
    }
}
