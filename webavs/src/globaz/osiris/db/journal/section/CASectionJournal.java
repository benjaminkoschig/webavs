package globaz.osiris.db.journal.section;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

public class CASectionJournal extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categorieSection;
    private String idJournal;
    private String idSection;

    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        setIdJournal(statement.dbReadNumeric(CAOperation.FIELD_IDJOURNAL));

        setCategorieSection(statement.dbReadNumeric(CASection.FIELD_CATEGORIESECTION));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not used.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not used.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not used.
    }

    public String getCategorieSection() {
        return categorieSection;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setCategorieSection(String categorieSection) {
        this.categorieSection = categorieSection;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

}
