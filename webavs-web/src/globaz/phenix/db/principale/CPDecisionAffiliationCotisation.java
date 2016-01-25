package globaz.phenix.db.principale;

import globaz.globall.util.JANumberFormatter;
import globaz.naos.translation.CodeSystem;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CPDecisionAffiliationCotisation extends CPDecisionAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (ISBPRO) */
    private Boolean aForceAZero = new Boolean(false);
    /** (ISBZER) */
    private Boolean cotisationMinimum = new Boolean(false);
    /** (ISMTAU) */
    private String debutCotisation = "";
    /** (ISDDCO) */
    private String finCotisation = "";
    /** (ISICOT) */
    private String genreCotisation = "";
    /** (ISTGCO) */
    private String idCotiAffiliation = "";
    private String idCotisation = "";
    /** (ISMCSE) */
    private String montantAnnuel = "";
    /** (ISTPER) */
    private String montantFacture = "";
    /** (MEICOT) */
    private String montantMensuel = "";
    /** (ISMCTR) */
    private String montantSemestriel = "";
    /** (ISMCME) */
    private String montantTrimestriel = "";
    /** (ISDFCO) */
    private String periodicite = "";
    /** (ISMCAN) */
    private String taux = "";

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {

        // String fields = super._getFields(statement);
        String fields = "";
        fields += _getCollection() + "CPDECIP.IAIDEC IAIDEC," + _getCollection() + "CPDECIP.HTITIE HTITIE,"
                + _getCollection() + "CPDECIP.MAIAFF MAIAFF," + _getCollection() + "AFAFFIP.MALNAF MALNAF,"
                + _getCollection() + "AFAFFIP.MADDEB MADDEB," + _getCollection() + "AFAFFIP.MADFIN MADFIN,"
                + _getCollection() + "CPDECIP.EBIPAS EBIPAS," + _getCollection() + "CPDECIP.IATTDE IATTDE,"
                + _getCollection() + "CPDECIP.IATGAF IATGAF," + _getCollection() + "CPDECIP.IAANNE IAANNE,"
                + _getCollection() + "CPDECIP.IADDEB IADDEB," + _getCollection() + "CPDECIP.IADFIN IADFIN,"
                + _getCollection() + "CPDECIP.IAACTI IAACTI," + _getCollection() + "CPDECIP.IABIMP IATETA, "
                + _getCollection() + "CPCOTIP.ISICOT ISICOT, " + _getCollection() + "CPCOTIP.ISTGCO ISTGCO, "
                + _getCollection() + "CPCOTIP.MEICOT MEICOT, " + _getCollection() + "CPCOTIP.ISMCME ISMCME, "
                + _getCollection() + "CPCOTIP.ISMCTR ISMCTR, " + _getCollection() + "CPCOTIP.ISMCSE ISMCSE, "
                + _getCollection() + "CPCOTIP.ISMCAN ISMCAN, " + _getCollection() + "CPCOTIP.ISMTAU ISMTAU, "
                + _getCollection() + "CPCOTIP.ISDDCO ISDDCO, " + _getCollection() + "CPCOTIP.ISDFCO ISDFCO, "
                + _getCollection() + "CPCOTIP.ISTPER ISTPER, " + _getCollection() + "CPCOTIP.ISMCFA ISMCFA, "
                + _getCollection() + "CPCOTIP.ISBZER ISBZER, " + _getCollection() + "CPCOTIP.ISBMIN ISBMIN, ";
        return fields;

    }

    /** (ISBMIN) */

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String calcul = _getCollection() + "CPDOCAP";
        String from = super._getFrom(statement);
        from += " INNER JOIN " + calcul + " ON (" + calcul + ".IAIDEC=" + _getCollection() + "CPDECIP.IAIDEC)";
        return from;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        idCotisation = statement.dbReadNumeric("ISICOT");
        idCotiAffiliation = statement.dbReadNumeric("MEICOT");
        genreCotisation = statement.dbReadNumeric("ISTGCO");
        montantMensuel = statement.dbReadNumeric("ISMCME", 2);
        montantTrimestriel = statement.dbReadNumeric("ISMCTR", 2);
        montantSemestriel = statement.dbReadNumeric("ISMCSE", 2);
        montantAnnuel = statement.dbReadNumeric("ISMCAN", 2);
        taux = statement.dbReadNumeric("ISMTAU", 5);
        debutCotisation = statement.dbReadDateAMJ("ISDDCO");
        finCotisation = statement.dbReadDateAMJ("ISDFCO");
        periodicite = statement.dbReadNumeric("ISTPER");
        montantFacture = statement.dbReadNumeric("ISMCFA", 2);
        aForceAZero = statement.dbReadBoolean("ISBZER");
        cotisationMinimum = statement.dbReadBoolean("ISBMIN");
    }

    public Boolean getAForceAZero() {
        return aForceAZero;
    }

    public Boolean getCotisationMinimum() {
        return cotisationMinimum;
    }

    /**
     * Returns the debutCotisation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDebutCotisation() {
        return debutCotisation;
    }

    /**
     * Returns the finCotisation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinCotisation() {
        return finCotisation;
    }

    public String getGenreCotisation() {
        return genreCotisation;
    }

    public String getIdCotiAffiliation() {
        return idCotiAffiliation;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdCotisation() {
        return idCotisation;
    }

    public String getLibelleGenreCotisation() {
        try {
            return CodeSystem.getLibelle(getSession(), getGenreCotisation());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne le libellé de la périodicité d'une cotisation
     * 
     * @return String
     */
    public String getLibellePeriodicite() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getPeriodicite());
        } catch (Exception e) {
            return "";
        }
    }

    public String getMontantAnnuel() {
        return JANumberFormatter.fmt(montantAnnuel, true, false, false, 2);
    }

    /**
     * @return
     */
    public java.lang.String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantMensuel() {
        return JANumberFormatter.fmt(montantMensuel, true, false, false, 2);
    }

    public String getMontantSemestriel() {
        return JANumberFormatter.fmt(montantSemestriel, true, false, false, 2);
    }

    public String getMontantTrimestriel() {
        return JANumberFormatter.fmt(montantTrimestriel, true, false, false, 2);
    }

    /**
     * Returns the periodicite.
     * 
     * @return java.lang.String
     */
    public java.lang.String getPeriodicite() {
        return periodicite;
    }

    /**
     * Returns the taux.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaux() {
        DecimalFormat myFormatter = new DecimalFormat("###.#####");
        DecimalFormatSymbols symbol = new DecimalFormatSymbols();
        symbol.setDecimalSeparator('.');
        myFormatter.setDecimalFormatSymbols(symbol);
        if (!"".equalsIgnoreCase(taux)) {
            return myFormatter.format(Double.parseDouble(taux));
        } else {
            return "";
        }

        // return JANumberFormatter.fmt(taux.toString(), true, false, true, 3);
    }

    public void setAForceAZero(Boolean forceAZero) {
        aForceAZero = forceAZero;
    }

    public void setCotisationMinimum(Boolean cotisationMinimum) {
        this.cotisationMinimum = cotisationMinimum;
    }

    /**
     * Sets the debutCotisation.
     * 
     * @param debutCotisation
     *            The debutCotisation to set
     */
    public void setDebutCotisation(java.lang.String debutCotisation) {
        this.debutCotisation = debutCotisation;
    }

    /**
     * Sets the finCotisation.
     * 
     * @param finCotisation
     *            The finCotisation to set
     */
    public void setFinCotisation(java.lang.String finCotisation) {
        this.finCotisation = finCotisation;
    }

    public void setGenreCotisation(String newGenreCotisation) {
        genreCotisation = newGenreCotisation;
    }

    public void setIdCotiAffiliation(String newIdCotiAffiliation) {
        idCotiAffiliation = newIdCotiAffiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdCotisation(String newIdCotisation) {
        idCotisation = newIdCotisation;
    }

    public void setMontantAnnuel(String newMontantAnnuel) {
        montantAnnuel = JANumberFormatter.deQuote(newMontantAnnuel);
    }

    /**
     * @param string
     */
    public void setMontantFacture(java.lang.String string) {
        montantFacture = string;
    }

    public void setMontantMensuel(String newMontantMensuel) {
        montantMensuel = JANumberFormatter.deQuote(newMontantMensuel);
    }

    public void setMontantSemestriel(String newMontantSemestriel) {
        montantSemestriel = JANumberFormatter.deQuote(newMontantSemestriel);
    }

    public void setMontantTrimestriel(String newMontantTrimestriel) {
        montantTrimestriel = JANumberFormatter.deQuote(newMontantTrimestriel);
    }

    /**
     * Sets the periodicite.
     * 
     * @param periodicite
     *            The periodicite to set
     */
    public void setPeriodicite(java.lang.String periodicite) {
        this.periodicite = periodicite;
    }

    /**
     * Sets the taux.
     * 
     * @param taux
     *            The taux to set
     */
    public void setTaux(java.lang.String taux) {
        this.taux = JANumberFormatter.deQuote(taux);
    }
}
