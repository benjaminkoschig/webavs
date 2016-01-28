package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

public class CPDecisionForCompareCI extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = new String();
    private BigDecimal difference1 = new BigDecimal(0);
    private BigDecimal difference2 = new BigDecimal(0);
    private String email = new String();
    private String genreAffilie = new String();
    private String idCI = new String();
    private String idTiers = new String();

    private BigDecimal montantCINoAffilie = new BigDecimal(0);

    private BigDecimal montantCINoAVSNoAffilie = new BigDecimal(0);
    private BigDecimal montantCP = new BigDecimal(0);
    private String numAffilie = new String();
    private String numAVS = new String();

    @Override
    protected String _getFields(BStatement statement) {
        // TODO Auto-generated method stub
        return "AFAFFIP.MALNAF, CIINDIP.KANAVS, CPDECIP.HTITIE, CPDECIP.IATGAF, CPDECIP.IAANNE, CIINDIP.KAIIND ";
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAffilie = statement.dbReadString("MALNAF");
        numAVS = statement.dbReadString("KANAVS");
        genreAffilie = statement.dbReadNumeric("IATGAF");
        annee = statement.dbReadNumeric("IAANNE");
        idTiers = statement.dbReadNumeric("HTITIE");
        idCI = statement.dbReadNumeric("KAIIND");

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAnnee() {
        return annee;
    }

    public BigDecimal getDifference1() {
        return difference1;
    }

    public BigDecimal getDifference2() {
        return difference2;
    }

    public String getEmail() {
        return email;
    }

    public String getGenreAffilie() {
        return genreAffilie;
    }

    public String getIdCI() {
        return idCI;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public BigDecimal getMontantCINoAffilie() {
        return montantCINoAffilie;
    }

    public BigDecimal getMontantCINoAVSNoAffilie() {
        return montantCINoAVSNoAffilie;
    }

    public BigDecimal getMontantCP() {
        return montantCP;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAVS() {
        return numAVS;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDifference1(BigDecimal difference1) {
        this.difference1 = difference1;
    }

    public void setDifference2(BigDecimal difference2) {
        this.difference2 = difference2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGenreAffilie(String genreAffilie) {
        this.genreAffilie = genreAffilie;
    }

    public void setIdCI(String idCI) {
        this.idCI = idCI;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantCINoAffilie(BigDecimal montantCINoAffilie) {
        this.montantCINoAffilie = montantCINoAffilie;
    }

    public void setMontantCINoAVSNoAffilie(BigDecimal montantCINoAVSNoAffilie) {
        this.montantCINoAVSNoAffilie = montantCINoAVSNoAffilie;
    }

    public void setMontantCP(BigDecimal montantCP) {
        this.montantCP = montantCP;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumAVS(String numAVS) {
        this.numAVS = numAVS;
    }

}
