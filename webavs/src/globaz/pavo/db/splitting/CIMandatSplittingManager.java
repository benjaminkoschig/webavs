package globaz.pavo.db.splitting;

import globaz.globall.db.BManager;

/**
 * Manager des mandats de splitting. Date de création : (15.10.2002 10:43:58)
 * 
 * @author: dgi
 */
public class CIMandatSplittingManager extends BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String aTraiter = new String();
    private String forIdDossierSplitting = new String();
    private String forIdEtat = new String();
    private String forIdTiersPartenaire = new String();
    private String order = "KEAFIN DESC";

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
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
            sqlWhere += _getCollection() + "CISPMAP.KDID="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDossierSplitting());
        }
        // traitement du positionnement
        if (getForIdTiersPartenaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KEITPA=" + _dbWriteNumeric(statement.getTransaction(), getForIdTiersPartenaire());
        }
        // traitement du positionnement
        if (getForIdEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KEIETA=" + _dbWriteNumeric(statement.getTransaction(), getForIdEtat());
        }
        if (getATraiter().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(KEIETA=" + CIMandatSplitting.CS_DEMANDE_REVOCATION + " OR KEIETA="
                    + CIMandatSplitting.CS_SPLITTING_EN_COURS + ")";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CIMandatSplitting();
    }

    /**
     * @return
     */
    public String getATraiter() {
        return aTraiter;
    }

    public java.lang.String getForIdDossierSplitting() {
        return forIdDossierSplitting;
    }

    public java.lang.String getForIdEtat() {
        return forIdEtat;
    }

    public java.lang.String getForIdTiersPartenaire() {
        return forIdTiersPartenaire;
    }

    /**
     * @param string
     */
    public void setATraiter(String string) {
        aTraiter = string;
    }

    public void setForIdDossierSplitting(java.lang.String newForIdDossierSplitting) {
        forIdDossierSplitting = newForIdDossierSplitting;
    }

    public void setForIdEtat(java.lang.String newForIdEtat) {
        forIdEtat = newForIdEtat;
    }

    public void setForIdTiersPartenaire(java.lang.String newForIdTiersPartenaire) {
        forIdTiersPartenaire = newForIdTiersPartenaire;
    }

    public void setOrderByPeriode() {
        order = "KEADEB";
    }

}
