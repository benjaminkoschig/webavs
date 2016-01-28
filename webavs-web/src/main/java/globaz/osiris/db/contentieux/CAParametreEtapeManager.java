package globaz.osiris.db.contentieux;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (17.12.2001 08:10:11)
 * 
 * @author: Administrator
 */
public class CAParametreEtapeManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String ORDER_SEQUENCE_DESC = "1000";
    private java.lang.String beforeNoSequence = new String();
    private java.lang.String forIdSequence = new String();
    private java.lang.String forIdSequenceContentieux = new String();
    private java.lang.String orderBy = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAPECTP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy().equalsIgnoreCase(CAParametreEtapeManager.ORDER_SEQUENCE_DESC)) {
            return "IDSEQCON, SEQUENCE DESC";
        }

        return "SEQUENCE";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un num�ro
        if (getForIdSequence().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SEQUENCE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSequence());
        }

        // traitement du positionnement depuis un num�ro de s�quence contentieux
        if (getForIdSequenceContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSEQCON=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSequenceContentieux());
        }

        // traitement du positionnement jusqu'� un num�ro de s�quence (no ordre)
        if (getBeforeNoSequence().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SEQUENCE<" + this._dbWriteNumeric(statement.getTransaction(), getBeforeNoSequence());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAParametreEtape();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 10:08:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getBeforeNoSequence() {
        return beforeNoSequence;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (23.05.2002 10:02:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 10:12:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSequenceContentieux() {
        return forIdSequenceContentieux;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 10:33:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderBy() {
        return orderBy;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 10:08:20)
     * 
     * @param newBeforeNoSequence
     *            java.lang.String
     */
    public void setBeforeNoSequence(java.lang.String newBeforeNoSequence) {
        beforeNoSequence = newBeforeNoSequence;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (23.05.2002 10:02:31)
     * 
     * @param newForIdSequenceContentieux
     *            java.lang.String
     */
    public void setForIdSequence(java.lang.String newForIdSequence) {
        forIdSequence = newForIdSequence;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 10:12:13)
     * 
     * @param newForIdSequenceContentieux
     *            java.lang.String
     */
    public void setForIdSequenceContentieux(java.lang.String newForIdSequenceContentieux) {
        forIdSequenceContentieux = newForIdSequenceContentieux;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.06.2002 10:33:50)
     * 
     * @param newOrderBy
     *            java.lang.String
     */
    public void setOrderBy(java.lang.String newOrderBy) {
        orderBy = newOrderBy;
    }
}
