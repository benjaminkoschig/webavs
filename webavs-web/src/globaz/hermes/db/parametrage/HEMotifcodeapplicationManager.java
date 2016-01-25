package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager pour les Motifs des codes application. Fichier HEMCAPP Date de création : (22.10.2002 13:59:08)
 * 
 * @author: Arnaud Dostes
 */
public class HEMotifcodeapplicationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Code application (RATCOA) */
    private String forIdCodeApplication = new String();
    /** id Critere Motif (RBICRM) */
    private String forIdCritereMotif = new String();
    /** id Motif (RATMOT) */
    private String forIdMotif = new String();
    /** Motif Code Application (RAIMCA) */
    private String forIdMotifCodeApplication = new String();

    /**
     * Commentaire relatif au constructeur HEMotifCodeApplicationManager.
     */
    public HEMotifcodeapplicationManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEMCAPP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "RAIMCA";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdMotifCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RAIMCA=" + _dbWriteNumeric(statement.getTransaction(), getForIdMotifCodeApplication());
        }
        // traitement du positionnement
        if (getForIdCritereMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBICRM=" + _dbWriteNumeric(statement.getTransaction(), getForIdCritereMotif());
        }
        // traitement du positionnement
        if (getForIdCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RATCOA=" + _dbWriteNumeric(statement.getTransaction(), getForIdCodeApplication());
        }
        // traitement du positionnement
        if (getForIdMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RATMOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMotif());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new HEMotifcodeapplication();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCodeApplication() {
        return forIdCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCritereMotif() {
        return forIdCritereMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMotif() {
        return forIdMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdMotifCodeApplication() {
        return forIdMotifCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @param newForIdCodeApplication
     *            java.lang.String
     */
    public void setForIdCodeApplication(java.lang.String newForIdCodeApplication) {
        forIdCodeApplication = newForIdCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @param newForIdCritereMotif
     *            java.lang.String
     */
    public void setForIdCritereMotif(java.lang.String newForIdCritereMotif) {
        forIdCritereMotif = newForIdCritereMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @param newForIdMotif
     *            java.lang.String
     */
    public void setForIdMotif(java.lang.String newForIdMotif) {
        forIdMotif = newForIdMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 14:12:04)
     * 
     * @param newForIdMotifCodeApplication
     *            java.lang.String
     */
    public void setForIdMotifCodeApplication(java.lang.String newForIdMotifCodeApplication) {
        forIdMotifCodeApplication = newForIdMotifCodeApplication;
    }
}
