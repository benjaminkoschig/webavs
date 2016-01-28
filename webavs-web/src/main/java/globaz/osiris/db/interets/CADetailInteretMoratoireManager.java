package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 10:53:59)
 * 
 * @author: Administrator
 */
public class CADetailInteretMoratoireManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Fichier CAIMDEP */

    private String forDomaine = "CA";
    /** (IDDETINTMOR) */
    private String forIdDetailInteretMoratoire = new String();
    /** (IDINTERETMORATOIRE) */
    private String forIdInteretMoratoire = new String();
    /** (DATEDEBUT) */
    private String fromDateDebut = new String();
    /** (IDDETINTMOR) */
    private String fromIdDetailInteretMoratoire = new String();
    /** (IDINTERETMORATOIRE) */
    private String fromIdInteretMoratoire = new String();

    private String orderBy = CADetailInteretMoratoire.FIELD_DATEDEBUT + " asc";

    /**
     * Commentaire relatif au constructeur CADetailInteretMoratoireManager.
     */
    public CADetailInteretMoratoireManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CADetailInteretMoratoire.TABLE_CAIMDEP;
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
        return getOrderBy();
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
        if (getForIdDetailInteretMoratoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CADetailInteretMoratoire.FIELD_IDDETINTMOR + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdDetailInteretMoratoire());
        }

        // traitement du positionnement
        if (getForIdInteretMoratoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CADetailInteretMoratoire.FIELD_IDINTERETMORATOIRE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdInteretMoratoire());
        }

        // traitement du positionnement
        if (getFromIdDetailInteretMoratoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CADetailInteretMoratoire.FIELD_IDDETINTMOR + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdDetailInteretMoratoire());
        }

        // traitement du positionnement
        if (getFromIdInteretMoratoire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CADetailInteretMoratoire.FIELD_IDINTERETMORATOIRE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdInteretMoratoire());
        }

        // traitement du positionnement
        if (getFromDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CADetailInteretMoratoire.FIELD_DATEDEBUT + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
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
        return new CADetailInteretMoratoire();
    }

    /**
     * @return
     */
    public String getForDomaine() {
        return forDomaine;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdDetailInteretMoratoire() {
        return forIdDetailInteretMoratoire;
    }

    public String getForIdInteretMoratoire() {
        return forIdInteretMoratoire;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getFromIdDetailInteretMoratoire() {
        return fromIdDetailInteretMoratoire;
    }

    public String getFromIdInteretMoratoire() {
        return fromIdInteretMoratoire;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isDomaineCA() {
        return forDomaine.equals("CA");
    }

    /**
     * @param string
     */
    public void setForDomaine(String string) {
        forDomaine = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdDetailInteretMoratoire(String newForIdDetailInteretMoratoire) {
        forIdDetailInteretMoratoire = newForIdDetailInteretMoratoire;
    }

    public void setForIdInteretMoratoire(String newForIdInteretMoratoire) {
        forIdInteretMoratoire = newForIdInteretMoratoire;
    }

    public void setFromDateDebut(String newFromDateDebut) {
        fromDateDebut = newFromDateDebut;
    }

    public void setFromIdDetailInteretMoratoire(String newFromIdDetailInteretMoratoire) {
        fromIdDetailInteretMoratoire = newFromIdDetailInteretMoratoire;
    }

    public void setFromIdInteretMoratoire(String newFromIdInteretMoratoire) {
        fromIdInteretMoratoire = newFromIdInteretMoratoire;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
