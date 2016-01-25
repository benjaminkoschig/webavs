package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class DSLigneDeclarationListViewBean extends BManager {
    /** Fichier DSLIDEP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (MAIAFF join on DSCECLP) */
    private String forAffiliationId = new String();
    /** (TBNANN) */
    private String forAnnee = new String();
    /** (MBIASS) */
    private String forAssuranceId = new String();
    /** (TAIDDE) */
    private String forIdDeclaration = new String();
    /** (TBILIDE) */
    private String forIdLigneDeclaration = new String();
    /** (MBIASS) */
    private String fromAssuranceId = new String();
    /** (TAIDDE) */
    private String fromIdDeclaration = new String();

    /** (TBILIDE) */
    private String fromIdLigneDeclaration = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "DSLIDEP";
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
        return "MBIASS";
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
        if (getForIdLigneDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TBILIDE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdLigneDeclaration());
        }

        // traitement du positionnement
        if (getForIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdDeclaration());
        }

        // traitement du positionnement
        if (getForAssuranceId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceId());
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TBNANN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }

        // traitement du positionnement
        if (getFromIdLigneDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TBILIDE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdLigneDeclaration());
        }

        // traitement du positionnement
        if (getFromIdDeclaration().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TAIDDE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdDeclaration());
        }

        // traitement du positionnement
        if (getFromAssuranceId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS>=" + this._dbWriteNumeric(statement.getTransaction(), getFromAssuranceId());
        }

        if (getForAffiliationId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " TAIDDE in ( select TAIDDE from " + _getCollection() + "DSDECLP where MAIAFF="
                    + getForAffiliationId() + ")";
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
        return new DSLigneDeclarationViewBean();
    }

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForAssuranceId() {
        return forAssuranceId;
    }

    public String getForIdDeclaration() {
        return forIdDeclaration;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdLigneDeclaration() {
        return forIdLigneDeclaration;
    }

    public String getFromAssuranceId() {
        return fromAssuranceId;
    }

    public String getFromIdDeclaration() {
        return fromIdDeclaration;
    }

    public String getFromIdLigneDeclaration() {
        return fromIdLigneDeclaration;
    }

    public void setForAffiliationId(String forAffiliationId) {
        this.forAffiliationId = forAffiliationId;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForAssuranceId(String newForAssuranceId) {
        forAssuranceId = newForAssuranceId;
    }

    public void setForIdDeclaration(String newForIdDeclaration) {
        forIdDeclaration = newForIdDeclaration;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newD
     *            String
     */

    public void setForIdLigneDeclaration(String newForIdLigneDeclaration) {
        forIdLigneDeclaration = newForIdLigneDeclaration;
    }

    public void setFromAssuranceId(String newFromAssuranceId) {
        fromAssuranceId = newFromAssuranceId;
    }

    public void setFromIdDeclaration(String newFromIdDeclaration) {
        fromIdDeclaration = newFromIdDeclaration;
    }

    public void setFromIdLigneDeclaration(String newFromIdLigneDeclaration) {
        fromIdLigneDeclaration = newFromIdLigneDeclaration;
    }

}