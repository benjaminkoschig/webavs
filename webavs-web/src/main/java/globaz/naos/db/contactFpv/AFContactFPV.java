package globaz.naos.db.contactFpv;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class AFContactFPV extends BEntity {

    private static final Logger logger = LoggerFactory.getLogger(AFAffiliation.class);

    public static final String TABLE_NAME = "AF_CONTACT_FPV";
    public static final String COLUMN_CONTACT_ID = "KEY";
    public static final String COLUMN_NUMERO_AFF = "AFFNUM";
    public static final String COLUMN_NOM = "NOM";
    public static final String COLUMN_PRENOM = "PRENOM";
    public static final String COLUMN_SEXE = "SEXE";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PROSPECTION = "PROSPECTION";

    private String contactId;

    private String affiliationNumero;

    private String nom;

    private String prenom;

    private String sexe;

    private String email;

    private boolean stopProspection;

    Pattern nomPattern = Pattern.compile("\\p{L}*(-\\p{L}*)*");

    Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement bStatement) throws Exception {
        contactId = bStatement.dbReadNumeric(COLUMN_CONTACT_ID);
        affiliationNumero = bStatement.dbReadString(COLUMN_NUMERO_AFF);
        nom = bStatement.dbReadString(COLUMN_NOM);
        prenom = bStatement.dbReadString(COLUMN_PRENOM);
        sexe = bStatement.dbReadString(COLUMN_SEXE);
        email = bStatement.dbReadString(COLUMN_EMAIL);
        stopProspection = bStatement.dbReadBoolean(COLUMN_PROSPECTION);
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {
        if (JadeStringUtil.isEmpty(getNom())) {
            _addError(bStatement.getTransaction(),"Le nom doit être renseigné.");
        } else if (!nomPattern.matcher(getNom()).matches()) {
            _addError(bStatement.getTransaction(),"Le format du nom renseigné n'est pas correct.");
        }

        if (JadeStringUtil.isEmpty(getPrenom())) {
            _addError(bStatement.getTransaction(),"Le prénom doit être renseigné.");
        } else if (!nomPattern.matcher(getPrenom()).matches()) {
            _addError(bStatement.getTransaction(),"Le format du prénom renseigné n'est pas correct.");
        }

        if (JadeStringUtil.isEmpty(getSexe())) {
            _addError(bStatement.getTransaction(),"Le sexe doit être renseigné.");
        }

        if (JadeStringUtil.isEmpty(getEmail())) {
            _addError(bStatement.getTransaction(),"L'e-mail doit être renseigné.");
        } else if (!emailPattern.matcher(getEmail()).matches()) {
            _addError(bStatement.getTransaction(),"Le format de l'adresse e-mail renseigné n'est pas correct.");
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement bStatement) throws Exception {
        bStatement.writeKey(COLUMN_CONTACT_ID, this._dbWriteNumeric(bStatement.getTransaction(), getContactId(), StringUtils.EMPTY));

    }

    @Override
    protected void _writeProperties(BStatement bStatement) throws Exception {
        bStatement.writeField(COLUMN_CONTACT_ID,
                this._dbWriteNumeric(bStatement.getTransaction(), getContactId(), "contactId"));
        bStatement.writeField(COLUMN_NUMERO_AFF, this._dbWriteString(bStatement.getTransaction(), getAffiliationNumero(), "affiliationNumero"));
        bStatement.writeField(COLUMN_NOM, this._dbWriteString(bStatement.getTransaction(), getNom(), "nom"));
        bStatement.writeField(COLUMN_PRENOM, this._dbWriteString(bStatement.getTransaction(), getPrenom(), "prenom"));
        bStatement.writeField(COLUMN_SEXE, this._dbWriteString(bStatement.getTransaction(), getSexe(), "sexe"));
        bStatement.writeField(COLUMN_EMAIL, this._dbWriteString(bStatement.getTransaction(), getEmail(), "email"));
        bStatement.writeField(COLUMN_PROSPECTION, this._dbWriteBoolean(bStatement.getTransaction(), isStopProspection(), "stopProspection"));
    }

    @Override
    public boolean hasCreationSpy(){
        return true;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getAffiliationNumero() {
        return affiliationNumero;
    }

    public void setAffiliationNumero(String affiliationNumero) {
        this.affiliationNumero = affiliationNumero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStopProspection() {
        return stopProspection;
    }

    public void setStopProspection(boolean stopProspection) {
        this.stopProspection = stopProspection;
    }
}
