package globaz.naos.db.statOfas;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import java.io.Serializable;

public class AFStatOFASControleManager extends AFControleEmployeurManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forIsRapportDifference = new Boolean(false);
    private java.lang.String forNotTypeControle;
    private java.lang.String forTypeControle;
    private java.lang.String forTypeReviseur;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement)
                + ", AFREVIP.MIIREV, AFREVIP.MIITIE, AFREVIP.MILVIS, AFREVIP.MILNOM, AFREVIP.MITTYR";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "AFREVIP";
        from = from + " INNER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".MILVIS="
                + _getCollection() + "AFCONTP.MDLNOM)";
        return from;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForTypeReviseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MITTYR=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeReviseur());
        }
        // traitement du positionnement
        if (getForIsRapportDifference().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDBERR="
                    + this._dbWriteBoolean(statement.getTransaction(), getForIsRapportDifference(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "forIsRapportDifference");
        }
        if (!JadeStringUtil.isEmpty(getForTypeControle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeControle());
        }
        if (!JadeStringUtil.isEmpty(getForNotTypeControle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDTGEN<>" + this._dbWriteNumeric(statement.getTransaction(), getForNotTypeControle());
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFControleEmployeur();
    }

    public Boolean getForIsRapportDifference() {
        return forIsRapportDifference;
    }

    public java.lang.String getForNotTypeControle() {
        return forNotTypeControle;
    }

    public java.lang.String getForTypeControle() {
        return forTypeControle;
    }

    public java.lang.String getForTypeReviseur() {
        return forTypeReviseur;
    }

    public void setForIsRapportDifference(Boolean forIsRapportDifference) {
        this.forIsRapportDifference = forIsRapportDifference;
    }

    public void setForNotTypeControle(java.lang.String forNotTypeControle) {
        this.forNotTypeControle = forNotTypeControle;
    }

    public void setForTypeControle(java.lang.String forTypeControle) {
        this.forTypeControle = forTypeControle;
    }

    public void setForTypeReviseur(java.lang.String forTypeReviseur) {
        this.forTypeReviseur = forTypeReviseur;
    }

}
