package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CAControleOperationSectionManager extends BManager {

    private static final long serialVersionUID = 3211749914937576099L;

    private String forIdJournal = null;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAControleOperationSection();
    }

    @Override
    protected String _getFields(BStatement statement) {
        final StringBuilder field = new StringBuilder();

        field.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE);
        field.append(" AS IDEXTERNEROLECOMPTE, ");
        field.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        field.append(" AS IDSECTIONSECTION, ");
        field.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDEXTERNE);
        field.append(" AS IDEXTERNESECTION, ");
        field.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE);
        field.append(" AS SOLDESECTION, ");
        field.append("SUM (" + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_MONTANT
                + ") AS SOMME");

        return field.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        final StringBuilder from = new StringBuilder();

        from.append(_getCollection() + CAOperation.TABLE_CAOPERP);
        from.append(CAOperationManager.INNER_JOIN);
        from.append(_getCollection() + CASection.TABLE_CASECTP);
        from.append(CAOperationManager.ON);
        from.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDSECTION);
        from.append(" = ");
        from.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        from.append(CAOperationManager.INNER_JOIN);
        from.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP);
        from.append(CAOperationManager.ON);
        from.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDCOMPTEANNEXE);
        from.append(" = ");
        from.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);

        return from.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        final StringBuilder where = new StringBuilder();

        // -- WHERE
        where.append(getWhere(statement));

        // -- GROUP BY
        where.append(getGroupBy());

        // -- HAVING
        where.append(getHaving());

        return where.toString();
    }

    private String getHaving() {
        final StringBuilder having = new StringBuilder();

        having.append(" HAVING ");

        having.append(_getCollection() + CASection.TABLE_CASECTP + "." + CACompteAnnexe.FIELD_SOLDE);
        having.append(" <> ");
        having.append(" SUM(" + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_MONTANT + ") ");

        return having.toString();
    }

    private String getGroupBy() {
        final StringBuilder groupBy = new StringBuilder();

        groupBy.append(" GROUP BY ");

        groupBy.append(_getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDEXTERNEROLE);
        groupBy.append(", ");
        groupBy.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        groupBy.append(", ");
        groupBy.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDEXTERNE);
        groupBy.append(", ");
        groupBy.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE);

        return groupBy.toString();
    }

    private String getWhere(BStatement statement) {
        final StringBuilder where = new StringBuilder();

        addCondition(where, _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "." + CACompteAnnexe.FIELD_IDROLE + "<>"
                + this._dbWriteNumeric(statement.getTransaction(), "517041"));

        addCondition(
                where,
                _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT + " IN ("
                        + this._dbWriteNumeric(statement.getTransaction(), "205002") + ", "
                        + this._dbWriteNumeric(statement.getTransaction(), "205004") + ") ");

        addCondition(where, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                + " LIKE " + this._dbWriteString(statement.getTransaction(), "E%"));

        // Si nous renseignons un id journal, nous recherchons tous les sections li�es
        if (getForIdJournal() != null) {
            addCondition(where, getListIdSectionsFromIdJournal(statement, getForIdJournal()));
        }

        return where.toString();
    }

    private String getListIdSectionsFromIdJournal(BStatement statement, final String idJournal) {
        if (idJournal == null) {
            return "";
        }

        final StringBuilder chaine = new StringBuilder();

        /**
         * (AND) shema.section.idsection in ( select schema.caoperp.idsection from schema.caoperp where
         * schema.caoperp.idjournal = ? )
         */
        chaine.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_IDSECTION);
        chaine.append(" IN ( SELECT ");
        chaine.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDSECTION);
        chaine.append(" FROM ");
        chaine.append(_getCollection() + CAOperation.TABLE_CAOPERP);
        chaine.append(" WHERE ");
        chaine.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDJOURNAL);
        chaine.append(" = ");
        chaine.append(this._dbWriteNumeric(statement.getTransaction(), idJournal));
        chaine.append(" ) ");

        return chaine.toString();
    }

    private void addCondition(StringBuilder sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CAOperationManager.AND);
        }
        sqlWhere.append(condition);
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }
}
