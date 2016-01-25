package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;

/**
 * Insérez la description du type ici. Date de création : (13.11.2002 13:51:13)
 * 
 * @author: ado
 */
public class HEMethodeListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (RHTLIB) */
    private String forIdLibelle = new String();
    /** Fichier HEMETHP */
    /** (RHIMET) */
    private String forIdMethode = new String();
    /** (RHTLIB) */
    private String fromIdLibelle = new String();
    /** (RHIMET) */
    private String fromIdMethode = new String();
    private String fromLibelle = new String();

    /**
     * Commentaire relatif au constructeur HEMethodeListViewBean.
     */
    public HEMethodeListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "HEMETHP";
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
        if (getForIdMethode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RHIMET=" + _dbWriteNumeric(statement.getTransaction(), getForIdMethode());
        }
        // traitement du positionnement
        if (getForIdLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RHTLIB=" + _dbWriteNumeric(statement.getTransaction(), getForIdLibelle());
        }
        // traitement du positionnement
        if (getFromIdMethode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RHIMET>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdMethode());
        }
        // traitement du positionnement
        if (getFromIdLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RHTLIB>=" + _dbWriteNumeric(statement.getTransaction(), getFromIdLibelle());
        }
        // traitement du positionnement spécial...
        if (getFromLibelle().length() != 0) {
            FWParametersSystemCodeManager scM = new FWParametersSystemCodeManager();
            try {
                scM.setForIdTypeCode("11100004");
                scM.setForIdGroupe("HEMETHP");
                scM.setSession(getSession());
                scM.setForIdLangue(getSession().getIdLangue());
                scM.setFromLibelle(getFromLibelle());
                scM.find();
                if (scM.size() > 0) { // si y'a des CS qui correspondent
                    String tmpWhere = "";
                    if (sqlWhere.length() != 0) {
                        tmpWhere += " AND (";
                    }
                    for (int i = 0; i < scM.size(); i++) {
                        tmpWhere += "RHTLIB="
                                + _dbWriteNumeric(statement.getTransaction(),
                                        ((FWParametersSystemCode) scM.getEntity(i)).getIdCode());
                        if (i < scM.size() - 1) { // pas le dernier
                            tmpWhere += " OR ";
                        }
                    }
                    if (sqlWhere.length() != 0) {
                        tmpWhere += " )";
                    }
                    sqlWhere += tmpWhere;
                }
            } catch (Exception e) { // tant pis pour la recherche
                e.printStackTrace();
                String s = e.getMessage();
            }
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
        return new HEMethodeViewBean();
    }

    public String getForIdLibelle() {
        return forIdLibelle;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdMethode() {
        return forIdMethode;
    }

    public String getFromIdLibelle() {
        return fromIdLibelle;
    }

    public String getFromIdMethode() {
        return fromIdMethode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 15:39:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromLibelle() {
        return fromLibelle;
    }

    public void setForIdLibelle(String newForIdLibelle) {
        forIdLibelle = newForIdLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setForIdMethode(String newForIdMethode) {
        forIdMethode = newForIdMethode;
    }

    public void setFromIdLibelle(String newFromIdLibelle) {
        fromIdLibelle = newFromIdLibelle;
    }

    public void setFromIdMethode(String newFromIdMethode) {
        fromIdMethode = newFromIdMethode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 15:39:43)
     * 
     * @param newFromLibelle
     *            java.lang.String
     */
    public void setFromLibelle(java.lang.String newFromLibelle) {
        fromLibelle = newFromLibelle;
    }
}
