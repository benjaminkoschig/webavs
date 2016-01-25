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
     * Cr�e une nouvelle instance de la classe LIEcheancesMultipleManager.
     */
    public LIEcheancesMultipleManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Red�finition de la m�thode _getWhere du parent afin de g�n�rer le WHERE de la requ�te en fonction des besoins
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
     * D�finition de l'entit� (LIEcheancesMultiple)
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
