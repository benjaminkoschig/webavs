package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 16:24:57)
 * 
 * @author: Administrator
 */
public class CAGenreInteretManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdGenreInteret = new String();
    private String forIdPlanCalculInteret = new String();
    private String forIdRubrique = new String();
    private String forIdTypeInteret = new String();
    private String fromIdGenreInteret = new String();

    /**
     * Commentaire relatif au constructeur CAGenreInteretManager.
     */
    public CAGenreInteretManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAGenreInteret.TABLE_CAIMGIP;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdGenreInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAGenreInteret.FIELD_IDGENREINTERET + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreInteret());
        }

        // traitement du positionnement
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAGenreInteret.FIELD_IDRUBRIQUE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // traitement du positionnement
        if (getForIdPlanCalculInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAGenreInteret.FIELD_IDPLACALINT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanCalculInteret());
        }

        // traitement du positionnement
        if (getForIdTypeInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAGenreInteret.FIELD_IDTYPEINTERET + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeInteret());
        }

        // traitement du positionnement
        if (getFromIdGenreInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAGenreInteret.FIELD_IDGENREINTERET + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdGenreInteret());
        }

        return sqlWhere;
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
        return new CAGenreInteret();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdGenreInteret() {
        return forIdGenreInteret;
    }

    public String getForIdPlanCalculInteret() {
        return forIdPlanCalculInteret;
    }

    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getForIdTypeInteret() {
        return forIdTypeInteret;
    }

    public String getFromIdGenreInteret() {
        return fromIdGenreInteret;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdGenreInteret(String newForIdGenreInteret) {
        forIdGenreInteret = newForIdGenreInteret;
    }

    public void setForIdPlanCalculInteret(String newForIdPlanCalculInteret) {
        forIdPlanCalculInteret = newForIdPlanCalculInteret;
    }

    public void setForIdRubrique(String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    public void setForIdTypeInteret(String newForIdTypeInteret) {
        forIdTypeInteret = newForIdTypeInteret;
    }

    public void setFromIdGenreInteret(String newFromIdGenreInteret) {
        fromIdGenreInteret = newFromIdGenreInteret;
    }
}
