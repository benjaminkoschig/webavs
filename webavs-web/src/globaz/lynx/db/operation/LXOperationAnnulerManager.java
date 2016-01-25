package globaz.lynx.db.operation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXOperationAnnulerManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<String> forCsEtatIn;
    private String forCsEtatNot;
    private ArrayList<String> forCsTypeOperationIn;
    private String forIdOperationLieeInIdJournal;

    private String forIdOperationSrcInIdJournal;
    private String forIdOperationSrcInIdOrdreGroupe;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXOperation.TABLE_LXOPERP;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (getForCsEtatIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        }

        if (getForCsTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSTYPEOPERATION, getForCsTypeOperationIn()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSETATOPERATION).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatNot()));
        }

        if (!JadeStringUtil.isBlank(getForIdOperationSrcInIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            // OPERATION.IDOPERATIONSRC IN (SELECT OPERATIONB.IDOPERATION FROM
            // WEBAVSCIAM.LXOPERP OPERATIONB where OPERATIONB.IDJOURNAL = 1)
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATIONSRC);
            sqlWhere.append(" IN (SELECT ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" FROM ").append(_getCollection())
                    .append(LXOperation.TABLE_LXOPERP);
            sqlWhere.append(" WHERE ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationSrcInIdJournal()))
                    .append(" )");
        }

        if (!JadeStringUtil.isBlank(getForIdOperationLieeInIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            // OPERATION.IDOPERATIONLIEE IN (SELECT OPERATIONB.IDOPERATION FROM
            // WEBAVSCIAM.LXOPERP OPERATIONB where OPERATIONB.IDJOURNAL = 1)
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATIONLIEE);
            sqlWhere.append(" IN (SELECT ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" FROM ").append(_getCollection())
                    .append(LXOperation.TABLE_LXOPERP);
            sqlWhere.append(" WHERE ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationLieeInIdJournal()))
                    .append(" )");
        }

        if (!JadeStringUtil.isBlank(getForIdOperationSrcInIdOrdreGroupe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            // OPERATION.IDOPERATIONLIEE IN (SELECT OPERATIONB.IDOPERATION FROM
            // WEBAVSCIAM.LXOPERP OPERATIONB where OPERATIONB.IDJOURNAL = 1)
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATIONLIEE);
            sqlWhere.append(" IN (SELECT ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" FROM ").append(_getCollection())
                    .append(LXOperation.TABLE_LXOPERP);
            sqlWhere.append(" WHERE ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORDREGROUPE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationSrcInIdOrdreGroupe()))
                    .append(" )");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOperation();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public ArrayList<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsEtatNot() {
        return forCsEtatNot;
    }

    public ArrayList<String> getForCsTypeOperationIn() {
        return forCsTypeOperationIn;
    }

    public String getForIdOperationLieeInIdJournal() {
        return forIdOperationLieeInIdJournal;
    }

    public String getForIdOperationSrcInIdJournal() {
        return forIdOperationSrcInIdJournal;
    }

    public String getForIdOperationSrcInIdOrdreGroupe() {
        return forIdOperationSrcInIdOrdreGroupe;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsEtatIn(ArrayList<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsEtatNot(String forCsEtatNot) {
        this.forCsEtatNot = forCsEtatNot;
    }

    public void setForCsTypeOperationIn(ArrayList<String> forCsTypeOperationIn) {
        this.forCsTypeOperationIn = forCsTypeOperationIn;
    }

    public void setForIdOperationLieeInIdJournal(String forIdOperationLieeInIdJournal) {
        this.forIdOperationLieeInIdJournal = forIdOperationLieeInIdJournal;
    }

    public void setForIdOperationSrcInIdJournal(String forIdOperationSrcInIdJournal) {
        this.forIdOperationSrcInIdJournal = forIdOperationSrcInIdJournal;
    }

    public void setForIdOperationSrcInIdOrdreGroupe(String forIdOperationSrcInIdOrdreGroupe) {
        this.forIdOperationSrcInIdOrdreGroupe = forIdOperationSrcInIdOrdreGroupe;
    }

}
