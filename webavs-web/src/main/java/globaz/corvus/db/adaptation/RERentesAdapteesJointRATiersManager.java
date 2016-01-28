package globaz.corvus.db.adaptation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

public class RERentesAdapteesJointRATiersManager extends PRAbstractManager implements
        BIGenericManager<RERentesAdapteesJointRATiers> {

    private static final long serialVersionUID = 1L;

    private String forAnneeAdaptation = "";
    private String forCodePrestation = "";
    private String forCsTypeAdaptation = "";
    private String forCsTypeAdaptationIn = "";
    private String forNouveauMontant = "";
    private transient String fromClause = null;
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RERentesAdapteesJointRATiers.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                        + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                        + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if ((!JadeStringUtil.isBlank(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS());
        }

        if (!JadeStringUtil.isEmpty(forCsTypeAdaptation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES + "."
                    + RERentesAdaptees.FIELDNAME_TYPE_ADAPTATION + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), forCsTypeAdaptation);
        }

        if (!JadeStringUtil.isEmpty(forCsTypeAdaptationIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES + "."
                    + RERentesAdaptees.FIELDNAME_TYPE_ADAPTATION + " IN( " + forCsTypeAdaptationIn + ")";
        }

        if (!JadeStringUtil.isEmpty(forAnneeAdaptation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES + "."
                    + RERentesAdaptees.FIELDNAME_NOUV_ANNEE_MONTANT_RAM + " = "
                    + this._dbWriteString(statement.getTransaction(), forAnneeAdaptation);
        }

        if (!JadeStringUtil.isEmpty(forCodePrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES + "."
                    + RERentesAdaptees.FIELDNAME_CODE_PRESTATION + " = "
                    + this._dbWriteString(statement.getTransaction(), forCodePrestation);
        }

        if (!JadeStringUtil.isEmpty(forNouveauMontant)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES + "."
                    + RERentesAdaptees.FIELDNAME_NOUV_MONTANT_PRESTATION + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), forNouveauMontant);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERentesAdapteesJointRATiers();
    }

    public String getForAnneeAdaptation() {
        return forAnneeAdaptation;
    }

    public String getForCodePrestation() {
        return forCodePrestation;
    }

    public String getForCsTypeAdaptation() {
        return forCsTypeAdaptation;
    }

    public String getForCsTypeAdaptationIn() {
        return forCsTypeAdaptationIn;
    }

    public String getForNouveauMontant() {
        return forNouveauMontant;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    @Override
    public String getOrderByDefaut() {
        return RERentesAdapteesJointRATiers.FIELDNAME_NOM + ", " + RERentesAdapteesJointRATiers.FIELDNAME_PRENOM;
    }

    public void setForAnneeAdaptation(String forAnneeAdaptation) {
        this.forAnneeAdaptation = forAnneeAdaptation;
    }

    public void setForCodePrestation(String forCodePrestation) {
        this.forCodePrestation = forCodePrestation;
    }

    public void setForCsTypeAdaptation(String forCsTypeAdaptation) {
        this.forCsTypeAdaptation = forCsTypeAdaptation;
    }

    public void setForCsTypeAdaptationIn(String forCsTypeAdaptationIn) {
        this.forCsTypeAdaptationIn = forCsTypeAdaptationIn;
    }

    public void setForNouveauMontant(String forNouveauMontant) {
        this.forNouveauMontant = forNouveauMontant;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    @Override
    public List<RERentesAdapteesJointRATiers> getContainerAsList() {
        List<RERentesAdapteesJointRATiers> list = new ArrayList<RERentesAdapteesJointRATiers>();

        for (int i = 0; i < size(); i++) {
            list.add((RERentesAdapteesJointRATiers) get(i));
        }

        return list;
    }

}
