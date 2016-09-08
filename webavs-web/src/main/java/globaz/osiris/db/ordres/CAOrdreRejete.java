package globaz.osiris.db.ordres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

// TODO en faire un BEntity et l'intégrer dans JADE......
public class CAOrdreRejete extends BEntity {

    public static final String FIELD_ID = "IDORER";
    public static final String FIELD_IDORDRE = "IDORDR";
    public static final String FIELD_CODE = "CODE";
    public static final String FIELD_PROPRIETARY = "PROPRI";
    public static final String FIELD_ADDITIONNAL_INFO = "ADDITI";

    public static final String TABLE_NAME = "CAORREJ";

    private String idOrdreRejete;

    private String idOrdre;
    // balise <Cd>
    private String code;
    // balise <Prtry>
    private String proprietary;
    // balise <AddtlInf>
    private String additionalInformations;

    public String getIdOrdre() {
        return idOrdre;
    }

    public void setIdOrdre(String idOrdre) {
        this.idOrdre = idOrdre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProprietary() {
        return proprietary;
    }

    public void setProprietary(String proprietary) {
        this.proprietary = proprietary;
    }

    public String getAdditionalInformations() {
        return additionalInformations;
    }

    public void setAdditionalInformations(String additionalInformations) {
        this.additionalInformations = additionalInformations;
    }

    public String getIdOrdreRejete() {
        return idOrdreRejete;
    }

    public void setIdOrdreRejete(String idOrdreRejete) {
        this.idOrdreRejete = idOrdreRejete;
    }

    @Override
    protected String _getTableName() {
        return CAOrdreRejete.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        setIdOrdreRejete(statement.dbReadNumeric(CAOrdreRejete.FIELD_ID));
        idOrdre = statement.dbReadNumeric(CAOrdreRejete.FIELD_IDORDRE);
        code = statement.dbReadString(CAOrdreRejete.FIELD_CODE);
        proprietary = statement.dbReadString(CAOrdreRejete.FIELD_PROPRIETARY);
        additionalInformations = statement.dbReadString(CAOrdreRejete.FIELD_ADDITIONNAL_INFO);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAOrdreRejete.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreRejete(), ""));
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdOrdreRejete(this._incCounter(transaction, idOrdreRejete));

        // type d'ordre groupé = versement par défaut
        // sch--- setTypeOrdreGroupe(this.VERSEMENT);
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAOrdreRejete.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreRejete(), "idOrdreRejete"));
        statement.writeField(CAOrdreRejete.FIELD_IDORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdre(), "idOrdreGroupe"));
        statement.writeField(CAOrdreRejete.FIELD_CODE,
                this._dbWriteString(statement.getTransaction(), getCode(), "Code err"));
        statement.writeField(CAOrdreRejete.FIELD_PROPRIETARY,
                this._dbWriteString(statement.getTransaction(), getProprietary(), "proprietary"));
        statement
                .writeField(CAOrdreRejete.FIELD_ADDITIONNAL_INFO, this._dbWriteString(statement.getTransaction(),
                        getAdditionalInformations(), "Additional Informations"));
    }

}
