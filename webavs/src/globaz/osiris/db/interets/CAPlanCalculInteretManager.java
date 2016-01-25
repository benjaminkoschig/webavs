package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CAPlanCalculInteretManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPlanCalculInteret = new String();
    private String fromIdPlanCalculInteret = new String();

    /**
     * Commentaire relatif au constructeur CAPlanCalculInteretManager.
     */
    public CAPlanCalculInteretManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAPlanCalculInteret.TABLE_CAIMPLP;
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
        if (getForIdPlanCalculInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAPlanCalculInteret.FIELD_IDPLACALINT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanCalculInteret());
        }

        // traitement du positionnement
        if (getFromIdPlanCalculInteret().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAPlanCalculInteret.FIELD_IDPLACALINT + ">="
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
        return new CAPlanCalculInteret();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdPlanCalculInteret() {
        return forIdPlanCalculInteret;
    }

    public String getFromIdPlanCalculInteret() {
        return fromIdPlanCalculInteret;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdPlanCalculInteret(String newForIdPlanCalculInteret) {
        forIdPlanCalculInteret = newForIdPlanCalculInteret;
    }

    public void setFromIdPlanCalculInteret(String newFromIdPlanCalculInteret) {
        fromIdPlanCalculInteret = newFromIdPlanCalculInteret;
    }
}
