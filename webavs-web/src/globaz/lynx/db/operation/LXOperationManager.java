package globaz.lynx.db.operation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXOperationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCodeTVA;
    private String forCsEtat;
    private ArrayList<String> forCsEtatIn;
    private String forCsEtatNot;
    private String forDate;
    private String forDateEcheance;
    private String forDateEcheanceInferieure;
    private String forDateInferieure;
    private String forIdJournal;
    private String forIdJournalNot;
    private String forIdOperation;
    private String forIdOperationLiee;
    private String forIdOperationNot;
    private String forIdOperationOrIdOperationSrc;
    private String forIdOperationSrc;
    private String forIdOrdreGroupe;
    private String forIdOrganeExecutionOrVide;
    private String forIdSection;
    private String forIdSectionLiee;
    private ArrayList<String> forIdTypeOperationIn;
    private String forIdTypeOperationNot;
    private Boolean forJournalBlank = new Boolean(false);
    private String forMontantMaxi;
    private String forMontantMini;
    private String forReferenceExterne;

    private String forTri;

    private String likeLibelle;
    private String likeReferenceExterne;

    private String withoutIdOperation;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXOperation.TABLE_LXOPERP;
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection()) && JadeStringUtil.isDigit(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDSECTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSectionLiee()) && JadeStringUtil.isDigit(getForIdSectionLiee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDSECTIONLIEE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSectionLiee()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdJournal()) && JadeStringUtil.isDigit(getForIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrdreGroupe()) && JadeStringUtil.isDigit(getForIdOrdreGroupe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORDREGROUPE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreGroupe()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperation()) && JadeStringUtil.isDigit(getForIdOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationNot()) && JadeStringUtil.isDigit(getForIdOperationNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationNot()));
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrganeExecutionOrVide())
                && JadeStringUtil.isDigit(getForIdOrganeExecutionOrVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("( ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecutionOrVide()));
            sqlWhere.append(" OR ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), "0")).append(" )");
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationOrIdOperationSrc())
                && JadeStringUtil.isDigit(getForIdOperationOrIdOperationSrc())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("( ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationOrIdOperationSrc()));
            sqlWhere.append(" OR ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATIONSRC).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationOrIdOperationSrc()))
                    .append(" )");
        }

        if (!JadeStringUtil.isIntegerEmpty(getWithoutIdOperation()) && JadeStringUtil.isDigit(getWithoutIdOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection() + LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" != ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getWithoutIdOperation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateEcheance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEECHEANCE).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheance()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateEcheanceInferieure())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEECHEANCE).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheanceInferieure()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEOPERATION).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDate()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateInferieure())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEOPERATION).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateInferieure()));
        }

        if (getForJournalBlank().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(");
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), "0"));
            sqlWhere.append(" or ");
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" IS ").append("NULL");
            sqlWhere.append(")");
        }

        if (getForIdTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSTYPEOPERATION, getForIdTypeOperationIn()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdTypeOperationNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSTYPEOPERATION).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdTypeOperationNot()));
        }

        if (getForCsEtatIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSETATOPERATION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSETATOPERATION).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatNot()));
        }

        if (!JadeStringUtil.isBlank(getLikeLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_LIBELLE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeLibelle() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMini())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_MONTANT).append(" >= ").append(getForMontantMini());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMaxi())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_MONTANT).append(" <= ").append(getForMontantMaxi());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdJournalNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdJournalNot()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsCodeTVA())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSCODETVA).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsCodeTVA()));
        }

        if (!JadeStringUtil.isBlank(getLikeReferenceExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_REFERENCEEXTERNE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeReferenceExterne() + "%"));
        }

        if (!JadeStringUtil.isBlank(getForReferenceExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_REFERENCEEXTERNE).append(" = ")
                    .append(this._dbWriteString(statement.getTransaction(), getForReferenceExterne()));
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

    public String getForCsCodeTVA() {
        return forCsCodeTVA;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public ArrayList<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsEtatNot() {
        return forCsEtatNot;
    }

    public String getForDate() {
        return forDate;
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    public String getForDateEcheanceInferieure() {
        return forDateEcheanceInferieure;
    }

    public String getForDateInferieure() {
        return forDateInferieure;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdJournalNot() {
        return forIdJournalNot;
    }

    public String getForIdOperation() {
        return forIdOperation;
    }

    public String getForIdOperationLiee() {
        return forIdOperationLiee;
    }

    public String getForIdOperationNot() {
        return forIdOperationNot;
    }

    public String getForIdOperationOrIdOperationSrc() {
        return forIdOperationOrIdOperationSrc;
    }

    public String getForIdOperationSrc() {
        return forIdOperationSrc;
    }

    public String getForIdOrdreGroupe() {
        return forIdOrdreGroupe;
    }

    public String getForIdOrganeExecutionOrVide() {
        return forIdOrganeExecutionOrVide;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSectionLiee() {
        return forIdSectionLiee;
    }

    public ArrayList<String> getForIdTypeOperationIn() {
        return forIdTypeOperationIn;
    }

    public String getForIdTypeOperationNot() {
        return forIdTypeOperationNot;
    }

    public Boolean getForJournalBlank() {
        return forJournalBlank;
    }

    public String getForMontantMaxi() {
        return forMontantMaxi;
    }

    public String getForMontantMini() {
        return forMontantMini;
    }

    public String getForReferenceExterne() {
        return forReferenceExterne;
    }

    public String getForTri() {
        return forTri;
    }

    public String getLikeLibelle() {
        return likeLibelle;
    }

    public String getLikeReferenceExterne() {
        return likeReferenceExterne;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getWithoutIdOperation() {
        return withoutIdOperation;
    }

    public void setForCsCodeTVA(String forCsCodeTVA) {
        this.forCsCodeTVA = forCsCodeTVA;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtatIn(ArrayList<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsEtatNot(String forCsEtatNot) {
        this.forCsEtatNot = forCsEtatNot;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    public void setForDateEcheanceInferieure(String forDateEcheanceInferieure) {
        this.forDateEcheanceInferieure = forDateEcheanceInferieure;
    }

    public void setForDateInferieure(String forDateInferieure) {
        this.forDateInferieure = forDateInferieure;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForIdJournalNot(String forIdJournalNot) {
        this.forIdJournalNot = forIdJournalNot;
    }

    public void setForIdOperation(String forIdOperation) {
        this.forIdOperation = forIdOperation;
    }

    public void setForIdOperationLiee(String forIdOperationLiee) {
        this.forIdOperationLiee = forIdOperationLiee;
    }

    public void setForIdOperationNot(String forIdOperationNot) {
        this.forIdOperationNot = forIdOperationNot;
    }

    public void setForIdOperationOrIdOperationSrc(String forIdOperationOrIdOperationSrc) {
        this.forIdOperationOrIdOperationSrc = forIdOperationOrIdOperationSrc;
    }

    public void setForIdOperationSrc(String forIdOperationSrc) {
        this.forIdOperationSrc = forIdOperationSrc;
    }

    public void setForIdOrdreGroupe(String forIdOrdreGroupe) {
        this.forIdOrdreGroupe = forIdOrdreGroupe;
    }

    public void setForIdOrganeExecutionOrVide(String forIdOrganeExecutionOrVide) {
        this.forIdOrganeExecutionOrVide = forIdOrganeExecutionOrVide;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSectionLiee(String forIdSectionLiee) {
        this.forIdSectionLiee = forIdSectionLiee;
    }

    public void setForIdTypeOperationIn(ArrayList<String> forIdTypeOperationIn) {
        this.forIdTypeOperationIn = forIdTypeOperationIn;
    }

    public void setForIdTypeOperationNot(String forIdTypeOperationNot) {
        this.forIdTypeOperationNot = forIdTypeOperationNot;
    }

    public void setForJournalBlank(Boolean forJournalBlank) {
        this.forJournalBlank = forJournalBlank;
    }

    public void setForMontantMaxi(String forMontantMaxi) {
        this.forMontantMaxi = forMontantMaxi;
    }

    public void setForMontantMini(String forMontantMini) {
        this.forMontantMini = forMontantMini;
    }

    public void setForReferenceExterne(String forReferenceExterne) {
        this.forReferenceExterne = forReferenceExterne;
    }

    public void setForTri(String forTri) {
        this.forTri = forTri;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }

    public void setLikeReferenceExterne(String likeReferenceExterne) {
        this.likeReferenceExterne = likeReferenceExterne;
    }

    public void setWithoutIdOperation(String withoutIdOperation) {
        this.withoutIdOperation = withoutIdOperation;
    }
}
