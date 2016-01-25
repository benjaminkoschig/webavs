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
public class CPLienSedexCommunicationFiscaleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCommunication = "";
    private String forIdMessageSedex = "";

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
        return _getCollection() + "CPLSECOP";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return " IBMEID ASC ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        /* Positionnement par id message */
        if (!JadeStringUtil.isBlank(getForIdMessageSedex())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IBMEID = " + this._dbWriteString(statement.getTransaction(), getForIdMessageSedex());
        }
        /* Positionnement par id dommunication */
        if (!JadeStringUtil.isBlankOrZero(getForIdCommunication())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " IBIDCF = " + this._dbWriteNumeric(statement.getTransaction(), getForIdCommunication());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPLienSedexCommunicationFiscale();
    }

    public String getForIdCommunication() {
        return forIdCommunication;
    }

    public String getForIdMessageSedex() {
        return forIdMessageSedex;
    }

    public void setForIdCommunication(String forIdCommunication) {
        this.forIdCommunication = forIdCommunication;
    }

    public void setForIdMessageSedex(String forIdCommunication) {
        forIdMessageSedex = forIdCommunication;
    }
}
