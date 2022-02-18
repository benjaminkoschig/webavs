package ch.globaz.eform.business.models;

import ch.globaz.common.util.Dates;
import ch.globaz.eavs.utils.StringUtils;
import globaz.common.util.CommonBlobUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeUUIDGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Getter
@Setter
public class GFFormulaireModel extends BEntity {

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_FORMULAIRE_ID = "FORMULAIRE_ID";
    public static final String FIELDNAME_FORMULAIRE_SUBJECT = "FORMULAIRE_SUBJECT";
    public static final String FIELDNAME_FORMULAIRE_DATE = "FORMULAIRE_DATE";
    public static final String FIELDNAME_FORMULAIRE_TYPE = "FORMULAIRE_TYPE";
    public static final String FIELDNAME_FORMULAIRE_NOM = "FORMULAIRE_NOM";
    public static final String FIELDNAME_BENEFICIAIRE_NSS = "BENEFICIAIRE_NSS";
    public static final String FIELDNAME_BENEFICIAIRE_NOM = "BENEFICIAIRE_NOM";
    public static final String FIELDNAME_BENEFICIAIRE_PRENOM = "BENEFICIAIRE_PRENOM";
    public static final String FIELDNAME_BENEFICIAIRE_DATE_NAISSANCE = "BENEFICIAIRE_DATE_NAISSANCE";
    public static final String FIELDNAME_USER_GESTIONNAIRE = "USER_GESTIONNAIRE";
    public static final String FIELDNAME_ATTACHEMENT_ID = "ATTACHEMENT_ID";
    public static final String TABLE_NAME_GF_FORMULAIRE = "GF_FORMULAIRE";

    private String formulaireId;
    private String typeFormulaire;
    private String nomFormulaire;
    private String formulaireSubject;
    private LocalDate formulaireDate;
    private String nssBeneficiaire;
    private String nomBeneficiaire;
    private String prenomBeneficiaire;
    private LocalDate dateNaissanceBeneficiaire;
    private String userGestionnaire;
    private String attachementId;

    public GFFormulaireModel(BSession session) {
        setSession(session);
    }

    public void addAttachement(String location) throws Exception {
        if (StringUtils.isBlank(location)) throw new IllegalArgumentException();

        this.attachementId = generateAttachementId();
        byte[] bytes = CommonBlobUtils.fileToByteArray(location);
        CommonBlobUtils.addBlob(this.attachementId, bytes, getSession().getCurrentThreadTransaction());
    }

    public File getAttachement(BTransaction transaction) throws Exception {
        if (Objects.isNull(this.attachementId)) {
            return null;
        }
        return (File) CommonBlobUtils.readBlob(this.attachementId, transaction);
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME_GF_FORMULAIRE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        formulaireId = statement.dbReadString(FIELDNAME_FORMULAIRE_ID);
        formulaireSubject = statement.dbReadString(FIELDNAME_FORMULAIRE_SUBJECT);
        formulaireDate = Dates.toDateFromDb(statement.dbReadString(FIELDNAME_FORMULAIRE_DATE));
        typeFormulaire = statement.dbReadString((FIELDNAME_FORMULAIRE_TYPE));
        nomFormulaire = statement.dbReadString(FIELDNAME_FORMULAIRE_NOM);
        nssBeneficiaire = statement.dbReadString(FIELDNAME_BENEFICIAIRE_NSS);
        nomBeneficiaire = statement.dbReadString(FIELDNAME_BENEFICIAIRE_NOM);
        prenomBeneficiaire = statement.dbReadString(FIELDNAME_BENEFICIAIRE_PRENOM);
        dateNaissanceBeneficiaire = Dates.toDate(FIELDNAME_BENEFICIAIRE_DATE_NAISSANCE);
        userGestionnaire = statement.dbReadString(FIELDNAME_USER_GESTIONNAIRE);
        attachementId = statement.dbReadString(FIELDNAME_ATTACHEMENT_ID);
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(GFFormulaireModel.FIELDNAME_FORMULAIRE_ID,
                this._dbWriteString(statement.getTransaction(), formulaireId, "formulaireId"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_FORMULAIRE_ID,
                this._dbWriteString(statement.getTransaction(), formulaireId, ""));
        statement.writeField(FIELDNAME_FORMULAIRE_SUBJECT,
                this._dbWriteString(statement.getTransaction(), formulaireSubject, ""));
        statement.writeField(FIELDNAME_FORMULAIRE_DATE,
                this._dbWriteString(statement.getTransaction(), Dates.toDbDate(formulaireDate), ""));
        statement.writeField(FIELDNAME_FORMULAIRE_TYPE,
                this._dbWriteString(statement.getTransaction(), typeFormulaire, ""));
        statement.writeField(FIELDNAME_FORMULAIRE_NOM,
                this._dbWriteString(statement.getTransaction(), nomFormulaire, ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_NOM,
                this._dbWriteString(statement.getTransaction(), nomBeneficiaire, ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_PRENOM,
                this._dbWriteString(statement.getTransaction(), prenomBeneficiaire, ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_DATE_NAISSANCE,
                this._dbWriteString(statement.getTransaction(), Dates.toDbDate(dateNaissanceBeneficiaire), ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_NSS,
                this._dbWriteString(statement.getTransaction(), nssBeneficiaire, ""));
        statement.writeField(FIELDNAME_USER_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), userGestionnaire, ""));
        statement.writeField(FIELDNAME_ATTACHEMENT_ID,
                this._dbWriteString(statement.getTransaction(), attachementId, ""));
    }


    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {

    }

    private String generateAttachementId() {
        return this.getClass().getName() + "_" + JadeUUIDGenerator.createStringUUID();
    }
}
