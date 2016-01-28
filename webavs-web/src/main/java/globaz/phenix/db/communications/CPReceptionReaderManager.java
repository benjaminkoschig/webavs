/*
 * Créé le 21 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BManager;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPReceptionReaderManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCanton = "";
    private String forIdCaisse = "";
    private String forIdCanton = "";

    // protected String _getFields(BStatement statement) {
    // return
    // "CPRECRE.IDCOMREADER AS IDCOMREADER, CPRECREP.IDCANTON AS IDCANTON, CPRECREP.FORMATXML AS FORMATXML, CPRECREP.NOMCLASSE AS NOMCLASSE";
    // }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPRECRE";
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
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCAISSE=" + _dbWriteNumeric(statement.getTransaction(), getForIdCaisse());
        }

        // traitement du positionnement
        if (getForIdCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCANTON=" + _dbWriteNumeric(statement.getTransaction(), getForIdCanton());
        }

        // traitement du positionnement
        if (getForCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CANTON=" + _dbWriteString(statement.getTransaction(), getForCanton());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPReceptionReader();
    }

    public String getForCanton() {
        return forCanton;
    }

    /**
     * @return
     */
    public String getForIdCaisse() {
        return forIdCaisse;
    }

    /**
     * @return
     */
    public String getForIdCanton() {
        return forIdCanton;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    /**
     * @param string
     */
    public void setForIdCaisse(String string) {
        forIdCaisse = string;
    }

    /**
     * @param string
     */
    public void setForIdCanton(String string) {
        forIdCanton = string;
    }

}
