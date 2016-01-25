package globaz.naos.db.statOfas;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFStatOFASControleDSManager extends DSDeclarationListViewBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeControle;
    private Boolean forFromContEmpl = new Boolean(false);
    private Boolean forIsFlagDernierRapport = new Boolean(true);
    private Boolean forIsIdRapportPasVide = new Boolean(false);
    private java.lang.String forNotTypeControle;
    private java.lang.String forTypeControle;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "AFCONTP";
        if (forFromContEmpl.booleanValue()) {
            from = from + " INNER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".MDICON="
                    + _getCollection() + "DSDECLP.TAICTR)";
        }
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
        if (getForIsIdRapportPasVide().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(TAICTR IS NOT NULL AND TAICTR<>0)";
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
        if (!JadeStringUtil.isEmpty(getForAnneeControle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TANDEC>=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeControle())
                    + "17000 and TANDEC<=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeControle())
                    + "17999";
        }
        if (getForIsFlagDernierRapport().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDBFDR='1'";
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
        return new DSDeclarationViewBean();
    }

    public String getForAnneeControle() {
        return forAnneeControle;
    }

    public Boolean getForFromContEmpl() {
        return forFromContEmpl;
    }

    public Boolean getForIsFlagDernierRapport() {
        return forIsFlagDernierRapport;
    }

    public Boolean getForIsIdRapportPasVide() {
        return forIsIdRapportPasVide;
    }

    public java.lang.String getForNotTypeControle() {
        return forNotTypeControle;
    }

    public java.lang.String getForTypeControle() {
        return forTypeControle;
    }

    public void setForAnneeControle(String forAnneeControle) {
        this.forAnneeControle = forAnneeControle;
    }

    public void setForFromContEmpl(Boolean forFromContEmpl) {
        this.forFromContEmpl = forFromContEmpl;
    }

    public void setForIsFlagDernierRapport(Boolean forIsFlagDernierRapport) {
        this.forIsFlagDernierRapport = forIsFlagDernierRapport;
    }

    public void setForIsIdRapportPasVide(Boolean forIsIdRapportPasVide) {
        this.forIsIdRapportPasVide = forIsIdRapportPasVide;
    }

    public void setForNotTypeControle(java.lang.String forNotTypeControle) {
        this.forNotTypeControle = forNotTypeControle;
    }

    public void setForTypeControle(java.lang.String forTypeControle) {
        this.forTypeControle = forTypeControle;
    }

}
