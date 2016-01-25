package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

/**
 * Ce manager permet de récupérer toutes les rubriques d'une section qui sont soumises aux intérêts moratoires.
 * 
 * @author sch
 */
public class CAEcritureCompteCourantCotSoumisInt extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String AND_DB_OPERAND = " AND ";
    protected static final String BETWEEN_DB_OPERAND = " BETWEEN ";
    protected static final String DECIMAL_DB_OPERAND = " DECIMAL ";
    protected static final String DIFFERENT_DB_OPERAND = " <> ";
    protected static final String EQUAL_DB_OPERAND = " = ";
    protected static final String GREATER_DB_OPERAND = " > ";
    protected static final String GREATER_EQUAL_DB_OPERAND = " >= ";
    protected static final String INNER_JOIN = " INNER JOIN ";
    protected static final String ON_DB_OPERAND = " ON ";
    protected static final String SMALLER_DB_OPERAND = " < ";
    protected static final String SMALLER_EQUAL_DB_OPERAND = " <= ";
    protected static final String SUBSTRING_DB_OPERAND = " SUBSTR ";

    private boolean compteCourantCotSoumisInt = true;
    private String forIdSection = new String();
    private String fromDate = new String();
    private String untilDate = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + CAEcritureCompteCourantCotSoumisInt.INNER_JOIN
                + _getCollection() + CARubrique.TABLE_CARUBRP + CAEcritureCompteCourantCotSoumisInt.ON_DB_OPERAND
                + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTE
                + CAEcritureCompteCourantCotSoumisInt.EQUAL_DB_OPERAND + _getCollection() + CARubrique.TABLE_CARUBRP
                + "." + CARubrique.FIELD_IDRUBRIQUE;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        // traitement du positionnement sur l'idSection
        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND);
            }
            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDSECTION
                    + CAEcritureCompteCourantCotSoumisInt.EQUAL_DB_OPERAND
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        // traitement de la date
        if (JadeStringUtil.isBlank(getFromDate()) && !JadeStringUtil.isBlank(getUntilDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND);
            }
            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE
                    + CAEcritureCompteCourantCotSoumisInt.SMALLER_EQUAL_DB_OPERAND
                    + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate()));
        }
        if (!JadeStringUtil.isBlank(getFromDate()) && JadeStringUtil.isBlank(getUntilDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND);
            }
            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE
                    + CAEcritureCompteCourantCotSoumisInt.GREATER_DB_OPERAND
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()));
        }
        // Tenir compte de la date
        if (!JadeStringUtil.isBlank(getFromDate()) && !JadeStringUtil.isBlank(getUntilDate())) {
            sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND);
            sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.DECIMAL_DB_OPERAND + "("
                    + CAEcritureCompteCourantCotSoumisInt.SUBSTRING_DB_OPERAND + "(" + "op." + "PSPY, 1, 8))"
                    + CAEcritureCompteCourantCotSoumisInt.BETWEEN_DB_OPERAND
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()));
            sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND);
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate()));
        }

        // Prend le reste de la clause where
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND);
        }
        sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT
                + CAEcritureCompteCourantCotSoumisInt.EQUAL_DB_OPERAND + APIOperation.ETAT_COMPTABILISE
                + CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND + _getCollection() + CAOperation.TABLE_CAOPERP
                + "." + CAOperation.FIELD_IDTYPEOPERATION + " LIKE 'E%' AND (" + _getCollection()
                + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE + " IN ("
                + APIRubrique.COMPTE_FINANCIER + "," + APIRubrique.COMPTE_COURANT_DEBITEUR + ","
                + APIRubrique.COMPTE_COURANT_CREANCIER + "," + APIRubrique.COMPTE_COMPENSATION + ") OR ( ("
                + _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                + CAEcritureCompteCourantCotSoumisInt.GREATER_EQUAL_DB_OPERAND + APIRubrique.STANDARD
                + CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND + _getCollection() + CARubrique.TABLE_CARUBRP
                + "." + CARubrique.FIELD_NATURERUBRIQUE + CAEcritureCompteCourantCotSoumisInt.SMALLER_EQUAL_DB_OPERAND
                + APIRubrique.COTISATION_SANS_MASSE + " ) " + CAEcritureCompteCourantCotSoumisInt.AND_DB_OPERAND + " ("
                + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_MONTANT + " < 0 ) ) ) ");
        sqlWhere.append("AND " + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTECOURANT);
        if (isCompteCourantCotSoumisInt()) {
            sqlWhere.append(" IN (");
        } else {
            sqlWhere.append(" NOT IN (");
        }
        sqlWhere.append("SELECT " + _getCollection() + CACompteCourant.TABLE_CACPTCP + "."
                + CACompteCourant.FIELD_IDCOMPTECOURANT + " FROM " + _getCollection() + CACompteCourant.TABLE_CACPTCP
                + CAEcritureCompteCourantCotSoumisInt.INNER_JOIN + _getCollection() + CARubrique.TABLE_CARUBRP
                + CAEcritureCompteCourantCotSoumisInt.ON_DB_OPERAND + _getCollection() + CACompteCourant.TABLE_CACPTCP
                + "." + CACompteCourant.FIELD_IDRUBRIQUE + CAEcritureCompteCourantCotSoumisInt.EQUAL_DB_OPERAND
                + _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDRUBRIQUE
                + CAEcritureCompteCourantCotSoumisInt.INNER_JOIN + _getCollection()
                + CARubriqueSoumiseInteret.TABLE_CAIMRSP + CAEcritureCompteCourantCotSoumisInt.ON_DB_OPERAND
                + _getCollection() + CACompteCourant.TABLE_CACPTCP + "." + CACompteCourant.FIELD_IDRUBRIQUE
                + CAEcritureCompteCourantCotSoumisInt.EQUAL_DB_OPERAND + _getCollection()
                + CARubriqueSoumiseInteret.TABLE_CAIMRSP + "." + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE + ")");

        // Le code suivant représent ceci
        // AND SCHEMA.CAOPERP.IDCOMPTE NOT IN (
        // SELECT SCHEMA.CARUBRP.IDRUBRIQUE FROM SCHEMA.CARUBRP
        // INNER JOIN SCHEMA.CAIMRSP ON SCHEMA.CARUBRP.IDRUBRIQUE = SCHEMA.CAIMRSP.IDRUBRIQUE
        // WHERE SCHEMA.CARUBRP.NATURERUBRIQUE NOT IN (200005,200006)
        // )
        sqlWhere.append(" AND ").append(_getCollection() + CAOperation.TABLE_CAOPERP + ".")
                .append(CAOperation.FIELD_IDCOMPTE).append(" NOT IN (");
        sqlWhere.append("SELECT " + _getCollection() + CARubrique.TABLE_CARUBRP + ".").append(
                CARubrique.FIELD_IDRUBRIQUE);
        sqlWhere.append(" FROM " + _getCollection() + CARubrique.TABLE_CARUBRP).append(" INNER JOIN ")
                .append(_getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " ON ")
                .append(_getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDRUBRIQUE);
        sqlWhere.append(" = ");
        sqlWhere.append(_getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + "."
                + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE);
        sqlWhere.append(" WHERE " + _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE);
        sqlWhere.append(" NOT IN (" + APIRubrique.COMPTE_COURANT_DEBITEUR + "," + APIRubrique.COMPTE_COURANT_CREANCIER
                + ") )");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEcriture();
    }

    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return the untilDate
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * @return the compteCourantCotSoumisInt
     */
    public boolean isCompteCourantCotSoumisInt() {
        return compteCourantCotSoumisInt;
    }

    /**
     * @param compteCourantCotSoumisInt the compteCourantCotSoumisInt to set
     */
    public void setCompteCourantCotSoumisInt(boolean compteCourantCotSoumisInt) {
        this.compteCourantCotSoumisInt = compteCourantCotSoumisInt;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param untilDate the untilDate to set
     */
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }
}
