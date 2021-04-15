package globaz.osiris.db.ebill;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.ebill.enums.CAFichierTraitementStatutEBillEnum;

import java.io.Serializable;

public class CAFichierTraitementEBill extends BEntity implements Serializable {

    public static final String TABLE_FICHIER_TRAITEMENT_EBILL = "FICHIER_TRAITEMENT_EBILL";
    public static final String FIELD_DATE_LECTURE = "DATE_LECTURE";
    public static final String FIELD_STATUT_FICIHER = "STATUT_FICHIER";
    public static final String FIELD_ID_FICHIER = "ID_FICHIER";
    public static final String FIELD_NB_ELEMENTS = "NB_ELEMENTS";
    public static final String FIELD_NB_ELEMENTS_TRAITES = "NB_ELEMENTS_TRAITES";
    public static final String FIELD_NB_ELEMENTS_EN_ERREURS = "NB_ELEMENTS_EN_ERREURS";
    public static final String FIELD_NB_ELEMENTS_REJETES = "NB_ELEMENTS_REJETES";
    public static final String FIELD_NOM_FICHIER = "NOM_FICHIER";

    private String idFichier;
    private String nomFichier;
    private String dateLecture;
    private String statutFichier;
    private String nbElements;
    private String nbElementsTraites;
    private String nbElementsEnErreurs;
    private String nbElementsRejetes;

    @Override
    protected String _getTableName() {
        return TABLE_FICHIER_TRAITEMENT_EBILL;
    }

    @Override
    protected void _readProperties(BStatement bStatement) throws Exception {
        idFichier = bStatement.dbReadNumeric(CAFichierTraitementEBill.FIELD_ID_FICHIER);
        nomFichier = bStatement.dbReadString(CAFichierTraitementEBill.FIELD_NOM_FICHIER);
        dateLecture = bStatement.dbReadDateAMJ(CAFichierTraitementEBill.FIELD_DATE_LECTURE);
        statutFichier = bStatement.dbReadNumeric(CAFichierTraitementEBill.FIELD_STATUT_FICIHER);
        nbElements = bStatement.dbReadNumeric(CAFichierTraitementEBill.FIELD_NB_ELEMENTS);
        nbElementsTraites = bStatement.dbReadNumeric(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_TRAITES);
        nbElementsEnErreurs = bStatement.dbReadNumeric(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_EN_ERREURS);
        nbElementsRejetes = bStatement.dbReadNumeric(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_REJETES);
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement bStatement) throws Exception {
        bStatement.writeKey(CAFichierTraitementEBill.FIELD_ID_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdFichier(), ""));
    }

    @Override
    protected void _writeProperties(BStatement bStatement) throws Exception {
        bStatement.writeField(CAFichierTraitementEBill.FIELD_ID_FICHIER,
                this._dbWriteNumeric(bStatement.getTransaction(), getIdFichier(), "idFichier"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_NOM_FICHIER,
                this._dbWriteString(bStatement.getTransaction(), getNomFichier(), "nomFichier"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_DATE_LECTURE,
                this._dbWriteDateAMJ(bStatement.getTransaction(), getDateLecture(), "dateLecture"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_STATUT_FICIHER,
                this._dbWriteNumeric(bStatement.getTransaction(), statutFichier, "statutFichier"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_NB_ELEMENTS,
                this._dbWriteNumeric(bStatement.getTransaction(), getNbElements(), "nbElements"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_TRAITES,
                this._dbWriteNumeric(bStatement.getTransaction(), getNbElementsTraites(), "nbElementsTraites"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_EN_ERREURS,
                this._dbWriteNumeric(bStatement.getTransaction(), getNbElementsEnErreurs(), "nbElementsEnErreurs"));
        bStatement.writeField(CAFichierTraitementEBill.FIELD_NB_ELEMENTS_REJETES,
                this._dbWriteNumeric(bStatement.getTransaction(), getNbElementsRejetes(), "nbElementsRejetes"));
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

    public CAFichierTraitementStatutEBillEnum getStatutFichier() {
        return CAFichierTraitementStatutEBillEnum.fromIndex(statutFichier);
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

    public String getNbElementsTraites() {
        return nbElementsTraites;
    }

    public void setNbElementsTraites(String nbElementsTraites) {
        this.nbElementsTraites = nbElementsTraites;
    }

    public String getNbElementsEnErreurs() {
        return nbElementsEnErreurs;
    }

    public void setNbElementsEnErreurs(String nbElementsEnErreurs) {
        this.nbElementsEnErreurs = nbElementsEnErreurs;
    }

    public String getNbElementsRejetes() {
        return nbElementsRejetes;
    }

    public void setNbElementsRejetes(String nbElementsRejetes) {
        this.nbElementsRejetes = nbElementsRejetes;
    }
}
