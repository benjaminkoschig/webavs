package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.ebill.enums.CAFichierInscriptionStatutEBillEnum;

import java.io.Serializable;

public class CAFichierInscriptionEBill extends BEntity implements Serializable {

    public static final String TABLE_FICHIER_INSCRIPTION_EBILL = "CAEFI";
    public static final String FIELD_DATE_LECTURE = "DATE_LECTURE";
    public static final String FIELD_STATUT_FICHIER = "STATUT_FICHIER";
    public static final String FIELD_ID_FICHIER = "ID_FICHIER";
    public static final String FIELD_NB_ELEMENTS = "NB_ELEMENTS";
    public static final String FIELD_NOM_FICHIER = "NOM_FICHIER";

    private String idFichier;
    private String nomFichier;
    private String dateLecture;
    private String statutFichier;
    private String nbElements;

    @Override
    protected String _getTableName() {
        return TABLE_FICHIER_INSCRIPTION_EBILL;
    }

    @Override
    protected void _readProperties(BStatement bStatement) throws Exception {
        idFichier = bStatement.dbReadNumeric(CAFichierInscriptionEBill.FIELD_ID_FICHIER);
        nomFichier = bStatement.dbReadString(CAFichierInscriptionEBill.FIELD_NOM_FICHIER);
        dateLecture = bStatement.dbReadDateAMJ(CAFichierInscriptionEBill.FIELD_DATE_LECTURE);
        statutFichier = bStatement.dbReadNumeric(CAFichierInscriptionEBill.FIELD_STATUT_FICHIER);
        nbElements = bStatement.dbReadNumeric(CAFichierInscriptionEBill.FIELD_NB_ELEMENTS);
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {
        // no implementation needed : pas de contrôle avant mise en bdd.
    }

    @Override
    protected void _writePrimaryKey(BStatement bStatement) throws Exception {
        bStatement.writeKey(CAFichierInscriptionEBill.FIELD_ID_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdFichier(), ""));
    }

    @Override
    protected void _writeProperties(BStatement bStatement) throws Exception {
        bStatement.writeField(CAFichierInscriptionEBill.FIELD_ID_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdFichier(), "idFichier"));
        bStatement.writeField(CAFichierInscriptionEBill.FIELD_NOM_FICHIER,
                this._dbWriteString(bStatement.getTransaction(), getNomFichier(), "nomFichier"));
        bStatement.writeField(CAFichierInscriptionEBill.FIELD_DATE_LECTURE,
                this._dbWriteDateAMJ(bStatement.getTransaction(), getDateLecture(), "dateLecture"));
        bStatement.writeField(CAFichierInscriptionEBill.FIELD_STATUT_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), statutFichier, "statutFichier"));
    }

    /*
     * (non-Javadoc)
     *
     * @seeglobaz.osiris.db.comptes.CAOperation#_beforeAdd(globaz.globall.db. BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFichier(this._incCounter(transaction, idFichier));
        super._beforeAdd(transaction);
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(String dateLecture) {
        this.dateLecture = dateLecture;
    }

    public CAFichierInscriptionStatutEBillEnum getStatutFichier() {
        return CAFichierInscriptionStatutEBillEnum.fromIndex(statutFichier);
    }

    public String getLibelleStatutFichier(){
        return getSession().getLabel(getStatutFichier().getDescription());
    }

    public void setStatutFichier(String statutFichier) {
        this.statutFichier = statutFichier;
    }

    public String getIdFichier() {
        return idFichier;
    }

    public void setIdFichier(String idFichier) {
        this.idFichier = idFichier;
    }

    public String getNbElements() {
        return nbElements;
    }

    public void setNbElements(String nbElements) {
        this.nbElements = nbElements;
    }
}
