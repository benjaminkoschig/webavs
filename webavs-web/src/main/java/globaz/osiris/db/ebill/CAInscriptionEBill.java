package globaz.osiris.db.ebill;

import ch.globaz.common.document.reference.AbstractReference;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ebill.enums.CAInscriptionTypeEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.process.ebill.CAInscriptionEBillEnum;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class CAInscriptionEBill extends BEntity implements Serializable {

    public static final String TABLE_INSCRIPTION_EBILL = "CAEI";

    public static final String FIELD_ID_INSCRIPTION = "ID_INSCRIPTION";
    public static final String FIELD_ID_FICHIER = "ID_FICHIER";
    public static final String FIELD_STATUT = "STATUT";
    private static final String FIELD_TEXTE_ERREUR_INTERNE = "TEXTE_ERREUR_INTERNE";
    private static final String FIELD_CONTACT = "CONTACT";
    private static final String FIELD_ADRESSE_2 = "ADRESSE_2";

    private String idInscription;
    private String idFichier;
    private String statut;
    private String texteErreurInterne;

    private String eBillAccountID;
    private String eBillAccountType;
    private String numeroAffilie;
    private String type;
    private String nom;
    private String prenom;
    private String entreprise;
    private String contact;
    private boolean roleParitaire;
    private boolean rolePersonnel;
    private String adresse1;
    private String adresse2;
    private Integer npa;
    private String localite;
    private String pays;
    private String email;
    private String numTel;
    private String numAdherentBVR;
    private String numRefBVR;

    private String billerId;

    @Override
    protected String _getTableName() {
        return TABLE_INSCRIPTION_EBILL;
    }

    @Override
    protected void _readProperties(BStatement bStatement) throws Exception {
        idInscription = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_ID_INSCRIPTION);
        idFichier = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_ID_FICHIER);
        contact = bStatement.dbReadString(CAInscriptionEBill.FIELD_CONTACT);
        adresse2 = bStatement.dbReadString(CAInscriptionEBill.FIELD_ADRESSE_2);
        statut = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_STATUT);
        texteErreurInterne = bStatement.dbReadString(CAInscriptionEBill.FIELD_TEXTE_ERREUR_INTERNE);

        eBillAccountID = bStatement.dbReadString(CAInscriptionEBillEnum.RECIPIENT_ID.getColNameSql());
        eBillAccountType = bStatement.dbReadString(CAInscriptionEBillEnum.RECIPIENT_TYPE.getColNameSql());
        numeroAffilie = bStatement.dbReadString(CAInscriptionEBillEnum.CUSTOMER_NBR.getColNameSql());
        type = bStatement.dbReadNumeric(CAInscriptionEBillEnum.SUBSCRIPTION_TYPE.getColNameSql());
        nom = bStatement.dbReadString(CAInscriptionEBillEnum.FAMILY_NAME.getColNameSql());
        prenom = bStatement.dbReadString(CAInscriptionEBillEnum.GIVEN_NAME.getColNameSql());
        entreprise = bStatement.dbReadString(CAInscriptionEBillEnum.COMPANY_NAME.getColNameSql());
        roleParitaire = bStatement.dbReadBoolean(CAInscriptionEBillEnum.PARITAIRE.getColNameSql());
        rolePersonnel = bStatement.dbReadBoolean(CAInscriptionEBillEnum.PERSONNEL.getColNameSql());
        adresse1 = bStatement.dbReadString(CAInscriptionEBillEnum.ADRESSE.getColNameSql());
        npa = Integer.parseInt(bStatement.dbReadNumeric(CAInscriptionEBillEnum.ZIP.getColNameSql()));
        localite = bStatement.dbReadString(CAInscriptionEBillEnum.CITY.getColNameSql());
        pays = bStatement.dbReadString(CAInscriptionEBillEnum.COUNTRY.getColNameSql());
        email = bStatement.dbReadString(CAInscriptionEBillEnum.EMAIL.getColNameSql());
        numTel = bStatement.dbReadString(bStatement.dbReadString(CAInscriptionEBillEnum.PHONE.getColNameSql()));
        numAdherentBVR = bStatement.dbReadString(bStatement.dbReadString(CAInscriptionEBillEnum.CREDIT_ACCOUNT.getColNameSql()));
        numRefBVR = bStatement.dbReadString(CAInscriptionEBillEnum.CREDITOR_REFERENCE.getColNameSql());
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {
            // no implementation needed : pas de contrôle avant mise en bdd.
    }

    @Override
    protected void _writePrimaryKey(BStatement bStatement) throws Exception {
        bStatement.writeKey(CAInscriptionEBill.FIELD_ID_INSCRIPTION,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdInscription(), ""));
    }

    @Override
    protected void _writeProperties(BStatement bStatement) throws Exception {
        bStatement.writeField(CAInscriptionEBill.FIELD_ID_INSCRIPTION,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdInscription(), "idInscription"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ID_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdFichier(), "idFichier"));
        bStatement.writeField(CAInscriptionEBill.FIELD_CONTACT,
                this._dbWriteString(bStatement.getTransaction(), getContact(), "contact"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ADRESSE_2,
                this._dbWriteString(bStatement.getTransaction(), getAdresse2(), "adresse2"));
        bStatement.writeField(CAInscriptionEBill.FIELD_STATUT,
                this._dbWriteNumeric(bStatement.getTransaction(), statut, "statut"));
        bStatement.writeField(CAInscriptionEBill.FIELD_TEXTE_ERREUR_INTERNE,
                this._dbWriteString(bStatement.getTransaction(), getTexteErreurInterne(), "texteErreurInterne"));

        bStatement.writeField(CAInscriptionEBillEnum.RECIPIENT_ID.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), geteBillAccountID(), "eBillAccountID"));
        bStatement.writeField(CAInscriptionEBillEnum.CUSTOMER_NBR.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getNumeroAffilie(), "numeroAffilie"));
        bStatement.writeField(CAInscriptionEBillEnum.SUBSCRIPTION_TYPE.getColNameSql(),
                this._dbWriteNumeric(bStatement.getTransaction(), type, "type"));
        bStatement.writeField(CAInscriptionEBillEnum.FAMILY_NAME.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getNom(), "nom"));
        bStatement.writeField(CAInscriptionEBillEnum.GIVEN_NAME.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getPrenom(), "prenom"));
        bStatement.writeField(CAInscriptionEBillEnum.COMPANY_NAME.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getEntreprise(), "entreprise"));
        bStatement.writeField(CAInscriptionEBillEnum.PARITAIRE.getColNameSql(),
                this._dbWriteBoolean(bStatement.getTransaction(), getRoleParitaire(), "roleParitaire"));
        bStatement.writeField(CAInscriptionEBillEnum.PERSONNEL.getColNameSql(),
                this._dbWriteBoolean(bStatement.getTransaction(), getRolePersonnel(), "rolePersonnel"));
        bStatement.writeField(CAInscriptionEBillEnum.ADRESSE.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getAdresse1(), "adresse1"));
        bStatement.writeField(CAInscriptionEBillEnum.ZIP.getColNameSql(),
                this._dbWriteNumeric(bStatement.getTransaction(), getNpa().toString(), "npa"));
        bStatement.writeField(CAInscriptionEBillEnum.CITY.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getLocalite(), "localite"));
        bStatement.writeField(CAInscriptionEBillEnum.EMAIL.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getEmail(), "email"));
        bStatement.writeField(CAInscriptionEBillEnum.PHONE.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getNumTel(), "numTel"));
        bStatement.writeField(CAInscriptionEBillEnum.CREDIT_ACCOUNT.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getNumAdherentBVR(), "numAdherentBVR"));
        bStatement.writeField(CAInscriptionEBillEnum.CREDITOR_REFERENCE.getColNameSql(),
                this._dbWriteString(bStatement.getTransaction(), getNumRefBVR(), "numRefBVR"));

    }

    /*
     * (non-Javadoc)
     *
     * @seeglobaz.osiris.db.comptes.CAOperation#_beforeAdd(globaz.globall.db. BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdInscription(this._incCounter(transaction, idInscription));
        super._beforeAdd(transaction);
    }

    public String getIdFichier() {
        return idFichier;
    }

    public void setIdFichier(String idFichier) {
        this.idFichier = idFichier;
    }

    public String geteBillAccountID() {
        return eBillAccountID;
    }

    public void seteBillAccountID(String eBillAccountID) {
        this.eBillAccountID = eBillAccountID;
    }

    public String getChampNumeroAffilie() {
        if (numeroAffilie.isEmpty()) {
            String numero = StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(numRefBVR) && !StringUtils.equals(numRefBVR.substring(0, 2), AbstractReference.IDENTIFIANT_REF_IDCOMPTEANNEXE)) {
                try {
                    CAApplication application = (CAApplication) GlobazServer.getCurrentSystem().getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
                    IFormatData affilieFormater = application.getAffileFormater();
                    return affilieFormater.format(Long.toString(new Long(numRefBVR.substring(3, 15))));
                } catch (Exception e) {
                    return numero;
                }
            } else {
                return numero;
            }
        } else {
            return numeroAffilie;
        }
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public CAInscriptionTypeEBillEnum getType() {
        return CAInscriptionTypeEBillEnum.parValeur(type);
    }

    public String getLibelleType() {
        return getSession().getLabel(getType().getDescription());
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean getRoleParitaire() {
        return roleParitaire;
    }

    public void setRoleParitaire(boolean roleParitaire) {
        this.roleParitaire = roleParitaire;
    }

    public boolean getRolePersonnel() {
        return rolePersonnel;
    }

    public void setRolePersonnel(boolean rolePersonnel) {
        this.rolePersonnel = rolePersonnel;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public CAStatutEBillEnum getStatut() {
        return CAStatutEBillEnum.parValeur(statut);
    }

    public String getLibelleStatut() {
        return getSession().getLabel(getStatut().getDescription());
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Integer getNpa() {
        return npa;
    }

    public void setNpa(Integer npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getTexteErreurInterne() {
        return texteErreurInterne;
    }

    public void setTexteErreurInterne(String texteErreurInterne) {
        this.texteErreurInterne = texteErreurInterne;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getNumAdherentBVR() {
        return numAdherentBVR;
    }

    public void setNumAdherentBVR(String numAdherentBVR) {
        this.numAdherentBVR = numAdherentBVR;
    }

    public String getNumRefBVR() {
        return numRefBVR;
    }

    public void setNumRefBVR(String numRefBVR) {
        this.numRefBVR = numRefBVR;
    }

    public String getIdInscription() {
        return idInscription;
    }

    public void setIdInscription(String idInscription) {
        this.idInscription = idInscription;
    }

    public String geteBillAccountType() {
        return eBillAccountType;
    }

    public void seteBillAccountType(String eBillAccountType) {
        this.eBillAccountType = eBillAccountType;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }
}
