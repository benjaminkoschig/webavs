package globaz.helios.db.comptes;

import globaz.helios.translation.CodeSystem;

public class CGExtendedBalMvtJournalManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdExerciceComptable = new String();

    /**
     * Commentaire relatif au constructeur CGCompteOfasManager.
     */
    public CGExtendedBalMvtJournalManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String sqlFrom = "";

        sqlFrom += _getCollection() + "CGJOURP";

        sqlFrom += " INNER JOIN  " + _getCollection() + "CGPERIP ON  " + _getCollection()
                + "CGPERIP.idPeriodeComptable =  " + _getCollection() + "CGJOURP.idPeriodeComptable";
        sqlFrom += " INNER JOIN  " + _getCollection() + "CGEXERP ON  " + _getCollection()
                + "CGEXERP.idExerComptable =  " + _getCollection() + "CGPERIP.idExerComptable";
        sqlFrom += " AND  " + _getCollection() + "CGEXERP.idExerComptable =  " + _getCollection()
                + "CGJOURP.idExerComptable";
        sqlFrom += " LEFT OUTER JOIN  " + _getCollection() + "CGECREP ON  " + _getCollection()
                + "CGECREP.idJournal =  " + _getCollection() + "CGJOURP.idJournal";
        sqlFrom += " LEFT OUTER JOIN  " + _getCollection() + "CGECRIP ON  " + _getCollection()
                + "CGECRIP.idEnteteEcriture =  " + _getCollection() + "CGECREP.idEnteteEcriture";

        return sqlFrom;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGJOURP.numero";

    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // composant de la requete initialises avec les options par defaut

        String sqlWhere = _getCollection() + "CGPERIP.code <> '" + CGPeriodeComptable.CS_CODE_CLOTURE + "'";

        sqlWhere += " AND (" + _getCollection() + "CGECRIP.ESTPROVISOIRE <> '1' OR " + _getCollection()
                + "CGECRIP.ESTPROVISOIRE is NULL)";

        sqlWhere += " AND " + _getCollection() + "CGECRIP.ESTACTIVE = '1' ";

        sqlWhere += " AND (" + _getCollection() + "CGECRIP.codedebitcredit = " + CodeSystem.CS_DEBIT + " OR "
                + _getCollection() + "CGECRIP.codedebitcredit = " + CodeSystem.CS_EXTOURNE_DEBIT + " OR "
                + _getCollection() + "CGECRIP.codedebitcredit is NULL)";

        if (getForIdExerciceComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "CGEXERP.IDEXERCOMPTABLE ="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        sqlWhere += " GROUP BY  " + _getCollection() + "CGJOURP.numero, " + _getCollection() + "CGJOURP.libelle,  "
                + _getCollection() + "CGJOURP.date,  " + _getCollection() + "CGJOURP.dateValeur,  " + _getCollection()
                + "CGJOURP.idEtat,  " + _getCollection() + "CGECREP.idJournal,  " + _getCollection()
                + "CGJOURP.idJournal";
        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExtendedBalMvtJournal();
    }

    /**
     * Returns the forIdExerciceComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * Sets the forIdExerciceComptable.
     * 
     * @param forIdExerciceComptable
     *            The forIdExerciceComptable to set
     */
    public void setForIdExerciceComptable(java.lang.String forIdExerciceComptable) {
        this.forIdExerciceComptable = forIdExerciceComptable;
    }

}
