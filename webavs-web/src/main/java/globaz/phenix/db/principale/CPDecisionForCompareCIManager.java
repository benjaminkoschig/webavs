package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CPDecisionForCompareCIManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeCI = "";
    private String forAnneeDecision = "";
    private String fromNoAffilie = "";

    private String tillNoAffilie = "";

    @Override
    protected String _getFrom(BStatement statement) {
        String anneeCI = "";
        if (getForAnneeCI().length() != 0) {
            anneeCI = " AND KBNANN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeCI());
        }
        String sqlFrom = _getCollection() + "CPDECIP AS CPDECIP INNER JOIN " + _getCollection()
                + "AFAFFIP AS AFAFFIP ON (CPDECIP.maiaff=AFAFFIP.maiaff) INNER JOIN " + _getCollection()
                + "CPDOCAP AS CPDOCAP ON (CPDOCAP.IAIDEC=CPDECIP.IAIDEC) LEFT OUTER JOIN " + _getCollection()
                + "CIECRIP AS CIECRIP ON (AFAFFIP.maiaff=CIECRIP.kbitie" + anneeCI + ") LEFT OUTER JOIN "
                + _getCollection() + "CIINDIP AS CIINDIP ON (CIECRIP.kaiind=CIINDIP.kaiind)";

        return sqlFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        // TODO Auto-generated method stub
        return "MALNAF";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = " IATETA IN (604004, 604009, 604005, 604006) AND  (IAACTI = '1'or (IAACTI='2' and (IADDEB>=MADFIN AND MADFIN!=0)or IADFIN<=MADDEB))"
                + " AND IATTDE <>605008 AND IATSPE <> 609020 AND CPDOCAP.IHIDCA=600021 AND  ((KBTCOD not in (313001)"
                + " AND  (KBTGEN IN (310003, 310004, 310009, 310002) OR KBTGEN =  310007 and KBTSPE IN (312002,312001,0,312004))) OR KANAVS IS NULL)";

        // traitement du positionnement
        if (getForAnneeDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IAANNE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDecision());
        }
        // traitement du positionnement
        if (getFromNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF>=" + this._dbWriteString(statement.getTransaction(), getFromNoAffilie());
        }

        // traitement du positionnement
        if (getTillNoAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF<=" + this._dbWriteString(statement.getTransaction(), getTillNoAffilie());
        }
        sqlWhere += " GROUP BY AFAFFIP.MALNAF, CIINDIP.KANAVS, CPDECIP.HTITIE, CPDECIP.IATGAF, CPDECIP.IAANNE, CIINDIP.KAIIND";
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPDecisionForCompareCI();
    }

    public String getForAnneeCI() {
        return forAnneeCI;
    }

    public String getForAnneeDecision() {
        return forAnneeDecision;
    }

    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    public String getTillNoAffilie() {
        return tillNoAffilie;
    }

    public void setForAnneeCI(String forAnneeCI) {
        this.forAnneeCI = forAnneeCI;
    }

    public void setForAnneeDecision(String forAnneeDecision) {
        this.forAnneeDecision = forAnneeDecision;
    }

    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    public void setTillNoAffilie(String tillNoAffilie) {
        this.tillNoAffilie = tillNoAffilie;
    }

}
