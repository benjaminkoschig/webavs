/*
 * Créé le 16 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPParametrePlausibiliteManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forActif;
    private Boolean forEnAvertissement = Boolean.FALSE;
    private String forIdPlausibilite = "";
    private String inTypeMessage = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPPARAP";
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return " IXPRIO ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (getForIdPlausibilite().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPIDPLAU = " + this._dbWriteNumeric(statement.getTransaction(), getForIdPlausibilite());
        }
        // traitement du positionnement
        if (forActif != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IXACTI = "
                    + this._dbWriteBoolean(statement.getTransaction(), getForActif(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        // traitement du positionnement
        if (forEnAvertissement.booleanValue() == true) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IXTMSG = " + CPParametrePlausibilite.CS_MSG_AVERTISSEMENT;
        }

        // Compris dans une sélection
        if (getInTypeMessage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IXTMSG in (" + getInTypeMessage() + ")";
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
        return new CPParametrePlausibilite();
    }

    /**
     * @return
     */
    public Boolean getForActif() {
        return forActif;
    }

    public Boolean getForEnAvertissement() {
        return forEnAvertissement;
    }

    /**
     * @return
     */
    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getInTypeMessage() {
        return inTypeMessage;
    }

    /**
     * @param boolean1
     */
    public void setForActif(Boolean boolean1) {
        forActif = boolean1;
    }

    public void setForEnAvertissement(Boolean forEnAvertissement) {
        this.forEnAvertissement = forEnAvertissement;
    }

    /**
     * @param string
     */
    public void setForIdPlausibilite(String string) {
        forIdPlausibilite = string;
    }

    public void setInTypeMessage(String inTypeMessage) {
        this.inTypeMessage = inTypeMessage;
    }

}
