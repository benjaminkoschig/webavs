package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.io.Serializable;

public class AFIdeListErrorAnnonce extends BEntity implements Serializable {

    private static final long serialVersionUID = 1;

    public static final String IDE_ANNONCE_TABLE_NAME = "AFANOIDE";

    public static final String IDE_ANNONCE_FIELD_ETAT = "AIDEET";
    public static final String IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS = "AIDEMB";
    public static final String IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL = "AIDEMT";

    private String messageErreurForBusinessUser = "";
    private String messageErreurForTechnicalUser = "";

    public String getMessageErreurForBusinessUser() {
        return messageErreurForBusinessUser;
    }

    public void setMessageErreurForBusinessUser(String messageErreurForBusinessUser) {
        this.messageErreurForBusinessUser = messageErreurForBusinessUser;
    }

    public String getMessageErreurForTechnicalUser() {
        return messageErreurForTechnicalUser;
    }

    public void setMessageErreurForTechnicalUser(String messageErreurForTechnicalUser) {
        this.messageErreurForTechnicalUser = messageErreurForTechnicalUser;
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "DISTINCT " + IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS + ","
                + IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sqlFrom = new StringBuilder();

        sqlFrom.append(_getCollection() + AFIdeListErrorAnnonce.IDE_ANNONCE_TABLE_NAME);
        return sqlFrom.toString();

    }

    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {

    }

    @Override
    protected String _getTableName() {
        return AFIdeListErrorAnnonce.IDE_ANNONCE_TABLE_NAME;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        messageErreurForBusinessUser = statement
                .dbReadString(AFIdeListErrorAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS);
        messageErreurForTechnicalUser = statement
                .dbReadString(AFIdeListErrorAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL);

    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {

        statement.writeField(AFIdeListErrorAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_BUSINESS, this._dbWriteString(
                statement.getTransaction(), messageErreurForBusinessUser, "messageErreurForBusinessUser"));
        statement.writeField(AFIdeListErrorAnnonce.IDE_ANNONCE_FIELD_MESSAGE_ERREUR_TECHNICAL, this._dbWriteString(
                statement.getTransaction(), messageErreurForTechnicalUser, "messageErreurForTechnicalUser"));

    }

}
