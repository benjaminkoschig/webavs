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
public class CPRegleParametrePlausibiliteManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forActifParametre;
    private Boolean forActifRegle;
    private String forCanton = "";
    private Boolean forEnAvertissement = Boolean.FALSE;
    private String forIdPlausibilite = "";
    private String inTypeMessage = "";

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPREGPLP T1";
        String table2 = "CPPARAP T2";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (T1.IPIDPLAU=T2.IPIDPLAU)";
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return " T1.IPIDPLAU, IXPRIO ";
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
            sqlWhere += "IPIDPLAU = " + _dbWriteNumeric(statement.getTransaction(), getForIdPlausibilite());
        }
        // traitement du positionnement pour les parametres (plausibilités)
        // actifs
        if (forActifParametre != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IXACTI = "
                    + _dbWriteBoolean(statement.getTransaction(), getForActifParametre(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // traitement du positionnement pour les règles actives
        if (forActifRegle != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPACTIF = "
                    + _dbWriteBoolean(statement.getTransaction(), getForActifRegle(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // traitement du positionnement pour un canton
        if (forCanton.length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IPCANT = " + _dbWriteNumeric(statement.getTransaction(), getForCanton());
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
        return new CPRegleParametrePlausibilite();
    }

    public Boolean getForActifParametre() {
        return forActifParametre;
    }

    public Boolean getForActifRegle() {
        return forActifRegle;
    }

    public String getForCanton() {
        return forCanton;
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

    public void setForActifParametre(Boolean forActifParametre) {
        this.forActifParametre = forActifParametre;
    }

    public void setForActifRegle(Boolean forActifRegle) {
        this.forActifRegle = forActifRegle;
    }

    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
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
