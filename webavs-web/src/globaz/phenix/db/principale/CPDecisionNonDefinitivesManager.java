/**
 * class CPDecisionsAPrendreMAnanger écrit le 19/01/05 par JPA
 * 
 * class manager pour les décisions à prendre càd les affilies qui n'ont pas de décision définitive pour une certaine
 * année
 * 
 * @author JPA
 */
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPDecisionNonDefinitivesManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String order = "";

    @Override
    protected String _getDbSchema() {
        return super._getDbSchema();
    }

    /**
     * déclaration de la clause select :
     */
    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        return _getFieldsForRepriseCotPers();
    }

    protected String _getFieldsForRepriseCotPers() {
        String champ1 = "TEMP.MALNAF ";
        String champ2 = _getCollection() + "CPDECIP.HTITIE ";
        String champ3 = "TEMP.IAANNE ";
        String champ4 = "IADDEB ";
        String champ5 = "IADFIN ";
        String champ6 = "IATTDE ";
        String champ7 = "HXNAVS ";
        String champ8 = _getCollection() + "CPDECIP.IAIDEC ";
        String champ9 = "HTLDE1";
        String champ10 = "HTLDE2";
        String champ11 = "IATSPE";
        return champ1 + " , " + champ2 + " , " + champ3 + " , " + champ4 + " , " + champ5 + " , " + champ6 + " , "
                + champ7 + " , " + champ8 + " , " + champ9 + " , " + champ10 + " , " + champ11;
    }

    /**
     * déclaration de la clause from :
     * 
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = "";
        sqlFrom += " (SELECT MAX(IAIDEC),IAANNE, ";
        sqlFrom += _getCollection() + "AFAFFIP.MALNAF, ";
        sqlFrom += _getCollection() + "AFAFFIP.MAIAFF ";
        sqlFrom += "FROM " + _getCollection() + "CPDECIP ";
        sqlFrom += "INNER JOIN " + _getCollection() + "AFAFFIP ON ";
        sqlFrom += _getCollection() + "CPDECIP.MAIAFF= ";
        sqlFrom += _getCollection() + "AFAFFIP.MAIAFF ";
        if (getForAnnee() != null) {
            sqlFrom += "WHERE IAANNE=" + _dbWriteNumeric(statement.getTransaction(), getForAnnee())
                    + " AND IAACTI='1' ";
        }
        sqlFrom += " AND " + _getCollection() + "CPDECIP.IATTDE <> 605005 ";
        if (getForAnnee() != null) {
            sqlFrom += "AND ((" + _getCollection() + "AFAFFIP.MADFIN = 0 OR ";
            sqlFrom += _getCollection() + "AFAFFIP.MADFIN > "
                    + _dbWriteNumeric(statement.getTransaction(), getForAnnee()) + "0101) AND ";
            sqlFrom += _getCollection() + "AFAFFIP.MADDEB < "
                    + _dbWriteNumeric(statement.getTransaction(), getForAnnee()) + "1231) ";
        }
        sqlFrom += "GROUP BY IAANNE, MALNAF, " + _getCollection() + "AFAFFIP.MAIAFF) ";
        sqlFrom += "AS TEMP (IAIDEC, IAANNE, MALNAF, MAIAFF), " + _getCollection() + "CPDECIP ";
        sqlFrom += "INNER JOIN " + _getCollection() + "TITIERP ON ";
        sqlFrom += _getCollection() + "TITIERP.HTITIE=" + _getCollection() + "CPDECIP.HTITIE ";
        sqlFrom += "INNER JOIN " + _getCollection() + "TIPAVSP ON ";
        sqlFrom += _getCollection() + "TIPAVSP.HTITIE=" + _getCollection() + "CPDECIP.HTITIE";
        return sqlFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return " MALNAF ASC, IAANNE ASC, IAIDEC DESC ";
    }

    /**
     * déclaration de la clause where :
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        sqlWhere += _getCollection() + "CPDECIP.MAIAFF=TEMP.MAIAFF ";
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CPDECIP.IAANNE=TEMP.IAANNE ";
        }
        // sqlWhere += "AND " + _getCollection() + "CPDECIP.IATSPE <> " +
        // CPDecision.CS_SALARIE_DISPENSE;
        sqlWhere += "AND " + _getCollection() + "CPDECIP.IATTDE ";
        sqlWhere += "NOT IN(" + CPDecision.CS_DEFINITIVE + " , " + CPDecision.CS_REDUCTION + " , "
                + CPDecision.CS_REMISE + " , " + CPDecision.CS_RECTIFICATION
                // + " , "
                // + CPDecision.CS_IMPUTATION
                + " , 0 ) ";
        sqlWhere += "AND TEMP.IAIDEC= " + _getCollection() + "CPDECIP.IAIDEC ";
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPDecisionNonDefinitives();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getOrder() {
        return order;
    }

    public void setForAnnee(String string) {
        forAnnee = string;
    }

    public void setOrder(String string) {
        order = string;
    }
}
