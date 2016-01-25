/**
 * 
 */
package globaz.osiris.db.contentieux;

import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SEL
 * 
 */
public class CADateExecutionSommationAquila extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateExecution = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateExecution = statement.dbReadDateAMJ(ICOHistoriqueConstante.FNAME_DATE_EXECUTION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return the dateExecution
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @param dateExecution
     *            the dateExecution to set
     */
    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

}
