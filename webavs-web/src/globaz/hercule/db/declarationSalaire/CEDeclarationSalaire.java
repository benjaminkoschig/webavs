package globaz.hercule.db.declarationSalaire;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 25 juin 2010
 */
public class CEDeclarationSalaire extends BEntity {

    private static final long serialVersionUID = -8575091918066979607L;
    public static final String FIELD_DATE_CREATION = "JJOUDA";
    public static final String FIELD_DATE_RECEPTION = "JGJORE";
    public static final String FIELD_NUM_AFFILIE = "JJOULI";

    private String dateCreation = "";
    private String dateReception = "";
    private String numAffilie = "";

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateReception = statement.dbReadDateAMJ(FIELD_DATE_RECEPTION);
        dateCreation = statement.dbReadDateAMJ(FIELD_DATE_CREATION);
        numAffilie = statement.dbReadString(FIELD_NUM_AFFILIE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

}
