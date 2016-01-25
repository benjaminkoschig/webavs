/*
 * Créé le 14 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.db.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author dostes Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEInfosManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (HEA_RNIANN) */
    private String forIdArc = new String();
    /** Fichier WEBAVS_D.HEINCOP */
    /** (IDINCO) */
    private String forIdInfoComp = new String();
    /** (LIBINCO) */
    private String forLibInfo = new String();
    /** (TYPEINCO) */
    private String forTypeInfo = new String();
    /** (HEA_RNIANN) */
    private String fromIdArc = new String();
    /** (IDINCO) */
    private String fromIdInfoComp = new String();
    /** (LIBINCO) */
    private String fromLibInfo = new String();
    /** (TYPEINCO) */
    private String fromTypeInfo = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEINCOP";
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
        if (getForIdInfoComp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDINCO=" + _dbWriteNumeric(statement.getTransaction(), getForIdInfoComp());
        }
        // traitement du positionnement
        if (getForIdArc().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNIANN=" + _dbWriteNumeric(statement.getTransaction(), getForIdArc());
        }
        // traitement du positionnement
        if (getForTypeInfo().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TYPEINCO=" + _dbWriteNumeric(statement.getTransaction(), getForTypeInfo());
        }
        // traitement du positionnement
        if (getForLibInfo().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBINCO=" + _dbWriteString(statement.getTransaction(), getForLibInfo());
        }
        // traitement du positionnement
        if (getFromIdInfoComp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDINCO>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdInfoComp());
        }
        // traitement du positionnement
        if (getFromIdArc().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RNIANN>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdArc());
        }
        // traitement du positionnement
        if (getFromTypeInfo().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TYPEINCO>=" + _dbWriteNumeric(statement.getTransaction(), getFromTypeInfo());
        }
        // traitement du positionnement
        if (getFromLibInfo().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBINCO>=" + _dbWriteString(statement.getTransaction(), getFromLibInfo());
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
        return new HEInfos();
    }

    public String getForIdArc() {
        return forIdArc;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdInfoComp() {
        return forIdInfoComp;
    }

    public String getForLibInfo() {
        return forLibInfo;
    }

    public String getForTypeInfo() {
        return forTypeInfo;
    }

    public String getFromIdArc() {
        return fromIdArc;
    }

    public String getFromIdInfoComp() {
        return fromIdInfoComp;
    }

    public String getFromLibInfo() {
        return fromLibInfo;
    }

    public String getFromTypeInfo() {
        return fromTypeInfo;
    }

    public void setForIdArc(String newForIdArc) {
        forIdArc = newForIdArc;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdInfoComp(String newForIdInfoComp) {
        forIdInfoComp = newForIdInfoComp;
    }

    public void setForLibInfo(String newForLibInfo) {
        forLibInfo = newForLibInfo;
    }

    public void setForTypeInfo(String newForTypeInfo) {
        forTypeInfo = newForTypeInfo;
    }

    public void setFromIdArc(String newFromIdArc) {
        fromIdArc = newFromIdArc;
    }

    public void setFromIdInfoComp(String newFromIdInfoComp) {
        fromIdInfoComp = newFromIdInfoComp;
    }

    public void setFromLibInfo(String newFromLibInfo) {
        fromLibInfo = newFromLibInfo;
    }

    public void setFromTypeInfo(String newFromTypeInfo) {
        fromTypeInfo = newFromTypeInfo;
    }
}
