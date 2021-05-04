package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ebill.enums.CAInscriptionTypeEBillEnum;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;

import java.io.Serializable;

public class CAInscriptionEBill extends BEntity implements Serializable {

    public static final String TABLE_INSCRIPTION_EBILL = "EBILL_INSCRIPTION";
    public static final String FIELD_ID_INSCRIPTION = "ID_INSCRIPTION";
    public static final String FIELD_ID_FICHIER = "ID_FICHIER";
    public static final String FIELD_EBILL_ACCOUNT_ID = "EBILL_ACCOUNT_ID";
    public static final String FIELD_NUMERO_AFFILIE = "NUMERO_AFFILIE";
    public static final String FIELD_TYPE = "TYPE";
    public static final String FIELD_STATUT = "STATUT";

    private static final String FIELD_NOM = "NOM";
    private static final String FIELD_PRENOM = "PRENOM";
    private static final String FIELD_ENTREPRISE = "ENTREPRISE";
    private static final String FIELD_CONTACT = "CONTACT";
    private static final String FIELD_ROLE_PARITAIRE = "ROLE_PARITAIRE";
    private static final String FIELD_ROLE_PERSONNEL = "ROLE_PERSONNEL";
    private static final String FIELD_ADRESSE_1 = "ADRESSE_1";
    private static final String FIELD_ADRESSE_2 = "ADRESSE_2";
    private static final String FIELD_NPA = "NPA";
    private static final String FIELD_LOCALITE = "LOCALITE";
    private static final String FIELD_TEXTE_ERREUR_INTERNE = "TEXTE_ERREUR_INTERNE";
    private static final String FIELD_EMAIL = "EMAIL";
    private static final String FIELD_NUM_TEL = "NUM_TEL";
    private static final String FIELD_NUM_ADHERENT_BVR = "NUM_ADHERENT_BVR";
    private static final String FIELD_NUM_REF_BVR = "NUM_REF_BVR";

    private String idInscription;
    private String idFichier;
    private String eBillAccountID;
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
    private String statut;
    private String npa;
    private String localite;
    private String texteErreurInterne;
    private String email;
    private String numTel;
    private String numAdherentBVR;
    private String numRefBVR;

    @Override
    protected String _getTableName() {
        return TABLE_INSCRIPTION_EBILL;
    }

