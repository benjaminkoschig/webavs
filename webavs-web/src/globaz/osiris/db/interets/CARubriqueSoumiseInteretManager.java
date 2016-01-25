package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 10:10:41)
 * 
 * @author: Administrator
 */
public class CARubriqueSoumiseInteretManager extends BManager {
    /** Fichier CAIMRSP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IDPLACALINT) */
    private String forIdPlanCalculInteret = new String();
    /** (IDRUBRIQUE) */
    private String forIdRubrique = new String();
    /** (IDPLACALINT) */
    private String fromIdPlanCalculInteret = new String();
    /** (IDRUBRIQUE) */
    private String fromIdRubrique = new String();

    /**
     * Commentaire relatif au constructeur CARubriqueSoumiseInteretManager.
     */
    public CARubriqueSoumiseInteretManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CAIMRSP rs inner join " + _getCollection()
                + "CARUBRP ru on ru.idrubrique = rs.idrubrique";
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
        return "ru.idexterne";
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
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "rs.IDRUBRIQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // traitement du positionnement
        if (getForIdPlanCalculInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPLACALINT=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanCalculInteret());
        }

        // traitement du positionnement
        if (getFromIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "rs.IDRUBRIQUE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdRubrique());
        }

        // traitement du positionnement
        if (getFromIdPlanCalculInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPLACALINT>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdPlanCalculInteret());
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
        return new CARubriqueSoumiseInteret();
    }

    public String getForIdPlanCalculInteret() {
        return forIdPlanCalculInteret;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getFromIdPlanCalculInteret() {
        return fromIdPlanCalculInteret;
    }

    public String getFromIdRubrique() {
        return fromIdRubrique;
    }

    public void setForIdPlanCalculInteret(String newForIdPlanCalculInteret) {
        forIdPlanCalculInteret = newForIdPlanCalculInteret;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdRubrique(String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    public void setFromIdPlanCalculInteret(String newFromIdPlanCalculInteret) {
        fromIdPlanCalculInteret = newFromIdPlanCalculInteret;
    }

    public void setFromIdRubrique(String newFromIdRubrique) {
        fromIdRubrique = newFromIdRubrique;
    }
}
