package globaz.helios.db.avs;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;

/**
 * Insert the type's description here. Creation date: (30.06.2003 17:41:29)
 * 
 * @author: Administrator
 */
public class CGExtendedCompteOfasManager extends CGCompteOfasManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String RAPPORT_ADMINISTRATION = "3";

    public static final String RAPPORT_AFFILIES = "4";
    public static final String RAPPORT_ANNONCES_OFAS = "6";
    public static final String RAPPORT_BALANCE_ANNUELLE = "5";
    public static final String RAPPORT_BILAN = "1";
    public static final String RAPPORT_EXPLOITATION = "2";
    private java.lang.String typeRapport;

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = _getCollection() + "CGOFCPP";
        String table2 = _getCollection() + "CGSECTP";
        String from = " " + table1;
        from += " LEFT OUTER JOIN " + table2 + " on (cast(substr(" + _getCollection()
                + "CGOFCPP.idexterne,1,3) as integer) * 10 = idsecteuravs)";

        return from;
    }

    /**
     * Renvoie la composante de tri de la requête SQL (sans le mot-clé ORDER BY)
     * 
     * @return la composante ORDER BY
     */
    @Override
    protected java.lang.String _getOrder(BStatement statement) {
        return _getCollection() + "CGOFCPP.idExterne";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        String sqlWhere = super._getWhere(statement);
        String mainTable = _getCollection() + "CGOFCPP.";
        String joinedTable = _getCollection() + "CGSECTP.";
        if (getTypeRapport() != null) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += mainTable + "NONANNONCABLE="
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR);

            if (getForIdMandat().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += joinedTable + "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
            }

            if (getTypeRapport().equals(RAPPORT_BILAN)) {
                sqlWhere += /*
                             * " AND " + mainTable + "IDNATURE<>"+_dbWriteNumeric (statement.getTransaction(),
                             * CGCompteOfas.CS_NATURE_FICTIF) +
                             */
                " AND " + mainTable + "IDDOMAINE="
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_DOMAINE_BILAN) + " AND ("
                        + joinedTable + "IDTYPETACHE="
                        + _dbWriteNumeric(statement.getTransaction(), CGSecteurAVS.CS_TACHE_FEDERAL) + " OR "
                        + "(SUBSTR(" + mainTable + "IDEXTERNE,5,3) = '110'))";
                /*
                 * " AND (SUBSTR(" + mainTable + "IDEXTERNE,1,3) in ('100','199','200','300','900') OR " + mainTable +
                 * "IDNATURE="+_dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_NATURE_CC_AFFILIES)+")";
                 */

            } else if (getTypeRapport().equals(RAPPORT_ADMINISTRATION)) {
                sqlWhere += " AND " + mainTable + "IDDOMAINE="
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_DOMAINE_ADMINISTRATION) + " AND "
                        + joinedTable + "IDTYPETACHE="
                        + _dbWriteNumeric(statement.getTransaction(), CGSecteurAVS.CS_TACHE_FEDERAL);
                /*
                 * " AND (SUBSTR(" + _getCollection() + "IDEXTERNE,1,1)='9' OR SUBSTR(" + mainTable +
                 * "IDEXTERNE,1,1)='3') AND NOT ( (SUBSTR(" + mainTable + "IDEXTERNE,4,1)='8' AND SUBSTR(" + mainTable +
                 * "IDEXTERNE,4,4) <> '8999') OR (SUBSTR(" + mainTable + "IDEXTERNE,4,1)='7' AND SUBSTR(" + mainTable +
                 * "IDEXTERNE,4,4) <> '7999') OR (SUBSTR(" + mainTable + "IDEXTERNE,1,3)<>'399' AND SUBSTR(" + mainTable
                 * + "IDEXTERNE,4,1)='5' AND SUBSTR(" + mainTable + "IDEXTERNE,4,4) <> '5999') OR (SUBSTR(" + mainTable
                 * + "IDEXTERNE,1,3) IN ('399', '381', '920', '930', '940' ) AND SUBSTR(" + mainTable +
                 * "IDEXTERNE,4,1)='6' AND SUBSTR(" + mainTable + "IDEXTERNE,4,4) <> '6999'))";
                 */
            } else if (getTypeRapport().equals(RAPPORT_EXPLOITATION)) {
                sqlWhere += " AND " + mainTable + "IDNATURE<>"
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_NATURE_FICTIF) +
                        /*
                         * " AND SUBSTR(" + mainTable + "IDEXTERNE,1,1)='2' AND " +
                         */
                        " AND " + mainTable + "IDDOMAINE="
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_DOMAINE_EXPLOITATION) + " AND "
                        + joinedTable + "IDTYPETACHE="
                        + _dbWriteNumeric(statement.getTransaction(), CGSecteurAVS.CS_TACHE_FEDERAL);
            }

            else if (getTypeRapport().equals(RAPPORT_AFFILIES)) {
                sqlWhere += " AND " + mainTable + "IDNATURE="
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_NATURE_CC_AFFILIES);
            } else if (getTypeRapport().equals(RAPPORT_BALANCE_ANNUELLE)) {
                sqlWhere += " AND " + mainTable + "IDNATURE<>"
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_NATURE_FICTIF) + " ";
            } else if (getTypeRapport().equals(RAPPORT_ANNONCES_OFAS)) {
                sqlWhere += " AND (" + mainTable + "IDNATURE<>"
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_NATURE_FICTIF) + " AND SUBSTR("
                        + mainTable + "IDEXTERNE,1,1) IN ('9'))";
                sqlWhere += " AND (" + mainTable + "IDDOMAINE="
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_DOMAINE_BILAN) + " OR "
                        + mainTable + "IDDOMAINE="
                        + _dbWriteNumeric(statement.getTransaction(), CGCompteOfas.CS_DOMAINE_ADMINISTRATION) + " ) ";
            }
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws java.lang.Exception {
        return new CGExtendedCompteOfas();
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 16:25:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeRapport() {
        return typeRapport;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 16:25:48)
     * 
     * @param newTypeRapport
     *            java.lang.String
     */
    public void setTypeRapport(java.lang.String newTypeRapport) {
        typeRapport = newTypeRapport;
    }
}
