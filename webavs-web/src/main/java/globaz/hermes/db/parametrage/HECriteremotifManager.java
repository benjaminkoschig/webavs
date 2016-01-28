package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (04.11.2002 11:51:26)
 * 
 * @author: Administrator
 */
public class HECriteremotifManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (CRI_IDCRITEREMOTIF) */
    private String forCri_idcriteremotif = new String();
    /** (IDCRITERE) */
    private String forIdcritere = new String();
    /** Fichier HECRMOP */
    /** (IDCRITEREMOTIF) */
    private String forIdcriteremotif = new String();
    /** (CRI_IDCRITEREMOTIF) */
    private String fromCri_idcriteremotif = new String();
    /** (IDCRITEREMOTIF) */
    private String fromIdcriteremotif = new String();
    // l'ensemble de la hiérarchie
    protected Vector liste = new Vector();

    /**
     * Commentaire relatif au constructeur HecriteremotifManager.
     */
    public HECriteremotifManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HECRMOP";
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
        if (getForIdcriteremotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBICRM=" + _dbWriteNumeric(statement.getTransaction(), getForIdcriteremotif());
        }
        // traitement du positionnement
        if (getForCri_idcriteremotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBICRP=" + _dbWriteNumeric(statement.getTransaction(), getForCri_idcriteremotif());
        }
        // traitement du positionnement
        if (getForIdcritere().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBTCRI=" + _dbWriteNumeric(statement.getTransaction(), getForIdcritere());
        }
        // traitement du positionnement
        if (getFromIdcriteremotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBICRM>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdcriteremotif());
        }
        // traitement du positionnement
        if (getFromCri_idcriteremotif().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RBICRP>=" + _dbWriteNumeric(statement.getTransaction(), getFromCri_idcriteremotif());
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
        return new HECriteremotif();
    }

    public String getForCri_idcriteremotif() {
        return forCri_idcriteremotif;
    }

    public String getForIdcritere() {
        return forIdcritere;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdcriteremotif() {
        return forIdcriteremotif;
    }

    public String getFromCri_idcriteremotif() {
        return fromCri_idcriteremotif;
    }

    public String getFromIdcriteremotif() {
        return fromIdcriteremotif;
    }

    public String getIndent(int level) {
        String back = "";
        for (int i = 0; i < level; i++) {
            back += "&nbsp;&nbsp;";
        }
        return level + back;
    }

    /**
     * Très sloppy :/<br>
     * Permet d'appeller en fait plusieurs fois le manager<br>
     * dans le cas d'une relation reflexive...<br>
     * Tentative d'implémentation d'un faux manager échouée
     */
    public void init(String id) {
        int level = 1;
        for (int i = 0; i < size(); i++) {
            HECriteremotif courant = (HECriteremotif) getEntity(i);
            // Le niveau de profondeur (1 pour le père, 2 pour le fils, 3 pour
            // le petit-fils...)
            courant.setLevel(getIndent(level));
            // L'id du père (pour les updates !)
            courant.setFatherId(id);
            // on ajoute
            liste.addElement(courant);
            // On appelle notre super méthode récursive
            // on passe en param la liste ET le numéro courant
            liste = seekAllChildren(liste, courant.getIdcriteremotif(), level + 1, id);
        }
    }

    /**
     * Super méthode récursive on passe en param la liste ET le numéro courant
     * 
     * @param table
     *            la liste de toute la hiérarchie
     * @param id
     *            l'id de l'objet parent courant
     * @param level
     *            le niveau hiérarchique
     * @param father
     *            l'id de l'elt le plus haut
     * @return la liste
     */
    public Vector seekAllChildren(Vector table, String id, int level, String father) {
        HECriteremotifManager cm = new HECriteremotifManager();
        cm.setSession(getSession());
        cm.setForCri_idcriteremotif(id);
        try {
            cm.find();
            for (int i = 0; i < cm.size(); i++) { // on les balaye
                HECriteremotif enfant = (HECriteremotif) cm.getEntity(i);
                // on écrit dans la table ce qu'on a trouvé
                enfant.setLevel(getIndent(level));
                enfant.setFatherId(father);
                table.addElement(enfant);
                // puis on cherche ses enfants
                table = seekAllChildren(table, enfant.getIdcriteremotif(), level + 1, father);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public void setForCri_idcriteremotif(String newForCri_idcriteremotif) {
        forCri_idcriteremotif = newForCri_idcriteremotif;
    }

    public void setForIdcritere(String newForIdcritere) {
        forIdcritere = newForIdcritere;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdcriteremotif(String newForIdcriteremotif) {
        forIdcriteremotif = newForIdcriteremotif;
    }

    public void setFromCri_idcriteremotif(String newFromCri_idcriteremotif) {
        fromCri_idcriteremotif = newFromCri_idcriteremotif;
    }

    public void setFromIdcriteremotif(String newFromIdcriteremotif) {
        fromIdcriteremotif = newFromIdcriteremotif;
    }
}
