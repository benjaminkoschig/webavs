/*
 * Créé le 30 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPLienCommunicationsPlausiManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCommunication = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer("");
        from.append(_getCollection() + "CPLCRPP");
        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return " IXIDPA ASC ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdCommunication())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IBIDCF = " + getForIdCommunication();
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPLienCommunicationsPlausi();
    }

    public String getForIdCommunication() {
        return forIdCommunication;
    }

    public void setForIdCommunication(String forIdCommunication) {
        this.forIdCommunication = forIdCommunication;
    }
}
