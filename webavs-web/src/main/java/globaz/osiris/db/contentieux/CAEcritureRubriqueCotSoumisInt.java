package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

/**
 * Ce manager permet de récupérer toutes les rubriques d'une section qui sont soumises aux intérêts moratoires.
 * 
 * @author sch
 */
public class CAEcritureRubriqueCotSoumisInt extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSection = new String();
    private boolean rubriqueCotSoumisInt = true;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP;
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
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDSECTION + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        // Prend le reste de la clause where
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT + " = "
                + APIOperation.ETAT_COMPTABILISE + " AND " + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                + CAOperation.FIELD_IDTYPEOPERATION + " LIKE 'E%' AND " + _getCollection() + CAOperation.TABLE_CAOPERP
                + "." + CAOperation.FIELD_IDCOMPTE);
        if (isRubriqueCotSoumisInt()) {
            sqlWhere.append(" IN (");
        } else {
            sqlWhere.append(" NOT IN (");
        }
        sqlWhere.append("SELECT " + _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDRUBRIQUE
                + " FROM " + _getCollection() + CARubrique.TABLE_CARUBRP + " INNER JOIN " + _getCollection()
                + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " ON " + _getCollection() + CARubrique.TABLE_CARUBRP + "."
                + CARubrique.FIELD_IDRUBRIQUE + " = " + _getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + "."
                + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE + " WHERE " + _getCollection() + CARubrique.TABLE_CARUBRP
                + "." + CARubrique.FIELD_NATURERUBRIQUE + " NOT IN (" + APIRubrique.COMPTE_COURANT_CREANCIER + ","
                + APIRubrique.COMPTE_COURANT_DEBITEUR + "))");

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
     * @return the rubriqueCotSoumisInt
     */
    public boolean isRubriqueCotSoumisInt() {
        return rubriqueCotSoumisInt;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param rubriqueCotSoumisInt
     *            the rubriqueCotSoumisInt to set
     */
    public void setRubriqueCotSoumisInt(boolean rubriqueCotSoumisInt) {
        this.rubriqueCotSoumisInt = rubriqueCotSoumisInt;
    }
}
