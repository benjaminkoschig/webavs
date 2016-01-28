package globaz.lynx.db.notedecreditlier;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXNoteDeCreditLierManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeOperation;
    private ArrayList<String> forCsTypeOperationIn;
    private String forIdFournisseur;
    private String forIdOperationLiee;
    private String forIdOperationSrc;
    private String forIdSection;

    private String forIdSociete;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXSection.TABLE_LXSECTP + " INNER JOIN " + _getCollection()
                + LXOperation.TABLE_LXOPERP + " ON " + _getCollection() + LXOperation.TABLE_LXOPERP + "."
                + LXOperation.FIELD_IDSECTION + "=" + _getCollection() + LXSection.TABLE_LXSECTP + "."
                + LXSection.FIELD_IDSECTION;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection()) && JadeStringUtil.isDigit(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                    .append(LXSection.FIELD_IDSECTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                    .append(LXSection.FIELD_IDSOCIETE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSociete()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                    .append(LXSection.FIELD_IDFOURNISSEUR).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdFournisseur()));
        }

        if (getForCsTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSTYPEOPERATION, getForCsTypeOperationIn()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsTypeOperation()) && JadeStringUtil.isDigit(getForCsTypeOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSTYPEOPERATION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsTypeOperation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationLiee()) && JadeStringUtil.isDigit(getForIdOperationLiee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATIONLIEE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationLiee()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationSrc()) && JadeStringUtil.isDigit(getForIdOperationSrc())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATIONSRC).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationSrc()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXNoteDeCreditLier();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsTypeOperation() {
        return forCsTypeOperation;
    }

    public ArrayList<String> getForCsTypeOperationIn() {
        return forCsTypeOperationIn;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdOperationLiee() {
        return forIdOperationLiee;
    }

    public String getForIdOperationSrc() {
        return forIdOperationSrc;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsTypeOperation(String forCsTypeOperation) {
        this.forCsTypeOperation = forCsTypeOperation;
    }

    public void setForCsTypeOperationIn(ArrayList<String> forCsTypeOperationIn) {
        this.forCsTypeOperationIn = forCsTypeOperationIn;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdOperationLiee(String forIdOperationLiee) {
        this.forIdOperationLiee = forIdOperationLiee;
    }

    public void setForIdOperationSrc(String forIdOperationSrc) {
        this.forIdOperationSrc = forIdOperationSrc;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }
}
