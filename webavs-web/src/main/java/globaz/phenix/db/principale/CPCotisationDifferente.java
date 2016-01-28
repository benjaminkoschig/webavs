/**
 * class CPDecisionsInférieureUneAnnee écrit le 19/01/05 par JPA
 * 
 * class entité pour les décisions récentes de moins d'une année
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;

public class CPCotisationDifferente extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FIELD_MADDEB = "MADDEB";
    public final static String FIELD_MADFIN = "MADFIN";
    public final static String FIELD_MALNAF = "MALNAF";
    public final static String FIELD_MBLLCD = "MBLLCD";
    public final static String FIELD_MBLLCF = "MBLLCF";
    public final static String FIELD_MBLLCI = "MBLLCI";
    // Constantes statiques pour les champs
    public final static String FIELD_MBTTYP = "MBTTYP";
    public final static String FIELD_MEMANN = "MEMANN";
    public final static String FIELD_MEMMEN = "MEMMEN";
    public final static String FIELD_MEMSEM = "MEMSEM";
    public final static String FIELD_MEMTRI = "MEMTRI";
    public final static String FIELD_MENANN = "MENANN";
    private String anneeDecisionAF = "";
    private String anneeDecisionCP = "";
    private String dateDebutAffiliation = "";
    private String dateDebutDecision = "";
    private String dateFinAffiliation = "";
    private String dateFinDecision = "";
    private String dateInformation = "";
    private String etat = "";
    private String genreDecision = "";
    private String idAffilie = "";
    private String idDecision = "";
    private String idTiers = "";
    private String montantAnnuelAF = "";
    private String montantAnnuelCP = "";
    private String montantMensuelAF = "";
    private String montantMensuelCP = "";
    private String montantSemestrielAF = "";
    private String montantSemestrielCP = "";
    private String montantTrimestrielAF = "";
    private String montantTrimestrielCP = "";
    private String numAff = "";
    private String numPassage = "";
    private String typeAssurance = "";
    private String typeAssurDE = "";
    private String typeAssurFR = "";
    private String typeAssurIT = "";

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAffilie = statement.dbReadString(CPDecision.FIELD_MAIAFF);
        idTiers = statement.dbReadString(CPDecision.FIELD_HTITIE);
        idDecision = statement.dbReadString(CPDecision.FIELD_IAIDEC);
        genreDecision = statement.dbReadString(CPDecision.FIELD_IATGAF);
        etat = statement.dbReadString(CPDecision.FIELD_IATETA);
        anneeDecisionCP = statement.dbReadString(CPDecision.FIELD_IAANNE);
        anneeDecisionAF = statement.dbReadString(FIELD_MENANN);
        numAff = statement.dbReadString(FIELD_MALNAF);
        montantTrimestrielAF = statement.dbReadString(FIELD_MEMTRI);
        montantTrimestrielCP = statement.dbReadString("ISMCTR");
        montantAnnuelAF = statement.dbReadString(FIELD_MEMANN);
        montantAnnuelCP = statement.dbReadString("ISMCAN");
        montantSemestrielAF = statement.dbReadString(FIELD_MEMSEM);
        montantSemestrielCP = statement.dbReadString("ISMCSE");
        montantMensuelAF = statement.dbReadString(FIELD_MEMMEN);
        montantMensuelCP = statement.dbReadString("ISMCME");
        dateDebutAffiliation = statement.dbReadDateAMJ(FIELD_MADDEB);
        dateFinAffiliation = statement.dbReadDateAMJ(FIELD_MADFIN);
        dateInformation = statement.dbReadDateAMJ(CPDecision.FIELD_IADINF);
        numPassage = statement.dbReadString(CPDecision.FIELD_EBIPAS);
        dateDebutDecision = statement.dbReadDateAMJ(CPDecision.FIELD_IADDEB);
        dateFinDecision = statement.dbReadDateAMJ(CPDecision.FIELD_IADFIN);
        typeAssurance = statement.dbReadString(FIELD_MBTTYP);
        typeAssurFR = statement.dbReadString(FIELD_MBLLCF);
        typeAssurDE = statement.dbReadString(FIELD_MBLLCD);
        typeAssurIT = statement.dbReadString(FIELD_MBLLCI);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAnneeDecisionAF() {
        return anneeDecisionAF;
    }

    public String getAnneeDecisionCP() {
        return anneeDecisionCP;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutDecision() {
        return dateDebutDecision;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinDecision() {
        return dateFinDecision;
    }

    public String getDateInformation() {
        return dateInformation;
    }

    public String getEtat() {
        return etat;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleAssurance() {
        String langue = getSession().getIdLangueISO();
        if (JACalendar.LANGUAGE_DE.equals(langue)) {
            return typeAssurDE;
        } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
            return typeAssurIT;
        }
        return typeAssurFR;
    }

    /**
     * @return
     */
    public String getMontantAnnuelAF() {
        return montantAnnuelAF;
    }

    /**
     * @return
     */
    public String getMontantAnnuelCP() {
        return montantAnnuelCP;
    }

    /**
     * @return
     */
    public String getMontantMensuelAF() {
        return montantMensuelAF;
    }

    /**
     * @return
     */
    public String getMontantMensuelCP() {
        return montantMensuelCP;
    }

    /**
     * @return
     */
    public String getMontantSemestrielAF() {
        return montantSemestrielAF;
    }

    /**
     * @return
     */
    public String getMontantSemestrielCP() {
        return montantSemestrielCP;
    }

    public String getMontantTrimestrielAF() {
        return montantTrimestrielAF;
    }

    public String getMontantTrimestrielCP() {
        return montantTrimestrielCP;
    }

    public String getNumAff() {
        return numAff;
    }

    public String getNumPassage() {
        return numPassage;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public void setAnneeDecisionAF(String string) {
        anneeDecisionAF = string;
    }

    public void setAnneeDecisionCP(String string) {
        anneeDecisionCP = string;
    }

    public void setDateDebutAffiliation(String string) {
        dateDebutAffiliation = string;
    }

    public void setDateDebutDecision(String string) {
        dateDebutDecision = string;
    }

    public void setDateFinAffiliation(String string) {
        dateFinAffiliation = string;
    }

    public void setDateFinDecision(String string) {
        dateFinDecision = string;
    }

    public void setDateInformation(String string) {
        dateInformation = string;
    }

    public void setEtat(String string) {
        etat = string;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    public void setIdDecision(String string) {
        idDecision = string;
    }

    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setMontantAnnuelAF(String string) {
        montantAnnuelAF = string;
    }

    /**
     * @param string
     */
    public void setMontantAnnuelCP(String string) {
        montantAnnuelCP = string;
    }

    /**
     * @param string
     */
    public void setMontantMensuelAF(String string) {
        montantMensuelAF = string;
    }

    /**
     * @param string
     */
    public void setMontantMensuelCP(String string) {
        montantMensuelCP = string;
    }

    /**
     * @param string
     */
    public void setMontantSemestrielAF(String string) {
        montantSemestrielAF = string;
    }

    /**
     * @param string
     */
    public void setMontantSemestrielCP(String string) {
        montantSemestrielCP = string;
    }

    public void setMontantTrimestrielAF(String string) {
        montantTrimestrielAF = string;
    }

    public void setMontantTrimestrielCP(String string) {
        montantTrimestrielCP = string;
    }

    public void setNumAff(String string) {
        numAff = string;
    }

    public void setNumPassage(String string) {
        numPassage = string;
    }

    public void setTypeAssurance(String string) {
        typeAssurance = string;
    }
}
