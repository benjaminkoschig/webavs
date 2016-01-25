package globaz.corvus.db.ordresversements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class RESoldePourRestitution extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_FACTURE_A_RESTITUER = "ZYIFAR";
    public static final String FIELDNAME_ID_ORDRE_VERSEMENT = "ZYIOVE";
    public static final String FIELDNAME_ID_PRESTATION = "ZYIPRE";
    public static final String FIELDNAME_ID_RETENUE = "ZYIRET";
    public static final String FIELDNAME_ID_SOLDE_RESTIT = "ZYISLR";
    public static final String FIELDNAME_MONTANT = "ZYMMON";
    public static final String FIELDNAME_MONTANT_MENSUEL = "ZYMMEN";
    public static final String FIELDNAME_TYPE_RESTITUTION = "ZYTRST";
    public static final String TABLE_NAME_SOLDE_POUR_RESTIT = "RESLDRST";

    private String csTypeRestitution = "";
    private String idFactureARestituer = "";
    private String idOrdreVersement = "";
    private String idPrestation = "";
    private String idRetenue = "";
    private String idSoldePourRestitution = "";
    private String montant = "";
    private String montantMensuelARetenir = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSoldePourRestitution(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RESoldePourRestitution.TABLE_NAME_SOLDE_POUR_RESTIT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSoldePourRestitution = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_ID_SOLDE_RESTIT);
        idPrestation = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_ID_PRESTATION);
        montant = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_MONTANT);
        montantMensuelARetenir = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_MONTANT_MENSUEL);
        csTypeRestitution = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_TYPE_RESTITUTION);
        idRetenue = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_ID_RETENUE);
        idFactureARestituer = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_ID_FACTURE_A_RESTITUER);
        idOrdreVersement = statement.dbReadNumeric(RESoldePourRestitution.FIELDNAME_ID_ORDRE_VERSEMENT);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RESoldePourRestitution.FIELDNAME_ID_SOLDE_RESTIT,
                this._dbWriteNumeric(statement.getTransaction(), idSoldePourRestitution, "idSoldePourRestitution"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RESoldePourRestitution.FIELDNAME_ID_SOLDE_RESTIT,
                this._dbWriteNumeric(statement.getTransaction(), idSoldePourRestitution, "idSoldePourRestitution"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_ID_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_MONTANT_MENSUEL,
                this._dbWriteNumeric(statement.getTransaction(), montantMensuelARetenir, "montantMensuelARetenir"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_TYPE_RESTITUTION,
                this._dbWriteNumeric(statement.getTransaction(), csTypeRestitution, "csTypeRestitution"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_ID_RETENUE,
                this._dbWriteNumeric(statement.getTransaction(), idRetenue, "idRetenue"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_ID_FACTURE_A_RESTITUER,
                this._dbWriteNumeric(statement.getTransaction(), idFactureARestituer, "idFactureARestituer"));
        statement.writeField(RESoldePourRestitution.FIELDNAME_ID_ORDRE_VERSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idOrdreVersement, "idOrdreVersement"));
    }

    public String getCsTypeRestitution() {
        return csTypeRestitution;
    }

    public String getIdFactureARestituer() {
        return idFactureARestituer;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRetenue() {
        return idRetenue;
    }

    public String getIdSoldePourRestitution() {
        return idSoldePourRestitution;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantMensuelARetenir() {
        return montantMensuelARetenir;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setCsTypeRestitution(String csTypeRestitution) {
        this.csTypeRestitution = csTypeRestitution;
    }

    public void setIdFactureARestituer(String idFactureARestituer) {
        this.idFactureARestituer = idFactureARestituer;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }

    public void setIdSoldePourRestitution(String idSoldePourRestitution) {
        this.idSoldePourRestitution = idSoldePourRestitution;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantMensuelARetenir(String montantMensuelARetenir) {
        this.montantMensuelARetenir = montantMensuelARetenir;
    }
}
