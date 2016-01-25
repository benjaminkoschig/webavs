package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class REFicheAugmentation extends BEntity {

    // ~ Static fields/Initializers
    // -----------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_AUGMENTATION = "WJDAUG";
    public static final String FIELDNAME_ID_ANNONCE_HEADER = "WJIANH";
    public static final String FIELDNAME_ID_FICHE_AUGMENTATION = "WJIFIA";
    public static final String FIELDNAME_ID_PRESTATION_ACCORDEE = "WJIPRA";
    public static final String TABLE_NAME_FICHE_AUGMENTATION = "REFICHA";

    // ~ Instance fields
    // -------------------------------------------------------------------------------------------------------------------

    private String dateAugmentation = "";
    private String idAnnonceHeader = "";
    private String idFicheAugmentation = "";
    private String idPrestationAccordee = "";

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idFicheAugmentation = _incCounter(transaction, idFicheAugmentation, TABLE_NAME_FICHE_AUGMENTATION);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + REFicheAugmentation.TABLE_NAME_FICHE_AUGMENTATION;
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME_FICHE_AUGMENTATION;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idFicheAugmentation = statement.dbReadNumeric(FIELDNAME_ID_FICHE_AUGMENTATION);
        idAnnonceHeader = statement.dbReadNumeric(FIELDNAME_ID_ANNONCE_HEADER);
        idPrestationAccordee = statement.dbReadNumeric(FIELDNAME_ID_PRESTATION_ACCORDEE);
        dateAugmentation = statement.dbReadString(FIELDNAME_DATE_AUGMENTATION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_FICHE_AUGMENTATION,
                _dbWriteNumeric(statement.getTransaction(), idFicheAugmentation, "idFicheAugmentation"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_FICHE_AUGMENTATION,
                _dbWriteNumeric(statement.getTransaction(), idFicheAugmentation, "idFicheAugmentation"));
        statement.writeField(FIELDNAME_ID_ANNONCE_HEADER,
                _dbWriteNumeric(statement.getTransaction(), idAnnonceHeader, "idAnnonceHeader"));
        statement.writeField(FIELDNAME_ID_PRESTATION_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
        statement.writeField(FIELDNAME_DATE_AUGMENTATION,
                _dbWriteString(statement.getTransaction(), dateAugmentation, "dateAugmentation"));
    }

    public String getDateAugmentation() {
        return dateAugmentation;
    }

    // ~ Setters and Getters
    // ----------------------------------------------------------------------------------------------------------------

    public String getIdAnnonceHeader() {
        return idAnnonceHeader;
    }

    public String getIdFicheAugmentation() {
        return idFicheAugmentation;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setDateAugmentation(String newDateAugmentation) {
        dateAugmentation = newDateAugmentation;
    }

    public void setIdAnnonceHeader(String newIdAnnonceHeader) {
        idAnnonceHeader = newIdAnnonceHeader;
    }

    public void setIdFicheAugmentation(String newIdFicheAugmentation) {
        idFicheAugmentation = newIdFicheAugmentation;
    }

    public void setIdPrestationAccordee(String newIdPrestationAccordee) {
        idPrestationAccordee = newIdPrestationAccordee;
    }

}
