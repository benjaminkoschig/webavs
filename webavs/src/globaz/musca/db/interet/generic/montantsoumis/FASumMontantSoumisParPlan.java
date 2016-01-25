package globaz.musca.db.interet.generic.montantsoumis;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.db.interets.CAInteretMoratoire;

/**
 * Montant soumis cumulé par entente facture, anneecotisation, date de réception et id externe de la facture.
 * 
 * @author DDA
 */
public class FASumMontantSoumisParPlan extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCotisation;
    private String dateReceptionDecompte;
    private String idEnteteFacture;
    private String idExterneFacture;
    private String idSoumisInteretsMoratoires;

    private String montant;
    private String montantDejaFacture;
    private String montantInitial;

    private String typeCalculInteretMoratoire = "0";

    @Override
    protected String _getTableName() {
        return FAAfact.TABLE_FAAFACP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdEnteteFacture(statement.dbReadNumeric(FAEnteteFacture.FIELD_IDENTETEFACTURE));
        setAnneeCotisation(statement.dbReadNumeric(FAAfact.FIELD_ANNEECOTISATION));
        setDateReceptionDecompte(statement.dbReadString(FAEnteteFacture.FIELD_REFCOLLABORATEUR));
        setIdExterneFacture(statement.dbReadString(FAEnteteFacture.FIELD_IDEXTERNEFACTURE));
        setIdSoumisInteretsMoratoires(statement.dbReadNumeric(FAEnteteFacture.FIELD_IDSOUINTMOR));

        setMontant(statement.dbReadNumeric(FAAfact.FIELD_MONTANTFACTURE));
        setMontantDejaFacture(statement.dbReadNumeric(FAAfact.FIELD_MONTANTDEJAFACTURE));
        setMontantInitial(statement.dbReadNumeric(FAAfact.FIELD_MONTANTINITIAL));

        setTypeCalculInteretMoratoire(statement.dbReadNumeric(FAAfact.FIELD_TYPECALCULIM));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Nothing
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Nothing
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getDateDebutCalcul() {
        return "01.01." + (Integer.parseInt(getAnneeCotisation()) + 1);
    }

    /**
     * @return date limite de récepètion du décompte.
     * @throws Exception
     */
    public JADate getDateLimite() throws Exception {
        // Inforom321 : Date limite doit être un jour ouvrable
        JACalendarGregorian calen = new JACalendarGregorian();
        return calen.getNextWorkingDay(new JADate("30.01." + (Integer.parseInt(getYearFromIdExterneFacture()) + 1)));
    }

    public String getDateReceptionDecompte() {
        return dateReceptionDecompte;
    }

    public JADate getDateReceptionDecompteAsJADate() throws Exception {
        return new JADate(getDateReceptionDecompte());
    }

    public String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    public String getIdExterneFacture() {
        return idExterneFacture;
    }

    public String getIdSoumisInteretsMoratoires() {
        return idSoumisInteretsMoratoires;
    }

    public String getMontant() {
        return montant;
    }

    public FWCurrency getMontantAsCurrency() {
        return new FWCurrency(getMontant());
    }

    public String getMontantDejaFacture() {
        return montantDejaFacture;
    }

    public String getMontantInitial() {
        return montantInitial;
    }

    public FWCurrency getMontantInitialAsCurrency() {
        return new FWCurrency(getMontantInitial());
    }

    public String getTypeCalculInteretMoratoire() {
        return typeCalculInteretMoratoire;
    }

    private String getYearFromIdExterneFacture() throws Exception {
        if ((getIdExterneFacture() == null) || (getIdExterneFacture().length() < 4)) {
            // TODO Dal 25 juil. 07 add label
            throw new Exception("Longueur d'id externe facture non supportée.");
        }

        return getIdExterneFacture().substring(0, 4);
    }

    public boolean isExempte() {
        return CAInteretMoratoire.CS_EXEMPTE.equals(getIdSoumisInteretsMoratoires());
    }

    public boolean isMontantNegatif() {
        return getMontantAsCurrency().isNegative();
    }

    public boolean isMontantPositif() {
        return getMontantAsCurrency().isPositive();
    }

    public boolean isSoumis() {
        return CAInteretMoratoire.CS_SOUMIS.equals(getIdSoumisInteretsMoratoires());
    }

    public void setAnneeCotisation(String anneCotisation) {
        anneeCotisation = anneCotisation;
    }

    public void setDateReceptionDecompte(String dateReceptionDecompte) {
        this.dateReceptionDecompte = dateReceptionDecompte;
    }

    public void setIdEnteteFacture(String idEnteteFacture) {
        this.idEnteteFacture = idEnteteFacture;
    }

    public void setIdExterneFacture(String idExterneFacture) {
        this.idExterneFacture = idExterneFacture;
    }

    public void setIdSoumisInteretsMoratoires(String idSoumisInteretsMoratoires) {
        this.idSoumisInteretsMoratoires = idSoumisInteretsMoratoires;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDejaFacture(String montantDejaFacture) {
        this.montantDejaFacture = montantDejaFacture;
    }

    public void setMontantInitial(String montantInitial) {
        this.montantInitial = montantInitial;
    }

    public void setTypeCalculInteretMoratoire(String typeCalculInteretMoratoire) {
        this.typeCalculInteretMoratoire = typeCalculInteretMoratoire;
    }

}
