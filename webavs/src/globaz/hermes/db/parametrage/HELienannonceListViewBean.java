package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (14.11.2002 08:17:35)
 * 
 * @author: ado
 */
public class HELienannonceListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Fichier HELIANP */
    /** (RIILIA) */
    private String forIdLienAnnonce = new String();
    /** (RITMES) */
    private String forIdMessage = new String();
    /** (RHIMET) */
    private String forIdMethode = new String();
    /** (RITMOT) */
    private String forIdMotif = new String();
    /** (REIPAE) */
    private String forIdParametrageAnnonce = new String();
    /** (HEP_REIPAE) */
    private String forPar_idParametrageAnnonce = new String();
    /** (REIPAE) */
    private String fromIdParametrageAnnonce = new String();

    /**
     * Commentaire relatif au constructeur HELienannonceListViewBean.
     */
    public HELienannonceListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HELIANP";
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
        if (getForIdLienAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RIILIA=" + _dbWriteNumeric(statement.getTransaction(), getForIdLienAnnonce());
        }
        // traitement du positionnement
        if (getForIdMethode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RHIMET=" + _dbWriteNumeric(statement.getTransaction(), getForIdMethode());
        }
        // traitement du positionnement
        if (getForIdParametrageAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "REIPAE=" + _dbWriteNumeric(statement.getTransaction(), getForIdParametrageAnnonce());
        }
        // traitement du positionnement
        if (getForPar_idParametrageAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HEP_REIPAE=" + _dbWriteNumeric(statement.getTransaction(), getForPar_idParametrageAnnonce());
        }
        // traitement du positionnement
        if (getForIdMotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RITMOT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMotif());
        }
        // traitement du positionnement
        if (getForIdMessage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RITMES=" + _dbWriteNumeric(statement.getTransaction(), getForIdMessage());
        }
        // traitement du positionnement
        if (getFromIdParametrageAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "REIPAE>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdParametrageAnnonce());
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
        return new HELienannonceViewBean();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdLienAnnonce() {
        return forIdLienAnnonce;
    }

    public String getForIdMessage() {
        return forIdMessage;
    }

    public String getForIdMethode() {
        return forIdMethode;
    }

    public String getForIdMotif() {
        return forIdMotif;
    }

    public String getForIdParametrageAnnonce() {
        return forIdParametrageAnnonce;
    }

    public String getForPar_idParametrageAnnonce() {
        return forPar_idParametrageAnnonce;
    }

    public String getFromIdParametrageAnnonce() {
        return fromIdParametrageAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdLienAnnonce(String newForIdLienAnnonce) {
        forIdLienAnnonce = newForIdLienAnnonce;
    }

    public void setForIdMessage(String newForIdMessage) {
        forIdMessage = newForIdMessage;
    }

    public void setForIdMethode(String newForIdMethode) {
        forIdMethode = newForIdMethode;
    }

    public void setForIdMotif(String newForIdMotif) {
        forIdMotif = newForIdMotif;
    }

    public void setForIdParametrageAnnonce(String newForIdParametrageAnnonce) {
        forIdParametrageAnnonce = newForIdParametrageAnnonce;
    }

    public void setForPar_idParametrageAnnonce(String newForPar_idParametrageAnnonce) {
        forPar_idParametrageAnnonce = newForPar_idParametrageAnnonce;
    }

    public void setFromIdParametrageAnnonce(String newFromIdParametrageAnnonce) {
        fromIdParametrageAnnonce = newFromIdParametrageAnnonce;
    }
}
