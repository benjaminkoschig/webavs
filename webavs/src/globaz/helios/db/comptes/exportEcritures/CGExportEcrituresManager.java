package globaz.helios.db.comptes.exportEcritures;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGPlanComptableManager;

public class CGExportEcrituresManager extends CGPlanComptableManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // SELECT * FROM AGLAQUA.CGPLANP INNER JOIN AGLAQUA.CGCOMTP ON (AGLAQUA.CGPLANP.IDCOMPTE=AGLAQUA.CGCOMTP.IDCOMPTE)
    // INNER JOIN AGLAQUA.CGSECTP ON (AGLAQUA.CGCOMTP.IDSECTEURAVS=AGLAQUA.CGSECTP.IDSECTEURAVS AND
    // AGLAQUA.CGCOMTP.IDMANDAT=AGLAQUA.CGSECTP.IDMANDAT) LEFT OUTER JOIN AGLAQUA.CGSOLDP ON
    // (AGLAQUA.CGPLANP.IDEXERCOMPTABLE = AGLAQUA.CGSOLDP.IDEXERCOMPTABLE and AGLAQUA.CGSOLDP.ESTPERIODE='1' AND
    // AGLAQUA.CGPLANP.IDCOMPTE=AGLAQUA.CGSOLDP.IDCOMPTE) WHERE AGLAQUA.CGCOMTP.IDMANDAT=900 AND
    // AGLAQUA.CGPLANP.IDEXERCOMPTABLE=6 AND IDPERIODECOMPTABLE=89 AND ( AGLAQUA.CGSOLDP.IDCENTRECHARGE=0 OR
    // AGLAQUA.CGSOLDP.IDCENTRECHARGE is null ) AND EXPORTCOMPTASIEGE='1' ORDER BY IDEXTERNE
    @Override
    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     */
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = _getCollection() + "CGPLANP";
        String table2 = _getCollection() + "CGCOMTP";
        String table3 = _getCollection() + "CGSOLDP";
        String table4 = _getCollection() + "CGSECTP";
        String from = " " + table1;
        from += " INNER JOIN " + table2 + " ON (" + table1 + ".IDCOMPTE=" + table2 + ".IDCOMPTE)";
        from += " INNER JOIN " + table4 + " ON (" + table2 + ".IDSECTEURAVS=" + table4 + ".IDSECTEURAVS AND " + table2
                + ".IDMANDAT=" + table4 + ".IDMANDAT)";
        from += " LEFT OUTER JOIN " + table3 + " ON (" + table1 + ".IDEXERCOMPTABLE = " + table3
                + ".IDEXERCOMPTABLE and " + table3 + ".ESTPERIODE="
                + this._dbWriteBoolean(statement.getTransaction(), getForEstPeriode(), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + " AND " + table1 + ".IDCOMPTE=" + table3 + ".IDCOMPTE) ";
        return from;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        if (sqlWhere.isEmpty()) {
            sqlWhere = "EXPORTCOMPTASIEGE='1'";
        } else {
            sqlWhere += "AND EXPORTCOMPTASIEGE='1'";
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGExportEcritures();
    }

}
