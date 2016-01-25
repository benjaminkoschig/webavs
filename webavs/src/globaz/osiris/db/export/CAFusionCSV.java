package globaz.osiris.db.export;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class CAFusionCSV extends BEntity {

    public static final String FIELD_DATA = "DATA";
    public static final String FIELD_DATE = "DATEDOC";
    public static final String FIELD_FUSIONID = "FUSIONID";
    public static final String FIELD_NOSERIE = "NOSERIE";
    public static final String FIELD_SOURCE = "SOURCE";
    public static String SEPARATOR = ";";
    private static final long serialVersionUID = -2721987840377213278L;
    public static final String TABLENAME = "CACCSVP";
    private String data = new String();
    private String date = new String();
    //
    private String fusionId = new String();
    private String noSerie = new String();

    private String source = new String();

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        // incrémente le prochain numéro
        setFusionId(this._incCounter(transaction, fusionId));
    }

    @Override
    protected String _getTableName() {
        return CAFusionCSV.TABLENAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        fusionId = statement.dbReadNumeric(CAFusionCSV.FIELD_FUSIONID);
        date = statement.dbReadDateAMJ(CAFusionCSV.FIELD_DATE);
        noSerie = statement.dbReadString(CAFusionCSV.FIELD_NOSERIE);
        source = statement.dbReadString(CAFusionCSV.FIELD_SOURCE);
        data = statement.dbReadString(CAFusionCSV.FIELD_DATA);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getFusionId(), getSession().getLabel(""));
        _propertyMandatory(statement.getTransaction(), getDate(), getSession().getLabel(""));
        _propertyMandatory(statement.getTransaction(), getNoSerie(), getSession().getLabel(""));
        _propertyMandatory(statement.getTransaction(), getSource(), getSession().getLabel(""));
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAFusionCSV.FIELD_FUSIONID,
                this._dbWriteNumeric(statement.getTransaction(), getFusionId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAFusionCSV.FIELD_FUSIONID,
                this._dbWriteNumeric(statement.getTransaction(), getFusionId(), "fusionId"));
        statement.writeField(CAFusionCSV.FIELD_DATE,
                this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField(CAFusionCSV.FIELD_NOSERIE,
                this._dbWriteString(statement.getTransaction(), getNoSerie(), "noSerie"));
        statement.writeField(CAFusionCSV.FIELD_SOURCE,
                this._dbWriteString(statement.getTransaction(), getSource(), "source"));
        statement
                .writeField(CAFusionCSV.FIELD_DATA, this._dbWriteString(statement.getTransaction(), getData(), "data"));
    }

    /**
     * Ajouter une colonne à la table
     * 
     * @param value
     *            la valeur de la colonne
     */
    public void addColumn(String value) {
        if (!JadeStringUtil.isBlank(value)) {
            if (JadeStringUtil.isBlank(data)) {
                data = value;
            } else {
                data = data + CAFusionCSV.SEPARATOR + value;
            }
        } else {
            data = data + CAFusionCSV.SEPARATOR;
        }
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    public String getFusionId() {
        return fusionId;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public String getSource() {
        return source;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFusionId(String fusionId) {
        this.fusionId = fusionId;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
