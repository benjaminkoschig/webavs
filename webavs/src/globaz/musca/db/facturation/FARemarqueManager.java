package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

public class FARemarqueManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdRemarque = new String();
    private java.lang.String fromTexte = new String();

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FARemarque.TABLE_FIELDS;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAREMAP AS FAREMAP";
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

        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAREMAP.IDREMARQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        if (getFromTexte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAREMAP.TEXTE>=" + this._dbWriteString(statement.getTransaction(), getFromTexte());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FARemarque();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdRemarque() {
        return forIdRemarque;
    }

    public java.lang.String getFromTexte() {
        return fromTexte;
    }

    /**
     * Setter
     */
    public void setForIdRemarque(java.lang.String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    public void setFromTexte(java.lang.String newFromTexte) {
        fromTexte = newFromTexte;
    }
}
