package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.db.ebill.enums.CATraitementCodeErreurEBillEnum;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class CATraitementEBill extends BEntity implements Serializable {

    public static final String TABLE_TRAITEMENT_EBILL = "CAET";
    public static final String FIELD_ID_TRAITEMENT = "ID_TRAITEMENT";
    public static final String FIELD_ID_FICHIER = "ID_FICHIER";
    public static final String FIELD_EBILL_ACCOUNT_ID = "EBILL_ACCOUNT_ID";
    public static final String FIELD_NUMERO_AFFILIE = "NUMERO_AFFILIE";
    public static final String FIELD_STATUT = "STATUT";
    public static final String FIELD_ETAT = "ETAT";

    private static final String FIELD_NOM = "NOM";
    private static final String FIELD_PRENOM = "PRENOM";
    private static final String FIELD_ENTREPRISE = "ENTREPRISE";
    public static final String FIELD_CODE_ERREUR = "CODE_ERREUR";
    private static final String FIELD_TEXTE_ERREUR = "TEXTE_ERREUR";
    public static final String FIELD_TEXTE_ERREUR_INTERNE = "TEXTE_ERREUR_INTERNE";
    private static final String FIELD_NUM_ADHERENT_BVR = "NUM_ADHERENT_BVR";
    private static final String FIELD_NUM_REF_BVR = "NUM_REF_BVR";

    public static final String FIELD_TRANSACTION_ID = "TRANSACTION_ID";
    private static final String FIELD_MONTANT_TOTAL = "MONTANT_TOTAL";
    private static final String FIELD_DATE_TRAITEMENT = "DATE_TRAITEMENT";
    
    private String idTraitement;
    private String idFichier;
    private String eBillAccountID;
    private String numeroAffilie;
    private String nom;
    private String prenom;
    private String entreprise;
    private String statut;
    private String etat;
    private String codeErreur;
    private String texteErreur;
    private String texteErreurInterne;
    private String numAdherentBVR;
    private String numRefBVR;

    private String transactionID;
    private String montantTotal;
    private String dateTraitement;

    @Override
    protected String _getTableName() {
        return TABLE_TRAITEMENT_EBILL;
    }

    @Override
    protected void _readProperties(BStatement bStatement) throws Exception {
        idTraitement = bStatement.dbReadNumeric(CATraitementEBill.FIELD_ID_TRAITEMENT);
        idFichier = bStatement.dbReadNumeric(CATraitementEBill.FIELD_ID_FICHIER);
        eBillAccountID = bStatement.dbReadNumeric(CATraitementEBill.FIELD_EBILL_ACCOUNT_ID);
        numeroAffilie = bStatement.dbReadString(CATraitementEBill.FIELD_NUMERO_AFFILIE);
        nom = bStatement.dbReadString(CATraitementEBill.FIELD_NOM);
        prenom = bStatement.dbReadString(CATraitementEBill.FIELD_PRENOM);
        entreprise = bStatement.dbReadString(CATraitementEBill.FIELD_ENTREPRISE);
        statut = bStatement.dbReadNumeric(CATraitementEBill.FIELD_STATUT);
        etat = bStatement.dbReadNumeric(CATraitementEBill.FIELD_ETAT);
        codeErreur = bStatement.dbReadString(CATraitementEBill.FIELD_CODE_ERREUR);
        texteErreur = bStatement.dbReadString(CATraitementEBill.FIELD_TEXTE_ERREUR);
        texteErreurInterne = bStatement.dbReadString(CATraitementEBill.FIELD_TEXTE_ERREUR_INTERNE);
        numAdherentBVR = bStatement.dbReadString(CATraitementEBill.FIELD_NUM_ADHERENT_BVR);
        numRefBVR = bStatement.dbReadString(CATraitementEBill.FIELD_NUM_REF_BVR);

        transactionID = bStatement.dbReadString(CATraitementEBill.FIELD_TRANSACTION_ID);
        montantTotal = bStatement.dbReadString(CATraitementEBill.FIELD_MONTANT_TOTAL);
        dateTraitement = bStatement.dbReadString(CATraitementEBill.FIELD_DATE_TRAITEMENT);
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement bStatement) throws Exception {
        bStatement.writeKey(CATraitementEBill.FIELD_ID_TRAITEMENT,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdTraitement(), ""));
    }

    @Override
    protected void _writeProperties(BStatement bStatement) throws Exception {
        bStatement.writeField(CATraitementEBill.FIELD_ID_TRAITEMENT,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdTraitement(), "idTraitement"));
        bStatement.writeField(CATraitementEBill.FIELD_ID_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdFichier(), "idFichier"));
        bStatement.writeField(CATraitementEBill.FIELD_EBILL_ACCOUNT_ID,
                this._dbWriteString(bStatement.getTransaction(), geteBillAccountID(), "eBillAccountID"));
        bStatement.writeField(CATraitementEBill.FIELD_NUMERO_AFFILIE,
                this._dbWriteString(bStatement.getTransaction(), getNumeroAffilie(), "numeroAffilie"));
        bStatement.writeField(CATraitementEBill.FIELD_NOM,
                this._dbWriteString(bStatement.getTransaction(), getNom(), "nom"));
        bStatement.writeField(CATraitementEBill.FIELD_PRENOM,
                this._dbWriteString(bStatement.getTransaction(), getPrenom(), "prenom"));
        bStatement.writeField(CATraitementEBill.FIELD_ENTREPRISE,
                this._dbWriteString(bStatement.getTransaction(), getEntreprise(), "entreprise"));
        bStatement.writeField(CATraitementEBill.FIELD_STATUT,
                this._dbWriteNumeric(bStatement.getTransaction(), statut, "statut"));
        bStatement.writeField(CATraitementEBill.FIELD_ETAT,
                this._dbWriteNumeric(bStatement.getTransaction(), etat, "etat"));
        bStatement.writeField(CATraitementEBill.FIELD_CODE_ERREUR,
                this._dbWriteString(bStatement.getTransaction(), codeErreur, "codeErreur"));
        bStatement.writeField(CATraitementEBill.FIELD_TEXTE_ERREUR,
                this._dbWriteString(bStatement.getTransaction(), getTexteErreur(), "texteErreur"));
        bStatement.writeField(CATraitementEBill.FIELD_TEXTE_ERREUR_INTERNE,
                this._dbWriteString(bStatement.getTransaction(), getTexteErreurInterne(), "texteErreurInterne"));
        bStatement.writeField(CATraitementEBill.FIELD_NUM_ADHERENT_BVR,
                this._dbWriteString(bStatement.getTransaction(), getNumAdherentBVR(), "numAdherentBVR"));
        bStatement.writeField(CATraitementEBill.FIELD_NUM_REF_BVR,
                this._dbWriteString(bStatement.getTransaction(), getNumRefBVR(), "numRefBVR"));

        bStatement.writeField(CATraitementEBill.FIELD_TRANSACTION_ID,
                this._dbWriteString(bStatement.getTransaction(), getTransactionID(), "transactionID"));
        bStatement.writeField(CATraitementEBill.FIELD_MONTANT_TOTAL,
                this._dbWriteString(bStatement.getTransaction(), getMontantTotal(), "montantTotal"));
        bStatement.writeField(CATraitementEBill.FIELD_DATE_TRAITEMENT,
                this._dbWriteString(bStatement.getTransaction(), getDateTraitement(), "dateTraitement"));
    }

    /*
     * (non-Javadoc)
     *
     * @seeglobaz.osiris.db.comptes.CAOperation#_beforeAdd(globaz.globall.db. BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdTraitement(this._incCounter(transaction, idTraitement));
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

    public CAStatutEBillEnum getStatut() {
        return   CAStatutEBillEnum.parValeur(statut);
    }

    public String getLibelleStatut() {
        return   getSession().getLabel(getStatut().getDescription());
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public CATraitementEtatEBillEnum getEtat() {
        return   CATraitementEtatEBillEnum.parValeur(etat);
    }

    public String getLibelleEtat() {
        return getSession().getLabel(getEtat().getDescription());
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCodeErreurDescription() {
        if (codeErreur != null) {
            CATraitementCodeErreurEBillEnum codeErreurEBill = CATraitementCodeErreurEBillEnum.parValeur(codeErreur);
            if (StringUtils.isNotEmpty(codeErreurEBill.getDescription())) {
                return codeErreurEBill.getDescription();
            } else {
                return codeErreur;
            }
        }
        return null;
    }

    public void setCodeErreur(String codeErreur) {
        this.codeErreur = codeErreur;
    }

    public String getTexteErreur() {
        return texteErreur;
    }

    public void setTexteErreur(String texteErreur) {
        this.texteErreur = texteErreur;
    }

    public String getTexteErreurInterne() {
        return texteErreurInterne;
    }

    public void setTexteErreurInterne(String texteErreurInterne) {
        this.texteErreurInterne = texteErreurInterne;
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

    public String getIdTraitement() {
        return idTraitement;
    }

    public void setIdTraitement(String idTraitement) {
        this.idTraitement = idTraitement;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }
}
