package globaz.corvus.db.attestationsFiscales;

import java.util.Date;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class REAttestationFiscaleRentAccordOrdreVerse extends BEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String idRenteAccordee = "";
    private String csTypePrestDu = "";
    private String montantDette = "";
    private String csTypeOV = "";
    private Date dateDecisionFinal = null;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdRenteAccordee(statement.dbReadNumeric(
                REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_RENTE_ACCORDEE_PRESTATIONS_DUE));
        setCsTypePrestDu(statement
                .dbReadNumeric(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_CS_TYPE_PRESTATIONS_DUE));
        setMontantDette(statement.dbReadNumeric(
                REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ORDRE_VERSEMENT_MONTANT_DETTE));
        setCsTypeOV(statement
                .dbReadNumeric(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_TYPE_ORDRE_VERSEMENT));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // NOT WRITE

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // NOT WRITE

    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public String getCsTypePrestDu() {
        return csTypePrestDu;
    }

    public void setCsTypePrestDu(String csTypePrestDu) {
        this.csTypePrestDu = csTypePrestDu;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

    public String getCsTypeOV() {
        return csTypeOV;
    }

    public void setCsTypeOV(String csTypeOV) {
        this.csTypeOV = csTypeOV;
    }

    public Date getDateDecisionFinal() {
        return dateDecisionFinal;
    }

    public void setDateDecisionFinal(Date dateDeDecision) {
        dateDecisionFinal = dateDeDecision;
    }

    public boolean hasVersementCreancier() {
        if (csTypeOV.equals(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE)
                || csTypeOV.equals(IREOrdresVersements.CS_TYPE_TIERS)) {
            return true;
        } else {
            return false;
        }

    }

}
