package globaz.naos.db.tauxAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité TauxMoyen.
 * 
 * @author dgi
 */
public class AFTauxMoyenManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnnee;
    private java.lang.String forIdAffiliation;
    private java.lang.String forIdAssurance;
    private java.lang.String order;

    private java.lang.String untilAnnee;

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

        if (!JadeStringUtil.isEmpty(getForIdAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation());
        }

        if (!JadeStringUtil.isEmpty(getForIdAssurance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAssurance());
        }

        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String debutAnnee = getForAnnee() + "0101";
            String finAnnee = getForAnnee() + "1231";
            sqlWhere += "(" + "MCDDEB >= " + debutAnnee + " AND " + "MCDDEB <= " + finAnnee + ")";
        }

        if (!JadeStringUtil.isEmpty(getUntilAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String finAnnee = getUntilAnnee() + "1231";
            sqlWhere += "MCDDEB <= " + finAnnee;
        }
        if (JadeStringUtil.isEmpty(order)) {
            setOrder("MCDDEB DESC");
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
        return new AFTauxMoyen();
    }

    public java.lang.String getForAnnee() {
        return forAnnee;
    }

    public java.lang.String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public java.lang.String getForIdAssurance() {
        return forIdAssurance;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public java.lang.String getUntilAnnee() {
        return untilAnnee;
    }

    public void setForAnnee(java.lang.String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdAffiliation(java.lang.String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForIdAssurance(java.lang.String forIdAssurance) {
        this.forIdAssurance = forIdAssurance;
    }

    public void setOrder(java.lang.String order) {
        this.order = order;
    }

    public void setUntilAnnee(java.lang.String untilAnnee) {
        this.untilAnnee = untilAnnee;
    }

}
