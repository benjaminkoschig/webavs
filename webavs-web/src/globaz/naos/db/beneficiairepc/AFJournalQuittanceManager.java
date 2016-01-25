package globaz.naos.db.beneficiairepc;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité Quittance.
 * 
 * @author jpa
 */
public class AFJournalQuittanceManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forDateCreation = "";
    private String forDescriptionJournal = "";
    private String forEtat = "";
    private String forIdJournalQuittance = "";
    private String forNombreQuittances = "";
    private String order;

    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + "AFJRNQU";
        return fromClause;
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFJRNQU.MAJANN="
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        if (!JadeStringUtil.isEmpty(getForIdJournalQuittance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFJRNQU.MAJQID="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalQuittance());
        }
        if (!JadeStringUtil.isEmpty(getForEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFJRNQU.MAJQET="
                    + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFJournalQuittance();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDateCreation() {
        return forDateCreation;
    }

    public String getForDescriptionJournal() {
        return forDescriptionJournal;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdJournalQuittance() {
        return forIdJournalQuittance;
    }

    public String getForNombreQuittances() {
        return forNombreQuittances;
    }

    public String getOrder() {
        return order;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForDateCreation(String forDateCreation) {
        this.forDateCreation = forDateCreation;
    }

    public void setForDescriptionJournal(String forDescriptionJournal) {
        this.forDescriptionJournal = forDescriptionJournal;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForIdJournalQuittance(String forIdJournalQuittance) {
        this.forIdJournalQuittance = forIdJournalQuittance;
    }

    public void setForNombreQuittances(String forNombreQuittances) {
        this.forNombreQuittances = forNombreQuittances;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
