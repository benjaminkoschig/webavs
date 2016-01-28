/**
 * class CPDecisionsAvecMiseEnCompteManager écrit le 19/01/05 par JPA
 * 
 * class manager pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPLienCommentaireRemarqueTypeManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEmplacement = "";
    private String forIdCommentaire = "";
    private String forIdRemarque = "";
    private String forLangue = "";

    @Override
    protected String _getDbSchema() {
        return super._getDbSchema();
    }

    /**
     * déclaration de la clause select :
     */
    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        String champ1 = _getCollection() + "CPCORTP.ISICRE";
        String champ2 = _getCollection() + "CPCORTP.IMTICO";
        String champ3 = _getCollection() + "CPREMAP.IOIDRE";
        String champ4 = _getCollection() + "CPREMAP.IOTLAN";
        String champ5 = _getCollection() + "CPREMAP.IOTEMP";
        String champ6 = _getCollection() + "CPREMAP.IOTEXT";
        return champ1 + " , " + champ2 + " , " + champ3 + " , " + champ4 + " , " + champ5 + " , " + champ6;
    }

    /**
     * déclaration de la clause from :
     * 
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = "";
        sqlFrom += _getCollection() + "CPCORTP " + "INNER JOIN " + _getCollection() + "CPREMAP " + "ON " + "("
                + _getCollection() + "CPCORTP.IOIDRE=" + _getCollection() + "CPREMAP.IOIDRE) ";
        return sqlFrom;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */

    @Override
    protected String _getOrder(BStatement statement) {
        return "IOIDRE";
    }

    /**
     * déclaration de la clause where :
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // pour l'id de passage
        if (getForIdCommentaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IMTICO=" + _dbWriteNumeric(statement.getTransaction(), getForIdCommentaire());
        }
        // pour le type de décision
        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOIDRE=" + _dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }
        // pour une langue
        if (getForLangue().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOTLAN=" + _dbWriteNumeric(statement.getTransaction(), getForLangue());
        }
        // pour un emplacement
        if (getForEmplacement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IOTEMP=" + _dbWriteNumeric(statement.getTransaction(), getForEmplacement());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPLienCommentaireRemarqueType();
    }

    public String getForEmplacement() {
        return forEmplacement;
    }

    public String getForIdCommentaire() {
        return forIdCommentaire;
    }

    public String getForIdRemarque() {
        return forIdRemarque;
    }

    public String getForLangue() {
        return forLangue;
    }

    public void setForEmplacement(String forEmplacement) {
        this.forEmplacement = forEmplacement;
    }

    public void setForIdCommentaire(String string) {
        forIdCommentaire = string;
    }

    public void setForIdRemarque(String string) {
        forIdRemarque = string;
    }

    public void setForLangue(String forIdLangue) {
        forLangue = forIdLangue;
    }
}
