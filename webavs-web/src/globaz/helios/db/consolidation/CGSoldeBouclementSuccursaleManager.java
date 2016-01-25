package globaz.helios.db.consolidation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.helios.api.consolidation.ICGConsolidationSoldeManager;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGSoldeBouclementSuccursaleManager extends BManager implements Serializable, ICGConsolidationSoldeManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forEstPeriode = new Boolean("true");
    private String forIdCompte = new String();
    private String forIdExerComptable = new String();
    private String forIdMandat = new String();
    private String forIdPeriodeComptable = new String();
    private String forIdSuccursale = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CGSoldeBouclementSuccursale.TABLE_NAME;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        sqlWhere += CGSoldeBouclementSuccursale.FIELD_ESTPERIODE + "="
                + _dbWriteBoolean(statement.getTransaction(), getForEstPeriode(), BConstants.DB_TYPE_BOOLEAN_CHAR);

        if (!JadeStringUtil.isBlank(getForIdSuccursale())) {
            sqlWhere = addAndToWhere(sqlWhere);
            sqlWhere += CGSoldeBouclementSuccursale.FIELD_IDSUCCURSALE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdSuccursale());
        }

        if (!JadeStringUtil.isBlank(getForIdMandat())) {
            sqlWhere = addAndToWhere(sqlWhere);
            sqlWhere += CGSoldeBouclementSuccursale.FIELD_IDMANDAT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (!JadeStringUtil.isBlank(getForIdExerComptable())) {
            sqlWhere = addAndToWhere(sqlWhere);
            sqlWhere += CGSoldeBouclementSuccursale.FIELD_IDEXERCICECOMPTABLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerComptable());
        }

        if (!JadeStringUtil.isBlank(getForIdPeriodeComptable())) {
            sqlWhere = addAndToWhere(sqlWhere);
            sqlWhere += CGSoldeBouclementSuccursale.FIELD_IDPERIODECOMPTABLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
        }

        if (!JadeStringUtil.isBlank(getForIdCompte())) {
            sqlWhere = addAndToWhere(sqlWhere);
            sqlWhere += CGSoldeBouclementSuccursale.FIELD_IDCOMPTE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGSoldeBouclementSuccursale();
    }

    private String addAndToWhere(String sqlWhere) {
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        return sqlWhere;
    }

    @Override
    public Boolean getForEstPeriode() {
        return forEstPeriode;
    }

    @Override
    public String getForIdCompte() {
        return forIdCompte;
    }

    @Override
    public String getForIdExerComptable() {
        return forIdExerComptable;
    }

    @Override
    public String getForIdMandat() {
        return forIdMandat;
    }

    @Override
    public String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    public String getForIdSuccursale() {
        return forIdSuccursale;
    }

    @Override
    public void setForEstPeriode(Boolean forEstPeriode) {
        this.forEstPeriode = forEstPeriode;
    }

    @Override
    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    @Override
    public void setForIdExerComptable(String forIdExerciceComptable) {
        forIdExerComptable = forIdExerciceComptable;
    }

    @Override
    public void setForIdMandat(String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    @Override
    public void setForIdPeriodeComptable(String forIdPeriodeComptable) {
        this.forIdPeriodeComptable = forIdPeriodeComptable;
    }

    @Override
    public void setForIdSuccursale(String forIdSuccursale) {
        this.forIdSuccursale = forIdSuccursale;
    }
}
