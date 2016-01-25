package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Insérez la description du type ici. Date de création : (11.11.2002 18:07:15)
 * 
 * @author: ado
 */
public class HEParametrageannonceManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (RENCED) mais APRES ! */
    private String forAfterCodeEnregistrementDebut = new String();
    /** (RENCEF) mais AVANT ! */
    private String forBeforeCodeEnregistrementFin = new String();
    /** (RENCED) */
    private String forCodeEnregistrementDebut = new String();
    /** (RENCEF) */
    private String forCodeEnregistrementFin = new String();
    /** (RETLIB) */
    private String forIdCSCodeApplication = new String();
    /** Fichier HEPAREP */
    /** (REIPAE) */
    private String forIdParametrageAnnonce = new String();
    /** (RETUTI) */
    private String forIdUtilisateur = new String();
    /** From RETLIB */
    private String fromIdCSCodeApplication = new String();

    /**
     * Commentaire relatif au constructeur HEParametrageAnnonceManager.
     */
    public HEParametrageannonceManager() {
        super();
    }

    /**
     * Constructor HEParametrageannonceManager.
     * 
     * @param bSession
     */
    public HEParametrageannonceManager(BSession bSession) {
        this();
        setSession(bSession);
    }

    @Override
    public void _beforeFind(BTransaction transaction) {
        FWParametersSystemCodeManager sc = new FWParametersSystemCodeManager();
        sc.setForCodeUtilisateur(getFromIdCSCodeApplication());
        sc.setForIdGroupe("HECODAPP");
        sc.setSession(getSession());
        try {
            sc.find(transaction);
            setFromIdCSCodeApplication(((FWParametersSystemCode) sc.getEntity(0)).getIdCode());
        } catch (Exception e) {
            // tant pis, recherche échoue....
            e.printStackTrace();
            String s = e.getMessage();
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEPAREP";
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
        return "RETLIB";
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
        if (getForIdParametrageAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "REIPAE=" + _dbWriteNumeric(statement.getTransaction(), getForIdParametrageAnnonce());
        }
        // traitement du positionnement
        if (getForCodeEnregistrementDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RENCED=" + _dbWriteNumeric(statement.getTransaction(), getForCodeEnregistrementDebut());
        }
        // traitement du positionnement
        if (getForCodeEnregistrementFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RENCEF=" + _dbWriteNumeric(statement.getTransaction(), getForCodeEnregistrementFin());
        }
        // traitement du positionnement
        if (getForAfterCodeEnregistrementDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RENCED<=" + _dbWriteNumeric(statement.getTransaction(), getForAfterCodeEnregistrementDebut());
        }
        // traitement du positionnement
        if (getForBeforeCodeEnregistrementFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RENCEF>=" + _dbWriteNumeric(statement.getTransaction(), getForBeforeCodeEnregistrementFin());
        }
        // traitement du positionnement
        if (getForIdCSCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RETLIB=" + _dbWriteNumeric(statement.getTransaction(), getForIdCSCodeApplication());
        }
        // traitement du positionnement
        if (getForIdUtilisateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RETUTI=" + _dbWriteNumeric(statement.getTransaction(), getForIdUtilisateur());
        }
        // traitement du positionnement
        if (getFromIdCSCodeApplication().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RETLIB>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdCSCodeApplication());
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
        return new HEParametrageannonce();
    }

    //
    public String getForAfterCodeEnregistrementDebut() {
        return forAfterCodeEnregistrementDebut;
    }

    public String getForBeforeCodeEnregistrementFin() {
        return forBeforeCodeEnregistrementFin;
    }

    public String getForCodeEnregistrementDebut() {
        return forCodeEnregistrementDebut;
    }

    public String getForCodeEnregistrementFin() {
        return forCodeEnregistrementFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 09:22:45)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCSCodeApplication() {
        return forIdCSCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdParametrageAnnonce() {
        return forIdParametrageAnnonce;
    }

    public String getForIdUtilisateur() {
        return forIdUtilisateur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 09:46:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdCSCodeApplication() {
        return fromIdCSCodeApplication;
    }

    //
    public void setForAfterCodeEnregistrementDebut(String acd) {
        forAfterCodeEnregistrementDebut = acd;
    }

    public void setForBeforeCodeEnregistrementFin(String ce) {
        forBeforeCodeEnregistrementFin = ce;
    }

    public void setForCodeEnregistrementDebut(String newForCodeEnregistrementDebut) {
        forCodeEnregistrementDebut = newForCodeEnregistrementDebut;
    }

    public void setForCodeEnregistrementFin(String newForCodeEnregistrementFin) {
        forCodeEnregistrementFin = newForCodeEnregistrementFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 09:22:45)
     * 
     * @param newForIdCSCodeApplication
     *            java.lang.String
     */
    public void setForIdCSCodeApplication(java.lang.String newForIdCSCodeApplication) {
        forIdCSCodeApplication = newForIdCSCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdParametrageAnnonce(String newForIdParametrageAnnonce) {
        forIdParametrageAnnonce = newForIdParametrageAnnonce;
    }

    public void setForIdUtilisateur(String newForIdUtilisateur) {
        forIdUtilisateur = newForIdUtilisateur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 09:46:53)
     * 
     * @param newFromIdCSCodeApplication
     *            java.lang.String
     */
    public void setFromIdCSCodeApplication(java.lang.String newFromIdCSCodeApplication) {
        fromIdCSCodeApplication = newFromIdCSCodeApplication;
    }
}