    @Override
    protected void _readProperties(BStatement bStatement) throws Exception {
        idInscription = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_ID_INSCRIPTION);
        idFichier = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_ID_FICHIER);
        eBillAccountID = bStatement.dbReadString(CAInscriptionEBill.FIELD_EBILL_ACCOUNT_ID);
        numeroAffilie = bStatement.dbReadString(CAInscriptionEBill.FIELD_NUMERO_AFFILIE);
        type = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_TYPE);
        nom = bStatement.dbReadString(CAInscriptionEBill.FIELD_NOM);
        prenom = bStatement.dbReadString(CAInscriptionEBill.FIELD_PRENOM);
        entreprise = bStatement.dbReadString(CAInscriptionEBill.FIELD_ENTREPRISE);
        contact = bStatement.dbReadString(CAInscriptionEBill.FIELD_CONTACT);
        roleParitaire = bStatement.dbReadBoolean(CAInscriptionEBill.FIELD_ROLE_PARITAIRE);
        rolePersonnel = bStatement.dbReadBoolean(CAInscriptionEBill.FIELD_ROLE_PERSONNEL);
        adresse1 = bStatement.dbReadString(CAInscriptionEBill.FIELD_ADRESSE_1);
        adresse2 = bStatement.dbReadString(CAInscriptionEBill.FIELD_ADRESSE_2);
        statut = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_STATUT);
        npa = bStatement.dbReadNumeric(CAInscriptionEBill.FIELD_NPA);
        localite = bStatement.dbReadString(CAInscriptionEBill.FIELD_LOCALITE);
        texteErreurInterne = bStatement.dbReadString(CAInscriptionEBill.FIELD_TEXTE_ERREUR_INTERNE);
        email = bStatement.dbReadString(CAInscriptionEBill.FIELD_EMAIL);
        numTel = bStatement.dbReadString(CAInscriptionEBill.FIELD_NUM_TEL);
        numAdherentBVR = bStatement.dbReadString(CAInscriptionEBill.FIELD_NUM_ADHERENT_BVR);
        numRefBVR = bStatement.dbReadString(CAInscriptionEBill.FIELD_NUM_REF_BVR);
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {

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
        bStatement.writeField(CAInscriptionEBill.FIELD_EBILL_ACCOUNT_ID,
                this._dbWriteString(bStatement.getTransaction(), geteBillAccountID(), "eBillAccountID"));
        bStatement.writeField(CAInscriptionEBill.FIELD_NUMERO_AFFILIE,
                this._dbWriteString(bStatement.getTransaction(), getNumeroAffilie(), "numeroAffilie"));
        bStatement.writeField(CAInscriptionEBill.FIELD_TYPE,
                this._dbWriteNumeric(bStatement.getTransaction(), type, "type"));
        bStatement.writeField(CAInscriptionEBill.FIELD_NOM,
                this._dbWriteString(bStatement.getTransaction(), getNom(), "nom"));
        bStatement.writeField(CAInscriptionEBill.FIELD_PRENOM,
                this._dbWriteString(bStatement.getTransaction(), getPrenom(), "prenom"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ENTREPRISE,
                this._dbWriteString(bStatement.getTransaction(), getEntreprise(), "entreprise"));
        bStatement.writeField(CAInscriptionEBill.FIELD_CONTACT,
                this._dbWriteString(bStatement.getTransaction(), getContact(), "contact"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ROLE_PARITAIRE,
                this._dbWriteBoolean(bStatement.getTransaction(), getRoleParitaire(), "roleParitaire"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ROLE_PERSONNEL,
                this._dbWriteBoolean(bStatement.getTransaction(), getRolePersonnel(), "rolePersonnel"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ADRESSE_1,
                this._dbWriteString(bStatement.getTransaction(), getAdresse1(), "adresse1"));
        bStatement.writeField(CAInscriptionEBill.FIELD_ADRESSE_2,
                this._dbWriteString(bStatement.getTransaction(), getAdresse2(), "adresse2"));
        bStatement.writeField(CAInscriptionEBill.FIELD_STATUT,
                this._dbWriteNumeric(bStatement.getTransaction(), statut, "statut"));
        bStatement.writeField(CAInscriptionEBill.FIELD_NPA,
                this._dbWriteNumeric(bStatement.getTransaction(), getNpa(), "npa"));
        bStatement.writeField(CAInscriptionEBill.FIELD_LOCALITE,
                this._dbWriteString(bStatement.getTransaction(), getLocalite(), "localite"));
        bStatement.writeField(CAInscriptionEBill.FIELD_TEXTE_ERREUR_INTERNE,
                this._dbWriteString(bStatement.getTransaction(), getTexteErreurInterne(), "texteErreurInterne"));
        bStatement.writeField(CAInscriptionEBill.FIELD_EMAIL,
                this._dbWriteString(bStatement.getTransaction(), getEmail(), "email"));
        bStatement.writeField(CAInscriptionEBill.FIELD_NUM_TEL,
                this._dbWriteString(bStatement.getTransaction(), getNumTel(), "numTel"));
        bStatement.writeField(CAInscriptionEBill.FIELD_NUM_ADHERENT_BVR,
                this._dbWriteString(bStatement.getTransaction(), getNumAdherentBVR(), "numAdherentBVR"));
        bStatement.writeField(CAInscriptionEBill.FIELD_NUM_REF_BVR,
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

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public CAInscriptionTypeEBillEnum getType() {
        return CAInscriptionTypeEBillEnum.parValeur(type);
    }

    public String getLibelleType(){
        return getSession().getLabel(getType().getDescription());
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNomPrenomOuEntreprise() {
        if(!JadeStringUtil.isEmpty(entreprise)) {
            return entreprise;
        } else {
            return (!JadeStringUtil.isEmpty(nom) ? nom : "") + (!JadeStringUtil.isEmpty(prenom) ? prenom : "");
        }
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
        return   CAStatutEBillEnum.parValeur(statut);
    }

    public String getLibelleStatut(){
        return getSession().getLabel(getStatut().getDescription());
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
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
}
