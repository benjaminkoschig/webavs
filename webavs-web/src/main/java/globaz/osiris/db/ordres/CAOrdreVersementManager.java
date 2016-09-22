package globaz.osiris.db.ordres;

import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (13.12.2001 13:56:37)
 * 
 * @author: Administrator
 */
public class CAOrdreVersementManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdOrdreGroupe = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAOPOVP";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        // R�cup�rer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement selon le num�ro d'ordre group�
        if (getForIdOrdreGroupe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAOrdreVersement.FIELD_IDORDREGROUPE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreGroupe());
        }
        return sqlWhere;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "NUMTRANSACTION";
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAOrdreVersement();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.03.2002 15:35:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdOrdreGroupe() {
        return forIdOrdreGroupe;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.03.2002 15:35:03)
     * 
     * @param newForIdOdreGroupe
     *            java.lang.String
     */
    public void setForIdOrdreGroupe(java.lang.String newForIdOrdreGroupe) {
        forIdOrdreGroupe = newForIdOrdreGroupe;
    }
}
