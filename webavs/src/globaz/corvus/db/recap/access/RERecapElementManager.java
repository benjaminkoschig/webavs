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

    /** Pour codeRecap - code récap (IRERecapElementCode) est = à ... (ZRLCOD) */
    private String forCodeRecap = new String();

    /** Pour idRecapElement - id récap élément (pk) est = à ... (ZSIELR) */
    private String forIdRecapElement = new String();

    /** Pour idRecapMensuelle - id récap mensuelle (fk) est = à ... (ZSIRM) */
    private String forIdRecapMensuelle = new String();

    /** Pour montant - montant récap est = à ... (ZRMMON) */
    private String forMontant = new String();
    /**
     * Pour codeRecap - code récap (IRERecapElementCode) est like à ... (ZRLCOD)
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
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERecapElement();
    }

    /**
     * Renvoie forCodeRecap;
     * 
     * @return String codeRecap - code récap (IRERecapElementCode);
     */
    public String getForCodeRecap() {
        return forCodeRecap;
    }

    /**
     * Renvoie forIdRecapElement;
     * 
     * @return String idRecapElement - id récap élément (pk);
     */
    public String getForIdRecapElement() {
        return forIdRecapElement;
    }

    /**
     * Renvoie forIdRecapMensuelle;
     * 
     * @return String idRecapMensuelle - id récap mensuelle (fk);
     */
    public String getForIdRecapMensuelle() {
        return forIdRecapMensuelle;
    }

    /**
     * Renvoie forMontant;
     * 
     * @return String montant - montant récap;
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * Sélection par likeCodeRecap;
     * 
     * @return String codeRecap - code récap (IRERecapElementCode);
     */
    public String getLikeCodeRecap() {
        return likeCodeRecap;
    }

    /**
     * Sélection par forCodeRecap
     * 
     * @param newForCodeRecap
     *            String - code récap (IRERecapElementCode)
     */
    public void setForCodeRecap(String newForCodeRecap) {
        forCodeRecap = newForCodeRecap;
    }

    /**
     * Sélection par forIdRecapElement
     * 
     * @param newForIdRecapElement
     *            String - id récap élément (pk)
     */
    public void setForIdRecapElement(String newForIdRecapElement) {
        forIdRecapElement = newForIdRecapElement;
    }

    /**
     * Sélection par forIdRecapMensuelle
     * 
     * @param newForIdRecapMensuelle
     *            String - id récap mensuelle (fk)
     */
    public void setForIdRecapMensuelle(String newForIdRecapMensuelle) {
        forIdRecapMensuelle = newForIdRecapMensuelle;
    }

    /**
     * Sélection par forMontant
     * 
     * @param newForMontant
     *            String - montant récap
     */
    public void setForMontant(String newForMontant) {
        forMontant = newForMontant;
    }

    /**
     * Sélection par likeCodeRecap
     * 
     * @param newLikeCodeRecap
     *            String - code récap (IRERecapElementCode)
     */
    public void setLikeCodeRecap(String newLikeCodeRecap) {
        likeCodeRecap = newLikeCodeRecap;
    }

}
