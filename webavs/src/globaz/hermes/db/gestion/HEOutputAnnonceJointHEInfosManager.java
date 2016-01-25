package globaz.hermes.db.gestion;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.pavo.application.CIApplication;

public class HEOutputAnnonceJointHEInfosManager extends HEOutputAnnonceListViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = new String();
    private String forMotifLettreCA = "";
    private String forTypeInco = new String();
    private String forTypeIncoNotNull = new String();
    private transient String fromClause = null;
    private String fromIdLottemp = new String();
    private boolean isCodeTraitement = false;
    private boolean isNNSS = false;
    private boolean isNotMotifPavo = false;
    private boolean isPrintCSV = false;
    private boolean isPrintCSVEmpl = false;
    private String order = new String();
    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */

    private String toIdLottemp = new String();

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(HEOutputAnnonceJointHEInfos.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer order = new StringBuffer();
        if (!JadeStringUtil.isEmpty(getOrder())) {
            order.append(getOrder());
        } else {
            // order.append(_getCollection() + "HEANNOP.RNIANN, TYPEINCO DESC");
            order.append("HEANNOP.RNIANN, TYPEINCO DESC");
        }
        return order.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // -- composant de la requete initialises avec les options par defaut
        String sqlWhere = super._getWhere(statement);

        // AJOUT
        if (getFromIdLottemp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RMILOT >=" + getFromIdLottemp();
        }

        if (getToIdLottemp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RMILOT <=" + getToIdLottemp();
        }

        if (getForMotifLettreCA().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += getForMotifLettreCA();
        }

        if (isNNSS()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " RNBNNS ='1' ";
        }

        // Impression des certificats uniquement pour le Code Traitement 0
        if (isCodeTraitement()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " SUBSTR(RNLENR,111,1)='0' ";
        }

        // Motif qui ne doive pas être imprimer dans le cas ou le producteur est
        // PAVO
        if (isNotMotifPavo()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ((RNTPRO NOT IN ('" + CIApplication.DEFAULT_APPLICATION_PAVO + "', '"
                    + AFApplication.DEFAULT_APPLICATION_NAOS + "')) OR (RNMOT NOT IN ('36', '46'))) ";
        }
        if (isPrintCSV()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (RNTPRO IN ('" + CIApplication.DEFAULT_APPLICATION_PAVO + "', '"
                    + AFApplication.DEFAULT_APPLICATION_NAOS + "') AND (RNMOT IN ('36', '46'))) ";
        }
        if (isPrintCSVEmpl()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (RNTPRO IN ('" + CIApplication.DEFAULT_APPLICATION_PAVO + "', '"
                    + AFApplication.DEFAULT_APPLICATION_NAOS + "') AND RNMOT = '36') ";
        }
        if (!JadeStringUtil.isEmpty(getForTypeInco())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " TYPEINCO=" + getForTypeInco();
        }

        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {

        return new HEOutputAnnonceJointHEInfos();
    }

    @Override
    public String getForIdLot() {
        return forIdLot;
    }

    @Override
    public String getForMotifLettreCA() {
        return forMotifLettreCA;
    }

    public String getForTypeInco() {
        return forTypeInco;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getFromIdLottemp() {
        return fromIdLottemp;
    }

    @Override
    public String getOrder() {
        return order;
    }

    public String getToIdLottemp() {
        return toIdLottemp;
    }

    public boolean isCodeTraitement() {
        return isCodeTraitement;
    }

    public boolean isNNSS() {
        return isNNSS;
    }

    public boolean isNotMotifPavo() {
        return isNotMotifPavo;
    }

    public boolean isPrintCSV() {
        return isPrintCSV;
    }

    public boolean isPrintCSVEmpl() {
        return isPrintCSVEmpl;
    }

    public void setCodeTraitement(boolean isCodeTraitement) {
        this.isCodeTraitement = isCodeTraitement;
    }

    @Override
    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    @Override
    public void setForMotifLettreCA(String forMotifLettreCA) {
        if (forMotifLettreCA.length() != 0) {
            String[] temp = forMotifLettreCA.split(",");
            StringBuffer query = new StringBuffer();

            query.append("( ");
            for (int i = 0; i < temp.length; i++) {
                query.append("RNMOT='");
                query.append(temp[i] + "'");
                if (!(i == temp.length - 1)) {
                    query.append(" OR ");
                }
            }
            query.append(" )");

            this.forMotifLettreCA = query.toString();
        }
    }

    public void setForTypeInco(String forTypeInco) {
        this.forTypeInco = forTypeInco;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setFromIdLottemp(String fromIdLottemp) {
        this.fromIdLottemp = fromIdLottemp;
    }

    public void setNNSS(boolean isNNSS) {
        this.isNNSS = isNNSS;
    }

    @Override
    public void setNotMotifPavo(boolean isNotMotifPavo) {
        this.isNotMotifPavo = isNotMotifPavo;
    }

    @Override
    public void setOrder(String order) {
        this.order = order;
    }

    public void setPrintCSV(boolean isPrintCSV) {
        this.isPrintCSV = isPrintCSV;
    }

    public void setPrintCSVEmpl(boolean isPrintCSVEmpl) {
        this.isPrintCSVEmpl = isPrintCSVEmpl;
    }

    public void setToIdLottemp(String toIdLottemp) {
        this.toIdLottemp = toIdLottemp;
    }

}
