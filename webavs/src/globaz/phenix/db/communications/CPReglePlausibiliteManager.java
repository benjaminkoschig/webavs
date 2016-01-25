/*
 * Créé le 15 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPReglePlausibiliteManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forActif;
    private String forCanton = "";
    private String forDeclenchement = "";
    private String forIdPlausibilite = "";

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return " IPPRIO ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdPlausibilite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPIDPLAU = " + _dbWriteNumeric(statement.getTransaction(), getForIdPlausibilite());
        }
        // traitement du positionnement
        if (forActif != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPACTIF = "
                    + _dbWriteBoolean(statement.getTransaction(), getForActif(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // traitement du déclenchement
        if (forDeclenchement.length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPDECL = " + _dbWriteNumeric(statement.getTransaction(), getForDeclenchement());
        }

        if (forCanton.length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPCANT = " + _dbWriteNumeric(statement.getTransaction(), getForCanton());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPReglePlausibilite();
    }

    /**
     * @return
     */
    public Boolean getForActif() {
        return forActif;
    }

    public String getForCanton() {
        return forCanton;
    }

    public String getForDeclenchement() {
        return forDeclenchement;
    }

    /**
     * @return
     */
    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    /**
     * @param boolean1
     */
    public void setForActif(Boolean boolean1) {
        forActif = boolean1;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    public void setForDeclenchement(String forDeclenchement) {
        this.forDeclenchement = forDeclenchement;
    }

    /**
     * @param string
     */
    public void setForIdPlausibilite(String string) {
        forIdPlausibilite = string;
    }

}
