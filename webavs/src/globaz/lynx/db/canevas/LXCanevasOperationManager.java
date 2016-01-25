package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXCanevasOperationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCodeTVA;
    private String forIdOperationCanevas;
    private String forIdOperationCanevasNot;
    private String forIdOrganeExecutionOrVide;
    private String forIdSectionCanevas;
    private ArrayList<String> forIdTypeOperationIn;
    private String forIdTypeOperationNot;
    private String forMontantMaxi;
    private String forMontantMini;
    private String forTri;

    private String likeLibelle;

    private String withoutIdOperationCanevas;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXCanevasOperation.TABLE_LXCANOP;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer ordreBy = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForTri())) {
            ordreBy.append(getForTri()).append(" DESC");
        }

        return ordreBy.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdSectionCanevas())
                && JadeStringUtil.isDigit(getForIdSectionCanevas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDSECTIONCANEVAS).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSectionCanevas()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationCanevas())
                && JadeStringUtil.isDigit(getForIdOperationCanevas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationCanevas()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationCanevasNot())
                && JadeStringUtil.isDigit(getForIdOperationCanevasNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationCanevasNot()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrganeExecutionOrVide())
                && JadeStringUtil.isDigit(getForIdOrganeExecutionOrVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("( ").append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecutionOrVide()));
            sqlWhere.append(" OR ").append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), "0")).append(" )");
        }

        if (!JadeStringUtil.isIntegerEmpty(getWithoutIdOperationCanevas())
                && JadeStringUtil.isDigit(getWithoutIdOperationCanevas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection() + LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS).append(" != ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getWithoutIdOperationCanevas()));
        }

        if (getForIdTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXCanevasOperation.FIELD_CSTYPEOPERATION,
                    getForIdTypeOperationIn()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdTypeOperationNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_CSTYPEOPERATION).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdTypeOperationNot()));
        }

        if (!JadeStringUtil.isBlank(getLikeLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_LIBELLE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeLibelle() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMini())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_MONTANT).append(" >= ").append(getForMontantMini());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMaxi())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_MONTANT).append(" <= ").append(getForMontantMaxi());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsCodeTVA())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_CSCODETVA).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsCodeTVA()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCanevasOperation();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsCodeTVA() {
        return forCsCodeTVA;
    }

    public String getForIdOperationCanevas() {
        return forIdOperationCanevas;
    }

    public String getForIdOperationCanevasNot() {
        return forIdOperationCanevasNot;
    }

    public String getForIdOrganeExecutionOrVide() {
        return forIdOrganeExecutionOrVide;
    }

    public String getForIdSectionCanevas() {
        return forIdSectionCanevas;
    }

    public ArrayList<String> getForIdTypeOperationIn() {
        return forIdTypeOperationIn;
    }

    public String getForIdTypeOperationNot() {
        return forIdTypeOperationNot;
    }

    public String getForMontantMaxi() {
        return forMontantMaxi;
    }

    public String getForMontantMini() {
        return forMontantMini;
    }

    public String getForTri() {
        return forTri;
    }

    public String getLikeLibelle() {
        return likeLibelle;
    }

    public String getWithoutIdOperationCanevas() {
        return withoutIdOperationCanevas;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsCodeTVA(String forCsCodeTVA) {
        this.forCsCodeTVA = forCsCodeTVA;
    }

    public void setForIdOperationCanevas(String forIdOperationCanevas) {
        this.forIdOperationCanevas = forIdOperationCanevas;
    }

    public void setForIdOperationCanevasNot(String forIdOperationCanevasNot) {
        this.forIdOperationCanevasNot = forIdOperationCanevasNot;
    }

    public void setForIdOrganeExecutionOrVide(String forIdOrganeExecutionOrVide) {
        this.forIdOrganeExecutionOrVide = forIdOrganeExecutionOrVide;
    }

    public void setForIdSectionCanevas(String forIdSectionCanevas) {
        this.forIdSectionCanevas = forIdSectionCanevas;
    }

    public void setForIdTypeOperationIn(ArrayList<String> forIdTypeOperationIn) {
        this.forIdTypeOperationIn = forIdTypeOperationIn;
    }

    public void setForIdTypeOperationNot(String forIdTypeOperationNot) {
        this.forIdTypeOperationNot = forIdTypeOperationNot;
    }

    public void setForMontantMaxi(String forMontantMaxi) {
        this.forMontantMaxi = forMontantMaxi;
    }

    public void setForMontantMini(String forMontantMini) {
        this.forMontantMini = forMontantMini;
    }

    public void setForTri(String forTri) {
        this.forTri = forTri;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }

    public void setWithoutIdOperationCanevas(String withoutIdOperationCanevas) {
        this.withoutIdOperationCanevas = withoutIdOperationCanevas;
    }
}
