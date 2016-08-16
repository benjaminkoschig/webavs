package globaz.osiris.db.ordres;

import globaz.globall.db.BManager;
import globaz.osiris.api.ordre.APIOrganeExecution;
import java.io.Serializable;

/**
 * CA organe d'exécution manager Date de création : (13.12.2001 12:16:28)
 * 
 * @author:Brand
 */
public class CAOrganeExecutionManager extends BManager implements Serializable {
    private static final long serialVersionUID = -2981642894616374872L;
    private java.lang.String forIdOrganeExecution = new String();
    private String fromIdOrganeExecution = "";
    private String fromNom = new String();
    private boolean forIdTypeTraitementOG = false;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAOREXP";
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
        // les composants de la requête initialisés avec les options par défaut
        String sqlWhere = "";

        // Traitement du positionnement depuis un IdOrganeExecution
        if (getForIdOrganeExecution().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDORGANEEXECUTION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecution());
        }

        // Traitement du positionnement depuis un nom
        if (getFromNom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NOM <= " + this._dbWriteNumeric(statement.getTransaction(), getFromNom());

        }

        if (getFromIdOrganeExecution().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDORGANEEXECUTION <= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdOrganeExecution());

        }

        if (isForIdTypeTraitementOG()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPETRAITEMENTOG IN (" + APIOrganeExecution.OG_OPAE_DTA + ","
                    + APIOrganeExecution.OG_ISO_20022 + ")";
        }
        return sqlWhere;

    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAOrganeExecution();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2002 15:16:02)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdOrganeExecution() {
        return forIdOrganeExecution;
    }

    /**
     * @author: sel Créé le : 15 nov. 06
     * @return
     */
    public String getFromIdOrganeExecution() {
        return fromIdOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2002 15:59:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNom() {
        return fromNom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2002 15:16:02)
     * 
     * @param newForIdOrganeExecution
     *            java.lang.String
     */
    public void setForIdOrganeExecution(java.lang.String newForIdOrganeExecution) {
        forIdOrganeExecution = newForIdOrganeExecution;
    }

    /**
     * @author: sel Créé le : 15 nov. 06
     * @param fromIdOrganeExecution
     */
    public void setFromIdOrganeExecution(String fromIdOrganeExecution) {
        this.fromIdOrganeExecution = fromIdOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2002 15:59:29)
     * 
     * @param newFromNom
     *            java.lang.String
     */
    public void setFromNom(java.lang.String newFromNom) {
        fromNom = newFromNom;
    }

    /**
     * @return the forIdTypeTraitementOG
     */
    public boolean isForIdTypeTraitementOG() {
        return forIdTypeTraitementOG;
    }

    /**
     * warning, this is missnamed function, this take boolean to know if must select only OE with specified
     * TypeTraitement or all
     * 
     * @param forIdTypeTraitementOG the forIdTypeTraitementOG to set
     */
    public void setForIdTypeTraitementOG(boolean forIdTypeTraitementOG) {
        this.forIdTypeTraitementOG = forIdTypeTraitementOG;
    }
}
