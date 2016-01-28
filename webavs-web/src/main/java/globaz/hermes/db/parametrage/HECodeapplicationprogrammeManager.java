package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (26.11.2002 08:07:58)
 * 
 * @author: ado
 */
public class HECodeapplicationprogrammeManager extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.11.2002 08:12:18)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HECodeapplicationprogrammeManager codeapp = new HECodeapplicationprogrammeManager();
            codeapp.setSession(session);
            codeapp.setForIdProgramme("PAVO");
            codeapp.find();
            System.out.println(codeapp.size());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    /** (RGTCOA) */
    private String forIdCodeapplication = new String();
    /** Fichier HECOAPP */
    /** (RGICAP) */
    private String forIdCodeapplicationprogramme = new String();
    /** (RGTPRO) */
    private String forIdProgramme = new String();
    /** (RGTCOA) */
    private String fromIdCodeapplication = new String();

    /** (RGTPRO) */
    private String fromIdProgramme = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HECOAPP";
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
        if (getForIdCodeapplicationprogramme().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RGICAP=" + _dbWriteNumeric(statement.getTransaction(), getForIdCodeapplicationprogramme());
        }
        // traitement du positionnement
        if (getForIdProgramme().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RGTPRO=" + _dbWriteString(statement.getTransaction(), getForIdProgramme());
        }
        // traitement du positionnement
        if (getForIdCodeapplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RGTCOA=" + _dbWriteNumeric(statement.getTransaction(), getForIdCodeapplication());
        }
        // traitement du positionnement
        if (getFromIdProgramme().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RGTPRO>=" + _dbWriteString(statement.getTransaction(), getFromIdProgramme());
        }
        // traitement du positionnement
        if (getFromIdCodeapplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RGTCOA>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdCodeapplication());
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
        return new HECodeapplicationprogramme();
    }

    public String getForIdCodeapplication() {
        return forIdCodeapplication;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdCodeapplicationprogramme() {
        return forIdCodeapplicationprogramme;
    }

    public String getForIdProgramme() {
        return forIdProgramme;
    }

    public String getFromIdCodeapplication() {
        return fromIdCodeapplication;
    }

    public String getFromIdProgramme() {
        return fromIdProgramme;
    }

    public void setForIdCodeapplication(String newForIdCodeapplication) {
        forIdCodeapplication = newForIdCodeapplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdCodeapplicationprogramme(String newForIdCodeapplicationprogramme) {
        forIdCodeapplicationprogramme = newForIdCodeapplicationprogramme;
    }

    public void setForIdProgramme(String newForIdProgramme) {
        forIdProgramme = newForIdProgramme;
    }

    public void setFromIdCodeapplication(String newFromIdCodeapplication) {
        fromIdCodeapplication = newFromIdCodeapplication;
    }

    public void setFromIdProgramme(String newFromIdProgramme) {
        fromIdProgramme = newFromIdProgramme;
    }
}
