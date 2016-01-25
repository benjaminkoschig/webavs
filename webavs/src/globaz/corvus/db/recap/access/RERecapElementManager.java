package globaz.corvus.db.recap.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author ${user}
 * @version 1.0 Created on Fri Nov 30 11:45:28 CET 2007
 */
public class RERecapElementManager extends BManager {
    /** Table : REELMREC */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour codeRecap - code r�cap (IRERecapElementCode) est = � ... (ZRLCOD) */
    private String forCodeRecap = new String();

    /** Pour idRecapElement - id r�cap �l�ment (pk) est = � ... (ZSIELR) */
    private String forIdRecapElement = new String();

    /** Pour idRecapMensuelle - id r�cap mensuelle (fk) est = � ... (ZSIRM) */
    private String forIdRecapMensuelle = new String();

    /** Pour montant - montant r�cap est = � ... (ZRMMON) */
    private String forMontant = new String();
    /**
     * Pour codeRecap - code r�cap (IRERecapElementCode) est like � ... (ZRLCOD)
     */
    private String likeCodeRecap = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "REELMREC" (Model : RERecapElement)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(IRERecapElementDefTable.TABLE_NAME).toString();
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param statement
     *            de type BStatement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            BStatement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();
        // traitement du positionnement
        if (getForIdRecapElement().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapElementDefTable.ID_RECAP_ELEMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdRecapElement()));
        }
        // traitement du positionnement
        if (getForIdRecapMensuelle().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapElementDefTable.ID_RECAP_MENSUELLE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdRecapMensuelle()));
        }
        // traitement du positionnement
        if (getForMontant().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapElementDefTable.MONTANT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForMontant()));
        }
        // traitement du positionnement
        if (getForCodeRecap().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapElementDefTable.CODE_RECAP).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCodeRecap()));
        }

        // traitement du positionnement
        if (getLikeCodeRecap().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapElementDefTable.CODE_RECAP).append(" LIKE ")
                    .append(_dbWriteString(statement.getTransaction(), getLikeCodeRecap() + "%"));
        }
        return sqlWhere.toString();
    }

    /**
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERecapElement();
    }

    /**
     * Renvoie forCodeRecap;
     * 
     * @return String codeRecap - code r�cap (IRERecapElementCode);
     */
    public String getForCodeRecap() {
        return forCodeRecap;
    }

    /**
     * Renvoie forIdRecapElement;
     * 
     * @return String idRecapElement - id r�cap �l�ment (pk);
     */
    public String getForIdRecapElement() {
        return forIdRecapElement;
    }

    /**
     * Renvoie forIdRecapMensuelle;
     * 
     * @return String idRecapMensuelle - id r�cap mensuelle (fk);
     */
    public String getForIdRecapMensuelle() {
        return forIdRecapMensuelle;
    }

    /**
     * Renvoie forMontant;
     * 
     * @return String montant - montant r�cap;
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * S�lection par likeCodeRecap;
     * 
     * @return String codeRecap - code r�cap (IRERecapElementCode);
     */
    public String getLikeCodeRecap() {
        return likeCodeRecap;
    }

    /**
     * S�lection par forCodeRecap
     * 
     * @param newForCodeRecap
     *            String - code r�cap (IRERecapElementCode)
     */
    public void setForCodeRecap(String newForCodeRecap) {
        forCodeRecap = newForCodeRecap;
    }

    /**
     * S�lection par forIdRecapElement
     * 
     * @param newForIdRecapElement
     *            String - id r�cap �l�ment (pk)
     */
    public void setForIdRecapElement(String newForIdRecapElement) {
        forIdRecapElement = newForIdRecapElement;
    }

    /**
     * S�lection par forIdRecapMensuelle
     * 
     * @param newForIdRecapMensuelle
     *            String - id r�cap mensuelle (fk)
     */
    public void setForIdRecapMensuelle(String newForIdRecapMensuelle) {
        forIdRecapMensuelle = newForIdRecapMensuelle;
    }

    /**
     * S�lection par forMontant
     * 
     * @param newForMontant
     *            String - montant r�cap
     */
    public void setForMontant(String newForMontant) {
        forMontant = newForMontant;
    }

    /**
     * S�lection par likeCodeRecap
     * 
     * @param newLikeCodeRecap
     *            String - code r�cap (IRERecapElementCode)
     */
    public void setLikeCodeRecap(String newLikeCodeRecap) {
        likeCodeRecap = newLikeCodeRecap;
    }

}
