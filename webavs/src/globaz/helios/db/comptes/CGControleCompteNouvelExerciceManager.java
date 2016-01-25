/**
 *
 */
package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;

/**
 * @author SEL
 * 
 */
public class CGControleCompteNouvelExerciceManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String currentIdExercice = "";

    private String nextIdExercice = "";
    private String tablePlan = _getCollection() + "CGPLANP";

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(tablePlan);
        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(subQueryPlan());
        sqlFrom.append(" ON ");
        sqlFrom.append(tablePlan).append(".").append("IDCOMPTE");
        sqlFrom.append(" = ");
        sqlFrom.append("p2.IDCOMPTE");
        sqlFrom.append(" AND ");
        sqlFrom.append(tablePlan).append(".").append("IDEXTERNE");
        sqlFrom.append(" = ");
        sqlFrom.append("p2.IDEXTERNE");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // where p1.IDEXERCOMPTABLE=11
        // and p1.AREOUVRIR='1'
        // and p2.idcompte is null

        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(tablePlan);
        sqlWhere.append(".IDEXERCOMPTABLE = ");
        sqlWhere.append(getCurrentIdExercice());

        sqlWhere.append(" AND ");
        sqlWhere.append(tablePlan).append(".AREOUVRIR = ");
        sqlWhere.append(BConstants.DB_BOOLEAN_TRUE_DELIMITED);

        sqlWhere.append(" AND ");
        sqlWhere.append("p2.IDCOMPTE IS NULL");

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGPlanComptableViewBean();
    }

    /**
     * @return the currentIdExercice
     */
    public String getCurrentIdExercice() {
        return currentIdExercice;
    }

    // select p1.idexercomptable, p1.idcompte, p1.idexterne, p2.idexterne, p2.idcompte, p2.idexercomptable
    // from ciciweb.cgplanp p1
    // left outer join
    // (select * from ciciweb.cgplanp p where p.IDEXERCOMPTABLE=13 and p.AREOUVRIR='1') p2
    // on p1.idcompte=p2.idcompte
    // and p1.idexterne=p2.idexterne

    // where p1.IDEXERCOMPTABLE=11
    // and p1.AREOUVRIR='1'
    // and p2.idcompte is null

    /**
     * @return the nextIdExercice
     */
    public String getNextIdExercice() {
        return nextIdExercice;
    }

    /**
     * @param currentIdExercice
     *            the currentIdExercice to set
     */
    public void setCurrentIdExercice(String currentIdExercice) {
        this.currentIdExercice = currentIdExercice;
    }

    /**
     * @param nextIdExercice
     *            the nextIdExercice to set
     */
    public void setNextIdExercice(String nextIdExercice) {
        this.nextIdExercice = nextIdExercice;
    }

    /**
     * @return
     */
    private String subQueryPlan() {
        StringBuffer subQuery = new StringBuffer();

        subQuery.append("(");
        // select * from ciciweb.cgplanp p where p.IDEXERCOMPTABLE=13 and p.AREOUVRIR='1'
        subQuery.append("SELECT * FROM ");
        subQuery.append(_getCollection());
        subQuery.append("CGPLANP p");
        subQuery.append(" WHERE ");
        subQuery.append("p.IDEXERCOMPTABLE=");
        subQuery.append(getNextIdExercice());
        subQuery.append(" AND ");
        subQuery.append("p.AREOUVRIR=");
        subQuery.append(BConstants.DB_BOOLEAN_TRUE_DELIMITED);

        subQuery.append(") p2");

        return subQuery.toString();
    }

}
