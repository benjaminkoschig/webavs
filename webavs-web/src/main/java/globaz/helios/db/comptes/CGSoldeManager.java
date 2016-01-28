package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.api.consolidation.ICGConsolidationSoldeManager;
import globaz.jade.client.util.JadeStringUtil;

public class CGSoldeManager extends BManager implements ICGConsolidationSoldeManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forEstPeriode = new Boolean("true");
    private String forIdCentreCharge = "";
    private String forIdCompte = "";
    private String forIdExerComptable = "";
    private String forIdMandat = "";
    private String forIdPeriodeComptable = "";

    /** Fichier CGSOLDP */

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CGSOLDP";
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
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // on applique tj le critere sur ESTPERIODE
        sqlWhere += "ESTPERIODE="
                + _dbWriteBoolean(statement.getTransaction(), getForEstPeriode(), BConstants.DB_TYPE_BOOLEAN_CHAR);

        if (getForIdCompte() != null && getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTE=" + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        if (getForIdPeriodeComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPERIODECOMPTABLE=" + _dbWriteNumeric(statement.getTransaction(), getForIdPeriodeComptable());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdCentreCharge())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCENTRECHARGE=" + _dbWriteNumeric(statement.getTransaction(), getForIdCentreCharge());
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCENTRECHARGE=" + _dbWriteNumeric(statement.getTransaction(), "0");
        }

        if (getForIdExerComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXERCOMPTABLE=" + _dbWriteNumeric(statement.getTransaction(), getForIdExerComptable());
        }

        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGSolde();
    }

    /**
     * Return la collection en cours utilisée par l'application.
     * 
     * @return
     */
    public String getCollection() {
        // TODO Remplacer par méthode public du fw (quand cette dernière sera
        // implémentée)
        return _getCollection();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 14:49:32)
     * 
     * @return String
     */
    @Override
    public Boolean getForEstPeriode() {
        return forEstPeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 17:09:35)
     * 
     * @return String
     */
    public String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 14:49:53)
     * 
     * @return String
     */
    @Override
    public String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 17:10:04)
     * 
     * @return String
     */
    @Override
    public String getForIdExerComptable() {
        return forIdExerComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:53:41)
     * 
     * @return String
     */
    @Override
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 16:25:52)
     * 
     * @return String
     */
    @Override
    public String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 14:49:32)
     * 
     * @param newForEstPeriode
     *            String
     */
    @Override
    public void setForEstPeriode(Boolean newForEstPeriode) {
        forEstPeriode = newForEstPeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 17:09:35)
     * 
     * @param newForIdCentreCharge
     *            String
     */
    public void setForIdCentreCharge(String newForIdCentreCharge) {
        forIdCentreCharge = newForIdCentreCharge;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 14:49:53)
     * 
     * @param newForIdCompte
     *            String
     */
    @Override
    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 17:10:04)
     * 
     * @param newForIdExerComptable
     *            String
     */
    @Override
    public void setForIdExerComptable(String newForIdExerComptable) {
        forIdExerComptable = newForIdExerComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.03.2003 11:53:41)
     * 
     * @param newForIdMandat
     *            String
     */
    @Override
    public void setForIdMandat(String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 16:25:52)
     * 
     * @param newForIdPeriodeComptable
     *            String
     */
    @Override
    public void setForIdPeriodeComptable(String newForIdPeriodeComptable) {
        forIdPeriodeComptable = newForIdPeriodeComptable;
    }

    @Override
    public void setForIdSuccursale(String idSuccursale) {
        // Do nothing. Only used by CGSoldeBouclementSuccursaleManager.
    }
}
