package globaz.osiris.db.services;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;

public class CARechercheMontantSectionManagerListViewBean extends CASectionManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdExterneRole = "";
    private String forSelectionRole = "";
    private String forSoldeABS = "";
    private String forSoldeOperator = "";
    private String likeDescription = "";
    private String likeIdExterneRole = "";

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder from = new StringBuilder();

        from.append(super._getFrom(statement));

        from.append(" INNER JOIN ");
        from.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP);
        from.append(" ON ");
        from.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);
        from.append("=");
        from.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);

        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder orderBy = new StringBuilder();
        orderBy.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDEXTERNEROLE).append(", ");
        orderBy.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                .append(CACompteAnnexe.FIELD_IDROLE).append(", ");
        orderBy.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE)
                .append(" DESC ").append(", ");
        orderBy.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESECTION)
                .append(" DESC");

        return orderBy.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String superWhere = super._getWhere(statement);

        if (!"*".equalsIgnoreCase(superWhere)) {
            sql.append(superWhere);
        }

        if (!JadeStringUtil.isBlank(getForSoldeABS())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append("ABS(");
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append(") ");
            sql.append(getForSoldeOperator());
            sql.append(" ABS(");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForSoldeABS()));
            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterneRole())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                    .append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), getLikeIdExterneRole() + "%"));
        }

        if (!JadeStringUtil.isBlank(getLikeDescription())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(CACompteAnnexe.TABLE_CACPTAP).append(".")
                    .append(CACompteAnnexe.FIELD_DESCUPCASE);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(),
                    "%" + JadeStringUtil.convertSpecialChars(getLikeDescription()).toUpperCase() + "%"));
        }

        if ((!JadeStringUtil.isBlank(getForSelectionRole())) && !getForSelectionRole().equals("1000")) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                sql.append("IDROLE");
                sql.append(" IN(");
                boolean firstElement = true;
                for (String role : JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE)) {
                    if (firstElement) {
                        firstElement = false;
                    } else {
                        sql.append(",");
                    }
                    sql.append(this._dbWriteNumeric(statement.getTransaction(), role));
                }
                sql.append(")");
            } else {
                sql.append("IDROLE");
                sql.append("=");
                sql.append(this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole()));
            }
        }

        return sql.toString();
    }

    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getForSoldeABS() {
        return forSoldeABS;
    }

    public String getForSoldeOperator() {
        return forSoldeOperator;
    }

    public String getLikeDescription() {
        return likeDescription;
    }

    public String getLikeIdExterneRole() {
        return likeIdExterneRole;
    }

    public void setForIdExterneRole(String forIdExterneRole) {
        this.forIdExterneRole = forIdExterneRole;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

    public void setForSoldeABS(String forMontantABS) {
        forSoldeABS = forMontantABS;
    }

    public void setForSoldeOperator(String forSoldeOperator) {
        this.forSoldeOperator = forSoldeOperator;
    }

    public void setLikeDescription(String likeDescription) {
        this.likeDescription = likeDescription;
    }

    public void setLikeIdExterneRole(String likeIdExterneRole) {
        this.likeIdExterneRole = likeIdExterneRole;
    }
}
