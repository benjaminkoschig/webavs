package globaz.libra.db.journalisations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LIEcheancesMultipleManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdJournalisation = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIEcheancesMultipleManager.
     */
    public LIEcheancesMultipleManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdJournalisation)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LIEcheancesMultiple.FIELDNAME_ID_JOURNALISATION);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdJournalisation));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (LIEcheancesMultiple)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIEcheancesMultiple();
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForIdJournalisation() {
        return forIdJournalisation;
    }

    public void setForIdJournalisation(String forIdJournalisation) {
        this.forIdJournalisation = forIdJournalisation;
    }

}
