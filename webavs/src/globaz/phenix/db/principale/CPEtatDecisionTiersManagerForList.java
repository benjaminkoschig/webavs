package globaz.phenix.db.principale;

public class CPEtatDecisionTiersManagerForList extends CPDecisionAffiliationTiersManager implements
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = super._getFrom(statement);
        // from += " INNER JOIN " + _getCollection() +
        // "AFAFFIP AFAFFIP ON AFAFFIP.MAIAFF=" + _getCollection()
        // +"CPDECIP.MAIAFF";
        return from;
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String where = super._getWhere(statement);
        where += " AND " + _getCollection() + "CPDECIP.IADDEB>=AFAFFIP.MADDEB AND " + "(( " + _getCollection()
                + "CPDECIP.IADFIN<=AFAFFIP.MADFIN) OR (COALESCE(AFAFFIP.MADFIN,0)=0))";
        // faire le total suivant le type de décision par collaborateur
        if (isStatistiques()) {
            where += " GROUP BY IARESP, IATTDE ";
        } else if (isForIdAffilieDouble()) {
            where += " GROUP BY HXNAFF, IAANNE HAVING COUNT(*) > 1) ";
        }
        return where;
    }
}
