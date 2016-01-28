package globaz.corvus.db.adaptation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author HPE
 * 
 */
public class RERentesAdaptees extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ANCI_ANNEE_MONTANT_RAM = "ABNAAM";

    public static final String FIELDNAME_ANCI_MNT_REDUCTION_ANTIC = "ABMAMR";
    public static final String FIELDNAME_ANCI_MNT_RENTE_ORDI_REMP = "ABMARO";
    public static final String FIELDNAME_ANCI_MONTANT_PRESTATION = "ABMAMP";
    public static final String FIELDNAME_ANCI_RAM = "ABMARA";
    public static final String FIELDNAME_ANCI_SUPP_AJOURNEMENT = "ABMASA";
    public static final String FIELDNAME_CODE_PRESTATION = "ABLCPR";
    public static final String FIELDNAME_FRACTION_RENTE = "ABLFRR";
    public static final String FIELDNAME_ID_PRESTATION_ACCORDEE = "ABIPRE";
    public static final String FIELDNAME_ID_RENTE_ADAPTEE = "ABIRAD";
    public static final String FIELDNAME_NOM = "ABLNOM";
    public static final String FIELDNAME_NOUV_ANNEE_MONTANT_RAM = "ABNNAM";
    public static final String FIELDNAME_NOUV_MNT_REDUCTION_ANTIC = "ABMNMR";
    public static final String FIELDNAME_NOUV_MNT_RENTE_ORDI_REMP = "ABMNRO";
    public static final String FIELDNAME_NOUV_MONTANT_PRESTATION = "ABMNMP";
    public static final String FIELDNAME_NOUV_RAM = "ABMNRA";
    public static final String FIELDNAME_NOUV_SUPP_AJOURNEMENT = "ABMNSA";
    public static final String FIELDNAME_NSS = "ABLNSS";
    public static final String FIELDNAME_PRENOM = "ABLPRE";
    public static final String FIELDNAME_REMARQUES_ADAPTATION = "ABLREM";
    public static final String FIELDNAME_TYPE_ADAPTATION = "ABTTAD";
    public static final String TABLE_NAME_RENTES_ADAPTEES = "READALI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String ancienAnneeMontantRAM = "";
    private String ancienMntReductionAnticipation = "";
    private String ancienMntRenteOrdinaireRempl = "";
    private String ancienMontantPrestation = "";
    private String ancienRAM = "";
    private String ancienSupplementAjournement = "";
    private String codePrestation = "";
    private String csTypeAdaptation = "";
    private String fractionRente = "";
    private String idPrestationAccordee = "";
    private String idRenteAdaptee = "";
    private String nomTri = "";
    private String nouveauAnneeMontantRAM = "";
    private String nouveauMntReductionAnticipation = "";
    private String nouveauMntRenteOrdinaireRempl = "";
    private String nouveauMontantPrestation = "";
    private String nouveauRAM = "";
    private String nouveauSupplementAjournement = "";
    private String nssTri = "";
    private String prenomTri = "";
    private String remarquesAdaptation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRenteAdaptee(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RERentesAdaptees.TABLE_NAME_RENTES_ADAPTEES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idRenteAdaptee = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ID_RENTE_ADAPTEE);
        csTypeAdaptation = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_TYPE_ADAPTATION);
        ancienRAM = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ANCI_RAM);
        nouveauRAM = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_NOUV_RAM);
        ancienMontantPrestation = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ANCI_MONTANT_PRESTATION);
        nouveauMontantPrestation = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_NOUV_MONTANT_PRESTATION);
        ancienMntRenteOrdinaireRempl = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ANCI_MNT_RENTE_ORDI_REMP);
        nouveauMntRenteOrdinaireRempl = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_NOUV_MNT_RENTE_ORDI_REMP);
        ancienSupplementAjournement = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ANCI_SUPP_AJOURNEMENT);
        nouveauSupplementAjournement = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_NOUV_SUPP_AJOURNEMENT);
        ancienMntReductionAnticipation = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ANCI_MNT_REDUCTION_ANTIC);
        nouveauMntReductionAnticipation = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_NOUV_MNT_REDUCTION_ANTIC);
        ancienAnneeMontantRAM = statement.dbReadString(RERentesAdaptees.FIELDNAME_ANCI_ANNEE_MONTANT_RAM);
        nouveauAnneeMontantRAM = statement.dbReadString(RERentesAdaptees.FIELDNAME_NOUV_ANNEE_MONTANT_RAM);
        idPrestationAccordee = statement.dbReadNumeric(RERentesAdaptees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        codePrestation = statement.dbReadString(RERentesAdaptees.FIELDNAME_CODE_PRESTATION);
        nomTri = statement.dbReadString(RERentesAdaptees.FIELDNAME_NOM);
        prenomTri = statement.dbReadString(RERentesAdaptees.FIELDNAME_PRENOM);
        nssTri = statement.dbReadString(RERentesAdaptees.FIELDNAME_NSS);
        fractionRente = statement.dbReadString(RERentesAdaptees.FIELDNAME_FRACTION_RENTE);
        remarquesAdaptation = statement.dbReadString(RERentesAdaptees.FIELDNAME_REMARQUES_ADAPTATION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RERentesAdaptees.FIELDNAME_ID_RENTE_ADAPTEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteAdaptee, "idRenteAdaptee"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RERentesAdaptees.FIELDNAME_ID_RENTE_ADAPTEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteAdaptee, "idRenteAdaptee"));
        statement.writeField(RERentesAdaptees.FIELDNAME_TYPE_ADAPTATION,
                this._dbWriteNumeric(statement.getTransaction(), csTypeAdaptation, "csTypeAdaptation"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ANCI_RAM,
                this._dbWriteNumeric(statement.getTransaction(), ancienRAM, "ancienRAM"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOUV_RAM,
                this._dbWriteNumeric(statement.getTransaction(), nouveauRAM, "nouveauRAM"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ANCI_MONTANT_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), ancienMontantPrestation, "ancienMontantPrestation"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOUV_MONTANT_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), nouveauMontantPrestation, "nouveauMontantPrestation"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ANCI_MNT_RENTE_ORDI_REMP, this._dbWriteNumeric(
                statement.getTransaction(), ancienMntRenteOrdinaireRempl, "ancienMntRenteOrdinaireRempl"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOUV_MNT_RENTE_ORDI_REMP, this._dbWriteNumeric(
                statement.getTransaction(), nouveauMntRenteOrdinaireRempl, "nouveauMntRenteOrdinaireRempl"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ANCI_SUPP_AJOURNEMENT, this._dbWriteNumeric(
                statement.getTransaction(), ancienSupplementAjournement, "ancienSupplementAjournement"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOUV_SUPP_AJOURNEMENT, this._dbWriteNumeric(
                statement.getTransaction(), nouveauSupplementAjournement, "nouveauSupplementAjournement"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ANCI_MNT_REDUCTION_ANTIC, this._dbWriteNumeric(
                statement.getTransaction(), ancienMntReductionAnticipation, "ancienMntReductionAnticipation"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOUV_MNT_REDUCTION_ANTIC, this._dbWriteNumeric(
                statement.getTransaction(), nouveauMntReductionAnticipation, "nouveauMntReductionAnticipation"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ANCI_ANNEE_MONTANT_RAM,
                this._dbWriteString(statement.getTransaction(), ancienAnneeMontantRAM, "ancienAnneeMontantRAM"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOUV_ANNEE_MONTANT_RAM,
                this._dbWriteString(statement.getTransaction(), nouveauAnneeMontantRAM, "nouveauAnneeMontantRAM"));
        statement.writeField(RERentesAdaptees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
        statement.writeField(RERentesAdaptees.FIELDNAME_CODE_PRESTATION,
                this._dbWriteString(statement.getTransaction(), codePrestation, "codePrestation"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NOM,
                this._dbWriteString(statement.getTransaction(), nomTri, "nomTri"));
        statement.writeField(RERentesAdaptees.FIELDNAME_PRENOM,
                this._dbWriteString(statement.getTransaction(), prenomTri, "prenomTri"));
        statement.writeField(RERentesAdaptees.FIELDNAME_NSS,
                this._dbWriteString(statement.getTransaction(), nssTri, "nssTri"));
        statement.writeField(RERentesAdaptees.FIELDNAME_FRACTION_RENTE,
                this._dbWriteString(statement.getTransaction(), fractionRente, "fractionRente"));
        statement.writeField(RERentesAdaptees.FIELDNAME_REMARQUES_ADAPTATION,
                this._dbWriteString(statement.getTransaction(), remarquesAdaptation, "remarquesAdaptation"));

    }

    public String getAncienAnneeMontantRAM() {
        return ancienAnneeMontantRAM;
    }

    public String getAncienMntReductionAnticipation() {
        return ancienMntReductionAnticipation;
    }

    public String getAncienMntRenteOrdinaireRempl() {
        return ancienMntRenteOrdinaireRempl;
    }

    public String getAncienMontantPrestation() {
        return ancienMontantPrestation;
    }

    public String getAncienRAM() {
        return ancienRAM;
    }

    public String getAncienSupplementAjournement() {
        return ancienSupplementAjournement;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsTypeAdaptation() {
        return csTypeAdaptation;
    }

    public String getFractionRente() {

        if (JadeStringUtil.isBlank(fractionRente)) {
            return "0";
        }

        return fractionRente;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdRenteAdaptee() {
        return idRenteAdaptee;
    }

    public String getNomTri() {
        return nomTri;
    }

    public String getNouveauAnneeMontantRAM() {
        return nouveauAnneeMontantRAM;
    }

    public String getNouveauMntReductionAnticipation() {
        return nouveauMntReductionAnticipation;
    }

    public String getNouveauMntRenteOrdinaireRempl() {
        return nouveauMntRenteOrdinaireRempl;
    }

    public String getNouveauMontantPrestation() {
        return nouveauMontantPrestation;
    }

    public String getNouveauRAM() {
        return nouveauRAM;
    }

    public String getNouveauSupplementAjournement() {
        return nouveauSupplementAjournement;
    }

    public String getNssTri() {
        return nssTri;
    }

    public String getPrenomTri() {
        return prenomTri;
    }

    public String getRemarquesAdaptation() {
        return remarquesAdaptation;
    }

    public void setAncienAnneeMontantRAM(String ancienAnneeMontantRAM) {
        this.ancienAnneeMontantRAM = ancienAnneeMontantRAM;
    }

    public void setAncienMntReductionAnticipation(String ancienMntReductionAnticipation) {
        this.ancienMntReductionAnticipation = ancienMntReductionAnticipation;
    }

    public void setAncienMntRenteOrdinaireRempl(String ancienMntRenteOrdinaireRempl) {
        this.ancienMntRenteOrdinaireRempl = ancienMntRenteOrdinaireRempl;
    }

    public void setAncienMontantPrestation(String ancienMontantPrestation) {
        this.ancienMontantPrestation = ancienMontantPrestation;
    }

    public void setAncienRAM(String ancienRAM) {
        this.ancienRAM = ancienRAM;
    }

    public void setAncienSupplementAjournement(String ancienSupplementAjournement) {
        this.ancienSupplementAjournement = ancienSupplementAjournement;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsTypeAdaptation(String csTypeAdaptation) {
        this.csTypeAdaptation = csTypeAdaptation;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdRenteAdaptee(String idRenteAdaptee) {
        this.idRenteAdaptee = idRenteAdaptee;
    }

    public void setNomTri(String nomTri) {
        this.nomTri = nomTri;
    }

    public void setNouveauAnneeMontantRAM(String nouveauAnneeMontantRAM) {
        this.nouveauAnneeMontantRAM = nouveauAnneeMontantRAM;
    }

    public void setNouveauMntReductionAnticipation(String nouveauMntReductionAnticipation) {
        this.nouveauMntReductionAnticipation = nouveauMntReductionAnticipation;
    }

    public void setNouveauMntRenteOrdinaireRempl(String nouveauMntRenteOrdinaireRempl) {
        this.nouveauMntRenteOrdinaireRempl = nouveauMntRenteOrdinaireRempl;
    }

    public void setNouveauMontantPrestation(String nouveauMontantPrestation) {
        this.nouveauMontantPrestation = nouveauMontantPrestation;
    }

    public void setNouveauRAM(String nouveauRAM) {
        this.nouveauRAM = nouveauRAM;
    }

    public void setNouveauSupplementAjournement(String nouveauSupplementAjournement) {
        this.nouveauSupplementAjournement = nouveauSupplementAjournement;
    }

    public void setNssTri(String nssTri) {
        this.nssTri = nssTri;
    }

    public void setPrenomTri(String prenomTri) {
        this.prenomTri = prenomTri;
    }

    public void setRemarquesAdaptation(String remarquesAdaptation) {
        this.remarquesAdaptation = remarquesAdaptation;
    }

}
